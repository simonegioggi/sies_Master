<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>
<html>
  <head>
    <title> [S.I.E.S.] - Test sistema SIES - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
 </head>

 <body class="corpo" onload="Javascript:stampa('/jsp/Main.jsp?Action=siap.sico.test.action.ActTestSistema')"></body>
</html>