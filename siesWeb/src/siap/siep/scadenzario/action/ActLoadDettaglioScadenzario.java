package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadDettaglioScadenzario
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioScadenzario extends ActionSiap implements ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdScadenzario = null;
		BigDecimal lIdFascicoloSiep = null;
		FascicoloSiepModel lFascMod = null;

		if (!isRequestParameterNullObj(CAMPO_ID_SCADENZARIO)) {
			lIdScadenzario = getRequestBigDecimalParameter(CAMPO_ID_SCADENZARIO);
		} else {
			if (this.isSessionAttributeNullObj("fascicolo"))
				return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

			lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			if (isFascicoloNonValidato())
				return IWebConstants.PG_MESSAGE;

			if (isFascicoloArchiviatoDefinito())
				return IWebConstants.PG_MESSAGE;

			lIdFascicoloSiep = lFascMod.getIdFascicoloSiep();
		}

		IScadenzario lCtrlScad = SIEPLookupRemote.getScadenzarioRemote();

		ScadenzarioModel llScaMod = null;
		// Se l'lIdScadenzario è null lo recupera attraverso l'lIdFascicoloSiep in sessione
		if (lIdScadenzario == null) {
			llScaMod = lCtrlScad.ExScadenzarioCorrenteByIdFascicoloTipoScadenzario(lIdFascicoloSiep, "01");
		} else {
			llScaMod = lCtrlScad.ExRicercaScadenzarioByKey(lIdScadenzario);

			// Cerca il fascicolo relativo allo scadenzario siep
			IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();

			lFascMod = lCtrlFas.ExRicercaFascicoloByKey(llScaMod.getFasSieIdFascicoloSiep());
		}

		if (llScaMod == null) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun elemento trovato.");
			ScadenzarioModel llScaMod2 = new ScadenzarioModel();
			llScaMod2.setFascicoloModel(lFascMod);
			setRequestAttribute("scadenzario", llScaMod2);
		} else {
			lIdFascicoloSiep = llScaMod.getFasSieIdFascicoloSiep();
			// Aggiorna lo scadenzario come visto
			llScaMod.setFascicoloModel(lFascMod);
			llScaMod.setFlagVisto("S");
			llScaMod.setDataVisto(DateUtils.getSysDate());
			lCtrlScad.ExModificaScadenzario(llScaMod);
			setRequestAttribute("scadenzario", llScaMod);
		}

		// Ricerca l'Ordine di Esecuzione Simeone
		EventoNotificaModel lEveNot = new EventoNotificaModel();
		IEventoSimeone lCtrlEveSime = SICOLookupRemote.getEventoSimeoneRemote();
		lEveNot = lCtrlEveSime.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lIdFascicoloSiep, "LS");

		if (lEveNot == null || lEveNot.getEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");

		if (lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");

		setRequestAttribute("eventonotifica", lEveNot);

		// Ricerca un decreto di Irreperibilità (l'ultimo validato inserito per data inserimento)
		EventoNotificaModel lEveNotDecIrr = lCtrlEveSime.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(
				lIdFascicoloSiep, "8BIS");

		setRequestAttribute("decretoIrreperibilita", lEveNotDecIrr);

		// Cerca la Notifica al condannato dell LS
		NotificaModel lNotCondannato = null;
		for (int i = 0; i < lEveNot.getNotifiche().length; i++) {
			if ("E".equals(lEveNot.getNotifiche()[i].getCodTipoNotifica())) {
				lNotCondannato = lEveNot.getNotifiche()[i];

				break;
			}
		}

		// Se esistono dei "solleciti/rinnovi" di qualunque tipo li presenta nel dettaglio
		if (lNotCondannato != null && lNotCondannato.getIdNotifica() != null) {
			IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
			List lRinnovo = lCtrl.ExRicercaRinnovoIdNotifica(lNotCondannato.getIdNotifica());

			if (lRinnovo != null && !lRinnovo.isEmpty()) {
				RinnovoModel lRinMod = (RinnovoModel) lRinnovo.get(0);

				setRequestAttribute("rinnovo", lRinMod);
			}
		}

		return PG_LOAD_DETTAGLIOSCADENZARIO;
	}

}