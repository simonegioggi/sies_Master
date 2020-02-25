<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<html>
<head>
<title>[S.I.E.S.] - Invio Mail Anomalia </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%> ></script>

<%
String cognome=UtenteConnesso.getCognome();
String nome=UtenteConnesso.getNome();
String userid=UtenteConnesso.getUserId();
String tipouff=UtenteConnesso.getUfficioUtente().getDescrTipoUfficio();
String sedeuff=UtenteConnesso.getUfficioUtente().getDescrComune() + " ( "+UtenteConnesso.getUfficioUtente().getDescProvincia()+" )";
%>
</head>


<body class="corpo">

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
<input value="siap.sico.help.action.SendMailToHelpDesk" type="hidden" name="<%= IWebConstants.ACTION_FIELD %>">
<table>

    <tr>
	<td class=L><font class=label>Segnalazione da :</font> <br><font class=campo><%= nome+"  "+cognome %></font>
        <font class=label>(Utente :</font> <font class=campo><%= userid %></font><font class=label>)</font> <br>
	<font class=label>Ufficio :</font><br><font class=campo> <%= tipouff %></font> <font class=label>di</font> <font class=campo><%= sedeuff %></font></td>
	</tr>
	<tr>
	<td class=L>La invitiamo a riportare qui sotto la sua segnalazione,<br> che verrà inoltrata immediatamente all'help desk.</td>
	</tr>
	<tr>
		<td class=L><textarea cols="50" rows="12" name="note"></textarea>
	</tr>
       <tr>
		<td class=L><input class=button type="submit" name="go" value="Invia Segnalazione >>"></td>
	</tr>
</table>
</FORM>

</body>
</html>