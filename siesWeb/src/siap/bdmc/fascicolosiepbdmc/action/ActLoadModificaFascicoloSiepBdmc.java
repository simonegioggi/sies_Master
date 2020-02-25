package siap.bdmc.fascicolosiepbdmc.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
//import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
//import per le combo
//import f3b.web.html.Option;
//import xxxx.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di FascicoloSiepBdmc
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
public class ActLoadModificaFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

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
		// Recupera la key del record da modificare x
		// ==========================================

		BigDecimal lIdFascicoloBdmc = getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_BDMC);

		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		logger.info("Recupero i dati del record da modificare: " + lIdFascicoloBdmc);
		IFascicoloSiepBdmc lCtrl = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		FascicoloSiepBdmcModel lFascicoloSiepBdmc = lCtrl.ExRicercaFascicoloSiepBdmcById(lIdFascicoloBdmc);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lFascicoloSiepBdmc == null) {
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
		logger.debug("Dati Record: " + lFascicoloSiepBdmc);

		setRequestAttribute("fascicolosiepbdmc", lFascicoloSiepBdmc);
		setSessionAttribute("fascicolosiepbdmcSession", lFascicoloSiepBdmc);

		IUfficio lUctrl = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficio = lUctrl.getUfficioByKey(lFascicoloSiepBdmc.getChiaveUfficioBdmc());
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
				lUfficio.getCodTipoUfficio());

		// Imposta i provvedimenti Rif
		setRequestAttribute("autoritaEmi", "" + lOption);

		String codUfficio = this.getUfficioUtenteConnesso().getCodUfficio(); // per il codice ufficio
																				// dell'operatore connesso
		String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la descrizione del
																						// tipo ufficio
																						// dell'utente
																						// connesso
		String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune dell'ufficio
																				// connesso
		String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della provincia

		setRequestAttribute("codUfficio", codUfficio);
		setRequestAttribute("descrUfficio", descrUfficio);
		setRequestAttribute("descrComune", descrComune);
		setRequestAttribute("codProv", codProv);

		setRequestAttribute("descrLuogoEmittente", lUfficio.getDescrComune());
		setRequestAttribute("codTipoAutoritaEmittente", lUfficio.getCodTipoUfficio());

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		// ==========================================================================
		// Restituisce la pagina di modifica.
		// n.b. è la stessa della pagina di Inserimento ma con modalità differente
		// ==========================================================================
		logger.debug("return: " + PG_LOAD_INSERISCIFASCICOLOSIEPBDMC);
		return PG_LOAD_INSERISCIFASCICOLOSIEPBDMC;
	}

}