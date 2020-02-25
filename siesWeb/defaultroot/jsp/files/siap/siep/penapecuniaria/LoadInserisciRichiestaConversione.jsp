<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.sius.titoloesecutivo.action.ICostantiTitoloEsecutivo"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="AutoritaCompetente" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaConv" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="FascSiepTrovato" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<%-- MAC20191202018-MG-13/12/2019: modificato il tipo oggetto da String a BigDecimale--%>
<jsp:useBean id="multaResidua" scope="request" class="java.math.BigDecimal"/>
<jsp:useBean id="ammendaResidua" scope="request" class="java.math.BigDecimal"/>
<%-- FINE MAC20191129017-MG-04/12/2019: aggiunti oggetti --%>
<%
//==============================================================================
// Form utilizzata per L'iscrizione Richiesta conversione 
// ( assegna un n. procedimento Classe VII) 
// - Conversione Pene Pecuniarie 
//==============================================================================

// 30/01/2015 MEV023 Conversioni Pene Pecuniarie
int lFasPro = 0;
if (fascicolo!=null && fascicolo.getChiaveProgr()!=null)
		lFasPro = fascicolo.getChiaveProgr().intValue();

String aIdFascicolo = "";
String aChiaveAnno = "";
String aChiaveProgr = "";
String aChiaveUfficio = "";
String aSedeUfficio = "";
Date aDataProvvedimento = null;
String aAnnoSentenza = "";
String aNumeroSentenza = "";
Date aDataIrrevocabilità = null;
String aNote = "";
String aCognomeRif = "";
String aNomeRif = "";
String aComuneNasRif = "";
String aDataNasRif = "";
String aCognome = "";
String aNome = "";
String aComuneNas = "";
String aDataNas = "";
String lAction = "siap.siep.penapecuniaria.action.ActRicercaFascicoloSiep";
	
if (FascSiepTrovato.getChiaveAnno() != null) {
	aChiaveAnno = FascSiepTrovato.getChiaveAnno().toString();
	aChiaveProgr = FascSiepTrovato.getChiaveProgr().toString();
	aIdFascicolo = FascSiepTrovato.getIdFascicoloSiep().toString();
	aChiaveUfficio = FascSiepTrovato.getChiaveUfficio();
	aSedeUfficio = FascSiepTrovato.getDescrComuneUfficio();
	aDataProvvedimento = FascSiepTrovato.getSentenza().getDataProvvedimento();
	LuogoUtenteConnesso = FascSiepTrovato.getSentenza().getDescrLuogoEmittente();
	// MEV_39: aggiunto controllo di consistenza
	if (FascSiepTrovato.getSentenza().getAnnoSentenza() != null)
		aAnnoSentenza = FascSiepTrovato.getSentenza().getAnnoSentenza().toString();
	aNumeroSentenza = FascSiepTrovato.getSentenza().getNumeroSentenza();
	// modifica conseguente alla variazione di SentenzaModel - Vincenzo 21/10/2010
	aDataIrrevocabilità = FascSiepTrovato.getDataIrrevocabilita();
	aNote = "";
	aCognomeRif = FascSiepTrovato.getSoggetto().getCognome();
	aNomeRif = FascSiepTrovato.getSoggetto().getNome();
	aComuneNasRif = FascSiepTrovato.getSoggetto().getDescrComuneNascita();
	// Ticket#20190814013 [SG]: aggiunto controllo di consistenza
	if (FascSiepTrovato.getSoggetto().getDataNascita() != null)
		aDataNasRif = FascSiepTrovato.getSoggetto().getDataNascita().toString();
  	lAction = "siap.siep.penapecuniaria.action.ActRicercaFascicoloSiep";	  
} else {
	aIdFascicolo = null;
	aDataProvvedimento = null;
	LuogoUtenteConnesso = "";
	aAnnoSentenza = "";
	aNumeroSentenza = "";
	aDataIrrevocabilità = null;
	aNote = "";
}

// Controllo soggetti
if (fascicolo.getSoggetto() != null) {
	aCognome = fascicolo.getSoggetto().getCognome();
	aNome = fascicolo.getSoggetto().getNome();
	aComuneNas = fascicolo.getSoggetto().getDescrComuneNascita();
	// Ticket#20190814013 [SG]: aggiunto controllo di consistenza
	if (fascicolo.getSoggetto().getDataNascita() != null)
		aDataNas = fascicolo.getSoggetto().getDataNascita().toString();
}
%>

<html>
<head>
<title>Iscrizione Richiesta Conversione</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript">
//============================================================================
// Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
//============================================================================
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUffici(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaDistretti(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ChoosePopup() {
	var selectTipoUfficio = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>;
	var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
	var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
	if (codTipoUfficio == 'PM' || codTipoUfficio == 'PMM'){
	 ListaUffici('LoadInserisciRichiestaConversione','<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>');
	} else if (codTipoUfficio == 'PGCAP'){
		ListaDistretti('LoadInserisciRichiestaConversione','<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>');
	}
}

function onload() {
	var fasPro = <%=lFasPro %>;
   	if (fasPro > 70000 && fasPro < 80000) {
 		radioClasseI();
 		// Controllo soggetto.
    	var soggettoOri = "<%=aCognome%>"+"<%=aNome%>"+"<%=aComuneNas%>"+"<%=aDataNas%>";
		var soggettoRif = "<%=aCognomeRif%>"+"<%=aNomeRif%>"+"<%=aComuneNasRif%>"+"<%=aDataNasRif%>";
   		if (soggettoRif.length > 1) {
   			if (soggettoOri == soggettoRif) {
				document.LoadInserisciRichiestaConversione.CONFERMA.disabled=false;
		     	document.LoadInserisciRichiestaConversione.idFascicoloClasseI.value="<%=aIdFascicolo %>";
				// Impostazione action di inserimento.
				document.LoadInserisciRichiestaConversione.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penapecuniaria.action.ActInserisciRichiestaConversione";
			} else {
 				if (!confirm("Soggetto del Titolo Esecutivo differente dal soggetto del procedimento SIEP! Si vuole continuare?")) {
			 		document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
			     	document.LoadInserisciRichiestaConversione.idFascicoloClasseI.value="";
 				} else {
					document.LoadInserisciRichiestaConversione.CONFERMA.disabled=false;
			     	document.LoadInserisciRichiestaConversione.idFascicoloClasseI.value="<%=aIdFascicolo %>";
					// Impostazione action di inserimento.
					document.LoadInserisciRichiestaConversione.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penapecuniaria.action.ActInserisciRichiestaConversione";
      			}
    		}
		} else {
	 		document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
	     	document.LoadInserisciRichiestaConversione.idFascicoloClasseI.value="";
		}
		loadUfficiAccorpatiByDesc();
	} else {
    	// Impostazione action di inserimento.
    	document.LoadInserisciRichiestaConversione.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penapecuniaria.action.ActInserisciRichiestaConversione";		  
  	}
}


  
//30/01/2015 Controllo campi Fascicolo SIEP collegato
function VerifyRicerca() {
  var fasPro = <%=lFasPro %>;
	if (fasPro > 70000 && fasPro < 80000) {
    // Impostazione action di ricerca.
    document.LoadInserisciRichiestaConversione.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penapecuniaria.action.ActRicercaFascicoloSiep";
	if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length < 4
			|| document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value < 1900
			|| isNaN(document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value)) {
      alert ("Anno Fascicolo SIEP Non Valido");
      document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
      document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
      return false;
    }
	if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value.length <= 0
			|| document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value < 0
			|| isNaN(document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value)) {
      alert ("Numero Fascicolo SIEP Non Valido");
      document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.focus();
      document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
      return false;
    }
    // Controllo obbligatorietà autorità Competente ed Emittente.
    codUfficio = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value;
	if (codUfficio == "-") {
      alert("Il tipo autorità competente è un campo obbligatorio");
      document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
      document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
      return false;
    }
	if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value == "") {
      alert("Il luogo per l'autorità competente è un campo obbligatorio");
      document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
      document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
      return false;
    }
  }
  return true;
} 
    
function radioClasseI() {
    var nodeDatiClasseI;

    nodeDatiClasseI=document.getElementById('divDatiClasseI');
	if (document.LoadInserisciRichiestaConversione.tipo[0].checked) {
  	  nodeDatiClasseI.style.display='block';
        document.LoadInserisciRichiestaConversione.valoreRadio.value='0';
        document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
 	} else if (document.LoadInserisciRichiestaConversione.tipo[1].checked) {
  	  nodeDatiClasseI.style.display='none';
        document.LoadInserisciRichiestaConversione.valoreRadio.value='1';    	  
        document.LoadInserisciRichiestaConversione.CONFERMA.disabled=false;
    }
  }   
	 
	
	//Funzione utile per impostare la data corrente.
	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna) {		
	day=dataOdierna.substring(0,2);
	month=dataOdierna.substring(3,5);
	year=dataOdierna.substring(6,10);
	document.getElementsByName(campo_giorno).item(0).value = day;
	document.getElementsByName(campo_mese).item(0).value = month;
	document.getElementsByName(campo_anno).item(0).value = year;      
	}
	
	
	 

function Verify() {
	// 30/01/2015 Controllo campi Fascicolo SIEP collegato
	var fasPro = <%=lFasPro %>
	if (fasPro > 70000 && fasPro < 80000 && document.LoadInserisciRichiestaConversione.tipo[1].isChecked) {
		if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length < 4
				|| document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value < 1900
				|| isNaN(document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value)) {
			alert ("Anno Fascicolo SIEP Non Valido");
			document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
			document.LoadInserisciRichiestaConversione.CONFERMA.disabled = true;
			return false;
		}
		if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value.length <= 0
				|| document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value < 0
				|| isNaN(document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value)) {
			alert ("Numero Fascicolo SIEP Non Valido");
			document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.focus();
			document.LoadInserisciRichiestaConversione.CONFERMA.disabled = true;
			return false;
		}
		// Controllo obbligatorietà autorità Competente ed Emittente.
		codUfficio = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value;
		if (codUfficio == "-") {
			alert("Il tipo autorità competente è un campo obbligatorio");
			document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
			document.LoadInserisciRichiestaConversione.CONFERMA.disabled = true;
			return false;
		}
		if (document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value == "") {
			alert("Il luogo per l'autorità competente è un campo obbligatorio");
			document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
            document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;
            return false;
        }
	}
	// ANNO/NUMERO PARTITA o ci sono entrambi o nessuno
	if ((document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value == ""
			&& !document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value == "")
			|| (!document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value == ""
					&& document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value == "")) {
		alert("anno e numero partita non corretti");
		document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
        return false;
	}
	// Almeno una delle due informazioni tra ANNO/NUMERO PARTITA, NUMERO EX CAMPIONE
	// deve essere valorizzato.
	if (document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.value == ""
			&& document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>.value == ""
			&& document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>.value == "") {
		alert("Almeno uno fra i due campi va inserito");
		document.LoadInserisciRichiestaConversione.<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>.focus(); 
		return false;
	}
	if (document.LoadInserisciRichiestaConversione.CodTipoAutoritaEmittente.value == "-") {
   		alert("Autorità obbligatoria");
   		return false;
	}
	//=============================================================
   	// controllo correttezza campo 'Data Ricezione Atto' 
   	//=============================================================
   	var data_Rice_Atto = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.value + '/' +
    					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO%>.value + '/' + 
    					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO%>.value;
	if (!ControllaData(data_Rice_Atto) && data_Rice_Atto.length > 2) { 
		alert('Data Ricezione Atto non corretta'); 
		document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO%>.focus(); 
		return false; 
	}
	//=============================================================
	// controllo correttezza campo 'Data Iscrizione Atto' 
	//=============================================================
	var data_Iscr_Atto = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.value + '/' +
    					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO%>.value + '/' +
    					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO%>.value;
	if (!ControllaData(data_Iscr_Atto) && data_Iscr_Atto.length > 2) { 
	 	alert('Data Iscrizione Atto non corretta'); 
	 	document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO%>.focus(); 
	 	return false; 
	}
	// 09/07/2015 Data Iscrizione Atto <= Data Odierna
	var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	if (!CompareDate(data_Iscr_Atto, data_sistema)) {
	   	alert('Data Iscrizione Atto non può essere superiore alla data odierna!');
	   	return false;
 	}
	// 09/07/2015 Data Ricezione Atto <= Iscrizione Atto <= Data Odierna
 	if (!CompareDate(data_Rice_Atto, data_Iscr_Atto)) {
	   	alert('Data Ricezione Atto non può essere superiore alla data Iscrizione Atto!');
	   	return false;
 	}
	//=============================================================
	// controllo correttezza campo 'Data Esazione' 
	//=============================================================
	var data_Esazione = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE%>.value + '/' +
   						document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE%>.value + '/' +
   						document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE%>.value;
	if (!ControllaData(data_Esazione) && data_Esazione.length > 2) {
		alert('Data Esazione non corretta'); 
		document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE%>.focus(); 
		return false; 
	} 
	// 09/07/2015 Data Esazione <= Ricezione Atto
 	if (!CompareDate(data_Esazione, data_Rice_Atto)) {
	   	alert('Data richiesta Impossibilità Esazione non può essere superiore alla data Ricezione Atto!');
	   	return false;
 	}
	//=============================================================
	// controllo correttezza campo 'Data Prescrizione Multa' 
	//=============================================================
	var data_to_verify = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA%>.value + '/' +
   						document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_MULTA%>.value + '/' +
    					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) { 
		alert('Data Prescrizione Multa non corretta'); 
		document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA%>.focus(); 
   	  return false; 
   	}
	//--------------------------------------------------------
	// se multa = vuoto and (data o flag presenti errore)
	// se multa = pieno and (data e flag assenti o presenti errore) 
	if (document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.value == ""
			&& document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>DEC.value == "") {
		if (data_to_verify.length > 2
				|| document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true) {
			// se l'importo della multa è vuoto non posso inserire la data prescrizione o avvalorare il flag imprescrittibile
			alert("Importo della multa non inserito");
			document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.focus(); 
			return false;
		}	
	} else {
		// Paolo Cherubini 07/03/2011 eliminato controllo su richiesta di Michele Testa
		if (data_to_verify.length > 2
				&& document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true) {
			// se l'importo della multa è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
			// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
			// document.LoadInserisciRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.focus(); 
			// return false;
		}
		if (data_to_verify.length < 3
				&& !document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA%>.checked == true) {
			// se l'importo della multa è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile			
			// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
			// document.LoadInserisciRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA %>INT.focus(); 
			// return false;
		}
	}
   	//=============================================================
   	// controllo correttezza campo 'Data Prescrizione Ammenda' 
   	//=============================================================
   	var data_to_verify = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA%>.value + '/' +
					    document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA%>.value + '/' +
					    document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
		alert('Data Prescrizione Ammenda non corretta'); 
		document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA%>.focus(); 
		return false; 
	}
   	//--------------------------------------------------------
   	// se ammenda = vuoto and (data o flag presenti errore)
   	// se ammenda = pieno and (data e flag assenti o presenti errore) 
	if (document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.value == ""
			&& document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>DEC.value == "") {	
		if (data_to_verify.length > 2
				|| document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true) {
			// se l'importo della multa è vuoto non posso inserire la data prescrizione o avvalorare il flag imprescrittibile
			alert("Importo della Ammenda non inserito");
			document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.focus(); 
			return false;
		}
	} else {
		// Paolo Cherubini 07/03/2011 eliminato controllo su richiesta di Michele Testa
		if (data_to_verify.length > 2
				&& document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true) {
			// se l'importo della Ammenda è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
			// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
			// document.LoadInserisciRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.focus(); 
			// return false;
		}
		if (data_to_verify.length < 3
				&& !document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA%>.checked == true) {
			// se l'importo della Ammenda è pieno devo inserire la data prescrizione o avvalorare il flag imprescrittibile
			// alert("Indicare la data di prescrizione o avvalorare il flag imprescrittibile")
			// document.LoadInserisciRichiestaConversione.<-%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA %>INT.focus(); 
			// return false;
		}		
	}
	// 02/03/2015 controllo capienza Importi multa/ammenda residue.
	var aMulta = <%=multaResidua%>;
	var aAmmenda = <%=ammendaResidua%>;
	var insMulta = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT.value + "." +
					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>DEC.value;
	var insAmmenda = document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT.value + "." +
					document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>DEC.value;
	if (insMulta > aMulta) {
    	alert("la Multa non può eccedere l'importo residuo"); 
    	return false; 
	}
	if (insAmmenda > aAmmenda) {
    	alert("l'Ammenda non può eccedere l'importo residuo"); 
    	return false; 
	}
	if (fasPro > 70000 && fasPro < 80000) {
		document.LoadInserisciRichiestaConversione.idFascicoloClasseI.value = "<%=aIdFascicolo %>";
		loadNumProgOrigin();
	}
	// Impostazione action di inserimento.
	document.LoadInserisciRichiestaConversione.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.penapecuniaria.action.ActInserisciRichiestaConversione";
   	return true; 
}

  




	  
	  
	
	
</script>
</head>

<body class="corpo" onload ="onload();">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0> </a></td>
		<td class="LBG"><font class="label">Funzione:</font>&nbsp;&nbsp;
		<font class="campo">Iscrizione Richiesta Conversione</font></td>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciRichiestaConversione'>

<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table>
<%-- MAC20191202018-MG-13/12/2019: per gli attributi multaResidua e ammendaResidua è stato modificato il tipo oggetto da String a BigDecimale--%>
<%
String sMultaResidua = (multaResidua != null ) ? multaResidua.toString() : "";
String sAmmendaResidua = (ammendaResidua != null ) ? ammendaResidua.toString() : "";
if (sMultaResidua.length() < 2)
	sMultaResidua="";
	if(sAmmendaResidua.length()<2) 
		sAmmendaResidua="";
if (sMultaResidua.length() > 1 || sAmmendaResidua.length() > 1) {
%>
	<tr>
		<td class="l" colspan="2">Sanzione Pecuniaria Residua : </td>
		<% if (sMultaResidua.length()>1) { %>
			<td class="l">Multa :
			<font class="campo"><%=sMultaResidua%>&nbsp;&nbsp;&nbsp;</font> </td>
  		<%} else { %>
			<td class="nobord" colspan="2">&nbsp;</td>
  		<%}%>
		<% if (sAmmendaResidua.length()>1) { %>
			<td class="l">Ammenda :  </td>
			<td class="campo"><%=sAmmendaResidua%>&nbsp;&nbsp;&nbsp;</td>
  		<%} else { %>
			<td class="nobord" colspan="2">&nbsp;</td>
  		<%} %>
	</tr>
  <%} %>
  <%-- FINE MAC20191129017-MG-04/12/2019: aggiunti oggetti --%>
</table>

<table>
	<!-- 28/01/2015 MEV23 Se si sta iscrivendo la richiesta per classe VII bisogna inserire gli estremi del fascicolo di classe I a cui va collegato -->
<%  if (lFasPro > 70000 && lFasPro < 80000)
    { %>

     <tr><td>&nbsp;</td></tr>
     <tr>
       <td class="l" colspan="3">Inserimento Estremi del Fascicolo di Classe I </td>
       <td class="l"><input type="radio" name="tipo" value="0" checked onClick="radioClasseI();"> &nbsp;&nbsp;Si&nbsp;&nbsp; </td>
	   <td class="l"colspan="2"><input type="radio" name="tipo" value="1" onClick="radioClasseI();"> &nbsp;&nbsp;No&nbsp;&nbsp;</td>
     </tr>
</table>    
	<div id="divDatiClasseI" style="display:none; float:left; position:relative; ">
  	  <table width="70%">
	    <tr>
	      <td class="LBG" colspan="6" >
	        <font class="label">Estremi del Fascicolo di classe I </font>&nbsp;
	      </td>
	    </tr>
	
	    <tr>
	      <td class="l" colspan="2">Anno/Numero SIEP <font class=ob>(*) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></td>
	      <td class="l" colspan="4">
	        <input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>" value ="<%=aChiaveAnno%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onchange="javascript:document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;">
	        /<input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>" value ="<%=aChiaveProgr%>" maxlength="14" size="14" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onchange="javascript:document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;">
	        <input type="hidden" name="<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(aChiaveProgr,"")%>">
	        <input type="hidden" name="idFascicoloClasseI" value="<%=StringUtils.toStringJSP(aIdFascicolo,"")%>">
	         &nbsp;&nbsp;<a href="Javascript:TrasformaRes('LoadInserisciRichiestaConversione','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>');">
	         R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
	         <a href="Javascript:TrasformaPret('LoadInserisciRichiestaConversione','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>');">
	         P.T.<img src="/images/filefolder.gif" border=0></a>
	      </td>
	    </tr>
	
	    <tr>
	      <td class="l" colspan="2">Autorità <font class=ob>(*)</font></td>
	      <td class="L" colspan="4">
	        <select class=medium name="<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>" onchange="javascript:resetField();">
	          <%= AutoritaCompetente %>
	        </select>
	      </td>
	    </tr>
	
	    <tr>
	      <td class="l" colspan="2">Luogo <font class=ob>(*)</font></td>
	      <td class="l" colspan="4">
	        <input name="<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>" value ="<%=aSedeUfficio%>" type="text" maxlength="35" size="35" onchange="javascript:document.LoadInserisciRichiestaConversione.CONFERMA.disabled=true;" readonly="readonly">
	        <!-- a href="Javascript:ListaComuni('LoadInserisciRichiestaConversione','<%= ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP %>' , document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>[document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.selectedIndex].value);" -->
	        <a href="Javascript:ChoosePopup();">
	        <img src="/images/filefolder.gif" border=0> </a>
	      </td>
	    </tr>
	
	    <tr>
	      <td class="l" colspan="2">Ufficio Accorpato</td>
	      <td class="l" colspan="4">
	         	<select name="<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>">
	         	<option value="0" >-</option>
	         	</select>
	      </td>
	    </tr>
	
	    <tr>
	      <td>
        	<input class="bottone" type="submit" name="Ricerca" value="Ricerca" onClick="javascript:return VerifyRicerca();">
	      </td>
	    </tr>
<%  	if (lFasPro > 70000 && lFasPro < 80000	&&
			aChiaveAnno != "")
		{ %>    
			<tr>
      			<td class="Label"  colspan="6">
        			<font class="label">N.B. Procedimento di Classe I individuato: procedere con i dati di Richiesta Conversione</font>&nbsp;
      			</td>
    		</tr>
			<tr><td><br></td></tr>
   	  <%}%>
    </table>
 </div>
    <%}%>

<table width="70%">
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>

<table width="70%">
	<tr>
		<td class="Titolo" colspan=6>Dati Richiesta Conversione</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Anno/Numero Partita</td>
		<td class="l" colspan="4">
			<input type="text" maxlength="4" size="4"  name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_PARTITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / 
			<input type="text" maxlength="9" size="11" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_PARTITA %>">
			<input type="text" maxlength="20" size="20" name="<%= ICostantiPenaPecuniaria.CAMPO_NUM_EX_CAMPIONE %>">
		</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Autorità<font class="ob">(*)</font></td>
		<td class="L" colspan="4"><select Title="Autorità"
			name="<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
			<%=autoritaConv%>
		</select></td>
	</tr>
	<tr>
		<td class="l" colspan="2">Sede <font class=ob>(*)</font></td>
		<td class="L" colspan="4"><input Title="Sede"
			name="<%=ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE%>"
			type="text" maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadInserisciRichiestaConversione','<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>');">
			<img src="/images/filefolder.gif" border=0> </a>
		</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Ricezione Atto <font class=ob>(*)</font></td>
		<td class="l" colspan="4">
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Iscrizione Atto <font class=ob>(*)</font></td>
		<td class="l" colspan="4">
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp; 
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	   		<%-- <a href="Javascript:impostaDataOdierna('<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>','<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>','<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">--%>
	   		<a href="javascript:impostaDataOdierna('<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>','<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>','<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
	     		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	   		</a>
		</td>
	</tr>
	<tr>
		<td class="l" colspan="2">Data Richiesta Impossibilità Esazione <font
			class=ob>(*)</font></td>
		<td class="l" colspan="4">
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Multa</td>
		<td class="l">
			<input type="text" maxlength="14" size="16" ONKEYPRESS="return TicTabNumField(this,event)" style="text-align:right" name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA%>INT"><strong>&nbsp;,&nbsp;</strong>
			<input type="text" maxlength="2" size="2" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_MULTA %>DEC"> 
		</td>
		<td class="l">Data Prescrizione</td>
		<td class="l">
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_MULTA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Imprescrittibile</td>
		<td class="l"><input type="checkbox"
			name="<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_MULTA %>">
		</td>
	</tr>
	<tr>
		<td class="l">Ammenda</td>
		<td class="l">
			<input type="text" maxlength="14" size="16" ONKEYPRESS="return TicTabNumField(this,event)" style="text-align:right" name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA%>INT"><strong>&nbsp;,&nbsp;</strong>
			<input type="text" maxlength="2" size="2" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiPenaPecuniaria.CAMPO_IMPORTO_AMMENDA %>DEC">
		</td>
		<td class="l">Data Prescrizione</td>
		<td class="l">
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
			<input type="text" size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Imprescrittibile</td>
		<td class="l"><input type="checkbox"
			name="<%= ICostantiPenaPecuniaria.CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA %>">
		</td>
	</tr>
	<tr>
		<td>&nbsp;
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    		<input type="HIDDEN" name="valoreRadio" value="">
		</td>
		
	</tr>
	<tr>
		<td class="lNoBord" colspan="2">
		  <input class="bottone" type="submit" name="CONFERMA" value="Conferma" onClick="javascript:return Verify();">
		  
		</td>
	</tr>

</table>
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciRichiestaConversione");
<%
if (lFasPro > 70000 && lFasPro < 80000) {
%>
if (document.LoadInserisciRichiestaConversione.CONFERMA.disabled == true) {
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorita è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
	frmvalidator.setAddnlValidationFunction("Verify");
}
<%
}
if (lFasPro < 20000) {
%>
frmvalidator.setAddnlValidationFunction("Verify");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorita è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ISCRIZIONE_ATTO %>","req","Il campo Data Iscrizione Atto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_ESAZIONE %>","req","Il campo Data Esazione è obbligatorio");
<%
}
%>

var ufficiAccorpatiArray = new Array();
<%
Iterator uaIter = ufficiAccorpati.iterator();
int uaIndice = 0;
while (uaIter.hasNext()) {
	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
%>
ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>"
												,"<%=uaModel.getIncrProgressivo()%>"
												,"<%=uaModel.getCodUfficio()%>"
												,"<%=uaModel.getCodUfficioNew()%>"
												,"<%=uaModel.getCodTipoUfficio()%>"
												,"<%=uaModel.getCodTipoUfficioNew()%>"
												,"<%=uaModel.getDescrizioneNewUfficio()%>"); 
<%
	uaIndice ++;
}
%>

function transCoding(cod) {
	var ret = cod;
 	if (cod == 'DIB') {
		ret = 'Tribunale Ordinario';
 	} else if (cod == 'TRIBSD') {
		ret = 'Sezione Distaccata Tribunale';
 	} else if (cod ==' CAS') {
  		ret = 'Corte Assise';
 	} else if (cod == 'GIP') {
  		ret = 'Gip presso Tribunale';
 	} else if (cod == 'PM') {
  		ret = 'Procura presso Tribunale';
 	} else if (cod == 'PGCAP') {
  		ret = 'Procura presso Corte Appello';
 	}
 	return ret;
}

function loadUfficiAccorpati(codUfficio) {
	  //alert('loadUfficiAccorpati');
	var i = 0;
	var ufficioAccorpatoSelect = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
	ufficioAccorpatoSelect.options.length = 0;
	ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
	while (i < ufficiAccorpatiArray.length) {
		var ufficio = ufficiAccorpatiArray[i];
		if (ufficio[3] == codUfficio) {
			ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]+"-"+ufficio[2]);
		}
		i++;
	}
}

function loadNumProgOrigin() {
	document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value = "";
	var numProg = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value;
	var ufficioAccorpato = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>.value;
	var parts=ufficioAccorpato.split("-");
	var offSetInt = parseInt(parts[0]);
	if (numProg) {
	    var newProg = parseInt(numProg) + offSetInt;
	    document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value = newProg;
    }
    return true;
}

function resetField() {
	document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value = "";
	var selectUfficioAccorpato = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
    selectUfficioAccorpato.options.length = 0;
    selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
}

function loadUfficiAccorpatiByDesc() {
	var i = 0;
	var ufficioAccorpatoSelect = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_ACCORPATO%>;
	var ufficioBaseDesc = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value;
	var ufficioTipoSelect = document.LoadInserisciRichiestaConversione.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>;
	if (ufficioTipoSelect.selectedIndex > 0) {
		var ufficioTipo = ufficioTipoSelect.options[ufficioTipoSelect.selectedIndex].value;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while (i < ufficiAccorpatiArray.length) {
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[6] == ufficioBaseDesc && ufficio[5] == ufficioTipo) {
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[4])+")", ufficio[1]+"-"+ufficio[2]);
			}
			i++;
		}
	}
}

function TrasformaRes(a_formname, a_fieldname, a_fieldname2) {
	desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function TrasformaPret(a_formname, a_fieldname, a_fieldname2) {
	desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
</script>
</body>
</html>