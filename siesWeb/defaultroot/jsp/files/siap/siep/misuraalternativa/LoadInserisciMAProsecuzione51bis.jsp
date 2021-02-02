<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="tipoMisura"           scope="request" class="java.lang.String"/>
<%
// isFaseOrdinanza = S se si sta inserendo il provvedimento SIUS
//                   N se si sta inserendo il provvedimento SIEP
%>
<jsp:useBean id="isFaseOrdinanza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="isConCumulo"      scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"        scope="request" class="java.lang.String"/>
<% // Pena residua rideterminata %>
<jsp:useBean id="nuovapenaresidua"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<% 
// Dopo inserimento Ordinanza di Prosecuzione 
// misuraalternativa = MA di concessione delle Prosecuzione (MDS o TDS)
// UfficioEmittente  = ufficio (MDS/TDS)
%>
<jsp:useBean id="misuraalternativa"    scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="UfficioEmittente"     scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="comboTipoMisuraAT" scope="request" class="java.lang.String"/>
<% //Combo per registrare l'ordinanza di prosecuzione %>
<jsp:useBean id="comboTipoUfficioSIUS" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvvSorv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"          scope="request" class="java.lang.String"/>
<% // Destinatari %>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutoritaC"      scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

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

<%
Collection oggettiTDS = (Collection) request.getAttribute("oggettiTDS");
Collection oggettiMDS = (Collection) request.getAttribute("oggettiMDS");

String strOggettiDecisione = "-;-;-#"; // nel formato TDS;cod1;desc1#TDS;cod2;desc2;MDS;cod1;desc1....

if (oggettiTDS!=null){
   Iterator itx = oggettiTDS.iterator();
   strOggettiDecisione+="TDS;-;-#";
   strOggettiDecisione+="TDSM;-;-#";
   while(itx.hasNext())
   {
      DecodificheModel lDecMod = (DecodificheModel)itx.next();

      strOggettiDecisione += "TDS;";
      strOggettiDecisione += lDecMod.getCode()+";";
      strOggettiDecisione += lDecMod.getDescription()+"#";
      strOggettiDecisione += "TDSM;";
      strOggettiDecisione += lDecMod.getCode()+";";
      strOggettiDecisione += lDecMod.getDescription()+"#";
   }
}

if (oggettiMDS!=null){
   Iterator itx = oggettiMDS.iterator();
   strOggettiDecisione+="UDS;-;-#";
   strOggettiDecisione+="UDSM;-;-#";
   while(itx.hasNext())
   {
      DecodificheModel lDecMod = (DecodificheModel)itx.next();

      strOggettiDecisione += "UDS;";
      strOggettiDecisione += lDecMod.getCode()+";";
      strOggettiDecisione += lDecMod.getDescription()+"#";
      strOggettiDecisione += "UDSM;";
      strOggettiDecisione += lDecMod.getCode()+";";
      strOggettiDecisione += lDecMod.getDescription()+"#";
   }
}
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
  var desktop;
  var strOggettiDecisione = "<%=strOggettiDecisione%>";
  
  function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
    // valueTextStr = stringa nel formato richiesto
    // sep1 = separatore interno alla coppia di valori
    // sep2 = separatore tra coppie
    // filtro = valore su cui fare il test
    // selField = oggetto combo da caricare


    clearDropDown(selField);

    var aPairs = valueTextStr.split(sep2);

    if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
    {
      aPairs[aPairs.length - 1] = null;
      aPairs.length--;
    }


    for (var i=0; i < aPairs.length; i++)
    {
      aValueText = aPairs[i].split(sep1);
      if (filtro=='null' || filtro==aValueText[0])
      {
        oItem = new Option;
        oItem.value = aValueText[1];
        oItem.text = aValueText[2];
        selField.options[selField.options.length] = oItem;
      }
    }

    selField.options.selectedIndex = 0;
  }

  function clearDropDown (selField)
  {
    while (selField.options.length > 0)
    selField.options[0] = null;
  }
  
  function caricaComboOggetto()
  {
    caricaCombo(strOggettiDecisione,';','#',document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value
                                           ,document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>);
  }
  //============================================================================
  function changeTipoUfficioSorv(){
    caricaComboOggetto();
    pulisciComune();
    pulisciId();
    
    // Visualizza sezioni opportune
    // TDS - Scarcerato da scarcerare solo se !isLibero()
    //     - Luogo della prova
    // MDS - Data Decorrenza Misura (senza Cumulo) (obbligatoria)
    //     - Decorrenza Pena (read only) + Data Decorrenza Misura (non obbligatoria)
    var tipoUfficio=document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
    
    if (tipoUfficio=='TDS' || tipoUfficio=='TDSM'){
      // Visualizzo Sezione Da scarcerare/scarcerato
      document.getElementById("idSezioneScarcerare").style.display = "block";      

      if (document.LoadInserisciMisuraAlternativa.tipomisura.value == "<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>"){
        document.getElementById("idSezioneQuantita").style.display = "block";
        document.getElementById("idSezioneDataScadenza").style.display = "block";
      }
      else {
        document.getElementById("idSezioneQuantita").style.display = "none";
        document.getElementById("idSezioneDataScadenza").style.display = "none";
      }
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value="";
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value="";

      
      <% if (!tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) { %>
      document.getElementById("idLuogoProva").style.display = "block";
      <% } %>
        
      // Nascondo Sezione data decorrenza misura
      document.getElementById("idDataDecorrenzaMisura").style.display = "none";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value="";
    }
    else if (tipoUfficio=='UDS' || tipoUfficio=='UDSM'){
      // Visualizzo Sezione data decorrenza misura
      document.getElementById("idDataDecorrenzaMisura").style.display = "block";    
      
      // Nascondo Sezione Da scarcerare/scarcerato
      document.getElementById("idSezioneScarcerare").style.display = "none";      
      document.LoadInserisciMisuraAlternativa.tipo[0].checked=true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value="";

      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled = true;
      
      <% if (!tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) { %>
      document.getElementById("idLuogoProva").style.display = "none";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>.value="";
      <% } %>      
      
      
      if (document.LoadInserisciMisuraAlternativa.tipomisura.value == "<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>") {
        // x UDS la quantità non viene visualizzata come su richiesta di Michele 02/10/2014
        //document.getElementById("idSezioneQuantita").style.display = "block";
        document.getElementById("idSezioneQuantita").style.display = "none";
        document.getElementById("idSezioneDataScadenza").style.display = "block";
      }
      else {
        document.getElementById("idSezioneQuantita").style.display = "none";
        document.getElementById("idSezioneDataScadenza").style.display = "none";
      }
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value="";
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value="";

    }
    else {
      // Nascondo Sezione scarcerato/da scarcerare
      document.getElementById("idSezioneScarcerare").style.display = "none";
      
      document.LoadInserisciMisuraAlternativa.tipo[0].checked=true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value="";
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled = true;
      
      // Nascondo Data Decorrenza Misura (UDS)
      document.getElementById("idDataDecorrenzaMisura").style.display = "none";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value="";

      <% if (!tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) { %>
      document.getElementById("idLuogoProva").style.display = "none";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>.value="";
      <% } %>   
      
      // Nascondo la sezione della Detenzione Domiciliare Termine
      document.getElementById("idSezioneQuantita").style.display = "none";
      document.getElementById("idSezioneDataScadenza").style.display = "none";
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value="";
      
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value="";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value="";
    }    
  }
  
  function handleChangeRadio(radioObj){
    if (radioObj.value=='scarcerare'){
      //alert("da scarcerare");
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = "";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = "";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = "";

      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled = true;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled = true;
    }
    else if (radioObj.value=='scarcerato'){
      //alert("scarcerato");
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value = "";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value = "";
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value = "";

      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled = false;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled = false;
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled = false;
    }
  }
  
  
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    var left = (screen.width/2)-( 500 /2);
    var top  = (screen.height/2)-( 500 /2);
    
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2
                        , "Ricerca_Istituto_Detenzione"
                        , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500, top="+top+", left="+left+"");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    var left = (screen.width/2)-( 300 /2);
    var top  = (screen.height/2)-( 500 /2);
    
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname
                        , "Ricerca_Comune"
                        , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500, top="+top+", left="+left+"");
  }
    
  function pulisciComune(){
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value="";
  }
    
  function ListaTDS(a_formname,a_fieldname)
  {
    var left = (screen.width/2)-( 370 /2);
    var top  = (screen.height/2)-( 500 /2);
    
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:"
                        , "Ricerca_UDS"
                        , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500, top="+top+", left="+left+"");
  }
  
  function ListaTDS_UDS(a_formname,a_fieldname)
  {
     var left = (screen.width/2)-( 370 /2);
     var top  = (screen.height/2)-( 500 /2);
     
     var valore = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;

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
     
  function ListaDocumentiSius(a_formname)
  {
    var tipoMA;
    var naturaMA;
    
    tipoMA='<%=tipoMisura%>';

    naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_51BIS%>';

    var left = (screen.width/2)-( 800 /2);
    var top  = (screen.height/2)-( 500 /2);
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA
                        , "Lista_Provvedimenti_Sius"
                        , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500, top="+top+", left="+left+"");
  }
  
  function ListaDocumentiSiusPerSoggetto(a_formname)
  {
    var tipoMA;
    var naturaMA;
    
    tipoMA='AFFIDAMENTO_IN_PROVA';

    naturaMA='CONCESSIONE';
    
    var left = (screen.width/2)-( 800 /2);
    var top  = (screen.height/2)-( 500 /2);

    
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA+"&RICERCA_PER_SOGGETTO=S"
                        , "Lista_Provvedimenti_Sius"
                        , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500, top="+top+", left="+left+"");
  }




  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

	function ListaComuniTds(formname,fieldname) {
    	var left = (screen.width/2)-( 300 /2);
    	var top  = (screen.height/2)-( 500 /2);
    	// MEV10-s3: aggiunto parametro di passaggio
		var a_typename = document.getElementById('<%=MinorMask.ComboTribunaleId%>').value;
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+a_typename
                        , "Ricerca_Comune_Tds"
                        , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500, top="+top+", left="+left+"");
  	}

  function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    var left = (screen.width/2)-( 500 /2);
    var top  = (screen.height/2)-( 500 /2);
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2
                        , "Ricerca_CSSA"
                        , "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500, top="+top+", left="+left+"");
  }
  
	function ListaUDS(a_formname,a_fieldname) {
		// MEV10-s3: aggiunto parametro di passaggio
		var a_typename = document.getElementById('<%=MinorMask.ComboMagistratoId%>').value;
	    var left = (screen.width/2)-( 370 /2);
    	var top  = (screen.height/2)-( 500 /2);
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,
    			"Ricerca_UDS",
    			"toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500, top="+top+", left="+left+"");
  	}

  function pulisciId()
  {
    //alert("pulisciId");
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
  }

  function VisualizzaAvvocati(){
    var node = document.getElementById('divavvocati');

    if(document.LoadInserisciMisuraAlternativa.checkAvvocati.checked == true) {
       node.style.display='block';
    }
    else {
       node.style.display='none';
    }
  }
  
  //=====================================
  //
  //=====================================
  function Verify()
  {
    <% if ("S".equals(isFaseOrdinanza)) { %>
    // Inserimento/Selezione Ordinanza SIUS
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="-")
    {
      alert("Selezionare l'Ufficio Emittente");
      document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
      return false;
    }
        
    if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
    {
      alert("La Sede dell'Ufficio Emittente è obbligatoria");
      document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
      return false;
    }
        
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value=="-")
    {
      alert("Selezionare il Tipo Provvedimento");
      document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.focus();
      return false;
    }  
        
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=="-")
    {
      alert("Selezionare l'Oggetto della Decisione");
      document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      return false;
    } 
    
    <% if ("N".equals(isConCumulo) && ("10".equals(lPosizione.getCodPosizioneGiuridica()) ||
    		"07".equals(lPosizione.getCodPosizioneGiuridica()))) {
    %>
	    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value == ""
	    		&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value == ""
	    		&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value == ""
	    		// MEV10-s3: aggiunto controllo preventivo
	   			&& ("UDS" == document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value ||
	   					"UDSM" == document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value)
	    ) {
	      alert('Data Inizio Misura obbligatoria');
	      document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.focus();
	      return false;
	    }
    <% } %>
    
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;

    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;

    var data_to_verify_re = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;

    if (!ControllaDataPassaVuota(data_to_verify_re))
    {
     alert('Data Inizio Misura non valida');
     return false;
    }
    
    //========================
    // Se TDS e Det Dom Termine controllo il fine Misura
    // alert ("xx = "+document.LoadInserisciMisuraAlternativa.tipomisura.value);
    if (   document.LoadInserisciMisuraAlternativa.tipomisura.value=="<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>"
        //&& document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="TDS"
       )
    {    
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value;
  
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value.length==1)
          document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value;
  
      var data_to_verify_sc =      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value
                              +'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value
                              +'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
  
      if (!ControllaDataPassaVuota(data_to_verify_sc))
      {
        alert('Data Scadenza Misura non valida');
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
        return false;
      }
    
      if(   document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value==""
         && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value==""
         && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value==""
         && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value=="")
      {
        if (document.getElementById("idSezioneQuantita").style.display == "block"){
          alert("Scadenza della misura o Quantità della misura Obbligatori!");
        } else {
          alert("Scadenza della misura Obbligatoria!");
        }
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
        return false;
      }

      if(  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value!=""
         && (   document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value!=""
             || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value!=""
             || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value!=""
            )
        )
      {
        alert("Inserire Scadenza della misura o in alternativa Quantità della misura!");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
        return false;
      }
    } // end 
    
    
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="TDS"){
      if (typeof(document.LoadInserisciMisuraAlternativa.tipo) != "undefined") {
        if(document.LoadInserisciMisuraAlternativa.tipo[1].checked == true)
        { // Scarcerato
          if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
          if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
    
          var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value
                          +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value
                          +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
                        
          if (!ControllaData(data_to_verify) )
          {
            alert('Data di Scarcerazione non valida');
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
            return false;
          }
        }
      } 
    } 
    
    <% } else { %>
    //===================================
    // Inserimento Provvedimento SIEP
    //===================================
    
    // Data Emissione
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
    {
     alert('Data di emissione non valida');
     return false;
    }
    
    // Data Trasmissione
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
    if (!ControllaData(data_to_verify) )
    {
     alert('Data di trasmissione non valida');
     return false;
    }
    
    // Data Fine Pena
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>)!="undefined")
    {
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
  
      var data_to_verifica = document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
  
      if (!ControllaData(data_to_verifica) )
      {
        alert('Data fine pena non valida');
        return false;
      }
    }
    
    // Dati Eventuale Precedente Ordinanza Di Concessione
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>)!="undefined")
    {
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
  
      var data_to_verifica = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>.value
                        +'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>.value
                        +'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>.value;
  
      if (!ControllaDataPassaVuota(data_to_verifica) )
      {
        alert('Data Emissione Ordinanza non Valida');
        return false;
      }
    }
    
    
    // Magistrato Firmatario
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Firmatario è obbligatorio");
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
      return false;
    }
    
    // Check sui destinatari: tutti obbligatori se previsti in form
    // Istituto
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>)!="undefined")
    {
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
      {
        alert("L'Istituto di Detenzione è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
        return false;
      }
    }
    
    // UEPE
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>)!="undefined")
    {
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" || document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
      {
    	// MEV10-s3: modificato msg
       	alert("L'UEPE/USSM è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.focus();
        return false;
      }
    }

    // Magistrato di Sorveglianza
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>)!="undefined")
    {
      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=="")
      {
    	// MEV10-s3: modificato msg
  		alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.focus();
        return false;
      }
    }
    
    // Tribunale di Sorveglianza
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>)!="undefined")
    {
      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=="")
      {
		// MEV10-s3: modificato msg
        alert("Il Tribunale di Sorveglianza è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.focus();
        return false;
      }
    }
    
    // Autorità per il territorio
    if (typeof (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>)!="undefined")
    {
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.value=="-")
      {
        alert("Autorità competente per territorio è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.focus();
        return false;
      }
    }
    
    var nodeavvocati  = document.getElementById('divavvocati');
    //alert("Nodo avvocato");
    if (nodeavvocati.style.display=='none'){
      //alert("Nodo avvocato non visibile");
      // Se il nodo avvocati non è visibile resetto i campi prima della submit
      // altrimenti vengono comunque notificati
      <% if (avvocati.size()==1) {%>
        document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value="";
      <% } else { %>
      for (var i=0; i<<%=avvocati.size()%>; i++){ 
        document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[i].value="";
      }
      <% } %>
    }
    else {
      //alert("nodo avvocati visibile");
    }
    
    <% } %>
    
  }

  </script>
  
  <jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>

<body class="corpo">
<table>
  <tr>
    <%
    String lTipoTitolo = null;
    if ("S".equals(isConCumulo))
      lTipoTitolo = "(CON CUMULO)";
    else
      lTipoTitolo = "(SENZA CUMULO)";
    %>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione : </font>&nbsp;&nbsp;
    <%if(tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA)){%>
        <font class="campo">Prosecuzione Misura in Corso - Affidamento In Prova</font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE)){%>
        <font class="campo">Prosecuzione Misura in Corso - Detenzione Domiciliare</font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) {%>
        <font class="campo">Prosecuzione Misura in Corso - Semilibertà</font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE)){%>
        <font class="campo">Prosecuzione Misura in Corso - Detenzione Domiciliare a Termine</font>
    <%}else if(tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)){%>
        <font class="campo">Prosecuzione Misura in Corso - Esecuzione Pena Presso Domicilio</font>
    <%}%>
    </td>
  </tr>
</table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<%
   BigDecimal lIdOrdinanzaSius = null;
   if(misuraalternativa.getEveIdEvento() != null){
     lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
   }
%>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMAProsecuzione51Bis">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
  <INPUT type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>"  >
  <INPUT type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"  >
  <INPUT type="HIDDEN" name="tipomisura" value="<%=tipoMisura%>">
  <INPUT type="HIDDEN" name="isFaseOrdinanza" value="<%=isFaseOrdinanza%>">
  <INPUT type="HIDDEN" name="isConCumulo" value="<%=isConCumulo%>">
  
  <%// n.b. posizionegiuridica serve alla ActMisuraAlternativa per gestire le notifiche%>
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  

<%
//==============================================================================
// Posizione Giuridica - Tipo Pena - Quantum - Decorrenza Scadenza
//==============================================================================
%>
<table width ="100%">
  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
      <font class="campo">
      <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
              DETENUTO PER ALTRA CAUSA
      <% } else {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
      <% } %>
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
        <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
        di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        <% } %>
      </td>
    </tr>
    
      <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
      <tr>
        <td class="l">Altro Luogo </td>
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
        </td>
      </tr>
      <% }
     }
   }
   else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
   {
   %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
            </td>
          </tr>
<%
  }%>
        
  
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
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
    </tr>
    <%
    }
  }
  %>


  <%
  // Visualizzazione della Pena Residua
  if(   penaresidua.getIdPenaResidua() != null 
     && !penaresidua.isErgastolo()
    )
  {
    if ( !penaresidua.isQuantumReclusioneZero() ) 
    {
    %>
      <tr>
        <td class="l">Reclusione</td>
        <td class="l" colspan=2>
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
        </td>
        <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
        <td class="l">Multa</td>
        <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
        <% } %>  
      </tr>
    <%}%>

   
   <% if ( !penaresidua.isQuantumArrestoZero() ) { %>
   <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      <% } %>
   </tr>
   <% } %>
 <%}%>


  <tr>
    <% if (penaresidua.getDataInizio() != null) { %>
    <td class="l">Data Decorrenza Pena</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
    <% } %>

    <% if ("S".equals(penaresidua.getFlagErgastolo())) { %>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
    <% } else if ("D".equals(penaresidua.getFlagErgastolo())) { %>
    <td class="l">Pena Detentiva</td>
    <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
    <% } %>

    <% 
    // Se non libero o detenuto altra causa
    if( !lPosizione.isLibero() || (lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) ) 
    {
      if (!penaresidua.isErgastolo())
      {
        if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
        {
        %>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>"  size="2" maxlength="2" name="">
           -
           <input type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" size="2" maxlength="2" name="">
           -
           <input type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" size="4" maxlength="4" name="">
         </td>
        <%
        }
        else if( penaresidua.getDataFine() != null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
          {
          %>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
          <% } else { %>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
          <% }
        }
      }  // end if (!penaresidua.isErgastolo())
    } // end if not libero o detienuto altra causa
  %>
  </tr>
</table>


<% if ("N".equals(isFaseOrdinanza)) { %>
<table>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>
<%}%>


<%
//==============================================================================
// Dati dell'ordinanza o decreto
// nuovapenaresidua!=null se 
//==============================================================================
%>
<table width ="100%">
  <tr>
    <td class="Titolo" colspan="4"> Dati Del Provvedimento della Sorveglianza </td>
  </tr>
<% 
if ("N".equals(isFaseOrdinanza)) 
{ 
  // Ho già Inserito/selezionato il provvedimento SIUS quindi visualizzo i dati
  // non modificabili
%>
  <tr>
    <td class="l" width=25%>Anno / Numero Sius</td>
    <td class="l" width=20%>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
    </td>
   
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l" width=25%> Anno / Numero Decreto </td>
    <% } else { %>
    <td class="l" width=25%> Anno / Numero Ordinanza </td>
    <% } %>     

    <td class="l" width=20%>
       <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
       <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
    </td>
  </tr>
    
  <tr>
    <td class="l">Ufficio Emittente</td>
    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
    <td class="l" colspan="3"> <font class="campo"> <%=descrTipoUfficio%> DI <%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
  </tr>
    
  <tr>
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l">Oggetto Decreto </td>
    <% } else { %> 
    <td class="l">Oggetto Ordinanza </td>
    <% } %>    
    <td class="l" colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
  </tr>
    
  <tr>
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l">Data Emissione Decreto </td>
    <% } else { %> 
    <td class="l">Data Emissione Ordinanza </td>
    <% } %>
    <td class="l" colspan="3">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
      </font>
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd"))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"MM"))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"yyyy"))%>">
    </td>
  </tr>
  <tr>
    <td class="l">Data decorrenza Misura</td>
    <td class="l" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd"))%>">
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"MM"))%>">
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"yyyy"))%>">
      </font>
    </td>
  </tr>
  
  <% 
  if ("TDS".equals(UfficioEmittente.getCodTipoUfficio())) 
  { 
    String checkScarcerare="";
    String checkScarcerato="";
    String statusData="readonly";
    
    if ("PROC".equals(misuraalternativa.getCodTipoUfficioScarcerazione())){
      checkScarcerare = "checked";
      statusData = "disabled='disabled'"; // se esegue proc disabilito i campi
    }
    else
      checkScarcerato = "checked";

    String lDescLuogoProva = "";
    
    if(tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA))
      lDescLuogoProva = "Luogo della Prova";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE))
      lDescLuogoProva = "Luogo della Detenzione Domiciliare";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) 
      lDescLuogoProva = ""; // NON PREVISTO
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE))
      lDescLuogoProva = "Luogo della Detenzione Domiciliare";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
      lDescLuogoProva = "Domicilio Imposto";

  %>
    
    <% if (!tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) {  %>
    <tr>
      <td class="l"><%=lDescLuogoProva%></td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP( misuraalternativa.getDescrLuogoProva() )%></font></td>
    </tr>
    <% } %>

    <tr>
      <td class="l">Da scarcerare&nbsp;
        <input type="radio" name="tipo" value="scarcerare"  <%=checkScarcerare%> disabled="disabled">&nbsp;&nbsp;
        Scarcerato&nbsp;
        <input type="radio" name="tipo" value="scarcerato" <%=checkScarcerato%> disabled="disabled">
      </td>
      <td class="l" colspan="3">Data Scarcerazione &nbsp;
        <input type="text" size="2" maxlength="2" title="Giorno Data Scarcerazione" 
               name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd"))%>"
               <%=statusData%> > -
        <input type="text" size="2" maxlength="2" title="Mese Data Scarcerazione"   
               name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"MM"))%>"
               <%=statusData%>> -
        <input type="text" size="4" maxlength="4" title="Anno Data Scarcerazione" 
               name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"yyyy"))%>"
               <%=statusData%>>
      </td>
    </tr>
    

    
  <% } // end if TDS %>
    
    <%// la durata della misura per la det dom term viene indicata sia dal TDS che dal MDS %>
    <% if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE)) { %>
    <tr>
      <td class="l">Quantità della misura</td>
      <td class="l" colspan="3">
        <% 
        if (   (misuraalternativa.getNumAnniMisura()!=null && misuraalternativa.getNumAnniMisura().intValue()>0 )
            || (misuraalternativa.getNumMesiMisura()!=null && misuraalternativa.getNumMesiMisura().intValue()>0 )
            || (misuraalternativa.getNumGiorniMisura()!=null && misuraalternativa.getNumGiorniMisura().intValue()>0 )
            )         
        { %>
        <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniMisura(),"0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiMisura(),"0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniMisura(),"0")%></font>
        <% } else { %>
        <font class="l">&nbsp;</font>
        <% } %>
      </td>
    </tr>
    <tr>
      <td class="l">Data Scadenza Misura</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
    </tr>    
    <% } %>
  
<% 
} 
else 
{ // Provengo dalla griglia delle funzione e devo ancora inserire/selezionare 
  // il provvedimento SIUS
%>
    <tr>
      <td class="l" colspan="3">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" 
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Provvedimento </td>
      <td class="l">
        <input Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" 
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='changeTipoUfficioSorv();'")%>      
      </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
        <a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
     </td>
    </tr>
    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <select  Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
          <%=comboTipoProvvSorv%>
        </select>
      </td>
    </tr>     
    <tr>
      <td class="l">Oggetto Decisione <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <select Title="Codice Motivo"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <option value = "-"  />-
          <% //=motivoProvv%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        </font>
      </td>
    </tr>
    
    <% if ("S".equals(isConCumulo)) {%>
    <tr id="idDataDecorrenzaMisura" style="display:none; position:relative;">
      <td class="l">Data decorrenza Pena</td>
      <td class="l" colspan="1">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "dd-MM-yyyy") )%></font>
      </td>
      <td class="l">Data decorrenza Misura</td>
      <td class="l" colspan="1">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      </td>
    </tr>        
    <% } else { %>
    <tr id="idDataDecorrenzaMisura" style="display:none; position:relative;">
      <td class="l">Data decorrenza Misura <% if ( "10".equals(lPosizione.getCodPosizioneGiuridica()) || "07".equals(lPosizione.getCodPosizioneGiuridica())){%><font class=ob>(*)</font><%}%></td>
      <td class="l" colspan="3">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      </td>
    </tr>
    <% } %>  
    
    <%
    String lDescLuogoProva = "";
    
    if(tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA))
      lDescLuogoProva = "Luogo della Prova";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE))
      lDescLuogoProva = "Luogo della Detenzione Domiciliare";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)) 
      lDescLuogoProva = ""; // NON PREVISTO
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE))
      lDescLuogoProva = "Luogo della Detenzione Domiciliare";
    else if(tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
      lDescLuogoProva = "Domicilio Imposto";
    
    %>
    
    <tr id="idLuogoProva" style="display:none; position:relative;">
      <td class="l"><%=lDescLuogoProva%></td>
      <td class="l" colspan="3">
        <input Title="<%=lDescLuogoProva%>" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>" size="50" type="text" >
      </td>
    </tr>
    
  <tr id="idSezioneScarcerare" style="display:none; position:relative;">
    <td class="l">
        Da scarcerare &nbsp;
        <input type="radio" name="tipo" value="scarcerare" checked onclick="handleChangeRadio(this);">
        &nbsp; Scarcerato  &nbsp;
        <input type="radio" name="tipo" value="scarcerato" onclick="handleChangeRadio(this);">
    </td>
    <td class="l">
        Data Scarcerazione &nbsp;
        <input title = "Giorno Data Scarcerazione" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%> disabled > -
        <input title = "Mese Data Scarcerazione" type="text" size="2" maxlength="2"   name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%> disabled > -
        <input title = "Anno Data Scarcerazione" type="text" size="4" maxlength="4"   name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%> disabled >
    </td>
  </tr>
  
  <tr id="idSezioneQuantita" style="display:none; position:relative;">
    <td class="l">Quantità della misura</td>
    <td class="l" colspan="3">
      Anni&nbsp;<input Title="Anni Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
      Mesi&nbsp;<input Title="Mesi Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
      Giorni&nbsp;<input Title="Giorni Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  <tr id="idSezioneDataScadenza" style="display:none; position:relative;">
    <td class="l">Data scadenza misura</td>
    <td class="l" colspan="3">
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </font>
    </td>
  </tr>  
  
  
<% } %>
  <tr>
    <td class="l">Note</td>
    <td class="L" colspan="3">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2 ><%= StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
    </td>
  </tr>

</table>


<%
//==============================================================================
// Se Devo inserire il provvedimento SIEP visualizzo le seguenti sezioni
// - Nuova Data Fine Pena (calcolata con la data inizio misura)
// - Estremi Ordinanza di concessione della Misura da Proseguire
// - Magistrato Firmatario
// - Destinatari
//==============================================================================
%>

<% if ("N".equals(isFaseOrdinanza)) { %>
  <% if (misuraalternativa.getDataInizioMisura()!=null) { %>
<table width ="100%">
  <tr>
    <td class="Titolo" colspan="8"> Nuova Data Fine Pena</td>
  </tr>
  <tr>
    <td class="l">Data Decorrenza Misura</td>
    <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"),"&nbsp;")%> </font></td>
    <%  if (nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null){ %>
    <td class="l">Data Fine Pena</td>
    <td class="L" colspan=2>
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <%
    }
    else if( penaresidua.getDataFine() != null)
    { 
      String lFontClass= "campo";
      String lTdClass= "L";
      if(!nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())){
        lFontClass = "lRosso";
        lTdClass = "lRosso";
      }
      %>
        <td class="l">Data Fine Pena</td>
        <td class="<%=lTdClass%>" colspan=2>
          <font class="<%=lFontClass%>">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>
          </font>
          <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
          <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
          <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
        </td>      
    <% } %>
  </tr>
</table>
<% } // end if data inizio misura %>

<%
//==============================================================================
// Estremi dell'ordinanza di concessione (solo x prosec. disposta dal MDS
//==============================================================================
%>
<% if ( !"TDS".equals(UfficioEmittente.getCodTipoUfficio())) { %>
<table width ="100%">
  <tr>
    <td class="Titolo" colspan=6>Ordinanza Del Tribunale di Sorveglianza che ha Concesso la Misura In corso</td>
  </tr> 
  <tr>
    <td class="l" colspan="4">
      <a href="Javascript:ListaDocumentiSiusPerSoggetto('LoadInserisciMisuraAlternativa');">
        Ricerca provvedimento di concessione nel Distretto <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l">Anno / Numero SIUS</td>
    <td class="l">
      <input Title="Anno Fascicolo Sius" 
             name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FAS_SIUS_MA_AT%>" type="text" size="4" maxlength="4" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      /
      <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FAS_SIUS_MA_AT%>" type="text" size="6" maxlength="6" 
             onkeypress="return TicTabNumField(this,event)">
    </td>
    <td class="l"> Anno / Numero Ordinanza </td>
    <td class="l">
      <input Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO_MA_AT%>" type="text" size="4" maxlength="4" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      /
      <input Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO_MA_AT%>" type="text" size="6" maxlength="6" 
             onkeypress="return TicTabNumField(this,event)">
    </td>
  </tr>
  
  <tr>
    <td class="l">Ufficio Emittente</td>
    <td class="l" colspan="3">
      <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale, "", ICostantiMisuraAlternativa.CAMPO_TIPO_UFF_EMITT_MA_AT)%> 
      <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE_MA_AT%>" value="03"> 
    </td>
  </tr>
  <tr>
    <td class="l">Sede Ufficio Emittente</td>
    <td class="l" colspan="3">
      <input type="text" Title="Luogo Tribunale Sorveglianza" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT %>" size=35  >
      <a href="Javascript:ListaComuniEmitTdsMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_UFF_EMITT_MA_AT%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
   </td>
  </tr>  
  <tr>
    <td class="l">Oggetto Ordinanza</td>
    <td class="L" colspan="3">
      <select Title="Codice Motivo" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA_MA_AT%>" >
        <%=comboTipoMisuraAT%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Data Emissione Ordinanza</td>
    <td class="l" colspan="3">
      <font class="campo">
        <input type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE_MA_AT%>" size="2" maxlength="2" value="" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE_MA_AT%>" size="2" maxlength="2" value="" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE_MA_AT%>"  size="4" maxlength="4" value="" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </font>
    </td>
  </tr>  
</table>
<% } %>

<table width="100%">    
    <tr>
      <td class="Titolo" colspan=2> Magistrato Firmatario </td>
    </tr>
    <tr>
      <%-- MEV10-s3: aggiunta proprietà width --%>
   	  <td class="l" width="30%">Magistrato Firmatario</td>
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
</table>


<%
//==============================================================================
// DESTINATARI:
// n.b. la form è in comune con MDS e TDS. I destinatari previsti sono
// - Istituto di detenzione: se MDS solo x Semilibertà. Se TDS solo se da scarcerare tranne per affidamento esemilibertà in cui è sempre previsto
// - UEPE (Sempre previsti TDS e MDS tutte le misure)
// - TDS  (Sempre previsti TDS e MDS tutte le misure) Tranne x TDS Espiazione presso il domicilio
// - MDS  (Sempre previsti TDS e MDS tutte le misure)
// - Autorità (Sempre previsti TDS e MDS tutte le misure)
// - Avvocati (Sempre previsti TDS e MDS tutte le misure con check)
//
// Tutti i destinatari se previsti sono obbligartori
//
//==============================================================================
%>
<table width ="100%">
  <tr>
    <td class="Titolo" colspan="4">Destinatari</td>
  </tr>
  
  <% 
  // Istituto: se MDS solo in caso di Semilibertà 
  //           se TDS solo se Da scarcerare tranne nel caso di Affidamento 
  //              e semilibertà che è sempre previsto
  if(   tipoMisura.equals(ICostantiMisuraAlternativa.SEMILIBERTA)
     || (   "TDS".equals(UfficioEmittente.getCodTipoUfficio()) 
         && "PROC".equals(misuraalternativa.getCodTipoUfficioScarcerazione())
        )
//     || (   "TDS".equals(UfficioEmittente.getCodTipoUfficio()) 
//         && tipoMisura.equals(ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA)
//        )
    ) 
  {%>
  <tr>
    <td class="l" width ="30%">Istituto di Detenzione <font class=ob>(*)</font></td>
    <td class="l">
      <%if(posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione()!= null && posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)
      {%>
      <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
      <%}else {%>
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
      <%}%>
    </td>
  </tr>
  <%}%>
  
  <%
  //============================================
  // UEPE - Sempre previsto (MDS-TDS tutte le misure)
  //============================================
  %>
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
		<td class="l" colspan="3"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
    <tr>
    	<td class="l">Sede</td>
      	<td class="L" colspan="3">
	    	<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="" size=60 >
	      	<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
	      	<% if(!tipoMisura.equals("SEMILIBERTA") && !tipoMisura.equals("SEMILIBERTACUMULO")) { %>
	      		<input type="hidden" Title="UEPE" name="cssaE" value="S">
	      	<% } %>
	        <a href="Javascript:ListaCSSAMinor('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
	        	<img src="/images/filefolder.gif" border=0>
	       	</a>
		</td>
  	</tr>

  	<%
  	//============================================
  	// MDS - Sempre previsto (MDS-TDS tutte le misure)
  	//============================================
 	%>
 	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="L" colspan="3">
     		<input title="ufficio" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" maxlength="35" size="25">
     		<a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
            	<img src="/images/filefolder.gif" border=0>
            </a>
    	</td>
  	</tr>
  
  	<%
  	//============================================
  	// TDS - Sempre previsto (MDS-TDS tutte le misure tranne Espiazione presso domicilio)
  	//============================================
  	%>
  	<% if ("TDS".equals(UfficioEmittente.getCodTipoUfficio())
  			&& !tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM)) {
	%>
	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
		<td class="L" colspan="3">
      		<input type="text" title="Sede Tribunale Sorveglianza" maxlength="35" size="35"
             		value="<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%>"            
             		name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>">
      		<a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>');">
        		<img src="/images/filefolder.gif" border=0 >
      		</a>
    	</td>
  	</tr>
	<% } %>

  <%
  //============================================
  // Autorità - Sempre prevista (MDS-TDS tutte le misure)
  //============================================
  %>  
  <tr>
    <td class="l" width=30%>Autorità competente per territorio <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
        <%=codiceAutoritaC%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" width=30%>Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C %>"  cols=30></textarea>
    </td>
  </tr>
</table> 


<%
//============================================
// Avvocati - Sempre previsti (MDS-TDS tutte le misure)
//============================================
%>  
<table width="100%">
  <tr>
    <td class="Titolo" colspan=6>Destinatari per Notifica </td>
  </tr>
  <tr>
    <td class="l">
      <input type="checkbox" name="checkAvvocati" onclick="VisualizzaAvvocati();"> &nbsp;&nbsp;Notifiche atti (Difensore)
    </td>
  </tr>
</table>

<div id="divavvocati" style="width: 100%; display:none; position:relative; " >
<table width ="100%"> 
  <tr>
    <td>
      <%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
      %>
        <table>
          <tr>
            <td class="l">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
        <table>
          <tr>
            <td class="l">Autorità Destinazione <font class=ob>(*)</font></td>
            <td class="L">
              <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
                <%=autoritaEsternaAvv%>
              </select>
            </td>
            <td rowspan=2 class="l">Note</td>
            <td rowspan=2 class="L">
              <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=40 rows=5 ></textarea>
            </td>
          </tr>
          <tr>
            <td class="l">Sede </td><td class="L">
              <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
              <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        </table>
      <%
        lIdxAvv++;
      }
      %>
    </td>
  </tr>
</table>
</div>
<% } // end if ("N".equals(isFaseOrdinanza)) %>


<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</FORM>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
  
  <% if ("S".equals(isFaseOrdinanza)) { %>
  
  <% } else { %>
  // Data Emissione
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050"); 
  
  // Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050"); 
  
    <% if (misuraalternativa.getDataInizioMisura()!=null) { %>
    // Data Fine Pena
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
    
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
    
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
    <% } %>
  <% } %>
</script>
</body>
</html>
