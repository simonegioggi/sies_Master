<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<% EventoModel lEve = evento;%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Trasferisci Ordinanza - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  </head>
  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Trasmissione Ordinanza</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
         <tr>
      <td class="l">Da inviare a </td>
      <td class="L" colspan=5>
        <font class="campo"><%=UfficioDestinatario.getDescrTipoUfficio()+" di "+UfficioDestinatario.getDescrComune()%></font>&nbsp;
      </td>
    </tr>
    <tr>
  </table>
  <br>
  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="TrasferisciOrdinanza">
    <table>
      <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>"  value="<%=UfficioDestinatario.getDescrComune()%>">
      <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" value="<%=UfficioDestinatario.getCodTipoUfficio()%>">
      <tr>
        <td class="L">
          <input class=bottone  type="submit" value="Conferma Trasmissione Ordinanza">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositoordinanzapc.action.ActConfermaTrasmissione">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getIdEvento()%>">
        </td>
      </tr>
    </table>
  </form>
</body>
</html>