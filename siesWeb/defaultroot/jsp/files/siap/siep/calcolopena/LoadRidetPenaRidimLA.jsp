<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.util.MinorMask"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%// NUOVO!! %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<% // 20/05/2014 - Nuova L.A - DL 146/2013 %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>

<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="lPosGiuModificata"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="PenaResidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="TIPO_COMPUTO_LA" scope="request" class="java.lang.String" />
<%// Oggetti motivo computo%>
<jsp:useBean id="oggetto"             scope="request" class="java.lang.String" />
<%// Pena in decorrenza %>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<%// Caricamento combo altra autorità %> 
<jsp:useBean id="tipoprovvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"             scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunti useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// form per l'inserimento del Provvedimento di Rideterminazione Pena 'Ridimesionamento LA'
// menu: 'Rideterminazione Pena - Provvedimenti del PM - Ridimesionamento LA'
//
// - Posizione giuridica
// - Pena Residua In espiazione/Da espiare
// -
// -
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>
<html>

<head>
  <title> [S.I.E.S.] - Ridimensionamento LA - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  
  function InserimentoManuale()
  {
    resetDati();
    document.RidimensionaLA.<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>.value="";
    
    // Disabilito le DIV dei dati selezionati dalla lista
    document.getElementById("datiSorvLA").style.display='none';
    document.getElementById("datiSorvLA_SPE").style.display='none';
    document.getElementById("datiSorvLA_INT").style.display='none';

    document.getElementById("partecomune").style.display='block'; 
    document.RidimensionaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked=true; 
    
    QualeLAConcede('2130')

    // Visualizza la DIV LA Ordinarie Manuali
    //document.getElementById("datiPeriodiLA").style.display='block';
  }
  
  function resetDati()
  {
    resetDatiProvvSIUS();
    resetDatiSelezionaLista();
    resetDatiManuali();
  }
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati selezionati dalla lista
  //==============================================================================
  function resetDatiProvvSIUS()
  {
    // Anche in dati del provvedimento SIUS
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value="";
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value="";
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value="";
    
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value="";
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.value="";
    
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.selectedIndex =0;

    document.RidimensionaLA.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>.value="";
    document.RidimensionaLA.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>.value="";

    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex =0;
    document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value="";
  }
  
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati selezionati dalla lista
  //==============================================================================
  function resetDatiSelezionaLista()
  {
    // DIV LA Ordinaria
    document.RidimensionaLA.giorniLA.value="";
    document.RidimensionaLA.NumGiorniLibanticipataSorv.value="";
    <% for (int s=0;s<6;s++) { %>
    document.RidimensionaLA.InizioLA<%=s%>.value="";
    document.RidimensionaLA.FineLA<%=s%>.value="";
    <% } %>   
    
    // DIV LA Speciale
    document.RidimensionaLA.giorniLA_SPE.value="";
    document.RidimensionaLA.NumGiorniLibanticipataSorv_SPE.value="";
    <% for (int s=0;s<6;s++) { %>
    document.RidimensionaLA.InizioLA_SPE<%=s%>.value="";
    document.RidimensionaLA.FineLA_SPE<%=s%>.value="";
    <% } %>    
    
    // DIV Integrazione LA
    document.RidimensionaLA.giorniLA_INT.value="";
    document.RidimensionaLA.NumGiorniLibanticipataSorv_INT.value="";
    <% for (int s=0;s<6;s++) { %>
    document.RidimensionaLA.InizioLA_INT<%=s%>.value="";
    document.RidimensionaLA.FineLA_INT<%=s%>.value="";
    <% } %>     
  }
  
  //==============================================================================
  // Resetta i campi della DIV di visualizzazione dei dati inseriti manualmente
  //==============================================================================
  function resetDatiManuali()
  {
    // LA Ordinaria
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA %>.value="";

    <% for (int k=0; k<6; k++) { %>
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO%>[<%=k%>].value="";

    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE%>[<%=k%>].value="";
    <% } %>
    
    // LA Speciale
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE %>.value="";
    <% for (int k=0; k<6; k++) { %>
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_SPE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_SPE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_SPE%>[<%=k%>].value="";

    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_SPE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_SPE%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_SPE%>[<%=k%>].value="";
    <% } %>
    
    // LA Integrazione
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>.value="";

    <% for (int k=0; k<6; k++) { %>
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_INT%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_INT%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_INT%>[<%=k%>].value="";

    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_INT%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_INT%>[<%=k%>].value="";
    document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_INT%>[<%=k%>].value="";
    <% } %>
  }
    
 /* 05/2014 NUOVA L.A.  DL 2013/146 - Inserimento Ulteriori Oggetti : L.A. SPECIALE (Cod 2131) e INTEGRAZIONE L.A. (Cod 2132) */
  //========================================================================================
  //  Abilitazione Iniziale dei radioButton per selezionare gli Oggetti L.A.  
  //========================================================================================
  function Inizia()
  {
  //alert("Inizia");
      node=document.getElementById("partecomune");
      node.style.display='block';  
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
        
        var valoreLA = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA %>.value;
        //if(valoreLA=="") valoreLA="0";
        
        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA %>.value = valoreLA;
  }
  
  //========================================================================================
  // ... Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. normale
  //========================================================================================
  function DisabilitaLA()
  {
    //alert("DisabilitaLA");
        var salvaggLA = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA %>.value;

        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA %>.value = salvaggLA;
        
        node=document.getElementById("datiPeriodiLA");
        node.style.display='none';
  }
//

  //========================================================================================
  // Abilito le DIV per la gestione dei periodi L.A. SPECIALE
  //========================================================================================
  function AbilitaLA_SPE()
  {
  //  alert("AbilitaLA_SPE");
        node=document.getElementById("datiPeriodiSPE");
        node.style.display='block';
        
        var valoreLS = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value;
        //if(valoreLS=="") valoreLS="0";
        
        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE %>.value = valoreLS;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e DISABILITO le DIV per la gestione dei periodi L.A. SPECIALE
  //========================================================================================
  function DisabilitaLA_SPE()
  {
    //alert("DisabilitaLA_SPE");
        var salvaggLS = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE %>.value;
        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>.value = salvaggLS;
        
        node=document.getElementById("datiPeriodiSPE");
        node.style.display='none';
  }
//    
    
  //========================================================================================
  // Abilito le DIV per la gestione dei periodi L.A. INTEGRAZIONE
  //========================================================================================
  function AbilitaLA_INT()
  {
    //alert("AbilitaLA_INT");
        node=document.getElementById("datiPeriodiINT");
        node.style.display='block';
        
        var valoreLI = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value;
        //if(valoreLI=="") valoreLI="0";
        
        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>.value = valoreLI;
  }
  
  //========================================================================================
  // Salvo gli eventuali gg inseriti e  DISABILITO le DIV per la gestione dei periodi L.A. INTEGRAZIONE
  //========================================================================================
  function DisabilitaLA_INT()
  {
    //alert("DisabilitaLA_INT");
        var salvaggLI = document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>.value;
        document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_INT %>.value = salvaggLI;
        
        node=document.getElementById("datiPeriodiINT");
        node.style.display='none';
  }
//  
  
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {
      // - Campo Totale giorni da scomputare obbligatorio
        var obbligatori;
        
      if(document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA%>.value == 0 && 
         document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE%>.value == 0  &&
         document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT%>.value == 0 )
      {
        if(document.RidimensionaLA.NumGiorniLibanticipataSorv.value == 0 && 
           document.RidimensionaLA.NumGiorniLibanticipataSorv_SPE.value == 0  &&
           document.RidimensionaLA.NumGiorniLibanticipataSorv_INT.value == 0 )
        {
            alert("Selezionare almeno un oggetto L.A. e digitare il Totale Giorni \n o selezionare un provvedimento della sorveglianza dalla lista ");
            return false; 
        }    
      }
  
//  LIBERAZIONE  ANTICIPATA
      var dateInserite=0;
        for (var T=0; T<6; T++)
        {
          if(document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO%>[T].value !=""   &&
            document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO%>[T].value !=""    &&
            document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO%>[T].value !=""  )
          {
              dateInserite++;
          }
          
          if(dateInserite!= 0)
              break;
        }

        if(dateInserite != 0 && document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA%>.value == 0)
        {
            DisabilitaLA_INT();
            DisabilitaLA_SPE();
            AbilitaLA();
            document.RidimensionaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[0].checked = true;
            alert('Inserire totale giorni di Liberazione Anticipata da Scomputare');
            document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA%>.focus();
            return false;
        }
//
//  LIBERAZIONE ANTICIPATA  SPECIALE
      var dateInserite=0;
        for (var Tspe = 0; Tspe < 6; Tspe++)
        {
          if(document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_SPE %>[Tspe].value !="" &&
             document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_SPE %>[Tspe].value !=""   &&
             document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_SPE %>[Tspe].value !="" )
          {
              dateInserite++;
          }
          
          if(dateInserite!= 0)
              break;
        }

        if(dateInserite != 0 && document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE %>.value == 0)
        {
            DisabilitaLA_INT();
            DisabilitaLA();
            AbilitaLA_SPE();
            document.RidimensionaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[1].checked = true;
            alert('Inserire totale giorni di Liberazione Anticipata Speciale da Scomputare');
            document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE %>.focus();
            return false;
        }
//
//  INTEGRAZIONE  LIBERAZIONE  ANTICIPATA
      var dateInserite=0;
        for (var Tint = 0; Tint < 6; Tint++)
        {
          if(   document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_INT %>[Tint].value !=""
             && document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_INT %>[Tint].value !=""
             && document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_INT %>[Tint].value !="")
          {
              dateInserite++;
          }
          
          if(dateInserite!= 0)
              break;
        }

        if(dateInserite != 0 && document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>.value == 0)
        {
            DisabilitaLA();
            DisabilitaLA_SPE();
            AbilitaLA_INT();
            document.RidimensionaLA.<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>[2].checked = true;
            alert('Inserire totale giorni di Integrazione Liberazione Anticipata da Scomputare');
            document.RidimensionaLA.<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>.focus();
            return false;
        }
//        
// ===============
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
  
        var data_to_verify = document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
  
        if (!ControllaData(data_to_verify) )
        {
            alert('Data di emissione non valida');
            document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
            return false;
        }     
      
      //=======================
      // Controllo Magistrato  
      //=======================
        if(document.RidimensionaLA.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Selezionare Magistrato firmatario");
          return false;
        }       

      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      // Data Ricezione Provv AA
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value;
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value;
  
        var data_to_verify = document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di ricezione Provvedimento Altra Autorità non valida');
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA.focus();
          return false;
        }
      
        // Data Emissione
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value;
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value.length==1)
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value='0'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value;
  
        var data_to_verify = document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value+'/'+document.RidimensionaLA.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value;
        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data di emissione Provvedimento Altra Autorità non valida');
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.focus();
          return false;
        }
      
      // Tipo provvedimento
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Tipo Provvedimento");
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.focus();
          return false;
        }
      
      // Autorità emittente 
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex==0)
        {        
          alert("Selezionare Autorità Emittente");
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.focus();
          return false;
        }
      
      // Sede 
        if (document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=="")
        {        
          alert("Selezionare Sede Autorità Emittente");
          document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.focus();
          return false;
        }
      
      return true;
    
    } // CHIUDE function verify()

// 20/05/2014 - Nuova L.A - DL 146/2013

  function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
     function ListaMagistrati(a_formname)
     {
       var desktop;
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
     }
   
    //==========================================================================
    function ListaLicenzeAnticipate(a_formname)
    {
      //alert("Lista"+a_formname);
      
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaLiberazioneAnticipata&flaglicenza=RidimensionamentoLA&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lPosizione.getFasSieIdFascicoloSiep()%>", "Lista_Licenze_Anticipate", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=700, height=500");
    }

/*    
   function hideDiv()
   {
   document.getElementById("datiSorv").style.display = 'none';
   document.getElementById("datiSorv").style.visibility = 'hidden';
   document.getElementById("datiPeriodiLA").style.display = 'block';
   document.getElementById("datiPeriodiLA").style.visibility = 'visible';
   }
   
   function hideDivPeriodi()
   {

   document.getElementById("datiPeriodiLA").style.visibility = 'hidden';
   document.getElementById("datiPeriodiLA").style.display = 'none';
  // document.getElementById("datiSorv").style.visibility = 'visible';
  // document.getElementById("datiSorv").style.display = 'block';

    }
  
   function sceglitipo()
   {
  alert("fine");
    }
*/

  </script>
</head>

<body class="corpo" onLoad="Javascript:Inizia();">

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="RidimensionaLA">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciRidimensionamentoLA">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
  <input type="hidden" name="<%=ICostantiLicenzaLibanticipata.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>_AA" value="">
  <input type="hidden" name="TipoOrd" value="altroUfficio">
  
  <input type="hidden" name="<%=ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA%>" value="<%=TIPO_COMPUTO_LA%>">
  

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena - Ridimensionamento LA</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua (attuale)
//==============================================================================
%>
<table>
    <tr><td><input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>"></td></tr>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
<%
      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
%>
        DETENUTO PER ALTRA CAUSA
<%
      }
      else
      {
        if(lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null)
        {
%>
          <%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
<%
        }
        else
        {
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
        }
      }
%>
      </font>
    </td>
  </tr>
<%
  if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
  {
     if( lAltraCausa.getIstitutoDetenzione()!= null )
     {
%>
       <tr>
         <td class="l">Detenuto presso </td>
         <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
       </tr>
<%
       if (lAltraCausa.getAltroLuogo()!=null)
       {
%>
          <tr>
            <td class="l">Altro Luogo </td >
            <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
<%
        }
      }
    }
    else if(lLuogoDetenzione.getIstitutoDetenzione()!= null)
    {
%>
      <tr>
        <td class="l">Detenuto presso </td>
        <td class="L" colspan=5>
          <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
          // if(lLuogoDetenzione.getDescrLuogo()!=null)
          // {
%>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
           // }
%>
        </td>
      </tr>
<%
    }
    else if(lLuogoDetenzione.getAltroLuogo()!= null)
    {
%>
        <tr>
           <td class="l">Detenuto presso Altro Luogo</td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getAltroLuogo()%></font>
          </td>
          </tr>
<%
    }
%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
   // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
    {
      if(lLuogoDetenzione.getIstitutoDetenzione() != null)
      {
%>
        <tr>
        <td class="l">Indirizzo</td>
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
        </td>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%> maxlength="6" size="6"--%>
        </tr>
<%
      }
    }
%>
<%
    if(PenaResidua.getIdPenaResidua() != null && ( (PenaResidua.getFlagErgastolo() == null) || (PenaResidua.getFlagErgastolo() != null && !PenaResidua.getFlagErgastolo().equals("S") && !PenaResidua.getFlagErgastolo().equals("D")) ) )
    {
      if ((PenaResidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (PenaResidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (PenaResidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
        <td class="l">Reclusione</td>
        <td class="l">
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaResidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaResidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniReclusione(),"0")%></font>
        </td>
<%
        if(PenaResidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
        {
%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
      }
%>
   </tr>
   <tr>
<%
    if ((PenaResidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (PenaResidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (PenaResidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(PenaResidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      <tr>
<%
       if (PenaResidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if (PenaResidua.getFlagErgastolo() != null)
       {
        if(PenaResidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(PenaResidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }
%>
<%
       if ( ((PenaResidua.getFlagErgastolo() == null) || (PenaResidua.getFlagErgastolo() != null && !PenaResidua.getFlagErgastolo().equals("S") && !PenaResidua.getFlagErgastolo().equals("D"))) && PenaResidua.getDataFine()!=null)
       {
          if(PenaResidua.getDataFine().equals(PenaResidua.getDataFinePresunta())){
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }
        else
        {
%>
          <td class="l">Data Fine Pena</td>
          <td class="lRosso" >
            <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
          </td>
<%
        }
      }
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(PenaResidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </tr>
</table>


<%
//==============================================================================
//                        Dati della Sorveglianza
//==============================================================================
%> 
  <table width="100%">
    <tr>
      <td colspan=4 class="titolo">Dati Altra Autorità</td>
    </tr>
    
    <tr>
      <input type="hidden" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>">
      <td class="l" colspan="2">
        <a href="Javascript:ListaLicenzeAnticipate('RidimensionaLA');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0></a>
        &nbsp;&nbsp;
        <a href="Javascript:InserimentoManuale();">
          Inserimento Manuale
        </a>
      </td>
    </tr>
    
    <tr>
    </tr>
    
    <tr>
      <td class="l">Data emissione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA"   maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>   
    
    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <select Title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA" >
          <%=tipoprovvedimento%>
        </select>
      </td>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        /
        <input Title="Numero Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>" type="text" size="6" maxlength="6">
      </td>
    </tr>    


	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      	<td class="l">
	        <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
		    <%
		    	if ("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) {
		    %>
	        	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE+"_AA", autorita)%>
	        <% } else { %>
		        <select Title="Autorità Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA">
		          <%=autorita%>
		        </select>
		    <% } %>
      	</td>

      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
        <input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('RidimensionaLA','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA',document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA[document.RidimensionaLA.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Data ricezione provvedimento</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  
        onFocus="javascript:textboxSelect(this)" 
        onkeypress="return TicTabNumField(this,event)" 
        onBlur="javascript:value=FillDM(value)"
        >
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>"   name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>_AA" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>_AA" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  
  <br>
<!--   // 20/05/2014 - Nuova L.A - DL 146/2013 -->
<%
//==============================================================================
//
//==============================================================================
%>
<div style="display:none" id="partecomune" > 
  <table width="100%">
    <tr>
      <td class="L">
        <font class="label" style="text-align: center; color:green; font-size: 10pt">
         Selezionare gli Oggetti L.A. di interesse (uno per volta) e inserire i rispettivi n.ro giorni/periodi
        </font>
      </td>
    </tr>
  </table> 
  
  <table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
      <td width="30%" class="l">
        <span id="LA" style="color=blu;font-weight:bold;"> Liberazione Anticipata  </span> 
        <input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>"  onclick="Javascript:return QualeLAConcede('2130');" CHECKED>
      </td>
      <td width="30%" class="l">
        <span id="LS" style="color=blu;font-weight:bold;"> Liberazione Anticipata Speciale </span> 
        <input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" onclick="Javascript:return QualeLAConcede('2131');">
      </td>
      <td width="30%" class="l">
        <span id="LI" style="color=blu;font-weight:bold;"> Integrazione Liberazione Anticipata </span> 
        <input value="" type="radio" name="<%=ICostantiLibertaAnticipata.CAMPO_RADIO_QUALE_LA_CONCEDE%>" onclick="Javascript:return QualeLAConcede('2132');">
      </td>
    </tr>
  </table>
</div>

<!-- le div "datiSorvLA", "datiSorvSPE", "datiSorvINT" sono sempre hidden TRANNE quando seleziono provvedimenti  dalla LISTA-->

<!--  "datiSorvLA"  Liberazione Anticipata  -->
<%
//==============================================================================
// DIV Visualizzate in caso di provvedimento della Sorveglianza selezionato
// dalla lista. I dati non sono modificabili
//==============================================================================
%>
<div style="display:none" id="datiSorvLA" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Liberazione Anticipata</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Liberazione Anticipata concessi</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA" size="4" value="" disabled>
        </font>
      </td> 
      <td class="l" width="45%">Totale giorni da scomputare (Liberazione Anticipata) (*)</td>
      <td class="L" width="5%">
        <font class="campo">
          <input Title="TotGiorni" name="NumGiorniLibanticipataSorv" value="" size="5" maxlength="4">
        </font>
      </td>
    </tr>
  </table>
  
 <table width=90%>
  <tr>   
    <%for (int s=0;s<3;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr>
  <tr>   
    <%for (int s=3;s<6;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr> 
 </table>
</div>

<!--  "datiSorvLA_SPE"  Liberazione Anticipata Speciale -->
<div style="display:none" id="datiSorvLA_SPE" >  
<table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Liberazione Anticipata Speciale</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Liberazione Anticipata Speciale concessi</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA_SPE" size="4" value="" disabled>
        </font>
      </td> 
      <td class="l" width="45%">Totale giorni da scomputare (Liberazione Anticipata Speciale) (*)</td>
        <td class="L" width="5%">
          <font class="campo">
            <input Title="TotGiorni" name="NumGiorniLibanticipataSorv_SPE"  value="" size="5" maxlength="4">
            </font>
      </td>
    </tr>
  </table>
  <table width=90%>
    <tr>   
    <%for (int s=0;s<3;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA_SPE<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA_SPE<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr>
  <tr>   
    <%for (int s=3;s<6;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA_SPE<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA_SPE<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr> 
 </table>
</div>

<!--  "datiSorvLA_INT"  Integrazione Liberazione Anticipata -->
<div style="display:none" id="datiSorvLA_INT" >  
  <table width=90%>
    <tr>
      <td colspan=4 class="titolo">Oggetto: Integrazione Liberazione Anticipata</td>
    </tr>
    <tr>
      <td class="l" width="35%">Giorni di Integrazione Liberazione Anticipata concessi</td>
      <td class="L" width="5%">
        <font class="campo"><input type="text" name="giorniLA_INT" size="4" value="" disabled>
        </font>
    </td> 
    <td class="l" width="45%">Totale giorni da scomputare (Integrazione Liberazione Anticipata) (*)</td>
      <td class="L" width="5%">
        <font class="campo">
          <input Title="TotGiorni" name="NumGiorniLibanticipataSorv_INT"  value="" size="5" maxlength="4">
          </font>
    </td>
    </tr>
 </table>
 <table width=90%>
  <tr>   
    <%for (int s=0;s<3;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA_INT<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA_INT<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr>
  <tr>   
    <%for (int s=3;s<6;s++) 
    {    %>
      <td class="l"><font class="label"><%=s+1 %>)</font></td>
        <td class="L" >
          <font class="campo">
          <input type="text" name="InizioLA_INT<%=s%>" size="12" value="" disabled>
          &nbsp; - &nbsp;
          <input type="text" name="FineLA_INT<%=s%>" size="12" value="" disabled>
          </font>
        </td>
    <%} %>
  </tr> 
 </table>
</div>

<% 
//==============================================================================
// Sezioni con i periodi e i giorni da computare in caso di compilazione 
// Manuale dei dati 
// CAMPI DATA DAL - AL   LIBERAZIONE ANTICIPATA
//==============================================================================
%>
<div style="display:block; position: relative;" id="datiPeriodiLA" >  

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Liberazione Anticipata da scomputare/Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++) { %>
  <tr>
    <% if (k==0) { %>
    <td class="l">
      Totale giorni Liberazione Anticipata da Scomputare<font class="ob">(*)</font>
      <input Title="Totale Giorni L.A." name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA %>" value="" size="5" maxlength="4">
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
    </tr>
  <% } %>
  </table>
</div>
<!--  END LIBERAZIONE ANTICIPATA -->

<!-- 
================================================================================
      Sezione DEI CAMPI DATA DAL - AL   LIBERAZIONE ANTICIPATA SPECIALE
================================================================================
-->
<div style="display:none; position: relative;" id="datiPeriodiSPE" >  

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Liberazione Anticipata Speciale da scomputare/Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++){ %>
  <tr>
    <% if (k==0){ %>
    <td class="l">
      Totale giorni Liberazione Anticipata Speciale da Scomputare<font class="ob">(*)</font>
      <input Title="Totale Giorni L.A. Speciale" name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE%>" value="" size="5" maxlength="4">
    </td>
    <%}else {%>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_SPE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
      &nbsp;&nbsp;
      Al
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_SPE%>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_SPE%>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <% } %>
</table>
</div>
<!--  END LIBERAZIONE ANTICIPATA SPECIALE-->

<!-- 
================================================================================
      Sezione DEI CAMPI DATA DAL - AL   INTEGRAZIONE LIBERAZIONE ANTICIPATA 
================================================================================
-->
<div style="display:none; position: relative;" id="datiPeriodiINT" >  

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Giorni Integrazione Liberazione Anticipata da scomputare/Periodi </td>
  </tr>
  <% for (int k=0; k<6; k++) {%>
  <tr>
    <% if (k==0){ %>
    <td class="l">
      Totale giorni Integrazione Liberazione Anticipata da Scomputare<font class="ob">(*)</font>
      <input Title="Totale Giorni Integrazione L.A." name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT %>" value="" size="5" maxlength="4">
    </td>
    <% } else { %>
    <td class="l"></td>
    <% } %>
    
    <td class="l">
      Dal
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
            &nbsp;&nbsp;
            Al
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_INT %>" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>>
      /
      <input type="text" name="<%=ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_INT %>" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <% } %>
</table>
</div>
<!--  END INTEGRAZIONE LIBERAZIONE ANTICIPATA -->


<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('RidimensionaLA','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="4"><textarea cols="100" rows="2" name="noteComputo"></textarea></td>
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" value="Conferma" name="bottConferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>

  <input type="HIDDEN" title="salva gg LA"     name="<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA %>"     value="">
  <input type="HIDDEN" title="salva gg LA SPE" name="<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_SPE %>" value="">
  <input type="HIDDEN" title="salva gg LA INT" name="<%=ICostantiCalcoloPena.CAMPO_SALVA_NUM_GIORNI_LA_INT %>" value="">
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("RidimensionaLA");

    // Data Emissione
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>