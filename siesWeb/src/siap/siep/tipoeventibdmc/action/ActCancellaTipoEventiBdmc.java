package siap.siep.tipoeventibdmc.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.tipoeventibdmc.controller.ITipoEventiBdmc;
import siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActCancellaTipoEventiBdmc
 * </p>
 * <p>
 * Description: Classe Action per la cancellazione di TipoEventiBdmc
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
public class ActCancellaTipoEventiBdmc extends ActionSiap implements ICostantiTipoEventiBdmc {

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
		BigDecimal lIdTipoEventiBdmc = getRequestBigDecimalParameter(CAMPO_ID_TIPO_EVENTI_BDMC);

		// ==========================================
		// Istanzia il model
		// ==========================================
		TipoEventiBdmcModel lTipMod = new TipoEventiBdmcModel();

		lTipMod.setIdTipoEventiBdmc(lIdTipoEventiBdmc);

		// ======================================================
		// Recupera il Controller ed effettua la cancellazione
		// ======================================================
		ITipoEventiBdmc lCtrl = SIEPLookupRemote.getTipoEventiBdmcRemote();
		lCtrl.ExCancellaTipoEventiBdmc(lTipMod);

		// ===========================================================
		// Restituisce la pagina di Conferma avvenuta Cancellazione.
		// ===========================================================
		setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT, "Cancellazione effettuata");
		// Specificare eventualmente la jump page dove verrà ridirezionata la
		// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
		// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
		// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
		// della root_dir es /siep/frame.htm
		setRequestAttribute(ISIAPCostantiWeb.GOTO_PAGE, ISIAPCostantiWeb.ROOT_DIR);

		return ISIAPCostantiWeb.PG_MESSAGE;
	}

}