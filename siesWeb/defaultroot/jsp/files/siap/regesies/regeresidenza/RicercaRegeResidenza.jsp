<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.regesies.regeresidenza.model.RegeResidenzaModel" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>
<%@ page import="siap.regesies.regeresidenza.action.ICostantiRegeResidenza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>


<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Rege Residenza</title>
    <script language="JavaScript" src="/html/conferma.js">
    </script>
  </head>

  <body class="corpo">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Residenze/Domicili Rege</font>
      </td>
    </tr>
  </table>
  <br>

<jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/></td>
  <table>
    <tr>
      <td class="int">Tipo</td>
      <td class="int">Indirizzo</td>
      <td class="int">CAP</td>
      <td class="int">Luogo</td>
      <td class="int">Comune Estero</td>
      <td class="int">Stato</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
  Iterator itx = residenze.iterator();
  while ( itx.hasNext())
  {//Ciccio
    RegeResidenzaModel residenza = (RegeResidenzaModel)itx.next();
%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrTipoResidenza(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getIndirizzo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getCap(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrComune(),"-")%></td>
      <%if(residenza.getDescComuneEstero()!=null){%>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescComuneEstero().toUpperCase(),"-")%></td>
    <%  }else{%>
     <td class="l"><%=StringUtils.toStringJSP(residenza.getDescComuneEstero(),"-")%></td>
    <%} %>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getDescrStato().toUpperCase(),"-")%></td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeResidenza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=residenza.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeResidenza.CAMPO_COD_TIPO_RESIDENZA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=residenza.getCodTipoResidenza()%>" />
          </jsp:include>

      </td>
    </tr>
<%
  }
%>
  </table>
  </form>
  </body>
</html>