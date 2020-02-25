<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.sico.ufficio.util.UfficioAccorpatoUtils" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoUfficioRichiedente" scope="request" class="java.lang.String"/>

<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="Flag_Visto" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Jsp per la visualizzazione delle richieste atti ricevute
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Atti Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    <script language="JavaScript">
      var desktop;
      
      function ListaUfficiPerTipo(a_formname, a_fieldname )
      {
        codTipoUfficio = document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      
      function resetSede(){
        document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value="";
      }
      
      function pulisciData(tipoData){
        if (tipoData=='inizio') {
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value= "";
          document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value= "";
          document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value= "";
        }
        else if (tipoData=='fine'){
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value= "";
          document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value= "";
          document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value= "";
        }
      }
      
      //Funzione utile per impostare la data corrente.
      function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna){    
        day=dataOdierna.substring(0,2);
        month=dataOdierna.substring(3,5);
        year=dataOdierna.substring(6,10);
        document.getElementsByName(campo_giorno).item(0).value = day;
        document.getElementsByName(campo_mese).item(0).value = month;
        document.getElementsByName(campo_anno).item(0).value = year;      
      }
      
      function espandi(idTabella){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        var hrefNew = "Javascript:collassa('"+idTabella+"');";
        
        $('#idHrefRicerca').attr('href',hrefNew);
        $('#idHrefRicerca').children().attr('src',collapseGif);
      
        var tabella = $('#'+idTabella).fadeIn();
      }
      
      function collassa(idTabella){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
        var hrefNew = "Javascript:espandi('"+idTabella+"');";
        
        $('#idHrefRicerca').attr('href',hrefNew);
        $('#idHrefRicerca').children().attr('src',collapseGif);
      
        var tabella = $('#'+idTabella).fadeOut();
      }
      
      
      function gestisciAccorpati(){
        return; // per ora non attivata
        if (document.f.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.value==""){
          // se presente azzero il campo Accorpato. Non è possibile effettare
          // la ricerca per solo accorpato
          if (typeof (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>)!="undefined"){
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value = "0";
          }
        }
      }
      
      
      function Verify(){
        var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        
        // Data Trasmissione dal
        var data_dal =     document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value
                      +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value
                      +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
        if (!ControllaDataPassaVuota(data_dal)){
          alert('Data Ricezione Richiesta Inizio periodo non corretta');      
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.focus();
          return false;
        }
        
        if (data_dal!='//'){
          if(!CompareDate(data_dal,dataOdierna))
          {
            alert('La Data di Ricezione Iniziale non può essere superiore alla data odierna');
            document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.focus();
            return false;
          }
        }
        
        
        // Data Trasmissione AL
        var data_al =      document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value
                      +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value
                      +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;
        if (!ControllaDataPassaVuota(data_al)){
          alert('Data Ricezione Richiesta Fine periodo non corretta');
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
          return false;
        }
        
        if (data_al!='//'){
          if(!CompareDate(data_al,dataOdierna))
          {
            alert('La Data di Ricezione Finale non può essere superiore alla data odierna');
            document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
            return false;
          }
        }        
      
        if (data_dal!='//' && data_al!='//'){
          if(!CompareDate(data_dal,data_al))
          {
            alert('La Data di Ricezione finale non può essere inferiore alla data iniziale');
            document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
            return false;
          }
        }
      
        if (   document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value=='-'
            && document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value!=''
           )
        {
            alert('Dati Ufficio Richiedente incompleti. Indicare la tipologia di ufficio');
            document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.focus();
            return false;
        }

        if (   document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value!='-'
            && document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value==''
           )
        {
            alert("Dati Ufficio Richiedente incompleti. Indicare la sede dell'Ufficio.");
            document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
            return false;
        }
        
        if (document.f.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.value==""){
          // se presente azzero il campo Accorpato. Non è possibile effettare
          // la ricerca per solo accorpato
          if (   typeof (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>)!="undefined"
              && document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value!= "0"
             )
          {
            alert("E' possibile indicare l'ufficio accorpato solo in presenza del numero fascicolo.");
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.focus();
            return false;
          }
        }
 
        
        
        // Ripulisco la lista
        $('#divRisultatoRicerca').hide();
        
        
        return true;
      }
    
      // Incrementa/decrementa il valore numerico del campo utilizzando le frecce
      function slideVal(e,obj) {
        //console.log(e);
        //console.log(e.which);
        
        var maxVal = 2000;
        if (obj.prop("class")=="dataGG")
          maxVal = 31;
        else if (obj.prop("class")=="dataMM")
          maxVal = 12;
        else if (obj.prop("class")=="dataAA")
          maxVal = 2050;
        
        var valore = obj.val();
        if (valore=="") valore = 0;
        valore = parseInt(valore);
        
        // 40 Arrow Down, 37 = Arrow Left, 109 segno -
        if (e.which==40 || e.which==37 || e.which==109) {
          if (valore>1) valore = valore-1; // min 1
          else valore = maxVal;
          obj.val(valore);
        }
        else if (e.which==38 || e.which==39 || e.which==107 ) {
          // Arrow Up
          if (valore<maxVal) valore = valore+1; // max 31
          else valore = 1;
          obj.val(valore);
        }
      }
    
      // On Load
      $(document).ready( function(){
        $('.dataGG,.dataMM,.dataAA').keydown(function(e){slideVal(e,$(this))} );
        
        <% if (elencoUfficiAccorpati.size()>0 && request.getParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)!=null) {%>
          $('#<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>').val('<%=request.getParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)%>');
        <% } %>
      });
    

    </script>
    
    
    
  </head>
  
<body class="corpo">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Richieste atti per competenza pervenute</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActIstruttorieGriglia">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
  <br>




<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadRichiesteAttiRicevute">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1">

  
<%
//==============================================================================
// Sezione con i criteri di ricerca
//==============================================================================
%>
  <table cellspacing="2" cellpadding="2" width="85%">
    <tr>
      <td class="Titolo" colspan="6"> Criteri di ricerca 
        <a id="idHrefRicerca" href="Javascript:collassa('tabCriteriRicerca');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0"/></a>
      </td>
    </tr>
  </table>
  <table id="tabCriteriRicerca" cellspacing="2" cellpadding="2" width="85%" >
    <tr>
      <td class="l">Data ricezione richiesta&nbsp;</td>
      <td class="l">&nbsp;Dalla data &nbsp;
        <input type="text" id="idDatIniGG" Title="Data di trasmissione inizio"  maxlength="2" size="2"
               class="dataGG"
               name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
         -
        <input type="text" id="idDatIniMM" Title="Data di trasmissione inizio"  maxlength="2" size="2"
               class="dataMM"
               name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         -
        <input type="text" id="idDatIniAA" Title="Data di trasmissione inizio"  maxlength="4" size="4"
               class="dataAA"
               name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          <a href="Javascript:impostaDataOdierna('<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>','<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>','<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
            <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
          <a href="Javascript:pulisciData('inizio');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
      </td>

      <td class="l">&nbsp;Alla data &nbsp;
        <input type="text" Title="Data di trasmissione fine"  maxlength="2" size="2"
               class="dataGG"
               name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
         -
        <input type="text" Title="Data di trasmissione fine" maxlength="2" size="2" 
               class="dataMM"
               name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         -
        <input type="text" Title="Data di trasmissione fine"  maxlength="4" size="4"
               class="dataAA"
               name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>"  
               value="<%=request.getParameter(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE)%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          <a href="Javascript:impostaDataOdierna('<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>','<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>','<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
            <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
          <a href="Javascript:pulisciData('fine');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Richiedente</td>
      <td class="l" colspan="2">
        <table>
          <tr>
            <td>
              <select name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" onChange="Javascript:resetSede();">
                <%=tipoUfficioRichiedente%>
              </select>
            </td>
            <td>
              <font class="label">&nbsp;Sede</font>&nbsp;&nbsp;&nbsp;&nbsp;
            </td>
            <td>
              <input type="text" title="Sede Ufficio" maxlength="35" size="35"
                     value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO),"")%>"
                     name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO %>" >
              <a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>');">
                <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
            </td>      
        </tr>
      </table>
    </tr>
    <tr>
      <td class="l">Procedimento Richiesto</td>
      <td class="l" colspan="2">&nbsp;
        Anno/Numero &nbsp;
        <input type="text" title="Anno Procedimento" maxlength="4" size="4"
               name="<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>" 
               value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiJMS.CHIAVE_ANNO_SIEP),"")%>"
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero Procedimento"  maxlength="14" size="18"
               name="<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>"
               value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiJMS.CHIAVE_PROGR_SIEP),"")%>"
               onkeypress="return TicTabNumField(this,event)" 
               onblur="gestisciAccorpati()">
               
        <% 
        if (elencoUfficiAccorpati.size()>0) 
        {
        %>
          <select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>" id="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
          <option value="0" selected>-</option>
          <%
          Iterator it = elencoUfficiAccorpati.iterator();
        
          while (it.hasNext())
          {
            UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
            %>
              <option value="<%=ua.getIncrProgressivo()%>" ><%=ua.getDescrizione()%></option>
            <%
          }
          %>

          </select>
        <% } %>
      </td>
    </tr>
    <tr>
      <td class="l">Soggetto</td>
      <td class="l" colspan="2">&nbsp;
        Cognome &nbsp;
        <input type="text" title="Cognome" maxlength="35" size="35"
               name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSoggetto.CAMPO_COGNOME),"")%>"
               >
        Nome &nbsp;
        <input type="text" title="Nome"  maxlength="35" size="35"
               name="<%=ICostantiSoggetto.CAMPO_NOME%>"
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSoggetto.CAMPO_NOME),"")%>"
               >
      </td>
    </tr>
  </table>
  
  <table cellspacing="2" cellpadding="2" width="35%">  
    <tr><td>  </td></tr>
    <tr>
      <td>
         <input class="bottone" type="submit" name="Ricerca" value="Ricerca">
      </td>
      
      <td class="l"> Cerca Solo Messaggi da Trasmettere </td>
      <td class="l">
      <%if(Flag_Visto.equals("N"))
      	{ %>
      		<input type="checkbox" name="<%=ICostantiSicoJMS.CAMPO_FLAG_VISTO%>" value="N" CHECKED >
      <%}
      	else
      	{ %>
      		<input type="checkbox" name="<%=ICostantiSicoJMS.CAMPO_FLAG_VISTO%>" value="N" >
      <%} %>			
      </td>
    </tr>

  </table>

</form>  
  
<div id="divRisultatoRicerca">
 
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br> 
  
<% if (Messaggi.size() == 0)  { %>
  <br>
  <table cellspacing="2" cellpadding="2" width="98%">
    <tr>
      <td class="int">Procedimento Richiesto<br>Anno/Numero SIEP</td>
      <td class="int">Ufficio Richiedente</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Soggetto</td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
    <tr>
      <td class="c" colspan="5">  <br>Non sono presenti richieste atti che soddisfano i criteri di ricerca selezionati<br> </td>
    </tr>
  </table>
<% } else { %>
  <table cellspacing="2" cellpadding="2" width="98%">
<%if(!Flag_Visto.equals("N"))
  { %>
    <tr>
     <td colspan="2" class="cVerde"> In verde gli atti già trasmessi </td>
   </tr>
<%}  %>  
    <tr>
      <td class="int">Procedimento Richiesto<br>Anno/Numero SIEP</td>
      <td class="int">Ufficio Richiedente</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Soggetto</td>
      <td class="int">Esito</td>
      <td class="int">Azioni</td>
    </tr>
    
<%
  UfficioAccorpatoUtils lUffAccorpUtils = new UfficioAccorpatoUtils();

  String coloreLinea = "c";
  String coloreesito = "c";
  String lesito = "-";
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    coloreLinea = "c";
    lesito = "-";
    coloreesito = "c";
    if (lMess.getFlagVisto().compareTo("S")==0)
    { 
   	   if(lMess.getCodEsito().compareTo("01006")==0)
   	   {	
      		coloreLinea = "cVerde";
      		coloreesito = "cVerde";
      		lesito = "TRASMESSO";
   	   }	
   	   
   	   if(lMess.getCodEsito().compareTo("01007")==0)
   	   {
   	   		coloreesito = "cRosso";
   	   		lesito = "Richiesta Rigettata";
   	   }	
    }  
    String lStrFascicoloRicevuto = null;
    if (lMess.getChiaveAnnoSiep()!=null ) 
    {
      UfficioAccorpatoModel lUfficioAccorpato = null;
      lUfficioAccorpato = lUffAccorpUtils.getUfficioAccorpatoByCodAccorpanteProgr ( lMess.getChiaveUfficioSiep(),  lMess.getChiaveProgrSiep());
      
      if (lUfficioAccorpato!=null){
        BigDecimal lProgOrig = (lMess.getChiaveProgrSiep()).subtract(new BigDecimal(lUfficioAccorpato.getIncrProgressivo()));
        lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lProgOrig;
        lStrFascicoloRicevuto += "<br> <font class=\"cRosso\">(Ex "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrizione()+")</font>"; 
      }
      else {
        lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lMess.getChiaveProgrSiep();
      }
    }
    else {
      lStrFascicoloRicevuto = "<font class=\"cRosso\">n.d.</font>";
    }

%>
    <tr>
      <td class="<%=coloreLinea%>"><%=lStrFascicoloRicevuto%></td>     


      
      <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%>
      	
      <%if (lMess.getMessaggiSollecito()!=null && lMess.getMessaggiSollecito().size()>0) 
 	  {
         Vector lMessaggiSollecito = lMess.getMessaggiSollecito();
         Iterator itxSoll = lMessaggiSollecito.iterator();
         while ( itxSoll.hasNext())
         {
         	  MessaggioModel lMessSoll = (MessaggioModel) itxSoll.next();
         	  if(lMessSoll.getDataInvio()!=null )
         	  {	  
         	  		//DataUltSoll = DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"); %>
         	  	<br>
                <font color="red">Ricevuto Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
<%	  		  } 
       	  } 
	 } 			%>		
      </td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
      <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>
      
      <td class="<%=coloreesito%>"><%=lesito%></td>
      <td class="<%=coloreLinea%>">
        <%
        // Per attivare la cancellazione mettere Modificabile a S
        %>
        <table>
          <tr>
            <td>
              <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
                <jsp:param name="CampoIdEntita"     value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
                <jsp:param name="ValoreIdEntita"    value="<%=lMess.getIdMessaggio()%>" />
                <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
                <jsp:param name="Modificabile"      value="S" />
              </jsp:include>
            </td>
          </tr>
        </table>
      </td>
      
    <% if (lMess.getIsErroreParser()) { %>
      <td>
        <font color="red"><b><img src="<%=IWebConstants.IMAGES_DIR%>attenzione.jpg" width="12" height="12" alt="Attenzione. Messaggio non elaborabile in quanto inviato con una versione SIEP differente da quella attualmente in uso da questo Ufficio" border="0"></b></font>
      </td>
    <% } %>
      
    </tr>
<% } // End while %>
  </table>
  
<% } // end id size()>0%>
</div>
</div>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>