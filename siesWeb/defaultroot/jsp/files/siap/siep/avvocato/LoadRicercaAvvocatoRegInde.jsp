<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_21: aggiunta pagina per chiamata a WS per individuare lista avvocato in ReGIndE --%>

<%@ page language="java" import="f3b.web.IWebConstants"%>

<html>
<head>
<title> [S.I.E.S.] - Ricerca Difensore su ReGIndE </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
</script>
</head>

<frameset rows="70%,*">
    <frame name="listaAvvocatiRegInde" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    <frame name="filtro" src="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActFiltraAvvRegInde&formname=<%=request.getParameter("formname")%>" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" noresize>
</frameset>

</html>