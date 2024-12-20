<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>

<jsp:useBean id="tipoAutorita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" 		scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="listaSanzioniSius" 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lESSModel" 			scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel"/>
<jsp:useBean id="istitutodetenzione" 	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<%
// MEV_2023-35: recupero info sul fascicolo per oggetto procedimento
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
<script language="JavaScript1.2"></script>
<title>[S.I.U.S.] - Inizio <%=tipoSostituzione%> Sostitutiva</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript">
var desktop;
function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaSanzioni() {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.sanzionesostitutiva.action.ActLoadListaSanzioniSostitutiveUDS&<%=ICostantiSanzioneSostitutiva.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
}

function Verify() {
	// Controllo della data pervenimento del verbale
  	var data_sanzione=document.LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE %>.value+'/'+LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>.value+'/'+LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE %>.value;
   	if (!ControllaData(data_sanzione)) {
		alert('Data inizio esecuzione non valida');
		return false;
   	}
	var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	if (!CompareDate(data_sanzione,data_sistema)) {
		alert('Data inizio esecuzione non può essere superiore alla data odierna');
		return false;
   	}
	var data_scadenza=document.LoadInserisciInizioSanzioneSostitutivaUDS.data_scadenza.value;
	if (CompareDate(data_sanzione,data_scadenza)) {
		alert('Data ripresa deve essere superiore alla data scadenza');
		return false;
   	}
	var trovata = 0;
   	// Controllo CAMPI INSERITI
   	if (LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value.length == 0
   			&& (LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>.value.length == 0
   					|| LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_AUTORITA%>.value.length <= 1)) {
		alert('Selezionare una autorità competente che ha inviato il verbale!');
		return false;
	} else {
		if (LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value.length != 0) {
			trovata++;
		}
		if (LoadInserisciInizioSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>.value.length != 0) {
			trovata++;
		}
	    if (trovata > 1) {
			alert('Selezionare una sola autorità competente che ha inviato il verbale!');
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
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"	alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class=LBG>
			<font class="label">Funzione : </font>&nbsp;<font class="campo">Inizio/Ripresa <%=tipoSostituzione%> Sostitutiva</font>
		</td>
	</tr>
</table>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"	name='LoadInserisciInizioSanzioneSostitutivaUDS'>
<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
<br>
<%
// fOperazione = 0 significa che è il primo inserimento
// fOperazione = 1 significa che è un inserimento successivo per cui imposto
// 				la data di inizio esecuzione = alla precedente data scadenza
// fOperazione = 2 significa che è un inserimento successivo, ma  il precedente non
//				è validato quindi ripropongo il precedente (data inizio esecuzione)
//				e vado in modifica
int fOperazione = 0; // 0 = inserimento; 1 = ripresa; 2 = modifica
if (listaSanzioniSius != null && !listaSanzioniSius.isEmpty()) {
	PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius.get(listaSanzioniSius.size() - 1);
	if (lPerMod.getDataScadenza() != null) {
		fOperazione = 1;
	} else {
		fOperazione = 2;
	}
	if (fOperazione == 1) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"	value="siap.sius.sanzionesostitutiva.action.ActInserisciInizioSanzioneSostitutivaUDS">
			<input type="HIDDEN" name="data_scadenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataScadenza(),"dd-MM-yyyy"), "-")%>">
<%
	}
	if (fOperazione == 2) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.sanzionesostitutiva.action.ActModificaInizioSanzioneSostitutivaUDS">
			<input type="HIDDEN" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>" value="<%=lPerMod.getIdPeriodoAltraSanzione()%>">
<%
		if (listaSanzioniSius.size() > 1) {
			PeriodoAltraSanzioneModel lPerMod1 = (PeriodoAltraSanzioneModel) listaSanzioniSius.get(listaSanzioniSius.size() - 2);
%>
			<input type="HIDDEN" name="data_scadenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod1.getDataScadenza(), "dd-MM-yyyy"), "-")%>">
<%
		} else {
%>
			<input type="HIDDEN" name="data_scadenza" value="">
<%
		}
	}
}
if (fOperazione == 0) {
%>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"	value="siap.sius.sanzionesostitutiva.action.ActInserisciInizioSanzioneSostitutivaUDS" >
			<input type="HIDDEN" name="data_scadenza" value="">
<%
}
%>
<table width="100%">
	<tr>
		<td class="L">
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
			-&nbsp;
<%
}
%>
		</td>
		<td class="L"> Quantum <%=tipoSostituzione%> Sostitutiva :
<%
if (lESSModel.getNumAnniSanzione() != null && lESSModel.getNumAnniSanzione().intValue() > 0) {
%>
			Anni : <font class="campo"> <%=lESSModel.getNumAnniSanzione()%> </font>
<%
}
if (lESSModel.getNumMesiSanzione() != null && lESSModel.getNumMesiSanzione().intValue() > 0) {
%>
			Mesi : <font class="campo"><%=lESSModel.getNumMesiSanzione()%> </font>
<%
}
if (lESSModel.getNumGiorniSanzione() != null && lESSModel.getNumGiorniSanzione().intValue() > 0) {
%>
			Giorni : <font class="campo"> <%=lESSModel.getNumGiorniSanzione()%> </font>
<%
}
%>
		</td>
	</tr>
</table>

<table width="100%">
	<tr>
		<td class="l">
			<a href="Javascript:ListaSanzioni();">Elenco Date Inizio <img src="/images/filefolder.gif" border="0"></a>
		</td>
	</tr>
	<tr>
<%
//	fOperazione = 0;
if (fOperazione == 0) {
%>
		<td class="l">Data inizio esecuzione <font class="ob">(*)</font></td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataInizioSanzione(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataInizioSanzione(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lESSModel.getDataInizioSanzione(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
			<input type="HIDDEN" value="01" name="<%=ICostantiSanzioneSostitutiva.CAMPO_FLAG_MOTIVO%>">
		</td>
<%
}
if (fOperazione == 1)  {
		PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius.get(listaSanzioniSius.size() - 1);
%>
		<td class="l">Data Ripresa esecuzione <font class="ob">(*)</font></td>
		<td class="L">
	        <input type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataScadenza(),"dd"), "-")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"	onBlur="javascript:value=FillDM(value)"> /
	        <input type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataScadenza(),"MM"), "-")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
	        <input type="text" size="4" maxlength="4" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataScadenza(),"yyyy"), "-")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	        <input type="HIDDEN" value="02"	name="<%=ICostantiSanzioneSostitutiva.CAMPO_FLAG_MOTIVO%>">
		</td>
<%
}
if (fOperazione == 2)  {
	PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius.get(listaSanzioniSius.size() - 1);
%>
		<td class="l">Data inizio esecuzione <font class="ob">(*)</font></td>
		<td class="L">
	        <input type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataInizioEsecuzione(),"dd"), "-")%>"	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"	onBlur="javascript:value=FillDM(value)"> /
	        <input type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataInizioEsecuzione(),"MM"), "-")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
	        <input type="text" size="4" maxlength="4" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPerMod.getDataInizioEsecuzione(),"yyyy"), "-")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td>
<%
	if (listaSanzioniSius.size() > 1) {
%>
			<input type="HIDDEN" value="02"	name="<%=ICostantiSanzioneSostitutiva.CAMPO_FLAG_MOTIVO%>">
<%
	} else {
%>
			<input type="HIDDEN" value="01"	name="<%=ICostantiSanzioneSostitutiva.CAMPO_FLAG_MOTIVO%>">
<%
	}
}
%>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
	</tr>
	<tr>
		<td colspan=2>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Istituto Detenzione</td>
		<td class="l">
			<input readonly Title="Istituto" name="Comune"  size=50
<%
if (istitutodetenzione != null && istitutodetenzione.getIdIstitutoDetenzione().trim().length() > 0) {
%>
				value="<%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune())%>"
<%
} else {
%>
				value=""
<%
}
%>
			><input type="hidden" Title="Istituto" size="50" name="<%=ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>"
<%
if (istitutodetenzione != null && istitutodetenzione.getIdIstitutoDetenzione().trim().length() > 0) {
%>
				value="<%=StringUtils.toStringJSP(istitutodetenzione.getIdIstitutoDetenzione())%>"
<%
} else {
%>
				value=""
<%
}
%>
			><a href="Javascript:ListaIstitutoDetenzione('LoadInserisciInizioSanzioneSostitutivaUDS','<%=ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr style="width: 100%;">
		<td class=l colspan="2">oppure indicare Tipo e Sede</td>
	</tr>

	<tr>
		<td class="l">Tipo Autorità</td>
		<td class="l">
			<select title="Destinatario" name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_AUTORITA%>">
				<%=tipoAutorita%>
			</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede Autorità</td>
		<td class="l">
			<input Title="Sede" name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>"
<%
PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
if (listaSanzioniSius != null && !listaSanzioniSius.isEmpty()) {
	lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius.get(listaSanzioniSius.size() - 1);
	if (fOperazione == 2  && !lPerMod.getDescrLuogoAutorita().equals("-")) {
%>
				value="<%=lPerMod.getDescrLuogoAutorita()%>"
<%
	} else {
%>
				value=""
<%
	}
}
%>
			type="text" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciInizioSanzioneSostitutivaUDS','<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
	<tr>
		<td class="l">Motivazione</td>
<%-- 13/05/2008 Annullata l'obbligatorietà del campo Motivazione
<%
if (listaSanzioniSius != null && !listaSanzioniSius.isEmpty() && listaSanzioniSius.size() > 1) {
%>
			<font class="ob">(*)</font>
<%
}
%>
--%>
<%
String note = "";
if (fOperazione == 2 && lPerMod.getMotivazione() != null) {
	note = lPerMod.getMotivazione();
}
%>
		<td class="L">
			<TEXTAREA title="Motivazione" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MOTIVAZIONE%>" cols="40" rows="5"><%=note%></textarea>
		</td>
	</tr>
</table>
<br>
<br>
<table cellspacing=2 cellpadding=2>
	<tr>
		<td>
			<input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>">
		</td>
	</tr>
</table>
</FORM>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciInizioSanzioneSostitutivaUDS");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>","req","Il campo Giorno della data inizio esecuzione è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>","req","Il campo Mese della data inizio esecuzione è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","req","Il campo Anno della data inizio esecuzione è obbligatoria");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","minlen=4","La lunghezza del campo Anno della data inizio esecuzione deve essere di 4 caratteri");

var data_scadenza=document.LoadInserisciInizioSanzioneSostitutivaUDS.data_scadenza.value;

// 13/05/2008 Annullata l'obbligatorietà del campo Motivazione.
<%--
if (!data_scadenza == "") {
	frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MOTIVAZIONE%>","req","Il campo motivazione della ripresa esecuzione è obbligatorio");
}
--%>

// Chiama la funzione di Verify()
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>