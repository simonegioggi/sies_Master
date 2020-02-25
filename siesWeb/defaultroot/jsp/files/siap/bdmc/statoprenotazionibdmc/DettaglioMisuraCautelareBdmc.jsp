<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"%>
<%@ page import="siap.siep.misuracautelarebdmc.action.ICostantiMisuraCautelareBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="misuracautelarebdmc" scope="request" class="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"/>
<jsp:useBean id="descUfficioBdmc" scope="request" class="java.lang.String"/>
<jsp:useBean id="descUfficioSiep" scope="request" class="java.lang.String"/>
<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio Misura Cautelare per Trasmissioni Bdmc </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione MisuraCautelareBdmc </title>
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
        <font class="campo">Dettaglio Misura Cautelare per Trasmissioni Bdmc</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione MisuraCautelareBdmc</font>
      <%}%> 
      </td>
       <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaMisuraCautelareBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siep.misuracautelarebdmc.action.ActCancellaMisuraCautelareBdmc">
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelareBdmc.CAMPO_ID_MISURA_CAUTELARE%>" value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getIdMisuraCautelare()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  
  <tr><td class="Titolo" colspan=6>Informazioni provenienti da Bdmc</td> </tr>
  <tr>
    <td class="l">Numero Prenotazione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getIdPren()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Prog. Periodo </td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getProgPeriPres()) %></font>&nbsp;</td>
  </tr>
   <tr>
    <td class="l">Data Inizio Periodo </td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizio(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Fine Periodo </td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFine(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
 <tr>
    <td class="l">Computabile</td>
    <% if (misuracautelarebdmc.getFlagComputabile().compareTo("0") ==0) { %>
    	<td class="l"><font class="campo">SI</font>&nbsp;</td>
    <%} else {%>  
    	<td class="l"><font class="campo">NO</font>&nbsp;</td>
    <%} %> 
</tr>
 <tr>
    <td class="l">Anno / Numero Bdmc</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascBdmc()) %> / <%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
   <tr>
    <td class="l">Ufficio Bdmc</td>
    <td class="l"><font class="campo"><%=descUfficioBdmc %></font>&nbsp;</td>
  </tr>
  <tr><td class="Titolo" colspan=6>Informazioni provenienti da Siepc</td> </tr>
  <tr>
    <td class="l">Anno / Numero Siep</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascSiep()) %> / <%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascSiep()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Ufficio Siep</td>
    <td class="l"><font class="campo"><%=descUfficioSiep %></font>&nbsp;</td>
  </tr>
 <tr>
    <td class="l">Data Inizio Periodo Usata</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizioUsata(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Fine Periodo Usata</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFineUsata(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuracautelarebdmc.getNote()) %></font>&nbsp;</td>
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