<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="UtenteConnesso" scope="session"
	class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="sentenza" scope="request"
	class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />

<%
SentenzaModel lSentenza = new SentenzaModel(sentenza);
%>

<html>

<head>
<title>[S.I.E.S.] - Dettaglio Decreto -</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
</head>

<BODY class="corpo">

<FORM name="comandi">
<input type="hidden" name="isSentenza" value="false" />
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
			class="campo">Dettaglio Estremi Decreto Penale</font></td>
		<td class="LBG">
		<%String lCancellabile ="SI";
		if (lSentenza.getEsistonoFascicoliAssociati())
			{//Se la sentenza ha un fascicolo associato non si cancella
				lCancellabile = "NO";
			}
		
		if (Modificabile.length() > 0) {%>		
				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
					<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" />
					<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>" />
					<jsp:param name="Modificabile" value="<%=Modificabile%>" />
					<jsp:param name="Cancellabile" value="<%=lCancellabile%>" />
				</jsp:include></td>
			<%} else { %>
				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
					<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" />
					<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>" />
					<jsp:param name="Cancellabile" value="<%=lCancellabile%>" />
				</jsp:include></td>
		<%}
		
 if (TornaQui.compareTo("") != 0) {
 %> <a href="<%=TornaQui%>"
			onMouseOver="hiLite('img01','clickme2')"
			onMouseOut="hiLite('img01','clickme1')"> <IMG
			SRC="<%=IWebConstants.IMAGES_DIR %>/arrowleft24.gif" BORDER="0"
			ALT="" NAME="img01"> </a> <%
 }
 %>
		</td>
		<%
					if (request.getParameter("NomeAzione") != null
					&& request.getParameter("NomeAzione").equals(
					"siap.siep.sentenza.action.ActRicercaSentenza")) {
		%>
		<td class="LBG"><a href="javascript:history.go(-1);"> <img
			align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif"
			alt="ritorna su" width="24" height="24" border="0"> </a></td>
		<%
		}
		%>
	</tr>
</table>
</FORM>
<table cellspacing=2 cellpadding=2>
	
	<tr>
		<td class="l">Anno/Numero R.G.N.R.</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getNumeroRegePm())%></font>&nbsp;
		</td>

		<td class="l"></td>
		<td class="L"></td>
		<td class="l"></td>
		<td class="L"></td>
	</tr>
	<tr>
		<td class="l">Numero Reg.Gen. GIP</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegeGip())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegeGip())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Data Decreto</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Decreto</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getAnnoSentenza())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrTipoAutoritaEmittente())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrLuogoEmittente())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getNumSezioneAutoritaEmittente())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza Cass.</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getAnnoSentenzaCassazione())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getNumeroSentenzaCassazione())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getAnnoRaccoltaGenerale())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getNumeroRaccoltaGenerale())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Dispositivo Cassazione</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrTipoDecisioneCassazione())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
		</td>
	</tr>
</table>
<br>
</body>

</html>