<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<% EventoModel lEve = evento;%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Evento- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  </head>
  <BODY class="corpo" onload="javascript:lookUpload();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Trasmissione Istanza</font>
        </td>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.istanza.action.ActStampaTrasferimentoIstanza&IdEvento=<%=lEve.getIdEvento()%>">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.istanza.action.ActStampaTrasferimentoIstanza&IdEvento="+lEve.getIdEvento()%>"/>
   </jsp:include>
      </tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
	 <tr>
      <td class="l">Da inviare a </td>
      <td class="L" colspan=5>
        <font class="campo"><%=UfficioDestinatario.getDescrTipoUfficio()+ " di " + UfficioDestinatario.getDescrComune()%></font>&nbsp;
			</td>
    </tr>
    <tr>
  </table>
  <br>
    <div align=left style="visibility:hidden" id="upld">
      <FORM name="comandi" enctype="multipart/form-data" method="post"  onsubmit="document.forms[0].go.disabled=true;return true;">
        <table>
          <td class="l">Trasmissione Istanza da Salvare</td>
          <td class="L">
          <font class="campo">
           <input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
          </td>
            <input type="hidden" name="<%=ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>"  value="<%=UfficioDestinatario.getDescrComune()%>" >
            <input type="hidden" name="<%=ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO %>" value="<%=UfficioDestinatario.getCodTipoUfficio()%>" >
          </tr>
          <tr><td><br></td></tr>
          <tr>
            <td class="L">
              <input name=go  class=bottone  type="submit" value="Conferma Trasmissione Istanza">
              <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActUploadDocumentConfermaTrasmissione">
              <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getIdEvento() %>">
            </td>
          </tr>
        </table>
      </FORM>
    </div>
  <br>
</body>

</html>