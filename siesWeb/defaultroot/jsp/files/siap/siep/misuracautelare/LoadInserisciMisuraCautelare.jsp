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
         //lAzione = "siap.siep.misuracautelare.action.ActInserisciMisuraCautelare";
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

<%
//==============================================================================
// primaSezione = S indica la presenza di una misura cautelare computabile 
// in corso (con data fine a null). In questo caso non viene visualizzata la
// relativa sezione di inserimento in quanto può essere presente una sola
// misura in corso di espiazione.
//==============================================================================
  if(!primaSezione.equals("S"))
  {
    // Solo se non esiste una misura cautelare computabile in corso visualizzo 
    // i campi per inserirla
%>
    <table cellspacing=2 cellpadding=2 width=100%>
    <!--In Corso al Momento del Passaggio in Giudicato -->
      <tr>
        <td class="Titolo"> In Corso al Momento del Passaggio in Giudicato </td>
      </tr>
      <tr>
        <td class="l">
          <table cellspacing=1 cellpadding=1>
            <tr>
              <td class="l">Misura<font class=ob>(*)</font></td>
              <td class="l">
                <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>">
                  <%=tipoMisura%>
                </select>
              </td>
              <td class="L" > Da <font class=ob>(*)</font></td>
              <td class="L" >
                <input title="Giorno Data inizio" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
                 -
                <input title="Mese Data inizio" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
                 -
                <input title="Anno Data inizio" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
              </td>
            </tr>
          </table>
          
          <table>
            <tr>
              <td class="l" >Istituto</td>
              <%if(primaSezione.equals("N")){%>
                <% if(lMis.getIstitutoDetenzione() == null) { %>
                  <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="" size=50>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[0]','Comune[0]');">
                  <img src="/images/filefolder.gif" border=0></a></td>
                <%} else {%>
                  <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=50>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=50>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[0]','Comune[0]');">
                  <img src="/images/filefolder.gif" border=0></a></td>
                <%}%>
              <%}else{%>
                <%if(lMis.getIstitutoDetenzione() == null){%>
                  <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="" size=50>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[0]','Comune[0]');">
                  <img src="/images/filefolder.gif" border=0></a></td>
                <%}else {%>
                  <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=50>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=50>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[0]','Comune[0]');">
                  <img src="/images/filefolder.gif" border=0></a></td>
                <%}%>
              <%}%>
              <td class="l" >Altro Luogo</td>
              <td class="l">
                <input value="<%=lMis.getAltroLuogoDetenzione()%>" title="Altro Luogo Detenzione" type="text" name="<%= ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
<%
}  // Fine sezione per inserimento con misura cautelare in corso
%>

<%
//==============================================================================
//Cessati al Momento del Passaggio in Giudicato - Computabili
//Periodi di presofferto da Banca Dati Misure Cautelari
//==============================================================================

%>

<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiPresoffertoInclude.jsp"/>

<%
//==============================================================================
// Cessati al Momento del Passaggio in Giudicato - Computabili
// Vengono visualizzate 2 sezioni
//==============================================================================
%>
<table cellspacing=1 cellpadding=1 width=100%>
  <tr>
    <td class="Titolo" > Cessati al Momento del Passaggio in Giudicato - Computabili </td>
  </tr>
  <% for(int i=1;i<3;i++) { %>
  <tr>
    <td class="l">
      <table cellspacing=1 cellpadding=1>
        <tr>
          <td class="l">Misura<font class=ob>(*)</font></td>
          <td class="l">
            <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>" >
              <%=tipoMisura%>
            </select>
          </td>
          <td class="l">Da <font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" size="2" maxlength="2" title="Giorno Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
          -
            <input type="text" size="2" maxlength="2" title="Mese Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
          -
            <input type="text" size="4" maxlength="4" title="Anno Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
          <td class="l">a<font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" size="2" maxlength="2" title="Giorno Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
          -
            <input type="text" size="2" maxlength="2" title="Mese Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
          -
            <input type="text" size="4" maxlength="4" title="Anno Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
        </tr>
      </table>
            
      <table cellspacing=1 cellpadding=1>
        <tr>
          <td class="l" >Istituto</td>
          <%
          if(primaSezione.equals("N")) // E' presente la sezione con le Misure in Corso  
          { // E' presente la sezione con le Misure in Corso
              if(lMis.getIstitutoDetenzione() == null)
              {%>
                  <td class="l">
                    <input readonly Title="Istituto" name="Comune" value="" size=50>
                    <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
                    <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i%>]','Comune[<%=i%>]');">
                    <img src="/images/filefolder.gif" border=0></a>
                  </td>
              <% } else { %>
                  <td class="l">
                    <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=50>
                    <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=50>
                    <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i%>]','Comune[<%=i%>]');">
                   <img src="/images/filefolder.gif" border=0></a>
                 </td>
              <% } %>
          
          <% } else { %>
              
              <%if(lMis.getIstitutoDetenzione() == null){%>
                  <td class="l">
                    <input readonly Title="Istituto" name="Comune" value="" size=50>
                    <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
                    <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i-1%>]','Comune[<%=i-1%>]');">
                    <img src="/images/filefolder.gif" border=0></a>
                  </td>
              <%}else {%>
                <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=50>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=50>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i-1%>]','Comune[<%=i-1%>]');">
                  <img src="/images/filefolder.gif" border=0></a>
                </td>
              <%}%>
          <%}%>


        </tr>
        <tr>
          <td class="l" >Altro Luogo </td>
          <td class="l">
             <input value="<%=lMis.getAltroLuogoDetenzione()%>" title="Altro Luogo Detenzione" type="text" name="<%= ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
          </td>
        </tr>
      </table>

      <table width=100%><tr><td width=100%><hr width=100%></td></tr></table>

    <%} // end for misure computabili%>
  </td>
 </tr>
</table>
<%
//==============================================================================
// Cessati al Momento del Passaggio in Giudicato - Non Computabili
// vengono visualizzate 2 sezioni
//==============================================================================
%>
<table cellspacing=1 cellpadding=1 width=100%>
  <tr><td class="Titolo" > Cessati al Momento del Passaggio in Giudicato - Non Computabili </td></tr>
  
  <% for(int i=0;i<2;i++) {%>
  <tr>
    <td class="l">
      <table  cellspacing=1 cellpadding=1>
        <tr>
          <td class="l">Misura<font class=ob>(*)</font></td>
          <td class="l">
            <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>" >
              <%=tipoMisura%>
            </select>
          </td>
          <td class="l">Da<font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" title="Giorno Data inizio" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Mese Data inizio" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Anno Data inizio" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
          <td class="l">a<font class=ob>(*)</font></td>
          <td class="L">
            <input type="text" title="Giorno Data fine" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Mese Data fine" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input type="text" title="Anno Data fine" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
        </tr>
      </table>
      
      <table  cellspacing=1 cellpadding=1>
        <tr>
          <%
          if(primaSezione.equals("N"))
          { // E'presente la sezione con le misure in corso di espiazione 
            // per cui esistono già 3 sezioni, questa è la quarta.
            // L'indice deve partire da i+3 (0,1,2,3)
          %>
          <td class="l" >Istituto</td>
            <% if(lMis.getIstitutoDetenzione() == null) { %>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=45>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=45>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i+3%>]','Comune[<%=i+3%>]');">
              <img src="/images/filefolder.gif" border=0></a>
            </td>
            <%}else {%>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=45>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=45>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i+3%>]','Comune[<%=i+3%>]');">
              <img src="/images/filefolder.gif" border=0></a>
            </td>
            <%}%>
          <%
          } 
          else 
          { 
            // Non esiste la prima sezione (misure in corso). Sono rpesenti solo due sezioni
            // l'indice deve partire da i+2
          %>
          <td class="l" >Istituto</td>
              <% if(lMis.getIstitutoDetenzione() == null) { %>
                <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="" size=45>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=45>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i+2%>]','Comune[<%=i+2%>]');">
                  <img src="/images/filefolder.gif" border=0></a>
                </td>
              <%}else {%>
                <td class="l">
                  <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=45>
                  <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=45>
                  <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>[<%=i+2%>]','Comune[<%=i+2%>]');">
                  <img src="/images/filefolder.gif" border=0></a>
                </td>
              <%}%>
          <% } %>
        </tr>
            
        <tr>
          <td class="l" >Altro Luogo </td>
          <td class="l">
            <input value="<%=lMis.getAltroLuogoDetenzione()%>" type="text" title="Altro Luogo Detenzione" name="<%=ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
          </td>
          <td class="l" >Motivo non Computabilità </td>
          <td class="l">
            <select Title="Motivo Computabilità" name="<%= ICostantiMisuraCautelare.CAMPO_COD_MOTIVO_NON_COMPUTABILE%>">
            <%=motComp%>
            </select>
          </td>
        </tr>
      </table>


      <table  cellspacing=1 cellpadding=1>
        <tr>
          <td class="l" >Ufficio </td>
          <td class="l">
            <select  title="Ufficio" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_RIFER %>">
            <%=tipUff%>
            </select>
          </td>
          <td class="l">Luogo</td>
          <td class="l">
            <%if(primaSezione.equals("N")){%>
            <input value="" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>">
            <a href="Javascript:ListaComuni('LoadInserisciMisuraCautelare','<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>[<%=i%>]');">
              <img src="/images/filefolder.gif" border=0></a>
            <%}else{%>
               <input value="" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>">
            <a href="Javascript:ListaComuni('LoadInserisciMisuraCautelare','<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>[<%=i%>]');">
              <img src="/images/filefolder.gif" border=0></a>
            <%}%>
          </td>
        </tr>
      </table>
          
      <table  cellspacing=1 cellpadding=1>
        <tr>
          <td class="l" >N° SIEP/RES</td>
          <td class="l">
            <input value="<%=lMis.getNumRifer()%>" type="text" title="N°SIEP/RES" name="<%= ICostantiMisuraCautelare.CAMPO_NUM_RIFER %>">
          </td>
          <td class="l" >Data Provvedimento</td>
          <td class="l">
            <input  title="Giorno Data Provvedimento" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input  title="Mese Data Provvedimento" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA%>>
            -
            <input title="Anno Data Provvedimento" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
          <td class="l" >Note</td>
          <td class="l">
            <textarea rows=2 cols=25 name="<%= ICostantiMisuraCautelare.CAMPO_NOTE %>"></textarea>
          </td>
        </tr>
      </table>

      <table width=100%><tr><td width=100%><hr width=100%></td></tr></table>
    </td>
  </tr>
  <%} // end for%>
</table>

<%
//==============================================================================
//Cessati al Momento del Passaggio in Giudicato - Computabili
//Periodi di presofferto da Banca Dati Misure Cautelari
//==============================================================================

%>

<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPresoffertoNonComputabile.jsp"/>

<table>
  <tr>
    <td colspan=2>
      <br>
      <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma">
    </td>
  </tr>
</table>

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraCautelare");

  <%
  int indice = 0;
  if(primaSezione.equals("N"))
    indice=5;
  else
    indice=4;

  for(int i=0;i<indice;i++)
  {
  %>
      //Controlli validità DATA INIZIO
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","<%=i%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","<%=i%>","lt=31");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","<%=i%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","<%=i%>","lt=12");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","<%=i%>","gt=1900");

      <%if(i<4){%>
      //Controlli validità DATA FINE
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","<%=i%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","<%=i%>","lt=31");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","<%=i%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","<%=i%>","lt=12");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","<%=i%>","gt=1900");
      <%}%>

   <%}%>

   <%
   for(int y=0;y<2;y++)
  {

  %>

//Controlli validità DATA PROVVEDIMENTO
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA%>","<%=y%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA%>","<%=y%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA%>","<%=y%>","lt=31");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA%>","<%=y%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA%>","<%=y%>","gt=1");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA%>","<%=y%>","lt=12");

      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA%>","<%=y%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA%>","<%=y%>","gt=1900");

   <%}%>
  frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>