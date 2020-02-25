package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioComunicazioneNuovoResiduoPena
 * </p>
 * <p>
 * Description: Classe Action per la Load del Dettaglio della Comunicazione Nuovo Residuo Pena.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @since 3.0
 * @version 1.0
 */

public class ActLoadDettaglioComunicazioneNuovoResiduoPena extends ActSIESDettaglioProvvedimento
		implements ICostantiSanzioneSostitutiva {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero l'id dell'evento passato sulla request
		// ==========================================================================
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEvNotModel = null;
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lEvNotModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEvNotModel = " + lEvNotModel);

		// =========================================
		// Carico la pena Residua
		// =========================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico la pena Residua");
		PenaResiduaModel lPenResMod = null;
		IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
		lPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		// ====================================================
		// Carico la SAnzione Sostitutiva Collegata alla pena
		// ====================================================
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		// SanzioneSostResiduaModel lSSResiduaModel =
		// lSSCtrl.getSSByIdPenaResidua(lPenResMod.getIdPenaResidua());
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lIdFascicolo, null);
		lPenResMod.setSanzSostResidua(lSSResiduaModel);

		// ========================================================
		// Recupero le Misure Cautelari In Sentenza (computabili)
		// ========================================================
		MisuraCautelareModel lMisCau = new MisuraCautelareModel();
		lMisCau.setFasSieIdFascicoloSiep(lIdFascicolo);
		lMisCau.setFlagComputabile("S");

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();

		Vector lMisureComputabili = null;
		try {
			lMisureComputabili = lCtrl.ExRicercaMisuraCautelare(lMisCau);
		} catch (F3BException e) {
			// e.printStackTrace();
		}

		CalendarModel lTotMCinCarcere = new CalendarModel();
		CalendarModel lTotMCinArrestiDom = new CalendarModel();
		CalendarModel lTotMC = new CalendarModel();

		CalendarUtil lCalendarUtil = new CalendarUtil();

		if (lMisureComputabili != null) {
			for (int i = 0; i < lMisureComputabili.size(); i++) {
				MisuraCautelareModel misMod = (MisuraCautelareModel) lMisureComputabili.elementAt(i);

				if (misMod.getCodTipoMisura() != null && misMod.getCodTipoMisura().equals("AD") // Arresti
																								// Domiciliari
				) {
					lTotMCinArrestiDom = lCalendarUtil.sommaGiorni(lTotMCinArrestiDom, misMod.getQuantum());
				} else if (misMod.getCodTipoMisura() != null && misMod.getCodTipoMisura().equals("CA") // Custodia
																										// cautelare
																										// in
																										// carcere
				) {
					lTotMCinCarcere = lCalendarUtil.sommaGiorni(lTotMCinCarcere, misMod.getQuantum());
				}
			}
		}

		lTotMC = lCalendarUtil.sommaGiorni(lTotMCinCarcere, lTotMCinArrestiDom);

		setRequestAttribute("aTotMCinCarcere", lTotMCinCarcere);
		setRequestAttribute("aTotMCinArrestiDom", lTotMCinArrestiDom);
		setRequestAttribute("aTotMC", lTotMC);

		// ============================================
		// Ricerca Magistrato firmatario
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEvNotModel.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ==========================================================
		// Passo i dati alla form di visualizzazione del dettaglio
		// ==========================================================
		setRequestAttribute("aEveNotComunicazione", lEvNotModel);
		setRequestAttribute("aPenaResidua", lPenResMod);

		// ==========================================
		// Recupero la posizione giuridica (quale?)
		// ==========================================
		if (lEvNotModel.getEvento().getFlagDocumentoRegistrato() == null
				|| lEvNotModel.getEvento().getFlagDocumentoRegistrato().equals("N")) { // n.b. recupero la
																						// posizione corrente
																						// solo se sto
																						// inserendo il
																						// provvedimento
																						// mentre se sto
																						// visualizzando un
																						// provvedimento già
																						// validato non
																						// ha senso recuperare
																						// la posizione
																						// corrente.
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
			setRequestAttribute("aPosizioneluogoaltra", lPosLuoAltr);
		}

		return PG_LOAD_DETTAGLIO_COM_NUOVO_RES_PENA;
	}
}