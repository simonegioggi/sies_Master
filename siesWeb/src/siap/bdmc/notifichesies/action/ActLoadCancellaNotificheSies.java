package siap.bdmc.notifichesies.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadCancellaNotificheSies
 * </p>
 * <p>
 * Description: Classe Action per la load cancella di NotificheSies
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
public class ActLoadCancellaNotificheSies extends ActionSiap implements ICostantiNotificheSies {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Cancellazione dei dati. Viene caricata la stessa pagina di
	 * Dettaglio con il tasto cancella
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da Visualizzare
		// ==========================================
		BigDecimal lIdNotificheSies = getRequestBigDecimalParameter(CAMPO_ID_NOTIFICHE_SIES);

		// ==========================================
		// Recupera i dati del record da Cancellare
		// ==========================================
		INotificheSies lCtrl = BDMCLookupRemote.getNotificheSiesRemote();
		NotificheSiesModel lNotMod = lCtrl.ExRicercaNotificheSiesById(lIdNotificheSies);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lNotMod == null) {
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
		setRequestAttribute("notifichesies", lNotMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "C");

		return PG_LOAD_CANCELLANOTIFICHESIES;
	}

}