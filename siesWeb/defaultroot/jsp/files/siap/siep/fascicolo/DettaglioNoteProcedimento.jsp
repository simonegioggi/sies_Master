<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<%
  FascicoloSiepModel lFascicolo = fascicolo;
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.A.P] - Dettaglio Note Procedimento - </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<script language="JavaScript">
</script>

  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Note Procedimento</font>
        </td>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadModificaNoteProcedimento&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicolo.getIdFascicoloSiep()%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>        
        </td>
        <td class="LBG">
          <a href="Javascript:conferma('siap.siep.fascicolo.action.ActCancellaNoteProcedimento','<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>','<%=lFascicolo.getIdFascicoloSiep()%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
        </td>
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
  </FORM>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo">Note Procedimento</td></tr>
    <tr>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(lFascicolo.getNote())%>
        </font>
      </td>
    </tr>
  </table>
</body>
</html>