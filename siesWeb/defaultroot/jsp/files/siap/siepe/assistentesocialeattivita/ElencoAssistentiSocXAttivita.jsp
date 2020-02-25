<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siepe.assistentesocialeattivita.model.AssistenteSocialeAttivitaAssSocModel" %>

<jsp:useBean id="assistenti" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Assistenti Sociali X Attività </title>
  </head>


  <body class="corpo">

  <table width=85%>
    <tr> <td class="Titolo" > Elenco Storico Assistenti Sociali Assegnati </td> </tr>
</table>
<% if (assistenti.size() > 0) { %>
<table width=85%>
      <tr>
        <td class="int" width=20%>Nome</td>
        <td class="int" width=20%>Cognome</td>
        <td class="int" width=20%>Data inizio</td>
        <td class="int" width=20%>Data fine</td>
      </tr>
<%
  Iterator itx = assistenti.iterator();
  while ( itx.hasNext())
  {
    AssistenteSocialeAttivitaAssSocModel assistente = (AssistenteSocialeAttivitaAssSocModel)itx.next();
%>
    <tr>
    	<td class="l"><%=assistente.getNome()%></td>
    	<td class="l"><%=assistente.getCognome()%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistente.getDataInizio(),"dd-MM-yyyy"),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(assistente.getDataFine(),"dd-MM-yyyy"),"-")%></td>
    </tr>
<%
  }
%>
   </table>
<% } else { %>
<table>
    <tr><td class="int" > Non ci sono Assistenti Sociali assegnati all'Attività </td></tr>
</table>
<% } %>
  <br>
  </body>
</html>