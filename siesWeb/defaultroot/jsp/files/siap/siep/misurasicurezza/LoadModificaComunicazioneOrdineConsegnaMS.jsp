<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>

<jsp:useBean id="eventonotifica"      		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"					scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="avvocati" 					scope="request" class="java.util.Vector" />
<jsp:useBean id="magistrato" 				scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="autoritaEsternaE" 			scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsternaC" 			scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsterna" 			scope="request" class="java.lang.String" />
<jsp:useBean id="MisuraModel" 				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" />
<jsp:useBean id="tipoUDS"                   scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="istitutodetenzione"		scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="NotificaAvvocati" 			scope="request" class="java.lang.String" />
<%-- MEV_39: aggiunto e gestito useBean su Istituto Detenzione --%>
<jsp:useBean id="strutturaDesignataModel"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<% FascicoloSiepModel fascicolo = (FascicoloSiepModel) session.getAttribute("fascicolo");

	UtenteModel lUtenteMod = new UtenteModel( (UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
	MisuraSicurezzaModel lMis=null;
	
// Gestione Provvedimento
	String lTitolo="";
	String lCodMotivo="";
	
	if(eventonotifica!=null && eventonotifica.getEvento()!=null 
	  && eventonotifica.getEvento().getIdEvento()!=null )
	{
		lCodMotivo = eventonotifica.getEvento().getCodMotivo();
	}
	
	if(lCodMotivo.compareTo("1131") == 0 )
		lTitolo="Modifica Comunicazione per esecuzione Misure di Sicurezza";
	else if(lCodMotivo.compareTo("1126") == 0 )
		lTitolo="Modifica Ordine di Consegna per esecuzione Misure di Sicurezza";
	else
		lTitolo="Modifica Provvedimento per esecuzione Misure di Sicurezza";

// Gestione Autorità Esterne sulle Notifiche - ( al MAX 2 Notifiche)
	String lCodTipoAutorita1 = "-";
	NotificaModel lPrimaNotifica = null;
	AutoritaEsternaModel lPrimaAutoritaEsterna = null;
	String lnote =""; 
	
	String lCodTipoAutorita2 = "-";
	NotificaModel lSecondaNotifica = null;
	AutoritaEsternaModel lSecondaAutoritaEsterna = null;
	String lnoteSec ="";
	
	UfficioModel lUffSorvModel = null;
	String lUfficioDescrTipo = "";
	String lUfficioDescrSede = "";

	IstitutoDetenzioneModel lIstMod = null;
	String lIdIstDete =null;
	
	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("AA").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{
				if(lCodTipoAutorita1.compareTo("-") == 0)
				{	
					lCodTipoAutorita1 = eventonotifica.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
		    		lPrimaNotifica = (NotificaModel) eventonotifica.getNotifiche()[i];
	      			lPrimaAutoritaEsterna = (AutoritaEsternaModel) lPrimaNotifica.getAutoritaEsterna();
	      			if(lPrimaNotifica!=null && lPrimaNotifica.getIdNotifica()!=null && lPrimaNotifica.getNote()!=null )
					{ 
						lnote=lPrimaNotifica.getNote().toString().trim();
					}	
				}
				else
				{
					lCodTipoAutorita2 = eventonotifica.getNotifiche()[i].getAutoritaEsterna().getCodTipoAutorita();
					lSecondaNotifica = (NotificaModel) eventonotifica.getNotifiche()[i];
	      			lSecondaAutoritaEsterna = (AutoritaEsternaModel) lSecondaNotifica.getAutoritaEsterna();
	      			if(lSecondaNotifica!=null && lSecondaNotifica.getIdNotifica()!=null && lSecondaNotifica.getNote()!=null )
					{ 
						lnoteSec=lSecondaNotifica.getNote().toString().trim();
					}	
				}
			}
			
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{
				lUffSorvModel = (UfficioModel) eventonotifica.getNotifiche()[i].getUfficio();
				lUfficioDescrTipo = lUffSorvModel.getDescrTipoUfficio();
				lUfficioDescrSede = lUffSorvModel.getDescrComune();		
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
<!--	 LoadModificaComunicazioneOrdineConsegnaMS	 -->
<html>
<head>
<title>[S.I.E.S.] -Gestione Misure sicurezza- Provvedimenti per Esecuzione MS</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
	
    	var desktop;
    	var NotificaAvvocati = '<%=NotificaAvvocati%>';

    	var lnote = '<%=lnote%>';
    	var lnoteSec = '<%=lnoteSec%>';
    	
     	// Lista dei MAGISTRATI
    	function ListaMagistrati(a_formname)
    	{
    	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    	}
    	
    	// Lista dei COMUNI
    	function ListaComuni(a_formname,a_fieldname)
        {
            desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	 
    	// Blocco combo Aut.Est.
    	function bloccaUNEP()
    	{
   	      <% if (avvocati.size()==1) {%>
				document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
    	      <% } else { %>
    	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
  				document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
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
            
            if(note=='1')
            	document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE_E%>.value = "";
            else
            	document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA%>.value = "";
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
        		document.ModificaComunicazioneMS.Difesa.checked = false;
        	}
        	else
        	{
        		document.ModificaComunicazioneMS.Difesa.checked = true;
        		if(NotificaAvvocati == "C0")
        		{
        			document.ModificaComunicazioneMS.SiNoTe.checked = true;
        		}
        		else
        		{
        			document.ModificaComunicazioneMS.SiNoTe.checked = false;
        		}
        		
        		Difensore();
        		AutEsterna();
        	}
        	
        	if(lnote!="")
        	{
        		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE_E%>.value=lnote;
        	}
        	
        	if(lnoteSec!="")
        	{
        		document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA%>.value=lnoteSec;
        	}
        		
        }
        
    	function Difensore()
        {
    		var numavvocati = <%=avvocati.size()%>;
    		var nodequan = document.getElementById("divq");
            if( document.ModificaComunicazioneMS.Difesa.checked )
         	{ 
           		nodequan.style.display='block';
           		
       	         if(numavvocati==1) 
       	         { 
            		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
           			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
               		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
               		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
          	     } 
       	         else
       	         { 
        	      	for (var i=0; i< numavvocati; i++)
        	      	{ 
	            		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
   	   	      	 } 
       	  	}
            else
            {
            	nodequan.style.display='none';
            	
       	         if (numavvocati==1) 
       	         { 
	        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
        			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
          	     }
       	         else 
       	         { 
   	      			for (var i=0; i< numavvocati; i++)
   	      			{ 
		        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
					}
   	   	      	} 
            }
        }
   		
   		function AutEsterna()
        {
    		var nodeAut = document.getElementById("divae");
    		
            if( document.ModificaComunicazioneMS.SiNoTe.checked )
         	{ 
           		nodeAut.style.display='none';
           		
       	        <% if (avvocati.size()==1) {%>
					document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
	           		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
          	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "";
							document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
						}
   	   	     	<% } %>
       	  	}
            else
            {
            	nodeAut.style.display='block';

       	        <% if (avvocati.size()==1) {%>
	        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
        			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
            		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
            		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
           	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	     	<% } %>
            }	
        }

    	function Verify()
  		{
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
  			// CONTROLLO DATA TRASMISSIONE
		  	if (document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		  	if (document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  	document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

		  	var data_to_verify = document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'/'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data Trasmissione NON Valida');
        		document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Trasmissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Trasmissione non può essere superiore alla data odierna!');
		      document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.focus();
		      return false;
		    }
		    
		    // CONTROLLO DATA EMISSIONE
		  	if (document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data di emissione non valida');
        		document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.ModificaComunicazioneMS.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

			
				// Controllo su AUTORITA per esecuzione	
				if(document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>.value == "-" 
					&& document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" )
				{
						alert('ERRORE : Inserire almeno un Destinatario');
	          			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
			 			return false;
				}
				else
				{
						// Autorità Esterna
						if(document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" 
							&& document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
						{
							alert('ERRORE : Inserire Sede Autorità Esterna ');
		          			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
				 			return false;		
						}
						
						if(document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" 
							&& document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "" )
						{
							alert('ERRORE : Inserire Tipo Autorità Esterna');
		          			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
				 			return false;				
						}
						
						// Altra Autorità Esterna
						if(document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>.value != "-" 
							&& document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>.value == "" )
						{
							alert('ERRORE : Inserire Sede Altra Autorità Esterna ');
		          			document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>.focus();
				 			return false;		
						}

						if(document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>.value == "-" 
							&& document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>.value != "" )
						{
							alert('ERRORE : Inserire Tipo Altra Autorità Esterna ');
		          			document.ModificaComunicazioneMS.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>.focus();
				 			return false;				
						}
				}
				
				// Controlli su Notifica al Difensore
				if( document.ModificaComunicazioneMS.Difesa.checked )
				{
					if( document.ModificaComunicazioneMS.SiNoTe.checked )
					{
		       	     <% if(avvocati.size()==1) 
	       	        	{ %>
							document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "";
			        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
			        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
			        		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	       	   <% 		}
	       	        	else
	       	        	{ %>
			    	    	for (var i=0; i< <%=avvocati.size()%>; i++)
			    	    	{ 
								document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
				        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
				        		document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
				        		document.ModificaComunicazioneMS.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
							}
					<%	} %>						
					}
					else
					{	
	       	        <%	if (avvocati.size()==1)
	       	        	{%>
							if(document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value != "" 
							&& document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
							{
								alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			          			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
					 			return false;		
							}
		
		       	   <% 	}
	       	        	else
	       	        	{ %>
			    	    	for (var i=0; i< <%=avvocati.size()%>; i++) 
			    	    	{ 
								if(document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value != "" 
									&& document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
								{
									alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
				          			document.ModificaComunicazioneMS.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
						 			return false;			
								}
							}
					<%	} %>
				  	}	
				}


				
				<%// Controllo Destinatario Sorveglianza%>
				if(document.ModificaComunicazioneMS.tipoUDS.value != "-")
				{
					if(document.ModificaComunicazioneMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
					{
						alert(' Inserire Sede Destinatario Sorveglianza');
						document.ModificaComunicazioneMS.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
						return false;
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
	 				<font class="campo"><%=lTitolo%></font>
	 </td>
	</tr>
 </table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
	<br>
<FORM method="POST" name="ModificaComunicazioneMS" action="<%=IWebConstants.PG_MAIN%>">
 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaComunicazioneOrdineConsegnaMS">
 <input type="HIDDEN" title="Id Evento"   value="<%= eventonotifica.getEvento().getIdEvento() %>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>">

<table>
  <tr>
	<td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
       <font class="campo">
       <%=posizione.getDescrPosizioneGiuridica()%>
       </font>
    </td>
   </tr>
	<!-- Misure di Sicurezza presenti -->
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
<br>

		<!-- Date del Provvedimento -->
<table>
  <tr>
	<td class="l">Data Emissione <font class="ob">(*)</font></td>
	<td class="L" colspan=2>
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>" 
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>" 
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>" 
		 type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> &nbsp;
					<!--  
				        <a href="Javascript:impostaDataOdierna('< %= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','< %= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','< %= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '< %=DateUtils.getSysDate("dd/MM/yyyy")%>');">
				          <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
				        </a>
				        	-->
	</td>
  </tr>
<%	NotificaModel lNotMod = eventonotifica.getNotifiche()[0];	%>
  <tr>					
	<td class="l">Data Trasmissione <font class="ob">(*)</font></td>
	<td class="L" colspan=2>
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"dd") )%>" 
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"MM") )%>"
		 type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
		<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(),"yyyy") )%>"
		 type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	</td>
  </tr>
  <tr>
	<td class="L" >Magistrato Firmatario <font class=ob>(*)</font></td>
	<td class="L" >
		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
		<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="35">
	     <a href="Javascript:ListaMagistrati('ModificaComunicazioneMS');">
	       <img src="/images/filefolder.gif" border=0>
	     </a>
	    <input  type="hidden"  title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>"  name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  >
	    <input type="HIDDEN" title="Id Misura" value="<%=StringUtils.toStringJSP(MisuraModel.getIdMisuraSicurezza())%>" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>">
	     
    </td>
   </tr>	
</table>
<br>
	<!--		Destinatari		-->		
<table>
	<tr>
		<td class="Titolo" colspan=6>Destinatari per Notifica</td>
	</tr>	
	<!--Eventuale Istituto di detenzione-->
<%if(lIdIstDete !=null)	
  { %>	
    <tr>
      <td class="l" width="20%">Istituto di Detenzione </td> 
      <td class="l" colspan="3">
   	    <input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
   		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= lIdIstDete %>" size=90>
   		 <a href="Javascript:ListaIstitutoDetenzione('ModificaComunicazioneMS','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
   		  <img src="/images/filefolder.gif" border=0></a>
   		 <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		  <img src="/images/delete.gif" border=0></a>
     </td>
   </tr>
<%}
  else
  { %>
   	<tr>
	  <td class="l" width="30%">Istituto di Detenzione </td> 
	  <td class="l" colspan="3">
     	<input readonly  Title="Istituto" name="Comune" value="" size=90>
  	    <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=90>
  		 <a href="Javascript:ListaIstitutoDetenzione('ModificaComunicazioneMS','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
   		  <img src="/images/filefolder.gif" border=0></a>
      	 <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
   		  <img src="/images/delete.gif" border=0></a>
	  </td>
 	</tr>		
<%} %>

<!--Eventuale sTRUTTURA DESIGNATA-->
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
<tr><td>&nbsp;</td></tr>	
 <tr>
	<td class="L">Autorità Esterna</td>
	<td class="L">
	 <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
	  <%=autoritaEsternaE%>
	 </select>
	</td>
 </tr>
 
 <tr>
 <% if(lPrimaAutoritaEsterna!=null && lPrimaAutoritaEsterna.getIdAutoritaEsterna()!=null) 
 	{ %>
	<td class="l">Sede Autorita Esterna</td>
	<td class="L"><input title="Sede Autorita Esterna" value="<%= StringUtils.toStringJSP(lPrimaAutoritaEsterna.getDescrSede())%>" type="text" 
					name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
	   	<a href="Javascript:pulisciAutorita('<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>',
   	 									'<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>','1');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>
	</td> 	
 <%	}
 	else
 	{ %>
	<td class="l">Sede Autorita Esterna</td>
	<td class="L"><input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
		<a href="Javascript:pulisciAutorita('<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>',
   	 									'<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>','1');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>
	</td> 	
 <%	} %>
 		
 	<td class="l">&nbsp;Note&nbsp;</td>
	<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=50 rows=2>
		</textarea>
	</td>	
 </tr>
 
<!-- 					SECONDA		-->
 <tr>
	<td class="L">Altra Autorità Esterna</td>
	<td class="L">
	 <select Title="Altra Autorita Esterna" class="small" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>">
	  <%=autoritaEsternaC%>
	 </select>
	</td>
 </tr>
 
 <tr>
 <% if(lSecondaAutoritaEsterna!=null && lSecondaAutoritaEsterna.getIdAutoritaEsterna()!=null) 
 	{ %>
	<td class="l">Sede Altra Autorita Esterna</td>
	<td class="L"><input title="Sede Altra Autorita Esterna" value="<%= StringUtils.toStringJSP(lSecondaAutoritaEsterna.getDescrSede())%>" type="text" 
					name="<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
   	 	<a href="Javascript:pulisciAutorita('<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>',
   	 									'<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>','2');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>	 	
	</td>
<%	}
 	else
 	{ %>
	<td class="l">Sede Altra Autorita Esterna</td>
	<td class="L"><input title="Sede Altra Autorita Esterna" value="" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>" size="35"> 
	 	<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>');"> 
	 	<img src="/images/filefolder.gif" border=0>
	 	</a>
	 	<a href="Javascript:pulisciAutorita('<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_ALTRA_AUTORITA%>',
   	 									'<%=ICostantiMisuraSicurezza.CAMPO_COD_SEDE_ALTRA_AUTORITA%>','2');">
	  &nbsp;<img src="/images/delete.gif" border=0></a>
	</td> 	 	
<%	} %> 

	<td class="l">&nbsp;Note&nbsp;</td>
	<td class="L"><TEXTAREA title="Note2" name="<%=ICostantiMisuraSicurezza.CAMPO_NOTE_ALTRA_AUTORITA%>" cols=50 rows=2>
		</textarea>
	</td>	
</tr>
 <tr><td>&nbsp;</td></tr>			
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
      <a href="Javascript:ListaUfficiComuni('ModificaComunicazioneMS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
	                                       ,document.ModificaComunicazioneMS.tipoUDS[document.ModificaComunicazioneMS.tipoUDS.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0></a>
	 <a href="Javascript:pulisciUDS('tipoUDS','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>');">
		 <img src="/images/delete.gif" border=0></a> 
    </td>
  </tr> 
</table>
		
<!--ChecK Notifica Immediata e Notifica per Difensore e condannato  -->
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
		
<%	if(eventonotifica!=null && eventonotifica.getEvento()!=null && 
		eventonotifica.getEvento().getFlagPiuMeno()!=null &&
		eventonotifica.getEvento().getFlagPiuMeno().compareTo("D")==0)
	{	%>	
		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiMisuraSicurezza.CAMPO_FLAG_ESECUZIONE_IMMEDIATA %>" value="D" CHECKED>Disposta l'esecuzione Immediata &nbsp;&nbsp;</td>
<%	}
	else
	{ %>
		<td class="l" colspan=3><input type="checkbox" name="<%= ICostantiMisuraSicurezza.CAMPO_FLAG_ESECUZIONE_IMMEDIATA %>" value="D" >Disposta l'esecuzione Immediata &nbsp;&nbsp;</td>
<%	}	%>				
 </tr>
</table>

		<div id="divq" style="display: none; position: relative; width:90%;">
			<table width="90%">
				<tr>
					<td class="Titolo" colspan=6>Destinatario per Notifica al Difensore</td>
				</tr>
				<tr>
					<td class="l" colspan=3><input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();">S.N.T. (Sistema Notifiche Telematiche) &nbsp;&nbsp;</td>
				</tr>
			</table>
			<div id="divae" style="display: block; position: relative; width:90%;">
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
					<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();">
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
					<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
					<img src="/images/filefolder.gif" border=0>
					</a>
			<%	}
		   		else
		   		{%>
		   			<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
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
				<td class="L"><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
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
						<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" onChange="Javascript:bloccaUNEP();">
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
							<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=IndSede%>]');">
							<img src="/images/filefolder.gif" border=0>
							</a>
					<%	}
			       		else
			       		{ %>
			       			<a href="Javascript:ListaComuni('ModificaComunicazioneMS','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
							<img src="/images/filefolder.gif" border=0>
							</a>
			       	<%	} %>				
						</td>
						<td class="l">Indirizzo</td>
						<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=30><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[ind].getNote())%></textarea></td>
						
					</tr>
	<% 	      		if( lItxAvv.hasNext() )
		      		{
		        		AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lItxAvv.next();
		     %>		
						<tr>
							<td class="l" width="20%">Per Avvocato</td>
							<td class="L"><font class="campo" > <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%> </font> &nbsp;Foro di&nbsp; 
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