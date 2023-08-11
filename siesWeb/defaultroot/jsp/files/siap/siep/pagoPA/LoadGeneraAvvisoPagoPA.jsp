<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento avviso PagoPA --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.pagoPA.action.ICostantiPagoPA"%>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>

<jsp:useBean id="elencoStatoPagamenti" 		scope="request" class="java.util.Vector<BollettinoPagopaModel>"/>
<jsp:useBean id="evento"    				scope="request" class="siap.sico.evento.model.EventoModel"/>
<%-- MEV_2023-33: aggiunti useBean --%>
<jsp:useBean id="isRateale"					scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="isSoloPrimaRata"			scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="dataNotificaCondannato" 	scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
function tornaIndietro(action) {
	document.LoadGeneraAvvisoPagoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.LoadGeneraAvvisoPagoPA.submit();
}

<%-- MEV_2023-33: aggiunta funzione di controllo --%>
<%
if (isRateale) {
%>
function Verify() {
<%
	if (isSoloPrimaRata) {
%>
	if ("<%=dataNotificaCondannato%>" == "") {
		alert("Attenzione! Non è possibile procedere per l'assenza della Data Avvenuta Notifica al Condannato dell'Ordine di Ingiunzione!");
		return false;
	}
<%
	}
%>
	if (document.LoadGeneraAvvisoPagoPA.<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>[2].checked	&& <%=!isSoloPrimaRata%>) {
		alert('Attenzione! Non è possibile procedere per la mancata generazione PagoPA del bollettino relativo alla prima rata!');
		return false;
	} else if ((document.LoadGeneraAvvisoPagoPA.<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>[0].checked
			||document.LoadGeneraAvvisoPagoPA.<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>[1].checked)
			&& <%=isSoloPrimaRata%>) {
		alert('Attenzione! Non è possibile procedere poiché è stata già richiesta la generazione PagoPA del bollettino relativo alla prima rata!');
		return false;
	}
}
<%
}
%>
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
    		<font class="label">Funzione:</font>&nbsp;&nbsp;
<%
// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
if (isRateale) {
	if (isSoloPrimaRata) {
%>
    		<font class="campo">Richiesta a PagoPA Generazione Bollettini Pagamento Pena Pecuniaria Rimanenti Rate</font>
<%
	} else {
%>
			<font class="campo">Richiesta a PagoPA Generazione Bollettini Pagamento Pena Pecuniaria</font>
<%
	}
} else {
%>
			<font class="campo">Richiesta a PagoPA Generazione Primo Bollettino Pagamento Pena Pecuniaria</font>
<%
}
%>
    	</td>
    	<td class="LBG"><!-- Tasto indietro -->
        	<a href="javascript:tornaIndietro('siap.siep.sanzionesostitutiva.action.ActRichiestaBollettiniPagoPA')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<FORM method="POST" name="LoadGeneraAvvisoPagoPA" action="<%= IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPA.action.ActDownloadAvvisoPagoPA">
<input type="HIDDEN" name="tipoFascicolo" value="SIEP">

<table cellspacing="0" cellpadding="0" width="95%">
<%
if (elencoStatoPagamenti.size() == 0) {
%>
	<tr>
      	<td>Nessuna Richiesta Pagamento Bollettini presente</td>
    </tr>
<%
} else {
	// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
	final String testoRateSuccessive = ", per le rate successive alla prima";
%>
	<tr>
		<td class="l" colspan="6">
			Elenco Bollettini da richiedere a PagoPA, relativi all'<%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento())%>&nbsp;
			<%=StringUtils.toStringJSP(evento.getDescrMotivo())%>&nbsp;del&nbsp;
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy"))%>
<%
	if (isRateale && isSoloPrimaRata) {
%>
			<%=testoRateSuccessive%>
<%
	}
%>
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
		</td>
	</tr>
<%
			// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
			if (isRateale && !isSoloPrimaRata) {
%>
	<tr>
		<td class="l" colspan="6">
			Numero Bollettini da generare:&nbsp;Tutti&nbsp;<input type="radio" name="<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>" value="T" checked>
			&nbsp;&nbsp;&nbsp;Solo Bollettino Prima Rata&nbsp;<input type="radio" name="<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>" value="P">
			&nbsp;&nbsp;&nbsp;Solo Bollettini Rate Successive alla Prima&nbsp;<input type="radio" name="<%=ICostantiPagoPA.RADIO_NUMERO_BOLLETTINI%>" value="S">
		</td>
	</tr>
<%
			}
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="int">N.ro Ordine</td>
		<td class="int">Tipo Pagamento</td>
		<td class="int">IUV</td>
		<td class="int">Importo</td>
		<td class="int">Data Scadenza</td>
		<td class="int">Stato</td>
	</tr>
<%
	Iterator<BollettinoPagopaModel> itx = elencoStatoPagamenti.iterator();
	while (itx.hasNext()) {
		BollettinoPagopaModel bpm = (BollettinoPagopaModel) itx.next();
		// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
		if (!(isRateale && isSoloPrimaRata && bpm.getProgRata() == 1)) {
%>
	<tr>
		<td class="c"><%=StringUtils.toStringJSP(bpm.getProgRata())%></td>
		<td class="c">
			<%="U".equals(bpm.getTipoRateizzazione()) ? bpm.getDescrTipoRateizzazione().toUpperCase() : "RATA " + StringUtils.toStringJSP(bpm.getProgRata())%>
		</td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getIuv(), "-")%></td>
      	<td class="c"><%=StringUtils.toEuroFormat(bpm.getImportoRata())%></td>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataScadenza(), "dd/MM/yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getDescrStatoPagamento())%></td>
	</tr>
<%
		}
	} // end while iterator bollettini
} // end else
// MEV_2023-33 aggiunta or condition
if ((Utils.isNullObj(evento.getDataTrasmissioneAtti()) && Utils.isNullObj(evento.getDataRicezioneAtti())) || isSoloPrimaRata) {
%>
   	<tr>
		<td class="lNoBord">
       		<br><INPUT class="bottone" type="submit" name="S" value="Salva" onClick="javascript:return Verify();">
       	</td>
   	</tr>
<%
}
%>
</table>
</form>
</body>
</html>