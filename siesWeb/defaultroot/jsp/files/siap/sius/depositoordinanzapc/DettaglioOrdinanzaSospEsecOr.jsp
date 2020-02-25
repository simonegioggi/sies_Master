<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="ufficioConcessoRiduzione" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<table cellspacing="2" cellpadding="2">
   <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
   </tr>
    <tr><td><br></td></tr>
  <tr>
    <td class="Titolo" colspan=2> Estremi ordinanza sospesa: <td>
  </tr>
    <tr>
      <td class="l"> Data Emissione</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(),"dd/MM/yyyy"),"-")%></font></td>
    </tr>
    <tr>
      <%--td class="l">Tribunale Sorveglianza emittente</td--%>
      <td class="l">Ufficio emittente : &nbsp;&nbsp;&nbsp;<%=ufficioConcessoRiduzione.getCodTipoUfficio()%>&nbsp;</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUffTdsConcessoRiduzione(), "-")%></font></td>
    </tr>
   <tr></tr>
</table>