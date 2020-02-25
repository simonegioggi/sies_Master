<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>

<jsp:useBean id="giudizio"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"    			scope="request" class="java.util.Vector" />

<jsp:useBean id="TornaQui" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoDest" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori" 				scope="session" class="java.util.Vector"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="elencoSezioniUdienza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="descrizioneAula"       scope="request" class="java.lang.String"/>
<jsp:useBean id="ingressoAula"          scope="request" class="java.lang.String"/>
<jsp:useBean id="pianoAula"             scope="request" class="java.lang.String"/>
<jsp:useBean id="idAula"                scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoSvolgimento"      scope="request" class="java.lang.String"/>

<jsp:useBean id="gg"      scope="request" class="java.lang.String"/>
<jsp:useBean id="mm"      scope="request" class="java.lang.String"/>
<jsp:useBean id="aaaa"    scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaProcedimentoSige"    scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaSige"    scope="request" class="java.lang.String"/>

<jsp:useBean id="oraInizio"    scope="request" class="java.lang.String"/>
<jsp:useBean id="minInizio"    scope="request" class="java.lang.String"/>

<jsp:useBean id="oraFine"    scope="request" class="java.lang.String"/>
<jsp:useBean id="minFine"    scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEmissione"    scope="request" class="java.util.Date"/>
<jsp:useBean id="flagRinvio"        scope="request" class="java.lang.String"/>

<%
		//Magistrato Assegnatario precedentemente impostato
	MagistratoAssegnatarioModel magistratoassegnatario = FascicoloSigeEsteso.getMagAssegnatario();
    String idUdiSige = "";
	/* Estrazione della data udienza precedentemente fissata */
	String lDataUdienza = "";
	if ((FascicoloSigeEsteso.getUdienzaProcedimento() != null) && (FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null) )
	    lDataUdienza = (DateUtils.getDateToString (FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(), "dd/MM/yyyy"));
   	
		//
		// Link alla Gestione Oggetti.
		//
   	RedirectTo lRedir = new RedirectTo();
   	lRedir.setPage(IWebConstants.PG_MAIN);
   	lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggetti");
   	lRedir.setParameter("TornaQui", TornaQui );

   	String lLinkOggettiSessione = lRedir.toString();
   	
   	//
   	// Preparazione Link Gestione udienze.
   	//
   	
   	// Monocratica
   	RedirectTo lRedirMonocratica = new RedirectTo();
   	lRedirMonocratica.setAction("siap.sige.udienzamonocratica.action.ActLoadInserisciUdienzaMonocraticaSige");	
	lRedirMonocratica.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
	lRedirMonocratica.setParameter("TornaQui", TornaQui );
	lRedirMonocratica.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "true");
	
	//Collegiale
	RedirectTo lRedirCollegiale = new RedirectTo();
	lRedirCollegiale.setAction("siap.sige.udienzacollegiale.action.ActLoadInserisciUdienzaCollegiale");	
	lRedirCollegiale.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
	lRedirCollegiale.setParameter("TornaQui", TornaQui );
	lRedirCollegiale.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "true");
	lRedirCollegiale.setParameter("PopUp", "yes");
   	
   	String lLinkUdienzaInserimentoUdienzaMonocratica= lRedirMonocratica.toString();
   	String lLinkUdienzaInserimentoUdienzaCollegiale = lRedirCollegiale.toString();
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Ordinanza Rinvio Udienza da Verbale </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/jquery-1.6.2.min.js"></script>
  
  <script language="JavaScript">
  
  
	function controllaRuolo()
	{
  	    // Controllo presenza del valore del rinvio ruolo
	 	var a='<%=flagRinvio %>';
	 	if(a == 'N'){
	 		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked = true;	 		
	 	}
	 	else{
	 		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked = false;
	 	}
	}

  	//
  	// Controllo tipo Rito
  	//
		function controlloTipoRito()
		{
	  	if ( document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "-" )
	  	{
	  		alert('Selezionare il tipo rito!');
	  		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.focus();
	    	return false;
	  	}
	  	return true;
		}

  	//
		// Controllo luogo svolgimento udeinza.
		//
		function controlloLuogoUdienza()
		{
			// Se non è rinviata a nuovo ruolo esegue il controllo.
			if( !document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked )
			{	
	    	if ( document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>.value == "-"
		  		|| document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>.value == "" )
		  	{
		  		alert('Inserire un Luogo svolgimento udienza!');
		  		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>.focus();
		    	return false;
		  	}
			}
		  return true;
		}

		//	
		// Controllo Oggetti.
		//
		function controlloOggetti()
		{
			// Controllo obbligatorietà presenza almeno un Oggetto.
		 	var numOggetti=<%=tenori.size()%>;
		  if(numOggetti == 0)
		  {
		  	alert('Occorre individuare almeno un oggetto del Procedimento SIGE!');
		    return false;
		  }
			return true;	
		}

		//
		// Verifica che esista almeno un avvocato associato al fascicolo.
		//
		function controlloAvvocato()
		{
		<%
			int numAvvocati = avvocato.size();
		  if ( numAvvocati == 0) 
		  { 
		%>
				alert('Il Difensore è obbligatorio!');
		    return false;
		<%
			} else { 
		%>
		   	return true;
		<%
			}
		%>
		}

		//
		// Verifica del Magistrato Assegnatario
		//
		function  controlloMagistrato()
		{
	    var ritorno = true;
	    <% 
	    	if (magistratoassegnatario == null ){%>
	    		alert ('Magistrato non assegnato!');
	        return false;
	    <% 
	    	} 
	    %>
	    return true;
		}

		//
		// Verifica data di emissione.
		//
		function controlloDataEmissione()
		{
			var ritorno = true;
			
		  	var dataSistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		  	var dataUdienzaFissata = '<%=lDataUdienza%>';
		  	
		  	
	    	var dataEmissione = 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+ 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+ 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

	    		
		    if (dataEmissione.length > 2)
		    {
		    	if (!ControllaData(dataEmissione))
		      	{
		      		alert('Data di emissione non corretta.');
				  	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		        	ritorno = false;
		      	}
			    else if( !CompareDate( dataEmissione, dataSistema) ) //1) Controllo data di sistema >= Data Emissione .
			    {
			      	alert('La data di emissione non può essere superiore alla data odierna!');
				  	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			      	ritorno = false;
			    }
			    else if( !CompareDate( dataUdienzaFissata, dataEmissione ) )
			    {	
				    if(window.confirm('A T T E N Z I O N E : \n La DATA EMISSIONE è INFERIORE alla DATA UDIENZA fissata!\n Premi "OK" se vuoi CONTINUARE comunque. \n Premi "ANNULLA" per CAMBIARE la DATA EMISSIONE'))
				    {
				    	
				    }
				    else
				    {	
				  		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
				    	ritorno = false;
				    }	
				}
		    }  
		    else
		    {
		    	alert('La data di emissione è obbligatoria.');
		    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		     	ritorno = false;
			}

			return ritorno;

		}	// Chiude  function controlloDataEmissione()


		//
		// Verifica data rinvio udienza.
		//
		function controlloDataUdienza()
		{
			var ritorno = true;
									
		  var dataSistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		  var dataUdienzaFissata = '<%=lDataUdienza%>';
	    var dataUdienza = 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+ 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+ 
	    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;

	    if (dataUdienza.length > 2)
	    {
	    	if (!ControllaData(dataUdienza))
	      {
	      	alert('Data udienza non corretta!');
			  	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
	        ritorno = false;
	      }
	    }  
	    else if( !document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked ) 
	    {
	    	alert('Valorizzare la data di rinvio o selezionare nuovo ruolo.');
	    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
	     	ritorno = false;
		  }
	    else if( dataUdienza.length > 2 &&
	    	    	 document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked ) 
	    {
	    	alert('Indicare la data di rinvio udienza o rinvio a nuovo ruolo.');
	    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.focus();
	     	ritorno = false;
		  }
		  
			return ritorno;
		}

	    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
	    {
	       desktop = 
	           window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	    }
  </script>

	<script language="JavaScript">
  	function Verify()
  	{
  	  // controlloMagistrato.	
			if( !controlloMagistrato() )
				return false;
			// controlloAvvocato.
			if( !controlloAvvocato() )
				return false;
			// controlloTipoRito.
			if( !controlloTipoRito() )
				return false;
			// controlloDataEmissione.
			if( !controlloDataEmissione() )
				return false;
			// controlloOggetti.
			if( !controlloOggetti() )
				return false;
			// controlloDataUdienza.
			if( !controlloDataUdienza() )
				return false;
			// 31/10/2019: eliminiamo il controllo di blocco dopo avere sentito Nunzia (Ticket otrs #201910250118)
			// controlloLuogoUdienza.
/* 			if( !controlloLuogoUdienza() )
				return false; */
							
    	return true;
  	}

	</script>

  <script language="JavaScript">
    var desktop;

		//
    // Chiamata funzione elenco Udienze.
    //
    function ListaUdienze( aNomeForm, aNomeCampoGG, aNomeCampoMM, aNomeCampoAA, aNomeCampoLuogo, aNomeCampoIdUdienza, aNomeCampoCollegio, aTipoRito)
    {
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienza.action.ActLoadRicercaUdienzePerProcedimento";
          lLink += "&formname="+ aNomeForm;
          lLink += "&campoGG=" + aNomeCampoGG;
          lLink += "&campoMM=" + aNomeCampoMM;
          lLink += "&campoAA=" + aNomeCampoAA;
          lLink += "&campoLuogo=" + aNomeCampoLuogo;
          lLink += "&campoID=" + aNomeCampoIdUdienza;
          lLink += "&campoColl=" + aNomeCampoCollegio;
          lLink += "&tipoRito=" + aTipoRito;
          desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550" );
    }

    //
    // Funzione JS per chiamata azione inserimento udienza.
    //   
     <%-- 20171019: [ec] modificata la logica dentro, apiva sempre la monocratica --%>   
	function InserisciUdienza(dataUdienza, idSezione) {		
	    var tipoRito=document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value;	
	    var lLink;
	    if( tipoRito == 'M' ){	 
	    	lLink = "<%=lLinkUdienzaInserimentoUdienzaMonocratica%>&DataUdienza="+dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>="+idSezione;
	    	// 20190506 [SG]: resize
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500");
		} else if( tipoRito == 'C' ){			
			lLink = "<%=lLinkUdienzaInserimentoUdienzaCollegiale%>&DataUdienza="+dataUdienza + "&<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>="+idSezione;
			// 20190506 [SG]: resize
			desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=750,height=500");
		} else {
			alert("Definire il tipo rito");
			return;
		}
	}
    
		
  </script>

  <script language="JavaScript">

    // Funzione di pulizia data udienza se rinvio per ruolo.
    function CheckUncheck()
    {
      if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked )
      {
        if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%> !=null)
          document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%> !=null)
          document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%> !=null)
          document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value = "";
        if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO %> !=null)
          document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value = "";
      }
    }

    function Uncheck()
    {
      document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.value = "1";
      document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>.checked = false;
    }
    
    function loadAula (aIdUdienza) {
    	$.ajaxSetup({cache: false});
    	$.ajax({
    	    url: '/jsp/files/siap/sige/udienza/AulaData.jsp',
    	    type: 'GET',
    	    cache: false,
    	    data: {idUdienza: aIdUdienza},
    	    success: function(data){ 
    	    	var obj = jQuery.parseJSON (data);
    	    	
    	    	$('input[name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>"]').val(obj.oraInizio);
    	    	$('input[name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>"]').val(obj.minInizio);
    	    	$('input[name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"]').val(obj.oraFine);
    	    	$('input[name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"]').val(obj.minFine);
    	    	$('input[name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>"]').val(obj.minFine);
    	    	$('input[name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"]').val(obj.aula);
    	    	$('input[name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"]').val(obj.ingresso);
    	    	$('input[name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"]').val(obj.piano);
    	    	$('input[name="<%=ICostantiAula.CAMPO_ID_AULA%>"]').val(obj.idAula);
    	    	$(".<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%> select").val(obj.idSezione);
    	    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value=obj.idSezione;
    	    },
    	    error: function(data) {
    	        // alert('ERROR @@@@');
    	    }
    	});    	
    }
    
    function ListaAule() {
		
		idSezione=document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value;
    	if ( idSezione == "" || idSezione == "-" ) {
			alert('Selezionare una sezione!');
	    } else {
			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.aula.action.ActRicercaAula&<%=IWebConstants.POPUP_PAGE%>=yes&formname=LoadInserisciVerbaleRinvioUdienza&<%=ICostantiAula.CAMPO_ID_SEZIONE%>="+idSezione, "Ricerca_Aule", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	    }
	}
    
    function VerificaChiamataInserisciUdienza(){

    	// Controllo validità data Udienza.
	    if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value.length==1)
	        document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value;

	    if (document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value.length==1)
	        document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value='0'+document.LoadInserisciFissazioneUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value;
    	
	    var dataUdienza= document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
	                     document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>.value +'/'+
	                     document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>.value;
        
	    // [EC] 20171019 : recupero la sezione e se specificata la passo alla funzione InserisciUdienza
   	    var idSezione = '';
   	    if(document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value != '-'){
   	    	idSezione = document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value;
   	    }
	                	    
	    if (dataUdienza.length>2) {
	    	if (! ControllaData(dataUdienza)) {
	    		alert('Data Udienza non valida!');
	    		return;
	    	} 

	    }
        InserisciUdienza(dataUdienza, idSezione);
    }
    
    <%-- 20171019: [ec] aggiunto parametro in più per sezioni --%>    
    <%-- intervento per 11.2.1 aggiunto il parametro codMagistrato--%>
    function setUdienza (idUdienza, dataUdienza, idCollegio, idSezione, codMagistrato) {
    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>.value=idUdienza;
    	// 20190517 [SG]: aggiunto controllo preventivo se dalla popup torno indietro senza aver inserito
		if (dataUdienza != "null") {
    	var dataSplitted=dataUdienza.split("-");
    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA %>.value=dataSplitted[0];
    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA %>.value=dataSplitted[1];
    	document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA %>.value=dataSplitted[2];    	 
		}
		// AGGIUNGO PER INTERVENTO PER PROBLEMATICA ORDINANZE DI RINVIO UDIENZE SU ESERCIZION 11.2.1
		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value = idCollegio;
		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>.value = idUdienza;
		
    	if (idSezione != null) {
    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>.value = idSezione;
    		// svuoto i campi aula, ingresso e piano
    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>.value = "";
    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>.value = "";
    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiAula.CAMPO_NUMERO_PIANO%>.value = "";
    	}
    	<%-- intervento per 11.2.1 --%>
    	if (codMagistrato != null) {
    		document.LoadInserisciVerbaleRinvioUdienza.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value = codMagistrato;    		
    	}
    }
  </script>
</head>

<!-- body class="corpo" onload="Javascript:return ControlloAvvocato();"-->
<body class="corpo" onload="Javascript:return controllaRuolo();" >

  <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
      <td class="LBG">
      	<font class="label"> Funzione :</font>&nbsp;
<%
    String lAzione = "siap.sige.udienzaprocedimento.action.ActModificaOrdinanzaVerbaleRinvioUdienza";

%>
       	<font class="campo">Modifica Ordinanza Rinvio Udienza da Verbale</font>
      </td>

  	<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <tr>
	  	<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    </tr>
   </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" 
				name="LoadInserisciVerbaleRinvioUdienza">
				
	  <table cellspacing="2" cellpadding="2" width="100%">
  	  <tr>
	  	  <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
      		<jsp:param name="MagAssRitorno" value="siap.sige.udienzaprocedimento.action.ActLoadModificaOrdinanzaRinvioUdienza"/>
    		</jsp:include>
    	</tr>
    	
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      	<jsp:param name="AvvRitorno" value="siap.sige.udienzaprocedimento.action.ActLoadModificaOrdinanzaRinvioUdienza"/>
      </jsp:include>
  	</table>
	<br>

  <table cellspacing="2" cellpadding="2" width="100%">
   <tr>
    <td class="label">Tipo rito <font class="ob">(*)</font> &nbsp;&nbsp;&nbsp;&nbsp;
      <select title="Tipo rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>">
        <%=tipoGiudizio%>
      </select>
    </td>
   </tr>

  </table>
	
  <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
      <td class="Titolo" colspan=6>Estremi Ordinanza</td>
    </tr>
    <tr>
      <td class="l">Data Emissione <font class="ob">(*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getDateToString(dataEmissione,"dd") %>" type="text" size="2" 
							 maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" 
							 onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)"
							 onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(dataEmissione,"MM") %>" type="text" size="2" 
							 maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" 
							 onFocus="javascript:textboxSelect(this)"
							 onkeypress="return TicTabNumField(this,event)"
							 onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(dataEmissione,"yyyy") %>" type="text" size="4" 
							 maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" 
							 onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillYear(value)" >

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciVerbaleRinvioUdienza','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>

      </td>
    </tr>

    <!-- Sezione Oggetti -->

    <tr>
  		<td class="L">
  		  <table cellspacing=1 cellpadding=1  width="100%" border="0">
  			<tr>
    		 <td class="label" width=15% colspan=2>
             	<a class="cliccabile" href="<%=lLinkOggettiSessione%>">
       	 			Oggetti
      			</a>
      		 </td>
  			</tr>
  			</table>
  		</td>
  			
   		<td  class="L">
  			<div id="elenco1" style="width:100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoTenoriSige.jsp"/>
  			</div>
	 		</td>
		</tr>
		
    <tr>
			<td>&nbsp;</td>
		</tr>
		
<tr><td>&nbsp;</td></tr>

    <tr>
      <td class="Titolo" colspan="4">Estremi Udienza</td>
    </tr>
    <tr>
      <td class="l">Data Udienza </td>
      <td class="l" colspan="3">
        <input value="<%=gg %>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>"
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  
        	maxlength="2" size="2">
        /
        <input value="<%=mm %>" type="text" name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  
        	maxlength="2" size="2">
        /
               
        <input value="<%=aaaa %>" type="text" name="<%= ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  
        	maxlength="4" size="4">
        &nbsp;
        
        <a class="cliccabile" href="Javascript:VerificaChiamataInserisciUdienza();">Inserimento Udienza</a>
      </td>	
    </tr>

<!--     <tr>
			<td class="l">oppure</td>
		</tr> -->
    <tr>
			<td class="l" colspan="6">Nuovo ruolo &nbsp;
				<input type="checkbox" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_CHECK_NUOVO_RUOLO%>" onClick ="Javascript:CheckUncheck()" >
			</td>
		</tr>
    
		<tr>
			<td>&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan=6>&nbsp;
    		<input type="hidden" name="<%=ICostantiUdienzaSige.CAMPO_COD_AVVOCATO%>" value="" >
			</td>
		</tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
    <input type="HIDDEN" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" value="<%=idUdienzaProcedimentoSige%>" >
    <input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE %>" value="<%=idUdienzaSige%>" >
     <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"  value="">
    <%-- AGGIUNGO PER INTERVENTO PER PROBLEMATICA ORDINANZE DI RINVIO UDIENZE SU ESERCIZION 11.2.1--%>
    <input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" value="">
    
<div id="AltriDatiUdienzaDiv" style="position:relative;  top: 0; left: 0;   visibility:hidden;" >  
<table>    
    <tr>
      <td class="l">Sezione</td>
      <td class="l">
        <select title="sezione" name="<%=ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA%>" >
        	<%=elencoSezioniUdienza%>
        </select>
      </td>
      <td class="l" colspan="2">
      Aula
        <input type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>" value="<%=descrizioneAula %>" size="12"  readonly>&nbsp;&nbsp;  
      Ingresso
        <input type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>" value="<%=ingressoAula %>" size="12"  readonly>&nbsp;&nbsp;
      Piano
        <input type="text" name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>" value="<%=pianoAula %>" size="12"  readonly>&nbsp;&nbsp;
        <input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>" value="<%=idAula %>" >
		<a href="Javascript:ListaAule ();"><img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l">
        <input type="text" maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
               value="<%=oraInizio %>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_INIZIO%>">
			:  
        <input type="text" maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
               value="<%=minInizio %>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_INIZIO%>">
      </td>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l">
        <input type="text" maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
               value="<%=oraFine %>"
               name="<%=ICostantiUdienzaSige.CAMPO_ORA_FINE%>"> 
				: 
        <input type="text" maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"
               value="<%=minFine%>"
               name="<%=ICostantiUdienzaSige.CAMPO_MIN_FINE%>">
      </td>
    </tr>

    <tr>
      <td class="l">Luogo svolgimento </td>

      <td class="l" colspan="3">
      	<input type="text" name="<%=ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO%>" value="<%=luogoSvolgimento%>" size="95">
      	<a href="Javascript:cleanLuogoSvolgimento();">
      		<img src="/images/delete.gif" width="12" height="12" alt="Pulisci Campo" border="0">
      	</a>
      </td>
    </tr>
    </table>
  </div>

  </form>

<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciVerbaleRinvioUdienza");
// Controllo data emissione.
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

// Controllo campo oggetto.
<%-- frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");--%>

// Controllo data udienza.
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>","req", "Il campo Giorno Data Udienza è obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>","req", "Il campo Mese Data Udienza é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","req", "Il campo Anno Data Udienza é obbligatorio"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","numeric"); --%>
<%--     frmvalidator.addValidation("<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri"); --%>

//Chiama la funzione di Verify().
frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>