<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>
<%@ page import="siap.sico.ufficio.util.UfficioAccorpatoUtils" %>


<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%-- MEV_2025-48: Atti pervenuti per competenza al cumulo --%> 
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="tipoUfficioRichiedente" scope="request" class="java.lang.String"/>
<jsp:useBean id="ChiaveAnnoCumulante" scope="request" class="java.lang.String"/>
<jsp:useBean id="ChiaveProgrCumulante" scope="request" class="java.lang.String"/>
<%-- MEV_2025-48 - FINE --%>

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>

<%-- // STUB 15/03/2005 Parametri per la visualizzazione dei criteri di ricerca --%>
<jsp:useBean id="codUfficio"   scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>

<jsp:useBean id="flagIncludeInCarico" scope="request" class="java.lang.String"/>
<jsp:useBean id="data1"               scope="request" class="java.lang.String"/>
<jsp:useBean id="data2"               scope="request" class="java.lang.String"/>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>


<%
//==================================================================================
// 11/09/2015 jsp invocata solo dalla ActLoadRicercaAttiCompetenzaRicevuti per la
// visualizzazione degli atti ricevuti per competenza (00066) e Seguito Atti (00078) 
// ancora da prendere in carico (CUMULO)
// A dispetto da quanto previsto nel codice, non sono previsti criteri di 
// ricerca e non vengono passati i messaggi gia'  presi in carico visualizzati
// in altra JSP.
//==================================================================================

String lHrefIstruttoria = "";
if(IstruttoriaCumulo !=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null) {
  lHrefIstruttoria="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
}

%>


<html>
  <head>
    <title>[S.I.E.S.] - Messaggi Ricevuti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <%-- MEV_2025-48: Atti pervenuti per competenza al cumulo --%> 
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    <script language="JavaScript">
    
      function impostaDataOdierna(campo_giorno, campo_mese, campo_anno, dataOdierna){    
        day=dataOdierna.substring(0,2);
        month=dataOdierna.substring(3,5);
        year=dataOdierna.substring(6,10);
        document.getElementsByName(campo_giorno).item(0).value = day;
        document.getElementsByName(campo_mese).item(0).value = month;
        document.getElementsByName(campo_anno).item(0).value = year;      
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
      
      function impostaDate (periodo){
    	  pulisciData ('inizio');
    	  pulisciData ('fine');
    	  
    	  var dataInizioRicalcolata = new Date();
    	  var meseCorrente = dataInizioRicalcolata.getMonth();
    	  if (periodo =='2M') 
    		  dataInizioRicalcolata.setMonth(meseCorrente - 2);
    	  else if (periodo =='6M') 
    		  dataInizioRicalcolata.setMonth(meseCorrente - 6);
          else if (periodo =='1A') 
        	  dataInizioRicalcolata.setMonth(meseCorrente - 12);

    	  var giorno = dataInizioRicalcolata.getDate()>9 ? dataInizioRicalcolata.getDate() : "0"+dataInizioRicalcolata.getDate();
    	  var mese   = (dataInizioRicalcolata.getMonth()+1)>9 ? (dataInizioRicalcolata.getMonth()+1) : "0"+(dataInizioRicalcolata.getMonth()+1);
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value = giorno;
          document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value = mese; 
          document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value = dataInizioRicalcolata.getYear();
    	  
    	  var dataOdierna = new Date();
    	  var giorno = dataOdierna.getDate()>9 ? dataOdierna.getDate() : "0"+dataOdierna.getDate();
    	  var mese   = (dataOdierna.getMonth()+1)>9 ? (dataOdierna.getMonth()+1) : "0"+(dataOdierna.getMonth()+1);
          document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value = giorno; 
          document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value = mese; 
          document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value = dataOdierna.getYear(); 
      } 
      
      function ListaUfficiPerTipo(a_formname, a_fieldname )
      {
        codTipoUfficio = document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      
      function resetSede(){
          document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value="";
      }
      
      function Verify(){
          var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
          
          // Data Trasmissione dal
          var data_dal =     document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value
                        +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value
                        +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
          if (!ControllaDataPassaVuota(data_dal)){
            alert('Data Ricezione Inizio periodo non corretta');      
            document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.focus();
            return false;
          }
          
          if (data_dal!='//'){
            if(!CompareDate(data_dal,dataOdierna))
            {
              alert("La Data di Ricezione Iniziale non puo' essere superiore alla data odierna");
              document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.focus();
              return false;
            }
          }
          
          
          // Data Trasmissione AL
          var data_al =      document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value
                        +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value
                        +'/'+document.f.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;
          if (!ControllaDataPassaVuota(data_al)){
            alert('Data Ricezione Fine periodo non corretta');
            document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
            return false;
          }
          
          if (data_al!='//'){
            if(!CompareDate(data_al,dataOdierna))
            {
              alert("La Data di Ricezione Finale non puo' essere superiore alla data odierna");
              document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
              return false;
            }
          }        
        
          if (data_dal!='//' && data_al!='//'){
            if(!CompareDate(data_dal,data_al))
            {
              alert("La Data di Ricezione finale non puo' essere inferiore alla data iniziale");
              document.f.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.focus();
              return false;
            }
          }  
          
          // Ripulisco la lista
          $('#divRisultatoRicerca').hide();
          
          
          return true;
        }      
    </script>
    
    <style>
    	a.noChangeColor:link, a.noChangeColor:visited {
    		color:#0000FF;
    		text-decoration: none;
    	}
    </style>
    <%-- MEV_2025-48: FINE --%> 
  </head>

<body class="corpo">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione : </font><font class="campo">Lista Atti Ricevuti per Competenza - in attesa di presa in carico</font>&nbsp;&nbsp;
        </td>
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
 
  <br>


<% if(IstruttoriaCumulo !=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <br>
<%
} else {
  // Visualizzazione Criteri di Ricerca
  // DA CORREGGERE i criteri non vengono mai indicati per questo tipo di
%>


  <% if (flagIncludeInCarico.equals("S") || (data1.length()>0 && data2.length()>0) && 1==2) {%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
      <%
      if(codUfficio.length()>1 )
      {%>
        <tr>
          <td class="lVerdeNB">Ufficio di provenienza : <%=descrUfficio.toUpperCase()%></td>
        </tr>
<%    }

      if (data1.length()>0 && data2.length()>0)
      {
%>
        <tr>
          <td class="lVerdeNB"> Dalla data: <%=data1%>&nbsp;&nbsp;&nbsp;
           alla data : <%=data2%></td>
        </tr>
<%    }

      if(flagIncludeInCarico.equals("S"))
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizza anche gli atti gia presi in carico (in Verde) </td>
        </tr>
<%    }
    }%>

  </table>
<% } %>


<%
/* ===================================================== */
/* MEV_2025-48: Atti pervenuti per competenza al cumulo */
/* ===================================================== */
%>
<%
//==============================================================================
// Sezione con i criteri di ricerca
//==============================================================================
%>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1"> 
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=StringUtils.toStringJSP(IstruttoriaCumulo.getIdIstruttoriaCumulo(),"")%>"> 
  
  
  <table cellspacing="2" cellpadding="2" width="85%">
    <tr>
      <td class="Titolo" colspan="6"> Criteri di ricerca 
        <a id="idHrefRicerca" href="Javascript:collassa('tabCriteriRicerca');"><img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0"/></a>
      </td>
    </tr>
  </table>
  <table id="tabCriteriRicerca" cellspacing="2" cellpadding="2" width="85%" >
    <tr>
      <td class="l">Data ricezione&nbsp;</td>
      <td class="l">&nbsp;Dalla data &nbsp;
        <input type="text" id="idDatIniGG" Title="Data di trasmissione inizio"  maxlength="2" size="2"
               class="dataGG"
               name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
         -
        <input type="text" id="idDatIniMM" Title="Data di trasmissione inizio"  maxlength="2" size="2"
               class="dataMM"
               name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         -
        <input type="text" id="idDatIniAA" Title="Data di trasmissione inizio"  maxlength="4" size="4"
               class="dataAA"
               name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        <a href="Javascript:impostaDataOdierna('<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>','<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>','<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
          <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
        <a href="Javascript:pulisciData('inizio');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        &nbsp;&nbsp;&nbsp;&nbsp;

        <a class="noChangeColor" href="Javascript:impostaDate('2M');">Ultimi 2 Mesi</a>&nbsp;&nbsp;-&nbsp;&nbsp;
        <a class="noChangeColor" href="Javascript:impostaDate('6M');">Ultimi 6 Mesi</a>&nbsp;&nbsp;-&nbsp;&nbsp;
        <a class="noChangeColor" href="Javascript:impostaDate('1A');">Ultimo Anno</a>      		
      </td>

      <td class="l">&nbsp;Alla data &nbsp;
        <input type="text" Title="Data di trasmissione fine"  maxlength="2" size="2"
               class="dataGG"
               name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
         -
        <input type="text" Title="Data di trasmissione fine" maxlength="2" size="2" 
               class="dataMM"
               name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         -
        <input type="text" Title="Data di trasmissione fine"  maxlength="4" size="4"
               class="dataAA"
               name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>"  
               value="<%=StringUtils.toStringJSP(request.getAttribute(ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE),"")%>"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          <a href="Javascript:impostaDataOdierna('<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>','<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>','<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
            <img src="<%=IWebConstants.IMAGES_DIR%>Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna"></a>
          <a href="Javascript:pulisciData('fine');"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
      </td>
    </tr>
    <% if(IstruttoriaCumulo.getIdIstruttoriaCumulo()==null){%>
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
      <td class="l">Procedimento Trasmesso</td>
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
               >
      </td>
    </tr>  
    
    <tr>
      <td class="l">Procedimento Cumulante</td>
      <td class="l" colspan="2">&nbsp;
        Anno/Numero &nbsp;
    	<% if(IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
        <input type="text" title="Anno Procedimento Cumulante" maxlength="4" size="4"
               name="<%=ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE%>" 
               value="<%=StringUtils.toStringJSP(ChiaveAnnoCumulante,"")%>"
               style="background-color: #e9ecef; cursor: default"
               readonly
               >
        /
        <input type="text" title="Numero Procedimento Cumulante"  maxlength="14" size="18"
               name="<%=ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE%>"
               value="<%=StringUtils.toStringJSP(ChiaveProgrCumulante,"")%>"
               style="background-color: #e9ecef; cursor: default;"
               readonly
               >
    	<% } else {%>
        <input type="text" title="Anno Procedimento Cumulante" maxlength="4" size="4"
               name="<%=ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE%>" 
               value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE),"")%>"
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero Procedimento Cumulante"  maxlength="14" size="18"
               name="<%=ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE%>"
               value="<%=StringUtils.toStringJSP(request.getParameter(ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE),"")%>"
               onkeypress="return TicTabNumField(this,event)" 
               >	
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
    <% } %>
  </table>  
  
  <br>
  
  <table cellspacing="2" cellpadding="2" width="35%">  
    <tr>
      <td>
         <input class="bottone" type="submit" name="Ricerca" value="Ricerca">
      </td>
    </tr>
  </table>
</form>    
<%
/* ====================================== */
/*    FINE - MEV_2025-48                  */
/* ====================================== */
%>


<div id="divRisultatoRicerca">


<% if (Messaggi.size() == 0) {%>
  <br>
  <%-- MEV_2025-48: Atti pervenuti per competenza al cumulo --%>
  <table cellspacing="2" cellpadding="2" width="98%">
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero Fascicolo Cumulante</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Tipo Operazione</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Azioni</td>
    </tr>
    <tr>
      <td class="c" colspan="7">  <br>Non sono presenti atti ricevuti che soddisfano i criteri di ricerca selezionati<br> </td>
    </tr>
  </table>
  <%-- MEV_2025-48:FINE --%>
<% } else { %>

<div align="left">
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Anno/Numero SIEP</td>
      <td class="int">Anno/Numero Fascicolo Cumulante</td>
      <td class="int">Soggetto</td>
      <td class="int">Data Arrivo</td>
      <td class="int">Tipo Operazione</td>
      <td class="int">Ufficio Mittente</td>
      <td class="int">Azioni</td>
    </tr>
    
    
    
<%

  UfficioAccorpatoUtils lUffAccorpUtils = new UfficioAccorpatoUtils();

  String coloreLinea = "c"; 
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    coloreLinea = "c";
    if (lMess.getFlagVisto().compareTo("S")==0)
      coloreLinea = "cVerde";


    UfficioAccorpatoModel lUfficioAccorpato = null;
    lUfficioAccorpato = lUffAccorpUtils.getUfficioAccorpatoByCodAccorpanteProgr ( lMess.getChiaveUfficioSiep(),  lMess.getChiaveProgrSiep());
    
    String lStrFascicoloRicevuto = null;
    if (lUfficioAccorpato!=null){
      BigDecimal lProgOrig = (lMess.getChiaveProgrSiep()).subtract(new BigDecimal(lUfficioAccorpato.getIncrProgressivo()));
      lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lProgOrig;
      lStrFascicoloRicevuto += "<br> <font class=\"cRosso\">(Ex "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrizione()+")</font>"; 
    }
    else {
      lStrFascicoloRicevuto = lMess.getChiaveAnnoSiep()+"/"+lMess.getChiaveProgrSiep();
    }

%>
    <tr>
    
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lStrFascicoloRicevuto)%></td>
      <%--
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>
      --%>
      
      <% if (lMess.getChiaveAnnoFasCumulante()!=null) {%>
      <td class="<%=coloreLinea%>">
      <% } else { %>
      <td class="cRosso">
      <% } %>
        <%= StringUtils.toStringJSP(lMess.getChiaveAnnoFasCumulante(),"n.d.")%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrFasCumulante(),"n.d.")%>
      </td>
      <td class="<%=coloreLinea%>"><%= lMess.getCognomeSoggetto()%>&nbsp;<%= lMess.getNomeSoggetto()%></td>
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm:ss"))%></td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrTipoOperazione()%>&nbsp;</td>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() +" "+ lMess.getDescrSedeUfficioMittente()%></td>
      <td class="<%=coloreLinea%>">
      
      <%
      String lStrHref = IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.presaincarico.action.ActDettaglioPresaincaricoCompetenza";
      lStrHref+="&"+ICostantiMessaggio.CAMPO_ID_MESSAGGIO+"="+lMess.getIdMessaggio();
      lStrHref+="&TornaQui="+TornaQui;
      lStrHref+="&"+request.getParameter("CampoAzioneChiamante")+"="+request.getParameter("ValoreAzioneChiamante");
      lStrHref+=lHrefIstruttoria;
      %>
  <table>
    <tr>
      <td>
        <%-- MEV_2025-48: Atti pervenuti per competenza al cumulo --%>
        <%--               Segnalazione BLOB non leggibili --%>        
      	<% if (ICostantiJMS.ERRORE_DEPLOY.equals(lMess.getCodEsito())){ %>
        <img src="/images/attenzione.jpg" width="12" height="12" class="alertIcon" alt="Attenzione. Il contenuto del messaggio ricevuto non risulta leggibile. Se necessario, richiedere una nuova trasmissione" border="0"></b></font>
        <% } else { %>
        <a href="<%=lStrHref%>">
          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
        </a>
        <% } %>
      </td>
    </tr>
  </table>
      
      
      <%--
        <jsp:include page="buttonsMessaggi.jsp">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>
      --%>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
<% } %>
   </div> 
 
  </body>
  <%-- MEV_2025-48: Atti pervenuti per competenza al cumulo --%>
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
  frmvalidator.setAddnlValidationFunction("Verify"); 
  </script>
  <%-- MEV_2025-48: FINE --%>
</html>