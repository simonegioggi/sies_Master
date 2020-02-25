<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="elencoMagistrati"	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoNazioni"		scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni"		scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Ricerca Soggetti per Posizione Giuridica</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script type="text/javascript">
function radio() {
	if (!document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>[0].checked) {
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>.disabled = true;
   	} else {
    	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>.disabled = false;
   	}
}

function visualizzaDataPendenza(val) {
	if (val == '') {
		if (document.lrssppg.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>[1].checked)
			val = 'P';
		else
			val = 'T';
	}
	if (val == "T") {
		document.getElementById("pendenza").style.display = 'none';
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.value = "";
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>.value = "";
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>.value = "";;
	} else
		document.getElementById("pendenza").style.display = 'block';
}

function calendario(a_formname, a_field_year, a_field_month, a_field_day) {
	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
}

function Verify() {
	var data_iniziale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.value + '/' + document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>.value + '/'+ document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>.value;
	var data_finale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.value + '/' + document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>.value + '/'+ document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>.value;
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

	var annoIniziale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.value;
	var annoFinale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.value;
	var numeroIniziale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.value;
	var numeroFinale = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.value;

	if (data_iniziale == "//" && data_finale == "//" && annoIniziale.length == 0 && numeroIniziale.length == 0
			&& annoFinale.length == 0 && numeroFinale.length == 0) {
		alert('Valorizzare obbligatoriamente Data Iniziale e Finale oppure i campi Anno/Numero Iniziale e Finale!');
		if (data_iniziale == "//")
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
		else
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
	   	return false;
	}

	// se solo una delle due date e' valorizzata
	if (data_iniziale == "//" && data_finale != "//") {
		alert('Valorizzare la Data Iniziale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
	   	return false;
	}
	if (data_iniziale != "//" && data_finale == "//") {
		alert('Valorizzare la Data Finale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
	   	return false;
	}

	// se solo uno dei due campi Anno/Numero Iniziale e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length != 0) {
		alert('Valorizzare l\'Anno Iniziale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length == 0) {
		alert('Valorizzare il Numero Iniziale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Finale e' valorizzato
	if (annoFinale.length == 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno Finale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
	   	return false;
	}
	if (annoFinale.length != 0 && numeroFinale.length == 0) {
		alert('Valorizzare il Numero Finale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Iniziale o Finale e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length == 0 && annoFinale.length != 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno/Numero Iniziale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length != 0 && annoFinale.length == 0 && numeroFinale.length == 0) {
		alert('Valorizzare l\'Anno/Numero Finale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
	   	return false;
	}

	// controllo sugli anni/numeri
	if (parseInt(annoIniziale) > parseInt(annoFinale)) {
		alert('Anno Finale minore dell\'Anno Iniziale!');
		document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
     	return false;
   	}
  	if ((parseInt(annoIniziale) == parseInt(annoFinale))
  			&& (parseInt(numeroIniziale) > parseInt(numeroFinale))) {
     	alert('Numero Finale minore del Numero Iniziale!');
     	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.focus();
     	return false;
   	}
  	var annoSistema = <%=DateUtils.getSysDate("yyyy")%>;
  	if (parseInt(annoFinale) > annoSistema) {
     	alert('Anno Finale maggiore dell\'Anno Corrente.');
     	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
     	return false;
   	}

	<%-- ulteriori controlli sulle date --%>
	if (data_iniziale != "//" && data_finale != "//") {
		if (!ControllaData(data_iniziale)) {
			alert('Data Iniziale non valida!');
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
		   	return false;
		} else if (!ControllaData(data_finale)) {
			alert('Data Finale non valida!');
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_finale)) {
		   	alert('Data Finale precedente alla Data Iniziale!');
		   	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_sistema)) {
		   	alert('Data Iniziale non può essere superiore alla data di sistema!');
		   	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_finale, data_sistema)) {
			alert('Data Finale non può essere superiore alla data di sistema!');
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		}
	}

	if (document.lrssppg.<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>[1].checked) {
		var dataFinePendenza = document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.value + '/' + document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>.value + '/'+ document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>.value;
		if (dataFinePendenza == "//") {
			alert('Data Fine Pendenza obbligatoria se lo stato procedimento è per solo pendenti!');
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
		   	return false;
		} else if (!ControllaData(dataFinePendenza)) {
			alert('Data Fine Pendenza non valida!');
			document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
		   	return false;
		}
// 		else if (!CompareDate(dataFinePendenza, data_sistema)) {
// 		   	alert('Data Fine Pendenza non può essere superiore alla data di sistema!');
<%-- 		   	document.lrssppg.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>.focus(); --%>
// 		   	return false;
// 		}
	}

	return true;
}

function mostraNazioni() {
	var selezione = document.getElementById("nazionalita");
	if (selezione.options[selezione.selectedIndex].value == 'S')
		document.getElementById("nazioni").style.visibility = 'visible';
	else
		document.getElementById("nazioni").style.visibility = 'hidden';
}
</script>
</head>

<body class="corpo" onload="javascript: visualizzaDataPendenza(''); mostraNazioni();">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="lrssppg">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione:</font>&nbsp;<font class="campo">Ricerca Soggetti per Posizione Giuridica</font></td>
    </tr>
</table>
<br>
<table  width="96%">
	<tr>
		<td class="Titolo">Intervallo Date di Iscrizione</td>
	</tr>
</table>
<table width="96%">
	<tr>
		<td class="L" width="20%">
			<font class="label">Data Iniziale (*)</font>
		</td>
		<td class="l" width="20%">
			<input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            <a href="javascript:calendario('lrssppg','<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>');">
				<img src="/images/calendario.gif" border="0">
          	</a>
		</td>
		<td class="L" width="20%">
			<font class="label">Data Finale (*)</font>
		</td>
		<td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            <a href="javascript:calendario('lrssppg','<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>');">
				<img src="/images/calendario.gif" border="0">
          	</a>
		</td>
	</tr>
</table>
<br>
<table style="width: 96%;">
   	<tr><td class="Titolo" colspan="4">Intervallo Procedimenti</td></tr>
	<tr>
		<td class="L" width="20%">
          	<font class="label">Anno/Numero Iniziale (*)</font>
        </td>
        <td class="l" width="20%">
          	<input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="L" width="20%">
          	<font class="label">Anno/Numero Finale (*)</font>
        </td>
        <td class="l">
          	<input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Finale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
	</tr>
</table>
<br>
<table width="96%">
	<tr>
		<td class="l" width="20%">Posizione giuridica:</td>
		<td class="l">Nessuna&nbsp;<input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="0" checked onclick="radio();"></td>
		<td class="l">In espiazione pena in carcere&nbsp;<input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="1" onclick="radio();"></td>
		<td class="l">In misura tutte&nbsp;<input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="2" onclick="radio();"></td>
		<td class="l">Libero e assimilati&nbsp;<input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="3" onclick="radio();"></td>
	</tr>
</table>
<br>
<table width="96%">
	<tr>
		<td class="Titolo" width="20%">Stato Procedimento:&nbsp;&nbsp;&nbsp;
			Tutti<input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="T" onclick="visualizzaDataPendenza(this.value);" checked>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			Solo Pendenti<input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="P" onclick="visualizzaDataPendenza(this.value);">
<!-- 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  -->
<%-- 			Solo Definiti <input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="D"> --%>
		</td>
	</tr>
</table>
<div style="display: none;" id="pendenza">
	<table width="96%">
		<tr>
			<td class="Titolo" colspan="2">Indicare la Data di Fine Pendenza</td>
		</tr>
		<tr>
			<td class="l" width="20%">Data Fine Pendenza <font class=ob>(*)</font></td>
			<td class="l">
	          	<input type="text" title="Giorno Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	          	<input type="text" title="Mese Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	          	<input type="text" title="Anno Fine Pendenza" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			  	<a href="javascript:calendario('lrssppg','<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>','<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>');">
					<img src="/images/calendario.gif" border="0">
	          	</a>
			</td>
		</tr>
	</table>
</div>
<br>
<table width="96%">
	<tr>
		<td class="l" width="20%">Magistrato</td>
      	<td class="l">
      		<select Title="Magistrato" name="<%=ICostantiFascicoloSige.CAMPO_COD_MAG_ASS%>">
        		<%=elencoMagistrati%>
			</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sezione</td>
		<td class="L">
			<select Title="Sezione" name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>">
				<%=elencoSezioni%>
	        </select>
	</tr>
	<tr>
		<td class="l">Nazionalita'</td>
		<td class="L">
	        <select Title="Nazionalita" name="nazionalita" id="nazionalita" onchange="javascript: mostraNazioni();">
				<option value="-" selected>-
				<option value="039">Italiano
				<option value="S">Straniero
          	</select>
          	&nbsp;&nbsp;&nbsp;
          	<select Title="Nazione" name="IdNazione" id="nazioni" style="visibility: hidden;">
				<%=elencoNazioni%>
	        </select>
		</td>
	</tr>
</table>
<br>
<table>
	<tr>
		<td class="label">
			<input class="bottone" type="submit" name="RICERCA" value="Ricerca">
		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActRicercaSoggettiSigePerPosizioneGiuridica">
</FORM>
 
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("lrssppg");

<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","req","Il campo Giorno Iniziale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>","minlen=2","La lunghezza del campo Giorno Iniziale deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","req","Il campo Giorno Finale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>","minlen=2","La lunghezza del campo Giorno Finale deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","req","Il campo Mese Iniziale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>","minlen=2","La lunghezza del campo Mese Iniziale deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","req","Il campo Mese Finale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>","minlen=2","La lunghezza del campo Mese Finale deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","req","Il campo Anno Iniziale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza del campo Anno Iniziale deve essere di 4 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","req","Il campo Anno Finale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza del campo Anno Finale deve essere di 4 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","req","Il campo Anno Iniziale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>","minlen=4","La lunghezza del campo Anno Iniziale deve essere di 4 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","req","Il campo Anno Finale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>","minlen=4","La lunghezza del campo Anno Finale deve essere di 4 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>","req","Il campo Numero Iniziale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>","numeric");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>","req","Il campo Numero Finale è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>","numeric");

<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","req","Il campo Giorno Fine Pendenza è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINE_PENDENZA%>","minlen=2","La lunghezza del campo Giorno Fine Pendenza deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","req","Il campo Mese Fine Pendenza è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_FINE_PENDENZA%>","minlen=2","La lunghezza del campo Mese Fine Pendenza deve essere di 2 caratteri");
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","req","Il campo Anno Fine Pendenza è obbligatorio"); --%>
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE_PENDENZA%>","minlen=4","La lunghezza del campo Anno Fine Pendenza deve essere di 4 caratteri");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>