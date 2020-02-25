<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutorita" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoArchiviazioni" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="MisuraModel"			scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="avvocati"        		scope="request" class="java.util.Vector" />
<jsp:useBean id="autoritaEsterna" 		scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsternaE" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUDS"         		scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"       		scope="request" class="java.lang.String"/>

<%
//===================================================================================================
//Form per inserimento di Archiviazione per Provvedimento di Altro Ufficio (appl. Misure Sicurezza) 
//====================================================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  

  SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
   
  // Misure Sicurezza
  List lMisure =(List) request.getAttribute("listaMisure");
  
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
<!-- 					LoadInserisciArchiviazionePerProvvAltroUfficio 				-->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - -Gestione Misure sicurezza - Archiviazione per Provvedimento di Altro Ufficio </title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
	    	  var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
     //DATA EMISSIONE
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;
	
	        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) )
	        {
	          alert('Data emissione non valida');
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	
	          return false;
	        }
	 //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }
	        
	        
	//DATA RICEZIONE
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;
	
	        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) )
	        {
	          alert('Data ricezione non valida');
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();
	
	          return false;
	        }
	        
	 //2) Controllo : data di sistema deve essere >= Data Ricezione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Ricezione non può essere superiore alla data odierna!');
		      document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>.focus();
		      return false;
		    }
	        
	//DATA DEFINIZIONE
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
	          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;
	
	        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;
	
	        if (!ControllaData(data_to_verify) )
	        {
	          alert('Data definizione non valida');
	          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
	
	          return false;
	        }
	// Oggetto Definizione
	        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
	        {
	          alert("Il Campo Oggetto definizione è obbligatorio");
	          document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.focus();
	          return false;
	        }
// Magistrato	
	        if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
	        {
	          alert("Il Cognome del Magistrato è obbligatorio");
	          document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
	          return false;
	        }
	
	        if(document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
	        {
	          alert("Il Nome del Magistrato è obbligatorio");
	          document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.focus();
	          return false;
	        }
// Autorità	        
	        if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-" &&
	        	document.f.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value == "" )
	        {
	        	alert("Digitare almeno una Autorità Emittente");
	        	document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
	        	return false;
	        }
	        
	        if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value != "-" &&
		       	document.f.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value != "" )
		    {
		       	alert("Digitare soltanto una Autorità Emittente");
		       	document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
		       	return false;
		    }
// Sede / Tipo Autorità	
	        if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value != "-" &&
			   	document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.value == "" )
			{
			   	alert("Digitare Sede Autorità Emittente");
			   	document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
			   	return false;
			}
			
	        if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-" &&
	           document.f.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value == "" &&	
			   document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.value != "" )
			{
			   	alert("Digitare Tipo Autorità");
			   	document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
			   	return false;
			}
	        
	// Controlli su Notifica al Difensore
			if( document.f.Difesa.checked )
			{
			       <% if (avvocati.size()==1) {%>
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" 
						&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
						{
							alert('Attenzione : Inserire Dati Destinatario notifica Difensore ');
			      			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
				 			return false;		
						}
			
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1
							&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
						{
							alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			      			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
				 			return false;		
						}
			
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
							&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" )
						{
							alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
				          	document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
						 	return false;		
						}
						
				   <% } else { %>
				    for (var i=0; i< <%=avvocati.size()%>; i++) { 
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" 
							&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
						{
							alert('Attenzione : Inserire Dati Destinatario notifica Difensore '+i);
			      			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
				 			return false;		
						}
			
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1
						&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
						{
							alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			      			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
			      			bloccaUNEP();
				 			return false;		
						}
			
						if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
							&& document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" )
						{
							alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
				          	document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
						 	return false;		
						}
					
					}
			<%	} %>
			}
			
		// Per Tipo Autorità S.N.T. si imposta la sede Autorità a "-".
			if( document.f.SiNoTe.checked ) {
			       <% if (avvocati.size()==1) {%>
						document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
			    		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
			    		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
						var codAut=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
						var indAut=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
			  <% } else { %>
				    for (var i=0; i< <%=avvocati.size()%>; i++) { 
						document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
			    		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
			    		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
						var codAut2=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
						var indAut2=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
					}
			<%	} %>
			}
			else
			{
				if( document.f.Difesa.checked ) {
				        <% if (avvocati.size()==1) {%>
			        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
							var codAut=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
							var indAut=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
				   <% } else { %>
			    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
			        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
							var codAut2=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
							var indAut2=document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
						}
				<%	} %>
				}
			}
			
		<%// Controllo Destinatario Sorveglianza%>
			if(document.f.tipoUDS.value != "-")
			{
				if(document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
				{
					alert(' Inserire Sede Destinatario Sorveglianza');
					document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
					return false;
				}	
			}
			else
			{
				if(document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value != "")
				{
					alert(' Inserire Tipo Destinatario Sorveglianza');
					document.f.tipoUDS.focus();
					return false;
				}			
			}	
			
		// Controllo su AUTORITA (destinatari)
			if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" )
			{
				if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
				{
					alert('ERRORE : Inserire Sede Autorità Destinatario');
					document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
					return false;
				}	
			}
		
			if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "" )
			{
				if(document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" )
				{
					alert('ERRORE : Inserire Tipo Autorità Destinatario');
					document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
					return false;
				}	
			}
			
	        return true;
   
      }
// End Verify

//Funzione utile per impostare la data corrente.
  function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
	day=dataOdierna.substring(0,2);
	month=dataOdierna.substring(3,5);
	year=dataOdierna.substring(6,10);
    document.getElementsByName(campo_giorno).item(0).value = day;
    document.getElementsByName(campo_mese).item(0).value = month;
    document.getElementsByName(campo_anno).item(0).value = year;      
  }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }
      
	  function ListaComuni(a_formname,a_fieldname)
      {
        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
	  
	  function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
	  {
		  var desktop;
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	  }
	  
      function Difensore()
      {
  		var nodequan = document.getElementById("divq");
  		
          if( document.f.Difesa.checked )
       	  { 
         		nodequan.style.display='block';
         		
     	        <% if (avvocati.size()==1) {%>
          		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
         			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
             		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
             		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
        	    <% } else { %>
      	      	for (var i=0; i< <%=avvocati.size()%>; i++){ 
	            		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
 	   	     <% } %>
     	  }
          else
          {
          	nodequan.style.display='none';
          	
     	        <% if (avvocati.size()==1) {%>
	        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
      				document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
        	    <% } else { %>
 	      			for (var i=0; i< <%=avvocati.size()%>; i++){ 
		        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
					}
 	   	     <% } %>
          }
          
      } // End Difensore()
      
		// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
  	function AutEsterna()
    {
  		var nodeAut = document.getElementById("divae");
  		
          if( document.f.SiNoTe.checked )
       	{ 
         		nodeAut.style.display='none';
         		
     	        <% if (avvocati.size()==1) {%>
					document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	           		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.value='';
	           		//document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
        	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		           			//document.f.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value='';
						}
 	   	     	<% } %>
     	  	}
          else
          {
          	nodeAut.style.display='block';

			<% if (avvocati.size()==1) {%>
	        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	        		document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
      				document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
          			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
          			document.f.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
            <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        			document.f.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
		        			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.f.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	     	<% } %>
          }
          
      } // End AutEsterna()
      
    	// Blocco combo Aut.Est.
    	function bloccaUNEP()
    	{
    	      <% if (avvocati.size()==1) {%>
    			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
    	      <% } else { %>
    	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
    			document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
    	      }
    	     <% } %>
    	}
      
        function pulisciIstituto (nomeCampoComune, nomeCampoId)
        {
            var campoDescr = document.getElementsByName(nomeCampoComune)[0];
            var campoId    = document.getElementsByName(nomeCampoId)[0];
            campoDescr.value="";
            campoId.value="";
        }
        
    	// lista Istituti di detenzione
	    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
	    {
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	    }
 
 </script>
 
  </head>
  <body class="corpo" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Archiviazione per Provvedimento di Altro Ufficio</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciArchiviazionePerProvvAltroUfficio">

    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }

	if(penaresidua != null)
	{	%>
      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l" width="25%">Pena Espiata dal</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
	        if(penaresidua.getFlagErgastolo().equals("S"))
	        {
	%>
	          <td class="l">Pena Detentiva</td>
	          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
	<%
	        }
	        else
	        if(penaresidua.getFlagErgastolo().equals("D"))
	        {
	%>
	          <td class="l">Pena Detentiva</td>
	          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
	<%
	        }
       }

		if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
		{
	  		if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
	  		{
	    		if( penaresidua.getDataFine() != null)
	    		{
	    			if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
	        		{
	%>
			             <td class="l">al</td>
			             <td class="L" colspan=2>
			               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
			            </td>
	<%
	          		}
	          		else
	          		{
	%>
		                <td class="l">al</td>
		                <td class="lRosso" colspan=2>
		                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
		                </td>
	<%         		}
	        	}
	      	}
		}
%>
   </tr>
<%
  	}	// chiude if(penaresidua != null)
%>
  </table>
  <!--    // Eventuali Misura di Sicurezza  -->
<%
	if(lMisure.size() > 0)
	{
%>
	    <table>
<%
	    Iterator itx = lMisure.iterator();
	    while ( itx.hasNext())
	    {
		      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
	%>
		    <tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C> Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C> Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C> Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		    </tr>  		
	<% 
	    } %>
	    
	    </table>
<%	}%> 
  <br>

  <!--  Dati Altra Autorità  -->  
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Autorità emittente Nota/Comunicazione</td>
    </tr>
    <tr>
      <td class="l" width="25%">Numero Protocollo Nota</td>
      <td class="l" colspan="3">
         <input Title="Numero Nota" value="" name="<%=ICostantiArchiviazione.CAMPO_NUM_NOTA%>" type="text" size="35" maxlength="35">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Emissione  <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Ricezione  <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	  <a href="Javascript:impostaDataOdierna('<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>','<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE %>','<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        	 <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
      	  </a>
      </td>
    </tr>
</table>
   <table width="100%">  
    <tr>
<!--autorità di polizia-->
      <td class="l" width="25%">Autorità che ha inviato la nota <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <select  Title="Autorita"  name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
         <%=codiceAutorita%>
         </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
     <td class="L">
          <input title="Sede Autorita"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Indirizzo" name="<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>"  cols=40 ></textarea>
            </td>
    </tr>
    <tr>
      <td class="l" width="25%">Altra Autorità</td>
      <td class="l" colspan="3">
         <input Title="Altra Autorita" name="<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA%>" type="text" size="35" style="width:400px">
      </td>
    </tr>
   </table>
   
     <!--  Definizione Procedimento -->   
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" width="25%">Data Definizione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	  <a href="Javascript:impostaDataOdierna('<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        	 <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
      	  </a>
      </td>
    </tr>
    <tr>
      <td class="l" width="25%">Oggetto Definizione <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Oggetto Definzione" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" >
            <%=tipoArchiviazioni%>
          </select>
        </td>
    </tr>
    <tr>
     	<td class="L" width="20%">Eventuali Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=2 ></textarea>
      	</td>
     </tr>
  </table>
  
<!--  Magistrato Firmatario -->
  <table width="90%">
    <tr>
      <td colspan=4 class="titolo">Magistrato Firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario</td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>

     <tr><td class="Titolo" colspan=6> Destinatari </td></tr>
	<!--Eventuale Istituto di detenzione-->
<%	if(posizioneluogoaltra!=null && posizioneluogoaltra.getLuogoDetenzione()!=null &&
		posizioneluogoaltra.getLuogoDetenzione().getIdLuogoDetenzione()!=null && 
		posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()!=null)
	{ %>	
 	     <tr>
		  <td class="l" width="30%">Struttura Designata </td> 
		  <td class="l" colspan="3">
		  	<input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
		  	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=90>
		  		<a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		       		<img src="/images/filefolder.gif" border=0></a>
		      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		      		<img src="/images/delete.gif" border=0></a>
		  </td>
		 </tr>
<%	} 
	else
	{%>
 	     <tr>
		  <td class="l" width="30%">Struttura Designata </td> 
		  <td class="l" colspan="3">
		  	<input readonly  Title="Istituto" name="Comune" value="" size=90>
		  	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=90>
		  		<a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		       		<img src="/images/filefolder.gif" border=0></a>
		      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		      		<img src="/images/delete.gif" border=0></a>
		  </td>
		 </tr>
<%	} %>
		 
	<!--Notifica per Altra Autorità 	-->
		<tr>
			<td class="L">Altra Autorità</td>
			<td class="L">
				<select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"> 
				<%=autoritaEsternaE%>
				 </select>
			</td>
			<td class="l">Note</td>
			<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=40></textarea></td>
		</tr>

		<tr>
			<td class="l">Sede</td>
			<td class="L"><input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
				<a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
				<img src="/images/filefolder.gif" border=0>
				</a>
			</td>
		</tr>
		
	<!--Destinatario per l'Ente di Sorveglianza per esecuzione (Revisione del 24/10/2014)-->
	<tr>
		<td class="L">Magistrato di Sorveglianza </td>
	    <td class="L">
	      <select Title="Magistrato di Sorveglianza" name="tipoUDS" >
	      <%=tipoUDS%>
	      </select>
	   	</td>
	   	<td class="L" COLSPAN=2>
	      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
	      <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
	                                           ,document.f.tipoUDS[document.f.tipoUDS.options.selectedIndex].value);">
	         <img src="/images/filefolder.gif" border=0>
	      </a> 
	    </td>
  	</tr>    	     

 </table>

<table width="90%">
  <tr>
  <% if (avvocati.size()>0 ) {%>
    <td class="l" colspan=3><input type="checkbox" name="Difesa" onclick="Javascript:Difensore();">Notifiche Atti (Difensore - Condannato) &nbsp;&nbsp;</td>
<%	} else { %>
	<td class="l" colspan=3 style="color:red"><input type="checkbox" name="Difesa" disabled >Notifiche Atti (Difensore - Condannato) Procedimento privo di Avvocato &nbsp;&nbsp;</td>
<%	} %>     
  </tr>
</table>

<div id="divq" style="display: none; position: relative; width: 90%;">
  <table width="90%">
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica</td>
    </tr>
    <tr>
      <td class="l" colspan=3><input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();"> Notifica ai sensi dell'art. 148 comma 2 bis c.p.p. &nbsp;&nbsp;</td>
    </tr>
  </table>
  <div id="divae" style="display: block; position: relative; width: 90%;">
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
          <select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();" >
              <%=autoritaEsterna%>
          </select></td>
        </tr>
        <tr>
          <td class="l">Sede <font class=ob>(*)</font></td>
          
          <td class="L">
            <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
       <%	if(avvocati.size()>1)
			{	%>             
              <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
                <img src="/images/filefolder.gif" border=0>
              </a>
      <%	}
       		else
       		{	%>
              <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
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
          <td class="l"><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
              <font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
              <font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%></font>
          </td>
          <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
        </tr>

      <%
        lIdxAvv++;
      }%>
    </table>
  </div>

  </table>
</div>

<table width="100%">   
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


//data emissione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno data Emissione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese data Emissione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno data Emissione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

//data ricezione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","req","Il campo Giorno data Ricezione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","req","Il campo Mese data Ricezione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","req","Il campo Anno data Ricezione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","lt=2050");

//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno data Definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese data Definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno data Definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno data Definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno data Definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>