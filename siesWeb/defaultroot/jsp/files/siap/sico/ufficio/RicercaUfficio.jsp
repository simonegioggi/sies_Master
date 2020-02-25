<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>

<%
String minor_param = "";
String unep_param = "";
if (request.getParameter("minor") != null) {
	minor_param = "&minor=" + request.getParameter("minor");
}
if (request.getParameter("unep") != null) {
	unep_param = "&unep=" + request.getParameter("unep");
}
%>
<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procura </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<frameset rows="25%,*">
  <frame name="listadistretti" src="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistrettiProcure&formname=<%=request.getParameter("formname")%>&fieldname=<%=request.getParameter("fieldname")%><%=minor_param%><%=unep_param%>" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
  <frame name="listauffici" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
</frameset>

</html>