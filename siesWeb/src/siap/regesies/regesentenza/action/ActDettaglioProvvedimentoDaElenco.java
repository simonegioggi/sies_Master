package siap.regesies.regesentenza.action;

//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.siep.sentenza.controller.SentenzaController;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.regesies.regesentenza.controller.IRegeSentenza;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioProvvedimento
 * </p>
 * <p>
 * Description: Dettaglio Provvedimento proveniente da ReGe
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioProvvedimentoDaElenco extends ActionSiap implements ICostantiRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		String lPage = "";

		// Parse della request
		RegeSentenzaModel lSenMod = new RegeSentenzaModel();

		int lProgr = getRequestIntParameter(CAMPO_PROGR_PROVVEDIMENTO);

		lSenMod.setCodTipoAutoritaEmittente(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE + lProgr));
		lSenMod.setCodLuogoEmittente(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE + lProgr));
		lSenMod.setDescrTipoAutoritaEmittente(
				getRequestStringParameter(CAMPO_DESCR_TIPO_AUTORITA_EMITTENTE + lProgr));
		lSenMod.setDescrLuogoEmittente(getRequestStringParameter(CAMPO_DESCR_LUOGO_EMITTENTE + lProgr));

		lSenMod.setAnnoSentenza(getRequestIntParameter(CAMPO_ANNO_SENTENZA + lProgr));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA + lProgr));
		lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO + lProgr,
				CAMPO_MESE_DATA_PROVVEDIMENTO + lProgr, CAMPO_GIORNO_DATA_PROVVEDIMENTO + lProgr));
		lSenMod.setDescrTipoProvvedimento(getRequestStringParameter(CAMPO_DESCR_TIPO_PROVVEDIMENTO + lProgr));

		int lSoggetti = getRequestIntParameter(CAMPO_COUNT_SOGGETTI + lProgr);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("*** SENTENZA REGE - " + lSenMod);

		if (lSoggetti == 1) {
			IRegeSentenza lCtrl = RegeSiesLookupRemote.getRegeSentenzaRemote();
			// Ricerca il Provvedimento
			ProvvedimentoModel lProvv = lCtrl.ExRicercaRegeSentenza(lSenMod);
			// Ricerca soggetti per lo stesso provvedimento SIEP
			setRequestAttribute("provvedimento", lProvv);
			setSessionAttribute("provvedimentoRege", lProvv);
			lPage = PG_LOAD_DETTAGLIO_PROVVEDIMENTO;
		} else { // Ci sono più soggetti associati allo stesso provvedimento
					// Si deve rimandare la maschera di elenco Soggetti
			IRegeSoggetto lCtrlSogg = RegeSiesLookupRemote.getRegeSoggettoRemote();

			Vector lVect = lCtrlSogg.ExRicercaRegeSoggettoPerProvvedimento(lSenMod);
			if (lVect != null && lVect.size() != 0) {
				setRequestAttribute("provvedimento", lSenMod);
				setSessionAttribute("provvedimentoRege", lSenMod);

				setRequestAttribute("soggetti", lVect);

				// Ricerco i Soggetti associati
				lPage = PG_ELENCO_SOGGETTI;
			}
		}

		return lPage;
	}

}