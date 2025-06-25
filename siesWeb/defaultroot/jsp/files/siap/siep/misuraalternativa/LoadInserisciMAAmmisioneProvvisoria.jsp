<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra"  	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"    	scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="motivoProvv"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="idmisuraalternativa"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipomisura"           	scope="request" class="java.lang.String"/>
<%-- Solo se proveniente dalla registrazione inizio misura --%>
<jsp:useBean id="verbale"              	scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<%-- Destinatari --%>
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutoritaE"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente" 	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="avvocati"             	scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="daticssa"             	scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="autoritaEsternaE"     	scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="comboTipoUfficioSIUS" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvvSorv"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 		scope="request" class="java.lang.String"/>

<%
// jsp utilizzata sia per l'inserimento del provvedimento si esecuzione
// dell'ordinanza, sia per l'eventuale Ordine di scarcerazione successivo
// alla registrazione data inizio misura
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();
if(lLuogoDetenzione == null)
  	lLuogoDetenzione = new LuogoDetenzioneModel();
if (lAltraCausa == null)
  	lAltraCausa = new AltraCausaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

// Chiamata lista Avvocati.
function ListaAvvocati(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
}

function delCSSA(a_formname) {
	document.LoadInserisciMisuraAlternativa.Indirizzo.value = "";
	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value = "";
}

function ListaDocumentiSius(a_formname) {
<%
if (tipomisura.equals("AFFIDAMENTO")) {
%>
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.AMMISSIONE_PROVVISORIA%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>=<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
<%
} else if (tipomisura.equals("DETENZIONE")) {
%>
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.AMMISSIONE_PROVVISORIA%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>=<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
<%
}
%>
}

function ListaTDS_UDS(a_formname,a_fieldname) {
	var valore = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
  	if (valore == "UDS") {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	} else if (valore == "TDS") {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
   	} else {
     	alert("Selezionare il tipo di ufficio emittente");
   	}
 }

function pulisciComune() {
  	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value = "";
}

function pulisciId() {
  	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value = "";
}

//========================================================================
// Funzione Visualizzare/Nascondere la sezione (div) per la richiesta di
// restituzione Ordine di Esecuzione
//========================================================================
function VisualizzaOE() {
  	var nodeOE = document.getElementById('divOrdineEsecuzione');
  	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>.checked == true) {
     	nodeOE.style.display = 'block';
  	} else {
     	nodeOE.style.display = 'none';
  	}
}

function VisualizzaAvvocati() {
  	var node = document.getElementById('divavvocati');
  	if (document.LoadInserisciMisuraAlternativa.checkAvvocati.checked == true) {
     	node.style.display = 'block';
  	} else {
     	node.style.display = 'none';
  	}
}

//========================================================================
// Funzione per il caricamento della lista degli Ordini di Esecuzione
//========================================================================
function ListaOrdiniEsecuzione(a_formname) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActListaOE&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Ordini_Esecuzione", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
}

function ListaCSSA(a_formname,a_fieldname,a_field2) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaUDS(a_formname,a_fieldname) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaComuniTds(formname,fieldname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
  
function pulisciIstituto (nomeCampoComune, nomeCampoId) {
    var campoDescr = document.getElementsByName(nomeCampoComune)[0];
    var campoId = document.getElementsByName(nomeCampoId)[0];
    campoDescr.value = "";
    campoId.value = "";
}

//========================================================================
// Funzione di controllo dei dati
//========================================================================
function Verifica() {
	var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
<%
//================================
// Verifica Data Fine Pena Manuale (se presente)
//================================
if (!lPosizione.isLibero()
		|| (lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S")
       	&& penaresidua.getDataFinePresunta() != null
       	&& penaresidua.getDataFine() == null)) {
	if (((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D")))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
   		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0' +
   		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
 		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value = '0' +
 		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
	var data_to_verifica = document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value + '/' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value + '/' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
	if (!ControllaData(data_to_verifica)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
		}
 	}
}
%>
	//====================
	// Data Emissione
	//====================
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
	var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value + '-' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value + '-' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data di Emissione non valida');
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	if (!CompareDate(data_to_verify,dataOdierna)) {
		alert('La Data Emissione non può essere superiore alla data odierna');
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
	//====================
	// Data Trasmissione
	//====================
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
	var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value + '-' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value + '-' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
		document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
		return false;
	}
	//========================
	// Data Emissione Decreto
	//========================
	var campo = document.LoadInserisciMisuraAlternativa.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;
	if (document.LoadInserisciMisuraAlternativa.flagmisura.value == "N") { //data emissione decreto/ordinanza
	  	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
		if (!ControllaData(data_to_verify))	{
			alert('Data di emissione Decreto non valida');
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.focus();
			return false;
		}
<%
if (tipomisura.equals("AFFIDAMENTO")) {
%>
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value == "-") {
			alert("Selezionare l'Ufficio Emittente");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
			return false;
		}
<%
}
%>
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value == "") {
			alert("La Sede dell'Ufficio Emittente è obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
			return false;
		}
<%
if (tipomisura.equals("AFFIDAMENTO")) {
%>
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value == "-") {
			alert("Selezionare il Tipo Provvedimento");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.focus();
			return false;
		}      
<%
}
%>
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "-") {
			alert("Selezionare l'Oggetto della Decisione");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
			return false;
  		}  
	}
<%
// Check su Data Inizio Misura
if ((tipomisura.equals("AFFIDAMENTO") || tipomisura.equals("DETENZIONE"))
		&& (misuraalternativa.getIdMisuraAlternativa() == null)) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) { // Esegue MDS la Data Inizio Misura è obbligatoria
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value == "") {
<%
	if (tipomisura.equals("DETENZIONE")) {
%> 
			alert("La data Inizio Misura è obbligatoria");
<%
	} else {
%> 
			alert("La data di Scarcerazione/Inizio Misura è obbligatoria");
<%
	}
%>  
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
    		return false;
  		}
	}
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked == true) { // Esegue Procura la Data Inizio Misura non va valorizzata
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value == "") {
  			
		} else {
<%
	if (tipomisura.equals("DETENZIONE")) {
%> 
			alert("Non si deve inserire la data Inizio Misura");
<%
	} else {
%> 
			alert("Non si deve inserire la data di Scarcerazione/Inizio Misura");
<%
	}
%>      
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
    		return false;
  		}
	}
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value != ""
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value != ""
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value != "") {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
		if (!ControllaData(data_to_verify)) {
<%
	if (tipomisura.equals("DETENZIONE")) {
%>
			alert("La data Inizio Misura è errata");
<%
	} else {
%> 
			alert("La data di Scarcerazione/Inizio Misura  è errata");
<%
	}
%>
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
    		return false;
  		}
	}
<%
} // chiude AFFIDAMENTO o DETENZIONE
%> 
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == ""
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
		alert("Il  Magistrato Firmatario è obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
		return false;
	}
	//==============================
	// Check sui destinatari
	//==============================
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-') {
<%
if (tipomisura.equals("DETENZIONE") && lPosizione.isLibero() && verbale.getIdVerbale() == null) {
%>
  			alert("Il Campo Destinatario per esecuzione è obbligatorio");
<%
} else {
%>
  			alert("Il Campo Autorità competente per territorio è obbligatorio");
<%
}
%>
			return false;
  		}
	}
<% 
if (tipomisura.equals("AFFIDAMENTO")) { 
	// Almeno un destinatario tra quelli previsti (!disabled) è obbligatorio
%>
	var novalorizzati = true;
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.value == "-")) {
     		novalorizzati = false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")) {
     		novalorizzati = false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "-")) {
     		novalorizzati = false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value == "")) {
   			novalorizzati = false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value == "")) {
   			novalorizzati = false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled) {
		if (!(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-')) {
     		novalorizzati = false;
  		}
	}
	if (novalorizzati) {
		alert("Inserire almeno un destinatario!");
		return false;
	}
<%
} else  if (tipomisura.equals("DETENZIONE")) {
%>
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("Il Campo Istituto di Detenzione è obbligatorio");
			return false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value == "") {
  			// MEV10-s3: modificato msg
			alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
    		return false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-') {
		    alert("Autorità competente per territorio è obbligatorio");
		    return false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "-") {
		    alert("L'UEPE/USSM è obbligatorio");
		    return false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value == "") {
  			// MEV10-s3: modificato msg
   			alert("Il Tribunale di Sorveglianza è obbligatorio");
   			return false;
  		}
	}
<%
}
if (tipomisura.equals("AFFIDAMENTO")) {
%>
	var nodeavvocati  = document.getElementById('divavvocati');
	if (nodeavvocati.style.display == 'none') {
  		// Se il nodo avvocati non è visibile resetto i campi prima della submit
  		// altrimenti vengono comunque notificati
<%
	if (avvocati.size() == 1) {
%>
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value = "";
<%
	} else {
%>
		for (var i = 0; i < <%=avvocati.size()%>; i++) {
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[i].value = "";
		}
<%
	}
%>
	} else {
		
	}
<%
}
%>
}

//============================================================================
// metodo per rendere visibili o meno le div dei destinatari in funzione del 
// tipo misura, posizione giuridica e ufficio che eventualmente esegue
//============================================================================
function radio() {      
	var pos = document.LoadInserisciMisuraAlternativa.CodPosizioneGiuridica.value;
	var nodesor       = document.getElementById('divsor');
	var nodecssa      = document.getElementById('divcssa');
	var nodeistituto  = document.getElementById('divistituto');
	var nodeautoritaE = document.getElementById('divautoritacompetenteE');
	var nodeautoritaC = document.getElementById('divautoritacompetenteC');
	var nodeavvocati  = document.getElementById('divavvocati');
	var nodebottone   = document.getElementById('divbottone');
<%
if (tipomisura.equals("DETENZIONE") && (verbale.getIdVerbale() == null)) {
	if (lPosizione.isLibero()) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) { // Libero ed Esegue Procura
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'none';
		nodesor.style.display = 'none';
		nodeautoritaC.style.display = 'none';      
		nodeavvocati.style.display = 'block';
		nodebottone.style.display = 'block';   
		nodeistituto.style.display = 'none';                
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	} else {
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'none';      
		nodeavvocati.style.display = 'block';
		nodebottone.style.display = 'block'; 
		nodeistituto.style.display = 'none';         
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	}
<%
	} // Chiudo isLibero 
	if ("02".equals(lPosizione.getCodPosizioneGiuridica()) || "04".equals(lPosizione.getCodPosizioneGiuridica()) 
			|| "82".equals(lPosizione.getCodPosizioneGiuridica()) || "83".equals(lPosizione.getCodPosizioneGiuridica())  
			|| "84".equals(lPosizione.getCodPosizioneGiuridica()) || "70".equals(lPosizione.getCodPosizioneGiuridica())
			|| "71".equals(lPosizione.getCodPosizioneGiuridica()) || "72".equals(lPosizione.getCodPosizioneGiuridica())) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'none';      
		nodeavvocati.style.display = 'block';
		nodebottone.style.display = 'block';   
		nodeistituto.style.display = 'none';
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	} else {
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'none';      
		nodeavvocati.style.display = 'block';
		nodebottone.style.display = 'block'; 
		nodeistituto.style.display = 'none';  
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	}
<%
	} // Chiude posGiu = 04,02,82,83,84,70,71,72
	else if (!lPosizione.isLibero()) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {  
		nodeautoritaE.style.display = 'none';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'block';      
		nodeavvocati.style.display = 'block';
		nodeistituto.style.display = 'block';
		nodebottone.style.display = 'block';   
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
	} else {
	    nodeautoritaE.style.display = 'none';
	    nodecssa.style.display = 'block';
	    nodesor.style.display = 'block';
	    nodeautoritaC.style.display = 'block';      
	    nodeavvocati.style.display = 'block';
	    nodeistituto.style.display = 'none';
	    nodebottone.style.display = 'block';   
	    document.LoadInserisciMisuraAlternativa.cssaE.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;    
 	}
<%         
	} // Chiudo else if posizione Giur =! IsLibero
} // Chiudo if DETENZIONE e Prima Volta
if (tipomisura.equals("DETENZIONE") && (verbale.getIdVerbale() != null)) {
	if ((lPosizione.isLibero()) || ("29".equals(lPosizione.getCodPosizioneGiuridica()))) {
%> 
	nodeautoritaE.style.display = 'block';
    nodecssa.style.display = 'block';
    nodesor.style.display = 'block';
    nodeautoritaC.style.display = 'none';      
    nodeavvocati.style.display = 'block';
    nodebottone.style.display = 'block'; 
    nodeistituto.style.display = 'none';           
    document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
<%
	} // Chiudo isLibero DETENZIONE SECONDA VOLTA
	if ("02".equals(lPosizione.getCodPosizioneGiuridica()) || "04".equals(lPosizione.getCodPosizioneGiuridica()) 
			|| "82".equals(lPosizione.getCodPosizioneGiuridica()) || "83".equals(lPosizione.getCodPosizioneGiuridica())  
			|| "84".equals(lPosizione.getCodPosizioneGiuridica()) || "70".equals(lPosizione.getCodPosizioneGiuridica())
			|| "71".equals(lPosizione.getCodPosizioneGiuridica()) || "72".equals(lPosizione.getCodPosizioneGiuridica())) {
%>
    nodeautoritaE.style.display = 'block';
    nodecssa.style.display = 'block';
    nodesor.style.display = 'block';
    nodeautoritaC.style.display = 'none';      
    nodeavvocati.style.display = 'block';
    nodebottone.style.display = 'block';   
    nodeistituto.style.display = 'none';
    document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
<%             
	} // Chiude posGiu = 04,02,82,83,84,70,71,72
	else if (!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("29")) {
%>
	nodeautoritaE.style.display = 'none';
	nodecssa.style.display = 'block';
	nodesor.style.display = 'block';
	nodeautoritaC.style.display = 'block';      
	nodeavvocati.style.display = 'block';
	nodeistituto.style.display = 'block';
	nodebottone.style.display = 'block';   
	document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
<%
	} // Chiudo Non Libero e Non 29
} // Chiudo DETENZIONE SECONDA VOLTA
//=========================================
// AFFIDAMENTO IN PROVA
//=========================================
if (tipomisura.equals("AFFIDAMENTO") && (verbale.getIdVerbale() == null)) {
	if (lPosizione.isLibero()) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
		nodeistituto.style.display = 'none';
		nodeautoritaE.style.display = 'none';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'none';
		nodeautoritaC.style.display = 'none';
     	// Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
     	if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
      		nodeavvocati.style.display = 'none';
     	}
     	nodebottone.style.display = 'block';   
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
 	} else {
	    nodeautoritaE.style.display = 'block';
	    nodecssa.style.display = 'block';
	    nodesor.style.display = 'block';
	    nodeautoritaC.style.display = 'none';  
	    // Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
	    if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
	     	nodeavvocati.style.display = 'block';
	    }
	    nodebottone.style.display = 'block';
	    nodeistituto.style.display = 'none';
	    document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	}
<%
	} // Chiudi isLibero
	if ("02".equals(lPosizione.getCodPosizioneGiuridica()) || "04".equals(lPosizione.getCodPosizioneGiuridica())          
			|| "82".equals(lPosizione.getCodPosizioneGiuridica()) || "83".equals(lPosizione.getCodPosizioneGiuridica())  
			|| "84".equals(lPosizione.getCodPosizioneGiuridica()) || "70".equals(lPosizione.getCodPosizioneGiuridica())
			|| "71".equals(lPosizione.getCodPosizioneGiuridica()) || "72".equals(lPosizione.getCodPosizioneGiuridica())) {
%>
 	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
		nodeistituto.style.display = 'none';
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'none';
		//Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
		if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
		  	nodeavvocati.style.display = 'block';
		}
		nodebottone.style.display = 'block';   
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	} else {
		nodeautoritaE.style.display = 'block';
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'none';
		// Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
		if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
		  	nodeavvocati.style.display = 'block';
		}
		nodebottone.style.display = 'block'; 
		nodeistituto.style.display = 'none';             
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
 	}
<%
	} // Chiude posGiu = 04,02,82,83,84,70,71,72 
	else if (!lPosizione.isLibero()) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) { // esegue procura
     	nodeautoritaE.style.display = 'none';
<%
		if ("50".equals(lPosizione.getCodPosizioneGiuridica()) // Esecuzione presso domicilio della pena detentiva
				|| "53".equals(lPosizione.getCodPosizioneGiuridica()) // Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
				|| "12".equals(lPosizione.getCodPosizioneGiuridica()) // Detenzione Domiciliare
				|| "25".equals(lPosizione.getCodPosizioneGiuridica())) { // Detenzione Domiciliare speciale
     		// new (22/01/2014) DL 146 2013 l'istituto non è più previsto per le posizioni 50 e 53
      		// 16/06/2014 aggiunte anche posizioni 12 e 25
%>
    	nodeistituto.style.display = 'none';
<%
		} else {
%>
   		nodeistituto.style.display = 'block';
<%
		}
%>
		nodecssa.style.display = 'block';
		nodesor.style.display = 'block';
		nodeautoritaC.style.display = 'block';             
		// Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
		if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
		  	nodeavvocati.style.display = 'block';
		}             
		nodebottone.style.display = 'block';
		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
 	} else {
	    nodeistituto.style.display = 'none';
	    nodeautoritaE.style.display = 'none';
	    nodecssa.style.display = 'block';
	    nodesor.style.display = 'block';
	    nodeautoritaC.style.display = 'block';
	    // Se presente il "checkAvvocati" non altero la visualizzazione della DIV, viene gestita direttamente dal check
	    if (typeof (document.LoadInserisciMisuraAlternativa.checkAvvocati) == "undefined") {
	      	nodeavvocati.style.display = 'block';
	    }
	    nodebottone.style.display = 'block';   
	    document.LoadInserisciMisuraAlternativa.cssaE.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
	    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
    	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;    
 	}
<%
	} // Chiudo posizione Giur =! IsLibero
} // Chiudo if AFFIDAMENTO e Prima Volta

// verbale.getIdVerbale() != null se ho registrato il verbale di sottomissione agli obblighi
if (tipomisura.equals("AFFIDAMENTO") && (verbale.getIdVerbale() != null)) { 
	if (lPosizione.isLibero() || lPosizione.getCodPosizioneGiuridica().equals("54")) {
		// Entra se è libero o in Affidamento in prova provvisorio - 
		// AMBROSINO - Cambiato da 51 a 54
%>
	nodeautoritaE.style.display = 'none';
	nodecssa.style.display = 'block';
	nodesor.style.display = 'block';
	nodeautoritaC.style.display = 'block';  <%-- aggiunta autorità nel caso di 54 --%>
    nodeavvocati.style.display = 'none';
    nodeistituto.style.display = 'none';
    nodebottone.style.display = 'block';   
    document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
    // Abilita TDS, UDS, UEPE
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
<%
 	} // Chiudi isLibero
	if ("02".equals(lPosizione.getCodPosizioneGiuridica()) || "04".equals(lPosizione.getCodPosizioneGiuridica())
			|| "82".equals(lPosizione.getCodPosizioneGiuridica()) || "83".equals(lPosizione.getCodPosizioneGiuridica())  
			|| "84".equals(lPosizione.getCodPosizioneGiuridica()) || "70".equals(lPosizione.getCodPosizioneGiuridica())
			|| "71".equals(lPosizione.getCodPosizioneGiuridica()) || "72".equals(lPosizione.getCodPosizioneGiuridica())) {
%>
	nodeautoritaE.style.display = 'block';
	nodecssa.style.display = 'block';
	nodesor.style.display = 'block';
	nodeautoritaC.style.display = 'none';      
	nodeavvocati.style.display = 'block';
	nodebottone.style.display = 'block';   
	nodeistituto.style.display = 'none';               
	document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
<%
	} // Chiude posGiu = 04,02,82,83,84,70,71,72
	else if (!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("54")) {
		// AMBROSINO - Affidamento in prova provvisorio - Cambiato da 51 a 54
%>
    nodeautoritaE.style.display = 'none';
    nodecssa.style.display = 'block';
    nodesor.style.display = 'block';
    nodeautoritaC.style.display = 'block';      
    nodeavvocati.style.display = 'block';
    nodeistituto.style.display = 'block';
    nodebottone.style.display = 'block';              
    document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
<%         
	} // Chiudo posizione Giur != IsLibero e != Provisorio
} // Chiudo if AFFIDAMENTO e Registra Data (Seconda Volta)
%>
}
</script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>

<body class="corpo" onload="radio();">
<table>	
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if (tipomisura.equals("DETENZIONE")) {
%>
      		<font class="campo">AMMISSIONE PROVVISORIA A DETENZIONE DOMICILIARE</font>
<%
} else {
%>
      		<font class="campo">AMMISSIONE PROVVISORIA AD AFFIDAMENTO IN PROVA</font>
<%
}
%>
		</td>
    </tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>	
<br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
<INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMAAmmProvvisoria">
<INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
<INPUT type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"  >
<INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
<INPUT type="HIDDEN" name="tipomisura" value="<%=tipomisura%>">
<INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=idmisuraalternativa%>">
<%
if (verbale != null && verbale.getIdVerbale() != null) {
%> 
<INPUT type="HIDDEN" name="flagverbale" value="S">
<%
}
String scarceratodisabilita = null;
String dascarceraredisabilita = null;
String disabilitaData = null;
String scarcerato = null;
String scarcerare = null;
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
<input type="HIDDEN" name="flagmisura" value="N">
<%
	scarcerare = "checked";
} else {
%>
<input type="HIDDEN" name="flagmisura" value="S">
<%
}
if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione() != null) {
	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
		scarcerato = "checked";
   		if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataScarcerazione() != null) {
    		dascarceraredisabilita = "disabled";
   		}
  	}
	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")) {
   		scarcerare = "checked";
   		if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataScarcerazione() != null) {
    		scarceratodisabilita = "disabled";
   		}
  	}
   	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("-")) {
   		scarcerare = "checked";
	}
}
if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
	if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataScarcerazione() != null) {
		disabilitaData = "readonly";
   	}
}

//==============================================================================
// Inizio visualizzazione Posizione, Luogo di detenzione, pena residua
//==============================================================================
%>
<table>
	<tr>
		<td class="l">Posizione Giuridica </td>
      	<td class="L" colspan=5>
          	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA
<%
} else {
%>
         		<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
   		</td>
	</tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Detenuto presso </td>
	  	<td class="L" colspan=5>
	  		<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
	 	</td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Altro Luogo </td >
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>
	  	</td>
	</tr>
<%
		}
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
	 	<td class="l">Detenuto presso </td>
	 	<td class="L" colspan=5>
	  		<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
	  	</td>
	</tr>
<%
}
%>
	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" maxlength="6" size="6">
<%
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02,04,82,83,84,70,71,72)
if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02")
		|| lPosizione.getCodPosizioneGiuridica().equals("04")
       	|| lPosizione.getCodPosizioneGiuridica().equals("82") || lPosizione.getCodPosizioneGiuridica().equals("83")	
       	|| lPosizione.getCodPosizioneGiuridica().equals("84") || lPosizione.getCodPosizioneGiuridica().equals("70")
       	|| lPosizione.getCodPosizioneGiuridica().equals("71") || lPosizione.getCodPosizioneGiuridica().equals("72"))) {
	if (lLuogoDetenzione.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
	  	</td>
	</tr>
<%
	}
}
%>
	<tr>
<%
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S")
				&& !penaresidua.getFlagErgastolo().equals("D")))) {
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
		
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
		<td class="l">Multa</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
	}
%>
	</tr>
   	<tr>
<%
	if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
		
	} else {
%>
		<td class="l" >Arresto</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
		<td class="l">Ammenda</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
	}
}
%>
	</tr>
	<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
		</td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
<%
	}
}
if ((!lPosizione.isLibero())
		|| (lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
		</td>
<%
          	} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
			<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
		</td>
<%
			}
		}
	}
}
//==============================================================================
// Fine visualizzazione Posizione, Luogo di detenzione, pena residua
//==============================================================================
%>
	</tr>
	<tr>
		<td class="l">Data Emissione</td>
		<td class="L">
			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Data Trasmissione</td>
		<td class="L">
			<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>

<%
//==============================================================================
// Sezione con i dati dell'ordinanza/decreto. Se sto caricando il primo 
// provvedimento (IdMisuraAlternativa == null) i campi sono editabili.
// Se sto emettendo il secondo provvedimento dopo la registrazione del verbale
// di sottomissione agli obblighi (IdMisuraAlternativa != null) i dati non sono 
// editabili, il decreto/ordinanza è già stato emesso
//==============================================================================
%>
<table width="100%">
	<tr>
<%
if (tipomisura.equals("AFFIDAMENTO")) {
%>
		<td class="Titolo" colspan='8'> Dati Del Provvedimento della Sorveglianza </td>
<%
} else {
%>     
      	<td class="Titolo" colspan='8'> Dati Decreto del Magistrato di Sorveglianza </td>
<%
}
%>
	</tr>
<% 
if (misuraalternativa.getIdMisuraAlternativa() != null) {
	// Sto registrando la 'Data Inizio Misura', provego quindi dall'inserimento 
	// del verbale di sottoscrizione. I dati del decreto/ordinanza non sono
	// editabili in quanto è stato già inserito
%>
	<tr>
		<td class="l">Anno / Numero SIUS</td>
        <td class="l">
          	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>/</font>
          	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
        </td>
<%
	if ("02".equals(misuraalternativa.getCodTipoDecisione())) {
%>
        <td class="l"> Anno / Numero Decreto</td>
<%
	} else {
%> 
        <td class="l"> Anno / Numero Ordinanza</td>
<%
	}
%>
        <td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
          	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
        </td>
	</tr>
	<tr>
        <td class="l">Ufficio Emittente</td>
        <td class="l" colspan="3"> <font class="campo"> <%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%> DI <%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%></font></td>
	</tr>
    <tr>
<%
	if ("02".equals(misuraalternativa.getCodTipoDecisione())) {
%>
		<td class="l">Oggetto Decreto</td>
<%
	} else {
%> 
        <td class="l">Oggetto Ordinanza</td>
<%
	}
%>
        <td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>
        </td>
	</tr>
	<tr>
<%
	if ("02".equals(misuraalternativa.getCodTipoDecisione())) {
%>
        <td class="l">Data Emissione Decreto</td>
<%
	} else {
%>
        <td class="l">Data Emissione Ordinanza</td>
<%
	}
%>
        <td class="l" colspan="3">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd-MM-yyyy"))%>
			</font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">
		</td>
	</tr>
<%
	if (tipomisura.equals("AFFIDAMENTO")) {
%>
	<tr>
	  	<td class="l">Luogo della Prova </td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font>
	 	</td>
	</tr>
<%
	} else if (tipomisura.equals("DETENZIONE")) {
%>
	<tr>
	  	<td class="l">Luogo della Detenzione Domiciliare</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font>
	  	</td>
	</tr>
<%
	}
	if (verbale.getDataEmissione() != null) {
%>
	<tr>
	  	<td class="l">Data Sottoscrizione Verbale Obblighi</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%></font>
	  	</td>
	</tr>
<%
	}
	if (misuraalternativa.getDataInizioMisura() != null) {
%>
	<tr>
	  	<td class="l">Data Inizio Misura</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd-MM-yyyy"))%></font>
	  	</td>
	</tr>
<%
	}
	if (misuraalternativa.getDataFineMisura() != null) {
%>
	<tr>
	  	<td class="l">Data Fine Misura</td>
	  	<td class="l" colspan="3">
	  		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%></font>
	  	</td>
	</tr>
<%
	}
%>
	<tr>
	  	<td class="l">Note</td>
	  	<td class="L" colspan="3">
	    	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>" cols=80 rows=2><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
	  	</td>
	</tr>
<%
} else {
	// Sto inserendo il primo provvedimento, devo indicare o selezionare i dati dell'ordinanza/decreto
%>
	<tr>
		<td class="l" colspan="4">
        	<a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          		Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
    </tr>
    <tr>
      	<td class="l">Anno / Numero SIUS</td>
      	<td class="l">
        	<input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();" onBlur="javascript:value=FillYear(value)">
        	/
        	<input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();">
      	</td>
<%
	if (tipomisura.equals("AFFIDAMENTO")) {
%>
		<td class="l" style="text-align:right;"> Anno / Numero Provvedimento</td>
<%
	} else {
%>
		<td class="l">Anno / Numero Decreto</td>
<%
	}
%>
		<td class="l">
	        <input Title="Anno"  name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();" onBlur="javascript:value=FillYear(value)">
	        /
	        <input Title="Numero" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();">
      	</td>
	</tr>
<%
	if (tipomisura.equals("DETENZIONE")) {
%>
	<tr>
		<td class="l">Ufficio Emittente</td>
      	<td class="l" colspan="3">
      		<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteUfficio)%>
      	</td>
    </tr>
    <tr>
      	<td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      	<td class="l" colspan="3">
        	<font class="campo">
          		<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" size=35 type="text" onChange="pulisciId();">
          		<a href="Javascript:ListaComuniEmitUdsMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            		<img src="/images/filefolder.gif" border=0>
          		</a>
        	</font>
      	</td>
    </tr>
<%
	} else if (tipomisura.equals("AFFIDAMENTO")) {
%>
	<tr>
      	<td class="l">Ufficio Emittente <font class=ob>(*)</font></td>
      	<td class="l" colspan="3">
        	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='pulisciComune();pulisciId();'")%>      
      	</td>
    </tr>
    <tr>
      	<td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      	<td class="l" colspan="3">
        	<font class="campo">
          		<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
       			<a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
         			<img src="/images/filefolder.gif" border=0>
       			</a>
       		</font>
      	</td>
    </tr>
    <tr>
      	<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      	<td class="l" colspan="3">
        	<select Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
          		<%=comboTipoProvvSorv%>
       		</select>
      	</td>
	</tr>    
<%
	}
%>
    <tr>
<%
	if (tipomisura.equals("AFFIDAMENTO")) {
%>
		<td class="l">Oggetto Decisione</td>
<%
	} else {
%>
      	<td class="l">Oggetto Decreto</td>
<%
	}
%>
      	<td class="L" colspan="3">
        	<select Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          		<option value="-">-
          		<%=motivoProvv%>
        	</select>
     	</td>
	</tr>
    <tr>
<%
	if (tipomisura.equals("AFFIDAMENTO")) {
%>
		<td class="l">Data Emissione</td>
<%
	} else {
%>
      	<td class="l">Data Emissione Decreto</td>
<%
	}
%>
      	<td class="l" colspan="3">
        	<font class="campo">
				<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
				<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
				<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        	</font>
      	</td>
    </tr>
    <tr>
<%
	if (tipomisura.equals("DETENZIONE")) {
%>
		<td class="l">Luogo della Detenzione</td>
<%
	} else if (tipomisura.equals("AFFIDAMENTO")) {
%>
      	<td class="l">Luogo della Prova</td>
<%
	}
%>
      	<td class="l" colspan="3">
        	<font class="campo">
          		<input Title="Luogo svolgimento della prova" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" size=35 type="text" onChange="pulisciId();">
        	</font>
      	</td>
    </tr>
    <tr>
      	<td class="l">Note</td>
      	<td class="L" colspan="3">
        	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      	</td>
	</tr>
<%
}
%>
</table>

<%
if ((tipomisura.equals("AFFIDAMENTO") || tipomisura.equals("DETENZIONE")) && (misuraalternativa.getIdMisuraAlternativa() == null)) {
%>
<table width="100%">
  	<tr>
		<td class="l">Eseguita da Procura&nbsp;
			<input type="radio" name="tipo" value="procura" checked onClick="javascript:radio();">&nbsp;&nbsp;
<%
	if (tipomisura.equals("AFFIDAMENTO")) {
%>
			Eseguita dalla Sorveglianza&nbsp;
<%
	} else {
%>
			Eseguita da Magistrato di Sorveglianza&nbsp;
<%
	}
%>          
			<input type="radio" name="tipo" value="mds" onClick="javascript:radio();" >
		</td>
		<td>
			<input type="HIDDEN" name="flagistitutoDet" value="">
		</td>
		<td class="l">Data Inizio Misura &nbsp; 
			<input type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="4" maxlength="4" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
  	</tr>
</table>
<%
}
%>

<table width="100%">
	<tr>
    	<td class="Titolo" width="100%" colspan=6> Magistrato Firmatario </td>
  	</tr>
  	<tr>
  		<%-- MEV10-s3: aggiunta proprietà width --%>
    	<td class="l" width="30%">Magistrato Firmatario
    	<td class="L">
      		<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      		<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      		<a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME %>','<%=ICostantiMagistrato.CAMPO_NOME %>');">
        		<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
  	</tr>
</table>

<%
//==============================================================================
// Sezione della restituzione dell'ordine di esecuzione
//==============================================================================
// Visualizzata solo se 'libero' e primo provvedimento e AFFIDAMENTO
if (lPosizione.isLibero() && misuraalternativa.getIdMisuraAlternativa() == null && tipomisura.equals("AFFIDAMENTO")) {
%>
<table width="100%">
	<tr>
	  	<td class="Titolo" colspan="4">Restituzione Ordine di Esecuzione</td>
	</tr>
	<tr>
	  	<td class="l">
	    	<input type="checkbox" name="<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>" onclick="VisualizzaOE();">&nbsp;&nbsp;Restituzione Ordine di Esecuzione
	  	</td>
	</tr>
</table>

<div id="divOrdineEsecuzione" style="display:none; position:relative;">
<table width="100%">
	<tr>
	  	<td class="l">
	    	<a href="Javascript:ListaOrdiniEsecuzione('LoadInserisciMisuraAlternativa');">
	      		Seleziona Ordine Esecuzione dalla lista <img src="/images/filefolder.gif" border=0>
	    	</a>
	  	</td>
	</tr>
	<tr>
	  	<td class="l"> Data Emissione </td>
	  	<td class="l">
	    	<input Title="Data Emissione" name="<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>" type="text" size="10" maxlength="10" READONLY>
	  	</td>
	  	<td class="l"> Oggetto </td>
	  	<td class="l" >
	    	<input Title="Oggetto" name="descrMotivoOE" type="text" size="50" maxlength="50" READONLY>
	  	</td>
	</tr>
	<tr>
	  	<td class="l" >Autorità per la Restituzione </td>
	  	<td class="L" colspan="3">
	    	<select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>">
				<%=codiceAutoritaE%>
	    	</select>
	  	</td>
	</tr>
	<tr>
	  	<td class="l">Sede</td>
	  	<td class="L">
	    	<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>" maxlength="35" size="35" READONLY>
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>');">
	    		<img src="/images/filefolder.gif" border=0>
	  		</a>
		</td>
		<td class="l">Indirizzo</td>
		<td class="L">
	  		<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>" cols="30" READONLY></textarea>
	  	</td>
	</tr>
</table>
</div>
<%
}
%>

<table width="100%">
  	<tr>
    	<td class="Titolo" width="100%" colspan="4">Destinatari</td>
  	</tr>
</table>

<!--div id="divautoritacompetenteE" width=100% style="visibility:hidden; position:relative;" -->
<div id="divautoritacompetenteE" style="width: 100%; display:block; position:relative;">
<table width="100%">
  	<tr>
    	<!--autorità di polizia-->
		<td class="l" width=30%>Autorità Competente per territorio <font class=ob>(*)</font></td>
		<td class="L" colspan="3">
  			<select Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
				<%=codiceAutoritaE%>
    		</select>
  		</td>
	</tr>
	<tr>
  		<td class="l">Sede</td>
  		<td class="L">
<%
if (autoritaEsternaE != null && autoritaEsternaE.getDescrSede() != null) {
%>
			<input title="Sede Autorita Esterna" value="<%=autoritaEsternaE.getDescrSede()%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>" maxlength="35" size="35">
<%
} else {
%>
			<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>" maxlength="35" size="35">
<%
}
%>
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
    			<img src="/images/filefolder.gif" border=0>
  			</a>
		</td>
		<td class="l">Indirizzo</td>
		<td class="L">
  			<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>" cols=30></textarea>
    	</td>
  	</tr>
</table>
</div>

<!--div id="divistituto" width=100% style="visibility:hidden; position:relative;" -->
<div id="divIstituto" style="width: 100%; display:block; position:relative;">
<table width="100%">
  	<tr>
	    <td class="l" width=30%>Istituto di Detenzione
<%
if (tipomisura.equals("DETENZIONE")) {
%>
			<font class=ob>(*)</font>
<%
}
%>
	    </td>
	    <td class="l">
<%
if (posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione() != null
		&& posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione() != null) {
%>
			<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
	      	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
	      	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
	      		<img src="/images/filefolder.gif" border=0></a>
	      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
<%
} else {
%>
	      	<input readonly Title="Istituto" name="Comune" value="" size=50>
	      	<input type="hidden" Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
	      	<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
	      		<img src="/images/filefolder.gif" border=0></a>
	      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
<%
}
%>
	    </td>
  	</tr>
</table>
</div>

<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
<!--div id="divcssa" width="100%" style="visibility:hidden; position:relative;" -->
<div id="divcssa" style="display: block; position: relative; width: 100%;">
<table width="100%">
<tr>
	<td class="l" width="30%">Destinatario&nbsp;
<%
if (tipomisura.equals("DETENZIONE")) {
%>
		<font class=ob>(*)</font>
<%
}
%>
		</td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l">
<%
String indirizzoCSSA = "";
if (daticssa.getIdCSSA() != null) {
	indirizzoCSSA = daticssa.getComune() + " - " + daticssa.getIndirizzo();
}
%>
			<input readonly title="Sede UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(indirizzoCSSA)%>" size=60>
			<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35>
			<input type="hidden" name="cssaE" value="S">
			<a href="Javascript:ListaCSSAMinor('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
				<img src="/images/filefolder.gif" border=0>
			</a>
			<a href="Javascript:delCSSA();"><img src="/images/delete.gif" border=0></a>
		</td>
	</tr>
</table>
</div>

<!--div id="divsor" width=100% style="visibility:hidden; position:relative; " -->
<div id="divsor" style="width: 100%; display: block; position: relative;">
<%
String lSedeUfficioMDS = null;
String lSedeUfficioTDS = null;
if ("UDS".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
	lSedeUfficioMDS = sedeUfficioEmittente.getDescrComune();
} else if ("TDS".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
	lSedeUfficioTDS = sedeUfficioEmittente.getDescrComune();
}
%>
<table width="100%">
	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario&nbsp;
<%
if (tipomisura.equals("DETENZIONE")) {
%>
			<font class=ob>(*)</font>
<%
}
%>
		</td>
		<td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
	</tr>
  	<tr>
  		<td class="l">Sede</td>
		<td class="L" colspan="3">
			<input type="text" title="ufficio" maxlength="35" size="25" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" value="<%=StringUtils.toStringJSP(lSedeUfficioMDS)%>">
			<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l">Destinatario</td>
		<td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
	</tr>
   	<tr>
	   	<td class="l">Sede</td>
		<td class="L" colspan="3">
			<input type="text" title="Sede Tribunale Sorveglianza" maxlength="35" size="35" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>" value="<%=StringUtils.toStringJSP(lSedeUfficioTDS)%>">
			<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
</table>
</div>

<!--div id="divautoritacompetenteC" width=100% style="visibility:hidden; position:relative; " -->
<div id="divautoritacompetenteC" style="display:block; position:relative; width:100%;">
<table width="100%">
  	<tr>
    	<!--autorità di polizia-->
    	<td class="l" width=30%>Autorità Competente per territorio
<%
if (tipomisura.equals("DETENZIONE")) {
%>       
      		<font class=ob>(*)</font>         
<%
}
%>
		</td>
		<td class="L" colspan="3">
      		<select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
       			<%=codiceAutoritaC%>
       		</select>
    	</td>
  	</tr>
  	<tr>
	    <td class="l">Sede</td>
	    <td class="L">
			<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
    	</td>
    	<td class="l">Indirizzo</td>
    	<td class="L">
      		<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>" cols=30 ></textarea>
    	</td>
  	</tr>
</table>
</div>

<% 
// I Difensori sono opzionali in caso di:
// - 'Libero' 
// - 'Detenuto'
boolean lAvvocatiOpzionali = false;
if (tipomisura.equals("AFFIDAMENTO") && verbale.getIdVerbale() == null && (lPosizione.isLibero()
		|| (!"02".equals(lPosizione.getCodPosizioneGiuridica())
				&& !"04".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"50".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"53".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"01".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"82".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"83".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"84".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"70".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"71".equals(lPosizione.getCodPosizioneGiuridica())
	            && !"72".equals(lPosizione.getCodPosizioneGiuridica())))) {
	lAvvocatiOpzionali = true;
}
if (lAvvocatiOpzionali) {
%>
<table width="100%">
	<tr>
	  	<td class="Titolo" colspan=6>Destinatari per Notifica</td>
	</tr>
	<tr>
	  	<td class="l">
	    	<input type="checkbox" name="checkAvvocati" onclick="VisualizzaAvvocati();">&nbsp;&nbsp;Notifiche atti (Difensore)
	  	</td>
	</tr>
</table>
<%
}
%>

<!-- <div id="divavvocati" style="width: 100%; visibility:hidden; position:relative;" -->
<div id="divavvocati" style="width: 100%; display:none; position:relative;">
<table width="100%">
<%
if (!lAvvocatiOpzionali) {
%>
  	<tr>
    	<td class="Titolo" colspan=6>Destinatario per Notifica </td>
  	</tr>
<%
}
int lIdxAvv = 0;
Iterator lItxAvv = avvocati.iterator();
while (lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
  	<tr>
    	<td>
        	<table>
          		<tr>
            		<td class="l" width="100%">Per Avvocato&nbsp;
	             		<font class="campo">
	               			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
	             		</font>
	             		&nbsp;Foro di&nbsp;
	             		<font class="campo">
	               			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
	             		</font>
	             		&nbsp;Difensore di&nbsp;
	             		<font class="campo">
	               			<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
	             		</font>
	             		<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
	           		</td>
	         	</tr>
	       </table>
	       <table>
	         	<tr>
           			<td class="l" width="35%">Autorità Destinazione</td>
	           		<td class="L" colspan="3">
	             		<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
	              			<%=autoritaEsternaAvv%>
	             		</select>
	           		</td>
	         	</tr>
	         	<tr>
            		<td class="l">Sede</td>
            		<td class="L">
              			<%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
              			<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
              			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
                			<img src="/images/filefolder.gif" border=0>
              			</a>
            		</td>
            		<td class="l">Note</td>
            		<td  class="L">
                		<textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=30 ></textarea>
            		</td>
          		</tr>
          		<tr><td>&nbsp;</td></tr>
        	</table>
		</td>
	</tr>
<%
	lIdxAvv++;
}
%>
</table>
</div>

<!--div id="divbottone" width=100% style="visibility:visible; position:relative;" -->
<div id="divbottone" style="display:block; position:relative; width:100%;">
<table width="100%">
  	<tr>
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    	</td>
  	</tr>
</table>
</div>
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

<%
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");

frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
<%
}
if ((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
  		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
  		}
 	}
}
if ((tipomisura.equals("AFFIDAMENTO") || tipomisura.equals("DETENZIONE")) && (misuraalternativa.getIdMisuraAlternativa() == null)) {
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","lt=2099");
<%  
}
%>
</script>
</body>
</html>