<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.math.RoundingMode"%>

<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.siep.util.CaricaHTML_Servlet" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>


<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo"          scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="misureCautelari"    scope="request" class="java.util.Vector" />
<jsp:useBean id="misureCautelariDateNull"    scope="request" class="java.util.Vector" />
<jsp:useBean id="ComingFromInsert"   scope="request" class="java.lang.String"/>
<jsp:useBean id="PosizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="LuogoDetenzione"    scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="lPenaResMod"    	 scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<%
//==============================================================================
// Form di visualizzazione dell'elenco delle Misure Cautelari inserite in fase
// di Iscrizione Fascicolo
//==============================================================================
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Misure Cautelari</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>jquery-1.6.2.min.js"></script>
    
    <script language="JavaScript">

	    function ViewModificaTotaleGiorni(a_formname,a_fieldname,a_fieldname1,a_fieldname2,a_fieldname3,codTipoMisura,idMisuraCautelare)
	 	{   
	 		var quantumNumGMA=document.getElementById('quantumNumGMA'+codTipoMisura);
	 		var quantumNumG=document.getElementById('quantumNumG'+codTipoMisura);
	 		quantumNumGMA.style.color = "red"; 
	 		quantumNumG.style.color = "red";  		
	 		document.getElementById('campoHiddenGiorniRisConv').value=a_fieldname1;
	 		document.getElementById('campoHiddenMesiRisConv').value=a_fieldname2;
	 		document.getElementById('campoHiddenAnniRisConv').value=a_fieldname3;
	 		
	 		var anni = document.getElementById(a_fieldname3);
	 		var mesi = document.getElementById(a_fieldname2);
	 		var giorni = document.getElementById(a_fieldname1);
	 		var giorniTot = document.getElementById(a_fieldname);
	 		
	 		anni.style.color = "red"; 
	 		mesi.style.color = "red";
	 		giorni.style.color = "red";
	 		giorniTot.style.color = "red";
	 		
	 		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadModificaTotaleGiorni&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname1="+a_fieldname1+"&fieldname2="+a_fieldname2+"&fieldname3="+a_fieldname3+"&idMisuraCautelare="+idMisuraCautelare, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=150");

	 	}
    
	 	function ViewModificaRisultatoConversione(a_formname,a_fieldnameTot,a_fieldname,a_fieldname2,a_fieldname3,codTipoMisura,idMisuraCautelare)
	  	{   
	 		var quantumNumGMA=document.getElementById('quantumNumGMA'+codTipoMisura);		
	 		var quantumNumG=document.getElementById('quantumNumG'+codTipoMisura);
	 		
	 		var anni = document.getElementById(a_fieldname3);
	 		var mesi = document.getElementById(a_fieldname2);
	 		var giorni = document.getElementById(a_fieldname);
	 		var giorniTot = document.getElementById(a_fieldnameTot);

	 		quantumNumGMA.style.color = "red"; 
	 		quantumNumG.style.color = "red"; 
	 		anni.style.color = "red"; 
	 		mesi.style.color = "red";
	 		giorni.style.color = "red";
	 		giorniTot.style.color = "red";
	 		
	 		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadModificaRisultatoConversione&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&fieldname3="+a_fieldname3+"&idMisuraCautelare="+idMisuraCautelare, "Ricerca","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=150");
	  	}
	
	 	 function loadModificaTotaleGiorni (totGiorni,idMisuraCautelare) {
	 		$.ajaxSetup({cache: false});	     	
	     	$.ajax({
	     	    url: '/jsp/files/siap/siep/calcolopena/ModificaTotaleGiorniData.jsp',
	     	    type: 'GET',
	     	    cache: false,
	     	    data: {totGiorni: totGiorni,idMisuraCautelare:idMisuraCautelare},
	     	    success: function(data){ 	  
	     	    	
	     	    	var data_split=data.split("|");	  
	     	    	
	     	    	var campoHiddenGiorniRisConv=eval("document.getElementById('campoHiddenGiorniRisConv').value");
	     	    	var campoHiddenMesiRisConv=eval("document.getElementById('campoHiddenMesiRisConv').value");
	     	    	var campoHiddenAnniRisConv=eval("document.getElementById('campoHiddenAnniRisConv').value");	     	    	
	     	    	eval('document.forms["elenco"].'+campoHiddenGiorniRisConv+'.value='+data_split[1]);
	     	    	eval('document.forms["elenco"].'+campoHiddenMesiRisConv+'.value='+data_split[2]);
	     	    	eval('document.forms["elenco"].'+campoHiddenAnniRisConv+'.value='+data_split[3]);	   
	     	    	
	     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaGiorni.value='+data_split[4]);
	     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaMesi.value='+data_split[5]);
	     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaAnni.value='+data_split[6]);
	     		},
	     	    error: function(data) {
	     	        // alert('ERROR @@@@');
	     	    }
	     	});    	
	     }
	 	 
	 	 function loadModificaRisultatoConversione (giorni,mesi,anni,idMisuraCautelare) {
		 		$.ajaxSetup({cache: false});	     	
		     	$.ajax({
		     	    url: '/jsp/files/siap/siep/calcolopena/ModificaRisultatoConversioneData.jsp',
		     	    type: 'GET',
		     	    cache: false,
		     	    data: {giorni: giorni,mesi: mesi,anni: anni,idMisuraCautelare:idMisuraCautelare},
		     	    success: function(data){   
		     	    	var data_split=data.split("|");		     	    	     	    	
		     			var totCustodiaCautelareSoffertaAnni=eval("document.getElementById('totCustodiaCautelareSoffertaAnni').value");
		     	    	var totCustodiaCautelareSoffertaMesi=eval("document.getElementById('totCustodiaCautelareSoffertaGiorni').value");
		     	    	var totCustodiaCautelareSoffertaGiorni=eval("document.getElementById('totCustodiaCautelareSoffertaGiorni').value");
		     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaGiorni.value='+data_split[0]);
		     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaMesi.value='+data_split[1]);
		     	    	eval('document.forms["elenco"].totCustodiaCautelareSoffertaAnni.value='+data_split[2]);
		     	    	
		     		},
		     	    error: function(data) {
		     	        // alert('ERROR @@@@');
		     	    }
		     	});    	
		     }

	 	function ViewModRisultatoConversionePerModTotaleGiorni(quantumNumGmodificato){
	 		//alert('ViewModRisultatoConversionePerModTotaleGiorni quantumNumGmodificato '+quantumNumGmodificato);
	 	}
	</script>
  
  </head>

<BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Misure Cautelari</font></td>
      </tr>
    </table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

    <br>

    <table cellpadding=2 cellspacing=2>
      <tr>
        <td class="C">Significato Colori :</td>
        <td class="C">Blu - Non Computabile</td>
        <td class="CVerde">Verde - Computabile</td>
      </tr>
    </table>
    
    <table cellpadding=2 cellspacing=2>
      <tr>
        <td class="CNoBord">&nbsp;</td>
      </tr>
    
      <tr>
        <td class="int">Tipo Misura</td>
        <td class="int">Data Inizio</td>
        <td class="int">Data Fine</td>
        <td class="int" colspan="4">Totale</td>
        <td class="int">Presso</td>
        <td class="int">Azioni</td>
      </tr>

      <%
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      // Attenzione modificare questa parte, i quantum vengono calcolati in fase
      //            di inserimento delle modifica, non devono essere ricalcolati
      //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      String modificabileGiorniRisultatoConversione = "";
      String esisteCL="dimensioneTabellaUno";
      int sommaColonnaAnniModManuale = 0;
      int sommaColonnaMesiModManuale = 0;
      int sommaColonnaGiorniModManuale = 0;
      String quantum=new String();
      String quantumGiorni=new String();
      String quantumNumGiorniMTG=new String();
      String quantumNumMesiMTG=new String();
      String quantumNumAnniMTG=new String();
      CalendarUtil cu=new CalendarUtil();
      CalendarModel cm;
      CalendarModel ctot=new CalendarModel();
      String TipoMisura=new String();
      String classFont="C";
      String classFontSecondario="C";
      String classFontSecondarioTotG="C";
      //inizio calcolo giorni  per periodi sovrapposti
      Vector vectorPeriodi = new Vector();      
      //fine calcolo giorni  per periodi sovrapposti
      Iterator itx = misureCautelari.iterator();
      int indiceTipoMisura=0;
      while ( itx.hasNext())
      { indiceTipoMisura=indiceTipoMisura+1;
        MisuraCautelareModel lMis = (MisuraCautelareModel)itx.next();
        TipoMisura=StringUtils.toStringJSP(lMis.getDescrTipoMisura());
        
        if (TipoMisura.equalsIgnoreCase("Carcere"))
          TipoMisura="CUSTODIA IN CARCERE";
          
        if (lMis.getDataFine()!=null && lMis.getDataInizio()!=null)
        {
          cm=new CalendarModel();
          //cm.setDataFine(lMis.getDataFine());
          //cm.setDataInizio(lMis.getDataInizio());
          //cm=cu.ricalcolaGAM(cu.CalcolaNumGiorniMesiAnni(cm));
          
          //inizio calcoloGioniMesiAnni A.S. gennaio2015
          Date datainizio = lMis.getDataInizio();             
          String ggInizio = DateUtils.getDayToString   (datainizio);
          String mmInizio   = DateUtils.getMonthToString (datainizio);
          String aaInizio   = DateUtils.getYearToString  (datainizio);
          
          Date datafine = lMis.getDataFine();             
          String ggFine = DateUtils.getDayToString   (datafine);
          String mmFine   = DateUtils.getMonthToString (datafine);
          String aaFine   = DateUtils.getYearToString  (datafine);
             
          String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
  		  String sep = "~#";
  		  String[] aPairs = new String[3];
		  aPairs = calcoloGioniMesiAnni.split(sep);

		  cm.setNumAnni(Integer.parseInt(aPairs[0]));
		  cm.setNumMesi(Integer.parseInt(aPairs[1]));
		  cm.setNumGiorni(Integer.parseInt(aPairs[2]));         
          //fine calcoloGioniMesiAnni  A.S. gennaio2015        
          
          int numGiorniTotale=0;
		  //inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
	      if (lMis.getCodTipoMisura().equalsIgnoreCase("CL") && lMis.getFlagModificaManuale()==null){
	      	int giorni = cm.getNumGiorni();
	    	int mesi = cm.getNumMesi();
	    	int anni = cm.getNumAnni();
	    	
	    	int numGiorni = anni*360 + mesi*30 + giorni;
	    	numGiorniTotale = numGiorni; 
	    	//numGiorni = numGiorni/3;
	    	
	    	BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
	      	BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
	    	BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
	    	BigDecimal numDiff = new BigDecimal(0);
	    	BigDecimal uno = new BigDecimal(1); 	 
	    	numGiorniAnniMesiGiorniDivTre=numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1, RoundingMode.HALF_UP);   	
	    	//int num = tot.intValue();
	    	BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue()).negate();
	    	//numDiff = numGiorniAnniMesiGiorniDivTre;
	    	numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);
	    	//Se il decimale è >= 6 allora arrotondamento per eccesso.
	    	//Se il decimale è <= 5 allora arrotondamento per difetto.
	    	BigDecimal zero5 = new BigDecimal("0.5");
	    	if(numDiff.compareTo(zero5)==0 || numDiff.compareTo(zero5)==-1){
	    		//Se il decimale è <= 5 allora arrotondamento per difetto.
	    		numGiorniAnniMesiGiorniDivTre = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue());
	    		//numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
	    	} else {
	    		numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
	    	}
	    	
	    	numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
	    	mesi=0;
	    	anni=0;
		    //I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM	   
	    	if (numGiorni > 30)
	        {
	          int tmp = numGiorni/30 ;
	          mesi+=tmp;
	          numGiorni-=tmp*30;
	        }       
	        if (numGiorni==30)
	        {
	          mesi++;
	          numGiorni=0;
	        }        
	        if (mesi > 12)
	        {
	          int tmp=mesi/12;
	          anni+=tmp;
	          mesi-=tmp*12;
	        }
	        
	        if (mesi==12)
	        {
	          anni++;
	          mesi=0;
	        }
	        
	        /* if (mesi<12)
	        {
	          anni=0;
	        } */
	        cm.setNumAnni(anni);
			cm.setNumMesi(mesi);
			cm.setNumGiorni(numGiorni);   
	       } else {
	    	    cm.setNumAnni(lMis.getNumAnni());
				cm.setNumMesi(lMis.getNumMesi());
				cm.setNumGiorni(lMis.getNumGiorni()); 
				if(lMis.getGiorni()!=null)
					numGiorniTotale = Integer.parseInt(lMis.getGiorni().toString()); 
				else 
					numGiorniTotale = Integer.parseInt("0"); 
	       }
	      //fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
          
          //inserimento dei periodi per il calcolo totale custodia cautelare Anni Mesi Giorni 
          //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    	  //"Cessata al momento del passaggio in giudicato computabili"
          if (lMis.getGiorni()==null && lMis.getFlagModificaManuale()==null && lMis.getFlagComputabile().equals("S")){  
        	  if(lMis.getCodOperatoreInserimento()==null || (lMis.getCodOperatoreInserimento()!=null && lMis.getCodOperatoreInserimento().length()>2 
        			  && !lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES"))) {
        		  vectorPeriodi.add(lMis.getDataInizio());
	              vectorPeriodi.add(lMis.getDataFine());
        	  }  
          }       
          
          Iterator itxModManuale = misureCautelari.iterator();
          //while ( itxModManuale.hasNext())
          //{  
        	//MisuraCautelareModel lMisModManuale = (MisuraCautelareModel)itxModManuale.next();
          	//if (lMisModManuale.getFlagModificaManuale()!=null && lMisModManuale.getFlagModificaManuale().equals("S")){  
          	if (lMis.getGiorni()!=null && lMis.getFlagComputabile().equals("S")){
              	sommaColonnaAnniModManuale = sommaColonnaAnniModManuale + Integer.parseInt(lMis.getNumAnni().toString());
          		sommaColonnaMesiModManuale = sommaColonnaMesiModManuale + Integer.parseInt(lMis.getNumMesi().toString());
          		sommaColonnaGiorniModManuale = sommaColonnaGiorniModManuale + Integer.parseInt(lMis.getNumGiorni().toString());
          	}  
          //}
          
          //quantumGiorni =" Giorni "+cm.getNumGiorni();
          //quantum="Anni " +cm.getNumAnni()+" Mesi "+cm.getNumMesi()+" Giorni "+cm.getNumGiorni();
          quantumGiorni =""+numGiorniTotale;
          quantumNumGiorniMTG =""+lMis.getNumGiorni().toString();
          quantumNumMesiMTG=""+lMis.getNumMesi().toString();
          quantumNumAnniMTG="" +lMis.getNumAnni().toString();
          quantum="";
          
          if (lMis.getFlagComputabile().equals("S"))
          {
            //somma giorni/mesi/anni solo per le Misure cautelari migrate dal sistema RES
        	if(lMis.getCodOperatoreInserimento()!=null && lMis.getCodOperatoreInserimento().length()>2 
        			  && lMis.getCodOperatoreInserimento().substring(0, 3).equalsIgnoreCase("RES")) {
        		  ctot=cu.sommaGiorni(ctot,cm);
        	}        	
            if (lMis.getCodTipoMisura().equalsIgnoreCase("CL")){
            	 classFont="CViola";
            	 classFontSecondarioTotG="CViola";
            	 if (lMis.getFlagModificaManuale()!=null && lMis.getFlagModificaManuale().equalsIgnoreCase("S")){
            		 classFontSecondario="cRosso";
            		 classFontSecondarioTotG="cRosso";
            	 } else {
                 	classFontSecondario="CVerde"; 
            	 }
            } else {
            	classFont="CVerde";
            	classFontSecondario="CVerde";
            }           
          } 
          else
          {
            quantum="NON COMPUTABILE";
            classFont="C";
            classFontSecondario="C";
            classFontSecondarioTotG="C";
          }
        } 
        else
        { 
          if (lMis.getFlagComputabile().equals("S")){
          	if (lMis.getCodTipoMisura().equalsIgnoreCase("CL")){
            	classFont="CViola";
            	if (lMis.getFlagModificaManuale()!=null && lMis.getFlagModificaManuale().equalsIgnoreCase("S")){
                	classFontSecondario="cRosso";
                	classFontSecondarioTotG="cRosso";
            	} else {
                	classFontSecondario="CVerde";
                	classFontSecondarioTotG ="CViola";
            	}
            } else {
            	classFont="CVerde";
            	classFontSecondario="CVerde";
            	classFontSecondarioTotG ="CViola";
            }           
          }           
          quantum="-";
        }

%>
    <tr>
      <td class=<%=classFont%> width="500px"><%=TipoMisura%></td>
      <td nowrap="nowrap" class=<%=classFont%> width="85px"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataInizio(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
      <td nowrap="nowrap" class=<%=classFont%> width="85px"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataFine(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
<% 

//String modificabileGiorniRisultatoConversione = "";
if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
{
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010
    if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("S"))  
        {modificabileGiorniRisultatoConversione = "NO";}
        else
        {modificabileGiorniRisultatoConversione = "SI";}        
} 
else
   {modificabileGiorniRisultatoConversione = "NO";}




if (!(lMis.getMisCautContinuativa()!=null && lMis.getMisCautContinuativa().equals("S"))){  
	  if (!lMis.getFlagComputabile().equals("S")){ %>		    
	     <td class=<%=classFont%> colspan="2">       
	        <%=quantum%>       	
		  </td>	
	<%} else { 
	      if (lMis.getCodTipoMisura().equalsIgnoreCase("CL")){ 
	          esisteCL="dimensioneTabellaDue";%> 
			  <td align="right" class=<%=classFontSecondarioTotG%> id="quantumNumG<%=indiceTipoMisura%>" width="300px">       
		        <label>Giorni </label>
		      	<input class=<%=classFontSecondarioTotG%> style=" color:<%=classFontSecondario%>; background-color: #EFEFEF; border: 0px;"  readonly title="Ufficio" size="2" type="text" value="<%=quantumGiorni%>" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI_TOT+indiceTipoMisura%>" id="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI_TOT+indiceTipoMisura%>">       	 
		      <%if (modificabileGiorniRisultatoConversione.equalsIgnoreCase("SI")){ %> 
		      		<td align="center" class=<%=classFontSecondarioTotG%> >  
			      		<a href="Javascript:ViewModificaTotaleGiorni('elenco','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI_TOT+indiceTipoMisura%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI+indiceTipoMisura+1%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI+indiceTipoMisura+2%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI+indiceTipoMisura+3%>','<%= indiceTipoMisura%>','<%=lMis.getIdMisuraCautelare()%>');">
			          	<img align="middle" src="/images/modificaTotaleGiorni.gif" alt="Modifica totale giorni">
			        	</a>
			         </td>  
	          <%} else { %> 
	          		 <td class=<%=classFont%> width="50px" colspan="1" align="center">&nbsp;
        				<%=StringUtils.toStringJSP("  ")%>&nbsp;
	      			 </td>
	          <%} %> 
			  </td>
	   	  <%} else { %> 
	   	  	<td class=<%=classFont%> width="50px" colspan="1" align="center">&nbsp;
        		<%=StringUtils.toStringJSP("  ")%>&nbsp;
	      	</td>
	      	<td class=<%=classFont%> width="50px" colspan="1" align="center">&nbsp;
	        	<%=StringUtils.toStringJSP("  ")%>&nbsp;
	      	</td>
	   	  <%} %> 
	
	<% if (lMis.getCodTipoMisura().equalsIgnoreCase("CL")){ %>
	    <td align="right" class=<%=classFontSecondario%> id="quantumNumGMA<%=indiceTipoMisura%>" width="10px">   
	<%} else {%>  
		<td align="right" class=<%=classFontSecondario%> colspan="1" id="quantumNumGMA<%=indiceTipoMisura%>" width="10px"> 
	<%} %>      
			  
			    <table style="border: 0;" cellspacing="0" cellpadding="400"  > 
			      <tr style="width: 20px;" align="right">
			    	<td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">
			        	<label>Anni</label>
			        </td>	
			        <td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">		        
				      	<input  class=<%=classFontSecondario%> width="px" style="background-color: #EFEFEF; text-align:left; border: 0px;" readonly title="Ufficio" size="2" type="text"  value="<%=quantumNumAnniMTG%>" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI+indiceTipoMisura+3%>" id="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI+indiceTipoMisura+3%>">       	 
			        </td>
		        	<td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">  		      	
				      	<label>Mesi</label>
				    </td>	
			        <td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">	
				      	<input class=<%=classFontSecondario%> style="background-color: #EFEFEF; text-align:left; border: 0px;" readonly title="Ufficio" size="2" type="text" value="<%=quantumNumMesiMTG%>" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI+indiceTipoMisura+2%>" id="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI+indiceTipoMisura+2%>">       	 
			        </td>
		        	<td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">
						<label>Giorni</label>
				    </td>	
			        <td align="right" style="border: 0px;" class=<%=classFontSecondario%> width="0px">	
				      	<input  class=<%=classFontSecondario%> style="background-color: #EFEFEF; text-align:left; border: 0px;" readonly title="Ufficio" size="2" type="text"  value="<%=quantumNumGiorniMTG%>" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI+indiceTipoMisura+1%>" id="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI+indiceTipoMisura+1%>">       	 
			        </td> 
			      </tr>
	        	</table>
	        	
	        	<input type="hidden" value="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI+indiceTipoMisura+1%>" id="campoHiddenGiorniRisConv"></input>
	        	<input type="hidden" value="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI+indiceTipoMisura+2%>" id="campoHiddenMesiRisConv"></input>
	        	<input type="hidden" value="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI+indiceTipoMisura+3%>" id="campoHiddenAnniRisConv"></input>
	<% if (modificabileGiorniRisultatoConversione.equalsIgnoreCase("SI")){
	   		if (lMis.getCodTipoMisura().equalsIgnoreCase("CL")){ %>      
	   			<td align="right" class=<%=classFontSecondario%>  width="100px" colspan="1">	  
			      	<a href="Javascript:ViewModificaRisultatoConversione('elenco','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI_TOT+indiceTipoMisura%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI+indiceTipoMisura+1%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI+indiceTipoMisura+2%>','<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI+indiceTipoMisura+3%>','<%= indiceTipoMisura%>','<%=lMis.getIdMisuraCautelare()%>');">
				          <img align="middle" src="/images/modificaRisultatoConversione.gif" alt="Modifica risultato conversione" >
				    </a>
				</td>
	<%		} else { %> 
				<td class=<%=classFont%> width="100px" colspan="1" align="center">&nbsp;
        			<%=StringUtils.toStringJSP("  ")%>&nbsp;
	      		</td>
		<%  } 
	   } else { %> 
	   		<td class=<%=classFont%> width="100px" colspan="1" align="center">&nbsp;
        			<%=StringUtils.toStringJSP("  ")%>&nbsp;
	      	</td>
	   <%} %>    	
		   </td>
	<%}
 } else if(lMis.getFlagUltimaMisCauCommutabile()!=null &&  lMis.getFlagUltimaMisCauCommutabile().equalsIgnoreCase("S")){%> 
		<td class=<%=classFont%> width="587px" colspan="4" align="center">&nbsp;
        	<%="Anni "+lMis.getNumTotAnniUltimaMisCauCom()+" Mesi "+lMis.getNumTotMesiUltimaMisCauCom()+" Giorni "+lMis.getNumTotGiorniUltimaMisCauCom()%>&nbsp;
      	</td>
<%} else {%> 
		<td class=<%=classFont%> width="587px" colspan="4" align="center">&nbsp;
        	<%=StringUtils.toStringJSP(" - ")%>&nbsp;
      	</td>
<%} %> 
      <%
      if(lMis.getAutoritaCompetente()!=null &&  !lMis.getAutoritaCompetente().equalsIgnoreCase("") &&  !lMis.getAutoritaCompetente().equalsIgnoreCase("-"))
      {%>
      <td class=<%=classFont%> width="170px">&nbsp;
        <%=StringUtils.toStringJSP(lMis.getAutoritaCompetenteDesc(),"-") +" - "+StringUtils.toStringJSP(lMis.getAutoritaCompetenteSedeDesc(),"-")%>&nbsp;
      </td>
      <%} else if( lMis.getIstDetIdIstitutoDetenzione()!=null &&  !lMis.getIstDetIdIstitutoDetenzione().equalsIgnoreCase("")){%>
      	<td class=<%=classFont%> width="170px"><%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto(),"-") +" - "+StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune(),"-")%></td>     
      <%} else { classFont="cRosso";%>
      	<td class=<%=classFont%> width="170px" align="center">&nbsp;
        	<%=StringUtils.toStringJSP("- - -")%>&nbsp;
      	</td>
      <%}%>
      
      <td class=C>
       <% String modificabile = "";
           if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
           	{
	           // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
	           // paolo cherubini lunedi 11/10/2010
	           if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("S"))  
	               {modificabile = "NO";}
	               else
	               {modificabile = "SI";}        
              } 
           else
              {modificabile = "NO";}
           

       %>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraCautelare()%>"/>
           <jsp:param name="Modificabile" value="<%=modificabile%>" />
         </jsp:include>
      </td>
    </tr>
<%
    }
    ctot=cu.ricalcolaGAM(ctot);
    
/*     //inizio calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    //"Cessata al momento del passaggio in giudicato computabili"
    int contaGiorniDaSottrarre=0;  
    if (vectorPeriodi != null && vectorPeriodi.size() != 0) {
    	for (int i=0;i<vectorPeriodi.size();i=i+2){   		
    		Date dataInizio = (Date) vectorPeriodi.get(i);
    		Date dataFine = (Date) vectorPeriodi.get(i+1);
    		
    		for (int k=0;k<vectorPeriodi.size();k=k+2){    			
    			Date dataInizioEsaminata = (Date) vectorPeriodi.get(k);
        		Date dataFineEsaminata = (Date) vectorPeriodi.get(k+1);
        		//isGreater(aDateA>aDateB)=true se aDateA>aDateB
        		//isEquals=rue se le date sono uguali
			    if(DateUtils.isEquals(dataInizioEsaminata,dataFine) && DateUtils.isGreater(dataFineEsaminata,dataFine)){ 
			    	//vectorPeriodi[i+1]=dataEsaminataInizio && vectorPeriodi[i+1]<dataEsaminataFine
			    	//1/11/2014-15/11/2014 ---------- 15/11/2014-20/11/2014
        			//vectorPeriodi[i]=1/11/2014
        			//vectorPeriodi[i+1]=15/11/2014 
        			//dataEsaminataInizio=15/11/2014 
        			//dataEsaminataFine=20/11/2014
        			contaGiorniDaSottrarre=contaGiorniDaSottrarre+1;		
        		} 			    
    		}
    		
    	}
    }

    int giorniConSovrapposizioneDate=ctot.getNumGiorni();
    if(giorniConSovrapposizioneDate>=contaGiorniDaSottrarre){
        int giorniNoSovrapposizioneDate=giorniConSovrapposizioneDate-contaGiorniDaSottrarre;
        ctot.setNumGiorni(giorniNoSovrapposizioneDate);
    }
    //fine calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    //"Cessata al momento del passaggio in giudicato computabili" */
    
    //inizio calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    //"Cessata al momento del passaggio in giudicato computabili"  
    Date DItempdata;       
    Date DFtempdata;
    String DItemp=""; String DFtemp="";
    int sommaColonnaAnni = 0;
	int sommaColonnaMesi = 0;
	int sommaColonnaGiorni = 0;
    if (vectorPeriodi != null && vectorPeriodi.size() != 0) {    	    
		for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   	
			// trova l'elemento minimo
			int jMin=i;
			for (int j=i+2;j<vectorPeriodi.size();j=j+1){ 
				if(DateUtils.isLower((Date)vectorPeriodi.get(j),(Date)vectorPeriodi.get(jMin)))// vectorPeriodi[j]<vectorPeriodi[jMin]
					jMin=j;
			}
			//scambia gli elementi con indice i e jMin
			if(i != jMin){
				//scambia
				DItemp = (String)vectorPeriodi.get(jMin);
				DFtemp = (String)vectorPeriodi.get(jMin+1);
				vectorPeriodi.set(jMin, vectorPeriodi.get(i));
				vectorPeriodi.set(jMin+1,vectorPeriodi.get(i+1));
				vectorPeriodi.set(i,DItemp);
				vectorPeriodi.set(i+1,DFtemp);
			}
		}   
    }	
    
    // popolo una tabella di sei colonne e n righe quanti sono i periodi
 	// nomi colonne: Data-inizio | Data-finale | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
 	// nella cella Periodi-continuativi che hanno uguale numero sono continuativi 
 	// ==> si deve fare la somma dei giorni mesi anni
 	int nRighe = vectorPeriodi.size()/2;
 	int nColonne = 7;	
 	String[][] matricePeriodi = new String[nRighe][nColonne];
 	//popolo  Data-inizio | Data-finale |
 	int r=0; int c=0;	
 	for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   
 		matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
 		r++;
 	}
 	r=0; c=1;
 	for (int i=1;i<=vectorPeriodi.size()-1;i=i+2){   
 		matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
 		r++;
 	}

 	// popolo le colonne | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
 	r=0;
 	int numPeriodiContinuativi = 0;
    for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   	
    	DItempdata = (Date)vectorPeriodi.get(i);
    	String ggInizio = DateUtils.getDayToString   (DItempdata).toString();
        String mmInizio   = DateUtils.getMonthToString (DItempdata).toString();
        String aaInizio   = DateUtils.getYearToString  (DItempdata).toString();
        
        DFtempdata = (Date)vectorPeriodi.get(i+1);
    	String ggFine = DateUtils.getDayToString   (DFtempdata).toString();
    	String mmFine   = DateUtils.getMonthToString (DFtempdata).toString();
    	String aaFine   = DateUtils.getYearToString  (DFtempdata).toString();
    	
        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
        String sep = "~#";
        String[] aPairs = new String[3];
        aPairs = calcoloGioniMesiAnni.split(sep);
        String numAnni = aPairs[0];
        String numMesi = aPairs[1];
        String numGiorni = aPairs[2];
        
        boolean periodiContinuativi = false;
        //controllo i periodi se sono continuativi(sovrapposti)
        if(i==0){
        	periodiContinuativi=true;//non cambia il numero del periodo
        } else {
        	Date DIprimoPeriodo = (Date)vectorPeriodi.get(i-1);
        	Date DFsecondoPeriodo = (Date)vectorPeriodi.get(i);
        	if(DateUtils.isEquals(DIprimoPeriodo, DFsecondoPeriodo) ||
        	   (DateUtils.isLower(DIprimoPeriodo, DFsecondoPeriodo) && DateUtils.getIntervallo(DIprimoPeriodo, DFsecondoPeriodo)==1)){
        		periodiContinuativi=true;//non cambia il numero del periodo
        	} else {
            	periodiContinuativi=false;// cambia il numero del periodo
            }            
        } 
        
        int cAnni=2; int cMesi=3; int cGiorni=4; int cPeriodiContinuativi=5; int cNumeroPeriodo=6;  
   		matricePeriodi[r][cAnni] = numAnni;
   		matricePeriodi[r][cMesi] = numMesi;
   		matricePeriodi[r][cGiorni] = numGiorni;
   		if(periodiContinuativi) {
   			//Periodi sono Continuativi
   			matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
   		}    			
   		else {
   			numPeriodiContinuativi = numPeriodiContinuativi + 1;
   			matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
   			periodiContinuativi = false;
   		}
   		matricePeriodi[r][cNumeroPeriodo] = Integer.toString(i);//uguale al numero del periodo: primo periodo "0" secondo "2" terzo "4"
   		r++;
    }
       
    //somma anni mesi giorni per periodi consecutivi
    String[][] matricePeriodiConsecutivi = new String[nRighe][nColonne];  
    boolean matricePeriodiConsecutiviEsiste=false;//non esiste
    Vector vectorPeriodiTemp = new Vector();
	boolean periodiContinuativi = false;
	int nRigPC=0;
	int nColPC=0;     
	
	String ggInizio="";
    String mmInizio="";
    String aaInizio="";		        
	String ggFine="";
	String mmFine="";
	String aaFine="";
	int numeroPeriodo=0;
	Date data = new Date();
	DItempdata = new Date();
	DFtempdata = new Date();
	for (int i=0;i<nRighe;i++){  
		 if (i==0) {
				periodiContinuativi = false;
		} else {
			if(matricePeriodi[i-1][5].equalsIgnoreCase(matricePeriodi[i][5])){
				periodiContinuativi = true;
			} else {
				periodiContinuativi = false;
	    		nRigPC=nRigPC+1;
			}	
		}
		
   		if (periodiContinuativi) {
   			// SI periodi Consecutivi
	    	ggInizio = DateUtils.getDayToString   (DItempdata);
	        mmInizio   = DateUtils.getMonthToString (DItempdata);
	        aaInizio   = DateUtils.getYearToString  (DItempdata);
	        for (int k=0;k<vectorPeriodi.size();k=k+1){   	
	    		data = (Date)vectorPeriodi.get(k);
	    	    String ggVectorPeriodi = DateUtils.getDayToString   (data);
	            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
	            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
	            String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
	            String mmMatricePeriodi   = matricePeriodi[i][1].substring(5, 7);
	            String aaMatricePeriodi   = matricePeriodi[i][1].substring(0, 4);		     
	    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
	    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
	    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
	    			DFtempdata = data;	
	    			break;
	    		}
	    			
	    	}
	       
	    	ggFine = DateUtils.getDayToString   (DFtempdata);
	    	mmFine   = DateUtils.getMonthToString (DFtempdata);
	    	aaFine   = DateUtils.getYearToString  (DFtempdata);

	        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
	        String sep = "~#";
	        String[] aPairs = new String[3];
	        aPairs = calcoloGioniMesiAnni.split(sep);
	        String numAnni = aPairs[0];
	        String numMesi = aPairs[1];
	        String numGiorni = aPairs[2];  

	        matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][2] = numAnni;
	        matricePeriodiConsecutivi[nRigPC][3] = numMesi;
	        matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
	        matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5];
	        matricePeriodiConsecutiviEsiste=true;
   		} else {
   			//NO periodi Consecutivi  				 			   		    
	    	if (i==0) {
	    		DItempdata = (Date)vectorPeriodi.get(i);
	    	} else {
	    		for (int k=0;k<vectorPeriodi.size()-1;k=k+1){   	
		    		data = (Date)vectorPeriodi.get(k);
                    
		            String ggVectorPeriodi = DateUtils.getDayToString   (data);
		            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
		            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
		            String ggMatricePeriodi =  matricePeriodi[i][0].substring(8, 10);
		            String mmMatricePeriodi   =  matricePeriodi[i][0].substring(5, 7);
		            String aaMatricePeriodi   =  matricePeriodi[i][0].substring(0, 4);		                     
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
		    			DItempdata = data;	
		    			break;
		    		}	    			
		    	}	
	    	}
	    	ggInizio = DateUtils.getDayToString   (DItempdata);
	        mmInizio   = DateUtils.getMonthToString (DItempdata);
	        aaInizio   = DateUtils.getYearToString  (DItempdata); 
	       
		    if (i==0){ 		
		    	DFtempdata = (Date)vectorPeriodi.get(i+1);
		    } else { 
		    	for (int k=0;k<vectorPeriodi.size();k=k+1){   	
		    		data = (Date)vectorPeriodi.get(k);
		    		String ggVectorPeriodi = DateUtils.getDayToString   (data);
			        String mmVectorPeriodi   = DateUtils.getMonthToString (data);
			        String aaVectorPeriodi   = DateUtils.getYearToString  (data);			        
		            String ggMatricePeriodi =  matricePeriodi[i][1].substring(8, 10);
		            String mmMatricePeriodi   =  matricePeriodi[i][1].substring(5, 7);
		            String aaMatricePeriodi   =  matricePeriodi[i][1].substring(0, 4);        
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
		    			DFtempdata = data;
		    			break;
		    		}
		    				
		    	}		        
		    }
	    	ggFine = DateUtils.getDayToString   (DFtempdata);
	    	mmFine   = DateUtils.getMonthToString (DFtempdata);
	    	aaFine   = DateUtils.getYearToString  (DFtempdata);  		    		
  		
   		    
	        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
	        String sep = "~#";
	        String[] aPairs = new String[3];
	        aPairs = calcoloGioniMesiAnni.split(sep);
	        String numAnni = aPairs[0];
	        String numMesi = aPairs[1];
	        String numGiorni = aPairs[2];
	        matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][2] = numAnni;
	        matricePeriodiConsecutivi[nRigPC][3] = numMesi;
	        matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
	        matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5].toString();
	        matricePeriodiConsecutiviEsiste=true;
   		}
   	}		
 	
	//somma colonna | Anni | Mesi | Giorni |
	//int sommaColonnaAnni = 0;
	//int sommaColonnaMesi = 0;
	//int sommaColonnaGiorni = 0;
	if(misureCautelari.size()>0){
		if(matricePeriodiConsecutiviEsiste) { //ci sono periodi consecutivi ==> in ogni riga della tabella è stato inserito un periodo consecutivo
			for (int i=0;i<=nRigPC;i++){ 
				if (matricePeriodiConsecutivi[i][2]!=null)
					sommaColonnaAnni = sommaColonnaAnni + Integer.parseInt(matricePeriodiConsecutivi[i][2]);
				if (matricePeriodiConsecutivi[i][3]!=null)
					sommaColonnaMesi = sommaColonnaMesi + Integer.parseInt(matricePeriodiConsecutivi[i][3]);
				if (matricePeriodiConsecutivi[i][4]!=null)
					sommaColonnaGiorni = sommaColonnaGiorni + Integer.parseInt(matricePeriodiConsecutivi[i][4]);   		
			}
		}		
	    
		sommaColonnaAnni = sommaColonnaAnni + sommaColonnaAnniModManuale;
		sommaColonnaMesi = sommaColonnaMesi + sommaColonnaMesiModManuale;
		sommaColonnaGiorni = sommaColonnaGiorni + sommaColonnaGiorniModManuale;
		
		int giorni=sommaColonnaGiorni;
		while (giorni>0){
			giorni = giorni-30;
			if(giorni>=0){
				sommaColonnaMesi = sommaColonnaMesi+1;
				sommaColonnaGiorni = giorni;
			}			
		}
		int mesi=sommaColonnaMesi;
		while (mesi>0){
			mesi = mesi-12;
			if(mesi>=0){
				sommaColonnaAnni = sommaColonnaAnni+1;
				sommaColonnaMesi = mesi;
			}			
		}
		int anni=sommaColonnaAnni;
	
	}
		
	int sommaColonnaAnniMisureCautelariMigrateRES = 0;
	int sommaColonnaMesiMisureCautelariMigrateRES = 0;
	int sommaColonnaGiorniMisureCautelariMigrateRES = 0;
	sommaColonnaAnniMisureCautelariMigrateRES = ctot.getNumAnni();
	sommaColonnaMesiMisureCautelariMigrateRES = ctot.getNumMesi();
	sommaColonnaGiorniMisureCautelariMigrateRES = ctot.getNumGiorni();
	int sommaColonnaAnniMisureCautelariMigrateRES_SIEP = sommaColonnaAnniMisureCautelariMigrateRES +sommaColonnaAnni;
	int sommaColonnaMesiMisureCautelariMigrateRES_SIEP = sommaColonnaMesiMisureCautelariMigrateRES +sommaColonnaMesi;
	int sommaColonnaGiorniMisureCautelariMigrateRES_SIEP = sommaColonnaGiorniMisureCautelariMigrateRES + sommaColonnaGiorni;
	//ctot.setNumAnni(sommaColonnaAnni);
	//ctot.setNumMesi(sommaColonnaMesi);
    //ctot.setNumGiorni(sommaColonnaGiorni);
    
    ctot.setNumAnni(sommaColonnaAnniMisureCautelariMigrateRES_SIEP);
	ctot.setNumMesi(sommaColonnaMesiMisureCautelariMigrateRES_SIEP);
    ctot.setNumGiorni(sommaColonnaGiorniMisureCautelariMigrateRES_SIEP);
    
    //fine calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    
    //inizio seconda lista con date null
      String dimTd1="110";
      String dimTd2="85";
      String dimTd3="451";
      String dimTd4="169";
    TipoMisura=new String();
    classFont="C"; 
    itx = misureCautelariDateNull.iterator();
    indiceTipoMisura=0;
    while ( itx.hasNext())
    { indiceTipoMisura=indiceTipoMisura+1;
      MisuraCautelareModel lMis = (MisuraCautelareModel)itx.next();
      TipoMisura=StringUtils.toStringJSP(lMis.getDescrTipoMisura());
		if (TipoMisura.equalsIgnoreCase("Carcere"))
        TipoMisura="CUSTODIA IN CARCERE";
        classFont="CVerde";
        classFontSecondario="CVerde";       
        quantum="-";   
        %>
        <tr>
        <td class=<%=classFont%>  width="300px"><%=TipoMisura%></td>
        <td nowrap="nowrap" class=<%=classFont%> width=<%=dimTd2%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataInizio(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
        <td nowrap="nowrap" class=<%=classFont%> width=<%=dimTd2%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataFine(),"dd-MM-yyyy"),"-")%>&nbsp;</td>
  		    
  	  <%if (modificabileGiorniRisultatoConversione.equalsIgnoreCase("SI")){	%>  
  	      <td class=<%=classFont%> colspan="4" width="620px">       
  	      	<%=quantum%>        	
  	      </td>	
  	  <%} else {%> 
  		  <td class=<%=classFont%> colspan="4" width="587px">       
  	      	<%=quantum%>        	
  	      </td>
   	  <%}%>

        <%
        if(lMis.getAutoritaCompetente()!=null &&  !lMis.getAutoritaCompetente().equalsIgnoreCase("") &&  !lMis.getAutoritaCompetente().equalsIgnoreCase("-"))
        {%>
        <td class=<%=classFont%> width=<%=dimTd4%>>&nbsp;
          <%=StringUtils.toStringJSP(lMis.getAutoritaCompetenteDesc(),"-") +" - "+StringUtils.toStringJSP(lMis.getAutoritaCompetenteSedeDesc(),"-")%>&nbsp;
        </td>
        <%} else if( lMis.getIstDetIdIstitutoDetenzione()!=null &&  !lMis.getIstDetIdIstitutoDetenzione().equalsIgnoreCase("")){%>
        	<td class=<%=classFont%> width=<%=dimTd4%>><%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto(),"-") +" - "+StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune(),"-")%></td>     
        <%} else { %>
        	<td class=<%=classFont%> width="98px" align="center">&nbsp;
          	<%=StringUtils.toStringJSP("- - -")%>&nbsp;
        	</td>
        <%}%>
        
        <td class=C>
         <% String modificabile = "";
             if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
             {
  	           // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
  	           // paolo cherubini lunedi 11/10/2010
  	           if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("S"))  
  	               {modificabile = "NO";}
  	               else
  	               {modificabile = "SI";}        
             } 
             else
                {modificabile = "NO";}
             

         %>
          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>"/>
             <jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraCautelare()%>"/>
             <jsp:param name="Modificabile" value="<%=modificabile%>" />
           </jsp:include>
        </td>
      </tr>
    <%}
    //fini seconda lista con date null
%>
</table>



<br><br>
  <table>
    <tr>
    	<td class=L width="140px">Totale Custodia Cautelare Sofferta:</td>
      	<td class=L width="330px"> 
       				Anni <font class=campo> <input style="background-color: #EFEFEF; border: 0px;" readonly title="Ufficio" size="8" type="text" value="<%=ctot.getNumAnni()%>" id="totCustodiaCautelareSoffertaAnni"></font> 
       				Mesi <font class=campo> <input style="background-color: #EFEFEF; border: 0px;" readonly title="Ufficio" size="8" type="text" value="<%=ctot.getNumMesi()%>" id="totCustodiaCautelareSoffertaMesi"></font> 
       				Giorni <font class=campo> <input style="background-color: #EFEFEF; border: 0px;" readonly title="Ufficio" size="8" type="text" value="<%=ctot.getNumGiorni()%>" id="totCustodiaCautelareSoffertaGiorni"></font>
      	</td>
      	<% if (PosizioneGiuridica != null && Utils.isPresent(PosizioneGiuridica.getCodPosizioneGiuridica())
        		&& !PosizioneGiuridica.getCodPosizioneGiuridica().equals("07") 
              	&& !PosizioneGiuridica.getCodPosizioneGiuridica().equals("10"))
        {%>
      		<td class=L>Posizione Giuridica :</td>
      		<td class=L>
        		<font class=campo><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font>
         		dal
        		<font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PosizioneGiuridica.getDataInizio(),"dd-MM-yyyy"))%></font> 
      		</td>
      	<%}%>
    </tr>
    <%=LuogoDetenzione.getDescrTipoIstituto()%>

    <% if (!LuogoDetenzione.getDescrTipoIstituto().equals("")) {%>
      <tr>
         <td class=L>Istituto di Detenzione :</td>
         <td class=L>
            <font class=campo><%=LuogoDetenzione.getDescrTipoIstituto()%></font>
            di <font class=campo><%=LuogoDetenzione.getDescrLuogo()%></font> 
         </td>
      </tr>
    <%}    %>
   
     </table>
    
     <%
    // richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena 
    // paolo cherubini lunedi 11/10/2010

     if (lPenaResMod!=null  && lPenaResMod.getFlagValidato().equals("N"))  {%>
    <FORM name="calcolopena">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadCalcoloPena">
	<table>
	<tr>
		<td class="lRosso">
			<font class="lRosso">
				Attenzione! Primo calcolo della pena già effettuato. Per computare le misure modificate,
				è necessario procedere nuovamente con il calcolo della pena
			</font>
		</td>
	</tr>
	 <tr>
       <td>
        <INPUT  class="bottone" type="submit" name="CALCOLA" value="Calcola Fine Pena" onClick="">
       </td>
     </tr>
	</table>
	</FORM>
 <%   }
    // fine a9/rr/075 
    
 %>
    
    

</FORM>

<%
  if (ComingFromInsert !=null && ComingFromInsert.equals("YES"))
  {
%>
    <table>
      <tr>
        <td class="LGB">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuracautelare.action.ActLoadInserisciMisuraCautelare">Inserimento ulteriori Misure Cautelari</a>
        </td>
      </tr>
    </table>
<%
  }
%>
</body>
</html>