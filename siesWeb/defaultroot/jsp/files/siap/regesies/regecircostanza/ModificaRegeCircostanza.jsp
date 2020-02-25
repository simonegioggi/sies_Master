<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.regesies.regecircostanza.model.RegeCircostanzaModel"%>
<%@ page import="siap.regesies.regecircostanza.action.ICostantiRegeCircostanza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="regeCircostanza" scope="request" class="siap.regesies.regecircostanza.model.RegeCircostanzaModel"/>
<jsp:useBean id="TipiFontiCircostanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Rege Circostanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
 </head>
<body class="corpo">
        <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
         <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
           <font class="campo">Modifica Rege Circostanza</font>
         </td>
</tr>
</table>
<br>   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaRegeCircostanza">
<%
  RegeCircostanzaModel lCircostanza = regeCircostanza;
  String lAzione = "siap.regesies.regecircostanza.action.ActModificaRegeCircostanza";%>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.qualificante</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
    <tr>
      <td class="l">
        <select name="<%= ICostantiRegeCircostanza.CAMPO_COD_FONTE %>">
          <%=TipiFontiCircostanza%>
        </select>
      </td>
      <td class="l">
        <input size=4 maxlength=4 title="Anno" value="<%=StringUtils.intZerotoString(lCircostanza.getAnnoFonte()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_ANNO_FONTE %>">
      </td>
      <td class="l">
        <input size=6 maxlength=6 title="Numero" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="l">
        <input size=5 maxlength=5 title="Articolo" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_ARTICOLO %>">
      </td>
      <td class="l">
      <select name="<%=ICostantiRegeCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
      </td>
      <td class="l">
        <strong>C</strong><input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(lCircostanza.getComma()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_COMMA %>">
      </td>
      <td class="l">
        <strong>L</strong><input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_LETTERA %>">
      </td>
      <td class="l">
        <strong>N</strong><input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero()) %>" type="text" name="<%= ICostantiRegeCircostanza.CAMPO_NUMERO %>">
      </td>
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>
      </td>
		</tr>
  </table>


      <input type="HIDDEN" value="<%=lCircostanza.getIdFile()%>" name="<%=ICostantiRegeCircostanza.CAMPO_ID_FILE%>">
      <input type="HIDDEN" value="<%=lCircostanza.getProgrCircostanza()%>" name="<%=ICostantiRegeCircostanza.CAMPO_PROGR_CIRCOSTANZA%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaRegeCircostanza");

  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_ANNO_FONTE%>", "num");
  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_ANNO_FONTE%>", "minlength=4");
  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_ARTICOLO%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_NUMERO_FONTE%>", "alphanumeric");

  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_COMMA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_LETTERA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiRegeCircostanza.CAMPO_NUMERO%>", "alphanumeric");
 </script>
</body>
</html>