<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Udienza a partire da data - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
</head>

<frameset rows="80%,*">
    <frame name="ListaUdienzePopUp" src="/html/blank.htm" marginwidth="0" marginheight="0" scrolling="auto" frameborder="1" noresize>
    <frame name="FiltraUdienze" src="/jsp/files/siap/sius/udienza/FiltraUdienze.jsp?formname=<%=request.getParameter("formname")%>&campoID=<%=request.getParameter("campoID")%>&campoGG=<%=request.getParameter("campoGG")%>&campoMM=<%=request.getParameter("campoMM")%>&campoAA=<%=request.getParameter("campoAA")%>&campoColl=<%=request.getParameter("campoColl")%>&dataOdierna=dataSI&campo_sub=<%=request.getParameter("campo_sub")%>&campoLuogo=<%=request.getParameter("campoLuogo")%>"   marginwidth="0" marginheight="0" scrolling="yes" frameborder="0" noresize>

</frameset>

</html>