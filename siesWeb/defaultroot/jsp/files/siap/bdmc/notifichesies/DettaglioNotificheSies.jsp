<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.notifichesies.model.NotificheSiesModel"%>
<%@ page import="siap.bdmc.notifichesies.action.ICostantiNotificheSies"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="notifichesies" scope="request" class="siap.bdmc.notifichesies.model.NotificheSiesModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio NotificheSies </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione NotificheSies </title>
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
        <font class="campo">Dettaglio NotificheSies</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione NotificheSies</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.notifichesies.action.ActLoadInserisciNotificheSies&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.notifichesies.action.ActLoadModificaNotificheSies&IdNotificheSies=<%=notifichesies.getIdNotificheSies()%>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.notifichesies.action.ActLoadCancellaNotificheSies','IdNotificheSies','<%=notifichesies.getIdNotificheSies()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaNotificheSies">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.notifichesies.action.ActCancellaNotificheSies">
  <input type="HIDDEN" name="<%=ICostantiNotificheSies.CAMPO_ID_NOTIFICHE_SIES%>" value="<%=StringUtils.toStringJSP(notifichesies.getIdNotificheSies()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Id Notifiche Sies</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getIdNotificheSies()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getAnnoSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getProgSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Ufficio Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getUfficioSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getAnnoFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Ufficio Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getUfficioFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Numero Fasc Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getNumeroFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Tipo Notifica</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getTipoNotifica()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Notifica</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataNotifica(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Stato Trasmissione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getStatoTrasmissione()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Trasmissione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataTrasmissione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getIdPren()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog Peri Pres</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getProgPeriPres()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Operatore Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getCodOperatoreInserimento()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataInserimento(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Cod Ufficio Inserimento</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(notifichesies.getCodUfficioInserimento()) %></font>&nbsp;</td>
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