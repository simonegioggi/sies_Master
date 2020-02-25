<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>


<jsp:useBean id="sentenza" scope="session"	class="siap.siep.sentenza.model.SentenzaModel" />

<%SentenzaModel lSentenza = sentenza; %>

<table width="100%">

	<tr>
		<td class="Titolo" colspan=6>Estremi della Sentenza</td>
	</tr>	
	<tr>
		<td class="l" width="25%">Anno/Numero Sentenza</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getAnnoSentenza())%></font>&nbsp; / <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
		</td>
	</tr>	
	<tr>
		<td class="l" width="25%">Data Sentenza</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font>&nbsp;</td>
	</tr>

	<tr>
		<td class="l" width="25%">Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrTipoAutoritaEmittente())%></font>&nbsp;</td>
	</tr>
	<%
				if (lSentenza.getCodTipoAutoritaEmittente().equals("DIB")
				|| lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
	%>
	<tr>
		<td class="l" width="25%">Tipo Rito</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
										.getDescrTipoRito())%></font>&nbsp;</td>
	</tr>
	<%
	}
	%>
	<tr>
		<td class="l" width="25%">Luogo Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrLuogoEmittente())%></font>&nbsp;</td>
	</tr>

</table>
