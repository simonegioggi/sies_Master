<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.utente.action.ICostantiUtente" %>
<%@ page import="siap.siep.pagoPaBatch.model.BatchPagopaModel" %>
<%@ page import="siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa" %>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

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
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="[da definire]">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Dettaglio Schedulazione Batch PagoPa</font></td>
      </tr>
    </table>


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
    
  </form>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("dettaglioBatchPagoPa");
  
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>

</html>