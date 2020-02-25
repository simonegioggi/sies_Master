<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Sezione - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
</head>

<frameset rows="80%,*">
    <frame name="listaSezioni" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    <frame name="filtro" src="/jsp/files/siap/sige/sezione/FiltraSezLista.jsp?formname=<%=request.getParameter("formname")%>" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
</frameset>

</html>