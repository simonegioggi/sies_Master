<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PeriodoLibAntCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPeriodoLibAntCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
  
<jsp:useBean id="tipoProvvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"          scope="request" class="java.lang.String"/>
<jsp:useBean id="EsitoProvvedimento"   scope="request" class="java.lang.String"/>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="StatoEsecTitoloCum"   	scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<!-- 		LoadInserisciLiberazioneAnticipataCumulo		 -->

<%
//=====================================================================================================
// form per l'inserimento del Provvedimento di Liberazione Anticipata relativa ad un Titolo Cumulato
//====================================================================================================

String lAzione = new String();
lAzione = "siap.siep.modulocumulo.action.ActInserisciLiberazioneAnticipataCumulo";

%>
<html>

<head>
  <title> [S.I.E.S.] - Liberazione Anticipata LA - Titolo Cumulato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>
  
  <script language="JavaScript">
    
  function InserimentoManuale()
  {
    resetDati();
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>.value="";
    
    // Disabilito le DIV dei dati selezionati dalla lista
    document.getElementById("datiSorvLA").style.display='none';
    document.getElementById("datiSorvLA_SPE").style.display='none';
    document.getElementById("datiSorvLA_INT").style.display='none';

    document.getElementById("partecomune").style.display='block'; 
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked=true; 
    
    QualeLAConcede('2130')

    // Visualizza la DIV LA Ordinarie Manuali
    //document.getElementById("datiPeriodiLA").style.display='block';
    
    
  }
  
  function resetDati()
  {
    resetDatiProvvSIUS();
    resetDatiSelezionaLista();
    resetDatiManuali();
  }
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati selezionati dalla lista
  //==============================================================================
  function resetDatiProvvSIUS()
  {
    // Anche in dati del provvedimento SIUS
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value="";
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value="";
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>.value="";
    
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value="";
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value="";
    
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex =0;

    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value="";
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value="";

    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.selectedIndex =0;
    document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>.value="";
  }
  
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati selezionati dalla lista
  //==============================================================================
  function resetDatiSelezionaLista()
  {
    // DIV LA Ordinaria
    document.InserisciLACum.giorniLA.value="";
    document.InserisciLACum.NumGiorniLibanticipataSorv.value="";
    <% for (int s=0;s<6;s++) { %>
    document.InserisciLACum.InizioLA<%=s%>.value="";
    document.InserisciLACum.FineLA<%=s%>.value="";
    <% } %>   
    
    // DIV LA Speciale
    document.InserisciLACum.giorniLA_SPE.value="";
    document.InserisciLACum.NumGiorniLibanticipataSorv_SPE.value="";
    <% for (int s=0;s<6;s++) { %>
    document.InserisciLACum.InizioLA_SPE<%=s%>.value="";
    document.InserisciLACum.FineLA_SPE<%=s%>.value="";
    <% } %>    
    
    // DIV Integrazione LA
    document.InserisciLACum.giorniLA_INT.value="";
    document.InserisciLACum.NumGiorniLibanticipataSorv_INT.value="";
    <% for (int s=0;s<6;s++) { %>
    document.InserisciLACum.InizioLA_INT<%=s%>.value="";
    document.InserisciLACum.FineLA_INT<%=s%>.value="";
    <% } %>     
  }
  
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati inseriti manualmente
  //==============================================================================
  function resetDatiManuali()
  {
    // LA Ordinaria
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>.value="";

    <% for (int k=0; k<6; k++) { %>
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>[<%=k%>].value="";

    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>[<%=k%>].value="";
    <% } %>
    
    // LA Speciale
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value="";
    <% for (int k=0; k<6; k++) { %>
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE%>[<%=k%>].value="";

    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_SPE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_SPE%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_SPE%>[<%=k%>].value="";
    <% } %>
    
    // LA Integrazione
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value="";

    <% for (int k=0; k<6; k++) { %>
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT%>[<%=k%>].value="";

    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_INT%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_INT%>[<%=k%>].value="";
    document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_INT%>[<%=k%>].value="";
    <% } %>
  }
    
    
  //========================================================================================
  //  Abilitazione Iniziale dei radioButton per selezionare gli Oggetti L.A.  
  //========================================================================================
  function Inizia()
  {
	  //alert("Inizia");
	  var modo=document.InserisciLACum.modalita.value;
	  if(modo=='I')
	  {
			InserimentoManuale();
	  }
  
  }
    
  //========================================================================================
  //  Selezione del Tipo di L.A. da visualizzare (L.A., L.A. speciale, Integrazione L.A. ), 
  //    per poter poi inserire i periodi concessi                         
  //========================================================================================
  function QualeLAConcede(cod)
  {
      //alert("QualeLAConcede - Codice Scelto = "+cod);
      
      if(cod == 2130)   //  E' stato selezionato il Radio Button L.A. NORMALE, quindi .....   
      {
        DisabilitaLA_INT();
        DisabilitaLA_SPE();
        AbilitaLA();
      }
      
      if(cod == 2131)   //  E' stato selezionato il Radio Button L.A. SPECIALE, quindi .....    
      {
        DisabilitaLA_INT();
        DisabilitaLA();
        AbilitaLA_SPE();
      }
      
      if(cod == 2132)   //  E' stato selezionato il Radio Button L.A. INTEGRAZIONE, quindi .....    
      {
        DisabilitaLA();
        DisabilitaLA_SPE();
        AbilitaLA_INT();
      }
  }   
    
  //========================================================================================
  // ...Abilito le DIV per la gestione dei periodi L.A. normale
  //========================================================================================
  function AbilitaLA()
  {
    //alert("AbilitaLA");
    node=document.getElementById("datiPeriodiLA");
    node.style.display='block';
    
    var valoreLA = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>.value = valoreLA;
  }
  
  //========================================================================================
  // ... Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. normale
  //========================================================================================
  function DisabilitaLA()
  {
    //alert("DisabilitaLA");
    var salvaggLA = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA %>.value = salvaggLA;
    
    node=document.getElementById("datiPeriodiLA");
    node.style.display='none';
  }


  //========================================================================================
  // Abilito le DIV per la gestione dei periodi L.A. SPECIALE
  //========================================================================================
  function AbilitaLA_SPE()
  {
  //  alert("AbilitaLA_SPE");
    node=document.getElementById("datiPeriodiSPE");
    node.style.display='block';
    
    var valoreLS = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value = valoreLS;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. SPECIALE
  //========================================================================================
  function DisabilitaLA_SPE()
  {
    //alert("DisabilitaLA_SPE");
    var salvaggLS = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value = salvaggLS;
    
    node=document.getElementById("datiPeriodiSPE");
    node.style.display='none';
  }
    
    
  //========================================================================================
  // Abilito le DIV per la gestione dei periodi L.A. INTEGRAZIONE
  //========================================================================================
  function AbilitaLA_INT()
  {
    //alert("AbilitaLA_INT");
    node=document.getElementById("datiPeriodiINT");
    node.style.display='block';
    
    var valoreLI = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value = valoreLI;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e  DISABILITO le DIV per la gestione dei periodi L.A. INTEGRAZIONE
  //========================================================================================
  function DisabilitaLA_INT()
  {
    //alert("DisabilitaLA_INT");
    var salvaggLI = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value;
    document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value = salvaggLI;
    
    node=document.getElementById("datiPeriodiINT");
    node.style.display='none';
  }
  
  
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {
	      // - Campo Totale giorni da scomputare obbligatorio
	        var obbligatori;
	        
	      if(document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value == 0 && 
	      document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>.value == 0  &&
	      document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT%>.value == 0 )
	      {
	        if(document.InserisciLACum.NumGiorniLibanticipataSorv.value == 0 && 
	          document.InserisciLACum.NumGiorniLibanticipataSorv_SPE.value == 0  &&
	          document.InserisciLACum.NumGiorniLibanticipataSorv_INT.value == 0 )
	        {
	            alert("digitare il Totale Giorni e almeno un oggetto L.A.  ");
	            return false; 
	        }    
	      }
  
//  LIBERAZIONE  ANTICIPATA
	     var dateInserite=0;
	     for (var T=0; T<6; T++)
	     {
	        if(document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>[T].value !=""   &&
	           document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>[T].value !=""    &&
	           document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>[T].value !=""  )
	        {
	              dateInserite++;
	        }
	          
	     	if(dateInserite!= 0)
	            break;
	     
	     }	
	
         if(dateInserite != 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value == 0)
	     {
		        DisabilitaLA_INT();
		        DisabilitaLA_SPE();
		        AbilitaLA();
		        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked = true;
	            alert('Inserire totale giorni di Liberazione Anticipata ');
	            document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.focus();
	            return false;
	     }
       
         if(dateInserite == 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value != 0)
 	     {
 		        DisabilitaLA_INT();
 		        DisabilitaLA_SPE();
 		       	document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA%>.value = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value;
 		        AbilitaLA();
 		        
 		        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked = true;
 	            alert('Inserire Almeno un Periodo di Liberazione Anticipata ');
 	            document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>[0].focus();
 	            return false;
 	     }
       
//
//  LIBERAZIONE ANTICIPATA  SPECIALE
        var dateInserite=0;
        for (var Tspe = 0; Tspe < 6; Tspe++)
        {
	          if(document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE %>[Tspe].value !="" &&
	             document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE %>[Tspe].value !=""   &&
	             document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE %>[Tspe].value !="" )
	          {
	              dateInserite++;
	          }
	          
	          if(dateInserite!= 0)
	              break;
        }

        if(dateInserite != 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value == 0)
        {
	        DisabilitaLA_INT();
	        DisabilitaLA();
	        AbilitaLA_SPE();
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[1].checked = true;
            alert('Inserire totale giorni di Liberazione Anticipata Speciale ');
            document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.focus();
            return false;
        }
        
        if(dateInserite == 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value != 0)
        {
	        DisabilitaLA_INT();
	        DisabilitaLA();
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE%>.value = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>.value;
	        AbilitaLA_SPE();
	        
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[1].checked = true;
            alert('Inserire Almeno un Periodo di Liberazione Anticipata Speciale ');
            document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE %>[0].focus();
            return false;
        } 
//
//  INTEGRAZIONE  LIBERAZIONE  ANTICIPATA
        var dateInserite=0;
        for (var Tint = 0; Tint < 6; Tint++)
        {
	          if(document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT %>[Tint].value !=""
	          && document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT %>[Tint].value !=""
	          && document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT %>[Tint].value !="")
	          {
	              dateInserite++;
	          }
	          
	          if(dateInserite!= 0)
	              break;
        }

        if(dateInserite != 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value == 0)
        {
	        DisabilitaLA();
	        DisabilitaLA_SPE();
	        AbilitaLA_INT();
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[2].checked = true;
            alert('Inserire totale giorni di Integrazione Liberazione Anticipata ');
            document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.focus();
            return false;
        }
 
        if(dateInserite == 0 && document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value != 0)
        {
	        DisabilitaLA();
	        DisabilitaLA_SPE();
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT%>.value = document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT%>.value;
	        AbilitaLA_INT();
	        
	        document.InserisciLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[2].checked = true;
            alert('Inserire Almeno un Periodo di Integrazione Liberazione Anticipata ');
            document.InserisciLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT%>[0].focus();
            return false;
        }

        //------------------------------------------------------------------------------------------------
      	// Data Emissione Provvedimento Sorveglianza
      	//------------------------------------------------------------------------------------------------
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value.length==1)
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value='0'+document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value;
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value.length==1)
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value='0'+document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value;
  
        var data_to_verify = document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value+'/'+document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value+'/'+document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>.value;
        if (data_to_verify=="//")
        {
        	alert('Data Emissione obbligatoria');
            document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
            return false;
        }
        else if (!ControllaData(data_to_verify))
        {
            alert('Data Emissione non valida');
            document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
            return false;
        }
        //if (!ControllaDataPassaVuota(data_to_verify) )
        //{
        //  alert('Data di emissione Provvedimento non valida');
        //  document.InserisciLACum.< %=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
        //  return false;
        //}
      	
        //--------------------------------------------------------------------------------------------------------------------
      	// Tipo provvedimento
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-")
        {        
          alert("Selezionare Tipo Provvedimento");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
          return false;
        }
        
        // Anno e Numero Provvedimento
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value == "" &&
      		 document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value != "")
        {        
          alert("Inserire Anno Provvedimento");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.focus();
          return false;
        }
        
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value != "" &&
       		 document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value == "")
        {        
           alert("Inserire Numero Provvedimento");
           document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.focus();
           return false;
        }
        
     	// Anno e Numero Procedimento SIUS
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value == "" &&
      		document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value != "")
        {        
          alert("Inserire Anno Procedimento SIUS");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
          return false;
        }
        
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value != "" &&
       		 document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value == "")
        {        
           alert("Inserire Numero Procedimento SIUS");
           document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.focus();
           return false;
        }
      
      	// Tipo Autorità emittente 
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.value == "-")
        {        
          alert("Selezionare Autorità Emittente");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.focus();
          return false;
        }
      
     	 // Sede Autorità emittente
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>.value=="")
        {        
          alert("Selezionare Sede Autorità Emittente");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>.focus();
          return false;
        }
      
      	// Esito
        if (document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="-")
        {        
          alert("Selezionare un Esito");
          document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.focus();
          return false;
        }
        
      return true;
    
    } // CHIUDE function verify()

// 20/05/2014 - Nuova L.A - DL 146/2013

  function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function eseguiFunzione(action)
  {
    document.InserisciLACum.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.InserisciLACum.submit();
  }  
    
  </script>
</head>


<body class="corpo" onLoad="Javascript:Inizia();">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione : </font>&nbsp;&nbsp;
        <font class="campo">Inserimento Liberazione Anticipata</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaLiberazioneAnticipataCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>
  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="InserisciLACum">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="hidden" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="">
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" value="" >

<%
//==============================================================================
//                        Dati della Sorveglianza
//==============================================================================
%> 
  <table width="95%">
    <tr>
      <td colspan=4 class="titolo">Dati del Provvedimento </td>
    </tr>
    
    <tr>
      <td class="l" width="15%" >Data emissione provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>" 
        	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>"   
        	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>"   
        	maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"   value="" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4"   
         	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" type="text" size="6" maxlength="6" 
         	onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>   
    
    <tr>
      <td class="l" width="15%" >Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>" >
          <%=tipoProvvedimento%>
        </select>
      </td>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" value="" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        /
        <input Title="Numero Sius" value=""  onkeypress="return TicTabNumField(this,event)"
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" type="text" size="6" maxlength="6">
      </td>
    </tr>    


    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Autorità Emittente" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>">
          <%=autoritaEmi%>
        </select>
      </td>
      <td class="l" width="15%" >Sede <font class="ob">(*)</font> &nbsp; </td>
      <td class="l" >
        <input title="Sede Autorita"  type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>"  
        	value="" maxlength="30" size="30">
        <a href="Javascript:ListaUfficiComuni('InserisciLACum','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>',
        			document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>[document.InserisciLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    
    <tr>
      <td class="l" width="15%" >Esito del Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
		<select Title="Esito Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>" >
          <%=EsitoProvvedimento%>
        </select>	
      </td>
    </tr>
  </table>
  <br>
<%
//==============================================================================
// Sezione con la scelta tipo LA per compilazione manuale.
// n.b. viene nascosta dalla PopUp in caso di "seleziona dalla lista"
//==============================================================================
%>
<div style="display:block" id="partecomune" > 
  <table width="100%">
    <tr>
      <td class="L">
        <font class="label" style="color:green; font-size: 10pt">
         Selezionare gli Oggetti L.A. di interesse (uno per volta) e inserire i rispettivi n.ro giorni/periodi
        </font>
      </td>
    </tr>
  </table> 
  
  <table cellspacing="2" cellpadding="2" width=90%>
    <tr>
      <td width="30%" class="l">
        <span id="LA" style="color=blu;font-weight:bold;"> Liberazione Anticipata  </span> 
        <input value="" type="radio" name="<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>"  onclick="Javascript:return QualeLAConcede('2130');" CHECKED>
      </td>
      <td width="30%" class="l">
        <span id="LS" style="color=blu;font-weight:bold;"> Liberazione Anticipata Speciale </span> 
        <input value="" type="radio" name="<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>" onclick="Javascript:return QualeLAConcede('2131');">
      </td>
      <td width="30%" class="l">
        <span id="LI" style="color=blu;font-weight:bold;"> Integrazione Liberazione Anticipata </span> 
        <input value="" type="radio" name="<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>" onclick="Javascript:return QualeLAConcede('2132');">
      </td>
    </tr>
  </table>
</div>

<!-- le div "datiSorvLA", "datiSorvSPE", "datiSorvINT" sono sempre hidden TRANNE in caso di modifica.........->

<!--  "datiSorvLA"  Liberazione Anticipata  -->
<%
//==============================================================================
// DIV Visualizzate in caso di provvedimento della Sorveglianza selezionato
// dalla lista. I dati non sono modificabili evengono caricati dalla popup
// 
//==============================================================================
%>

<div style="display:none" id="datiSorvLA" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Liberazione Anticipata</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Liberazione Anticipata Revocati</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA" size="4" value="" disabled>
        </font>
      </td> 
      <td class="l" width="45%">Totale giorni da scomputare (Liberazione Anticipata) (*)</td>
      <td class="L" width="5%">
        <font class="campo">
          <input Title="TotGiorni" name="NumGiorniLibanticipataSorv" value="" size="5" maxlength="4"
                 onkeypress="return TicTabNumField(this,event)" >
        </font>
      </td>
    </tr>
  </table>
  
  <table width=90%>
    <tr>   
      <%for (int s=0;s<3;s++) 
      {    %>
        <td class="l"><font class="label"><%=s+1 %>)</font></td>
          <td class="L" >
            <font class="campo">
            <input type="text" name="InizioLA<%=s%>" size="12" value="" disabled>
            &nbsp; - &nbsp;
            <input type="text" name="FineLA<%=s%>" size="12" value="" disabled>
            </font>
          </td>
      <%} %>
    </tr>
    <tr>   
      <%for (int s=3;s<6;s++) 
      {    %>
        <td class="l"><font class="label"><%=s+1 %>)</font></td>
          <td class="L" >
            <font class="campo">
            <input type="text" name="InizioLA<%=s%>" size="12" value="" disabled>
            &nbsp; - &nbsp;
            <input type="text" name="FineLA<%=s%>" size="12" value="" disabled>
            </font>
          </td>
      <%} %>
    </tr> 
  </table>
</div>

<!--  "datiSorvLA_SPE"  Liberazione Anticipata Speciale -->
<div style="display:none" id="datiSorvLA_SPE" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Liberazione Anticipata Speciale</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Liberazione Anticipata Speciale Revocati</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA_SPE" size="4" value="" disabled>
        </font>
    </td> 
    <td class="l" width="45%">Totale giorni da scomputare (Liberazione Anticipata Speciale) (*)</td>
      <td class="L" width="5%">
        <font class="campo">
          <input Title="TotGiorni" name="NumGiorniLibanticipataSorv_SPE"  value="" size="5" maxlength="4"
                 onkeypress="return TicTabNumField(this,event)" >
        </font>
      </td>
    </tr>
  </table>
  <table width=90%>
    <tr>   
      <%for (int s=0;s<3;s++) 
      {    %>
        <td class="l"><font class="label"><%=s+1 %>)</font></td>
          <td class="L" >
            <font class="campo">
            <input type="text" name="InizioLA_SPE<%=s%>" size="12" value="" disabled>
            &nbsp; - &nbsp;
            <input type="text" name="FineLA_SPE<%=s%>" size="12" value="" disabled>
            </font>
          </td>
      <%} %>
    </tr>
    <tr>   
      <%for (int s=3;s<6;s++) { %>
        <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA_SPE<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA_SPE<%=s%>" size="12" value="" disabled>
          </font>
        </td>
      <%} %>
    </tr> 
  </table>
</div>

<!--  "datiSorvLA_INT"  Integrazione Liberazione Anticipata -->
<div style="display:none" id="datiSorvLA_INT" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Integrazione Liberazione Anticipata</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Integrazione Liberazione Anticipata Revocati</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA_INT" size="4" value="" disabled>
        </font>
      </td> 
      <td class="l" width="45%">Totale giorni da scomputare (Integrazione Liberazione Anticipata) (*)</td>
      <td class="L" width="5%">
        <font class="campo">
          <input Title="TotGiorni" name="NumGiorniLibanticipataSorv_INT"  value="" size="5" maxlength="4"
                 onkeypress="return TicTabNumField(this,event)" >
        </font>
      </td>
    </tr>
  </table>
  <table width=90%>
    <tr>   
      <%for (int s=0;s<3;s++) { %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
      <td class="L" >
        <font class="campo">
        <input type="text" name="InizioLA_INT<%=s%>" size="12" value="" disabled>
        &nbsp; - &nbsp;
        <input type="text" name="FineLA_INT<%=s%>" size="12" value="" disabled>
        </font>
      </td>
      <% } %>
    </tr>
    <tr>   
      <%for (int s=3;s<6;s++) { %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
      <td class="L" >
        <font class="campo">
        <input type="text" name="InizioLA_INT<%=s%>" size="12" value="" disabled>
        &nbsp; - &nbsp;
        <input type="text" name="FineLA_INT<%=s%>" size="12" value="" disabled>
        </font>
      </td>
      <%} %>
    </tr> 
  </table>
</div>

<% 
//==============================================================================
// Sezioni con i periodi e i giorni da computare in caso di compilazione 
// Manuale dei dati 
// CAMPI DATA DAL - AL   LIBERAZIONE ANTICIPATA
//==============================================================================
%>
<%
if(modalita.equals("I") )
{	%>
<div style="display:block; position: relative;" id="datiPeriodiLA" >  
<table width="99%">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Liberazione Anticipata da assegnare / Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++) { %>
  <tr>
    <% if (k==0) { %>
    <td class="l">
      Totale giorni Liberazione Anticipata da assegnare<font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni L.A." name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>" value="" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)"
             >
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
    </tr>
  <% } %>
  </table>
</div>
<%
 }	%>
<!--  END LIBERAZIONE ANTICIPATA -->

<!-- 
================================================================================
      Sezione DEI CAMPI DATA DAL - AL   LIBERAZIONE ANTICIPATA SPECIALE
================================================================================
-->
<%
if(modalita.equals("I"))
{	%>
<div style="display:none; position: relative;" id="datiPeriodiSPE" >  
<table width="99%">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Liberazione Anticipata Speciale da assegnare / Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++){ %>
  <tr>
    <% if (k==0){ %>
    <td class="l">
      Totale giorni Liberazione Anticipata Speciale da assegnare<font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni L.A. Speciale" name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>" value="" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)">
    </td>
    <%}else {%>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
      &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_SPE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <% } %>
</table>
</div>
<%
 }	%>
<!--  END LIBERAZIONE ANTICIPATA SPECIALE-->

<!-- 
================================================================================
      Sezione DEI CAMPI DATA DAL - AL   INTEGRAZIONE LIBERAZIONE ANTICIPATA 
================================================================================
-->
<%
if(modalita.equals("I"))
{	%>
<div style="display:none; position: relative;" id="datiPeriodiINT" >  
<table width="99%">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Integrazione Liberazione Anticipata da assegnare / Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++) {%>
  <tr>
    <% if (k==0){ %>
    <td class="l">
      Totale giorni Integrazione Liberazione Anticipata da assegnare <font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni Integrazione L.A." name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>" value="" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)" >
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
            Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <% } %>
</table>
</div>
<!--  END INTEGRAZIONE LIBERAZIONE ANTICIPATA -->
<%
 }%>

<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table width="95%">
  <tr>
    <td class="Titolo"  colspan="6">Dati relativi al Motivo inserimemto / modifica</td>
  </tr>

  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4">
     <textarea cols="100" rows="2" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>"></textarea>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" value="Conferma" name="bottConferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>

  <input type="HIDDEN" title="salva gg LA"     name="<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA %>"     value="">
  <input type="HIDDEN" title="salva gg LA SPE" name="<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>" value="">
  <input type="HIDDEN" title="salva gg LA INT" name="<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT %>" value="">
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("InserisciLACum");

    // Data Emissione
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>","gt=1900");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>