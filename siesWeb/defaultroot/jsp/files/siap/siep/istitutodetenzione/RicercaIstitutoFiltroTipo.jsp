<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="f3b.web.IWebConstants" %>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Istituto Detenzione - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
</head>

<frameset rows="70%,*">
    <frame name="listaIstituto" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    <% if ("SI".equals(request.getParameter("LoadDescEstesa")) ) { %>
    <frame name="filtro" src="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActListaIstituto&formname=<%=request.getParameter("formname")%>&fieldname=<%=request.getParameter("fieldname")%>&field2=<%=request.getParameter("field2")%>&LoadDescEstesa=<%=request.getParameter("LoadDescEstesa")%>" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
    <% } else { %>
    <frame name="filtro" src="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActListaIstituto&formname=<%=request.getParameter("formname")%>&fieldname=<%=request.getParameter("fieldname")%>&field2=<%=request.getParameter("field2")%>" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
    <% } %>
</frameset>

</html>