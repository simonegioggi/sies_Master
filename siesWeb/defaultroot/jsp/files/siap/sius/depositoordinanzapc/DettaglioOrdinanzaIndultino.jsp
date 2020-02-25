<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing="2" cellpadding="2">
    <tr> <td> <br></td></tr>
    <tr>
        <td class="Titolo" colspan=6> L. 2003/207 <td>
    </tr>
    <tr>
      <td class="l"> Esistenza condizioni ostative </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getFlagEsistenzaReatoostativo()).compareTo("S") == 0 ? "Si" : "No"%></font></td>
    </tr>
    <tr>
      <td class="l"> Avvenuta espiazione condanna per reato ostativo</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getFlagEspiazioneReatoostativo()).compareTo("S") == 0 ? "Si" : "No"%></font></td>
    </tr>
    <tr>
      <td class="l">Data Termine Misura </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(  datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">Magistrato di sorveglianza Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">UEPE Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrComuneCssaComp(), "-")%></font></td>
    </tr>
    <tr>
      <td class="l">Autorità delegata alla vigilanza </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getAutoritaVigilante(), "-")%></font></td>
    </tr>
    <tr> <td> <br></td></tr>

</table>

