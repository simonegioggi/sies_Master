<%-- MEV_2023-33: aggiunta pagina --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="elencoStatoPagamenti" 	scope="request" class="java.util.Vector<BollettinoPagopaModel>"/>
<jsp:useBean id="TornaQui"    			scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"    			scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalitaPagamento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="importoPagato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="importoDaPagare" 		scope="request" class="java.lang.String"/>
<%-- MEV_2023-33: aggiunti useBean --%>
<jsp:useBean id="dataAvvenutaNotifica" 	scope="request" class="java.lang.String"/>
<%-- <jsp:useBean id="isSoloPrimaRata"		scope="request" class="java.lang.Boolean"/> --%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>
<script language="JavaScript">
var azione;
function eseguiAzione(tipoAzione, id) {
	if (tipoAzione == 'Stampa') {
		azione = "/jsp/Main.jsp?Action=siap.siep.pagoPA.action.ActLoadAvvisoPagoPA&idBollettinoPagopa=" + id;
		var hrefStampa = azione;
		var indice = hrefStampa.indexOf("?");
		var parametri = hrefStampa.substring(indice + 1, azione.length);
		stampa2("/jsp/files/Stampa.jsp", parametri);
	}
}

function tornaIndietro(action) {
	document.ElencoStatoPagamenti.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.ElencoStatoPagamenti.submit();
}
</script>
</head>
<body class="corpo">
<table>
    <tr>
      	<td class="LBG">
      		<a href="Javascript:window.print();">
      			<img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      		</a>
      	</td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Stato Bollettini per Pagamento Pena Pecuniaria</font>
      	</td>
      	<td class="LBG">
      	         <% if ("90".equals(UtenteConnesso.getUserProfile().getProfileId().toString())) { %>
          <a href="<%=IWebConstants.PG_MAIN%>?Action=siap.siep.pagoPA.action.ActVerificaErroriPagopa">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a> 
          <% } else { %>  
			<a href="javascript:tornaIndietro('siap.siep.sanzionesostitutiva.action.ActGrigliaBollettiniPagoPA')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
          <% } %>
		</td>
		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
       		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaMassivaBollettini"%>"/>
      	</jsp:include>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ElencoStatoPagamenti">

<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="hidden" name="<%=IWebConstants.LINK_RITORNO%>" value="">

<table cellspacing="0" cellpadding="0" width="95%">
<%
if (elencoStatoPagamenti.size() == 0) {
%>
	<tr>
      	<td>Nessun Pagamento Bollettini presente</td>
    </tr>
<%
} else {
%>
	<tr>
		<td class="l" colspan="9">
			<%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento())%>&nbsp;
			<%=StringUtils.toStringJSP(evento.getDescrMotivo())%>&nbsp;del&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy"))%></font>
			<%-- MEV_2023-33: aggiunta frase di notifica --%>
			&nbsp;notificato&nbsp;il:&nbsp;
			<font class="campo"><%=dataAvvenutaNotifica%></font>
<%
	if ("A".equals(evento.getFlagDocumentoRegistrato())) {
%>
			<font style="color:red"> (Annullato)</font>
<%
	}
%>
		</td>
	</tr>
	<tr>
		<td class="l" colspan="9">
			<%=modalitaPagamento%>
		</td>
	</tr>
	<tr>
		<td class="l" colspan="9">
			Importo Pagato:&nbsp;<font class="cVerde"><%=StringUtils.toEuroFormat(new BigDecimal(importoPagato))%></font>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Importo da Pagare:&nbsp;<font class="cRosso"><%=StringUtils.toEuroFormat(new BigDecimal(importoDaPagare))%></font>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="int">N.ro Ordine</td>
		<td class="int">Tipo Pagamento</td>
		<td class="int">IUV</td>
		<td class="int">Importo</td>
		<td class="int">Importo Pagato</td>
		<td class="int">Data Pagamento</td>
		<td class="int">Data Scadenza</td>
		<td class="int">Stato</td>
		<td class="int">Azioni</td>
	</tr>
<%
	Iterator<BollettinoPagopaModel> itx = elencoStatoPagamenti.iterator();
	while (itx.hasNext()) {
		BollettinoPagopaModel bpm = (BollettinoPagopaModel) itx.next();
// 		// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
// 		if ("U".equals(bpm.getTipoRateizzazione())
// 				|| "R".equals(bpm.getTipoRateizzazione())
// 				&& (isSoloPrimaRata && bpm.getProgRata() > 1)
// 				|| !isSoloPrimaRata) {
%>
	<tr>
		<td class="c"><%=StringUtils.toStringJSP(bpm.getProgRata())%></td>
<%
		if ("U".equals(bpm.getTipoRateizzazione())) {
%>
		<td class="l"><%=StringUtils.toStringJSP(bpm.getDescrTipoRateizzazione().toUpperCase())%></td>
<%
		} else {
%>
		<td class="l"><%=StringUtils.toStringJSP(bpm.getProgRata())%>&nbsp;RATA</td>
<%
		}
%>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getIuv(), "-")%></td>
      	<td class="c"><%=StringUtils.toEuroFormat(bpm.getImportoRata())%></td>
      	<td class="c"><%=StringUtils.toEuroFormat(bpm.getImportoPagato())%></td>
		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataAvvPagamento(), "dd/MM/yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataScadenza(), "dd/MM/yyyy"), "-")%></td>
<%
		String coloreClasse = "cVerde";
		if ("PN".equals(bpm.getStatoPagamento())) {
			coloreClasse = "cRosso";
		}
%>
      	<td class="<%=coloreClasse%>"><%=StringUtils.toStringJSP(bpm.getDescrStatoPagamento())%></td>
      	<td class="c">
        	<%--
        	<a href="javascript:eseguiAzione('Dettaglio', <%=bpm.getIdBollettinoPagopa()%>)"> 
				<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Bollettino" border="0">
          	</a>&nbsp;&nbsp;&nbsp;
          	--%>
<%
		// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
		if (!Utils.isNullObj(bpm.getIuv())) {
%>
      		<a href="javascript:eseguiAzione('Stampa', <%=bpm.getIdBollettinoPagopa()%>)">
				<img src="/images/print24.gif" alt="Stampa Bollettino" width="12" height="12" border="0">
			</a>
<%
		} else {
%>
			&nbsp;
<%
		}
%>
      	</td>
	</tr>
<%
// 		}
	} // end while su iterator sugli eventi
} // end else
%>
</table>
</FORM>
</body>
</html>