<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"           	scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"         	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"       	scope="request" class="java.lang.String"/>
<%-- Destinatari --%>
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"    	scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"    	scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="codiceAutoritaE"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="UffTDS"              	scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"              	scope="request" class="java.lang.String"/>
<%-- Se provengo dal Verbale Sottoscrizione, viene caricata e passata alla form la Misura Alternativa di concessione --%>
<jsp:useBean id="misuraalternativa"   	scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"            	scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="luogodetenzione"     	scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="sedepoliziaNotN"     	scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="sedepoliziaNotC"     	scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="motivoProvv"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoIstituto"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente" 	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="verbale"             	scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="lcodicePosizione"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="lFlagAffi"           	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="idmisuraalternativa" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="codicemotivo"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoammissioneprovvisoria" 	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="maammissioneprovvisoria" 		scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="eventoammissioneprovvisoriaaffidamento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="tipoUfficioSIUS" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="lFlagSanzione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 		scope="request" class="java.lang.String"/>
<%-- MEV_9-SIEP: aggiunti useBean --%>
<jsp:useBean id="tipoOperazione" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="comboCSSATrattinoModif"		scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUfficioEmittenteModif"	scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
// instanzia il model dell'EVENTO per prelevare la data di emissione (se esiste)
// per inserirla come default nel campo "Data Ammissione Provvisoria a Detenzione Domiciliare" (posizione giuridica 29)
EventoModel lEventoAmmProvv = eventoammissioneprovvisoria;
// instanzia il model della misura alternativa per prelevare il luogo della detenzione domiciliare
MisuraAlternativaModel lMAAmmProvv = maammissioneprovvisoria;

// Instanzia il model dell'EVENTO (per  posizione giuridica "13" motivo "2006"
// e tipo provvedimento "4" o "9" o "12" quando proviene da ammissione provvisoria))
EventoModel lEventoAmmProvvAff = eventoammissioneprovvisoriaaffidamento;

if (lPosizione == null)
  	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
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

// Chiamata lista Avvocati
function ListaAvvocati(a_formname) {
  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
}

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
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'DETENZIONE') {
  		tipoMA = '<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'ESP_PRESSO_DOM') {
  		tipoMA = '<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>';
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'SEMILIBERTA') {
  		tipoMA = '<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
	} else if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'INDULTINO') {
  		tipoMA = '<%=ICostantiMisuraAlternativa.INDULTINO%>';
	}
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.CONCESSIONE%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
}

function pulisciId() {
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value = "";
}

function Verify() {
<%
// Controllo campo Data Fine Pena se Editabile
if (!lPosizione.isLibero() || ("S".equals(lFascicoloAssociato.getFlagAltraCausa())
        && penaresidua.getDataFinePresunta()!= null 
        && penaresidua.getDataFine() == null)) {
	if (!penaresidua.isErgastolo()) {
    	if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value = '0' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
	var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value + '/' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value + '/' +
		document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
		}
	}
}
%>
	// Data Trasmissione
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
	    return false;
  	}
	var campo = document.LoadInserisciMisuraAlternativa.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value;
	// MEV_9-SIEP: aggiunta variabile
	var codMotivo = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value;
	// Controllo sui campi dell'ordinanza
	if (document.LoadInserisciMisuraAlternativa.flagmisura.value == "N") {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value + '-' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
		if (!ControllaData(data_to_verify)) {
			alert('Data di emissione Ordinanza non valida');
			return false;
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value == "") {
			alert("La Sede dell'Ufficio Emittente e' obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.focus();
			return false;
  		}
		// MEV_9-SIEP: aggiunti controlli per determinati codici di AFFIDAMENTO + DETENZIONE + SEMILIBERTA'
		if (codMotivo == "0720" || codMotivo == "0721" || codMotivo == "0722" || codMotivo == "0723" || codMotivo == "0730"
				|| codMotivo == "0731" || codMotivo == "0732" || codMotivo == "0733" || codMotivo == "0734") {
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.value == ""
					|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>.value == "") {
				alert('Anno e Numero Ordinanza Provvisoria obbligatori!');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>.focus();
				return false;
			}
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value == ""
					&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value == ""
					&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value == "") {
				alert('Data Emissione Provvedimento obbligatoria!');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.focus();
				return false;
			}
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value != ""
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value != ""
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value != "") {
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value;
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value;
			var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value;
			if (!ControllaData(data_to_verify)) {
				alert('Data Emissione Provvedimento non valida');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.focus();
				return false;
			}
		}
		// FINE MEV_9-SIEP
	}
<%
// AMBROSINO   - 23-12-2010
//============================================================================
// Controllo sui campi: Scarcerato/da scarcerare, Data Scarcerazione, Data Ammissione Provvisoria
//============================================================================
// MEV_9-SIEP: aggiunta diversificazione e controllo validità data
// if (lPosizione.isLibero() || tipoMisura.equals("SEMILIBERTA")) {
if (lPosizione.isLibero() && tipoMisura.equals("AFFIDAMENTO")) {
%>
		// MEV_9-SIEP: aggiunti controlli
		if (codMotivo == "0720" || codMotivo == "0721" || codMotivo == "0730" || codMotivo == "0731" || codMotivo == "0732") {
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value == ""
					&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value == ""
					&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value == "") {
				alert('Data Applicazione Provvisoria obbligatoria');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.focus();
				return false;
			}
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value != ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value != "") {
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;
			if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length == 1)
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = '0' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;
			var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value + '-' +
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;
			if (!ControllaData(data_to_verify)) {
				alert('Data Applicazione Provvisoria non valida');
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.focus();
				return false;
			}
		}
// FINE MEV_9-SIEP
<%
} else if (lPosizione.isDetenuto()
				|| lPosizione.getCodPosizioneGiuridica().equals("14") // Espiazione Pena in Regime di Semilibertà
        		|| lPosizione.getCodPosizioneGiuridica().equals("04")) { // Arresti Domiciliari ex art. 656/10
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value == "") {
			alert("La data di Scarcerazione/Esecuzione e' obbligatoria")
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
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value
           	+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value
           	+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
		if (!ControllaData(data_to_verify)) {
			alert('Data Scarcerazione/Esecuzione non valida');
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
			return false;
  		}
	}
<%
} // chiude if (!lPosizione.isDetenuto() && !tipoMisura.equals("SEMILIBERTA")   
else if (lPosizione.getCodPosizioneGiuridica().equals("29")) { // Detenzione Domiciliare Provvisoria (29)
%>
	if (document.LoadInserisciMisuraAlternativa.tipomisura.value == 'DETENZIONE'
			|| document.LoadInserisciMisuraAlternativa.tipomisura.value == 'AFFIDAMENTO') {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value
			+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value
			+ '-' +	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;
		if (!ControllaData(data_to_verify)) {
		    alert('Data Ammissione Provvisoria a Detenzione Domiciliare non valida');
	    	return false;
		}
	}
<%
// MEV_9-SIEP: aggiunto controllo
} else if ((lPosizione.getCodPosizioneGiuridica().equals("13") && lEventoAmmProvvAff.getIdEvento() != null)
				|| lPosizione.getCodPosizioneGiuridica().equals("54")) {
%>
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length > 0
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length > 0
			|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value.length > 0) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length == 1)
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value = '0' +
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value
			+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value
			+ '-' +	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;
		if (!ControllaData(data_to_verify)) {
			<%
				if (tipoMisura.equals("AFFIDAMENTO")) {
			%>
					    alert('Data Applicazione Provvisoria non valida');
			<%
				} else {
			%>
						alert('Data Ammissione Provvisoria a Detenzione Domiciliare non valida');
			<%
				}
			%>
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.focus();
		    return false;
		}
	}
<%
// FINE MEV_9-SIEP
} else if (lPosizione.isMisuraAlternativa() || lPosizione.getCodPosizioneGiuridica().equals("27")) { // L.207/03 indultino
	// nulla
} else { // per le altre posizioni giuridiche
%>
	if (document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value == "") {
			alert("La data di Scarcerazione e' obbligatoria")
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
		var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value
			+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value
			+ '-' + document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
		if (!ControllaData(data_to_verify)) {
			alert('Data Scarcerazione non valida');
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
			return false;
  		}
	}
<%
}
%>
	//=======================================
	// Controlli sui destinatari
	//=======================================
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == ""
			&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
		alert("Il  Magistrato Firmatario e' obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
		return false;
	}
	// Destinatari per l'esecuzione se abilitato è obbligatorio
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-'
  				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.value == "") {
<%
if (lPosizione.isLibero() && (verbale == null || verbale.getIdVerbale() == null)) {
%>
			alert("Il Campo Destinatario per esecuzione è obbligatorio");
<%
} else {
%>
			alert("Il Campo Autorità competente per territorio è obbligatorio");
<%
}
%>
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.focus();
    		return false;
		}
	}
	// Istituto di detenzione se abilitato è obbligatorio
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
		    alert("Il Campo Istituto di Detenzione e' obbligatorio");
		    return false;
  		}
	}
<%--
	// MEV10-s3: aggiunto controllo preventivo
	if (document.getElementById('<%=MinorMask.ComboCSSAId%>').value != undefined &&
		document.getElementById('<%=MinorMask.ComboCSSAId%>').value == "-") { 
		alert("L'UEPE/USSM e' obbligatorio");
		return false;
	}
--%>
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == ""
				|| document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "-"
				|| document.LoadInserisciMisuraAlternativa.<%=MinorMask.ComboCSSAId%>.value == "-") {
			alert("L'UEPE/USSM e' obbligatorio");
			return false;
  		}
	}
	<%-- MEV10-s3: modificato il controllo: document.getElementById('nodesor') && (nodesor.style.visibility!='hidden') && --%>
	<%-- 20170908: [SG] aggiunte and condition --%>
	if (document.getElementById('divsor') && document.getElementById('divsor').style.visibility != 'hidden'
			&& !document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value == "") {
 			// MEV10-s3: modificato msg
			alert ("L'Ufficio / Magistrato di Sorveglianza e' obbligatorio");
			return false;
  		}
	}
<%
if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
<%-- MEV10-s3: modificato il controllo: document.getElementById('nodesor') && (nodesor.style.visibility!='hidden') && --%>
<%-- 20170908: [SG] aggiunte and condition --%>
	if (document.getElementById('divsor') && document.getElementById('divsor').style.visibility != 'hidden'
			&& !document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled) {
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value == "") {
			// MEV10-s3: modificato msg
	     	alert("Il Tribunale di Sorveglianza e' obbligatorio");
	     	return false;
   		}
 	}
<%
}
%>
	if (!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled) {
  		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-') {
			alert("Autorita' competente per territorio e' obbligatorio");
			return false;
		}
		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.value == "") {
			alert("La Sede dell'autorita' competente per il territorio e' obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.focus();
			return false;
    	}
  	}
 	// inserisco il parametro return true per disattivare il pulsante "conferma" del form
 	return true;
} <%-- Chiude function Verify --%>

function ListaCSSA(a_formname,a_fieldname,a_field2) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

// magistrato competente
function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaUDS(a_formname,a_fieldname) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaComuniTds(formname,fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function DisabilitaAvvocato() {
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
  		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[0].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[1].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[0].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[1].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[0].disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[1].disabled = true;
	} else {
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = true;
    }
}

function AbilitaAvvocato() {
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[0].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[1].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[0].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[1].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[0].disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[1].disabled = false;
	} else {
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
    }
}

//==============================================================================
// Funzione che visualizza/nasconde le DIV dei destinatari in funzione di:
// - tipo misura
// - posizione giuridica
// - Check scarcerato/da scarcerare
//==============================================================================
function radio() {
	var pos = document.LoadInserisciMisuraAlternativa.CodPosizioneGiuridica.value;
	var affi = document.LoadInserisciMisuraAlternativa.lFlagAffi.value;
	var tipomisuraJS = document.LoadInserisciMisuraAlternativa.tipomisura.value;
	var lEveAmmProvAff = <%=lEventoAmmProvvAff.getIdEvento()%>;
	var flagsanzione = '<%=lFlagSanzione%>';
	var nodesor = document.getElementById('divsor');
	var nodecssa = document.getElementById('divcssa');
	var nodeistituto = document.getElementById('divistituto');
	var nodeautoritaE = document.getElementById('divautoritacompetenteE');
	var nodeautoritaC = document.getElementById('divautoritacompetenteC');
	var nodebottone = document.getElementById('divbottone');
	var nodeavvocati = document.getElementById('divavocati');
   	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
	<%-- document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true; --%>
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
	DisabilitaAvvocato();
<%
if (lPosizione.isMisuraAlternativa() || lPosizione.getCodPosizioneGiuridica().equals("27")) { // L.207/03 indultino
%>
	nodeautoritaE.style.visibility = 'visible';
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
	AbilitaAvvocato();
<%
}
if (tipoMisura.equals("SEMILIBERTA")) {
%>
	document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
	if (affi == "S") {
		nodeistituto.style.visibility = 'visible';
		nodeautoritaE.style.visibility = 'hidden';
		nodeavvocati.style.visibility = 'hidden';
		nodecssa.style.visibility = 'hidden';
		nodesor.style.visibility = 'hidden';
		nodeautoritaC.style.visibility = 'hidden';
		nodeistituto.style.top = '-70px';
		nodebottone.style.top = '-350px';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
		DisabilitaAvvocato();
	} else if (pos == "04") {
		nodeautoritaE.style.visibility = 'visible';
       	nodeavvocati.style.visibility = 'visible';
		nodecssa.style.visibility = 'visible';
		nodesor.style.visibility = 'visible';
		nodeautoritaC.style.visibility = 'hidden';
		nodeistituto.style.visibility = 'hidden';
		nodecssa.style.top = '-35px';
		nodesor.style.top = '-35px';
		nodeavvocati.style.top = '-110px';
		nodebottone.style.top = '-110px';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
       	AbilitaAvvocato();
	} else if (pos == "03") {
		nodeistituto.style.visibility = 'visible';
		nodeautoritaC.style.visibility = 'visible';
		nodecssa.style.visibility = 'visible';
		nodesor.style.visibility = 'visible';
		nodeautoritaE.style.visibility = 'hidden';
		nodeavvocati.style.visibility = 'hidden';
		nodeistituto.style.top = '-70px';
		nodeautoritaC.style.top = '-70px';
		nodesor.style.top = '-70px';
		nodecssa.style.top = '-70px';
		nodebottone.style.top = '-210px';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
       	DisabilitaAvvocato();
	} else {
		nodeautoritaE.style.visibility = 'visible';
		nodeavvocati.style.visibility = 'visible';
		nodeistituto.style.visibility = 'hidden';
		nodecssa.style.visibility = 'hidden';
		nodesor.style.visibility = 'hidden';
		nodeautoritaC.style.visibility = 'hidden';
		<%-- MEV10-s3: modificati valori proprietà top --%>
		nodeavvocati.style.top = '-340px';
		nodebottone.style.top = '-360px';
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
		AbilitaAvvocato();
	}
<%
} // end if concessione semiliberta
else {
%>
	if ((pos == "07") || (pos == "10") || (pos == "16") || (pos == "17") // libero
			|| (pos == "20") || (pos == "46") || (pos == "47") || (pos == "26") // libero
        	|| (pos == "30") || (pos == "29") || (pos == "54") // Detenzione o Affidamento provvisorio
			|| (affi == "S")) {
		if (affi == "S") { // Provengo dal verbale di sottoscrizione
			nodeautoritaE.style.visibility = 'visible';
	        nodecssa.style.visibility = 'visible';
	        nodesor.style.visibility = 'visible';
	        nodeautoritaC.style.visibility = 'hidden';
	        nodeistituto.style.visibility = 'hidden';
	        nodebottone.style.visibility = 'visible';
<%
	if (tipoMisura.equals("DETENZIONE")) {
%>
			nodeavvocati.style.visibility = 'visible';
			nodeavvocati.style.top = '-110px';
			nodebottone.style.top = '-110px';
			AbilitaAvvocato();
<%
	} else {
%>
			nodeavvocati.style.visibility = 'hidden';
			nodebottone.style.top = '-220px';
			DisabilitaAvvocato();
<%
	}
%>
			nodesor.style.top = '-35px';
			nodecssa.style.top = '-35px';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
<%
	if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
<%
	}
%>
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
    		document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
		} else {
<%
	if (tipoMisura.equals("DETENZIONE") && !lPosizione.getCodPosizioneGiuridica().equals("29")) {
%>
			nodecssa.style.visibility = 'hidden';
			nodeautoritaE.style.visibility = 'visible';
			nodeavvocati.style.visibility = 'visible';
			nodesor.style.visibility = 'hidden';
			// nodeavvocati.style.top = '-210px';
			// nodebottone.style.top = '-210px';
			<%-- MEV10-s3: modificati valori proprietà top --%>
			nodeavvocati.style.top = '-330px';
			nodebottone.style.top = '-330px';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
			AbilitaAvvocato();
			document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
<%
	} else if ((tipoMisura.equals("DETENZIONE") && lPosizione.getCodPosizioneGiuridica().equals("29"))
			|| (tipoMisura.equals("AFFIDAMENTO") && lPosizione.getCodPosizioneGiuridica().equals("29"))) { // MEV29 gestione affidamento da posizione 29
%>
			nodeautoritaE.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.visibility = 'visible';
			nodeavvocati.style.visibility = 'hidden';
			DisabilitaAvvocato();
			nodecssa.style.top = '-35px';
			nodesor.style.top = '-35px';
			nodebottone.style.top = '-250px';
			// fix mev29 lo UEPE non passava anche se selezionato in quanto il campo restava disabled
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
<%
	} else if (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
   			// INDULTINO e ESPIAZIONE DA LIBERO o Misura provvisoria
			nodecssa.style.visibility = 'hidden';
			nodesor.style.visibility = 'hidden';
			nodeautoritaE.style.visibility = 'visible';
			nodeavvocati.style.visibility = 'visible';
			<%-- MEV10-s3: modificati valori proprietà top --%>
			nodeavvocati.style.top = '-260px';
			nodebottone.style.top = '-280px';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = true;
			AbilitaAvvocato();
			document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
<%
	} else if (tipoMisura.equals("AFFIDAMENTO")) {
%>
			nodecssa.style.visibility = 'visible';
			nodeautoritaE.style.visibility = 'hidden';
			nodeavvocati.style.visibility = 'hidden';
			nodesor.style.visibility = 'hidden';
			nodecssa.style.top = '-105px';
			if (pos == "07" || pos == "54") <%-- MEV_9-SIEP: aggiunto 54 --%>
				nodebottone.style.top = '-460px';
	      	else
				nodebottone.style.top = '-340px';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
			DisabilitaAvvocato();
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.cssaE.disabled = false;
<%
	}
%>
			nodeautoritaC.style.visibility = 'hidden';
			nodeistituto.style.visibility = 'hidden';
			nodebottone.style.visibility = 'visible';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
		}
     	// MEV10-s3: aggiunto controllo preventivo
     	if (document.LoadInserisciMisuraAlternativa.UDSE)
       		document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
       	document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
	} else {
		<%-- Tutti gli altri casi di posizione giuridica... --%>
		if (pos == "13" && tipomisuraJS == "AFFIDAMENTO" && lEveAmmProvAff != null || pos=="54") {
			nodeautoritaE.style.visibility = 'visible';
			nodecssa.style.visibility = 'visible';
			nodesor.style.visibility = 'visible';
			nodeavvocati.style.visibility = 'hidden';
			DisabilitaAvvocato();
			nodecssa.style.top = '-35px';
			nodesor.style.top = '-35px';
			nodebottone.style.top = '-250px';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
		} else if ((pos == "03") || (flagsanzione == "S" && pos=="19")
				|| (pos == "14" && (tipomisuraJS != "INDULTINO" && tipomisuraJS != "ESP_PRESSO_DOM"))) {
			if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
				nodeistituto.style.visibility = 'visible';
				nodecssa.style.visibility = 'visible';
				nodesor.style.visibility = 'visible';
				nodeautoritaC.style.visibility = 'visible';
				nodebottone.style.visibility = 'visible';
				nodeautoritaE.style.visibility = 'hidden';
				nodeavvocati.style.visibility = 'hidden';
				nodeistituto.style.top = '-70px';
				nodesor.style.top = '-70';
				nodeautoritaC.style.top = '-70';
				nodecssa.style.top = '-70';
     				if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
					nodebottone.style.top = '-300px';
				} else {
					nodebottone.style.top = '-200px';
				}
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
<%
	if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
<%
	}
%>
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
				DisabilitaAvvocato();
				<%-- AMBROSINO - per evitare errore nella form --%>
				if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>')
						&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>')
						&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>')) {
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = "";
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = "";
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = "";
				}
				if (document.LoadInserisciMisuraAlternativa.UDSE)
					document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
				document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
    			} <%-- Chiude if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) --%>
		} else {
			if (document.getElementById('tipo')) {
				if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
					nodeautoritaE.style.visibility = 'visible';
					nodecssa.style.visibility = 'visible';
					nodesor.style.visibility = 'visible';
					nodebottone.style.visibility = 'visible';
					nodeautoritaC.style.visibility = 'hidden';
					nodeistituto.style.visibility = 'hidden';
<%
	if (tipoMisura.equals("DETENZIONE")) {
%>
					nodeavvocati.style.visibility = 'visible';
					nodeavvocati.style.top = '-110px';
					nodebottone.style.top = '-110px';
					AbilitaAvvocato();
<%
	} else {
%>
					nodeavvocati.style.visibility = 'hidden';
					DisabilitaAvvocato();
					nodebottone.style.top = '-220px';
<%
	}
%>
					nodesor.style.top = '-35px';
					nodecssa.style.top = '-35px';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
<%
	if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
<%
	}
%>
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
					<%-- AMBROSINO - per evitare errore nella form --%>
					if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>')
							&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>')
							&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>')) {
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = "";
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = "";
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = "";
					}
					// MEV10-s3: aggiunto controllo preventivo
					if (document.LoadInserisciMisuraAlternativa.UDSE)
       	  				document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
					document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
				} <%-- fine tipo checked 0 --%>
				<%-- MEV10-s3: aggiunto ramo else --%>
				else if (document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
					nodeautoritaE.style.visibility = 'visible';
					nodecssa.style.visibility = 'visible';
					nodesor.style.visibility = 'visible';
					nodebottone.style.visibility = 'visible';
					nodeautoritaC.style.visibility = 'hidden';
					nodeistituto.style.visibility = 'hidden';
<%
	if (tipoMisura.equals("DETENZIONE")) {
%>
					nodeavvocati.style.visibility = 'visible';
					nodeavvocati.style.top = '-110px';
					nodebottone.style.top = '-110px';
					AbilitaAvvocato();
<%
	} else {
%>
					nodeavvocati.style.visibility = 'hidden';
					DisabilitaAvvocato();
					nodebottone.style.top = '-220px';
<%
	}
%>
					nodesor.style.top = '-35px';
					nodecssa.style.top = '-35px';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
<%
	if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
<%
	}
%>
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
					if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>')
							&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>')
							&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>')) {
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = "";
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = "";
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = "";
					}
						if (document.LoadInserisciMisuraAlternativa.UDSE)
 							document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
	  					document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
				} <%-- fine tipo checked 1 --%>
			} // chiude if (document.getElementById('tipo'))
   			else {
<%
	if (tipoMisura.equals("DETENZIONE")) {
%>
				nodeautoritaE.style.visibility = 'visible';
				nodecssa.style.visibility = 'visible';
				nodesor.style.visibility = 'visible';
				nodebottone.style.visibility = 'visible';
				nodeautoritaC.style.visibility = 'hidden';
				nodeistituto.style.visibility = 'hidden';
				nodeavvocati.style.visibility = 'hidden';
				DisabilitaAvvocato();
				nodebottone.style.top = '-220px';
				nodesor.style.top = '-35px';
				nodecssa.style.top = '-35px';
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
				document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
				if (document.LoadInserisciMisuraAlternativa.UDSE)
					document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
				document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
<%
	}
%>
			}
			} <%--- fine ELSE di else if ((pos == "03") ||(flagsanzione == "S" && pos=="19") || (pos == "14" && (tipomisuraJS != "INDULTINO" && tipomisuraJS != "ESP_PRESSO_DOM"))) --%>
		// Paolo Cherubini aggiungo controllo poiche da pos semiliberta se concedo AFFIDAMENTO
		// si sovrappone l'istituto con l'autorità competente
		if (pos == "14" && tipomisuraJS == "AFFIDAMENTO" && lEveAmmProvAff == null
				&& document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
			document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
			nodeistituto.style.visibility = 'hidden';
      			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
			nodeautoritaC.style.visibility = 'hidden';
			nodecssa.style.visibility = 'visible';
			document.LoadInserisciMisuraAlternativa.cssaE.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
			<%-- 20180104: [SG] segnalazione CAMPI SOVRAPPOSTI del 27/12/2017 --%>
			nodesor.style.top = '-30px';
			nodecssa.style.top = '-30px';
			// nodecssa.style.top = '+25px';
			nodebottone.style.top = '-250px';
		} else if (pos != "13" && tipomisuraJS != "AFFIDAMENTO" && lEveAmmProvAff == null) {
			if (typeof (document.LoadInserisciMisuraAlternativa.tipo) != "undefined") {
   				if (document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
		       		nodecssa.style.visibility = 'visible';
		          	nodesor.style.visibility = 'visible';
		       		nodeistituto.style.visibility = 'hidden';
		          	nodeavvocati.style.visibility = 'hidden';
		          	nodebottone.style.visibility = 'visible';
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
<%
	if (tipoMisura.equals("DETENZIONE")) {
%>
					nodeautoritaC.style.visibility = 'hidden';
					nodeautoritaE.style.visibility = 'visible';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
					if (document.LoadInserisciMisuraAlternativa.UDSE)
						document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
					document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
					nodesor.style.top = '-35px';
					nodecssa.style.top = '-35px';
<%
	} else {
		if (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
					nodeautoritaC.style.visibility = 'visible';
					nodeautoritaC.style.top = '-170px';
<%
		} else {
%>
					nodeautoritaC.style.visibility = 'visible';
<%
		}
%>				 
					nodeautoritaE.style.visibility = 'hidden';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
<%
		if (tipoMisura.equals("AFFIDAMENTO")) {
%>
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.cssaE.disabled = false;
					if (document.LoadInserisciMisuraAlternativa.UDSE)
						document.LoadInserisciMisuraAlternativa.UDSE.disabled = true;
					document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = true;
					nodesor.style.top = '-105px';
					nodeautoritaC.style.top = '-105px';
					nodecssa.style.top = '-105px';
<%
		} else if (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
					document.LoadInserisciMisuraAlternativa.cssaE.disabled = true;
					if (document.LoadInserisciMisuraAlternativa.UDSE)
					   	document.LoadInserisciMisuraAlternativa.UDSE.disabled = false;
				  	document.LoadInserisciMisuraAlternativa.notificaSemiliberta.disabled = false;
					<%-- MEV10-s3: modificati valori proprietà top --%>
					// Modifica del 06/11/2015
					// Risolta anomalia sovrapposizione div
					// nodesor.style.top = '-135px'; --> nodesor.style.top = '-0px';
					// nodeautoritaC.style.top = '-135px'; --> nodeautoritaC.style.top = '-250px';
					// nodecssa.style.top = '-0px';
					// 20170905: [SG] modifica risoluzione video
					// casistica: ESP_PRESSO_DOM + document.LoadInserisciMisuraAlternativa.tipo[1].checked (SCARCERATO)
					nodeautoritaC.style.top = '-260px';
					nodecssa.style.top = '-30px';
					nodesor.style.top = '-30px';
<%
		}
	}
%>
					if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length)
 							nodebottone.style.top = '-350px';
				 	else
				  		nodebottone.style.top = '-230px';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled = true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled = false;
   					DisabilitaAvvocato();
				} // chiusura (document.LoadInserisciMisuraAlternativa.tipo[1].checked)
			} // d.f. chiusura (typeof)
		} // chiusura (pos != "13" && tipomisuraJS != "AFFIDAMENTO" && lEveAmmProvAff == null)
		<%-- 20170908: [SG] aggiunta impostazione --%>
		else if (pos == "13"  && tipomisuraJS == "AFFIDAMENTO" && lEveAmmProvAff == null) {
			nodebottone.style.top = '-460px';
		}
<%
	// AMBROSINO - Veniva SedeAut_c e SedeAut_e  Hidden 
	if (tipoMisura.equals("AFFIDAMENTO")) {
%>
		if (pos == "03" && document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
			nodeautoritaC.style.visibility = 'visible';
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled = false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
		} else {
			if (pos == "04" && document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
					nodeautoritaE.style.visibility = 'visible';
  					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled = false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled = false;
			}
		}
<%
	}
%>
	} <%-- chiude la else //Tutti gli altri casi di posizione giuridica... --%>
<%
} // fine else per diverso da semiliberta
%>
}
</script>
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>

<body class="corpo" onload="javascript:radio();">
<table>
	<tr>
   		<td class="LBG">
   			<a href="Javascript:window.print();">
   				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
   			</a>
   		</td>
   		<td class="LBG">
   			<font class="label">Funzione :</font>&nbsp;&nbsp;
<%
MisuraAlternativaModel lModel = new MisuraAlternativaModel();
String lAzione = new String();
String flagMis = new String();
%>
			<%-- MEV_9-SIEP: aggiunta dicitura /Ratifica (x3) --%>
    		<font class="campo">
<%
if (tipoMisura.equals("AFFIDAMENTO")) {
%>
    			Concessione/Ratifica Affidamento In Prova
<%
} else if (tipoMisura.equals("DETENZIONE")) {
%>
   				Concessione/Ratifica Detenzione Domiciliare
<%
}
if (tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
   				Concessione Espiazione Pena presso Domicilio
<%
} else if (tipoMisura.equals("SEMILIBERTA")) {
%>
    			Concessione/Ratifica Semiliberta'
<%
} else if (tipoMisura.equals("INDULTINO")) {
%>
    			Concessione L.207/2003
<%
}
%>
			</font>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciConcessione">
<INPUT type="HIDDEN" name="posizionegiuridica" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
<INPUT type="HIDDEN" name="lFlagAffi" value="<%=lFlagAffi%>">
<INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
<INPUT type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lEventoAmmProvvAff.getIdEvento())%>">
<INPUT type="HIDDEN" name="lFlagSanzione" value="<%=lFlagSanzione%>">
<%-- MEV_9-SIEP: aggiunta impostazione campo nascosto --%>
<INPUT type="HIDDEN" name="tipoOperazione" value="<%=tipoOperazione%>">
<%
if (tipoMisura.equals("AFFIDAMENTO")) {
%>
<input type="HIDDEN" name="tipomisura" value="AFFIDAMENTO">
<%
} else if (tipoMisura.equals("DETENZIONE")) {
%>
<input type="HIDDEN" name="tipomisura" value="DETENZIONE">
<%
} else if (tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
<input type="HIDDEN" name="tipomisura" value="ESP_PRESSO_DOM">
<%
} else if (tipoMisura.equals("SEMILIBERTA")) {
%>
<input type="HIDDEN" name="tipomisura" value="SEMILIBERTA">
<%
} else if (tipoMisura.equals("INDULTINO")) {
%>
<input type="HIDDEN" name="tipomisura" value="INDULTINO">
<%
}
// inizio if per semilibertà
String scarceratodisabilita = null;
String dascarceraredisabilita = null;
String disabilitaData = null;
String scarcerato = null;
String scarcerare = null;
if (misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa() == null) {
%>
<input type="HIDDEN" name="flagmisura" value="N">
<%
	if (!tipoMisura.equals("SEMILIBERTA")) {
		scarcerare ="checked";
	}
} else {
%>
<input type="HIDDEN" name="flagmisura" value="S">
<%
}
if (!tipoMisura.equals("SEMILIBERTA")) {
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
			scarcerare ="checked";
  		}
	}
	if (misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")) {
   		if (misuraalternativa.getFlagUfficioInserimento() == null && misuraalternativa.getDataScarcerazione() != null) {
    		disabilitaData ="readonly";
   		}
	}
} //fine if per semilibertà %>

<input type="HIDDEN" value="<%=idmisuraalternativa%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>">
<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
<input type="HIDDEN" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>">
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l">Posizione Giuridica</td>
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
		<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
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
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02")
		|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
	if (lLuogoDetenzione.getAltroLuogo() != null) {
%>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>
	  	</td>
	</tr>
<%
	}
}
%>
	<tr>
<%
if (penaresidua.getIdPenaResidua() != null && ((penaresidua.getFlagErgastolo() == null)
		|| (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")
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
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
			<font class="l">Euro</font>
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
		<td class="l">Arresto</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
		<td class="l">Ammenda</td>
		<td class="l" colspan=2>
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
			<font class="l">Euro</font>
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
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font>
		</td>
<%
}
if (penaresidua.getFlagErgastolo() != null) {
	if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
	} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
        <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
	}
}
if ((!lPosizione.isLibero())
		|| (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if (!penaresidua.isErgastolo()) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input title="Giorno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>"  <%=IWebConstants.UTIL_DATA%> >
			-
			<input title="Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"  <%=IWebConstants.UTIL_DATA%> >
			-
			<input title="Anno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"  <%=IWebConstants.UTIL_DATA_ANNO%> >
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
			<font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
		</td>
<%
			}
		}
	}
}
%>
	</tr>
<%       
//==============================================================================      
//                          SANZIONE SOSTITUTIVA
//==============================================================================      
if ("S".equals(lFlagSanzione) && penaresidua != null && penaresidua.getFlagSanzioneSostitutiva() != null) {
%>
	<tr>
		<td class="l">Sanzione sostitutiva</td>  
      	<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           	<font class="label">Anni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           	<font class="label">Mesi:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           	<font class="label">Giorni:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
	if ((penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
			|| (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)) {
%>
			<font class="label"> Sanz.Pec.&nbsp;</font>
<%
		if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) {
%>
			<font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
<%
		}
%>
<%
		if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) {
%>
			<font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
<%
		}
	}
%>  
		</td>
    </tr>
<%
}
//==============================================================================      
// FINE SANZIONE SOSTITUTIVA
//==============================================================================      
%>
	<tr>
<%
// MEV_9-SIEP: aggiunta gestione modifica
String giornoDE = DateUtils.getSysDate("dd"), meseDE = DateUtils.getSysDate("MM"), annoDE = DateUtils.getSysDate("yyyy");
String giornoDT = giornoDE, meseDT = meseDE, annoDT = annoDE;
if (!Utils.isNullObj(eventonotifica.getEvento())) {
	if (!Utils.isNullObj(eventonotifica.getEvento().getDataEmissione())) {
		giornoDE = DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd");
		meseDE = DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "MM");
		annoDE = DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "yyyy");
	}
	if (eventonotifica != null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0
			&& eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio() != null) {
		giornoDT = DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "dd");
		meseDT = DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "MM");
		annoDT = DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "yyyy");
	} else if (!Utils.isNullObj(eventonotifica.getEvento().getDataTrasmissioneAtti())) {
		giornoDT = DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "dd");
		meseDT = DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "MM");
		annoDT = DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "yyyy");
	}
}
// FINE MEV_9-SIEP
%>
		<td class="l">Data Emissione</td>
        <td class="L">
			<input title="Giorno Data Emissione" value="<%=giornoDE%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Emissione" value="<%=meseDE%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Emissione" value="<%=annoDE%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L" colspan="2">
			<input title="Giorno Data Trasmissione" value="<%=giornoDT%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Trasmissione" value="<%=meseDT%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Trasmissione" value="<%=annoDT%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
	</tr>
</table>
<table cellspacing="0" cellpadding="0" width="90%">
	<tr>
		<td class="Titolo" colspan="4"> Dati Ordinanza Tribunale di Sorveglianza </td>
   	</tr>
<%
// MEV_9-SIEP: aggiunta gestione modifica
boolean isModifica = "MODIFICA".equals(tipoOperazione);
if (misuraalternativa.getIdMisuraAlternativa() != null && !isModifica) {
%>
	<tr>
		<td class="l" width="25%">Anno/Numero SIUS</td>
	  	<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>/</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
		</td>
		<td class="l">Anno/Numero Ordinanza</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>/</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
	 	</td>
	</tr>
	<tr>
		<td class="l">Ufficio Emittente</td>
	 	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%> DI <%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%></font>
	 	</td>
	</tr>
	<tr>
	  	<td class="l">Oggetto Ordinanza</td>
	  	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>
	  	</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Ordinanza</td>
	  	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd-MM-yyyy"))%></font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">
	  	</td>
	</tr>
<%
	if (tipoMisura.equals("AFFIDAMENTO")) {
%>
	<tr>
	  	<td class="l">Luogo della Prova</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font>	
	  	</td>
	</tr>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x AFFIDAMENTO - SIAMO IN PRESENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>/</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%></font>
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
	 	</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd-MM-yyyy"))%></font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisioneMaAt()))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	} else if (tipoMisura.equals("DETENZIONE")) {
%>
	<tr>
	  	<td class="l" width="20%">Luogo della Detenzione Domiciliare</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font>
	  	</td>
	</tr>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x DETENZIONE - SIAMO IN PRESENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>/</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%></font>
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
	 	</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd-MM-yyyy"))%></font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisioneMaAt()))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	} else if (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
	<tr>
	  	<td class="l" width="20%">Domicilio Imposto</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font>
	  	</td>
	</tr>
<%
	// MEV_9-SIEP: aggiunto ramo else x SEMILIBERTA'
	} else if (tipoMisura.equals("SEMILIBERTA")) {
%>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x SEMILIBERTA' - SIAMO IN PRESENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>/</font>
			<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%></font>
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
	 	</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd-MM-yyyy"))%></font>
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisioneMaAt()))%>">
			<INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisioneMaAt()))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	}
	if (verbale.getDataEmissione() != null) {
%>
	<tr>
		<td class="l" width="20%">Data Sottoscrizione Verbale Obblighi</td>
	   	<td class="l" colspan="3">
	     	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%></font>
	   	</td>
	</tr>
<%
	}
	// MEV_9-SIEP: aggiunto controllo
	if (!((lPosizione.getCodPosizioneGiuridica().equals("13") && lEventoAmmProvvAff.getIdEvento() != null)
			|| lPosizione.getCodPosizioneGiuridica().equals("54"))) {
		if (misuraalternativa.getDataInizioMisura() != null) {
%>
	<tr>
	  	<td class="l" width="20%">Data Inizio Misura</td>
	  	<td class="l" colspan="3">
	    	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd-MM-yyyy"))%></font>
	  	</td>
	</tr>
<%
		}
	}
	if (misuraalternativa.getDataFineMisura() != null) {
%>
	<tr>
	  	<td class="l" width="20%">Data Fine Misura</td>
	  	<td class="l" colspan="3">
	  		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%></font>
	  	</td>
	</tr>
<%
	}
%>
	<tr>
		<td class="l" width="20%">Note</td>
		<td class="L" colspan="3">
		  	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols="80" rows="2"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
		</td>
	</tr>
</table>
<table cellspacing="0" cellpadding="0" width="90%">
<%
} // if (misuraalternativa.getIdMisuraAlternativa() != null)
else { // misuraalternativa.getIdMisuraAlternativa() == null
%>
	<tr>
		<td class="l" colspan="4">
			<a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
	       		Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
	     	</a>
		</td>
	</tr>
	<tr>
	   	<td class="l" width="25%">Anno/Numero SIUS</td>
	   	<td class="l">
	     	<input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>">
			/
			<input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6"
				onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>">
		</td>
		<td class="l">Anno/Numero Ordinanza</td>
		<td class="l">
			<input Title="Anno Ordinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" 
				onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>">
			/
			<input Title="Numero Ordinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6"
				onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>">
		</td>
	</tr>
	<tr>
		<td class="l">Ufficio Emittente</td>
		<td class="l" colspan="3">
<%
	String listaComuniScript = "";	
	if (tipoMisura != null && (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM"))) {
		listaComuniScript = "ListaComuniEmitUTMinor";
%>
			<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff)%>
<%
	} else {
		listaComuniScript = "ListaComuniEmitTdsMinor";
%>
<%if (isModifica) {%>
			<select Title="Ufficio Emittente" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" id="<%=MinorMask.ComboEmittenteId%>">
				<%=comboUfficioEmittenteModif%>
       		</select>
<%} else {%>
			<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
<%}%>
<%
	}
%>      
		</td>
	</tr>
	<tr>
		<td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
		<td class="l" colspan="3">
  			<font class="campo">
  				<input Title="Luogo Ufficio Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>" size=35 type="text"
  				onChange="pulisciId();" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>">
				<a href="Javascript:<%=listaComuniScript%>('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
       				<img src="/images/filefolder.gif" border=0>
      			</a>
   			</font>
  		</td>
  	</tr>
	<tr>
		<td class="l">Oggetto Ordinanza</td>
   		<td class="L" colspan="3">
<%
	if (codicemotivo != null && !codicemotivo.equals("")) {
%>
			<font class="campo"><%=motivoProvv%></font>
	        <INPUT type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codicemotivo%>">
<%
	} else {
%>
        	<select Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          		<%=motivoProvv%>
        	</select>
<%
	}
%>
		</td>
    </tr>
    <tr>
		<td class="l">Data Emissione Ordinanza</td>
		<td class="l" colspan="3">
<%if (isModifica) {%>
			<font class="campo">
				<input title="Giorno Data Emissione Ordinanza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd"))%>"
				type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
				<input title="Mese Data Emissione Ordinanza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "MM"))%>"
				type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
				<input title="Anno Data Emissione Ordinanza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "yyyy"))%>"
				type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
			</font>
<%} else {%>
			<font class="campo">
				<input title="Giorno Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2"
				name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
				<input title="Mese Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2"
				name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
				<input title="Anno Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4"
				name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
			</font>
<%}%>
		</td>
    </tr>

<%
	if (tipoMisura.equals("AFFIDAMENTO")) {
%>
	<tr>
	    <td class="l">Luogo della Prova</td>
	  	<td class="l" colspan="3">
	    	<font class="campo">
	      		<input Title="Luogo svolgimento della prova" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>"
	      		value="<%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>" size="50" type="text" onChange="pulisciId();">
	    	</font>
	  	</td>
	</tr>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x AFFIDAMENTO - SIAMO IN ASSENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<input Title="Anno Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>" type="text" size="4" maxlength="4" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>">
			/
			<input Title="Numero Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>" type="text" size="6" maxlength="6"
				onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%>">
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%if (isModifica) {%><%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%><%}%>">
		</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
	    	<input title="Giorno Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
	    		name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
	    		value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd"))%>"> -
			<input title="Mese Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "MM"))%>"> -
			<input title="Anno Data Emissione Provvedimento" type="text" size="4" maxlength="4" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "yyyy"))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	} else if (tipoMisura.equals("DETENZIONE"))	{
%>
	<tr>
   		<td class="l" width="20%">Luogo della Detenzione Domiciliare</td>
	  	<td class="l" colspan="3">
	    	<font class="campo">
	      		<input Title="Luogo della Detenzione Domiciliare" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="<%=StringUtils.toStringJSP(lMAAmmProvv.getDescrLuogoProva())%>" size="50" type="text" onChange="pulisciId();">
	    	</font>
	  	</td>
	</tr>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x DETENZIONE - SIAMO IN ASSENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<input Title="Anno Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>" type="text" size="4" maxlength="4" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>">
			/
			<input Title="Numero Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>" type="text" size="6" maxlength="6"
				onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%>">
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%if (isModifica) {%><%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%><%}%>">
		</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
	    	<input title="Giorno Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
	    		name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
	    		value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd"))%>"> -
			<input title="Mese Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "MM"))%>"> -
			<input title="Anno Data Emissione Provvedimento" type="text" size="4" maxlength="4" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "yyyy"))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	} else if (tipoMisura.equals("INDULTINO") || tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
	<tr>
	    <td class="l" width="20%">Domicilio Imposto</td>
	  	<td class="l" colspan="3">
	    	<font class="campo">
	      		<input Title="Domicilio Imposto" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" size="50" type="text" onChange="pulisciId();">
	    	</font>
	  	</td>
	</tr>
<%
	// MEV_9-SIEP: aggiunto ramo else x SEMILIBERTA'
	} else if (tipoMisura.equals("SEMILIBERTA")) {
%>
	<%-- MEV_9-SIEP: aggiunte due nuove sezioni x SEMILIBERTA' - SIAMO IN ASSENZA MISURA ALTERNATIVA --%>
	<tr>
		<td class="l" width="20%">Anno/Numero Ordinanza Provvisoria</td>
		<td class="l" colspan="3">
			<input Title="Anno Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>" type="text" size="4" maxlength="4" 
				onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistroMaAt())%>">
			/
			<input Title="Numero Ordinanza Provvisoria" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>" type="text" size="6" maxlength="6"
				onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistroMaAt())%>">
			<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_EVE_ID_EVENTO%>" value="<%if (isModifica) {%><%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%><%}%>">
		</td>
	</tr>
	<tr>
	  	<td class="l">Data Emissione Provvedimento</td>
	  	<td class="l" colspan="3">
	    	<input title="Giorno Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
	    		name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
	    		value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "dd"))%>"> -
			<input title="Mese Data Emissione Provvedimento" type="text" size="2" maxlength="2" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "MM"))%>"> -
			<input title="Anno Data Emissione Provvedimento" type="text" size="4" maxlength="4" 
				name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisioneMaAt(), "yyyy"))%>">
	  	</td>
	</tr>
	<%-- FINE MEV_9-SIEP --%>
<%
	}
%>
	<tr>
	  	<td class="l" width="20%">Note</td>
	  	<td class="L" colspan="3">
	    	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>" cols="80" rows="2"><%=StringUtils.toStringJSP(misuraalternativa.getNote(), "")%></textarea>
	  	</td>
	</tr>
<%
} // AMBROSINO 12-2010 Chiude la else della if (MisAlt != null)
%>
</table>
<table cellspacing="0" cellpadding="0" width="90%">
<%
// AMBROSINO 12-2010
// if (!lPosizione.isLibero() && lFlagAffi.equals("N") && !tipoMisura.equals("SEMILIBERTA"))
// Controllo per posizione giuridica
// SONO STATI USATI GLI STESSI CAMPI DELLA SCARCERAZIONE CAMBIANDO SOLO LE LABEL
// PER QUANTO RIGURDA I VALUE DEL RADION BOTTON SONO RIMASTI GLI STESSI DELLA SCARCERAZIONE
// PER UNIFORMITA' CON TUTTI GLI ALTRI INSERIMENTI DI CONCESSIONE.

// Se Libero nulla 0 tipo misura semiliberta	X
// caso Is detenuto + pos SEML : Data  Da Scarcerare (Da aggiungere)	X
// Caso Arresto Domiciliare :  Data Da Eseguire	X solo x Detenzione Domiciliare
// le 2 eccezione di ammissione provvisoria
// tutti gli altri casi  Data  Da Scarcerare (Da aggiungere)
//==============================================================================
// Sezione con i check:Da eseguire/eseguita o da scarcerare/già scarcerato
//==============================================================================
// MEV_9-SIEP: aggiunta diversificazione
// if (lPosizione.isLibero() || tipoMisura.equals("SEMILIBERTA")) {
if (lPosizione.isLibero() && tipoMisura.equals("AFFIDAMENTO")) {
%>
	<tr>
		<td class="l" width="20%">Data Applicazione Provvisoria</td>
		<td class="l">
<%if (isModifica) {%>
	 		<input title="Giorno Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Applicazione Provvisoria" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%} else {%>
			<input title="Giorno Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Applicazione Provvisoria" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%}%>
		</td>
	</tr>
<%
} else if (lPosizione.getCodPosizioneGiuridica().equals("04")
		// MEV 10 S3 gestione nuove posizioni giuridiche successive
		|| lPosizione.getCodPosizioneGiuridica().equals("82")
		|| lPosizione.getCodPosizioneGiuridica().equals("83")
		|| lPosizione.getCodPosizioneGiuridica().equals("84")
		|| lPosizione.getCodPosizioneGiuridica().equals("85")
		|| lPosizione.getCodPosizioneGiuridica().equals("86")
		|| lPosizione.getCodPosizioneGiuridica().equals("87")
		// && tipoMisura.equals("DETENZIONE")) // Arresti Domiciliari ex art. 656/10
		// Paolo Cherubini 24/03/2011aggiungo le seguenti posizioni giuridiche in or con la misura concessa, 
		// faccio vedere la data da scarcerare solo se la misura concessa e' diversa dalla sua posizione giuridica,
		// poiche' se la misura concessa e' uguale alla sua posizione giuridica significa che e' il II giro dopo il verbale provengo da libero
		// eccezione la posizione giuridica 53 poiche' non viene da misura alternativa quindi non ce' pericolo che sia la II volta
		|| lPosizione.getCodPosizioneGiuridica().equals("12") && !tipoMisura.equals("DETENZIONE") // Espiazione Pena in Regime di Detenzione Domiciliare
		// || lPosizione.getCodPosizioneGiuridica().equals("50") 
      	&& !tipoMisura.equals("ESP_PRESSO_DOM") // Esecuzione presso domicilio della pena detentiva
      	// Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
		|| lPosizione.getCodPosizioneGiuridica().equals("53")) {
	// in questo caso viene visualizzata la dicitura da eseguire o eseguito
	// Paolo Cherubini 24/03/2011 ho anche commentato il controllo sulla misura = DETENZIONE poiche'
	// vogliono sempre vedere la dicitura seguente quando vengo da arresti domiciliari (04)
%>
	<tr>
		<td class="l" width="20%">
			Da eseguire&nbsp;
			<input type="radio" name="tipo" value="scarcerare" checked onclick="radio();">
			&nbsp;Gia' eseguito &nbsp;
			<input type="radio" name="tipo" value="scarcerato" onclick="radio();">
		</td>
		<td class="l">Data Esecuzione&nbsp;
			<input title="Giorno Data Esecuzione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Esecuzione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Esecuzione" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
	</tr>
<%
} else if (lPosizione.isDetenuto()
		|| lPosizione.getCodPosizioneGiuridica().equals("14") // Espiazione Pena in Regime di Semilibertà
		// Espiazione Pena in Regime di Semilibertà in Prosec.Provv. 51 Bis
		|| lPosizione.getCodPosizioneGiuridica().equals("43")) {
		// Paolo Cherubini 24/03/2011 vedi commento sopra	|| lPosizione.getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari ex art. 656/10
		// || lPosizione.getCodPosizioneGiuridica().equals("12") // Espiazione Pena in Regime di Detenzione Domiciliare
		// || lPosizione.getCodPosizioneGiuridica().equals("50") // Esecuzione presso domicilio della pena detentiva
		// || lPosizione.getCodPosizioneGiuridica().equals("53") //  Arresti domiciliari - Esecuzione presso domicilio della pena detentiva
%>
	<tr>
        <td class="l" width="25%">
            Da scarcerare&nbsp;
            <input type="radio" name="tipo" value="scarcerare" checked onclick="radio();">
			&nbsp;Scarcerato&nbsp;
			<input type="radio" name="tipo" value="scarcerato" onclick="radio();">
		</td>
		<td class="l">
    		Data Scarcerazione&nbsp;
    		<input title="Giorno Data Scarcerazione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Scarcerazione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Scarcerazione" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    	</td>
	</tr>
<%
} else if (lPosizione.getCodPosizioneGiuridica().equals("29")) { // Detenzione Domiciliare Provvisoria (29)
%>
	<tr>
		<td class="l" width="20%">Data Ammissione Provvisoria a Detenzione Domiciliare</td>
		<td class="l">&nbsp;
			<input title="Giorno Data Ammissione Provvisoria a Detenzione Domiciliare" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Ammissione Provvisoria a Detenzione Domiciliare" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Ammissione Provvisoria a Detenzione Domiciliare" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
	</tr>
<%
}
// CONTROLLO PER POSIZIONE GIURIDICA "13"  = Espiazione Pena in Regime di Affidamento in Prova
// con Evento validato motivo 2006 e tipo provvedimento 4 o 9 o 12
// CONTROLLO PER POSIZIONE GIURIDICA "54"  = Affidamento in Prova, Provvisorio
else if ((lPosizione.getCodPosizioneGiuridica().equals("13") && lEventoAmmProvvAff.getIdEvento() != null)
		|| lPosizione.getCodPosizioneGiuridica().equals("54")) {
%>
	<tr>
<%
	// MEV_9-SIEP: aggiunta diversificazione
	if (tipoMisura.equals("AFFIDAMENTO")) {
%>
		<td class="l" width="20%">Data Applicazione Provvisoria</td>
		<td class="l">
<%if (isModifica) {%>
	 		<input title="Giorno Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Applicazione Provvisoria" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%} else {%>
			<input title="Giorno Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "dd"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Applicazione Provvisoria" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "MM"))%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Applicazione Provvisoria" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "yyyy"))%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%}%>
		</td>
<%
	} else {
%>
		<td class="l" width="20%">Data Ammissione Provvisoria ad Affidamento in Prova</td>
		<td class="l">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "dd"))%>-
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "MM"))%>-
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "yyyy"))%>
			</font>
			<%-- campi Data nascosti - Commentato perche' il campo "Data Ammissione Provvisoria ad Affidamento in Prova"
				non e' piu' modificabile sono state inserite le label per visulizzare i valori --%>
			<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "dd"))%>">
			<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "MM"))%>">
			<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMAAmmProvv.getDataInizioMisura(), "yyyy"))%>">
		</td>
<%
	}
%>
	</tr>
<%
} else if (lPosizione.isMisuraAlternativa() || lPosizione.getCodPosizioneGiuridica().equals("27")) { // L.207/03 indultino
	// do Nothing
} else { // per le altre posizioni giuridiche
%>
	<tr>
		<td class="l" width="25%">
			Da scarcerare&nbsp;
			<input type="radio" name="tipo" value="scarcerare" checked onclick="radio();">
			&nbsp;Scarcerato&nbsp;
			<input type="radio" name="tipo" value="scarcerato" onclick="radio();">
		</td>
		<td class="l">
		    Data Scarcerazione&nbsp;
		    <input title="Giorno Data Scarcerazione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Mese Data Scarcerazione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%>> -
			<input title="Anno Data Scarcerazione" type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
	    </td>
	</tr>
<%
}
%>
</table>
<%
//==============================================================================
// Magistrato Firmatario  
//==============================================================================
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
    	<td class="Titolo" style="width: 100%;" colspan=6> Magistrato Firmatario </td>
   	</tr>
   	<tr>
		<%-- MEV10-s3: aggiunta proprietà width --%>
     	<td class="l" width="25%">Magistrato Firmatario</td>
     	<td class="L">
			<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"  maxlength="35" size="35">
	       	<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
	       	<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME %>" maxlength="35" size="25">
        	<a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME %>','<%=ICostantiMagistrato.CAMPO_NOME %>');">
	          	<img src="/images/filefolder.gif" border=0>
	        </a>
     	</td>
     	<td>
     	</td>
	</tr>
   	<tr>
		<td class="Titolo" style="width: 100%;" colspan=6> Destinatari</td>
	</tr>
</table>
<%
//==============================================================================
// Autorità di polizia  
//==============================================================================
%>
<div id="divautoritacompetenteE" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
	<tr>
	<!--autorità di polizia-->
<%
if ((lPosizione.isLibero()) && (verbale == null || verbale.getIdVerbale() == null)) {
%>
		<td class="l" width="25%">Destinatario per esecuzione <font class=ob>(*)</font></td>
<%
} else {
%>
		<td class="l" width="25%">Autorita' Competente per territorio <font class=ob>(*)</font></td>
<%
}
%>
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
<%-- MEV_9-SIEP: aggiunta valorizzazione textarea --%>
<%
String indirizzoE = "", indirizzoC = "", noteAvv = "";
if (eventonotifica != null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	for (int i = 0; i < eventonotifica.getNotifiche().length; i++) {
		NotificaModel nm = eventonotifica.getNotifiche()[i];
		if (nm.getAutEstIdAutoritaEsterna() != null && nm.getCodTipoNotifica().equals("E"))
			indirizzoE = nm.getNote();
		else if (nm.getAutEstIdAutoritaEsterna() != null && nm.getCodTipoNotifica().equals("C"))
			indirizzoC = nm.getNote();
		else if (nm.getAutEstIdAutoritaEsterna() != null && nm.getCodTipoNotifica().equals("N"))
			noteAvv = nm.getNote();
	}
}
%>
		<td class="L">
		   	<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>" cols="30"><%=StringUtils.toStringJSP(indirizzoE, "")%></textarea>
		</td>
	</tr>
</table>
</div>
<%
//==============================================================================
// Istituto di Detenzione 
//==============================================================================
%>
<div id="divIstituto" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
	<tr>
     	<td class="l" width="25%">Istituto di Detenzione <font class=ob>(*)</font></td>
  		<td class="l">
<%
if (posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null
		&& posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null) {
%>
			<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
			<input type="hidden" Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
			<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
				<img src="/images/filefolder.gif" border=0>
			</a>
<%
} else {
%>
			<input readonly Title="Istituto" name="Comune" value="" size=50>
			<input type="hidden" Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
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
<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
<div id="divcssa" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="25%">Destinatario <font class=ob>(*)</font></td>
		<td class="l">
<%if (isModifica) {%>
			<select Title="UEPE/USSM" class="small" name="<%=MinorMask.ComboCSSAId%>" id="<%=MinorMask.ComboCSSAId%>">
				<%=comboCSSATrattinoModif%>
       		</select>
<%} else {%>
			<%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%>
<%}%>
		</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l">
			<input readonly title="Sede UEPE Competente" name="Indirizzo" size="60"
					value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>">
			<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>">
			<input type="hidden" name="cssaE" value="S">
				<a href="Javascript:ListaCSSAMinor('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
					<img src="/images/filefolder.gif" border=0>
				</a>
		</td>
	</tr>
</table>
</div>
<%
//==============================================================================
// Uffici di Sorveglianza: Magistrato e Tribunale
//==============================================================================
%>
<div id="divsor" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
 	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="25%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="L">
      		<input type="hidden" name="magSorv" value="S">
       		<input type="hidden" name="notificaMagistrato" value="C">
       		<input title="ufficio" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" maxlength="35" size="25">
       		<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
      	</td>
   	</tr>
<%
if (!tipoMisura.equals("INDULTINO") && !tipoMisura.equals("ESP_PRESSO_DOM")) {
%>
	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="25%">Destinatario <font class=ob>(*)</font></td>
        <td class="L"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
		<td class="L">
			<input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
	       	<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
	       	<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
	       		<img src="/images/filefolder.gif" border=0>
	       	</a>
		</td>
	</tr>
<%
}
%>
</table>
</div>
<%
//==============================================================================
// Autorità Competente per territorio
//==============================================================================
%>
<div id="divautoritacompetenteC" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
	<tr>
		<!--autorità di polizia-->
    	<td class="l" width="25%">Autorita' Competente per territorio <font class=ob>(*)</font></td>
    	<td class="L" colspan="3">
      		<select Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
        		<%=codiceAutoritaC%>
      		</select>
      	<input type="hidden" Title="notificaSemiliberta" name="notificaSemiliberta" value="S">
  	</tr>
  	<tr>
		<td class="l">Sede</td>
		<td class="L">
<%
if (autoritaEsternaC != null && autoritaEsternaC.getDescrSede() != null) {
%>
			<input title="Sede Autorita Esterna" value="<%=autoritaEsternaC.getDescrSede()%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>" maxlength="35" size="35">
<%
} else {
%>
       		<input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>" maxlength="35" size="35">
<%
}
%>
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
			    <img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td class="l">Indirizzo</td>
		<%-- MEV_9-SIEP: aggiunta valorizzazione textarea --%>
		<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>" cols="30"><%=StringUtils.toStringJSP(indirizzoC, "")%></textarea>
		</td>
    </tr>
</table>
</div>
<%
//==============================================================================
// AVVOCATI
//==============================================================================
%>
<div id="divavocati" style="width: 95%; visibility: hidden; position: relative;">
<table style="width: 100%;">
    <tr>
      	<td class="Titolo" colspan=6>Destinatario per Notifica</td>
	</tr>
<%
int lIdxAvv = 0;
Iterator lItxAvv = avvocati.iterator();
while(lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
<!-- <table> -->
	<tr>
   		<td class="l" colspan="4">Per Avvocato&nbsp;
			<font class="campo">
				<%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
			</font>
			&nbsp;Foro di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font>
			&nbsp;Difensore di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></font>
			<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
   		</td>
	</tr>
<!-- </table> -->
<!-- <table> -->
	<tr>
		<td class="l" width="25%">Autorita' Destinazione</td>
		<td class="L" colspan="3">
   			<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
				<%=autoritaEsternaAvv%>
       		</select>
   		</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="L">
			<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
  				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
		<td class="l">Note</td>
		<%-- MEV_9-SIEP: aggiunta valorizzazione textarea --%>
		<td class="L">
   			<textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>" cols="30"><%=StringUtils.toStringJSP(noteAvv, "")%></textarea>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
<%
	lIdxAvv++;
}
%>
	</tr>
<!-- </table> -->
</table>
</div>
<div id="divbottone" style="width: 100%; visibility:visible; position:relative;">
<table style="width: 100%;">
	<tr>
		<td class="lNoBord" colspan="2">
     		<br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    	</td>
	</tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");

// Controlli Data Emissione
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto e' obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>')
		// 20190726 [SG]: aggiunto controllo sbloccante per inserimento evento di Concessione L.207/2003
		&& (<%=!"54".equals(lPosizione.getCodPosizioneGiuridica())%>
		// MEV_9-SIEP: aggiunta casistica per NON inviare msg bloccante
		&& <%=!(lPosizione.isLibero() && tipoMisura.equals("AFFIDAMENTO"))%>)) {
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","req","Il campo Giorno Data Ammissione Provvisoria a Detenzione Domiciliare e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","req","Il campo Mese Data Ammissione Provvisoria a Detenzione Domiciliare e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","req","Il campo Anno Data Ammissione Provvisoria a Detenzione Domiciliare e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","lt=2099");
}

if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>')) {
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");
	
	// Controlli Data Emissione Ordinanza
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");
	
	// Controlli Data Trasmissione
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");

// MEV_9-SIEP: aggiunti controlli
<%
if (tipoMisura.equals("AFFIDAMENTO") || tipoMisura.equals("DETENZIONE") || tipoMisura.equals("SEMILIBERTA")) {
%>
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>","lt=2099");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>","lt=31");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>","lt=12");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>","lt=2099");
<%
}
%>
// FINE MEV_9-SIEP
}

// Controlli Data Fine Pena
if (document.getElementById('<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>')) {
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena e' obbligatorio");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
}

// Controlli Data Scarcerazione
if (document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>')
		&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>')
		&& document.getElementById('<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>')) {
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","lt=2099");
}

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>