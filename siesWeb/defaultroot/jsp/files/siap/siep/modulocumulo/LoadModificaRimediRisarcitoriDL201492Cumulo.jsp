<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

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
<jsp:useBean id="TipoLicenza" 			scope="request" class="java.lang.String"/>

<!-- 	LoadModificaRimediRisarcitoriDL201492Cumulo	 -->
<%
//==============================================================================
// form per la modifica del Provvedimento di Concessione/Reclamo Rimedi Risarcitori
// DL92/2014 relativamente al Titolo_Cumulato
//
// La form consente di inserire manualmente i dati simulando il provvedimento
// SIUS
//
//==============================================================================
// Dati per la costruzione delle DIV
// Sono presenti 2 DIV con i periodi DAL - AL con 10 periodi per tipologia
// DIV_RD_C = Giorni di riduzione concessi
// DIV_SL_C = Somme Liquidate concesse

String [] lNomiDivPeriodi = {"DIV_RD_C","DIV_SL_C"};
int lNumPeriodi = 10; // numero di periodi (DAL-AL) previsti per ogni div

//
String lNumgg = "";
BigDecimal lNumeroGiorniRiduzione = null;
String lSomma_int = "";
String lSomma_dec = "";

Vector<PeriodoLibAntCumuloModel> lPeriodiRD = null;
Vector<PeriodoLibAntCumuloModel> lPeriodiSL = null;

String presenzaperiodiRD = "";
String presenzaperiodiSL = "";

// Ricerca del provvedimento da modificare
Iterator ITX = StatoEsecTitoloCum.getListaLiberazioniAnticipate().iterator();
while(ITX.hasNext())
{
  LibAnticipataCumuloModel LACumodel = (LibAnticipataCumuloModel)ITX.next();
  if( "RD".equals(LACumodel.getCodTipoLicenza()))
  {
	 lNumeroGiorniRiduzione = LACumodel.getNumeroGiorni();
	 lNumgg = LACumodel.getNumeroGiorni().toString();
		   
	 if(LACumodel.getListaPeriodiLibAnticipate()!=null && LACumodel.getListaPeriodiLibAnticipate().size() > 0)
	 {
		presenzaperiodiRD = "SI";
	    lPeriodiRD = (Vector<PeriodoLibAntCumuloModel>) LACumodel.getListaPeriodiLibAnticipate();
	 }	   
  }
  else if( "SL".equals(LACumodel.getCodTipoLicenza()))
  {
	  //lSommaLiquidata = StringUtils.toEuroFormat(LACumodel.getSommaRisarcDanni());
	  lSomma_int = StringUtils.getParteIntera(LACumodel.getSommaRisarcDanni() );
	  lSomma_dec = StringUtils.getParteDecimale(LACumodel.getSommaRisarcDanni() );
		
 	  if(LACumodel.getListaPeriodiLibAnticipate()!=null && LACumodel.getListaPeriodiLibAnticipate().size() > 0)
   	  {
 		 presenzaperiodiSL = "SI";
	   	 lPeriodiSL = (Vector<PeriodoLibAntCumuloModel>) LACumodel.getListaPeriodiLibAnticipate();
   	  }
  }
}

String lCheckRD ="";
String lCheckSL ="";

if(TipoLicenza.equals("RD"))
	lCheckRD="checked";
else if(TipoLicenza.equals("SL"))
	lCheckSL="checked";
%>

<html>

<head>
  <title> [S.I.E.S.] - Rimedi Risarcitori D.L. 92/2014 - Titolo Cumulato</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  
  	var TipodiLic = '<%=TipoLicenza%>';
  	var presenzeRD = '<%=presenzaperiodiRD%>';
  	var presenzeSL = '<%=presenzaperiodiSL%>';
  	
    var lNumPeriodi = <%=lNumPeriodi%>;
    var lNomiDivPeriodi = ["DIV_RD_C", "DIV_SL_C"];
  
    function Inizia()
    {
   		// per ora non fa nulla
   	 	//return;
   		
   	 	if(TipodiLic == "RD")
   		{		
   			ViewLayer1('DIV_RD_C');
   		}
   		else if(TipodiLic == "SL")
   		{
   			ViewLayer1('DIV_SL_C');
   		}

    }
    
    // Seleziona gli uffici in base alla tipologia
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================
    // Funzione che visualizza o nasconde le DIV con i periodi
    //==========================================================
    function ViewLayer(Object, idDiv)
    {
   	  //alert('3 - idDiv ='+idDiv);
   	  //alert('3 - Object ='+Object);
   	  
      if (Object.checked)
      {
        // ho selezionato un check, devo chiudere eventuali DIV aperte e 
        // visualizzare la DIV corrispondente al check selezionato.
	        for (i=0;i<lNomiDivPeriodi.length;i++)
	        {
	          	if (lNomiDivPeriodi[i]!=idDiv)
	          	{
	            	// Disabilito i check
	            	var idCheck = "check_"+lNomiDivPeriodi[i];
	            	document.getElementById(idCheck).disabled = true;
	          	}
	        }
	        // visualizzo la DIV 
	        document.getElementById(idDiv).style.display='block';
      }
      else 
      {
        // ho deselezionato un check, devo chiudere la DIV corrispondente e 
        // riabilitare i check
        if (chiudiDivPeriodi(idDiv)==true){
          for (i=0;i<lNomiDivPeriodi.length;i++){
            if (lNomiDivPeriodi[i]!=idDiv){
              var idCheck = "check_"+lNomiDivPeriodi[i];
              document.getElementById(idCheck).disabled = false;
            }
          }
        }
        else {
          Object.checked=true; // dati errati nella DIV rimetto il segno di spunta;
        }
      }
    }
    
    function ViewLayer1(idDiv)
    {
      //alert ("idDiv = "+idDiv);
      
   	  	if(idDiv=='DIV_RD_C')
   		{
   	  		// devo presentare i periodi di Riduzione pena
   	  	 	node=document.getElementById("DIV_RD_C");
   	    	//node.style.display='block';
   	    	node.style.display='none';
   	    	document.getElementById("check_DIV_RD_C_label").style.color="Red";
   	    	
   	    	node1=document.getElementById("DIV_SL_C");
   	    	node1.style.display='none';
   	    	//document.getElementById("check_DIV_SL_C").disabled = true;
   	    	if(presenzeSL=='SI')
   	    		document.getElementById("check_DIV_SL_C_label").style.color="Red";
   	    		
   	    	
   		}
   	  	else if(idDiv=='DIV_SL_C')
   		{
   	  		// devo presentare i periodi di Somma risarcita
   	  		node1=document.getElementById("DIV_SL_C");
   	    	//node1.style.display='block';
   	    	node1.style.display='none';
   	    	document.getElementById("check_DIV_SL_C_label").style.color="Red";
   	    	
   	  	 	node=document.getElementById("DIV_RD_C");
   	    	node.style.display='none';
   	    	//document.getElementById("check_DIV_RD_C").disabled = true;
   	    	if(presenzeRD=='SI')
   	    		document.getElementById("check_DIV_RD_C_label").style.color="Red";
   		}
    } 	  		
   	  
   	  
   	/*  
      if (Object.checked)
      {
        // ho selezionato un check, devo chiudere eventuali DIV aperte e 
        // visualizzare la DIV corrispondente al check selezionato.
	        for (i=0;i<lNomiDivPeriodi.length;i++)
	        {
	          	if (lNomiDivPeriodi[i]!=idDiv)
	          	{
	            	// Disabilito i check
	            	var idCheck = "check_"+lNomiDivPeriodi[i];
	            	document.getElementById(idCheck).disabled = true;
	          	}
	        }
	        // visualizzo la DIV 
	        document.getElementById(idDiv).style.display='block';
      }
      else 
      {
        // ho deselezionato un check, devo chiudere la DIV corrispondente e 
        // riabilitare i check
        if (chiudiDivPeriodi(idDiv)==true){
          for (i=0;i<lNomiDivPeriodi.length;i++){
            if (lNomiDivPeriodi[i]!=idDiv){
              var idCheck = "check_"+lNomiDivPeriodi[i];
              document.getElementById(idCheck).disabled = false;
            }
          }
        }
        else {
          Object.checked=true; // dati errati nella DIV rimetto il segno di spunta;
        }
      }*/
    
    //==========================================================================
    // Chiude una DIV effettuando i controlli sui dati in essa presenti
    //==========================================================================
    function chiudiDivPeriodi(idDiv){
      // prima di nascondere la DIV va verificato se i dati inseriti sono 
      // corretti. In caso negativo si deve inviare alert, non chiudere la DIV
      // e restituire false
      // Se i dati sono corretti va chiusa la DIV e modificato il colore del 
      // check corrispondente: rosso se la DIV contiene dati.
      var numPeriodiInseriti = 0;
      var lNomeCheck = "check_"+idDiv+"_label";
       
      var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
       
      for (i=0; i<lNumPeriodi; i++){
        // data dal
        var lNomeCampoGG_Dal   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Dal   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Dal = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;

        var lNomeCampoGG_Al   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Al   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Al = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>"+"_"+idDiv+"_"+i;

        if (document.getElementById(lNomeCampoGG_Dal).value.length==1)
          document.getElementById(lNomeCampoGG_Dal).value='0'+document.getElementById(lNomeCampoGG_Dal).value;
        if (document.getElementById(lNomeCampoMM_Dal).value.length==1)
          document.getElementById(lNomeCampoMM_Dal).value='0'+document.getElementById(lNomeCampoMM_Dal).value;
          
        var data_dal = document.getElementById(lNomeCampoGG_Dal).value
                  +'/'+document.getElementById(lNomeCampoMM_Dal).value
                  +'/'+document.getElementById(lNomeCampoAAAA_Dal).value;
                  
        //alert("data_dal = "+data_dal);
        if (!ControllaDataPassaVuota(data_dal))
        {
          alert('Data inizio periodo '+data_dal+' non valida');
          document.getElementById(lNomeCampoGG_Dal).focus();
          return false;
        } 
        
        if (document.getElementById(lNomeCampoGG_Al).value.length==1)
          document.getElementById(lNomeCampoGG_Al).value='0'+document.getElementById(lNomeCampoGG_Al).value;
        if (document.getElementById(lNomeCampoMM_Al).value.length==1)
          document.getElementById(lNomeCampoMM_Al).value='0'+document.getElementById(lNomeCampoMM_Al).value;
          
        var data_al = document.getElementById(lNomeCampoGG_Al).value
                 +'/'+document.getElementById(lNomeCampoMM_Al).value
                 +'/'+document.getElementById(lNomeCampoAAAA_Al).value;
                 
        if (!ControllaDataPassaVuota(data_al))
        {
          alert('Data fine periodo '+data_al+' non valida');
          document.getElementById(lNomeCampoGG_Al).focus();
          return false;
        } 
        
        if (data_dal!='//' && data_al!='//')
        {        
          if (CompareDate(data_dal,dataOdierna)== false) {
            alert ("Data inizio periodo non può essere una data futura");
            document.getElementById(lNomeCampoGG_Dal).focus();
            return false;          
          }
          if (CompareDate(data_al,dataOdierna)== false) {
            alert ("Data fine periodo non può essere una data futura");
            document.getElementById(lNomeCampoGG_Al).focus();
            return false;          
          }
        
          if (CompareDate(data_dal,data_al)== false) {
            alert ("Data di Fine minore di Data inizio periodo");
            document.getElementById(lNomeCampoGG_Al).focus();
            return false;
          }
          //else if (CompareDate(dataFine, data_emissione)== false){
          //  alert ("Data di Fine periodo maggiore di Data emissione");
          //  document.getElementById(lNomeCampoGG_Dal).focus();
          //}
          else {
            numPeriodiInseriti++;
          }
        }
        else if (data_dal!='//' && data_al=='//')
        {
          alert ("Data di fine periodo mancante");
          document.getElementById(lNomeCampoGG_Al).focus();
          return false;
        }
        else if (data_dal=='//' && data_al!='//')
        {
          alert ("Data di inizio periodo mancante");
          document.getElementById(lNomeCampoGG_Dal).focus();
          return false;
        }
      }

      if (numPeriodiInseriti>0){
        // setto rosso il check
        document.getElementById(lNomeCheck).style.color="Red";
      }
      else {
        document.getElementById(lNomeCheck).style.color="Navy";
      }
      
      // chiudo la DIV
      document.getElementById(idDiv).style.display='none';
      
      return true;
    }
    
    //==================================================
    // Verifica la presenza di dati in una DIV periodi
    //==================================================
    function checkPresenzaPeriodi(idDiv){
      for (i=0; i<lNumPeriodi; i++){
        // data dal
        var lNomeCampoGG_Dal   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Dal   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Dal = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;

        var lNomeCampoGG_Al   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Al   = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Al = "<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        
        if(   document.getElementById(lNomeCampoGG_Dal).value.length>0
           || document.getElementById(lNomeCampoMM_Dal).value.length>0
           || document.getElementById(lNomeCampoAAAA_Dal).value.length>0
           || document.getElementById(lNomeCampoGG_Al).value.length>0
           || document.getElementById(lNomeCampoMM_Al).value.length>0
           || document.getElementById(lNomeCampoAAAA_Al).value.length>0
          )
        {
          return true;
        }
      }
      
      return false;
    }
    
    //=================================
    //
    //=================================
    function Verify()
    {      
      //=======================================================
      // Controllo sui campi del Provvedimento Sorveglianza
      //=======================================================
      // Data Emissione Provvedimento      
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (data_to_verify=='//' )
      {
        alert('Indicare la Data di emissione Provvedimento');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione Provvedimento non valida');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }      
      
      // Anno e numero Provvedimento
      if ( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value.length==0 &&
      		document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value !="")
      {
        alert('Inserire Anno Provvedimento');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      if ( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value.length==0 &&
      		document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value != "")
      {
        alert('Inserire Numero Provvedimento');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.focus();
        return false;
      }   
      
      // Tipo provvedimento
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex==0)
      {        
        alert("Selezionare Tipo Provvedimento");
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
        return false;
      }  
      
      // Anno e numero SIUS
      if ( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value.length==0 &&
      		document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value !="")
      {
        alert('Inserire Anno Procedimento SIUS ');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
        return false;
      }
      
      if ( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value.length==0 && 
      		document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value != "")
      {
        alert('Inserire Numero Procedimento SIUS');
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.focus();
        return false;
      }     
      
      // Autorità emittente 
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex==0)
      {        
        alert("Selezionare Autorità Emittente");
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
        return false;
      }
      
      // Sede 
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.value=="")
      {
        alert("Selezionare Sede Autorità Emittente");
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
      }
      
   	  // Esito
      if (document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="-")
      {        
        alert("Selezionare un Esito");
        document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.focus();
        return false;
      }
      
      //====================================================
      // Controllo che sia stato specificato almeno un dato
      //====================================================
      // E' obbligatorio inserire almeno uno tra i RD_C e SL_C, non è possibile
      // inserire solo i periodi NON concessi (non ha senso per SIEP)
      // Se sono stati inseriti i GG_RD sono obbligatori i periodi e VV
      // Se sono stati inseriti i SL sono obbligatori i periodi e VV
      
      // Se ci sono DIV aperte procedo alla chiusura per attivare i controlli
      for (i=0;i<lNomiDivPeriodi.length;i++){
        var idCheck = "check_"+lNomiDivPeriodi[i];
        if (document.getElementById(idCheck).checked==true){
          //alert("E' presente una DIV aperta la chiudo");
          if (chiudiDivPeriodi(lNomiDivPeriodi[i])==false){
            //alert("Errore nei dati della DIV");
            return false;
          }
          else {
            //alert("Div chiusa tolgo il check");
            document.getElementById(idCheck).checked=false;
            document.getElementById(idCheck).onclick();
          }
        }
      }
      
      var isPeriodi_RD_C_presenti = checkPresenzaPeriodi('DIV_RD_C');
      var isPeriodi_SL_C_presenti = checkPresenzaPeriodi('DIV_SL_C');
      //var isPeriodi_RD_R_presenti = checkPresenzaPeriodi('DIV_RD_R');
      //var isPeriodi_RD_I_presenti = checkPresenzaPeriodi('DIV_RD_I');
      //var isPeriodi_RD_N_presenti = checkPresenzaPeriodi('DIV_RD_N');
      
      var totGG_RD_presenti = true;
      if (   document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI%>.value.length==0
          || parseInt(document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI%>.value)==0
         )
      {
        //alert("Non sono stati inseriti giorni");
        totGG_RD_presenti = false;
      }

      var tot_SL_presente = true;
      if (  (   document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI%>.value.length==0
             || parseInt(document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI%>.value)==0
            )
          &&(   document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI%>.value.length==0
             || parseInt(document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI%>.value)==0
            )
         )
      {
        //alert("Non è stata inserita a somma liquidata");
        tot_SL_presente = false;
      }
      
      if (!totGG_RD_presenti && !tot_SL_presente){
        alert("Attenzione non sono stati indicati ne i giorni di riduzione ne la somma liquidata a titolo risarcitorio. Almeno uno dei due dati è obbligatorio.")
        return false;
      }
      
      if (totGG_RD_presenti && !isPeriodi_RD_C_presenti){
        alert("Sono stati indicati i giorni di riduzione pena ma non i relativi periodi di computo");
        return false;
      }
      if ( !totGG_RD_presenti && isPeriodi_RD_C_presenti){
        alert("Sono stati indicati i periodi di computo dei giorni di riduzione ma non il totale di giorni concessi");
        return false;
      }
      
      if (tot_SL_presente && !isPeriodi_SL_C_presenti){
        alert("E' stata indicata la somma liquidata ma non i relativi periodi di computo");
        return false;
      }
      if (!tot_SL_presente && isPeriodi_SL_C_presenti){
        alert("Sono stati indicati i periodi di computo ma non la corrispondente somma liquidata");
        return false;
      }
      
      // Controllo Congruenza dati ESito e Giorni Riduzione/Somma Risarcimento
      if( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="0390"   // Riduzione Pene Detentiva 
     	  && 
     	  totGG_RD_presenti == false  )	 
      {	 
	 	  alert("Indicare il Totale Giorni concessi, oppure selezionare un altro tipo di Esito");
	      document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI %>.focus();
	      return false;
      }
      
      if( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="0392"	// Risarcimento danni in denaro 
      	  && 
      	  tot_SL_presente == false  )	
      {	 
 	 	  alert("Indicare Somma Liquidata, oppure selezionare un altro tipo di Esito");
 	      document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI %>.focus();
 	      return false;
      }
      
      if( document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.value=="0391"	// Riduzione Pene Detentiva e Risarcimento danni in denaro 
    	  && 
    	  ( !totGG_RD_presenti || !tot_SL_presente )
    	 )   
      {	 
	 	  alert("Indicare Totale Giorni concessi e Somma Liquidata, oppure selezionare un altro tipo di Esito");
	      document.ModRimediRis.<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI %>.focus();
	      return false;
      }
      
      return true;
    }
    
    // Torna Insietro
    function eseguiFunzione(action)
    {
      document.ModRimediRis.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.ModRimediRis.submit();
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
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Provvedimento Rimedi Risarcitori D.L. 92/2014</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaRimediRisarcitoriDL201492Cumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>
  
<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua (attuale)
//==============================================================================
// [DA FARE]
%>

<%
//==============================================================================
//                  Dati del Provvedimento dela Sorveglianza
//==============================================================================
%> 
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="ModRimediRis">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRimediRisarcitoriDL201492Cumulo">

  <input type="hidden" name="NumPeriodi" value="<%=lNumPeriodi%>">
  <input type="hidden" name="modalita" value="<%=modalita%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="<%=StatoEsecTitoloCum.getIdStatoEsecTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" value="" >
  <input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_FLAG_STATO%>" value="<%=StatoEsecTitoloCum.getFlagStato()%>">


  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati del Provvedimento </td>
    </tr>
    
    <tr>
    </tr>
    
    <tr>
      <td class="l">Data emissione provvedimento<font class="ob">(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"dd") ) %>" 
               name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"MM") ) %>"   
               name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>"   maxlength="2" size="2" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"yyyy") ) %>" 
               name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Anno / Numero Provvedimento</td>	<!--  ex  ICostantiEvento.CAMPO_ANNO_PROTOCOLLO e ICostantiEvento.CAMPO_PROGR_PROTOCOLLO-->
      <td class="l">
         <input Title="Anno Provvedimento"   value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProvvedimento(), "" ) %>" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProvvedimento(), "" ) %>" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>   
    
    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
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
        <select Title="Autorità Emittente" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>">
          <%=autoritaEmi%>
        </select>
      </td>

      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
        <input title="Sede Autorita"  type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>"  
        	value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getDescrLuogoEmittente(), "-" ) %>" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('ModRimediRis','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>',document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>[document.ModRimediRis.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    <tr>
      <td class="l" >Esito del Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
		<select Title="Esito Provvedimento" class="small" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>" >
          <%=EsitoProvvedimento%>
        </select>	
      </td>
    </tr>
  </table>
  
  <br>
  
  <%
  // DIV di contenimento per allineare le DIV dei periodi
  %>
  <div id="contenitore" style="position: relative; top: 0; left: 0;" >

  
  <table>
    <tr>
      <td class="l">Riduzione/Aumento Pena: Totale giorni concessi </td>
      <td class="l" style="text-align:right">
        <input type="text" value="<%=StringUtils.toStringJSP(lNumeroGiorniRiduzione, "") %>" style="text-align: right;" 
        	name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUM_GIORNI_RISARCITORI%>" 
        	maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">&nbsp;<span id="check_DIV_RD_C_label" style="color=navy;font-weight:bold;">in relazione ai periodi</span>&nbsp;
        <input type="checkbox" id="check_DIV_RD_C" name="check_DIV_RD_C" 
               value="1" onclick="Javascript:ViewLayer(this,'DIV_RD_C');" >
      </td>
    </tr>
    <tr>
      <td class="l">Somma liquidata a titolo risarcimento danno Euro</td>
      <td class="l">
        <input type="text" value="<%=StringUtils.toStringJSP(lSomma_int,"") %>" style="text-align: right;" name="<%=ICostantiLibAnticipataCumulo.CAMPO_INTERO_SOMMA_RISARC_DANNI%>" 
        	maxlength="5" size="5"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        ,
        <input type="text" value="<%=StringUtils.toStringJSP(lSomma_dec,"") %>" name="<%=ICostantiLibAnticipataCumulo.CAMPO_DECIMALE_SOMMA_RISARC_DANNI%>" 
        	maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">&nbsp;<span id="check_DIV_SL_C_label" style="color=navy;font-weight:bold;">in relazione ai periodi</span>&nbsp;
        <input type="checkbox" id="check_DIV_SL_C" name="check_DIV_SL_C" 
               value="1" onclick="Javascript:ViewLayer(this,'DIV_SL_C');" >
      </td>
    </tr>
  </table>
  <br>
  <table>
  <tr>
    <td class="Titolo"  colspan="6">Eventuali Note</td>
  </tr>
  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4">
     <textarea cols="80" rows="2" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(StatoEsecTitoloCum.getNote(), "" ) %></textarea>
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
  
  <%
  //============================================================================
  // Costruzione delle 2 DIV con i periodi
  //============================================================================
  %>
  <%
  String topOffSetDIV = "-165";  // -175
  String leftOffSetDIV = "630";  // 620
  
  //LogF3B.getLogger().debug("--XX-- Entro nei periodi: lNomiDivPeriodi length = "+lNomiDivPeriodi.length);
  for (int i=0; i<lNomiDivPeriodi.length; i++)
  {
	  Vector<PeriodoLibAntCumuloModel> lVecPeriodi = new Vector<PeriodoLibAntCumuloModel>();
	  
	  if(i==0)
	  {	  if(lPeriodiRD != null && lPeriodiRD.size() > 0)
		  	lVecPeriodi = lPeriodiRD;
	  }	  
	  else if(i==1)
	  {	  
		  if(lPeriodiSL != null && lPeriodiSL.size() > 0)
		  	lVecPeriodi = lPeriodiSL;
	  } 
	  
	//  if(lVecPeriodi != null && lVecPeriodi.size() > 0)
	//  	LogF3B.getLogger().debug("--XX-- ho separato le Lic - i = "+i+" - lVecPeriodi.size = "+lVecPeriodi.size());
	  
  %>
  <div id="<%=lNomiDivPeriodi[i]%>" style="position: relative; top: <%=topOffSetDIV%>; left: <%=leftOffSetDIV%>; display:none;" >
    <table>
    <%
    // COSTRUZIONE DEI CAMPI DATA DAL - AL
    for (int k=0; k<lNumPeriodi; k++)
    {	%>
      <tr>
   <% 	if (k < lVecPeriodi.size() )
  	  	{ 
  	 		PeriodoLibAntCumuloModel PeriodoMod = (PeriodoLibAntCumuloModel) lVecPeriodi.get(k);
  	 		//LogF3B.getLogger().debug("--XX-- inizio ciclo per riempire le date - i = "+i+" - k = "+k+" - PeriodoMod = "+PeriodoMod );
    %>
        <td class="l">
          Dal
          <input type="text" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataInizio(),"dd"), "")%>" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataInizio(),"MM"), "")%>" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataInizio(),"yyyy"), "")%>" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
                &nbsp;&nbsp;
          Al
          <input type="text" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataFine(),"dd"), "")%>" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataFine(),"MM"), "")%>" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(PeriodoMod.getDataFine(),"yyyy"), "")%>" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
        </td>
        
    <%	}
   		else
   		{	%>
   		
   		<td class="l">
          Dal
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
                &nbsp;&nbsp;
          Al
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiPeriodoLibAntCumulo.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
        </td>
   		
    <%	} %>
    		    
      </tr>
    <%
    }  // chiude ciclo for (int k=0; k<NumDate; k++)
    %>
    </table>
  </div>  
  <% } // end for NumCheck %>  
  
  
  </div> <%// end div contenitore%>
  

</form>

<script language="JavaScript" type="text/javascript">
  
  var frmvalidator  = new Validator("ModRimediRis");
  
  // Attivare le funzione di controllo
  frmvalidator.addValidation("<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>","gt=2000");
  frmvalidator.addValidation("<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>","lt=<%=DateUtils.getSysDate("yyyy")%>");

  frmvalidator.addValidation("<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>","gt=2000");
  frmvalidator.addValidation("<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>","lt=<%=DateUtils.getSysDate("yyyy")%>");
  
  frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>

  