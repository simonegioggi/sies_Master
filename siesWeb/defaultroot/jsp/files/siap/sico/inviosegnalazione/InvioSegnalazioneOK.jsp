<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV10-s3: aggiunta pagina invio segnalazione --%>
<%@ page import="f3b.web.IWebConstants"%>
<html>
	<head>
		<title>[S.I.E.S.] - Invio Segnalazione Avvenuto</title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	</head>
	<body class="corpo">
		<table style="border: 2.5px solid black; width: 100%;">
			<tr>
		      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
		      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" colspan="4">
					Mail di segnalazione inviata.
		      	</td>
		    </tr>
			<tr>
				<td class="L">
					<center>
						<input class="bottone" type="button" name="chiudi" value="Chiudi" onclick="Javascript:window.close()">
					</center>
				</td>
			</tr>
		</table>
	</body>
</html>