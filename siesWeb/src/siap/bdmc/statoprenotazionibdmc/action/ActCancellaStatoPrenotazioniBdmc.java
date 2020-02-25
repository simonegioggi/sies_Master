package siap.bdmc.statoprenotazionibdmc.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.statoprenotazionibdmc.controller.IStatoPrenotazioniBdmc;
import siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActCancellaStatoPrenotazioniBdmc
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di StatoPrenotazioniBdmc
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
public class ActCancellaStatoPrenotazioniBdmc extends ActionSiap implements ICostantiStatoPrenotazioniBdmc {

	/*****************************************************************************
	 * Azione per la cancellazione dei dati.
	 * 
	 * @return PG_MESSAGE di avvenuta cancellazione
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {

		// ==========================================
		// Recupera la key del record da Cancellare
		// ==========================================
		BigDecimal lStatoPrenotazioniBdmc = getRequestBigDecimalParameter(CAMPO_STATO_PRENOTAZIONI_BDMC);

		// ==========================================
		// Istanzia il model
		// ==========================================
		StatoPrenotazioniBdmcModel lStaMod = new StatoPrenotazioniBdmcModel();

		lStaMod.setStatoPrenotazioniBdmc(lStatoPrenotazioniBdmc);

		// ======================================================
		// Recupera il Controller ed effettua la cancellazione
		// ======================================================
		IStatoPrenotazioniBdmc lCtrl = BDMCLookupRemote.getStatoPrenotazioniBdmcRemote();
		lCtrl.ExCancellaStatoPrenotazioniBdmc(lStaMod);

		// ===========================================================
		// Restituisce la pagina di Conferma avvenuta Cancellazione.
		// ===========================================================
		setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Cancellazione effettuata");
		// Specificare eventualmente la jump page dove verrà ridirezionata la
		// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
		// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
		// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
		// della root_dir es /siap/frame.htm
		setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);

		return ISIAPCostantiWeb.PG_MESSAGE;
	}

}