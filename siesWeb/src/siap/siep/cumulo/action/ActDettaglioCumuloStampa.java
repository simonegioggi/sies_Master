package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioCumuloStampa
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio
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
@SuppressWarnings("rawtypes")
public class ActDettaglioCumuloStampa extends ActSIESDettaglioProvvedimento implements ICostantiCumulo {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Controllo Esistenza pena residua validata per quel fascicolo
		/*
		 * REWORK DETTAGLIO PenaResiduaModel lPenaResMod = new PenaResiduaModel(); IPenaResidua lPenResCtrl =
		 * SIEPLookupRemote.getPenaResiduaRemote(); lPenaResMod =
		 * lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());
		 */

		PenaResiduaModel lPenaResMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", lPenaResMod);

		// ricerca posizione giuridica
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		 */

		PosizioneGiuridicaModel lPos = this.getPosizioneGiuridica(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("posizione", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		this.setRequestAttribute("eventonotifica", lEveMod);

		// ricerca cumulo
		// flag proveniente dalla validazine per cambiare la ricerca del cumulo
		String lFlagValidato = null;
		// if(!this.isRequestParameterNullObj("lFlagValidato"))
		// {
		// lFlagValidato = this.getRequestStringParameter("lFlagValidato");
		// }

		if (lEveMod != null && lEveMod.getEvento() != null) {
			lFlagValidato = lEveMod.getEvento().getFlagDocumentoRegistrato();
		}

		ICumulo lCtrlCumulo = SIEPLookupRemote.getCumuloRemote();
		Vector lVectMod = new Vector();
		CumuloModel lCumMod = new CumuloModel();
		if (lFlagValidato == null || lFlagValidato.equals("N")) {// non validati
			lVectMod = lCtrlCumulo
					.ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(lFascMod.getIdFascicoloSiep());
		} else if (lFlagValidato != null) {// validati
			lVectMod = lCtrlCumulo.ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(
					lFascMod.getIdFascicoloSiep());
		}

		if (lVectMod.size() > 0) {
			lCumMod = (CumuloModel) lVectMod.get(0);
		}

		setRequestAttribute("cumulo", lCumMod);

		// ricerca magistrato competente
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistratocompetente", lMagi);

		this.setRequestAttribute("tipo", lCumMod.getFlagTipoStampa());

		// ========================================================================
		// Vengono recuperati i GG di LA concessi (presi in carico e associati a
		// un evento SIES VALIDATI) già detratti o da detrarre
		// Nuova gestione LA (12/2006)
		// ========================================================================
		/*
		 * CalcoloPenaControllerF5 lCtrlF5 = new CalcoloPenaControllerF5(); CalcoloPenaModel lCalcPenaModel =
		 * lCtrlF5.exGetPenaIniziale(lFascMod.getIdFascicoloSiep(), null); Vector lListaLA =
		 * lCtrlF5.exGetLiberazioneAnticipata(lFascMod.getIdFascicoloSiep(),lCalcPenaModel.getDataDal(),null);
		 * lCalcPenaModel.setLibAnticipate(lListaLA);
		 * 
		 * int totGiorniLAConcessi = lCalcPenaModel.getLiberazioneAnticipataGiaConcesse();
		 * this.setRequestAttribute("giorniLA",""+ totGiorniLAConcessi);
		 */
		// Recupero il record PENA_CUMULO per le LA
		IPenaCumulo lPenaCumCtrl = SIEPLookupRemote.getPenaCumuloRemote();
		PenaCumuloModel lPenaCumMod = null;
		if (lCumMod != null && lCumMod.getIdCumulo() != null) {
			lPenaCumMod = lPenaCumCtrl.ExRicercaPenaCumuloByIdCumulo(lCumMod.getIdCumulo());
			// if (lPenaCumMod!=null & lPenaCumMod.getIdPenaCumulo()!=null)
			// this.setRequestAttribute("giorniLA",""+ lPenaCumMod.getNumGiorniLibAnticipata());
		}

		// 20/05/2014 - Nuova L.A. - decreto 2013/146 - Distinzione tra L.A., L.A. SPECIALE e INTEGRAZIONE
		// L.A.
		this.setRequestAttribute("penaCum", lPenaCumMod);

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("FC")) {
				setRequestAttribute("fogliocomplementare", "1");
				break; // se lo trova esce, altrimenti potrebbe "sporcare" l'attributo nel successivo ciclo
						// del for
			} else {
				setRequestAttribute("fogliocomplementare", "0"); // se non lo trova continua a cercare nel
																	// successivo ciclo del for
			}
		}

		// Ricerca del Foglio Complementare
		IDocumentoAllegato lDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		DocumentoAllegatoModel lDocAll = null;
		lDocAll = lDocAllCtrl.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lIdEvento, "06");
		setRequestAttribute("documentoAllegato", lDocAll);

		return PG_LOAD_DETTALIO_STAMPA_CUMULO;
	}

}