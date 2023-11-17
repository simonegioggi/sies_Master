<%-- MEV_2023-13: aggiunta pagina --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="listaRichiestaBollettini" 	scope="request" class="java.util.Vector<EventoRateizzazionePPModel>"/>
<jsp:useBean id="modalitaPagamento" 		scope="request" class="java.util.ArrayList<String>"/>
<jsp:useBean id="TornaQui"    				scope="request" class="java.lang.String"/>
<%-- MEV_2023-33: aggiunti useBean --%>
<jsp:useBean id="isSoloPrimaRata"			scope="request" class="java.util.ArrayList<java.lang.Boolean>"/>
<jsp:useBean id="isRateale"					scope="request" class="java.lang.Boolean"/>
<jsp:useBean id="areRateGiaGenerate"		scope="request" class="java.util.ArrayList<java.lang.Boolean>"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
var azione;
function eseguiAzione(tipoAzione, id) {
	if (tipoAzione == 'Dettaglio') {
		azione = "siap.siep.sanzionesostitutiva.action.ActElencoStatoPagamenti";
		document.ListaRichiestaBollettini.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = id;
		document.ListaRichiestaBollettini.<%=IWebConstants.ACTION_FIELD%>.value = azione;
		document.ListaRichiestaBollettini.<%=IWebConstants.LINK_RITORNO%>.value = "<%=TornaQui%>";
		document.ListaRichiestaBollettini.submit();
	} else if (tipoAzione == 'Inoltra') {
		azione = "siap.siep.pagoPA.action.ActLoadGeneraAvvisoPagoPA";
		document.ListaRichiestaBollettini.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = id;
		document.ListaRichiestaBollettini.<%=IWebConstants.ACTION_FIELD%>.value = azione;
		document.ListaRichiestaBollettini.<%=IWebConstants.LINK_RITORNO%>.value = "<%=TornaQui%>";
		document.ListaRichiestaBollettini.submit();
	}
}

function tornaIndietro(action) {
	document.ListaRichiestaBollettini.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.ListaRichiestaBollettini.submit();
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
<%
// MEV_2023-33: aggiungo gestione numero dei Bollettini da generare
if (areRateGiaGenerate.get(areRateGiaGenerate.size()-1) || !isRateale) {
%>
			<font class="campo">Richiesta a PagoPA Generazione Bollettini Pagamento Pena Pecuniaria</font>
<%
} else {
	if (isRateale) {
		if (isSoloPrimaRata.get(isSoloPrimaRata.size()-1)) {
%>
    		<font class="campo">Richiesta a PagoPA Generazione Bollettini Pagamento Pena Pecuniaria Rimanenti Rate</font>
<%
		} else {
%>
			<font class="campo">Richiesta a PagoPA Generazione Primo Bollettino Pagamento Pena Pecuniaria</font>
<%
		}
	}
}
%>
      	</td>
      	<td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        	<a href="javascript:tornaIndietro('siap.siep.sanzionesostitutiva.action.ActGrigliaBollettiniPagoPA')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="ListaRichiestaBollettini">

<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="hidden" name="<%=IWebConstants.LINK_RITORNO%>" value="">
<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">

<table cellspacing="0" cellpadding="0" width="95%">
<%
if (listaRichiestaBollettini.size() == 0) {
%>
	<tr>
      	<td>Nessuna Richiesta Pagamento Bollettini presente</td>
    </tr>
<%
} else {
%>
	<tr>
		<td class="titolo" colspan="6">Richiesta Generazione Bollettini PagoPA relativi a:</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="int">Provvedimento</td>
		<td class="int">Modalit&agrave; Pagamento</td>
		<td class="int">Inoltra</td>
		<td class="int">Data Richiesta</td>
		<td class="int">Data Ricezione</td>
		<td class="int">Visualizza</td>
	</tr>
<%
	Iterator<EventoRateizzazionePPModel> itx = listaRichiestaBollettini.iterator();
	int cont = 0;
	final int size = listaRichiestaBollettini.size();
	while (itx.hasNext()) {
		EventoRateizzazionePPModel erppm = (EventoRateizzazionePPModel) itx.next();
		EventoModel em = erppm.getEvento();
		
		String annullato = "";
		if ("A".equals(em.getFlagDocumentoRegistrato()))
		  annullato = "<br><font style='color:red'>(Annullato)</font>";
%>
	<tr>
		<td class="c">
			<%=StringUtils.toStringJSP(em.getDescrTipoProvvedimento()) 
			+ " " + StringUtils.toStringJSP(em.getDescrMotivo())%>
			&nbsp;del&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataEmissione(), "dd/MM/yyyy"))%><%=annullato%>
		</td>
		<td class="l">
			<%=modalitaPagamento.get(cont)%>
		</td>
<%
// MEV_2023-33: posso inoltrare anche se il pagamento è rateale ed ho emesso solo la prima delle n rate
if (((Utils.isNullObj(em.getDataTrasmissioneAtti()) && Utils.isNullObj(em.getDataRicezioneAtti()))
		|| (isSoloPrimaRata.get(cont) && !areRateGiaGenerate.get(cont))) && cont == size-1) {
%>
      	<td class="c">
      	<% if ("S".equals(em.getFlagDocumentoRegistrato())) { %>
      		<a href="javascript:eseguiAzione('Inoltra', <%=em.getIdEvento()%>)">
				<img src="/images/esegui.gif" alt="Genera Avviso PagoPA" width="12" height="12" border="0">
        <% } else { %>	
        &nbsp;			
        <% } %>
			</a>
		</td>
<%
} else {
%>
		<td class="c">-</td>
<%
}
%>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataTrasmissioneAtti(), "dd/MM/yyyy"), "-")%></td>
      	<td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(em.getDataRicezioneAtti(), "dd/MM/yyyy"), "-")%></td>
      	<td class="c">
        	<a href="javascript:eseguiAzione('Dettaglio', <%=em.getIdEvento()%>)">
          		<img src="/images/dettagli.gif" width="12" height="12" alt="Verifica Stato Pagamenti" border="0">
          	</a>
      	</td>
	</tr>
<%
		cont++;
	} // end while su iterator sugli eventi
} // end else
%>
</table>
</FORM>
</body>
</html>