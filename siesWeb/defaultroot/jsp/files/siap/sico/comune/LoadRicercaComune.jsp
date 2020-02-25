<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Comune - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
</head>

<frameset rows="70%,15%,*">
    <frame name="listacomuni" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    <frame name="listaprovince" src="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaProv&formname=<%=request.getParameter("formname")%>&fieldname=<%=request.getParameter("fieldname") %> " marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
    <frame name="comuni" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
</frameset>

</html>