<%-- MEV_2023-13: aggiunta pagina --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:useBean id="listaRichiestaBollettini" scope="request" class="java.util.Vector<EventoRateizzazionePPModel>"/>

<html>
<head>
<title>Richiesta Bollettini PagoPA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
var azione;
function eseguiAzione(tipoAzione, idEvento) {
	if (tipoAzione == 'Dettaglio') {
		azione = "siap.sico.evento.action.ActLoadDettaglioEvento";
		document.ListaRichiestaBollettini.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = idEvento;
		document.ListaRichiestaBollettini.<%=IWebConstants.ACTION_FIELD%>.value = azione;
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
      	<td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Elenco Pagamenti Pena Pecuniaria &nbsp;</font>
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
		<td class="int">Provvedimento</td>
		<td class="int">Modalit&agrave; Pagamento</td>
		<td class="int">Inoltra</td>
		<td class="int">Data Richiesta</td>
		<td class="int">Data Ricezione</td>
		<td class="int">Visualizza</td>
	</tr>
<%
	Iterator<EventoRateizzazionePPModel> itx = listaRichiestaBollettini.iterator();
	while (itx.hasNext()) {
		EventoRateizzazionePPModel erppm = (EventoRateizzazionePPModel) itx.next();
%>
	<tr>
		<td class="c">
			<%=StringUtils.toStringJSP(erppm.getEvento().getDescrTipoProvvedimento()) + " " + StringUtils.toStringJSP(erppm.getEvento().getDescrMotivo())%>
			&nbsp;del&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(erppm.getEvento().getDataEmissione(), "dd-MM-yyyy"))%>
		</td>
		<td class="c">&nbsp;</td>
      	<td class="c">&nbsp;</td>
      	<td class="c">&nbsp;</td>
      	<td class="c">&nbsp;</td>
      	<td class="c" style="text-align: center;"> &nbsp;
        	<a href="javascript:eseguiAzione('Dettaglio', <%=erppm.getEvento().getIdEvento()%>)">
          		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Evento" border="0">
          	</a>
      	</td>
	</tr>
<%
	} // end while su iterator sugli eventi
} // end else
%>
</table>
</FORM>
</body>
</html>