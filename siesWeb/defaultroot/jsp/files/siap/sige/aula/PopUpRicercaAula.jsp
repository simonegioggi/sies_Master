<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sige.aula.action.ICostantiAula" %>
<%@ page import="siap.sige.aula.model.AulaUdienzaModel" %>

<jsp:useBean id="aule" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Pop up - Lista Aule</title>

<script language="JavaScript">
function caricaAula(idAula, descAula, descIngresso, descPiano, descStanza)      {
  	window.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAula.CAMPO_ID_AULA%>.value=idAula;
  	window.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value=descAula;
  	window.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value=descIngresso;
  	window.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value=descPiano;
  	window.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>.value=descStanza;
  	self.close();
}
</script>

</head>

<body class=corpo>

 <table>
  <tr>
    <td class=LBG>Selezionare Aula</td>
  </tr>
</table>
<br>

<%
Iterator itx = aule.iterator();
if (!itx.hasNext()) {
%>
Nessuna aula associata alla sezione
<%
} else {
%>
<table width="100%">
	<tr>
        <td class="int" width=10%>Sezione</td>
        <td class="int" width=25%>Descrizione</td>
		<td class="int" width=10%>Stanza</td>
		<td class="int" width=5%>Piano</td>
		<td class="int" width=25%>Ingresso</td>
		<td class="int" width=5%>Azioni</td>
	</tr>
<%
	while ( itx.hasNext())  {
    	AulaUdienzaModel aula = (AulaUdienzaModel)itx.next();
%>
	<tr>
      	<td class=l>
      	  	<%=StringUtils.toStringJSP(aula.getSezione().getDescrizione(), "-")%>
      	</td>
      	<td class=l>
      	  	<%=StringUtils.toStringJSP(aula.getDescrizioneAula(), "-")%>
      	</td>
      	<td class=l>
      	  	<%=StringUtils.toStringJSP(aula.getDescrizioneStanza(), "-")%>
      	</td>
      	<td class=l>
      	  	<%=StringUtils.toStringJSP(aula.getNumeroPiano(), "-")%>
      	</td>
      	<td class=l>
      	  	<%=StringUtils.toStringJSP(aula.getDescrizioneIngresso(), "-")%>
      	</td>
      	<td class=c>
      		<%-- 20171005: [SG] gestione valori null --%>
      		<a href="Javascript:caricaAula('<%=aula.getIdAula()%>',
      				'<%=StringUtils.toStringJSP(aula.getDescrizioneAula())%>',
      				'<%=StringUtils.toStringJSP(aula.getDescrizioneIngresso())%>',
      				'<%=StringUtils.toStringJSP(aula.getNumeroPiano())%>',
      				'<%=StringUtils.toStringJSP(aula.getDescrizioneStanza())%>');">
      			<img align="middle" src="/images/fileselected.gif" border=0>
      		</a>
      	</td>
	</tr>
<%
  	}
}
%>
</table>
</body>
</html>