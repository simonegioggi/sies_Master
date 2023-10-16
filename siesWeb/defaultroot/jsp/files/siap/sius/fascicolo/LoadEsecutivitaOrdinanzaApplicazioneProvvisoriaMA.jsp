<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Date"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>

<jsp:useBean id="fascicoloSiusGP" 			scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoDepositoModel"		scope="request" class="siap.sico.evento.model.EventoDepositoModel"/>
<jsp:useBean id="maxDataAvvenutaNotifica"	scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEsecutivita"			scope="request" class="java.lang.String"/>
<jsp:useBean id="noteDataEsecutivita"		scope="request" class="java.lang.String"/>
<jsp:useBean id="provenienza"				scope="request" class="java.lang.String"/>

<jsp:useBean id="destDeposito" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="TipiIstitutiSog" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutorita"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="altreAutoritaGiudiziarie"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Aggiungi"         			scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"      			scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="luogodet"					scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="notificheSog"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="NotifichePresenti"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="notifiche" 				scope="request" class="java.util.Vector"/>

<%
/* Estrazione della data udienza o data iscrizione */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
else  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(), "dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");

Date dataEsecutivitaNew = null;
if (Utils.isPresent(dataEsecutivita))
	dataEsecutivitaNew = DateUtils.getDate(dataEsecutivita, "dd/MM/yyyy");

String action = "siap.sius.fascicolo.action.ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA";
%>

<html>
<head>
<title>[S.I.E.S.] - Load Esecutivita&#768; Ordinanza Applicazione Provvisoria M.A.</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>listeDestSIUS.js"></script>
<script language="JavaScript">
function Verifica() {
	var data_minima = '<%=data1%>';
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var data_deposito_ordinanza = '<%=DateUtils.getDateToString(eventoDepositoModel.getDataDeposito(), "dd/MM/yyyy")%>'
	var data_esecutivita = document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ESECUTIVITA%>.value+'/'+document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ESECUTIVITA%>.value+'/'+document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ESECUTIVITA%>.value;
    // Controllo della data di Esecutivita&#768;
    if (!ControllaData(data_esecutivita)) {
		alert('Data Esecutività non valida: '+ data_esecutivita);
		return false;
    }
    // Controllo data di sistema >= Data Esecutivita&#768;
    else if (!CompareDate(data_esecutivita, data_sistema)) {
		alert('Data Esecutività non può essere superiore alla data odierna!');
		return false;
    } else if (!CompareDate(data_minima, data_esecutivita)) {
		alert("Data Esecutività non può precedere: " + data_minima);
		return false;
   	} else if (!CompareDate(data_deposito_ordinanza, data_esecutivita)) { // Controllo Data Esecutivita&#768 >= data_deposito_ordinanza
		alert("Data Esecutività non può precedere la data di deposito ordinanza: " + data_deposito_ordinanza);
		return false;
   	}
    var max_data_avvenutaNotifica = '<%=maxDataAvvenutaNotifica%>';
    // Controllo che il valore inserito superi i 10 giorni rispetto alla data dell'ultima notifica
    if (ControllaData(max_data_avvenutaNotifica)) {
	    if (DifferenzaDateInGiorni(max_data_avvenutaNotifica, data_esecutivita) > 10) {
	    	var msgConfirm = "Attenzione! La Data di Esecutività (" + data_esecutivita + ") supera i 10 giorni rispetto alla data dell'ultima Notifica ("
	    			+ max_data_avvenutaNotifica + "). Si vuole procedere con l'inserimento dei dati?";
	        if (window.confirm(msgConfirm))
				return true;
	        else
	          	return false;
	    }
    }
    return true;
}
</script>
</head>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione : Esecutivita&#768; Ordinanza Applicazione Provvisoria M.A.</font></td>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA">
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="label" nowrap>Estremi Ordinanza (art. 678 comma 1-ter c.p.p.)</td>
	</tr>
	<tr>
	    <td class="int">Contenuto</td>
	    <td class="int" width="10%">Data Udienza</td>
	    <td class="int">Provvedimento</td>
	    <td class="int" width="10%">Data Emissione</td>
	    <td class="int">Oggetto Provvedimento</td>
	    <td class="int">Esito Provvedimento</td>
	</tr>
	<tr>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoDepositoModel.getDescrTipoProvvedimento(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoDepositoModel.getDataEmissione(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoDepositoModel.getDescrMotivo(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoDepositoModel.getDescrEsito(), "-")%></font></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="15%">Data Esecutivita&#768;&nbsp;<font class="ob">(*)</font></td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivitaNew, "dd"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivitaNew, "MM"), "")%>" type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivitaNew, "yyyy"), "")%>" type="text" size="4" maxlength="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillYear(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<textarea title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols="80" rows="5"><%=StringUtils.toStringJSP(noteDataEsecutivita, "")%></textarea>
	    </td>
 	 </tr>
</table>

<%-- DESTINATARI --%>
<%
if (provenienza.equals("modifica")) {
	// controllo se esistono notifiche
	if (!Utils.isNullObj(notifiche) && notifiche.size() > 0) {
%>
<jsp:include page="<%=ICostantiDepositoDecreto.PG_MODIFICA_DESTINATARI%>">
	<jsp:param name="NomeForm" value="LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA"/>
</jsp:include>
<%
	}
} else {
%>
<div id="elenco" style="visibility:visible; width:100%;">
<jsp:include page="<%=ICostantiUdienza.PG_LOAD_DESTINATARI%>"/>
<jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_DESTINATARI%>">
	<jsp:param name="NomeForm" value="LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA"/>
</jsp:include>
<jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_NOTIFICA_SOGGETTO%>">
 	<jsp:param name="NomeForm" value="LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA"/>
</jsp:include>
<jsp:include page="<%=ICostantiDepositoDecreto.PG_INSERIMENTO_NOTIFICA_AVVOCATI_ALTRO%>">
 	<jsp:param name="NomeForm" value="LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA"/>
</jsp:include>
</div>
<%
}
%>
<%-- FINE DESTINATARI --%>

<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
    	<td>
      		<input class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action%>">
<input type="HIDDEN" name="IdEvento" value="<%=eventoDepositoModel.getIdEvento()%>">
<input type="HIDDEN" name="provenienza" value="<%=provenienza%>">
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA");
// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verifica");
</script>
</body>
</html>