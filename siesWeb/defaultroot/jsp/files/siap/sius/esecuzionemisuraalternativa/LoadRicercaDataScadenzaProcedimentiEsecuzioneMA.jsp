<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2025-48: aggiunta pagina per Scadenzario monitoraggio misure alternative espiate --%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>

<html>
<head>
<title> [S.I.E.S.] - Ricerca Data Scadenza Procedimenti Esecuzione M.A.</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
function init() {
	document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_ANNO_INI%>.focus();
}
function Verify() {																																																	
	var data_to_verify =
	  	document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>.value + '/' +
		document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>.value + '/' +
		document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
	 	alert('Data di Iscrizione iniziale non valida!');
	 	document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>.focus();
	 	return false;
	}
	var data_to_verify =
		document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>.value + '/' +
		document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>.value + '/' +
		document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
    	alert('Data di Iscrizione finale non valida!');
    	document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>.focus();
    	return false;
  	}
	if (document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.tipo[1].checked) {
		if (document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>.value == ""
				&& document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>.value == ""
				&& document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>.value == "") {
			alert("Inserire Periodo Procedimenti in Scadenza entro!");
			document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>.focus();
			return false;
		}
	}
    if (!(document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.tipo[0].checked
    		|| document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.tipo[1].checked
    		|| document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.tipo[2].checked
    		|| document.LoadRicercaDataScadenzaProcedimentiEsecuzioneMA.tipo[3].checked)) {
		alert("E' obbligatorio selezionare almeno un criterio di ricerca");
		return false;
    }
	return true;
}
</script>
</head>
<body class="corpo" onLoad="javascript:init()">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaDataScadenzaProcedimentiEsecuzioneMA">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionemisuraalternativa.action.ActRicercaDataScadenzaProcEsecMA">
<table>
 	<tr>
 		<td class="LBG">
 			<a href="Javascript:window.print();">
 				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
 			</a>
 		</td>
   		<td class="LBG">
	   		<font class="label">Funzione :</font> 
	   		<font class="campo">Ricerca Data Scadenza Procedimenti Esecuzione M.A.</font>
   		</td>
 	</tr>
</table>
<br>
<table width="95%">
	<tr><td class="Titolo" >Intervallo Estremi Procedimenti</td></tr>
	<tr>
       	<td class="c" width="61%">     
	   		Anno/Numero Iniziale 
       		<input Title="Anno Iniziale" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			<input Title="Numero Iniziale" type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_INI%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
			&nbsp;&nbsp; Anno/Numero Finale &nbsp;&nbsp;
			<input Title="Anno Finale" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			<input Title="Numero Finale" type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_FINE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
  	</tr>
</table>
<br>
<table width="95%">
	<tr><td class="Titolo" >Intervallo Date Iscrizione</td></tr>
	<tr>
		<td class="c" width="61%">
      		Data Iscrizione Iniziale      
      		<input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
			<input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
			<input Title="dalla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
			&nbsp;&nbsp; Data Iscrizione Finale  &nbsp;&nbsp;
			<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
			<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
			<input Title="alla Data" type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<br>
<table width="95%">
 	<tr><td class="Titolo" colspan="3">Criteri Ricerca</td></tr>
	<tr>
		<td class="l" width="15%">Tutti</td>
		<td class="l" width="%" colspan="2"><input type="radio" name="tipo" value="tutti"></td>
	</tr>
	<tr>
		<td class="l">In scadenza</td>
		<td class="l"><input type="radio" name="tipo" value="intervallo" ></td>
		<td class="L">
			entro:  Anni
	    	<input title="Anni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
			Mesi
			<input title="Mesi" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
			Giorni
			<input title="Giorni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">In scadenza Oggi</td>
		<td class="l" colspan="2"><input type="radio" name="tipo" value="oggi" ></td>
	</tr>
	<tr>
		<td class="l">Scaduti</td>
		<td class="l" colspan="2"><input type="radio" name="tipo" value="scaduti" ></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2">
    <tr><td><INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca"></td></tr>
</table>
<br>
<table cellspacing="2" cellpadding="2">
	<tr><td class="lVerdeNB">N.B.: Le date inizio e fine misura sono estratte dal procedimento SIEP collegato</td></tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadRicercaDataScadenzaProcedimentiEsecuzioneMA");

frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>", "numeric");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>