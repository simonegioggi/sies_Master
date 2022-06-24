<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<%-- penaresidua = Ultima pena residua validata --%>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<%-- Solo sulla seconda chiamata se la pena e' stata ricalcolata --%>
<jsp:useBean id="nuovapenaresidua"      scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="motivoProvv"           scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRevoca"            scope="request" class="java.lang.String"/>
<jsp:useBean id="distretto"             scope="request" class="java.lang.String"/>
<%-- STUB 14/12/2005 Modifiche inerenti la gestione del tipoUfficioSIUS (inserita funzione JS ListaTDS_UDS) --%>
<jsp:useBean id="tipoUfficioSIUS"       scope="request" class="java.lang.String"/>
<%-- misuraalternativa= Misura Alternativa di Revoca --%>
<jsp:useBean id="misuraalternativa"     scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<%-- misurasospesa: valorizzata se l'ultima misura è una sospensione Provvisoria --%>
<jsp:useBean id="misurasospesa"         scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<%-- misuraconcessa: valorizzata se l'ultima misura è una Concessione --%>
<jsp:useBean id="misuraconcessa"        scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<%-- misuraammissioneprovv: valorizzata se l'ultima misura è una Concessione Provvisoria --%>
<jsp:useBean id="misuraammissioneprovv" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="avvocati"              scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"    scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutoritaE"       scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 		scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" 	scope="request" class="java.lang.String"/>

<%
//==============================================================================
// JSP per l'inserimento della Revoca di:
// -- Affidamento in Prova
// -- Indultino 207/2003
// -- Espiazione presso il Domicilio
//
// La jsp viene invocata 2 volte:
// -- la prima per l'inserimente/selezione del provvedimento della sorveglianza
//     e calcolo pena
// -- la seconda per l'inserimento del provvedimento dell'esecuzione (destinatari)
//==============================================================================

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();
if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();
if (lAltraCausa == null)
	lAltraCausa = new AltraCausaModel();
String lNota = "N.B.: I dati relativi alla Revoca della Misura Alternativa vanno inseriti solo se occorre rideterminare la pena";

// Nel caso di Espiazione presso il domicilio vanno gestiti sia i provvedimenti del TDS che del UDS.
// La combo motivo provv va caricata dinamicamente in funzione del tipo ufficio selezionato
Collection oggettiTDS = (Collection) request.getAttribute("motivoProvvTDS");
Collection oggettiMDS = (Collection) request.getAttribute("motivoProvvUDS");
String strOggettiDecisione = "-;-;-;-#"; // nel formato TDS;cod1;desc1#TDS;cod2;desc2;MDS;cod1;desc1....
if (oggettiTDS != null) {
	Iterator itx = oggettiTDS.iterator();
   	while (itx.hasNext()) {
		DecodificheModel lDecMod = (DecodificheModel)itx.next();
		// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
		// Integrato con Uffici di Minorenni
		strOggettiDecisione += "TDS;TDSM;";
		// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******
		strOggettiDecisione += lDecMod.getCode()+";";
		strOggettiDecisione += lDecMod.getDescription()+"#";
   	}
}

if (oggettiMDS != null) {
	Iterator itx = oggettiMDS.iterator();
	while (itx.hasNext()) {
		DecodificheModel lDecMod = (DecodificheModel)itx.next();
		// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
		// Integrato con Uffici di Minorenni
		strOggettiDecisione += "UDS;UDSM;";
		// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******
		strOggettiDecisione += lDecMod.getCode()+";";
		strOggettiDecisione += lDecMod.getDescription()+"#";
   	}
}
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var desktop;
var strOggettiDecisione = "<%=strOggettiDecisione%>";

function caricaCombo (valueTextStr, sep1, sep2, filtro, selField) {
	// valueTextStr = stringa nel formato richiesto
	// sep1 = separatore interno alla coppia di valori
	// sep2 = separatore tra coppie
	// filtro = valore su cui fare il test
	// selField = oggetto combo da caricare
  	clearDropDown(selField);
  	var aPairs = valueTextStr.split(sep2);
  	if (valueTextStr.substr(valueTextStr.length - 1) == sep2) {
	    aPairs[aPairs.length - 1] = null;
	    aPairs.length--;
  	}
  	for (var i = 0; i < aPairs.length; i++) {
	    aValueText = aPairs[i].split(sep1);
	    // Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
	    // Integrato con Uffici di Minorenni
	    if (filtro=='null' || filtro==aValueText[0] || filtro==aValueText[1]) {
			oItem = new Option;
			oItem.value = aValueText[2];
			oItem.text = aValueText[3];
			// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******
			selField.options[selField.options.length] = oItem;
    	}
  	}
  	selField.options.selectedIndex = 0;
}

function clearDropDown (selField) {
	while (selField.options.length > 0)
	selField.options[0] = null;
}

function caricaComboOggetto() {
<%
if (tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
	caricaCombo(strOggettiDecisione,';','#',
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value,
		document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>);
<%
}
%>
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaAvvocati(a_formname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
}

// STUB 14/12/2005 Lista Uffici della Sorveglianza per Distretti ( TDS oppure UDS)
function ListaTDS_UDS(a_formname,a_fieldname) {
	var valore = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
	var i = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.selectedIndex;
	if (i == 1) {
  		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	} else {
  		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}
}

function ListaDocumentiSius(a_formname) {
	var tipoMA;
	if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'AFFIDAMENTO') {
		tipoMA = '<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'INDULTINO') {
	  	tipoMA = '<%=ICostantiMisuraAlternativa.INDULTINO%>';
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == '<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>') {
	  	tipoMA = '<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>';
	}
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.REVOCA%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
}

function pulisciId() {
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
}

function Verify() {
	<%-- 29/11/2010 Controllo eliminato e ripristinato su indicazioni di Nunzia Alfieri (Test Esecuzione Pena presso Domicilio). --%>
	<%-- MEV10-s3: aggiunto controllo preventivo --%>
	if ((document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value != "")
			|| (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value != "")
			|| (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value != "")) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value != "" 
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value != "") {
			alert('Mettere o Data Inizio Revoca o Quantum Calcolo Pena residua');
	      	return false;
	  	} 
	}

	<%-- 25/13/2011 --%> <%-- MEV10-s3: aggiunto controllo preventivo --%>      
	if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>) != "undefined") {
<%-- 15-07-2015 - MEV 29, punto 4 doc. di analisi funzionale: ELIMINARE MESSAGGIO DI CONFERMA nel caso di Revoca Affidamento in prova --%>
<%
if (!tipoRevoca.equals("AFFIDAMENTO")) {
%>
		if (document.LoadInserisciMisuraAlternativa.flagmisura.value == "N" //primo giro
    			&& (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value != ""
    			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>
    			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value != "")) {
			var continua = confirm("Nel caso in elaborazione occorre rideterminare la pena ?");
      		if (!continua) {
       			return false;
      		} else {
       			// proseguo con i controlli
       			//alert ("proseguo con i controlli");
   			}
		}
<%
}
%>
	}
<%
if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
	var data_to_verifica = document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
	if (!ControllaData(data_to_verifica)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
}
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
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
		alert('Data di emissione non valida');
		return false;
	}
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
		alert('Data di trasmissione non valida');
		return false;
	}
<%
}
%>
	// flagmisura = N sul primo giro. Sto inserendo i dati dell'ordinanza oppure l'ho selezionata dalla lista
	// controllo la completezza dei dati
	if (document.LoadInserisciMisuraAlternativa.flagmisura.value == "N") {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.value == "-") {
			alert("Selezionare il tipo di Ufficio Emittente");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
			return false;
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value == "") {
			alert("La Sede dell'Ufficio Emittente e' obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
			return false;
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO %>.value == "-") {
			alert("Selezionare l'Oggetto Ordinanza");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO %>.focus();
			return false;
		}
		// Data Revoca non obbligatoria ma deve essere formalmente corretta
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value;
		var data_to_verify_re = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value;
		if (!ControllaDataPassaVuota(data_to_verify_re)) {
			alert('Data Revoca non valida');
			return false;
		}
<%-- 29/11/2010 Controllo eliminato e ripristinato su indicazioni di Nunzia Alfieri (Test Esecuzione Pena presso Domicilio) --%>
<%
if (tipoRevoca != null && tipoRevoca.equals("AFFIDAMENTO")) {
%>
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value == ""
				&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value == "") {
			alert("Data Revoca o Pena Residua Rideterminata Obbligatori");
			return false;
		}
<%
}
%>
	} // end document.LoadInserisciMisuraAlternativa.flagmisura.value=="N"
<%
if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero()) {
%>
	// controllo sulla data detenzione se selezionato il check: detenuto dal
	if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value == "") {
			alert("La data di Detenzione e' obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.focus();
			return false;
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value != "") {
  			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value;
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value;
			var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value;
			if (!ControllaData(data_to_verify)) {
				alert('Data Detenzione non valida');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.focus();
      			return false;
     		}
  		}
	} else {
  		// alert("AAA = "+document.LoadInserisciMisuraAlternativa.tipo[1].checked)
	}
<%
}
// Se si sta inserendo il provvedimento dell'Esecuzione controllo i dati 
// del Magistrato e dei Destinatari
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == ""
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
		alert("Il  Magistrato Firmatario e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
		return false;
	}
<%
	// Se in Sospensione di Misura (cautelativa o provvisoria)
	if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null) {
		if (("AFFIDAMENTO".equals(tipoRevoca)
				&& (lPosizione.getCodPosizioneGiuridica().equals("32")
						|| lPosizione.getCodPosizioneGiuridica().equals("37")))
				|| ( "INDULTINO".equals(tipoRevoca)
						&& (lPosizione.getCodPosizioneGiuridica().equals("35")
								|| lPosizione.getCodPosizioneGiuridica().equals("40")))
				|| (ICostantiMisuraAlternativa.ESP_PRESSO_DOM.equals(tipoRevoca)
						&& (lPosizione.getCodPosizioneGiuridica().equals("51")
								|| lPosizione.getCodPosizioneGiuridica().equals("52")))) {
%>
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
			alert("L'Istituto di Detenzione e' obbligatorio");
			return false;
  		}
	}
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-') {
			alert("L'Autorita' competente per territorio e' obbligatorio");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.focus();
			return false;
  		}
	}
<%
		} else {
%>
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-') {
			alert("L'Autorita' competente per territorio e' obbligatorio");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.focus();
			return false;
  		}
	}
<%
  		}
	}
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value == "") {
<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione e' differente --%>
<%
	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	if (("PM".equals(codiceTipoUfficio)
			|| "PMM".equals(codiceTipoUfficio)
			|| "PGCAP".equals(codiceTipoUfficio))
			&& "UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	}
%>
		var descrTipoUfficio = '<%=descrTipoUfficio%>';
		alert("Il " + descrTipoUfficio + " e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.focus();
	 	return false;
	}
<%
	if (avvocati.size() >1) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].selectedIndex].value == '-') {
		alert("Il campo Autorita' per la Notifica  di un Condannato e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
		return false;
	}
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].selectedIndex].value == '-') {
		alert("Il campo Autorita' per la  Notifica di un Condannato e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
		return false;
	}
<%
	} else {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-') {
		alert("Il campo Autorita' per la Notifica  di un Condannato e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
		return false;
	}
<%
  	}
}
%>
}

function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

// 30/11/2010 Lista Uffici per TIPO_UFFICIO.
function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function functionRadio() {
	var tipoRevoca = '<%=tipoRevoca%>';
	var nodebot = document.getElementById('bottone');
	nodebot.style.display = 'block';
<%
// Sulla seconda form Visualizza le sezioni con i detinatari
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	var nodealt = document.getElementById('altri');
	var nodeist = document.getElementById('istituto'); 
	var nodeaut = document.getElementById('autorita');       
	nodealt.style.display = 'block';
<%     
	// Se in Sospensione provvisoria o cautelativa
	if ((tipoRevoca != null && tipoRevoca.equals("AFFIDAMENTO") && lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null
			&& (lPosizione.getCodPosizioneGiuridica().equals("32") || lPosizione.getCodPosizioneGiuridica().equals("37")))
			|| (tipoRevoca != null && tipoRevoca.equals("INDULTINO") && lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null
			&& (lPosizione.getCodPosizioneGiuridica().equals("35") || lPosizione.getCodPosizioneGiuridica().equals("40")))
  			|| (tipoRevoca != null && tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) && lPosizione != null
  			&& lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("50")
  					|| lPosizione.getCodPosizioneGiuridica().equals("51") || lPosizione.getCodPosizioneGiuridica().equals("52")))) {
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked == true) {               
		nodeist.style.display = 'none'; 
		nodeaut.style.display = 'block';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;          
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
	} else if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
		nodeist.style.display = 'block'; 
		nodeaut.style.display = 'none';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;          
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;           
	}
<%
	} else { // non in sospensione visualizzo
%> 
	if (typeof (document.LoadInserisciMisuraAlternativa.tipo) != "undefined"
			&& document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
		nodeist.style.display = 'block'; 
		nodeaut.style.display = 'none';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;          
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;           
	} else { 
		nodeist.style.display = 'none'; 
		nodeaut.style.display = 'block';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;          
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;           
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
	}
<%
  	}
}
if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero()) {
	if (misurasospesa != null && misurasospesa.getDataInizioMisura() != null) {
%>
	var mese = <%=DateUtils.getDateToString(misurasospesa.getDataInizioMisura(),"MM")%>;
	var giorno = <%=DateUtils.getDateToString(misurasospesa.getDataInizioMisura(),"dd")%>;
	var anno = <%=DateUtils.getDateToString(misurasospesa.getDataInizioMisura(),"yyyy")%>;
	if (mese < 10)
  		mese = '0' +mese;
	if (giorno < 10)
  		giorno= '0' +giorno;
<%
	}
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[0].checked == true) {
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = "";
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = "";
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value = "";
		<%-- Ticket#202206160110 - Restava abilitata la data anche se contro soggetto non detenuto --%>
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.readOnly = true; 
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.readOnly = true; 
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.readOnly = true; 
		<%-- Ticket#202206160110 - FINE --%>
	} else if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
<%
	if (nuovapenaresidua == null || nuovapenaresidua.getIdPenaResidua() == null) {
%>        	
		<%-- Ticket#202206160110 --%>
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.readOnly = false; 
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.readOnly = false; 
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.readOnly = false; 
		<%-- Ticket#202206160110 - FINE --%>     
<%
	}
	if (misurasospesa != null && misurasospesa.getDataInizioMisura() != null) {
		// MEV29 - la data non va piu' precaricata ma resta obligatoria%>
		<%-- document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = giorno; --%>
		<%-- document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = mese; --%>
		<%-- document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value = anno; --%>
<%
	}
%>
	}
<%
}
%>
}
</script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>
<%-- [SG]: 20220622: Decisioni Sorveglianza >> Esecuzione Pena Presso domicilio >> Revoca = Revoca Espiazione Pena presso Domicilio
	aggiunta chiamata a funzione al caricamento della pagina se la action torna un errore --%>
<body class="corpo" onload="functionRadio(); caricaComboOggetto();">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
if (tipoRevoca != null && tipoRevoca.equals("INDULTINO")) {
%>
        	<font class="campo">Revoca L.207/2003</font>
<%
} else if (tipoRevoca != null && tipoRevoca.equals("AFFIDAMENTO")) {
%>
        	<font class="campo">Revoca Affidamento in Prova</font>
<%
} else if (tipoRevoca != null && tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
        	<font class="campo">Revoca Espiazione Pena presso Domicilio</font>
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
<%
BigDecimal lIdOrdinanzaSius = null;
if (misuraalternativa != null && misuraalternativa.getEveIdEvento() != null) {
	lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
}
%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciRevocaMAAffProv">
<INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
<INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">
<%
if (tipoRevoca != null && tipoRevoca.equals("INDULTINO")) {
%>
<input type="HIDDEN" name="tipomisura" value="INDULTINO">
<%
} else if (tipoRevoca != null && tipoRevoca.equals("AFFIDAMENTO")) {
%>
<input type="HIDDEN" name="tipomisura" value="AFFIDAMENTO">
<%
} else if (tipoRevoca != null && tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
<input type="HIDDEN" name="tipomisura" value="<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>">
<%
}
%>
<input type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
<%
if (misurasospesa != null && misurasospesa.getDataInizioMisura() != null) {
%>
<input type="HIDDEN" name="ggnuovoiniziopena" value="<%=DateUtils.getDayToString(misurasospesa.getDataInizioMisura())%>">
<input type="HIDDEN" name="MMnuovoiniziopena" value="<%=DateUtils.getMonthToString(misurasospesa.getDataInizioMisura())%>">
<input type="HIDDEN" name="yyyynuovoiniziopena" value="<%=DateUtils.getYearToString(misurasospesa.getDataInizioMisura())%>">
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
//==============================================================================
// d.f. 13/03/2015 questi controlli sono tutti errati, da rivedere!!!!!!!!!!!!!!
// 
// Sul primo giro i campi restano tutti abilitati in quanto 'misuraalternativa'
// di fatto e' null.
// Sul secondo giro non ha senso tenerli abilitati in quanto i dati sono stati 
// utilizzati per i calcoli sul primo giro e non verranno aggiornati, quindi 
// che senso ha consentire all'utente di modificarli?
//
// Inoltre:
// Sembra che si vogliano disabilitare i campi solo nel caso in cui la misura 
// sia stata inserita da SIUS (misuraalternativa.getFlagUfficioInserimento() == null)
// in caso contrario il flag vale 'P' Procura.
// 
// Sulla disabilitazione del check Esecuzione contro soggetto non detetnuto ('scarcerare')
// si testa misuraalternativa.getDataIngressoIstituto() != null
// ma in questo caso e' necessariamente null essendo non detenuto.
//
// Sul test di disabilitazione della data ingresso in istituto si testa:
// misuraalternativa.getDataScarcerazione() != null
// si dovrebbe testare 
// misuraalternativa.getDataIngressoIstituto() != null
// Probabilmente un refuso della 'concessione' in cui si parla di data Scarcerazione mentre
// sulla 'revoca' conta a data ingresso in istituto 
//==============================================================================
if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione() != null) {
	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
    	scarcerato = "checked";
   		if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataIngressoIstituto() != null) {
   			// la data scarcerazione viene disabilitata SOLO se getFlagUfficioInserimento()  = null 
      		// ovvero solo se il dato e' stato iscritto da SIUS
      		dascarceraredisabilita = "disabled";
    	}
  	}
  	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")) {
   		scarcerare = "checked";
    	if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataIngressoIstituto() != null) {
      		scarceratodisabilita = "disabled";
    	}
  	}
  	if (misuraalternativa.getCodTipoUfficioScarcerazione().equals("-")) {
   	 	scarcerare = "checked";
  	}
}
if (misuraalternativa != null && misuraalternativa.getDataScarcerazione() != null
		&& misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
	if (misuraalternativa.getFlagUfficioInserimento() == null) {
    	disabilitaData = "readonly";
   	}
}
%>
<table>
	<tr>
      	<td class="l">Posizione Giuridica </td>
     	<td class="L" colspan="5">
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
		<td class="L" colspan="5">
			<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
		if (lAltraCausa.getIstitutoDetenzione().getDescrComune() != null) {
%>
			di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
        }
%>
		</td>
	</tr>
<%
		if (lAltraCausa != null && lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo </td>
		<td class="L" colspan=5>
		  	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
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
<%
	if (lLuogoDetenzione.getIstitutoDetenzione().getDescrComune() != null) {
%>
			di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
	}
%>
		</td>
	</tr>
<%
}
%>
	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02")
		|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
		</td>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		<%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%>" type="text" name=<%=ICostantiLuogoDetenzione..CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
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
		<td class="l" colspan="2">
			<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
<%
		if (penaresidua.getImportoMulta().compareTo((new BigDecimal(0))) != 0) {
%>
		<td class="l">Multa</td>
		<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        } 
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
<%
		if (penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0))) != 0) {
%>
		<td class="l">Ammenda</td>
		<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
		}
	}
}
%>
	</tr>
<%
if (!lPosizione.isLibero() && penaresidua.getDataInizio() != null) {
%>
	<tr>
		<td class="l">Data Decorrenza Pena</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	} else {
		if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
		}
	}
}
if ((!lPosizione.isLibero())
		|| (lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
		if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		 	-
		 	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		 	-
		 	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
			} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
		  	<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
			}
		}
	}
}
%>
	</tr>
<%
if (misuraconcessa != null) {
%>  
  	<tr>
<%
	if (misuraconcessa.getDataInizioMisura() != null) {
%>
		<td class="l">Data Inizio Misura</td>
    	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraconcessa.getDataInizioMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
	}
	if (misuraconcessa.getDataFineMisura() != null) {
%>
		<td class="l">Data Fine Misura</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraconcessa.getDataFineMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
	}
%>
	</tr>      
<%
}
if (misuraammissioneprovv != null) {
%>
  	<tr>
<%
	if (misuraammissioneprovv.getDataInizioMisura() != null) {
%>
		<td class="l">Data Inizio Misura</td>
    	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraammissioneprovv.getDataInizioMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
	}
	if (misuraammissioneprovv.getDataFineMisura() != null) {
%>
		<td class="l">Data Fine Misura</td>
    	<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraammissioneprovv.getDataFineMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
	}
%>
  	</tr>
<%
}
%>
<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	<tr>
		<td class="l">Data Emissione</td>
		<td class="L" >
	  		<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">Data Trasmissione</td>
		<td class="L">
		 	<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
			<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
<%
}
%>
</table>
<%--
==============================================================================
                     Dati dell'ordinanza di revoca
==============================================================================
--%>
<table style="width: 95%;">
	<tr>
      	<td class="Titolo" colspan="4"> Dati Ordinanza di Revoca del Tribunale di Sorveglianza </td>
	</tr>
<%
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
	// n.b. teoricamente si entra qui solo sul secondo giro quindi tutti i dati 
	//      dell'ordinanza sono gia' stati inseriti/selezionati ne deriva che
	//      misuraalternativa.getIdMisuraAlternativa() != null
	//      Quindi gran parte del codice inserito in questa sezione non viene
	//      mai eseguito, quello relativo all'inserimento dati.
%>
	<tr>
		<td class="l" width="15%">Anno / Numero Sius</td>
<%
	if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
		<td class="l" width="20%">
		  	<input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4"> /
			<input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6">
		</td>
		<td class="l" width="25%"> Anno / Numero Ordinanza </td>
		<td class="l" width="20%"><input value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4"> /
			<input value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6">
		</td>
<%
	} else {
%>
		<td class="l" width="20%">
			<font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
		</td>
		<td class="l" width="25%"> Anno / Numero Ordinanza </td>
		<td class="l" width="20%"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
			<font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
		</td>
<%
	}
%>
	</tr>
    <tr>
		<td class="l">Ufficio Emittente </td>
<%
	if (misuraalternativa.getChiaveUfficioFascicoloSius() == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
		<td class="l" colspan="3">
<%
		// x Indultino e Esp Pre Dom la revoca puo' essere fatta da TDS o UDS, visualizzo combo
		if (tipoRevoca.equals("INDULTINO")
				|| tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
			<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff)%>
		</td>
	</tr>
	<tr>
		<td class="l">Sede Ufficio Emittente<font class=ob>(*)</font></td>
		<td class="l" colspan="3">
			<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" value="<%=distretto%>" type="text" maxlength="35" size="35">
			<a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>');"><img src="/images/filefolder.gif" border=0></a>
		</td>
<%
		} else {
%>
		<td class="l" colspan="4">
			<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
		</td>
	</tr>
	<tr>
		<td class="l">Sede Ufficio Emittente<font class=ob>(*)</font></td>
		<td class="l" colspan="3">
			<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" value="<%=distretto%>" type="text" maxlength="35" size="35">
			<a href="Javascript:ListaComuniEmitTdsMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');"><img src="/images/filefolder.gif" border=0></a>
		</td>
<%
		}
	} else {
%>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione e' differente --%>
<%
		String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
		if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio))
				&& "UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
			descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
		}
%>
		<td class="l" colspan="3">
			<font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font>
		</td>
<%
	}
%>
  	</tr>
  	<tr>
    	<td class="l">Oggetto Ordinanza </td>
<%
	if (misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa() != null) {
%>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
<%
	} else {
%>
		<td class="L" colspan="3">
			<select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
				<%=motivoProvv%>
			</select>
		</td>
<%
	}
%>
    </tr>
    <tr>
		<td class="l">Data Emissione Ordinanza </td>
<%
	if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
		<td class="l" colspan="3">
			<font class="campo">
				<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
				<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
				<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</font>
		</td>
<%
	} else {
%>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%></font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">
		</td>
<%
	}
%>
	</tr>
<%
} else {
	//==========================================================================
	// Primo giro: devo selezionere i dati dell'ordinanza di revoca o inserirli
	//==========================================================================
%>
	<tr>
		<td class="l" colspan="4">
        	<a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          		Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
	</tr>
    <tr>
      	<td class="l">Anno /Numero SIUS</td>
      	<td class="l">
        	<input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" 
				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               	onChange="pulisciId();">
       		/
        	<input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" 
               	onkeypress="return TicTabNumField(this,event)"
               	onChange="pulisciId();">
		</td>
      	<td class="l"> Anno / Numero Ordinanza</td>
      	<td class="l">
        	<input Title="Anno Orinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" 
               	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               	onChange="pulisciId();">
        	/
        	<input Title="Numero Ordinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" 
               	onkeypress="return TicTabNumField(this,event)"
               	onChange="pulisciId();">
      	</td>
	</tr>
    <tr>
		<td class="l">Ufficio Emittente</td>
		<td class="l" colspan="3">
<%
	String listaComuniScript = "";
	if (tipoRevoca != null && !tipoRevoca.equals("INDULTINO") && !tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
		listaComuniScript = "ListaComuniEmitTdsMinor";
%>
			<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
<%
	} else {
		listaComuniScript = "ListaComuniEmitUTMinor";
%>
			<!-- Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ****** -->
          	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='pulisciId();caricaComboOggetto();'")%>
		  	<!-- Modifica del 24/02/2016 Nuova Infrastruttura - FINE ****** -->
<%
	}
%>
		</td>
    </tr>
    <tr>
		<td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
		<td class="l" colspan="3">
		  	<font class="campo">
		    	<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
				<a href="Javascript:<%=listaComuniScript%>('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
		      		<img src="/images/filefolder.gif" border=0>
		    	</a>
		  	</font>
		</td>
    </tr>
    <tr>
		<td class="l">Oggetto Ordinanza</td>
		<td class="L" colspan="3">
			<select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
<%
	if (tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
%>
				<option value="-">-</option>
<%
	} else {
%>
				<%=motivoProvv%>
<%
	}
%>
			</select>
		</td>
	</tr>
    <tr>
		<td class="l">Data Emissione Ordinanza</td>
      	<td class="l" colspan="3">
        	<font class="campo">
				<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
				<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
				<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        	</font>
		</td>
    </tr>
<%
}
// Fine blocco con i dati dell'ordinanza
// END IF (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null)

//============================================================================
// Blocco con i dati della Data Revoca e/o Pena Rideterminata
// -- Misura Alternativa Revocata dal
// -- 
//============================================================================
if ((misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null)
		|| (nuovapenaresidua == null || nuovapenaresidua.getIdPenaResidua() == null)) {
	// o primo giro (misuraalternativa.getIdMisuraAlternativa() == null)
	// o secondo ma pena non rideterminata (nuovapenaresidua.getIdPenaResidua() == null) forse???
%>
	<tr></tr><tr></tr><tr></tr>
<%
	if (!tipoRevoca.equals("AFFIDAMENTO")) {
%>
	<tr>
		<td class="lRosso" colspan="4">
			<%=lNota%>
        </td>
	</tr>
<%
	}
%>
	<tr>
		<td class="l">Misura Alternativa Revocata dal</td>
		<td class="l" colspan="3">
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();" > -
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
				<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
			</font>
		</td>
	</tr>
	<tr>
		<td class="l">Pena residua rideterminata</td>
		<td class="l" colspan="3">   RECLUSIONE
			Anni &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
			</font>
			Mesi &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
			</font>
			Giorni &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
			</font>
			ARRESTO
			Anni &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>" onChange="pulisciId();">
			</font>
			Mesi &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>" onChange="pulisciId();">
			</font>
			Giorni &nbsp;
			<font class="campo">
				<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>" onChange="pulisciId();">
			</font>
		</td>
	</tr>
<%
} else if (misuraalternativa != null && misuraalternativa.getDataInizioRevoca() != null
		&& nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
	// Secondo giro misura selezionata/inserita e pena ricalcolata visualizzo
	// i dati e riporto nei campi hidden i valori
%>
	<tr></tr><tr></tr><tr></tr>
    <tr>
		<td class="l">Misura Alternativa Revocata dal</td>
        <td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioRevoca(), "dd-MM-yyyy"))%></font>          
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataInizioRevoca()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataInizioRevoca()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataInizioRevoca()))%>">
       	</td>
	</tr>
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
<%
	if (misuraalternativa.getNumAnniRevocaReclusione() != null
			|| misuraalternativa.getNumMesiRevocaReclusione() != null
			|| misuraalternativa.getNumGiorniRevocaReclusione() != null 
			|| misuraalternativa.getNumAnniRevocaArresto() != null 
			|| misuraalternativa.getNumMesiRevocaArresto() != null
			|| misuraalternativa.getNumGiorniRevocaArresto() != null) {
%>
		<td class="l">Pena residua rideterminata</td>
		<td class="l" colspan="3">RECLUSIONE
			&nbsp;Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaReclusione(),"0")%></font>
			&nbsp;Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaReclusione(),"0")%></font>
			&nbsp;Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaReclusione(),"0")%></font>
			&nbsp;ARRESTO
			&nbsp;Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaArresto(),"0")%></font>
			&nbsp;Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaArresto(),"0")%></font>
			&nbsp;Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaArresto(),"0")%></font>
<%
	}
%>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaReclusione())%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaReclusione())%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaReclusione())%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaArresto())%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaArresto())%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaArresto())%>">
		</td>      
<%
}
// Solo se "In Misura" (13,27,50), si escludono quindi le sospensive e le provvisorie,
// visualizzo i giorni passati in istituto
if (("AFFIDAMENTO".equals(tipoRevoca) && "13".equals(lPosizione.getCodPosizioneGiuridica()))
		|| ("INDULTINO".equals(tipoRevoca) && "27".equals(lPosizione.getCodPosizioneGiuridica()))
      	|| (ICostantiMisuraAlternativa.ESP_PRESSO_DOM.equals(tipoRevoca) && "50".equals(lPosizione.getCodPosizioneGiuridica()))) {
%>
	<tr>
		<td class="l">Periodo espiato in Istituto da detrarre</td>
		<td class="l" colspan="3">
<%
	if (misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa() != null
			&& nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
			Giorni: <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getFlagPeriodoEspiato())%></font>
<%
	} else if ((misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null)
			|| (nuovapenaresidua == null || nuovapenaresidua.getIdPenaResidua() == null)) {
%>
      		<input type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO%>">
<%
	}
%>
			&nbsp;
		</td>
	</tr>
<%
}
if (misuraalternativa != null && misuraalternativa.getDataInizioRevoca() != null
		&& nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan="3">
		  	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
		</td>
	</tr>
<%
} else if ((misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null)
		|| (nuovapenaresidua == null || nuovapenaresidua.getIdPenaResidua() == null)) {
%>
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
if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null && !lPosizione.isLibero()) {
%>
<table>
	<tr>
<%
	if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
		// secondo giro 
		if (misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa() != null
				&& misuraalternativa.getDataIngressoIstituto() != null) {
%>
		<td class="l">Esecuzione contro soggetto non detenuto &nbsp;<input type="radio" name="tipo" value="nondetenuto" disabled>
			&nbsp; Esecuzione contro soggetto detenuto &nbsp; <input type="radio" name="tipo" value="detenuto" checked>
		</td>
		<td class="l">Dal &nbsp;      
			<input readonly type="text" size="2" maxlength="2" value="<%=DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"dd")%>"  name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" <%=disabilitaData%>  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input readonly type="text" size="2" maxlength="2" value="<%=DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"MM")%>"  name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>" <%=disabilitaData%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input readonly type="text" size="4" maxlength="4" value="<%=DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"yyyy")%>"  name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" <%=disabilitaData%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else {
%>
		<td class="l">Esecuzione contro soggetto non detenuto &nbsp;<input type="radio" name="tipo" value="nondetenuto"  checked  >
			&nbsp; Esecuzione contro soggetto detenuto &nbsp; <input type="radio" name="tipo" value="detenuto"  disabled  >
		</td>
		<td  class="l">Dal &nbsp;      
			<input readonly type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input readonly type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input readonly type="text" size="4" maxlength="4" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		}
	} else {
%>
		<td class="l">Esecuzione contro soggetto non detenuto &nbsp;<input type="radio" name="tipo" value="nondetenuto" checked onclick="functionRadio();">
			&nbsp; Esecuzione contro soggetto detenuto  &nbsp; <input type="radio" name="tipo" value="detenuto" onclick="functionRadio();">
		</td>
		<td class="l">Dal &nbsp;
			<input type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="2" maxlength="2" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
			<input type="text" size="4" maxlength="4" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
	}
%>
	</tr>
</table>
<%
}

//==============================================================================
// Seconda sezione della form visualizzata solo nel caso di inserimeto 
// provvedimento SIEP:
// -- Pena residua rideterinata
//==============================================================================
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
<table style="width: 95%;">
	<tr>
		<td class="Titolo" colspan="8"> Pena Residua da Espiare </td>
    </tr>
    <tr>
		<td class="l">Reclusione</td>
		<td class="l">
		  	Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniReclusione())%></font>
		  	&nbsp;Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiReclusione())%></font>
		  	&nbsp;Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniReclusione())%></font>
		</td>
		<td class="l" colspan="2">Arresto</td>
		<td class="l" colspan="3">
		  	Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniArresto())%></font>
		  	&nbsp;Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiArresto())%></font>
		  	&nbsp;Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniArresto())%></font>
		</td>
	</tr>
  	<tr>
<%
	if (nuovapenaresidua.getDataInizio() != null) {
%>
		<td class="l">Data Decorrenza Pena</td>
		<td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
<%
	}
	if (nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
			-
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
	} else if (nuovapenaresidua.getDataFine() != null) {
%>
		<td class="l">Data Fine Pena</td>          
		<td class="L" colspan="2">
<%
		if (nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())) {
%>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%></font>
<%
		} else {
%>
			<font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%></font>
<%
		}
%>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
	}
%>
	</tr>
</table>
<%
}
if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
<table style="width: 95%;">
	<input type="hidden" Title="presenzanuovopena" name="presenzanuovopenaricalcolata" value="S">
 	<tr>
     	<td class="Titolo" colspan=6> Magistrato Firmatario </td>
  	</tr>
  	<tr>
		<td class="l">Magistrato Firmatario</td>
		<td class="L">
		  	<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
			<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME %>','<%=ICostantiMagistrato.CAMPO_NOME %>');">
		    	<img src="/images/filefolder.gif" border="0">
		  	</a>
		</td>
		<td>
		</td>
	</tr>
   	<tr>
		<td class="Titolo" colspan="6">Destinatari</td>
	</tr>
</table>

<div id="istituto" style="width: 100%; display:none; position:relative;">  
<table style="width: 95%;">
	<tr>
		<td class="l" width ="30%">Istituto di Detenzione <font class="ob">(*)</font></td>
      	<td class="l">    
<%
	if (posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione() != null
			&& posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null) {
%>
			<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=70>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
<%
	} else {
%>
			<input readonly Title="Istituto" name="Comune" value="" size=70>
			<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
<%
	}
%>
		</td>
	</tr>
</table>
</div> 

<div id="autorita" style="width: 100%; display:none; position:relative; " >  
<table style="width: 95%;">
	<tr>
        <td class="l" width ="30%">Autorita' Competente per territorio <font class="ob">(*)</font></td>
        <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
				<%=codiceAutoritaE%>
            </select>
        </td>
	</tr>
    <tr>
		<td class="l" width ="30%">Sede</td>
		<td class="L">
		    <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
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

<div id="altri" style="width: 100%; display:none; position:relative;">  
<table width ="95%">
	<tr>
	    <%--30/11/2010 td class="l" width ="30%">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
	    <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>"--%>
	    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione e' differente --%>
<%
	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio))
			&& "UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
   	}
%>
		<td class="l" width ="30%"><%=descrTipoUfficio%> <font class=ob>(*)</font></td>
		<input type="hidden" value="<%=StringUtils.toStringJSP(UfficioEmittente.getCodTipoUfficio())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
		<td class="l" colspan="3">
  			<input title="Sede" value="<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
			<a href="Javascript:ListaUfficiPerTipo('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>','<%=StringUtils.toStringJSP(UfficioEmittente.getCodTipoUfficio())%>');">
     			<img src="/images/filefolder.gif" border=0>
     		</a>
		</td>
	</tr>
  	<tr>
		<td class="Titolo" colspan="6">Destinatario per Notifica </td>
  	</tr>
</table>
<%
	int lIdxAvv = 0;
    Iterator lItxAvv = avvocati.iterator();
	while (lItxAvv.hasNext()) {
		AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
<table>
	<tr>
    	<td class="l">Per Avvocato&nbsp;
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
		</td>
		<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
	</tr>
</table>
<table>
  	<tr>
    	<td class="l">Autorita' Destinazione <font class=ob>(*)</font></td >
    	<td class="L" colspan="3">
      		<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
				<%=autoritaEsternaAvv%>
    		</select>
  		</td>
	</tr>
	<tr>
  		<td class="l">Sede </td><td class="L">
    		<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
    			<img src="/images/filefolder.gif" border="0">
  			</a>
		</td>
		<td class="l">Note</td>
		<td class="L">
  			<textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>" cols=35></textarea>
     	</td>
	</tr>
  	<tr><td>&nbsp;</td>  </tr>
</table>
<%
		lIdxAvv++;
	}
%>
</div>
<%
}
%>

<div id="bottone" style="width: 100%; display:none; position:relative; " >  
<table>
	<tr>
		<td class="lNoBord" colspan="2">
			<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
		</td>
	</tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
<%
if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null) {
%>
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
}
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2050");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2050");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2050");

<%
	if (nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua() != null) {
%>
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");
<%
	}
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>","numeric","Pena Residua Rideterminata : Il Campo Anni Reclusione e' numerico");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>","numeric","Pena Residua Rideterminata : Il Campo Mesi Reclusione e' numerico");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>","numeric","Pena Residua Rideterminata : Il Campo Giorni Reclusione e' numerico");

frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>","numeric","Pena Residua Rideterminata : Il Campo Anni Arresto e' numerico");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>","numeric","Pena Residua Rideterminata : Il Campo Mesi Arresto e' numerico");
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>","numeric","Pena Residua Rideterminata : Il Campo Giorni Arresto e' numerico");
<%
}
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>","alphabetic");
<%
}
if ((tipoRevoca != null && tipoRevoca.equals("AFFIDAMENTO") && lPosizione != null
		&& lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("13"))
  		|| (tipoRevoca != null && tipoRevoca.equals("INDULTINO") && lPosizione != null
  		&& lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("27"))
  		|| (tipoRevoca != null && tipoRevoca.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) && lPosizione != null
  		&& lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("50"))) {
	if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_FLAG_PERIODO_ESPIATO%>","numeric","Il Campo Periodo espiato in Istituto da detrarre e' numerico");
<%
	}
}
%>
</script>
</body>
</html>