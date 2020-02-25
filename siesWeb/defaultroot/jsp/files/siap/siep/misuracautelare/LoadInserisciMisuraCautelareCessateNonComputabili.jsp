<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@page import="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"%>
<%@page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="misuracautelare" scope="request" class="siap.siep.misuracautelare.model.MisuraCautelareModel"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="flagComputabile"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraDetentive"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraNonDetentive"      scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompTerritorio"      scope="request" class="java.lang.String"/>
<jsp:useBean id="motComp"         scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUff"          scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffDetNonDet"          scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffPM"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffRegGen"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffAutEmi"        scope="request" class="java.lang.String"/>
<jsp:useBean id="primaSezione"    scope="request" class="java.lang.String"/>
<jsp:useBean id="lProvv"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="defaultLuogoAutoritaEmittente"      scope="request" class="java.lang.String"/>
<jsp:useBean id="defaultSedeTipoUfficioPM"      scope="request" class="java.lang.String"/>


<%
  SentenzaModel sentenza = fascicolo.getSentenza();
  String descrEmittenteLuogo=sentenza.getDescrLuogoEmittente();
%>



<%
//==============================================================================
// Form di inserimento delle Misure Cautelare (fase di Iscrizione)
// Attenzione!! La form non viene mai utilizzata in modifica, quindi modalita = 'I'
//              sempre e misuracautelare = null sempre
//==============================================================================

String jsNumField = " onFocus='javascript:textboxSelect(this)' onkeypress='return TicTabNumField(this,event)' ";

%>
<html>
<!-- LoadInserisciMisuraCautelareCessateNonComputabili.jsp -->
<head>
<title>[S.I.E.S.] - Gestione Misura Cautelare </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
   <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jsrsClient.js"></script>
<script language="JavaScript">
 
 function ControllaCheckBox(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox,dtIni,dtFine,idMC) { 		
  		
  		if (!(cBox.checked)  ) {
  		
  	      ddDa.value="";  
  	      mmDa.value="";  
  	      aaDa.value="";  
  	      ddA.value=""; 
  	      mmA.value=""; 
  	      aaA.value="";  
  	      document.LoadInserisciMisuraCautelareCessateNonComputabili.appoCheck.value =999999999;
  	      clearQuantum(idMC);
        }
        else {  
       
            var sep = "-";
        	var data_dal = dtIni.value.split(sep);
        	var data_al = dtFine.value.split(sep);
          	    ddDa.value=data_dal[0];
		        mmDa.value=data_dal[1];
		        aaDa.value=data_dal[2];

			if (dtFine.value.length == 10) {
		        ddA.value=data_al[0];
		        mmA.value=data_al[1];
		        aaA.value=data_al[2];
		        }
          	   ddDa.focus(); 
        	}
  	}

 	function pulisciIstitutoDetenzione (nomeCampoComune){
 		var campoDescr = document.getElementsByName(nomeCampoComune)[0]; 	      
 		campoDescr.value=""; 	      
 	}
 	
	function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
	{
	  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
	 	 
 	function radioBase()
 	{ //alert("radio");
 	  nodetipoEspPenIstDiDetenzioneDiv=document.getElementById('tipoEspPenIstDiDetenzioneDiv');
 	  nodetipoEspPenIstDiDetenzioneSezioneEDiv=document.getElementById('tipoEspPenIstDiDetenzioneSezioneEDiv');
 	  
 	  nodetipoEspPenInAltroLuogoDiv=document.getElementById('tipoEspPenInAltroLuogoDiv');
 	  nodetipoEspPenInAltroLuogoSezioneFDiv=document.getElementById('tipoEspPenInAltroLuogoSezioneFDiv');
 	  
 	  if(document.LoadInserisciMisuraCautelareCessateNonComputabili.EspiazionePenaIstDeteAltroLuogo[0].checked )
 	  { 	    
 		 nodetipoEspPenIstDiDetenzioneDiv.style.display='block';
 		 nodetipoEspPenIstDiDetenzioneSezioneEDiv.style.display='block';
 		 nodetipoEspPenInAltroLuogoDiv.style.display='none';
 		 nodetipoEspPenInAltroLuogoSezioneFDiv.style.display='none';
 		 
 		document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>.value="-";
 	  } 
 	  else  if(document.LoadInserisciMisuraCautelareCessateNonComputabili.EspiazionePenaIstDeteAltroLuogo[1].checked)
 	  { 	     
 		 nodetipoEspPenIstDiDetenzioneDiv.style.display='none';
 		 nodetipoEspPenIstDiDetenzioneSezioneEDiv.style.display='none';
 		 nodetipoEspPenInAltroLuogoDiv.style.display='block';
 		 nodetipoEspPenInAltroLuogoSezioneFDiv.style.display='block';
 		 
 		document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_DETENTIVA%>.value="-";
      }  	  
 	}
 	
	function ListaUffici(a_formname,a_fieldname)
 	{
 	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
 	}
 	function ListaDistretti(a_formname,a_fieldname)
 	{
 	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
 	}
 	function ChoosePopup()
 	{	
 	    var codTipoUfficio = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>.value 	  
 	    if (codTipoUfficio == 'PM'){
 	    	ListaUffici('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>');
 	    } else if (codTipoUfficio == 'PGCAP'){
 	    	ListaDistretti('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>');
 	     } else if (codTipoUfficio == 'PMM'){
 	    	 ListaUffici('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>');
 	     }
 	}
 	
 	function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
 	{
 		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 	}
 	
<%--  	function settaParametriDefault()
 	{
 	 document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>.value=<%=sede%>;
 	} --%>
  	
 
     function ControllaPeriodo(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox,dtIni,dtFine,flagCalcolo)
      {
    	// viene bypassato il controllo sulla data fine nel caso sia vuota questo per
    	// consentire la gestione dei periodi vigenti paolo 16 giugno 2008  (dtFine.value.length == 10)
		var dA=ddA.value+'/'+mmA.value+'/'+aaA.value;
		
		  var dDa=ddDa.value+'/'+mmDa.value+'/'+aaDa.value; 
		
		 if ((cBox.checked) || (flagCalcolo !='1')) {
		   if (! ControllaData(dDa))
		      {
        		alert('Data inizio periodo prenotato non valida');
        		
        		ddDa.focus();
		        return 1;
		      }
		      
		  if  (!CompareDate(dtIni.value,dDa) ) 
		  {
        		alert('Data inizio periodo fuori intervallo temporale consentito ');
        	
        		ddDa.focus();
		        return 1;
		      }
		  
		  if ((dtFine.value.length == 10) &&  (!CompareDate(dDa,dtFine.value))) 
		  {
        		alert('Data inizio periodo fuori intervallo temporale consentito ');
        	
        		ddDa.focus();
		        return 1;
		      }
		      
		    if ((dtFine.value.length == 10) &&  (! ControllaData(dA)))
		      {
        		alert('Data fine periodo prenotato non valida');
        		
        		ddA.focus();
		        return 1;
		      }
		//  if ( (CompareDate(dA,dtIni.value) ) || (!CompareDate(dA,dtFine.value)) )
		
		  if ((dtFine.value.length == 10) &&  (!CompareDate(dA,dtFine.value)))
		
		  {
        		alert('Data Fine periodo fuori intervallo temporale consentito ');
        		
        		ddA.focus();
		        return 1;
		      }
		      
		  if ((dtFine.value.length == 10) &&  (! CompareDate(dDa,dA) ))
		  {
        		alert('Data inizio periodo prenotato maggiore di Data Fine Periodo');
        		ddDa.focus();
		        return 1;
		      }
		    }
		  return 0;
  	}
	

     
 function VerifyAltreBDI()  
 {     	 
	   var DataInizio = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>.value;
	   var DataFine = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>.value;		
	   var DataEmissioneOrdinanza = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%>.value+'/'+document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA%>.value;
	   
	   if(!ControllaDataPassaVuota(DataInizio)){
	      alert('Data Inizio Misura non valida');
	      return false;
	   }
	   if(!ControllaDataPassaVuota(DataFine)){
	      alert('Data Fine Misura non valida');
	      return false;
	   }
	   
	   if(!ControllaDataPassaVuota(DataEmissioneOrdinanza)){
		  alert('Data Emissione Ordinanza non valida');
		  return false;
	   }
		  
	   if (DataFine.length>2 && DataInizio.length<=2){
	        alert('Data Misura non valorizzata');
	        return false;
	   }
	   
	   if (! CompareDate(DataInizio, DataFine))
       {
       		alert('Data di Inizio Misura non può essere successiva a quella di Fine');
        	return false;
       }
	  
 	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>.value=="" || 
 			  document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>.value=="-")
	   {
	      alert("Tipo Ufficio PM è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>.focus();
				 return false;
	   }	   
 	   if(document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>.value=="")
	   {
		  alert("Sede è obbligatorio");
		  document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>.focus();
	   	  return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE%>.value=="" ||
			   document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE%>.value=="-")
	   {
	      alert("Autorità Emittente è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE%>.focus();
				 return false;
	   }
	   <%-- if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>.value=="" ||
			   document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>.value=="-")
	   {
	      alert("Luogo Autorità Emittente è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>.focus();
				 return false;
	   } --%>
	   if ((document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>.value=="" ||
   			document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>.value=="-" ||
   			document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>.value==null) &&
   			(document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_DETENTIVA%>.value=="" ||
   	    	 document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_DETENTIVA%>.value=="-" ||
   	    	 document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_DETENTIVA%>.value==null))
   	   {
   	      alert("Misura è obbligatorio");
    	  //document.LoadInserisciMisuraCautelareCessateNonComputabili.<--%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>.focus();
   	      		return false;
   	   }   	  
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>.focus();
				 return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=  ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=  ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>.focus();
				 return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO  %>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO  %>.focus();
				 return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>.focus();
				 return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>.focus();
				 return false;
	   }
	   if (document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>.value=="" )
	   {
	      alert("Data misura è obbligatorio");
	      document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>.focus();
				 return false;
	   }
	   
       return true;

   }
  
    
   
  //============================================================================
  // Funzione per la verifica dell'inserimento di almeno una Misura cautelare
  // e della correttezza delle date
  //============================================================================
  function Verify()
  {
      var almenoUna=false;
      var misura="-";
 	  if(document.LoadInserisciMisuraCautelareCessateNonComputabili.EspiazionePenaIstDeteAltroLuogo[0].checked )
 	  { 		 
 		 misura = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA%>[document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA %>.selectedIndex].value;
 	  } 
 	  else  if(document.LoadInserisciMisuraCautelareCessateNonComputabili.EspiazionePenaIstDeteAltroLuogo[1].checked )
 	  { 	
 		 misura = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>[document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>.selectedIndex].value;
      }
 	  
      //==================================
      // Recupero la data Inizio misura
      //==================================
      var giornoDataInizio = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>.value;
      var meseDataInizio   = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>.value;
      var annoDataInizio   = document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>.value;

      if(giornoDataInizio.length==1)
        giornoDataInizio='0'+giornoDataInizio;
      if(meseDataInizio.length==1)
        meseDataInizio='0'+meseDataInizio;

      //==================================
      // Recupero la data Fine misura
      //==================================
     

      //========================================================================
      // Verifico se è stata selezionata la Misura e in questo caso controllo
      // la presenza di data inizio e data fine e la loro correttezza e coerenza
      //========================================================================
      if(misura!="-")
      {
        almenoUna=true;        
        dataInizio=giornoDataInizio+"/"+meseDataInizio+"/"+annoDataInizio;
        if(dataInizio.length > 2)
        {
          if(!ControllaData(dataInizio))
          {
            alert("Data Inizio non valida");
            return false;
          }
        }
        else
        {
          alert("Data Inizio Obbligatoria");
          return false;
        }

        // se non mi trovo nella sezione della misure in corso verifico la data fine
        dataFine=giornoDataFine+"/"+meseDataFine+"/"+annoDataFine;
        if(dataFine.length > 2)
        {
        	if(!ControllaData(dataFine))
            {
              alert("Data Fine non valida");
              return false;
            }
        } else
        {
        	alert("Data Fine Obbligatoria");
            return false;
        }           
        if(!CompareDate(dataInizio,dataFine))
        {
        	alert("La Data Inizio deve essere minore o uguale della Data Fine");
            return false;
        }
        if(dataFine.length > 2)
        {
          if(!ControllaData(dataFine))
          {
            alert("Data Fine non valida");
            return false;
          }
        }
        else
        {
          alert("Data Fine Obbligatoria");
          return false;
        }
        
        if(!CompareDate(dataInizio,dataFine))
        {
          alert("La Data Inizio deve essere minore o uguale della Data Fine");
          return false;
        }       
      } // end if(misura!="-")
 
    
      if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi != null && !almenoUna)
      {
	  	if (isNaN(parseInt(document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi.length)))
	  	{							
			if (!document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi.checked) {
		    	almenoUna=false;
	        }   
	        else 
		        almenoUna=true;
	    	} else {
			for (lSel=0;lSel<document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi.length;lSel++) 
			{			          		
				if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi[lSel].checked) {					           
					almenoUna=true;
				}
			}
		}
      }
      if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodiNC != null && !almenoUna)
      {
	  	if (isNaN(parseInt(document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodiNC.length)))
		{							
			if (!document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodiNC.checked) {
				almenoUna=false;
			} else 
				almenoUna=true;
	    } else 
	    {
	    	for (lSel=0;lSel<document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodiNC.length;lSel++) 
			{			          		
				if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodiNC[lSel].checked) 
				{					           
					almenoUna=true;
				}
			}
		}
      }
	 /*  if(!almenoUna)
	  {
	  	alert("Selezionare almeno una misura cautelare");
	    return false;
	  } */
    
   	  if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi != null)
      {
      <%   
      	if (lProvv != null) {   	
    		for (int i=0; i<lProvv.size();i++) {    	
			    ProvvedimentoModelBDMC lProvvBdmcMod= (ProvvedimentoModelBDMC) lProvv.get(i);
			    Vector lPeriodi = lProvvBdmcMod.getSbPeriPren();
			    if(lPeriodi != null && lPeriodi.size() != 0)
				{
					Iterator lIterPeriodi = lPeriodi.iterator();
					int lsel=0;					
					BigDecimal cambioPrenotazione = new BigDecimal(0);	
					while(lIterPeriodi.hasNext())
					{
						SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
						if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
				        else {
				        	String campoCheckBox = ICostantiSbPren.CAMPO_CHECK_PERIODI+"["+lsel+"]";  
				            String campoGiornoDa = ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel;
				            %>
				            
				         //alert("eccomi");
				         if (document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi[0] != null){
			          		 var appo = ControllaPeriodo(document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=campoCheckBox %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.dtIni<%=lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.dtFine<%=lsel %>,'1');
				       		 if (appo > 0)	
				       		 {
				       		 	return false;
				       		 }
				       	 } else	
				       	 {
						 	var appo = ControllaPeriodo(document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.CheckPeriodi,document.LoadInserisciMisuraCautelareCessateNonComputabili.dtIni<%=lsel %>,document.LoadInserisciMisuraCautelareCessateNonComputabili.dtFine<%=lsel %>,'1');
					       	if (appo > 0)	
					       	{
					       		return false;
					       	}
						  }				       	  
				       <% lsel++;}  cambioPrenotazione = lPeriodo.getIdPren();}
	     }}}%>
	  }
    return true;
  }
</script>

<script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaComuniPerTipoUfficio(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComunePerTipoUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>
  <script language="JavaScript">
    var desktop;
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=NO", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
  </script>
  
    <style>
	  .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </style>
  
</head>




<body class="corpo" onload="radioBase();">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
       MisuraCautelareModel lMis = new MisuraCautelareModel();
       String lAzione = new String();
       String check1 = "checked";
	   String check2 = "";
       if( modalita.equals("I") )
       {
         lAzione = "siap.siep.misuracautelare.action.ActInserisciMisuraCautelare";
%>
         <font class="campo">Inserimento Misura Cautelare - NON COMPUTABILE</font>
<%
       }
       else if( modalita.equals("M") )
       {
         lAzione = "siap.siep.misuracautelare.action.ActModificaMisuraCautelare";
         lMis = misuracautelare;
                  String codTipoMisura=lMis.getCodTipoMisura();
         if (codTipoMisura.equals("CA") || codTipoMisura.equals("CD")){
        	 //popolato con misure detentive  
        	 check1 = "checked";
        	 check2 = "";
         } else if (codTipoMisura.equals("AD") || codTipoMisura.equals("CL") || codTipoMisura.equals("CB") || codTipoMisura.equals("CC") || codTipoMisura.equals("CE")){
        	 //popolato con misure non detentive  
        	 check1 = "";
        	 check2 = "checked";
         }
%>
         <font class="campo">MODIFICA MISURA CAUTELARE - NON COMPUTABILE</font>
<%
       }
%>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraCautelareCessateNonComputabili">
  <input type="HIDDEN" name="PrimaSezione" value="<%=primaSezione%>" >
  <input type="HIDDEN" name="Action"       value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>" value="<%=lMis.getIdMisuraCautelare()%>">
  <input type="HIDDEN" name="flagComputabile" value="<%=flagComputabile%>" >
  
  
	<br>
	<table cellspacing=2 cellpadding=2  width="100%" >		    		
      	<tr>
      	  <td class="L"> Anno/Numero RG.N.R. &nbsp;</td>
          <td class="L">
            <input type="text" title="Anno Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getAnnoRgnr()) %>" name="<%=ICostantiMisuraCautelare.ANNO_RGNR%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
            /
            <input type="text" title="Numero Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getNumeroRgnr()) %>" name="<%=ICostantiMisuraCautelare.NUMERO_RGNR%>" maxlength="6" size="5" <%=jsNumField%>>           
          </td>        
          <td class="l">Tipo Ufficio PM(*) </td>
          <td class="l">
            <select  title="Ufficio" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>">
            <%=tipUffPM%>
            </select>
          </td>
        
          <td class="l">Sede(*) </td>	           
          <td class="l">
          	<input value="<%=defaultSedeTipoUfficioPM%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>">          	
          	<input type="HIDDEN" name="<%=ICostantiMisuraCautelare.COMUNE_COD_SEDE_UFFICIO_PM%>" value="<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>" >           
            <%--<a href="Javascript:ListaComuniPerTipoUfficio('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>');">
            <a href="Javascript:ChoosePopup();">--%>
            <a href="Javascript:ListaUfficiComuni('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM%>',document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>[document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0></a>
          </td>		
             
        </tr>
        <tr>
			<td class="Titolo" colspan="6" height="20px" >   </td>
		</tr>
      	<tr>
           <td class="L"> Anno/Numero B.D.M.C. </td>
          <td class="L">
            <input type="text" title="Anno Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getAnnoFascBdmc()) %>" name="<%=ICostantiMisuraCautelare.ANNO_FASC_BDMC%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
            /
            <input type="text" title="Numero Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getNumeFascBdmc()) %>" name="<%=ICostantiMisuraCautelare.NUME_FASC_BDMC%>" maxlength="6" size="5" <%=jsNumField%>>           
          </td>        
          <td class="L"> Anno/Numero Reg. Gen. &nbsp;&nbsp;</td>
          <td class="L">
            <input type="text" title="Anno Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getAnnoRegGen()) %>" name="<%=ICostantiMisuraCautelare.ANNO_REG_GEN%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
            /
            <input type="text" title="Numero Procedimento" value="<%=StringUtils.toStringJSP(misuracautelare.getNumeroRegGen()) %>" name="<%=ICostantiMisuraCautelare.NUMERO_REG_GEN%>" maxlength="6" size="5" <%=jsNumField%>>           
          </td>
          	     		
      	  <td class="l">Tipo ufficio Reg. Gen.</td>
          <td class="l"> 
	            <select  title="Ufficio" name="<%= ICostantiMisuraCautelare.TIPO_UFFICIO_REG_GEN %>">
	            	<%=tipUffRegGen%>
            	</select>
          </td>
         <tr>        
        </table>
        
        <table cellspacing=2 cellpadding=2  width="100%" >	
      	<tr>	     		
      		<td class="l">Autorità Emittente(*)</td>
        	<td class="l"> 
	            <select  title="Ufficio" name="<%= ICostantiMisuraCautelare.AUTORITA_EMITTENTE %>">
	            	<%=tipUffAutEmi%>
            	</select>
          	</td>
         <tr>
         </tr>
          	<td class="l">Luogo</td>          	
          	<td class="l">	            
	        	 <input value="<%=defaultLuogoAutoritaEmittente%>" type="text" name="<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>">                  
	             	<a href="Javascript:ListaUfficiPerTipo('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO%>',document.LoadInserisciMisuraCautelareCessateNonComputabili.<%= ICostantiMisuraCautelare.AUTORITA_EMITTENTE %>[document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.AUTORITA_EMITTENTE%>.selectedIndex].value);">	             
	             <img src="/images/filefolder.gif" border=0></a>
          	</td>         	
      	</tr>
      	<tr>    	
          	<td class="l">Data emissione Ordinanza</td>
	        <td class="L"> 																				
	           <input type="text" title="Giorno Data inizio emissione Ordinanza" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataEmissioneOrdinanza(), "dd")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>" <%=IWebConstants.UTIL_DATA%>>
	           -
	           <input type="text" title="Mese Data inizio emissione Ordinanza" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataEmissioneOrdinanza(), "MM")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>" <%=IWebConstants.UTIL_DATA%>>
	           -
	           <input type="text" title="Anno Data inizio emissione Ordinanza" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataEmissioneOrdinanza(), "yyyy")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
	        </td>         
      	</tr>
      	<tr>
			<td class="Titolo" colspan="6" height="20px" >   </td>
		</tr>
	</table>

	<br>
	<table cellspacing=2 cellpadding=2  width="100%" >
		<tr>
			<td class="c">
			           Espiazione pena in istituto di detenzione &nbsp;                   <input type="radio" value="istitutoDetenzione" name="<%=ICostantiMisuraCautelare.ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO%>" onClick="radioBase();" <%=check1%>>
			           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Espiazione pena in altro luogo &nbsp;<input type="radio" value="altroLuogo" name="<%=ICostantiMisuraCautelare.ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO%>" onClick="radioBase();" <%=check2%>>			           
			</td>
		</tr>
	</table>
	
	<!-- Espiazione pena in altro luogo -->
	<br>	
	<div id="tipoEspPenInAltroLuogoDiv" style="display:block; width:100%;" >
		<table cellspacing=1 cellpadding=1 width=100%>
   		<tr>
			<td class="l">
		      <table  cellspacing=1 cellpadding=1>
		        <tr>
		          <td class="l">Misura<font class=ob>(*)</font></td> 	         
		          <td class="l">
		            <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_NON_DETENTIVA %>" >
		              <%=tipoMisuraNonDetentive%>
		            </select>
		          </td>	         
		        </tr>
		      </table>
		    </td>
		  </tr>
		</table>
	</div>
	
	<!-- Espiazione pena in istituto di detenzione -->
	<div id="tipoEspPenIstDiDetenzioneDiv" style="display:block; width:100%;" >
		<table cellspacing=1 cellpadding=1 width=100%>
   		<tr>
			<td class="l">
		      <table  cellspacing=1 cellpadding=1>
		        <tr>
		          <td class="l">Misura<font class=ob>(*)</font></td> 	         
		          <td class="l">
		            <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_DETENTIVA %>" >
		              <%=tipoMisuraDetentive%>
		            </select>
		          </td>	         		          
		        </tr>
		      </table>
		    </td>
		  </tr>
		</table>
	</div>
	
	 <table  cellspacing=1 cellpadding=1>
        <tr>		               
          <td class="l">Da<font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" title="Giorno Data inizio" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataInizio(), "dd") )%>" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Mese Data inizio" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataInizio(), "MM")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Anno Data inizio" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataInizio(), "yyyy")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
          <td class="l">a<font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" title="Giorno Data fine" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFine(), "dd")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Mese Data fine" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFine(), "MM")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Anno Data fine" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFine(), "yyyy")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
        </tr>
        <table width=100%><tr><td width=100%><hr width=100%></td></tr></table>
      </table>
		      
	<br>
	<!-- Espiazione pena in istituto di detenzione -->
	<div id="tipoEspPenIstDiDetenzioneSezioneEDiv" style="display:block; width:100%;" >
	<table cellspacing=2 cellpadding=2  width="100%" > 	
      	<tr>
          	<td class="l">Istituto di Detenzione </td>
          	<!-- Comune Istituto di Detenzione -->	
          	<td class="l">         	
                  <%  if( misuracautelare!=null &&  misuracautelare.getIstitutoDetenzione()!=null) {%>    	
          	      	<input readonly Title="Istituto" value="<%=StringUtils.toStringJSP(misuracautelare.getIstitutoDetenzione().getDescrTipoIstituto()+" di "+misuracautelare.getIstitutoDetenzione().getDescrizione()) %>" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE%>" size=50>
            	  <% } else { %> 
              	  	<input readonly Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE%>" value="" size=50>
                  <% } %>           
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE%>" value="" size=50>
                  <input type="HIDDEN" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" >
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelareCessateNonComputabili','IstDetIdIstitutoDetenzione','<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE%>[0]');">
                  <img src="/images/filefolder.gif" border=0></a>
                  <a href="Javascript:pulisciIstitutoDetenzione('<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE_COMUNE%>');"><img src="/images/delete.gif" border=0></a>         	
          	</td>	
          	
      	</tr>
      	
	</table>
	</div>
	
	<br>
	<!-- Espiazione pena in altro luogo -->	
	<div id="tipoEspPenInAltroLuogoSezioneFDiv" style="display:block; width:100%;" >
	<table cellspacing=2 cellpadding=2  width="100%" >
		<tr>	
			<td class="l" >Luogo di espiazione</td>
	        <td class="l">
	        	<input type="text" title="LuogoDiEspiazione"  maxlength="50" size="90" value="<%=StringUtils.toStringJSP(misuracautelare.getAltroLuogoDetenzione()) %>" name="<%= ICostantiMisuraCautelare.CAMPO_COD_LUOGO_DI_ESPIAZIONE %>">
	        </td>
		</tr> 
        <tr>
        	<td class="l">Autorita' competente per territorio </td>
          	<td class="l">
		            <select Title="autoritaCompetentePerTerritorio" name="<%= ICostantiMisuraCautelare.AUTORITA_COMPETENTE%>" >
		              <%=autoritaCompTerritorio%>
		            </select>
		    </td>
      	 </tr>
      	 </tr>
        	<td class="l">Sede</td> 
          	<td class="l">
	        	<input value="<%=StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteSedeDesc()) %>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PER_TERRITORIO%>">
	            	<a href="Javascript:ListaComuni('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PER_TERRITORIO%>');">
	              	<img src="/images/filefolder.gif" border=0></a>	 
          	</td>

          	
          	<td class="l" >Indirizzo</td>
	        <td class="l">
	        	<textarea rows=3 cols=50 style="color: blue" name="<%= ICostantiMisuraCautelare.AUTORITA_COMPETENTE_INDIRIZZO %>"><%=StringUtils.toStringJSP(misuracautelare.getAutoritaCompetenteIndirizzo()) %></textarea>
	        </td>
      	 </tr>
	</table>
	</div>
	
	<br>
	<table cellspacing=2 cellpadding=2  width="100%" >
		<tr>   	
			<td class="l">Motivo Non Computabilità</td> 
		 	<td class="l">
	            <select Title="Motivo Computabilità" name="<%= ICostantiMisuraCautelare.CAMPO_COD_MOTIVO_NON_COMPUTABILE%>">
	            	<%=motComp%>
            	</select>          
            </td> 
		 </tr>
		 <tr>
		 	<td class="l">Data provvedimento di Fungibilita'</td>
		    
            
         <td class="l">
	            <input  title="Giorno Data inizio fungibilita" type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFungibilita(), "dd")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA%>>
	            -
	            <input  title="Mese Data inizio fungibilita" type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFungibilita(), "MM")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA%>>
	            -
	            <input title="Anno Data inizio fungibilita" type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelare.getDataFungibilita(), "yyyy")) %>" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
            </td>  
		            
		 	<td class="l">SIEP ANNO/NUMERO<font class=ob></font></td>
		    <td class="L">
		    	<input type="text" title="AnnoSiep" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(misuracautelare.getAnnoRifer()) %>" name="<%= ICostantiMisuraCautelare.ANNO_SIEP %>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
		        /
		        <input type="text" title="NumeroSieo" maxlength="6" size="5" value="<%=StringUtils.toStringJSP(misuracautelare.getNumRifer()) %>" name="<%= ICostantiMisuraCautelare.NUMERO_SIEP %>"  maxlength="14" size="14" <%=jsNumField%>>
		 	</td>         
		 </tr>
		 <tr>
		 	<td class="l">Ufficio </td> 
          	<td class="l"> 
	            <select  title="Ufficio" name="<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_RIFER%>">
	            	<%=tipUffDetNonDet%>
            	</select>
          	</td>
		 </tr>
		 <tr>
		 	<td class="l">Sede </td>
          	<td class="l">
	        	<input type="text" value="<%=StringUtils.toStringJSP(misuracautelare.getDescrLuogoUfficioRifer()) %>" name="<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>">
	            	<a href="Javascript:ListaUfficiComuni('LoadInserisciMisuraCautelareCessateNonComputabili','<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>',document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_RIFER%>[document.LoadInserisciMisuraCautelareCessateNonComputabili.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_RIFER%>.options.selectedIndex].value);">
	              	<img src="/images/filefolder.gif" border=0></a>	 
          	</td>
		 </tr>
		 <tr>
		 	<td class="l" >Note</td>
          	<td class="l">
            	<textarea rows=5 cols=80 style="color: blue" name="<%= ICostantiMisuraCautelare.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(misuracautelare.getNote()) %></textarea>
         	</td>
		 </tr>
		 <tr>
		    <td colspan=2>
		      <br>
		      <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma" onClick="javascript:return VerifyAltreBDI();">
		    </td>
  		</tr>
	</table>
	


</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraCautelareCessateNonComputabili");

  //Controlli validità DATA EMISSIONE ORDINANZA
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA %>","lt=31");  
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA %>","lt=12");  
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>","numeric"); 
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA %>","lt=2050"); 

  //Controlli validità DATA INIZIO
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","lt=2050");
  
  //Controlli validità DATA FINE
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","lt=2050");

  
  frmvalidator.setAddnlValidationFunction("Verify");


  </script>
</body>
</html>