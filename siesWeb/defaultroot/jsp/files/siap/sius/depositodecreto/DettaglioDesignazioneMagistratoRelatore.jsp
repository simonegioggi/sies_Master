<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="depositoDecretoMotivazioni" 	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"    					scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistratorelatore"   			scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="Modificabile"              	scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto Designazione Magistrato Relatore</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function lookUpload() {
	var node;
  	node = document.getElementById('upld');
	node.style.visibility = 'visible';
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione:</font>&nbsp;
			<font class="campo">Dettaglio Decreto Designazione Magistrato Relatore</font>
		</td>
<%
if (depositoDecretoMotivazioni != null && depositoDecretoMotivazioni.getDepositoDecreto() != null
		&& depositoDecretoMotivazioni.getEvento() != null) {
   if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() == null
		   || depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
			<jsp:param name="ValoreIdEntita" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
		</jsp:include>
<%
		if (Modificabile.compareTo("SI") == 0) {
%>
		<td class="LBG">
		 	<a href="/jsp/Main.jsp?Action=siap.sius.depositodecreto.action.ActLoadModificaDesignazioneMagistratoRelatore&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
		  		<img  align="middle" src="/images/modifica24.gif" alt="Modifica Decreto" width="24" height="24" border="0">
		  	</a>
		</td>
<%
//    		}
// 		if (depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito() == null) {
%>
		<!-- BOTTONE DI CANCELLAZIONE -->
		<td class="LBG">
			<a href="Javascript:conferma('siap.sius.depositodecreto.action.ActCancellaDepositoDecreto','<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>','<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto()%>');">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="CancellaDM" width="24" height="24" border="0">
		  	</a>
		</td>
<%
		}
	}
}
%>
		<!-- BOTTONE DI RITORNO -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>
<table cellspacing="4" cellpadding="4" width="95%">
	<tr><jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/></tr>
  	<tr><td colspan="2">&nbsp;</td></tr>
	<tr><td class="Titolo" colspan="2">Oggetti</td></tr>
<%
Iterator<?> tenIter = tenori.iterator();
while (tenIter.hasNext()) {
%>
	<tr>
<%
	TenoreModel tm = (TenoreModel) tenIter.next();
%>
		<td class="l" colspan="2"><%=tm.getDescrOggettoTenore()%></td>
		<!-- ESITO = Designa Magistrato art. 678 1-ter -->
  	</tr>
<%
}
if (!Utils.isNullObj(magistratorelatore)
		&& !Utils.isNullObj(magistratorelatore.getMagistrato())) {
%>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td class="l">Magistrato Relatore</td>
    	<td class="l">
    		<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getNome())%>
    	</td>
	</tr>
<%
}
if (!Utils.isNullObj(depositoDecretoMotivazioni.getDepositoDecreto())) {
	if (!Utils.isNullObj(depositoDecretoMotivazioni.getDepositoDecreto().getDataTermineEmissione())) {
%>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td class="l">Data Termine</td>
    	<td class="l">
    		<font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataTermineEmissione(),"dd/MM/yyyy")%></font>
    	</td>
	</tr>
<%
	} else {
%>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td class="l">Numero Giorni Termine</td>
    	<td class="l">
    		<font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumGiorniTermineEmissione())%></font>
    	</td>
	</tr>
<%
	}
}
%>
</table>
<div align=left style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
    <tr>
      	<td class="L">
       		<input class="bottone" type="submit" value="Conferma">
       		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sius.depositodecreto.action.ActLoadDettaglioDesignazioneMagistratoRelatore">
       		<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
       		<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
      	</td>
	</tr>
</table>
</FORM>
</div>
</body>
</html>