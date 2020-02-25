<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Stack"%>
<%@ page import="f3b.web.IWebConstants"%>
<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <!-- BOTTONE DI RITORNO -->
<%
if (StackDiRitorno != null) {
	if (!StackDiRitorno.isEmpty()) {
		String lRet = "";
        // Lo Stack non è vuoto
       	if (TornaQui != null) {
        // C'è Link di Ritorno di tipo 2
			if (TornaQui.trim().compareTo("20") == 0) {
           		if (StackDiRitorno.size() > 1) {
             		lRet = (String)StackDiRitorno.get(StackDiRitorno.size() - 2);
            		lRet  += "&" + IWebConstants.FLAG_RITORNO + "=2";
           		}
        	}
        	// C'è Link di Ritorno di tipo 1
 			// if (TornaQui.trim().compareTo("10") == 0)
			else {
           		lRet = (String) StackDiRitorno.peek();
           		lRet  += "&" + IWebConstants.FLAG_RITORNO + "=1";
        	}
		}
		// 20190519 [SG]: aggiunta gestione idUdienzaSige
        if (lRet.length() > 1) {
        	String s = request.getParameter("idUdiSig");
        	if (Utils.isPresent(s))
        		lRet += "&idUdiSig=" + s;
%>
		<td class="LBG">
          	<a href="<%=lRet%>">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
        </td>
<%
		}
	}
}
%>