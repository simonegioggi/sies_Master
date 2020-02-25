<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page language="java" import="f3b.web.IWebConstants"%>
<%@page import="f3b.web.RedirectTo"%>

<html>
	<head>
  	<title> [S.I.E.S.] - Ricerca Giudice Popolare - </title>
  		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  		<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	</head>

	<frameset rows="65%,*">
    <frame name="listaGiudiciPopolari" src="/html/blank.htm" 
					 marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
<% 
	RedirectTo lRedirect = new RedirectTo();
	lRedirect.setPage(IWebConstants.PG_MAIN);
	lRedirect.setAction("siap.sige.giudicepopolare.action.ActLoadRicercaGiudicePopolareLista");
	lRedirect.setParameter("formname",request.getParameter("formname"));
	lRedirect.setParameter("idfieldnum",request.getParameter("idfieldnum"));
	lRedirect.setParameter("isFormFilter","y");
%>
    <frame name="filtro" src="<%=lRedirect%>" marginwidth="0" 
					 marginheight="0" scrolling="no" frameborder="0" noresize>
	</frameset>
</html>