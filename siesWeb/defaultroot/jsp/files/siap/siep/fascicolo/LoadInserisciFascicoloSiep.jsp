<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="fascicolo"             scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="sentenza"              scope="session" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="assegnazione_manuale"  scope="request" class="java.lang.String"/>

<%-- S/N indica l'esistenza di almeno un procedimento di classe IV per l'anno corrente --%>
<jsp:useBean id="EsisteFascicoloClasseIVAnnoCorrente" scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoCorrente" scope="request" class="java.lang.String"/>

<%-- Form per l'assegnazione numero siep (automatica o manuale) --%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Sentenza</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<style>
#dhtmltooltip {
	position: absolute;
	width: 150px;
	border: 2px solid black;
	padding: 2px;
	background-color: lightyellow;
	visibility: hidden;
	z-index: 100;
	/*Remove below line to remove shadow. Below line should always appear last within this CSS*/
	filter: progid:DXImageTransform.Microsoft.Shadow(color=gray,direction=135);
}
</style>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
function Verify() {
	if (checkPrimoFascicoloMS()) {
        return false;
	}

<%--        if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value.length==1) --%>
<%--         document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value; --%>
<%--       if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value.length==1) --%>
<%--         document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value; --%>
<%--       if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1) --%>
<%--         document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value; --%>
<%--       if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1) --%>
<%--         document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value; --%>

	// Data Iscrizione provvedimento
	var d4 = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;

	if (!ControllaData(d4)) {
		alert('Data Iscrizione Procedimento non valida '+ d4);
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.focus();
		return false;
	}
      
	/* inizio modifica 9/03/2009 */
	var d2 = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>.value;

	if (! ControllaData(d2)) {
		alert('Data Arrivo Atto non valida '+ d2);
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.focus();
		return false;
	}

	// Data Irrevocabilità
	var d3 = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value +
			'/' + document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
   
	if (!ControllaData(d3) && d3.length > 2) {
		alert('Data Irrevocabilità non valida');
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
		return false;
	}

	if (!CompareDate(d2,d4)) {
		alert('la Data iscrizione Procedimento deve essere successiva alla Data Arrivo Atto');
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.focus();
		return false;
	}

	if (!CompareDate(d3,d2)) {
		alert('La Data Irrevocabilità deve essere precedente alla Data Arrivo Atto');
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
		return false;
	}

    var d5= '<%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd/MM/yyyy"))%>';
	if (!CompareDate(d5,d2)) {
		alert('Data Arrivo Atto deve essere successiva alla Data sentenza');
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.focus();
		return false;
	}

	if (CompareDate(d3,d5)) {
		alert('Data Irrevocabilità deve essere successiva alla Data sentenza');
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
		return false;
	}
	/* fine modifica 9/03/2009 */
      
	/*
	if (document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value.length==1)
	  	document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value;
	if (document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value.length==1)
	  	document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value;
	*/
<%
if (assegnazione_manuale.equals("S")) {
%>
	// Controllo campo chiave anno.
	var anno_sistema = '<%=DateUtils.getSysDate("yyyy")%>'
	var chiave_anno = document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value;

	if (chiave_anno > anno_sistema) {
		alert("Il campo Anno Procedimento non può superare l'anno corrente");
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
		return false;
	}

	// Verifica delle congruenza del numero digitato con la classe scelta
	var minrange;
	var maxRange;

	if (document.LoadInserisciFascicolo.tipo[0].checked) {
		minrange = 0;
		maxRange = 20000;
	}

	if (document.LoadInserisciFascicolo.tipo[1].checked) {
	    minrange = 20000;
	    maxRange = 30000;
	}  

	if (document.LoadInserisciFascicolo.tipo[2].checked) {
	    minrange = 30000;
	    maxRange = 40000;
	}   

	if (document.LoadInserisciFascicolo.tipo[3].checked) {
	    minrange = 40000;
	    maxRange = 50000;
	}

	if (document.LoadInserisciFascicolo.tipo[4].checked) {
	    minrange = 50000;
	    maxRange = 60000;
	}

	if (document.LoadInserisciFascicolo.tipo[5].checked) {
	    minrange = 60000;
	    maxRange = 70000;
	}

	if (document.LoadInserisciFascicolo.tipo[6].checked) {
	    minrange = 70000;
	    maxRange = 80000;
	}   

	// Controllo il progressivo solo se numerazione ordinaria
	if (document.LoadInserisciFascicolo.checkNumSpec.checked == false) {
		if (document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value > maxRange) {
			alert ("Valore superiore al massimo consentito nel campo Numero Procedimento");
			document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
			return false;
		}
		if (parseInt(document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value) == 0) {
			alert ("Valore non consentito nel campo Numero Procedimento");
			document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
			return false;
		}
		if ((document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value < minrange)
				|| (document.LoadInserisciFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value > maxRange)) {
			alert ("Valore INCONGRUENTE con il 'Tipo Classe' nel campo Numero Procedimento");
			document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
			return false;
	  	}
	}
<%
}
%>
<%-- 	var data_to_verify = document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value
		+'/'+document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value
		+'/'+document.LoadInserisciFascicolo.< %=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value; --%>
// 	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
// 		alert('Data di iscrizione agli atti non valida');
// 		return false;
// 	}
}

function VisualizzaNumerazioneSpeciale() {
	var rigaMS = document.getElementById("alertMS");
	if (rigaMS != null) {
  		rigaMS.style.display = "none";
	}
	var colonna = document.getElementById("numSpec");
	if (document.LoadInserisciFascicolo.checkNumSpec.checked == true) {
		colonna.style.display = "block";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = "";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = "";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly = true;
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.style.backgroundColor = "C0C0C0";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.readOnly = true;
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.style.backgroundColor = "C0C0C0";
		document.LoadInserisciFascicolo.tipo[0].disabled = true;
		document.LoadInserisciFascicolo.tipo[1].disabled = true;
		document.LoadInserisciFascicolo.tipo[2].disabled = true;
		document.LoadInserisciFascicolo.tipo[3].disabled = true;
		document.LoadInserisciFascicolo.tipo[4].disabled = true;
		document.LoadInserisciFascicolo.tipo[5].disabled = true;
		document.LoadInserisciFascicolo.tipo[6].disabled = true;
	} else {
		colonna.style.display = "none";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = "";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = "";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly = false;
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.style.backgroundColor = "FFFFFF";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.readOnly = false;
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.style.backgroundColor = "FFFFFF";
	    document.LoadInserisciFascicolo.tipo[0].disabled = false;
	    document.LoadInserisciFascicolo.tipo[0].checked = true;
	    document.LoadInserisciFascicolo.tipo[1].disabled = false;
	    document.LoadInserisciFascicolo.tipo[2].disabled = false;
	    document.LoadInserisciFascicolo.tipo[3].disabled = false;
	    document.LoadInserisciFascicolo.tipo[4].disabled = false;
	    document.LoadInserisciFascicolo.tipo[5].disabled = false;
	    document.LoadInserisciFascicolo.tipo[6].disabled = false;
  	}
}

function TrasformaRes(a_formname,a_fieldname,a_fieldname2) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function TrasformaPret(a_formname,a_fieldname,a_fieldname2) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function checkPrimoFascicoloMS() {
<%
if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente)) {
	if (assegnazione_manuale.equals("S")) {
%>
	if (document.LoadInserisciFascicolo.tipo[3].checked) {
		var rigaMS = document.getElementById("alertMS");
		rigaMS.style.display = "block";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = <%=AnnoCorrente%>;
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly  = true;
	} else {
		var rigaMS = document.getElementById("alertMS");
		rigaMS.style.display = "none";
		document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly = false;
		//document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value = "";
	}
<%
	} else {
%>
	if (document.LoadInserisciFascicolo.tipo[3].checked) 	{
		alert("Attenzione! Si sta procedendo all'iscrizione del primo procedimento di Classe IV per l'anno corrente. E' obbligatorio procedere con l'assegnazione manuale per poter inizializzare opportunamente la numerazione");
		return true;
	}
<%
	}
}
%>
	return false;
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
      	<td class=LBG>
      		<font class="label">Funzione:</font>&nbsp;
<%
FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
Date lDataIscrizione = new Date();
// Date lDataIrrevocabilita = new Date();
// if (sentenza != null && sentenza.getDataIrrevocabilita() != null)
// 		lDataIrrevocabilita = sentenza.getDataIrrevocabilita();
String lAction = new String();
if ( modalita.equals("I")) {
	if (assegnazione_manuale.equals("S")) {
		lAction = "siap.siep.fascicolo.action.ActInserisciFascicoloManuale";
 %>
			<font class="campo">Inserimento Procedimento Manuale</font>
 <% 
	} else {
				lAction = "siap.siep.fascicolo.action.ActInserisciFascicolo";
 %>
			<font class="campo">Inserimento Procedimento</font>
 <%
	}
} else if (modalita.equals("M")) {
	lAction = "siap.siep.fascicolo.action.ActModificaFascicolo";
	lFascicolo = fascicolo;
	lDataIscrizione = lFascicolo.getDataIscrizione();
	// lDataIrrevocabilita = lFascicolo.getDataIrrevocabilita();
   %>
            <font class="campo">Modifica Procedimento</font>
<%
}
%>
		</td>
	</tr>
</table>
<br>  
<%
if (modalita.equals("I")) {
%>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaAttribuzione.jsp"/>
<%
} else if (modalita.equals("M")) {
%>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<%
}
%>
<br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">
<table cellspacing=2 cellpadding=2 <% if (assegnazione_manuale.equals("S")) {%> width="90%" <%}%> >
	<tr>
      	<td class="l">Data Iscrizione Procedimento</td>
      	<td class="l">
<%
if (!assegnazione_manuale.equals("S")) {
	if (modalita.equals("I")) {
%>
			<%=DateUtils.getSysDate("dd")%>/<%=DateUtils.getSysDate("MM")%>/<%=DateUtils.getSysDate("yyyy")%>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>">
<%
	} else if (modalita.equals("M")) {
		lDataIscrizione = lFascicolo.getDataIscrizione();
%>
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>/<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>/<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>">
<%
	}
} else {
	if (modalita.equals("I")) {
%>
			<input title="Data Iscrizione Procedimento" value="" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Iscrizione Procedimento" value="" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Iscrizione Procedimento" value="" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
    } else if (modalita.equals("M")) {
		lDataIscrizione = lFascicolo.getDataIscrizione();
%>
			<input title="Data Iscrizione Procedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Iscrizione Procedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Iscrizione Procedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
	}
}
%>
		</td>
    </tr>
  	<tr>
      	<td class="l">Data Arrivo Atto</td>
      	<td class="l">
<%
if (modalita.equals("I")) {
%>
			<input title="Data Arrivo Atto" value="" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Arrivo Atto" value="" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Arrivo Atto" value="" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
} else if (modalita.equals("M")) {
	lDataIscrizione = lFascicolo.getDataArrivoAtto();
%>
            <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            /
            <input title="Data Arrivo Atto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
		</td>
    </tr>
    <tr>
<%
// paolo cherubini 05/01/2011 
if (!sentenza.getCodTipoProvvedimento().equals("02")) {
%>
		<td class="l">Data Irrevocabilità</td>
<%
} else {
%>
    	<td class="l">Esecutivo il</td>
<%
}
%>
      	<td class="L">
<%
if (modalita.equals("I")) {
%>
			<input title="Data Irrevocabilità" value="" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
<%
} else if (modalita.equals("M")) {
	lDataIscrizione = lFascicolo.getDataIrrevocabilita();
%>
			<input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "dd"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "MM"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			/
			<input title="Data Irrevocabilità" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataIscrizione, "yyyy"))%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
}
%>
		</td>
    </tr>
<%
// Assegnazione Manuale 
if (assegnazione_manuale.equals("S")) {
%>
    <tr height="15"><td>&nbsp;</td></tr>
    <tr style="display:none" id="alertMS">
		<td class="l" colspan="4" >
        	<font color="red">Attenzione! Si sta procedendo all'iscrizione del primo procedimento di Classe IV per l'anno corrente. 
         	<br>Il progressivo indicato in questa fase sarà il valore dal quale partirà la numerazione automatica per i successivi procedimenti di classe IV.
         	<br>Una volta indicato il progressivo iniziale, sarà possibile acquisire il pregresso per l'anno corrente (assegnare numerazione manuale) solo per procedimenti con numerazione inferiore a quella indicata in questa fase.
         	<br>Come prima iscrizione è necessario quindi registrare o un nuovo procedimento assegnandogli opportuno progressivo secondo quanto prevede l'attuale registro Misure di Sicurezza,
         	oppure registrare l'ultimo procedimento presente sul registro Misure di Sicurezza.
        	</font> 
		</td>    
    </tr>
    <tr>
      	<td class="l">Anno e Numero Procedimento</td>
      	<td class="l">
	        <input type="text" title="Anno Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onFocus="javascript:textboxSelect(this)" onBlur="javascript:value=FillYear(value)">
	        /
	        <input type="text" title="Numero Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="13" size="15" onkeypress="return TicTabNumField(this,event)">
      	</td>
      	<td class="l"> Assegna numerazione speciale&nbsp;<input type="checkbox" name="checkNumSpec" onclick="VisualizzaNumerazioneSpeciale();"></td>
      	<td class="l" style="display:none" id="numSpec">
        	<a href="Javascript:TrasformaRes('LoadInserisciFascicolo','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>');">
          		R.E.S.<img src="/images/filefolder.gif" border=0>
        	</a>&nbsp;&nbsp;
        	<a href="Javascript:TrasformaPret('LoadInserisciFascicolo','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>');">
          		P.T.<img src="/images/filefolder.gif" border=0>
        	</a>
      	<td>
    </tr>
    <tr height="15"><td>&nbsp;</td></tr>
<%
}
%>
</table>

<div id="dhtmltooltip"></div>
<script type="text/javascript">

/***********************************************
* Cool DHTML tooltip script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

var offsetxpoint=-60 //Customize x offset of tooltip
var offsetypoint=-120 //Customize y offset of tooltip
var ie=document.all
var enabletip=false
var tipobj=document.all? document.all["dhtmltooltip"] : document.getElementById? document.getElementById("dhtmltooltip") : ""

function ietruebody() {
	return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function ddrivetip(thetext, thecolor, thewidth){
	if (ie) {
		if (typeof thewidth!="undefined")
			tipobj.style.width=thewidth+"px"
		if (typeof thecolor!="undefined" && thecolor!="")
			tipobj.style.backgroundColor=thecolor
		tipobj.innerHTML=thetext
		enabletip=true
		return false
	}
}

function positiontip(e) {
	if (enabletip) {
  		var curX= event.clientX+ietruebody().scrollLeft;
  		var curY=event.clientY+ietruebody().scrollTop;
		// Find out how close the mouse is to the corner of the window
		var rightedge=ie&&!window.opera? ietruebody().clientWidth-event.clientX-offsetxpoint : window.innerWidth-e.clientX-offsetxpoint-20
		var bottomedge=ie&&!window.opera? ietruebody().clientHeight-event.clientY-offsetypoint : window.innerHeight-e.clientY-offsetypoint-20
		var leftedge=(offsetxpoint<0)? offsetxpoint*(-1) : -1000
		// if the horizontal distance isn't enough to accomodate the width of the context menu
		if (rightedge<tipobj.offsetWidth)
			// move the horizontal position of the menu to the left by it's width
			tipobj.style.left=ie? ietruebody().scrollLeft+event.clientX-tipobj.offsetWidth+"px" : window.pageXOffset+e.clientX-tipobj.offsetWidth+"px"
		else if (curX<leftedge)
			tipobj.style.left="5px"
		else
			//position the horizontal position of the menu where the mouse is positioned
			tipobj.style.left=curX+offsetxpoint+"px"

		// same concept with the vertical position
		if (bottomedge<tipobj.offsetHeight)
			tipobj.style.top=ie? ietruebody().scrollTop+event.clientY-tipobj.offsetHeight-offsetypoint+"px" : window.pageYOffset+e.clientY-tipobj.offsetHeight-offsetypoint+"px"
		else
			tipobj.style.top=curY+offsetypoint+"px"
		tipobj.style.visibility="visible"
	}
}

function hideddrivetip() {
	if (ie) {
		enabletip=false
		tipobj.style.visibility="hidden"
		tipobj.style.left="-1000px"
		tipobj.style.backgroundColor=''
		tipobj.style.width=''
	}
}

document.onmousemove=positiontip
</script> 
<%
String ck1="";
String ck2="";
String ck3="";
String ck4="";
String ck5="";
String ck6="";
String ck7="";
if (modalita.equals("M")) {
    int progr= fascicolo.getChiaveProgr().intValue();
    if (progr < 20000) 
      ck1="checked";
    else if (progr >= 20000 && progr < 30000)
      ck2="checked";
    else if (progr >= 30000 && progr < 40000)
      ck3="checked";
    else if (progr >= 40000 && progr < 50000)
      ck4="checked";
    else if (progr >= 50000 && progr < 60000)
      ck5="checked";
    else if (progr >= 60000 && progr < 70000)
      ck6="checked";
    else if (progr >= 70000 && progr < 80000)
      ck7="checked";
} else if (modalita.equals("I"))
	ck1="checked";
%>
<table cellspacing=2 cellpadding=2>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Detentiva','yellow')" ONMOUSEOUT="hideddrivetip()">Classe I</td>
		<td class="l"><input type="radio" name="tipo" value="1" <%=ck1%> onClick="javascript:checkPrimoFascicoloMS();"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe II</td>
		<td class="l"><input type="radio" name="tipo" value="2" <%=ck2%> onClick="javascript:checkPrimoFascicoloMS();"></td>
     </tr>
     <tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Pena Sospesa','yellow')" ONMOUSEOUT="hideddrivetip()">Classe III</td>
		<td class="l"><input type="radio" name="tipo" value="3" <%=ck3%> onClick="javascript:checkPrimoFascicoloMS();"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Misura Sicurezza','yellow')" ONMOUSEOUT="hideddrivetip()">Classe IV</td>
		<td class="l"><input type="radio" name="tipo" value="4" onClick="javascript:checkPrimoFascicoloMS();"></td>
     </tr>
     <tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Persona Giuridica','yellow')" ONMOUSEOUT="hideddrivetip()">Classe V</td>
		<td class="l"><input type="radio" name="tipo" value="5" onClick="javascript:checkPrimoFascicoloMS();"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td class="l" ONMOUSEOVER="ddrivetip('Giudice di Pace','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
		<td class="l"><input type="radio" name="tipo" value="6" onClick="javascript:checkPrimoFascicoloMS();"></td>
     </tr>
     <tr>
		<td class="l" ONMOUSEOVER="ddrivetip('Conversione Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VII</td>
		<td class="l"><input type="radio" name="tipo" value="7" onClick="javascript:checkPrimoFascicoloMS();"></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td>      
     </tr>
     <tr><td>&nbsp;</td></tr>
</table>
<table>
    <tr>
      	<td class="l">Note Procedimento</td>
      	<td class="l">
        	<textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote())%></textarea>
	</tr>
    <tr>
      	<td>
        	<input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
      	</td>
    </tr>
</table>

<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(lFascicolo.getIdFascicoloSiep())%>">
<input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
<input type="HIDDEN" name="assegnazione_manuale" value="<%=assegnazione_manuale%>">

</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciFascicolo");
<%
if (assegnazione_manuale.equals("S")) {
%>
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req", "Il campo Anno Procedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req", "Il campo Numero Procedimento è obbligatorio");
	
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");
	
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per il campo Anno Procedimento è di 4 caratteri");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","gt=1900");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","gt=0");
	
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","numeric");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","gt=1900");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","lt=3000");
	
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","req");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>","req");
	frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>","req");
	
	frmvalidator.setAddnlValidationFunction("Verify");
<%
}
%>
</script>
</body>
</html>