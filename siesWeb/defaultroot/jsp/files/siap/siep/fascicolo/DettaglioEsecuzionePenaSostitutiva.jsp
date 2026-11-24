<%-- 20261124-MEV_2026-124_SG: Esecuzione Pena Sostitutiva (semiliberta' / detenzione domiciliare sostitutiva) nel Dettaglio Procedimento SIEP --%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel" %>
<%
DettaglioFascicoloModel lDettEPS = (DettaglioFascicoloModel) request.getAttribute("dettagliofascicolo");
PenaResiduaModel lPenaEPS = lDettEPS != null ? lDettEPS.getPenaResidua() : null;
SanzioneSostResiduaModel lSsrEPS = lPenaEPS != null ? lPenaEPS.getSanzSostResidua() : null;
if (lSsrEPS != null && lSsrEPS.getCodTipoSanzione() != null
    && ("T".equals(lSsrEPS.getCodTipoSanzione()) || "V".equals(lSsrEPS.getCodTipoSanzione())))
{
  boolean lSospesaEPS = "S".equals(lPenaEPS.getFlagPenaSospesa());
%>
<table cellspacing="0" cellpadding="0" width="95%">
  <tr>
    <td class="Titolo" colspan="6">Esecuzione Pena Sostitutiva</td>
  </tr>
  <tr>
    <td class="l"><font class="label">Tipo pena sostitutiva</font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSsrEPS.getDescrTipoSanzione(), "-")%></font></td>
    <td class="l"><font class="label">Esecuzione sospesa</font></td>
    <td class="l"><font class="campo"><%=lSospesaEPS ? "SI" : "NO"%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Residuo da eseguire</font></td>
    <td class="l" colspan="3">
      <font class="campo">
        AA:&nbsp;<%=StringUtils.toStringJSP(lSsrEPS.getNumAnni(), "0")%>&nbsp;
        MM:&nbsp;<%=StringUtils.toStringJSP(lSsrEPS.getNumMesi(), "0")%>&nbsp;
        GG:&nbsp;<%=StringUtils.toStringJSP(lSsrEPS.getNumGiorni(), "0")%>
      </font>
    </td>
  </tr>
  <tr>
    <td class="l"><font class="label">Data inizio</font></td>
    <td class="l"><font class="campo"><%=lSsrEPS.getDataInizio() != null ? DateUtils.getDateToString(lSsrEPS.getDataInizio(), "dd-MM-yyyy") : "-"%></font></td>
    <td class="l"><font class="label">Data fine presunta</font></td>
    <td class="l"><font class="campo"><%=lSsrEPS.getDataFinePresunta() != null ? DateUtils.getDateToString(lSsrEPS.getDataFinePresunta(), "dd-MM-yyyy") : "-"%></font></td>
  </tr>
</table>
<%
}
%>
