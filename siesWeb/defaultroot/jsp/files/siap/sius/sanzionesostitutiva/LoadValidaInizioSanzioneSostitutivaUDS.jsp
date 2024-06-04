<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>

<jsp:useBean id="PeriodoAltraSanzione" 	scope="request" class="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"/>
<jsp:useBean id="istitutodetenzione" 	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="lESSModel" 			scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel"/>

<%
// MEV_35: recupero info sul fascicolo per oggetto procedimento
String codOggettoProcedimento = "", tipoSostituzione = "Sanzione";
GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
if (!Utils.isNullObj(fascicoloSiusGP.getGeneraleProcedimentoModel()))
	gpm = fascicoloSiusGP.getGeneraleProcedimentoModel();
if (Utils.isPresent(gpm.getCodOggettoProcedimento()))
	codOggettoProcedimento = gpm.getCodOggettoProcedimento();
if ("U134".equals(codOggettoProcedimento) || "U126".equals(codOggettoProcedimento))
	tipoSostituzione = "Pena";
%>

<html>
<head>
<title>[S.I.U.S.] - Valida Inizio <%=tipoSostituzione%> Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function Verify() {
	// Controllo della data scadenza
	var data_scadenza=document.LoadValidaInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+LoadValidaInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+LoadValidaInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA %>.value;
	if (! ControllaData(data_scadenza)) {
		alert('Data scadenza non valida');
		return false;
	}
	var data_sanzione = document.LoadValidaInizioSanzioneSostitutivaUDS.data_sanzione.value;
	if (CompareDate(data_scadenza,data_sanzione)) {
		alert('Data scadenza deve essere superiore alla data inizio');
		return false;
	}
	return true;
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a>
		</td>
		<td class=LBG><font class="label">Funzione : </font>&nbsp;
 			<font class="campo">Valida Inizio <%=tipoSostituzione%> Sostitutiva</font></td>
	</tr>
	<tr>
		<jsp:include
			page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="L"><font class="campo"> 
<%
 		if (fascicoloSiusGP.getTenori() != null) {
 			int lSize = fascicoloSiusGP.getTenori().length;
 			if (lSize == 0)
 %>
 			-&nbsp;
<%
			for (int x = 0; x < lSize; x++) {
 %> 
			<font class="label"> <%=fascicoloSiusGP.getTenori()[x].getDescrOggettoTenore()%>
<%
				if (fascicoloSiusGP.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
 %> 
			</font> <font class="descr"> - <%=fascicoloSiusGP.getTenori()[x].getDescrDettaglioOggetto()%></font>
<%
				}
			}
 		} else {
 %>
			-&nbsp; <%
 		}
 %>
			</font>
		</td>
	</tr>
</table>
<table>
	<tr>
		<td class="l">Data Inizio Esecuzione</td>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoAltraSanzione.getDataInizioEsecuzione(), "dd/MM/yyyy"))%></font>
		</td>
	</tr>
	<tr>
		<td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
	</tr>
<%
		if (istitutodetenzione != null) {
			if (istitutodetenzione.getIdIstitutoDetenzione().trim().length() > 0) {
%>
	<tr>
		<td class="l">Istituto Detenzione</td>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune())%></font>
		</td>
	</tr>
<%
			}
		}
		if (!PeriodoAltraSanzione.getCodTipoAutorita().equals("-")) {
%>
	<tr>
		<td class="l">Autorità</td>
		<td class="l">
			<font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getDescrLuogoAutorita())%></font>
		</td>
	</tr>
<%
		}
%>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L">
			<font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraSanzione.getMotivazione())%></font>
		</td>
	</tr>
</table>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadValidaInizioSanzioneSostitutivaUDS'>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.sanzionesostitutiva.action.ActValidaInizioSanzioneSostitutivaUDS" >
<br>
<table cellspacing=2 cellpadding=2>
	<tr>
      	<td class="l">Data Fine Pena <font class="ob">(*)</font></td>
     	<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataTermineAttuale(), "dd"))%>" type="text" size="2" maxlength="2"
					 name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>" <%=IWebConstants.UTIL_DATA%>>
			/ 
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataTermineAttuale(), "MM"))%>" type="text" size="2" maxlength="2"
					name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>" <%=IWebConstants.UTIL_DATA%>>
			/ 
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataTermineAttuale(), "yyyy"))%>" type="text" size="4" maxlength="4" 
					name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td>
			<input class="bottone" type="submit" value="Valida Scadenza">
			<input type="HIDDEN" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>" value="<%=PeriodoAltraSanzione.getIdPeriodoAltraSanzione()%>">
			<input type="HIDDEN" name="data_sanzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoAltraSanzione.getDataInizioEsecuzione(), "dd/MM/yyyy"))%>">
        </td>
	</tr>
</table>
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadValidaInizioSanzioneSostitutivaUDS");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>","req","Il campo Giorno della data scadenza è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>","numeric");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>","req","Il campo Mese della data scadenza è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>","numeric");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>","req","Il campo Anno della data scadenza è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>","minlen=4","La lunghezza del campo Anno della data scadenza deve essere di 4 caratteri");

// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>