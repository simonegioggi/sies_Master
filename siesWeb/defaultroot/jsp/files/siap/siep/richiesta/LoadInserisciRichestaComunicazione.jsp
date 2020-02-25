<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.utente.action.ICostantiUtente"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="posizioneluogoaltra"  	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autorita"             	scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="ufficioge"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiopm"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="richiesta"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"              	scope="request" class="java.util.Vector"/>
<jsp:useBean id="titolo"               	scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" 		scope="session" class="siap.sico.utente.model.UtenteModel" />
<%-- MEV_66: aggiunti useBean per gestione combo tds ed uds ed uepe --%>
<jsp:useBean id="ufficioMdS"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioTdS"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="uepe"		   			scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

if (lPosizione == null)
  	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
  	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
  	lAltraCausa = new AltraCausaModel();
String strOggetto ="";
Iterator itx = oggetto.iterator();
while (itx.hasNext()) {
	DecodificheModel lDecMod = (DecodificheModel)itx.next();
	strOggetto += lDecMod.getFiltro() +";";
	strOggetto += lDecMod.getCode()+";";
	strOggetto += lDecMod.getDescription()+"#";
}
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Generica</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
var strOggetto = "<%=strOggetto%>";

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

    for (var i=0; i < aPairs.length; i++) {
      aValueText = aPairs[i].split(sep1);
      if (filtro=='null' || filtro==aValueText[0]) {
    		oItem = new Option;
    		oItem.value = aValueText[1];
    		oItem.text = aValueText[2];
    		selField.options[selField.options.length] = oItem;
      }
    }

    selField.options.selectedIndex = 0;
    //se il valore del filtro è "-" disabilito il campo
    if (filtro=='-') {
    	selField.disabled=true;
    } else {
    	selField.disabled=false;
    }
}

function clearDropDown (selField) {
  	while (selField.options.length > 0)
  	selField.options[0] = null;
}

function caricatuttecombo() {
  caricaCombo(strOggetto,';','#',document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciRichiestaComunicazione.<%= ICostantiEvento.CAMPO_COD_MOTIVO%>);
}

function Verifica() {
	// CONTROLLO CHE SIA STATO SELEZIONATA LA TIPOLOGIA DELL'ATTO
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-') {
		alert("E' obbligatorio selezionare la tipologia dell'atto");
      	return false;		
	}

<%
if ((!lPosizione.isLibero()) || (((lFascicoloAssociato.getFlagAltraCausa() != null
		&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))
		&& (penaresidua.getDataFinePresunta() != null
		&& penaresidua.getDataFine() == null)))) {
	if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) {
		if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
    if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
		document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
		document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

	var data_to_verifica = document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

	if (!ControllaData(data_to_verifica)) {
		alert('Data fine pena non valida');
		return false;
	}
<%
		}
 	}
}
%>
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
		document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      	document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify)) {
		alert('Data di Trasmissione non valida');
      	return false;
	}

  	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value == ""
  			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value == "") {
    	alert("Il  Magistrato Firmatario è obbligatorio");
    	document.LoadInserisciRichiestaComunicazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
    	return false;
	}

  	<%-- MEV_66: aggiunti controlli per i nuovi destinatari --%>
  	<%-- 			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value == "" --%>
  	<%-- 			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value == "" --%>
	// Destinatari
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>.selectedIndex].value == '-'
			&& document.LoadInserisciRichiestaComunicazione.ufficioTds[document.LoadInserisciRichiestaComunicazione.ufficioTds.selectedIndex].value == '-'
			&& document.LoadInserisciRichiestaComunicazione.ufficioMdS[document.LoadInserisciRichiestaComunicazione.ufficioMdS.selectedIndex].value == '-'
			&& document.LoadInserisciRichiestaComunicazione.uepe[document.LoadInserisciRichiestaComunicazione.uepe.selectedIndex].value == '-'
			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>.selectedIndex].value == '-'
			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == ""
			&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-') {
		alert("Almeno un destinatario deve essere inserito");
		return false;
	}

	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value != '') {
			if (document.LoadInserisciRichiestaComunicazione.ufficioTds[document.LoadInserisciRichiestaComunicazione.ufficioTds.selectedIndex].value == '-') {
	        alert("Il campo Tribunale di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
	        document.LoadInserisciRichiestaComunicazione.ufficioTds.focus();
	        return false;
  		}
	}

	if (document.LoadInserisciRichiestaComunicazione.ufficioTds[document.LoadInserisciRichiestaComunicazione.ufficioTds.selectedIndex].value != '-') {
			if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value == '') {
	        alert("Il campo Sede Tribunale di Sorveglianza e' obbligatorio, se il campo Tribunale di Sorveglianza e' valorizzato!");
	        document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.focus();
	        return false;
  		}
	}

	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value != '') {
			if (document.LoadInserisciRichiestaComunicazione.ufficioMdS[document.LoadInserisciRichiestaComunicazione.ufficioMdS.selectedIndex].value == '-') {
	        alert("Il campo Magistrato di Sorveglianza e' obbligatorio, se la Sede e' valorizzata!");
	        document.LoadInserisciRichiestaComunicazione.ufficioMdS.focus();
	        return false;
  		}
	}

	if (document.LoadInserisciRichiestaComunicazione.ufficioMdS[document.LoadInserisciRichiestaComunicazione.ufficioMdS.selectedIndex].value != '-') {
			if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value == '') {
	        alert("Il campo Sede Magistrato di Sorveglianza e' obbligatorio, se il campo Magistrato di Sorveglianza e' valorizzato!");
	        document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.focus();
	        return false;
  		}
	}

	if (document.LoadInserisciRichiestaComunicazione.Indirizzo.value != '') {
		if (document.LoadInserisciRichiestaComunicazione.uepe[document.LoadInserisciRichiestaComunicazione.uepe.selectedIndex].value == '-') {
    		alert("Il campo UEPE e' obbligatorio, se la Sede e' valorizzata!");
    		document.LoadInserisciRichiestaComunicazione.uepe.focus();
    		return false;
  		}
	}

	if (document.LoadInserisciRichiestaComunicazione.uepe[document.LoadInserisciRichiestaComunicazione.uepe.selectedIndex].value != '-') {
  		if (document.LoadInserisciRichiestaComunicazione.Indirizzo.value == '') {
    		alert("Il campo Sede UEPE e' obbligatorio, se il campo UEPE e' valorizzato!");
    		document.LoadInserisciRichiestaComunicazione.Indirizzo.focus();
    		return false;
  		}
	}

	// controlli per cessazione/prosecuzione misura
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "5440"
			|| document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "5441") {
		if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value != ""
				&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value != "") {
			alert("Scegliere un solo destinatario tra MDS e TDS per la Trasmissione");
			document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.focus();
			return false;
		}
		if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value == ""
				&& document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value == "") {
			alert("Scegliere un destinatario tra MDS e TDS per la Trasmissione");
			document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.focus();
			return false;
		}
	}
} // Chiude if verify

function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}
  
function ListaFunzionari(a_formname, a_field2,a_field3) {
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadRicercaUtenteAttivo&formname="+a_formname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Funzionario", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
}

function ListaUDS(a_formname,a_fieldname) {
	<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
	var codTipoSede = document.LoadInserisciRichiestaComunicazione.ufficioMdS[document.LoadInserisciRichiestaComunicazione.ufficioMdS.selectedIndex].value;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+codTipoSede, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaComuniTds(formname,fieldname) {
	<%-- MEV_66: aggiunto parametro di passaggio = typename --%>
	var codTipoSede = document.LoadInserisciRichiestaComunicazione.ufficioTds[document.LoadInserisciRichiestaComunicazione.ufficioTds.selectedIndex].value;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}

function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
 
// per la prosecuzione/cessazione Misura non servono tutti i destinatari
function mostradestino() {
	var nodeD=document.getElementById('div51');
	if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=="0339") {
		 nodeD.style.display="block";
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
		 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
	 } else {
		 if (document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=="5440"
				 || document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=="5441") {
			 nodeD.style.display="none";
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=true;
			 document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
		}		 
	}	 
}

function visFirmatario(elementoV, elementoH) {
	document.getElementById(elementoV).style.display='block';
	document.getElementById(elementoH).style.display='none';
}

<%-- MEV_66: aggiunta funzione per lista cssa --%>
function ListaCSSA(a_formname,a_fieldname,a_field2) {
	var codTipoSede = document.LoadInserisciRichiestaComunicazione.uepe[document.LoadInserisciRichiestaComunicazione.uepe.selectedIndex].value;
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+codTipoSede, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
</script>
</head>

<body class="corpo" onLoad="javascript:caricatuttecombo()">
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
String lAzione = new String();
%>
    		<font class="campo"><%=titolo%></font>
		</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaComunicazione">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciRichiestaGenerica">
<input type="HIDDEN" title="Id Pena Residua"  value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">

<table>
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
		<td class="l">Detenuto presso</td>
		<td class="L" colspan=5>
			<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
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
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO
	} else {
%>
		<td class="l">Reclusione</td>
		<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
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
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO
	} else {
%>
		<td class="l" >Arresto</td>
      	<td class="l" colspan=2>
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         	<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         	<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
		</td>
      	<td class="l">Ammenda</td>
      	<td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
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
if ((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))) {
	if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
		if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           	-
           	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           	-
           	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
<%
		} else if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">Data Fine Pena</td>
		<td class="L" colspan=2>
			<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
           	<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
			} else {
%>
		<td class="l">Data Fine Pena</td>
		<td class="lRosso" colspan=2>
			<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
			<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
		</td>
<%
			}
		}
	}
}
%>
	</tr>
	<tr>
		<td class="l">Data Emissione</td>
        <td class="L" >
          	<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          	<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          	<input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          	<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
	</tr>
</table>
<table style="width: 90%;">
   <tr>
      <td class="Titolo" colspan='8'> Dati Atto </td>
   </tr>
   <tr>
      <td class="l">Tipologia Atto (*)</td>
        <td class="L" colspan="3">
            <select name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciRichiestaComunicazione.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciRichiestaComunicazione.<%= ICostantiEvento.CAMPO_COD_MOTIVO%>);" Title="Tipologia Atto" >
             <%=richiesta%>
             </select>
        </td>
    </tr>
    <tr>
      <td class="l">Oggetto Atto </td>
        <td class="L" colspan="3">
            <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="Javascript:mostradestino();">
             </select>
        </td>
    </tr>
    <tr>
     <td class="l">Contenuto</td>
           <td  class="L" colspan="3">
             <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ></textarea>
           </td>
    </tr>
 </table>

<table style="width: 100%;">
   <tr>
     <td class="Titolo" style="width: 100%;" colspan=6> Firmatario </td>
   </tr>
	<tr>
   		<td class="l" width="15%">Firmatario</td>
   		<td class="l">
	   		<input checked type="Radio" name="selFirm" value="mag" id="selFirm1" onclick="visFirmatario('nomeMag', 'nomeFunc')"> Magistrato
			<input type="Radio" name="selFirm" value="fun" id="selFirm2" onclick="visFirmatario('nomeFunc', 'nomeMag')"> Funzionario
   		</td>
		<td class="L" width="50%" >
			<div id="nomeMag" onclick="visFirmatario('nomeMag', 'nomeFunc')">
 			<table style="border:0;padding:0">
				<tr>
				<td>
					<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
					<input readonly title="Cognome Magistrato" 
					value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
					type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
					<input readonly title= "Nome Magistrato"    
					value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
					type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
					<a href="Javascript:ListaMagistrati('LoadInserisciRichiestaComunicazione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
						<img src="/images/filefolder.gif" border=0>
					</a>
				</td>
				</tr>
			</table>
			</div>
			<div id="nomeFunc" style="display:none; position:relative;">
			<table  style="border:0;padding:0">
				<tr>
					<td>						
						<input readonly title="Cognome Funzionario" 
							value="<%=StringUtils.toStringJSP(UtenteConnesso.getCognome().toUpperCase())%>" 
							type="text" name="CognomeFunzionario" maxlength="35" size="25">
						<input readonly title= "Nome Funzionario"    
							value="<%=StringUtils.toStringJSP(UtenteConnesso.getNome().toUpperCase())%>" 
							type="text" name="NomeFunzionario" maxlength="35" size="25">
						<a href="Javascript:ListaFunzionari('LoadInserisciRichiestaComunicazione','CognomeFunzionario','NomeFunzionario');">
							<img src="/images/filefolder.gif" border=0>
						</a>					
					</td>
				</tr>
			</table>
			</div>					
		</td>
  	</tr>
 	<tr>
		<td class="Titolo" style="width: 100%;" colspan="6">Destinatari</td>
	</tr>
</table>

<%-- MEV_66: aggiunte combo per MS e TS e GE e UEPE --%>
<table style="width: 100%;">
	<tr>
	  	<td class="l" width="20%">Magistrato di Sorveglianza</td>
	    <td class="L">
	    	<select title="Magistrato di Sorveglianza" name="ufficioMdS"><%=ufficioMdS%></select>
	    </td>
	    <td class="l">Sede</td>
	    <td class="l">
			<input title="Sede Magistrato di Sorveglianza" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="35">
	       	<a href="Javascript:ListaUDS('LoadInserisciRichiestaComunicazione','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
	        	<img src="/images/filefolder.gif" border="0">
			</a>
		</td>
	</tr>
	<tr>
		<td class="L">Tribunale di Sorveglianza</td>
		<td class="L">
			<select Title="Tribunale di Sorveglianza" name="ufficioTds"><%=ufficioTdS%></select>
		</td>
   		<td class="l">Sede</td>
		<td class="l">
			<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS%>" maxlength="35" size="35">
			<a href="Javascript:ListaComuniTds('LoadInserisciRichiestaComunicazione','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
				<img src="/images/filefolder.gif" border="0">
			</a>
		</td>
	</tr>
	<tr>
   		<td class="l">UEPE/USSM</td>
   		<td class="l">
   			<select title="UEPE/USSM" name="uepe"><%=uepe%></select>
   		</td>
   		<td class="l">Sede</td>
   		<td class="l">
       		<input readonly title="UEPE Competente" name="Indirizzo" value="" maxlength="35" size="35">
       		<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="">
       		<a href="Javascript:ListaCSSA('LoadInserisciRichiestaComunicazione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
       			<img src="/images/filefolder.gif" border="0">
       		</a>
   		</td>
   	</tr>
</table>
	
<div id="div51" style="display: block; position: relative;">
<table style="width: 100%;">
	<tr>
		<td class="l" width="20%">Ufficio Giudice dell'Esecuzione</td>
		<td class="L">
			<select title="Ufficio Giudice Esecuzione" name="<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>">
				<%=ufficioge%>
            </select>
		</td>
		<td class="l">Sede</td>
        <td class="L">
			<input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>" maxlength="35" size="35">
          	<a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaComunicazione','<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>',document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>.options.selectedIndex].value);">
            	<img src="/images/filefolder.gif" border="0">
          	</a>
		</td>
  	</tr>
  	<tr>
		<td class="l">Ufficio Pubblico Ministero</td>
		<td class="L" colspan="3">
			<select title="Ufficio Pubblico Ministero" name="<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>">
				<%=ufficiopm%>
            </select>
		</td>
 	</tr>
  	<tr>
		<td class="l">Sede</td>
		<td class="L" colspan="3">
			<input title="Sede Ufficio Pubblico Ministero"  type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM%>" maxlength="35" size="35">
           	<a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaComunicazione','<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_PM%>',document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_PM%>.options.selectedIndex].value);">
            	<img src="/images/filefolder.gif" border="0">
           	</a>
		</td>
  	</tr>
  	<tr>
 		<td class="l">Istituto di Detenzione</td>
<%
if (posizioneluogoaltra != null
		&& posizioneluogoaltra.getAltraCausa() != null
		&& posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null) {
%>
              <td class="l" colspan="3">
              <input readonly title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden" title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRichiestaComunicazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border="0"></a></td>
<%
}else
{%>
              <td class="l" colspan="3">
              <input readonly title="Istituto" name="Comune" value="" size=50>
              <input type="hidden" title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRichiestaComunicazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>
<%
}%>

 </tr>
 
  <tr>
<!--autorità di polizia-->
        <td class="l">Altro Destinatario</td>
        <td class="L" colspan="3">
            <select  Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
             	<%=autorita%>
             </select>
		</td>	
  </tr>
    
    		<tr>
      			<td class="l">Sede</td>
     			<td class="L">
          			<input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
          			<a href="Javascript:ListaComuni('LoadInserisciRichiestaComunicazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          			<img src="/images/filefolder.gif" border=0>
        			</a>
      			</td>
           		<td class="l">Indirizzo</td>
           		<td class="L">
              		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30 ></textarea>
            	</td>
    		</tr>
</table>    		
</div>

<table>
	<tr>
	<td class="lNoBord" colspan="2">
	      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
	    </td>
	</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaComunicazione");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
<%
if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)

 {%>
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
  }
 }
}
%>
</script>
</body>
</html>