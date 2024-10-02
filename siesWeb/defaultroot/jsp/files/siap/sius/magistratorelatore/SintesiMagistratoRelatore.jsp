<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<jsp:useBean id="magistratorelatore"   scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="TornaQui"   scope="request" class="java.lang.String"/>

<%
// 20131130 - Si recupera l'id evento dalla request
BigDecimal IdEvento = null;
IdEvento = (BigDecimal) request.getAttribute("IdEvento");
// 20131130 - Si recupera il codice provvedimento dalla request
String lCodTipoProvvedimento = null;
lCodTipoProvvedimento = (String) request.getAttribute("CodTipoProvvedimento");
// 20131205 - Si recupera il codice tipo provvedimento dalla request
String lCodTipoDecreto = null;
lCodTipoDecreto = (String) request.getAttribute("CodTipoDecreto");
%>
<table cellspacing="2" cellpadding="2" style="width: 95%;">
	<tr>
  		<td class="L">
  			<font class="label">Magistrato Relatore:&nbsp;</font>
<%
if (magistratorelatore != null) {
%>
			<font class="campo">
<%
	if (magistratorelatore.getMagistrato() != null) {
%>
				<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(magistratorelatore.getMagistrato().getNome())%>
<%
		if (lCodTipoProvvedimento != null && lCodTipoProvvedimento.equals("03")) { // ordinanza
%>
				&nbsp; - &nbsp;
   				<a class="cliccabile" 
   		 			href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositoordinanzapc.action.ActLoadModificaMagistratoOrdinanza&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=03<%--=retParam--%>">
					[Modifica]
				</a>
<%
		} else if (lCodTipoProvvedimento != null && lCodTipoProvvedimento.equals("02"))  { // decreto
			if (lCodTipoDecreto != null && !lCodTipoDecreto.equals(ICostantiDepositoDecreto.IRREPERIBILITA)
					&& !lCodTipoDecreto.equals(ICostantiDepositoDecreto.CITAZIONE)
					// MEV_2019-09: aggiunta casistica per non consentire la modifica
					&& !lCodTipoDecreto.equals(ICostantiDepositoDecreto.DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA)) { 
%>
				&nbsp; - &nbsp;
				<a class="cliccabile" 
		 			href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadModificaMagistratoDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=02<%--=retParam--%>">
	 				[Modifica]
				</a>
<%
			}
		} else if (lCodTipoProvvedimento != null && lCodTipoProvvedimento.equals("01"))  { // sentenza
%>
				&nbsp; - &nbsp;
   				<a class="cliccabile" 
   		 			href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositosentenza.action.ActLoadModificaMagistratoSentenza&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=IdEvento%>&<%=ICostantiProvvedimento.CAMPO_TIPO_PROVVEDIMENTO%>=01<%--=retParam--%>">
					[Modifica]
				</a>
<%
		}
	} else if (magistratorelatore.getEsperto() != null) {
%>
				<%=StringUtils.toStringJSP(magistratorelatore.getEsperto().getCognome())%>&nbsp;
				<%=StringUtils.toStringJSP(magistratorelatore.getEsperto().getNome())%>
<%
	} else {
%>
				--
<%
	}
%>
			&nbsp;</font>
<%
	if (request.getParameter("MagRelRitorno") != null) {
 		// Inseriamo la nuova modalita' di Bottone di ritorno
   		if (TornaQui.trim().length() > 1) {
%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.magistratorelatore.action.ActLoadInserisciMagistratoRelatore&TornaQui=<%=request.getParameter("TornaQui")%>">
 				Assegnazione/Cambio Magistrato Relatore
			</a>
<%
		} else {
%>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.magistratorelatore.action.ActLoadInserisciMagistratoRelatore&acdest=<%=request.getParameter("MagRelRitorno")%>">
 				Assegnazione/Cambio Magistrato Relatore
			</a>
<%
		} // endif TornaQui
  	} // endif MagRelRitorno
}
%>
		</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
</table >