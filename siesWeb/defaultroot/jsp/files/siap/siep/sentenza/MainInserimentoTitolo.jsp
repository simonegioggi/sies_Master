<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProvvedimenti" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProvvedimentiAltro" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagSN" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="pageInclude" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Gestione Sentenza</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

// 09/06/2010 Lista Uffici per TIPO_UFFICIO
function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function Verify() {
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
		document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
		document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value.length==1)
		document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value.length==1)
		document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value;
 	// Data Sentenza
	var d2 = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
	if (!ControllaData(d2)) {
		alert("Data Sentenza non valida");
		return false;
	}
	// Data Sentenza di Riferimento
	var d3 = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
	if (!ControllaData(d3) && d3.length > 2) {
        alert('Data Sentenza di riferimento non valida');
		return false;
    }
    if (document.LoadInserisciSentenza.TipoRG[document.LoadInserisciSentenza.TipoRG.selectedIndex].value == '-') {
		alert("Il Tipo Registro Generale e' obbligatorio");
		document.LoadInserisciSentenza.TipoRG.focus();
      	return false;
    }
	/**
	 * Nel caso in cui l'utente inserisca almeno uno tra i seguenti campi:
	 *  - Tipo Sentenza di Riferimento
	 *  - Data Sentenza di Riferimento
	 *  - Autorita' Sentenza di Riferimento
	 *  - Luogo Sentenza di Riferimento
	 *  deve inserirli tutti
	 */
	var TipoSentRif = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value;
	var GGSentRif   = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.value;
	var MMSentRif   = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.value;
	var AASentRif   = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.value;
	var TipoAutRif  = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.value;
	var SedeRif     = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.value;
	var CodTipoProvv= document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value;
	if (TipoSentRif != '-' || GGSentRif != '' || MMSentRif != '' || AASentRif != '' || TipoAutRif != '-' || SedeRif != '') {
		/* inizio modifica marzo 2010
		 if(CodTipoProvv=='53') {
		 	return true;
		 }
		fine  modifica marzo 2010 */    	
	  	if (TipoSentRif == '-' && CodTipoProvv != '53') {
		    alert('Dati della Sentenza di Riferimento Incompleti - - Tipo Sentenza');
		    document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
			return false;
		}
		if (GGSentRif == '') {
			alert('Dati della Sentenza di Riferimento Incompleti - - Giorno Sentenza');
			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF %>.focus();
			return false;
		}
		if (MMSentRif == '') {
			alert('Dati della Sentenza di Riferimento Incompleti - - Mese Sentenza');
			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF %>.focus();
			return false;
		}
		if (AASentRif == '') {
			alert('Dati della Sentenza di Riferimento Incompleti - Anno Sentenza');
			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF %>.focus();
			return false;
		}
		if (TipoAutRif == '-') {
			alert("Dati della Sentenza di Riferimento Incompleti - Autorita' Emittente");
			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_PROVV_RIF %>.focus();
			return false;
		}
		if (SedeRif == '') {
			alert("Dati della Sentenza di Riferimento Incompleti - Luogo Autorita' Emittente");
			document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF %>.focus();
			return false;
	     }
	}
   	/* 
	controllo coerenza date sentenza realizzato inanalogia a quanto sviluppato per i webservices
	(vedi siap.sico.webservices.action.ActNscToSiesLoadSentenza.java)
	*/
	var auEmi = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;
	var dRif = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value
		+ '/' + document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>.value
		+ '/' + document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>.value;
	var gRif = document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>.value;
	if (gRif != null && gRif != '') {
		if (auEmi == "CAP"
				|| auEmi == "CAPMID"
				|| auEmi == "CASAP"
				|| auEmi == "CAPSM"
				|| auEmi == "CAPMI") {
			if (CompareDate(d2,dRif)) {
	        	alert('La Data Sentenza deve essere successiva alla Data della Sentenza di grado differente');
	        	return false;
      		}
		} else {
			if (CompareDate(dRif,d2)) {
	        	alert('La Data della Sentenza di grado differente deve essere successiva alla Data Sentenza');
	        	return false;
      		}
		}
	}
	return true;
}
  
function ctrl_autorita(idcmb1, idcmb2, idDiv, idTipoRito) {
	// cmb1 e' la combo che fa scattare la funzione
	var cmb1 = document.getElementById(idcmb1);	
	var cmb2 = document.getElementById(idcmb2);
		
	var arrCmb = new Array(cmb1, cmb2);
	var arrGrado = new Array();
	
 	for (var i = 0; i < arrCmb.length; i++) {
		if (arrCmb[i].value == "CSS") {
			arrGrado[i] = 3;
		} else if (arrCmb[i].value == "CAP"
				|| arrCmb[i].value == "CASAP"
				|| arrCmb[i].value == "CAPMI"
				|| arrCmb[i].value == "CAPSM"
				|| arrCmb[i].value == "CAPMID") {
			arrGrado[i] = 2;
		} else {
			arrGrado[i] = 1;
		}
	}
 	if (cmb1.value != "-" && cmb2.value != "-") {
		if (cmb1.value == cmb2.value) {
			alert("Non e' consentito selezionare due Autorita' Emittenti uguali!");
			cmb1.selectedIndex = 0;
			cmb1.focus();
		} else if (arrGrado[0] == arrGrado[1]) {			
			// eccezione per Giudice di Pace e Tribunale Ordinario (anche sezione distaccata)
			if (!(cmb1.value == "GP" && (cmb2.value == "DIB" || cmb2.value == "TRIBSD"))
					&& !(cmb2.value == "GP" && (cmb1.value == "DIB" || cmb1.value == "TRIBSD"))) {
				alert("Non e' consentito selezionare due Autorita' Emittenti dello stesso grado!");
				cmb1.selectedIndex = 0;
				cmb1.focus();
			}
		}
	}	
	var node = document.getElementById(idDiv);
	var cmbRito = document.getElementById(idTipoRito);
	if (cmb1.value == "DIB" || cmb1.value == "TRIBSD") {
		node.style.visibility = "visible";
	} else {
		node.style.visibility = "hidden";
		cmbRito.selectedIndex = 0;		
	}
}

function viewDiv(idDiv, aForm, aField, valueHidden) {
	var node = document.getElementById(idDiv);
   	var valF = eval('document.'+aForm+'.'+aField+'.value');
	if (idDiv == 'cassazione') {
		if (valF == valueHidden) {
			node.style.display = "none";
			document.getElementById('tipoSentenza').style.display="none";				
			document.getElementById('labelSentenza').style.display="none";
			document.getElementById('anSentenza').style.display="none";				
			document.getElementById('labelOrdinanza').style.display="block";
			document.getElementById('anOrdinanza').style.display="block";
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value=""');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
			eval('document.'+aForm+'.ANNOREGECAS.value=""');
			eval('document.'+aForm+'.NUMREGECAS.value=""');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value="-"');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value="-"');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>.value=""');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>.value=""');
		} else {
			node.style.display = "block";
			document.getElementById('tipoSentenza').style.display="block";
			document.getElementById('labelSentenza').style.display="block";
			document.getElementById('anSentenza').style.display="block";
			document.getElementById('labelOrdinanza').style.display="none";
			document.getElementById('anOrdinanza').style.display="none";
		}
	} else if (idDiv == '0') {
		if (valF == valueHidden) {						
			document.getElementById('anRegGen').style.display="none";				
			document.getElementById('anRacGen').style.display="none";				
			document.getElementById('disp').style.display="none";				
			document.getElementById('lblSentenza').style.display="none";
			document.getElementById('lblOrdinanza').style.display="block";
			// PULISCO I CAMPI
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>.value="-"');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>.value=""');
			eval('document.'+aForm+'.<%=ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>.value=""');
			eval('document.'+aForm+'.ANNOREGECAS.value=""');
			eval('document.'+aForm+'.NUMREGECAS.value=""');
		} else {
			document.getElementById('anRegGen').style.display="block";
			document.getElementById('anRacGen').style.display="block";
			document.getElementById('disp').style.display="block";
			document.getElementById('lblSentenza').style.display="block";
			document.getElementById('lblOrdinanza').style.display="none";
		}
	}
}

function ctrlDiv(){
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>.value == '53') {
		viewDiv('cassazione', 'LoadInserisciSentenza', '<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_RIF %>','53')
	}
	if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>.value == '53') {
		viewDiv('0', 'LoadInserisciSentenza', '<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO %>','53')
	}
}
</script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
<%
SentenzaModel lSentenza = new SentenzaModel();
String lAction = new String();
if (modalita.equals("I")) {
	lAction = "siap.siep.sentenza.action.ActInserisciSentenza";
%>
			<font class="campo">Inserimento Sentenza</font>
<%
} else if (modalita.equals("M")) {
	lAction = "siap.siep.sentenza.action.ActModificaSentenza";
	lSentenza = new SentenzaModel(sentenza);
%>
			<font class="campo">Modifica Sentenza</font>
<%
}
%>
		</td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenza">
<jsp:include page="<%=pageInclude%>"></jsp:include>
<table cellspacing=2 cellpadding=2>
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>
<input type="HIDDEN" name="Action" value="<%=lAction%>"> 
<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>" value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>"> <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
</form>
<script language="JavaScript" type="text/javascript">
// verifica iniziale per la sezione cassazione
ctrlDiv();

var frmvalidator  = new Validator("LoadInserisciSentenza");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il Giorno della data Sentenza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il Mese della data Sentenza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","L'Anno della data Sentenza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM %>","req","L'Anno Re.Ge PM e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>","req","Il Numero Re.Ge PM e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA %>","req","L'Anno Sentenza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il Numero sentenza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","alfanumeric");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","L'Autorita' Emittente e' obbligatoria");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il Luogo Emittente e' obbligatorio");
<%-- Ticket#202506190164 - SIEP - errore in fase di iscrizione per carattere non consentito --%>
<%-- frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic"); --%>

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","gt=1");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVV_RIF%>","lt=31");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","numeric");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","gt=1");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVV_RIF%>","lt=12");

frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","maxlen=4","La lunghezza massima per l'anno della data Sentenza di riferimento e' di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVV_RIF%>","gt=1900");

<%-- Ticket#202506190164 - SIEP - errore in fase di iscrizione per carattere non consentito --%>
<%-- frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_COD_LUOGO_PROVV_RIF%>","alphabetic"); --%>

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>