<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2026-124_SG: procedimenti con pena sostitutiva sospesa e priva di data ripresa --%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.RedirectTo" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.statistiche.model.PenaSostitutivaSospesaModel" %>

<jsp:useBean id="elencoPeneSospese" scope="request" class="java.util.ArrayList" />
<jsp:useBean id="TornaQui"          scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti con pene sostitutive sospese e prive di data ripresa</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>&nbsp;
          <font class="campo">Procedimenti con pene sostitutive sospese e prive di data ripresa</font>
        </td>
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
    <br>

    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="int">Numero SIUS</td>
        <td class="int">Data Iscrizione</td>
        <td class="int">Cognome</td>
        <td class="int">Nome</td>
        <td class="int">Data Inizio Sospensione</td>
        <td class="int">Data Fine Sospensione</td>
        <td class="int">Durata Sospensione (A/M/G)</td>
        <td class="int">Sospensione disposta da</td>
      </tr>
<%
  Iterator itx = elencoPeneSospese.iterator();
  while (itx.hasNext()) {
    PenaSostitutivaSospesaModel pena = (PenaSostitutivaSospesaModel) itx.next();
    RedirectTo lRedirect = new RedirectTo();
    lRedirect.setPage(IWebConstants.PG_MAIN);
    lRedirect.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
    lRedirect.setParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, "" + pena.getIdFascicoloSius());
    if ((TornaQui != null) && TornaQui.trim().length() > 1)
      lRedirect.setParameter("TornaQui", TornaQui);
%>
      <tr>
        <td class="c">
          <font class="label">
            <a class="cliccabile" href="<%=lRedirect.toString()%>" Title="Dettaglio Procedimento">
              <%=pena.getChiaveAnno()%>/<%=pena.getChiaveProgr()%>
            </a>
          </font>
        </td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataIscrizione(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(pena.getCognome(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(pena.getNome(), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataInizioSospensione(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(pena.getDataFineSospensione(), "dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=pena.getSospensioneAA() == null ? "0" : pena.getSospensioneAA().toString()%> / <%=pena.getSospensioneMM() == null ? "0" : pena.getSospensioneMM().toString()%> / <%=pena.getSospensioneGG() == null ? "0" : pena.getSospensioneGG().toString()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(pena.getCodTipoUfficioSosp(), "-")%></font></td>
      </tr>
<%
  }
%>
    </table>
  </body>
</html>
