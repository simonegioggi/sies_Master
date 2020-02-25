package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioAnnotazioneEspulsione
 * </p>
 * <p>
 * Description: Classe Action per la Load del Dettaglio Avvenuta Espulsione. Invocata sia per la
 * visualizzazione del verbale, sia per la visualizzazione della comunicazione. I due eventi vengono inseriti
 * e validati contestualmente.
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
public class ActLoadDettaglioAnnotazioneEspulsione extends ActSIESDettaglioProvvedimento implements
		ICostantiSanzioneSostitutiva {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero l'id dell'evento passato sulla request che può essere il Verbale
		// o la Comunicazione
		// ==========================================================================
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoModel lEvRicercaModel = null;
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lEvRicercaModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEvRicercaModel = " + lEvRicercaModel);

		VerbaleModel lVerbaleModel = null;
		EventoModel lEvVerbModel = null;
		EventoNotificaModel lEvNotComunicazioneModel = null;

		if (lEvRicercaModel.getCodTipoEvento().equals("07")
				&& lEvRicercaModel.getCodTipoProvvedimento().equals("27")
				&& lEvRicercaModel.getCodMotivo().equals("0926")) {
			// E' stato richiesto il dettaglio del Verbale
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("E' stato richiesto il dettaglio del Verbale");
			lEvVerbModel = lEvRicercaModel;

			// Recupero il verbale
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero il verbale");
			IVerbale lVerbaleCtrl = SIEPLookupRemote.getVerbaleRemote();
			lVerbaleModel = lVerbaleCtrl.ExRicercaVerbaleByIdEvento(lEvVerbModel.getIdEvento());

			// Recupero la comunicazione che punta il verbale (e la/le notifiche)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la comunicazione che punta il verbale (e la/le notifiche)");
			lEvNotComunicazioneModel = lCtrlEvento.ExRicercaEventoNotificaByEveIdEvento(lEvVerbModel
					.getIdEvento());
		} else if (lEvRicercaModel.getCodTipoEvento().equals("01")
				&& (lEvRicercaModel.getCodTipoProvvedimento().equals("12") || lEvRicercaModel
						.getCodTipoProvvedimento().equals("25"))
				&& lEvRicercaModel.getCodMotivo().equals("0927")) {
			// E' stato richiesto il dettaglio della Comunicazione
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("E' stato richiesto il dettaglio della Comunicazione");

			// Recupero la comunicazione che punta il verbale (e la/le notifiche)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero la comunicazione che punta il verbale (e la/le notifiche)");
			lEvNotComunicazioneModel = lCtrlEvento
					.ExRicercaEventoNotificaByKey(lEvRicercaModel.getIdEvento());

			// Recupero l'evento verbale (puntato dalla comunicazione)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero l'evento verbale (puntato dalla comunicazione): "
					+ lEvNotComunicazioneModel.getEvento().getEveIdEvento());
			lEvVerbModel = lCtrlEvento.ExRicercaEventoByKey(lEvNotComunicazioneModel.getEvento()
					.getEveIdEvento());

			// Recupero il verbale
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Recupero il verbale");
			IVerbale lVerbaleCtrl = SIEPLookupRemote.getVerbaleRemote();
			lVerbaleModel = lVerbaleCtrl.ExRicercaVerbaleByIdEvento(lEvVerbModel.getIdEvento());
		}

		// =========================================
		// Carico la pena Residua e la Sospensione
		// =========================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico la pena Residua");
		PenaResiduaModel lPenResMod = null;
		IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
		lPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdEvento(lEvNotComunicazioneModel.getEvento()
				.getIdEvento());

		// =========================================================
		// Recupero i dati della Sospensione (pena espiata)
		// =========================================================
		SospensioneModel lSospModel = null;
		if (lPenResMod != null && lPenResMod.getIdPenaResidua() != null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Carico la Sospensione");
			ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
			lSospModel = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(lPenResMod.getIdPenaResidua());
		}

		// ============================================
		// Ricerca Magistrato firmatario
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEvNotComunicazioneModel.getEvento()
				.getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ==========================================================
		// Passo i dati alla form di visualizzazione del dettaglio
		// ==========================================================
		setRequestAttribute("aEveVerbale", lEvVerbModel);
		setRequestAttribute("aVerbale", lVerbaleModel);
		setRequestAttribute("aEveNotComunicazione", lEvNotComunicazioneModel);
		setRequestAttribute("aPenaResidua", lPenResMod);
		setRequestAttribute("aSospensione", lSospModel);

		// ==========================================
		// Recupero la posizione giuridica (quale?)
		// ==========================================
		if (lEvNotComunicazioneModel.getEvento().getFlagDocumentoRegistrato() == null
				|| lEvNotComunicazioneModel.getEvento().getFlagDocumentoRegistrato().equals("N")) { // n.b.
																									// recupero
																									// la
																									// posizione
																									// corrente
																									// solo se
																									// sto
																									// inserendo
																									// il
																									// provvedimento
																									// mentre
																									// se sto
																									// visualizzando
																									// un
																									// provvedimento
																									// già
																									// validato
																									// non
																									// ha
																									// senso
																									// recuperare
																									// la
																									// posizione
																									// corrente.
			IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltr = lPosCtrl
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lIdFascicolo);
			setRequestAttribute("aPosizioneluogoaltra", lPosLuoAltr);
		}

		return PG_LOAD_DETTAGLIO_ANNOTAZIONE_ESPULSIONE;
	}

}