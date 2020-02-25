<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
  
<jsp:useBean id="TipiFontiReato"        scope="request" class="java.lang.String"/> 
<jsp:useBean id="TipiSottonumerazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoAnnotazione"   scope="request" class="java.lang.String"/> 

<jsp:useBean id="RichiestaAlGE"     scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="VectorTitoli"          scope="request" class="java.util.Vector"/>

<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
  
<!--            LoadInsRichiestaGERevocaPenaPrincCum               -->  
<%
   //============================================================================== 
   // Form per l'inserimento delle richieste al GE di Revoca
   // Pena Principale: Depenalizzazione/Incostituzionalità/Illecito Amministrativo
   //============================================================================== 
   
   int TotTitoli = VectorTitoli.size();

 %>

<html>
<head>
  <title> Gestione Cumulo - Richiesteal al GE - Revoca Sentenza abolizione del Reato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.RicRevocaPena.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicRevocaPena.submit();
    }

    function tipoBeneficio(obj)
    {
      // Depenalizzazione     Illecito Amministrativo
      if (obj.value=="004" || obj.value=="017") 
      {   
        $('#NormeDepen').show();        
        $('#tabNormeDepen input[type=text]').prop('disabled',false);
        $('#tabNormeDepen select').prop('disabled',false);
        
        $('#tabNormeDepen select[name=<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>]').val("03");
        $('#tabNormeDepen input[name=<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>]').val('2016');
        if (obj.value=="004") 
          $('#tabNormeDepen input[name=<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>]').val('7');
        else if (obj.value=="017") 
          $('#tabNormeDepen input[name=<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>]').val('8');
        
        $('#NormeInc').hide();
        $('#tabNormeInc input[type=text]').prop('disabled',true);        
      }
      //  Incostituzionalità
      else if (obj.value=="013") 
      {
        $('#NormeDepen').hide();
        $('#tabNormeDepen input[type=text]').prop('disabled',true);    
        $('#tabNormeDepen select').prop('disabled',true);        
         
        $('#NormeInc').show();
        $('#tabNormeInc input[type=text]').prop('disabled',false);        
      }
      else 
      {
        $('#NormeDepen').hide();
        $('#NormeInc').hide();
       
        $('#tabNormeDepen input[type=text]').prop('disabled',true);
        $('#tabNormeDepen select').prop('disabled',true);
       
        $('#tabNormeInc input[type=text]').prop('disabled',true);
      }
    } 
    
    function mettiFonte()
    {
      if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "004") 
      {
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_COD_FONTE %>.value="03";
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_ANNO_FONTE %>.value="2016";
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_NUMERO_FONTE %>.value="7";
      }
      else if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "017")  
      {
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_COD_FONTE %>.value="03";
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_ANNO_FONTE %>.value="2016";
        document.RicRevocaPena.<%= ICostantiReatoCumulo.CAMPO_NUMERO_FONTE %>.value="8";
      } 

    }


    function Verify() 
    { 
      var total = <%=TotTitoli%>;
    
      // Controllo sulla selezione dei checkBox relativa ai Titoli
      var SpuntaTitolo = "NO";
      for (var i = 0; i < document.getElementsByName("idTitoloCompleto").length; i++ )
      {
        //alert ("idTitoloCompleto = "+document.getElementsByName("idTitoloCompleto")[i].checked);
        if(document.getElementsByName("idTitoloCompleto")[i].checked )
        { 
          SpuntaTitolo = "SI";
        }
      }
      
        
      // Controllo sulla selezione dei checkBox relativa ai Reati
      var SpuntaReato = "NO";
      for (var i = 0; i < document.getElementsByName("idReatoSelezionato").length; i++ )
      {
        //alert ("idReatoSelezionato = "+document.getElementsByName("idReatoSelezionato")[i].checked);
        if(document.getElementsByName("idReatoSelezionato")[i].checked )
        { 
          SpuntaReato = "SI";
        }
      }

      
      // Controllo che ci sia almeno un check selezionato
      //alert ("SpuntaTitolo = "+SpuntaTitolo);      
      //alert ("SpuntaReato = "+SpuntaReato);
      if(SpuntaTitolo == "NO" && SpuntaReato == "NO" )
      {
        alert("Attenzione: \nSelezionare almeno un Elemento nella form (Titolo o Reato)");
        return false;
      }
      
      // Data Emissione
      if (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (data_to_verify=='//' )
      {
          alert('Indicare la Data Richiesta');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      }
      
      if (!ControllaData(data_to_verify) )
      {
          alert('Data Richiesta non valida');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      } 
      
      // Data Emissione deve essere <= Data del Giorno
      var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if(!CompareDate(data_to_verify, data_od))
      {
        alert('Data Richiesta NON può essere superiore alla Data Odierna');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
    
      // Estremi Beneficio per Revica pena
      if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "-") 
      {
        alert('Selezionare un Beneficio');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.focus();
        return false;
      }
      
      // Quantum
      var pos = document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.indexOf('.');
      if(pos > 0)
      {
        alert('Inserire correttamente il valore INTERO della Multa');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.focus();
        return false;
      }
        
      pos = document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.indexOf('.');
      if(pos > 0)
      {
        alert('Inserire correttamente il valore INTERO della Ammenda');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.focus();
        return false;
      }
        
      if(document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.value == ''  )
      {
        alert('Digitare il segno + / - quantum pena');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.focus();
        return false;
      }
      
      if( ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '' ||
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value.length == 0 )  &&
        ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '' ||
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value.length == 0  ) &&  
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '' ||
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value.length == 0 ) &&
             
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '' ||
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value.length == 0 ) &&
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '' ||
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value.length == 0 ) &&
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '' ||
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value.length == 0 ) &&  
            
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.length == 0 ) &&
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC.value.length == 0 ) &&  
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.length == 0 ) &&
          ( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC.value.length == 0 ) 
        ) 
      {
        alert('Digitare una quantità di pena');
        document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.focus();
        return false;
      }
      
      // Controllo valori per Depenalizzazione
      if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "004" || 
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "017" ) 
      {
          if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>.value == "-" && 
            (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>.value == "" ||
             document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>.value.length == 0 ) &&
            (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>.value == "" ||
               document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>.value.length == 0 ) && 
              (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>.value == "" ||
               document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>.value.length == 0 ) 
            )  
          {
            alert('Digitare correttamente la norma di legge');
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>.focus();
            return false; 
          }     
        
      }
      // Controllo valori per Incostituzionalità
      else if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "013" )
      {
        if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC %>.value == "" ||
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC%>.value.length == 0 )
        {
          alert('Indicare Anno Sentenza Corte Costituzionale');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC%>.focus();
          return false;
        }
        else if (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC %>.value<1900
          || document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC %>.value><%=DateUtils.getSysDate("yyyy")%>)
        {
          alert('Indicare correttamente Anno Sentenza Corte Costituzionale');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC%>.focus();
          return false;
        }
          
        if( document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_CC %>.value == "" ||
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_CC%>.value.length == 0 )
        {
          alert('Indicare Numero Sentenza Corte Costituzionale');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_CC%>.focus();
          return false;
        }
          
        // Data Sentenza C.C.
        if (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.value.length==1)
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.value='0'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.value;
        if (document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_CC%>.value.length==1)
            document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_CC%>.value='0'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_CC%>.value;

        data_to_verify = document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.value+'/'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_CC%>.value+'/'+document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_CC%>.value;
        if (data_to_verify=='//' )
        {
          alert('Indicare la Data Sentenza Corte Costituzionale');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.focus();
          return false;
        }
            
        if (!ControllaData(data_to_verify) )
        {
          alert('Data Sentenza Corte Costituzionale non valida');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.focus();
          return false;
        } 
            
        // Data sentenza C.C. deve essere <= Data del Giorno
        if(!CompareDate(data_to_verify, data_od))
        {
          alert('La data sentenza Corte Costituzionale NON può essere superiore alla Data Odierna');
          document.RicRevocaPena.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC%>.focus();
          return false;
        }
      } 
      
      return true; 
    } 
    
    
    
    $(document).ready(function(){
       $('#tabNormeDepen input[type=text]').prop('disabled',true);
       $('#tabNormeDepen select').prop('disabled',true);
       
       $('#tabNormeInc input[type=text]').prop('disabled',true);      
    });


  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <%
          if (modalita.equals("I") ) {
        %>
        <font class="campo">Inserimento Richiesta Revoca Sentenza abolizione del Reato &nbsp;</font>
        <%
          } else if( modalita.equals("M") ) {
        %>
        <font class="campo">Modifica Richiesta Revoca Sentenza abolizione del Reato &nbsp;</font>
        <%
          }
        %>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicRevocaPena">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInsRichiestaGERevocaPenaPrincCum">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >

  <%
  //============================================================================
  // Sezione con l'elenco dei reati per titolo
  //============================================================================
  %>
<div id="idTitolo_"  style="display:block" >
<%  String AnnoNumero = "";
  Iterator itx = VectorTitoli.iterator();
  while(itx.hasNext())
  { 
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
%> 
   <table width="95%" align="center">
    <tr>
    
    <table width="95%" align="center">
      <tr>
        <td class="titolo" width="90%">In relazione al Titolo</td>
        <td class="titolo" width="10%" style="color:red" >tutto il Titolo</td>
      </tr>
      
      <tr>
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>" >
<%  
  AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
%>        
    <td class="l" >
      <font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
          <font class="campo"><%=AnnoNumero%></font>&nbsp;
          &nbsp;<font class="label"> del </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp; 
         
          &nbsp;<font class="label"> Emessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoAutoritaEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
          <font class="campo"><%=StringUtils.toStringJSP(lTitolo.getDescrLuogoEmittente(), "") %></font>
          
          &nbsp;<font class="label"> Irrevocabile il  </font>
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
        <td class="c">
          <input type="checkbox" 
                 name="idTitoloCompleto" 
                 value="<%=StringUtils.toStringJSP(lTitolo.getIdTitoloCumulato()) %>">                 
        </td>
      </tr>
    </table>

<%  //  ------------    REATI   --------------
  if(lTitolo.getReatoCircostanzaCumulo()!=null && lTitolo.getReatoCircostanzaCumulo().size() > 0)
  { %>    
    <table width="95%" align="center">
      <tr>
        <td class="c">Reato</td>
        <td class="c">Durata</td>
        <td class="c">Sanzione</td>
        <td class="c">Sel.</td>
      </tr>
<%    String lStringaSanzione="";
    String lStringPenaReato ="";
    for(int kr = 0; kr < lTitolo.getReatoCircostanzaCumulo().size(); kr++ )
    {
      ReatoCircostanzaCumuloModel lReatoCircostanza = (ReatoCircostanzaCumuloModel)lTitolo.getReatoCircostanzaCumulo().get(kr);
        ReatoCumuloModel lReato = lReatoCircostanza.getReatoCum();
        ReatoCumuloModel[] lCircostanze = lReatoCircostanza.getCircostanzeCum();
      
        boolean lFlagAnnoNumero = false;
          if( lReato.getAnnoFonte() != null
               && !lReato.getAnnoFonte().equals("")
               && lReato.getNumeroFonte() != null
               && !lReato.getNumeroFonte().equals("") )
          {
              lFlagAnnoNumero = true;
          }
      %>
      <tr>
        <input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM %>" value="<%=StringUtils.toStringJSP(lReato.getIdReatoCum() ) %>" >
        <td class="l">
<%      //REATO
            if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
            { %>
            <font class="label">
             &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %> : </font>
            </font>
<%           }
             else
             {  %>
             <font class="label">
              &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrReato()) %> : </font>
       </font>  
<%           }  %>    

      <font class="campo">
<%      if(lFlagAnnoNumero)
      {
         if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
           out.println(lReato.getDescrFonte()+" ");
         if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
           out.println(lReato.getAnnoFonte());
         if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
           out.println("/"+lReato.getNumeroFonte());
      }

      if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
         out.println("art."+lReato.getArticolo());
      if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
         out.println(" "+lReato.getDescrSottonumerazione());

      if(!lFlagAnnoNumero)
      {
         if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
           out.println(lReato.getDescrFonte());
      }

        if(lReato.getComma() != null && !lReato.getComma().equals(""))
          out.println(" c. "+lReato.getComma());
      if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
          out.println(" l. "+lReato.getLettera());
      if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
          out.println(" n. "+lReato.getNumero()); 
      
          //CIRCOSTANZE
          if(lCircostanze != null)
          {
              ReatoCumuloModel lCirc = null;
              for(int i=0; i<lCircostanze.length; i++)
              {
                lCirc = lCircostanze[i];
%>    
     ,
     
<%  
           boolean lFlagAnnoNumeroCirc = false;
             if( lCirc.getAnnoFonte() != null
                 && !lCirc.getAnnoFonte().equals("")
                 && lCirc.getNumeroFonte() != null
                 && !lCirc.getNumeroFonte().equals("") )
             {
               lFlagAnnoNumeroCirc = true;
             }
             if(lFlagAnnoNumeroCirc)
             {
               if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                 out.println(lCirc.getDescrFonte()+" ");
               if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
                 out.println(lCirc.getAnnoFonte());
               if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                 out.println("/"+lCirc.getNumeroFonte());
             }
      
             if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
               out.println("art."+lCirc.getArticolo());
             if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
               out.println(" "+lCirc.getDescrSottonumerazione());
      
             if(!lFlagAnnoNumeroCirc)
             {
               if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                 out.println(lCirc.getDescrFonte());
             }

             if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
               out.println(" c. "+lCirc.getComma());
             if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
               out.println(" l. "+lCirc.getLettera());
             if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
               out.println(" n. "+lCirc.getNumero());
          }
              
        }  // Chiude if(Circostanze) 
        
          lStringaSanzione = "";
          lStringPenaReato = "";
          
          // Quantum Pena Detentiva
           if(lReato.getDescrTipoPenaDetentiva()!=null && 
            lReato.getDescrTipoPenaDetentiva().length()>0)
           {
              lStringPenaReato = lReato.getDescrTipoPenaDetentiva();
              
              if (lReato.getNumAnni()!=null)
                {
                  if (lReato.getNumAnni().intValue()!= 0)
                      lStringPenaReato += " Anni " + lReato.getNumAnni();
                }
              
                if (lReato.getNumMesi()!= null)
                {
                  if (lReato.getNumMesi().intValue()!= 0)
                      lStringPenaReato += " Mesi " + lReato.getNumMesi();
                }
              
                if (lReato.getNumGiorni()!= null)
                {
                  if (lReato.getNumGiorni().intValue()!= 0)
                      lStringPenaReato += " Giorni " + lReato.getNumGiorni();
                }
           }
           
          // Quantum Sanzione Pecuniaria
          if(lReato.getDescrTipoSanzione()!=null && lReato.getDescrTipoSanzione().length()>0)
          {
            lStringaSanzione = lReato.getDescrTipoSanzione();
              
                if ( lStringaSanzione.length() > 1)
                {
                  //lStringaSanzione += ""+"&Euro;"+"";
                  lStringaSanzione += ": "+lReato.getSanzionePecuniaria()+" "+"&euro;";
                }
            }
          
  %>      
      </font> 
       </td>
       <td class="l">
     <font class="campo"><%=StringUtils.toStringJSP(lStringPenaReato ,"")%></font>&nbsp; 
     </td>

        <td class="r">
      <font class="campo"><%=StringUtils.toStringJSP(lStringaSanzione ,"")%></font>&nbsp; 
        </td>
        <td class="c">
          <input type="checkbox" name="idReatoSelezionato" value="<%=StringUtils.toStringJSP(lReato.getIdReatoCum() ) %>">
        </td>
      </tr>

<%    } %>      
    </table>
<% } %>

   </tr>  
  </table>   <%// CHIUDE la TABELLONA relativa ad 1 Titolo %>
  <br>
<% } // Chiude ciclo while principale %>    
    
  </div>  

  <table width="95%" align="center">
    <tr>
      <td colspan=6 class="Titolonocap">Richiesta al Giudice dell' Esecuzione</td>
    </tr>
    <tr>
      <td class="l" width="20%" >Data Richiesta </td>
      <td class="L" colspan="2">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td class="l">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
      <td class="l">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" onChange="javascript:tipoBeneficio(this);" >
          <%=TipoAnnotazione%>
        </select>
      </td>
    </tr>
  </table>
 
<div id="NormeDepen"  style="display:none" >
  <table id="tabNormeDepen" width="95%" align="center">
    <tr><td class="Titolonocap" colspan=8> Norma Depenalizzante </td></tr>

    <tr>
      <td class="Titolo">Fonte</td>
      <td class="Titolo">Anno</td>
      <td class="Titolo">Numero</td>
      <td class="Titolo">Articolo</td>
      <td class="Titolo">Art.qualificante</td>
      <td class="Titolo">Comma</td>
      <td class="Titolo">Lettera</td>
      <td class="Titolo">Numero</td>
    </tr>
    <tr>
      <td class="c">
          <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_FONTE%>">
            <%=TipiFontiReato%>
          </select>
      </td>
      <td class="c">
        <input size=4 maxlength=4 title="Anno Fonte" value="" type="text" 
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_FONTE%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"             
               >
      </td>
      <td class="c">
        <input size=6 maxlength=6 title="Numero Fonte" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_FONTE%>">
      </td>
      <td class="c">
        <input size=5 maxlength=5 title="Articolo Fonte" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ARTICOLO%>">
      </td>
      <td class="c">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_SOTTONUMERAZIONE%>">
          <%=TipiSottonumerazione%>
        </select>
      </td>
      <td class="c">
        <input size=10 maxlength=10 title="Comma" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COMMA%>">
      </td>
      <td class="c">
        <input size=2 maxlength=2 title="Lettera" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_LETTERA%>">
      </td>
      <td class="c">
        <input size=2 maxlength=2 title="Numero" value="" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO%>">
      </td>
  </tr>
 </table>
</div>

<div id="NormeInc"  style="display:none" >  
  <table id="tabNormeInc" width="95%" align="center">
    <tr><td class="Titolonocap" colspan=8> Dichiarazione di illegittimità costituzionale </td></tr>
    <tr>
      <td class="l" colspan=2 valign=middle>Sentenza Corte Costituzionale :</td>
      <td class="l" colspan=3><font class="label"> Anno/Numero </font> <font class="ob">(*)</font>&nbsp;&nbsp;
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_CC %>" size=4 maxlength=4 
          onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_CC %>" size=4 maxlength=4 
          onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="c" colspan=3>
        <font class="label">data</font> <font class="ob">(*)</font>&nbsp;&nbsp;
          <input title = "Giorno Sentenza Corte Costituzionale" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_CC %>" 
            maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input title = "Mese Sentenza Corte Costituzionale" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_CC %>" 
            maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          /
          <input title = "Anno Sentenza Corte Costituzionale" type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_CC %>" 
            maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>  
</div>
  
  <%
  //============================================================================
  // Sezione con i quantum
  //============================================================================
  %>
  <table width="95%" align="center">
    <tr>
      <td colspan=6>
        <hr width="100%">
      </td>
    </tr>
    <tr>
      <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
        <select name=<%= ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R %> >
          <option value=""></option>
          <option value="+">+</option>
          <option value="-">-</option>
        </select>
      </td>
      <td class="titolo" colspan=2>Reclusione</td>
      <td width="25">&nbsp;</td>
      <td class="titolo" colspan=2>Arresto</td>
    </tr>
    <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>" 
          maxlength="2" size="2" value="" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>" 
          maxlength="2" size="2" value="" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>" 
          maxlength="4" size="4" value="" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="c">
        <font  class="label">Multa</font><br>
        <input style="align:right" type="text" maxlength="7" size="7" value=""
          name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT"  
          onkeypress="return TicTabNumField(this,event)" >
        ,
        <input style="align:right" type="text" maxlength="2" size="2" value=""
          name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC" 
          onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td width="25">&nbsp;</td>
      <td class=c>
        <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Giorni</font><br>
        <input  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R %>" 
          maxlength="2" size="2" value="" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R %>" 
          maxlength="2" size="2" value="" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R %>" 
          maxlength="4" size="4" value="" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Ammenda</font><br>
        <input style="align:right" type="text" maxlength="7" size="7" value="" 
          name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT" 
          onkeypress="return TicTabNumField(this,event)" >
        ,
        <input style="align:right" type="text" maxlength="2" size="2" value="" 
          name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC" 
          onkeypress="return TicTabNumField(this,event)" >
      </td>
    </tr>
    <tr>
      <td class="c" colspan=5>
        <font class="label" style="vertical-align: top;">Note</font>
        <textarea cols="60" rows="2" name=<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>></textarea>
      </td>
      <td width=20>&nbsp;</td>
    </tr>
  </table>
  
  <table width="95%" align="center">
    <tr>
      <td class="l">
        Anticipazione degli effetti&nbsp;&nbsp;
        <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_APP_PROVVISORIA %>" value="A"  >
      </td>
    </tr>
  </table>  
  
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>


</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("RicRevocaPena");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 