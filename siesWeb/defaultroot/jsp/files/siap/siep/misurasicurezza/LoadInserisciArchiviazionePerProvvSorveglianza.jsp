<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.permesso.action.ICostantiEventoPermessoLicenza"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
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
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>

<jsp:useBean id="dataeditabile"				scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="MisuraModel"				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="ListaOrd"					scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoArchiviazioni" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"					scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoprovvedimento"			scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"        			scope="request" class="java.util.Vector" />
<jsp:useBean id="autoritaEsterna" 			scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsternaE" 			scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUDS"         			scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"       			scope="request" class="java.lang.String"/>

<%
//===================================================================================================
//Form per inserimento di Archiviazione per Provvedimento della Sorveglianza (appl. Misure Sicurezza) 
//====================================================================================================

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  
if(lPosizione == null)
   lPosizione = new PosizioneGiuridicaModel();

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
  
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
<!-- 					LoadInserisciArchiviazionePerProvvSorveglianza 				-->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza - Archiviazione per Provvedimento della Sorveglianza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
      var desktop;
		
  		function Verify()
  		{
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
		  	if (document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
        		alert('Data di emissione non valida');
        		document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			   	return false;
		  	}

		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
		      alert('Data Emissione non può essere superiore alla data odierna!');
		      document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		      return false;
		    }

<% 			if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
	 		{
		  		if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
		  		{
	%>
					      if (document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
							  document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
						  if (document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
							  document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
				
						  var data_to_verifica = document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		
				      	  if (!ControllaData(data_to_verifica) )
						  {
			          			alert('Data fine pena non valida');
			          			document.LoadArchiviazioneProvSorv.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.focus();
					 			return false;
				  		  }
	<%
				}
		}
%>
// Tipo Provvedimento
		if( document.LoadArchiviazioneProvSorv.<%= ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO %>[document.LoadArchiviazioneProvSorv.<%= ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO %>.selectedIndex].value == '-') 
		{
			alert(" TIPO Provvedimento OBBLIGATORIO");
        	document.LoadArchiviazioneProvSorv.<%= ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO %>.focus();
        	return false;
		}
		
// Controllo AUTORITA EMITTENTE	
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE %>.value == "-"  && 
			document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value == "" )
		{
				alert('ERRORE : Inserire Tipo e Sede Autorità Emittente');
				document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
				return false;
		}
		
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.value != "-" )
		{
			if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value == "" )
			{
				alert('ERRORE : Inserire Sede Autorità Emittente');
				document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
				return false;
			}	
		}
	
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value != "" )
		{
			if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.value == "-" )
			{
				alert('ERRORE : Inserire Tipo Autorità Emittente');
				document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
				return false;
			}	
		}

// OGGETTO DEFINIZIONE
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>.value == "-"  || 
			document.LoadArchiviazioneProvSorv.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>.value == "" )
		{
				alert('ERRORE : Inserire Oggetto Definizione');
				document.LoadArchiviazioneProvSorv.<%=ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>.focus();
				return false;
		}


// Anno e numero protocollo		
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value=="" && document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value!="")
      	{
	        alert("Inserire Numero Protocollo");
	        document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.focus();
	        return false;
      	}
		
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value !="" && document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value =="")
      	{
	        alert("Inserire Anno Protocollo");
	        document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
	        return false;
      	}
		
// Anno e numero SIUS
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>.value=="" && document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>.value!="")
      	{
	        alert("Inserire Numero Procedimento SIUS");
	        document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>.focus();
	        return false;
      	}
		
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>.value !="" && document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>.value =="")
      	{
	        alert("Inserire Anno Procedimento SIUS");
	        document.LoadArchiviazioneProvSorv.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>.focus();
	        return false;
      	}
// Magistrato		
    	if(document.LoadArchiviazioneProvSorv.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadArchiviazioneProvSorv.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      	{
	        alert("Il Magistrato è obbligatorio");
	        document.LoadArchiviazioneProvSorv.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
	        return false;
      	}

	// Controllo su AUTORITA per destinazione	
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value != "-" )
		{
			if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
			{
				alert('ERRORE : Inserire Sede Destinatario');
				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
				return false;
			}	
		}
	
		if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value != "" )
		{
			if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-" )
			{
				alert('ERRORE : Inserire Tipo Autorità Destinatario');
				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
				return false;
			}	
		}
		
		// Controlli su Notifica al Difensore
		if( document.LoadArchiviazioneProvSorv.Difesa.checked )
		{
		       <% if (avvocati.size()==1) {%>
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" 
					&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
					{
						alert('Attenzione : Inserire Dati Destinatario notifica Difensore ');
		      			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
			 			return false;		
					}
		
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1
						&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value == "" )
					{
						alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
		      			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.focus();
			 			return false;		
					}
		
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value != ""
						&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value == "-" )
					{
						alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
			          	document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.focus();
					 	return false;		
					}
					
			   <% } else { %>
			    for (var i=0; i< <%=avvocati.size()%>; i++) { 
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" 
						&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
					{
						alert('Attenzione : Inserire Dati Destinatario notifica Difensore '+i);
		      			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
			 			return false;		
					}
		
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1
					&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value == "" )
					{
						alert('Attenzione : Inserire la Sede Destinatario notifica Difensore ');
		      			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].focus();
		      			bloccaUNEP();
			 			return false;		
					}
		
					if(document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value != ""
						&& document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value == "-" )
					{
						alert('Attenzione : Inserire il tipo di  Destinatario notifica Difensore  ');
			          	document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
					 	return false;		
					}
				
				}
		<%	} %>
		}
		
		// Per Tipo Autorità S.N.T. si imposta la sede Autorità a "-".
		if( document.LoadArchiviazioneProvSorv.SiNoTe.checked ) {
		       <% if (avvocati.size()==1) {%>
					document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
		    		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=0;
		    		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value='C0';
					var codAut=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
					var indAut=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
		  <% } else { %>
			    for (var i=0; i< <%=avvocati.size()%>; i++) { 
					document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		    		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=0;
		    		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value='C0';
					var codAut2=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
					var indAut2=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
				}
		<%	} %>
		}
		else
		{
			if( document.LoadArchiviazioneProvSorv.Difesa.checked ) {
			        <% if (avvocati.size()==1) {%>
		        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
						var codAut=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value;
						var indAut=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex;
			   <% } else { %>
		    	    for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
						var codAut2=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].value;
						var indAut2=document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex;
					}
			<%	} %>
			}
		}
		
		<%// Controllo Destinatario Sorveglianza%>
		if(document.LoadArchiviazioneProvSorv.tipoUDS.value != "-")
		{
			if(document.LoadArchiviazioneProvSorv.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "")
			{
				alert(' Inserire Sede Destinatario Sorveglianza');
				document.LoadArchiviazioneProvSorv.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
				return false;
			}	
		}
		
    }	<% // Chiude function Verify %>

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=400");
    }
    
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
//==========================================================================
// Caricamento POPUP	
    function ListaProvvSIUS(a_formname)
    {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadListaArchiviazioniProvvedimentiSIUS&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep() %>", "Lista_Provvedimenti_Sorveglianza", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
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
// ----	----
    function Difensore()
    {
  		var nodequan = document.getElementById("divq");
  		
        if( document.LoadArchiviazioneProvSorv.Difesa.checked )
     	{ 
       		nodequan.style.display='block';
       		
   	        <% if (avvocati.size()==1) {%>
        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
       			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
           		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
           		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
      	    <% } else { %>
    	      	for (var i=0; i< <%=avvocati.size()%>; i++){ 
	            		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	               		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	               		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
					}
	   	     <% } %>
   	    }
        else
	    {
        	nodequan.style.display='none';
        	
   	        <% if (avvocati.size()==1) {%>
	        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
    				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
      	    <% } else { %>
	      			for (var i=0; i< <%=avvocati.size()%>; i++){ 
		        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
	    				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
					}
	   	     <% } %>
        }
    }    
   
	// SELEZIONE MUTUAMENTE ESCLUSIVA S.N.T./UNEP
  	function AutEsterna()
    {
  		var nodeAut = document.getElementById("divae");
  		
        if( document.LoadArchiviazioneProvSorv.SiNoTe.checked )
       	{ 
         		nodeAut.style.display='none';
         		
     	        <% if (avvocati.size()==1) {%>
					document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value = "-";
	           		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.value='';
	           		//document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=true;
        	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
							document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value = "-";
		           			//document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value='';
						}
 	   	     	<% } %>
     	}
        else
        {
          	nodeAut.style.display='block';

   	        <% if (avvocati.size()==1) {%>
	        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value="";
	        		document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.value="";
	        		document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
      				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
          			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
          			document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>.disabled=false;
       	    <% } else { %>
		    	    	for (var i=0; i< <%=avvocati.size()%>; i++) { 
		        			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].value="";
		        			document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>[i].value="";
		        			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].disabled=false;
	    					document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	            			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[i].disabled=false;
	            			document.LoadArchiviazioneProvSorv.<%=ICostantiNotifica.CAMPO_NOTE%>[i].disabled=false;
						}
	   	   	<% } %>
        }	
    }
	
  	function ListaComuni(a_formname,a_fieldname)
    {
      var desktop;
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
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
  	
	// Blocco combo Aut.Est.
	function bloccaUNEP()
	{
	      <% if (avvocati.size()==1) {%>
			document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex=1;
	      <% } else { %>
	      for (var i=0; i< <%=avvocati.size()%>; i++){ 
				document.LoadArchiviazioneProvSorv.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[i].selectedIndex=1;
	      }
	     <% } %>
	}

  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Definizione Procedimento - Archiviazione per Provvedimento della Sorveglianza</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadArchiviazioneProvSorv" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciArchiviazionePerProvvSorveglianza">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </tr>
  
<%	//fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && 
    ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
    	&& !penaresidua.getFlagErgastolo().equals("S") 
    	&& !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            	(penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
        	{
        		
        	}
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
%>

<% 
			if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         		(penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             	(penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
     	 	{
				
     	 	}
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
			      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
			    	</td>
			    </tr>	
<%
      		}

     }  // CHIUDO if(penaresidua...)
%>
    <tr>
<%
      if (penaresidua.getDataInizio() != null)
      {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
      }
 
       if (penaresidua.getFlagErgastolo() != null)
       {
	         if(penaresidua.getFlagErgastolo().equals("S"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
	<%
	         }
	         else if(penaresidua.getFlagErgastolo().equals("D"))
	         {
	%>
	           	<td class="l">Pena Detentiva</td>
	           	<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
	<%
	         }
       }
%>
<%
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
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
          }
          else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
	             <td class="l">Data Fine Pena</td>
	             <td class="L" colspan=2>
	               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
	               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
	            </td>
<%
          }
          else
          {
%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

<%       	}
        }
      }
%>
	</tr>
</table>
<!--    // Eventuali Misura di Sicurezza  -->
<%
	List lMisure =(List) request.getAttribute("listaMisure");
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
     <tr>
   	  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </tr>
 
 
 <table width="80%"> 
<!-- 	Dati provvedimento della Sorveglianza --> 
	<tr><td class="Titolo" colspan=6>Dati provvedimento della Sorveglianza</td></tr>
	<tr>
      <input type="hidden" value="" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>">
      <input type="hidden" value="" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>">
      <td class="l" colspan="2">
        <a href="Javascript:ListaProvvSIUS('LoadArchiviazioneProvSorv');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
    </tr>
</table>
<table width="90%">     
	<tr>
    	<td class="l" width="16%"> Autorità Emittente <font class="ob">(*)</font></td>
    	<td class="l" width="20%">
      		<select Title="Autorità emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE %>">
        	<%=autorita%>
      		</select>
      	</td>
      	<td class="l" width="12%" style="text-align:center" >Sede <font class="ob">(*)</font></td>
      	<td class="l" width="25%">	
        	<input title="Sede" type="text" value="" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE %>"  maxlength="35" size="35">
        	<a href="Javascript:ListaUfficiComuni('LoadArchiviazioneProvSorv','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE %>',document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE %>[document.LoadArchiviazioneProvSorv.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE %>.options.selectedIndex].value);">
          	<img src="/images/filefolder.gif" border=0>
        	</a>
    	</td>
    </tr>
</table>
<table width="90%">     
    <tr>
        <td class="l" width="20%">Data Ricezione Provvedimento<font class="ob">(*)</font></td>
        <td class="L" colspan=2 width="15%">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      		<a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>','<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
      		</a>
        </td>
        <td class="l" width="20%">Data Emissione provvedimento <font class="ob">(*)</font></td>
        <td class="L" colspan=2 width="20%">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
     </tr>
     
     <tr>
     	<td class="l" width="20%">Tipo Provvedimento<font class="ob">(*)</font></td>
        <td class="L" colspan=2 width="15%">
          <select title="Tipo Provvedimento" name="<%= ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO %>">
          <%=tipoprovvedimento%>
          </select>
        </td>
      	<td class="l" width="20%" >Anno / Numero Provvedimento</td>
      	<td class="L" colspan=2 width="20%">
         	<input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         	<input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      	</td>        
   </tr>
   <tr>
       <td class="l" width="20%" >Anno / Numero Fascicolo SIUS </td>
       <td class="L" colspan=2 width="20%">
         	<input Title="Anno Procedimento"   value="" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         	<input Title="Numero Procedimento" value="" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      	</td>  
   </tr>     	 	
</table>

<table width="90%"> 
 <!-- 	Definizione procedimento -->
	<tr><td class="Titolo" colspan=6>Dati definizione Procedimento</td></tr>
	<tr>
		<td class="L" width="20%">Data Definizione <font class=ob>(*)</font></td>
		<td class="L" colspan=2 >
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      		<a href="Javascript:impostaDataOdierna('<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE %>','<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE %>','<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
      		</a>
        </td>
     </tr>
     <tr>
     	<td class="L" width="20%">Oggetto Definizione <font class="ob">(*)</font></td>
        <td class="L">
          <select title="Tipo Archiviazione" name="<%= ICostantiMisuraSicurezza.CAMPO_COD_RICH_MISURA_SIC%>">
          <%=tipoArchiviazioni%>
          </select>
        </td>
        <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_ESITO %>">
        <input type="hidden" name="CodEsitoAlt3Value"> 
     </tr>   
     <tr>
     	<td class="L" width="20%">Note</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=80 rows=3 ></textarea>
      	</td>
     </tr>
</table>
  
 <table width="90%">
     <tr><td class="Titolo" colspan=6> Magistrato Firmatario</td></tr>
<!--Magistrato Firmatario -->     
     <tr>
     <td class="L">Magistrato Firmatario <font class=ob>(*)</font></td>
        <td class="L" colspan="3">
         <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
         <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="35">
           <a href="Javascript:ListaMagistrati('LoadArchiviazioneProvSorv');">
            <img src="/images/filefolder.gif" border=0>
            </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
     </tr>
     
     <tr><td class="Titolo" colspan=6> Destinatari </td></tr>
	<!--Eventuale Istituto di detenzione-->
<%	if(posizioneluogoaltra!=null && posizioneluogoaltra.getLuogoDetenzione()!=null &&
		posizioneluogoaltra.getLuogoDetenzione().getIdLuogoDetenzione()!=null && 
		posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()!=null)
	{%>	
 	     <tr>
		  <td class="l" width="30%">Struttura Designata <font class="ob">(*)</font></td> 
		  <td class="l" colspan="3">
		  	<input readonly  Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=90>
		  	<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%= posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=90>
		  		<a href="Javascript:ListaIstitutoDetenzione('LoadArchiviazioneProvSorv','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
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
		  		<a href="Javascript:ListaIstitutoDetenzione('LoadArchiviazioneProvSorv','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
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
				<a href="Javascript:ListaComuni('LoadArchiviazioneProvSorv','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');"> 
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
	      <a href="Javascript:ListaUfficiComuni('LoadArchiviazioneProvSorv','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
	                                           ,document.LoadArchiviazioneProvSorv.tipoUDS[document.LoadArchiviazioneProvSorv.tipoUDS.options.selectedIndex].value);">
	         <img src="/images/filefolder.gif" border=0>
	      </a> 
	    </td>
  </tr>      
</table>

<table width="90%">
  <tr>
<%	if(avvocati.size() > 0)
	{	%>    
    	<td class="l" colspan=3><input type="checkbox" name="Difesa" onclick="Javascript:Difensore();">Notifiche Atti (Difensore - Condannato) &nbsp;&nbsp;</td>
<%	}
	else
	{%>
		<td class="l" colspan=3 style="color:red"><input type="checkbox" name="Difesa" disabled >Notifiche Atti (Difensore - Condannato) Procedimento privo di Avvocato &nbsp;&nbsp;</td>
<%	} %>    	
    	
  </tr>
</table>

<div id="divq" style="display: none; position: relative;  width: 90%;">
  <table width="90%">
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica</td>
    </tr>
    <tr>
      <td class="l" colspan=3><input type="checkbox" name="SiNoTe" onclick="Javascript:AutEsterna();"> Notifica ai sensi dell'art. 148 comma 2 bis c.p.p. &nbsp;&nbsp;</td>
    </tr>
  </table>
  <div id="divae" style="display: block; position: relative;  width: 90%;">
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
            <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <%	if(avvocati.size()>1)
        	{	%>     
              <a href="Javascript:ListaComuni('LoadArchiviazioneProvSorv','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[<%=lIdxAvv%>]');">
              <img src="/images/filefolder.gif" border=0>
              </a>
        <%	}
        	else
        	{%> 
        		<a href="Javascript:ListaComuni('LoadArchiviazioneProvSorv','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
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
	<tr> 
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>
</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadArchiviazioneProvSorv");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>","req","Il campo Giorno Ricezione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>","req","Il campo Mese Ricezione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>","req","Il campo Anno Ricezione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>","lt=2099");
  
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno Definizione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese Definizione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno Definizione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","lt=2099");

<%
 if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
 {
	  if ( dataeditabile.equals("S")  && penaresidua.getDataFinePresunta() != null )
	  {%>
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
	
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
	
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
	  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
	<%}
 }
%>

</script>
</body>
</html>