<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"%>
<%@ page import="siap.bdmc.sbviewcapoimpu.action.ICostantiSbViewCapoimpu"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewcapoimpu" scope="request" class="siap.bdmc.sbviewcapoimpu.model.SbViewCapoimpuModel"/>

<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio SbViewCapoimpu </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione SbViewCapoimpu </title>
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
        <font class="campo">Dettaglio SbViewCapoimpu</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione SbViewCapoimpu</font>
      <%}%> 
      </td>
      <td class="LBG">
         <a href="Main.jsp?Action=siap.bdmc.sbviewcapoimpu.action.ActLoadInserisciSbViewCapoimpu&TornaQui=10">
       <img align="middle" src="/images/new24.gif" alt="Inserisci" width="24" height="24" border="0">
    </a>
 
       <a href="Main.jsp?Action=siap.bdmc.sbviewcapoimpu.action.ActLoadModificaSbViewCapoimpu&IdSbViewCapoimpu=<%=sbviewcapoimpu.getNumeProgCapoImpu() %>&TornaQui=10">
         <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
        </a>
 
         <a href="Javascript:conferma('siap.bdmc.sbviewcapoimpu.action.ActLoadCancellaSbViewCapoimpu','IdSbViewCapoimpu','<%=sbviewcapoimpu.getNumeProgCapoImpu()%>');">
         <img align="middle" src="/images/delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
 
      </td>
    </tr>
  </table>
</FORM>

<%  if( modalita.equals("C") )  {%> 
<FORM method="POST" action="Main.jsp" name="CancellaSbViewCapoimpu">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewcapoimpu.action.ActCancellaSbViewCapoimpu">
  <input type="HIDDEN" name="<%=ICostantiSbViewCapoimpu.CAMPO_NUME_PROG_CAPO_IMPU%>" value="<%=StringUtils.toStringJSP(sbviewcapoimpu.getNumeProgCapoImpu()) %>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
<%}%> 
<table cellspacing=4 cellpadding=4>
  <tr>
    <td class="l">Flag Arti 0056</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0056()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0061</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0061()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti 0061 Comm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0061Comm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0081</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0081()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti 0081 Comm</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0081Comm()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Art 0110</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArt0110()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0112</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0112()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Arti 0112 Commi</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getArti0112Commi()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0113</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0113()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0114</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0114()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0116</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0116()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Arti 0117</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagArti0117()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Luog Reat</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getLuogReat()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Flag Peri Temp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getFlagPeriTemp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Reat 0101</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0101(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Data Reat 0202</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewcapoimpu.getDataReat0202(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Desc Peri Temp</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getDescPeriTemp()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nume Prog Capo Impu</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getNumeProgCapoImpu()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Id Pren</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(sbviewcapoimpu.getIdPren()) %></font>&nbsp;</td>
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