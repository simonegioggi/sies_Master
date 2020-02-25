<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="eventi" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Provvedimento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : Ricerca Provvedimenti</font>&nbsp;&nbsp;
     </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
  <br>
 <br>
  <div align=center>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Data Emissione</td>
      <td class="int">Autorità</td>
      <td class="int">Descrizione Provvedimento</td>
      <td class="int">Documento<br>Validato</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = eventi.iterator();
  while ( itx.hasNext())
  {
    EventoModel lEvento = (EventoModel)itx.next();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
      <td class="l"><%= lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()  %></td>
      <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
      <td class="c">
<%
      if (lEvento.getFlagDocumentoRegistrato()!=null){if (lEvento.getFlagDocumentoRegistrato().compareTo("S")==0)
      {
%>
        <img src="/images/TickRed.gif">
<%
      }
  }
%>
      </td>
      <td class="c">
        <%String isBlob="SI";if(lEvento.getFlagDocumentoRegistrato() == null){isBlob="NO";}%>
        <jsp:include page="<%=ICostantiEvento.PG_BUTTONS_RICERCA%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEvento.getCodMotivo()%>" />
          <jsp:param name="modalita" value="R" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
  </body>
</html>