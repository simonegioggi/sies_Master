<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>


<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="ListaTitoli"       scope="request" class="java.util.Vector"/>


<jsp:useBean id="penaComplessivaSanzioneSostitutivaCumulo" scope="request" class="siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel"/>

<jsp:useBean id="modalita"                scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoPenaDetentiva"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoSanzioneSostitutiva" scope="request" class="java.lang.String"/>
<jsp:useBean id="valute"                  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaSentenza"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRegGen"              scope="request" class="java.lang.String"/>


<jsp:useBean id="TornaQui"                scope="request" class="java.lang.String"/>

<%
//==============================================================================
//  Form per inserimento e modifica della
//  - Pena Complessiva
//  - Sanzione sostitutiva
//  - Continuazione ??
//==============================================================================

  PenaComplessivaCumuloModel lPenComCum = penaComplessivaSanzioneSostitutivaCumulo.getPenaComplessivaCumulo();
  SanzioneSostitutivaCumuloModel lSanSosCum = penaComplessivaSanzioneSostitutivaCumulo.getSanzioneSostitutivaCumulo();

  boolean flagSanzioneSostitutiva = true;
  if(lSanSosCum == null)
  {
    lSanSosCum = new SanzioneSostitutivaCumuloModel();
    flagSanzioneSostitutiva = false;
  }
 
  
%>

<html>
<head>
<title>[S.I.E.S.] - Pena Complessiva (CUMULO) </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    
<script language="JavaScript">
  var desktop;


  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaTitoliInIstruttoria (a_formname)
  {
    <%
    String lStrParametri = "";
    lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
    lStrParametri +="&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+TitoloInCumulo.getIdTitoloCumulato();
    %>
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActListaTitoliPerContinuazione&formname="+a_formname+"<%=lStrParametri%>", "Lista_Titoli_In_Istruttoria", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
  } 
  
  //==========================================================================
  // Ritorna alla Griglia dei dati analitici
  //==========================================================================
  function tornaIndietro(action)
  {
    document.LoadInserisciPenaComplessivaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.LoadInserisciPenaComplessivaCumulo.submit();
  }

 
  
  //=======================
  //
  //=======================
  function Verify()
  {
    //==========================================================================
    // Controllo sulla pena detentiva, valorizzazione di almeno uno dei campi
    if(   document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA%>.value=="-"
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_INTERO_IMPORTO_MULTA%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_INTERO_IMPORTO_AMMENDA%>.value.length==0
       && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_DECIMALE_IMPORTO_AMMENDA%>.value.length==0 )
    {
      alert('Almeno un campo della sezione Pena deve essere valorizzato');
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.focus();

      return false;
    }

    // Data Prescrizione
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.value.length==1)
        document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.value='0'+document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.value;
     
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_MESE_DATA_PRESCRIZIONE%>.value.length==1)
        document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_MESE_DATA_PRESCRIZIONE%>.value='0'+document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_MESE_DATA_PRESCRIZIONE%>.value;

    var d3=document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.value+'/'+document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_MESE_DATA_PRESCRIZIONE%>.value+'/'+document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_ANNO_DATA_PRESCRIZIONE%>.value;

    if ( !ControllaDataPassaVuota(d3) )
    {
        alert('Data Prescrizione non valida');
        document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>.focus();
        return false;
    }

    //======================================
    // Controlli sulla Sanzione Sostitutiva
    //======================================
    TipoSanzione();

    if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true)
    {
      if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE %>.selectedIndex].value == '-' )
      {
        alert('Selezionare il tipo di sanzione sostitutiva!');
        document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE %>.focus();
        return false;
      }
     
      if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
      {
        if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.value=='' && 
           document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.value==''&& 
           document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.value=='' &&
           document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.value=='' )
        {
         alert('Pena Pecuniaria Sostitutiva obbligatoria!');
         document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.focus();
         return false;
        }
      }
      else
      {     
        if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.value=='' &&  
           document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.value==''&& 
           document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.value=='')
        {
         alert('Durata Sanzione Sostitutiva obbligatoria!');
         document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.focus();
         return false;
        }
      }
    }
    
    //=======================================
    // Controlli su Continuazione Sentenza
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>.checked==true)
    {
      if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.value=="-")
      {
        alert('Tipo continuazione obbligatorio');
        document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();
        return false;
      }
      
      if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.value !="-" )
      {
        if (    (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.value.length==0)
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.value.length==0)
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==0)
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value.length==0)
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.value.length==0)
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.value=="-")
             || (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.value.length==0 ) )
        {
          alert('Tipo Continuazione, Anno, Numero, Data, Luogo e Autorità Sentenza sono OBBLIGATORI');
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();  
          return false;
        }
      }
      

      if (   document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length==0
          && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length>0 )
      {
          alert('Anno R.G.N.R. non presente');
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.focus();
          return false;  
      }
          
      if (  document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length>0
         && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length==0)
      {
          alert('Numero R.G.N.R. non presente');
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.focus();
          return false;  
      }

      if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value != "-")
      {
        if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length==0       
           || document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length==0)           
        {
            alert("Anno o Numero Reg. Gen. non presente");
            document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.focus();
            return false;  
        } 
      }       
      
      if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value == "-") 
      {
        if (   document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length!=0       
            || document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length!=0)           
        {
            alert("Inserire uno tra GIP/DIB/CAS/CAP/CASAP in TIPO RG");
            document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.focus();
            return false;  
        } 
      }
    }  // Chiude if (controlli su sentenza in continuazione)   
    

     document.LoadInserisciPenaComplessivaCumulo.INSERISCI.disabled=true;
  
  } //  chiude functione Verify
  
  //============================================================================
  // Funzione cha abilità o disabilità i campi delle Sanzioni Sostitutive ed 
  // eventualmente li precarica convertendo la pena principale secondo i criteri
  // di legge
  //============================================================================
  function TipoSanzione(par)
  {
    // Durata sanzione (quantum)
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=false;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=false;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=false;
    // Pena Pecuniaria Sostitutiva
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;    
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=false;
       
    if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true)
    {
      if (par=='carica'){
        if(   document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'L' 
           || document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'S' 
          )
        {
          covertiPenaPrincipale();
        }
      }
      else{
        //alert("TipoSanzione");
      }  
      
     if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
     {//Pena Pecuniaria
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;       
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=false; 
             
     }
     else if(   document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'S' 
             || document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'L' 
             || document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'E' )
     {
       if(   par=='carica'
          && document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'E' 
         )
       { // Espulsione
         document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.value='5';
         document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.value='';
         document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.value='';
       }
       
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=false;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;       
       document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true;
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
    
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value.length==0) {
      aaRec = 0;
    }
    else { aaRec = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value; }
      
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value.length==0) {
      mmRec = 0;
    }
    else { mmRec = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value; }
    
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value.length==0) {
      ggRec = 0;
    }
    else { ggRec = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value; }

    // Arresto
    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value.length==0) {
      aaArr = 0;
    }
    else { aaArr = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value; }

    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_ARRESTO%>.value.length==0) {
      mmArr = 0;
    }
    else { mmArr = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_ARRESTO%>.value; }

    if (document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value.length==0) {
      ggArr = 0;
    }
    else { ggArr = document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value; }


    aaSS = parseInt(aaRec) + parseInt(aaArr);
    mmSS = parseInt(mmRec) + parseInt(mmArr);
    ggSS = parseInt(ggRec) + parseInt(ggArr);
    
   
    if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'L' )
    {
       aaSS = 2*aaSS;
       mmSS = 2*mmSS;
       ggSS = 2*ggSS;
    }
    
    // Normalizzo i quantum prima di caricarli
    SSNorm = normalizzaQuantum(aaSS,mmSS,ggSS);
    

    if (SSNorm[1]>0){
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.value = SSNorm[1];
    } else {
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.value = '';
    }
    
    if (SSNorm[2]>0){
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.value   = SSNorm[2]; 
    } else {
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.value   = ''; 
    }

    if (SSNorm[3]>0){
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.value = SSNorm[3];
    } else {
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.value = '';
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

  //============================================================================
  // Abilita/disabilita i campi della SS in funzione del valore del check
  // invocata sull'onChange
  //============================================================================
  function checkSanzione()
  {
    if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>.checked==true)
    {
      // Abilito tutti i campi
      document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.disabled=false;
      
      if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == '-' )
      { // Non abilito nulla
      }
          
      if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>[document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex].value == 'P' )
      { 
          //Pena Pecuniaria
          // Quantum Disabilitati
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=true;
          // Pena Pecuniaria Sostitutiva Abilitata
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=false; 
      }
      else
      {
          // Quantum Abilitati
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=false;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=false;
          // Pena Pecuniaria Sostitutiva Disabilitata
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true;
        }
      }
      else 
      {
          // Disabilito tutti i campi
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>.disabled=true;
          // Quantum
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>.disabled=true;
          // Pena Pecuniaria Sostitutiva
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>.disabled=true;
          document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>.disabled=true;

      }
  }
  
  // Continuazione
  function disableCampiTitolo (){
    $('#trSelLista').hide();
    $('#trAssociazione').show();
    $('tr[trDatiSentenza]').hide();

    
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>]').eq(0).val());
    
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>]:eq(0) option:selected').text());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>]:eq(0) option:selected').text());


    $('tr[trLabelSentenza]').show();
  }
  
  function enableCampiTitolo (){
    $('#trSelLista').show();
    $('#trAssociazione').hide();
    
    $('tr[trDatiSentenza]').show();
    $('tr[trLabelSentenza]').hide();    
  }

  function EliminaAssociazione ()
  {
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>.value="";
    
    // Riabilito i campi
    enableCampiTitolo();
  }   
  
  function checkContinuazione()
  {
    var continuazione = document.getElementById('continuazione');
    if(document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>.checked==true)
    {
      continuazione.style.display='block';
      abilitacontinuazione();
    }
    else
    {
      continuazione.style.display='none';
      disabilitacontinuazione();
    }
  } 
  
  function inizializza(){
    //disableCampiTitolo();
    enableCampiTitolo();
    checkSanzione();
  } 

  function disabilitacontinuazione()
  {          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.disabled=true;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.disabled=true;                
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.disabled=true;
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.disabled=true;
    
  }
    
  function abilitacontinuazione()
  {
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.disabled=false;                
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.disabled=false;          
    document.LoadInserisciPenaComplessivaCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.disabled=false; 
  } 
</script>

</head>

<% if( modalita.equals("I") ) { %>
<body class="corpo" onload="inizializza();">
<% } else { %>
<!--  body class="corpo" -->    
<body class="corpo" onLoad="Javascript:checkSanzione();">
<% } %> 
    
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
            PenaComplessivaCumuloModel lPenaComplessiva = new PenaComplessivaCumuloModel();

            SanzioneSostitutivaCumuloModel lSanzioneSostitutiva = new SanzioneSostitutivaCumuloModel();
            
            String lAzione = new String();
            if( modalita.equals("I") )
            {
              lAzione = "siap.siep.modulocumulo.action.ActInserisciPenaComplessivaCumulo";
%>
              <font class="campo">Inserimento Pena Complessiva relativa al Titolo Cumulato</font>
<%
            }
            else if( modalita.equals("M") )
            {
              lAzione = "siap.siep.modulocumulo.action.ActModificaPenaComplessivaCumulo";
              lPenaComplessiva = lPenComCum;
              lSanzioneSostitutiva = lSanSosCum;
%>
              <font class="campo">Modifica Pena Complessiva relativa al Titolo Cumulato</font>
<%
            }
%>
        </td>
        
<%    if( modalita.equals("I") )
    {
%>     
          <td class="LBG">
            <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
<%
        }
        else if( modalita.equals("M") )
        {
%>              
          <td class="LBG">
            <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo')">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td> 
<%                    
    }  
%>        
      
      </tr>
    </table>
 
    <br>
    <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
      <tr>
        <td>
          <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
        </td>
      </tr>
      <tr>
        <td>
          <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
        </td>
      </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciPenaComplessivaCumulo">    
    <input type="HIDDEN" name="Action" value="<%=lAzione%>" >

    <input type="HIDDEN" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>"     value="<%=StringUtils.toStringJSP(lPenaComplessiva.getIdPenaComplessivaCum())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_STATO %>"                 value="<%=StringUtils.toStringJSP(lPenaComplessiva.getFlagStato())%>">
    
    <% if( modalita.equals("M") ) { %>
    <input type="HIDDEN" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_PENA_IN_CONTINUAZIONE %>" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getFlagPenaInContinuazione())%>">
    <% } %>
    
    <input type="HIDDEN" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_ID_SANZIONE_SOSTITUTIVA_CUM%>" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getIdSanzioneSostitutivaCum())%>">
    <input type="HIDDEN" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_FLAG_STATO %>"                 value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getFlagStato())%>">

    <!-- Campi sempre presenti sulle form dei dati analitici -->
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
    
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
    


    <table cellspacing=2 cellpadding=4>
      <tr><td class="Titolo" colspan=4>Pena</td></tr>
      <tr>
          <td class="l" colspan=1 >Reclusione</td>
          <td class="l" colspan=1>
            Anni&nbsp;<input Title="Anni Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniReclusione())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
            Mesi&nbsp;<input Title="Mesi Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiReclusione())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_RECLUSIONE%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
            Giorni&nbsp;<input Title="Giorni Reclusione" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniReclusione())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          </td>
          
          <td class="l" colspan=1 >Multa</td>
          <td class="l" colspan=1 >

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
                lParteDecimaleMulta = lImportoMulta.substring(lIndexMulta+1);
            }
%>

                <input Title="Multa" size=8 maxlength=16 value="<%=StringUtils.toZerotoStringaVuota(lParteInteraMulta,"")%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_INTERO_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">
                ,
                <input Title="Multa" size=2 maxlength=2 value="<%=lParteDecimaleMulta%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_DECIMALE_IMPORTO_MULTA%>" onkeypress="return TicTabNumField(this,event)">
                <select name="<%=ICostantiPenaComplessivaCumulo.CAMPO_VALUTA_IMPORTO_MULTA%>">
                    <%=valute%>
                </select>
              </td>
          </tr>
        
          <tr>
              <td class="l" colspan=1>Arresto</td>
              <td class="l" colspan=1>
                Anni&nbsp;<input Title="Anni Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniArresto())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_ARRESTO%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
                Mesi&nbsp;<input Title="Mesi Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiArresto())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_ARRESTO%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
                Giorni&nbsp;<input Title="Giorni Arresto" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniArresto())%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_ARRESTO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
              </td>
              
              <td class="l" colspan=1>Ammenda</td>
              <td class="l" colspan=1>
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
                lParteDecimaleAmmenda = lImportoAmmenda.substring(lIndexAmmenda+1);
            }
%>
                <input Title="Ammenda" size=8 maxlength=16 value="<%=StringUtils.toZerotoStringaVuota(lParteInteraAmmenda,"")%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_INTERO_IMPORTO_AMMENDA%>" onkeypress="return TicTabNumField(this,event)">
                ,
                <input Title="Ammenda" size=2 maxlength=2 value="<%=lParteDecimaleAmmenda%>" type="text" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" onkeypress="return TicTabNumField(this,event)">
                <select name="<%=ICostantiPenaComplessivaCumulo.CAMPO_VALUTA_IMPORTO_AMMENDA%>">
                  <%=valute%>
                </select>
              </td>
          </tr>
        
          <tr>
              <td class="l" colspan=1>Ergastolo</td>
              <td class="l" colspan=1>
                <select Title="Pena Detentiva" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA%>">
                    <%=tipoPenaDetentiva%>
                </select>
              </td>
          </tr>
      
          <tr>
              <td class="l" colspan=1>Durata Isolamento Diurno</td>
              <td class="l" colspan=1>
                    Anni
                  <input Title="Anni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumAnniIsolamentoDiurno()) %>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
                    Mesi
                  <input Title="Mesi Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumMesiIsolamentoDiurno())%>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
                    Giorni
                  <input Title="Giorni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lPenaComplessiva.getNumGiorniIsolamentoDiurno())%>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO%>"maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
              </td>
              <td class="l" colspan=1>Data Prescrizione</td>
              <td class="l" colspan=1>
                <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "dd")) %>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_GIORNO_DATA_PRESCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                -
                <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "MM"))%>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_MESE_DATA_PRESCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                -
                <input Title="Data Prescrizione" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaComplessiva.getDataPrescrizione(), "yyyy"))%>" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_ANNO_DATA_PRESCRIZIONE%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
              </td>
          </tr>
          
          <tr>
            <td class="l" colspan=1>Motivo<br>Inserimento/Modifica<br>Pena Complessiva</td>
            <td class="l" colspan=3><TextArea cols=80 rows=4 name="<%= ICostantiPenaComplessivaCumulo.CAMPO_MOTIVO_MODIFICA %>"><%=StringUtils.toStringJSP(lPenaComplessiva.getMotivoModifica()) %></textarea></td>
          </tr>
      </table>
      <br>
      
<%
//==============================================================================
//                             Sanzione Sostitutiva
//==============================================================================
%>      
      
      <table cellspacing="2" cellpadding="4" width="95%">
        <tr>
          <td class="Titolo" colspan=4>Sanzione Sostitutiva</td>
        </tr>
        
        <tr>
            <td class="l" colspan=1>Sanzione Sostitutiva</td>
            <td class="l" colspan=1>
              <input type="checkbox" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>" value="S" 
                    <%= flagSanzioneSostitutiva ? "checked" : ""%>  
                      onClick="Javascript:checkSanzione();"
                    >
            </td>
            <td colspan="2">&nbsp;</td>
        </tr>
        
        <tr>
          <td class="l" colspan=1>Tipo Sanzione Sostitutiva</td>
          <td class="l" colspan=1>
            <select Title="Tipo Sanzione Sostitutiva" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_COD_TIPO_SANZIONE%>" onChange="TipoSanzione('carica');">
              <%=tipoSanzioneSostitutiva%>
            </select>
          </td>
          
          <td class="l" colspan=1>Durata Sanzione Sostitutiva</td>
          <td class="l" colspan=1>
            Anni&nbsp;<input Title="Anni Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumAnni())%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_ANNI%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
            Mesi&nbsp;<input Title="Mesi Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumMesi())%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_MESI%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
            Giorni&nbsp;<input Title="Giorni Sanzione Sostitutiva" value="<%=StringUtils.toStringJSP(lSanzioneSostitutiva.getNumGiorni())%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_NUM_GIORNI%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          </td>
        </tr>
<%
  //**********************************************************
  // Pena Pec MULTA - 
  //**********************************************************
%>     
          <tr>
              <td class="Titolo" colspan=4>Pena Pecuniaria Sostitutiva</td>
          </tr> 
                      
          <tr>
              <td class="l" colspan=1 >MULTA</td>
              <td class="l" colspan=1 >
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
                lParteDecimalePenaPecSost = lImportoPenaPecSost.substring(lIndexPenaPecSost+1);
            }
%>
                <input Title="Pena Pecuniaria Sostitutiva" size=8 maxlength=16 value="<%=lParteInteraPenaPecSost%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA%>" onkeypress="return TicTabNumField(this,event)">
                ,
                <input Title="Pena Pecuniaria Sostitutiva" size=2 maxlength=2 value="<%=lParteDecimalePenaPecSost%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA%>" onkeypress="return TicTabNumField(this,event)">
                <select name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>">
                    <%=valute%>
                </select>
              </td>
              
<%
//*==============*********************************************************
// Pena Pec AMMENDA - Nuovo campo in Sanzione Sostitutiva da Luglio 2009 
 //**************==============********************************************
 %>                   
              <td class="l" colspan=1 >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AMMENDA</td>
              <td class="l" colspan=1 >
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
                lParteDecimalePenaPecSostA = lImportoPenaPecSostA.substring(lIndexPenaPecSostA+1);
            }
%>
                <input Title="Pena Pecuniaria Sostitutiva" size=8 maxlength=16 value="<%=lParteInteraPenaPecSostA%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA%>" onkeypress="return TicTabNumField(this,event)">
                ,
                <input Title="Pena Pecuniaria Sostitutiva" size=2 maxlength=2 value="<%=lParteDecimalePenaPecSostA%>" type="text" name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA%>" onkeypress="return TicTabNumField(this,event)">
                <select name="<%=ICostantiSanzioneSostitutivaCumulo.CAMPO_VALUTA_SANZIONE_PECUNIARIA%>">
                  <%=valute%>
                </select>
              </td>
              
              <td colspan="2">&nbsp;</td>
          </tr>
        
        <tr>
        <td class="l" colspan=1>Motivo<br>Inserimento/Modifica<br>Sanzione Sostitutiva</td>
        <td class="l" colspan=3><TextArea cols=80 rows=4 name="<%= ICostantiSanzioneSostitutivaCumulo.CAMPO_MOTIVO_MODIFICA_NOTE %>"><%=StringUtils.toStringJSP(lSanzioneSostitutiva.getMotivoModifica()) %></textarea></td>
      </tr>
    </table>
      
<br>  
        
<%
//==============================================================================
// Sezione con la Continuazione (solo in fase di inserimento, no modifica)
// In cumulo previsto solo una sezione
//==============================================================================
if(modalita.equals("I"))
{
%>
<table cellspacing=2 cellpadding=4>
  <tr>
    <td class="Titolo" colspan=4>Continuazione con Altre Sentenze</td>
  </tr>

  <tr>
    <td class="l" colspan=1>Continuazione con Altre Sentenze</td>
    <td class="l" colspan=1>
      <input type="checkbox" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_FLAG_PENA_IN_CONTINUAZIONE%>" value="S" 
             onClick="Javascript:checkContinuazione();" >
    </td>
    <td colspan="2">&nbsp;</td>
  </tr>
</table>


<%
//==============================================================================
//
//==============================================================================
%>
<div id="continuazione" style="width: 100%; display:none; position:relative; "  >
  <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>"  value="">

  <table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      <td class="l">Tipo Continuazione <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <select Title="Tipo Continuazione" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>">
          <%=tipoContinuazione%>
        </select>
      </td>
    </tr>
    
    <tr id="trSelLista">
      <td class="l" colspan="4">
        <a href="Javascript:ListaTitoliInIstruttoria('LoadInserisciPenaComplessivaCumulo');">
          Seleziona Titolo In Continuazione <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
      </td>
    </tr>
    <tr id="trAssociazione">
      <td class="l" colspan="4">
          Deassocia Continuazione da titolo in istruttoria
          <a href="Javascript:EliminaAssociazione();" title="Elimina associazione"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
      </td>
    </tr>
    
    <tr>
      <td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
      <td class="L" >
          <input type="text" Title="Anno Sentenza" maxlength="4" size="4"
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>" value="" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" Title="Numero Sentenza" maxlength="6" size="6" 
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>" value="" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="l">Data Sentenza <font class="ob">(*)</font></td>
      <td class="l">
          <input type="text" Title="Giorno Data Sentenza"  maxlength="2" size="2"  
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>" value=""
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" Title="Mese Data Sentenza"  maxlength="2" size="2" 
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>" value="" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input type="text" Title="Anno Data Sentenza"  maxlength="4" size="4"   
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>" value=""
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    
    <tr>
      <td class="l">Autorità Sentenza <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <select Title="Autorità Sentenza" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>">
          <%=autoritaSentenza%>
        </select>
      </td>
    </tr>
    
    <tr>
      <td class="l">Luogo Sentenza <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <input Title="Luogo Sentenza" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>" value="" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciPenaComplessivaCumulo','<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>',,document.LoadInserisciPenaComplessivaCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>[document.LoadInserisciPenaComplessivaCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>.selectedIndex].value);">
          <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    
    <tr>
      <td class="l">Anno/Numero R.G.N.R.</td>
      <td class="L">
        <input type="text" Title="Anno R.G.N.R." maxlength="4" size="4"   
               name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>" value=""
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" Title="Numero R.G.N.R."  maxlength="6" size="6" 
               name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>" value=""
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>

      <td class="l">Anno/Numero Reg.Gen.</td>
      <td class="L">
        <input type="text" Title="Anno Reg.Gen." value=""   maxlength="4" size="4"
               name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" Title="Numero Reg.Gen." value=""  maxlength="6" size="6"
               name="<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      &nbsp;
        <select name="<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>">
          <%=tipoRegGen%>
        </select>
      </td>
    </tr>
    
    <%// Sezione con le etichette %>
      <tr>
        <td class="l">Anno/Numero Sentenza</td>
        <td class="L" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>_L"></font>&nbsp;/
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Data Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>_L"></font>-
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>_L"></font>-
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Autorità Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Luogo Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>_L"></font>
        </td>
      </tr>
      
      <tr>            
        <td class="l">Anno/Numero Re.Ge. PM</td>
        <td class="l">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>_L"></font>
          /
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>_L"></font>
        </td>
     
        <td class="l">Anno/Numero Reg.Gen.</td>
        <td class="L">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>_L"></font>&nbsp;
          /
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>_L"></font>
          &nbsp;
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>_L"></font>
        </td>
      </tr>    
    
    <tr>
      <td class="l" colspan=1>Motivo<br>Inserimento/Modifica<br>Continuazione</td>
      <td class="l" colspan=3><TextArea cols=80 rows=4 name="<%= ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD %>"></textarea></td>
    </tr>
  </table>
</div>
<%
  } // Chiude If
%>

<table>
  <tr>
    <td colspan=2>
      <br><input class="bottone" type="submit" name="INSERISCI" value="Conferma">
    </td>
  </tr>
</table>  
    
  
</FORM>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciPenaComplessivaCumulo");

  <% if( modalita.equals("I") ) { %>
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","lt=3000");
  
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","lt=3000");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>","numeric");  
  <% } %>
  
  
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>