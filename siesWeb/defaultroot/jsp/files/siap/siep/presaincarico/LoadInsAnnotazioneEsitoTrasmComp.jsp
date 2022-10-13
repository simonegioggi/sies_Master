<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>



<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>


<%@ page import="siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione"%>



<% // Combo %>
<jsp:useBean id="comboUfficiPM"           scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvv"          scope="request" class="java.lang.String"/>
<jsp:useBean id="comboMotivoProvv"        scope="request" class="java.lang.String"/>
<jsp:useBean id="comboEsitiProvvedimento" scope="request" class="java.lang.String"/>

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>

<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="competenza" scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>


<jsp:useBean id="messaggioEsito"        scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="messaggioTrasmissione" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="messaggioComunicazione" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>


<jsp:useBean id="ufficioEsito"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<%//??? evento solo se in modifica, ma di cosa?%>
<jsp:useBean id="evento"           scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="annotazioneEsito" scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>

<jsp:useBean id="modalita"         scope="request" class="java.lang.String"/>
<%
//==============================================================================
// JSP per l'inserimento dell'annotazione esito trasmissione atti per competenza
// per assorbimento in cumulo
//==============================================================================


  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
     
%>

<html>
<head>
<title>[S.I.E.S.] - TRASMISSIONE PER COMPETENZA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

  function Lock(frm,sel,cb,index){
	if (frm[cb].checked){
		frm[sel].selectedIndex=index;
	 }
  }

  function gestisciEsito(){
    var codEsito = document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.value;

    var idMessaggioComunicazione = <%=messaggioComunicazione.getIdMessaggio()%>;
	if (idMessaggioComunicazione!=null ) {
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_ESITO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=false;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=false;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.readOnly=true;
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=false;    	
    }
    else
    if (codEsito=="-" ){
      // Nulla o Atti Presi In Carico
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=true;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=true;
    }
     else if (codEsito=="<%=ICostantiJMS.PRESAINCARICO%>" || 
  		 	  codEsito=="<%=ICostantiJMS.ASSORBITO_IN_CUMULO%>" ) {
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=true;
    }
    else if (codEsito=="<%=ICostantiJMS.PRESAINCARICO%>" || 
   		 	 codEsito=="<%=ICostantiJMS.ASSORBITO_IN_CUMULO%>" ) {
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.value="";

      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.disabled=false;
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>.disabled=true;
    }
    else if (codEsito=="<%=ICostantiJMS.RESTITUITO%>" ||
    		<%-- Ticket#202210130113 - Aggiunta gestione el codice di RIGETTO --%>
    		 codEsito=="<%=ICostantiJMS.RIGETTATO%>"){
      // Restituito
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value="";
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value="";

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
  
  function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
    day=dataOdierna.substring(0,2);
    month=dataOdierna.substring(3,5);
    year=dataOdierna.substring(6,10);
    document.getElementsByName(campo_giorno).item(0).value = day;
    document.getElementsByName(campo_mese).item(0).value = month;
    document.getElementsByName(campo_anno).item(0).value = year;      
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
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
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
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
      return false;   
    }
    
    if(document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=='-'){
      alert("E' obbligatorio selezionare l'oggetto del provvedimento.");
      document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      return false;   
    } 
  
    if(   document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
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
      
      // Gli estremi del procedimento che determina la competenza se indicati
      // devono essere completi, anno e numero
      if (  (   document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value!=""
             && document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value==""
            )
          ||(   document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.value==""
             && document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>.value!=""
            )         
         )
      {
        alert("Se noti, gli estremi (anno/progressivo) del fascicolo che determina la competenza devono essere inseriti entrambi.");
        document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>.focus();
        return false;      
      }
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
    
    return true;
  }
  
  </script>
</head>

<body class="corpo" onLoad="Javascript:gestisciEsito();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">ANNOTAZIONE ESITO TRASMISSIONE ATTI PER COMPETENZA</font>
      </td>
<%--       <% if (messaggioEsito.getIdMessaggio()==null && 1==2) { %> --%>
<!--       <td class="LBG"> -->
<%--         <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza"> --%>
<%--           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!--         </a> -->
<!--       </td> -->
<%--       <% } else { %> --%>
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%--       <% } %> --%>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEsitoTrasmissioneCompetenza" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.presaincarico.action.ActInsAnnotaEsitoTrasmComp">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(evento.getIdEvento(),"")%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
<%
  if (messaggioComunicazione.getIdMessaggio()!=null) { %>
	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_RICHIESTA%>" value="<%=StringUtils.toStringJSP(messaggioComunicazione.getIdMessaggio(),"")%>">	  
	<input type="HIDDEN" name="idMessaggioComunicazione" value="<%=StringUtils.toStringJSP(messaggioComunicazione.getIdMessaggio(),"")%>">	  


  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_ESITO%>"     value="<%=StringUtils.toStringJSP(messaggioEsito.getIdMessaggio(),"")%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"dd"),"")%>">
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"MM"),"")%>">
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"yyyy"),"")%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_OGGETTO_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(messaggioComunicazione.getCodTipoOperazione())%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(messaggioComunicazione.getCodUfficioDestinatario(),"")%>">

<%  } else { %>  
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_RICHIESTA%>" value="<%=StringUtils.toStringJSP(messaggioTrasmissione.getIdMessaggio(),"")%>">
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_ESITO%>"     value="<%=StringUtils.toStringJSP(messaggioEsito.getIdMessaggio(),"")%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioTrasmissione.getDataInvio(),"dd"),"")%>">
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioTrasmissione.getDataInvio(),"MM"),"")%>">
  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_TRASMISSIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioTrasmissione.getDataInvio(),"yyyy"),"")%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_OGGETTO_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(messaggioTrasmissione.getCodTipoOperazione())%>">

  	<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COD_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(messaggioTrasmissione.getCodUfficioDestinatario(),"")%>">
<%  } %>
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
      <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
        <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
      </a>
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

<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>"  value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveAnno(),"")%>">
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>" value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveProgr(),"")%>">
   
<input type="HIDDEN" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>" value="<%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"")%>">

  <tr>
    <td class="l">Ufficio del Pubblico Ministero</td>
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
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font></td>
  </tr>
  <tr>
    <td class="l">Procedimento che determina la competenza <br>(Anno/Progressivo)</td>
    <% if (ICostantiJMS.PRESAINCARICO.equals(annotazioneEsito.getCodEsito())  || 
   		   ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI.equals(messaggioEsito.getCodTipoOperazione()) ) {%>        
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
	if (messaggioComunicazione.getIdMessaggio()!=null)
	{
	  // I campi vengono precaricati dal messaggio ricevuto e non sono modificabili

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
	             value="<%=StringUtils.toStringJSP(annotazioneEsito.getIdEsitoTrasmissione()!=null?annotazioneEsito.getDescrComuneUfficioEsito():competenza.getDescrLuogoAutoritaComp(),"")%>">
	             
	      <%-- a href="Javascript:ListaUfficiComuni('LoadInserisciEsitoTrasmissioneCompetenza','<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>'
	                                           , document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>[document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.options.selectedIndex].value);"--%>
	         <img src="/images/filefolder.gif" border=0 >
	      <%--/a--%>
	    </td>
	  </tr>
	  
	  <tr>  
	    <td class="l">Data Esito <font class=ob>(*)</td>
	    <td class="L">
	      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_GIORNO_DATA_ESITO %>"
	             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"dd"),"")%>" 
	             type="text" size="2" maxlength="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
	      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_MESE_DATA_ESITO %>"
	             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"MM"),"")%>" 
	             type="text" size="2" maxlength="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
	      <input name="<%= ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ANNO_DATA_ESITO %>"
	             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioComunicazione.getDataInvio(),"yyyy"),"")%>" 
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
	    <td class="l">Procedimento che determina la competenza <br>(Anno/Progressivo)</td>        
	    <td class="L">
	      <input type="text" Title="Anno SIEP "  maxlength="4" size="4"
	             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>"  
	             value="<%=StringUtils.toStringJSP(messaggioComunicazione.getChiaveAnnoFasCumulante(),"")%>"
	             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
	             disabled="disabled" >
	      /
	      <input type="text" Title="Numero SIEP " maxlength="5" size="15"
	             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>" 
	             value="<%=StringUtils.toStringJSP(messaggioComunicazione.getChiaveProgrFasCumulante(),"")%>"
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
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getIdEsitoTrasmissione()!=null?annotazioneEsito.getDescrComuneUfficioEsito():competenza.getDescrLuogoAutoritaComp(),"")%>">
             
      <a href="Javascript:ListaUfficiComuni('LoadInserisciEsitoTrasmissioneCompetenza','<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_COMUNE_UFFICIO_ESITO%>'
                                           , document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>[document.LoadInserisciEsitoTrasmissioneCompetenza.<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_TIPO_UFFICIO_ESITO%>.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  
  <tr>  
    <td class="l">Data Esito <font class=ob>(*)</font></td>
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
    <td class="l">Procedimento che determina la competenza <br>(Anno/Progressivo)</td>        
    <td class="L">
      <input type="text" Title="Anno SIEP "  maxlength="4" size="4"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_ANNO%>"  
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveAnno(),"")%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             disabled >
      /
      <input type="text" Title="Numero SIEP " maxlength="5" size="15"
             name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_CHIAVE_PROGR%>" 
             value="<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveProgr(),"")%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
             disabled >
    </td>
  </tr>
  
  <tr>
    <td class="l">Motivazioni Esito</td>
    <td  class="L">
      <TEXTAREA title="Contenuto" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_NOTE_ESITO%>" cols=90 rows=5 disabled><%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"")%></textarea>
    </td> 
  </tr>
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