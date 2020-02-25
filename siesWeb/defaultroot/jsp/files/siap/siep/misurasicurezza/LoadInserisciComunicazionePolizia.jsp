<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel"%>

<jsp:useBean id="dataeditabile" scope="request" class="java.lang.String" />
<jsp:useBean id="StrdataInizioPena" scope="request" class="java.lang.String" />
<jsp:useBean id="StrdataFinePenaA" scope="request" class="java.lang.String" />
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="lSogg" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="ListaOrd" scope="request" class="java.util.Vector" />
<jsp:useBean id="avvocati" scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" />
<jsp:useBean id="autoritaEsternaE" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String" />
<jsp:useBean id="MisuraModel" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" />
<jsp:useBean id="tipoUDS"                   scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"                 scope="request" class="java.lang.String"/>
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<% FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

	UtenteModel lUtenteMod = new UtenteModel( (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
	
	MisuraSicurezzaModel lMis=null;
	
	String Desc="";
	if(posizioneluogoaltra!=null && posizioneluogoaltra.getLuogoDetenzione()!=null &&
		posizioneluogoaltra.getLuogoDetenzione().getIdLuogoDetenzione()!=null && 
		posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()!=null)
	{
		Desc= posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto();
		Desc+=" di ";
		Desc+=posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione();
		if(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo()!=null)
		{	
			Desc+=" - ";
			Desc+=posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getIndirizzo();
		}	
	}
%>
<!-- LoadInserisciComunicazionePolizia -->
<html>
<head>
<title>[S.I.E.S.] -Gestione Misure sicurezza- Comunicazione alle Forze di Polizia</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	
    	var desktop;
 
    	// Lista dei COMUNI
    	function ListaComuni(a_formname,a_fieldname)
        {
            desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	 
    	// Blocco combo Aut.Est.
    	function bloccaUNEP()
    	{
   	      <% if (avvocati.size()==1) {%>
				document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
    	      <% } else { %>
    	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
  				document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
    	      }
   	     <% } %>
    	}
    	
    	// Lista dei MAGISTRATI
    	function ListaMagistrati(a_formname)
    	{
    	      var desktop;
    	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    	}
    	
    	// Lista Uffici per Destinatari della Sorveglianza
        function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
        {
            desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	
    	// lista Istituti di detenzione
        function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
        }
        
        function pulisciIstituto (nomeCampoComune, nomeCampoId)
        {
            var campoDescr = document.getElementsByName(nomeCampoComune)[0];
            var campoId    = document.getElementsByName(nomeCampoId)[0];
            campoDescr.value="";
            campoId.value="";
         }
    	
    	//Funzione utile per impostare la data corrente.
    	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
    		day=dataOdierna.substring(0,2);
    		month=dataOdierna.substring(3,5);
    		year=dataOdierna.substring(6,10);
    	    document.getElementsByName(campo_giorno).item(0).value = day;
    	    document.getElementsByName(campo_mese).item(0).value = month;
    	    document.getElementsByName(campo_anno).item(0).value = year;      
    	}
    	
    	function Difensore()
        {
    		var nodequan = document.getElementById("divq");
    		
            if( document.LoadInsComunicaPol.Difesa.checked )
         	{ 
           		nodequan.style.display='block';
           		
       	        <% if (avvocati.size()==1) {%>
            		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
           			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
               		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
               		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
          	    <% } else { %>
        	      	for (var i=0; i< <%=avvocati.size()%>; i++){ 
	            		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
   	   	     <% } %>
       	  	}
            else
            {
            	nodequan.style.display='none';
            	
       	        <% if (avvocati.size()==1) {%>
	        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
        			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
            		//document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
          	    <% } else { %>
   	      			for (var i=0; i< <%=avvocati.size()%>; i++){ 
		        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            		//document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=true;
					}
   	   	     <% } %>
            }
        }
   		
		// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
    	function AutEsterna()
        {
    		var nodeAut = document.getElementById("divae");
    		
            if( document.LoadInsComunicaPol.SiNoTe.checked )
         	{ 
           		nodeAut.style.display='none';
           		
       	        <% if (avvocati.size()==1) {%>
					document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	           		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.value='';
	           		//document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
          	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		           			//document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value='';
						}
   	   	     	<% } %>
       	  	}
            else
            {
            	nodeAut.style.display='block';
            	

       	        <% if (avvocati.size()==1) {%>
	        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
        			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
            		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
            		document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
           	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        			document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
		        			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.LoadInsComunicaPol.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	     	<% } %>
            }	
        }

    	function Verify()
  		{
  			var Misu ='<%=ListaOrd.size()%>';
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
  			// CONTROLLO DATA TRASMISSIONE
		  	if (document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		  	if (document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

		  	var data_to_verify = document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data Trasmissione NON Valida');
        		document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Trasmissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Trasmissione non può essere superiore alla data odierna!');
		      document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
		      return false;
		    }
		    
		    // CONTROLLO DATA EMISSIONE
		  	if (document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data di emissione non valida');
        		document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.LoadInsComunicaPol.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

			<%if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
					&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) 
			{
				if (dataeditabile.equals("S")
					&& penaresidua.getDataFinePresunta() != null)
				{%>
					      if (document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
							  document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
						  if (document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
							  document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
				
						  var data_to_verifica = document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		
				      	  if (!ControllaData(data_to_verifica) )
						  {
			          			alert('Data fine pena non valida');
			          			document.LoadInsComunicaPol.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
					 			return false;
				  		  }
		<%		}
			}	%>
			
				// Controllo su AUTORITA per esecuzione	
				if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value == "-" 
					&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" )
				{
						alert('ERRORE : Inserire almeno un Destinatario');
	          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			 			return false;
				}
				else
				{
						if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" 
							&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
						{
								alert('ERRORE : Inserire la descrizione SEDE ');
			          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
					 			return false;		
						}
						else
						{	
								if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value != "-" 
									&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>.value == "" )
								{
										alert('ERRORE : Inserire la descrizione SEDE ');
					          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>.focus();
							 			return false;		
								}
						}	
				}
				
				// Controlli su Notifica al Difensore
				if( document.LoadInsComunicaPol.Difesa.checked )
				{
	       	        <% if (avvocati.size()==1) {%>
							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" 
							&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
							{
								alert('Attenzione : Inserire Dati Destinatario notifica Difensore ');
			          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
					 			return false;		
							}

							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1
								&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
							{
								alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
					 			return false;		
							}

							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
								&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" )
							{
								alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
					          	document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
							 	return false;		
							}
							
		       	   <% } else { %>
			    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" 
								&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
							{
								alert('Attenzione : Inserire Dati Destinatario notifica Difensore '+i);
			          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
					 			return false;		
							}

							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1
							&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
							{
								alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			          			document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
					 			return false;		
							}

							if(document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
								&& document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" )
							{
								alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
					          	document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
							 	return false;		
							}
						
						}
				<%	} %>
				}

				// Per Tipo Autorità S.N.T. si imposta la sede Autorità a "-".
				if( document.LoadInsComunicaPol.SiNoTe.checked ) {
	       	        <% if (avvocati.size()==1) {%>
							document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
			        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
			        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
							var codAut=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
							var indAut=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
	       	   <% } else { %>
			    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
			        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
			        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
							var codAut2=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
							var indAut2=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
						}
				<%	} %>
				}
				else
				{
					if( document.LoadInsComunicaPol.Difesa.checked ) {
		       	        <% if (avvocati.size()==1) {%>
				        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
								var codAut=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
								var indAut=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
		       	   <% } else { %>
				    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
				        		document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
								var codAut2=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
								var indAut2=document.LoadInsComunicaPol.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
							}
					<%	} %>
					}
				}
				
				<%// Controllo Destinatario Sorveglianza%>
				if(document.LoadInsComunicaPol.tipoUDS.value != "-")
				{
					if(document.LoadInsComunicaPol.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
					{
						alert(' Inserire Sede Destinatario Sorveglianza');
						document.LoadInsComunicaPol.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
						return false;
					}	
				}
					
    }	<%// Chiude function Verify%>
    
 </script>
</head>
<body class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp; <font class="campo">Comunicazione per esecuzione Misure di Sicurezza</font></td>
		</tr>
	</table>
	<br>
	<jsp:include
		page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
	<br>
	<FORM method="POST" name="LoadInsComunicaPol" action="<%=IWebConstants.PG_MAIN%>">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciComunicazionePolizia">
		<table>
			<tr>
				<td class="l">Posizione Giuridica</td>
				<td class="L" colspan=5><font class="campo"> <%=posizioneluogoaltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%>	</font></td>
				<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" maxlength="6" size="6">
				<input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>">
			</tr>

			<%
				if (penaresidua.getIdPenaResidua() != null
						&& ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
						&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) {
					if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
					} else {
			%>
			<tr>
				<td class="l">Reclusione</td>
				<td class="l" colspan=2>
					<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
					<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
					<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
				</td>
				<td class="l">Multa</td>
				<td class="l" colspan=2>
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
					<font class="l">Euro</font></td>
			</tr>
			<%
				}
			%>

			<%
				if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
					} else {
			%>
			<tr>
				<td class="l">Arresto</td>
				<td class="l" colspan=2>
					<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
					<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
					<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
				</td>
				<td class="l">Ammenda</td>
				<td class="l" colspan=2>
					<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
					<font class="l">Euro</font></td>
			</tr>
			<%
				}

				} // CHIUDO if(penaresidua...)
			%>
			<tr>
				<%
					if (penaresidua.getDataInizio() != null) {
				%>
				<td class="l">Data Decorrenza Pena</td>
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
				<td class="L"><font class="campo">ERGASTOLO CON
						ISOLAMENTO DIURNO&nbsp;</font></td>
				<%
					}
					}
				%>
				<%
					if ((penaresidua.getFlagErgastolo() == null)
						|| (penaresidua.getFlagErgastolo() != null
						&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
						if (dataeditabile.equals("S")
								&& penaresidua.getDataFinePresunta() != null) {
				%>
				<td class="l">Data Fine Pena</td>
				<td class="L" colspan=2>
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd"))%>"type="text" size="2" maxlength="2"
						name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"MM"))%>"type="text" size="2" maxlength="2"
						name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"yyyy"))%>" type="text" size="4" maxlength="4"
						name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"
						onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"></td>
				<%
					} else if (penaresidua.getDataFine() != null) {
							if (penaresidua.getDataFine().equals(
									penaresidua.getDataFinePresunta())) {
				%>
				<td class="l">Data Fine Pena</td>
				<td class="L" colspan=2><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd"))%>-
															 <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"MM"))%>-
															 <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy"))%></font>
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),	"MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"> 
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),	"yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"></td>
				<%
					} else {
				%>
				<td class="l">Data Fine Pena</td>
				<td class="lRosso" colspan=2><font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd"))%>-
																   <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"MM"))%>-
																   <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy"))%></font>
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>">
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"> 
					<input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"></td>

				<%
					}
					}
				}
				%>
			</tr>
		</table>

		<!-- Misure di Sicurezza già presenti -->

		<table>
	<%	List lMisure =(List) request.getAttribute("listaMisure");
		if(lMisure.size() > 0)
		{
	    	Iterator itx = lMisure.iterator();
	    	while ( itx.hasNext())
	    	{
		       lMis = (MisuraSicurezzaModel)itx.next();
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
	<%		}
		}	%>		
		</table>

		<!-- Date del Provvedimento -->
		<br>
		<table>
			<tr>
				<td class="l">Data Emissione <font class="ob">(*)</font></td>
				<td class="L" colspan=2>
					<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
				        <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
				          <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
				        </a>
				</td>
				<td class="l">Data Trasmissione <font class="ob">(*)</font></td>
				<td class="L" colspan=2>
					<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
					<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				</td>
			</tr>
			<tr>
				<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>">
				<input type="HIDDEN" title="Id Misura" value="<%=StringUtils.toStringJSP(lMis.getIdMisuraSicurezza())%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>">
			</tr>
		</table>
		
		<table>	

			<!--Magistrato Firmatario -->
			<tr>
				<td class="Titolo" colspan=6>Magistrato Firmatario</td>
			</tr>
			<tr>
				<td class="L">Magistrato Firmatario <font class=ob>(*)</font>
				</td>
				<td class="L" colspan="3">
					<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>"	maxlength="35" size="35"> 
					<input readonly title="Nome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="35"> 
						<a	href="Javascript:ListaMagistrati('LoadInsComunicaPol');"> <img src="/images/filefolder.gif" border=0> </a>
				</td>
			</tr>
			<tr>
				<td>
					<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato())%>" type="text" name="<%=ICostantiEvento.CAMPO_COD_MAGISTRATO%>"	maxlength="35" size="35">
				</td>
			</tr>

								<!--Destinatari-->
			<tr>
				<td class="Titolo" colspan=6>Destinatari</td>
			</tr>
			
		<!--Eventuale Istituto di detenzione-->
		<%if(posizioneluogoaltra!=null && posizioneluogoaltra.getLuogoDetenzione()!=null &&
			posizioneluogoaltra.getLuogoDetenzione().getIdLuogoDetenzione()!=null && 
			posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()!=null)
		{ %>	

		    <tr>
		    	<td class="l" width="20%">Istituto di Detenzione</td> 
		     	<td class="l" colspan="3">
		      		<input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
		      		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=90>
		      			<a href="Javascript:ListaIstitutoDetenzione('LoadInsComunicaPol','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		        			<img src="/images/filefolder.gif" border=0></a>
		      			<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		      				<img src="/images/delete.gif" border=0></a>
		    	</td>
		    </tr>
  		
	<%	} %>	
	
<!-- MEV 39 VISUALIZZAZIONE Struttura Designata-->
	<tr>	
		<td class="L">Struttura Designata</td>
	<%
	if (strutturaDesignataModel != null && Utils.isPresent(strutturaDesignataModel.getIdIstitutoDetenzione())) {
	%>
				<td class="l" colspan="3">
					<input readonly title="Istituto" name="ComuneStruttura" value="<%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(strutturaDesignataModel.getDescrizione())%> - <%=StringUtils.toStringJSP(strutturaDesignataModel.getIndirizzo())%>" size="100">

				</td>
	<%
	} else {
	%>
			<td class="l" colspan="3">
		      	<input readonly title="Istituto" name="ComuneStruttura" value="" size="100">		      
		    </td>
	<%
		} 
	%>
	</tr>
	
	<!--Notifica per Altra Autorità 		PRIMA	-->
			<tr>
				<td class="L">Altra Autorità</td>
				<td class="L">
					<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"> <%=autoritaEsternaE%> </select>
				</td>
				<td class="l">Note</td>
				<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=40></textarea></td>
			</tr>

			<tr>
				<td class="l">Sede</td>
				<td class="L"><input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
					<a href="Javascript:ListaComuni('LoadInsComunicaPol','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
						<img src="/images/filefolder.gif" border=0>
					</a>
				</td>
			</tr>
			<!-- 			SECONDA		-->
			<tr>
				<td class="L">Altra Autorità</td>
				<td class="L">
					<select Title="Autorita Esterna 1" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>"> <%=autoritaEsternaE%> </select>
				</td>
				<td class="l">Note</td>
				<td class="L"><TEXTAREA title="Note 1" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>" cols=40></textarea></td>
			</tr>

			<tr>
				<td class="l">Sede</td>
				<td class="L"><input title="Sede Autorita Esterna 1" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>" size="35"> 
					<a href="Javascript:ListaComuni('LoadInsComunicaPol','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
						<img src="/images/filefolder.gif" border=0>
					</a>
				</td>
			</tr>
			
			<!--Destinatario per l'Ente di Sorveglianza (Revisione del 24/10/2014)-->
		  <tr>
			<td class="L">Magistrato di Sorveglianza </td>
		    <td class="L">
		      <select Title="Magistrato di Sorveglianza" name="tipoUDS" >
		      <%=tipoUDS%>
		      </select>
		   	</td>
		   	<td class="L" COLSPAN=2>
		      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
		      <a href="Javascript:ListaUfficiComuni('LoadInsComunicaPol','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
		                                           ,document.LoadInsComunicaPol.tipoUDS[document.LoadInsComunicaPol.tipoUDS.options.selectedIndex].value);">
		         <img src="/images/filefolder.gif" border=0>
		      </a> 
		    </td>
		  </tr> 
		</table>
		
		<!--Notifica per Difensore e condannato  -->

		<table width="90%">
			<tr>
			<%	if(avvocati.size() > 0)
				{	%>
					<td class="l" colspan=3><input type="checkbox" name="Difesa" onclick="Javascript:Difensore();">Notifiche Atti (Difensore - Condannato) &nbsp;&nbsp;</td>
			<%	}
				else
				{	%>
					<td class="l" colspan=3 style="color:red"><input type="checkbox" name="Difesa" disabled >Notifiche Atti (Difensore - Condannato) Procedimento privo di Avvocato &nbsp;&nbsp;</td>
			<%	} %>			
				<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiMisuraSicurezza.CAMPO_FLAG_ESECUZIONE_IMMEDIATA %>" value="D" >Disposta l'esecuzione Immediata &nbsp;&nbsp;</td>
			</tr>
		</table>

		<div id="divq" style="width: 90%; display: none; position: relative;">
			<table width="90%">
				<tr>
					<td class="Titolo" colspan=6>Destinatario per Notifica</td>
				</tr>
				<tr>
					<td class="l" colspan=3><input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();"> Notifica ai sensi dell'art. 148 comma 2 bis c.p.p. &nbsp;&nbsp;</td>
				</tr>
			</table>
			<div id="divae" style="width: 90%; display: block; position: relative;">
			<%
				int lIdxAvv = 0;
				Iterator lItxAvv = avvocati.iterator();
				while (lItxAvv.hasNext()) {
					AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
			%>
					<table width="90%">
						<tr>
							<td class="l" width="20%">Autorità Destinazione <font class=ob>(*)</font></td>
							<td class="L" colspan="3">
							<select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();">
									<%=autoritaEsterna%>
							</select></td>
						</tr>
						<tr>
							<td class="l">Sede <font class=ob>(*)</font></td>
							
							<td class="L">
								<input title="Sede Autorita per Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
				       <%	if(avvocati.size()>1)
        					{	%>   				 
								<a href="Javascript:ListaComuni('LoadInsComunicaPol','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
									<img src="/images/filefolder.gif" border=0>
								</a>
						<%	}
				       		else
				       		{%>
				       			<a href="Javascript:ListaComuni('LoadInsComunicaPol','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
										<img src="/images/filefolder.gif" border=0>
								</a>
				       	<%	} %>				
							</td>
							<td class="l">Indirizzo</td>
							<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=30></textarea></td>
						</tr>

						<tr>
							<td class="l" width="20%">Per Avvocato</td>
							<input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
							<td><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
								<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
								<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
								</font>
							</td>
							<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
						</tr>

					<%
						lIdxAvv++;
					}%>
				</table>
			</div>
		</div>

		<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
		<table>
			<tr>
				<td class="lNoBord" colspan="2"><br>
				<INPUT class="bottone" type="submit" name="I" value="Conferma"
					onClick="javascript:return Verify();"></td>
			</tr>
		</table>

	</form>
	<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInsComunicaPol");

  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2050");

<%	if( ( (penaresidua.getFlagErgastolo() == null) || 
		  (penaresidua.getFlagErgastolo() != null	&& !penaresidua.getFlagErgastolo().equals("S") 
		  && !penaresidua.getFlagErgastolo().equals("D") ) ) ) 
	{
			if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
			{%>
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
			
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
			
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
			  frmvalidator.addValidation("<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%			}
	}%>
		
	</script>
</body>
</html>