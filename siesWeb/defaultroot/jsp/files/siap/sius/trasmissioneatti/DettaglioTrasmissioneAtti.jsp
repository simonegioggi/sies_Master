<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.trasmissioneatti.action.ICostantiTrasmissioneAtti" %>
<jsp:useBean id="eventoNotTA" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Trasmissione Atti </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Trasmissione Atti</font>
        </td>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiTrasmissioneAtti.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=eventoNotTA.getEvento().getIdEvento()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
  <br>
  <jsp:include page="<%=ICostantiTrasmissioneAtti.PG_LOAD_DETTAGLIOPROCEDIMENTOSIUS%>"/>

</FORM>
  <table cellspacing=2 cellpadding=2>

    <tr>
      <td class="L"><font class="label">Data Trasmissione </font></td>
      <td class="L"><font class="campo"><%=DateUtils.getDateToString(eventoNotTA.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy")%></font></td>
    </tr>
    <tr>
      <td class="L"><font class="label">Destinatario </font></td>
      <td class="L"><font class="campo"><%=eventoNotTA.getEvento().getDescrTipoUfficioDestinatario()%> <BR>
                                        <%=eventoNotTA.getEvento().getDescrLuogoDestinatario()%> </font></td>
    </tr>
    <tr>
        <td class="L"><font class="label">Note </font></td>
        <td class="l"><font class="campo"><%=(eventoNotTA.getNotifiche()[0]).getNote() %></font></td>
    </tr>

  </table>
</body>
</html>