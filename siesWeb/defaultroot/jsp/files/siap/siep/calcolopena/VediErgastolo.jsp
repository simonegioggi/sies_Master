<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>

<jsp:useBean id="PenaResidua"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="PenaComplessiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Calcolo Pena</title>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Calcolo Pena</font>
        </td>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <table cellspacing=4 cellpadding=4>
	    <tr>
        <td class="Titolo"colspan=6><font  class="label">Pena Complessiva in Sentenza</font></td>
      </tr>
      <tr>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%></td>
        <td class="l"><font class="label">Data Inizio Pena</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizio(),"dd-MM-yyyy"))%></td>
        <td class="l"><font class="label">Data Fine Pena</td>
        <td class="l"><font class="campo">MAI</td>
      </tr>
<%
     if (PenaResidua.getDataInizioIsolamentoDiurno()!=null && PenaResidua.getDataFineIsolamentoDiurno()!=null)
     {
%>
	    <tr>
          <td class="Titolo" colspan=6><font  class="label">Isolamento Diurno</font></td>
        </tr>
        <% if (PenaResidua.getDataInizioIsolamentoDiurno()!=null)
        { %>
         <tr>
            <td class="l"><font class="label">Data Inizio</font></td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%></font></td>
        </tr>
        <% } %>
         <% if (PenaResidua.getDataFineIsolamentoDiurno()!=null)
        { %>
         <tr>
            <td class="l"><font class="label">Data Fine</font></td>
            <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%></font></td>
        </tr>
        <% } %>
        <% if (PenaResidua.getNumGiorniIsolamentoDiurno()!=null || PenaResidua.getNumMesiIsolamentoDiurno()!=null || PenaResidua.getNumAnniIsolamentoDiurno()!=null)
        { %>
         <tr>
            <td class="l"><font class="label">Durata</font></td>
            <td class="l"><font class="label">Anni</font>
            <font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniIsolamentoDiurno(),"0")%></font>
            <font class="label">Mesi</font>
            <font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiIsolamentoDiurno(),"0")%></font>
            <font class="label">Giorni</font>
            <font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniIsolamentoDiurno(),"0")%></font></td>
        </tr>
        <% } %>
    </table>
<% } %>
</html>