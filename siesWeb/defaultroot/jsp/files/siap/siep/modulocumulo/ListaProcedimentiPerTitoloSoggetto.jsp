<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel"%>


<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>


<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<jsp:useBean id="ListaProcedimenti" scope="request" class="java.util.Vector"/>

<jsp:useBean id="tipoUfficioRichiedente" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento" scope="request" class="java.lang.String"/>


 
<%
//==============================================================================
// Jsp per la visualizzazione delle richieste atti ricevute
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    <script language="JavaScript">
      window.focus();
      
      var desktop;
      
      function ListaUfficiPerTipo(a_formname, a_fieldname )
      {
        codTipoUfficio = document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value;
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
      
      
      function insertIT (annoProcedimento, numeroProcedimento)
      {
        //alert('insertIT ANNO = '+annoProcedimento+' NUMERO = '+numeroProcedimento);
        var pupUp = "<%=request.getParameter("popUp")%>";
        if (pupUp=="S"){
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>.value=annoProcedimento;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.value=numeroProcedimento;
  
          try {
            //
            //window.parent.opener.disableCampiTitolo();
          }
          catch(err){  }
  
          window.parent.close();
        }
        else {
          document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.richiesta.action.ActLoadInserisciTrasmissioneCompetenza";
          document.f.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = "<%=request.getParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)%>";
          
          document.f.<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>.value = annoProcedimento;
          document.f.<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>.value = numeroProcedimento;
          
          document.f.submit();  
        }
      }
      
      
      function Verify(){
//return true;
        // Tipo Provvedimento
        if (document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-')
        {
          alert('Indicare il Tipo Provvedimento (Sentenza / Decreto Penale)');      
            document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
            return false;
        }         

        // Autorità Emittente
        if (   document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=='-'
            && document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.value==''
           )
        {
            alert('Dati Autorità Emittente Assenti. Indicare tipologia e sede Autorità Emittente');
            document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
            return false;
        }
        
        if (   document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=='-'
            && document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.value!=''
           )
        {
            alert('Dati Autorità Emittente incompleti. Indicare la tipologia Autorità');
            document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
            return false;
        }

        if (   document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value!='-'
            && document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.value==''
           )
        {
            alert("Dati Autorità Emittente incompleti. Indicare la sede dell'Autorità emittente.");
            document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
            return false;
        }

        var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
        
        // Data Provvedimento
        var data_provv =     document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value
                        +'/'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value
                        +'/'+document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (!ControllaDataPassaVuota(data_provv)){
          alert('Data Provvedimento non corretta');      
          document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
          return false;
        }
        
        if (data_provv!='//'){
          if(!CompareDate(data_provv,dataOdierna))
          {
            alert('La Data Provvedimento non può essere superiore alla data odierna');
            document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
            return false;
          }
        }
        else
        {
            alert('La Data Provvedimento è obbligatoria');
            document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
            return false;
        }  
        
        
        // Data Irrevocabilita
        var data_IRR =      document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
                       +'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
                       +'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
        if (!ControllaDataPassaVuota(data_IRR)){
          alert('Data Irrevocabilità non corretta');
          document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
          return false;
        }
        
        if (data_IRR!='//'){
          if(!CompareDate(data_IRR,dataOdierna))
          {
            alert('La Data Irrevocabilità non può essere superiore alla data odierna');
            document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
            return false;
          }
        }        
      
        if (data_provv!='//' && data_IRR!='//'){
          if(!CompareDate(data_provv,data_IRR))
          {
            alert('La Data Irrevocabilità non può essere inferiore alla Data Provvedimento');
            document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
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
      });
    

    </script>
  </head>
  
<body class="corpo" style="margin-top: 0px; margin-left: 0px;">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Ricerca Procedimenti per Ttolo e Soggetto</font>&nbsp;&nbsp;
    </td>
    <% if (request.getParameter("popUp").equals("N") ) { %>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActLoadDettaglioRichiestaAttiRicevuta&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=request.getParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
    <% } %>    
  </tr>
  

</table>

<div id="divPosizionamento" align="left" style="padding-left: 0px; border: 0px solid black;">

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActRicercaProcedimentiPerTitoloSoggetto">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1">

  <input type="hidden" name="formname" value="<%=request.getParameter("formname")%>">

  <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>"  value="<%=request.getParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)%>">
  <input type="hidden" name="<%=ICostantiJMS.CHIAVE_ANNO_SIEP%>"  value="">
  <input type="hidden" name="<%=ICostantiJMS.CHIAVE_PROGR_SIEP%>" value="">
  <input type="hidden" name="popUp" value="<%=request.getParameter("popUp")%>">

<%
//==============================================================================
//
//==============================================================================
%>
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="Titolo" colspan="6"> Criteri di ricerca 
        <a id="idHrefRicerca" href="Javascript:collassa('tabCriteriRicerca');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0"/></a>
      </td>
    </tr>
  </table>
  <table id="tabCriteriRicerca" cellspacing="2" cellpadding="2" width="100%" >
    <tr>
      <td class="l">Tipo Provvedimento&nbsp;<font class="ob">(*)</font></td>
      <td class="L" colspan="3">
        <select name="<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
          <%=TipoProvvedimento%>
        </select>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Anno/Numero
        &nbsp;&nbsp;
        <input type="text" maxlength="4" size="4"  Title="Anno Sentenza" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_ANNO_SENTENZA),"") %>"
               name="<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
        &nbsp;/&nbsp;
        <input type="text" maxlength="8" size="8" Title="Numero Sentenza"
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_NUMERO_SENTENZA),"") %>"
               name="<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>"  
               onkeypress="return TicTabNumField(this,event)" >         
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L" colspan="3">
        <select Title="Autorità Emittente" 
                name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
        <%=autoritaEmi%>
        </select>
      </td> 
    </tr>
    
    <tr>
      <td class="l">Sede <font class="ob">(*)</font></td>
      <td class="l" colspan="3">     
        <input type="text" maxlength="35" size="35" Title="Luogo Emittente"
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE),"") %>"
               name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>"  
               >
        <a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>'
                                               ,document.f.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.f.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a>                

      </td>
    </tr>
    
    <tr>
      <td class="l">Data Provvedimento <font class="ob">(*)</font></td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO),"") %>" 
               name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO),"") %>" 
               name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO),"") %>" 
               name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="l">Data Irrevocabilità</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA),"") %>" 
               name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA),"") %>" 
               name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA),"") %>" 
               name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Soggetto</td>
      <td class="l" colspan="3" nowrap>&nbsp;
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
        Codice CUI &nbsp;
        <input type="text" title="Codice CUI" maxlength="7" size="7"
               name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>"
               value="<%=StringUtils.toStringJSP(request.getParameter (ICostantiSoggetto.CAMPO_COD_AFIS),"")%>"
          >
      </td>
    </tr>
    <tr>
      <td>
         <input class="bottone" type="submit" name="Ricerca" value="Ricerca">
      </td>
    </tr>
  </table>
</form>  
  
  
<%
//==============================================================================
//
//==============================================================================
%>
<div id="divRisultatoRicerca">
 
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  
  <!--br-->
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="Titolo" colspan="7">Elenco Procedimenti associati trovati </td>
    </tr>
<!--    <tr><td>&nbsp;</td></tr>  -->
    <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Azioni</td>
    </tr>
    <% 
    if (ListaProcedimenti.size() == 0)  
    { %>
    <tr>
      <td class="c" colspan="7">  <br>Non sono presenti richieste atti che soddisfano i criteri di ricerca selezionati<br> </td>
    </tr>
    <% 
    } 
    else 
    {
        Iterator itx = ListaProcedimenti.iterator();
        while ( itx.hasNext())
        {
            //FascicoloSiepModel lFascicolo = (FascicoloSiepModel) itx.next();
            SentenzaSoggettoFascicoloModel lFascicolo = (SentenzaSoggettoFascicoloModel)itx.next();
            %>
            
        <tr>
          <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataProvvedimento(),"dd-MM-yyyy"),"&nbsp;")%>&nbsp;</font></td>
          <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrTipoAutoritaEmittente(),"&nbsp;")%>&nbsp;</font> 
                di  <font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrLuogoEmittente(),"&nbsp;")%>&nbsp;</font></td>
          <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"&nbsp;") %>&nbsp;</font></td>
          <td class="c"><font class="cRosso"><%=StringUtils.toStringJSP(lFascicolo.getChiaveAnnoFascicolo())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(lFascicolo.getChiaveNumeroFascicolo())%>&nbsp;</font></td>
          <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataIscrizione(),"dd-MM-yyyy"),"&nbsp;") %>&nbsp;</font></td>
          <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrStatoFasc(),"&nbsp;") %>&nbsp;</font></td>
          <td class="c">
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(lFascicolo.getChiaveAnnoFascicolo())%>','<%=StringUtils.toStringJSP(lFascicolo.getChiaveNumeroFascicolo())%>');">
              <img src="/images/filefolder.gif" border=0> </a>
          </td>
        </tr>
        <tr>
          <td class="L" colspan="6">
           A carico di: <font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getCognome(),"-")%>&nbsp;<%=StringUtils.toStringJSP(lFascicolo.getNome(),"-")%>&nbsp;</font>
           nato il: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataNascita(),"dd-MM-yyyy"),"-")%>&nbsp;</font>
           in: <font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrComuneNascita(),"&nbsp;")%> (<%=StringUtils.toStringJSP(lFascicolo.getCodProvinciaNascita(),"&nbsp;")%>)&nbsp;</font>
           codice CUI: <font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getCodiceCUI(),"-")%></font>  
          </td>      
        </tr>        
   <%     } // end while  %>
    <% } %>
    </table>
</div> <% //divRisultatoRicerca %>
</div> <% //divPosizionamento %>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>
</html>