<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel" />
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<%-- 07/08/2014	MIS. SICUREZZA : Accertamento Pericolosità Sociale --%>
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="misureSicurezza" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="documentoAllegato" scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />

<html>
<head>
	<title>[S.I.E.S.] - Dettaglio Provvedimento</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src="/html/conferma.js"></script>
</head>
<body class="corpo">
	<FORM name="comandi">
		<table>
			<tr>
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
					</a>
				</td>
				<td class="LBG">
					<font class="label">Funzione :</font>&nbsp;
<%
String strTitle = "";
if (Messaggio.getCodTipoOperazione().compareTo(Messaggio.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0) {
	strTitle = "Dettaglio Richiesta Accertamento Pericolosità Sociale";
} else {
	strTitle = "Dettaglio Provvedimento Spedito";
}
%>
					<font class="campo"><%=strTitle%></font>
				</td>
<%
					// MEV 16: gestite casistiche per cui far vedere l'icona del FC
String[] lListaCodMotivi = ICostantiEvento.CODICI_SOSPENSIONE_DELLA_PENA;
List<String> lCodMotivi = Arrays.asList(lListaCodMotivi);
String lCodMotivo = evento.getEvento().getCodMotivo();
boolean isPresentCodMotivo = false;
if (lCodMotivo != null && lCodMotivo.length() > 0)
	isPresentCodMotivo = lCodMotivi.contains(lCodMotivo);
if (isPresentCodMotivo) {
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=evento.getEvento().getIdEvento()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0">
					</a>
				</td>
<%
}
%>

				<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
			</tr>
		</table>
	</FORM>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
	<br>
	<table cellspacing="2" cellpadding="2">
		<!----------- MESSAGGIO --------------------->
		<tr>
			<td class="Titolo" colspan=4>Oggetto del Provvedimento</td>
		</tr>
		<tr>
			<td class="l"><font class="label">Oggetto</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(evento.getEvento().getDescrMotivo())%></font></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="Titolo" colspan=4>Dati Messaggio</td>
		</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
   <td class="l"><font class="label">Id JMS</font></td>
   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getJmsIdMessaggio())%></font></td>
</tr>
--%>
		<tr>
			<td class="l"><font class="label">Stato Messaggio</font></td>
<%
if (Messaggio.getMessaggioCorrelato() == null) {
%>
			<td class="lRosso">In Attesa di risposta...</td>
<%
} else {
%>
			<td class="lVerde"><font class="campo">Risposta Ricevuta</font></td>
<%
}
%>
		</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
  <td class="l"><font class="label">Tipo Messaggio</font></td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoMessaggio())%></font></td>
</tr>
<tr>
  <td class="l"><font class="label">Tipo Operazione</font></td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getTipoOperazione())%></font></td>
</tr>
--%>
		<tr>
			<td class="l"><font class="label">Data Invio</font></td>
			<td class="l"><font class="campo"><%=DateUtils.getDateToString(Messaggio.getDataInvio(), "dd-MM-yyyy  HH:mm:ss")%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Utente Mittente</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getCodiceUtenteMittente())%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Ufficio Mittente</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente() + " "
					+ Messaggio.getDescrSedeUfficioMittente())%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Ufficio Destinatario</font></td>
<%
// MEV_66: gestita casistica che se destinatario è udsm allora cambio il nome
String descrTipoUfficio = Messaggio.getDescrUfficioDestinatario();
if ("UDSM".equals(Messaggio.getCodUfficioDestinatario()))
	descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
%>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(descrTipoUfficio + " " + Messaggio.getDescrSedeUfficioDestinatario())%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Data Ricezione</font></td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataEsito(), "dd-MM-yyyy HH:mm:ss"))%></font></td>
		</tr>
		<tr>
			<td class="l"><font class="label">Esito</font></td>
			<td class="l">
<%
if (Messaggio.getMessaggioCorrelato() != null) {
%>
				<font class="campo"><%=Messaggio.getMessaggioCorrelato().getDescrEsito()%></font>
<%
}
%>
			</td>
		</tr>
<%
//==============================================================================
// Se Richiesta Accertamento pericolosità sociale, visualizzo le Misure SIcurezza
//==============================================================================
if (Messaggio.getCodTipoOperazione().compareTo(Messaggio.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE) == 0
		&& misureSicurezza != null && misureSicurezza.size() > 0) {
%>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="Titolo" colspan=4>Misure di Sicurezza</td>
		</tr>
<%
	Iterator itxMS = misureSicurezza.iterator();
	while (itxMS.hasNext()) {
		MisuraSicurezzaModel lMisuraSicModel = (MisuraSicurezzaModel) itxMS.next();
%>
		<tr>
			<td class="L">Misura di Sicurezza da espiare</td>
			<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getDescrTipo(), "")%></font></td>
			<td class="l">
<%
		if (lMisuraSicModel.getNumAnni() != null && lMisuraSicModel.getNumAnni().intValue() > 0) {
%>
				Anni:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumAnni(), "0")%>&nbsp;</font>
<%
		}
 		if (lMisuraSicModel.getNumMesi() != null && lMisuraSicModel.getNumMesi().intValue() > 0) {
%>
				Mesi:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumMesi(), "0")%>&nbsp;</font>
<%
		}
 		if (lMisuraSicModel.getNumGiorni() != null && lMisuraSicModel.getNumGiorni().intValue() > 0) {
%>
				Giorni:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMisuraSicModel.getNumGiorni(), "0")%>&nbsp;</font>
<%
		}
%>
			</td>
		</tr>
<%
	}
}
%>
	</table>
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoProvvedimento">
		<table>
			<tr>
				<td>
	            	<!-- <input class=bottone  type="submit" value="Conferma Presa in Carico"> -->
					<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActConfermaPresaInCarico">
					<input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
					<input type="HIDDEN" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>" value="<%=documentoAllegato.getIdDocumentoAllegato()%>">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>