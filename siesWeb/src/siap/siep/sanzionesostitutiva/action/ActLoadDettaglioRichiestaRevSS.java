package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaRevSS
 * </p>
 * <p>
 * Description: Classe Action per la Load del Dettaglio Richiesta Revoca SS al GE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @since 3.0
 * @version 1.0
 */
public class ActLoadDettaglioRichiestaRevSS extends ActSIESDettaglioProvvedimento
		implements ICostantiSanzioneSostitutiva {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero l'id dell'evento passato sulla request
		// ==========================================================================
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEvNotModel = null;
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lEvNotModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		// =========================================
		// Carico la pena Residua
		// =========================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico la pena Residua");
		PenaResiduaModel lPenResMod = null;
		IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
		lPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		// ricerco le sanzione sostitutive
		ISanzioneSostitutiva lSSCtrl = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		SanzioneSostResiduaModel lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(),
				"S");

		// Inserisco la SS residua nel model della PR
		if (lSSResiduaModel == null || lSSResiduaModel.getIdSanzioneSostResidua() == null) {
			lSSResiduaModel = lSSCtrl.getUltimaSSResidua(lFascMod.getIdFascicoloSiep(), "N");
		}

		lPenResMod.setSanzSostResidua(lSSResiduaModel);

		// ============================================
		// Ricerca Magistrato firmatario
		// ============================================
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEvNotModel.getEvento().getCodMagistrato());

		setRequestAttribute("magistrato", lMagi);

		// ==========================================================
		// Passo i dati alla form di visualizzazione del dettaglio
		// ==========================================================
		setRequestAttribute("aEveNotRichiesta", lEvNotModel);
		setRequestAttribute("penaresidua", lPenResMod);

		// Posizione giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("aPosizioneluogoaltra", lPos);
		return PG_DETTAGLIO_RICHIESTA_REVOCA;
	}
}