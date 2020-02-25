<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.continuazione.action.ICostantiContinuazione"%>

<jsp:useBean id="penaComplessivaSanzioneSostitutiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="modalita"                scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoPenaDetentiva"       scope="request" class="java.lang.String"/>
<jsp:useBean id="valute"                  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaSentenza"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoSanzioneSostitutiva" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"           scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
//==============================================================================
//  Form per inserimento e modifica della
//  - Pena Complessiva
//  - Sanzione sostitutiva
//  - Continuazione
//==============================================================================
  PenaComplessivaModel lPenCom = penaComplessivaSanzioneSostitutiva.getPenaComplessiva();
  SanzioneSostitutivaModel lSanSos = penaComplessivaSanzioneSostitutiva.getSanzioneSostitutiva();

  boolean flagSanzioneSostitutiva = true;
  if(lSanSos == null)
  {
    lSanSos = new SanzioneSostitutivaModel();
    flagSanzioneSostitutiva = false;
  }
  
  boolean flagPenaInContinuazione = false;
  
  // Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;

%>

<html>
<head>
<title>[S.I.E.S.] - Pena Complessiva </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>
<script language="JavaScript">
  function Verify()
  {
 
  // NEW
  TipoSanzione();
    
  if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true)
  {
     if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == '-' )
     {
      alert('Selezionare il tipo di sanzione sostitutiva!');
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.focus();
      return false;
     }
     
     if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
     {
        if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.value=='' && 
           document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.value=='')
        {
    	    if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.value=='' && 
                document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.value=='')
             {
              alert('Pena Pecuniaria Sostitutiva obbligatoria!');
              document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.focus();
              return false;
             }
          }  
      }
     else
     {
     
        if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.value=='' &&  
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.value==''&& 
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.value=='')
        {
         alert('Durata Sanzione Sostitutiva obbligatoria!');
         document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.focus();
         return false;
        }     
    }
  }

  // END NEW


    // Controllo valorizzazione di almeno uno dei campi della sezione Pena
    if(   document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_COD_TIPO_PENA_DETENTIVA%>.value=="-"
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>.value.length==0
       && document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.value.length==0 )
    {
      alert('Almeno un campo della sezione pena deve essere valorizzato');
      document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.focus();

      return false;
    }

    /* Data Inizio Isolamento Diurno
     if (document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==1)
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;
     if (document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value.length==1)
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;

    var d1=document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.value;
    if (! ControllaData(d1) && d1.length > 2)
    {
      alert('Data Inizio Isolamento Diurno non valida');
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO%>.focus();

      return false;
    }
    // Data Fine Isolamento Diurno
    if (document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==1)
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value;
    if (document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value.length==1)
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value='0'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value;

    var d1=document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO%>.value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO%>.value;
    if (! ControllaData(d1) && d1.length > 2)
    {
      alert('Data Fine Isolamento Diurno non valida');
      document.LoadInserisciPenaComplessiva.<!%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO%>.focus();

      return false;
    } */

	 // Data Prescrizione
     var d3=document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_PRESCRIZIONE%>.value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_PRESCRIZIONE%>.value;

	if ( !ControllaDataPassaVuota(d3) )
    {
      alert('Data Prescrizione non valida');
      document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.focus();

      return false;
    }
    
     if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>.checked==true)
     {
	    if (controlliobbligatorietacontinuazione('0')==false){
		    return false;
	    }else{  
	    	if (controlliobbligatorietacontinuazione('1')== false) return false;
	    }
  	 }
  	 
     document.LoadInserisciPenaComplessiva.INSERISCI.disabled=true;
     
  }
  
  //============================================================================
  // Funzione cha abilità o disabilità i campi delle Sanzioni Sostitutive ed 
  // eventualmente li precarica convertendo la pena principale secondo i criteri
  // di legge
  //============================================================================
  function TipoSanzione(par)
  {
//    alert("TipoSanzione: par="+par);
    

    // Durata sanzione (quantum)
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled==false;
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled==false;
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled==false;
    // Pena Pecuniaria Sostitutiva
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled==false;
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled==false;
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled==false;
    document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled==false;
  
     document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled==false;
       
    if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true)
    {
      if (par=='carica'){
        if(   document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == "L" 
           || document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == "S" 
          )
        {
          covertiPenaPrincipale();
        }
      }
      else{
        //alert("TipoSanzione");
      }  
      
     if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
     {//Pena Pecuniaria
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=false; 
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
                
     }
     else if(   document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'S' 
             || document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'L' 
             || document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'E' )
     {
       if(   par=='carica'
          && document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'E' 
         )
       { // Espulsione
         document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.value='5';
         document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.value='';
         document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.value='';
       }
       
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled=false;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
       document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
       }
   }
   else {}
  }
  
  //============================================================================
  // Funzione che converte la detentiva nella Sanzione Sostitutiva corrispondente
  // secondo le conversioni di legge e carica gli opportuni campi
  //============================================================================
  function covertiPenaPrincipale()
  {
    
    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0) {
      aaRec = 0;
    }
    else { aaRec = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>.value; }
      
    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0) {
      mmRec = 0;
    }
    else { mmRec = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>.value; }
    
    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0) {
      ggRec = 0;
    }
    else { ggRec = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>.value; }

    // Arresto
    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0) {
      aaArr = 0;
    }
    else { aaArr = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>.value; }

    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value.length==0) {
      mmArr = 0;
    }
    else { mmArr = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>.value; }

    if (document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0) {
      ggArr = 0;
    }
    else { ggArr = document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>.value; }


    aaSS = parseInt(aaRec) + parseInt(aaArr);
    mmSS = parseInt(mmRec) + parseInt(mmArr);
    ggSS = parseInt(ggRec) + parseInt(ggArr);
    
   
    if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'L' )
    {
       aaSS = 2*aaSS;
       mmSS = 2*mmSS;
       ggSS = 2*ggSS;
    }
    
    // Normalizzo i quantum prima di caricarli
    SSNorm = normalizzaQuantum(aaSS,mmSS,ggSS);
    

    if (SSNorm[1]>0){
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.value = SSNorm[1];
    } else {
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.value = '';
    }
    
    if (SSNorm[2]>0){
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.value   = SSNorm[2]; 
    } else {
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.value   = ''; 
    }

    if (SSNorm[3]>0){
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.value = SSNorm[3];
    } else {
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.value = '';
    }
 
  }
  
  //==================================================
  // Restituisce un array con i quantum normalizzati
  // [0] = segno +/-
  // [1] = anni 
  // [2] = mesi   (0-11)
  // [3] = giorni (0-29)
  //==================================================
  function normalizzaQuantum(anni,mesi,giorni)
  {
    tot_giorni=anni*30*12 + mesi*30 + giorni;
    segno='+';
    //alert("tot_giorni = "+tot_giorni);
    if (tot_giorni<0){
      tot_giorni=tot_giorni*(-1);
      segno='-';
    }
    giorni = tot_giorni % 30; // restituisce il resto dell'operazione
    tot_giorni = tot_giorni-giorni;
    tot_mesi = parseInt(tot_giorni/30); // es 55 mesi
    mesi = tot_mesi % 12;
    tot_mesi = tot_mesi - mesi;
    anni = parseInt(tot_mesi/12);
    //alert("anni = "+anni);
    //alert("mesi = "+mesi);
    //alert("giorni = "+giorni);
    var quantumNormalizzati = new Array(segno,anni ,mesi ,giorni );
    return quantumNormalizzati;
  }

  // Abilita/disabilita i campi della SS in funzione del valore del check
  // invocata sull'onChange
  function checkSanzione()
  {
    if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true){
      // Abilito tutti i campi
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.disabled=false;
      
      if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == '-' )
      { // Non abilito nulla
      }
      if(document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
      { //Pena Pecuniaria
        // Quantum Disabilitati
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled=true;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled=true;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled=true;
        // Pena Pecuniaria Sostitutiva Abilitata
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=false; 
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
         }
      else
      {
        // Quantum Abilitati
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled=false;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled=false;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled=false;
        // Pena Pecuniaria Sostitutiva Disabilitata
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true; 
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
        document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
        }
    }
    else {
      // Disabilito tutti i campi
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>.disabled=true;
      // Quantum
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>.disabled=true;
      // Pena Pecuniaria Sostitutiva
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
      document.LoadInserisciPenaComplessiva.<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
      }

  }
  
  function checkContinuazione()
  {
	  //**********************************************************
	  // Federica - a9-rr-078
	  //**********************************************************
      var continuazione0 = document.getElementById('continuazione0');
	  if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>.checked==true){
		  continuazione0.style.display='block';
		  abilitacontinuazione('0');
	  }else{
		  continuazione0.style.display='none';
		  disabilitacontinuazione('0');
	  }	 
	  
	  var continuazione1 = document.getElementById('continuazione1');
	  if(document.LoadInserisciPenaComplessiva.<%=ICostantiPenaComplessiva.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>.checked==true){
		  continuazione1.style.display='block';
		  abilitacontinuazione('1');
	  }else{
		  continuazione1.style.display='none';
		  disabilitacontinuazione('1');
	  }	 
   }	 

	  function disabilitacontinuazione(ind)
	  {          
		  //**********************************************************
		  // Federica - a9-rr-078
		  //**********************************************************
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].disabled=true;                
	      document.LoadInserisciPenaComplessiva.ARG[ind].disabled=true;          
	      document.LoadInserisciPenaComplessiva.NRG[ind].disabled=true;                
	  }
	  
	  function abilitacontinuazione(ind)
	  {
		  //**********************************************************
		  // Federica - a9-rr-078
		  //**********************************************************
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].disabled=false;                
	      document.LoadInserisciPenaComplessiva.ARG[ind].disabled=false;          
	      document.LoadInserisciPenaComplessiva.NRG[ind].disabled=false; 
	  } 
 	  
	  function controlliobbligatorietacontinuazione(ind)
	  { 
		//**********************************************************
		// Federica - a9-rr-078
		// inseriti controlli su tutti i campi della maschera
		//**********************************************************
    	  
    	  // se tutti i campi sono vuoti tranne l'ultima riga, errore
		  if ((document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].value=="-")
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].value.length==0)          
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].value.length==0)       
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].value.length==0)          
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[ind].value.length==0)         
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[ind].value.length==0)          
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].value=="-")         
	       && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].value.length==0))         
		  {
			 if (document.LoadInserisciPenaComplessiva.TipoRG[ind].value != "-"
			  || document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].value.length>0       
			  || document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].value.length>0             
			  || document.LoadInserisciPenaComplessiva.ARG[ind].value.length>0          
		      || document.LoadInserisciPenaComplessiva.NRG[ind].value.length>0) 
			 {
				alert("Tipo continuazione obbligatorio");
			    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].focus();
				return false;	 
			 }
			 else
			 {
				return true;
			 } 	 
		  } 

		  // se tutti i campi sono pieni e l'ultima riga non è riempita correttamente, errore 
		 if ((document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].value!="-")
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].value.length>0)          
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].value.length>0)       
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].value.length>0)          
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[ind].value.length>0)         
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[ind].value.length>0)          
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].value!="-")         
	      && (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].value.length>0))         
		  {		  
			  // ultima riga bianca, ok
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].value.length==0       
			   && document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].value.length==0                
			   && document.LoadInserisciPenaComplessiva.TipoRG[ind].value=="-"
			   && document.LoadInserisciPenaComplessiva.ARG[ind].value.length==0       
			   && document.LoadInserisciPenaComplessiva.NRG[ind].value.length==0)                
			  {
				  return true;
			  } 
			  // ultima riga parzialmente riempita
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].value.length==0       
	          && document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].value.length>0)                
			  {
					alert("Anno R.G.N.R. non presente");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].focus();
					return false;	 
			  }
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[ind].value.length>0       
	          && document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].value.length==0)                
			  {
					alert("Numero R.G.N.R. non presente");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[ind].focus();
					return false;	 
			  }
			  if (document.LoadInserisciPenaComplessiva.TipoRG[ind].value != "-")
			  {
					if (document.LoadInserisciPenaComplessiva.ARG[ind].value.length==0       
				     || document.LoadInserisciPenaComplessiva.NRG[ind].value.length==0)           
			 		{
						alert("Anno/Numero/Tipo Reg. Gen. non presente");
						document.LoadInserisciPenaComplessiva.ARG[ind].focus();
						return false;	 
					} 
			  } 
			  if (document.LoadInserisciPenaComplessiva.TipoRG[ind].value == "-")
			  {
		       		if (document.LoadInserisciPenaComplessiva.ARG[ind].value.length!=0       
				     || document.LoadInserisciPenaComplessiva.NRG[ind].value.length!=0)           
			 		{
						alert("Anno/Numero/Tipo Reg. Gen. non presente");
						document.LoadInserisciPenaComplessiva.ARG[ind].focus();
						return false;	 
					} 
			  } 
			  if (document.LoadInserisciPenaComplessiva.ARG[ind].value.length==0       
	          && document.LoadInserisciPenaComplessiva.NRG[ind].value.length>0)                
			  {
					alert("Anno Reg. Gen. non presente");
				    document.LoadInserisciPenaComplessiva.ARG[ind].focus();
					return false;	 
			  }
			  if (document.LoadInserisciPenaComplessiva.ARG[ind].value.length>0       
	          && document.LoadInserisciPenaComplessiva.NRG[ind].value.length==0)                
			  {
					alert("Numero Reg. Gen. non presente");
				    document.LoadInserisciPenaComplessiva.NRG[ind].focus();
					return false;	 
			  }		  
			  return true;
		  }
		  else
		  {
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].value=="-")
			  {
					alert("Tipo continuazione obbligatorio");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[ind].focus();
					return false;		 
			  }
			  
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].value.length==0)          
			  {
					alert("Anno sentenza obbligatorio");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[ind].focus();
					return false;		 
			  }
			  
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].value.length==0)          
			  {
					alert("Numero sentenza obbligatorio");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[ind].focus();
					return false;	 
			  }
			  
	    	  var d1=document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[ind].value+'/'+document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[ind].value;  	     
	    	  if (! ControllaData(d1))
		      {	    		      
		        alert('Data sentenza non valida');
			    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[ind].focus();
		        return false;
		      }
			  if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].value=="-")
			  {
					alert("Autorità obbligatoria");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[ind].focus();
					return false;		 
			  }
		      if (document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].value.length==0)    
			  {
					alert("Luogo Autorità obbligatoria");
				    document.LoadInserisciPenaComplessiva.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[ind].focus();
					return false;		 
			  }
	
	   } 
   } 
 
</script>

</head>

<%   if( modalita.equals("I") )
	 { 
%>
  			<body class="corpo" onload="checkContinuazione();">
<%   }
	 else
	 { %>  			
	 		<body class="corpo">
<%   } %>	

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
          PenaComplessivaModel lPenaComplessiva = new PenaComplessivaModel();
          //lPenaComplessiva.setFlagPenaInContinuazione("N");

          SanzioneSostitutivaModel lSanzioneSostitutiva = new SanzioneSostitutivaModel();

          String lAzione = new String();
          if( modalita.equals("I") )
          {
        	if (modoSIGE)
                lAzione = "siap.sige.penacomplessiva.action.ActInserisciPenaCompSige";
        	else
        		lAzione = "siap.siep.penacomplessiva.action.ActInserisciPenaComplessiva";
%>
            <font class="campo">Inserimento Pena Complessiva</font>
<%
          }
          else if( modalita.equals("M") )
          {
          	if (modoSIGE)
                lAzione = "siap.sige.penacomplessiva.action.ActModificaPenaCompSige";
        	else

            lAzione = "siap.siep.penacomplessiva.action.ActModificaPenaComplessiva";
            lPenaComplessiva = lPenCom;
            lSanzioneSostitutiva = lSanSos;
%>
            <font class="campo">Modifica Pena Complessiva</font>
<%
          }
%>
        </td>
      </tr>
    </table>

	  <br>
   <%if(!modoSIGE)
    {%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  	<br>
    
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciPenaComplessiva">
      <table cellspacing="2" cellpadding="2">
        <tr><td class="Titolo" colspan="4">Pena</td></tr>
        <tr>
          <td class="l" width="22%">Reclusione</td>
          <td class="l">
            Anni&nbsp;<input Title="Anni Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniReclusione())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" 
            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            Mesi&nbsp;<input Title="Mesi Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiReclusione())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" 
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            Giorni&nbsp;<input Title="Giorni Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniReclusione())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
          <td class="l">Multa</td>
          <td class="l" width="31%">
<%
            String lImportoMulta = StringUtils.toStringJSP(lPenaComplessiva.getImportoMulta());
            String lParteInteraMulta = "";
            String lParteDecimaleMulta = "";
            int lIndexMulta = lImportoMulta.indexOf(".");
            if(lIndexMulta == -1)
            {
              lParteInteraMulta = lImportoMulta;
              lParteDecimaleMulta = "";
            }
            else
            {
              lParteInteraMulta = lImportoMulta.substring(0,lIndexMulta);
              lParteDecimaleMulta = lImportoMulta.substring(lIndexMulta+1);;
            }
%>
            <input Title="Multa" size="14" maxlength="14" value="<%=StringUtils.toZerotoStringaVuota(lParteInteraMulta,"")%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input Title="Multa" size="2" maxlength="2" value="<%=lParteDecimaleMulta%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            <select name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_MULTA%>">
              <%=valute%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Arresto</td>
          <td class="l">
            Anni&nbsp;<input Title="Anni Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniArresto())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            Mesi&nbsp;<input Title="Mesi Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiArresto())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"> 
            Giorni&nbsp;<input Title="Giorni Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniArresto())%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" 
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
  
          </td>
          <td class="l">Ammenda</td>
          <td class="l">
<%
            String lImportoAmmenda = StringUtils.toStringJSP(lPenaComplessiva.getImportoAmmenda());
            String lParteInteraAmmenda = "";
            String lParteDecimaleAmmenda = "";
            int lIndexAmmenda = lImportoAmmenda.indexOf(".");
            if(lIndexAmmenda == -1)
            {
              lParteInteraAmmenda = lImportoAmmenda;
              lParteDecimaleAmmenda = "";
            }
            else
            {
              lParteInteraAmmenda = lImportoAmmenda.substring(0, lIndexAmmenda);
              lParteDecimaleAmmenda = lImportoAmmenda.substring(lIndexAmmenda+1);;
            }
%>
            <input Title="Ammenda" size="14" maxlength="14" value="<%=StringUtils.toZerotoStringaVuota(lParteInteraAmmenda,"")%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input Title="Ammenda" size="2" maxlength="2" value="<%=lParteDecimaleAmmenda%>" type="text" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" 
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            <select name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_AMMENDA%>">
              <%=valute%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Ergastolo</td>
          <td class="l">
            <select Title="Pena Detentiva" name="<%=ICostantiPenaComplessiva.CAMPO_COD_TIPO_PENA_DETENTIVA%>">
              <%=tipoPenaDetentiva%>
            </select>
          </td>
          <td colspan="2">&nbsp;</td>
        </tr>

        <tr>
            <td class="l">Durata Isolamento Diurno</td>
            <td class="l">Anni
              <input Title="Anni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniIsolamentoDiurno()) %>" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" 
              onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
              Mesi
              <input Title="Mesi Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiIsolamentoDiurno())%>" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" 
              onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
              Giorni
              <input Title="Giorni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniIsolamentoDiurno())%>" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO%>"maxlength="4" size="4" 
              onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            </td>
            <td class="l">Data Prescrizione</td>
            <td class="l">
             <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "dd")) %>" name="<%=ICostantiPenaComplessiva.CAMPO_GIORNO_DATA_PRESCRIZIONE%>" maxlength="2" size="2" 
             	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
             <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "MM"))%>" name="<%=ICostantiPenaComplessiva.CAMPO_MESE_DATA_PRESCRIZIONE%>" maxlength="2" size="2" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "YYYY"))%>" name="<%=ICostantiPenaComplessiva.CAMPO_ANNO_DATA_PRESCRIZIONE%>"maxlength="4" size="4" 
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

            </td>
        </tr>
      </table>
      
<%
//==============================================================================
//                             Sanzione Sostitutiva
//==============================================================================
%>      
      
      <table cellspacing="2" cellpadding="2">
        <tr><td class="Titolo" colspan="4">Sanzione Sostitutiva</td></tr>
        
        <tr>
          <td class="l">Sanzione Sostitutiva</td>
          <td class="label">
            <input type="checkbox" name="<%=ICostantiPenaComplessiva.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>" value="S" 
                   <%= flagSanzioneSostitutiva ? "checked" : ""%>title="Check per selezionare la Sanzione Sostitutiva"  
                   onClick="Javascript:checkSanzione();" >
          </td>
        </tr>
        
        <tr>
          <td class="l">Tipo </td>
          <td class="l">
            <select Title="Tipo Sanzione Sostitutiva" name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE%>" onChange="TipoSanzione('carica');">
              <%=tipoSanzioneSostitutiva%>
            </select>&nbsp;&nbsp;&nbsp;&nbsp;

          Durata&nbsp;:&nbsp;&nbsp;&nbsp;
            Anni&nbsp;<input Title="Anni Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumAnni())%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            Mesi&nbsp;<input Title="Mesi Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumMesi())%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            Giorni&nbsp;<input Title="Giorni Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumGiorni())%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
        </tr>
        
        <tr>
          <td class="l">Pena Pecuniaria </td>
          <td class="l">MULTA&nbsp;&nbsp;
<%
            String lImportoPenaPecSost = StringUtils.toStringJSP(lSanzioneSostitutiva.getSanzionePecuniariaMulta());
            String lParteInteraPenaPecSost = "";
            String lParteDecimalePenaPecSost = "";
            int lIndexPenaPecSost = lImportoPenaPecSost.indexOf(".");
            if(lIndexPenaPecSost == -1)
            {
              lParteInteraPenaPecSost = lImportoPenaPecSost;
              lParteDecimalePenaPecSost = "";
            }
            else
            {
              lParteInteraPenaPecSost = lImportoPenaPecSost.substring(0, lIndexPenaPecSost);
              lParteDecimalePenaPecSost = lImportoPenaPecSost.substring(lIndexPenaPecSost+1);;
            }
%>
            <input Title="Pena Pecuniaria Sostitutiva" size="14" maxlength="14" value="<%=lParteInteraPenaPecSost%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input Title="Pena Pecuniaria Sostitutiva" size="2" maxlength="2" value="<%=lParteDecimalePenaPecSost%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            <select name="<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>">
              <%=valute%>
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
  
<%
  //**********************************************************
  // Federica - a9-rr-078
  // inserita multa o ammenda
  //**********************************************************
%>      
 
          AMMENDA&nbsp;&nbsp;
<%
            String lImportoPenaPecSostA = StringUtils.toStringJSP(lSanzioneSostitutiva.getSanzionePecuniariaAmmenda());
            String lParteInteraPenaPecSostA = "";
            String lParteDecimalePenaPecSostA = "";
            int lIndexPenaPecSostA = lImportoPenaPecSostA.indexOf(".");
            if(lIndexPenaPecSostA == -1)
            {
              lParteInteraPenaPecSostA = lImportoPenaPecSostA;
              lParteDecimalePenaPecSostA = "";
            }
            else
            {
              lParteInteraPenaPecSostA = lImportoPenaPecSostA.substring(0, lIndexPenaPecSostA);
              lParteDecimalePenaPecSostA = lImportoPenaPecSostA.substring(lIndexPenaPecSostA+1);;
            }
%>
            <input Title="Pena Pecuniaria Sostitutiva" size="14" maxlength="14" value="<%=lParteInteraPenaPecSostA%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            ,
            <input Title="Pena Pecuniaria Sostitutiva" size="2" maxlength="2" value="<%=lParteDecimalePenaPecSostA%>" type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
            <select name="<%=ICostantiSanzioneSostitutiva.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>">
              <%=valute%>
            </select>
          </td>
        </tr>
        
<%
//==============================================================================
// Sezione con la continuazione
//==============================================================================
  if(modalita.equals("I") && (!modoSIGE) )
  {
%>
       <tr><td class="Titolo" colspan="4">Continuazione con altre sentenze</td></tr>
      </table>
      <table cellspacing="2" cellpadding="2">
        <tr>
          <td class="l">
            Continuazione con altre sentenze&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="checkbox" name="<%=ICostantiPenaComplessiva.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>" value="S"
                   <%= flagPenaInContinuazione ? "checked" : ""%>  
                   onClick="Javascript:checkContinuazione();"
            >
            &nbsp;
          </td>
        </tr>
<%
  }
%>
      </table>
<%
  
  if(modalita.equals("I")&& (!modoSIGE))
  {
      for(int i=0; i<2; i++)
      {
%>

<div id="continuazione<%=i%>" style="width: 100%; display:none; position:relative;">
            <table cellspacing="2" cellpadding="2" width="100%">
              <tr>
                <td class="l">Tipo Continuazione</td>
                <td class="l" colspan="3">
                  <select Title="Tipo Continuazione" name="<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>">
                    <%=tipoContinuazione%>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="l">Anno/Numero Sentenza</td>
                <td class="L">
                  <input Title="Anno Sentenza" value="<%=""%>" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4"
                  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                  /
                  <input Title="Numero Sentenza" value="<%=""%>" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>" maxlength="6" size="6"
                  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                </td>
                <td class="l">Data Sentenza</td>
                <td class="l">
                  <input Title="Giorno Data Sentenza" type="text" value="<%=""%>" name="<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>" maxlength="2" size="2" 
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Mese Data Sentenza" type="text" value="<%=""%>" name="<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>" maxlength="2" size="2" 
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Anno Data Sentenza" type="text" value="<%=""%>" name="<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>"maxlength="4" size="4" 
                  	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                </td>
              </tr>
              <tr>
                <td class="l">Autorità Sentenza</td>
                  <td class="l" colspan="3">
                    <select Title="Autorità Sentenza" name="<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>">
                      <%=autoritaSentenza%>
                    </select>
                  </td>
              </tr>
              <tr>
                <td class="l">Luogo Sentenza</td>
                <td class="l" colspan="3">
                  <input Title="Luogo Sentenza" name="<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>" value="" type="text" maxlength="35" size="35">
                  <a href="Javascript:ListaComuni('LoadInserisciPenaComplessiva','<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[<%=i%>]');">
                    <img src="/images/filefolder.gif" border="0">
                  </a>
                </td>
              </tr>
              <tr>
      <td class="l">Anno/Numero R.G.N.R.</td>
      <td class="L">
          <input Title="Anno R.G.N.R." value="" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4"
          	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         /<input Title="Numero R.G.N.R." value="" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>" maxlength="6" size="6"
         	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>

     <td class="l">Anno/Numero Reg.Gen.</td>
      <td class="L">
          <input Title="Anno Reg.Gen." value="" type="text" name="ARG" maxlength="4" size="4"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         /<input Title="Numero Reg.Gen." value="" type="text" name="NRG" maxlength="6" size="6"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      &nbsp;

      <select name="TipoRG">

      <option value="-">-</option>

      <option value="gip">GIP</option>

      <option value="dib">DIB</option>

      <option value="cas">CAS</option>

      <option value="cap">CAP</option>

      <option value="casap">CASAP</option>
      </select>
      </td>

		</tr>

            </table>
            </div>
<%
      }
  }
%>      
      <table cellspacing="2" cellpadding="2" width="100%">

        <tr>
          <td colspan="2">
            <br>
            <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
          </td>
        </tr>
  </table>
  <input type="HIDDEN" name="Action" value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getIdPenaComplessiva())%>">
  <input type="HIDDEN" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getIdSanzioneSostitutiva())%>">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
   <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">

</form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator = new Validator("LoadInserisciPenaComplessiva");

  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>","numeric");

<%
  if(modalita.equals("I")&& (!modoSIGE) )
  {
    for(int i=0; i<2; i++)
    {
%>
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","gt=1900");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","lt=3000");


      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","gt=1900");
      frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","lt=3000");

      frmvalidator.addValidationWithIdx("NRG","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("ARG","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("ARG","<%=i%>","gt=1900");
      frmvalidator.addValidationWithIdx("ARG","<%=i%>","lt=3000");
<%
    }
  }
%>

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>