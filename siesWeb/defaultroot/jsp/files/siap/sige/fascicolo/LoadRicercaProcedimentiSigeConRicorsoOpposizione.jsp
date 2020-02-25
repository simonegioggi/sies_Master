<%-- MEV_65: aggiunta pagina per gestire nuova funzionalita' --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="model" scope="request" class="siap.sige.fascicolo.model.RicercaFascicoloSigeModel"/>

<html>
<head>
<title>[S.I.E.S.] - Ricerca Procedimenti con Ricorso/Opposizione</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script type="text/javascript">
function calendario(a_formname, a_field_year, a_field_month, a_field_day) {
	var desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
}

function Verify() {
	var data_iniziale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.value + '/' + document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>.value + '/'+ document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>.value;
	var data_finale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.value + '/' + document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>.value + '/'+ document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>.value;
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

	var annoIniziale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.value;
	var annoFinale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.value;
	var numeroIniziale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.value;
	var numeroFinale = document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.value;
	var annoPuntuale = document.lrpscro.annoRicorso.value;
	var numeroPuntuale = document.lrpscro.numeroRicorso.value;

	if ((data_iniziale == "//" && data_finale == "//" && annoIniziale.length == 0 && numeroIniziale.length == 0
			&& annoFinale.length == 0 && numeroFinale.length == 0
			&& annoPuntuale.length == 0 && numeroPuntuale.length == 0)
			|| (data_iniziale != "//" && data_finale != "//" && annoIniziale.length != 0 && numeroIniziale.length != 0
					&& annoFinale.length != 0 && numeroFinale.length != 0)
			|| (data_iniziale != "//" && data_finale != "//" && annoPuntuale.length != 0 && numeroPuntuale.length != 0)
			|| (annoIniziale.length != 0 && numeroIniziale.length != 0
					&& annoFinale.length != 0 && numeroFinale.length != 0
					&& annoPuntuale.length != 0 && numeroPuntuale.length != 0)) {
		alert('Valorizzare obbligatoriamente Anno/Numero Ricorso/Opposizione oppure l\'Intervallo Estremi Ricorso/Opposizione oppure l\'Intervallo Date Arrivo in Cancelleria!');
		if (annoPuntuale.length == 0)
			document.lrpscro.annoRicorso.focus();
		else
			document.lrpscro.numeroRicorso.focus();
	   	return false;
	}

	// se solo una delle due date e' valorizzata
	if (data_iniziale == "//" && data_finale != "//") {
		alert('Valorizzare la Data Iniziale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
	   	return false;
	}
	if (data_iniziale != "//" && data_finale == "//") {
		alert('Valorizzare la Data Finale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Ricorso/Opposizione e' valorizzato
	if (annoPuntuale.length == 0 && numeroPuntuale.length != 0) {
		alert('Valorizzare l\'Anno Ricorso/Opposizione!');
		document.lrpscro.annoRicorso.focus();
	   	return false;
	}
	if (annoPuntuale.length != 0 && numeroPuntuale.length == 0) {
		alert('Valorizzare il Numero Ricorso/Opposizione!');
		document.lrpscro.numeroRicorso.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Iniziale e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length != 0) {
		alert('Valorizzare l\'Anno Iniziale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length == 0) {
		alert('Valorizzare il Numero Iniziale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Anno/Numero Finale e' valorizzato
	if (annoFinale.length == 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno Finale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
	   	return false;
	}
	if (annoFinale.length != 0 && numeroFinale.length == 0) {
		alert('Valorizzare il Numero Finale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.focus();
	   	return false;
	}
	// se solo uno dei due campi Estremi Ricorso/Opposizione e' valorizzato
	if (annoIniziale.length == 0 && numeroIniziale.length == 0 && annoFinale.length != 0 && numeroFinale.length != 0) {
		alert('Valorizzare l\'Anno/Numero Iniziale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>.focus();
	   	return false;
	}
	if (annoIniziale.length != 0 && numeroIniziale.length != 0 && annoFinale.length == 0 && numeroFinale.length == 0) {
		alert('Valorizzare l\'Anno/Numero Finale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
	   	return false;
	}

	// controllo sugli anni/numeri
	if (parseInt(annoIniziale) > parseInt(annoFinale)) {
		alert('Anno Finale minore dell\'Anno Iniziale!');
		document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
     	return false;
   	}
  	if ((parseInt(annoIniziale) == parseInt(annoFinale))
  			&& (parseInt(numeroIniziale) > parseInt(numeroFinale))) {
     	alert('Numero Finale minore del Numero Iniziale!');
     	document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>.focus();
     	return false;
   	}
  	var annoSistema = <%=DateUtils.getSysDate("yyyy")%>;
  	if (parseInt(annoFinale) > annoSistema) {
     	alert('Anno Finale maggiore dell\'Anno Corrente.');
     	document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>.focus();
     	return false;
   	}
  	if (parseInt(annoPuntuale) > annoSistema) {
     	alert('Anno Ricorso maggiore dell\'Anno Corrente.');
     	document.lrpscro.annoRicorso.focus();
     	return false;
   	}

	<%-- ulteriori controlli sulle date --%>
	if (data_iniziale != "//" && data_finale != "//") {
		if (!ControllaData(data_iniziale)) {
			alert('Data Iniziale non valida!');
			document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
		   	return false;
		} else if (!ControllaData(data_finale)) {
			alert('Data Finale non valida!');
			document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_finale)) {
		   	alert('Data Finale precedente alla Data Iniziale!');
		   	document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_iniziale, data_sistema)) {
		   	alert('Data Iniziale non può essere superiore alla data di sistema!');
		   	document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>.focus();
		   	return false;
		} else if (!CompareDate(data_finale, data_sistema)) {
			alert('Data Finale non può essere superiore alla data di sistema!');
			document.lrpscro.<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>.focus();
		   	return false;
		}
	}

	return true;
}

function caricaValori() {
<%
if (Utils.isPresent(model.getTipoRicorso())) {
	if ("01".equals(model.getTipoRicorso()) || "04".equals(model.getTipoRicorso()))
		model.setTipoRicorso("00");
%>
	document.lrpscro.tipoRicorso.value = '<%=model.getTipoRicorso()%>';
<%
}
if (Utils.isPresent(model.getStatoValidazione())) {
%>
	document.lrpscro.TipoRicerca.value = '<%=model.getStatoValidazione()%>';
<%
}
if (Utils.isPresent(model.getChiaveProgrRicorso())) {
%>
	document.lrpscro.numeroRicorso.value = '<%=model.getChiaveProgrRicorso().toString()%>';
<%
}
if (Utils.isPresent(model.getChiaveProgrRicorso())) {
%>
	document.lrpscro.annoRicorso.value = '<%=model.getChiaveAnnoRicorso().toString()%>';
<%
}
if (model.getChiaveAnnoIniziale() != null) {
%>
	document.lrpscro.AnnoIni.value = '<%=model.getChiaveAnnoIniziale().toString()%>';
<%
}
if (Utils.isPresent(model.getChiaveProgrIniziale())) {
%>
	document.lrpscro.NumIni.value = '<%=model.getChiaveProgrIniziale().toString()%>';
<%
}
if (Utils.isPresent(model.getChiaveAnnoFinale())) {
%>
	document.lrpscro.AnnoFine.value = '<%=model.getChiaveAnnoFinale().toString()%>';
<%
}
if (Utils.isPresent(model.getChiaveProgrFinale())) {
%>
	document.lrpscro.NumFine.value = '<%=model.getChiaveProgrFinale().toString()%>';
<%
}
if (model.getDataArrivoCancelleriaIniziale() != null) {
%>
	document.lrpscro.GiornoIniziale.value = '<%=DateUtils.getDayToString(model.getDataArrivoCancelleriaIniziale())%>';
	document.lrpscro.MeseIniziale.value = '<%=DateUtils.getMonthToString(model.getDataArrivoCancelleriaIniziale())%>';
	document.lrpscro.AnnoIniziale.value = '<%=DateUtils.getYearToString(model.getDataArrivoCancelleriaIniziale())%>';
	var gi = document.lrpscro.GiornoIniziale.value;
	if (gi.length < 2) {
		document.lrpscro.GiornoIniziale.value = "0" + document.lrpscro.GiornoIniziale.value;
	}
	var mi = document.lrpscro.MeseIniziale.value;
	if (mi.length < 2)
		document.lrpscro.MeseIniziale.value = "0" + document.lrpscro.MeseIniziale.value;
<%
}
if (model.getDataArrivoCancelleriaFinale() != null) {
%>		
	document.lrpscro.GiornoFinale.value = '<%=DateUtils.getDayToString(model.getDataArrivoCancelleriaFinale())%>';
	document.lrpscro.MeseFinale.value = '<%=DateUtils.getMonthToString(model.getDataArrivoCancelleriaFinale())%>';
	document.lrpscro.AnnoFinale.value = '<%=DateUtils.getYearToString(model.getDataArrivoCancelleriaFinale())%>';
	var gf = document.lrpscro.GiornoFinale.value;
	if (gf.length < 2) {
		document.lrpscro.GiornoFinale.value = "0" + document.lrpscro.GiornoFinale.value;
	}
	var mf = document.lrpscro.MeseFinale.value;
	if (mf.length < 2) {
		document.lrpscro.MeseFinale.value = "0" + document.lrpscro.MeseFinale.value;
	}
<%
}
%>
}
</script>
</head>

<body class="corpo" onload="Javascript:caricaValori();">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="lrpscro">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione:</font>&nbsp;<font class="campo">Ricerca Procedimenti con Ricorso/Opposizione</font></td>
    </tr>
</table>
<br>
<table width="96%">
	<tr>
		<td class="l" width="25%">Tipo Ricorso/Opposizione (*)</td>
      	<td class="l">
      		<select Title="Tipo Ricorso/Opposizione" name="tipoRicorso">
        		<option value="00" selected="selected">Tutti</option>
        		<option value="02">Ricorso senza esito</option>
        		<option value="03">Ricorso con esito</option>
        		<option value="05">Opposizione senza esito</option>
        		<option value="06">Opposizione con esito</option>
			</select>
		</td>
	</tr>
	<tr>
		<td class="L" >
          	<font class="label">Anno/Numero Ricorso/Opposizione (*)</font>
        </td>
        <td class="l">
          	<input type="text" title="Anno Ricorso/Opposizione" name="annoRicorso" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Ricorso/Opposizione" name="numeroRicorso" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
	</tr>
</table>
<br>
<table  width="96%">
	<tr>
		<td class="Titolo">Intervallo Estremi Ricorso/Opposizione</td>
	</tr>
</table>
<table width="96%">
	<tr>
		<td class="L" width="25%">
          	<font class="label">Anno/Numero Iniziale (*)</font>
        </td>
        <td class="l" width="25%">
          	<input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_INI %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="L" width="25%">
          	<font class="label">Anno/Numero Finale (*)</font>
        </td>
        <td class="l">
          	<input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Finale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
	</tr>
</table>
<br>
<table  width="96%">
	<tr>
		<td class="Titolo">Intervallo Date Arrivo in Cancelleria</td>
	</tr>
</table>
<table width="96%">
	<tr>
		<td class="L" width="25%">
			<font class="label">Data Iniziale (*)</font>
		</td>
		<td class="l" width="25%">
			<input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            <a href="javascript:calendario('lrpscro','<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>');">
				<img src="/images/calendario.gif" border="0">
          	</a>
		</td>
		<td class="L" width="25%">
			<font class="label">Data Finale (*)</font>
		</td>
		<td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            <a href="javascript:calendario('lrpscro','<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>');">
				<img src="/images/calendario.gif" border="0">
          	</a>
		</td>
	</tr>
</table>
<br>
<table width="96%">
	<tr>
		<td class="L" width="25%">
          	<font class="label">Stato Validazione:</font>
        </td>
        <td class="l">
        	&nbsp;&nbsp;&nbsp;
          	Tutti<input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="Tutti" checked>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			Non Annullati<input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="Non Annullati">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			Annullati<input type="radio" name="<%=ICostantiFascicoloSige.RADIO_TIPO_RICERCA%>" value="Annullati">
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
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActRicercaProcedimentiSigeConRicorsoOpposizione">
<input type="hidden" name="<%=IWebConstants.LINK_RITORNO %>" value="<%=TornaQui%>">
</FORM>
 
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("lrpscro");

<%-- DATA INIZIALE e FINALE --%>
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

<%-- ANNO/NUMERO INIZIALE E FINALE --%>
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

<%-- ANNO/NUMERO RICORSO/OPPOSIZIONE --%>
frmvalidator.addValidation("numeroRicorso","numeric");
frmvalidator.addValidation("annoRicorso","numeric");
frmvalidator.addValidation("annoRicorso","minlen=4","La lunghezza del campo Anno Ricorso deve essere di 4 caratteri");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>