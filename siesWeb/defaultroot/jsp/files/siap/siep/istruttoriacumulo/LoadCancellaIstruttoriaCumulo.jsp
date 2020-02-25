<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<%
//==============================================================================
//         Form per l'Annullamento/Cancellazione di un'istruttoria cumulo
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript" >
    function Verify()  
    {
      // Verificare se le note sono abbligatorie
      if (window.confirm("Confermi la chiusura dell'istruttoria ?")) {
      	return true;
      }
      else {
        return false;
      }
    }
    
    //==========================================================================
    // Ritorna alla Griglia Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.CancellaIstruttoriaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.CancellaIstruttoriaCumulo.submit();
    }
  </script>  
</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Annullamento/Cancellazione Istruttoria Cumulo</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla alla Griglia Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="CancellaIstruttoriaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActCancellaIstruttoriaCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <table cellspacing="1" cellpadding="1" width="95%">
    <tr>
      <td class="titolo" colspan="100%">Chiusura Istruttoria Cumulo</td>
    </tr>
  
    <tr>
      <td class="L" colspan="100%">
        <font class="label">Istruttoria N. </font>
        <font class="campo">
          <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" title="Istruttoria">
          <%=IstruttoriaCumulo.getAnnoProtocollo()%>
          /
          <%=IstruttoriaCumulo.getNumProtocollo()%>
          </a>
        </font>
        <font class="label">Del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(IstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td class="L" colspan="100%">
        Attenzione!! Si sta procedendo alla chiusura forzata dell'istruttoria. Questa operazione non consentirà di emettere alcun provvedimento di cumulo. 
      </td>
    </tr>
    <tr>
      <td class="c">
        <font class="label">Note</font>
      </td>
      <td class="l">
        <textarea cols="100" rows="6" name="<%=ICostantiIstruttoriaCumulo.CAMPO_NOTE%>"></textarea>
      </td>
    </tr>
    
    <tr>
      <td colspan="1">
        <input class="bottone" type="submit" value="Conferma" onclick="javascript:return Verify();">
      </td>
    </tr>
  </table> 
</form>   
</body>
</html>