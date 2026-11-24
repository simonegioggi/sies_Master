<%-- 20261124-MEV_2026-124_SG - Annotazioni dell'esecuzione della pena sostitutiva nell'Elenco Procedimenti della Sorveglianza e del GE --%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.dettaglioprovvedimento.controller.DettaglioProvvedimentoManager" %>
<%@ page import="siap.siep.fascicolo.penasostitutiva.ElencoAnnotazioniPenaSostitutivaController" %>
<%
  String lCodUtentePS = ((siap.sico.utente.model.UtenteModel) session.getAttribute("UtenteConnesso")).getCodUtente();
  BigDecimal lIdFascPS = null;
  if (request.getParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP) != null
      && request.getParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP).length() > 0) {
    lIdFascPS = new BigDecimal(request.getParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP));
  } else if (session.getAttribute("fascicolo") != null) {
    lIdFascPS = ((FascicoloSiepModel) session.getAttribute("fascicolo")).getIdFascicoloSiep();
  }
  // Visibile agli utenti SIEP (non Sorveglianza)
  if (lIdFascPS != null && !(lCodUtentePS.startsWith("D") || lCodUtentePS.startsWith("E"))) {
    List lAnnotazioniPS = new ElencoAnnotazioniPenaSostitutivaController().exRicerca(lIdFascPS);
    if (lAnnotazioniPS != null && !lAnnotazioniPS.isEmpty()) {
%>
  <br>
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr><td class="lVerdeNB" colspan="5">Annotazioni Esecuzione Pena Sostitutiva</td></tr>
    <tr>
      <td class="int" width="25%">Annotazione</td>
      <td class="int" width="10%">Data Emissione</td>
      <td class="int" width="10%">Data Inserimento</td>
      <td class="int" width="45%">Pena Sostitutiva Residua</td>
      <td class="int" width="10%">Azioni</td>
    </tr>
<%
      Iterator itPS = lAnnotazioniPS.iterator();
      while (itPS.hasNext()) {
        ElencoAnnotazioniPenaSostitutivaController.Riga r = (ElencoAnnotazioniPenaSostitutivaController.Riga) itPS.next();
        String lActionPS = DettaglioProvvedimentoManager.getInstance().getActionDettaglio("01", r.codTipoProvvedimento, r.codMotivo, "");
%>
    <tr>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(r.descrMotivo)%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(r.dataEmissione,"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(r.dataInserimento,"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(r.sanzioneResidua,"-")%></font></td>
      <td class="c"><a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActionPS%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=r.idEvento%>">Dettaglio</a></td>
    </tr>
<%
      }
%>
  </table>
<%
    }
  }
%>
