package siap.bdmc.sbviewnotifiche.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
//import siap.bdmc.sbviewnotifiche.controller.ISbViewNotifiche;
import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadModificaSbViewNotifiche
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di SbViewNotifiche
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
public class ActLoadModificaSbViewNotifiche extends ActionSiap implements ICostantiSbViewNotifiche {

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
		BigDecimal lProgNoti = getRequestBigDecimalParameter(CAMPO_PROG_NOTI);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		logger.info("Recupero i dati del record da modificare: " + lProgNoti);
		ISbViewNotifiche lCtrl = BDMCLookupRemote.getSbViewNotificheRemote();
		SbViewNotificheModel lSbViewNotifiche = lCtrl.ExRicercaSbViewNotificheById(lProgNoti);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lSbViewNotifiche == null) {
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
		logger.debug("Dati Record: " + lSbViewNotifiche);
		setRequestAttribute("sbviewnotifiche", lSbViewNotifiche);

		// Inserire Eventuali ComboBOX precaricando i dati del model
		// Option lOption = new Option( DecodificheManager.getInstance().get???());
		// setRequestAttribute("???", "" + lOption );

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		logger.debug("return: " + PG_LOAD_INSERISCISBVIEWNOTIFICHE);
		return PG_LOAD_INSERISCISBVIEWNOTIFICHE;
	}

}