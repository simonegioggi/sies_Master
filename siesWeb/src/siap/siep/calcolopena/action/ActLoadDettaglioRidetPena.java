package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioRidetPena
 * </p>
 * <p>
 * Description: Classe action per la Load del Dettaglio dopo l'inserimento del Provvedimento o Comunicazione
 * per la Rideterminazione Pena - Decisioni del PM - Altro
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */

public class ActLoadDettaglioRidetPena extends ActSIESDettaglioProvvedimento
		implements ICostantiEvento, ICostantiAnnotazioneManuale {
	/**
	 * Recupera i dati dell'evento inserito e li passa alla jsp di visualizzazione del dettaglio. - Evento -
	 * Notifiche - Annotazione Manuale - Pena Residua (rideterminata associata all'evento) - Posizione
	 * Giuridica
	 *
	 * @return jsp di visualizzazione del detteglio
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ============================================
		// ricerca eventonotifica inserito
		// ============================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();
		lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		this.setRequestAttribute("eventonotifica", lEveNotMod);

		// ============================================
		// ricerca annotazioni manuali
		// ============================================
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lVecAnnMod = new Vector();
		lVecAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lIdEvento);

		this.setRequestAttribute("annotazioni", lVecAnnMod);

		// ============================================
		// ricerca posizione giuridica
		// ============================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ============================================
		// ricerca la pena residua
		// ============================================
		IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		setRequestAttribute("penaresidua", lPenRes);

		// ============================================
		// Ricerca Magistrato
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ============================================
		// Gestione Foglio Complementare
		// ============================================
		for (int i = 0; i < lEveNotMod.getNotifiche().length; i++) {
			if (lEveNotMod.getNotifiche()[i].getCodTipoNotifica().equals("FC")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
			}
		}

		// ========================================================
		// Restituisce la pagina di visualizzazione del Dettaglio
		// ========================================================
		return PG_LOAD_DETTAGLIO_RIDETERMINAZIONE_PENA_ALTRO;
	}
}