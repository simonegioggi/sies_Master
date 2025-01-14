<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.action.ICostantiFascicoloSiepBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolosiepbdmc" scope="request" class="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"/>
<jsp:useBean id="codUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="codProv" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrLuogoEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<html>
<head>
<%
if (modalita.equals("D")) {
%> 
<title> Dettaglio Associazione Fascicolo Siep-Bdmc </title>
<%
}
%> 
<link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
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
<%
if (modalita.equals("D")) {
%> 
        	<font class="campo">Dettaglio Associazione Fascicolo Siep-Bdmc</font>
<%
}
%> 
		</td>
    </tr>
</table>
</FORM>

<table cellspacing=4 cellpadding=4>
  	<tr>
	    <td class="l">Id Fascicolo Bdmc</td>
	    <td class="l">
	    	<font class="campo"><%=StringUtils.toStringJSP(fascicolosiepbdmc.getIdFascicoloBdmc())%></font>
	    	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_ID_FASCICOLO_BDMC%>" value="<%=StringUtils.toStringJSP(fascicolosiepbdmc.getIdFascicoloBdmc())%>">
	    </td>
	</tr>
  	<tr>
	    <td class="l">Anno / Numero Bdmc</td>
	    <td class="l">
		    <font class="campo"><%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveAnnoBdmc())%></font>&nbsp;
			<font class="campo">/</font>&nbsp;
		    <font class="campo"><%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveProgrBdmc())%></font>
	    </td>
	</tr>
	<tr>
	    <td class="l">Ufficio Bdmc</td>
	    <td class="l"><font class="campo"><%=codTipoAutoritaEmittente + " - " + descrLuogoEmittente %></font>&nbsp;</td>
	</tr>
	<tr>
	    <td class="l">Anno / Numero Siep</td>
	    <td class="l">
		    <font class="campo"><%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveAnnoSiep())%></font>&nbsp;
		    <font class="campo">/</font>&nbsp;
		    <font class="campo"><%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveProgrSiep())%></font>
	    </td>
	</tr>
	<tr>
	    <td class="l">Ufficio Siep</td>
	    <td class="l"><font class="campo"><%=descrUfficio + " - " + descrComune + " (" + codProv + ")"%></font>&nbsp;</td>
	</tr>
	<tr>
    	<td class="l">Trasmissione</td>
<%
String flag_trasm = StringUtils.toStringJSP(fascicolosiepbdmc.getFlagTrasmissione());
if (flag_trasm.equalsIgnoreCase("S")) {
%> 
		<td class="l"><font class="campo">SI</font>&nbsp;</td>
<%
}
if (flag_trasm.equalsIgnoreCase("N")) {
%> 
		<td class="l"><font class="campo">NO</font>&nbsp;</td>
<%
}
if (flag_trasm.equalsIgnoreCase("")) {
%> 
		<td class="l"><font class="campo">&nbsp;</font>&nbsp;</td>
<%
}
%> 
	</tr>
</table>
</body>
</html>