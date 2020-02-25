<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>

<jsp:useBean id="tipoFascicolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="idEvento" scope="request" class="java.lang.Object"/>

<html>
	<head>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<title> Richiesta Certificato Penale da Sies a Nsc </title>
		<script language="JavaScript">
  			function CaricaDatiPerInoltroRichiesta() 
  			{ 
 						document.LoadInviaRichiestaToNsc.submit();
				}
  		</script>
	</head>

	<body onLoad="CaricaDatiPerInoltroRichiesta()" class="corpo"> 
		<FORM method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadInviaRichiestaToNsc"> 
			<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" 
					value="siap.sico.webservice.action.ActPrelevaDatiRichiestaCertificato" />
			<input type="hidden" name="tipoFascicolo" value="<%=tipoFascicolo%>" />
			<input type="hidden" name="idEvento" value="<%=idEvento%>" />
				
			<div align=center id="richCert" style="visibility:visible;position:absolute;top:200px;left:200px">
     			<table bgcolor="#EEEEEE">
       				<tr>
       					<td>
         					<img src="/images/rotelle3.gif">
       					</td>
       					<td>
         					<font size=+1 color=navy>Attendere... Inoltro Richiesta Certificato Penale in corso.</font>
       					</td>
       				</tr>
     			</table>
	   		</div> 	
		</FORM>
	</body>
</html>