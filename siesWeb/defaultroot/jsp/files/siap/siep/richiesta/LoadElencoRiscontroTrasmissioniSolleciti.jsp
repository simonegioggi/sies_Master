<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>


<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%> 
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="tipoUfficioRichiedente" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoEsitoTrasfCompCk"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoEsitoSeguitoAttiCk" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoEsitoComuProcureCk" scope="request" class="java.lang.String"/>


<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%> 

<jsp:useBean id="Messaggi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la visualizzazione dei messaggio di presa in carico atti
// (trasmissione x competenza cumulo)
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Riscontro Trasmissioni/Solleciti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni --%> 
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
          
          $("[name='<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','yellow');
          $("[name='<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','yellow');
          $("[name='<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','yellow');
          
          setTimeout (function (){
              $("[name='<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','');
              $("[name='<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','');
              $("[name='<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>']").css('background-color','');  
          },500);
      } 
      
      function ListaUfficiPerTipo(a_formname, a_fieldname )
      {
        codTipoUfficio = document.f.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      
      function resetSede(){
          document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value="";
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
          
          if (   !document.getElementById("<%=ICostantiSicoJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI%>").checked
        	  && !document.getElementById("<%=ICostantiSicoJMS.ESITO_TRASFERIMENTO_COMPETENZA%>").checked
        	  && !document.getElementById("<%=ICostantiSicoJMS.ESITO_SEGUITO_ATTI%>").checked
             ) 
          {
              alert("Selezionare almeno una tipologia di operazione");
              document.getElementById("<%=ICostantiSicoJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI%>").focus();
              return false;        	  
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
<%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni --%>    
    
  </head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Riscontro Trasmissioni/Solleciti</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoria.action.ActIstruttorieGriglia">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<%
/* ===================================================== */
/* MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni  */
/* ===================================================== */
%>
<%
//==============================================================================
// Sezione con i criteri di ricerca
//==============================================================================
%>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActLoadRicercaTrasmissioniSolleciti">
  <input type="hidden" name="<%=IWebConstants.NUM_PAGE%>" value="1"> 
  
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

      <td class="l" colspan="2">&nbsp;Alla data &nbsp;
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

    <tr>
      <td class="l">Ufficio Mittente</td>
      <td class="l" colspan="3">
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
      </td>
    </tr>

    <tr>
      <td class="l">Procedimento Trasmesso</td>
      <td class="l" colspan="3">&nbsp;
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
      <td class="l">Tipo operazione</td>
      <td class="l" colspan="2">
        <input type="checkbox" value="N" <%=tipoEsitoComuProcureCk %> 
               name="<%=ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI%>" 
               id="<%=ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI%>">
        &nbsp;COMUNICAZIONE CUMULO PROCURE COMPETENTI<br>
        <input type="checkbox"  value="N" <%=tipoEsitoTrasfCompCk %> 
	           name="<%=ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA%>" 
	           id="<%=ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA%>" >
        &nbsp;ESITO TRASFERIMENTO COMPETENZA<br>
        <input type="checkbox" value="N" <%=tipoEsitoSeguitoAttiCk %>
               name="<%=ICostantiJMS.ESITO_SEGUITO_ATTI%>"  
               id="<%=ICostantiJMS.ESITO_SEGUITO_ATTI%>">
        &nbsp;ESITO SEGUITO ATTI
      </td>
    </tr>
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
/* ===================================================== */
/* MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni  */
/* ===================================================== */
%>

  <br>  
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <br>
  


<div align="left" id="divRisultatoRicerca">
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="int">Anno/Numero&nbsp;<br>SIEP</td>
      <td class="int">Ufficio Destinatario Atti&nbsp;</td>
      <td class="int">Anno/Numero Fascicolo Cumulante&nbsp;</td>
      <td class="int">Tipo operazione&nbsp;</td>
      <td class="int">Data Trasmissione&nbsp;<br>Esito</td>
      <td class="int">Esito&nbsp;</td>
      <td class="int">Motivazioni&nbsp;</td>
      <td class="int">Data Ultimo Sollecito&nbsp;</td>
      <td class="int">Azioni&nbsp;</td>
    </tr>
    
<% if (Messaggi.size() == 0) { %>
<tr>
	<td class="c" colspan="8">
	   Non sono presenti Esiti che soddisfano i criteri di ricerca selezionati
	</td>    
</tr>
<%} else { %>
    
<%
  String coloreLinea = "c"; // STUB 15/03/2005
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    // STUB 15/03/2005
    coloreLinea = "c";
   // if (lMess.getFlagVisto().compareTo("S")==0)
   //   coloreLinea = "cVerde";
   String strCumulante = "";
   if (lMess.getChiaveAnnoFasCumulante()!=null)
       strCumulante = StringUtils.toStringJSP(lMess.getChiaveAnnoFasCumulante())+"/"+StringUtils.toStringJSP(lMess.getChiaveProgrFasCumulante());
   else
       strCumulante = "<font style='color:red;'>n.d./n.d.</font>";
%>
  <tr>
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>     
    <td class="<%=coloreLinea%>"><%= lMess.getDescrUfficioMittente() + " " + lMess.getDescrSedeUfficioMittente()%></td>     
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(strCumulante)%></td>
    <td class="<%=coloreLinea%>"><%= lMess.getDescrTipoOperazione()%></td>     
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
    
<%	if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01001")==0 )	// Atti Presi in carico
	{%>    
		<td class="cVerde"><%= lMess.getDescrEsito()%></td>
<%	}
	else if(lMess.getCodEsito()!=null && lMess.getCodEsito().compareTo("01007")==0 )	// Atti Rigettati
	{%>
		<td class="cRosso"><%= lMess.getDescrEsito()%></td>
<%	}
	else	// Atti Trasmessi o Restituiti
	{%>
      <td class="<%=coloreLinea%>"><%= lMess.getDescrEsito()%></td>
<%	} %>
      
      <td class="<%=coloreLinea%>"><%if(lMess.getNote()!=null){%><%=lMess.getNote()%><%} %>&nbsp;</td>

<%	if(lMess.getDataUltimoSollecito()!=null )
	{%>      
      <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataUltimoSollecito(),"dd-MM-yyyy HH:mm"))%></td>
<%	}
	else
	{	%>
		<td class="<%=coloreLinea%>"> &nbsp; </td>
<%	} %>	      
      <td class="<%=coloreLinea%>">
        <%--jsp:include page="<%=IWebConstants.PG_BUTTONS%>"--%>
        <jsp:include page="<%=ICostantiMessaggio.PG_BUTTONS_MESSAGGIO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lMess.getIdMessaggio()%>" />
          <jsp:param name="CodTipoOperazione" value="<%=lMess.getCodTipoOperazione()%>" />
        </jsp:include>&nbsp;
    </td>
  </tr>
<%
  }
%>
<% } %>
    </table>
  </div>

  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >

  </body>
  <%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission--%>
  <script language="JavaScript" type="text/javascript">
      var frmvalidator = new Validator("f");
      frmvalidator.setAddnlValidationFunction("Verify"); 
  </script>
  <%-- MEV_2025-48 - 2.15 Gestione Annotazioni Trasmission --%>   
  
 
  
</html>