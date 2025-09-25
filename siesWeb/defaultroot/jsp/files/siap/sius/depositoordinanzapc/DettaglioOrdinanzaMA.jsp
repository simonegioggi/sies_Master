<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>


<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="f3b.web.IWebConstants"%>


<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<%-- MEV10-s3: aggiunto riferimento all'oggetto "codTipoUfficio" --%>
<jsp:useBean id="codTipoUfficio" scope="request" class="java.lang.String"/>


<%
//INIZIO: MEV_9 (D.lgs. 123/2018)
FascicoloGPModel mFasGPMod = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
GeneraleProcedimentoModel mGeneraleProcedimentoModel = mFasGPMod.getGeneraleProcedimentoModel();

boolean is678 = false;
if (ICostantiDepositoOrdinanzaPc.MISURA_ALTERNATIVA_AMMISSIONE_DL_123_2018.equals(datiOrdinanza.getOrdinanza().getCodTipoOrdinanza())) {
	is678 = true;
}
//FINE: MEV_9
%>
<table cellspacing="2" cellpadding="2">

	<tr>
		<td class="l">Ulteriore descrizione della decisione </td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getUlterioreDescrizione(), "-")%></font></td>
	</tr>
		
    <tr><td><br></td></tr>
  	<tr>
    	<td class="Titolo" colspan="6">Misura Alternativa <td>
    </tr>
    <tr>
      <td class="l">UEPE Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrComuneCssaComp(), "-")%></font></td>
    </tr>
    <!-- Il campo deve essere visibile solo per gli uffici dei minori -->
    <% if ("TDSM".equalsIgnoreCase(codTipoUfficio) || "UDSM".equalsIgnoreCase(codTipoUfficio)) { %>
    <tr>
      <td class="l">USSM Competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrComuneUssmComp(), "-")%></font></td>
    </tr>
    <% } %>
    <tr>
    	<%-- MEV10-s3: aggiunto controllo per le varie casistiche per tipo ufficio --%>
    	<% if ("TDS".equalsIgnoreCase(codTipoUfficio)) { %>
	      	<td class="l">Tribunale di Sorveglianza Competente </td>
	      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
		<% } else if ("TDSM".equalsIgnoreCase(codTipoUfficio)) { %>
			<td class="l">Tribunale per i Minorenni competente </td>
	      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
		<% } else if ("UDS".equalsIgnoreCase(codTipoUfficio)) { %>
			<td class="l">Magistrato di sorveglianza Competente </td>
	      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
		<% } else if ("UDSM".equalsIgnoreCase(codTipoUfficio)) { %>
			<td class="l">Ufficio di Sorveglianza per i Minorenni competente </td>
	      	<td class="l"><font class="campo"> <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getDescrUfficioMagistratoComp(), "-")%></font></td>
		<% } %>
	</tr>
    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(  datiOrdinanza.getOrdinanza().getLuogoSvolgimentoProva(), "-")%></font></td>
    <tr>
    <tr>
      <td class="l">Servizio terapeutico competente </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(  datiOrdinanza.getOrdinanza().getServizioTerapeuticoComp(), "-")%></font></td>
    </tr>

<% if (is678) { %>    
     <tr>
      <td class="l">Data Esecutivita' </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataEsecutivita(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
<% } %>

<% if (!is678) { %>    
     <tr>
      <td class="l">Data Termine Misura </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(datiOrdinanza.getOrdinanza().getDataFineMisura(),"dd/MM/yyyy"), "-")%></font></td>
    </tr>
   <tr>
      <td class="l">Durata Misura (AA-MM-GG)</td>
      <td class="l">
      <font class="campo">
         <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumAnniDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumMesiDetenzioneDom())%> - <%=StringUtils.toStringJSP( datiOrdinanza.getOrdinanza().getNumGiorniDetenzioneDom())%>
      </font>
       </td>
    </tr>
<% } %>     
    <tr>
			<td>
				<br>
			</td>
		</tr>
</table>
