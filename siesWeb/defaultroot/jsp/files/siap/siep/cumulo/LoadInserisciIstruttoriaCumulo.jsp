<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="EvIstruttoria" scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
//==============================================================================
//             Form per l'Apertura di un'istruttoria cumulo
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Apertura Istruttoria Cumulo</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciIstruttoriaCumulo">

  <table cellspacing="0" cellpadding="0">
    <% if (EvIstruttoria!=null && EvIstruttoria.getIdEvento()!=null) {%>
    <tr>
      <td class="L">
        Attenzione!! Esiste già una istruttoria aperta: 
        <font class="label">Istruttoria N. </font>
        <font class="campo">
          <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.cumulo.action.ActLoadGrigliaCumulo&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=EvIstruttoria.getIdEvento()%>" title="Istruttoria">
          <%=EvIstruttoria.getAnnoProtocollo()%>
          /
          <%=EvIstruttoria.getProgrProtocollo()%>
          </a>
        </font>
        <font class="label">Del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(EvIstruttoria.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    <% } else { %>
    <tr>
      <td class="L">
        Apertura nuova istruttoria cumulo
      </td>
      <td colspan="1">
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
    <% } %>
  </table> 
</form>   
</body>
</html>