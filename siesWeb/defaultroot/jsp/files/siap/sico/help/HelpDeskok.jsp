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


<table>

    <tr>
	<td class=L>
         <font class=label>Mail inviata all'Help Desk.</font>
        <br><br><br><br><br><br>
        <a href="javascript:window.close()">Chiudi</a>
        </td>
    </tr>
</table>


</body>
</html>