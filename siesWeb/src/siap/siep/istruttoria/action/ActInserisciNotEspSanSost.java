package siap.siep.istruttoria.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: ActInserisciNotEspSanSost
 * </p>
 * <p>
 * Description: ActInserisciNotEspSanSost
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciNotEspSanSost extends ActionSiap implements ICostantiIstruttoria {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();

		// DF. modifica a seguito segnalazione del cliente (06/05/2008)
		// Le richieste devono comparire sullo stato esecuzione, sulle stampe e
		// tra gli ultimi eventi del dettaglio procedimento, quindi non possono
		// essere di tiop 05 e -
		// lEve.getEvento().setCodTipoEvento("05"); //Tipo Evento = RIchiesta Istruttoria
		// lEve.getEvento().setCodTipoProvvedimento("-");

		lEve.getEvento().setCodTipoEvento("02"); // Tipo Evento = Richiesta
		lEve.getEvento().setCodTipoProvvedimento("26"); // Tipo Provvedimento = Richiesta
		lEve.getEvento().setFlagStampaSiep("S"); // Stampabile (stato esecuzione)
		lEve.getEvento().setFlagVideoSiep("S"); // Visibile

		// Se la data espulsione è disabilitata il codice del motivo è 0565 altrimenti è 0566
		if (!this.isRequestParameterNullObj(DATA_ESPULSIONE_ANNO)) {
			lEve.getEvento().setCodMotivo("0566");
		} else {
			lEve.getEvento().setCodMotivo("0565");
		}

		if (!this.isRequestParameterNullObj(DATA_ESPULSIONE_ANNO)) {
			lEve.getEvento().setDataEspulsioneSanzSost(getRequestDateParameter(DATA_ESPULSIONE_ANNO,
					DATA_ESPULSIONE_MESE, DATA_ESPULSIONE_GIORNO));
		}
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());

		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// Se vengo da IstruttoriaCUMULO
		if (!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				&& getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) != null
				&& !getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
						.equals("")) {
			lEve.getEvento().setIstruidIstruttoriaCumulo(new BigDecimal(
					getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)));
		}

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(lDataEmissione);
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());

		AutoritaEsternaModel lAutEstMod = new AutoritaEsternaModel();

		lAutEstMod.setCodTipoAutorita("95");
		ComuneModel lCom = this.getCodComuneByDescr(
				getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));
		Iterator itx = DecodificheManager.getInstance().getQuesture().iterator();
		String lCode = lCom.getCodComune();
		boolean lIsQuestura = false;
		while (itx.hasNext()) {
			DecodificheModel ldecodeModel = (DecodificheModel) itx.next();
			if ((ldecodeModel.getCode()).equals(lCode)) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("!!!!!!!! TROVATO!!!!!! ");
				lIsQuestura = true;
				break;
			}
		}

		if (!lIsQuestura) {
			// il comune esiste ma non è sede di una Questura
			throw new F3BException(F3BException.USER_MESSAGE,
					"Il comune " + lCom.getDescrizione() + " non risulta essere sede di una Questura");
		}

		lAutEstMod.setCodSede(lCom.getCodComune());

		lAutEstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAutEstMod.setDataInserimento(DateUtils.getSysDate());
		lAutEstMod.setCodUfficioInserimento(lUff.getCodUfficio());
		lNot.setAutoritaEsterna(lAutEstMod);

		if (!this.isRequestParameterNullObj(ICostantiIstruttoria.CAMPO_NOTE)) {
			lNot.setNote(getRequestStringParameter(ICostantiIstruttoria.CAMPO_NOTE));
		}

		lNotifiche[0] = lNot;

		lEve.setNotifiche(lNotifiche);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.istruttoria.action.ActDettaglioNotEspSanSost&" + ICostantiEvento.CAMPO_ID_EVENTO
				+ "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}
}