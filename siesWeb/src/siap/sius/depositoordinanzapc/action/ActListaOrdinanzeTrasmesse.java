package siap.sius.depositoordinanzapc.action;

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
 * @author not attributable
 * @version 1.0
 */
public class ActListaOrdinanzeTrasmesse extends ActionSiap {

	public String processRequest() throws Exception {

//		String lPage = "";
		return /*lPage = */"/jsp/MainAttesa.jsp?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.depositoordinanzapc.action.ActListaOrdinanzeTrasmesseReale";
	}

}