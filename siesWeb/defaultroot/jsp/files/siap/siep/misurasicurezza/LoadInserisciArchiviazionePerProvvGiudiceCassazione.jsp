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
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
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
<jsp:useBean id="tipoprovvedimento"		scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"				scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"        		scope="request" class="java.util.Vector" />
<jsp:useBean id="autoritaEsterna" 		scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsternaE" 		scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUDS"         		scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"       		scope="request" class="java.lang.String"/>

<%
//===================================================================================================
//Form per inserimento di Archiviazione per Provvedimento del G.E. (Definizione Misure Sicurezza) 
//====================================================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
 
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

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
  
  SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
 
   
  // Misure Sicurezza
  List lMisure =(List) request.getAttribute("listaMisure");
%>
<!-- 					LoadInserisciArchiviazionePerProvvGiudiceCassazione 				-->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - -Gestione Misure sicurezza Provvidorie o Fuori Sentenza- Archiviazione per Provvedimento del Giudice/Cassazione </title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
	    	  var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
     //DATA EMISSIONE
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;
	
	        var data_to_verify = document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) )
	        {
	          alert('Data emissione non valida');
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	
	          return false;
	        }
	 //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }
	        
	        
	//DATA RICEZIONE
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;
	
	        var data_to_verify = document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) )
	        {
	          alert('Data ricezione non valida');
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();
	
	          return false;
	        }
	        
	 //2) Controllo : data di sistema deve essere >= Data Ricezione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Ricezione non può essere superiore alla data odierna!');
		      document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>.focus();
		      return false;
		    }
	        
	//DATA DEFINIZIONE
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;
	
	        var data_to_verify = document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;
	
	        if (!ControllaData(data_to_verify) )
	        {
	          alert('Data definizione non valida');
	          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
	
	          return false;
	        }
	        
// Autorità 
	        if(document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-")
		    {
		       	alert("Digitare Tipo Autorità Emittente ");
		       	document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
		       	return false;
		    }	
		    
// Sede Autorità	
	        if(document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value != "-" &&
			   	document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.value == "" )
			{
			   	alert("Digitare Sede Autorità Emittente");
			   	document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
			   	return false;
			}		    
		    
// Tipo Provvedimento
			if(document.ArchGiuCassa.<%= ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC %>.value == "-") 
			{
				alert(" TIPO Provvedimento OBBLIGATORIO");
	        	document.ArchGiuCassa.<%= ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC %>.focus();
	        	return false;
			}	

// Anno e numero protocollo		
			if(document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value=="" && document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value!="")
	      	{
		        alert("Inserire Numero Protocollo");
		        document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.focus();
		        return false;
	      	}
			
			if(document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value !="" && document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value =="")
	      	{
		        alert("Inserire Anno Protocollo");
		        document.ArchGiuCassa.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
		        return false;
	      	}
		    
// Oggetto Definizione
	        if (document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
	        {
		          alert("Il Campo Oggetto definizione è obbligatorio");
		          document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.focus();
		          return false;
	        }
	        
// Magistrato	
	        if(document.ArchGiuCassa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
	        {
		          alert("Il Cognome del Magistrato è obbligatorio");
		          document.ArchGiuCassa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
		          return false;
	        }
	
	        if(document.ArchGiuCassa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
	        {
		          alert("Il Nome del Magistrato è obbligatorio");
		          document.ArchGiuCassa.<%=ICostantiMagistrato.CAMPO_NOME %>.focus();
		          return false;
	        }
	        
   	// Controllo su AUTORITA (destinatari)
			if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" )
			{
				if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
				{
					alert('ERRORE : Inserire Sede Destinatario');
					document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
					return false;
				}	
			}
		
			if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "" )
			{
				if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" )
				{
					alert('ERRORE : Inserire Tipo Autorità Destinatario');
					document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
					return false;
				}	
			}
			
			// Controlli su Notifica al Difensore
			if( document.ArchGiuCassa.Difesa.checked )
			{
			       <% if (avvocati.size()==1) {%>
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" 
						&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
						{
							alert('Attenzione : Inserire Dati Destinatario notifica Difensore ');
			      			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
				 			return false;		
						}
			
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1
							&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
						{
							alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			      			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
				 			return false;		
						}
			
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
							&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" )
						{
							alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
				          	document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
						 	return false;		
						}
						
				   <% } else { %>
				    for (var i=0; i< <%=avvocati.size()%>; i++) { 
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" 
							&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
						{
							alert('Attenzione : Inserire Dati Destinatario notifica Difensore '+i);
			      			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
				 			return false;		
						}
			
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1
						&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
						{
							alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
			      			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
			      			bloccaUNEP();
				 			return false;		
						}
			
						if(document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
							&& document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" )
						{
							alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
				          	document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
						 	return false;		
						}
					
					}
			<%	} %>
			}
			
			// Per Tipo Autorità S.N.T. si imposta la sede Autorità a "-".
			if( document.ArchGiuCassa.SiNoTe.checked ) {
			       <% if (avvocati.size()==1) {%>
						document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
			    		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
			    		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
						var codAut=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
						var indAut=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
			  <% } else { %>
				    for (var i=0; i< <%=avvocati.size()%>; i++) { 
						document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
			    		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
			    		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
						var codAut2=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
						var indAut2=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
					}
			<%	} %>
			}
			else
			{
				if( document.ArchGiuCassa.Difesa.checked ) {
				        <% if (avvocati.size()==1) {%>
			        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
							var codAut=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
							var indAut=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
				   <% } else { %>
			    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
			        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
							var codAut2=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
							var indAut2=document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
						}
				<%	} %>
				}
			}
			
			<%// Controllo Destinatario Sorveglianza%>
			if(document.ArchGiuCassa.tipoUDS.value != "-")
			{
				if(document.ArchGiuCassa.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
				{
					alert(' Inserire Sede Destinatario Sorveglianza');
					document.ArchGiuCassa.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
					return false;
				}	
			}
		    
	        return true;
   
      }
// End Verify

		function valorizzaAutorita()
		{
			document.getElementById("Autorita").selectedIndex = 1;
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
	  
      function ListaComuniperuff(a_formname,a_fieldname,codTipoUfficio)
      {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
	  
	  function ListaProvvGiudice(a_formname)
	  {
	      //desktop = window.open("< %=IWebConstants.PG_MAIN%>?< %=IWebConstants.ACTION_FIELD%>=siap.siep.archiviazione.action.ActListaDocumentiArchiviazione&formname="+a_formname+"&< %=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=< %=lFascicoloAssociato.getIdFascicoloSiep()%>+", "Lista_Provvedimenti", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
		  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActListaProvvedimentiGiudiceCassazione&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>+", "Lista_Provvedimenti", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
	  }
	  
	  function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
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
      
      function Difensore()
      {
  		var nodequan = document.getElementById("divq");
  		
          if( document.ArchGiuCassa.Difesa.checked )
       	  { 
         		nodequan.style.display='block';
         		
     	        <% if (avvocati.size()==1) {%>
          		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
         			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
             		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
             		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
        	    <% } else { %>
      	      	for (var i=0; i< <%=avvocati.size()%>; i++){ 
	            		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
 	   	     <% } %>
     	  }
          else
          {
          	nodequan.style.display='none';
          	
     	        <% if (avvocati.size()==1) {%>
	        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
      				document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
        	    <% } else { %>
 	      			for (var i=0; i< <%=avvocati.size()%>; i++){ 
		        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
					}
 	   	     <% } %>
          }
          
      } // End Difensore()
      
		// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
  	function AutEsterna()
    {
  		var nodeAut = document.getElementById("divae");
  		
          if( document.ArchGiuCassa.SiNoTe.checked )
       	{ 
         		nodeAut.style.display='none';
         		
     	        <% if (avvocati.size()==1) {%>
					document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	           		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.value='';
	           		//document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
        	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		           			//document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value='';
						}
 	   	     	<% } %>
     	  	}
          else
          {
          	nodeAut.style.display='block';

			<% if (avvocati.size()==1) {%>
	        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	        		document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
      				document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
          			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
          			document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
            <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        			document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
		        			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.ArchGiuCassa.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	     	<% } %>
          }
          
      } // End AutEsterna()
      
    	// Blocco combo Aut.Est.
    	function bloccaUNEP()
    	{
    	      <% if (avvocati.size()==1) {%>
    			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
    	      <% } else { %>
    	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
    			document.ArchGiuCassa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
    	      }
    	     <% } %>
    	}

      //Funzione utile per impostare la data corrente.
  	  function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna) {    
  		 day=dataOdierna.substring(0,2);
  		 month=dataOdierna.substring(3,5);
  		 year=dataOdierna.substring(6,10);
  	     document.getElementsByName(campo_giorno).item(0).value = day;
  	     document.getElementsByName(campo_mese).item(0).value = month;
  	     document.getElementsByName(campo_anno).item(0).value = year;      
  	  }
      
      function pulisciIstituto (nomeCampoComune, nomeCampoId)
      {
          var campoDescr = document.getElementsByName(nomeCampoComune)[0];
          var campoId    = document.getElementsByName(nomeCampoId)[0];
          campoDescr.value="";
          campoId.value="";
      }

 </script>
 
  </head>
  <!--  body class="corpo" onLoad="valorizzaAutorita();" -->
  <body class="corpo" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Archiviazione per Provvedimento del Giudice / Cassazione</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="ArchGiuCassa" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciArchiviazionePerProvvGiudiceCassazione">
    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_ESITO %>" >
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
  <!--  Dati del Giudice / Cassazione  -->  
  <table width="90%">
    <tr>
      <td colspan=4 class="titolo">dati Provvedimento del/la Giudice/Cassazione</td>
    </tr>
    <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaProvvGiudice('ArchGiuCassa');">
          Seleziona provvedimenti dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
 	<tr><td>&nbsp;</td></tr>
</table>
<table width="90%">    
    <tr>
      <td class="l" width="20%">Data Emissione <font class=ob>(*)</font></td>
      <td class="l" >
        <input type="text" Title="Giorno Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione" value="" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l" width="20%">Data Ricezione <font class=ob>(*)</font></td>
      <td class="l" >
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

<!-- 	Dati Autorità Emittente ( Giudice / Cassazione)  -->

  <tr>
    <td class="l" width="20%">Autorità </td>
	<td class="L">
        <select id="Autorita" Title="Autorita" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
          <%=autorita%>
         </select>
    </td>
    <td class="l" width="10%">&nbsp;&nbsp;Sede </td>
    <td class="L" width="30%">
          <input title="Sede Autorita uff"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuniperuff('ArchGiuCassa','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>',document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.ArchGiuCassa.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
    </td>        

  </tr>

<!--  dati sul Provvedimento emesso -->
  <tr>
  	<td class="l" width="20%">Tipo Provvedimento<font class="ob">(*)</font></td>
    <td class="L" >
      <select title="Tipo Provvedimento" name="<%= ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC %>">
      <%=tipoprovvedimento%>
      </select>
    </td>
   	<td class="l" width="25%">Anno / Numero Provvedimento</td>
  	<td class="L" >
     	<input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
       	<input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
   	</td>        
 </tr>
 
</table>    
   <br>
<!--  Definizione Procedimento -->   
  <table width="90%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l" width="20%">Data Definizione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
   		  <a href="Javascript:impostaDataOdierna('<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE %>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
       		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
   		  </a>
      </td>
    </tr>
    <tr>
      <td class="l" width="20%">Oggetto Definizione <font class=ob>(*)</font></td>
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
  <br>
<!--  Magistrato Firmatario -->
  <table width="90%">
    <tr>
      <td colspan=4 class="titolo">Magistrato Firmatario</td>
    </tr>
    <tr>
      <td class="l" width="20%">Magistrato Firmatario</td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('ArchGiuCassa');">
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
		  <td class="l" width="20%">Struttura Designata </td> 
		  <td class="l" colspan="3">
		  	<input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
		  	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=90>
		  		<a href="Javascript:ListaIstitutoDetenzione('ArchGiuCassa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		       		<img src="/images/filefolder.gif" border=0></a>
		      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		      		<img src="/images/delete.gif" border=0></a>
		  </td>
		 </tr>
<%	} 
	else
	{ %>
 	     <tr>
		  <td class="l" width="20%">Struttura Designata </td> 
		  <td class="l" colspan="3">
		  	<input readonly  Title="Istituto" name="Comune" value="" size=90>
		  	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=90>
		  		<a href="Javascript:ListaIstitutoDetenzione('ArchGiuCassa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		       		<img src="/images/filefolder.gif" border=0></a>
		      	<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
		      		<img src="/images/delete.gif" border=0></a>
		  </td>
		 </tr>
<%	} %>
		 
	<!--Notifica per Altra Autorità 	-->
		<tr>
			<td class="L" width="20%">Altra Autorità</td>
			<td class="L">
				<select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>"> 
				<%=autoritaEsternaE%>
				 </select>
			</td>
			<td class="l">Note</td>
			<td class="L"><TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=40></textarea></td>
		</tr>

		<tr>
			<td class="l" width="20%">Sede</td>
			<td class="L" colspan=3><input title="Sede Autorita Esterna" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>" size="35"> 
				<a href="Javascript:ListaComuni('ArchGiuCassa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
				<img src="/images/filefolder.gif" border=0>
				</a>
			</td>
		</tr>
		
	<!--Destinatario per l'Ente di Sorveglianza per esecuzione (Revisione del 24/10/2014)-->
	<tr>
		<td class="L" width="20%">Magistrato di Sorveglianza </td>
	    <td class="L">
	      <select Title="Magistrato di Sorveglianza" name="tipoUDS" >
	      <%=tipoUDS%>
	      </select>
	   	</td>
	   	<td class="L" COLSPAN=2>
	      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
	      <a href="Javascript:ListaUfficiComuni('ArchGiuCassa','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
	                                           ,document.ArchGiuCassa.tipoUDS[document.ArchGiuCassa.tipoUDS.options.selectedIndex].value);">
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
              <a href="Javascript:ListaComuni('ArchGiuCassa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
                <img src="/images/filefolder.gif" border=0>
              </a>
      <%	}
       		else
       		{	%>
              <a href="Javascript:ListaComuni('ArchGiuCassa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
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
    var frmvalidator = new Validator("ArchGiuCassa");


//data emissione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");

//data ricezione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","maxlen=4","La lunghezza massima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","minlen=4","La lunghezza minima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","numeric");

//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>