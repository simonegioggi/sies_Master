<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="oggettodefinizione" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoUfficioSIUS" scope="request" class="java.lang.String"/>

<%
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

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Definizione Procedimento - Pena Espiata</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">

    function Verify()
    {
        //DATA EMISSIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();

          return false;
        }
        
        //DATA RICEZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.focus();

          return false;
        }
        
        //DATA DEFINIZIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data definizione non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.selectedIndex].value == '-')
        {
          alert("Il Campo Tipo Provvedimento è obbligatorio");
          return false;
        }

        if (document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex].value == '-')
        {
          alert("Il Campo Oggetto definizione è obbligatorio");
          return false;
        }
        
        if(document.f.<%= ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.f.<%= ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        return true;
    }

  	//Funzione utile per impostare la data corrente.
	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna) {    
		day=dataOdierna.substring(0,2);
		month=dataOdierna.substring(3,5);
		year=dataOdierna.substring(6,10);
	    document.getElementsByName(campo_giorno).item(0).value = day;
	    document.getElementsByName(campo_mese).item(0).value = month;
	    document.getElementsByName(campo_anno).item(0).value = year;      
	}
    
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaMagistrati(a_formname)
    {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function disabilitaCampi(){
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.disabled=true;
        
        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.disabled=true;
        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.disabled=true;
        
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>.disabled=true;
        document.f.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>.disabled=true;
        
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=true;
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=true;
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.disabled=true;

        document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=true;
        document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.disabled=true;
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=true;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.disabled=true;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.disabled=true;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.disabled=true;  

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.disabled=true;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.disabled=true;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.disabled=true;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.disabled=true;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.disabled=true;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.disabled=true;

        
    }
      
    function abilitaCampi(){
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.disabled=false;
        
        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.disabled=false;
        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.disabled=false;
        
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>.disabled=false;
        document.f.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>.disabled=false;
        
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.disabled=false;
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.disabled=false;
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.disabled=false;

        document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=false;
        document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.disabled=false;
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=false;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.disabled=false;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.disabled=false;

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.disabled=false;  

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.disabled=false;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.disabled=false;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.disabled=false;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.disabled=false;
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.disabled=false;
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.disabled=false;
        
    }

    function resettaCampi(){
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.selectedIndex=0;

        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.value="";
        document.f.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.value="";
        
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>.value="";
        document.f.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>.value="";
        
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex=0;
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.value="";
        document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.selectedIndex=0;

        document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value="";
        document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value="";
        document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value="";

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value="";

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value="";

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value=""; 

        document.f.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value="";
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value="";
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.value="";
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.value="";
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.value="";
        
        document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.value="";
        
    	//alert ("fine resettaCampi");
      
      }

    function inserimentoManuale(){
        document.f.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value="";
        resettaCampi();
        abilitaCampi();
        EsiDurata("");
    }
    
    function CtrId()
    {
//      var IdSca =document.f.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value;
//      if (document.f.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value =='')
//      { 
//        alert('Selezionare almeno UN Provvedimento dalla lista SIUS'); 
//        return false; 
//      }
//      else
//      {
		abilitaCampi();
        document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.penapecuniaria.action.ActInserisciAnnotazioneProvvedimento";
//      }
      
      return true;    
    } 

    function EsiDurata(valueSel)
    {
      var nodeLibDur  = document.getElementById('rec_0156');
      var nodeLavDur  = document.getElementById('rec_0157');
      var nodeDifDur  = document.getElementById('rec_0158');
      var nodeRateDur1 = document.getElementById('rec_0159_1');
      var nodeRateDur2 = document.getElementById('rec_0159_2');
      
      if(valueSel == '0156') 
      {
        nodeLibDur.style.display='block';
        
        disabilitaLavoroSostitutivo();
        disabilitaDifferimento();
        disabilitaRateizzazione();
      }
      else if(valueSel == '0157')
      {
        nodeLavDur.style.display='block';
        
        disabilitaLibertaControllata();
        disabilitaDifferimento();
        disabilitaRateizzazione();
      }
      else if(valueSel == '0158')
      {
        nodeDifDur.style.display='block';
        
        disabilitaLibertaControllata();
        disabilitaLavoroSostitutivo();
        disabilitaRateizzazione();
      }
      else if(valueSel == '0159')
      {
        nodeRateDur1.style.display='block';
        nodeRateDur2.style.display='block';
         
        disabilitaLibertaControllata();
        disabilitaLavoroSostitutivo();
        disabilitaDifferimento();
      }
      else
      {
        disabilitaLibertaControllata();
        disabilitaLavoroSostitutivo();
        disabilitaDifferimento();
        disabilitaRateizzazione();
      }
    } // End function EsiDurata 
    
    function disabilitaLibertaControllata (){
      document.getElementById('rec_0156').style.display='none';
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.value="";
    }
    
    function disabilitaLavoroSostitutivo(){
      document.getElementById('rec_0157').style.display='none';
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value="";
    }
    
    function disabilitaDifferimento(){
      document.getElementById('rec_0158').style.display='none';
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value="";  
    }
    
    function disabilitaRateizzazione(){
      document.getElementById('rec_0159_1').style.display='none';
      document.getElementById('rec_0159_2').style.display='none';
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value="";
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value="";
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.value="";
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.value="";
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.value="";
      
      document.f.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.value="";

    }

    function ListaDocumentiSiusCPPec(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActListaDocumentiSiusCPPecEPS&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>+", "Lista_Provvedimenti", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

  </script>
  </head>
  <body class="corpo" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Provvedimento Sorveglianza</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciProvvAltraAutorita">
    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }
%>

  <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }


       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {

      if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                </td>
<%            }
        }
      }
}
%>
</tr>


  </table>
  <br>
  <table width="90%">
    <tr>
      <td colspan=4 class="titolo">Dati Definizione Procedimento</td>
    </tr>
    <tr>
      <td class="l">Data Definizione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
   		<a href="Javascript:impostaDataOdierna('<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE %>','<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
     		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
   		</a>
      </td>
      <td class="l">Data ricezione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Ricezione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
   		<a href="Javascript:impostaDataOdierna('<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE %>','<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE %>','<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
     		<img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
   		</a>
      </td>
    </tr>

    <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSiusCPPec('f');">
          Seleziona provvedimenti dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
        <a href="Javascript:inserimentoManuale();">
          Inserimento Manuale
        </a>
        
      </td>
    </tr>   

    <tr>
      <td class="l">Data emissione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
	    <td class="l">Anno / Numero SIUS</td>
	    <td class="l">
	      <input type="text" size="4" maxlength="4" Title="Anno Fascicolo Sius" 
	             name="<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" 
	             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
	             >
	      /
	      <input type="text" size="6" maxlength="6" Title="Numero Sius" 
	             name="<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>"  
	             onkeypress="return TicTabNumField(this,event)"
	             >
	    </td>
	    <td class="l"> Anno / Numero Provvedimento </td>
	    <td class="l">
	      <input Title="Anno Provvedimento" name="<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4" 
	             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
	             >
	      /
	      <input Title="Numero Provvedimento" name="<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>" type="text" size="6" maxlength="6" 
	             onkeypress="return TicTabNumField(this,event)"
	             >
	    </td>
    </tr>

    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
        <td class="l" colspan=3>
          <select Title="Tipo Provvedimento" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>">
            <%=tipoprovvedimento%>
          </select>
        </td>
    </tr>


    <tr>
      <td class="l" >Autorità Emittente <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Autorità Emittente" name="<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
        	<option value = "-"  />-
        	<%=comboTipoUfficioSIUS%>
      	  </select>
       </td>
       <input type="HIDDEN" title="Provvedimento" value="0003" name="<%=ICostantiArchiviazione.CAMPO_COD_PROVVEDIMENTO%>" >

      <td class="l" colspan=2>Sede  &nbsp;
          <input title="Sede Autorita"  type="text" name="<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr>

    <tr>
      <td class="l">Oggetto Definizione <font class=ob>(*)</font></td>
        <td class="l" colspan=3>
          <select Title="Oggetto Definizione" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>"  onChange="javascript:EsiDurata(this.value)">
            <%=oggettodefinizione%>
          </select>
        </td>
    </tr>
<%//==========================================================================%>
  <tr style="display:none" id="rec_0156">
    <td class="L" colspan="1"><font class="label">Giorni Libertà Controllata</font><font class=ob>(*)</font></td>
    <td class="L" colspan="3">
    Anni <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
    Mesi <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
    Giorni <input Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB %>" size="3"  maxlength="4" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr> 
  
  <tr style="display:none" id="rec_0157">
    <td class="L" colspan="1"><font class="label">Lavoro Sostitutivo Applicato</font><font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      Anni <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
      Mesi <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
      Giorni <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV %>" size="3"  maxlength="4" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr style="display:none" id="rec_0158">
    <td class="L" colspan="1"><font class="label">Durata Differimento</font><font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      Anni <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
      Mesi <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
      Giorni <input type="text" Title="Esito" name="<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF %>" size="3"  maxlength="4" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>

  <tr style="display:none" id="rec_0159_1">
    <td class="L" colspan="4" >
      Numero Rate <font class=ob>(*)</font>&nbsp;<input type="text" Title="Numero Rate" name="<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE %>" size="4"  maxlength="4" onkeypress="return TicTabNumField(this,event)">
      Valore Rata <font class=ob>(*)</font>&nbsp;<input type="text" Title="Valore Rata" name="<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I %>" size="4"  maxlength="4" onkeypress="return TicTabNumField(this,event)" style="text-align: right;">
                                           ,
                                           <input type="text" Title="Valore Rata" name="<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)" >
      Valore Ultima Rata <input type="text" Title="Valore Ultima Rata" name="<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I %>" size="4"  maxlength="4" onkeypress="return TicTabNumField(this,event)" style="text-align: right;">
                         ,
                         <input type="text" Title="Valore Ultima Rata" name="<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D %>" size="2"  maxlength="2" onkeypress="return TicTabNumField(this,event)">
    </td> 
  </tr>
  <tr style="display:none" id="rec_0159_2">
    <td class="L" colspan="4">
      <font class="label">Pagamento Prima Rata entro il</font>
        <input type="text"  Title="Giorno Data IniPagamento" size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA %>" <%=IWebConstants.UTIL_DATA%>> -
        <input type="text"  Title="Mese Data IniPagamento"   size="2" maxlength="2" name="<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA %>" <%=IWebConstants.UTIL_DATA%>> -
        <input type="text"  Title="Anno Data IniPagamento"   size="4" maxlength="4" name="<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
      <font class="label">Oppure entro</font>
        <input type="text" Title="Giorni" name="<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA %>" size="4"  maxlength="4" onkeypress="return TicTabNumField(this,event)">
      <font class="label">giorni dalla data di Notifica</font>
    </td>
  </tr>
<%//==========================================================================%>
    
    <tr>
      <td class="l"width="18%" >Magistrato Firmatario <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr>
       <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L" colspan=3>
          <textarea title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=4 ></textarea>
       </td>
     </tr>
</table>
<table>
    <tr>
      <td class="lNoBord">
      <td class="lNoBord" colspan="2">
      	<input type="HIDDEN" name="<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>">
      	<input type="HIDDEN" name="<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>">
      	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      	<input type="submit"  class="bottone"  name="I" value="Conferma" onClick="javascript:return CtrId();"> 
      </td>
    </tr>

</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");

//data emissione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","maxlen=4","La lunghezza massima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza minima per l'Anno emissione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");

//data ricezione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","maxlen=4","La lunghezza massima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","minlen=4","La lunghezza minima per l'Anno ricezione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>","numeric");

//data definizione
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno definizione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l'Anno definizione è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>