<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS"%>

<jsp:useBean id="modalita"  							scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" 						scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="datiOrdinanza" 						scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="depositoDecretoMotivazioni" 			scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori" 								scope="request" class="java.util.Vector"/>
<jsp:useBean id="misuraSicurezza" 						scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<%-- MEV_2023-35: aggiunto useBean per gestione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva --%>
<jsp:useBean id="tipoPeneSostitutive" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="esecuzioneSanzioneSostitutivaModel" 	scope="request" class="siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel"/>
  
<%
String[] esiti = (String[]) request.getAttribute("esiti");

// MERGE v10: per i maggiorenni la tabella "tableInForma" non deve essere visibile, aggiunti import
UtenteModel lUteMod = (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
boolean isUffMinor = false;
if ("TDSM".equals(CodUff) || "UDSM".equals(CodUff))
	isUffMinor = true;

// Variabili
Date data_emissione =null;
Date data_deposito = null;
TenoreModel[] lTenori = null;
String lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
String lIdEvento = "";
String lIdOrdinanza = "";
String lIdDecreto = "";

String data_decorrenza_ins = "";
String durata_misura_ins = "";
if (misuraSicurezza != null && misuraSicurezza.getDataDecorrenza() != null)
	data_decorrenza_ins = DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(),"dd/MM/yyyy");

if (datiOrdinanza.getTenori() != null && datiOrdinanza.getTenori()[0].getCodOggettoTenore().equals("2431")) {
   data_decorrenza_ins="true";
   durata_misura_ins="true";
}

// Estrazione della data minima: data udienza oppure iscrizione fascicolo
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
else if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
  	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
else
	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");

// MEV_39: aggiunta variabile
String tipo = "";

// Switch fra il caso Decreto e quello  Ordinanza
if (request.getParameter("tipo_provvedimento") != null && request.getParameter("tipo_provvedimento").equalsIgnoreCase("decreto")) {
	// Modifica Decreto
	data_emissione = depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione();
	data_deposito = depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito();
	lTenori = (TenoreModel[])  tenori.toArray(new TenoreModel[0]);
	lIdEvento = depositoDecretoMotivazioni.getEvento().getIdEvento().toString();
	lIdOrdinanza = "";
	lIdDecreto = depositoDecretoMotivazioni.getDepositoDecreto().getIdDepositoDecreto().toString();
	// MEV_39: impostazione variabile
	tipo = depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto();
} else {
	// Modifica Ordinanza
	data_emissione = datiOrdinanza.getEvento().getDataEmissione();
	data_deposito = datiOrdinanza.getOrdinanza().getDataDeposito();
	lTenori = datiOrdinanza.getTenori();
	lIdEvento = datiOrdinanza.getEvento().getIdEvento().toString();
	lIdOrdinanza = datiOrdinanza.getOrdinanza().getIdDepositoOrdinanzaPc().toString();
	lIdDecreto = "";
	// MEV_39: impostazione variabile
	tipo = datiOrdinanza.getOrdinanza().getCodTipoOrdinanza();
}

// Estrazione della data massima: data di deposito o data di sistema
String data2;
if (data_deposito != null)
  	data2 = DateUtils.getDateToString(data_deposito,"dd/MM/yyyy");
else
 	data2 = DateUtils.getSysDate("dd/MM/yyyy");
%>
<html>
	<head>
	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	    <script language="JavaScript">
			function Verify() {
		     	var flagDate = VerificaDate();
		     	// MEV_39: aggiunto controllo
		     	<% if ("42".equals(tipo) || "MS".equals(tipo)) { %>
			     	node = document.getElementById("datarinvio");
		      		if (node.style.display == 'none') {
		      			<% if (Utils.isPresent(lIdDecreto)) { %>
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value = "";
			      		<% } else { %>
				      		document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value = "";
			      			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value = "";
			      		<% } %>
		      		}
		     	<% } %>
		     	if (!flagDate)
		    	 	return flagDate;
		     	var durata_misura='<%=durata_misura_ins%>';
		     	if (durata_misura != null && durata_misura != "") {
		         	divDurata.style.visibility='visible';
		         	var numMesi=document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA%>.value;
		         	if (numMesi > 11) {
		             	alert ("Il campo Mesi non deve essere maggiore di 11");
		             	return false;
		         	}
					var numGiorni=document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA%>.value;
					if (numGiorni > 29) {
						alert ("Il campo Giorni non deve essere maggiore di 29");
			        	return false;
			        }
			        return true;
				}
		     	
		     	return true;
		    }
		
			function VerificaDate() {
		      	var ritorno = true;
		      	var data_camera = '<%=data1%>';
		      	var data_deposito = '<%=data2%>';
		      	var data_emissione = document.ModificaOrdinanza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
		      	var data_decorrenza;
		      	var nodeDataDec;
			  	nodeDataDec = document.getElementById('divDataDecorrenza');
			  	if (nodeDataDec.style.visibility == 'visible'){
		    	  	data_decorrenza = document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>.value;
		    	  	document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
			  	}
		      	// Controllo della data emissione.
			  	if (!ControllaData(data_emissione)) {
			        alert('Data emissione non valida!');
			        ritorno =  false;
		      	}
		      	// Controllo data di sistema >= Data Emissione .
		      	else if (!CompareDate(data_emissione, data_deposito)) {
			        alert("La data di emissione non può essere maggiore della data di deposito o in assenza di essa, della data di Sistema!");
			        ritorno =  false;
		     	}
		      	// Controllo della data deposito <= data camera di consiglio
		      	else if (data_camera != null && !CompareDate(data_camera, data_emissione)) {
			        alert("La data di emissione non può essere minore della Data Udienza!");
			        ritorno =  false;
		      	}
		      	// Controllo della data decorrenza
		      	else if (data_decorrenza != null && data_decorrenza.length > 2 && !ControllaData(data_decorrenza)) {
			     	alert('Data Decorrenza per la Misura di Sicurezza non valida');
			     	return false;
			  	}
				// MEV_39: aggiunti controlli su nuovi campi
				<% if ("42".equals(tipo) || "MS".equals(tipo)) { %>
				  	node = document.getElementById("datarinvio");
	  				if (node.style.display == 'block') {
	  					var data_sospensione;
	  					var data_scadenza_sospensione;
	  					var durata_periodo = document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value;
	  					<% if (Utils.isPresent(lIdDecreto)) { %>
							data_sospensione = document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value;
							data_scadenza_sospensione = document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value;
						<% } else { %>
	  						data_sospensione = document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
							data_scadenza_sospensione = document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
						<% } %>
			    		if (data_sospensione > 2) {
			    			ritorno =  ControllaData(data_sospensione);
			  			}
		        		if (!ritorno) {
		        			// MEV_39: modifica etichetta per U082 e U077
		        			<% if ("42".equals(tipo) || "MS".equals(tipo)) { %>
								alert("Data Decorrenza Differimento Esecuzione non valida");
							<% } else {%>
								alert("Data Differimento Esecuzione non valida");
							<% } %>
							<% if (Utils.isPresent(lIdDecreto)) { %>
								document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.focus();
							<% } else { %>
								document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
							<% } %>
							return false;
		        		} else {
				            if (data_scadenza_sospensione.length > 2) {
				            	ritorno = ControllaData(data_scadenza_sospensione);
			          			if (!ritorno) {
					           		alert("Data Rinvio fino al non valida");
					           		<% if (Utils.isPresent(lIdDecreto)) { %>
					           			document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.focus();
				           			<% } else { %>
										document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
									<% } %>
					           		ritorno = false;
			           			} else {
			           		 		if (CompareDate(data_scadenza_sospensione,data_sospensione)) {
			           		 			// MEV_39: modifica etichetta per U082 e U077
			           		 			<% if ("42".equals(tipo) || "MS".equals(tipo)) { %>
					            			alert("La Data Decorrenza Differimento Esecuzione deve precedere la Data Rinvio fino al");
					            		<% } else {%>
					            			alert("La Data Differimento Esecuzione deve precedere la Data Rinvio fino al");
					            		<% } %>
					            		<% if (Utils.isPresent(lIdDecreto)) { %>
											document.ModificaOrdinanza.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.focus();
										<% } else { %>
											document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
										<% } %>
					            		ritorno = false;
			            			}
			            		}
			        		}
		        		}
		   			}
	  			<% } %>
			  	// FINE MEV_39

			  	// valore di ritorno
		      	return ritorno;
			}

		    function AbilitaDataDecorrenza() {
		 	   var esito = 0;
		 	   for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
		 		   if ( document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
		 			    // Data Decorrenza della Misura di sicurezza
			 			// viene visualizzata solo in corrispondenza di:
			 			// inserimento Emissione "Inosservanza delle misure di sicurezza detentive"
			 			// oggetto "Inosservanza delle Misure di Sicurezza  Detentive (art. 214 c.p.)"
			 			// ed Esito "Dispone che ricominci a decorrere il periodo minimi della misura"
		        		if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2004':1 }) {
		        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
		          			nodeDecorrenza.style.visibility='visible';
		          			document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
		        		}
		        		if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '-':1, '2005':1, '2006':1, '2007':1 }) {
		        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
		          			nodeDecorrenza.style.visibility='hidden';
		          			document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='N';
		        		}
	
		        		// oppure in corrispondenza di:
		        		// inserimento Emissione "Proposta di aggravamento della libertà vigilata per persone in stato di infermità 
		        		// psichica (art.232 c.p.)" e l'oggetto "Proposta di aggravamento della libertà vigilata per persone in stato 
		        		// di infermità psichica (art.232 c.p.)" ed esito "Sostituisce la libertà vigilata con la casa di cura
		        		// e custodia"
		        		if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2008':1 }) {
		        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
		          			nodeDecorrenza.style.visibility='visible';
		          			document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
		        		}
		        		if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '-':1, '2009':1, '2010':1, '2011':1, '2012':1, '2013':1 }) {
		        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
		          			nodeDecorrenza.style.visibility='hidden';
		          			document.ModificaOrdinanza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='N';
		        		}
			        }
		    	} // fine ciclo for
		    }
		
		    function visualizza_data_decorrenza() {
		      var data_decorrenza = '<%=data_decorrenza_ins%>';
			  
		      if(data_decorrenza != null && data_decorrenza != ""){
		    	  divDataDecorrenza.style.visibility='visible';
		      } else {
		    	  divDataDecorrenza.style.visibility='hidden';
		      } 
		      
		      var durata_misura='<%=durata_misura_ins%>';
		      if(durata_misura != null && durata_misura != ""){
		          divDurata.style.visibility='visible';
		      } else {
		          divDurata.style.visibility='hidden';
		      }
		    }

		    <%-- MERGE v10: nuova funzione per abilitare la modifica dell'esito per i minorenni --%>
		    function abilitaModificaEsito() {
		    	var nodeTableInforma = document.getElementById("tableInForma");
	   			nodeTableInforma.style.visibility = 'hidden';
	   			if (typeof (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
	   				for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
	   					if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
	   						if (<%=isUffMinor%>) {
	   							if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '1190':1, '1198':1, '1206':1, '1741':1, '1743':1, '1960':1, '1990':1 }) {
				       				nodeTableInforma.style.visibility = 'visible';
				       				<%
				       				if (!Utils.isNullObj(misuraSicurezza.getFlFormaMisura())) {
					       				if (misuraSicurezza.getFlFormaMisura().compareTo(new BigDecimal(1)) == 0) {
				       					%>
					       					document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[0].checked = true;
				       					<%
					       				} else {
				       					%>
					       					document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[1].checked = true;
					       				<%
					       				}
				       				}
			       					%>
				       			} else {
				       				document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[0].checked = false;
				       				document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[1].checked = false;
				       				document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>.value = '';
				       			}
				       		}
       					}
   	   				}
	 			} else {
		   			for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
						for (i = 0; i < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
				           	if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '1190':1, '1198':1, '1206':1, '1741':1, '1743':1, '1960':1, '1990':1 }) {
				           		if (<%=isUffMinor%>) {
				      				nodeTableInforma.style.visibility = 'visible';
				           		}
				      		}
						}
					}
     			}
		    }

		    <%-- MERGE v10: nuova funzione per abilitare la modifica dell'esito per i minorenni --%>
		    function abilitaDisabilitaComunita() {
		    	if (document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[0].checked)
		    		document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>.disabled = true;
	    		else
	    			document.ModificaOrdinanza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>.disabled = false;
		    }

<%-- MEV_39: aggiunta funzione --%>
function AbilitaCampiEsiti() {
<%
if ("42".equals(tipo) || "MS".equals(tipo)) {
%>
		        	var node = document.getElementById("datarinvio");
		        	var ric = document.getElementById("ricovero");
		     	   	if (typeof (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
		     	   		for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
		     	   			if ( document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
		     	   				if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1}
		     	   						|| document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1}) {
		              				node.style.display = 'block';
		              				if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
		              					ric.style.display = 'block';
		              				else
		              					ric.style.display = 'none';
		            			}
		            			if (!(document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1})
		            					&& !(document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
		              				node.style.display = 'none';
		              				ric.style.display = 'none';
		            			}
		            		}
		        		} // fine ciclo for
		     	   	} // Fine caso singolo oggetto
		     		// Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
		          	else {
		      			node.style.display = 'none';
		      			ric.style.display = 'none';
		      			for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
		      				for (i = 0; i < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
		      					if ((document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected)
		      							&& (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in {'1836':1, '1900':1, '0259':1}
		      									|| document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
		              				node.style.display = 'block';
		              				if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
		              					ric.style.display = 'block';
		                		}
		              		}
		            	}
					} // Fine caso più oggetti
<%
}
// MEV_2023-35: aggiunta gestione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva (C063)
else if (tipo.equals(ICostantiDepositoOrdinanzaPc.RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA)) {
%>
	var nodeReclamoAvversoRevocaPenaSostitutiva = document.getElementById('idDivReclamoAvversoRevocaPenaSostitutiva');
  	var isReclamo = "false";
  	if (typeof (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
    	for (j = 0; j < document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
      		if (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected
      				&& (document.ModificaOrdinanza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "3127")) {
      			isReclamo = "true";
      		}
    	} // fine ciclo for
	} // Fine caso singolo oggetto
	if (isReclamo == "true")
    	nodeReclamoAvversoRevocaPenaSostitutiva.style.display = "block";
  	else
    	nodeReclamoAvversoRevocaPenaSostitutiva.style.display = "none";
<%
}
%>
}
</script>
</head>
<body class="corpo" onload="visualizza_data_decorrenza()">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaOrdinanza">
<input type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=(misuraSicurezza!=null?misuraSicurezza.getIdMisuraSicurezza():"") %>">
<table cellspacing="2" cellpadding="2"   width=95%>
	<tr>
		<td class="Titolo" colspan=8 ><font class="label"> Dati Modificabili </font></td>
	</tr>
</table>
<table cellspacing="2" cellpadding="2" width=95%>
	<tr>
		<td class="l" width="50%">Data Emissione<font class="ob"> (*)</font></td>
		<td class="L" width="50%">
 			<input value="<%=DateUtils.getDateToString(data_emissione, "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
 			<input value="<%=DateUtils.getDateToString(data_emissione, "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
 			<input value="<%=DateUtils.getDateToString(data_emissione, "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width=95% id="divDataDecorrenza" style="visibility: hidden">
	<tr>
		<td class="l" width="50%">Data Decorrenza Misura di Sicurezza</td> 
		<td class="L" width="50%">
          	<input value="<%=(misuraSicurezza.getDataDecorrenza() == null ? "":DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"/> /
          	<input value="<%=(misuraSicurezza.getDataDecorrenza() == null ? "":DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"/> /
          	<input value="<%=(misuraSicurezza.getDataDecorrenza() == null ? "":DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"/>
  		</td>
	</tr>
</table>
<table cellspacing="2" cellpadding="2" width=95% id="divDurata" style="visibility: hidden">
	<tr>
       	<td class="l" width="50%">Durata  Misura di Sicurezza</td> 
       	<td class="L" width="50%">
           	Anni: 
           	<input value="<%=(misuraSicurezza.getNumAnni() == null ? "":misuraSicurezza.getNumAnni())%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA%>" onFocus="javascript:textboxSelect(this)"  /> 
          	Mesi:
           	<input value="<%=(misuraSicurezza.getNumMesi() == null ? "":misuraSicurezza.getNumMesi())%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA%>" onFocus="javascript:textboxSelect(this)"  /> 
           	Giorni:
           	<input value="<%=(misuraSicurezza.getNumGiorni() == null ? "":misuraSicurezza.getNumGiorni())%>" type="text" size="4" maxlength="4" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA%>" onFocus="javascript:textboxSelect(this)"    />
		</td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width=95%>
   <tr>
       <td class="Titolo" colspan=2 width=50%> Oggetto </td>
       <td class="Titolo" colspan=2 width=50%> Specificare esito per ciascuno oggetto:</td>
   </tr>
<%
for (int i=0; i < lTenori.length;i++) {
%>
	<tr>
 		<td class="l"  colspan=2 ><%=lTenori[i].getDescrOggettoTenore()%>
   			<input Title="ID Tenore" type="hidden" name="<%=ICostantiTenore.CAMPO_ID_TENORE %>" value="<%=lTenori[i].getIdTenore().toString()%>">
 		</td>
   		<td class="l"  colspan=2 width=50%>
   			<%-- MERGE v10: aggiunte chiamate a nuove funzioni js --%>
   			<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript: AbilitaDataDecorrenza(); abilitaModificaEsito(); abilitaDisabilitaComunita(); AbilitaCampiEsiti();">
      			<%=esiti[i]%>
   			</select>
 		</td>
	</tr>
<%
}
%>
	<tr><td>&nbsp;</td></tr>
	<table id="tableInForma" style="visibility:hidden" width="95%">
		<tr>
			<td class="l" width="40%">Indicare se la misura deve essere eseguita nelle forme della:</td>
			<td class="l" width="30%">
				<input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>" onclick="abilitaDisabilitaComunita();">Permanenza in casa
			</td>
			<td >&nbsp;</td>
		</tr>
		<tr>
			<td width="40%">&nbsp;</td>
			<td width="30%" class="l">
				<input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>" onclick="abilitaDisabilitaComunita();">Collocamento in Comunità
			</td>
			<td class="l">
				<input size="50" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>" value="<%=(misuraSicurezza.getDescrizioneComunita() == null ? "" : StringUtils.toStringJSP(misuraSicurezza.getDescrizioneComunita()))%>">
			</td>
		</tr>
	</table>
</table>
<%-- MEV_39: aggiunta sezione per U082 e U077 --%>
<%
if ("42".equals(tipo) || "MS".equals(tipo)) {
%>
<br>
<table cellspacing="2" cellpadding="2" width="100%" id="datarinvio" style="display: none;">
	<tr>
		<td class="l" colspan="3">In caso di sospensione indicare:<td>
	</tr>
<%
	if (Utils.isPresent(lIdDecreto)) {
%>
	<tr>
<%
				if(depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto() != null && 
				   (depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().equals("42") || depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto().equals("MS")) ){
%>
		<td class="l">Data Decorrenza Differimento Esecuzione</td>
<%
				} else {
%>     			
		<td class="l">Data Differimento Esecuzione</td>
<%
				}
%>
		<td class="L" colspan="2">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataSospensioneSS(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Rinvio fino al</td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataScadenzaSospensioneSS(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">oppure Rinvio nella misura di: 
			Anni
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneAASS()), "")%>" title="Numero Anni sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>"> 
			Mesi
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneMMSS()), "")%>" title="Numero Mesi sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>"> 
			Giorni
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSospensioneGGSS()), "")%>" title="Numero Giorni sospensione" type="text" size="4" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>">
		</td>
	</tr>
	<tr id="ricovero" style="display: none;">
		<td class="l">Luogo Ricovero</td>
		<td class="L" colspan="2">
			<textarea title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" cols="80" rows="4"><%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getLuogoSvolgimentoProva())%></textarea>
		</td>
	</tr>
<%
	} else if (Utils.isPresent(lIdOrdinanza)) {
%>
	<tr>
<%
				if(datiOrdinanza.getOrdinanza().getCodTipoOrdinanza() != null && 
				   (datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().equals("42")
						   || datiOrdinanza.getOrdinanza().getCodTipoOrdinanza().equals("MS"))) {
%>
		<td class="l">Data Decorrenza Differimento Esecuzione</td>
<%
				} else {
%>     			
		<td class="l">Data Differimento Esecuzione</td>
<%
				}
%>
		<td class="L" colspan="2">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	<tr>
		<td class="l">Rinvio fino al</td>
		<td class="L">
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(), "dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(), "MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
		<td class="l">oppure Rinvio nella misura di: 
			Anni
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getSospensioneAASS()), "")%>" title="Numero Anni sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>"> 
			Mesi
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getSospensioneMMSS()), "")%>" title="Numero Mesi sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>"> 
			Giorni
			<input value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getSospensioneGGSS()), "")%>" title="Numero Giorni sospensione" type="text" size="4" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>">
		</td>
	</tr>
	<tr id="ricovero" style="display: none;">
		<td class="l">Luogo Ricovero</td>
		<td class="L" colspan="2">
				<TEXTAREA title="Note" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" cols="80" rows="4"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva())%></textarea>
		</td>
	</tr>
<%
	}
%>
</table>
<%
}
%>
<%-- FINE MEV_39 --%>

<%
// MEV_2023-35: aggiunta gestione Ordinanza Reclamo Avverso Revoca Pena Sostitutiva (C063)
if (tipo.equals(ICostantiDepositoOrdinanzaPc.RECLAMO_AVVERSO_REVOCA_PENA_SOSTITUTIVA)) {
%>
<div id="idDivReclamoAvversoRevocaPenaSostitutiva" style="display:none;"> 
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="30%"><font class="label">Pena Sostitutiva piu' grave</font></td>  
        <td class="l">          
          	<select title="Tipo Pena Sostitutiva" name="<%=ICostantiEsecuzioneSS.CAMPO_COD_TIPO_SANZIONE%>">
          		<option value="-">-</option>
          		<%=tipoPeneSostitutive%>
          	</select>
		</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Rideterminazione Quantum Pena Da Espiare</font></td>
		<td class="l">
			<font class="label">Anni</font>&nbsp;
			<input type="text" title="Anni" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_ANNI_SANZIONE%>" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(esecuzioneSanzioneSostitutivaModel.getNumAnniSanzione()), "")%>" ONKEYPRESS="return TicTabNumField(this,event)">
			<font class="label">Mesi</font>&nbsp;
			<input type="text" title="Mesi" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_MESI_SANZIONE%>" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(esecuzioneSanzioneSostitutivaModel.getNumMesiSanzione()), "")%>" ONKEYPRESS="return TicTabNumField(this,event)">
			<font class="label">Giorni</font>&nbsp;
			<input type="text" title="Giorni" size="4" maxlength="2" name="<%=ICostantiEsecuzioneSS.CAMPO_NUM_GIORNI_SANZIONE%>" value="<%=StringUtils.toZerotoStringaVuota(StringUtils.toStringJSP(esecuzioneSanzioneSostitutivaModel.getNumGiorniSanzione()), "")%>" ONKEYPRESS="return TicTabNumField(this,event)">
		</td>
	</tr>
</table>
</div>
<%
}
%>

<br>
<table>
	<tr>
		<td class="label">
			<input class="bottone" type="submit" name="Conferma" value="Conferma">
		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=lIdOrdinanza%>">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lIdEvento%>">
<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=lIdDecreto%>">
<input type="HIDDEN" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>" value="">
<%-- MEV_2023-35: aggiunto campo nascosto --%>
<%
String lTipoOrdinanza = "";
if (datiOrdinanza!=null && datiOrdinanza.getOrdinanza()!=null && datiOrdinanza.getOrdinanza().getCodTipoOrdinanza()!=null)
  lTipoOrdinanza = datiOrdinanza.getOrdinanza().getCodTipoOrdinanza();
%>
<input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=lTipoOrdinanza%>">
</form>

<script language="JavaScript" type="text/javascript">
<%-- MERGE v10: aggiunta chiamata a nuova funzione js --%>
abilitaModificaEsito();
<%-- MEV_39: aggiunta chiamata a nuova funzione js --%>
AbilitaCampiEsiti();

var frmvalidator  = new Validator("ModificaOrdinanza");

frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno  della Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese  della Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=13");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno della Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

// Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>