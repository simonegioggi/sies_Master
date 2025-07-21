<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="tipoRevoca"   scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"     scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="tipoUfficioSIUS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvvSorv"    scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"           scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoRevoca"          scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="idmisuraalternativa"   scope="request" class="java.lang.String"/>
<jsp:useBean id="distretto"             scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"              scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"      scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="codiceAutoritaE"       scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per l'inserimento della cessazione delle seguenti misure:
// - DETENZIONE DOMICILIARE
// - SEMILIBERTA'
// - DETENZIONE DOMICILIARE A TERMINE
//==============================================================================
%>

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
    
//==============================================================================
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

  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

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
    caricaCombo(strOggettiDecisione,';','#',document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value
                                           ,document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>);
  }
  //============================================================================

    function VisualizzaAvvocati(){
      var node = document.getElementById('divavvocati');
  
      if(document.LoadInserisciCessazioneMA.checkAvvocati.checked == true) {
         node.style.display='block';
      }
      else {
         node.style.display='none';
      }
    }

    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function pulisciComune(){
      return;
    
      document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.value="";
      
      if (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="TDS"){
        document.getElementById("destTDS").style.display = "block";
        document.getElementById("destUDS").style.display = "none";
      }
      else if (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="UDS"){
        document.getElementById("destTDS").style.display = "none";
        document.getElementById("destUDS").style.display = "block";        
      }
      else if (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value="-"){
        document.getElementById("destTDS").style.display = "none";
        document.getElementById("destUDS").style.display = "none";
      }
      
      // Ripulisco i campi
      document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value="";
      document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value="";
     
    }
    
    function ListaUDS(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
    function ListaTDS_UDS(a_formname,a_fieldname)
    {
      var valore = document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
      var i = document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.selectedIndex;
      if ( valore == "UDS")  {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else if (valore == "TDS") {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else {
        alert("Selezionare il tipo di ufficio emittente");
      }
    }
    
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }    
      
    function ListaDocumentiSius(a_formname)
    {
      var tipoMA;
      if(document.LoadInserisciCessazioneMA.tipomisura.value=='DETENZIONE')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
      }
      else if(document.LoadInserisciCessazioneMA.tipomisura.value=='SEMILIBERTA')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
      }
      else if(document.LoadInserisciCessazioneMA.tipomisura.value=='DETENZIONE_TERM')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>';
      }

      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.CESSAZIONE%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }
    
    
    function radio()
    {
      var nodeIstituto  = document.getElementById('divIstituto');
      var nodeAutorita  = document.getElementById('divAutorita');
    
      if(document.LoadInserisciCessazioneMA.tipo[0].checked){
        // Contro soggetto ancora in misura
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value = "";
        
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.readOnly = true;
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.readOnly = true;
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.readOnly = true;
        
        nodeIstituto.style.display='none';
        nodeAutorita.style.display='block';
        
        document.LoadInserisciCessazioneMA.Comune.value="";
        document.LoadInserisciCessazioneMA.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value="";
      }
      else {
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.readOnly = false;
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.readOnly = false;
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.readOnly = false;
      

        nodeIstituto.style.display='block';
        nodeAutorita.style.display='none';
        
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex = 0;
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.value="";
        document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.value="";
      }
    }

    //=========================
    //
    //=========================
    function Verify()
    {
<%
if(    !lPosizione.isLibero()
    || (lFascicoloAssociato.getFlagAltraCausa()!=null &&   "S".equals(lFascicoloAssociato.getFlagAltraCausa())           
        && penaresidua.getDataFinePresunta()!= null 
        && penaresidua.getDataFine() == null            
       )
  )
{
 if ( !penaresidua.isErgastolo())
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
  {%>
      if (document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
      if (document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

      var data_to_verifica = document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciCessazioneMA.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

      if (!ControllaData(data_to_verifica) )
      {
        alert('Data fine pena non valida');
        return false;
      }
<%}
 }
}%>

      // Data Emissione
      if (document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
       alert('Data di emissione non valida');
       return false;
      }

      // Data Trasmissione
      if (document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      var data_to_verify = document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciCessazioneMA.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      if (!ControllaData(data_to_verify) )
      {
       alert('Data di Trasmissione non valida');
       return false;
      }


      
      if(document.LoadInserisciCessazioneMA.flagmisura.value=="N")
      {
        if(document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.value=="-")
        {
          alert("L'Ufficio Emittente è obbligatoria");
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
          return false;
        }
      
        if(document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
        {
          alert("La Sede dell'Ufficio Emittente è obbligatoria");
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
          return false;
        }
        
        if(document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.value=="-")
        {
          alert("Il Tipo Provvedimento è obbligatorio");
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.focus();
          return false;
        }
        
        if(document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_COD_MOTIVO %>.value=="-")
        {
          alert("L'Oggetto Provvedimento è obbligatorio");
          document.LoadInserisciCessazioneMA.<%=ICostantiEvento.CAMPO_COD_MOTIVO %>.focus();
          return false;
        }
      }
      
      
      // Data di detenzione se presente deve essere valida
      if (typeof (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>)!="undefined" )
      {
        if (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value.length==1)
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value;
        if (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value.length==1)
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value;
  
        var data_to_verify =     document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value
                            +'-'+document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value
                            +'-'+document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value;
  
        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.focus();
          alert('Data di Inizio Misura non valida');
          return false;
        }
      }

      
      //==============================================
      // 
      //==============================================
      if(document.LoadInserisciCessazioneMA.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciCessazioneMA.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Magistrato Firmatario è obbligatorio");
        document.LoadInserisciCessazioneMA.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }



      // Istituto di detenzione: se presente è obbligatorio
      if (typeof (document.LoadInserisciCessazioneMA.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>)!="undefined" ){
         if(   document.getElementById('divIstituto').style.display=='block'
            && document.LoadInserisciCessazioneMA.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value==""
           )
         {
            alert("L'Istituto di Detenzione è obbligatorio");
            return false;
         }
      }

      if (typeof (document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>)!="undefined" ){
        if(    document.getElementById('divAutorita').style.display=='block'
            && document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>[document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-'
           )
        {
          alert("L'Autorità competente per territorio è obbligatorio");
          document.LoadInserisciCessazioneMA.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.focus();
          return false;
        }
      }


      //if (document.getElementById("destTDS").style.display=="block"){
        if(document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value=="") {
          alert("Indicare il Tribunale di Sorveglianza destinatario per la Comunicazione");
          document.LoadInserisciCessazioneMA.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.focus();
          return false;
        }
      //}
      
      /*
      if (document.getElementById("destUDS").style.display=="block"){
        if(document.LoadInserisciCessazioneMA.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value=="") {
          alert("Indicare l'Ufficio di Sorveglianza destinatario per la Comunicazione");
          document.LoadInserisciCessazioneMA.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.focus();
          return false;
        }
      }
      */

      var nodeavvocati  = document.getElementById('divavvocati');
      if (nodeavvocati.style.display=='none'){
        // Se il nodo avvocati non è visibile resetto i campi prima della submit
        // altrimenti vengono comunque notificati
        <% if (avvocati.size()==1) {%>
          document.LoadInserisciCessazioneMA.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value="";
        <% } else { %>
        for (var i=0; i<<%=avvocati.size()%>; i++){ 
          document.LoadInserisciCessazioneMA.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[i].value="";
        }
        <% } %>
      }
      else {
        //alert("nodo avvocati visibile");
        <% if (avvocati.size() >1) {%>
        if (document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0][document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].selectedIndex].value == '-')
        {
            alert("Il campo Autorità per la Notifica  di un Condannato è obbligatorio");
            document.LoadInserisciCessazioneMA.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
            return false;
        }
        
        if (document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1][document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].selectedIndex].value == '-')
        {
            alert("Il campo Autorità per la  Notifica di un Condannato è obbligatorio");
            document.LoadInserisciCessazioneMA.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
            return false;
        }
        <%} else {%>
        if (document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciCessazioneMA.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-')
        {
          alert("Il campo Autorità per la Notifica  di un Condannato è obbligatorio");
          document.LoadInserisciCessazioneMA.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
          return false;
        }
        <%}%>
      }
      
      
    }
  </script>
  
<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>

<body class="corpo" onload="caricaComboOggetto();">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;

    <% if(tipoRevoca.equals("DETENZIONE")) { %>
         <font class="campo">Cessazione Misura Alternativa Detenzione Domiciliare</font>
    <% } else if(tipoRevoca.equals("SEMILIBERTA")) { %>
         <font class="campo">Cessazione Misura Alternativa Semilibertà</font>
    <% } else if(tipoRevoca.equals("DETENZIONE_TERM")) { %>
         <font class="campo">Cessazione Misura Alternativa Detenzione Domiciliare a Termine</font>
    <% } %>
    </td>
  </tr>
</table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
  
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciCessazioneMA">
  <INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciCessazioneMA">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">

  <INPUT type="HIDDEN" name="tipomisura" value="<%=tipoRevoca%>">

  
  <INPUT type="hidden" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">

  <% if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null) { %>
    <input type="HIDDEN" name="flagmisura" value="N">
  <% } else { %>
    <input type="HIDDEN" name="flagmisura" value="S">
  <% } %>

<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=idmisuraalternativa%>">
<input type="HIDDEN" name="codiceMotivo" value="<%=eventoRevoca.getCodMotivo()%>">

<table width ="100%">
  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
      <font class="campo">
      <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) {%>
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

<%
               if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null)
               {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               }
%>
            </td>
           </tr>
<%
               if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td>
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
        {
%>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
              if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null)
              {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
              }
%>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%>" type="text" name=<%=ICostantiLuogoDetenzione..CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>

      <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
           <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
           }  }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
           <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>


      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
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

       if (penaresidua.getFlagErgastolo() != null)
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
%>

<%
    if((!lPosizione.isLibero() ) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <!--td class="l">Data Fine Pena Manuale</td-->
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
           if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else
            {%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

 <%         }
        }
      }
}
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
       
       
       
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
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


<%
//==============================================================================
//
//==============================================================================
%>
<table width ="100%">
  <tr>
    <td class="Titolo" colspan='8'> Dati Del Provvedimento (Decreto/Ordinanza) di Cessazione della Misura</td>
  </tr>
  <tr>
    <td class="l" colspan="4">
      <a href="Javascript:ListaDocumentiSius('LoadInserisciCessazioneMA');">
        Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l">Anno / Numero SIUS</td>
    <td class="l">
      <input type="text" Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>"  
             size="4" maxlength="4" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             onChange="pulisciId();">
      /
      <input type="text" Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>"  
             size="6" maxlength="6" 
             onkeypress="return TicTabNumField(this,event)"
             onChange="pulisciId();">
    </td>
    <td class="l"> Anno / Numero Provvedimento</td>
    <td class="l">
      <input type="text" Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
             size="4" maxlength="4" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             onChange="pulisciId();">
      /
      <input type="text" Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>"  
             size="6" maxlength="6" 
             onkeypress="return TicTabNumField(this,event)"
             onChange="pulisciId();">
    </td>
  </tr>
  
  <tr>
    <td class="l">Ufficio Emittente <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='caricaComboOggetto();pulisciComune();pulisciId();'", ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, tipoUfficioSIUS)%>    
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <font class="campo">
        <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
        <a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciCessazioneMA','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </font>
    </td>
  </tr>
  
  <tr>
    <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select title="tipoUfficioSIUS"  name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
        <%=comboTipoProvvSorv%>
      </select>  
    </td>
  </tr>
  
  <tr>
    <td class="l">Oggetto Provvedimento <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
        <option value = "-"  />-
        <%-- MERGE v10: modificato commento --%>
        <%--//=motivoProvv--%>
      </select>
    </td>
  </tr>
    
  <tr>
    <td class="l" width="1%" nowrap>Data Emissione Provvedimento </td>
    <td class="l" colspan="3">
      <font class="campo">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      </font>
    </td>
  </tr>
  
  <tr>
    <td  class="l">Note</td>
    <td  class="L" colspan="3">
      <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
    </td>
  </tr>
  
  
  <%
  //============================================================================
  // Contro soggetto non detenuto/detenuto
  // - da visualizzare solo in caso dei Detenzione Domiciliare (+ a termine)
  // - da verificare le posizioni giuridiche
  //============================================================================
  %>
  <% if(tipoRevoca.equals("DETENZIONE") || tipoRevoca.equals("DETENZIONE_TERM") ) { %>
  <tr>
    <td class="l" colspan="2">Esecuzione contro soggetto non detenuto &nbsp;<input type="radio" name="tipo" value="nondetenuto" checked onClick="javascript:radio();">
      &nbsp; Esecuzione contro soggetto detenuto  &nbsp; <input type="radio" name="tipo" value="detenuto" onClick="javascript:radio();">
    </td>
    <td class="l" colspan="2">Dal &nbsp;
      <input type="text" size="2" maxlength="2" value=""  readonly
             name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input type="text" size="2" maxlength="2" value=""  readonly
             name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>"  
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input type="text" size="4" maxlength="4" value=""  readonly
             name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" 
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  <% } %>
</table>



<table width ="100%">
  	<tr>
    	<td class="Titolo" colspan=6>Magistrato Firmatario</td>
  	</tr>
  	<tr>
  		<%-- MEV10-s3: aggiunta proprietà per la larghezza del campo --%>
    	<td class="l" width="30%">Magistrato Firmatario</td>
    	<td class="L">
      		<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      		<input readonly title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      		<a href="Javascript:ListaMagistrati('LoadInserisciCessazioneMA','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
      			<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
   </tr>
   <tr>
     	<td class="Titolo" colspan=6>Destinatari</td>
   </tr>
</table>

<% // se
String lDisplayDivIstituto = "";
String lDisplayDivAutorità = "";
if(   lPosizione!= null && lPosizione.getCodPosizioneGiuridica()!= null 
   && (   lPosizione.getCodPosizioneGiuridica().equals("14") // Espiazione Pena in Regime di Semiliberta' 
       || lPosizione.getCodPosizioneGiuridica().equals("31") // Sospensione Cautelativa 51 Ter (di Det. Domiciliare)
       || lPosizione.getCodPosizioneGiuridica().equals("33") // Sospensione Cautelativa 51 Ter (di Semiliberta')
       || lPosizione.getCodPosizioneGiuridica().equals("36") // Sospensione Provvisoria 51 Bis (di Det. Domiciliare)
       || lPosizione.getCodPosizioneGiuridica().equals("38") // Sospensione Provvisoria 51 Bis (di Semiliberta')
      )
  )
{
  lDisplayDivIstituto = "block";
  lDisplayDivAutorità = "none";
}
else {
  lDisplayDivIstituto = "none";
  lDisplayDivAutorità = "block";
}
%>
<div id="divIstituto" style="width: 100%; display:<%=lDisplayDivIstituto%>; position:relative; " >
<table width="100%">
  <tr>
    <td class="l" width ="30%">Istituto di Detenzione <font class=ob>(*)</font></td>
    <td class="l">
    <%
    if(posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione()!= null && posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)
    {
    %>
      <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciCessazioneMA','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
    <% } else { %>
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciCessazioneMA','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
    <% } %>
    </td>
  </tr>
</table>
</div>

<div id="divAutorita" style="width: 100%; display:<%=lDisplayDivAutorità%>; position:relative; " >
<table width="100%">
  <tr>
    <!--autorità esterna e-->
    <td class="l" width ="30%">Autorità Competente per territorio <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
        <%=codiceAutoritaE%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l" width="30%">Sede</td>
    <td class="L">
      <% if(autoritaEsternaE !=null) { %>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
<%--       <% } else  {%> --%>
<%--         <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35"> --%>
      <% } %>
        <a href="Javascript:ListaComuni('LoadInserisciCessazioneMA','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>" cols=30></textarea>
    </td>
  </tr>
</table>
</div>

<table width="100%" style="display:block; position:relative; ">
  	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
  	<tr>
  		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="l" colspan="3">
      		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
      		<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciCessazioneMA','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
      			<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
	</tr>
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--
  <tr id="destUDS" style="display:none; position:relative; " >
    <td class="l" width ="30%">Ufficio di Sorveglianza <font class=ob>(*)</font></td>
    <td class="l"  colspan="3">
      <input type="text" title="Sede Tribunale Sorveglianza" value="" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>"  maxlength="35" size="35">
    <a href="Javascript:ListaUDS('LoadInserisciCessazioneMA','<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>');">
    <img src="/images/filefolder.gif" border=0></a></td>
  </tr>
  --%>
</table>    

<table width="100%">
  <tr>
    <td class="Titolo" colspan=6>Destinatari per Notifica</td>
  </tr>
  <!--
  <tr>
    <td class="l">
      <input type="checkbox" name="checkAvvocati" onclick="VisualizzaAvvocati();"> &nbsp;&nbsp;Notifiche atti (Difensore)
    </td>
  </tr>
  -->
</table>

<div id="divavvocati" style="width: 100%; display:block; position:relative;" >
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
            <td class="l" width="30%">Autorità Destinazione <font class=ob>(*)</font></td >
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
             <a href="Javascript:ListaComuni('LoadInserisciCessazioneMA','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
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
</div>


<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>

</form>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciCessazioneMA");
<%
if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)

 {%>
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
  }
 }
}
%>


<%if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null){%>
 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");



  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");


  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");


  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");



<%}
if(lPosizione!= null && lPosizione.getCodPosizioneGiuridica()!= null && lPosizione.getCodPosizioneGiuridica().equals("12"))
 {%>
<%--    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>","alphabetic"); --%>
<%}
if( misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
{%>
<%-- frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>","alphabetic"); --%>
<%}%>
</script>


</body>
</html>