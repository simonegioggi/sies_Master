<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="autoritaemittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"               scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"            scope="request" class="java.lang.String"/>
<jsp:useBean id="posizione"               scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua"             scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="FlagLicenzeNonElaborate" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>
<%
String lAction = new String();

lAction = "siap.sico.libertaanticipata.action.ActInserisciLiberazioneAnticipata";

int NumColonne = ICostantiLibertaAnticipata.NUM_COLONNE_SEMESTRI;
int NumRighe = ICostantiLibertaAnticipata.NUM_RIGHE_SEMESTRI;
int NumTotale = ICostantiLibertaAnticipata.NUM_TOTALE_SEMESTRI;
int NumDate = ICostantiLibertaAnticipata.NUM_PERIODI;
int NumTotaleSemestri = NumRighe*NumColonne;
int NumCheck = 4 + NumTotaleSemestri;         /* numero complessivo dei check box */
//***  int NumCheck = 1 + NumTotaleSemestri;         /* numero complessivo dei check box */
int IndPer =  NumTotaleSemestri;     /* indice del check box relativo al Periodo unico */
int IndRig = 1 + NumTotaleSemestri;  /* indice del check box relativo a Periodi rigettati */
int IndIna = 2 + NumTotaleSemestri;  /* indice del check box relativo a Periodi inammissibili */
int IndNlp = 3 + NumTotaleSemestri;  /* indice del check box relativo a Periodi NLP */
%>

<html>
<head>
<title>[S.I.E.S.] - Liberazione Anticipata</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

 <script language="JavaScript">
var NumRighe = <%=NumRighe%>;             /* numero di righe gruppo semestri */
var NumColonne = <%=NumColonne%>;         /* numero di colonne gruppo semestri */
var NumTotale = <%=NumTotaleSemestri%>;   /* numero complessivo semestri  */
var NumDate = <%=NumDate%>;               /* numero totale gruppo di date  */
var giorni = new Array(NumTotale);        /* Array dei giorni concessi per semestre  */
var giorni_spe = new Array(NumTotale);	  /* 				totali concessi per L.A. SPECIALE	*/
var giorni_int = new Array(NumTotale);	  /* 				totali concessi per INTEGRAZIONE L.A.	*/
var NumCheck = <%=NumCheck%>;             /* numero complessivo dei check box */
var IndPer = <%=IndPer%>;
var IndRig = <%=IndRig%>;
var IndIna = <%=IndIna%>;
var IndNlp = <%=IndNlp%>;
var node;
var GioniConcessi = 0;
var GioniConcessi_spe = 0;
var GioniConcessi_int = 0;
var ColoreLA = "BLU";
var ColoreLS = "BLU";
var ColoreLI = "BLU";
var modalita = 'S' ;	/* modalità di scelat. S : Semestr C: periodo Complessivo */

function init() {
<%
if ("S".equals(FlagLicenzeNonElaborate)) {
%>
	alert('Attenzione vi sono ordinanze di Liberazione Anticipata già caricate e non elaborate. Premere su "Seleziona dalla lista" per verificarle');
<%
}
%>
	var i=0;
	for (i=0; i<NumTotale; i++)
		giorni[i] = 0;
	for (i=0; i<NumCheck; i++)
	   	uncheckDate(i);
	
	for (i=0; i<NumTotale; i++)
		giorni_spe[i] = 0;
	for (i=0; i<NumCheck; i++)
	   	uncheckDate_spe(i);
	
	for (i=0; i<NumTotale; i++)
		giorni_int[i] = 0;
	for (i=0; i<NumCheck; i++)
	   	uncheckDate_int(i);
	// All'inizio la Form è PreImpostata sull'OGGETTO 'Liberazione Anticipata' (COD. 2130)
	DisabilitaLA_INT();
	DisabilitaLA_SPE();
	AbilitaLA();
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value = 0;
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE %>[0].checked = true;
	document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
}

// 05/2014 --> Decreto Legge 2013/146 - Inserimento Ulteriori Oggetti : L.A. SPECIALE (Cod 2131) e INTEGRAZIONE L.A. (Cod 2132)

/* Abilita la modalità di selezione periodi concessi a seconda di L.A., L.A. speciale, Integrazione L.A. */
function QualeLiberazioneConcede(cod) {
	if (cod == 2130) { // E' stato selezionato il Radio Button L.A. NORMALE, quindi ...
		// ... Controllo i Totali di L.A. INTEGRAZIONE inseriti e ne disabilito le DIV
		var totLI = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
		var dateInseriteLI = 0;
		for (var TI = 0; TI <NumCheck*NumDate; TI ++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[TI].value != ""
				      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[TI].value != ""
				      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[TI].value != "") {
				dateInseriteLI ++;
			}
			if (dateInseriteLI != 0)
				break;
		}
		if (ColoreLI == "BLU" && dateInseriteLI == 0 && totLI == 0) {
			DisabilitaLA_INT();
		} else if (ColoreLI == "BLU") {
			if (dateInseriteLI == 0) {
				alert('Inserire periodo di Integrazione L.A.;  Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
				return false;
			}
		    if (dateInseriteLI != 0
		    		&& totLI == 0
		    		&& document.getElementById('SL_INT' + IndPer).style.color == 'red') {
				alert('Totale Giorni Concessi Integrazione L.A.\n non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
				return false;
			}
		    var ctr = "KO";
			if ((document.getElementById('SL_INT' + IndRig).style.color == 'red'
					|| document.getElementById('SL_INT' + IndIna).style.color == 'red'
					|| document.getElementById('SL_INT' + IndNlp).style.color == 'red')
					&& (totLI != 0)
					&& (document.getElementById('SL_INT' + IndPer).style.color != 'red')) {
				for (var kg = 0; kg < 12; kg++) {
					if (document.getElementById('SL_INT' + kg).style.color == 'red') {
						ctr = "OK";
						break;
					}
				}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione Integrazione L.A.');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
					return false;
				}
			}
			// Tutto OK! quindi disabilito L.A. Integrazione
			node = document.getElementById('LI');
			node.style.color = "Red";
			ColoreLI = "RED";
			DisabilitaLA_INT();
		} else {
			node = document.getElementById('LI');
			node.style.color = "Red";
			ColoreLI = "RED";
			DisabilitaLA_INT();
		}	
		// ... Controllo i Totali di L.A. SPECIALE inseriti e ne disabilito le DIV
		var totLS = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
		var dateInseriteLS = 0;
	    for (var TS = 0; TS <NumCheck*NumDate; TS ++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[TS].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[TS].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[TS].value != "") {
				dateInseriteLS ++;
			}
			if (dateInseriteLS != 0)
				break;
	    }
	    if (ColoreLS == "BLU" && dateInseriteLS == 0 && totLS == 0) {
			DisabilitaLA_SPE();
		} else if (ColoreLS == "BLU") {
			if (dateInseriteLS == 0) {
				alert('Inserire periodo di L.A. Speciale;  Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
				return false;
			}
		    if (dateInseriteLS != 0 && totLS == 0
		    		&& document.getElementById('SL_SPE' + IndPer).style.color == 'red') {
				alert('Totale Giorni Concessi L.A. Speciale\n non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
				return false;
			}
		    var ctr = "KO";
	    	if ((document.getElementById('SL_SPE' + IndRig).style.color == 'red'
	    			|| document.getElementById('SL_SPE' + IndIna).style.color == 'red'
	    			|| document.getElementById('SL_SPE' + IndNlp).style.color == 'red')
	    			&& (totLS != 0)
	    			&& (document.getElementById('SL_SPE' + IndPer).style.color != 'red')) {
	    		for (var kg = 0; kg < 12; kg++) {
	    			if (document.getElementById('SL_SPE' + kg).style.color == 'red') {
		    			ctr = "OK";
						break;
					}
				}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione L.A. Speciale');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
					return false;				    			
    			}
			}
			// Tutto OK! quindi disabilito L.A. Speciale
			node = document.getElementById('LS');
			node.style.color = "Red";
			ColoreLS = "RED";
			DisabilitaLA_SPE();
		} else {
			node = document.getElementById('LS');
			node.style.color = "Red";
			ColoreLS = "RED";
			DisabilitaLA_SPE();
		}	
		AbilitaLA();
		document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
		node = document.getElementById('LA');
		node.style.color = "Navy";
		ColoreLA = "BLU";
	} // chiude if (cod == 2130)	
	if (cod == 2131) { //  E' stato selezionato il Radio Button L.A. SPECIALE, quindi ...
		// ... Controllo i Totali di L.A. NORMALE inseriti e ne disbilito de DIV 
		var totLA = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
		var dateInserite = 0;
		for (var T=0; T<NumCheck*NumDate; T++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[T].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[T].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[T].value != "") {
				dateInserite++;
			}
			if (dateInserite != 0)
				break;
		}
		if (ColoreLA == "BLU" && dateInserite == 0 && totLA == 0) {
			DisabilitaLA();
		} else if (ColoreLA == "BLU") {
			if (dateInserite == 0) {
				alert('Inserire periodo di L.A.; Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
				return false;
			}
		    if (dateInserite != 0 && totLA == 0
		    		&& document.getElementById('SL' + IndPer).style.color == 'red') {
				alert('Totale Giorni Concessi L.A. non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
				return false;
			}
		    var ctr = "KO";
			if ((document.getElementById('SL' + IndRig).style.color == 'red'
					|| document.getElementById('SL' + IndIna).style.color == 'red'
					|| document.getElementById('SL' + IndNlp).style.color == 'red')
					&& (totLA != 0)
					&& (document.getElementById('SL' + IndPer).style.color != 'red')) {
				for (var kg = 0; kg < 12; kg++) {
					if (document.getElementById('SL' + kg).style.color == 'red') {
						ctr = "OK";
						break;
					}
				}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione L.A.');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
					return false;
	   			}
			}
			// Tutto OK! quindi disabilito L.A. Integrazione
			node = document.getElementById('LA');
			node.style.color = "Red";
			ColoreLA = "RED";
			DisabilitaLA();
		} else {	
			node = document.getElementById('LA');
			node.style.color = "Red";
			ColoreLA = "RED";
			DisabilitaLA();
		}	
		// ... Controllo i Totali di L.A. INTEGRAZIONE inseriti e ne disabilito le DIV
		var totLI = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
		var dateInseriteLI = 0;
		for (var TI = 0; TI < NumCheck*NumDate; TI ++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[TI].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[TI].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[TI].value != "") {
				dateInseriteLI ++;
			}
			if (dateInseriteLI != 0)
				break;
		}
		if (ColoreLI == "BLU" && dateInseriteLI == 0 && totLI == 0) {
			DisabilitaLA_INT();
		} else if (ColoreLI == "BLU") {
			if (dateInseriteLI == 0) {
				alert('Inserire periodo di Integrazione L.A.;  Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
				return false;
			}
		    if (dateInseriteLI != 0 && totLI == 0
		    		&& document.getElementById('SL_INT' + IndPer).style.color == 'red') {
				alert('Totale Giorni Concessi Integrazione L.A.\n non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
				return false;
			}
		    var ctr = "KO";
		    if ((document.getElementById('SL_INT' + IndRig).style.color == 'red'
		    		|| document.getElementById('SL_INT' + IndIna).style.color == 'red'
		    		|| document.getElementById('SL_INT' + IndNlp).style.color == 'red')
		    		&& (totLI != 0)
		    		&& (document.getElementById('SL_INT' + IndPer).style.color != 'red')) {
				for (var kg = 0; kg < 12; kg++) {
					if (document.getElementById('SL_INT' + kg).style.color == 'red') {
						ctr = "OK";
						break;
					}
				}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione Integrazione L.A.');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
					return false;
				}
			}				    
			// Tutto OK! quindi disabilito L.A. Integrazione
			node = document.getElementById('LI');
			node.style.color = "Red";
			ColoreLI = "RED";
			DisabilitaLA_INT();
		} else {
			node = document.getElementById('LI');
			node.style.color = "Red";
			ColoreLI = "RED";
			DisabilitaLA_INT();
		}
		AbilitaLA_SPE();
		document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
		node = document.getElementById('LS');
		node.style.color = "Navy";
		ColoreLS = "BLU";
	} // Chiudo if (cod == 2131)
	if (cod == 2132) { // E' stato selezionato il Radio Button INTEGRAZIONE L.A., quindi ...
		// ... Controllo i Totali di L.A. NORMALE inseriti e ne disbilito de DIV
		var totLA = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.value;
		var dateInserite = 0;
		for (var T = 0; T< NumCheck * NumDate; T++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[T].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[T].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[T].value != "") {
				dateInserite++;
			}
			if (dateInserite != 0)
				break;
		}
		if (ColoreLA == "BLU" && dateInserite == 0 && totLA == 0) {
			DisabilitaLA();
		} else if (ColoreLA == "BLU") {
			if (dateInserite == 0) {
				alert('Inserire periodo di L.A.; Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
				return false;
			}
		    if (dateInserite != 0 && totLA == 0
		    		&& document.getElementById('SL' + IndPer).style.color == 'red') {
				alert('Totale Giorni Concessi L.A. non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
				return false;
			}
		    var ctr = "KO";
			if ((document.getElementById('SL' + IndRig).style.color == 'red'
					|| document.getElementById('SL' + IndIna).style.color == 'red'
					|| document.getElementById('SL' + IndNlp).style.color == 'red')
					&& (totLA != 0)
					&& (document.getElementById('SL' + IndPer).style.color != 'red')) {
				for (var kg = 0; kg < 12; kg++) {
					if (document.getElementById('SL' + kg).style.color == 'red') {
		    			ctr = "OK";
		    			break;
					}
				}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione L.A.');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 0;
					return false;
				}
			}
		    // Tutto OK! quindi disabilito L.A. Normale
			node = document.getElementById('LA');
			node.style.color = "Red";
			ColoreLA = "RED";
			DisabilitaLA();
		} else {	
			node = document.getElementById('LA');
			node.style.color = "Red";
			ColoreLA = "RED";
			DisabilitaLA();
		}	
		// ... Controllo i Totali di L.A. SPECIALE inseriti e ne disabilito le DIV
		var totLS = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
		var dateInseriteLS = 0;
	    for (var TS = 0; TS <NumCheck*NumDate; TS ++) {
			if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[TS].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[TS].value != ""
					&& document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[TS].value != "") {
				dateInseriteLS ++;
			}
			if (dateInseriteLS != 0)
				break;
	    }
		if (ColoreLS == "BLU" && dateInseriteLS == 0 && totLS == 0) {
			DisabilitaLA_SPE();
		} else if (ColoreLS == "BLU") {	
			if (dateInseriteLS == 0) {
				alert('Inserire periodo di L.A. Speciale;  Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
				return false;
			}
		    if (dateInseriteLS != 0 && totLS == 0
		    		&& document.getElementById('SL_SPE' + IndPer).style.color == 'red')	{
				alert('Totale Giorni Concessi L.A. Speciale\n non può essere uguale a zero!\n Impossibile procedere');
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
				document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
				document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
				return false;
			}
		    var ctr = "KO";
	   		if ((document.getElementById('SL_SPE' + IndRig).style.color == 'red'
	   				|| document.getElementById('SL_SPE' + IndIna).style.color == 'red'
	   				|| document.getElementById('SL_SPE' + IndNlp).style.color == 'red')
	   				&& (totLS != 0)
	   				&& (document.getElementById('SL_SPE' + IndPer).style.color != 'red')) {
				for (var kg = 0; kg < 12; kg++) {
					if (document.getElementById('SL_SPE' + kg).style.color == 'red') {
						ctr = "OK";
						break;
		    		}
	    		}
	    		if (ctr == "KO") {
					alert('Azzerare Totale Giorni Concessi o \n Inserire periodi di Concessione L.A. Speciale');
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.focus();
					document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
					document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 1;
					return false;
				}
			}
			// Tutto OK! quindi disabilito L.A. Speciale
			node = document.getElementById('LS');
			node.style.color = "Red";
			ColoreLS = "RED";
			DisabilitaLA_SPE();
		} else {	
			node = document.getElementById('LS');
			node.style.color = "Red";
			ColoreLS = "RED";
			DisabilitaLA_SPE();
	    }
		AbilitaLA_INT();
		document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value = 2;
		node = document.getElementById('LI');
		node.style.color = "Navy";
		ColoreLI = "BLU";
	} // Chiude if (cod == 2132)
} // Chiudo function QualeliberazioneConcede()

// Oggetto : L.A.(Liberazione Anticipata)
function AbilitaLA() {
	node = document.getElementById("tipoconcessione");
	node.style.display = 'block';
	AbilitaPeriodo();
	AbilitaSemestri();
	if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
		AbilitaPeriodo();
	node = document.getElementById("resto");
	node.style.display = 'block';
	var valoreLA = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value;
	if (valoreLA == "")
		valoreLA="0";
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = valoreLA;
/*
	if (document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked)
		document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = false;
	else
		document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.readOnly = true;
*/	
}

function DisabilitaLA() {
	var valLA = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>.value = valLA;
	node = document.getElementById("tipoconcessione");
	node.style.display = 'none';
	node = document.getElementById("comune");
	node.style.display = 'none';
	node = document.getElementById("semestri");
	node.style.display = 'none';
	node = document.getElementById("periodo");
	node.style.display = 'none';
	node = document.getElementById("resto");
	node.style.display = 'none';
}
	
// Oggetto : L.A.S.(Liberazione Anticipata Speciale)
function AbilitaLA_SPE() {
	node = document.getElementById("tipoconcessione_spe");
	node.style.display = 'block';
	AbilitaPeriodo_SPE();
	AbilitaSemestri_SPE();  
	if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
		AbilitaPeriodo_SPE();
	node = document.getElementById("resto_spe");
	node.style.display = 'block';
	var valoreSPE = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value;
	if (valoreSPE == "")
		valoreSPE = "0";
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value = valoreSPE;
/*
	if (document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked)
	 	document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = false;
	else
		document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.readOnly = true;
*/
}

function DisabilitaLA_SPE() {
	var valSPE = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE%>.value;
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>.value = valSPE;
	node = document.getElementById("tipoconcessione_spe");
	node.style.display = 'none';
	node = document.getElementById("comune_spe");
	node.style.display = 'none';
	node = document.getElementById("semestri_spe");
	node.style.display = 'none';
	node = document.getElementById("periodo_spe");
	node.style.display = 'none';	
	node = document.getElementById("resto_spe");
	node.style.display = 'none';
}

// Oggetto : L.A.I.(Liberazione Anticipata Integrazione)
function AbilitaLA_INT() {
	node = document.getElementById("tipoconcessione_int");
	node.style.display = 'block';
	AbilitaPeriodo_INT();
	AbilitaSemestri_INT();
	if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
		AbilitaPeriodo_INT(); 
	node = document.getElementById("resto_int");
	node.style.display = 'block';        
	var valoreLA_INT = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>.value;
	if (valoreLA_INT == "")
		valoreLA_INT = "0";
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.value = valoreLA_INT;
/*
	if (document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>[1].checked)
   	 	document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = false;
    else
    	document.InserisciOrdinanzaLiberazioneAnticipata.< %=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT%>.readOnly = true;
*/
}

function DisabilitaLA_INT() {
	var valLA_INT = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT %>.value = valLA_INT;
	node = document.getElementById("tipoconcessione_int");
	node.style.display = 'none';
	node = document.getElementById("comune_int");
	node.style.display = 'none';
	node = document.getElementById("semestri_int");
	node.style.display = 'none';
	node = document.getElementById("periodo_int");
	node.style.display = 'none';
	node = document.getElementById("resto_int");
	node.style.display = 'none';
}
	
// Chiusura dell'eventuale blocco aperto
function Chiusura() {
	var retValue = true;
	for (var i = 0; i < NumCheck; i++) {
		if (document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].checked) {
		  	document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].checked = false;
			retValue = ViewLayer(i);
		}
		if (document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].checked) {
		   	document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].checked = false;
		   	retValue = ViewLayer_SPE(i);
		}
		if (document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].checked) {
		   	document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].checked = false;
		   	retValue = ViewLayer_INT(i);
		}
	}
	return retValue;
}

// L.A. NORMALE
// Disabilita i blocchi date vuoti e quelli della modalità non selezionata (Semestri/Periodo)
function DisabilitaDate() {
	var i = 0;
	for (i = 0; i < NumCheck; i++) {
		// Vengono disabilitati tutti i blocchi periodi vuoti
        if (!IsCheckedDate(i)) {
           	node = document.getElementById('L'+i);
           	node.disabled = true;
        }
	}
	if (!document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked) {
		for (i = 0; i < NumTotale; i++) {
          	node = document.getElementById('L'+i);
          	node.disabled = true;
        }
	} else {
		node = document.getElementById('L'+ IndPer);
        node.disabled = true;
	}
}

var DataFine;
var DataIni;

/* Controllo  e conteggio date */
function conteggioDate (id) {
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id < NumTotale)
          giorni[id] = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         DataFine = null;
         DataIni = null;

         if (leggiDate(elem) == false)
         {
           // alert ("Errore nelle date");
            retValue = false;
            periodo = 0;
            break;
         }
         else
         {
            // Solo sugli elementi Periodi Concessi
            if (id < NumTotale)
            {
               if (DataFine != null && DataIni != null)
               { // + 1 ?????
                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
               }
            }
         }
       }
       if (periodo != 0)
       {
/*
          if (periodo != 180)
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
*/
          if (retValue)
              giorni[id] = 45;
       }

       //alert("conteggioDate fine ");
       return retValue;
   }	// Chiude function conteggioDate (id)
	
	/* Abilita la modalità di selezione periodi concessi a semestri */
    function AbilitaSemestri()
    {
      var retValue = true;
      //alert("AbilitaSemestri: inizio");
	  
      if (modalita != 'S')
      {
         retValue = Chiusura();
         if (retValue)
         {
        	   node = document.getElementById("comune");
	           node.style.display = 'block';
	           node = document.getElementById("semestri");
	           node.style.display = 'block';
	           node = document.getElementById("periodo");
	           node.style.display = 'none';
	           // Salvataggio dei giorni concessi per il periodo unico
	           GioniConcessi = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value;
	           // Ripristino dei giorni concessi per semestre
	           modalita = 'S';
	           aggiornaTotGiorni();
         }
         else
         {
	           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = true;
	           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = false;
         }
      }

      // alert("AbilitaSemestri: fine");
      return retValue;
    }

/* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo()
    {
      var retValue = true;
      // alert("AbilitaPeriodo: inizio");
	  
      if (modalita != 'C')
      {
         retValue = Chiusura();
         if (retValue)
         {
	      	   node = document.getElementById("comune");
	           node.style.display = 'block';        	 
	           node = document.getElementById("semestri");
	           node.style.display = 'none';
	           node = document.getElementById("periodo");
	           node.style.display = 'block';
	           // Ripristino dei giorni concessi per periodo unico
	           modalita = 'C';
	           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = GioniConcessi;
         }
         else
         {
	           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[0].checked = true;
	           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>[1].checked = false;
         }
      }

      // alert("AbilitaPeriodo: fine");
      return retValue;
    }

    var node;
    var modalita = 'S' ;            /* modalità di scelat. S : Semestr C: periodo Complessivo */

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate (elem)
   {
    	//alert("leggiDate");
      var ret = true;
      var gg0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value);
      var mm0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value);
      var aa0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value);
      var mm1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value);
      var aa1 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value;
      var dataFine = gg1 + "/" + mm1 + "/" + aa1;

      var data_emissione = '<%=DateUtils.getDateToString(new Date(),"dd/MM/yyyy")%>';

      if (dataIni.length == 2)
      {
         if (dataFine.length != 2)
         {
           ret = false;
           alert ("Data di inizio periodo mancante");
         }
      }
      else if (dataFine.length == 2)
      {
        ret = false;
        alert ("Data di fine periodo mancante");
      }
      else if (ControllaData (dataIni) == false)
      {
        /* entrambe le date valorizzate */
        ret = false;
        alert ("Errore nella data : " + dataIni);
      }
      else if (ControllaData (dataFine) == false)
      {
        ret = false;
        alert ("Errore nella data : " + dataFine);
      }
      else if (CompareDate(dataIni,dataFine)== false)
      {
        ret = false;
        alert ("Data di Fine minore di  Data inizio periodo");
      }
      else if (CompareDate(dataFine, data_emissione)== false)
      {
        ret = false;
        alert ("Data di Fine maggiore di Data emissione");
      }
      else
      {
        /* OK Date presenti */
        DataIni = new Date(aa0, mm0-1, gg0);
        DataFine  = new Date(aa1, mm1-1, gg1);
        if (DataIni == null && DataFine == null)
        {
        	//alert ("fffffffffffffffffffff");
        	return false;
         }
        //alert("Data iniziale : " + DataIni.toString());
         //alert("Data finale : " + DataFine.toString());
      }
     return ret;
   }

//	-	-
    function aggiornaTotGiorni()
    {
      //alert("aggiornaTotGiorni(): inizio");
      if (modalita == 'S')
      {
	        var totale = 0;
	        for (var i=0; i<NumTotale; i++)
	          totale += giorni[i];
	        //alert ("Totale giorni concessi: " +totale);
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value = totale;
      }
      //alert("aggiornaTotGiorni(): fine");
   }
    
    function checkDate(id)
    {
       //alert("check id : " + id);
      if (id < NumTotale)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = true;
      }
      else if (id == IndPer)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = true;
      }
      else if (id == IndRig)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = true;
      }
      else if (id == IndIna)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = true;
      }
      else if (id == IndNlp)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = true;
      }

      //alert("check id : " + id);
    }

    function uncheckDate(id)
    {
      //alert ("uncheck " + id);

      if (id < NumTotale)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked = false;
      }
      else if (id == IndPer)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked = false;
      }
      else if (id == IndRig)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked = false;
      }
      else if (id == IndIna)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked = false;
      }
      else if (id == IndNlp)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked = false;
      }

      //alert ("uncheck " + id);
    }

    function IsCheckedDate(id)
    {
        var retValue = false;
       //alert ("IsCheckedDate " + id);

      if (id < NumTotale)
      {
       if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>[id].checked)
          retValue = true;
      }
      else if (id == IndPer)
      {
        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>.checked)
          retValue = true;
      }
      else if (id == IndRig)
      {
        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>.checked)
          retValue = true;
      }
      else if (id == IndIna)
      {
        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>.checked)
          retValue = true;
      }
      else if (id == IndNlp)
      {
        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>.checked)
          retValue = true;
      }

      //alert ("IsCheckedDate " + retValue);

      return retValue;
    }

/* L.A. NORMALE - Visualizzazione del layer Date corrispondente alla posizione id   */
   function ViewLayer(id)
   {
    //  alert ("ViewLayer id: " + id);
      // Check ON/OFF
      for (var i=0; i<NumCheck; i++)
      {
	        if (i != id && document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked)
	        {
	          document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].disabled=true;
	        }
	        else
	        {
	          document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].disabled=false;
	        }
      }
      // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
      for (var i=0; i<NumCheck; i++)
      {
         node = document.getElementById('L'+i);
         if (i == id && document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked)
         {
           // apertura
           //alert ("Sto aprendo " + i);
           // node.enabled = true;

           node.style.display = 'block';
         }
         else
         {
            if (i == id)
            {
               // chiusura
               //alert ("Sto chiudendo " + i);
                if (conteggioDate (i) == false)
                {
                 	/* Riposizionamento del check  */
                    document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked = true;
					//ViewLayer(id);

					// disabilita gli altri Check
            		for (var i=0; i<NumCheck; i++)
				    {
				    	if (i != id)
              			{
							  document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].disabled=true;
              			}
				    }
              		return false;
            	}
				else
        		{
					if (chkDateContigue(id) == false)
					{
					 	// Reinserisce i Check
      					for (var i=0; i<NumCheck; i++)
				      	{
				        	if (i == id)
				        	{
								document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked = true;
				        	}
				        	else
				        	{
								document.InserisciOrdinanzaLiberazioneAnticipata.gg[i].disabled=true;
				        	}
				      	}
						alert('Le date devono essere contigue.');
						return false;
					}
				}
            }

            node.style.display = 'none';
         }
       }
       // Colore dei check
       var blue=true;
       node = document.getElementById('SL'+id);
       var elem = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[elem].value=="");
         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>[elem].value=="");
       }
       if (blue)
       {
         //alert ("blue");
         node.style.color = "Navy";
         uncheckDate(id);
       }
       else
       {
         //alert ("red");
         node.style.color = "Red";
         checkDate(id);
       }

       aggiornaTotGiorni();

       //alert("ViewLayeer fine ");
       return true;
    }

	//CONTROLLO DATE CONTIGUE
	function chkDateContigue(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if (j==0)
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;

				if (gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem].value;
				var gg0rigaprima = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[elem-1].value;
				if (gg0 != '')
				{
					riga = true;
				}
				else
        {
					riga = false
				}

				if (gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if (rigaprima == false && riga == true){
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;
	//FINE - CONTROLLO DATE CONTIGUE
	}
//
//	END L.A. NORMALE			END L.A. NORMALE

////////////////////////////////////////////////////////////// 

//	-	-	-	 L.A. SPEECIALE	-	-	-	 L.A. SPECIALE	-	-	-	 L.A. SPECIALE	-	-	-	 L.A. SPECIALE	
//
   var DataFine;
   var DataIni;
   /* Controllo  e conteggio date */
   function conteggioDate_spe (id)
   {
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id < NumTotale)
          giorni_spe[id] = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
	         DataFine = null;
	         DataIni = null;
	
	         if (leggiDate_spe(elem) == false)
	         {
		            //alert ("Errore nelle date");
		            retValue = false;
		            periodo = 0;
		            break;
	         }
	         else
	         {
	            // Solo sugli elementi Periodi Concessi
		            if (id < NumTotale)
		            {
			               if (DataFine != null && DataIni != null)
			               { // + 1 ?????
			                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
			               }
		            }
	         }
       }
       //alert ("giorni = " + periodo);
       if (periodo != 0)
       {
/*
          if (periodo != 180)
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
*/
          if (retValue)
              giorni_spe[id] = 75;
       }

       //alert("conteggioDate fine ");
       return retValue;
   }	// Chiude function conteggioDate (id)	
//
    function checkDate_spe(id)
    {
       //alert("checkdate_spe id : " + id);
      if (id < NumTotale)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked = true;
      }
      else if (id == IndPer)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE%>.checked = true;
      }
      else if (id == IndRig)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE%>.checked = true;
      }
      else if (id == IndIna)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE%>.checked = true;
      }
      else if (id == IndNlp)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE%>.checked = true;
      }

      //alert("checkdate_spe - id : " + id);
    }

    function uncheckDate_spe(id)
    {
      //alert ("uncheckdate_spe - id " + id);

      if (id < NumTotale)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked = false;
      }
      else if (id == IndPer)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>.checked = false;
      }
      else if (id == IndRig)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>.checked = false;
      }
      else if (id == IndIna)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>.checked = false;
      }
      else if (id == IndNlp)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>.checked = false;
      }

      //alert ("uncheckdate_spe " + id);
    }
//    
   function IsCheckedDate_spe(id)
    {
	        var retValue = false;
	       //alert ("IsCheckedDate_spe " + id);
	
	      if (id < NumTotale)
	      {
	       if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>[id].checked)
	          retValue = true;
	      }
	      else if (id == IndPer)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>.checked)
	          retValue = true;
	      }
	      else if (id == IndRig)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>.checked)
	          retValue = true;
	      }
	      else if (id == IndIna)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>.checked)
	          retValue = true;
	      }
	      else if (id == IndNlp)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>.checked)
	          retValue = true;
	      }
	
	      //alert ("IsCheckedDate_spe " + retValue);
	
	      return retValue;
    }    
//
/* Abilita la modalità di selezione periodi concessi a semestri */
    function AbilitaSemestri_SPE()
    {
	   //   alert("AbilitaSemestri_spe: inizio - modalita = "+modalita);
	      var retValue = true;		  
	      if (modalita != 'S')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	   node = document.getElementById("comune_spe");
			           node.style.display = 'block';
			           node = document.getElementById("semestri_spe");
			           node.style.display = 'block';
			           node = document.getElementById("periodo_spe");
			           node.style.display = 'none';
			           // Salvataggio dei giorni concessi per il periodo unico
			           GioniConcessi_spe = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value;
			           // Ripristino dei giorni concessi per semestre
			           modalita = 'S';
			           aggiornaTotGiorni_SPE();
		         }
		         else
		         {
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[1].checked = true;
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>[0].checked = false;
		         }
	      }
	
	      // alert("AbilitaSemestri: fine");
	      return retValue;
    }

/* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo_SPE()
    {
	    //  alert("AbilitaPeriodo_SPE: inizio - modalita = "+modalita);
		var retValue = true;		  
	    if (modalita != 'C')
	    {
		         retValue = Chiusura();
		         if (retValue)
		         {
			      	   node = document.getElementById("comune_spe");
			           node.style.display = 'block';        	 
			           node = document.getElementById("semestri_spe");
			           node.style.display = 'none';
			           node = document.getElementById("periodo_spe");
			           node.style.display = 'block';
			           // Ripristino dei giorni concessi per periodo unico
			           modalita = 'C';
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = GioniConcessi_spe;
		         }
		         else
		         {
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked = true;
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[1].checked = false;
		         }
	    }
	
	      // alert("AbilitaPeriodo_SPE : fine");
	      return retValue;
    }

    var node;
    var modalita = 'S' ;            /* modalità di scelat. S : Semestr C: periodo Complessivo */
//
    // Disabilita i blocchi date vuoti e quelli della modalità non selezionata (Semestri/Periodo)

    function DisabilitaDate_spe()
    {
	    //  alert("DisabilitaDate_spe : inizio");
	      var i=0;
	      for (i=0; i < NumCheck; i++)
	      {
		       // Vengono disabilitati tutti i blocchi periodi vuoti
		        if (!IsCheckedDate_spe(i))
		        {
		           // alert("disabilito L" + i);
		           node = document.getElementById('L_SPE'+i);
		           node.disabled = true;
		           //alert("disabilitato L" + i);
		        }
	      }
	
	      if (!document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE %>[0].checked)
	      {
		        for (i=0; i < NumTotale; i++)
		        {
		          node = document.getElementById('L_SPE'+i);
		          node.disabled = true;
		          // alert("disabilitato L" + i);
		        }
	      }
	      else
	      {
		        node = document.getElementById('L_SPE'+ IndPer);
		        node.disabled = true;
		        //alert("disabilitato L" + IndPer);
	      }
	   //   alert("DisabilitaDate_spe: fine");
    }
//
    function aggiornaTotGiorni_SPE()
    {
    		var totale = 0;
      	//	alert("aggiornaTotGiorni_SPE(): inizio - modalita = "+modalita);
	      if (modalita == 'S')
	      {
		        for (var i=0; i<NumTotale; i++)
		        {	
		          	totale += giorni_spe[i];
		        	//alert ("Totale giorni concessi: i = " +i+ " - totale = " +totale);
		        }
		        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value = totale;
	      }
	      
	    //  alert("aggiornaTotGiorni_SPE(): fine - totale = "+totale);
   }
//	-	-	
/* L.A. SPECIALE  Visualizzazione del layer Date corrispondente alla posizione id   */

   function ViewLayer_SPE(id)
   {
      //alert ("ViewLayer_SPE -  id: " + id);
      // Check ON/OFF
      for (var i=0; i<NumCheck; i++)
      {
	        if (i != id && document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[id].checked)
	        {
	          document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].disabled=true;
	        }
	        else
	        {
	          document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].disabled=false;
	        }
      }
      // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
      for (var i=0; i<NumCheck; i++)
      {
	         node = document.getElementById('L_SPE'+i);
	         if (i == id && document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[id].checked)
	         {
	           // apertura
	           //alert ("Sto aprendo " + i);
	           // node.enabled = true;
	
	           node.style.display = 'block';
	         }
         	 else
         	 {
	            if (i == id)
	            {
	               // chiusura
	               //alert ("Sto chiudendo " + i);
	                if (conteggioDate_spe (i) == false)
	                {
	                 	/* Riposizionamento del check  */
	                    document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[id].checked = true;
						//ViewLayer(id);
	
						// disabilita gli altri Check
	            		for (var i=0; i<NumCheck; i++)
					    {
					    	if (i != id)
	              			{
								  document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].disabled=true;
	              			}
					    }
						
	              		return false;
	            	}
					else
	        		{
						if (chkDateContigue_spe(id) == false)
						{
					 	// Reinserisce i Check
	      					for (var i=0; i<NumCheck; i++)
					      	{
					        	if (i == id)
					        	{
									document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[id].checked = true;
					        	}
					        	else
					        	{
									document.InserisciOrdinanzaLiberazioneAnticipata.gg_SPE[i].disabled=true;
					        	}
					      	}
							alert('Le date devono essere contigue.');
							return false;
						}
						
					}	// Chiude else di if (conteggioDate (i) == false)
						
            	}	// chiude if (i == id)

           		node.style.display = 'none';
           		 
         	}	// Chiude la Else di if (i == id && document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked)
         		
       }	// Chiude ciclo for (var i=0; i<NumCheck; i++)
      
       // Colore dei check
       var blue=true;
       node = document.getElementById('SL_SPE'+id);
       var elem = 0;
       elem=id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[elem].value=="");
         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE%>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>[elem].value=="");
       }
       if (blue)
       {
         //alert ("blue");
         node.style.color = "Navy";
         uncheckDate_spe(id);
       }
       else
       {
         //alert ("red");
         node.style.color = "Red";
         checkDate_spe(id);
       }

       aggiornaTotGiorni_SPE();

       //alert("ViewLayeer fine ");
       return true;
    }
//

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate_spe (elem)
   {
      var ret = true;
      var gg0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE%>[elem].value);
      var mm0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[elem].value);
      var aa0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE%>[elem].value;
      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
      var gg1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE%>[elem].value);
      var mm1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE%>[elem].value);
      var aa1 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE%>[elem].value;
      var dataFine = gg1 + "/" + mm1 + "/" + aa1;

      var data_emissione = '<%=DateUtils.getDateToString(new Date(),"dd/MM/yyyy")%>';

      if (dataIni.length == 2)
      {
         if (dataFine.length != 2)
         {
           ret = false;
           alert ("Data di inizio periodo mancante");
         }
      }
      else if (dataFine.length == 2)
      {
        ret = false;
        alert ("Data di fine periodo mancante");
      }
      else if (ControllaData (dataIni) == false)
      {
        /* entrambe le date valorizzate */
        ret = false;
        alert ("Errore nella data : " + dataIni);
      }
      else if (ControllaData (dataFine) == false)
      {
        ret = false;
        alert ("Errore nella data : " + dataFine);
      }
      else if (CompareDate(dataIni,dataFine)== false)
      {
        ret = false;
        alert ("Data di Fine minore di  Data inizio periodo");
      }
      else if (CompareDate(dataFine, data_emissione)== false)
      {
        ret = false;
        alert ("Data di Fine maggiore di Data emissione");
      }
      else
      {
        /* OK Date presenti */
        DataIni = new Date(aa0, mm0-1, gg0);
        DataFine  = new Date(aa1, mm1-1, gg1);
        if (DataIni == null && DataFine == null)
        {
        	//alert ("fffffffffffffffffffff");
        	return false;
         }
        //alert("Data iniziale : " + DataIni.toString());
         //alert("Data finale : " + DataFine.toString());
      }
     return ret;
   }

//
	//CONTROLLO DATE CONTIGUE
	function chkDateContigue_spe(id)
	{
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem=id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if (j==0)
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value;

				if (gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem].value;
				var gg0rigaprima = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[elem-1].value;
				if (gg0 != '')
				{
					riga = true;
				}
				else
        		{
					riga = false
				}

				if (gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if (rigaprima == false && riga == true){
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;
	//FINE - CONTROLLO DATE CONTIGUE
	}
//	
//	END L.A. SPEECIALE			END L.A. SPEECIALE			END L.A. SPEECIALE			END L.A. SPEECIALE

//////////////////////////////////////////////////////////////

//-	-	-	 L.A. INTEGRAZIONE	-	-	-	 L.A. INTEGRAZIONE	-	-	-	 L.A. INTEGRAZIONE	-	-	-	 L.A. INTEGRAZIONE	
//
   var DataFine;
   var DataIni;
   /* Controllo  e conteggio date */
   function conteggioDate_int (id_int)
   {
	 //  alert("conteggioDate_int  - id_int = "+id_int)
       var retValue = true;
       var elem = 0;
       var periodo = 0;
       var data;

       if (id_int < NumTotale)
          giorni_int[id_int] = 0;
       
       elem = id_int*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
	         DataFine = null;
	         DataIni = null;
	
	         if (leggiDate_int(elem) == false)
	         {
		            //alert ("Errore nelle date");
		            retValue = false;
		            periodo = 0;
		            break;
	         }
	         else
	         {
	            // Solo sugli elementi Periodi Concessi
		            if (id_int < NumTotale)
		            {
			               if (DataFine != null && DataIni != null)
			               { // + 1 ?????
			                  periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));
			               }
		            }
	         }
       }
       //alert ("giorni = " + periodo);
       if (periodo != 0)
       {
/*
          if (periodo != 180)
          {
             retValue = confirm("Le date inserite individuano un periodo di " + periodo +" giorni e non di 180. Confermi comunque la concessione del semestre?");
          }
*/
          if (retValue)
              giorni_int[id_int] = 30;
       }

       //alert("conteggioDate_int fine ");
       return retValue;
   }	// Chiude function conteggioDate (id_int)	
//
    function checkDate_int(id)
    {
       //alert("checkdate_int - inizio - id : " + id);
      if (id < NumTotale)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked = true;
      }
      else if (id == IndPer)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT%>.checked = true;
      }
      else if (id == IndRig)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT%>.checked = true;
      }
      else if (id == IndIna)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT%>.checked = true;
      }
      else if (id == IndNlp)
      {
        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT%>.checked = true;
      }

      //alert("checkdate_int - fine - id : " + id);
    }

    function uncheckDate_int(id)
    {
      //alert ("uncheckdate_int - inizio - id " + id);

	      if (id < NumTotale)
	      {
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked = false;
	      }
	      else if (id == IndPer)
	      {
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>.checked = false;
	      }
	      else if (id == IndRig)
	      {
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>.checked = false;
	      }
	      else if (id == IndIna)
	      {
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>.checked = false;
	      }
	      else if (id == IndNlp)
	      {
	        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>.checked = false;
	      }

      //alert ("uncheckdate_int - fine - id = " + id);
    }
//    
   function IsCheckedDate_int(id)
    {
	        var retValue = false;
	       //alert ("IsCheckedDate_int - inizio - id = " + id);
	
	      if (id < NumTotale)
	      {
	       if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>[id].checked)
	          retValue = true;
	      }
	      else if (id == IndPer)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>.checked)
	          retValue = true;
	      }
	      else if (id == IndRig)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>.checked)
	          retValue = true;
	      }
	      else if (id == IndIna)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>.checked)
	          retValue = true;
	      }
	      else if (id == IndNlp)
	      {
	        if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>.checked)
	          retValue = true;
	      }
	
	      //alert ("IsCheckedDate_int - fine - retvalue = " + retValue);
	
	      return retValue;
    }    
//
/* Abilita la modalità di selezione periodi concessi a semestri */
    function AbilitaSemestri_INT()
    {
	   //   alert("AbilitaSemestri_INT: inizio - modalita = "+modalita);
	      var retValue = true;		  
	      if (modalita != 'S')
	      {
		         retValue = Chiusura();
		         if (retValue)
		         {
		        	   node = document.getElementById("comune_int");
			           node.style.display = 'block';
			           node = document.getElementById("semestri_int");
			           node.style.display = 'block';
			           node = document.getElementById("periodo_int");
			           node.style.display = 'none';
			           // Salvataggio dei giorni concessi per il periodo unico
			           GioniConcessi_int = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value;
			           // Ripristino dei giorni concessi per semestre
			           modalita = 'S';
			           aggiornaTotGiorni_INT();
		         }
		         else
		         {
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked = true;
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked = false;
		         }
	      }
	
	      // alert("AbilitaSemestri_INT  fine - modalita = "+modalita);
	      return retValue;
    }

/* Abilita la modalità di selezione periodi concessi a periodo unico */
    function AbilitaPeriodo_INT()
    {
	    //  alert("AbilitaPeriodo_INT: inizio - modalita = "+modalita);
		var retValue = true;		  
	    if (modalita != 'C')
	    {
		         retValue = Chiusura();
		         if (retValue)
		         {
			      	   node = document.getElementById("comune_int");
			           node.style.display = 'block';        	 
			           node = document.getElementById("semestri_int");
			           node.style.display = 'none';
			           node = document.getElementById("periodo_int");
			           node.style.display = 'block';
			           // Ripristino dei giorni concessi per periodo unico
			           modalita = 'C';
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = GioniConcessi_int;
		         }
		         else
		         {
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked = true;
			           document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[1].checked = false;
		         }
	    }
	
	      // alert("AbilitaPeriodo_INT : fine");
	      return retValue;
    }

    var node;
    var modalita = 'S' ;            /* modalità di scelat. S : Semestr C: periodo Complessivo */
//
    // Disabilita i blocchi date vuoti e quelli della modalità non selezionata (Semestri/Periodo)

    function DisabilitaDate_int()
    {
	   //  alert("DisabilitaDate_int: inizio");
	      var i=0;
	      for (i=0; i < NumCheck; i++)
	      {
		       // Vengono disabilitati tutti i blocchi periodi vuoti
		        if (!IsCheckedDate_int(i))
		        {
			           // alert("disabilito L_INT"+i);
			           node = document.getElementById('L_INT'+i);
			           node.disabled = true;
			           //alert("disabilitato L_INT" + i);
		        }
	      }
	
	      if (!document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT %>[0].checked)
	      {
		        for (i=0; i < NumTotale; i++)
		        {
			          node = document.getElementById('L_INT'+i);
			          node.disabled = true;
			          // alert("disabilitato L_INT"+i);
		        }
	      }
	      else
	      {
		        node = document.getElementById('L_INT'+IndPer);
		        node.disabled = true;
		        //alert("disabilitato L_INT"+IndPer);
	      }
	
	   //   alert("DisabilitaDate_int: fine");
    }
//
    function aggiornaTotGiorni_INT()
    {
    		var totale = 0;
      	//	alert("aggiornaTotGiorni_INT(): inizio - modalita = "+modalita);
	      if (modalita == 'S')
	      {
		        for (var i=0; i<NumTotale; i++)
		        {	
		          	totale += giorni_int[i];
		        	//alert ("Totale giorni concessi: i = " +i+ " - totale = " +totale);
		        }
		        document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value = totale;
	      }
	      
	    //  alert("aggiornaTotGiorni_INT(): fine - totale = "+totale);
   }
//	-	-	
/* L.A. INTEGRAZIONE  Visualizzazione del layer Date corrispondente alla posizione id   */

   function ViewLayer_INT(id)
   {
      //alert ("ViewLayer_INT Inizio -  id: "+ id);
      // Check ON/OFF
      for (var i=0; i<NumCheck; i++)
      {
	        if (i != id && document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[id].checked)
	        {
	          	document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].disabled=true;
	        }
	        else
	        {
	          	document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].disabled=false;
	        }
      }
      // Apertura o chiusura del campo date e controllo correttezza date in chiusura */
      for (var i=0; i<NumCheck; i++)
      {
	         node = document.getElementById('L_INT'+i);
	         if (i == id && document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[id].checked)
	         {
		           // apertura
		           //alert ("Sto aprendo INT " + i);
		           node.style.display = 'block';
	         }
         	 else
         	 {
	            if (i == id)
	            {
	               // chiusura
	               //alert ("Sto chiudendo INT  " + i);
	                if (conteggioDate_int(i) == false)
	                {
	                 	/* Riposizionamento del check  */
	                    document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[id].checked = true;
						//ViewLayer(id);
	
						// disabilita gli altri Check
	            		for (var i=0; i<NumCheck; i++)
					    {
					    	if (i != id)
	              			{
								  document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].disabled=true;
	              			}
					    }
						
	              		return false;
	            	}
					else
	        		{
						if (chkDateContigue_int(id) == false)
						{
					 	// Reinserisce i Check
	      					for (var i=0; i<NumCheck; i++)
					      	{
					        	if (i == id)
					        	{
									document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[id].checked = true;
					        	}
					        	else
					        	{
									document.InserisciOrdinanzaLiberazioneAnticipata.gg_INT[i].disabled=true;
					        	}
					      	}
							alert('Le date devono essere contigue.');
							return false;
						}
						
					}	// Chiude else di if (conteggioDate_int (i) == false)
						
            	}	// chiude if (i == id)

           		node.style.display = 'none';
           		 
         	}	// Chiude la Else di if (i == id && document.InserisciOrdinanzaLiberazioneAnticipata.gg[id].checked)
         		
       }	// Chiude ciclo for (var i=0; i<NumCheck; i++)
      
       // Colore dei check
       var blue=true;
       node = document.getElementById('SL_INT'+id);
       var elem = 0;
       elem = id*NumDate;
       for (var j=0; j<NumDate; j++, elem++)
       {
	         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value=="");
	         blue=blue && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value=="")  && (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value=="");
       }
       if (blue)
       {
	         //alert ("blue");
	         node.style.color = "Navy";
	         uncheckDate_int(id);
       }
       else
       {
	         //alert ("red");
	         node.style.color = "Red";
	         checkDate_int(id);
       }

       aggiornaTotGiorni_INT();

       //alert("ViewLayeer_INT fine ");
       return true;
    }
//

/* Lettura  e controllo del periodo di posizione elem  */
   function leggiDate_int(elem)
   {
	      var ret = true;
	      var gg0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT%>[elem].value);
	      var mm0 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[elem].value);
	      var aa0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[elem].value;
	      var dataIni = gg0 + "/" + mm0 + "/" + aa0;
	      var gg1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>[elem].value);
	      var mm1 = FillDM(document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>[elem].value);
	      var aa1 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>[elem].value;
	      var dataFine = gg1 + "/" + mm1 + "/" + aa1;
	
	      var data_emissione = '<%=DateUtils.getDateToString(new Date(),"dd/MM/yyyy")%>';
	
	      if (dataIni.length == 2)
	      {
	         if (dataFine.length != 2)
	         {
	           ret = false;
	           alert ("Data di inizio periodo mancante");
	         }
	      }
	      else if (dataFine.length == 2)
	      {
	        ret = false;
	        alert ("Data di fine periodo mancante");
	      }
	      else if (ControllaData (dataIni) == false)
	      {
	        /* entrambe le date valorizzate */
	        ret = false;
	        alert ("Errore nella data : " + dataIni);
	      }
	      else if (ControllaData (dataFine) == false)
	      {
	        ret = false;
	        alert ("Errore nella data : " + dataFine);
	      }
	      else if (CompareDate(dataIni,dataFine)== false)
	      {
	        ret = false;
	        alert ("Data di Fine minore di  Data inizio periodo");
	      }
	      else if (CompareDate(dataFine, data_emissione)== false)
	      {
	        ret = false;
	        alert ("Data di Fine maggiore di Data emissione");
	      }
	      else
	      {
		        /* OK Date presenti */
		        DataIni = new Date(aa0, mm0-1, gg0);
		        DataFine  = new Date(aa1, mm1-1, gg1);
		        if (DataIni == null && DataFine == null)
		        {
		        	//alert ("fffffffffffffffffffff");
		        	return false;
		        }
		        //alert("Data iniziale : " + DataIni.toString());
		        //alert("Data finale : " + DataFine.toString());
	      }
	     	return ret;
	     	
   }	// Chiude function leggiDate_int(elem)

//
	//CONTROLLO DATE CONTIGUE
	function chkDateContigue_int(id)
	{
	 //  alert("chkDateContigue_int - id = "+id);
		var returnchkDC = true;

		var rigaprima = false;
		var riga = false;

		elem = id*NumDate;
		for (var j=0; j<NumDate; j++, elem++)
		{
			if (j==0)
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value;

				if (gg0 != '')
				{
					riga = true;
				}
			}
			else
			{
				var gg0 = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem].value;
				var gg0rigaprima = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[elem-1].value;
				if (gg0 != '')
				{
					riga = true;
				}
				else
        		{
					riga = false
				}

				if (gg0rigaprima != '')
				{
					rigaprima = true;
				}
				else{
					rigaprima = false
				}

				if (rigaprima == false && riga == true)
				{
					returnchkDC = false;
				}
			}
		}
		return returnchkDC;
	//FINE - CONTROLLO DATE CONTIGUE
	}
//	
//	END L.A. INTEGRAZIONE			END L.A. INTEGRAZIONE			END L.A. INTEGRAZIONE			END L.A. INTEGRAZIONE
//
// - 	-	-	-	-	-	-	-	-	
//
	function Verify()
  {
		//alert("NumRighe"+NumRighe);
		//alert("NumDate"+NumDate);
		//alert("NumTotale"+NumTotale);
		//alert("NumCheck*NumDate"+NumCheck*NumDate);

		//DATA EMISSIONE obbligatoria
		if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
		  	document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
		  document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

	    var data_emissione = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	
	    if (!ControllaData(data_emissione) )
	    {
		      alert('Data emissione non valida');
		      document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();
		
		      return false;
	    }
	
			// CONTROLLO CAMPO SEDE
		if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE%>.value == "")
	    {
	      alert('Sede Autorità emittente obbligatoria');
	      return false;
	    }

		//DATA EMISSIONE ORDINANZA (non-obbligatoria)
		if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value.length==1)
	  		document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value='0'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value;
		if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>.value.length==1)
	  		document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>.value='0'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>.value;

		var data_emissione_ordinanza = document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value+'/'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>.value+'/'+document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA%>.value;

		if (!ControllaDataPassaVuota(data_emissione_ordinanza) )
		{
		      alert('Data emissione ordinanza non valida');
		      document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.focus();
		
		      return false;
		}

		var indiceChekato = document.InserisciOrdinanzaLiberazioneAnticipata.<%= ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO %>.value;
//  Se al <CONFERMA> è selezionato OGGETTO L.A. di indice[0] (COD 2130)....	

		if (indiceChekato == 0)
		{	
			var dateInserite = 0;	
		    for (var T=0; T<NumCheck*NumDate; T++)
		    {
			      if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>[T].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>[T].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>[T].value != "")
			      {
			        	dateInserite++;
			      }
			      
			      if (dateInserite!= 0)
			        	break;
		    }
	
		    if (dateInserite == 0)
		    {
		      	alert('Inserire periodo di L.A.; Impossibile procedere');
		        return false;
		    }
		
		    var retValue = true;
		    retValue = Chiusura();

		    if (retValue)
		    {	
		    	DisabilitaDate();
		    	DisabilitaDate_spe();
		    	DisabilitaDate_int();
		    }	
	
// Se è stato inserito almeno un periodo,
// i giorni concessi di L.A.  possono essere uguali a zero, solo se sono stati selezionati esclusivamente periodi non concessi.
//
// N.B. Sui giorni "per semestri" viene aggiornato automaticamente il contatore di 45, quindi va controllato solo il caso "unico periodo" 
//  >>> 05/2014 NO!!! ----- > perchè il 45 può essere azzerato a mano SE NON E' PRESENTE L'ATTRIBUTO 'ReadOnly' SUL CAMPO !!!
			
			if ( dateInserite != 0
			    && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>.value == 0
			    && document.getElementById('SL' + IndPer).style.color == 'red'	)
			{
					alert('Totale Giorni Concessi L.A. non può essere uguale a zero!\n Impossibile procedere');
				  	return false;
			}
			
		}
		//  Se al <CONFERMA> è selezionato OGGETTO L.A. SPECIALE di indice[1] (COD 2131)....	
		else if (indiceChekato == 1)		
		{	
			var dateInseriteLS = 0;	
		    for (var TS = 0; TS < NumCheck*NumDate; TS ++)
		    {
			      if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>[TS].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>[TS].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>[TS].value != "")
			      {
			        	dateInseriteLS ++;
			      }
			      
			      if (dateInseriteLS != 0)
			        	break;
		    }
	
		    if (dateInseriteLS == 0)
		    {
		      	alert('Inserire periodo di L.A.SPECIALE; Impossibile procedere');
		        return false;
		    }
		
		    var retValue = true;
		    retValue = Chiusura();
		    
		    if (retValue)
		    {	
		    	DisabilitaDate_spe();
		    	DisabilitaDate_int();
		    	DisabilitaDate();
		    }	
	
// Se è stato inserito almeno un periodo,
// i giorni concessi di L.A. Speciale possono essere uguali a zero, solo se sono stati selezionati esclusivamente periodi non concessi.
//
// N.B. Sui giorni "per semestri" viene aggiornato automaticamente il contatore di 75, quindi va controllato solo il caso "unico periodo" 
//  >>> 05/2014 NO!!! ----- > perchè il 75 può essere azzerato a mano SE NON E' PRESENTE L'ATTRIBUTO 'ReadOnly' SUL CAMPO !!!

			
			if ( dateInseriteLS != 0
			    && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>.value == 0
			    && document.getElementById('SL_SPE' + IndPer).style.color == 'red')	
			{
					alert('Giorni Concessi L.A.SPECIALE non può essere uguale a zero!\n Impossibile procedere');
				  	return false;
			}
			
		}
		//  Se al <CONFERMA> è selezionato OGGETTO L.A. INTEGRAZIONE di indice[2] (COD 2132)....	
		else if (indiceChekato == 2)		
		{	
			var dateInseriteLI = 0;	
		    for (var TI = 0; TI < NumCheck*NumDate; TI ++)
		    {
			      if (document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>[TI].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>[TI].value != ""
			      && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>[TI].value != "")
			      {
			        	dateInseriteLI ++;
			      }
			      
			      if (dateInseriteLI != 0)
			        	break;
		    }
	
		    if (dateInseriteLI == 0)
		    {
		      	alert('Inserire periodo di INTEGRAZIONE L.A.; Impossibile procedere');
		        return false;
		    }
		
		    var retValue = true;
		    retValue = Chiusura();
		    
		    if (retValue)
		    {	
		    	DisabilitaDate_int();
		    	DisabilitaDate_spe();
		    	DisabilitaDate();
		    }	
	
// Se è stato inserito almeno un periodo,
// i giorni concessi di Integrazione L.A. possono essere uguali a zero, solo se sono stati selezionati esclusivamente periodi non concessi.
//
// N.B. Sui giorni "per semestri" viene aggiornato automaticamente il contatore di 30, quindi va controllato solo il caso "unico periodo" 
//  >>> 05/2014 NO!!! ----- > perchè il 30 può essere azzerato a mano SE NON E' PRESENTE L'ATTRIBUTO 'ReadOnly' SUL CAMPO !!!
			
			if ( dateInseriteLI != 0
			    && document.InserisciOrdinanzaLiberazioneAnticipata.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>.value == 0
			    && document.getElementById('SL_INT' + IndPer).style.color == 'red')	
			{
					alert('Giorni Concessi di INTEGRAZIONE L.A. non può essere uguale a zero!\n Impossibile procedere');
				  	return false;
			}
			
		}

	//	DisabilitaDate();
	//	DisabilitaDate_spe();
	//	DisabilitaDate_int();
		//alert("Verify: fine");
		return retValue;
  
  } // Chiude function Verify()


/* **
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
** */

    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaLicenzeAnticipate(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaLiberazioneAnticipata&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=posizione.getFasSieIdFascicoloSiep()%>", "Lista_Licenze_Anticipate", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=500, height=500");
    }
  </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>  

</head>

  <body class="corpo" onLoad="Javascript:init();">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza di Liberazione Anticipata</font>&nbsp;
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaLiberazioneAnticipata">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=8>
        <font class="campo">
          <%=StringUtils.toStringJSP(posizione.getDescrPosizioneGiuridica())%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" colspan="2">
      <input type="hidden" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>">
        <a href="Javascript:ListaLicenzeAnticipate('InserisciOrdinanzaLiberazioneAnticipata');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
    </tr>
    <tr>
			<td class="l">Anno / Numero SIUS</td>
			<td class="l">
				<input Title="Anno Fascicolo Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
				/
				<input Title="Numero Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>" type="text" size="6" maxlength="6">
			  &nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;
        Anno / Numero Ordinanza
        &nbsp;&nbsp;&nbsp;&nbsp;
				<input Title="Anno Ordinanza" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_ORDINANZA%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
				/
				<input Title="Numero Ordinanza" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_ORDINANZA%>" type="text" size="6" maxlength="6">
			</td>
		</tr>
    <tr>
    <td class="l">
      Autorità Emittente
      <input type="hidden" name="ritorno" value="">
    </td>

    <td class="l">
      <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE, autoritaemittente)%>&nbsp;
      Sede <font class="ob">(*)</font>&nbsp;
      <input title="Sede Autorita Esterna"  type="text" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuniEmitUTMinor('InserisciOrdinanzaLiberazioneAnticipata','<%=ICostantiLicenzaLibanticipata.CAMPO_COD_LUOGO_EMITTENTE%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l"> Data Emissione Ordinanza </td>
    <td class="l">
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>" <%=IWebConstants.UTIL_DATA%> >
        -
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiLicenzaLibanticipata.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>" <%=IWebConstants.UTIL_DATA%>>
        -
        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA%>"  <%=IWebConstants.UTIL_DATA_ANNO%>>
      </font>
    </td>
  </tr>
</table>
<br>
<table width="100%">
	<tr>
		<td class="L">
			<font class="label" style="text-align: center; color:green; font-size: 10pt">
			 Selezionare gli Oggetti L.A. di interesse (uno per volta) e inserire i rispettivi semestri/periodi
			</font>
		</td>
	</tr>
</table> 

<table cellspacing="2" cellpadding="2" style="width: 90%;">
	<tr>
		<td width="30%" class="l">
			<span id="LA" style="color=blu;font-weight:bold;"> Liberazione Anticipata  </span> 
			<input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>"  onclick="Javascript:return QualeLiberazioneConcede('2130');">
		</td>
		<td width="30%" class="l">
			<span id="LS" style="color=blu;font-weight:bold;"> Liberazione Anticipata Speciale </span> 
			<input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" onclick="Javascript:return QualeLiberazioneConcede('2131');">
		</td>
		<td width="30%" class="l">
			<span id="LI" style="color=blu;font-weight:bold;"> Integrazione Liberazione Anticipata </span> 
			<input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" onclick="Javascript:return QualeLiberazioneConcede('2132');">
		</td>
	</tr>
</table>
<br>
<!--  < < < < < <  INSERISCI LIBERAZIONE ANTICIPATA  NORMALE - L.A.  45gg 	> > > > > > >	-->
<div id="tipoconcessione" style="position: relative; top: 0; left: 0;">
    <table>
      <tr>
        <td class="l"> Modalità di scelta dei periodi di Concessione L.A.</td>
      </tr>
      <tr>
        <td>
          <input value="S" onclick="Javascript:return AbilitaSemestri();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>"  CHECKED> per semestri &nbsp;
          <input value="C" onclick="Javascript:return AbilitaPeriodo();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE%>"> unico periodo
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
</div>		<!--  chiude DIV id="tipoconcessione" -->    

<div id="comune" style="position: relative; top: 0; left: 0;">
  <div id="semestri" style="position: relative; top: 0; left: 0;">
    <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
        <td class="Titolo" colspan=6> L.A.: Semestri concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=26%>
<%
      for (int i=0;i<NumRighe;i++)
      {
%>
        <tr>
<%
          for (int j=0;j<NumColonne;j++)
          {
%>
		    <td width=3%><span id="SL<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 45</span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=i*NumColonne+j%>');"></td>
<%
          }
%>
        </tr>
<%
      }
%>
     </table>
  </div>		<!--  chiude DIV id="semestri" -->
  
  <div id="periodo" style="position: relative; top: 0; left: 0; display:none; " >
    <table cellspacing="2" cellpadding="2" width=26%>
      <tr>
        <td class="Titolo" colspan=6> L.A.: Periodo concesso&nbsp;&nbsp;&nbsp; </td>
      </tr>
    </table>
    <table width=26%>
      <tr>
       <td width=3%><span id="SL<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo  </span> <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndPer%>');"></td>
      </tr>
    </table>
  </div>		<!--  chiude DIV id="periodo" -->
  
    <table width=35%>
    <tr>
      <td class="l">Totale giorni di L.A. Concessi </td>
      <td class="l">
        <input Title="TotGiorni" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA%>" value="" size="5" maxlength="4">
      </td>
    </tr>
  </table> 

  <br>
<div id="resto" style="position: relative; top: 0; left: 0;" >
  <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
      <td class="Titolo" colspan=6> L.A.: Periodi NON concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
  </table>
  <table width=35%>
    <tr>
      <td width=10%><span id="SL<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati    </span>
        <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndRig%>');">
      </td>
      <td width=10%><span id="SL<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span>
        <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndIna%>');">
      </td>
      <td width=10%><span id="SL<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span>
        <input type=checkbox name=gg value=1 onclick="Javascript:ViewLayer('<%=IndNlp%>');">
      </td>
    </tr>
  </table>
</div>  	<!-- CHIUDE DIV id='resto' -->  

<%
for (int i=0; i<NumCheck; i++)
{
%>
<div id="L<%=i%>" style="position: relative; top: -170; left: 500; display:none;" >
  <table>

<!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
<%
   for (int k=0; k<NumDate; k++)
   {
%>
      <tr>
        <td class="l">
			Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
            Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
      </tr>
<%
   }	// chiude ciclo for (int k=0; k<NumDate; k++)
%>
  </table>
<%  
     if (i < NumTotaleSemestri )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI%>" value=1 style="display:none;">
<%
     }
     else if (i == IndPer)
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO%>" value=1 style="display:none;">
<%
     }
     else if (i == (IndRig))
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI%>" value=1 style="display:none;">
<%
     }
     else if (i == ( IndIna) )
     {
%>
        <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI%>" value=1 style="display:none;">
<%

     }
     else if (i == ( IndNlp) )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP%>" value=1 style="display:none;">
<%
     }
%>
</div>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- chiude DIV id="L<%=i%>" --%>

<%
  }	// chiude ciclo for (int i=0; i<NumCheck; i++)
%>

</div>	<!--  CHIUDE DIV id='comune' -->

<!-- < > -->

<!--  < < < < < <  INSERISCI LIBERAZIONE ANTICIPATA  SPECIALE - L.A.S. 75gg 	> > > > > > >	-->
<div id="tipoconcessione_spe" style="position: relative; top: 0; left: 0;">
    <table>
      <tr>
        <td class="l"> Modalità di scelta dei periodi di Concessione L.A. Speciale</td>
      </tr>
      <tr>
        <td>
          <input value="S" onclick="Javascript:return AbilitaSemestri_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>"  CHECKED> per semestri &nbsp;
          <input value="C" onclick="Javascript:return AbilitaPeriodo_SPE();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_SPE%>"> unico periodo
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
</div>		<!--  CHIUDE DIV id= "tipoconcessione_spe"-->
    
<div id="comune_spe" style="position: relative; top: 0; left: 0;">
  <div id="semestri_spe" style="position: relative; top: 0; left: 0;">
    <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
        <td class="Titolo" colspan=6>L.A. Speciale: Semestri concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=26%>
<%
      for (int i=0;i<NumRighe;i++)
      {
%>
        <tr>
<%
          for (int j=0;j<NumColonne;j++)
          {
%>
		    <td width=3%><span id="SL_SPE<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 75</span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=i*NumColonne+j%>');"></td>
<%
          }
%>
        </tr>
<%
      }
%>
     </table>
  </div>		<!--  CHIUDE DIV id= "semestri_spe"-->
  
  <div id="periodo_spe" style="position: relative; top: 0; left: 0; display:none; " >
    <table cellspacing="2" cellpadding="2" width=26%>
      <tr>
        <td class="Titolo" colspan=6>L.A. Speciale: Periodo concesso&nbsp;&nbsp;&nbsp; </td>
      </tr>
    </table>
    <table width=26%>
      <tr>
       <td width=3%><span id="SL_SPE<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo  </span> <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndPer%>');"></td>
      </tr>
    </table>
  </div>		<!--  chiude DIV id="periodo_spe" -->
  
    <table width=35%>
    <tr>
      <td class="l">Totale giorni di L.A. Speciale Concessi </td>
      <td class="l">
        <input Title="TotGiorni_LA_speciale" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_SPE %>" value="" size="5" maxlength="4">
      </td>
    </tr>
  </table> 
  
<br>
<div id="resto_spe" style="position: relative; top: 0; left: 0;" >
  <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
      <td class="Titolo" colspan=6>L.A. Speciale: Periodi NON concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
  </table>
  <table width=35%>
    <tr>
      <td width=10%><span id="SL_SPE<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati    </span>
        <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndRig%>');">
      </td>
      <td width=10%><span id="SL_SPE<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span>
        <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndIna%>');">
      </td>
      <td width=10%><span id="SL_SPE<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span>
        <input type=checkbox name=gg_SPE value=1 onclick="Javascript:ViewLayer_SPE('<%=IndNlp%>');">
      </td>
    </tr>
  </table>
</div>  	<!-- CHIUDE DIV id='resto_spe' -->  
  
<%
for (int i=0; i<NumCheck; i++)
{
%>
<div id="L_SPE<%=i%>" style="position: relative; top: -170; left: 500; display:none;" >
  <table>

<!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
<%
   for (int k=0; k<NumDate; k++)
   {
%>
      <tr>
        <td class="l">
			Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_SPE %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_SPE %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_SPE %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
            Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_SPE %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_SPE %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_SPE %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
      </tr>
<%
   } // chiude ciclo for (int k=0; k<NumDate; k++)	
%>
  </table>
<%  
     if (i < NumTotaleSemestri )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_SPE %>" value=1 style="display:none;">
<%
     }
     else if (i == IndPer)
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_SPE %>" value=1 style="display:none;">
<%
     }
     else if (i == (IndRig))
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_SPE %>" value=1 style="display:none;">
<%
     }
     else if (i == ( IndIna) )
     {
%>
        <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_SPE %>" value=1 style="display:none;">
<%

     }
     else if (i == ( IndNlp) )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_SPE %>" value=1 style="display:none;">
<%
     }
%>
</div>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- chiude DIV id="L_SPE<%=i%>" --%>

<%
  }	// chiude ciclo for (int i=0; i<NumCheck; i++)
%>
  </div>	<!--  CHIUDE DIV id='comune_spe' -->

<!-- < > -->
 
 <!--  < < < < < <  INSERISCI LIBERAZIONE ANTICIPATA  INTEGRAZIONE - L.A.I. 30 gg 	> > > > > > >	-->
 
<div id="tipoconcessione_int" style="position: relative; top: 0; left: 0;">
    <table>
      <tr>
        <td class="l"> Modalità di scelta dei periodi di Concessione Integrazione L.A. </td>
      </tr>
      <tr>
        <td>
          <input value="S" onclick="Javascript:return AbilitaSemestri_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>"  CHECKED> per semestri &nbsp;
          <input value="C" onclick="Javascript:return AbilitaPeriodo_INT();" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_TIPO_CONCESSIONE_INT%>"> unico periodo
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
</div>		<!--  CHIUDE DIV id= "tipoconcessione_int"-->
    
<div id="comune_int" style="position: relative; top: 0; left: 0;">
  <div id="semestri_int" style="position: relative; top: 0; left: 0;">
    <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
        <td class="Titolo" colspan=6>Integrazione L.A.: Semestri concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
    </table>
    <table width=26%>
<%
      for (int i=0;i<NumRighe;i++)
      {
%>
        <tr>
<%
          for (int j=0;j<NumColonne;j++)
          {
%>
		    <td width=3%><span id="SL_INT<%=i*NumColonne+j%>" style="color=navy;font-weight:bold;">Giorni 30</span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=i*NumColonne+j%>');"></td>
<%
          }
%>
        </tr>
<%
      }
%>
     </table>
  </div>		<!--  CHIUDE DIV id= "semestri_int"-->
  
  <div id="periodo_int" style="position: relative; top: 0; left: 0; display:none; " >
    <table cellspacing="2" cellpadding="2" width=26%>
      <tr>
        <td class="Titolo" colspan=6>Integrazione L.A.: Periodo concesso&nbsp;&nbsp;&nbsp; </td>
      </tr>
    </table>
    <table width=26%>
      <tr>
       <td width=3%><span id="SL_INT<%=IndPer%>" style="color=navy;font-weight:bold;">Periodo  </span> <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndPer%>');"></td>
      </tr>
    </table>
  </div>		<!--  chiude DIV id="periodo_int" -->
  
    <table width=35%>
    <tr>
      <td class="l">Totale giorni di Integrazione L.A. Concessi </td>
      <td class="l">
        <input Title="TotGiorni_LA_integrazione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_LIBANTICIPATA_INT %>" value="" size="5" maxlength="4">
      </td>
    </tr>
  </table> 
  
<br>
<div id="resto_int" style="position: relative; top: 0; left: 0;" >
  <table cellspacing="2" cellpadding="2" width=26%>
    <tr>
      <td class="Titolo" colspan=6>Integrazione L.A.: Periodi NON concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
  </table>
  <table width=35%>
    <tr>
      <td width=10%><span id="SL_INT<%=IndRig%>" style="color=navy;font-weight:bold;">Rigettati    </span>
        <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndRig%>');">
      </td>
      <td width=10%><span id="SL_INT<%=IndIna%>" style="color=navy;font-weight:bold;">Inammissibili </span>
        <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndIna%>');">
      </td>
      <td width=10%><span id="SL_INT<%=IndNlp%>" style="color=navy;font-weight:bold;">N.L.P./N.D.P. </span>
        <input type=checkbox name=gg_INT value=1 onclick="Javascript:ViewLayer_INT('<%=IndNlp%>');">
      </td>
    </tr>
  </table>
</div>  	<!-- CHIUDE DIV id='resto_int' -->  
  
<%
for (int i=0; i<NumCheck; i++)
{
%>
<div id="L_INT<%=i%>" style="position: relative; top: -170; left: 500; display:none;" >
  <table>

<!-- COSTRUZIONE DEI CAMPI DATA DAL - AL -->
<%
   for (int k=0; k<NumDate; k++)
   {
%>
      <tr>
        <td class="l">
			Dal
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
            Al
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
			/
			<input type="text" name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
      </tr>
<%
   } // chiude ciclo for (int k=0; k<NumDate; k++)	
%>
  </table>
<%  
     if (i < NumTotaleSemestri )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_CONCESSI_INT %>" value=1 style="display:none;">
<%
     }
     else if (i == IndPer)
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_PERIODO_INT %>" value=1 style="display:none;">
<%
     }
     else if (i == (IndRig))
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_RIGETTATI_INT %>" value=1 style="display:none;">
<%
     }
     else if (i == ( IndIna) )
     {
%>
        <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_INAMMISSIBILI_INT %>" value=1 style="display:none;">
<%

     }
     else if (i == ( IndNlp) )
     {
%>
       <input type=checkbox name="<%=ICostantiLibertaAnticipata.CAMPO_CHECK_NLP_INT %>" value=1 style="display:none;">
<%
     }
%>
</div>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- chiude DIV id="L_INT<%=i%>" --%>

<%
  }	// chiude ciclo for (int i=0; i<NumCheck; i++)
%>
  </div>	<!--  CHIUDE DIV id='comune_int' -->
   
  <br>
   	<table cellspacing="2" cellpadding="2" width="90%">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
    </tr>
    <tr><td>&nbsp;</td></tr>
--%>
	    <tr>
	      <td>
	        <input class="bottone" type="submit" name="bottConferma" value="Conferma">
	      </td>
	    </tr>
  	</table>
  	
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_SPE%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_SALVA_GIORNI_LA_INT%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_SELEZIONATO%>" value="" >
    <input type="HIDDEN" name="<%=ICostantiLibertaAnticipata.CAMPO_QUALE_IND_SELEZIONATO%>" value="" >
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>--%>
	<input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_ESITO_TENORE %>" value="" >
<%
	for(int konta = 0; konta < 3; konta++)
	{	
%>	
		<input type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="" >
<%	} %>		

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaLiberazioneAnticipata");

<%
	// CONTROLLO PER I CAMPI ANNO DATE DAL AL SEMESTRI/PERIODI
	int cont = 0;
	String mex = "";
	for (int i=0; i<NumCheck; i++)
	{
		//for (int x=0; x< NumCheck*NumDate; x++)
		for (int x=0; x< NumDate; x++)
   		{
			// Costruzione dei messaggi di errore in base alla sezione Semestri o Periodi
			if (i<12)
			{
				mex ="Semestri Concessi: sezione "+ (i+1) +"\\n\\n";
			}
			else if (i == 12)
			{
				mex ="Periodo Concesso: \\n\\n";
			}
			else if (i == 13)
			{
				mex ="Periodi Non Concessi: Rigettati \\n\\n";
			}
			else if (i == 14)
			{
				mex ="Periodi Non Concessi: Inammissibili \\n\\n";
			}
			else if (i == 15)
			{
				mex ="Periodi Non Concessi: N.L.P./N.D.P. \\n\\n";
			}

			else
			{
				mex ="";
			}
	%>
			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>","<%=cont%>","numeric");
			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>","<%=cont%>","gt=1900","<%=mex%> Il valore del campo Anno Data Inizio dovrebbe essere maggiore di 1900");

			frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>","<%=cont%>","numeric");
		  	frmvalidator.addValidationWithIdx("<%= ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>","<%=cont%>","gt=1900","<%=mex%> Il valore del campo Anno Data Fine dovrebbe essere maggiore di 1900");
	<%
		 cont = cont + 1;
		}
  	}
	%>



    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>