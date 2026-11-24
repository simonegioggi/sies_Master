<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- 20261124-MEV_2026-124_SG --%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.scadenzario.penasostitutiva.ScadenzarioPenaSostitutivaRiga" %>

<jsp:useBean id="tipo" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Consultazione Scadenzario Pene Sostitutive Scadute</title>
  </head>
  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Consultazione Scadenzario Pene Sostitutive Scadute - <%=titolo%></font></td>
      </tr>
    </table>
    <br>
<%
    List scadenzario = (List) request.getAttribute("scadenzario");
    if (scadenzario == null || scadenzario.isEmpty())
    {
%>
    <font class="campo">Nessun procedimento trovato.</font>
<%
    }
    else
    {
%>
    <table cellpadding=2 cellspacing=2>
      <tr>
        <td class="int">N° SIEP</td>
        <td class="int">Cognome</td>
        <td class="int">Nome</td>
        <td class="int">Luogo Nascita</td>
        <td class="int">Data Nascita</td>
        <td class="int">Tipo Pena Sostitutiva</td>
        <td class="int">Data Inizio</td>
        <td class="int">Data Fine Presunta</td>
        <td class="int">Giorni Residui</td>
      </tr>
<%
      Iterator itx = scadenzario.iterator();
      while (itx.hasNext())
      {
        ScadenzarioPenaSostitutivaRiga r = (ScadenzarioPenaSostitutivaRiga) itx.next();
        String cls = r.getGiorniResidui() < 0 ? "cgrigio" : (r.getGiorniResidui() == 0 ? "crosso" : "C");
        String tipoPena = "T".equals(r.getCodTipoSanzione()) ? "Semilibertà sostitutiva" : "Detenzione domiciliare sostitutiva";
%>
      <tr>
        <td class=<%=cls%>>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=r.getIdFascicoloSiep()%>" title="Procedimento"><%=r.getChiaveAnno()%> / <%=r.getChiaveProgr()%></a>
        </td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(r.getCognome())%>&nbsp;</td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(r.getNome())%>&nbsp;</td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(r.getLuogoNascita())%>&nbsp;</td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(r.getDataNascita(),"dd/MM/yyyy"))%>&nbsp;</td>
        <td class=<%=cls%>><%=tipoPena%>&nbsp;</td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(r.getDataInizio(),"dd/MM/yyyy"))%>&nbsp;</td>
        <td class=<%=cls%>><%=StringUtils.toStringJSP(DateUtils.getDateToString(r.getDataFine(),"dd/MM/yyyy"))%>&nbsp;</td>
        <td class=<%=cls%>><%=r.getGiorniResidui()%>&nbsp;</td>
      </tr>
<%
      }
%>
    </table>
<%
    }
%>
  </BODY>
</html>
