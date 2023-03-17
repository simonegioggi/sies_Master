<%-- MEV_2023-13: aggiunta pagina --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="elencoStatoPagamenti" 	scope="request" class="java.util.Vector<BollettinoPagopaModel>"/>
<jsp:useBean id="TornaQui"    			scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
var azione;
function eseguiAzione(tipoAzione, id) {
	if (tipoAzione == 'Dettaglio') {
		azione = "siap.siep.sanzionesostitutiva.action.ActVerificaStatoPagamenti";
		document.ElencoStatoPagamenti.<%=ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>.value = id;
		document.ElencoStatoPagamenti.<%=IWebConstants.ACTION_FIELD%>.value = azione;
		document.ElencoStatoPagamenti.<%=IWebConstants.LINK_RITORNO%>.value = "<%=TornaQui%>";
		document.ElencoStatoPagamenti.submit();
	} else if (tipoAzione == 'Stampa') {
		azione = "siap.siep.pagoPA.action.ActLoadGeneraAvvisoPagoPA";
		document.ElencoStatoPagamenti.<%=ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>.value = id;
		document.ElencoStatoPagamenti.<%=IWebConstants.ACTION_FIELD%>.value = azione;
		document.ElencoStatoPagamenti.<%=IWebConstants.LINK_RITORNO%>.value = "<%=TornaQui%>";
		document.ElencoStatoPagamenti.submit();
	}
}
</script>
</head>
<body class="corpo">
<table>
    <tr>
      	<td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Stato Bollettini per Pagamento Pena Pecuniaria</font>
      	</td>
      	<td class="LBG">
			<a href="javascript:history.back()">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ElencoStatoPagamenti">

<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="hidden" name="<%=IWebConstants.LINK_RITORNO%>" value="">
<input type="hidden" name="<%=ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="">

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
		<td class="titolo" colspan="6">Elenco Stato Pagamenti Bollettini PagoPA</td>
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
	int numeroOrdine = 1;
	while (itx.hasNext()) {
		BollettinoPagopaModel bpm = (BollettinoPagopaModel) itx.next();
%>
	<tr>
		<td class="c"><%="" + numeroOrdine%></td>
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
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getIuv())%></td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getImportoRata())%></td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getImportoPagato())%></td>
		<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataAvvPagamento(), "dd-MM-yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataScadenza(), "dd-MM-yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getDescrStatoPagamento())%></td>
      	<td class="c" style="text-align: center;">
        	<a href="javascript:eseguiAzione('Dettaglio', <%=bpm.getIdBollettinoPagopa()%>)">
          		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Bollettino" border="0">
          	</a>&nbsp;&nbsp;&nbsp;
      		<a href="javascript:eseguiAzione('Stampa', <%=bpm.getIdBollettinoPagopa()%>)">
				<img src="/images/print24.gif" alt="Stampa Bollettino" width="12" height="12" border="0">
			</a>
      	</td>
	</tr>
<%
		numeroOrdine += 1;
	} // end while su iterator sugli eventi
} // end else
%>
</table>
</FORM>
</body>
</html>