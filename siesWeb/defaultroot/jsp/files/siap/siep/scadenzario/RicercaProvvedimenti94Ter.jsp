<%-- 20261028-MEV_2026-105_INLAY - MEV Legge 140/2026: ricerca provvedimenti emessi ex art. 94 ter DPR 309/90 --%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sico.evento.model.EventoFascicoloStatoModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%
  List lista = (List) request.getAttribute("ListaProvv94Ter");
  BigDecimal totale = (BigDecimal) request.getAttribute("CountRisultati");
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti con Provvedimenti ex art. 94 Ter DPR 309/90</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    function dettaglio(aCodMotivo, aIdEvento) {
      var lAzione = "siap.siep.ordineesecuzione.action.ActDettaglioLSLibero";
      if (aCodMotivo == '0761') lAzione = "siap.siep.ordineesecuzione.action.ActDettaglioLSAltraCausa";
      if (aCodMotivo == '0762') lAzione = "siap.siep.ordineesecuzione.action.ActDettaglioLSArrestiDomiciliari";
      document.fd.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.fd.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = aIdEvento;
      document.fd.submit();
    }
    </script>
  </head>
  <BODY class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">RICERCA PROCEDIMENTI CON PROVVEDIMENTI EX ART. 94 TER DPR 309/90</font></td>
        <td class="LBG"><a href="javascript:document.fx.submit();" title="Estrai in formato xls"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Estrai in formato xls" border="0"></a></td>
        <td class="LBG"><a href="javascript:history.go(-<%=request.getAttribute(IWebConstants.NUM_PAGE)%>);"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a></td>
      </tr>
    </table>
    <br>
    <font class="label">Totale provvedimenti estratti:</font>&nbsp;<font class="campo"><%=totale%></font>
    <br>
    <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
    <%-- export xls: ripropone gli stessi filtri della ricerca --%>
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="fx">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActReportExcelProvvedimenti94Ter">
<%
  String[] nomi = new String[] { ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE, ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE,
      ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE, ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE,
      "GiornoEmissioneIniziale", "MeseEmissioneIniziale", "AnnoEmissioneIniziale",
      "GiornoEmissioneFinale", "MeseEmissioneFinale", "AnnoEmissioneFinale", "tipoNotifica", "estraiDefiniti" };
  for (int n = 0; n < nomi.length; n++) {
    String[] valori = request.getParameterValues(nomi[n]);
    for (int v = 0; valori != null && v < valori.length; v++) {
      if (valori[v] != null && valori[v].trim().length() > 0) {
%>
      <input type="HIDDEN" name="<%=nomi[n]%>" value="<%=StringUtils.toStringJSP(valori[v].trim())%>">
<%
      }
    }
  }
%>
    </form>
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="fd">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
    </form>
    <table width="100%" cellpadding=2 cellspacing=2>
      <tr>
        <td class="int">N&deg; SIEP</td>
        <td class="int">Cognome</td>
        <td class="int">Nome</td>
        <td class="int">Luogo Nascita</td>
        <td class="int">Data Nascita</td>
        <td class="int">Provvedimento</td>
        <td class="int">Data Emissione Decreto</td>
        <td class="int">Stato Procedimento</td>
        <td class="int">Azioni</td>
      </tr>
<%
  Iterator itx = lista.iterator();
  while (itx.hasNext()) {
    EventoFascicoloStatoModel lSca = (EventoFascicoloStatoModel) itx.next();
    FascicoloSiepModel lFas = lSca.getFascicoloSiep();
    EventoModel lEve = lSca.getEvento();
    SoggettoModel lSog = lFas.getSoggetto();
%>
      <tr>
        <td class=C>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento"><%=lFas.getChiaveAnno()%> / <%=lFas.getChiaveProgr()%></a>
        </td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getCognome())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getNome())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(), "dd/MM/yyyy"))%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(lEve.getDescrMotivo())%>&nbsp;</td>
        <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(), "dd/MM/yyyy"))%>&nbsp;</td>
        <td class=C><%=lSca.getStatoProcedimento() != null ? StringUtils.toStringJSP(lSca.getStatoProcedimento().getDescrStatoProcedimento()) : ""%>&nbsp;</td>
        <td class=C>
          <a href="javascript:dettaglio('<%=lEve.getCodMotivo()%>', <%=lEve.getIdEvento()%>)"><img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Provvedimento" border="0"></a>
        </td>
      </tr>
<%
  }
%>
    </table>
  </body>
</html>
