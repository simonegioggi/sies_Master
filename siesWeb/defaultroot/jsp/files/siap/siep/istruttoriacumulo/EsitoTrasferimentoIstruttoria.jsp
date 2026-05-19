<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>


<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>


<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>


<jsp:useBean id="IstruttoriaCumuloNew" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<%
//==============================================================================
//             MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
//                         Form esito trasferimento
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    function Verify()  
    {
        return true;
     }
  </script> 
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Esito Trasferimento Istruttoria Cumulo</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadTrasferisciIstruttoria')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br> 
  
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="ApriIstruttoria">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActLoadIstruttoriaDopoTrasferimento">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>_new" value="<%=IstruttoriaCumuloNew.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=IstruttoriaCumuloNew.getFasSieIdFascicoloSiep()%>">
  
    <table width="300"  cellspacing="0" align="center" class="tab" border="1">
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <B><%= IstruttoriaCumuloNew.getMessage() %></B>
          <p>&nbsp;<p>
        </td>
      </tr>

      <tr>
        <td colspan="2" align="center">
          <input class="bottone" type="submit" name="Apri Istruttoria" value="Apri Istruttoria">
        </td>
      </tr>                                                        

      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>

    </table>  
  </form>
  
  <script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("ApriIstruttoria");
     frmvalidator.setAddnlValidationFunction("Verify");  
  </script>
</body>
</html>  