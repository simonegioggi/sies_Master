<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="avvocati" 				scope="request" class="java.util.Vector" />
<jsp:useBean id="archiviazione"      	scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="tipoArchiviazioni"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUDS"               scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"	 		scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsterna" 		scope="request" class="java.lang.String" />
<jsp:useBean id="NotificaAvvocati" 		scope="request" class="java.lang.String" />
<jsp:useBean id="magistrato"			scope="request" class="siap.sico.magistrato.model.MagistratoModel" />

<% FascicoloSiepModel fascicolo = (FascicoloSiepModel) session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel( (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
	
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
	
// Gestione Autorità Esterna sulle Notifiche  
	String lCodTipoAutorita1 = "-";
	NotificaModel lPrimaNotifica = null;
	AutoritaEsternaModel lPrimaAutoritaEsterna = null;
	String lnote =""; 
	
	String lnoteGE ="";
	IstitutoDetenzioneModel lIstMod = null;
	String lIdIstDete =null;
	
	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			if(eventonotifica.getNotifiche()[i] != null &&
				eventonotifica.getNotifiche()[i].getAutoritaEsterna() != null &&
			   ("AA").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{
					lCodTipoAutorita1 = eventonotifica.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
		    		lPrimaNotifica = (NotificaModel) eventonotifica.getNotifiche()[i];
	      			lPrimaAutoritaEsterna = (AutoritaEsternaModel) lPrimaNotifica.getAutoritaEsterna();
	      			if(lPrimaNotifica!=null && lPrimaNotifica.getIdNotifica()!=null && lPrimaNotifica.getNote()!=null )
					{ 
						lnote=lPrimaNotifica.getNote().toString().trim();
					}	
			}
			
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("E").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{
				lIstMod = (IstitutoDetenzioneModel)eventonotifica.getNotifiche()[i].getIstitutoDetenzione();
				lIdIstDete = eventonotifica.getNotifiche()[i].getIstDetIdIstitutoDetenzione();
			}
			
		}	
	}
	
	String Desc="";
	if(lIstMod != null && lIstMod.getIdIstitutoDetenzione()!=null)
	{
		Desc= lIstMod.getDescrTipoIstituto();
		Desc+=" di ";
		Desc+=lIstMod.getDescrizione();
		if( lIstMod.getIndirizzo()!=null && !lIstMod.getIndirizzo().equals("") )
		{
			Desc+=" - ";
			Desc+=lIstMod.getIndirizzo();
		}	
	}

%>
<!--	 LoadModificaArchiviazionePerProvvAltroUfficioMS	 -->
<html>
<head>
<title>[S.I.E.S.] -Gestione Misure sicurezza - Definizione Mis Sic</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	
    	var desktop;
    	var NotificaAvvocati = '<%=NotificaAvvocati%>';

    //	var lnote = '< %=lnote%>';
    	
    	// Lista dei COMUNI
    	function ListaComuni(a_formname,a_fieldname)
        {
            desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	
    	// Lista dei MAGISTRATI
    	function ListaMagistrati(a_formname)
    	{
    	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    	}
    	 
    	// Blocco combo Aut.Est.
    	function bloccaUNEP()
    	{
   	      <% if (avvocati.size()==1) {%>
				document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
    	      <% } else { %>
    	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
  				document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
    	      }
   	     <% } %>
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
        
        function pulisciAutorita (nomeTipo,nomeComune,note)
        {
        	var campoTipoAut = document.getElementsByName(nomeTipo)[0];
            var campoSedeAut = document.getElementsByName(nomeComune)[0];
            campoTipoAut.value="-";
            campoSedeAut.value="";
            
            if(note=='0')
            	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>.value="";
           	else if(note=='1')
            	document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE_E%>.value = "";

        }
        
        function pulisciUDS (TipoUfficio, sedeUfficio)
        {
            var campoDescrTipo = document.getElementsByName(TipoUfficio)[0];
            var campoDescrSede = document.getElementsByName(sedeUfficio)[0];
            campoDescrTipo.value="-";
            campoDescrSede.value="";
         }
    	
        function VediNotificaAvvocati()
        {
 			//alert("1");
        	if(NotificaAvvocati == "-")
        	{
        		document.ModificaArchAltUffiMS.Difesa.checked = false;
        	}
        	else
        	{
        		document.ModificaArchAltUffiMS.Difesa.checked = true;
        		if(NotificaAvvocati == "C0")
        		{
        			document.ModificaArchAltUffiMS.SiNoTe.checked = true;
        		}
        		else
        		{
        			document.ModificaArchAltUffiMS.SiNoTe.checked = false;
        		}
        		
        		Difensore();
        		AutEsterna();
        	}
     	
        }
        
    	function Difensore()
        {
    		var numavvocati = <%=avvocati.size()%>;
    		var nodequan = document.getElementById("divq");
            if( document.ModificaArchAltUffiMS.Difesa.checked )
         	{ 
           		nodequan.style.display='block';
           		
       	         if(numavvocati==1) 
       	         { 
            		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
           			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
               		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
               		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
          	     } 
       	         else
       	         { 
        	      	for (var i=0; i< numavvocati; i++)
        	      	{ 
	            		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
   	   	      	 } 
       	  	}
            else
            {
            	nodequan.style.display='none';
            	
       	         if (numavvocati==1) 
       	         { 
	        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
        			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
          	     }
       	         else 
       	         { 
   	      			for (var i=0; i< numavvocati; i++)
   	      			{ 
		        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
					}
   	   	      	} 
            }
        }
   		
		// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
    	function AutEsterna()
        {
    		var nodeAut = document.getElementById("divae");
    		
            if( document.ModificaArchAltUffiMS.SiNoTe.checked )
         	{ 
           		nodeAut.style.display='none';
           		
       	        <% if (avvocati.size()==1) {%>
					document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
	           		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
          	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "";
							document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
						}
   	   	     	<% } %>
       	  	}
            else
            {
            	nodeAut.style.display='block';

       	        <% if (avvocati.size()==1) {%>
	        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
        			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
            		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
            		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
           	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	     	<% } %>
            }	
        }

    	// Caricamento POPUP	
        function ListaProvvGE(a_formname)
	  {
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.archiviazione.action.ActListaDocumentiArchiviazione&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>+", "Lista_Provvedimenti", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
	  }

// --------------------------------------------------------------------------    	
// VERIFICHE ---    	
    	function Verify()
  		{
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
   // CONTROLLO DATA EMISSIONE
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('ERRORE: Data Emissione non valida');
        		document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('ERRORE: Data Emissione non può essere superiore alla data odierna!');
		      document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

 // CONTROLLO DATA RICEZIONE		    
	        if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
	          	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
	        if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
	          	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;
		
	        var data_to_verify = document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;
		
	        if (!ControllaData(data_to_verify) )
	        {
	          	alert('ERRORE: Data Ricezione non valida');
	          	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();
		
	          	return false;
	        }
		        
		 // Controllo : data di sistema deve essere >= Data Ricezione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      	alert('ERRORE: Data Ricezione non può essere superiore alla data odierna!');
		      	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>.focus();
		      	return false;
		    }

// CONTROLLO DATA DEFINIZIONE (Data Archiviazione)
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

		  	var data_to_verify = document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('ERRORE: Data Definizione NON Valida');
        		document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Definizione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('ERRORE: Data Definizione non può essere superiore alla data odierna!');
		      document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
		      return false;
		    }
		  	
 // CONTROLLO DATA TRASMISSIONE
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		  	if (document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

		  	var data_to_verify = document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('ERRORE: Data Trasmissione NON Valida');
        		document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Trasmissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('ERRORE: Data Trasmissione non può essere superiore alla data odierna!');
		      document.ModificaArchAltUffiMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
		      return false;
		    }
		  	
	 // Autorità Emittente	        
	        if(document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-" &&
	        	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value == "" )
	        {
	        	alert("Digitare almeno un valore uno tra \n 'Autorità che ha Inviato la Nota' e 'Altra Autorità'");
	        	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
	        	return false;
	        }
	        
	        if(document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value != "-" &&
		       	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value != "" )
		    {
		       	alert("Digitare soltanto uno tra \n 'Autorità che ha Inviato la Nota' e 'Altra Autorità' ");
		       	document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.focus();
		       	return false;
		    }
		    
	// CONTROLLO AUTORITA' EMITTENTE  	
			if( (document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "" || 
			   	 document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-" )
			   && document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA %>.value == "" )  
			{
				alert('ERRORE: Inserire Tipo Autorità Emittente');
       			document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
	 			return false;
			}
			
			if(document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.value == "" || 
			   document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.value == "-" )
			{
				alert('ERRORE: Inserire Luogo Autorità Emittente');
       			document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
	 			return false;
			}			

	// OGGETTO DEFINIZIONE
			if(document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.value == "-"  || 
				document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.value == "" )
			{
					alert('ERRORE:  Inserire Oggetto Definizione');
					document.ModificaArchAltUffiMS.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.focus();
					return false;
			}
			
			
// AUTORITA' ESTERNA 
			if(document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" 
				&& document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
			{
				alert('ERRORE: Inserire SEDE Autorità esterna ');
       			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
	 			return false;		
			}
						
			if(document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" 
			&& document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "" )
			{
				alert('ERRORE: Inserire TIPO Autorità Esterna');
       			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
	 			return false;				
			}
						
	<%// CONTROLLO DESTINATARIO SORVEGLIANZA %>
			if(document.ModificaArchAltUffiMS.tipoUDS.value != "-")
			{
				if(document.ModificaArchAltUffiMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
				{
					alert('ERRORE: Inserire Sede Destinatario Sorveglianza');
					document.ModificaArchAltUffiMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
					return false;
				}	
			}
			
			if(document.ModificaArchAltUffiMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value != "")
			{
				if(document.ModificaArchAltUffiMS.tipoUDS.value == "-")
				{
					alert('ERRORE: Inserire Tipo Ufficio Destinatario Sorveglianza');
					document.ModificaArchAltUffiMS.tipoUDS.focus();
					return false;
				}
			}
			
// CONTROLLI NOTIFICA AL DIFENSORE
				if( document.ModificaArchAltUffiMS.Difesa.checked )
				{
					if( document.ModificaArchAltUffiMS.SiNoTe.checked )
					{
		       	     <% if(avvocati.size()==1) 
	       	        	{ %>
							document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
			        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
			        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
			        		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	       	   <% 		}
	       	        	else
	       	        	{ %>
			    	    	for (var i=0; i< <%=avvocati.size()%>; i++)
			    	    	{ 
								document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
				        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
				        		document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
				        		document.ModificaArchAltUffiMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
							}
					<%	} %>						
					}
					else
					{	
	       	        <%	if (avvocati.size()==1)
	       	        	{%>
							if(document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value != "" 
							&& document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
							{
								alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			          			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
					 			return false;		
							}
		
		       	   <% 	}
	       	        	else
	       	        	{ %>
			    	    	for (var i=0; i< <%=avvocati.size()%>; i++) 
			    	    	{ 
								if(document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value != "" 
									&& document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
								{
									alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
				          			document.ModificaArchAltUffiMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
						 			return false;			
								}
							}
					<%	} %>
				  	}	
				}
					
    }	<%// Chiude function Verify%>
    
 </script>
</head>
<body class="corpo" onLoad="Javascript:VediNotificaAvvocati();">
 <table>
	<tr>
     <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp; 
	 				<font class="campo">Modifica Archiviazione Misure di Sicurezza su provvedimento di Altro Ufficio</font>
	 </td>
	</tr>
 </table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
	<br>
<FORM method="POST" name="ModificaArchAltUffiMS" action="<%=IWebConstants.PG_MAIN%>">
 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaArchiviazionePerProvvAltroUfficioMS">
 <input type="HIDDEN" title="Id Evento" value="<%= eventonotifica.getEvento().getIdEvento() %>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>">
 <input type="HIDDEN" title="Cod Pos" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" >

  <table>
    <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
          <%=lPosizione.getDescrPosizioneGiuridica()%>
          </font>
        </td>
    </tr>    
 <!-- Misure di Sicurezza già presenti -->
<%
	List lMisure =(List) request.getAttribute("listaMisureSic");
	if(lMisure.size() > 0)
	{
	    Iterator itx = lMisure.iterator();
	    while ( itx.hasNext())
	    {
		      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
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
    } %>	
</table>
<br>

 <table style="width: 95%;"> 
<!-- 				Dati provvedimento emesso da Altro Ufficio			--> 
	<tr><td class="Titolo" colspan=6>Dati Autorità Emittente Nota / Comunicazione</td></tr>
  <tr>
	<td class="l" width="20%">Data Emissione <font class="ob">(*)</font></td>
	<td class="L">
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataEmissione(),"dd"))%>" 
			type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataEmissione(),"MM"))%>" 
			type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataEmissione(),"yyyy"))%>" 
			 type="text" size="4" maxlength="4" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
	</td>
	<td class="l" width="20%">Data Ricezione <font class="ob">(*)</font></td>
	<td class="L">
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataRicezione(),"dd") )%>" 
			 type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataRicezione(),"MM") )%>"
			 type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataRicezione(),"yyyy") )%>"
			 type="text" size="4" maxlength="4" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	</td>
  </tr>
  <tr>
	<td class="L" >Magistrato Firmatario <font class=ob>(*)</font></td>
	<td class="L" colspan=3>
		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
		<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="35">
	     <a href="Javascript:ListaMagistrati('ModificaArchAltUffiMS');">
	       <img src="/images/filefolder.gif" border=0>
	     </a>
	    <input  type="hidden"  title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>"  name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  > 
    </td>
   </tr>
</table>
<table style="width: 95%;">
  <tr>
   	<td class="l" width="25%">Numero Protocollo Nota</td>
   	<td class="l" colspan="3">
     <input Title="Numero Nota" value="<%= StringUtils.toStringJSP(archiviazione.getNumNota() )%>" 
     							name="<%=ICostantiArchiviazione.CAMPO_NUM_NOTA%>" type="text" size="35" maxlength="35">
   	</td>
  </tr>	  
  <tr>
	<td class="L" width="25%">Autorità che ha inviato la nota <font class="ob">(*)</font></td>
	<td class="L" width="15%">
 		<select Title="Autorita Emittente" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
  		<%=autoritaEmi%>
 		</select>
	</td>
	<td class="l" width="10%" style="text-align:center" >Sede <font class="ob">(*)</font></td>
	<td class="L" width="35%">
		<%-- // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger() --%>
		<% //siesLogger.debug("--XX-- Trattino = "+archiviazione.getDescrLuogoEmittente()); %>
		<input title="Sede Autorita Emittente" value="<%= StringUtils.toStringJSP(archiviazione.getDescrLuogoEmittente() )%>" type="text" 
											   name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>" size="35" >
 		 <a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>');">
 		  <img src="/images/filefolder.gif" border=0>
 		 </a>
   		 <a href="Javascript:pulisciAutorita('<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>','0');">
	    &nbsp;<img src="/images/delete.gif" border=0></a>
	</td> 	
  </tr>
  <tr>    
    <td class="l" width="25%">Indirizzo</td>
    <td class="L" width="25%">
      <TEXTAREA title="Indirizzo" name="<%=ICostantiArchiviazione.CAMPO_INDIRIZZO_EMITTENTE%>" cols=40 ><%=StringUtils.toStringJSP(archiviazione.getIndirizzoEmittente())%></textarea>
    </td>
  </tr>
  <tr>
    <td class="l" width="25%">Altra Autorità</td>
    <td class="l" colspan="3">
      <input Title="Altra Autorita" value="<%=StringUtils.toStringJSP(archiviazione.getAltraAutorita())%>" 
      								name="<%=ICostantiArchiviazione.CAMPO_ALTRA_AUTORITA%>" type="text" size="35" style="width:400px">
    </td>
  </tr>
</table>
<br>
<!--   Tipo di Provvedimento di Archiviazione  ed eventuali Note  -->
<table style="width: 95%;">
  <tr>
    <td class="L" width="20%">Oggetto Definizione <font class="ob">(*)</font></td>
    <td class="L" colspan=3>
      <select title="Tipo Archiviazione" name="<%= ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>">
      <%=tipoArchiviazioni%>
      </select>
    </td>
    <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_ESITO %>">
    <input type="hidden" name="CodEsitoAlt3Value"> 
  </tr>   
  <tr>
    <td class="L" width="20%">Note</td>
    <td class="L" colspan=3>
      <TEXTAREA title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>" cols=80 rows=3 ><%=StringUtils.toStringJSP(archiviazione.getNote() )%> </textarea>
    </td>
  </tr>
  <tr>
	<td class="l" width="20%">Data Definizione <font class="ob">(*)</font></td>
	<td class="L" >
	 <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>" 
	  type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
	 <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>" 
	  type="text" size="2" maxlength="2" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
	 <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>" 
	  type="text" size="4" maxlength="4" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
	</td>

<%
 if(eventonotifica!= null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
 {
	NotificaModel lNotMod = eventonotifica.getNotifiche()[0];	
%>
 				
	<td class="l" width="20%">Data Trasmissione <font class="ob">(*)</font></td>
	<td class="L" >
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"dd") )%>" 
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"MM") )%>"
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"yyyy") )%>"
		 type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	</td>

<%
 }
 else
 {	%>	
					
	<td class="l" width="20%">Data Trasmissione <font class="ob">(*)</font></td>
	<td class="L" >
     <input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
	 <input type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
	 <input type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	</td>
	
<%
 } %> 
 </tr>
</table>
<br>
	<!--		Destinatari		-->		
<table style="width: 95%;">
	<tr>
		<td class="Titolo" colspan=6>Destinatari per Notifica</td>
	</tr>	
	<!--Eventuale Istituto di detenzione-->
<%if(lIdIstDete !=null)	
  { %>	
    <tr>
      <td class="l" width="15%">Struttura Designata </td> 
      <td class="l" colspan="3">
   	    <input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
   		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= lIdIstDete %>" size=90>
   		 <a href="Javascript:ListaIstitutoDetenzione('ModificaArchAltUffiMS','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
   		  <img src="/images/filefolder.gif" border=0></a>
   		 <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		  <img src="/images/delete.gif" border=0></a>
     </td>
   </tr>
<%}
  else
  { %>
   	<tr>
	  <td class="l" width="15%">Struttura Designata </td> 
	  <td class="l" colspan="3">
     	<input readonly  Title="Istituto" name="Comune" value="" size=90>
  	    <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=90>
  		 <a href="Javascript:ListaIstitutoDetenzione('ModificaArchAltUffiMS','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
   		  <img src="/images/filefolder.gif" border=0></a>
      	 <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
   		  <img src="/images/delete.gif" border=0></a>
	  </td>
 	</tr>		
<%} %>
		
	<!--Notifica per Altra Autorità 	-->
	
 <tr>
	<td class="L" width="20%">Autorità Esterna</td>
	<td class="L">
	 <select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
	  <%=autoritaEsternaE%>
	 </select>
	</td>
	
<% if(lPrimaAutoritaEsterna!=null && lPrimaAutoritaEsterna.getIdAutoritaEsterna()!=null) 
 	{ %>
	<td class="l" width="10%">Sede </td>
	<td class="L"><input title="Sede Autorita Esterna" value="<%= StringUtils.toStringJSP(lPrimaAutoritaEsterna.getDescrSede())%>" type="text" 
					name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
	   	<a href="Javascript:pulisciAutorita('<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>',
   	 									'<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>','1');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>
	</td> 	
 <%	}
 	else
 	{ %>
	<td class="l" width="10%">Sede </td>
	<td class="L"><input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
	 	<a href="Javascript:pulisciAutorita('<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>',
   	 									'<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>','1');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>

	</td> 	
 <%	} %>	
	
 </tr>
 <tr>
  	<td class="l">&nbsp;Note&nbsp;</td>
	<td class="L">
		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=50><%=StringUtils.toStringJSP(lnote)%></textarea>
	</td>	
 </tr>

	<!--Destinatario per l'Ente di Sorveglianza (Revisione del 24/10/2014)-->
  <tr>
	<td class="L" width="15%">Magistrato di Sorveglianza </td>
    <td class="L">
      <select Title="Magistrato di Sorveglianza" name="tipoUDS" >
      <%=tipoUDS%>
      </select>
   	</td>
   	<td class="L" COLSPAN=2>
      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
      <a href="Javascript:ListaUfficiComuni('ModificaArchAltUffiMS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
	                                       ,document.ModificaArchAltUffiMS.tipoUDS[document.ModificaArchAltUffiMS.tipoUDS.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0></a>
	 <a href="Javascript:pulisciUDS('tipoUDS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>');">
		 <img src="/images/delete.gif" border=0></a> 
    </td>
  </tr>  
</table>
<br>		
<!--ChecK  Notifica per Difensore e condannato  -->
<table width="90%">
 <tr>
<% if (avvocati.size()>0 ) { %>  
	<td class="l" colspan=3><input type="checkbox" name="Difesa" onclick="Javascript:Difensore();">Notifiche Atti (Difensore - Condannato) &nbsp;&nbsp;</td>
<%	} else { %>
	<td class="l" colspan=3 style="color:red"><input type="checkbox" name="Difesa" disabled >Notifiche Atti (Difensore - Condannato) Procedimento privo di Avvocato &nbsp;&nbsp;</td>
<%	} %> 
 </tr>
</table>

		<div id="divq" style="display: none; position: relative; width: 90%;">
			<table width="90%">
				<tr>
					<td class="Titolo" colspan=6>Destinatario per Notifica al Difensore</td>
				</tr>
				<tr>
					<td class="l" colspan=3><input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();">S.N.T. (Sistema Notifiche Telematiche) &nbsp;&nbsp;</td>
				</tr>
			</table>
			<div id="divae" style="display: block; position: relative; width: 90%;">
<!--Autorita ESTERNA per Notifica AVVOCATO Difensore       	-->
<%  int lIdxAvv = 0;
	int IndSede = 0;
	Iterator lItxAvv = avvocati.iterator(); 
	
	if(NotificaAvvocati.compareTo("-") == 0)	//- INSERIMENTO : Notifiche al Difensore NON PRESENTI
	{	
		while (lItxAvv.hasNext())
		{
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
				  <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
					<input title="Sede Autorita per Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" 
							type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
		     <%	if(avvocati.size()>1)
        		{	%>   				 
					<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
					<img src="/images/filefolder.gif" border=0>
					</a>
			<%	}
		   		else
		   		{%>
		   			<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
					<img src="/images/filefolder.gif" border=0>
					</a>
	       	<%	} %>				
				</td>
				<td class="l">Indirizzo</td>
				<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=40></textarea></td>
				</tr>

				<tr>
				<td class="l" width="20%">Per Avvocato</td>
					<input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
				<td class="l"><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
					<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
					<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
					</font>
				</td>
					<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
				</tr>
			</table>
<%			lIdxAvv++;
		}
	}		
 	else			//- MODIFICA : Notifiche al Difensore GIA' PRESENTI
	{	
		for (int ind = 0;ind < eventonotifica.getNotifiche().length; ind++ )
		{
			if( eventonotifica.getNotifiche()[ind].getCodTipoNotifica().equals("ND"))
			{	%>
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
						<input title="Sede Autorita per Avvocato" value="<%=eventonotifica.getNotifiche()[ind].getAutoritaEsterna().getDescrSede()%>" 
											type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
			       <%	if(avvocati.size()>1)
	   					{	%>   				 
							<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=IndSede%>]');">
							<img src="/images/filefolder.gif" border=0>
							</a>
					<%	}
			       		else
			       		{ %>
			       			<a href="Javascript:ListaComuni('ModificaArchAltUffiMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
							<img src="/images/filefolder.gif" border=0>
							</a>
			       	<%	} %>				
						</td>
						<td class="l">Indirizzo</td>
						<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=40><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getNote())%></textarea></td>
						
					</tr>
	<% 	      		if( lItxAvv.hasNext() )
		      		{
		        		AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lItxAvv.next();
		     %>		
						<tr>
							<td class="l" width="20%">Per Avvocato</td>
							<td class="l"><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
								<font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%></font> &nbsp;Difensore di&nbsp; 
								<font class="campo"> <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
								</font>
							</td>
							<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
						</tr>								
	<%				} %>
	
					</table>
					
	<% 			IndSede++;
				}
			
		}		
	}	%>							

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
</body>
</html>