<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- @since MEV_2026-1 --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.scadenzario.model.ScadenzarioSiusModel"%>
<%@ page import="siap.sius.scadenzario.action.ICostantiScadenzarioSius"%>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<html>
<head>
<title> [S.I.E.S.] - Scadenzario - Ricerca Fine Pena Procedimenti Pendenti </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function verifica() {
  	var data_iniziale = document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>.value + '/' +
	  	document.f.<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_INIZIALE%>.value + '/' +
	  	document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIALE%>.value;
	if (!ControllaData(data_iniziale) && data_iniziale.length > 2) {
		alert('Attenzione! Data di Iscrizione Iniziale non valida!');
    	return false;
  	}

  	var data_finale = document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>.value + '/' +
	  	document.f.<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_FINALE%>.value + '/' +
	  	document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_FINALE%>.value;
  
	if (!ControllaData(data_finale) && data_finale.length > 2) {
    	alert('Attenzione! Data di Iscrizione Finale non valida!');
    	return false;
  	}
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

	var annoIniziale = document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_INIZIALE%>.value;
	var annoFinale = document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.value;
	var numeroIniziale = document.f.<%=ICostantiScadenzarioSius.CAMPO_NUM_INIZIALE%>.value;
	var numeroFinale = document.f.<%=ICostantiScadenzarioSius.CAMPO_NUM_FINALE%>.value;

// 	if ((data_iniziale == "//" && data_finale == "//" && annoIniziale.length == 0 && numeroIniziale.length == 0
// 			&& annoFinale.length == 0 && numeroFinale.length == 0)
// 		|| (data_iniziale != "//" && data_finale != "//" && annoIniziale.length != 0 && numeroIniziale.length != 0
// 				&& annoFinale.length != 0 && numeroFinale.length != 0)) {
// 		alert('Valorizzare obbligatoriamente l\'Intervallo Estremi Procedimenti oppure l\'Intervallo Date Iscrizione!');
<%-- 		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_INIZIALE%>.focus(); --%>
// 	   	return false;
// 	}
	if (annoIniziale == '' && annoFinale != '') {
		alert("Se indicato l'anno finale va indicato anche l'anno iniziale");
	 	document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
		return false; 
	} 
	if (annoIniziale != '' && annoFinale != '' && annoFinale < annoIniziale){
		alert("L'anno finale non puo' essere inferiore all'anno iniziale");
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
		return false;
	}
	// se solo uno dei due campi Anno/Numero Iniziale e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length != 0) {
		alert('Valorizzare l\'Anno Iniziale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_INIZIALE%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length == 0) {
		alert('Valorizzare il Numero Iniziale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_NUM_INIZIALE%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Finale e' valorizzato
	if (annoFinale.length == 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno Finale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
	   	return false;
	}
	if (annoFinale.length != 0 && numeroFinale.length == 0) {
		alert('Valorizzare il Numero Finale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Estremi procedimenti e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length == 0 && annoFinale.length != 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno/Numero Iniziale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_INIZIALE%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length != 0 && annoFinale.length == 0 && numeroFinale.length == 0) {
		alert('Valorizzare l\'Anno/Numero Finale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
	   	return false;
	}
	// controllo sugli anni/numeri
	if (parseInt(annoIniziale) > parseInt(annoFinale)) {
		alert('Anno Finale minore dell\'Anno Iniziale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
     	return false;
   	}
  	if ((parseInt(annoIniziale) == parseInt(annoFinale))
  			&& (parseInt(numeroIniziale) > parseInt(numeroFinale))) {
     	alert('Numero Finale minore del Numero Iniziale!');
     	document.f.<%=ICostantiScadenzarioSius.CAMPO_NUM_FINALE%>.focus();
     	return false;
   	}
  	var annoSistema = <%=DateUtils.getSysDate("yyyy")%>;
  	if (parseInt(annoFinale) > annoSistema) {
     	alert('Anno Finale maggiore dell\'Anno Corrente.');
     	document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>.focus();
     	return false;
   	}

	// se solo una delle due date e' valorizzata
	if (data_iniziale == "//" && data_finale != "//") {
		alert('Valorizzare la Data Iscrizione Iniziale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>.focus();
	   	return false;
	}
	if (data_iniziale != "//" && data_finale == "//") {
		alert('Valorizzare la Data Iscrizione Finale!');
		document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>.focus();
	   	return false;
	}

	<%-- ulteriori controlli sulle date --%>
	if (data_iniziale != "//" && data_finale != "//") {
		if (!ControllaData(data_iniziale)) {
			alert('Data Iscrizione Iniziale non valida!');
			document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>.focus();
		   	return false;
		} else if (!ControllaData(data_finale)) {
			alert('Data Iscrizione Finale non valida!');
			document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_finale)) {
		   	alert('Data Iscrizione Finale precedente alla Data Iscrizione Iniziale!');
		   	document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_sistema)) {
		   	alert('Data Iscrizione Iniziale non può essere superiore alla Data di Sistema!');
		   	document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_finale, data_sistema)) {
			alert('Data Iscrizione Finale non può essere superiore alla Data di Sistema!');
			document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>.focus();
		   	return false;
		}
	}

	if (document.f.tipo[1].checked) {
		if (document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>.value == ""
				&& document.f.<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>.value == ""
				&& document.f.<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>.value == "") {
			alert("Attenzione! Inserire Periodo Fine Pena Procedimenti in Scadenza!");
			document.f.<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>.focus();
			return false;
		}
	}
    if (!(document.f.tipo[0].checked || document.f.tipo[1].checked || document.f.tipo[2].checked|| document.f.tipo[3].checked)) {
		alert("Attenzione! E' obbligatorio selezionare almeno un criterio di ricerca!");
		return false;
    }
    if (!(document.f.rife[0].checked || document.f.rife[1].checked)) {
		alert("Attenzione! E' obbligatorio selezionare almeno un riferimento per l'estrazione!");
		return false;
    }

    return true;
}
</script>
</head>
<body class="corpo">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.scadenzario.action.ActRicercaFinePenaProcedimentiPendenti">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
	  	<td class="LBG">
	  		<font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzario Ricerca Fine Pena Procedimenti Pendenti</font>
	  	</td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="80%">
	<tr><td class="Titolo">Intervallo Estremi Procedimenti</td></tr>
   	<tr>
		<td class="c" width="61%">Anno/Numero Iniziale 
			<input Title="Anno Iniziale" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			<input Title="Numero Iniziale" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_NUM_INIZIALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          	&nbsp;&nbsp;&nbsp;Anno/Numero Finale&nbsp;&nbsp;&nbsp;
          	<input Title="Anno Finale" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			<input Title="Numero Finale" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_NUM_FINALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="80%">
	<tr><td class="Titolo">Intervallo Date Iscrizione</td></tr>
	<tr>
		<td class="c" width="61%">Data Iscrizione Iniziale      
			<input Title="dalla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
			<input Title="dalla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
			<input Title="dalla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
			&nbsp;&nbsp;&nbsp;Data Iscrizione Finale&nbsp;&nbsp;&nbsp;
			<input Title="alla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
			<input Title="alla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
			<input Title="alla Data" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="80%">
	<tr><td class="Titolo" colspan="3">Criteri di Ricerca</td></tr>
	<tr>
		<td class="l" width="15%">Tutti</td>
		<td class="l" width="4%"><input type="radio" name="tipo" value="tutti"></td>
		<td class="l">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">In scadenza</td>
		<td class="l"><input type="radio" name="tipo" value="intervallo"></td>
		<td class="L" width="40%">
			entro: Anni
	    	<input title="Anni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
			Mesi
			<input title="Mesi" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
			Giorni
			<input title="Giorni" size="2" maxlength="2" value="" type="text" name="<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>" onkeypress="return TicTabNumField(this,event)">
		</td>
	</tr>
	<tr>
		<td class="l">In scadenza Oggi</td>
		<td class="l"><input type="radio" name="tipo" value="oggi"></td>
		<td class="l">&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Scaduti</td>
		<td class="l"><input type="radio" name="tipo" value="scaduti"></td>
		<td class="l">&nbsp;</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l" colspan="3">Indicare se l'Estrazione deve far Riferimento a:&nbsp;&nbsp;&nbsp;
			Data Fine Pena Reale&nbsp;&nbsp;&nbsp;<input type="radio" name="rife" value="reale">
			&nbsp;&nbsp;&nbsp;
			Data Fine Pena Virtuale&nbsp;&nbsp;&nbsp;<input type="radio" name="rife" value="virtuale">
			&nbsp;&nbsp;&nbsp;
			Estrai anche i Procedimenti SIUS Definiti&nbsp;&nbsp;&nbsp;<input type="checkbox" name="defi" value="S">
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
    <tr>
		<td colspan="2">
	    	<INPUT class="bottone" type="submit" name="RICERCA" value="Ricerca">
	  	</td>
	</tr>
	<tr><td>&nbsp;</td>
	<tr><td class="Titolo" style="color: red;" colspan="3">N.B. Le date di inizio e fine pena sono quelle presenti al momento dell'estrazione sul Procedimento SIEP collegato</td></tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNI_SCADENZA%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESI_SCADENZA%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNI_SCADENZA%>", "numeric");

frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_INIZIALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIALE%>", "minlen=4", "La lunghezza minima per l'anno è di 4 caratteri");

frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_GIORNO_DATA_ISCRIZIONE_FINALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_MESE_DATA_ISCRIZIONE_FINALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_FINALE%>", "numeric");
frmvalidator.addValidation("<%=ICostantiScadenzarioSius.CAMPO_ANNO_DATA_ISCRIZIONE_FINALE%>", "minlen=4", "La lunghezza minima per l'anno è di 4 caratteri");

frmvalidator.setAddnlValidationFunction("verifica");
</script>
</body>
</html>