<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina per gestione OE per Differimento MS --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="dataeditabile"			scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"		scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="posizioneluogoaltra"	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"				scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistrato"			scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"		scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUDS"				scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"				scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="autorita"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoprovvedimento"		scope="request" class="java.lang.String"/>
<jsp:useBean id="mp"					scope="request" class="java.lang.String"/>
<jsp:useBean id="et"					scope="request" class="java.lang.String"/>
<jsp:useBean id="provvedimento"			scope="request" class="siap.sius.misurasicurezza.model.ProvvedimentoEventoTenoreFascicoloSiusModel"/>
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="decreto"				  scope="request" class="siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel"/>

<%
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
PosizioneGiuridicaModel lPosMod = posizioneluogoaltra.getPosizioneGiuridica();
String codposizione = lPosMod.getCodPosizioneGiuridica();
MisuraSicurezzaModel lMis = null;
%>
<!-- LoadInserisciOLDifferimento -->
<html>
<head>
   	<title>[S.I.E.S.] - Gestione Misure sicurezza - Richiesta O.L. per Differimento (Esecuzione MS)</title>
   	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
   	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
		var desktop;

    	// Lista dei COMUNI
    	function ListaComuni(a_formname,a_fieldname) {
            desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }

    	//Funzione utile per impostare la data corrente.
    	function impostaDataOdierna(campo_giorno, campo_mese, campo_anno, dataOdierna) {
    		day = dataOdierna.substring(0,2);
    		month = dataOdierna.substring(3,5);
    		year = dataOdierna.substring(6,10);
    	    document.getElementsByName(campo_giorno).item(0).value = day;
    	    document.getElementsByName(campo_mese).item(0).value = month;
    	    document.getElementsByName(campo_anno).item(0).value = year;      
    	}

    	// Blocco combo Aut.Est.
    	function bloccaUNEP() {
   	      <% if (avvocati.size() == 1) {%>
				document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
    	      <% } else { %>
    	      for (var i = 0; i < <%=avvocati.size()%>; i++){ 
  				document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
    	      }
   	     <% } %>
    	}

    	// Lista dei MAGISTRATI
    	function ListaMagistrati(a_formname) {
    	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    	}

    	// Lista degli ISTITUTI DI DETENZIONE
    	function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
        	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      	}

        function pulisciIstituto (nomeCampoComune, nomeCampoId) {
            var campoDescr = document.getElementsByName(nomeCampoComune)[0];
            var campoId = document.getElementsByName(nomeCampoId)[0];
            campoDescr.value = "";
            campoId.value = "";
    	}

    	// Lista Uffici per Destinatari della Sorveglianza
        function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio) {
            desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }

    	function Difensore() {
    		var nodequan = document.getElementById("divq");
            if (document.LoadInsOLDifferimento.Difesa.checked) {
           		nodequan.style.display='block';
       	        <% if (avvocati.size() == 1) {%>
            		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
           			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
               		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
               		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = false;
          	    <% } else { %>
        	      	for (var i = 0; i < <%=avvocati.size()%>; i++) {
	            		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled = false;
	    				document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
	               		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled = false;
	               		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled = false;
					}
   	   	     <% } %>
       	  	} else {
            	nodequan.style.display='none';
       	        <% if (avvocati.size() == 1) {%>
	        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
	        		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.value = "";
        			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
            		//document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = true;
          	    <% } else { %>
   	      			for (var i = 0; i < <%=avvocati.size()%>; i++) {
		        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "";
		        		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value = "";
	    				document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
	            		//document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled = true;
					}
   	   	     <% } %>
            }
        }

		// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
    	function AutEsterna() {
			var ind = 0;
    		var nodeAut = document.getElementById("divae");
            if (document.LoadInsOLDifferimento.SiNoTe.checked) {
           		nodeAut.style.display = 'none';
       	        <% if (avvocati.size() == 1) {%>
					document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	           		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.value = '';
	           		//document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = true;
          	    <% } else { %>
		    	    	for (var i = 0; i < <%=avvocati.size()%>; i++) { 
							document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		           			//document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value = '';
						}
   	   	     	<% } %>
       	  	} else {
            	nodeAut.style.display = 'block';
       	        <% if (avvocati.size() == 1) {%>
	        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
	        		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.value = "";
	        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled = false;
        			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
            		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled = false;
            		document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled = false;
           	    <% } else { %>
		    	    	for (var i = 0; i < <%=avvocati.size()%>; i++) { 
		        			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "";
		        			document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value = "";
		        			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled = false;
	    					document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
	            			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled = false;
	            			document.LoadInsOLDifferimento.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled = false;
						}
	   	     	<% } %>
            }	
        }

		function Verify() {
  			var data_sistema = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

  			// CONTROLLO DATA TRASMISSIONE
		  	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value = '0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		  	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;
		  	var data_to_verify = document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;
        	if (!ControllaData(data_to_verify)) {
        		alert("Data Trasmissione non valida");
        		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
			   	return false;
		  	}

		    // Controllo : data di sistema deve essere >= Data Trasmissione
		    if (!CompareDate(data_to_verify, data_sistema)) {
		      alert("Data Trasmissione non può essere superiore alla data odierna");
		      document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
		      return false;
		    }

		    // CONTROLLO DATA EMISSIONE
		  	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
		  	data_to_verify = document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
        	if (!ControllaData(data_to_verify)) {
        		alert("Data di emissione non valida");
        		document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}

		    // Controllo : data di sistema deve essere >= Data Emissione
		    if (!CompareDate(data_to_verify, data_sistema)) {
		      alert("Data Emissione non può essere superiore alla data odierna");
		      document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

		    // PENA RESIDUA
			<%
			if (penaresidua.getFlagErgastolo() == null || (penaresidua.getFlagErgastolo() != null
					&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
				if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
			%>
					if (document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length == 1)
						document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value = '0'
							+ document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
				  	if (document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length == 1)
					  	document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'
					  		+ document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
				  	var data_to_verifica = document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+
				  		document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+
				  		document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		      	  	if (!ControllaData(data_to_verifica)) {
	          			alert("Data fine pena non valida");
	          			document.LoadInsOLDifferimento.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
			 			return false;
					}
			<%
				}
	 		}
			%>

			// CONTROLLO notifica all'AUTORITA'xx
			// mev 39: trasferta Torino: 
			//Sulla pagina Inserimento Ordine Liberazione per Differimento della Misura Sicurezza eliminare obbligatoriet sul Destinatario per Esecuzione.
			
<%-- 			if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
					 {
				alert("Attenzione: Inserire almeno un Destinatario per Esecuzione");
       			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
	 			return false;
			} --%>

			// Autorità Esterna
			if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-"
					&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
				alert("Attenzione: Inserire la descrizione SEDE Destinatario per Esecuzione");
	       		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
		 		return false;
			}
			if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-"
					&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "") {
				alert("Attenzione: Inserire Autorità Destinatario per Esecuzione");
	       		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
		 		return false;
			}

			// CONTROLLO Notifica al Difensore
			if (document.LoadInsOLDifferimento.Difesa.checked) {
       	        <% if (avvocati.size() == 1) { %>
					if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-"
							&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "") {
						alert("Attenzione: Inserire Dati Destinatario notifica Difensore");
	         			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
			 			return false;
					}
					if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex == 1
							&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "") {
						alert("Attenzione: Inserire la Sede Destinatario notifica Difensore");
	          			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
			 			return false;
					}
					if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
							&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-") {
						alert("Attenzione: Inserire il tipo di Destinatario notifica Difensore");
			          	document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
					 	return false;
					}
       	   		<% } else { %>
		    	    for (var i = 0; i < <%=avvocati.size()%>; i++) {
						if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-"
								&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "") {
							alert("Attenzione: Inserire Dati Destinatario notifica Difensore " + i);
		          			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
				 			return false;		
						}
	
						if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex == 1
								&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "") {
							alert("Attenzione: Inserire la Sede Destinatario notifica Difensore " + i);
		          			document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
				 			return false;		
						}
	
						if (document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
								&& document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-") {
							alert("Attenzione: Inserire il tipo di  Destinatario notifica Difensore " + i);
				          	document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
						 	return false;		
						}
					}
			<%	} %>
			}

			// Per Tipo Autorita' S.N.T. si imposta la sede Autorita' a "-"
			if (document.LoadInsOLDifferimento.SiNoTe.checked) {
	   	        <% if (avvocati.size() == 1) { %>
					document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 0;
	        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value = 'C0';
	   	   	<% } else { %>
		    	    for (var i = 0; i < <%=avvocati.size()%>; i++) {
						document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 0;
		        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value = 'C0';
					}
			<%	} %>
			} else {
				if (document.LoadInsOLDifferimento.Difesa.checked) {
	       	        <% if (avvocati.size() == 1) { %>
						document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex = 1;
	       	   		<% } else { %>
			    	    for (var i = 0; i < <%=avvocati.size()%>; i++)
			        		document.LoadInsOLDifferimento.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex = 1;
					<%	} %>
				}
			}

			<%-- Controllo Destinatario Sorveglianza --%>
			if (document.LoadInsOLDifferimento.tipoUDS.value != "-") {
				if (document.LoadInsOLDifferimento.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "") {
					alert("Inserire Sede Destinatario Sorveglianza");
					document.LoadInsOLDifferimento.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
					return false;
				}
			}

			<%-- Controllo Estremi Provvedimento della Sorveglianza --%>
<%-- 			if (document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value == "" --%>
// 					|| document.LoadInsOLDifferimento.descrComuneUfficio.value == ""
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value == "" --%>
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value == "" --%>
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value == "" --%>
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "" --%>
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value == "" --%>
<%-- 					|| document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == "") { --%>
// 				alert("Selezionare provvedimento di Sorveglianza dalla lista");
// 				return false;
// 			}

			// CONTROLLO DATA EMISSIONE PROVVEDIMENTO
			if (document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value = '0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
		  	if (document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;
		  	data_to_verify = document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
        	if (!ControllaData(data_to_verify)) {
        		alert("Data Emissione Provvedimento non valida");
        		document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.focus();
			   	return false;
		  	}

        	// Controllo : data di sistema deve essere >= DATA EMISSIONE PROVVEDIMENTO
		    if (!CompareDate(data_to_verify, data_sistema)) {
		      alert("Data Emissione Provvedimento non può essere superiore alla data odierna");
		      document.LoadInsOLDifferimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.focus();
		      return false;
		    }

        	// pulisco i campi data diff o data scarc
//         	if (document.LoadInsOLDifferimento.eseguita[0].checked == true) {
<%--         		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = ""; --%>
<%-- 	    		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = ""; --%>
<%-- 	    		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = ""; --%>
// 	    	} else {
// 	    		document.LoadInsOLDifferimento.GiornoDataScarcerazione.value = "";
// 	    		document.LoadInsOLDifferimento.MeseDataScarcerazione.value = "";
// 	    		document.LoadInsOLDifferimento.AnnoDataScarcerazione.value = "";
// 	    	}

			// CONTROLLO DATA DIFFERIMENTO
// 			if (document.LoadInsOLDifferimento.eseguita[1].checked == true) {
		  	if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = '0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value;
		  	if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value='0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value;
		  	data_to_verify = document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
        	if (!ControllaData(data_to_verify)) {
        		alert("Data Differimento non valida");
        		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
			   	return false;
		  	}
// 			}

        	// Controllo : data di sistema deve essere >= DATA DIFFERIMENTO
<%-- 		    if (!CompareDate(data_to_verify, data_sistema)) {
		      alert("Data Differimento non può essere superiore alla data odierna");
		      document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
		      return false;
		    } --%>

		 	// CONTROLLO DATA FINE RINVIO
		    if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = '0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value;
		  	if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value.length == 1)
			  	document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value='0'
			  		+ document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value;
		  	data_to_verify = document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+
		  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
        	if (data_to_verify != "//" && !ControllaData(data_to_verify)) {
        		alert("Data Fine Rinvio non valida");
        		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
			   	return false;
		  	}

        	// CONTROLLO DATA FINE RINVIO <=> codEsito = 0035 (in alternativa alla durata)
        	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == '0035') {
        		if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value == ""
			  			&& document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value == ""
			  			&& document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value == ""
			  			&& document.LoadInsOLDifferimento.numAnniSospensione.value == ""
			  			&& document.LoadInsOLDifferimento.numMesiSospensione.value == ""
			  			&& document.LoadInsOLDifferimento.numGiorniSospensione.value == "") {
		  			alert("Attenzione: per l'esito Concede per un periodo\nvalorizzare uno dei campi Data Fine Rinvio\noppure Durata");
			  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
			  		return false;
			  	}
        	}

        	// CONTROLLO CASA o ALTRO LUOGO DI CURA <=> codEsito = 0145
        	if (document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value == '0145') {
		  		if (document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
		  			alert("Attenzione: per l'esito Concede e ordina il ricovero\nin una casa di cura o in altro luogo di cura\nvalorizzare il campo Casa o Altro Luogo di Cura");
			  		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.focus();
			  		return false;
			  	}
        	}

        	// CONTROLLO DATA SCARCERAZIONE
//         	if (document.LoadInsOLDifferimento.eseguita[0].checked == true) {
<%
if (!lPosMod.isLibero()) {
%>
			if (document.LoadInsOLDifferimento.GiornoDataScarcerazione.value.length != 0
					&& document.LoadInsOLDifferimento.MeseDataScarcerazione.value.length != 0
					&& document.LoadInsOLDifferimento.AnnoDataScarcerazione.value.length != 0) {
	        	if (document.LoadInsOLDifferimento.GiornoDataScarcerazione.value.length == 1)
				  	document.LoadInsOLDifferimento.GiornoDataScarcerazione.value = '0'
				  		+ document.LoadInsOLDifferimento.GiornoDataScarcerazione.value;
			  	if (document.LoadInsOLDifferimento.MeseDataScarcerazione.value.length == 1)
				  	document.LoadInsOLDifferimento.MeseDataScarcerazione.value='0'
				  		+ document.LoadInsOLDifferimento.MeseDataScarcerazione.value;
			  	data_to_verify = document.LoadInsOLDifferimento.GiornoDataScarcerazione.value+'/'+
			  		document.LoadInsOLDifferimento.MeseDataScarcerazione.value+'/'+
			  		document.LoadInsOLDifferimento.AnnoDataScarcerazione.value;
	        	if (!ControllaData(data_to_verify)) {
	        		alert("Data Scarcerazione non valida");
	        		document.LoadInsOLDifferimento.GiornoDataScarcerazione.focus();
				   	return false;
			  	}

	        	// Controllo : data di sistema deve essere >= DATA SCARCERAZIONE
			    if (!CompareDate(data_to_verify, data_sistema)) {
			      alert("Data Scarcerazione non può essere superiore alla data odierna");
			      document.LoadInsOLDifferimento.GiornoDataScarcerazione.focus();
			      return false;
			    }
			}
<%
}
%>
//         	}

        	// controllo obbligatorietà Campi inseriti manualmente
<%
if (decreto != null && Utils.isNullObj(decreto.getEvento())) {
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
<%-- 			if ((dataFineRinvio != "" && durata != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)
					|| (dataFineRinvio != "" && durata != "")
					|| (durata != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)
					|| (dataFineRinvio != "" && document.LoadInsOLDifferimento.flagDecisioneTribunale.checked)) {
				alert("Data Fine Rinvio, Durata e Decisione del TDS sono mutuamente esclusivi! Scegliere solo una delle tre opzioni!");
				document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
				return false;
			} --%>
<%
}
%>
		} <%-- Chiude function Verify --%>

	    function checkMagSorv(field) {
	    	if (field.value != "" && (document.LoadInsOLDifferimento.tipoUDS.value == ""
	    			|| document.LoadInsOLDifferimento.tipoUDS.value == "-")) {
				alert("Specificare il Magistrato di Sorveglianza");
				return false;
	    	}
	    }

// 	    function gestisciEsecuzione(rb) {
// 	    	if (rb.value == "siep") {
// 	    		document.getElementById("rigaDataDiff").style.display = "none";
// 	    		document.getElementById("rigaScarcerazione").style.display = "block";
<%-- 	    		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = ""; --%>
<%-- 	    		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = ""; --%>
<%-- 	    		document.LoadInsOLDifferimento.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = ""; --%>
// 	    	} else {
// 	    		document.getElementById("rigaDataDiff").style.display = "block";
// 	    		document.getElementById("rigaScarcerazione").style.display = "none";
// 	    		document.LoadInsOLDifferimento.GiornoDataScarcerazione.value = "";
// 	    		document.LoadInsOLDifferimento.MeseDataScarcerazione.value = "";
// 	    		document.LoadInsOLDifferimento.AnnoDataScarcerazione.value = "";
// 	    	}
// 	    }
	</script>
</head>
<body class="corpo">
	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class="LBG">
      			<font class="label">Funzione:</font>&nbsp;&nbsp;
        		<font class="campo">Inserimento Ordine Liberazione per Differimento della Misura Sicurezza</font>
      		</td>
    	</tr>
	</table>
  	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
  	<FORM method="POST" name="LoadInsOLDifferimento" action="<%=IWebConstants.PG_MAIN%>">
  		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciOLDifferimento">
  		<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
   		<input type="HIDDEN" title="id Evento" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
    	<table style="width: 95%;">
      		<tr>
        		<td class="l" width="21%">Posizione Giuridica</td>
        		<td class="L" colspan="7">
          			<font class="campo">
          				<%=posizioneluogoaltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%>
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
		lMis = (MisuraSicurezzaModel) itx.next();
%>	    
			<tr>
		    	<td class=L width="21%">Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
		      	<td class=C> Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%></font></td>
		      	<td class=C> Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%></font></td>
		      	<td class=C> Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%></font></td>
		    </tr>
		    <tr>
		    	<td>
		    		<input type="HIDDEN" title="Id Misura" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=StringUtils.toStringJSP(lMis.getIdMisuraSicurezza())%>">
		    	</td>
		    </tr>
<%
	}
}
%>
	    </table>
		<!-- Date del Provvedimento -->
		<br>	
		<table style="width: 95%;">
			<tr>
	        	<td class="l" width="21%">Data Emissione<font class="ob">(*)</font></td>
	        	<td class="L">
		          	<input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		          	<input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		          	<input type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;
			        <a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
	        	</td>
		        <td class="l" width="21%">Data Trasmissione<font class="ob">(*)</font></td>
		        <td class="L">
		          	<input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" value="<%=DateUtils.getSysDate("dd")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		          	<input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" value="<%=DateUtils.getSysDate("MM")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
		          	<input type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" value="<%=DateUtils.getSysDate("yyyy")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		          	<a href="Javascript:impostaDataOdierna('<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>','<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
		        </td>
      		</tr>
		</table>

<%
// se provengo da annotazione decisione sorveglianza
if (provvedimento != null && provvedimento.getEvento() != null && provvedimento.getFascicoloSiusModel() != null
			&& eventonotifica != null && eventonotifica.getEvento() != null) {
%>
		<!-- Dati provvedimento della Sorveglianza -->
		<table style="width: 95%;"> 
			<tr>
				<td class="Titolo">Estremi Provvedimento della Sorveglianza</td>
			</tr>
			<tr>
				<td class="l">
      				<input type="hidden" name="idEventoFascSius" value="<%=StringUtils.toStringJSP(provvedimento.getEvento().getIdEvento())%>">
      				<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getIdFascicoloSius())%>">
<!--         			<a href="Javascript:listaProvvSIUS('LoadInsOLDifferimento');"> -->
<!--           				Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border="0"> -->
<!--         			</a> -->
      			</td>
    		</tr>
		</table>

		<table style="width: 95%;">
			<tr>
		    	<td class="l" width="21%">Autorita' Emittente<font class="ob">(*)</font></td>
		    	<td class="l">
		    		<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" value="<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getChiaveUfficio())%>">
		    		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(provvedimento.getEvento().getCodUfficioEmittente())%>">
		    		<input Title="Autorita' Emittente" name="descrTipoUfficio" value="<%=StringUtils.toStringJSP(provvedimento.getDescrTipoUfficio())%>" type="text" size="35" readonly>
<%-- 		    		<select Title="Autorita' Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>"> --%>
<%--         				<%=autorita%> --%>
<!--       				</select> -->
		      	</td>
		      	<td class="l">Sede<font class="ob">(*)</font></td>
		      	<td class="l">
		      		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(provvedimento.getEvento().getCodLuogoEmittente())%>">
		        	<input title="Sede Autorita' Emittente" type="text" name="descrComuneUfficio" value="<%=StringUtils.toStringJSP(provvedimento.getDescrComuneUfficio())%>" size="35" readonly>
<%--         			<a href="Javascript:ListaUfficiComuni('LoadInsOLDifferimento','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>', --%>
<%--         					document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);"> --%>
<!--           				<img src="/images/filefolder.gif" border="0"> -->
<!--         			</a> -->
		    	</td>
		    </tr>
		</table>

		<table style="width: 95%;">
    		<tr>
        		<td class="l" width="21%">Data Emissione Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
          			<input Title="Giorno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(), "dd"))%>" type="text" size="2" readonly> -
          			<input Title="Mese Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(), "MM"))%>" type="text" size="2" readonly> -
          			<input Title="Anno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(), "yyyy"))%>" type="text" size="4" readonly>
        		</td>
        		<td class="l">Anno / Numero Fascicolo SIUS</td>
       			<td class="L">
         			<input Title="Anno Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>" value="<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getChiaveAnno())%>" type="text" size="4" readonly> /
         			<input Title="Numero Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>" value="<%=StringUtils.toStringJSP(provvedimento.getFascicoloSiusModel().getChiaveProgr())%>" type="text" size="6" readonly>
      			</td>
     		</tr>
     		<tr>
     			<td class="l">Tipo Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
        			<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=StringUtils.toStringJSP(eventonotifica.getEvento().getCodTipoProvvedimento())%>">
        			<input title="Tipo Provvedimento" type="text" name="descrProvvedimento" value="<%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>" size="70" readonly>
<%--         			<select title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>"> --%>
<%--           				<%=tipoprovvedimento%> --%>
<!--           			</select> -->
        		</td>
<%
	String annoS3S72 = "";
	String numS3S72 = "";
	if (provvedimento.getEvento().getCodTipoProvvedimento().compareTo("02") == 0) {
		annoS3S72 = StringUtils.toStringJSP(provvedimento.getDecreto().getAnnoS72());
		numS3S72 = StringUtils.toStringJSP(provvedimento.getDecreto().getNumS72());
	}
%>
      			<td class="l">Anno / Numero Provvedimento</td>
      			<td class="L">
         			<input Title="Anno Provvedimento" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>" value="<%=annoS3S72%>" type="text" size="4" readonly> /
         			<input Title="Numero Provvedimento" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" value="<%=numS3S72%>" type="text" size="6" readonly>
      			</td>        
   			</tr>
			<!-- Definizione procedimento -->
			<tr>
				<td class="L">Oggetto<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=StringUtils.toStringJSP(provvedimento.getEvento().getCodMotivo())%>">
					<input title="Oggetto Procedimento" type="text" name="descrOggetto" value="<%=StringUtils.toStringJSP(provvedimento.getDescrOggetto())%>" size="100" readonly>
<%-- 			   		<select title="Oggetto Procedimento" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"> --%>
<%--           				<%=mp%> --%>
<!--           			</select> -->
				</td>
			</tr>
			<tr>
				<td class="L">Esito<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>" value="<%=StringUtils.toStringJSP(provvedimento.getEvento().getCodEsito())%>">
					<input title="Esito Provvedimento" type="text" name="descrEsito" value="<%=StringUtils.toStringJSP(provvedimento.getDescrEsito())%>" size="100" readonly>
<%-- 					<select title="Esito Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>"> --%>
<%--           				<%=et%> --%>
<!--           			</select> -->
				</td>
			</tr>
		</table>

<%
	String gdfm = "", mdfm = "", adfm = "", aass = "", mmss = "", ggss = "", lsdp = "", gdip = "", mdip = "", adip = "";
	if (provvedimento.getDecreto() != null) {
		if (provvedimento.getDecreto().getDataScadenzaSospensioneSS() != null) {
			gdfm = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataScadenzaSospensioneSS(), "dd"));
			mdfm = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataScadenzaSospensioneSS(), "MM"));
			adfm = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataScadenzaSospensioneSS(), "yyyy"));
		}
		aass = StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(provvedimento.getDecreto().getSospensioneAASS()),"");
		mmss = StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(provvedimento.getDecreto().getSospensioneMMSS()),"");
		ggss = StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(provvedimento.getDecreto().getSospensioneGGSS()),"");
		lsdp = StringUtils.toStringJSP(provvedimento.getDecreto().getLuogoSvolgimentoProva(),"-");
		if (provvedimento.getDecreto().getDataSospensioneSS() != null) {
			gdip = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataSospensioneSS(), "dd"));
			mdip = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataSospensioneSS(), "MM"));
			adip = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDecreto().getDataSospensioneSS(), "yyyy"));
		}
	}
%>
		<table style="width: 95%;">
			<tr>
				<td class="L">Data Fine Rinvio</td>
				<td class="L" nowrap="nowrap">
			 		<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" value="<%=gdfm%>" title="Giorno Data Fine Rinvio" type="text" size="2" readonly> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" value="<%=mdfm%>" title="Mese Data Fine Rinvio" type="text" size="2" readonly> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" value="<%=adfm%>" title="Anno Data Fine Rinvio" type="text" size="4" readonly>
<%-- 					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');"> --%>
<!-- 						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"> -->
<!-- 			        </a> -->
			   	</td>
			   	<td class="L">oppure Durata:</td>
				<td class="L" nowrap="nowrap">
			 		&nbsp;Anni&nbsp;<input name="numAnniSospensione" value="<%=aass%>" type="text" size="2" readonly> -
					&nbsp;Mesi&nbsp;<input name="numMesiSospensione" value="<%=mmss%>" type="text" size="2" readonly> -
					&nbsp;Giorni&nbsp;<input name="numGiorniSospensione" value="<%=ggss%>" type="text" size="4" readonly>
					&nbsp;oppure&nbsp;Fino alla Decisione del TDS&nbsp;
					<input type="checkbox" name="flagDecisioneTribunale" title="Decisione TDS" onclick="this.checked=!this.checked;" readonly>
			   	</td>
			</tr>
			<tr>
				<td class="L">Casa o Altro Luogo di Cura</td>
			   	<td class="L" colspan="3">
			   		<input title="Casa o Altro Luogo di Cura" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" value="<%=lsdp%>" size="100" readonly>
				</td>
			</tr>
			<tr>
				<td class="L" width="21%">Data Differimento<font class="ob">(*)</font></td>
				<td class="L" colspan="3">
			 		<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" value="<%=gdip%>" title="Giorno Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" value="<%=mdip%>" title="Mese Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" value="<%=adip%>" title="Anno Data Differimento" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
			   	</td>
			</tr>
<%
	if (!lPosMod.isLibero()) {
%>
			<tr>
				<td class="l">
					Da Scarcerare&nbsp;<input type="radio" name="isScarcerato" value="daScarcerare" checked>
	    		</td>
	    		<td class="l">
					Scarcerato&nbsp;<input type="radio" name="isScarcerato" value="scarcerato">
	    		</td>
	    		<td class="L" colspan="2">
	    			Data Scarcerazione&nbsp;
			 		<input name="GiornoDataScarcerazione" value="" title="Giorno Data Scarcerazione" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="MeseDataScarcerazione" value="" title="Mese Data Scarcerazione" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="AnnoDataScarcerazione" value="" title="Anno Data Scarcerazione" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			   	</td>
			</tr>
<%
	}
%>
		</table>
<%
} else {
	if (decreto != null && decreto.getEvento() != null && decreto.getFascicoloSiusModel() != null
			&& decreto.getDecreto()!= null) {
// provengo dal menu' gestione MS --> O.L. per differimento
// RIEMPIO I CAMPI IN AUTOMATICO
%>
		<table style="width: 95%;"> 
			<tr>
				<td class="Titolo">Estremi Provvedimento della Sorveglianza</td>
			</tr>
			<tr>
				<td class="l">
      				<input type="hidden" name="idEventoFascSius" value="<%=StringUtils.toStringJSP(decreto.getEvento().getIdEvento())%>">
      				<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(decreto.getFascicoloSiusModel().getIdFascicoloSius())%>">
<!--         			<a href="Javascript:listaProvvSIUS('LoadInsOLDifferimento');"> -->
<!--           				Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border="0"> -->
<!--         			</a> -->
				</td>
    		</tr>
		</table>

		<table style="width: 95%;">
			<tr>
		    	<td class="l" width="21%">Autorita' Emittente<font class="ob">(*)</font></td>
		    	<td class="l">
		    		<input type="hidden" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" value="<%=StringUtils.toStringJSP(decreto.getFascicoloSiusModel().getChiaveUfficio())%>">
		    		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(decreto.getEvento().getCodUfficioEmittente())%>">
		    		<input Title="Autorita' Emittente" type="text" name="descrTipoUfficio" value="<%=StringUtils.toStringJSP(decreto.getDescrTipoUfficio())%>" size="35" readonly>
<%-- 		    		<select Title="Autorita' Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>"> --%>
<%--         				<%=autorita%> --%>
<!--       				</select> -->
		      	</td>
		      	<td class="l">Sede<font class="ob">(*)</font></td>
		      	<td class="l">	
		        	<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(decreto.getEvento().getCodLuogoEmittente())%>">
		        	<input title="Sede Autorita' Emittente" type="text" name="descrComuneUfficio" value="<%=StringUtils.toStringJSP(decreto.getDescrComuneUfficio())%>" size="35" readonly>
<%--         			<a href="Javascript:ListaUfficiComuni('LoadInsOLDifferimento','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>', --%>
<%--         					document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);"> --%>
<!--           				<img src="/images/filefolder.gif" border="0"> -->
<!--         			</a> -->
		    	</td>
		    </tr>
		</table>

		<table style="width: 95%;">
    		<tr>
        		<td class="l" width="21%">Data Emissione Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
          			<input Title="Giorno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getEvento().getDataEmissione(), "dd"))%>" type="text" size="2" readonly> -
          			<input Title="Mese Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getEvento().getDataEmissione(), "MM"))%>" type="text" size="2" readonly> -
          			<input Title="Anno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getEvento().getDataEmissione(), "yyyy"))%>" type="text" size="4" readonly>
        		</td>
        		<td class="l">Anno / Numero Fascicolo SIUS</td>
       			<td class="L">
         			<input Title="Anno Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>" value="<%=StringUtils.toStringJSP(decreto.getFascicoloSiusModel().getChiaveAnno())%>" type="text" size="4" readonly> /
         			<input Title="Numero Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>" value="<%=StringUtils.toStringJSP(decreto.getFascicoloSiusModel().getChiaveProgr())%>" type="text" size="6" readonly>
      			</td>
     		</tr>
     		<tr>
     			<td class="l">Tipo Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
        			<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" value="<%=StringUtils.toStringJSP(decreto.getEvento().getCodTipoProvvedimento())%>">
        			<input title="Tipo Provvedimento" type="text" name="descrProvvedimento" value="<%=StringUtils.toStringJSP(decreto.getDescrProvvedimento())%>" size="70" readonly>
<%--         			<select title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>"> --%>
<%--           				<%=tipoprovvedimento%> --%>
<!--           			</select> -->
        		</td>
      			<td class="l">Anno / Numero Provvedimento</td>
      			<td class="L">
         			<input Title="Anno Provvedimento" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>" value="<%=StringUtils.toStringJSP(decreto.getAnnoProtocollo())%>" type="text" size="4" readonly> /
         			<input Title="Numero Provvedimento" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" value="<%=StringUtils.toStringJSP(decreto.getNumeroProtocollo())%>" type="text" size="6" readonly>
      			</td>        
   			</tr>
			<!-- Definizione procedimento -->
			<tr>
				<td class="L">Oggetto<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=StringUtils.toStringJSP(decreto.getEvento().getCodMotivo())%>">
			   		<input title="Oggetto Procedimento" type="text" name="descrOggetto" value="<%=StringUtils.toStringJSP(decreto.getDescrOggetto())%>" size="100" readonly>
<%-- 			   		<select title="Oggetto Procedimento" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"> --%>
<%--           				<%=mp%> --%>
<!--           			</select> -->
				</td>
			</tr>
			<tr>
				<td class="L">Esito<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>" value="<%=StringUtils.toStringJSP(decreto.getEvento().getCodEsito())%>">
					<input title="Esito Provvedimento" type="text" name="descrEsito" value="<%=StringUtils.toStringJSP(decreto.getDescrEsito())%>" size="100" readonly>
<%-- 					<select title="Esito Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>"> --%>
<%--           				<%=et%> --%>
<!--           			</select> -->
				</td>
			</tr>
		</table>

		<table style="width: 95%;">
			<tr>
				<td class="L">Data Fine Rinvio</td>
				<td class="L" nowrap="nowrap">
			 		<input title="Giorno Data Fine Rinvio" type="text" size="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataSospensioneSS(),"dd"))%>" readonly> -
					<input title="Mese Data Fine Rinvio" type="text" size="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataSospensioneSS(),"MM"))%>" readonly> -
					<input title="Anno Data Fine Rinvio" type="text" size="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataSospensioneSS(),"yyyy"))%>" readonly>
<%-- 					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');"> --%>
<!-- 						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"> -->
<!-- 			        </a> -->
			   	</td>
			   	<td class="L">oppure Durata:</td>
				<td class="L" nowrap="nowrap">
			 		&nbsp;Anni&nbsp;<input type="text" size="2" name="numAnniSospensione" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(decreto.getDecreto().getSospensioneAASS()),"")%>" readonly> -
					&nbsp;Mesi&nbsp;<input type="text" size="2" name="numMesiSospensione" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(decreto.getDecreto().getSospensioneMMSS()),"")%>" readonly> -
					&nbsp;Giorni&nbsp;<input type="text" size="4" name="numGiorniSospensione" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(decreto.getDecreto().getSospensioneGGSS()),"")%>" readonly>
					&nbsp;oppure&nbsp;Fino alla Decisione del TDS&nbsp;
					<input type="checkbox" name="flagDecisioneTribunale" title="Decisione TDS" onclick="this.checked=!this.checked;" readonly>
			   	</td>
			</tr>
			<tr>
				<td class="L">Casa o Altro Luogo di Cura</td>
			   	<td class="L" colspan="3">
			   		<input title="Casa o Altro Luogo di Cura" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" value="<%=StringUtils.toStringJSP(decreto.getDecreto().getLuogoSvolgimentoProva(),"-")%>" size="100" readonly>
				</td>
			</tr>
			<tr>
				<td class="L" width="21%">Data Differimento<font class="ob">(*)</font></td>
				<td class="L" colspan="3">
			 		<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataScadenzaSospensioneSS(),"dd"))%>" title="Giorno Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataScadenzaSospensioneSS(),"MM"))%>" title="Mese Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decreto.getDecreto().getDataScadenzaSospensioneSS(),"yyyy"))%>" title="Anno Data Differimento" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
			   	</td>
			</tr>
			
<%
	if (!lPosMod.isLibero()) {
%>
			<tr>
				<td class="l">
					Da Scarcerare&nbsp;<input type="radio" name="isScarcerato" value="daScarcerare" checked>
	    		</td>
	    		<td class="l">
					Scarcerato&nbsp;<input type="radio" name="isScarcerato" value="scarcerato">
	    		</td>
	    		<td class="L" colspan="2">
	    			Data Scarcerazione&nbsp;
			 		<input title="Giorno Data Scarcerazione" name="GiornoDataScarcerazione" value="" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Mese Data Scarcerazione" name="MeseDataScarcerazione" value="" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Anno Data Scarcerazione" name="AnnoDataScarcerazione" value="" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			   	</td>
			</tr>
<%
	}
%>
		</table>
<%
	} else { // FINE RIEMPIO I CAMPI AUTOMATICAMENTE
		// RIEMPIO I CAMPI MANUALMENTE
%>
		<table style="width: 95%;"> 
			<tr>
				<td class="Titolo">Estremi Provvedimento della Sorveglianza</td>
			</tr>
		</table>
		<table style="width: 95%;">
			<tr>
		    	<td class="l" width="21%">Autorita' Emittente<font class="ob">(*)</font></td>
		    	<td class="l">
		    		<select Title="Autorita' Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>">
        				<%=autorita%>
      				</select>
		      	</td>
		      	<td class="l">Sede<font class="ob">(*)</font></td>
		      	<td class="l">	
		        	<input title="Sede Autorita' Emittente" type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>" maxlength="35" size="35">
        			<a href="Javascript:ListaUfficiComuni('LoadInsOLDifferimento','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',
        					document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsOLDifferimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
          				<img src="/images/filefolder.gif" border="0">
        			</a>
		    	</td>
		    </tr>
		</table>

		<table style="width: 95%;">
    		<tr>
        		<td class="l" width="21%">Data Emissione Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
          			<input Title="Giorno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" type="text" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          			<input Title="Mese Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>" type="text" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          			<input Title="Anno Emissione Provvedimento" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>" type="text" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        		</td>
        		<td class="l">Anno / Numero Fascicolo SIUS</td>
       			<td class="L">
         			<input Title="Anno Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>" type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         			<input Title="Numero Fascicolo SIUS" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      			</td>
     		</tr>
     		<tr>
     			<td class="l">Tipo Provvedimento<font class="ob">(*)</font></td>
        		<td class="L">
        			<select title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
          				<%=tipoprovvedimento%>
          			</select>
        		</td>
      			<td class="l">Anno / Numero Provvedimento</td>
      			<td class="L">
         			<input Title="Anno Provvedimento" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>" type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         			<input Title="Numero Provvedimento" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      			</td>        
   			</tr>
			<!-- Definizione procedimento -->
			<tr>
				<td class="L">Oggetto<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
			   		<select title="Oggetto Procedimento" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
          				<%=mp%>
          			</select>
				</td>
			</tr>
			<tr>
				<td class="L">Esito<font class="ob">(*)</font></td>
			   	<td class="L" colspan="3">
					<select title="Esito Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>">
          				<%=et%>
          			</select>
				</td>
			</tr>
		</table>

		<table style="width: 95%;">
			<tr>
				<td class="L">Data Fine Rinvio</td>
				<td class="L" nowrap="nowrap">
			 		<input title="Giorno Data Fine Rinvio" type="text" size="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Mese Data Fine Rinvio" type="text" size="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Anno Data Fine Rinvio" type="text" size="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
			   	</td>
			   	<td class="L">oppure Durata:</td>
				<td class="L" nowrap="nowrap">
			 		&nbsp;Anni&nbsp;<input type="text" maxlength="2" size="2" name="numAnniSospensione" onkeypress="return TicTabNumField(this,event)"> -
					&nbsp;Mesi&nbsp;<input type="text" maxlength="2" size="2" name="numMesiSospensione" onkeypress="return TicTabNumField(this,event)"> -
					&nbsp;Giorni&nbsp;<input type="text" maxlength="4" size="4" name="numGiorniSospensione" onkeypress="return TicTabNumField(this,event)">
					&nbsp;oppure&nbsp;Fino alla Decisione del TDS&nbsp;
					<input type="checkbox" name="flagDecisioneTribunale" title="Decisione TDS">
			   	</td>
			</tr>
			<tr>
				<td class="L">Casa o Altro Luogo di Cura</td>
			   	<td class="L" colspan="3">
			   		<input title="Casa o Altro Luogo di Cura" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" size="100">
				</td>
			</tr>

<!-- 			<tr id="rigaDataDiff" style="display: none;"> -->
			<tr>
				<td class="L" width="21%">Data Differimento<font class="ob">(*)</font></td>
				<td class="L" colspan="3">
			 		<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" value="" title="Giorno Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" value="" title="Mese Data Differimento" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" value="" title="Anno Data Differimento" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
					<a href="Javascript:impostaDataOdierna('<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>','<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
						<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
			        </a>
			   	</td>
			</tr>
			<%
		if (!lPosMod.isLibero()) {
%>
			<tr>
				<td class="l">
					Da Scarcerare&nbsp;<input type="radio" name="isScarcerato" value="daScarcerare" checked>
	    		</td>
	    		<td class="l">
					Scarcerato&nbsp;<input type="radio" name="isScarcerato" value="scarcerato">
	    		</td>
	    		<td class="L" colspan="2">
	    			Data Scarcerazione&nbsp;
			 		<input title="Giorno Data Scarcerazione" name="GiornoDataScarcerazione" value="" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Mese Data Scarcerazione" name="MeseDataScarcerazione" value="" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
					<input title="Anno Data Scarcerazione" name="AnnoDataScarcerazione" value="" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			   	</td>
			</tr>
<%
		}
%>
		</table>
<%
	} // FINE RIEMPIO I CAMPI MANUALMENTE
}
%>

    	<!--Magistrato Firmatario -->   
	 	<table style="width: 95%;">
			<tr><td class="Titolo" colspan="4">Magistrato Firmatario</td></tr>
	     	<tr>
			     <td class="L" width="21%">Magistrato Firmatario<font class=ob>(*)</font></td>
			     <td class="L" colspan="3">
			         <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" size="35">
			         <input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" size="35">
			           <a href="Javascript:ListaMagistrati('LoadInsOLDifferimento');">
			            <img src="/images/filefolder.gif" border="0">
			          </a>
			          <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" type="text" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>">
			     </td>
			</tr>
			<!--Notifica per ESECUZIONE - ISTITUTO -->
	 		<tr><td class="Titolo" colspan="4">Destinatari</td></tr>
	 		
	 	  <!-- Notifica per ESECUZIONE - Struttura Designata-->
	   <tr>	
		 <td class="L">Struttura Designata</td>
		<%
		if (strutturaDesignataModel != null && Utils.isPresent(strutturaDesignataModel.getIdIstitutoDetenzione())) {
		%>
				<td class="l" colspan="3">				
			      	<input readonly Title="Struttura" name="Comune" value="<%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrizione())%> - <%=StringUtils.toStringJSP(strutturaDesignataModel.getIndirizzo())%>" size=100>
			      	<input type="hidden" Title="Struttura" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(strutturaDesignataModel.getIdIstitutoDetenzione())%>">
		      		<%-- <a href="Javascript:ListaIstitutoDetenzione('LoadInsOLDifferimento','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>','Comune');">
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
	
			<!-- Notifica per ESECUZIONE - Autorita' per Esecuzione -->
			<tr>	
				<td class="L">Autorita' per Esecuzione</td>
		 		<td class="L" >
		       		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
		        		<%=autoritaEsternaE%>
		       		</select>
		      	</td>
		      	<td class="l">Note</td>
		      	<td class="L">
		        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="40"></textarea>
		      	</td>
		    </tr>
	    	<tr>
	      		<td class="l">Sede</td>
	      		<td class="L" colspan="3">
	        		<input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35">
	        		<a href="Javascript:ListaComuni('LoadInsOLDifferimento','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
	          			<img src="/images/filefolder.gif" border="0">
	        		</a>
	      		</td>
			</tr>
			<!-- Destinatario per l'Ente di Sorveglianza -->
	  		<tr>
				<td class="L">Magistrato di Sorveglianza</td>
		    	<td class="L">
		      		<select Title="Magistrato di Sorveglianza" name="tipoUDS" >
		      			<%=tipoUDS%>
		      		</select>
		   		</td>
		   		<td class="L" colspan="2">
		      		<input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>" name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25" onblur="javascript: return checkMagSorv(this);">
		      			<a href="Javascript:ListaUfficiComuni('LoadInsOLDifferimento','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>',document.LoadInsOLDifferimento.tipoUDS[document.LoadInsOLDifferimento.tipoUDS.options.selectedIndex].value);">
		         			<img src="/images/filefolder.gif" border="0">
		      			</a> 
    			</td>
		  	</tr>
		</table>

		<!--Notifica per Difensore e condannato  -->    
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
<%
int lIdxAvv = 0;
Iterator lItxAvv = avvocati.iterator();
while (lItxAvv.hasNext()) {
	AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
%>
				<table style="width: 95%;">
					<tr>
						<td class="l" width="21%">Autorita' Destinazione <font class=ob>(*)</font></td>
						<td class="L" colspan="3">
							<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP()">
								<%=autoritaEsterna%>
							</select>
						</td>
					</tr>
					<tr>
						<td class="l">Sede <font class=ob>(*)</font></td>
						<td class="L">
						  <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
							<input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
	if (avvocati.size() > 1) {
%>
							<a href="Javascript:ListaComuni('LoadInsOLDifferimento','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
	} else {
%>
			       			<a href="Javascript:ListaComuni('LoadInsOLDifferimento','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
								<img src="/images/filefolder.gif" border="0">
							</a>
<%
	}
%>				
						</td>
						<td class="l">Indirizzo</td>
						<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="30"></textarea></td>
					</tr>
					<tr>
						<td class="l">Per Avvocato</td>
						<td class="l" colspan="3">
							<input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
							<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
							<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
							<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></font>
							<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>">
						</td>
					</tr>
<%
	lIdxAvv++;
}
%>
				</table>
			</div>
		</div>

		<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
		<table>
			<tr>
				<td class="lNoBord" colspan="2"><br>
				<INPUT class="bottone" type="submit" name="I" value="Conferma" onclick="javascript: return Verify();"></td>
			</tr>
		</table>
	</form>

</body>
</html>