<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>

<jsp:useBean id="PeriodoAltraMisura" scope="request"
	class="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel" />
<jsp:useBean id="istitutodetenzione" scope="request"
	class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />
<jsp:useBean id="fascicoloSiusGP" scope="session"
	class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="modalita" scope="request" 
	class="java.lang.String"/>
	
<html>
<head>
  <%  if( modalita.equals("D") )  {%> 
  <title> Dettaglio PeriodoAltraMisura </title>
  <%} else if( modalita.equals("C") ) { %> 
  <title> Cancellazione PeriodoAltraMisura </title>
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
        <font class="campo">Dettaglio Inizio Misura Sicurezza</font>
      <%} else if( modalita.equals("C") ) { %> 
        <font class="campo">Cancellazione Inizio Misura Sicurezza</font>
      <%}%> 
      </td>
											   
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%= ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=PeriodoAltraMisura.getIdPeriodoAltraMisura()%>" />
          </jsp:include>
     </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
 

    </tr>
  </table>
</FORM>

<table>
	<tr>
		<jsp:include
			page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>" />
	</tr>
	<br>
	<tr>
		<td class="L"><font class="campo"> 
<%
 		if (fascicoloSiusGP.getTenori() != null) {
 			int lSize = fascicoloSiusGP.getTenori().length;
 			if (lSize == 0)
 %> -&nbsp; <%
 				for (int x = 0; x < lSize; x++) {
 %> 
 					<font class="label"> <%=fascicoloSiusGP.getTenori()[x].getDescrOggettoTenore()%> <%
 					if (fascicoloSiusGP.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
 %> 
 						</font> <font class="descr"> - <%=fascicoloSiusGP.getTenori()[x].getDescrDettaglioOggetto()%></font> <%
 					}
 				}
 		} else {
 %> -&nbsp; <%
 		}
 %> </font></td>
	</tr>
</table>
<table>
	<tr>
		<td class="l">Data Inizio Esecuzione</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
							PeriodoAltraMisura.getDataInizioEsecuzione(),
							"dd/MM/yyyy"))%></font></td>
	</tr>
	<tr>
		<td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
	</tr>
	<%
			if (istitutodetenzione != null) {
			if (istitutodetenzione.getIdIstitutoDetenzione().trim()
			.length() > 0) {
	%>
	<tr>
		<td class="l">Istituto Detenzione</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(istitutodetenzione
									.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione
									.getDescrComune())%> </font></td>
	</tr>
	<%
			}
		}
	%>

	<%
	if (!PeriodoAltraMisura.getCodTipoAutorita().equals("-")) {
	%>
	<tr>
		<td class="l">Autorità</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraMisura
								.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(PeriodoAltraMisura
								.getDescrLuogoAutorita())%> </font></td>
	</tr>
	<%
	}
	%>

	<tr>
		<td colspan=2>&nbsp;</td>
	</tr>

	<tr>
		<td class="l">Note</td>
		<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraMisura
							.getMotivazione())%> </font></td>
	</tr>
	<tr>
		<td class="l">Data Fine Pena</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
							PeriodoAltraMisura.getDataScadenza(),
							"dd/MM/yyyy"))%></font></td>
	</tr>
 
<%  if( modalita.equals("C") )  {%> 
  <tr>
    <td align="center">
      <input class="bottone" type="submit" name="conferma" value="Conferma"  onclick="Javascript: return Verify();">
    </td>
  </tr>
<%}%>

</table>

</body>
</html>