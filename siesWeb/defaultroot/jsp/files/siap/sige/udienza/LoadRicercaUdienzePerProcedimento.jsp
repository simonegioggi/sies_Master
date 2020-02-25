<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="f3b.util.Utils"%>
<%@ page language="java" import="f3b.web.IWebConstants" %>

<html>
	<head>
  		<title> [S.I.E.S.] - Ricerca Udienza a partire da data - </title>
  		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  		<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	</head>

	<frameset rows="80%,*">
    	<frame name="ListaUdienzePopUp" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    	<%-- 20171003: [SG] tolgo (dataSI) dalla data odierna perchè deve cercare a partire dalla data udienza nella pagina --%>
<%
String giornoDU = request.getParameter("campoGG");
String meseDU = request.getParameter("campoMM");
String annoDU = request.getParameter("campoAA");
String data = "dataSI";
if (Utils.isPresent(giornoDU) && Utils.isPresent(meseDU) && Utils.isPresent(annoDU))
	data = "dataNO";
%>
    	<frame name="FiltraUdienze" src="/jsp/files/siap/sige/udienza/FiltraUdienze.jsp?formname=<%=request.getParameter("formname")%>&campoID=<%=request.getParameter("campoID")%>&campoGG=<%=request.getParameter("campoGG")%>&campoMM=<%=request.getParameter("campoMM")%>&campoAA=<%=request.getParameter("campoAA")%>&campoColl=<%=request.getParameter("campoColl")%>&campoLuogo=<%=request.getParameter("campoLuogo")%>&tipoRito=<%=request.getParameter("tipoRito")%>&dataOdierna=<%=data%>&" marginwidth="0" marginheight="0" scrolling="yes" frameborder="0" noresize>
	</frameset>
</html>