package siap.bdmc.sbviewreat.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewreat.controller.ISbViewReat;
import siap.bdmc.sbviewreat.model.SbViewReatModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActCancellaSbViewReat
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di SbViewReat
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
public class ActCancellaSbViewReat extends ActionSiap implements ICostantiSbViewReat {

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
		BigDecimal lIdPren = getRequestBigDecimalParameter(CAMPO_ID_PREN);
		BigDecimal lNumeProgCapoImpu = getRequestBigDecimalParameter(CAMPO_NUME_PROG_CAPO_IMPU);
		BigDecimal lNumeProgReat = getRequestBigDecimalParameter(CAMPO_NUME_PROG_REAT);

		// ==========================================
		// Istanzia il model
		// ==========================================
		SbViewReatModel lSbVMod = new SbViewReatModel();

		lSbVMod.setIdPren(lIdPren);
		lSbVMod.setNumeProgCapoImpu(lNumeProgCapoImpu);
		lSbVMod.setNumeProgReat(lNumeProgReat);

		// ======================================================
		// Recupera il Controller ed effettua la cancellazione
		// ======================================================
		ISbViewReat lCtrl = BDMCLookupRemote.getSbViewReatRemote();
		lCtrl.ExCancellaSbViewReat(lSbVMod);

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