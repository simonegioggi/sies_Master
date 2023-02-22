<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento avviso PagoPA --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/>

<html>
<head>
<title>[S.I.E.S.] - Genera Avviso PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<!-- <script language="JavaScript"> -->
<!-- function Verify() { -->
<!-- 	return true; -->
<!-- } -->
<!-- </script> -->
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
    	<td class="LBG">
    		<font class="label">Funzione:</font>&nbsp;&nbsp;<font class="campo">Genera Avviso PagoPA</font>
    	</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<FORM method="POST" name="LoadGeneraAvvisoPagoPA" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActGeneraAvvisoPagoPA">

<table>
	<tr>
		<td class="Titolo">Genera Avviso PagoPA</td>
	</tr>
   	<tr>
		<td class="lNoBord">
       		<br><INPUT class="bottone" type="submit" name="I" value="Genera Avviso"> <%-- onClick="javascript:return Verify();" --%>
       	</td>
   	</tr>
</table>
</form>
</body>
</html>