package siap.siep.jms.action;

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
public class ActListaMessaggiTrasmessi extends ActionSiap implements ICostantiSiepJMS {

	public String processRequest() throws Exception {

		// return PG_ATTESA;
//		String lPage = "";
		String lTipoOperazione = "&tipoOperazione=" + TRASFERIMENTO_PROVVEDIMENTO;
		if (!isRequestParameterNullObj("tipoOperazione"))
			lTipoOperazione = "&tipoOperazione=" + getRequestStringParameter("tipoOperazione").toString();
		return /*lPage = */"/jsp/MainAttesa.jsp?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.jms.action.ActListaMessaggiTrasmessiReale" + lTipoOperazione;
	}

}