<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel" %>

<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoModel"%>
<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="esito" scope="request" class="siap.regesies.regesentenza.model.EsitoImportModel"/>

<%
BigDecimal KeyFascicolo = (BigDecimal) request.getAttribute("KeyFascicolo");
%>
<html>
  <head>
    <title> [S.I.E.S.] - Esito Importazione Dati da ReGe in SIEP- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

<BODY class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
       <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Esito Integrazione Dati da ReGe in SIEP</font>
       </td>
       <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>"></jsp:include>
       </td>
      </tr>
    </table>
  </FORM>

 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>
 <br>
    <jsp:include page="/jsp/files/siap/regesies/regesentenza/EsitoInclude.jsp"/>
 <br>



  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioProvvedimentoRege">
  <table>
  <tr>
   <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Dettaglio Procedimento SIEP">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActLoadDettaglioFascicolo">
        <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=KeyFascicolo.toString()%>">
      </td>
  </tr></table>

  </body>

</html>