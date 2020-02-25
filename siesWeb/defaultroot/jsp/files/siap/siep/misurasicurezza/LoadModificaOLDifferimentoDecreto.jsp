<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per gestione OE per Differimento MS --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>

<jsp:useBean id="dataeditabile"		scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"	scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"		scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="posizione"			scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati" 			scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato" 		scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="autoritaEsternaE" 	scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsterna" 	scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUDS"           scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"         scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="provvSorv"				scope="request" class="siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel"/>
<jsp:useBean id="NotificaAvvocati" 	scope="request" class="java.lang.String" />
<%-- MisuraAlternativa (evento inserito manualmente) --%>
<jsp:useBean id="autorita"			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoprovvedimento"	scope="request" class="java.lang.String"/>
<jsp:useBean id="mp"				scope="request" class="java.lang.String"/>
<jsp:useBean id="et"				scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraAlternativa"	scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="ufficioEmittente"  scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<%
PosizioneGiuridicaModel pgm = posizione.getPosizioneGiuridica();
String codposizione = pgm.getCodPosizioneGiuridica();
MisuraSicurezzaModel msm = null;
// Gestione Provvedimento
String lTitolo = "Modifica Ordine Liberazione per Differimento della Misura Sicurezza";

//Gestione Autorità Esterne sulle Notifiche - ( al MAX 2 Notifiche)
String codTipoAutorita1 = "-";
NotificaModel nm1 = null;
AutoritaEsternaModel aem1 = null;
String note1 = "";
String codTipoAutorita2 = "-";
NotificaModel nm2 = null;
AutoritaEsternaModel aem2 = null;
String note2 = "";
UfficioModel um = null;
String descrTipoUfficio = "";
String descrSedeUfficio = "";
IstitutoDetenzioneModel idm = null;
String idIstDet = null;
if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	for (int i = 0; i < eventonotifica.getNotifiche().length; i++) {
		if (eventonotifica.getNotifiche()[i] != null
				&& ("E").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())
				&& eventonotifica.getNotifiche()[i].getAutoritaEsterna() != null) {
			if (codTipoAutorita1.compareTo("-") == 0) {
				codTipoAutorita1 = eventonotifica.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
	    		nm1 = (NotificaModel) eventonotifica.getNotifiche()[i];
   			aem1 = (AutoritaEsternaModel) nm1.getAutoritaEsterna();
   			if (nm1 != null && nm1.getIdNotifica() != null && nm1.getNote() != null) {
					note1 = nm1.getNote().toString().trim();
				}
			} else {
				codTipoAutorita2 = eventonotifica.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
				nm2 = (NotificaModel) eventonotifica.getNotifiche()[i];
   			aem2 = (AutoritaEsternaModel) nm2.getAutoritaEsterna();
   			if (nm2 != null && nm2.getIdNotifica() != null && nm2.getNote() != null) {
					note2 = nm2.getNote().toString().trim();
				}
			}
		}
		if (eventonotifica.getNotifiche()[i] != null
				&& ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())) {
			um = (UfficioModel) eventonotifica.getNotifiche()[i].getUfficio();
			descrTipoUfficio = um.getDescrTipoUfficio();
			descrSedeUfficio = um.getDescrComune();
		}
		if (eventonotifica.getNotifiche()[i] != null
				&& ("E").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica()))	{
			idm = (IstitutoDetenzioneModel) eventonotifica.getNotifiche()[i].getIstitutoDetenzione();
			idIstDet = eventonotifica.getNotifiche()[i].getIstDetIdIstitutoDetenzione();
		}
	}
}
String descrTipoIstituto = "";
if (idm != null && idm.getIdIstitutoDetenzione() != null) {
	descrTipoIstituto = idm.getDescrTipoIstituto();
	descrTipoIstituto += " di ";
	descrTipoIstituto += idm.getDescrizione();
	if (idm.getIndirizzo() != null && !"".equals(idm.getIndirizzo())) {
		descrTipoIstituto += " - ";
		descrTipoIstituto += idm.getIndirizzo();
	}	
}
%>
<!-- LoadModificaOLDifferimento -->
<html>
<head>
<title>[S.I.E.S.] - Gestione Misure Sicurezza - Richiesta O.L. per Differimento (Esecuzione MS)</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	var desktop;
	var NotificaAvvocati = '<%=NotificaAvvocati%>';
	var note1 = '<%=note1%>';
	var note2 = '<%=note2%>';

	// Lista dei COMUNI
	function ListaComuni(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

	//Funzione utile per impostare la data corrente.
	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna) {
		day = dataOdierna.substring(0,2);
		month = dataOdierna.substring(3,5);
		year = dataOdierna.substring(6,10);
	    document.getElementsByName(campo_giorno).item(0).value = day;
	    document.getElementsByName(campo_mese).item(0).value = month;
	    document.getElementsByName(campo_anno).item(0).value = year;      
	}

	// Blocco combo Aut.Est.
	function bloccaUNEP() {
		<% if (avvocati.size() == 1) { %>
			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
		<% } else { %>
			for (var i=0; i< <%=avvocati.size()%>; i++) { 
				document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
	      	}
     	<% } %>
	}

	// Lista dei MAGISTRATI
	function ListaMagistrati(a_formname) {
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
	}

	// lista Istituti di detenzione
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

    function pulisciIstituto(nomeCampoComune, nomeCampoId) {
        var campoDescr = document.getElementsByName(nomeCampoComune)[0];
        var campoId = document.getElementsByName(nomeCampoId)[0];
        campoDescr.value = "";
        campoId.value = "";
	}

 	// Lista Uffici per Destinatari della Sorveglianza
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function VediNotificaAvvocati() {
    	if (NotificaAvvocati == "-") {
			document.ModificaOEDiffMS.Difesa.checked = false;
    	} else {
    		document.ModificaOEDiffMS.Difesa.checked = true;
    		if (NotificaAvvocati == "C0") {
    			document.ModificaOEDiffMS.SiNoTe.checked = true;
    		} else {
    			document.ModificaOEDiffMS.SiNoTe.checked = false;
    		}
    		Difensore();
    		AutEsterna();
		}
    	if (note1 != "") {
    		document.ModificaOEDiffMS.<%=ICostantiNotifica.CAMPO_NOTE_E%>.value = note1;
    	}
    	if (note2 != "") {
    		document.ModificaOEDiffMS.<%=ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA%>.value = note2;
    	}
    }

	function Difensore() {
		var numavvocati = <%=avvocati.size()%>;
		var nodequan = document.getElementById("divq");
        if (document.ModificaOEDiffMS.Difesa.checked) {
       		nodequan.style.display = 'block';
			if (numavvocati == 1) {
        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
       			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
           		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
           		document.ModificaOEDiffMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = false;
			} else {
    	      	for (var i = 0; i < numavvocati; i++) {
    	      		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled = false;
 					document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
            		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled = false;
            		document.ModificaOEDiffMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled = false;
				}
			} 
		} else {
        	nodequan.style.display = 'none';
			if (numavvocati == 1) {
    			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
			} else {
      			for (var i = 0; i < numavvocati; i++) {
 					document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
				}
   	      	}
		}
	}

	// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
  	function AutEsterna() {
  		var nodeAut = document.getElementById("divae");
        if (document.ModificaOEDiffMS.SiNoTe.checked) {
        	nodeAut.style.display = 'none';
		} else {
        	nodeAut.style.display = 'block';
   	        <% if (avvocati.size() == 1) { %>
     			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
    			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
        		document.ModificaOEDiffMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = false;
       	    <% } else { %>
  	    		for (var i = 0; i < <%=avvocati.size()%>; i++) {
      				document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled = false;
 					document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
         			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled = false;
         			document.ModificaOEDiffMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled = false;
				}
	     	<% } %>
		}
	}

	function Verify() {
		var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

		// CONTROLLO DATA TRASMISSIONE
		if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
	 		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value = '0' +
	 			document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
	 		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value = '0' +
	 			document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;
		var data_to_verify = document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value +
			'/' + document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value +
			'/' + document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
    	if (!ControllaData(data_to_verify)) {
    		alert('Data Trasmissione NON Valida');
    		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
  			return false;
		}

  		// Controllo : data di sistema deve essere >= Data Trasmissione
  		if (!CompareDate(data_to_verify, data_sistema)) {
		    alert('Data Trasmissione non può essere superiore alla data odierna!');
    		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
    		return false;
  		}

  		// CONTROLLO DATA EMISSIONE
		if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
			document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0' +
				document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
			document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0' +
				document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
		var data_to_verify = document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +
			'/' + document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +
			'/' + document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    	if (!ControllaData(data_to_verify)) {
    		alert('Data di emissione non valida');
    		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
  			return false;
		}

  		// Controllo : data di sistema deve essere >= Data Emissione
  		if (!CompareDate(data_to_verify, data_sistema)) {
  			alert('Data Emissione non può essere superiore alla data odierna!');
    		document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
    		 return false;
   		}

  		// PENA RESIDUA
		<%
		if (penaresidua.getFlagErgastolo() == null || (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
			if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
		%>
				if (document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
					document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0'
						+ document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
			  	if (document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
				  	document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'
				  		+ document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
			  	var data_to_verifica = document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+
			  		document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+
			  		document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
	      	  	if (!ControllaData(data_to_verifica)) {
          			alert("Data fine pena non valida");
          			document.ModificaOEDiffMS.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
		 			return false;
				}
		<%
			}
 		}
		%>

		// CONTROLLO notifica su AUTORITA'
		// mev 39: trasferta Torino: 
			//Sulla pagina Inserimento Ordine Liberazione per Differimento della Misura Sicurezza eliminare obbligatoriet sul Destinatario per Esecuzione.
<%-- 		if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
			alert('ATTENZIONE: Inserire almeno un Destinatario per Esecuzione');
       		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			return false;
		} --%>

		// Autorità Esterna
		if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-"
				&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
			alert('Attenzione: Inserire la descrizione SEDE Destinatario per Esecuzione');
    		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
			return false;		
		}
		if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-"
				&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "") {
			alert('Attenzione: Inserire Autorità Destinatario per Esecuzione');
    		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			return false;				
		}
	
		// CONTROLLO Notifica al Difensore
		if (document.ModificaOEDiffMS.Difesa.checked) {
   	        <% if (avvocati.size() == 1) { %>
				if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-"
						&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "") {
					alert("Attenzione: Inserire Dati Destinatario notifica Difensore");
         			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
		 			return false;
				}
				if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex == 1
						&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "") {
					alert("Attenzione: Inserire la Sede Destinatario notifica Difensore");
          			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
		 			return false;
				}
				if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
						&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-") {
					alert("Attenzione: Inserire il tipo di Destinatario notifica Difensore");
		          	document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
				 	return false;
				}
   	   		<% } else { %>
	    	    for (var i = 0; i < <%=avvocati.size()%>; i++) {
					if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-"
							&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "") {
						alert("Attenzione: Inserire Dati Destinatario notifica Difensore " + i);
	          			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
			 			return false;		
					}

					if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex == 1
							&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "") {
						alert("Attenzione: Inserire la Sede Destinatario notifica Difensore " + i);
	          			document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
			 			return false;		
					}

					if (document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
							&& document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-") {
						alert("Attenzione: Inserire il tipo di  Destinatario notifica Difensore " + i);
			          	document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
					 	return false;		
					}
				}
		<%	} %>
		}

		// Per Tipo Autorita' S.N.T. si imposta la sede Autorita' a "-"
		if (document.ModificaOEDiffMS.SiNoTe.checked) {
   	        <% if (avvocati.size() == 1) { %>
				document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 0;
        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value = 'C0';
   	   	<% } else { %>
	    	    for (var i = 0; i < <%=avvocati.size()%>; i++) {
					document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
	        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 0;
	        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value = 'C0';
				}
		<%	} %>
		} else {
			if (document.ModificaOEDiffMS.Difesa.checked) {
       	        <% if (avvocati.size() == 1) { %>
					document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
       	   		<% } else { %>
		    	    for (var i = 0; i < <%=avvocati.size()%>; i++)
		        		document.ModificaOEDiffMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
				<%	} %>
			}
		}

		// Controllo Destinatario Sorveglianza
		if (document.ModificaOEDiffMS.tipoUDS.value != "-") {
			if (document.ModificaOEDiffMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "") {
				alert('Inserire Sede Destinatario Sorveglianza');
				document.ModificaOEDiffMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
				return false;
			}
		}

		<%-- Controllo Estremi Provvedimento della Sorveglianza --%>
<%-- 		if (document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value == "" --%>
// 				|| document.ModificaOEDiffMS.descrComuneUfficio.value == ""
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value == "" --%>
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value == "" --%>
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value == "" --%>
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "" --%>
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "" --%>
<%-- 				|| document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == "") { --%>
// 			alert('Selezionare provvedimento di Sorveglianza dalla lista');
// 			return false;
// 		}

		// CONTROLLO DATA EMISSIONE PROVVEDIMENTO
		if (document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value = '0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
	  	if (document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;
	  	data_to_verify = document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
    	if (!ControllaData(data_to_verify)) {
    		alert('Data Emissione Provvedimento non valida');
    		document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.focus();
		   	return false;
	  	}

    	// Controllo : data di sistema deve essere >= DATA EMISSIONE PROVVEDIMENTO
	    if (!CompareDate(data_to_verify, data_sistema)) {
	      alert("Data Emissione Provvedimento non può essere superiore alla data odierna");
	      document.ModificaOEDiffMS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.focus();
	      return false;
	    }

		// CONTROLLO DATA DIFFERIMENTO
	  	if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = '0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value;
	  	if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value='0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value;
	  	data_to_verify = document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
    	if (!ControllaData(data_to_verify)) {
    		alert('Data Differimento non valida');
    		document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
		   	return false;
	  	}

    	// Controllo : data di sistema deve essere >= DATA DIFFERIMENTO
	    if (!CompareDate(data_to_verify, data_sistema)) {
	      alert("Data Differimento non può essere superiore alla data odierna");
	      document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
	      return false;
	    }

    	// CONTROLLO DATA FINE RINVIO
    	if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = '0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value;
	  	if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value.length == 1)
		  	document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value='0'
		  		+ document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value;
	  	data_to_verify = document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
    	if (data_to_verify != "//" && !ControllaData(data_to_verify)) {
    		alert('Data Fine Rinvio non valida');
    		document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
		   	return false;
	  	}

    	// CONTROLLO DATA FINE RINVIO <=> codEsito = 0035 (in alternativa alla durata)
    	if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == '0035') {
    		if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value == ""
		  			&& document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value == ""
		  			&& document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value == ""
		  			&& document.ModificaOEDiffMS.numAnniSospensione.value == ""
		  			&& document.ModificaOEDiffMS.numMesiSospensione.value == ""
		  			&& document.ModificaOEDiffMS.numGiorniSospensione.value == "") {
	  			alert("Attenzione: per l'esito Concede per un periodo\nvalorizzare uno dei campi Data Fine Rinvio\noppure Durata");
		  		document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
		  		return false;
		  	}
    	}

    	// CONTROLLO CASA o ALTRO LUOGO DI CURA <=> codEsito = 0145
    	if (document.ModificaOEDiffMS.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == '0145') {
	  		if (document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
	  			alert("Attenzione: per l'esito Concede e ordina il ricovero\nin una casa di cura o in altro luogo di cura\nvalorizzare il campo Casa o Altro Luogo di Cura");
		  		document.ModificaOEDiffMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.focus();
		  		return false;
		  	}
    	}

    	// CONTROLLO DATA SCARCERAZIONE
<%
if (!pgm.isLibero()) {
%>
		if (document.ModificaOEDiffMS.GiornoDataScarcerazione.value.length != 0
				&& document.ModificaOEDiffMS.MeseDataScarcerazione.value.length != 0
				&& document.ModificaOEDiffMS.AnnoDataScarcerazione.value.length != 0) {
        	if (document.ModificaOEDiffMS.GiornoDataScarcerazione.value.length == 1)
			  	document.ModificaOEDiffMS.GiornoDataScarcerazione.value = '0'
			  		+ document.ModificaOEDiffMS.GiornoDataScarcerazione.value;
		  	if (document.ModificaOEDiffMS.MeseDataScarcerazione.value.length == 1)
			  	document.ModificaOEDiffMS.MeseDataScarcerazione.value='0'
			  		+ document.ModificaOEDiffMS.MeseDataScarcerazione.value;
		  	data_to_verify = document.ModificaOEDiffMS.GiornoDataScarcerazione.value+'/'+
		  		document.ModificaOEDiffMS.MeseDataScarcerazione.value+'/'+
		  		document.ModificaOEDiffMS.AnnoDataScarcerazione.value;
        	if (!ControllaData(data_to_verify)) {
        		alert("Data Scarcerazione non valida");
        		document.ModificaOEDiffMS.GiornoDataScarcerazione.focus();
			   	return false;
		  	}

        	// Controllo : data di sistema deve essere >= DATA SCARCERAZIONE
		    if (!CompareDate(data_to_verify, data_sistema)) {
		      alert("Data Scarcerazione non può essere superiore alla data odierna");
		      document.ModificaOEDiffMS.GiornoDataScarcerazione.focus();
		      return false;
		    }
		}
<%
}
%>

// controllo obbligatorietà Campi inseriti manualmente
<%
if (provvSorv != null && Utils.isNullObj(provvSorv.getEvento())) {
%>
			if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.value == "-") {
				alert("Autorità Emittente obbligatoria!");
				document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
				return false;
			}
			if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value == "") {
				alert("Sede Autorità Emittente obbligatoria!");
			 	document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
			 	return false;
			}
			if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-") {
				alert("Tipo Provvedimento obbligatorio!");
				document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
				return false;
			}
			if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "-") {
				alert("Oggetto obbligatorio!");
				document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
				return false;
			}
			if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == "-") {
				alert("Esito obbligatorio!");
				document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_ESITO%>.focus();
				return false;
			}
			// GESTIONE OPPURE
			var dataFineRinvio = document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value
				+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value
				+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
			var as = document.LoadInsOLDifferimento.numAnniSospensione.value;
			var ms = document.LoadInsOLDifferimento.numMesiSospensione.value;
			var gs = document.LoadInsOLDifferimento.numGiorniSospensione.value;
			var durata = as + ms + gs;
			if ((dataFineRinvio != "" && durata != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)
					|| (dataFineRinvio != "" && durata != "")
					|| (durata != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)
					|| (dataFineRinvio != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)) {
				alert("Data Fine Rinvio, Durata e Decisione del TDS sono mutuamente esclusivi! Scegliere solo una delle tre opzioni!");
				document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
				return false;
			}
<%
}
%>
	} // Chiude function Verify

    function checkMagSorv(field) {
    	if (field.value != "" && (document.ModificaOEDiffMS.tipoUDS.value == ""
    			|| document.ModificaOEDiffMS.tipoUDS.value == "-")) {
			alert('Specificare il Magistrato di Sorveglianza');
			return false;
    	}
    }
</script>
</head>

<body class="corpo" onload="Javascript:VediNotificaAvvocati();">
	<table style="width: 95%;">
		<tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
				</a>
			</td>
		 	<td class="LBG">
		 		<font class="label">Funzione:</font>&nbsp;&nbsp;&nbsp;
				<font class="campo"><%=lTitolo%></font>
		 	</td>
		</tr>
	</table>
	<br>
	<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
	<br>
	<FORM method="POST" name="ModificaOEDiffMS" action="<%=IWebConstants.PG_MAIN%>">
	 	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaOLDifferimento">
	 	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizione.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
	 	<input type="HIDDEN" title="Id Evento" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
		<table style="width: 95%;">
	  		<tr>
				<td class="l" width="21%">Posizione Giuridica</td>
	    		<td class="L" colspan="7">
		       		<font class="campo">
		       			<%=posizione.getPosizioneGiuridica().getDescrPosizioneGiuridica()%>
		       		</font>
	    		</td>
	   		</tr>
   		<%
if (penaresidua.getIdPenaResidua() != null &&
		(penaresidua.getFlagErgastolo() == null || (penaresidua.getFlagErgastolo() != null
		&& !"S".equals(penaresidua.getFlagErgastolo()) && !"D".equals(penaresidua.getFlagErgastolo())))) {
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
        
   	} else {
%>
			<tr>
				<td class="l" width="21%">Reclusione</td>
				<td class="l">
		            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
		            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
				</td>
				<td class="l">Multa</td>
				<td class="l" colspan="5">
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
					<font class="l">Euro</font>
				</td>
			</tr>
<%
	}
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
		
 	 } else {
%>
			<tr>
				<td class="l" width="21%">Arresto</td>
				<td class="l">
					<font class="l">Anni&nbsp;</font>
					<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
					<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
					<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
				</td>
				<td class="l">Ammenda</td>
				<td class="l" colspan="5">
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
					<font class="l">Euro</font>
				</td>
			</tr>
<%
	}
} // CHIUDO if (penaresidua...)
%>
   			<tr>
<%
if (penaresidua.getDataInizio() != null) {
%>
	       		<td class="l" width="21%">Data Decorrenza Pena</td>
	       		<td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
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
if (penaresidua.getFlagErgastolo() == null ||
		(penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
	if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
				<td class="l">Data Fine Pena</td>
		        <td class="L" colspan="5">
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
	            <td class="L" colspan="5">
	               	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
	               	<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>"  name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
	               	<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>"  name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
	               	<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>"  name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
	            </td>
<%
		} else {
%>
	           	<td class="l">Data Fine Pena</td>
	           	<td class="lRosso" colspan="5">
	           		<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
	           		<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>"  name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
	           		<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>"  name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>">
	           		<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>"  name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>">
	          	</td>
<%
		}
	}
}
%>
			</tr>
			<tr>
				<td>
					<input type="HIDDEN" title="Id Pena Residua" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>">
	   			</td>
			</tr>
	   	</table>
		<!-- Misure di Sicurezza presenti -->
		<table style="width: 95%;">
<%
List lMisure = (List) request.getAttribute("listaMisure");
if (lMisure.size() > 0) {
	Iterator itx = lMisure.iterator();
	while (itx.hasNext()) {
		msm = (MisuraSicurezzaModel) itx.next();
%>
		  	<tr>
			 	<td class=L width="21%">Misura di Sicurezza da espiare</td>
			 	<td class=L><font class="campo"><%=StringUtils.toStringJSP(msm.getDescrTipo())%></font></td>
			 	<td class=C>Anni</td>
			 	<td class=L><font class="campo"><%=StringUtils.toStringJSP(msm.getNumAnni(), "0")%></font></td>
				<td class=C>Mesi</td>
			 	<td class=L><font class="campo"><%=StringUtils.toStringJSP(msm.getNumMesi(), "0")%></font></td>
			 	<td class=C>Giorni</td>
			 	<td class=L><font class="campo"><%=StringUtils.toStringJSP(msm.getNumGiorni(), "0")%></font></td>
		  	</tr>
		  	<tr>
		    	<td>
		 			<input type="HIDDEN" title="Id Misura" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=StringUtils.toStringJSP(msm.getIdMisuraSicurezza())%>">
		 		</td>
		    </tr>
<%
	}
}
%>
		</table>
		<br>
		<!-- Date del Provvedimento -->
		<table style="width: 95%;">
	  		<tr>
				<td class="l" width="21%">Data Emissione<font class="ob">(*)</font></td>
				<td class="L">
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>" 
			 			type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>" 
			 			type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>" 
			 			type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillYear(value)">
		 			<a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
				</td>
	  		</tr>

	  		<tr>					
				<td class="l" width="21%">Data Trasmissione<font class="ob">(*)</font></td>
				<td class="L">
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>" 
			 			type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>"
			 			type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>"
			 			type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
			 			onBlur="javascript:value=FillYear(value)">
		          	<a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
				</td>
	  		</tr>

	  	</table>

		<!-- Dati provvedimento della Sorveglianza -->
		<table style="width: 95%;"> 
			<tr>
				<td class="Titolo">Estremi Provvedimento della Sorveglianza</td>
			</tr>
			<%-- 20190605 [SG]: modificata gestione evento inserito manualmente --%>
			<tr>
	   			<td class="l">
					<input type="hidden" name="idEventoFascSius" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getIdEvento() : eventonotifica.getEvento().getIdEvento())%>">
	    			<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(provvSorv.getFascicoloSiusModel() != null ? provvSorv.getFascicoloSiusModel().getIdFascicoloSius() : "")%>">
<!--      			<a href="Javascript:listaProvvSIUS('ModificaOEDiffMS');"> -->
<!--        				Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border="0"> -->
<!--      			</a> -->
	   			</td>
	   		</tr>
		</table>

		<table style="width: 95%;">
			<tr>
		    	<td class="l" width="21%">Autorita' Emittente<font class="ob">(*)</font></td>
		    	<td class="l">
		    		<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" value="<%=StringUtils.toStringJSP(provvSorv.getFascicoloSiusModel() != null ? provvSorv.getFascicoloSiusModel().getChiaveUfficio() : "")%>">
		    		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getCodUfficioEmittente() : ufficioEmittente.getCodUfficio())%>">
		    		<input Title="Autorita Emittente" name="descrTipoUfficio" value="<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrTipoUfficio()) ? provvSorv.getDescrTipoUfficio() : ufficioEmittente.getDescrTipoUfficio())%>" type="text" size="35" readonly>
		      	</td>
		      	<td class="l">Sede<font class="ob">(*)</font></td>
		      	<td class="l">
		      		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getCodLuogoEmittente() : "")%>">
		        	<input title="Sede Autorita Emittente" type="text" value="<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrComuneUfficio()) ? provvSorv.getDescrComuneUfficio() : ufficioEmittente.getDescrComune())%>" name="descrComuneUfficio" size="35" readonly>
		    	</td>
		    </tr>
		</table>

		<table style="width: 95%;">
	   		<tr>
	       		<td class="l" width="21%">Data Emissione Provvedimento<font class="ob">(*)</font></td>
	       		<td class="L">
	       			<input Title="Giorno Emissione Provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getEvento() != null ? provvSorv.getEvento().getDataEmissione() : misuraAlternativa.getDataDecisione(),"dd"), "")%>"
	       				type="text" size="2" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" readonly> -
	       			<input Title="Mese Emissione Provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getEvento() != null ? provvSorv.getEvento().getDataEmissione() : misuraAlternativa.getDataDecisione(),"MM"), "")%>"
	       				type="text" size="2" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>" readonly> -
	       			<input Title="Anno Emissione Provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getEvento() != null ? provvSorv.getEvento().getDataEmissione() : misuraAlternativa.getDataDecisione(),"yyyy"), "")%>"
	       				type="text" size="4" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>" readonly>
	       		</td>
<%
String afs = "", nfs = "";
if (provvSorv.getFascicoloSiusModel() != null) {
	afs = provvSorv.getFascicoloSiusModel().getChiaveAnno() != null ? provvSorv.getFascicoloSiusModel().getChiaveAnno().toString() : "";
	nfs = provvSorv.getFascicoloSiusModel().getChiaveProgr() != null ? provvSorv.getFascicoloSiusModel().getChiaveProgr().toString() : "";
}
else if (misuraAlternativa != null) {
	afs = misuraAlternativa.getChiaveAnnoFascicoloSius() != null ? misuraAlternativa.getChiaveAnnoFascicoloSius().toString() : "";
	nfs = misuraAlternativa.getChiaveProgrFascicoloSius() != null ? misuraAlternativa.getChiaveProgrFascicoloSius().toString() : "";
}
%>
	       		<td class="l">Anno / Numero Fascicolo SIUS</td>
	   			<td class="L">
	      			<input Title="Anno Fascicolo SIUS" value="<%=afs%>" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>"  type="text" size="4" readonly> /
	      			<input Title="Numero Fascicolo SIUS" value="<%=nfs%>" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>" type="text" size="6" readonly>
	   			</td>
	   		</tr>
	   		<tr>
	   			<td class="l">Tipo Provvedimento<font class="ob">(*)</font></td>
	       		<td class="L">
	       			<input type="hidden" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getCodTipoProvvedimento() : misuraAlternativa.getCodTipoDecisione())%>" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
	       			<input title="Tipo Provvedimento" type="text" value="<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrProvvedimento()) ? provvSorv.getDescrProvvedimento() : misuraAlternativa.getDescrTipoDecisione())%>" name="descrProvvedimento" size="70" readonly>
	       		</td>
<%
String ap = "", np = "";
if (provvSorv.getEvento() != null) {
	ap = provvSorv.getEvento().getAnnoProtocollo() != null ? provvSorv.getEvento().getAnnoProtocollo().toString() : "";
	np = provvSorv.getEvento().getProgrProtocollo() != null ? provvSorv.getEvento().getProgrProtocollo().toString() : "";
}
else if (misuraAlternativa != null) {
	ap = misuraAlternativa.getAnnoRegistro() != null ? misuraAlternativa.getAnnoRegistro().toString() : "";
	np = misuraAlternativa.getNumeroRegistro() != null ? misuraAlternativa.getNumeroRegistro().toString() : "";
}
%>
	   			<td class="l">Anno / Numero Provvedimento</td>
	   			<td class="L">
	      			<input Title="Anno Provvedimento" value="<%=ap%>" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>" type="text" size="4" readonly> /
	      			<input Title="Numero Provvedimento" value="<%=np%>" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" readonly>
	   			</td>        
			</tr>
			<!-- Definizione procedimento -->
			<tr>
				<td class="L">Oggetto<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getCodMotivo() : misuraAlternativa.getCodTipoMisura())%>" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
			   		<input title="Oggetto Procedimento" type="text" value="<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrOggetto()) ? provvSorv.getDescrOggetto() : misuraAlternativa.getDescrTipoMisura())%>" name="descrOggetto" size="120" readonly>
				</td>
			</tr>
			<tr>
				<td class="L">Esito<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" value="<%=StringUtils.toStringJSP(provvSorv.getEvento() != null ? provvSorv.getEvento().getCodEsito() : misuraAlternativa.getCodNaturaDecisione())%>" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>">
					<input title="Esito Provvedimento" type="text" value="<%=StringUtils.toStringJSP(Utils.isPresent(provvSorv.getDescrEsito()) ? provvSorv.getDescrEsito() : misuraAlternativa.getDescrNaturaDecisione())%>" name="descrEsito" size="120" readonly>
				</td>
			</tr>
		</table>

		<table style="width: 95%;">
			<tr>
				<td class="L">Data Fine Rinvio</td>
				<td class="L" nowrap="nowrap">
			 		<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getDecreto() != null ? provvSorv.getDecreto().getDataScadenzaSospensioneSS() : provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataFineMisura() : misuraAlternativa.getDataFineMisura(), "dd"), "")%>"
			 			title="Giorno Data Fine Rinvio" type="text" size="2" readonly> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getDecreto() != null ? provvSorv.getDecreto().getDataScadenzaSospensioneSS() : provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataFineMisura() : misuraAlternativa.getDataFineMisura(), "MM"), "")%>"
						title="Mese Data Fine Rinvio" type="text" size="2" readonly> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getDecreto() != null ? provvSorv.getDecreto().getDataScadenzaSospensioneSS() : provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataFineMisura() : misuraAlternativa.getDataFineMisura(), "yyyy"), "")%>"
						title="Anno Data Fine Rinvio" type="text" size="4" readonly>
			   	</td>
			   	<td class="L">oppure Durata:</td>
				<td class="L" nowrap="nowrap">
			 		&nbsp;Anni&nbsp;<input value="<%=StringUtils.toZerotoStringaVuota(provvSorv.getDecreto() != null ? StringUtils.toStringJSP(provvSorv.getDecreto().getSospensioneAASS()) : StringUtils.toStringJSP(misuraAlternativa.getNumAnniMisura()), "")%>" type="text" size="2" name="numAnniSospensione" readonly> -
					&nbsp;Mesi&nbsp;<input value="<%=StringUtils.toZerotoStringaVuota(provvSorv.getDecreto() != null ? StringUtils.toStringJSP(provvSorv.getDecreto().getSospensioneMMSS()) : StringUtils.toStringJSP(misuraAlternativa.getNumMesiMisura()), "")%>" type="text" size="2" name="numMesiSospensione" readonly> -
					&nbsp;Giorni&nbsp;<input value="<%=StringUtils.toZerotoStringaVuota(provvSorv.getDecreto() != null ? StringUtils.toStringJSP(provvSorv.getDecreto().getSospensioneGGSS()) : StringUtils.toStringJSP(misuraAlternativa.getNumGiorniMisura()), "")%>" type="text" size="4" name="numGiorniSospensione" readonly>
					&nbsp;oppure&nbsp;Fino alla Decisione del TDS&nbsp;
					<input type="checkbox" name="flagDecisioneTribunale" title="Decisione TDS" onclick="this.checked=!this.checked;" value="" readonly>
			   	</td>
			</tr>
			<tr>
				<td class="L">Casa o Altro Luogo di Cura</td>
			   	<td class="L" colspan="3">
			   		<input title="Casa o Altro Luogo di Cura" type="text" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(provvSorv.getDecreto() != null ? provvSorv.getDecreto().getLuogoSvolgimentoProva() : misuraAlternativa.getDescrLuogoProva()))%>"
			   			name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" size="100" readonly>
				</td>
			</tr>

			<tr>
				<td class="L" width="21%">Data Differimento<font class="ob">(*)</font></td>
				<td class="L" colspan="3">
					<input type="hidden" name="idMisuraAlternativa" value="<%=StringUtils.toStringJSP(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getIdMisuraAlternativa() : misuraAlternativa.getIdMisuraAlternativa())%>">
<%-- 		 		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getOrdinanza().getDataInizioPeriodo(),"dd"), "")%>" --%>
<%-- 		 			title="Giorno Data Differimento" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" --%>
<!-- 		 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - -->
<%-- 				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getOrdinanza().getDataInizioPeriodo(),"MM"), "")%>" --%>
<%-- 					title="Mese Data Differimento" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" --%>
<!-- 					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - -->
<%-- 				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getOrdinanza().getDataInizioPeriodo(),"yyyy"), "")%>" --%>
<%-- 					title="Anno Data Differimento" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" --%>
<!-- 					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> -->
<%-- 				<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');"> --%>
<!-- 					<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"> -->
<!-- 		        </a> -->
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataInizioMisura() : misuraAlternativa.getDataInizioMisura(),"dd"), "")%>"
			 			title="Giorno Data Differimento" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>"
			 			onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataInizioMisura() : misuraAlternativa.getDataInizioMisura(),"MM"), "")%>"
						title="Mese Data Differimento" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataInizioMisura() : misuraAlternativa.getDataInizioMisura(),"yyyy"), "")%>"
						title="Anno Data Differimento" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
			   	</td>
			</tr>
			
			<%
if (!pgm.isLibero()) {
	String checkedPROC = "";
	String checkedSORV = "";
	if ("SORV".equals(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getCodTipoUfficioScarcerazione() : misuraAlternativa.getCodTipoUfficioScarcerazione()))
		checkedSORV = "checked='checked'";
	else if ("PROC".equals(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getCodTipoUfficioScarcerazione() : misuraAlternativa.getCodTipoUfficioScarcerazione()))
		checkedPROC = "checked='checked'";
%>
			<tr>
				<td class="l">
					Da Scarcerare&nbsp;<input type="radio" name="isScarcerato" value="daScarcerare" <%=checkedPROC%>>
	    		</td>
	    		<td class="l">
					Scarcerato&nbsp;<input type="radio" name="isScarcerato" value="scarcerato" <%=checkedSORV%>>
	    		</td>
	    		<td class="L" colspan="2">
	    			Data Scarcerazione&nbsp;
			 		<input name="GiornoDataScarcerazione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataScarcerazione() : misuraAlternativa.getDataScarcerazione(),"dd"),"")%>" title="Giorno Data Scarcerazione" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="MeseDataScarcerazione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataScarcerazione() : misuraAlternativa.getDataScarcerazione(),"MM"),"")%>" title="Mese Data Scarcerazione" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="AnnoDataScarcerazione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSorv.getMisuraAlternativa() != null ? provvSorv.getMisuraAlternativa().getDataScarcerazione() : misuraAlternativa.getDataScarcerazione(),"yyyy"),"")%>" title="Anno Data Scarcerazione" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			   	</td>
			</tr>
<%
}
%>

		</table>

		<table style="width: 95%;">
			<tr><td class="Titolo" colspan="4">Magistrato Firmatario</td></tr>
	  		<tr>
				<td class="L" width="21%">Magistrato Firmatario<font class=ob>(*)</font></td>
				<td class="L">
					<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="35">
					<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="35">
		     		<a href="Javascript:ListaMagistrati('ModificaOEDiffMS');">
		       			<img src="/images/filefolder.gif" border=0>
		    		</a>
		    		<input type="hidden" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato())%>" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>">
	    		</td>
	   		</tr>
	   		<!--Notifica per ESECUZIONE - ISTITUTO -->
	   		<tr><td class="Titolo" colspan="4">Destinatari</td></tr>

<!--Eventuale sTRUTTURA DESIGNATA-->
	   <tr>	
		 <td class="L">Struttura Designata</td> 
		<%
		if (strutturaDesignataModel != null && Utils.isPresent(strutturaDesignataModel.getIdIstitutoDetenzione())) {
		%>
				<td class="l" colspan="3">				
			      	<input readonly Title="Struttura" name="Comune" value="<%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrizione())%> - <%=StringUtils.toStringJSP(strutturaDesignataModel.getIndirizzo())%>" size=100>
			      	<input type="hidden" Title="Struttura" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(strutturaDesignataModel.getIdIstitutoDetenzione())%>">
		      		<%-- <a href="Javascript:ListaIstitutoDetenzione('ModificaOEDiffMS','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
		        		<img src="/images/filefolder.gif" border="0">
		      		</a>
		      		<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		  				<img src="/images/delete.gif" border="0">
		  			</a> --%>
			    </td>
		<%
		} else {
		%>
				<td class="l" colspan="3">
			      		<input readonly Title="Struttura" name="ComuneStruttura" value="" size=100>      
			    </td>
		<%
			} 
		%>
	</tr>

			<!-- Notifica per Autorita' per Esecuzione -->
	 		<tr>
				<td class="L">Autorita' per Esecuzione</td>
				<td class="L">
		 			<select Title="Autorita per Esecuzione" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
		  				<%=autoritaEsternaE%>
		 			</select>
				</td>
				<td class="l">Note</td>
				<td class="L">
					<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="40"><%=note1%></textarea>
				</td>	
	 		</tr>
			<tr>
				<td class="l">Sede</td>
				<td class="L" colspan="3">
					<input title="Sede Autorita per Esecuzione" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" value="<%=aem1 != null ? StringUtils.toStringJSP(aem1.getDescrSede()) : ""%>" type="text" size="35">
		 			<a href="Javascript:ListaComuni('ModificaOEDiffMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
		 				<img src="/images/filefolder.gif" border="0">
		 			</a>
				</td>
			</tr>
			<!--Destinatario per l'Ente di Sorveglianza -->
	  		<tr>
				<td class="L">Magistrato di Sorveglianza</td>
	    		<td class="L">
	      			<select Title="Magistrato di Sorveglianza" name="tipoUDS" >
	      				<%=tipoUDS%>
	      			</select>
	   			</td>
	   			<td class="L" colspan="2">
	      			<input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS, "")%>" name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25" onblur="javascript: return checkMagSorv(this);">
	      			<a href="Javascript:ListaUfficiComuni('ModificaOEDiffMS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>',document.ModificaOEDiffMS.tipoUDS[document.ModificaOEDiffMS.tipoUDS.options.selectedIndex].value);">
	         			<img src="/images/filefolder.gif" border="0">
	         		</a>
<%-- 	 			<a href="Javascript:pulisciUDS('tipoUDS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>');"> --%>
<!-- 		 			<img src="/images/delete.gif" border="0"> -->
<!-- 		 		</a>  -->
	    		</td>
	  		</tr> 
		</table>

		<!--ChecK Notifica Immediata e Notifica per Difensore e condannato  -->
		<table style="width: 95%;">
	 		<tr>
<%
if (avvocati.size() > 0) {
%>  
				<td class="l">
					<input type="checkbox" name="Difesa" onclick="Javascript:Difensore();">Notifiche Atti (Difensore - Condannato)
				</td>
<%
} else {
%>
				<td class="l" style="color:red">
					<input type="checkbox" name="Difesa" disabled>Notifiche Atti (Difensore - Condannato) Procedimento privo di Avvocato
				</td>
<%
}
%>
			</tr>
		</table>

		<div id="divq" style="display: none; position: relative;">
			<table style="width: 95%;">
				<tr>
					<td class="Titolo">Destinatario per Notifica</td>
				</tr>
				<tr>
					<td class="l">
						<input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();">Notifica ai sensi dell'art. 148 comma 2 bis c.p.p.
					</td>
				</tr>
			</table>
			<div id="divae" style="display: block; position: relative;">

			<!-- Autorita' ESTERNA per Notifica AVVOCATO Difensore -->
<%
int lIdxAvv = 0;
int IndSede = 0;
Iterator lItxAvv = avvocati.iterator(); 
if (NotificaAvvocati.compareTo("-") == 0) {
	while (lItxAvv.hasNext()) {
		AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
				<table style="width: 95%;">
					<tr>
						<td class="l" width="21%">Autorità Destinazione <font class=ob>(*)</font></td>
						<td class="L" colspan="3">
							<select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();">
								<%=autoritaEsterna%>
							</select></td>
					</tr>
					<tr>
						<td class="l">Sede <font class=ob>(*)</font></td>
						<td class="L">
							<input title="Sede Autorita per Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>"
								type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
		if (avvocati.size() > 1) {
%>   				 
							<a href="Javascript:ListaComuni('ModificaOEDiffMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
		} else {
%>
			   				<a href="Javascript:ListaComuni('ModificaOEDiffMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
		}
%>
						</td>
						<td class="l">Indirizzo</td>
						<td class="L"><textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="30"></textarea></td>
					</tr>
					<tr>
						<td class="l" width="21%">Per Avvocato</td>
							<td class="l" colspan="3">
								<input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
								<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
								<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
								<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></font>
								<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>">
						</td>
					</tr>
				</table>
<%
		lIdxAvv++;
	}
	// MODIFICA : Notifiche al Difensore GIA' PRESENTI
} else {
	for (int ind = 0; ind < eventonotifica.getNotifiche().length; ind++) {
		if (eventonotifica.getNotifiche()[ind].getCodTipoNotifica().equals("ND")) {
%>
				<table style="width: 95%;">
					<tr>
						<td class="l" width="21%">Autorità Destinazione <font class=ob>(*)</font></td>
						<td class="L" colspan="3">
							<select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();">
								<%=autoritaEsterna%>
							</select>
						</td>
					</tr>
					<tr>
						<td class="l">Sede <font class=ob>(*)</font></td>
						<td class="L">
							<input title="Sede Autorita per Avvocato" value="<%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrSede()%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
			if (avvocati.size() > 1) {
%>   				 
							<a href="Javascript:ListaComuni('ModificaOEDiffMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=IndSede%>]');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
			} else {
%>
							<a href="Javascript:ListaComuni('ModificaOEDiffMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
			}
%>				
						</td>
						<td class="l">Indirizzo</td>
						<td class="L">
							<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="30"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getNote())%></textarea>
						</td>
					</tr>
<%
			if (lItxAvv.hasNext()) {
				AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>		
					<tr>
						<td class="l" width="21%">Per Avvocato</td>
						<td>
							<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
							<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
							<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></font>
							<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>">
						</td>
						
					</tr>
<%
			}
%>
				</table>
<%
			IndSede++;
		}
	}		
}
%>							
			</div>
		</div>

		<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
		<table>
			<tr>
				<td class="lNoBord" colspan="2"><br>
					<INPUT class="bottone" type="submit" name="M" value="Conferma" onClick="javascript:return Verify();">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>