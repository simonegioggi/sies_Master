<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-13_ aggiunta pagina di caricamento avviso PagoPA --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>

<jsp:useBean id="elencoStatoPagamenti" 	scope="request" class="java.util.Vector<BollettinoPagopaModel>"/>
<jsp:useBean id="evento"    			scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
function tornaIndietro(action) {
	document.LoadGeneraAvvisoPagoPA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.LoadGeneraAvvisoPagoPA.submit();
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
    		<font class="label">Funzione:</font>&nbsp;&nbsp;<font class="campo">Stato Bolletini per Pagamento Pena Pecuniaria</font>
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
%>
	<tr>
		<td class="l" colspan="6">
			Elenco Bollettini Generati da PagoPA per Richiesta del 
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataRichiesta(), "dd-MM-yyyy"))%> relativa a
			<%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento())%>&nbsp;
			<%=StringUtils.toStringJSP(evento.getDescrMotivo())%>&nbsp;del&nbsp;
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(), "dd-MM-yyyy"))%>
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
		</td>
	</tr>
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
%>
	<tr>
		<td class="c"><%=StringUtils.toStringJSP(bpm.getProgRata())%></td>
		<td class="l">
			<%="U".equals(bpm.getTipoRateizzazione()) ? bpm.getDescrTipoRateizzazione().toUpperCase() : "RATA " + StringUtils.toStringJSP(bpm.getProgRata())%>
		</td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getIuv(), "-")%></td>
      	<td class="c"><%=StringUtils.toEuroFormat(bpm.getImportoRata())%></td>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bpm.getDataScadenza(), "dd/MM/yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(bpm.getDescrStatoPagamento())%></td>
	</tr>
<%
	} // end while su iterator sugli eventi
} // end else
if (Utils.isNullObj(evento.getDataTrasmissioneAtti()) && Utils.isNullObj(evento.getDataRicezioneAtti())) {
%>
   	<tr>
		<td class="lNoBord">
       		<br><INPUT class="bottone" type="submit" name="S" value="Salva">
       	</td>
   	</tr>
<%
}
%>
</table>
</form>
</body>
</html>