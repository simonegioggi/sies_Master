<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing="2" cellpadding="2">
  <tr>
  	<td class="l">Ulteriore descrizione della decisione </td>
  	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>
  	<td class="Titolo" colspan=6> Revoca Liberazione Condizionale <td>
  </tr>
  
  <tr>
	<td class="l">Data Inizio  </td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataInizioPeriodo(),"dd/MM/yyyy"), "-")%></font></td>
  </tr>

   <tr>
      <td class="l">Pena residua rideterminata in (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
      </font>
       </td>
    </tr>
    <tr> <td> <br></td></tr>

</table>

