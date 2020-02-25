<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>
<%@ page import="siap.sico.profilo.model.ProfiloModel"%>
<jsp:useBean id="utente" scope="request" class="siap.sico.utente.model.UtenteModel"/>


<html>
<head>
<title>[S.I.E.S.] - Dettaglio Utente </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">



</head>


		<body class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Utente</font>
      </td>

   </tr>
 </table>

<br>
<br>
<br>

<table cellspacing=2 cellpadding=2>
<tr>
<td class="int">Username</td><td class="l"><%=utente.getUserId() %></font></td>
</tr>
<tr>
<td class="int">Cognome</font></td><td class="l"><%=utente.getCognome() %></font></td>
</tr>
<tr>
<td class="int">Nome</td><td class="l"><%=utente.getNome() %></font></td>
</tr>
<tr>
<td class="int" width=30%>Ufficio</td><td class="l"><%=utente.getUfficioUtente().getDescrTipoUfficio()+" DI <font color=navy>"+utente.getUfficioUtente().getDescrComune() %></font></font></td>
</tr>
<tr>
<td class="int" width=30%>Inserisci Nuova Password</td><td class="l"></td>
<td class="int" width=30%>Ripeti Nuova Password</td><td class="l"></td>
</tr>


</table>
</body>
</html>