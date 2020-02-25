<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regecircostanza.action.ICostantiRegeCircostanza" %>
<%@ page import="siap.regesies.regecircostanza.model.RegeCircostanzaModel" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>

<jsp:useBean id="regecircostanze" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>
<body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Rege Circostanza</font></td>
        </td>
    </tr>
  </table>
</form>
  <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/></td>
<div align=center>
<table >
    <tr><td class=titolo colspan=13>Rege Circostanze</td></tr>
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.Qual.</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
      <td class="int" width=5%>Azioni</td>
   </tr>
 <%//CIRCOSTANZE
if(regecircostanze != null)
 {
    Iterator lItx = regecircostanze.iterator();
    RegeCircostanzaModel lCirc = null;

    while(lItx.hasNext())
    {
      lCirc = (RegeCircostanzaModel)lItx.next();%>
      <tr>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.intZerotoString(lCirc.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getComma(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(lCirc.getNumero(),"-")%></td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeCircostanza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lCirc.getIdFile()%>" />
             <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiRegeCircostanza.CAMPO_PROGR_CIRCOSTANZA%>" />
             <jsp:param name="ValoreIdEntitaProvv" value="<%=lCirc.getProgrCircostanza()%>" />
          </jsp:include>
      </td>

     </tr>
<%
        }
       }
 %>
  </body>
</html>