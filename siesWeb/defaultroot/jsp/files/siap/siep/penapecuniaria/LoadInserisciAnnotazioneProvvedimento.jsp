<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneRichiestaConvModel"%>

<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel" />

<jsp:useBean id="ufficiocompetente"    scope="request" class="java.lang.String" />


<jsp:useBean id="comboTipoProvvSorv"   scope="request" class="java.lang.String" />
<jsp:useBean id="comboTipoUfficioSIUS" scope="request" class="java.lang.String" />
<jsp:useBean id="comboOggettoProvv"    scope="request" class="java.lang.String" />
<jsp:useBean id="comboEsitoProvv"      scope="request" class="java.lang.String" />

<jsp:useBean id="lScaSanzRC"  scope="request" class="java.util.Vector" />
<jsp:useBean id="lCodTipPro"  scope="request" class="java.util.Vector" />


<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  if (lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();
  
  Iterator itx = lScaSanzRC.iterator();
  int lSizeScaSan = 0;
  while ( itx.hasNext())
  {
	  ScambioSanzioneRichiestaConvModel lScaRic = (ScambioSanzioneRichiestaConvModel)itx.next();
	  if (lScaRic.getScambioSanzione().getCodTipoSanzione().compareTo("2470")==0  ||  
		  lScaRic.getScambioSanzione().getCodTipoSanzione().compareTo("2471")==0 )	{
			  lSizeScaSan = 1;
	  }
  }  

%>

<html>
<head>
<title>[S.I.E.S.] - ANNOTAZIONE PROVVEDIMENTI DECISIONI SORVEGLIANZA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript">
  //============================================================================
  // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
  //============================================================================
  function avvio()
  {
	  radio();
	  var sizeScaSan = <%=lSizeScaSan%>;
	  if (sizeScaSan > 0)
		  inserimentoAutomatico();
  }
  
  function radio()
  {
    var noderadio =document.getElementById('noderadio');
    if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_FLAG_PIU_MENO%>[0].checked)
      {
      noderadio.style.display='block';
    }else{
      noderadio.style.display='none'; 
    }
  }
  
  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  
  function ListaComuniUfficio(a_formname,a_fieldname)
  {
    codTipoUfficio = document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.value;

    if (codTipoUfficio=='-'){
      alert("Selezionare il tipo di ufficio emittente");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
    }
    else {
      desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  }
    
  //============================================
  //
  //============================================
  function ListaTDS_UDS(a_formname,a_fieldname)
  {
     var left = (screen.width/2)-( 370 /2);
     var top  = (screen.height/2)-( 500 /2);
     
     var valore = document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.value;

     if ( valore == "UDS")  {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:"
                           , "Ricerca_UDS"
                           , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500, top="+top+", left="+left+"");
     }
     else if (valore == "TDS") {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:"
                           , "Ricerca_TDS"
                           , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500, top="+top+", left="+left+"");
     }
     else {
       alert("Selezionare il tipo di ufficio emittente");
     }
  }   

  function disabilitaCampi(){
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>.disabled=true;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=true;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.disabled=true;

    //==============================
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.disabled=true;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.disabled=true;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.disabled=true;  

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.disabled=true;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.disabled=true;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.disabled=true;

    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NOTE_ANN%>.disabled=true;

  }
  
  function abilitaCampi(){
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>.disabled=false;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=false;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.disabled=false;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.disabled=false;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.disabled=false;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.disabled=false;  

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.disabled=false;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.disabled=false;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NOTE_ANN%>.disabled=false;

  }
  
  function resettaCampi(){
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>.selectedIndex=0;
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex=0;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>.value="";

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.value="";

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.selectedIndex=0;
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.selectedIndex=0;

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.value="";

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value="";

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value=""; 

    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_NOTE_ANN%>.value="";
  
  }
  
  function inserimentoManuale(){
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value="";
    resettaCampi();
    abilitaCampi();
    EsiDurata("");
  }
  
  // 01/12/2015 Solo inserimento automatico
  function inserimentoAutomatico(){
	    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value="";
	    resettaCampi();
	    disabilitaCampi();
	    EsiDurata("");
  }

  function ListaDocumentiSiusCPPec(a_formname)
  {
    var IdF = <%=lFascicoloAssociato.getIdFascicoloSiep()%>
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penapecuniaria.action.ActListaDocumentiSiusCPPec&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=yes, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500"); 
  }
  
  function CtrId()
  {
//    var IdSca =document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value;
//    if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value =='')
//    { 
//      alert('Selezionare almeno UN Provvedimento dalla lista SIUS'); 
//      return false; 
//    }
//    else
//    {
      document.LoadInserisciAnnotazioneProvvedimento.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.penapecuniaria.action.ActInserisciAnnotazioneProvvedimento";
//    }
    
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
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.value="";
  }
  
  function disabilitaLavoroSostitutivo(){
    document.getElementById('rec_0157').style.display='none';
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value="";
  }
  
  function disabilitaDifferimento(){
    document.getElementById('rec_0158').style.display='none';
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value="";  
  }
  
  function disabilitaRateizzazione(){
    document.getElementById('rec_0159_1').style.display='none';
    document.getElementById('rec_0159_2').style.display='none';
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.value="";
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.value="";
    
    document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.value="";

  }
  
  //==============================
  //
  //==============================
  function Verify() 
  {   
    //=============================================================
    // controllo correttezza campo 'Data Ricezione Provvedimento' 
    //=============================================================
    var data_to_verify = document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+ 
                         document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+ 
                         document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value; 
    
    if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
      alert('Data Ricezione non corretta'); 
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus(); 
      return false; 
    } 
    
    //==========================================================================
    if(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>.value=="-")
    {
      alert("Selezionare il Tipo Provvedimento");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>.focus();
      return false;
    }     
    
    if(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.value=="-")
    {
      alert("Selezionare l'Ufficio Emittente");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
      return false;
    }
        
    if(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>.value=="")
    {
      alert("La Sede dell'Ufficio Emittente è obbligatoria");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>.focus();
      return false;
    }    
    
    var data_to_verify = document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+ 
                         document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+ 
                         document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.value; 
    
    if (!ControllaDataPassaVuota(data_to_verify)){ 
      alert('Data Emissione Provvedimento non corretta'); 
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus(); 
      return false; 
    } 
    
    
    if(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.value=="-")
    {
      alert("Selezionare l'Oggetto Provvedimento");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>.focus();
      return false;
    }
    
    if(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.value=="-")
    {
      alert("Selezionare l'Esito");
      document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.focus();
      return false;
    }
    else if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.value=="0156"){
      //Dispone conversione in libertà controllata
      if (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB%>.value==""
         )
      {
        alert("Indicare la durata della libertà controllata");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB%>.focus();
        return false;
      }
    }
    else if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.value=="0157"){
      //Dispone conversione in lavoro sostitutivo
      if (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV%>.value==""
         )
      {
        alert("Indicare la durata della lavoro sostitutivo");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV%>.focus();
        return false;
      }
    }
    else if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.value=="0158"){
      //Differisce la conversione
      if (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF%>.value==""
          && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value==""
         )
      {
        alert("Indicare la durata del Differimento");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF%>.focus();
        return false;
      }
    }
    else if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>.value=="0159"){
      //Rateizza pagamento
      if (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value.length==0
          || parseInt(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.value)==0
         )
      {
        alert("Indicare il numero di rate");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE%>.focus();
        return false;
      }
      
      if (   (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value.length==0
              || parseInt(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.value)==0
             )
          && (   document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value.length==0
              || parseInt(document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D%>.value)==0
             )
         )
      {
        alert("Indicare l'importo delle rate");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I%>.focus();
        return false;
      } 
      
      var data_to_verify = document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.value+'/'+ 
                           document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA%>.value+'/'+ 
                           document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA%>.value; 
      
      if (!ControllaDataPassaVuota(data_to_verify) ){ 
        alert("Data Pagamento Prima Rata non corretta");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.focus();
        return false;
      }
      
      if (data_to_verify=='//' && document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA%>.value=="") {
        alert("Indicare i termini di pagamento");
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA%>.focus();
        return false;
      }
    }
    
    
    
    //==========================================================================
    
    
    if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_FLAG_PIU_MENO%>[0].checked)
    {   
      //=============================================================
      // controllo obbligatorietà campo 'ufficio competente' 
      //=============================================================
      if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.value =='-'){ 
        alert('Il campo ufficio competente è obbligatorio per inviare la comunicazione'); 
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>.focus();
        return false; 
      }
          
      //=============================================================
      // controllo obbligatorietà campo 'sede ufficio competente' 
      //=============================================================
      if (document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.value ==''){ 
        alert('Il campo sede ufficio competente è obbligatorio per inviare la comunicazione'); 
        document.LoadInserisciAnnotazioneProvvedimento.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.focus();
        return false; 
      }
    }
    
    return true;
  } // Chiude Verify  
  
  
  
  </script>
  
  <style>
  .readonly{
    background-color: transparent;
    border: solid 1px #8FBFC5;
    text-transform : uppercase;
    color : Blue;
    font-family: 'Tahoma';
    font-size: 12px;
    font-weight: bold;
    text-align:left;  
    padding: 1px 1px 1px 1px; 
    user-modify: read-only;   
  }
  </style>
  
</head>  

<body class="corpo" onLoad="javascript:avvio();">
 <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAnnotazioneProvvedimento">
 
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      </a>
    </td>
    
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">ANNOTAZIONE PROVVEDIMENTI DECISIONI SORVEGLIANZA</font>
    </td>
  </tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
<br>

<table>
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="L" colspan="1"><font class="campo"> <%
      if (lFascicoloAssociato.getFlagAltraCausa() != null
      && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
        DETENUTO PER ALTRA CAUSA 
      <%} else { %> 
        <%=lPosizione.getDescrPosizioneGiuridica()%> <%
      }%> </font>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  
  <tr>
    <td class="Titolo" colspan="10">Pena pecuniaria da convertire</td>
  </tr>
  <tr>
    <td class="l" colspan="1">Multa: Importo</td>
    <td class="l" colspan="1"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta())%></font>&nbsp;</td>
    <td class="l">Data Prescrizione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    <% if ("S".equals(richiestaconversione.getFlagImprescrittibileMulta())) { %>
    <td class="l">Imprescrittibile</td>
    <% } %>
  </tr>
  <tr>
    <td class="l" colspan="1">Ammenda: Importo</td>
    <td class="l" colspan="1"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda())%></font>&nbsp;</td>
    <td class="l">Data Prescrizione</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    <% if ("S".equals(richiestaconversione.getFlagImprescrittibileAmmenda())) { %>
    <td class="l">Imprescrittibile</td>
    <% } %>
  </tr>
</table>

<table>
  <tr>
    <td class="l" colspan="1">Data Emissione</td>
    <td class="L" colspan="1">
      <input type="text" size="2" maxlength="2"
             title="Giorno Data Ricezione" 
             value="<%=DateUtils.getSysDate("dd")%>" 
             name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI %>"
             <%=IWebConstants.UTIL_DATA%> > / 
      <input type="text" size="2" maxlength="2"
             title="Mese Data Ricezione" 
             value="<%=DateUtils.getSysDate("MM")%>"
             name="<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI %>"
             <%=IWebConstants.UTIL_DATA%>> / 
      <input type="text" size="4" maxlength="4"
             title="Anno Data Ricezione" 
             value="<%=DateUtils.getSysDate("yyyy")%>"
             name="<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI %>"
             <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>

<table width="90%">
  <tr>
    <td class="Titolo" colspan="4">Dati Provvedimento Uffici di Sorveglianza</td>
  </tr>
  <tr>
    <td class="l" colspan="4">
      <a href="Javascript:ListaDocumentiSiusCPPec('LoadInserisciAnnotazioneProvvedimento');">
        Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0> </a>
<%	if(lSizeScaSan==0) {
%>
      <a href="Javascript:inserimentoManuale();" >
          Inserimento Manuale
        </a>
<%}%>
      <!--a href="Javascript:abilitaCampi();">Abilita</a>
      <a href="Javascript:disabilitaCampi();">Disabilita</a>
      <a href="Javascript:resettaCampi();">Resetta</a-->
    </td>
  </tr>
  
  <tr>
    <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select  Title="Tipo Provvedimento" name="<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE%>">
        <%=comboTipoProvvSorv%>
      </select>
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
      <input Title="Anno Provvedimento" name="<%=ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             >
      /
      <input Title="Numero Provvedimento" name="<%=ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" 
             onkeypress="return TicTabNumField(this,event)"
             >
    </td>
  </tr> 
  
  <tr>
    <td class="l">Ufficio Emittente <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select  Title="Ufficio Emittente" name="<%=ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE%>">
        <option value = "-"  />-
        <%=comboTipoUfficioSIUS%>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <input type="text" name="<%= ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE %>" size=35 Title="Luogo Ufficio Sorveglianza" >
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--a href="Javascript:ListaTDS_UDS('LoadInserisciAnnotazioneProvvedimento','<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>');"--%>
      <a href="Javascript:ListaComuniUfficio('LoadInserisciAnnotazioneProvvedimento','<%=ICostantiScambioSanzione.CAMPO_COD_SEDE_AUTORITA_EMITTENTE%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
    
  <tr>
    <td class="l" colspan="1" >Data Emissione Provvedimento </td>
    <td class="L" colspan="3">
      <font class="campo">
        <input type="text" Title = "Giorno Data Emissione" size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>> -
        <input type="text" Title = "Mese Data Emissione"   size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE %>"   <%=IWebConstants.UTIL_DATA%>> -
        <input type="text" Title = "Anno Data Emissione"   size="4" maxlength="4" name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE %>"   <%=IWebConstants.UTIL_DATA_ANNO%>>
      </font>
    </td>
  </tr>
  
  <tr>
    <td class="l" colspan="1">Oggetto Provvedimento <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select Title="Codice Motivo"  name="<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>" >
        <option value = "-"  />-
        <%=comboOggettoProvv%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l" colspan="1">Esito <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select Title="Codice Motivo" name="<%=ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE%>" onChange="javascript:EsiDurata(this.value)">
        <option value = "-"  />-
        <%=comboEsitoProvv%>
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
    <td  class="l" colspan="1">Note</td>
    <td  class="L" colspan="3">
      <TEXTAREA Title="Note scambio" name="<%=ICostantiScambioSanzione.CAMPO_NOTE_ANN %>" cols="80" rows="2"></textarea>
    </td>
  </tr>
</table>



<table>
  <tr>
    <td class="l" colspan="2">Invio Comunicazioni</td>
    <td class="l" colspan="8">
      <input type="radio" value="0"  name="<%=ICostantiEvento.CAMPO_FLAG_PIU_MENO%>" CHECKED onClick="javascript:radio();">SI
      <input type="radio" value="1"   name="<%=ICostantiEvento.CAMPO_FLAG_PIU_MENO%>" onClick="javascript:radio();">NO
    </td>
  </tr>
  
  <tr>
    <td colspan="10">
      <div id="noderadio" style="width: 100%; display:block">
        <table width="100%">
          <tr>
            <td class="l" colspan="2">Ufficio Competente<font class=ob>(*)</font></td>
            <td class="L" colspan="8">
              <select Title="Ufficio Competente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO%>">
              <%=ufficiocompetente%>
              </select>
            </td>
          </tr>
          <tr>  
            <td class="l" colspan="2">di<font class=ob>(*)</font></td>
            <td class="L" colspan="8">
              <input type="text" Title="Sede" value=""              
                     name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>"
                     maxlength="35" size="35"> 
                <a href="Javascript:ListaComuni('LoadInserisciAnnotazioneProvvedimento','<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>');">
                   <img src="/images/filefolder.gif" border=0></a>
            </td>
          </tr>
          <tr>
            <td class="l" colspan="2">Altro destinatario</td>
            <td class="l" colspan="8">
              <input type="text" name="<%=ICostantiNotifica.CAMPO_NOTE %>" size="80">
            </td>
          </tr>
        </table>
      </div>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>

  <tr>
    <td class="lNoBord" colspan="2">
      <input type=hidden name="<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value=""> 
      <input type="submit"  class="bottone"  name="conferma" value="Conferma" onClick="javascript:return CtrId();"> 
  </td>
  </tr>
</table>
 </FORM>  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciAnnotazioneProvvedimento");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>
</html>