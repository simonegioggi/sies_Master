<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: nuova pagina di dettaglio rinvio esec MS --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori"                     scope="request" class="java.util.Vector"/>
<jsp:useBean id="prescrizioni"               scope="request" class="java.util.Vector"/>
<jsp:useBean id="AutoTemplate"               scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoLibertatis"            scope="request" class="java.util.Vector"/>

<%
UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficioTrib = "";
String labelUfficioSorv = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
	labelUfficioTrib = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
	labelUfficioSorv = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
} else {
	labelUfficioTrib = "Tribunale di Sorveglianza";
	labelUfficioSorv = "Ufficio di Sorveglianza";
}
%>
<html>
	<head>
		<title>[S.I.E.S.] - Dettaglio Decreto Rinvio MS</title>
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
      			<td class="LBG"><font class="label">Funzione: </font>
        			<font class="campo">Dettaglio Decreto Rinvio Esecuzione Misura Sicurezza</font>
      			</td>
      			<jsp:include page="<%=ICostantiDepositoDecreto.BOTTONI_DETTAGLIO_DECRETO%>"/>
    		</tr>
  		</table>
    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	<jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
    	<jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>"/>
  		<table cellspacing="4" cellpadding="4" width="95%">
    		<input Title="Id Evento" type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
    		<jsp:include page="<%=ICostantiDepositoDecreto.PG_DETTAGLIO_DATA%>"/>
    		<tr>
      			<td class="l">Eventuale motivazione</td>
      			<td class="l">
      				<font class="campo"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNote(),"-")%></font>
      			</td>
    		</tr>
<%
if (depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS() != null) {
%>
        	<tr>
<%
			// MEV_39: modifica etichetta per U082 e U077
          	if (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto() != null && 
          		(depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().equals("42") || depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().equals("MS")) ) {
%>
          		<td class="l">Data Decorrenza Differimento Esecuzione</td>
<%
          	} else {
%>
				<td class="l">Data rinvio</td>
<%
          	}
%>
          		<td class="l">
          			<font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS(),"dd/MM/yyyy")%></font>
          		</td>
        	</tr>
<%
}
if (depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS() != null) { %>
        	<tr>
          		<td class="l">Rinvio fino al </td>
          		<td class="l">
					<font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS(),"dd/MM/yyyy")%></font>
          		</td>
        	</tr>
<%
}
if ((depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS() != null && depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS().intValue() > 0)
		|| (depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS() != null && depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS().intValue() > 0)
		|| (depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS() != null && depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS().intValue() > 0)) {
%>
			<tr>
          		<td class="l">Rinvio nella misura di</td>
          		<td class="l">
          			<font class="campo">
          				Anni <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS())%>&nbsp;
          				Mesi <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS())%>&nbsp;
          				Giorni <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS())%>
          			</font>
          		</td>
        	</tr>
<%
}
if (depositoDecretoMotivazioni.getDepositoDecreto().getLuogoSvolgimentoProva() != null) {
%>
			<tr>
				<td class="l">Luogo Ricovero</td>
				<td class="l">
					<font class="campo"><%=StringUtils.cStrForJS(depositoDecretoMotivazioni.getDepositoDecreto().getLuogoSvolgimentoProva())%></font>
          		</td>
			</tr>
<%
}
%>
    		<tr>
      			<td class="l"><%=labelUfficioTrib%> Competente</td>
      			<td class="l">
      				<font class="campo"><%=StringUtils.cStrForJS(depositoDecretoMotivazioni.getDepositoDecreto().getDescrTdsComp())%></font>
				</td>
    		</tr>
    		<tr>
      			<td class="l"><%=labelUfficioSorv%> Competente</td>
      			<td class="l">
      				<font class="campo"><%=StringUtils.cStrForJS(depositoDecretoMotivazioni.getDepositoDecreto().getDescrUfficioCompetente())%></font>
      			</td>
    		</tr>
    		<tr>
      			<td colspan="2">&nbsp;</td>
    		</tr>
    		<tr>
      			<td class="Titolo" colspan="2">Esiti</td>
    		</tr>
		</table>
		<table cellspacing="4" cellpadding="4" width="95%">
<%
Iterator lInd = tenori.iterator();
while (lInd.hasNext()) {
%>
   			<tr>
<%
	TenoreModel lTen = (TenoreModel) lInd.next();
%>
      			<td class="l" width="70%"><%=lTen.getDescrOggettoTenore()%></td>
      			<td class="l"><%=lTen.getDescrEsitoTenore()%></td>
  			</tr>
<%
}
%>
  			<tr>
    			<td colspan="2">&nbsp;</td>
 			</tr>
  		</table>
  		<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
   			<jsp:param name="EveIdEvento" value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>"/>
   			<jsp:param name="nextaction" value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito"/>
  		</jsp:include>
  		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
	</form>
 	<div align="left" style="visibility:hidden" id="upld">
  		<FORM name="comandi" enctype="multipart/form-data" method="post">
  		<table>
			<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
    		<tr>
      			<td class="L">
					<input class="bottone"  type="submit" value="Conferma">
					<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
					<input type="HIDDEN" name="IdEvento"  value="<%=depositoDecretoMotivazioni.getEvento().getIdEvento()%>">
					<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.depositodecreto.action.ActLoadDettaglioDecretoDeposito">
					<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.EMISSIONE_DECRETO%>">
      			</td>
    		</tr>
  		</table>
  		</FORM>
  	</div>
	</body>
</html>