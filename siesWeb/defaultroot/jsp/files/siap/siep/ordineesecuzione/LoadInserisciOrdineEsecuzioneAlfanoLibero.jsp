<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>

<jsp:useBean id="evento"								scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"							scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"			scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"			scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"						scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="StrdataInizioPena"			scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"							scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"						scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="istanza"								scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="dataeditabile"					scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoIstituto"					scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioMagistrato"	scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovaistanza"					scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="tipoUfficioSIUS" 			scope="request" class="java.lang.String"/>

<!--jsp:useBean id="posizione"          scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/-->
<!--jsp:useBean id="detenutoAltraCausa" scope="request" class="java.lang.String"/-->
<!--jsp:useBean id="altracausaposizionegiuridica"       scope="request" class="siap.siep.altracausa.model.AltraCausaModel"/-->

<%-- 20191002 [SG]: intervento post collaudo 11.3 -- refactoring pagina --%>

<%
	String FlagIstanza="";
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
  
  // Per tutti i provvedimenti che lo gestiscono
  // aggiungere campo OBBLIGATORIO editabile CAMPO_CASELLARIO
  //       1. precaricato a '-' se lo stato nascita dell'imputato è blank
  //       2. altrimenti COD_UFFICIO dell'utente collegato
  // ad eccezione dei quattro provvedimento sotto elencati.
  // SOLO per questi 4 provvedimenti e se l'imputato è straniero (stato nascita diverso da ITALIA) --> casellario = ROMA
  //   -- Computo fungibilità
  //   -- Unificazione delle pene concorrenti
  //   -- Rideterminazione della pena
  //   -- Sospensione pena 656 
  SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
   
  String lCasellario = lUfficioUtenteConnesso.getDescrComune();

  if( lSoggettoAssociato != null && 
    (!"039".equals(lSoggettoAssociato.getCodStatoNascita())) )
  {
    lCasellario = "ROMA";
  }

  if( lSoggettoAssociato != null && 
    ( lSoggettoAssociato.getCodStatoNascita() == null
    	|| "".equals(lSoggettoAssociato.getCodStatoNascita()) 
      || "-".equals(lSoggettoAssociato.getCodStatoNascita()) ) )
  {
    lCasellario = "-";
  }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Ordine di Esecuzione con Sospensione (Decreto Alfano) </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
		<script language="JavaScript">
      var desktop;

    	function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
		</script>
    <script language="JavaScript">
    	var desktop;
    	function ListaComuni(a_formname,a_fieldname)
    	{
      	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	}
   		
   		// Chiamata lista Avvocati.
    	function ListaAvvocati(a_formname)
    	{
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    	}

 			// Chiamata all'elenco degli UEPE
      function ListaCSSA(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

  		function Verify()
  		{
		  	//Se il "Foglio Complementare" è selezionato, il "Casellario Giudiziale" è obbligatorio
		  	if( document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>.checked == true )
		  	{
       		if (   document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
       		    || document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
       		   )
          {
            alert("Il campo Casellario Giudiziale è obbligatorio!");

            return false;
          }		  	
		  	} 
		  	
		  	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
		  	{
        	alert('Data di emissione non valida');
			   	
			   	return false;
		  	}

        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			  	document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		  	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			  	document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  	var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      	var campo = document.LoadInserisciOrdineEsecuzione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

<%
				if(		 (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") && lAltraCausa.getCodTipoPosGiuridica().equals("23") )
						|| (lPosizione.getCodPosizioneGiuridica().equals("07") && lFascicoloAssociato.getFlagAltraCausa()== null  )
						|| (lPosizione.getCodPosizioneGiuridica().equals("10") && lFascicoloAssociato.getFlagAltraCausa()== null )
						|| (lPosizione.getCodPosizioneGiuridica().equals("-") && lFascicoloAssociato.getFlagAltraCausa()== null )
						|| lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04") || lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20")
						|| lPosizione.getCodPosizioneGiuridica().equals("46") || lPosizione.getCodPosizioneGiuridica().equals("47"))
        {
%>
          if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>[document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.selectedIndex].value == '-' )
          {
            alert("Il campo Autorità Destinazione  è obbligatorio!");
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E %>.focus();
            
            return false;
          }
<%
				}  
				else if (posizioneluogoaltra!= null && lAltraCausa!= null && lAltraCausa.getIstitutoDetenzione() != null)
				{
%>
       		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == '-' )
          {
            alert("Il campo Autorità Destinazione è obbligatorio!");
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>.focus();
            
            return false;
          }
<%
			}
%>
<%
			if(avvocati.size() >1)
      {
%>
      	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0][document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].selectedIndex].value == '-')
				{
          alert("Il campo Autorità per la Notifica  di un Condannato Libero è obbligatorio!");
          document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    
			    return false;
		  	}
        
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1][document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].selectedIndex].value == '-')
        {
	        alert("Il campo Autorità per la  Notifica di un Condannato Libero è obbligatorio!");
	        document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
	        return false;
        }
<%
			}
			else
			{
%>
				if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-')
				{
          alert("Il campo Autorità per la prima Notifica  di un Condannato Libero è obbligatorio!");
          document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    
			    return false;
		  	}
<%
			}
%>

    	if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Magistrato è obbligatorio");
        return false;
      }

    	if( 	 document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '10'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '07'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '02'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '04'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '16'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '20'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '46'
     			&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '46')

     {
       if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
       {
          alert("Autorità Destinazione obbligatorio");
          return false;
       }
     }

<% 
		 if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") && !lAltraCausa.getCodTipoPosGiuridica().equals("23"))
 		 {
%>
    	 if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
       {
         alert("Autorità Destinazione obbligatorio");
          
         return false;
       }
<%
		}
%>
<% 
		if(lPosizione.getCodPosizioneGiuridica().equals("04") || lPosizione.getCodPosizioneGiuridica().equals("02"))
   	{
%>
      if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_SSPA%>.value == "")
      {
        alert("Ufficio di Esecuzione Penale Esterna obbligatorio");
       
        return false;
      }
<%
		}
		
		if(!lPosizione.isLibero() ||(((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
		{
			if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
 			{
  			if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
  			{
%>
			    if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
					  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
				  if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
					  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
		
				  var data_to_verifica = document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

		      if (!ControllaData(data_to_verifica) )
				  {
	          alert('Data fine pena non valida');
			 
			 			return false;
		  		}
<%
				}
 			}
		}
%>
    }

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    // 02/12/2010 Lista Uffici della Sorveglianza ( TDS oppure UDS)
    function ListaTDS_UDS(a_formname,a_fieldname)
    {
      var codTipoSede = document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS%>.value;	  
      if(codTipoSede=="" || codTipoSede=='-')
	  {
	  	alert("Ufficio Destinatario in Notifica Ente di Sorveglianza è obbligatorio");       
	  } else {
		  <%-- var i = document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS%>.selectedIndex; --%>
	      //if ( i == 1 )
	      if(codTipoSede=="UDS" || codTipoSede=='UDSM')
	      { 
	    	var desktop;
	        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+codTipoSede+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	      } else if(codTipoSede=="TDS" || codTipoSede=='TDSM')	      
	      {
	    	var desktop;
	        <%-- desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500"); --%>
	        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	      }
	  }            
    }

    function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // NGG
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
    		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    
    function toggleComuneCasellario()
		{
			vistaLabel = (document.getElementById("labelCasellario").style.display == 'none') ? 'block' : 'none';
			document.getElementById("labelCasellario").style.display = vistaLabel;

			vistaInput = (document.getElementById("inputCasellario").style.display == 'none') ? 'block' : 'none';
			document.getElementById("inputCasellario").style.display = vistaInput;
		}
  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        EventoModel lProvvedimento = new EventoModel();
        String lAzione = new String();

        if( modalita.equals("I") )
			  {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.siep.ordineesecuzione.action.ActInserisciOEDetenutoQC";
        if( lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
			   {%>
            <font class="campo">Ordine di Esecuzione con sospensione (LEGGE 199/2010) - DETENUTO PER ALTRA CAUSA</font>
			   <%}else{%>
            <% if (lPosizione.getCodPosizioneGiuridica().equals("02")) { %>
                <font class="campo">Ordine di Esecuzione con sospensione (LEGGE 199/2010) - CUST. CAUT. PER QUESTA CAUSA IN REGIME DI ARRESTI DOMICILIARI</font>
            <% } else {%>
            <font class="campo">Ordine di Esecuzione con sospensione (LEGGE 199/2010) - CONDANNATO <%=lPosizione.getDescrPosizioneGiuridica()%></font>
         <%}
         }
        }
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
			    lAzione = "siap.siep.ordineesecuzione.action.ActModificaOEDetenutoQC";
%>
          <font class="campo">Modifica di Ordine di Esecuzione Sospensione Alfano -  <%=lPosizione.getDescrPosizioneGiuridica()%></font>
<%
      }
%>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciOrdineEsecuzione" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciOrdineEsecuzioneAlfanoLibero">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
%>
          DETENUTO PER ALTRA CAUSA  <%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
        }else{
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
        }
%>
        </font>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" maxlength="6" size="6" >
        </td>
      </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
					//modifica relativa al tipo istituto
          if(lAltraCausa.getIstitutoDetenzione() != null)
         	// if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              <%//if(lAltraCausa.getDescrLuogoIstituto()!=null){%>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            <%//}%>
            </td>
           </tr>

          <%}
           if (lAltraCausa.getAltroLuogo()!=null)
            { %>
            <tr>
             <td class="l">Altro Luogo </td >
             <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp; </td>
          <%}
       } else
      if(lLuogoDetenzione.getIstitutoDetenzione() != null)
       //if(!lLuogoDetenzione.getDescrTipoIstituto().equals("") && lLuogoDetenzione.getDescrTipoIstituto()!= null && !lLuogoDetenzione.getDescrTipoIstituto().equals("-"))
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
            //  if(lLuogoDetenzione.getDescrLuogo()!=null)
            //  {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
            //  }
%>
            </td>
          </tr>
<%
        }

        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") ||
        lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Altro Luogo </td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>  maxlength="6" size="6" --%>
        <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>  maxlength="6" size="6" --%>
  <tr>
<%	//fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) ||
    (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}else{
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<% 
			if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}else{
%>
	      <td class="l" >Arresto</td>
	      <td class="l" colspan=2>
	         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
	         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
	         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
	      </td>
	      <td class="l">Ammenda</td>
	      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
	    	</td>
<%
      }
    }
%>
    <tr>
<%
    if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")  && !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20") && !lPosizione.getCodPosizioneGiuridica().equals("46")
         && !lPosizione.getCodPosizioneGiuridica().equals("47")) ||
     (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
		{
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
		}
%>
<%
    if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10") && !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20") && !lPosizione.getCodPosizioneGiuridica().equals("46")
         && !lPosizione.getCodPosizioneGiuridica().equals("47")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
		{
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
		}
%>
		</tr>
	</table>
	<table>
		<tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=2 >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
         <td class="l">Data Trasmissione</td>
        <td class="L" colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

<%
		if(nuovaistanza!=null && nuovaistanza.getIdNuovaIstanza()!=null)
		{%>
	    <tr>
	       <td class="l">Data Istanza </td>
	       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%>&nbsp;</font></td>
	     </tr>
	     <tr>
	       <td class="l">Oggetto dell'Istanza </td>
	       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrContenuto()) %> &nbsp;</font></td>
	    </tr>
<%
		} else if(istanza!=null && istanza.getIdEvento()!=null)
		{
%>
		  <tr>
		    <td class="l">Data Istanza </td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(istanza.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
		  </tr>
		  <tr>
		    <td class="l">Oggetto dell'Istanza </td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istanza.getDescrMotivo()) %> &nbsp;</font></td>
		  </tr>
<%
		}
%>
	</table>
	<table>
 		<tr>
    	<td class="l" colspan="3">
    		<input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>" checked>
     		Foglio Complementare
     	</td>
     	<td class="l" id="labelCasellario" style="display:none;">Casellario Giudiziale</td>
     	<td class="l" id="inputCasellario" style="display:none;">
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaUfficiPerTipo('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
    <tr>
    	<td>
   	  		<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    	</td>
    </tr>
  </table>
  <table width="100%">
     <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
     <tr>
     <td class="l">Magistrato</td>
        <td class="L" colspan="3">
         <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
         <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
           <a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzione');">
            <img src="/images/filefolder.gif" border=0>
            </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
     </tr>

		<tr><td class="Titolo" colspan=6>Notifica al Condannato </td></tr>

<%	if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") )
		{%>
		<tr>
			<td class="l" width="20%">Autorità Destinazione <font class=ob>(*)</font></td>

<%		if(lAltraCausa.getCodTipoPosGiuridica().equals("23"))
			{%>

				<td class="L"  colspan="3">
       		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
    	</tr>
    	<tr>
      	<td class="l">Sede <font class=ob>(*)</font></td>
      	<td class="L">
					<!--modifica relativa al tipo istituto-->
        	<input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
					<!-- fine modifica relativa al tipo istituto-->
					<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          	<img src="/images/filefolder.gif" border=0>
        	</a>
      	</td>
      	<td class="l">Indirizzo</td>
      	<td class="L">
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
      	</td>
		<%}else{
				if(posizioneluogoaltra!= null && lAltraCausa!= null && lAltraCausa.getIstitutoDetenzione() != null)
 				{%>
					<td class="l">
					<input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrComune())%>" size=50>
					<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lAltraCausa.getIstDetIdIstitutoDetenzione()%>" size=50>
					<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
					<img src="/images/filefolder.gif" border=0></a></td>
			<%}else{%>
					<td class="l">
					<input readonly Title="Istituto" name="Comune" value="" size=50>
					<input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
					<a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
					<img src="/images/filefolder.gif" border=0></a></td>
			<%}%>
      	<td class="l">Note</td>
				<td class="L">
					<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
				</td>
		<%}%>

		<tr><td>&nbsp;</td></tr>
	</tr>
<%}else{%>
		<td class="l" width="20%">Autorità Destinazione <font class=ob>(*)</font></td>
			<%if(lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10") ||
      		 lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04") ||
      		 lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") || 
     			 lPosizione.getCodPosizioneGiuridica().equals("46")	|| lPosizione.getCodPosizioneGiuridica().equals("47") )
			{%>
 				<td class="L" colspan="3">
       		<select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
    	</tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
      </td>

	<%}else{
			//modifica relativa al tipo istituto
		  if(lLuogoDetenzione != null && lLuogoDetenzione.getIstitutoDetenzione() != null)
		  {%>
		     <td class="l">
		     <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
		     <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
		     <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
		     <img src="/images/filefolder.gif" border=0></a></td>

	  <%}else{%>
         <td class="l">
         <input readonly Title="Istituto" name="Comune" value="" size=50>
         <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
         <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
         <img src="/images/filefolder.gif" border=0></a></td>
    <%}%>
      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
      </td>
		<tr><td>&nbsp;</td></tr>
   <%}%>

		</tr>
<%}%>

  	</tr>
    <tr>
      <td class="Titolo" colspan=6>Notifica al Difensore</td></tr>
<%
      int lIdxAvv = 0;
			int lNumAvvocati = avvocati.size();
      Iterator lItxAvv = avvocati.iterator();
      while( lItxAvv.hasNext() )
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
				</table>
        <table width=100%>
          <tr>
            <td class="l"  width="20%">Per Avvocato </td>
            <td class="L">
              <input type="hidden" name="indexAvvocati" value="<%=lIdxAvv%>">
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
               <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
            </td>
          </tr>
        </table>
         <table width=100%>
          <tr><td class="l">Autorità Destinazione </td >
          <td class="L" colspan=3>
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaN%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
				if( lNumAvvocati < 2 )
				{%>
        	<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
<%
				}else{
%>
        	<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
<%
				}
%>
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
       <td class="l">Note</td>
       <td class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
    lIdxAvv++;
  }
%>
<!--Notifica per l'Ente di Sorveglianza -->
  <tr>
		<td class="Titolo" colspan=6> Notifica Ente di Sorveglianza</td></tr>
  <tr>

		<td class="l">Destinatario</td>
    <td class="l" colspan="3">
       <select title="tipoUfficioSIUS" class=small name="<%=ICostantiOrdineEsecuzione.CAMPO_TIPO_UFFICIO_UDS%>">
         <%= tipoUfficioSIUS %>
       </select>
 		</td>              
  </tr>

  <tr>
  	<td class="L">Sede <font class=ob>(*)</font></td>
  	<td class="L">
      <input Title="Luogo Ente di Sorveglianza" name="<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS%>" type="text" maxlength="35" size="35">
      <a href="Javascript:ListaTDS_UDS('LoadInserisciOrdineEsecuzione','<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Note</td>
    <td class="L">
    	<TEXTAREA title="Note" name="<%= ICostantiOrdineEsecuzione.CAMPO_NOTE_UDS %>"  cols=35></textarea>
    </td>
  </tr>


	<%if(istanza!=null && istanza.getIdEvento()!=null)
	{
      FlagIstanza="S";
%>
      <input type="hidden" name="IdIstanza" value="<%=istanza.getIdEvento()%>">
<%
		}else{
      FlagIstanza="N";
		}%>
		<input type="hidden" name="istanza" value="<%=FlagIstanza%>">

    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");
<%if(istanza!=null && istanza.getIdEvento()!=null)
{%>
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
<%}%>

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

<%if(lPosizione.getCodPosizioneGiuridica().equals("07") || lPosizione.getCodPosizioneGiuridica().equals("10") || lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")
 || lPosizione.getCodPosizioneGiuridica().equals("16") || lPosizione.getCodPosizioneGiuridica().equals("20") || lPosizione.getCodPosizioneGiuridica().equals("46")
         || lPosizione.getCodPosizioneGiuridica().equals("47"))
{
  if(lFascicoloAssociato.getFlagAltraCausa()==null || (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("N")))
{%>
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%}}%>

<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") )
{%>
<%if(lAltraCausa.getCodTipoPosGiuridica().equals("23"))
{%>
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%}}%>

 frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","req","Luogo Autorità Destinazione obbligatoria");
 frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","alphabetic");



<% if(lPosizione.getCodPosizioneGiuridica().equals("04") || lPosizione.getCodPosizioneGiuridica().equals("02"))
   {%>
 frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>","req","Sede UDS obbligatoria");
 frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>","alphabetic");
<%}%>

  <%if(((istanza != null) && (istanza.getIdEvento()!=null))||(lPosizione.getCodPosizioneGiuridica().equals("04")) || (lPosizione.getCodPosizioneGiuridica().equals("02")))
   {
   %>
 frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS %>","req","Sede TDS obbligatoria");
 frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_TDS %>","alphabetic");
 <%}%>



<%if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10") && !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20") && !lPosizione.getCodPosizioneGiuridica().equals("46")
         && !lPosizione.getCodPosizioneGiuridica().equals("47")) || (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
{
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
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%}
 }
}%>

</script>
</body>
</html>