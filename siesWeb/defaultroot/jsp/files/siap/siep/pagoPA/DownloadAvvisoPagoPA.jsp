<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di download avviso PagoPA --%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="tipoFascicolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="idEvento" 		scope="request" class="java.lang.Object"/>
<%-- MEV_33: aggiungo recupero numero dei Bollettini da generare --%>
<jsp:useBean id="numBollettini"	scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>Download Avviso PagoPA</title>
<script language="JavaScript">
function downloadAvviso() {
	document.LoadDownloadAvvisoPagoPA.submit();
}
</script>
</head>

<body onLoad="downloadAvviso()" class="corpo">
<FORM method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadDownloadAvvisoPagoPA"> 
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPA.action.ActInvocaWSGeneraAvvisoPagoPA"/>
<input type="hidden" name="tipoFascicolo" value="<%=tipoFascicolo%>"/>
<input type="hidden" name="idEvento" value="<%=idEvento%>"/>
<input type="hidden" name="numBollettini" value="<%=numBollettini%>"/>
	
<div align=center id="richCert" style="visibility:visible;position:absolute;top:200px;left:200px">
<table bgcolor="#EEEEEE">
	<tr>
		<td>
			<img src="/images/rotelle3.gif">
		</td>
		<td>
			<font size=+1 color=navy>Attendere... Generazione Avviso PagoPA in corso.</font>
		</td>
	</tr>
</table>
</div>
</FORM>
</body>
</html>