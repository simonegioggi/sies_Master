<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="misuraAlternativa"      	 scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="prescrizioni"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"               scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoLibertatis"            scope="request" class="java.util.Vector"/>

<html>
<head>
<title>[S.I.E.S.] - Modifica Decreto</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>
</head>

<body class="corpo">
<form name="dettaglio">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione : </font>
			<font class="campo">Modifica Decreto</font>
		</td>
		<!-- BOTTONE DI RITORNO -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>

<table cellspacing="4" cellpadding="4" width="95%">
  	<tr>
  		<td>
    		<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
    	</td>
	</tr>
 	<tr>
        <jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
  	</tr>
</table>

<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
	<jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
	<jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito"/>
</jsp:include>
  
<!-- MEV63 -->
<%
if (misuraAlternativa != null && misuraAlternativa.getFlFormaMisura() != null) {
   	String labelFormaMisura = "&nbsp;";
   	if (misuraAlternativa.getFlFormaMisura() != null) {
   		labelFormaMisura = "Permanenza in casa";
   		if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal(2)) == 0) {
   			labelFormaMisura = "Collocamento in comunità";
   		}
   	}
%>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr>
	    <td class="l">La misura deve essere eseguita nelle forme della:</td>
	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(labelFormaMisura)%></font></td>
	</tr>
<%
if (misuraAlternativa.getFlFormaMisura().compareTo(new BigDecimal (2)) == 0 && misuraAlternativa.getDescrizioneComunita() != null) {
%>
	<tr>
	 	<td class="l">Comunit&agrave;:</td> 
	 	<td class="l">
	 		<font class="campo"><%=(misuraAlternativa.getDescrizioneComunita() == null ? "" : StringUtils.toStringJSP(misuraAlternativa.getDescrizioneComunita()))%></font>
	 	</td>
	</tr>
<%
}
%>
</table>
<%
}
%>
</form>
	
<jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_MODIFICA_ORDINANZA%>">
	<jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
	<jsp:param name="tipo_provvedimento" value="decreto"/>
</jsp:include>
</body>
</html>