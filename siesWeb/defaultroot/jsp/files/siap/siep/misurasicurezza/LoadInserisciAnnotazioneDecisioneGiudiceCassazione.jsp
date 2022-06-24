<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>

<jsp:useBean id="penaresidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="AzioneChiamante" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoprovvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmittente" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="MisuraModel"   		scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>

<% 
Collection contenuto = (Collection) request.getAttribute("contenuto");
Collection esito = (Collection) request.getAttribute("esito");
Collection oggetto = (Collection) request.getAttribute("oggetto");

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

MisuraSicurezzaModel lMis=null;

String strContenuto = "";
Iterator itx = contenuto.iterator();
while(itx.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel) itx.next();
	strContenuto += lDecMod.getCodiceAlternativo() + ";";
	strContenuto += lDecMod.getCode() + ";";
	strContenuto += lDecMod.getDescription() + "#";
}

String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while (itxOggetto.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
	strOggetto += lDecMod.getCodiceAlternativo() +";";
	strOggetto += lDecMod.getCode()+";";
	strOggetto += lDecMod.getDescription()+"#";
}

String strEsito ="";
Iterator itxEs = esito.iterator();
while(itxEs.hasNext()) {
    DecodificheModel lDecMod = (DecodificheModel)itxEs.next();
    strEsito += lDecMod.getCodiceAlternativo() +";";
    strEsito += lDecMod.getFiltro()+";";
    strEsito += lDecMod.getDescription()+"#";
}

// gestione Inserimento Misure Sicurezza
String strOggettoMisura ="";
Iterator itxMis = tipoMisuraSicurezza.iterator();
while (itxMis.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel) itxMis.next();
	strOggettoMisura += lDecMod.getFiltro() + ";";
	strOggettoMisura += lDecMod.getCode() + ";";
	strOggettoMisura += lDecMod.getDescription() + "#";
}
%>
<!-- 	LoadInserisciAnnotazioneDecisioneGiudiceCassazione	 -->
<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Annotazione Decisione - Misure Sicurezza Provvisorie o Fuori sentenza</title>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript">
var desktop;

function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
 
// Funzione utile per impostare la data corrente.
function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna) {    
	day = dataOdierna.substring(0,2);
	month = dataOdierna.substring(3,5);
	year = dataOdierna.substring(6,10);
	document.getElementsByName(campo_giorno).item(0).value = day;
	document.getElementsByName(campo_mese).item(0).value = month;
	document.getElementsByName(campo_anno).item(0).value = year;      
}

var strContenuto = "<%=strContenuto%>";
var strOggetto = "<%=strOggetto%>"
var strEsito = "<%=strEsito%>";
var strOggettoMisura = "<%=strOggettoMisura%>";

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
		if (filtro == 'null' || filtro == aValueText[0]) {
			oItem = new Option;
			oItem.value = aValueText[1];
			oItem.text = aValueText[2];
			selField.options[selField.options.length] = oItem;
		}
    }
    selField.options.selectedIndex = 0;
}

function clearDropDown (selField) {
	while (selField.options.length > 0)
 	selField.options[0] = null;
}

function caricatuttecombo() {
	var nodeRegGen = document.getElementById('divRG');
	// Corte Suprema Cassazione
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "CSS") {
		nodeRegGen.style.display = 'block';
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.disabled = false;
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.disabled = false;
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_TIPO_REG_GEN%>.disabled = false;
	} else {
		nodeRegGen.style.display = 'none';
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.disabled = true;
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.disabled = true;
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_TIPO_REG_GEN%>.disabled = true;
 	}

	// Corte Suprema Cassazione o Tribunale del Riesame
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "CSS"
			|| document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "RIE"
			|| document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "-") {
		caricaCombo(strContenuto,';','#',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value,document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>);
	} else {
		// 'RESTO' = Tutte le altre 10 Autorità Giudicanti (tranne 'CSS' e 'RIE')
		caricaCombo(strContenuto,';','#',"RESTO",document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>);
	}

	caricaCombo(strOggetto,';','#',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value,document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>);
	caricaCombo(strEsito,';','#',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value,document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>);
  	accertaEsito();
}

function caricatuttecomboContenuto() {
	caricaCombo(strOggetto,';','#',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value,document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>);
    caricaCombo(strEsito,';','#',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>.value,document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>);
}
  
function accertaEsito() {
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value != "-") {
		var Descesito = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>.options[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>.options.selectedIndex].text;
		var Codesito = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>.options[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>.options.selectedIndex].value;
		// 0043 / 0189 - Sostituisce la Misura # 0057 - Trasforma la Misura
		if (Codesito=='0403' || Codesito=='0057' || Codesito=='0189') {
			node1=document.getElementById("ChangeMisura");
			node1.style.display='block';
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled=false;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.disabled=false;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.disabled=false;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>.disabled=false;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>.disabled=false;
			node2=document.getElementById("LuogoMisura");
			node2.style.display='block';
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>.disabled=false;
			// 0051 - Esecuzione della Misura # 0053 - Proroga della Misura
   		} else if (Codesito == '0051' || Codesito=='0053') {															
			node1=document.getElementById("ChangeMisura");
			node1.style.display='none';
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>.disabled=true;
			node2=document.getElementById("LuogoMisura");
			node2.style.display='block';
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>.disabled=false;
   		} else {
			node1=document.getElementById("ChangeMisura");
			node1.style.display='none';
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>.disabled=true;
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>.disabled=true;
			node2=document.getElementById("LuogoMisura");
			node2.style.display='none'; 
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>.disabled=true;
   		}
   	} else {
		node1=document.getElementById("ChangeMisura");
		node1.style.display='none';
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled=true;
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.disabled=true;
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.disabled=true;
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>.disabled=true;
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>.disabled=true;
		node2=document.getElementById("LuogoMisura");
		node2.style.display='none'; 
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>.disabled=true;
   	}
}

function Verify() {
	// DATA RICEZIONE ATTI
	if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value.length==1)
		document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value;
	if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value.length==1)
		document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value;
	var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data Ricezione provvedimento non valida');
		document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
		return false;
	}
    // DATA EMISSIONE DELL'ANNOTAZIONE SIEP (obbligatoria)
	if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
		document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
		document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
    var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
    if (!ControllaData(data_to_verify)) {
		alert('Data Annotazione non valida');
		document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		return false;
	}
    // DATA EMISSIONE PROVVEDIMENTO CASSAZIONE O RIESAME (Dereto o Ordinanza)
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
	if (!ControllaData(data_to_verify)) {
		alert('Data emissione provvedimento non valida');
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.focus();
		return false;
	}

	// Anno e Numero RegePM (R.G.N.R.)
	/* --> 11/2015 Tolti dalla form su Richiesta di M.T.
	if (document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length == 0 && 
   		document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length != 0) {
    	alert("Valorizzare Anno RGNR ");
    	document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.focus();
    	return false;
    }
    if (document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length != 0 && 
       		document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length == 0) {
        	alert("Valorizzare Numero RGNR ");
        	document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.focus();
        	return false;
    }
    if (document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length != 0 && 
       		document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value.length != 0) {
        	if (document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value.length < 4 ||
           		document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value < 1900 ||
           		isNaN(document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.value)) {	
               	alert("Anno RGNR NON Valido ");
               	document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>.focus();
               	return false;
            }
            if (isNaN(document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.value)) {	
               	alert("Numero RGNR NON Valido ");
               	document.f.< %=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>.focus();
               	return false;
    	}
    }
	*/

	// Anno/Numero e Tipo REG.GEN.  
	if (!document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.disabled) {	
		if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value.length != 0
				&& document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.value.length == 0) {
			alert('Valorizzare Numero Reg. Gen. ');
			document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.focus();
			return false;
        }
       	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value.length == 0
       			&& document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.value.length != 0) {
			alert('Valorizzare Anno Reg. Gen. ');
			document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.focus();
			return false;       		
		}
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value.length != 0
        		&& document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.value.length != 0) {
			if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value.length < 4
					|| document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value < 1900
					|| isNaN(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.value)) {
               	alert("Anno Reg. Gen. NON Valido ");
               	document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>.focus();
               	return false;
           	}
       		if (isNaN(document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.value) {
               	alert("Numero Reg. Gen. NON Valido ");
               	document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>.focus();
               	return false;
           	}
        }
	}

	// Anno/Numero Provvedimento (NON Obbligatorio)
	if ((document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length != 0)
			&& (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length == 0)) {
		alert("Valorizzare Anno Provvedimento");
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.focus();
        return false;
	}

	if ((document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.value.length == 0)
			&& (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value.length != 0)) {
		alert("Valorizzare Numero Provvedimento");
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>.focus();
		return false;
	}

	// Tipo Provvedimento 
	if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-") {
		alert('Tipo Provvedimento non valido');
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
		return false;       
	}

     // Tipo e Sede Autorità
     if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "-") {
		alert("Tipo Autorità Emittente è obbligatoria");
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
		return false;
	}
    if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>.value == "") {
		alert("Sede Autorità Emittente è obbligatoria");
		document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
		return false;
	}

	// MISURA SICUREZZA  
	if (!document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled) {		   
		if (document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.value == "-") {
			alert('Natura Misura Sicurezza NON Valida');
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.focus();
			return false;
		}
	}    
    if (!document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.disabled) {
		if (document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.value == "-") {
			alert('Tipo Misura Sicurezza NON Valido');
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>.focus();
			return false;
		}
	}
	if (!document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.disabled
			&& document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.value != "-") {
		if (document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.value.length == 0
				&& document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>.value.length == 0
				&& document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>.value.length == 0) {
			alert('Inserire durata Misura Sicurezza ');
			document.f.<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>.focus();
			return false;
   		}
	}
    return true;
} // Chiude Verify()
</script>
</head>
<body class="corpo" onLoad="javascript:caricatuttecombo()">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
        <td class="LBG">
			<font  class="label">Funzione :&nbsp;</font>
         	<font class="campo">Annotazione Provvedimento del Giudice della Cognizione/Riesame/Cassazione/</font>
        </td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciAnnotazioneDecisioneGiudiceCassazione">
<input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
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
		<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font></td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo </td >
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
			di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
}
%>
	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" maxlength="6" size="6">
<%
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02")
				|| lPosizione.getCodPosizioneGiuridica().equals("04"))) {
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
		<td class="l" >Arresto</td>
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
		|| (lFascicoloAssociato.getFlagAltraCausa() != null
		&&  lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S")
			&& !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
		  	<input title = "Giorno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" <%=IWebConstants.UTIL_DATA%>>
			-
			<input title = "Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>" <%=IWebConstants.UTIL_DATA%>>
			-
			<input title = "Anno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
<%
		} else if( penaresidua.getDataFine() != null) {
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
		<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>">
	</tr>
</table>
  
<!-- Misure di Sicurezza già presenti -->
<table>
<%
List lMisure =(List) request.getAttribute("listaMisure");
if (lMisure.size() > 0) {
	Iterator itx1 = lMisure.iterator();
	while (itx1.hasNext()) {
		lMis = (MisuraSicurezzaModel) itx1.next();
%>	
	<tr>
		<td class=C>Misura di Sicurezza da espiare</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		<td class=C>Anni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%>&nbsp;</font></td>
		<td class=C>Mesi</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%>&nbsp;</font></td>
		<td class=C>Giorni</td>
		<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%>&nbsp;</font></td>
	</tr>
<%
	}
}
%>
</table>
<br>
<table style="width: 95%;">
	<tr>
		<td class="l">Data Ricezione Provvedimento <font class="ob">(*)</font></td>
	    <td class="L" >
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
	       	<a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>','<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
	          	<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	       	</a>
		</td>
       	<td class="l">Data Annotazione Decisione<font class="ob">(*)</font></td>
       	<td class="L">
			<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" > &nbsp;
	        <a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
	          	<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	        </a>
       	</td>
	</tr>
</table>
<table style="width: 95%;">
	<tr><td class="Titolo" colspan=4>Dati Provvedimento del Giudice Riesame/Cassazione</td></tr>
<!--   	<tr> -->
<!-- 	   	<td class="l">Anno/Numero R.G.N.R.</td> -->
<!--     	<td class="L"> -->
<%--       		<input Title="Anno R.G.N.R." value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> / --%>
<%--       		<input Title="Numero R.G.N.R." value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> --%>
<!-- 		</td> -->
<!--     </tr> -->
	<tr>
		<td class="l" width="25%">Autorità emittente <font class="ob">(*)</font></td>
    	<td class="l" colspan="3">
			<select Title="Autorità emittente" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>" onchange="javascript:caricatuttecombo()">
        		<%=autoritaEmittente%>
     		</select>
        	Sede&nbsp;<font class="ob">(*)</font>
        	<input title="Sede Autorita Emittente"  type="text" value="" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
        	<a href="Javascript:ListaUfficiComuni('f','<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
          		<img src="/images/filefolder.gif" border=0>
        	</a>
    	</td>
  	</tr>
  	<tr>
		<td class="l" width="25%">Data Emissione Provvedimento<font class="ob">(*)</font></td>
       	<td class="L">
	      	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	       	<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
	        <a href="Javascript:impostaDataOdierna('<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>',
	        										'<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>',
	        										'<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>', 
	        										'<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
				<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	        </a>
		</td>
	</tr>
</table>

<div id="divRG" style="display:none; position:relative;">
<table style="width: 95%;">
	<tr>
       	<td class="l" width="25%" >Anno/Numero Reg.Gen. </td>
    	<td class="L">
      		<input Title="Anno Reg.Gen." value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REG_GEN%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
      		<input Title="Numero Reg.Gen." value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUMERO_REG_GEN%>" maxlength="6" size="6"> &nbsp; 
   			<select name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_TIPO_REG_GEN%>">
				<!-- CORTE SUPREMA CASSAZIONE -->
     			<option value="css">CSS</option>
   			</select>
		</td>
	</tr>
</table>
</div>

<table style="width: 95%;">	
	<tr>
    	<td class="l" width="25%" >
    		Tipo provvedimento <font class="ob">(*)</font>
		    &nbsp;&nbsp;&nbsp;&nbsp;
		    &nbsp;&nbsp;&nbsp;&nbsp;
    	</td>
    	<td class="l">
      		<select Title="Tipo Provvedimento" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
        		<%=tipoprovvedimento%>
      		</select>
  		</td>
    	<td class="l">Anno/Numero Provvedimento</td>
    	<td class="L">
			<input Title="Anno Provv" value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Provv" value="" type="text" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>" maxlength="6" size="6">
		</td>
	</tr>
</table>

<table style="width: 95%;">
	<tr>
	    <td class="l" nowrap>
			Tipologia decisione <font class="ob">(*)</font>
	    </td>
    	<td class="l">
      		<select Title="Contenuto Decisione" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_PROCEDIMENTO%>" onchange="javascript:caricatuttecomboContenuto()"></select>
    	</td>
	</tr>
  	<tr>
    	<td class="l" nowrap>Oggetto decisione <font class="ob">(*)</font></td>
    	<td class="l">
      		<select Title="Oggetto Decisione"  name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>"></select>
    	</td>
  	</tr>
  	<tr>
   		<td class="l" nowrap>Esito Decisione<font class="ob">(*)</font></td>
   		<td class="l">
     		<select Title="Tipologia Decisione" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>" onchange="javascript:accertaEsito()"></select>
   		</td>
	</tr>
  	<tr>
    	<td class="l">Note</td>
    	<td class="l">
      		<textarea cols="80" rows="3" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NOTE%>"></textarea>
		</td>
	</tr>
</table>

<div id="ChangeMisura" style="position: relative; top: 0; left: 0;" >
<table style="width: 95%;">
	<tr><td>&nbsp;</td></tr>
   	<tr>
		<td class="l">Natura Misura</td>
        <td class="l">
			<select title="Natura Misura" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>" onChange="javascript:caricaCombo(strOggettoMisura,';','#',document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_NATURA%>.value, document.f.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>);" >
            	<%=naturaMisuraSicurezza%>
          	</select>
        </td>
	</tr>
    <tr>
		<td class="l">Tipo Misura</td>
        <td class="l">
			<select title="Tipo Misura" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO%>">
				<!-- %=tipoMisuraSicurezza% -->
          	</select>
        </td>
    </tr>
    <tr>
        <td class="l">Durata Misura</td>
        <td class="l">Anni
			&nbsp;<input title="Anni" size=2 maxlength=2 value="" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_NUM_ANNI%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;&nbsp;
			Mesi
			&nbsp;<input title="Mesi" size=2 maxlength=2 value="" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;&nbsp;
			Giorni
			&nbsp;<input title="Giorni" size=2 maxlength=2 value="" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
    </tr>
</table>    
</div>

<div id="LuogoMisura" style="position: relative; top: 0; left: 0;">
<table style="width: 95%;">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<!-- VEDERE SE é IL CASO DI FARE QUESTO CAMPO UN TEXT_AREA COME IN ISCRIZIONEMISURAPROVVISORIA -->
		<td class="l"> Luogo Esecuzione Misura </td>
		<td class="L">
			<input title="Luogo_Esecuzione_Misura" value="<%=StringUtils.toStringJSP(MisuraModel.getLuogoEsecuzioneMisura(), "")%>" name="<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>" type="text" size="90">
<%-- 			<textarea cols="90" rows="2" name="<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA%>"></textarea> --%>
		</td>
	</tr>
</table>			
</div>

<table style="width: 95%;">
  	<tr><td>&nbsp;</td></tr>
  	<tr>
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    	</td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("f");

// Anno provvedimento
frmvalidator.addValidation("<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
frmvalidator.addValidation("<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno provvedimento è di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno provvedimento è di 4 caratteri");

// Numero provvedimento
frmvalidator.addValidation("<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>","numeric");

// Data emissione provvedimento
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Annotazione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31")

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Annotazione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Annotazione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'anno Annotazione è di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'anno Annotazione è di 4 caratteri");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>