package siap.siep.fascicolo.action;

//import siap.sico.decodifiche.controller.ComuneController;
//import siap.sico.soggetto.controller.SoggettoController;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaFascicoliNonValidatiPerMessaggio extends ActionSiap implements ICostantiFascicoloSiep {

	public String processRequest() throws Exception {

//		String lPage = "";

		return /*lPage = */"/jsp/MainAttesa.jsp?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActRicercaFascicoliNonValidati";
	}

}