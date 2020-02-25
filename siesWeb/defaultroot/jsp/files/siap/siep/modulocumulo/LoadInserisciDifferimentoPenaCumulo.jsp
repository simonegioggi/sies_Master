<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" 		scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>
<jsp:useBean id="aIdComputo" 			scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittente" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="lPenaResidua" 			scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<% 

//============================================================================== 
// Form per l'inserimento e la modifica dei provv. di Differimento Pena - 
// Provvedimenti della Sorveglianza - modulo cumulo. 
//============================================================================== 
String strChecked = "";
String strCheckedMin = "";
String strTipoTrib = "";

ComputiCumuloModel aComputo = new ComputiCumuloModel();
Vector <ComputiCumuloModel> lListaComputi = aProvvedimento.getListaComputi();
if ( modalita.equals("M") )
{
  Iterator itxComputi = lListaComputi.iterator();
  while ( itxComputi.hasNext()) 
  {
    ComputiCumuloModel lComputo = (ComputiCumuloModel) itxComputi.next();
    BigDecimal lIdCompDaModificare = new BigDecimal (aIdComputo);
    if (lComputo.getIdComputiCumulo().compareTo(lIdCompDaModificare)==0){
      aComputo = lComputo;
      break;
    }
  }
  
  if (aComputo.getFlagDecisioneTribunale() != null ) {
  	 if(aComputo.getFlagDecisioneTribunale().compareTo("S")==0 ) {
   strChecked="checked";
   		strTipoTrib = "Tribunale di Sorveglianza";
  	 } else if(aComputo.getFlagDecisioneTribunale().compareTo("M")==0 ) {
 		strCheckedMin="checked";
 		strTipoTrib = "Tribunale per i minorenni in funzione di Tribunale di Sorveglianza";
} 
  }	 
} 


%> 

<html>
<head>
  <title> Gestione Provvedimento Differimento Pena </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" src="/html/jsrsClient.js"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_JQUERY%>></script>  

  <script language="JavaScript" >
	var desktop;
	
    function ListaComuniperTipoUfficio(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }

    function showDivForOggetto()
    {
      var codOggetto = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value ;
      var arrayUDS = [ "2010","2011"];
      var arrayTDS = [ "0030","0031","0032","0033","0031","0201","0202"];

      if ($.inArray(codOggetto, arrayUDS)>-1) {
        $('#divUDS').show();
        $('#divUDS input[type=text]').prop('disabled',false);
        $('#divUDS input[type=checkbox]').prop('disabled',false);        
      }
      else {
        $('#divUDS').hide();
        $('#divUDS input[type=text]').prop('disabled',true);
        $('#divUDS input[type=checkbox]').prop('disabled',true);        
      }

      if ($.inArray(codOggetto, arrayTDS)>-1) {
        $('#divTDS').show();
        $('#divTDS input[type=text]').prop('disabled',false);
      }
      else {
        $('#divTDS').hide();
        $('#divTDS input[type=text]').prop('disabled',true);
      }
    }
	
// == Calcolo del periodo     
    function clearQuantum(){

    }
    
    function testCalcolaPresoffertoUds(par)
    {
   	 
   	 //callCalcolaQuantum();
   	 //alert("testCalcolaPresoffertoUds ");
   	 
       var reply = 'noreply';
       if (par=='X')
         reply = 'reply';
         
       if (controllaPeriodiUds(reply)==false){
         //clearQuantum();
       }
       else {
         callCalcolaQuantumUds();
       }
       
    }
    
    function testCalcolaPresofferto(par)
    {
   	 
   	 //callCalcolaQuantum();
   	 //alert("testCalcolaPresofferto ");
   	 
       var reply = 'noreply';
       if (par=='X')
         reply = 'reply';
         
       if (controllaPeriodi(reply)==false){
         //clearQuantum();
       }
       else {
         callCalcolaQuantum();
       }
       
    }
    
  	//==========================================================================
    // Affettua la chiamata sincrona alla servlet di calcolo quantum
    //==========================================================================
    function callCalcolaQuantum () {
      //alert("callCalcolaQuantum: ");
      
      //if (controllaPeriodi(idMC)==false)
      //  return;
    

      var gg_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1];
      var mm_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1];
      var aa_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[1];

      var gg_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1];
      var mm_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1];
      var aa_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[1];
      
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      
      var gg_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[1];
      var mm_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[1];
      var aa_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[1];
      
      // Prepara l'array dei dati da passare 
      var myParams = new Array(gg_dal.value,
                               mm_dal.value,
                               aa_dal.value,
                               gg_al.value,
                               mm_al.value,
                               aa_al.value,
                    gg_diff.value,
      				mm_diff.value,
      					aa_diff.value
                              );

      document.body.style.cursor='wait';
      //alert('Chiamata');
      // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
      jsrsExecute("/CaricaHTML_Servlet", caricaQuantum, "getDataRinvioDifferimento",myParams); 


    }
  	
    function callCalcolaQuantumUds () {
       //alert("callCalcolaQuantumUds: ");
     
       var gg_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0];
       var mm_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0];
       var aa_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[0];

       var gg_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0];
       var mm_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0];
       var aa_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[0];
       
       var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
       var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
       
       var gg_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[0];
       var mm_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[0];
       var aa_diff = document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[0];
       
       // Prepara l'array dei dati da passare 
       var myParams = new Array(gg_dal.value,
                                mm_dal.value,
                                aa_dal.value,
                                gg_al.value,
                                mm_al.value,
                                aa_al.value,
                     gg_diff.value,
       				 mm_diff.value,
       					aa_diff.value
                               );

       document.body.style.cursor='wait';
       //alert('Chiamata');
       // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
       jsrsExecute("/CaricaHTML_Servlet", caricaQuantumUds, "getDataRinvioDifferimento",myParams); 


     }
    
    //==========================================================================
    // Funzione di callback invocata di ritorno dalla servlet
    //==========================================================================
    function caricaQuantum(valueTextStr){ 
   	 
	   	  //alert(" caricaQuantum return "+valueTextStr);
	      document.body.style.cursor='auto';
	      
	      var sep = "~#";
	      var aPairs = valueTextStr.split(sep);
	      
	      quale_campo = aPairs[0];
	      
	      if(quale_campo == 0) {
	      	  anni = aPairs[1];
		      mesi = aPairs[2];
		      giorni = aPairs[3];
		      
	      	  document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[1].value = giorni;
		  	  document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[1].value = mesi;
		  	  document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[1].value = anni;
	      } else {
	      	 //alert(" TDS - dovrei caricare la data fine");
	      	 aa_al = aPairs[1];
			 mm_al = aPairs[2];
			 gg_al = aPairs[3];
	      	 
	      	 document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1].value = gg_al;
	         document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1].value = mm_al;
	         document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[1].value = aa_al;
	      }	


    }
    
    //=========
   	 function caricaQuantumUds(valueTextStr){ 
   	 
   		//alert(" caricaQuantumUDS return "+valueTextStr);
      	document.body.style.cursor='auto';
      
	    var sep = "~#";
	    var aPairs = valueTextStr.split(sep);
	    
	    quale_campo = aPairs[0];
	    
	    if(quale_campo == 0) {
	   	 
	   	    anni = aPairs[1];
		    mesi = aPairs[2];
		    giorni = aPairs[3];
		    
		   document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[0].value = giorni;
		   document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[0].value = mesi;
		   document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[0].value = anni;
	    } else {
	      	//alert(" UDS - dovrei caricare la data fine");
	        aa_al = aPairs[1];
		    mm_al = aPairs[2];
		    gg_al = aPairs[3];
	      	 
     	    document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].value = gg_al;
            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0].value = mm_al;
            document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[0].value = aa_al;
	      	
	    }   

    }

    function passaTds() {
   	 	if( $("#idFlagTrib").prop('checked') == true  && $("#idFlagTribMin").prop('checked') == true ) {
   	 		$("#idFlagTribMin").attr('checked', false );
   	 	} 
   	 	valorizzaSede();
   	 
    }
    
    function passaTdsMin() {
	 	if( $("#idFlagTribMin").prop('checked') == true  && $("#idFlagTrib").prop('checked') == true ) {
	 		$("#idFlagTrib").attr('checked', false );
	 	}
	 	valorizzaSede();
	 
 	}
    
    function valorizzaSede() {
  	 	if( $("#idFlagTrib").prop('checked') == true  && $("#idFlagTribMin").prop('checked') == false ) {
	 		$("#idTipoUfficioDest").val('Tribunale di Sorveglianza');	
	 	} else if( $("#idFlagTrib").prop('checked') == false  && $("#idFlagTribMin").prop('checked') == true ) {
	 		$("#idTipoUfficioDest").val('Tribunale per i minorenni in funzione di Tribunale di Sorveglianza');
	 	} else {
	 		$("#idTipoUfficioDest").val('');
	 	}
    }
// ============================  
	
	function controllaPeriodi(reply)
    {
		//alert("controllaPeriodiUds");
      var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
      var gg_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1];
      var mm_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1];
      var aa_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[1];

      var gg_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1];
      var mm_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1];
      var aa_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[1];
      
      // Data Differimento INIZIO
      if (gg_dal.value.length<2 && gg_dal.value.length!=0)
        gg_dal.value="0"+gg_dal.value;
      if (mm_dal.value.length<2 && mm_dal.value.length!=0)
        mm_dal.value="0"+mm_dal.value;
      if (aa_dal.value.length==2) {
        if (aa_dal.value>50)
          aa_dal.value = '19'+aa_dal.value;
        else 
          aa_dal.value = '20'+aa_dal.value;
      }        

      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      
     // alert("controlla Periodi uds - DataDal = "+dataDAL );

      if (! ControllaData(dataDAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data Differimento Esecuzione non valida');
          gg_dal.focus();
          return false;
        }
      }

      if (!CompareDate(dataDAL,sysDate)){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data Differimento Esecuzione non può essere una data futura');
          gg_dal.focus();
          return false;
        }
      }
      
      if (aa_dal.value<1900){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data Differimento Esecuzione deve essere maggiore del 1900');
          aa_dal.focus();
          return false;
        }
      }  

      // Data Differimento FINO_AL
      if (gg_al.value.length<2 && gg_al.value.length!=0)
        gg_al.value="0"+gg_al.value;
      if (mm_al.value.length<2 && mm_al.value.length!=0)
        mm_al.value="0"+mm_al.value;
      if (aa_al.value.length==2) {
        if (aa_al.value>50)
          aa_al.value = '19'+aa_al.value;
        else 
          aa_al.value = '20'+aa_al.value;
      }

      // Data Fine non obbligatoria ma se inserita effettuo i controlli
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      if (dataAL!="//")
      {
        if (! ControllaData(dataAL))
        {
          if (reply=='noreply') {
            return false
          }
          else {
            alert('Data Differimento Esecuzione FINO AL non valida');
            gg_al.focus();
            return false;
          }
        }
      
        if (!CompareDate(dataAL,sysDate)){
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL non può essere una data futura');
            gg_dal.focus();
            return false;
          }
        }
      
        if (aa_al.value<1900){
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL deve essere maggiore del 1900');
            aa_al.focus();
            return false;
          } 
        } 
      
        if(!CompareDate(dataDAL, dataAL))
        {
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL non può essere precedente a quella di Differimento');
            gg_dal.focus();
            return false;
          }
        }
      }
           
      return true;
    }
	
	function controllaPeriodiUds(reply)
    {
		//alert("controllaPeriodiUds");
      var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
      var gg_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0];
      var mm_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0];
      var aa_dal = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[0];

      var gg_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0];
      var mm_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0];
      var aa_al = document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[0];
      
      // Data Differimento INIZIO
      if (gg_dal.value.length<2 && gg_dal.value.length!=0)
        gg_dal.value="0"+gg_dal.value;
      if (mm_dal.value.length<2 && mm_dal.value.length!=0)
        mm_dal.value="0"+mm_dal.value;
      if (aa_dal.value.length==2) {
        if (aa_dal.value>50)
          aa_dal.value = '19'+aa_dal.value;
        else 
          aa_dal.value = '20'+aa_dal.value;
      }        

      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      
     // alert("controlla Periodi uds - DataDal = "+dataDAL );

      if (! ControllaData(dataDAL))
      {
        if (reply=='noreply') {
          return false
        }
        else {
          alert('Data Differimento Esecuzione non valida');
          gg_dal.focus();
          return false;
        }
      }

      if (!CompareDate(dataDAL,sysDate)){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data Differimento Esecuzione non può essere una data futura');
          gg_dal.focus();
          return false;
        }
      }
      
      if (aa_dal.value<1900){
        if (reply=='noreply') {
          return false
        }
        else {
          alert('La Data Differimento Esecuzione deve essere maggiore del 1900');
          aa_dal.focus();
          return false;
        }
      }  

      // Data Differimento FINO_AL
      if (gg_al.value.length<2 && gg_al.value.length!=0)
        gg_al.value="0"+gg_al.value;
      if (mm_al.value.length<2 && mm_al.value.length!=0)
        mm_al.value="0"+mm_al.value;
      if (aa_al.value.length==2) {
        if (aa_al.value>50)
          aa_al.value = '19'+aa_al.value;
        else 
          aa_al.value = '20'+aa_al.value;
      }

      // Data Fine non obbligatoria ma se inserita effettuo i controlli
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
      if (dataAL!="//")
      {
        if (! ControllaData(dataAL))
        {
          if (reply=='noreply') {
            return false
          }
          else {
            alert('Data Differimento Esecuzione FINO AL non valida');
            gg_al.focus();
            return false;
          }
        }
      
        if (!CompareDate(dataAL,sysDate)){
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL non può essere una data futura');
            gg_dal.focus();
            return false;
          }
        }
      
        if (aa_al.value<1900){
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL deve essere maggiore del 1900');
            aa_al.focus();
            return false;
          } 
        } 
      
        if(!CompareDate(dataDAL, dataAL))
        {
          if (reply=='noreply') {
            return false
          }
          else {
            alert('La Data Differimento Esecuzione FINO AL non può essere precedente a quella di Differimento');
            gg_dal.focus();
            return false;
          }
        }
      }
           
      return true;
    }
    
// ===============================    
    
    function Verify()
    {
        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
            document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
            document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) ||
        	data_to_verify.length < 10 )
        {
          alert('Data emissione provvedimento non valida');
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Tipo Provvedimento è obbligatorio");
          return false;
        }

        // Anno e Numero Provvedimento Obbligatori.
        if( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO %>.value.length == 0)
        {
          alert("Valorizzare Anno Provvedimento");
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO %>.focus();

          return false;
        }
        
        if(document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO %>.value.length == 0)
        {
          alert("Valorizzare Numero Provvedimento");
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value=="-")
        {
          alert("Selezionare l'Autorità Emittente");
          document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
          return false;
        }
        
        if(document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.value.length == 0)
        {
          alert("Valorizzare la sede Autorità Emittente");
          document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE %>.focus();

          return false;
        }

        // Controllo Combo Oggetto Procedimento e data scarcerazione.
        if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto Procedimento è obbligatorio");
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>.focus();
          return false;
        }
        
		// Controlli parte variabile della form.        
        var codOggetto = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value ;
        var arrayUDS = [ "2010","2011"];
        var arrayTDS = [ "0030","0031","0032","0033","0031","0201","0202"];

    	// Controllo dati di Rinvio x UDS
        if ($.inArray(codOggetto, arrayUDS) >-1) {
        	// Controllo data differimento
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0].value;
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0].value;
	
	        var data_to_verify = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[0].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[0].value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) ||
	        	data_to_verify.length < 10 )
	        {
	          alert('Data Differimento non valida');
	          document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[0].focus();
	
	          return false;
	        }
        	// Controllo data fine Rinvio.
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].value;
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0].value;
	
	        var data_to_verify = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[0].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[0].value;
	          //alert('data_to_verify = '+data_to_verify);
	          //alert('data_to_verify length = '+data_to_verify.length );
	
        	// Controllo della data fine Rinvio valorizzata ma non valida.
	        if (!ControllaDataPassaVuota(data_to_verify) ||
	        	  (2 < data_to_verify.length	&&
	        		   data_to_verify.length  < 10 ) )
	        {
	          alert('Data fine Rinvio non valida');
	          document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA %>[0].focus();
	          return false;
	        }

        	// Controllo della data fine Rinvio non valida e i quantum di rinvio misura non valorizzati.
        	if (ControllaDataPassaVuota(data_to_verify)  &&
		        (document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[0].value.length>0    &&
			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[0].value.length>0    &&
			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[0].value.length>0  &&
			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked==true ) )
	        {
	        //  alert('Valorizzare Data fine Rinvio o i quantum di rinvio');
	        //  document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA %>[0].focus();
	        //  return false;
	        }
        	// Controllo della data fine Rinvio valida i quantum di rinvio misura valorizzati.
	        if ((ControllaDataPassaVuota(data_to_verify)  &&
	        	  data_to_verify.length == 10 )			  &&
        	   (document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[0].value.length>0    ||
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[0].value.length>0    ||
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[0].value.length>0  ||
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked==true ) )
        	{
		  	       //   alert('Valorizzare solo Data fine Rinvio o i quantum di rinvio');
			       //   document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA %>[0].focus();
			       //   return false;
        	}
        	
	        // Controllo congruenza dati di Rinvio: 
			// I campi "Rinvio fino al"  e "Rivio nella misura" (sia quantum che Fino alla decisione del TDS) non possono essere valorizzati entrambi.
			// I campi "Quantum di rinvio"  e check "fino alla decisione del TDS" non possono essere valorizzati entrambi.
        	if ((document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[0].value.length>0    ||
        		 document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[0].value.length>0    ||
        		 document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[0].value.length>0) &&
        		 document.f.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked==true ) {
  	        //  alert('Valorizzare solo i quantum di rinvio o selezionare "fino alla decisione del TDS"');
	        //  document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[0].focus();
	        //  return false;
        	}
        	
        	// Controllo per MEV_70
       		if( $("#idFlagTribMin").prop('checked') == true ||
       			$("#idFlagTrib").prop('checked') == true )
       		{
       			if (document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value=="TDS" ||
       				document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value=="TDSM" )
                {
                   alert("Selezionare un UFFICIO, e non un TRIBUNALE come Autorità Emittente");
                   document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
	          return false;
        	}
        }

        	if (document.f.TipoUfficioDest.value != "" && 
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>.value == "" ) {
        			alert("Valorizzare correttamente la sede per gli atti trasmessi a");
            		document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE %>.focus();
            		return false;
        	}
        	
        }

    	// Controllo dati di Rinvio x TDS
        if ($.inArray(codOggetto, arrayTDS) >-1) {
        	// Controllo data differimento
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1].value;
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1].value;
	
	        var data_to_verify = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>[1].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>[1].value;
	
	        if (!ControllaDataPassaVuota(data_to_verify) ||
	        	data_to_verify.length < 10 )
	        {
	          alert('Data Differimento non valida');
	          document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>[1].focus();
	
	          return false;
	        }
        	// Controllo data fine Rinvio.
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1].value;
	        if (document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1].value.length==1)
	            document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1].value='0'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1].value;
	
	        var data_to_verify = document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>[1].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>[1].value+'/'+document.f.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>[1].value;
	          //alert('data_to_verify = '+data_to_verify);
	          //alert('data_to_verify length = '+data_to_verify.length );
	
        	// Controllo della data fine Rinvio valorizzata ma non valida.
	        if (!ControllaDataPassaVuota(data_to_verify) ||
	        	  (2 < data_to_verify.length	&&
	        		   data_to_verify.length  < 10 ) )
	        {
	          alert('Data fine Rinvio non valida');
	          document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA %>[0].focus();
	          return false;
	        }

        	// Controllo della data fine Rinvio non valida e i quantum di rinvio misura non valorizzati.
           	if ( ControllaDataPassaVuota(data_to_verify)  &&
       		        (document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[1].value.length>0    &&
       			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[1].value.length>0    &&
       			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[1].value.length>0  &&
       			     document.f.<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked==true ) )
	        {
	  //        alert('Valorizzare Data fine Rinvio o i quantum di rinvio');
	  //        document.f.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA %>[1].focus();
	  //        return false;
	        }
        	// Controllo della data fine Rinvio valida i quantum di rinvio misura valorizzati.
	        if ((ControllaDataPassaVuota(data_to_verify)  &&
	        	  data_to_verify.length == 10 )			  &&
        	   (document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>[1].value.length>0    ||
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>[1].value.length>0    ||
        		document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>[1].value.length>0   ) )
        	{
  	     //     alert('Valorizzare solo Data fine Rinvio o i quantum di rinvio');
	     //     document.f.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA %>[1].focus();
	     //     return false;
        	}
        }
    	
      return true;
      }
  </script>

</head>

<body class="corpo" onLoad="showDivForOggetto();" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento Differimento Pena &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica Differimento Pena &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaDifferimentoPenaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDifferimentoPenaCumulo">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">


  <table width="95%" align="center">
    <tr>
      <td colspan="4" class="titolo">Dati del provvedimento </td>
    </tr>
    <tr>
      <td class="l" width="240" >
        Data emissione provvedimento <font class="ob">(*)</font>
      </td>
      <td class="l">
        <input type="text" Title="Giorno emissione provvedimento" 
          			name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Mese emissione provvedimento"
					name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno emissione provvedimento" 
          			name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aProvvedimento.getDataEmissione(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l" colspan="1">
        <select  Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" >
          <%=tipoProvvedimento%>
        </select>
      </td>
	</tr>
	
	<tr>
	  <td class="l">
	  	Anno / Numero provvedimento <font class="ob">(*)</font> :</td>
	  <td class="l">
	  	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>" size=4 maxlength=4 
			value="<%=StringUtils.toStringJSP (aComputo.getAnnoProvv() )%>"      
	 				<%=IWebConstants.UTIL_DATA_ANNO%> >
	    		/
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" size=8 maxlength=38 
		value="<%=StringUtils.toStringJSP (aComputo.getProgrProvv() )%>"      
	    		onkeypress="return TicTabNumField(this,event)" >
	    </td>
	    <td class="l">Anno / Numero SIUS </td>
	    <td class="l">
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" size=4 maxlength=4 
				value="<%=StringUtils.toStringJSP (aComputo.getAnnoProc() )%>"      
	    		<%=IWebConstants.UTIL_DATA_ANNO%> >
	    		/
	    	<input type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" size=8 maxlength=38 
				value="<%=StringUtils.toStringJSP (aComputo.getProgrProc() )%>"      
	    		onkeypress="return TicTabNumField(this,event)" >
	  </td>
	</tr>     
	
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font> </td>	  
	    <td class="L">
	      	<select Title="Autorità Emittente" name="<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
	        <%=tipoUfficioEmittente%>
	      	</select>
	    </td>
	    <td class="l">Sede <font class="ob">(*)</font> </td>
	    <td class="L" nowrap>
	      	<input Title="Sede Autorità Emittente" name="<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%= aProvvedimento.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35" class="small"> 
	        <a href="Javascript:ListaComuniperTipoUfficio('f','<%=ICostantiTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%= ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.f.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
	          <img src="/images/filefolder.gif" border=0> 
	        </a>
	    </td>
	</tr>
	
	<tr>
	    <td class="l">Oggetto Procedimento <font class=ob>(*)</font></td>
	    <td class="l" colspan="3">
	      <select  Title="Oggetto Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" onChange="Javascript:showDivForOggetto();" >
	        <%=oggettoProvvedimento%>
	      </select>
	    </td>
	    
	</tr>     

  </table>

  <table width="95%" align="center">

	<div id="blank" style="display:block;">
	  <table width="95%" align="center" >
	  <tr>
	   </tr>
	  </table>
	</div>  

	<div id="divUDS" style="display:block;">
	  <table width="95%" align="center" >

  		<tr>
	  		<td class="l" width="210" >
		        Data Decorrenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l" width="210">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %>  		    
         		<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>  
				<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>        			
          	</td>

			<td class="l" width="210" >
		        Data Scadenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l" width="210">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %> 		    
		        <input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>
				<input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>          			
		    </td>
		</tr>
		<tr><td>&nbsp;</td></tr>
  		<tr>
		    <td class="l" width="240" >
		        Data Differimento Esecuzione <font class=ob>(*)</font></td>

		    <td class="l" colspan="3">
		        <input type="text" Title="Giorno data Differimento Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" maxlength="2" size="2"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"dd"))%>"  
		          			<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Mese data Differimento Esecuzione"
							name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>"
							maxlength="2" size="2"
							value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"MM"))%>"
							<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Anno data Differimento Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" maxlength="4" size="4"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"yyyy"))%>"  
		          			<%=IWebConstants.UTIL_DATA_ANNO%>>
		    </td>
		</tr>

		<tr>
		  	<td colspan="4" class="titolo">Durata e Differimento Della Pena </td>
		</tr>

  		<tr>
		    <td class="l" width="240" >Rinvio fino alla decisione del </td>
		    <td class="l">&nbsp;&nbsp; TDS <input type="checkbox" name="<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE%>" 
		    								id="idFlagTrib" <%=strChecked%> value="T" onClick="javascript:passaTds();" >
		    &nbsp;&nbsp;&nbsp;&nbsp; TDS Minorenni <input type="checkbox" name="<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE_MINORI%>"
		    								 id="idFlagTribMin" <%=strCheckedMin%> value="M" onClick="javascript:passaTdsMin();" >
		    </td>
		    <td class="l" colspan="2">&nbsp;</td>
		</tr>
		<tr><td class="l" width="240" > oppure </td></tr> 

		<tr>
	      <td class="l"  width="240">Rinvio nella Misura di </td>
          <td class="l">Anni
	          &nbsp;<input type="text" title="Anni" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumAnniMisura()) %>" 
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          Mesi
	          &nbsp;<input type="text" title="Mesi" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumMesiMisura()) %>" 
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          Giorni
	          &nbsp;<input type="text" title="Giorni" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniMisura()) %>" 
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      </td>
	      
	      <td class="l" colspan="2" > &nbsp;&nbsp;
	      		<a href="Javascript:testCalcolaPresoffertoUds('X');">
	      			<img src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

					Fino al  &nbsp;&nbsp;&nbsp;
		        <input type="text" Title="Giorno data fine Rinvio" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" maxlength="2" size="2"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"dd"))%>"  
		          			<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Mese data fine Rinvio"
							name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>"
							maxlength="2" size="2"
							value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"MM"))%>"
							<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Anno data fine Rinvio" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" maxlength="4" size="4"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"yyyy"))%>"  
		          			<%=IWebConstants.UTIL_DATA_ANNO%>>
		  </td>        			
	      
	   </tr>
		
		<tr>
		  	<td colspan="4" class="titolo"> &nbsp; </td>
		</tr>
		
		<tr>
			<td class="l" colspan="2"> Atti trasmessi al   
	      		&nbsp;&nbsp;&nbsp;&nbsp;<input Title="Tipo Ufficio Destinatario" name="TipoUfficioDest" id="idTipoUfficioDest"  
	      									value="<%=strTipoTrib%>" size="65">
	    	</td>
	    	<td class="l" colspan="2" > Sede &nbsp;&nbsp;&nbsp;&nbsp;
	      		<input Title="Sede TDS destinatario" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>" value="<%=StringUtils.toStringJSP(aComputo.getDescrTDSCompetente(),"")%>" type="text" maxlength="35" size="35" class="small"> 
		        <a href="Javascript:ListaComuniperTipoUfficio('f','<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>','TDS');">
		          <img src="/images/filefolder.gif" border=0> 
		        </a>
	    	</td>	
		</tr>

	  </table>
	</div>

	<div id="divTDS" style="display:block;">
	  <table width="95%" align="center" >

  		<tr>
		    <td class="l" width="240" >
		        Data Decorrenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %>  		    
         		<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataInizio(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>  
				<input type="text" Title="Giorno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_DA %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Decorrenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_DA %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Decorrenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_DA %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneDa(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>        			
          	</td>

			<td class="l" width="240" >
		        Data Scadenza Pena <font class=ob>(*)</font>
		    </td>
		    <td class="l">
<%			if(lPenaResidua!=null && lPenaResidua.getIdPenaResidua()!=null )	{ %> 		    
		        <input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(lPenaResidua.getDataFine(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} else { %>
				<input type="text" Title="Giorno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_GIORNO_DATA_RECLUSIONE_A %>" maxlength="2" size="2"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"dd"))%>"  
          			<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Mese Scadenza Pena"
					name="<%=ICostantiComputiCumulo.CAMPO_TDS_MESE_DATA_RECLUSIONE_A %>"
					maxlength="2" size="2"
					value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"MM"))%>"
					<%=IWebConstants.UTIL_DATA%>> /
        		<input type="text" Title="Anno Scadenza Pena" 
          			name="<%=ICostantiComputiCumulo.CAMPO_TDS_ANNO_DATA_RECLUSIONE_A %>" maxlength="4" size="4"  
          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataReclusioneA(),"yyyy"))%>"  
          			<%=IWebConstants.UTIL_DATA_ANNO%>>
<%			} %>          			
		    </td>
		</tr>
		<tr><td>&nbsp;</td></tr>
  		<tr>
		    <td class="l" width="240" >
		        Data Differimento Esecuzione <font class=ob>(*)</font></td>

		    <td class="l" colspan="3">
		        <input type="text" Title="Giorno data Differimento Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" maxlength="2" size="2"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"dd"))%>"  
		          			<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Mese data Differimento Esecuzione"
							name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>"
							maxlength="2" size="2"
							value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"MM"))%>"
							<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Anno data Differimento Esecuzione" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" maxlength="4" size="4"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataInizioMisura(),"yyyy"))%>"  
		          			<%=IWebConstants.UTIL_DATA_ANNO%>>
		    </td>
		</tr>

		<tr>
		  	<td colspan="4" class="titolo">Durata e Differimento Della Pena </td>
		</tr>

  		<tr>
	      <td class="l"  width="240">Rinvio nella Misura di </td>
          <td class="l">Anni
	          &nbsp;<input type="text" title="Anni" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumAnniMisura()) %>"  
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          Mesi
	          &nbsp;<input type="text" title="Mesi" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumMesiMisura()) %>"
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          Giorni
	          &nbsp;<input type="text" title="Giorni" size=2 maxlength=2 
	                       value="<%=StringUtils.toStringJSP(aComputo.getNumGiorniMisura()) %>" 
	                       name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA %>"  
	                       onkeypress="return TicTabNumField(this,event)">
	          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      </td>
 
	      <td class="l" colspan="2" > &nbsp;&nbsp;
	      		<a href="Javascript:testCalcolaPresofferto('X');">
	      			<img src="<%=IWebConstants.IMAGES_DIR%>freccia_verde.gif" alt="Calcola Quantum" border=0> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

					Fino al  &nbsp;&nbsp;&nbsp;
		        <input type="text" Title="Giorno data fine Rinvio" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" maxlength="2" size="2"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"dd"))%>"  
		          			<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Mese data fine Rinvio"
							name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>"
							maxlength="2" size="2"
							value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"MM"))%>"
							<%=IWebConstants.UTIL_DATA%>> /
		        <input type="text" Title="Anno data fine Rinvio" 
		          			name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" maxlength="4" size="4"  
		          			value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataFineMisura(),"yyyy"))%>"  
		          			<%=IWebConstants.UTIL_DATA_ANNO%>>
		  </td>        			
	   </tr>
	  </table>
	</div>

	  
</table>
<br>
  <table cellspacing="2" cellpadding="2" width="95%" >
  <tr><td>&nbsp;</td></tr>
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

</form>
</body>
</html>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
	
    // Data emissione provvedimento
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","minlen=1","La lunghezza minima per il giorno emissione provvedimento è di 1 carattere");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");
	
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");
	
	frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");


    frmvalidator.setAddnlValidationFunction("Verify");
  </script>