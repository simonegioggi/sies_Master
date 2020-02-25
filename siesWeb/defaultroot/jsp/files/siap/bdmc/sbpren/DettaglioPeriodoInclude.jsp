<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@ page import="siap.bdmc.sbperipren.action.ICostantiSbPeripren"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbperipren" scope="request" class="siap.bdmc.sbperipren.model.SbPeriprenModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio SbPeripren </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione SbPeripren </title>
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
        <font class="campo">Dettaglio SbPeripren</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione SbPeripren</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.sbperipren.action.ActLoadInserisciSbPeripren&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.sbperipren.action.ActLoadModificaSbPeripren&IdSbPeripren=<%=sbperipren.getProgPeriPres() %>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.sbperipren.action.ActLoadCancellaSbPeripren','IdSbPeripren','<%=sbperipren.getProgPeriPres()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaSbPeripren">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbperipren.action.ActCancellaSbPeripren">
  <input type="HIDDEN" name="<%=ICostantiSbPeripren.CAMPO_PROG_PERI_PRES%>" value="<%=StringUtils.toStringJSP(sbperipren.getProgPeriPres()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Data Iniz Peri</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbperipren.getDataInizPeri(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Fine Peri</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbperipren.getDataFinePeri(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog Peri Pres</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbperipren.getProgPeriPres()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbperipren.getIdPren()) %></font>&nbsp;</td>
  </tr>
  
  <tr>
    <td class="l">Cod Uffi Sies</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbperipren.getCodiUffiSies()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Anno Fasc Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbperipren.getAnnoFascSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Fasc Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbperipren.getNumeFascSiep()) %></font>&nbsp;</td>
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