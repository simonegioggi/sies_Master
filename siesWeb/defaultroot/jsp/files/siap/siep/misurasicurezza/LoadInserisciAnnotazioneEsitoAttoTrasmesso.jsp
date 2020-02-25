<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>


<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>

<%@ page import="siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione"%>


<% // Dettagli %>
<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="residenza"             scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"            scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"           scope="request" class="java.util.Vector"/>

<% // Combo %>
<jsp:useBean id="comboUfficiPM"           scope="request" class="java.lang.String"/>
<jsp:useBean id="comboUfficiPMInoltro"    scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvv"          scope="request" class="java.lang.String"/>
<jsp:useBean id="comboMotivoProvv"        scope="request" class="java.lang.String"/>
<jsp:useBean id="comboEsitiProvvedimento" scope="request" class="java.lang.String"/>

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>

<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>

<jsp:useBean id="messaggioEsito"      scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="messaggioRichiesta"  scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="annotazioneEsito"    scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>

<jsp:useBean id="ufficioEsito"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioInoltro" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="evento"         scope="request" class="siap.sico.evento.model.EventoModel"/>

<jsp:useBean id="modalita"         scope="request" class="java.lang.String"/>
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
<title>[S.I.E.S.] - TRASMISSIONE PER COMPETENZA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
 
  function gestisciEsito(){
    var codEsito = document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.value;
    if (codEsito=="-" || codEsito=="<%=ICostantiJMS.PRESAINCARICO%>"){
      // Nulla o Atti Presi In Carico
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";
      <% if (messaggioEsito.getIdMessaggio()==null) { %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.options.selectedIndex=0;
      <% } %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.value="";
      
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=true;
    }
    else if (codEsito=="<%=ICostantiJMS.ISCRITTO_CLASSE_IV%>"){
      // Iscritto in classe IV
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";
      <% if (messaggioEsito.getIdMessaggio()==null) { %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.options.selectedIndex=0;
      <% } %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=true;
    }
    else if (codEsito=="<%=ICostantiJMS.RESTITUITO%>" ){
      // Restituito
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";
      <% if (messaggioEsito.getIdMessaggio()==null) { %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.options.selectedIndex=0;
      <% } %>
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=false;
    }
    else if (codEsito=="<%=ICostantiJMS.TRASFERITO%>"){
      // Inoltrati
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.options.selectedIndex=0;
//      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=false;
    }
  }



  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3){
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }
  
  function ListaComuni(a_formname,a_fieldname){
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }


  function Verifica(){  
    //======================
    // Data Emissione
    //======================
    if (document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        
    if (document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
       
    var data_to_verify =     document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Emissione non valida.');
      return false;
    }
    
    var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
    if(!CompareDate(data_to_verify,dataOdierna))
    {
      alert('La Data Emissione non può essere superiore alla data odierna');
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
      return false;
    }
    
    // CONTROLLO CHE SIA STATO SELEZIONATA LA TIPOLOGIA DELL'ATTO
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
      alert("E' obbligatorio selezionare la tipologia dell'atto");
      return false;   
    }
    
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='-'){
      alert("E' obbligatorio selezionare l'oggetto del provvedimento.");
      return false;   
    } 

    
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='-'){
      alert("E' obbligatorio selezionare un valore del campo 'Oggetto Atto'.");
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      return false;   
    }     
  
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
       && document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Firmatario è obbligatorio.");  
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()  ;
      return false;
    } 
    
    
    //================================
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.value=='-')
    {
      alert("E' obbligatorio selezionare l'ufficio che ha inviato l'esito.");
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.focus();
      return false;   
    }     
   
    if( document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>.value==''){
      alert("E' obbligatorio selezionare la sede dell'Ufficio che ha inviato l'esito.");
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>.focus();
      return false;   
    } 
    
    if (document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.value.length==1)
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.value='0'
       +document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.value;
        
    if (document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>.value.length==1)
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>.value='0'
       +document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>.value;
       
    var data_to_verify =     document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.value
                        +'-'+document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>.value
                        +'-'+document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data Esito non valida.');
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.focus();
      return false;
    }
    
    if(!CompareDate(data_to_verify,dataOdierna))
    {
      alert('La Data Esito non può essere superiore alla data odierna');
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.focus();
      return false;
    }
    
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.value=='-')
    {
      alert("E' obbligatorio selezionare l'Esito Provvedimento.");
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.focus();
      return false;   
    }
    
    var codEsito = document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.value;
    if (codEsito=="<%=ICostantiJMS.PRESAINCARICO%>"){
      // Atti Presi In Carico nessun dato obbligatorio
    }
    else if (codEsito=="<%=ICostantiJMS.ISCRITTO_CLASSE_IV%>"){
      // Iscritto in classe IV
      if (   document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value==""
          || document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value==""
         )
      {
        alert("E' obbligatorio indicare gli estremi (anno/progressivo) del fascicolo di esecuzione delle misure.");
        return false;
      }
      var anno_sistema = '<%=DateUtils.getSysDate("yyyy")%>';
      var chiave_anno =  document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value;
      if (chiave_anno<1900) {
          alert("Verificare il campo Anno Procedimento, il valore digitato è troppo basso.");
          document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
      }
      if (chiave_anno > anno_sistema) {
          alert("Il campo Anno Procedimento non può superare l'anno corrente.");
          document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.focus();
          return false;
      }
      
      // Verifica delle congruenza del numero digitato con la classe scelta
      var minrange = 40000;
      var maxRange = 50000;
      if ((document.LoadInserisciEsitoTrasmissioneCompetenza.<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value < minrange) ||
          (document.LoadInserisciEsitoTrasmissioneCompetenza.<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value > maxRange) )
      {
        alert ("Il Progressivo del Procedimento di esecuzione delle Misure (classe IV) deve avere un valore compreso tra "+minrange+" e "+maxRange+".");
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
    }
    else if (codEsito=="<%=ICostantiJMS.RESTITUITO%>" ){
      // Restituito nessun dato obbligatorio
    }
    else if (codEsito=="<%=ICostantiJMS.TRASFERITO%>"){
      // Atti Inoltrati:
      if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.value=='-') {
        alert("E' obbligatorio selezionare l'Ufficio a cui sono stati inoltrati gli atti.");
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.focus();
        return false;   
      }    
   
      if( document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.value==''){
        alert("E' obbligatorio selezionare la sede dell'Ufficio a cui sono stati inoltrati gli atti.");
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>.focus();
        return false;   
      }
    }    
    
    return true;
  }
  
  </script>
</head>

<body class="corpo" onLoad="Javascript:gestisciEsito();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">ANNOTAZIONE ESITO TRASMISSIONE ATTI PER COMPETENZA ESECUZIONE MISURE DI SICUREZZA</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <% if (messaggioEsito.getIdMessaggio()==null) { %>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <% } else { %>
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      <% } %>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  

<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Residenza attuale
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile (?) o meno)
// - Misure di sicurezza: in sentenza o in cumulo
//==============================================================================
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
          <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
              DETENUTO PER ALTRA CAUSA
          <% } else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
          <% } %>
        </font>
      </td>
    </tr>
    <%
      // Se Detenuto altra causa: Istituto di detenzione o Altro Luogo
      if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        if( lAltraCausa.getIstitutoDetenzione()!= null ) { %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
              <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
        } // fine istituto di detenzione
      }
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null ) { %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
          </td>
        </tr>
      <%}%>
      <%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(   lPosizione.getCodPosizioneGiuridica() != null
           && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
          <% }
        }
      %>
      <%
      //========================================================================
      // Residenza se presente
      //========================================================================      
      %>
      <% if (residenza!=null && residenza.getIdResidenza()!=null) { %>
      <tr>
        <td class="l">Residenza</td>
        <td class="L"> <font class="campo" ><%=StringUtils.toStringJSP(residenza.toStringaResidenza(),"&nbsp;")%></font></td>
      </tr>
      <% } %>
      <%
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
           )
        {}
        else
        { %>
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
        <%
        //===============
        // Arresti
        //===============
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {}
        else
        { %>
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
      <% }
      }  // fine IF sulla pena residua
      %>
      <%
      //==========================================================================
      // Inserisco il rigo con Data Inizio e Tipo Ergastolo (se presente)
      //==========================================================================
      %>
      <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
           <td class="l">Data Decorrenza Pena</td>
           <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if (penaresidua.getFlagErgastolo() != null) {
           if(penaresidua.getFlagErgastolo().equals("S")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
           <% }
           else if(penaresidua.getFlagErgastolo().equals("D")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
           <% }
         }
      %>
      <%
      //========================================================================
      // Inserisco la data Fine pena
      // - se non libero o comunque detenuto pre altra causa
      // - se non in ergastolo
      // - se data editabile (se lapena residua recuperata è non validata)
      //   inserisco i campi altrimenti solo label
      //========================================================================
      if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) ) {
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
          %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           </td>
          <% }
          else if( penaresidua.getDataFine() != null) {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
            %>
               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td>
            <%
            }else{%>
               <td class="l">Data Fine Pena</td>
               <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td><%
            }
          }
        }
      } %>
    </tr>
  </table>
  --%>
  <%
  //===================================================================
  // Misure di sicurezza
  //===================================================================
  %>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <table>
  <% if (penacumulo!=null && penacumulo.getIdPenaCumulo()!=null) { %>
  <tr><td class="Titolo" colspan="3">Misure Sicurezza in Cumulo</td></tr>
  <tr>
    <td class="l">
      <font class="campo">        
      <%=StringUtils.toStringJSP(penacumulo.getMisuraSicurezza())%>
      </font>
    </td>
  </tr>
  <% } else if (listaMisure != null && listaMisure.size() != 0){ %>
  <tr>
    <td class="Titolo" colspan="3">Misure Sicurezza</td>
  </tr>
  <tr>
    <td class="l">
      <center><font class="label">Natura Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Tipo Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Durata Misura</font></center>
    </td>
  </tr>
      <%
      Iterator lIterMis = listaMisure.iterator();
      while (lIterMis.hasNext())
      {
        MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
      %>
        <tr>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
          </td>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
          </td>
          <td class="l">            
              <font class="l">AA:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;</font>
              <font class="l">MM:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;</font>
              <font class="l">GG:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%></font>
            </font>
          </td>
        </tr>
      <% } %>
  <% } %>
  </table>
--%>

<%
//==============================================================================
//     Dati del Provvedimento
//==============================================================================
%>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEsitoTrasmissioneCompetenza" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciAnnotaEsitoTrasmissione">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(evento.getIdEvento(),"")%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_RICHIESTA%>" value="<%=StringUtils.toStringJSP(messaggioRichiesta.getIdMessaggio(),"")%>">
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_ESITO%>"     value="<%=StringUtils.toStringJSP(messaggioEsito.getIdMessaggio(),"")%>">

  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioRichiesta.getDataInvio(),"dd"),"")%>">
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioRichiesta.getDataInvio(),"MM"),"")%>">
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioRichiesta.getDataInvio(),"yyyy"),"")%>">

  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_OGGETTO_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(messaggioRichiesta.getCodTipoOperazione())%>">

  <input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(messaggioRichiesta.getCodUfficioDestinatario(),"")%>">
  
<table width=90%>
  <tr>
    <td class="Titolo" colspan="2"> Dati Provvedimento </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>  
    <td class="L" >
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd"),DateUtils.getSysDate("dd")) %>" 
             type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"MM"),DateUtils.getSysDate("MM")) %>"   
             type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"yyyy"),DateUtils.getSysDate("yyyy")) %>" 
             type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  <tr>
    <td class="l">Tipologia Provvedimento <font class=ob>(*)</font></td>
    <td class="L">
      <select  name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciEsitoTrasmissioneCompetenza.<%= ICostantiEvento.CAMPO_COD_MOTIVO%>);" Title="Tipologia Atto" >
        <%=comboTipoProvv%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Oggetto Provvedimento <font class=ob>(*)</font></td>
    <td class="L">
      <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
        <%=comboMotivoProvv%> 
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Note</td>
    <td  class="L">
      <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ><%=StringUtils.toStringJSP(contenuto,"")%></textarea>
    </td> 
  </tr>
  
  <tr>
    <td class="l">Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L" >
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" 
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciEsitoTrasmissioneCompetenza','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>          
    </td>
  </tr>
</table>

<%
//===================================================================
//    
//===================================================================
%>
<br>
<table width=90%>
  <tr>
    <td class="Titolo" colspan="2">Esito Trasmissione</td>
  </tr>
<%
if (messaggioEsito.getIdMessaggio()!=null)
{
    // I campi vengono precaricati dal messaggio ricevuto e non sono modificabili

%>    
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>"   value="<%=ufficioEsito.getCodTipoUfficio()%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>" value="<%=ufficioEsito.getDescrComune()%>">

<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO %>" value="<%=DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"dd")%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>"    value="<%=DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"MM")%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>"    value="<%=DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"yyyy")%>">

<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>" value="<%=StringUtils.toStringJSP(annotazioneEsito.getCodEsito(),"")%>">

<% //if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(messaggioEsito.getCodEsito()) ) {%>
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>"  value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveAnno(),"")%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>" value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveProgr(),"")%>">
<% //} %>
   
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>" value="<%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"")%>">

<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>"   value="<%=StringUtils.toStringJSP(ufficioInoltro.getCodTipoUfficio(),"")%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>" value="<%=StringUtils.toStringJSP(ufficioInoltro.getDescrComune(),"")%>">

  <tr>
    <td class="l">Ufficio del Pubblico Ministero</font></td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(ufficioEsito.getDescrTipoUfficio(),"")%></font></td>
  </tr>
  <tr>  
    <td class="l">Luogo</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(ufficioEsito.getDescrComune(),"")%></font></td>
  </tr>  
  <tr>  
    <td class="l">Data Esito</td>
    <td class="L"><font class="campo"><%=DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"dd/MM/yyyy")%></font></td> 
  </tr>
  
  <tr>
    <td class="l">Esito Provvedimento</td>
    <% if (ICostantiJMS.TRASFERITO.equals(annotazioneEsito.getCodEsito()) ) { %>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font>
      <font class="label"> a </font>
      <font class="campo"><%=ufficioInoltro.getDescrTipoUfficio()+" di "+ufficioInoltro.getDescrComune()%> </font>
    </td>
    <% } else {%>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font></td>
    <% } %>
  </tr>
  <tr>
    <td class="l">Procedimento di classe IV iscritto <br>(Anno/Progressivo)</td>
    <% if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(annotazioneEsito.getCodEsito()) ) {%>        
    <td class="L"><font class="campo"><%=annotazioneEsito.getChiaveAnno()%>/<%=annotazioneEsito.getChiaveProgr()%></font></td>
    <% } else { %>
    <td class="L"><font class="campo">&nbsp;</font></td>
    <% } %>
  </tr>  
  <tr>
    <td class="l">Motivazioni Esito</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"&nbsp;")%></font></td> 
  </tr>
    
<%    
}
else
{
%>
  <tr>
    <td class="l">Ufficio del Pubblico Ministero <font class=ob>(*)</font></td>
    <td class="L">
      <select title="Sede Ufficio Pubblico Ministero"  name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>" >
        <%=comboUfficiPM %>
      </select>
    </td>
  </tr>
  <tr>  
    <td class="l">Luogo <font class=ob>(*)</font></td>
    <td class="L">      
      <input title="Sede Ufficio Esito" type="text" maxlength="35" size="30"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>"              
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getDescrComuneUfficioEsito(),"")%>">
      <a href="Javascript:ListaUfficiComuni('LoadInserisciEsitoTrasmissioneCompetenza','<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>'
                                           , document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>[document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <tr>  
    <td class="l">Data Esito <font class=ob>(*)</td>
    <td class="L">
      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO %>"
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"dd"),"")%>" 
             type="text" size="2" maxlength="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO %>"
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"MM"),"")%>" 
             type="text" size="2" maxlength="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO %>"
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"yyyy"),"")%>" 
             type="text" size="4" maxlength="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  
  <tr>
    <td class="l">Esito Provvedimento <font class=ob>(*)</font></td>
    <td class="L">
        <select  Title="Oggetto Atto"  name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>" onChange="Javascript:gestisciEsito();">
          <%=comboEsitiProvvedimento%>
        </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Inoltrato a Ufficio del Pubblico Ministero <font class=ob>(*)</font></td>
    <td class="L">
      <select title="Sede Ufficio Pubblico Ministero"  name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>" disabled="disabled">
        <%=comboUfficiPMInoltro %>
      </select>
    </td>
  </tr>
  
  <tr>  
    <td class="l">Luogo <font class=ob>(*)</font></td>
    <td class="L">      
      <input title="Sede Ufficio Inoltro" type="text" maxlength="35" size="30" disabled="disabled"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>"              
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getDescrComuneUfficioInoltro(),"")%> ">
      <a href="Javascript:ListaUfficiComuni('LoadInserisciEsitoTrasmissioneCompetenza','<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_INOLTRO%>'
                                           , document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>[document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_INOLTRO%>.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <tr>
    <td class="l">Procedimento di classe IV iscritto <br>(Anno/Progressivo)</td>        
    <td class="L">
      <input type="text" Title="Anno SIEP "  maxlength="4" size="4"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>"  
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveAnno(),"")%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             disabled="disabled" >
      /
      <input type="text" Title="Numero SIEP " maxlength="5" size="15"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>" 
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveProgr(),"")%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
             disabled="disabled" >
    </td>
  </tr>
  
  <tr>
    <td class="l">Motivazioni Esito</td>
    <td  class="L">
      <TEXTAREA title="Contenuto" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>" cols=90 rows=5 disabled="disabled"><%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"")%></textarea>
    </td> 
  </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L" colspan="7">    
      <select Title="Altro Destinatario" class="small" name="AltroDestinatario" >
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>   
    <td class="L" colspan="3">
      <input title="Sede Altro Destinatario" type="text" name="SedeAltroDestinatario" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciEsitoTrasmissioneCompetenza','SedeAltroDestinatario');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td> 
  </tr>  
  --%>
<% } %>  
  <tr>
    <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    </td>
  </tr>
</table>

</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciEsitoTrasmissioneCompetenza");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
  
  // DATA ESITO
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>","req","Il campo Giorno Esito è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>","req","Il campo Mese Esito è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>","req","Il campo Anno Esito è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>","lt=2050");

</script>
</body>
</html>