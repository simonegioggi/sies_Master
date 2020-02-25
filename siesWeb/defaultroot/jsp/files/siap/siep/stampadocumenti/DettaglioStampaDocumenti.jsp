<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.stampadocumenti.model.StampaDocumentiModel"%>
<%@ page import="siap.siep.stampadocumenti.action.ICostantiStampaDocumenti"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="stampadocumenti" scope="request" class="siap.siep.stampadocumenti.model.StampaDocumentiModel"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio StampaDocumenti </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione StampaDocumenti </title>
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
        <font class="campo">Dettaglio StampaDocumenti</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione StampaDocumenti</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.siep.stampadocumenti.action.ActLoadInserisciStampaDocumenti&TornaQui=10">
       <img align="middle" src="images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.siep.stampadocumenti.action.ActLoadModificaStampaDocumenti&IdStampa=<%=stampadocumenti.getIdStampa()%>&TornaQui=10">
         <img align="middle" src="images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.siep.stampadocumenti.action.ActLoadCancellaStampaDocumenti','IdStampa','<%=stampadocumenti.getIdStampa()%>');">
         <img align="middle" src="images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaStampaDocumenti">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.stampadocumenti.action.ActCancellaStampaDocumenti">
  <input type="HIDDEN" name="<%=ICostantiStampaDocumenti.CAMPO_ID_STAMPA%>" value="<%=StringUtils.toStringJSP(stampadocumenti.getIdStampa()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Id Stampa</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(stampadocumenti.getIdStampa()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Utente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(stampadocumenti.getIdUtente()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(stampadocumenti.getData(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Stato</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(stampadocumenti.getStato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Num Stampe Richieste</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(stampadocumenti.getNumStampeRichieste()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Stampe Effettuate</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(stampadocumenti.getNumeStampeEffettuate()) %></font>&nbsp;</td>
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