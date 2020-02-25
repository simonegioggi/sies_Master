<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@page import="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<jsp:useBean id="CausaleComputo"      scope="request" class="java.lang.String" />
<jsp:useBean id="lProvv"  scope="request" class="java.util.Vector"/>


<%
//==============================================================================
// Form per l'inserimento dei dati del computo Fungibilità Misura Cautelare
// - PenRes1 = Reclusione
// - PenRes2 = Arresti
// - LAConcesse = 
//==============================================================================
%>

<%

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

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
%>
<head>
  <title> [S.I.E.S.] - Calcolo Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jsrsClient.js"></script>
  <script language="JavaScript">

/*
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
*/
function ControllaCheckBox(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox,dtIni,dtFine,idMC) { 		
  		if (!(cBox.checked)  ) {
  		
  	      ddDa.value="";  
  	      mmDa.value="";  
  	      aaDa.value="";  
  	      ddA.value=""; 
  	      mmA.value=""; 
  	      aaA.value="";  
  	      document.f.appoCheck.value =999999999;
  	      clearQuantum(idMC);
        }
        else {  
       
            var sep = "-";
        	var data_dal = dtIni.value.split(sep);
        	var data_al = dtFine.value.split(sep);
          	    ddDa.value=data_dal[0];
		        mmDa.value=data_dal[1];
		        aaDa.value=data_dal[2];

		        ddA.value=data_al[0];
		        mmA.value=data_al[1];
		        aaA.value=data_al[2];
          	   ddDa.focus(); 
        	}
  	}
  	
  	function ControllaGiornoDa(ddDa,cBox,mmDa) {
  	    
  		if ((!cBox.checked) && (ddDa.value > 0) && (document.f.appoCheck.value !=cBox.value)) {
  	        alert("Cliccare sulla checkBox del periodo in oggetto prima di inserire la Data Da");
            ddDa.focus();
            return false;
        }
        
  	}
  	
  	function DisattivaVincoli(cBox){
  			
  				document.f.appoCheck.value=cBox.value;
  	}
  
  	function AttivaVincoli(){
  		
  				document.f.appoCheck.value=999999999;
  	}
  	
  	
 
     function ControllaPeriodo(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox,dtIni,dtFine,flagCalcolo)
      {
 
		var dA=ddA.value+'/'+mmA.value+'/'+aaA.value;
		
		  var dDa=ddDa.value+'/'+mmDa.value+'/'+aaDa.value; 
	
		 if ((cBox.checked) || (flagCalcolo !='1')) {
		
		 
		   if (! ControllaData(dDa))
		      {
        		alert('Data inizio periodo prenotato non valida');
        		
        		ddDa.focus();
		        return 1;
		      }
		  if ( (!CompareDate(dtIni.value,dDa) ) || (!CompareDate(dDa,dtFine.value)) )
		  {
        		alert('Data inizio periodo fuori intervallo temporale consentito ');
        	
        		ddDa.focus();
		        return 1;
		      }
		       
		    if (! ControllaData(dA))
		      {
        		alert('Data fine periodo prenotato non valida');
        		
        		ddA.focus();
		        return 1;
		      }
	//	  if ( (CompareDate(dA,dtIni.value) ) || (!CompareDate(dA,dtFine.value)) )
	      if ( !CompareDate(dA,dtFine.value)) 
		  {
        		alert('Data Fine periodo fuori intervallo temporale consentito ');
        		
        		ddA.focus();
		        return 1;
		      }
		  if (! CompareDate(dDa,dA) )
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
 //     alert("callCalcolaQuantum: "+idMC);
      var ipos = idMC.indexOf('_');
      var id = idMC.substr(ipos+1);
    //  var id = idMC.substr(idMC.length-1);
     
      //if (controllaPeriodi(idMC)==false)
        //return;
       
       if (document.f.CheckPeriodi == null){
	        var appo = ControllaPeriodo(document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA%>"+id)[0],document.f.CheckPeriodi[id],document.getElementsByName("dtIni"+id)[0],document.getElementsByName("dtFine"+id)[0],'0');
		       		if (appo > 0)	{
					
		       		
		       		return;
		       		}
		   }    		
	       else	{
	       	 var appo = ControllaPeriodo(document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA%>"+id)[0],document.getElementsByName("<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA%>"+id)[0],document.f.CheckPeriodi,document.getElementsByName("dtIni"+id)[0],document.getElementsByName("dtFine"+id)[0],'0');
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
   //   var id = idMisura.substr(idMisura.length-1);

      document.getElementById('Quantum_MC_'+id).innerHTML = strQuantum;
    }
    
    
    function clearQuantum(idMC){
      var ipos = idMC.indexOf('_');
      var id = idMC.substr(ipos+1);
  //    var id = idMC.substr(idMC.length-1);
      document.getElementById('Quantum_MC_'+id).innerHTML = '&nbsp;';
    }
function Verify()
    {
     var check=false;
     if (document.f.CheckPeriodi != null)
    	{
       		if (isNaN(parseInt(document.f.CheckPeriodi.length)))
			 	{
						if (!document.f.CheckPeriodi.checked) {
				            check=false;
				        }   
				        else 
				        	check=true;
    			 }
			 else {
	
	 
	
					  for (lSel=0;lSel<document.f.CheckPeriodi.length;lSel++) {
				          
				          if (document.f.CheckPeriodi[lSel].checked) {
				           
				             check=true;
				          }
				        }
       				 }
       	}
      if ((!check) && (document.f.DaGidalR.value.length == 0)){
          alert("Selezionare almeno un periodo prenotato da importare oppure inserirne uno manualmente!");
          return false;
        }
      if (document.f.DaGidalR.value.length != 0){ 
      

      var dataDAL=document.f.DaGidalR.value +"/"+document.f.DaMedalR.value+"/"+document.f.DaAndalR.value;

      if (! ControllaData(dataDAL))
      {
        alert('Data di Inizio Periodo non valida');
        document.f.DaGidalR.focus();

        return false;
      }

     

      var dataAL=document.f.DaGialR.value +"/"+document.f.DaMealR.value+"/"+document.f.DaAnalR.value;

      if (! ControllaData(dataAL))
      {
        alert('Data di Fine Periodo non valida');
        document.f.DaGialR.focus();

        return false;
      }

      if(!CompareDate(dataDAL, dataAL))
      {
        alert('La Data di Fine Periodo non può essere precedente a quella di Inizio');
        document.f.DaGidalR.focus();

        return false;
      }
	  }
 
    <% 
  
    if (lProvv != null) {
    	int lsel=0;
    for (int i=0; i<lProvv.size();i++) {
    	
    	ProvvedimentoModelBDMC lProvvBdmcMod= (ProvvedimentoModelBDMC) lProvv.get(i);
    	Vector lPeriodi = lProvvBdmcMod.getSbPeriPren();
    if(lPeriodi != null && lPeriodi.size() != 0)
	{
		Iterator lIterPeriodi = lPeriodi.iterator();
		
		
		while(lIterPeriodi.hasNext())
		{
			
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else {
	        	
	        
	        
	        	
	        	
		           String campoCheckBox = ICostantiSbPren.CAMPO_CHECK_PERIODI+"["+lsel+"]";  
	               String campoGiornoDa = ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel;
	            %>
	            
	        
	       
        			
	          if (document.f.CheckPeriodi[0] != null){
	          		var appo = ControllaPeriodo(document.f.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.f.<%=campoCheckBox %>,document.f.dtIni<%=lsel %>,document.f.dtFine<%=lsel %>,'1');
		       		if (appo > 0)	{
	
		       		
		       		return false;
		       		}
	       		 }    		
			       else	{
			      		var appo = ControllaPeriodo(document.f.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.f.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.f.CheckPeriodi,document.f.dtIni<%=lsel %>,document.f.dtFine<%=lsel %>,'1');
		       		if (appo > 0)	{
	
		       		
		       		return false;
		       		}
		       		
			       }
	       	  
	       <% lsel++;} }}}}%>
	
      document.f.subm2.disabled=true;
		
       
     		 document.f.submit();
     
    }


    function Verify_Quantum()
    {
      document.f.operazione.value="Quantum";
      Verify();
    }
  </script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiCompAltroTitolo">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0212">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="006">
    <input type="hidden" name="lFlagPage" value="A">

    <input type="hidden" name="operazione" value="">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Computo misura cautelare Computo altro reato (fungibilità)</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<table>
  <tr>
    <td class="l">
      Posizione Giuridica :
      <font class="campo">
<%
      if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
%>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
<%
      }
      else
      {
%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
      }
%>
      </font>
    </td>
  </tr>
</table>
<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: solo data inizio e data fine MAI
// - Se Libero vengono visualizzati i Quantum 
//   - PenRes1 = Reclusione
//   - PenRes2 = Arresti
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<% // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(  PenaComplessiva.getCodTipoPenaDetentiva() != null
    && PenaComplessiva.getCodTipoPenaDetentiva() != ""
    && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
  {
%>
    <table style="width: 95%;">
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else if (!PenRes1.getErrorMsg().equals("-"))
  {
%>
  <table style="width: 95%;">
    <tr>
      <%if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) { %>
      <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
      <% } else { %>
      <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
      <% } %>
    </tr>

    <%
    if (PenRes1.getErrorMsg().startsWith("Libero")) //Patch per gestire il titolo
    { //
    %>
      <tr>
        <td class="l"> Reclusione :
          Anni <font class=campo><%=PenRes1.getNumAnni()%></font>
          Mesi <font class=campo><%=PenRes1.getNumMesi()%></font>
          Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
          Multa <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
        </td>
        <td class="l"> Arresto :
          Anni <font class=campo><%=PenRes2.getNumAnni()%></font>
          Mesi <font class=campo><%=PenRes2.getNumMesi()%></font>
          Giorni <font class=campo><%=PenRes2.getNumGiorni()%></font>
          Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
        </td>
      </tr>
      
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
  
    <%
    }
    else //non libero
    {
      //==========================================================================
      // Se non libero visualizzo il quantum calcolato al tra la data di sistema
      // e la data fine prevista
      //==========================================================================
      //CalendarUtil lCU = new CalendarUtil();
      //PenRes2.setDataFine(PenRes1.getDataFine());
      //PenRes2.setDataInizio(DateUtils.getSysDate());
      //PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
    %>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="l">
        Anni: <font class=campo><%=PenRes2.getNumAnni()%></font>
        Mesi: <font class=campo><%=PenRes2.getNumMesi()%></font>
        Giorni: <font class=campo><%=PenRes2.getNumGiorni()%></font></td>
      <td class=l>Data Inizio: <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%> </font></td>
      <td class=l>Data Fine: <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy")%> </font></td>
    </tr--%>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr>
    
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
%>
</table>
<%
  }
%>
  <table style="width: 95%;">
  <tr>
    <td class="titolo" colspan=6>Dati identificativi della misura cautelare da computare</td>
  </tr>
  <tr>
    <td class="l">Causale computo : </td>
    <td class="l" colspan=2>
      <select Title="Causale Computo" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_CAUSALE_COMPUTO%>">
        <%=CausaleComputo%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" nowrap>Procedimento R.G.P.M. : </td>
    <td class="l">Anno/Numero
      <input type="text" name="annoReGe" size=4 maxlength=4>
      /
      <input type="text" name="numeroReGe" size=6 maxlength=6>
    </td>
  </tr>
  <tr>
   <td class="l" nowrap>Procedimento B.D.M.C. : </td>
    <td class="l">Anno/Numero
      <input type="text" name="annoMc" size=4 maxlength=4>
      /
      <input type=text name="numeroMc" size=6 maxlength=6>
    </td>
  </tr>
  <tr>
    <td class="l">
      <font class="label">Data presentazione Istanza</font>
    </td>
    <td class="l">
      <input type="text" name="GiornoDataIstanza" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="MeseDataIstanza" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input  type="text" name="AnnoDataIstanza" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  <tr>
    <td class="l" colspan=5>
      <input type="radio" name="TipoOrd" value="SenzaRichiestaPPP" checked>d'ufficio &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="ConformePPP">su richiesta difensore &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="DifformePPP">su richiesta interessato
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td><input type="hidden" name="PM" value="-"></td>
  </tr>
  <tr>
    <td class="titolo" colspan=6>Periodo da computare</td>
  </tr>
  <tr>
    <td class=l colspan=1>
      <font class="label">Dalla Data &nbsp;&nbsp;</font>
      <input type="text" name="DaGidalR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaMedalR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input  type="text" name="DaAndalR" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class=l colspan=1>
      <font  class="label">Alla Data &nbsp;&nbsp;</font>
      <input type="text" name="DaGialR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaMealR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input  type="text" name="DaAnalR" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiPresoffertoInclude.jsp"/>

<table style="width: 95%;">
  <tr>
    <td class="c">
      <font class="label">Note</font>
    </td>
    <td class="l">
      <textarea cols="80" rows="6" name="noteRec"></textarea>
    </td>
  </tr>
</table>
<br>
<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT class="bottone" type="button" name="subm2" value="Conferma" onClick="javascript:Verify_Quantum();">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

</body>
</html>