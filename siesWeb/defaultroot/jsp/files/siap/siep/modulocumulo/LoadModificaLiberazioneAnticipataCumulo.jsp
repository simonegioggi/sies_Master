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
  
<%// Caricamento combo altra autorità %> 
<jsp:useBean id="tipoProvvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"          scope="request" class="java.lang.String"/>
<jsp:useBean id="EsitoProvvedimento"   scope="request" class="java.lang.String"/>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="StatoEsecTitoloCum"   	scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoLiberazioneAnt"	scope="request" class="java.lang.String"/>

<!-- 		LoadModificaLiberazioneAnticipataCumulo		 -->

<%
//=====================================================================================================
// form per la Modifica del Provvedimento di Liberazione Anticipata relativa ad un Titolo Cumulato
//====================================================================================================

String lAzione = new String();
lAzione = "siap.siep.modulocumulo.action.ActInserisciLiberazioneAnticipataCumulo"; 

int totggLa=0;
int totggLaS=0;
int totggLaI=0;

String lIdLibLa="";
String lIdLibLaS="";
String lIdLibLaI="";

Vector<PeriodoLibAntCumuloModel> VecPerLA = new Vector<PeriodoLibAntCumuloModel>();
Vector<PeriodoLibAntCumuloModel> VecPerLAS = new Vector<PeriodoLibAntCumuloModel>();
Vector<PeriodoLibAntCumuloModel> VecPerLAI = new Vector<PeriodoLibAntCumuloModel>();

	Iterator ITX = StatoEsecTitoloCum.getListaLiberazioniAnticipate().iterator();
	while(ITX.hasNext())
	{
	  LibAnticipataCumuloModel LAModel = (LibAnticipataCumuloModel)ITX.next();
	  
	  if(LAModel != null && LAModel.getIdLibAnticipataCumulo() != null)
	  {
	     if("LA".equals(LAModel.getTipoLa() ) ) 
	     {
	   	  	lIdLibLa = LAModel.getIdLibAnticipataCumulo().toString();
	   	 	if(LAModel.getNumeroGiorni() != null && LAModel.getNumeroGiorni().intValue() > 0 )
	       		totggLa = LAModel.getNumeroGiorni().intValue();
		   	 	
	   	 	if(LAModel.getListaPeriodiLibAnticipate()!=null && LAModel.getListaPeriodiLibAnticipate().size() > 0 )
	   	 	{
	   	 		VecPerLA = LAModel.getListaPeriodiLibAnticipate();  
	   	 		//LogF3B.getLogger().debug("--XX-- VecPerLA size = "+VecPerLA.size());
	   	 	}

	     }
	     else if("LS".equals(LAModel.getTipoLa() ) ) 
	     {
	   	  	lIdLibLaS = LAModel.getIdLibAnticipataCumulo().toString();
	   	 	if(LAModel.getNumeroGiorni() != null && LAModel.getNumeroGiorni().intValue() > 0 )
	       		totggLaS = LAModel.getNumeroGiorni().intValue();
		   	 	
	   	 	if(LAModel.getListaPeriodiLibAnticipate()!=null && LAModel.getListaPeriodiLibAnticipate().size() > 0 )
	   	 	{
	   	 		VecPerLAS = LAModel.getListaPeriodiLibAnticipate();  
	   	 		//LogF3B.getLogger().debug("--XX-- VecPerLS size = "+VecPerLAS.size());
	   	 	}

	     }
	     else if("LI".equals(LAModel.getTipoLa() ) ) 
	     {
	   	  	lIdLibLaI = LAModel.getIdLibAnticipataCumulo().toString();
	   	 	if(LAModel.getNumeroGiorni() != null && LAModel.getNumeroGiorni().intValue() > 0 )
	       		totggLaI = LAModel.getNumeroGiorni().intValue();
		   	 	
	   	 	if(LAModel.getListaPeriodiLibAnticipate()!=null && LAModel.getListaPeriodiLibAnticipate().size() > 0 )
	   	 	{
	   	 		VecPerLAI = LAModel.getListaPeriodiLibAnticipate();  
	   	 		//LogF3B.getLogger().debug("--XX-- VecPerLI size = "+VecPerLAI.size());
	   	 	}
	     }
	     
	  }	 // Chiude if(LAModel != null 
	  
	}  // Chiude ciclo while()
	 
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
  
  var TipodiLA = '<%=TipoLiberazioneAnt%>';
  
  function Inizia()
  {
	  //alert(" Inizia - Tipo = "+TipodiLA);
	  if(TipodiLA=='LA')
	  {	 
		 //alert(" Inizi - Tipodi LA = "+TipodiLA);
		 document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value;
	  	 QualeLAConcede('2130');
	  	 document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked = true;
	  }
	  else if(TipodiLA=='LS')
	  {	 
		 //alert(" Inizi - Tipodi LA = "+TipodiLA);
		 document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>.value;
	  	 QualeLAConcede('2131');
	  	document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[1].checked = true;
	  }
	  else if(TipodiLA=='LI')
	  {	 
		 //alert(" Inizi - Tipodi LA = "+TipodiLA);
		 document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT%>.value;
	  	 QualeLAConcede('2132');
	  	document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[2].checked = true;
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
    
    var valoreLA = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>.value = valoreLA;
  }
  
  //========================================================================================
  // ... Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. normale
  //========================================================================================
  function DisabilitaLA()
  {
    //alert("DisabilitaLA");
    var salvaggLA = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA %>.value = salvaggLA;
    
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
    
    var valoreLS = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value = valoreLS;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. SPECIALE
  //========================================================================================
  function DisabilitaLA_SPE()
  {
    //alert("DisabilitaLA_SPE");
    var salvaggLS = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value = salvaggLS;
    
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
    
    var valoreLI = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value = valoreLI;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e  DISABILITO le DIV per la gestione dei periodi L.A. INTEGRAZIONE
  //========================================================================================
  function DisabilitaLA_INT()
  {
    //alert("DisabilitaLA_INT");
    var salvaggLI = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value;
    document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value = salvaggLI;
    
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
	        
	      if(document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value == 0 && 
	         document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>.value == 0  &&
	         document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT%>.value == 0 )
	      {
            alert("digitare il Totale Giorni e almeno un oggetto L.A.  ");
            return false; 
 
	      }
  
//  LIBERAZIONE  ANTICIPATA
	     var dateInserite=0;
	     for (var T=0; T<6; T++)
	     {
	        if(document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>[T].value !=""   &&
	           document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>[T].value !=""    &&
	           document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>[T].value !=""  )
	        {
	              dateInserite++;
	        }
	          
	     	if(dateInserite!= 0)
	            break;
	     
	     }	
	
         if(dateInserite != 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value == 0)
	     {
		        DisabilitaLA_INT();
		        DisabilitaLA_SPE();
		        AbilitaLA();
		        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked = true;
	            alert('Inserire totale giorni di Liberazione Anticipata ');
	            document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.focus();
	            return false;
	     }
         
         if(dateInserite == 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value != 0)
 	     {
 		        DisabilitaLA_INT();
 		        DisabilitaLA_SPE();
 		       	document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA%>.value;
 		        AbilitaLA();
 		        
 		        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[0].checked = true;
 	            alert('Inserire Almeno un Periodo di Liberazione Anticipata ');
 	            document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>[0].focus();
 	            return false;
 	     }
         
//
//  LIBERAZIONE ANTICIPATA  SPECIALE
        var dateInserite=0;
        for (var Tspe = 0; Tspe < 6; Tspe++)
        {
	          if(document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE %>[Tspe].value !="" &&
	             document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE %>[Tspe].value !=""   &&
	             document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE %>[Tspe].value !="" )
	          {
	              dateInserite++;
	          }
	          
	          if(dateInserite!= 0)
	              break;
        }

        if(dateInserite != 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value == 0)
        {
	        DisabilitaLA_INT();
	        DisabilitaLA();
	        AbilitaLA_SPE();
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[1].checked = true;
            alert('Inserire totale giorni di Liberazione Anticipata Speciale ');
            document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.focus();
            return false;
        }
        
        if(dateInserite == 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>.value != 0)
        {
	        DisabilitaLA_INT();
	        DisabilitaLA();
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_SPE%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE%>.value;
	        AbilitaLA_SPE();
	        
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[1].checked = true;
            alert('Inserire Almeno un Periodo di Liberazione Anticipata Speciale ');
            document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE %>[0].focus();
            return false;
        }
//
//  INTEGRAZIONE  LIBERAZIONE  ANTICIPATA
        var dateInserite=0;
        for (var Tint = 0; Tint < 6; Tint++)
        {
	          if(document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT %>[Tint].value !=""
	          && document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT %>[Tint].value !=""
	          && document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT %>[Tint].value !="")
	          {
	              dateInserite++;
	          }
	          
	          if(dateInserite!= 0)
	              break;
        }

        if(dateInserite != 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value == 0)
        {
	        DisabilitaLA();
	        DisabilitaLA_SPE();
	        AbilitaLA_INT();
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[2].checked = true;
            alert('Inserire totale giorni di Integrazione Liberazione Anticipata ');
            document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.focus();
            return false;
        }
        
       if(dateInserite == 0 && document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>.value != 0)
        {
	        DisabilitaLA();
	        DisabilitaLA_SPE();
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_SALVA_NUM_GIORNI_LA_INT%>.value = document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT%>.value;
	        AbilitaLA_INT();
	        
	        document.ModificaLACum.<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>[2].checked = true;
            alert('Inserire Almeno un Periodo di Integrazione Liberazione Anticipata ');
            document.ModificaLACum.<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT%>[0].focus();
            return false;
        }
  
      	//------------------------------------------------------------------------------------------------
     	// Data Emissione Provvedimento Sorveglianza
     	//------------------------------------------------------------------------------------------------
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value.length==1)
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value='0'+document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value;
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value.length==1)
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value='0'+document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value;
  
        var data_to_verify = document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.value+'/'+document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>.value+'/'+document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>.value;
        if (data_to_verify=="//")
        {
        	alert('Data Emissione obbligatoria');
            document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
            return false;
        }
        else if (!ControllaData(data_to_verify))
        {
            alert('Data Emissione non valida');
            document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
            return false;
        }
        //if (!ControllaDataPassaVuota(data_to_verify) )
        //{
        //  alert('Data di emissione Provvedimento non valida');
        //  document.ModificaLACum.< %=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>.focus();
        //  return false;
        //}
      
      	// Tipo provvedimento
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value == "-")
        {        
          alert("Selezionare Tipo Provvedimento");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
          return false;
        }
      	
     	// Anno e Numero Provvedimento
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value == "" &&
      		 document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value != "")
        {        
          alert("Inserire Anno Provvedimento");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.focus();
          return false;
        }
        
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value != "" &&
       		 document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value == "")
        {        
           alert("Inserire Numero Provvedimento");
           document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.focus();
           return false;
        }
        
     	// Anno e Numero Procedimento SIUS
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value == "" &&
      		document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value != "")
        {        
          alert("Inserire Anno Procedimento SIUS");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
          return false;
        }
        
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value != "" &&
       		 document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value == "")
        {        
           alert("Inserire Numero Procedimento SIUS");
           document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.focus();
           return false;
        }
      
      	// Tipo Autorità emittente 
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.value == "-")
        {        
          alert("Selezionare Autorità Emittente");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.focus();
          return false;
        }
      
     	 // Sede Autorità emittente
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>.value=="")
        {        
          alert("Selezionare Sede Autorità Emittente");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>.focus();
          return false;
        }
      
      	// Esito
        if (document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="-")
        {        
          alert("Selezionare un Esito");
          document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.focus();
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
    document.ModificaLACum.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.ModificaLACum.submit();
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
        <font class="campo">Modifica Liberazione Anticipata &nbsp;</font>
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
  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="ModificaLACum">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="hidden" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="<%=StatoEsecTitoloCum.getIdStatoEsecTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" value="<%=lIdLibLa%>" >
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_SPE_CUMULO%>" value="<%=lIdLibLaS%>" >
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_INT_CUMULO%>" value="<%=lIdLibLaI%>" >
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO%>" value="<%=StatoEsecTitoloCum.getFlagStato()%>">

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
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"dd"), "") %>" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE_ALTRO%>" 
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"MM"), "")%>"   
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE_ALTRO%>"   
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"yyyy"), "")%>" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE_ALTRO%>"   
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"   value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProvvedimento(), "" ) %>" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4"   
         	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProvvedimento(), "" ) %>" 
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
        <input Title="Anno Fascicolo Sius" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProcedimento(), "" ) %>" 
        name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        /
        <input Title="Numero Sius" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProcedimento(), "" ) %>" 
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
        	value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getDescrLuogoEmittente(), "-" ) %>" maxlength="30" size="30">
        <a href="Javascript:ListaUfficiComuni('ModificaLACum','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_ALTRO%>',
        			document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>[document.ModificaLACum.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_UFFICIO_ALTRO%>.selectedIndex].value);">
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
  
  <table cellspacing="2" cellpadding="2" width="90%">
    <tr>
      <td width="30%" class="l">
        <span id="LA" style="color=blu;font-weight:bold;"> Liberazione Anticipata  </span> 
        <input value="" type="radio" name="<%=ICostantiLibAnticipataCumulo.CAMPO_RADIO_QUALE_LA%>"  onclick="Javascript:return QualeLAConcede('2130');" >
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

<!--    Liberazione Anticipata  -->
<%
//========================================================
// DIV Visualizzate in caso di Modifica provvedimento 
//========================================================
%>

<div style="display:none; position: relative;" id="datiPeriodiLA" >  
<table width="99%">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Liberazione Anticipata da assegnare / Periodi </td>
  </tr>
  <% for (int k=0; k < 6; k++) {
	  %>
  <tr>
    <% if (k==0) { %>
    <td class="l">
      Totale giorni Liberazione Anticipata da assegnare<font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni L.A." name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA %>" 
      		 value="<%=totggLa%>" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)"
             >
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <%if (k < VecPerLA.size() )
   	  { 
   	 	PeriodoLibAntCumuloModel PeriodoLA = (PeriodoLibAntCumuloModel) VecPerLA.get(k);
   	  %>
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataInizio(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataInizio(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataInizio(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataFine(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataFine(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLA.getDataFine(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
     </td>

  <% } 
  	 else
  	 { %>
	<td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>  
    	 
  <% } // chiude if (k < VecPerLA.size() )
    
  } // chiude ciclo for 
  %>	
  
  </tr> 
  </table>
</div>

<!--  "datiSorvLA_SPE"  Liberazione Anticipata Speciale -->
<div style="display:none; position: relative;" id="datiPeriodiSPE" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Liberazione Anticipata Speciale</td>
    </tr>

  <% for (int k=0; k < 6; k++) {
	  %>
  <tr>
    <% if (k==0) { %>
    <td class="l">
      Totale giorni Liberazione Anticipata Speciale da assegnare<font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni L.A.S." name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_SPE %>" 
      		 value="<%=totggLaS%>" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)"
             >
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <%if (k < VecPerLAS.size() )
   	  { 
   	 	PeriodoLibAntCumuloModel PeriodoLAS = (PeriodoLibAntCumuloModel) VecPerLAS.get(k);
   	  %>
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE %>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataInizio(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE %>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataInizio(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE %>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataInizio(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_SPE %>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataFine(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_SPE %>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataFine(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_SPE %>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAS.getDataFine(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
     </td>

  <% } 
  	 else
  	 { %>
	<td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_SPE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_SPE%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_SPE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_SPE%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_SPE%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>  
    	 
  <% } // chiude if (k < VecPerLA.size() )   %>
    
   </tr> 
<% } // chiude ciclo for   %>

  </table>
</div>

<!--  "datiSorvLA_INT"  Integrazione Liberazione Anticipata -->
<div style="display:none; position: relative;" id="datiPeriodiINT" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Integrazione Liberazione Anticipata</td>
    </tr>

 <% for (int k=0; k < 6; k++) {
	  %>
  <tr>
    <% if (k==0) { %>
    <td class="l">
      Totale giorni di Integrazione Liberazione Anticipata da assegnare<font class="ob">(*)</font>
      <input type="text" Title="Totale Giorni L.A.S." name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_LA_INT %>" 
      		 value="<%=totggLaI%>" 
             size="5" maxlength="4"
             onkeypress="return TicTabNumField(this,event)"
             >
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <%if (k < VecPerLAI.size() )
   	  { 
   	 	PeriodoLibAntCumuloModel PeriodoLAI = (PeriodoLibAntCumuloModel) VecPerLAI.get(k);
   	  %>
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataInizio(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataInizio(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT%>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataInizio(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_INT%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataFine(),"dd"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_INT%>" maxlength="2" size="2" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataFine(),"MM"), "-")%>" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_INT%>" maxlength="4" size="4" 
      	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoLAI.getDataFine(),"yyyy"), "-")%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
     </td>

  <% } 
  	 else
  	 { %>
	<td class="l">
      Dal
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO_INT%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO_INT%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO_INT%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE_INT%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE_INT%>" maxlength="2" size="2" 
      	value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE_INT%>" maxlength="4" size="4" 
      	value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>  
    	 
  <% } // chiude if (k < VecPerLA.size() )   %>
    
   </tr> 
<% } // chiude ciclo for   %>

  </table>
</div>

<%
//==============================================================================
// Sezioni con i periodi e i giorni da computare in caso di compilazione 
// Manuale dei dati 
// CAMPI DATA DAL - AL   LIBERAZIONE ANTICIPATA
//==============================================================================
%>

<table width="95%">
  <tr>
    <td class="Titolo"  colspan="6">Dati relativi al Motivo inserimemto / modifica</td>
  </tr>

  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4">
     <textarea cols="100" rows="2" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(StatoEsecTitoloCum.getNote(), "" ) %></textarea>
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

    var frmvalidator  = new Validator("ModificaLACum");

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