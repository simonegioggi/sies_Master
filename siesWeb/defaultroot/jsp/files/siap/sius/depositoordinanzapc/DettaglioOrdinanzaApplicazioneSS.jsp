<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>



<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<table cellspacing=4 cellpadding=4  width=95%>

<%
String desUfficioCompetente = "";
// Si ricava la descrizione dell'Ufficio Competente
if ( datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp() != null &&   datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp().trim().length() > 1)
{
  UfficioModel lUff = UfficioUtils.getUfficioByCodUfficio(datiOrdinanza.getOrdinanza().getCodUfficioMagistratoComp());
  desUfficioCompetente = lUff.getDescrTipoUfficio() + " di " +  lUff.getDescrComune();
}
%>

  <tr>
    <td class="Titolo" colspan=2> Dati relativi alla Sanzione Sostitutiva <td>
  </tr>

		<tr>
			<td class="l">Ulteriore descrizione della decisione </td>
			<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
		</tr>
 <tr>
    <td class="l"> Dispositivo</td>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento(),"-")%></font></td>
  </tr>		
 
   <tr>
      <td class="l">Durata Sanzione Sostitutiva (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
      </font>
       </td>
    </tr>
     </tr>
      <td class="l">Ufficio Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( desUfficioCompetente, "-")%></font></td>
    </tr>
    
    <tr>
			<td>
				<br>
			</td>
		</tr>
</table>