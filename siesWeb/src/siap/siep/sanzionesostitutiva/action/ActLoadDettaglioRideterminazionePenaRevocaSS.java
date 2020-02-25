package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioRideterminazionePenaRevocaSS
 * </p>
 * <p>
 * Description: Classe Action per la Load del Dettaglio Rideterminazione Pena per revoca/conversione Sanzione
 * Sostitutiva
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

public class ActLoadDettaglioRideterminazionePenaRevocaSS extends ActSIESDettaglioProvvedimento
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
		// Recupero l'id dell'evento passato sulla request (OE)
		// ==========================================================================
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		EventoNotificaModel lEvNotModel = null;
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		lEvNotModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEvNotModel = " + lEvNotModel);

		// ===============================================
		// Carico la pena Residua (collegata all'evento)
		// ===============================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico la pena Residua");
		PenaResiduaModel lPenResMod = null;
		IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
		lPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdEvento(lIdEvento);

		// ==========================================================================
		// Carico la Pena Precedente (quella legata al Provvedimento )
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Carico la pena Prima della Conversione/Revoca");
		PenaResiduaModel lPenPrecResMod = null;

		lPenPrecResMod = lCtrlPenaResidua
				.ExRicercaPenaResiduaByIdEvento(lEvNotModel.getEvento().getEveIdEvento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lPenPrecResMod = " + lPenPrecResMod);
		// Vector lElencoPenResMod = lCtrlPenaResidua.ExRicercaPenaResiduaByIdFascicolo(lIdFascicolo);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lElencoPenResMod.size() = "+lElencoPenResMod.size());
		// if (lElencoPenResMod!=null && lElencoPenResMod.size()>0){
		// for (int i=0; i<lElencoPenResMod.size();i++) {
		// lPenPrecResMod = (PenaResiduaModel) lElencoPenResMod.elementAt(i);
		// if ( lPenPrecResMod.getEveIdEvento()==lEvNotModel.getEvento().getIdEvento() // Pena Rideterminata
		// || lPenPrecResMod.getEveIdEvento()==lEvNotModel.getEvento().getEveIdEvento() // Pena legata
		// all'annotazione
		// )
		// {
		// // Non faccio nulla
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Non faccio nulla = "+lPenPrecResMod.getIdPenaResidua());
		// }
		// else {
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lPenPrecResMod = "+lPenPrecResMod.getIdPenaResidua());
		// break;
		// }
		// }
		// }

		// ==========================================================================
		// Recupero la SS Convertita (su annotazione manuale legata all'annotazione)
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Ricerco annotazioni di revoca ");
		IAnnotazioneManuale lAnnManCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lListaAnnotazioni = null;
		lListaAnnotazioni = lAnnManCtrl
				.ExRicercaAnnotazioneManualeByIdEvento(lEvNotModel.getEvento().getEveIdEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaAnnotazioni.size() = " + lListaAnnotazioni.size());

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
		setRequestAttribute("aNuovaPenaResidua", lPenResMod);
		setRequestAttribute("aPenaResiduaPrecedente", lPenPrecResMod);
		setRequestAttribute("aListaAnnotazioni", lListaAnnotazioni);

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

		return PG_LOAD_DETTAGLIO_RIDETPENA_REVOCA_SS;
	}
}