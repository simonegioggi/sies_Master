<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>

<jsp:useBean id="avvocato"	scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"	scope="request" class="java.lang.String"/>

<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<table cellspacing="2" cellpadding="2" width="95%">
 	<tr>
		<td class="Titolo" colspan="4"> Difensori </td>
 	</tr>
<%
Iterator itx = avvocato.iterator();
while (itx.hasNext()) {
AvvocatoSiusModel lAvv = (AvvocatoSiusModel) itx.next();
%>
	<tr style="width: 100%;">
		<td class=l><%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%></td>
		<td class=l><%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%></td>
		<td class=l><%=StringUtils.toStringJSP(lAvv.getAvvocato().getIndirizzo(),"-")%></td>
		<td class=l><%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
	</tr>
<%
}
if (request.getParameter("AvvRitorno") != null) {
%>
	<tr>
      	<td class="L" colspan="4">
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadInserisciAvvocato<%=retParam%>">
          		Inserimento Difensore&nbsp;<font class="ob">(*)</font>
        	</a>
      	</td>
	</tr>
<%
}
%>
<!-- <tr><td>&nbsp;</td></tr> -->
</table>