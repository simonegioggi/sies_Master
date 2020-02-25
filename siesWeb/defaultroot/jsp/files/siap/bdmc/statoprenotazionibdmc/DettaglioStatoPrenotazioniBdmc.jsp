<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"%>
<%@ page import="siap.bdmc.statoprenotazionibdmc.action.ICostantiStatoPrenotazioniBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="statoprenotazionibdmc" scope="request" class="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio StatoPrenotazioniBdmc </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione StatoPrenotazioniBdmc </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    function Verify() { 
      var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio StatoPrenotazioniBdmc</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione StatoPrenotazioniBdmc</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.statoprenotazionibdmc.action.ActLoadInserisciStatoPrenotazioniBdmc&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.statoprenotazionibdmc.action.ActLoadModificaStatoPrenotazioniBdmc&IdStatoPrenotazioniBdmc=<%=statoprenotazionibdmc.getStatoPrenotazioniBdmc()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.statoprenotazionibdmc.action.ActLoadCancellaStatoPrenotazioniBdmc','IdStatoPrenotazioniBdmc','<%=statoprenotazionibdmc.getStatoPrenotazioniBdmc()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaStatoPrenotazioniBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.statoprenotazionibdmc.action.ActCancellaStatoPrenotazioniBdmc">
  <input type="HIDDEN" name="<%=ICostantiStatoPrenotazioniBdmc.CAMPO_STATO_PRENOTAZIONI_BDMC%>" value="<%=StringUtils.toStringJSP(statoprenotazionibdmc.getStatoPrenotazioniBdmc()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Stato Prenotazioni Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getStatoPrenotazioniBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Misura Cautelare Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getIdMisuraCautelareBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Trasmissione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(statoprenotazionibdmc.getDataTrasmissione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Esito Id</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getEsitoId()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Esito Msg</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getEsitoMsg()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Prenotazione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getIdPrenotazione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog Peri Pres</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getProgPeriPres()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Tipo Trasmissione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(statoprenotazionibdmc.getTipoTrasmissione()) %></font>&nbsp;</td>
  </tr>

<%  if( modalita.equals("C") )  {%> 
  <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Conferma"  onclick="Javascript: return Verify();">
    </td>
  </tr>
<%}%>

</table>
<%  if( modalita.equals("C") )  {%> 
</form>
<%}%>
</body>
</html>