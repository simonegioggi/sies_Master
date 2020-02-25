<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regeresidenza.action.ICostantiRegeResidenza" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>
<%@ page import="siap.regesies.regeresidenza.model.RegeResidenzaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="regeresidenza"  scope="request" class="siap.regesies.regeresidenza.model.RegeResidenzaModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio Rege REsidenza - </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
  <form name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font>
       <% if(regeresidenza.getCodTipoResidenza().equals("R"))
          {
           %>
            <font class="campo">Dettaglio Residenza Rege</font>
       <% }
          else
          { %>
            <font class="campo">Dettaglio Domicilio Rege</font>
       <% } %>
        </td>
       <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeResidenza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=regeresidenza.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeResidenza.CAMPO_COD_TIPO_RESIDENZA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=regeresidenza.getCodTipoResidenza()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
  </form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l"><font class="label">Indirizzo</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regeresidenza.getIndirizzo())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">CAP</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regeresidenza.getCap())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Luogo</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regeresidenza.getDescrComune())%>&nbsp;</font></td>
      </tr>
       <tr>
        <td class="l"><font class="label">Comune Estero</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regeresidenza.getDescComuneEstero())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Stato</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regeresidenza.getDescrStato())%>&nbsp;</font></td>
      </tr>
  </table>
  </body>
</html>