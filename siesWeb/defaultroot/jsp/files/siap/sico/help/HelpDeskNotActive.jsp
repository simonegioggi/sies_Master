<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<html>
<head>
<title>[S.I.E.S.] - Invio Mail Anomalia </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

</head>


<body class="corpo">



<table>

    <tr>
	<td class=C>Servizio di Segnalazione HelpDesk non attivo.<br>Contattare amministratore di sistema
<br><br><br><br><br><br>
        <a href="javascript:window.close()">Chiudi</a>
</td>

	</tr>
</table>

</body>
</html>