package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.web.ISIAPCostantiWeb;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioComunicazioneL78del2013
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio Comunicazione Legge 78 del 2013
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActDettaglioComunicazioneL78del2013 extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	public String processRequest() throws F3BException {

		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// se vengo da ricerca Trasmessi L78/2013
		String torna = "";

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			torna = getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
		}

		setRequestAttribute("tornasuelenco", torna);

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lId,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Il Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		NotificaModel[] lNotifica = lEveMod.getNotifiche();
		for (int i = 0; i < lNotifica.length; i++) {
			if (lNotifica[i].getCodTipoNotifica().equals("C")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
			}
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		if (lNotifica != null) {
			for (int i = 0; i < lNotifica.length; i++) {
				if (lNotifica[i].getUfficio() != null
						&& lNotifica[i].getUfficio().getCodTipoUfficio().equals("UDSM")) {
					lNotifica[i].getUfficio()
							.setDescrTipoUfficio("Magistrato di Sorveglianza per i minorenni");
				}
			}
		}

		PenaResiduaModel llPenMod = this.getPenaResidua(lId, lEveMod.getEvento().getFasSieIdFascicoloSiep());
		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
			// lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute("eventonotifica", lEveMod);

		// Passaggio dell'eventuale istituto detenzione relativo alla prima notifica
		if (lNotifica != null && lNotifica.length > 0) {
			NotificaModel lPrimaNotifica = new NotificaModel();
			lPrimaNotifica = lNotifica[0];

			if (lPrimaNotifica.getIstDetIdIstitutoDetenzione() != null) {
				IstitutoDetenzioneModel lIstDetenzione = new IstitutoDetenzioneModel();
				IIstitutoDetenzione ctrld = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				lIstDetenzione = ctrld
						.ExRicercaIstitutoDetenzioneByKey(lPrimaNotifica.getIstDetIdIstitutoDetenzione());
				setRequestAttribute("istitutodetenzione", lIstDetenzione);
			}
		}

		return PG_DETTAGLIO_COMUNICAZIONE_LEGGE_78_2013;
	}

}