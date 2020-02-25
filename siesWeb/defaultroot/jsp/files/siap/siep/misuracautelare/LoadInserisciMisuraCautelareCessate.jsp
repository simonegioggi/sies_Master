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

<jsp:useBean id="misuracautelare" scope="request" class="siap.siep.misuracautelare.model.MisuraCautelareModel"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="motComp"         scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUff"          scope="request" class="java.lang.String"/>
<jsp:useBean id="primaSezione"    scope="request" class="java.lang.String"/>
<jsp:useBean id="lProvv"  scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Form di inserimento delle Misure Cautelare (fase di Iscrizione)
// Attenzione!! La form non viene mai utilizzata in modifica, quindi modalita = 'I'
//              sempre e misuracautelare = null sempre
//==============================================================================
%>
<html>
<!-- LoadInserisciMisuraCautelare.jsp -->
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
  	      document.LoadInserisciMisuraCautelare.appoCheck.value =999999999;
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
  	
  	function ControllaGiornoDa(ddDa,cBox,mmDa) {
  	    	
  		if ((!cBox.checked) && (ddDa.value > 0) && (document.LoadInserisciMisuraCautelare.appoCheck.value !=cBox.value)) {
  	        alert("Cliccare sulla checkBox del periodo in oggetto prima di inserire la Data Da");
            ddDa.focus();
            return false;
        }
        
  	} 
  	
  	function DisattivaVincoli(cBox){
  			
  				document.LoadInserisciMisuraCautelare.appoCheck.value=cBox.value;
  	}
  
  	function AttivaVincoli(){
  		
  				document.LoadInserisciMisuraCautelare.appoCheck.value=999999999;
  	}
  	
  	
 
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
	//==========================================================================
    // Recupera data inizio e data fine e chiama la servlet per il calcolo dei 
    // quantum
    //==========================================================================
    var idMisura;
    function callCalcolaQuantum (idMC) {
      //alert("callCalcolaQuantum: "+idMC);
      var ipos = idMC.indexOf('_');
      var id = idMC.substr(ipos+1);
  //    var id = idMC.substr(idMC.length-1);
     
      //if (controllaPeriodi(idMC)==false)
        //return;
       if (document.LoadInserisciMisuraCautelare.CheckPeriodi == null){

	        var appo = ControllaPeriodo(document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA%>"+id)[0],document.LoadInserisciMisuraCautelare.CheckPeriodi[id],document.getElementsByName("dtIni"+id)[0],document.getElementsByName("dtFine"+id)[0],'0');
		       		if (appo > 0)	{
					
		       		
		       		return;
		       		}
		   }    		
	       else	{
	       	 var appo = ControllaPeriodo(document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA%>"+id)[0],document.LoadInserisciMisuraCautelare.CheckPeriodi,document.getElementsByName("dtIni"+id)[0],document.getElementsByName("dtFine"+id)[0],'0');
		       		if (appo > 0)	{
					
		       		
		       		return;
		       		}
	       
	       }
      idMisura= idMC;
      
      
      
      var gg_dal = document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA%>"+id)[0];
      var mm_dal = document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA%>"+id)[0];
      var aa_dal = document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA%>"+id)[0];
	
      var gg_al = document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA%>"+id)[0];
      var mm_al = document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA%>"+id)[0];
      var aa_al = document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA%>"+id)[0];
      
     
      var dataDAL = gg_dal.value +"/"+mm_dal.value+"/"+aa_dal.value;
      var dataAL  = gg_al.value +"/"+mm_al.value+"/"+aa_al.value;
	 
      
      // verifica che data inizio e fine appartengano all'intervallo selezionato
      var myParams = new Array(gg_dal.value,
                               mm_dal.value,
                               aa_dal.value,
                               gg_al.value,
                               mm_al.value,
                               aa_al.value
                              );

      document.body.style.cursor='wait';
     
      // Chiamata:
      // jsrsExecute(<nome servlet>,<funzione js da invocare al ritorno>, <nome del metodo server da invocare>, <parametro da passare al server o array di parametri>
      jsrsExecute("/CaricaHTML_Servlet", caricaQuantum, "getQuantumIntervallo",myParams);      
		
    }

    //==========================================================================
    // Funzione invocata di ritorno. Riceve in input una stringa con i quantum
    // e visualizza il dato nell'opportuno campo
    //==========================================================================
    function caricaQuantum(valueTextStr){
    
      document.body.style.cursor='auto';
      
      
      var sep = "~#";
      var aPairs = valueTextStr.split(sep);

      strQuantum = 'Anni <font class="campo">'+aPairs[0]+'</font> '+
                   'Mesi <font class="campo">'+aPairs[1]+'</font> '+
                   'Giorni <font class="campo">'+aPairs[2]+'</font>';
      
      var ipos = idMisura.indexOf('_');
      var id = idMisura.substr(ipos+1);
    //  var id = idMisura.substr(idMisura.length-1);

      document.getElementById('Quantum_MC_'+id).innerHTML = strQuantum;
    }
    
    
    function clearQuantum(idMC){
      var ipos = idMC.indexOf('_');
      var id = idMC.substr(ipos+1);
 //     var id = idMC.substr(idMC.length-1);
      document.getElementById('Quantum_MC_'+id).innerHTML = '&nbsp;';
    }
  //============================================================================
  // Funzione per la verifica dell'inserimento di almeno una Misura cautelare
  // e della correttezza delle date
  //============================================================================
  function Verify()
  {
    var i = 0;
    var almenoUna=false;
    
    // controllo esistenza almeno una misura cautelare inserita
    <% if(primaSezione.equals("N")) { %>
      var indice=5; // presenti 5 sezioni
    <% } else { %>
      var indice=4; // presenti 4 sezioni (manca la sezione delle misure in corso)
    <% } %>
      
    //==========================================================================
    // Ciclo for sulle sezioni
    //==========================================================================
    for(i=0;i<indice;i++)
    {
      var misura = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>[i][document.LoadInserisciMisuraCautelare.<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>[i].selectedIndex].value;
      
      //==================================
      // Recupero la data Inizio misura
      //==================================
      var giornoDataInizio = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>[i].value;
      var meseDataInizio   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>[i].value;
      var annoDataInizio   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>[i].value;

      if(giornoDataInizio.length==1)
        giornoDataInizio='0'+giornoDataInizio;
      if(meseDataInizio.length==1)
        meseDataInizio='0'+meseDataInizio;

      //==================================
      // Recupero la data Fine misura
      //==================================
      <% if(primaSezione.equals("N")) { %>
      // E' presente la sezione con le Misure in corso di espiazione
          if(i>0)
          { // i campi data fine sono uno in meno di quelli data inizio
            var giornoDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>[i-1].value;
            var meseDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>[i-1].value;
            var annoDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>[i-1].value;

            if(giornoDataFine.length==1)
              giornoDataFine='0'+giornoDataFine;
            if(meseDataFine.length==1)
              meseDataFine='0'+meseDataFine;
          }
      <% } else { %>
          var giornoDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>[i].value;
          var meseDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>[i].value;
          var annoDataFine=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>[i].value;
          
          if(giornoDataFine.length==1)
            giornoDataFine='0'+giornoDataFine;
          if(meseDataFine.length==1)
            meseDataFine='0'+meseDataFine;
      <%}%>

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
         
        <%if(primaSezione.equals("N")){%>
        if(i>0)
        { // se non mi trovo nella sezione della misure in corso verifico la data fine
          dataFine=giornoDataFine+"/"+meseDataFine+"/"+annoDataFine;

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
        }
        <%}else{%>
        dataFine=giornoDataFine+"/"+meseDataFine+"/"+annoDataFine;
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
        <%}%>
      } // end if(misura!="-")
    } // end ciclo for
    if (document.LoadInserisciMisuraCautelare.CheckPeriodi != null && !almenoUna)
       {
	       if (isNaN(parseInt(document.LoadInserisciMisuraCautelare.CheckPeriodi.length)))
			 {
							
							if (!document.LoadInserisciMisuraCautelare.CheckPeriodi.checked) {
					            almenoUna=false;
					        }   
					        else 
					        	almenoUna=true;
	    	 }
	 		 else {
	
	 
	
			  for (lSel=0;lSel<document.LoadInserisciMisuraCautelare.CheckPeriodi.length;lSel++) 
				  		{
			          		
					          if (document.LoadInserisciMisuraCautelare.CheckPeriodi[lSel].checked) {
					           
					             almenoUna=true;
					          }
			        	}
		        	}
        }
        if (document.LoadInserisciMisuraCautelare.CheckPeriodiNC != null && !almenoUna)
       {
	       if (isNaN(parseInt(document.LoadInserisciMisuraCautelare.CheckPeriodiNC.length)))
			 {
							
							if (!document.LoadInserisciMisuraCautelare.CheckPeriodiNC.checked) {
					            almenoUna=false;
					        }   
					        else 
					        	almenoUna=true;
	    	 }
	 		 else {
	
	 
	
			  for (lSel=0;lSel<document.LoadInserisciMisuraCautelare.CheckPeriodiNC.length;lSel++) 
				  		{
			          		
					          if (document.LoadInserisciMisuraCautelare.CheckPeriodiNC[lSel].checked) {
					           
					             almenoUna=true;
					          }
			        	}
		        	}
        }
    if(!almenoUna)
    {
      alert("Selezionare almeno una misura cautelare");
      return false;
    }
    
   if (document.LoadInserisciMisuraCautelare.CheckPeriodi != null)
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
	         if (document.LoadInserisciMisuraCautelare.CheckPeriodi[0] != null){
	          		var appo = ControllaPeriodo(document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%=campoCheckBox %>,document.LoadInserisciMisuraCautelare.dtIni<%=lsel %>,document.LoadInserisciMisuraCautelare.dtFine<%=lsel %>,'1');
		       		if (appo > 0)	{
	
		       		
		       		return false;
		       		}
	       		 }    		
			       else	{
			      		var appo = ControllaPeriodo(document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.LoadInserisciMisuraCautelare.CheckPeriodi,document.LoadInserisciMisuraCautelare.dtIni<%=lsel %>,document.LoadInserisciMisuraCautelare.dtFine<%=lsel %>,'1');
		       		if (appo > 0)	{
	
		       		
		       		return false;
		       		}
			       }
	       	  
	       <% lsel++;}  cambioPrenotazione = lPeriodo.getIdPren();}
			
				
	     }}}%>
	  }

    return true;
  }
  
  function passaggioInGiudicatoComputabili()
  { //Inserisci Misura Cautelare Cessate al momento del passaggio in giudicato computabili
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuracautelare.action.ActLoadInserisciMisuraCautelareCessComp";
  }
  
  function passaggioInGiudicatoNonComputabili()
  { //Inserisci Misura Cautelare Cessate al momento del passaggio in giudicato non computabili
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuracautelare.action.ActLoadInserisciMisuraCautelareCessNonComp";
  }

</script>

<script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>
  <script language="JavaScript">
    var desktop;
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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




<body class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
       MisuraCautelareModel lMis = new MisuraCautelareModel();
       String lAzione = new String();
       if( modalita.equals("I") )
       {
         lAzione = "siap.siep.misuracautelare.action.ActInserisciMisuraCautelare";
%>
         <font class="campo">Inserimento Misura Cautelare</font>
<%
       }
       else if( modalita.equals("M") )
       {
         lAzione = "siap.siep.misuracautelare.action.ActModificaMisuraCautelare";
         lMis = misuracautelare;
%>
         <font class="campo">Modifica Misura Cautelare</font>
<%
       }
%>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraCautelare">
  <input type="HIDDEN" name="PrimaSezione" value="<%=primaSezione%>" >
  <input type="HIDDEN" name="Action"       value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>" value="<%=lMis.getIdMisuraCautelare()%>">


   


<BR>
<table cellpadding="25" cellspacing="5" width="100%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	<tr>
			<td class="Titolo" colspan="6"> Misure Cautelari </td>
	</tr>
	<tr>
		<td width="32%" class="menulines" nowrap><a href="javascript:passaggioInGiudicatoComputabili();">Cessata al momento del passaggio in giudicato computabili</a></td>

		<td width="32%" class="menulines" nowrap><a href="javascript:passaggioInGiudicatoNonComputabili();">Cessata al momento del passaggio in giudicato non computabili</a></td>
	</tr>
</table>





</form>

</body>
</html>