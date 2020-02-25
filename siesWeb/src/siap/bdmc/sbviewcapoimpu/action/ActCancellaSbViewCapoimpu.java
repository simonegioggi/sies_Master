package siap.bdmc.sbviewcapoimpu.action;

import java.math.BigDecimal;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbviewcapoimpu.controller.ISbViewCapoimpu;
import siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel;
import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActCancellaSbViewCapoimpu
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di SbViewCapoimpu
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
public class ActCancellaSbViewCapoimpu extends ActionSiap implements ICostantiSbViewCapoimpu {

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

		// ==========================================
		// Istanzia il model
		// ==========================================
		SbViewCapoimpuModel lSbVMod = new SbViewCapoimpuModel();

		lSbVMod.setIdPren(lIdPren);
		lSbVMod.setNumeProgCapoImpu(lNumeProgCapoImpu);

		// ======================================================
		// Recupera il Controller ed effettua la cancellazione
		// ======================================================
		ISbViewCapoimpu lCtrl = BDMCLookupRemote.getSbViewCapoimpuRemote();
		lCtrl.ExCancellaSbViewCapoimpu(lSbVMod);

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