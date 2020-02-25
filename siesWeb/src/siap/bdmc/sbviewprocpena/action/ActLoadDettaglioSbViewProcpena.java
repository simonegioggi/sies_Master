package siap.bdmc.sbviewprocpena.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioSbViewProcpena
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di SbViewProcpena
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
public class ActLoadDettaglioSbViewProcpena extends ActionSiap implements ICostantiSbViewProcpena {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdPren = getRequestBigDecimalParameter(CAMPO_ID_PREN);

		// String lFlagSele = getRequestStringParameter (CAMPO_FLAG_INFO_SELE);
		// ==========================================
		// Recupera i dati del record da modificare
		// ==========================================
		ISbViewProcpena lCtrl = BDMCLookupRemote.getSbViewProcpenaRemote();
		SbViewProcpenaModel lSbVMod = lCtrl.ExRicercaSbViewProcpenaById(lIdPren);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lSbVMod == null) {
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
		setRequestAttribute("sbviewprocpena", lSbVMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		return PG_LOAD_DETTAGLIOSBVIEWPROCPENA;
	}

}