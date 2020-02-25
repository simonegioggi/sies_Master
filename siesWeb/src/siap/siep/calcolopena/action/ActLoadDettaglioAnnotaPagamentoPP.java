package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioAnnotaPagamentoPP
 * </p>
 * <p>
 * Description: Azione nella Converisione delle Pene Pecuniarie
 * </p>
 * <p>
 * MicroFunzione: Dettaglio dell'Annotazione avvenuto pagamento Pene Pecuniarie
 * </p>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @since 4.0
 */

public class ActLoadDettaglioAnnotaPagamentoPP extends ActSIESDettaglioProvvedimento
		implements ICostantiEvento, ICostantiAnnotazioneManuale {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// id dell'evento di computo
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ============================================
		// Ricerca evento di computo inserito
		// ============================================
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEveComputo = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

		this.setRequestAttribute("aEventoComputo", lEveComputo);

		// ===============================================
		// Ricerco eventuale provvedimeto altra autorità
		// ===============================================
		if (lEveComputo.getEveIdEvento() != null) {
			EventoModel lEveAltraAut = lCtrlEvento.ExRicercaEventoByKey(lEveComputo.getEveIdEvento());
			this.setRequestAttribute("aEventoAltraAut", lEveAltraAut);
		}

		// ============================================
		// Recupero il campo nota
		// ============================================
		ICampoNota lCtrlCampoNota = SICOLookupRemote.getCampoNotaRemote();
		Vector lVecCNota = new Vector();
		lVecCNota = lCtrlCampoNota.ExRicercaVectCampoNotaByIdEvento(lEveComputo.getIdEvento());

		this.setRequestAttribute("aListaCampoNota", lVecCNota);

		// CampoNotaModel lCampoNotaModel =
		// lCtrlCampoNota.ExRicercaCampoNotaByIdEvento(lEveComputo.getIdEvento());
		// this.setRequestAttribute("aCampoNota",lCampoNotaModel);

		// ============================================
		// ricerca annotazioni manuali
		// ============================================
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lVecAnnMod = new Vector();
		try {
			lVecAnnMod = lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lIdEvento);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}

		this.setRequestAttribute("annotazioni", lVecAnnMod);

		// ===================================================================
		// Ricerca LA (nel caso di ridimensionamento LA o scomputo permesso)
		// ===================================================================
		ILicenzaPeriodiLibAnticipata lLicCtrl = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		Vector lVectLic = new Vector();
		try {
			lVectLic = lLicCtrl.ExRicercaLicenzeByEve(lIdEvento);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE)
				throw e;
		}
		this.setRequestAttribute("licenze", lVectLic);

		// ============================================
		// ricerca posizione giuridica
		// ============================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==============================================
		// Ricerca la pena residua collegata al computo
		// ==============================================
		IPenaResidua lCtrlPenaRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenRes = lCtrlPenaRes.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		setRequestAttribute("penaresidua", lPenRes);

		// ==============================================
		// Ricerca l'eventuale fungibilità
		// ==============================================
		IFungibilita lFungCtrl = SIEPLookupRemote.getFungibilitaRemote();
		FungibilitaModel lFunMod = lFungCtrl.ExRicercaFungibilitaByKeyEvento(lIdEvento);

		setRequestAttribute("fungibilita", lFunMod);

		// ============================================
		// Ricerca Magistrato
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveComputo.getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ========================================================
		// Restituisce la pagina di visualizzazione del Dettaglio
		// ========================================================
		return PG_LOAD_DETTAGLIO_ANNOTA_PAGAMENTO_PP;
	}
}