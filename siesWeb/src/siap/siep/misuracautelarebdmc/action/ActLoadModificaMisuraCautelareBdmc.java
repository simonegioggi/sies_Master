package siap.siep.misuracautelarebdmc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
//import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadModificaMisuraCautelareBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di MisuraCautelareBdmc
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
public class ActLoadModificaMisuraCautelareBdmc extends ActionSiap implements ICostantiMisuraCautelareBdmc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger logger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche di precaricare tutti i dati da
	 * visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da modificare
		// ==========================================
		BigDecimal lIdMisuraCautelare = getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		logger.info("Recupero i dati del record da modificare: " + lIdMisuraCautelare);
		IMisuraCautelareBdmc lCtrl = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		MisuraCautelareBdmcModel lMisuraCautelareBdmc = lCtrl
				.ExRicercaMisuraCautelareBdmcById(lIdMisuraCautelare);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lMisuraCautelareBdmc == null) {
			logger.error("Record non trovato!!");
			setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);
			return ISIAPCostantiWeb.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		logger.debug("Dati Record: " + lMisuraCautelareBdmc);
		setRequestAttribute("misuracautelarebdmc", lMisuraCautelareBdmc);

		// Inserire Eventuali ComboBOX precaricando i dati del model
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		logger.debug("return: " + PG_LOAD_INSERISCIMISURACAUTELAREBDMC);
		return PG_LOAD_INSERISCIMISURACAUTELAREBDMC;
	}

}