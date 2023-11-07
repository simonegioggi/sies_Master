<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.utente.action.ICostantiUtente" %>
<%@ page import="siap.siep.pagoPaBatch.model.BatchPagopaModel" %>
<%@ page import="siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa" %>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="listaLanciJob"        scope="request" class="java.util.Vector" />
<jsp:useBean id="CriteriRicerca"       scope="request" class="siap.siep.pagoPaBatch.model.CriteriRicercaBatchPagopaModel" />
<jsp:useBean id="ConsultaPagamentiJob" scope="request" class="siap.siep.pagoPaBatch.model.QuartzJobModel" />


<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Batch PagoPa - </title>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  function Verify()
  {
    // validazione delle date
    var data_inizio = document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_GIORNO_ESECUZIONE_INIZIALE%>.value
                +'/'+ document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_MESE_ESECUZIONE_INIZIALE%>.value
                +'/'+ document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_ANNO_ESECUZIONE_INIZIALE%>.value;
    var data_fine = document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_GIORNO_ESECUZIONE_FINALE%>.value
              +'/'+ document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_MESE_ESECUZIONE_FINALE%>.value
              +'/'+ document.dettaglioBatchPagoPa.<%=ICostantiBatchPagoPa.CAMPO_ANNO_ESECUZIONE_FINALE%>.value;
    
    if (!ControllaDataPassaVuota(data_inizio))
    {
      alert('Data Esecuzione iniziale non valida');
      return false;
    }      
    
    if (!ControllaDataPassaVuota(data_fine))
    {
      alert('Data Esecuzione Finale non valida');
      return false;
    }  

    if (   data_inizio != '//' && data_fine != '//' 
        && !CompareDate(data_inizio, data_fine) 
       )
    {
      alert("La Data di fine non puo' essere inferiore alla data di inizio");
      return false;
    }   
    
    return true;
  }
    
  </script>
  <style>
    td.int,td.c {
      padding-left: 10px;
      padding-right: 10px;
    }
  </style>
</head>

<body class="corpo">

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="dettaglioBatchPagoPa">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPaBatch.action.ActVisualizzaBatchPagoPa">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Dettaglio Batch PagoPa</font></td>
      </tr>
    </table>

<% if (ConsultaPagamentiJob.getDescrizione()!=null && ConsultaPagamentiJob.getDescrizione().length()>0) { %>
    <br><br>
    <%
    // Visualizzo lo stato attuale del batch
    %>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Programmazione Esecuzione</td>
        <td class="int">Ultima Esecuzione</td>
        <td class="int">Prossima Esecuzione</td>
        <td class="int">Stato</td>
        <td class="int">Azioni</td>
      </tr>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getDescrizione(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getCronExpression(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getLastExec(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getNextSched(), "-")%></td>
        <td class="c"><%=StringUtils.toStringJSP(ConsultaPagamentiJob.getStatus(), "-")%></td>
        <td class="c" nowrap >
        	<a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActLoadConfiguraDemoneConsultazionePagoPa">
            <img  alt="Configura Schedulazione" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" border="0"></a>
          
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActArrestaDemoneConsultazionePagoPa">
            <img  alt="Sospendi Schedulazione" src="<%=IWebConstants.IMAGES_DIR%>arresta.png" style="HEIGHT: 24px;WIDTH:24px" border="0"></a>
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActAvviaDemoneConsultazionePagoPa">
            <img  alt="Attiva Schedulazione" src="<%=IWebConstants.IMAGES_DIR%>avvia.png" style="HEIGHT: 24px;WIDTH:24px" border="0"></a>
        </td>     
      </tr>
    </table>
<% } %>  

    <br>
    <table width="50%">
      <tr>
        <td class="l" >
          Data Esecuzione Iniziale:&nbsp;&nbsp;
          <font class="l">
            <input type="text" maxlength="2" size="2"  title="Giorno Esecuzione Iniziale"                    
                   name="<%=ICostantiBatchPagoPa.CAMPO_GIORNO_ESECUZIONE_INIZIALE%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneDal(),"dd"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" maxlength="2" size="2" title="Mese Esecuzione Iniziale"             
                   name="<%=ICostantiBatchPagoPa.CAMPO_MESE_ESECUZIONE_INIZIALE%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneDal(),"MM"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" maxlength="4" size="4"  title="Anno Esecuzione Iniziale" 
                   name="<%=ICostantiBatchPagoPa.CAMPO_ANNO_ESECUZIONE_INIZIALE%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneDal(),"yyyy"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </font>
          Data Esecuzione Finale:&nbsp;&nbsp;
          <font class="l">
            <input type="text" maxlength="2" size="2" title="Giorno Esecuzione Finale" 
                   name="<%=ICostantiBatchPagoPa.CAMPO_GIORNO_ESECUZIONE_FINALE%>"  
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneAl(),"dd"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" maxlength="2" size="2" title="Mese Esecuzione Finale" 
                   name="<%=ICostantiBatchPagoPa.CAMPO_MESE_ESECUZIONE_FINALE%>" 
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneAl(),"MM"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" maxlength="4" size="4" title="Anno Esecuzione Finale" 
                   name="<%=ICostantiBatchPagoPa.CAMPO_ANNO_ESECUZIONE_FINALE%>"
                   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInizioEsecuzioneAl(),"yyyy"),"")%>"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </font>
          &nbsp;&nbsp;<input type="submit" class="bottone"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>        
    
    
    <br>        
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
    
    <%
    // Visualizzo tabella con l'esecuzione degli ultimi 10 lanci
    %>
    
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="int">Avviato</td>
        <td class="int">Terminato</td>
        <td class="int">Tempo di Esecuzione<br>hh:mm:ss</td>
        <td class="int" title="Interrogazioni per Codice Fiscale">Posizioni Verificate</td>
        <td class="int" title="Interrogazioni per IUV per i Bollettini privi di Codice Fiscale">IUV Verificati</td>
        <td class="int" title="Bollettini aggiornati in stato Pagato">Bollettini Pagati</td>
        <td class="int">Errori Invocazione</td>
        <td class="int">Esito</td>
        <td class="int">Errori</td>
        <td class="int">Azioni</td>
      </tr>
      
      <% 
      for (int i = 0; i< listaLanciJob.size(); i++) 
      { 
        BatchPagopaModel batchModel = (BatchPagopaModel) listaLanciJob.elementAt(i);

        String test = "";
        if (batchModel.getIdBatchPagopa().compareTo(new BigDecimal(723032023))==0) 
        	test = "";  // test = " style='background-color: coral;' ";
        else
        	test = "";

        int maxLength = 50;
        String esito = batchModel.getEsitoEsecuzione();
        if (esito!=null && esito.length()>maxLength)
          esito = esito.substring(0,maxLength);
        String errore = batchModel.getErroreEsecuzione();
        if (errore!=null && errore.length()>maxLength)
          errore = errore.substring(0,maxLength);
        
        String colorErr= "";
        if (batchModel.getNumErroriInvocazione()!=null && batchModel.getNumErroriInvocazione().intValue()>0)
          colorErr = " style='color:red;' ";
      %>
      <tr <%=test %> >
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(batchModel.getDataInizioEsecuzione(),"dd-MM-yyyy - HH:mm:ss"),"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(batchModel.getDataFineEsecuzione(),"dd-MM-yyyy - HH:mm:ss") ,"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(batchModel.getDurataAsString(),"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(batchModel.getNumPosDebitorieVerificate(),"n.d.")%></td>
        <td class="c"><%=StringUtils.toStringJSP(batchModel.getNumIUVVerificati(),"n.d.")%></td>
        <td class="c"><%=StringUtils.toStringJSP(batchModel.getNumBollettiniAggiornati(),"n.d.")%></td>
        <td class="c" <%=colorErr%> ><%=StringUtils.toStringJSP(batchModel.getNumErroriInvocazione(),"n.d.")%></td>
        <td class="c" <%=colorErr%> ><%=StringUtils.toStringJSP(esito,"&nbsp;")%></td>
        <td class="c"><%=StringUtils.toStringJSP(errore,"&nbsp;")%></td>
        <td class="c">
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActLoadDettaglioEsecuzioneBatchPagoPa&<%=ICostantiBatchPagoPa.CAMPO_ID_BATCH_PAGOPA%>=<%=batchModel.getIdBatchPagopa()%>">
            <img  alt="Dettaglio Esecuzione" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"></a>
        </td>
      </tr>
      <% }
 // } //TEST
%>

    </table>    
    
    
  </form>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("dettaglioBatchPagoPa");
  
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>

</html>