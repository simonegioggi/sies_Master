<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="foro"               scope="request" class="java.lang.String"/>
<jsp:useBean id="comune"             scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"           scope="request" class="siap.sige.avvocato.model.AvvocatoSigeModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAvvocato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"    scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoDesignazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaDif" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazione"			  scope="request" class="java.lang.String"/> <!-- MEV_21 -->
<jsp:useBean id="statoAvv"			  scope="request" class="java.lang.String"/> <!-- MEV_21 -->

<%
AvvocatoModel lAvv = avvocato.getAvvocato();
%>

<html>
<head>
<title>[S.I.E.S.] - Sostituzione Avvocato </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jsrsClient.js"></script>
<script language="JavaScript">
var desktop;

//Enable ComboBox
function EnableCombo() {
	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.disabled = false;
	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.disabled = false;
	document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.disabled = false;
}

function Inserisci() {
  	// MEV_21 - Aggiunti controlli per inserimento avvocato non certificato.
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value.length==0 ) {
		alert('Il Cognome è obbligatorio');
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COGNOME %>.focus;
		return false;
	}
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_NOME%>.value.length==0 ) {
		alert('Il Nome è obbligatorio');
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_NOME %>.focus;
		return false;
	}

	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') {
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value="";
		if (document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value.length==0) {
			alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
			document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
			return false;
		}
	} else 	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value!='-') {
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.value='';
		cancellaCodComuneReale();
	}

	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value.length > 0
			&& document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE %>.value.length > 0 ) {
		alert('Il Comune di Nascita e il luogo di Nascita Estero sono alternativi');
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>.focus;
		return false;
	}
			
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
		document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value;
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
		document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value;

<%-- 	var data_to_verify=document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value; --%>
// 	if (! ControllaData(data_to_verify)) {
// 		alert('Data di nascita non valida');
// 		return false;
// 	}

	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.value == "-") {
		alert('Il tipo difensore è obbligatorio');
		return false;
	}

	document.LoadModificaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActSostituzioneDifensore";
  	document.LoadModificaAvvocato.submit();
}

function ListaComuni(a_formname,a_fieldname) {
	 if (document.LoadModificaAvvocato.lTipoInserimento.value != "reginde")
		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaAvvocati(a_formname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function cancellaCodComuneReale() {
	document.LoadModificaAvvocato.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
}

function Verify() {
	if (document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value == "") {
  		alert('Selezionare un difensore dalla lista');
  		return false;
	}

	// MEV_21 - 20210720
	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value.length==0 ) {
		alert('Il Cognome è obbligatorio');
		document.LoadModificaAvvocato.<%= ICostantiAvvocato.CAMPO_COGNOME %>.focus;
		return false;
	}

	// MEV 29 - 07/2015 Aggiunto controllo su presenza Foro
  	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value == "") {
		alert('Selezionare il Foro');
		return false;
	}

	var tipo = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>[document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex].value;

	if (tipo == "-") {
		alert("Campo Tipo Avvocato è obbligatorio");
		return false;
	}

	if (document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value == "") {
  		document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=document.LoadModificaAvvocato.idAvvVecchio.value;
  	}
}

function cambiaMotivo() {
	var note = document.getElementById('note');
	var idxSelMotivo = document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE%>.selectedIndex;
	var valoreMotivo = document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE%>[idxSelMotivo].value;
  	if (valoreMotivo == '0008') {
	    note.style.visibility='visible';
	    ufficioSotto.style.top='-90px';
	    conferma.style.top='-35px';    
  	} else {
	    note.style.visibility='hidden';
	    ufficioSotto.style.top='-135px';
	    conferma.style.top='-50px';    
  	}
}

function caricamento() {
	var idxSel = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>.selectedIndex;
	var valore = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_COD_TIPO%>[idxSel].value;
	var fiducia = document.getElementById('fiducia');
	var ufficio = document.getElementById('ufficio');
	var ufficioSotto = document.getElementById('ufficioSotto');
	var conferma = document.getElementById('conferma');
	var motivoDes = document.getElementById('motivoDes');
	note.style.visibility='hidden';
	ufficioSotto.style.top='-135px';

	if (valore == '01') {
		<%-- MEV_21 --%>
		caricaDescComuneForo(document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value);
<%-- 		document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF%>.value = document.LoadModificaAvvocato.<%=ICostantiAvvocato.CAMPO_FORO%>.value; --%>
	}

	conferma.style.visibility = 'visible';
	fiducia.style.visibility = 'hidden';
	if (valore == '01') {
		fiducia.style.visibility='hidden';
		ufficio.style.visibility='visible';
		ufficioSotto.style.visibility='visible';
		conferma.style.visibility='visible';
		motivoDes.style.visibility='visible';
		conferma.style.top='-100px';
	}
	if (valore == '02') {
		fiducia.style.visibility='visible';
		ufficio.style.visibility='hidden';
		ufficioSotto.style.visibility='hidden';
		motivoDes.style.visibility='hidden';
		conferma.style.visibility='visible';
		conferma.style.top='-350px';
	}
	if (valore == '-') {
		fiducia.style.visibility='hidden';
		ufficio.style.visibility='hidden';
		ufficioSotto.style.visibility='hidden';
		motivoDes.style.visibility='hidden';
		conferma.style.visibility='visible';
		conferma.style.top='-350px';
	}
	if (valore == '03') {
		fiducia.style.visibility='hidden';
		ufficio.style.visibility='hidden';
		ufficioSotto.style.visibility='hidden';
		motivoDes.style.visibility='hidden';
		conferma.style.visibility='visible';
		conferma.style.top='-350px';
	}

	// 20210721 Controllo tipo inserimento non Reginde
	var lTipoIns = document.LoadModificaAvvocato.lTipoInserimento.value;
	if (lTipoIns != 'reginde' ) {
		document.getElementById('inserimento').style.visibility = 'visible';
		document.getElementById('confermaBtn').style.visibility = 'hidden';
		document.getElementById('ricReginde').style.visibility = 'hidden';
		document.getElementById('<%=ICostantiAvvocato.CAMPO_COGNOME%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_NOME%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>').disabled = false;
		document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_FORO%>').disabled = false;
		document.getElementById('<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_TELEFONO%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_FAX%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_E_MAIL%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_PEC%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>').readOnly = false; 
		document.getElementById('<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>').disabled = false;
	}
}

function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
}

// MEV_21: aggiunti metodi
function caricaDescComuneForo(foro) {
	var myParams = new Array(foro);
	jsrsExecute("/CaricaHTML_Servlet", loadDescComuneForo, "getDescComuneForo", myParams);      
}

function loadDescComuneForo(descComuneForo) {
	document.LoadModificaAvvocato.<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF%>.value = descComuneForo;
}

<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
function ListaAvvocatiRegInde(a_formname) {
	var inserimento = document.getElementById('inserimento');
	inserimento.style.visibility = 'hidden';
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocatoRegInde&formname="+a_formname,"Ricerca_Avvocato_RegInde","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=1000,height=600");
}

<!-- 20210524	MEV Scheda-21 -->
function ListaComuniNascita(a_formname,a_fieldname) {
  	if (document.LoadModificaAvvocato.lTipoInserimento.value != "reginde")
  		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
} 
</script>
</head>

<body class="corpo" onLoad="caricamento();">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
   		<td class=LBG><font class="label">Funzione:</font>&nbsp;&nbsp;
   			<font class="campo">Sostituzione Difensore</font>
  		</td>
  		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
</table>

<br><jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/><br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaAvvocato">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.avvocato.action.ActSostituzioneDifensore">
<table>
	<tr>
    	<td class="l" id="ricReginde"  style="visibility:visible;">
    		<%-- MEV_21: aggiunta chiamata a WS per individuare lista avvocato in RegInde --%>
<!--       		<a href="Javascript:ListaAvvocati('LoadModificaAvvocato');"> -->
<!--       			Seleziona dalla lista <img src="/images/filefolder.gif" border=0> -->
<!--       		</a> -->
			<a href="Javascript:ListaAvvocatiRegInde('LoadModificaAvvocato');">
         		Seleziona da RegInde <img src="/images/filefolder.gif" border=0>
       		</a>
    	</td>
	</tr>
</table>
<table>
	<tr>
    	<td class="l" colspan=2>
    		<input type="hidden" value="<%=lAvv.getIdAvvocato()%>" name="idAvvVecchio">
    		<font class="label">Difensore da sostituire :  </font>&nbsp; 
    		<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getCognome())%> &nbsp; <%=StringUtils.toStringJSP(lAvv.getNome())%></font> &nbsp;&nbsp;
    		<font class="label">  foro di  </font> &nbsp;
    		<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getForo())%></font>
    	</td>
  	</tr>
  	<tr>
		<td class="l">Cognome</td>
    	<td class="l">
    		<%-- input type="hidden" value="<%=lAvv.getIdAvvocato()%>" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>"> --%>
    		<input type="hidden" value="" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>">
    		<input size=35 maxlength=35 title="Campo Cognome" type="text" readonly name="<%=ICostantiAvvocato.CAMPO_COGNOME%>">
    	</td>
	</tr>
  	<tr>
		<td class="l">Nome</td>
    	<td class="l">
    		<input size=35 maxlength=35 title="Campo Nome" type="text" readonly name="<%=ICostantiAvvocato.CAMPO_NOME%>">
    	</td>
  	</tr>
	<%-- MEV_21: aggiunti campi per chiamata a WS per individuare lista avvocato in RegInde --%>
<%
String comuneNascita = "", comuneNascitaEstero = "";
if (Utils.isPresent(lAvv.getDescStatoNascita())) {
	if ("ITALIA".equalsIgnoreCase(lAvv.getDescStatoNascita()))
		comuneNascita = lAvv.getDescLuogoNascita();
	else
		comuneNascitaEstero = lAvv.getDescLuogoNasRegInde();
} else if (Utils.isPresent(lAvv.getDescLuogoNascita())) {
	comuneNascita = lAvv.getDescLuogoNascita();
}
%>
 	<tr>
        <td class="l">Comune di Nascita</td>
        <td class="L">
          	<input title="Comune di Nascita" readonly type="text" name="<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>" maxlength="35" size="35" onChange="cancellaCodComuneReale();">
        	<a href="Javascript:ListaComuniNascita('LoadModificaAvvocato','<%= ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA %>');" >
				<img src="/images/filefolder.gif" border=0>
			</a>
        </td>
	</tr>
	<tr>
        <td class="l">Stato di Nascita</td>
		<td class="L">
          	<select disabled="disabled" name="<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>" size="1"><%=nazione%></select>
        </td>
	</tr>
  	<tr>
        <td class="l">Luogo di Nascita Estero</td>
        <td class="l">
			<input title="Luogo di Nascita Estero" readonly type="text" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>" maxlength="35" size="35">
		</td>
	</tr>
	<tr>
		<td class="l">Data di nascita</td>
       	<td class="L">
			<input type="text" readonly value ="" title="Giorno Data di nascita" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly value ="" title="Mese Data di nascita" name="<%= ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" readonly value ="" title="Anno Data di nascita" name="<%= ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Foro <font class=ob>(*)</font></td>
		<td class="l">
          	<select disabled="disabled" name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1"><%=foro%></select>
		</td> 
    </tr>
  	<tr>
		<td class="l">Indirizzo</td>
      	<td class="l">
      		<input type="text" readonly size=80 maxlength=200 title="Indirizzo" name="<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>">
      	</td>
  	</tr>
  	<%-- MEV_21: modificato campo per chiamata a WS per individuare lista avvocato in RegInde --%>
  	<tr>
        <td class="l">Con Studio in</td>
        <td class="L">
			<input type="text" readonly title="Comune Sede dello Studio" name="<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadModificaAvvocato','<%= ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO %>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
        </td>
    </tr>
    <tr>
		<td class="l">Telefono</td>
		<td class="l">
			<input type="text" readonly size=12 maxlength=12 title="Telefono" name="<%=ICostantiAvvocato.CAMPO_TELEFONO%>">
		</td>
    </tr>
    <tr>
		<td class="l">Fax</td>
		<td class="l">
			<input type="text" readonly size=12 maxlength=12 title="Fax" name="<%=ICostantiAvvocato.CAMPO_FAX%>">
		</td>
    </tr>
    <tr>
		<td class="l">e-mail</td>
		<td class="l"><input type="text" readonly size=50 maxlength=50 title="e-mail" name="<%=ICostantiAvvocato.CAMPO_E_MAIL%>"></td>
	</tr>
   	<%-- MEV_21: aggiunto campo per chiamata a WS per individuare lista avvocato in RegInde --%>
    <tr>
		<td class="l">pec</td>
		<td class="l"><input type="text" readonly size=50 maxlength=50 title="pec" name="<%=ICostantiAvvocato.CAMPO_PEC%>"></td>
	</tr>
	<tr>
		<td class="l">Codice Fiscale</td>
		<td class="l">
			<input type="text" readonly size=20 maxlength=16 readonly title="Codice Fiscale" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>">
		</td>
	</tr>
	<tr>
		<td class="l">Stato Difensore</td>
		<td class="L">
           	<select disabled="disabled" title="Stato Difensore" name="<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>"><%=statoAvv%></select>
		</td>
	</tr>
	<tr>
		<td class="l">Tipo Difensore <font class=ob>(*)</font></td>
		<td class="L">
           	<select title="attività" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO%>" onChange="caricamento();"><%=tipoAvvocato%></select>
		</td>
	</tr>
    <tr><td>&nbsp;</td></tr>
</table>

<div id="fiducia" style="visibility:hidden; position:relative; top:-50px; left:330px;">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="label">Nomina in Data</td>
		<td>
			<input type="text" title="Giorno Data di Nomina" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA%>" value="<%=DateUtils.getDayToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Mese Data di Nomina" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA%>" value="<%=DateUtils.getMonthToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Anno Data di Nomina" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA%>" value="<%=DateUtils.getYearToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	</td>
	   	<!-- MEV 15 - Revisione SIGE -->
		<td>
			<a href="javascript:calendario('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_NOMINA%>','<%=ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_NOMINA%>','<%=ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_NOMINA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
		</td>
	</tr>
</table>
</div>

<div id="ufficio" style="visibility:hidden; position:relative; top:-90px; left:320px;">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="label">Designato in Data</td>
       	<td>
            <input type="text" title="Giorno Data di Designazione" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getDayToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Mese Data di Designazione" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getMonthToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="2" size="2"onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input type="text" title="Anno Data di Designazione" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE%>" value="<%=DateUtils.getYearToString(avvocato.getAvvocatoFascicoloSigeModel().getDataInizioValidita())%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       	</td>
	   	<!-- MEV 15 - Revisione SIGE -->
	   	<td>
			<a href="javascript:calendario('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSige.CAMPO_ANNO_DATA_DESIGNAZIONE%>','<%=ICostantiAvvocatoFascicoloSige.CAMPO_MESE_DATA_DESIGNAZIONE%>','<%=ICostantiAvvocatoFascicoloSige.CAMPO_GIORNO_DATA_DESIGNAZIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
       	</td>
	</tr>
</table>
</div>
<div id="motivoDes" style="visibility:hidden; position:relative; top:-77px;">
<table>
	<tr>
		<td class="l">Motivo della Designazione</td>
		<td class="L" width="70%">
			<select title="designazione" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_MOTIVO_DESIGNAZIONE%>" onChange="cambiaMotivo();">
				<%=motivoDesignazione%>
		 	</select>
		</td>
	</tr>
</table>
</div>

<div id="note" style="visibility:hidden; position:relative; top:-80px;">
<table width="68%">
	<tr>
		<td class="l" width="30%">Note</td>
		<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_NOTE%>" cols=40 ></textarea>
		</td>
	</tr>
</table>
</div>

<div id="ufficioSotto" style="visibility:hidden; position:relative; top:-90px;">
<table>
	<tr><td>&nbsp;</td></tr>
	<tr><td class="Titolo" colspan='8'>Autorità per la notifica al Condannato</td></tr>
 	<tr>
		<!--autorità di polizia-->
		<td class="l" width=30%>Autorità</td>
		<td class="L" colspan="3">
		  	<select Title="Autorita Esterna" class="small" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA%>">
				<%=autoritaEsterna%>
		 	</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="L">
		  	<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA%>"  maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA%>');">
		   		<img src="/images/filefolder.gif" border=0>
		 	</a>
		</td>
    </tr>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L">
		   	<TEXTAREA title="Note" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_INDIRIZZO_TIPO_AUTORITA%>" cols=40></textarea>
		</td>
    </tr>
	<tr>
		<td class="l" width=30%>Istituto di Detenzione</td>
		<td class="l">
		   	<input readonly Title="Istituto" name="Comune" value="" size=50>
		   	<input type="hidden" Title="Istituto" name ="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>"size=50>
				<a href="Javascript:ListaIstitutoDetenzione('LoadModificaAvvocato','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
					<img src="/images/filefolder.gif" border=0>
				</a>
		</td>
 	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td class="Titolo" colspan='8'>Autorità per la notifica al Difensore</td></tr>
	<tr>
		<!--autorità di polizia-->
		<td class="l" width=30%>Autorità</td>
		<td class="L" colspan="3">
		  	<select Title="Autorita Esterna" class="small" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_TIPO_AUTORITA_DIF%>">
				<%=autoritaEsternaDif%>
		 	</select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="L">
		  	<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadModificaAvvocato','<%=ICostantiAvvocatoFascicoloSige.CAMPO_COD_SEDE_AUTORITA_DIF%>');">
		   		<img src="/images/filefolder.gif" border=0>
		 	</a>
		</td>
	</tr>
</table>
</div>

<div id="conferma" style="visibility:hidden; position:relative; top:-110px;">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td colspan=2>
        	<input class="bottone" id="confermaBtn" type="submit" value="Conferma" name="IA" onClick="Javascript:return EnableCombo();">
        </td>

		<!-- 20210611 MEV_21 -->
       	<td colspan=2 id="inserimento" style="visibility:hidden;">
       		<input class="bottone" type="button" value="Inserimento" name="IN" onClick="Javascript:Inserisci();">
       	</td>
	</tr>
</table>
</div>
<%-- <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>> --%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
<input type="HIDDEN" name="lTipoInserimento" value="reginde">
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadModificaAvvocato");
frmvalidator.addValidation("<%=ICostantiAvvocato.CAMPO_COGNOME%>","req","Il campo Cognome Avvocato è obbligatorio");
frmvalidator.addValidation("<%=ICostantiAvvocato.CAMPO_NOME%>","alpha");
frmvalidator.addValidation("<%=ICostantiAvvocato.CAMPO_FORO%>","req","Il campo Foro  è obbligatorio");
<%-- frmvalidator.addValidation("<%=ICostantiAvvocato.CAMPO_FORO%>","alpha"); --%>

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>