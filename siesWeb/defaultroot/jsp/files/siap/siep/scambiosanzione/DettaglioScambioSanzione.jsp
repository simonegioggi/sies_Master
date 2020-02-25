<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="scambiosanzione" scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio ScambioSanzione </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione ScambioSanzione </title>
  <%}%> 
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
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
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
      <%  if( modalita.equals("D") )  {%> 
        <font class="campo">Dettaglio ScambioSanzione</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione ScambioSanzione</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.siep.scambiosanzione.action.ActLoadInserisciScambioSanzione&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.siep.scambiosanzione.action.ActLoadModificaScambioSanzione&IdScambioSanzione=<%=scambiosanzione.getIdScambioSanzione()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.siep.scambiosanzione.action.ActLoadCancellaScambioSanzione','IdScambioSanzione','<%=scambiosanzione.getIdScambioSanzione()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaScambioSanzione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scambiosanzione.action.ActCancellaScambioSanzione">
  <input type="HIDDEN" name="<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>" value="<%=StringUtils.toStringJSP(scambiosanzione.getIdScambioSanzione()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Id Scambio Sanzione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getIdScambioSanzione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Tipo Decisione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodTipoDecisione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Natura Sanzione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodNaturaSanzione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Tipo Sanzione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodTipoSanzione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Inizio</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInizio(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Fine</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataFine(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getNote()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Registro</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getAnnoRegistro()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Registro</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getNumeroRegistro()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Chiave Anno Fascicolo Sius</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getChiaveAnnoFascicoloSius()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Chiave Progr Fascicolo Sius</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getChiaveProgrFascicoloSius()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Sorveglianza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioSorveglianza()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Emittente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioEmittente()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Operatore Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodOperatoreInserimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInserimento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioInserimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Operatore Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodOperatoreAggiornamento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataAggiornamento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Aggiornamento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioAggiornamento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Eve Id Evento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(scambiosanzione.getEveIdEvento()) %></font>&nbsp;</td>
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