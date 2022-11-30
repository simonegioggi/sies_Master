<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>

<jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="EventoModel"		scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
/* Estrazione della data udienza o data iscrizione */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
String action = "siap.sius.fascicolo.action.ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA";
%>

<html>
<head>
<title>[S.I.E.S.] - Load Esecutivita' Ordinanza Applicazione Provvisoria M.A.</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
var desktop;
function  Verifica() {
	var ritorno = true;
	var data_minima = '<%=data1%>';
	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var data_esecutivita = document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ESECUTIVITA%>.value+'/'+document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ESECUTIVITA%>.value+'/'+document.LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ESECUTIVITA%>.value;
    // Controllo della data di Esecutivita'
    if (ritorno && (!ControllaData(data_esecutivita))) {
		alert('Data Esecutività non valida: '+ data_esecutivita );
		return false;
    }
    // Controllo data di sistema >= Data Esecutivita'
    else if (!CompareDate(data_esecutivita, data_sistema)) {
		alert('Data Esecutività non può essere superiore alla data odierna!');
		ritorno =  false;
    } else if (!CompareDate(data_minima, data_esecutivita)) {
		alert("Data Esecutività non può precedere: " + data_minima);
		ritorno =  false;
   	}
  	return ritorno;
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
		<td class="label">Estremi Ordinanza (art. 678 comma 1-ter c.p.p.)</td>
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
		<td class="c"><font class="label"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(EventoModel.getDescrTipoProvvedimento(),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(EventoModel.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(EventoModel.getDescrMotivo(),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(EventoModel.getDescrEsito(),"-")%></font></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="15%">Data Esecutivita&#768;<font class="ob">(*)</font></td>
		<td class="L">
			<input type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input type="text" size="2" maxlength="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillDM(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> /
			<input type="text" size="4" maxlength="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ESECUTIVITA%>" onBlur="javascript:value=FillYear(value)" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<textarea title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols="80" rows="5"></textarea>
	    </td>
 	 </tr>
	<tr>
    	<td>
      		<input class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action%>">
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadEsecutivitaOrdinanzaApplicazioneProvvisoriaMA");
// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verifica");
</script>
</body>
</html>