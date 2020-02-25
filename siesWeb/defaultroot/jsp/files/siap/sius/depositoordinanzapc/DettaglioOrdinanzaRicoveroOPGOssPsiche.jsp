<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> Ricovero OPG per Osservazione Psichiatrica <td>
    </tr>
  <tr>
      <td class="l"> Rilevato</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
  </tr>
  <tr>
    <td class="l">Struttura Ospedaliera designata</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(),"-")%></font></td>
  </tr>
    <tr> <td> <br></td></tr>
</table>

