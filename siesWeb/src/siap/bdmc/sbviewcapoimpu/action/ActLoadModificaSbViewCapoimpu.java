package siap.bdmc.sbviewcapoimpu.action;

/**
 * <p>Title: ActLoadModificaSbViewCapoimpu</p>
 * <p>Description: Classe Action per la load modifica di SbViewCapoimpu</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
//import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class ActLoadModificaSbViewCapoimpu extends ActionSiap implements ICostantiSbViewCapoimpu {

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
		BigDecimal lIdPren = getRequestBigDecimalParameter(CAMPO_ID_PREN);
		BigDecimal lNumeProgCapoImpu = getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		logger.info("Recupero i dati del record da modificare: " + lIdPren + "; " + lNumeProgCapoImpu);
		ISbViewCapoimpu lCtrl = BDMCLookupRemote.getSbViewCapoimpuRemote();
		SbViewCapoimpuModel lSbViewCapoimpu = lCtrl.ExRicercaSbViewCapoimpuById(lIdPren, lNumeProgCapoImpu);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lSbViewCapoimpu == null) {
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
		logger.debug("Dati Record: " + lSbViewCapoimpu);
		setRequestAttribute("sbviewcapoimpu", lSbViewCapoimpu);

		// Inserire Eventuali ComboBOX precaricando i dati del model
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		logger.debug("return: " + PG_LOAD_INSERISCISBVIEWCAPOIMPU);
		return PG_LOAD_INSERISCISBVIEWCAPOIMPU;
	}

}