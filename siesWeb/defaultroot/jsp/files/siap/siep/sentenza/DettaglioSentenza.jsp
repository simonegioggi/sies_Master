<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="sentenza" scope="request"	class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String"/>

<%
SentenzaModel lSentenza = new SentenzaModel(sentenza);
String tipoProvvedimento = "Sentenza";
String vis = "";
String tipoProvvedimentoCas = "Sentenza";
String visCas = "";
String vSpec = "";
if (lSentenza.getCodTipoProvvedimentoRif().equalsIgnoreCase("53")) {
	tipoProvvedimento = "Ordinanza";
	vis = "style=\"display:none\"";
	visCas = "style=\"display:none\"";
	vSpec = "style=\"display:none\"";
	
}
if (lSentenza.getCodTipoProvvedimentoAltro().equalsIgnoreCase("53")) {
	tipoProvvedimentoCas = "Ordinanza";
	visCas = "style=\"display:none\"";	
}
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Sentenza -</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (!"".equals(lTipoFunzione) && !lTipoFunzione.equals("ritornodettaglio")) {
%>
<script language="JavaScript">
var aForm = null;
function Verify() {
	alert("La funzione di Iscrizione Guidata è stata Interrotta");
   	aForm = document.getElementById("Abbandona");
    Disabilita();
}

function Disabilita() {
	if (aForm == null)
		aForm = document.getElementById("FascSiep");
    document.Abbandona.A.disabled = true;
    document.FascSiep.NS.disabled = true;
   	aForm.submit();
}
</script>
<%
}
%>
</head>

<BODY class="corpo">
<FORM name="comandi">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"	alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Dettaglio Sentenza</font></td>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if ("".equals(lTipoFunzione)) {
	String lCancellabile = "SI";
	if (lSentenza.getEsistonoFascicoliAssociati()) {
		// Se la sentenza ha un fascicolo associato non si cancella
		lCancellabile = "NO";
	}
	if (Modificabile.length() > 0) {
%>
		<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>"/>
				<jsp:param name="Modificabile" value="<%=Modificabile%>"/>
				<jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
			</jsp:include>
		</td>
<%
	} else {
%>
		<td class="LBG">
			<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>"/>
				<jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
			</jsp:include>
		</td>
<%
	}
%>		
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%
}
if (request.getParameter("NomeAzione") != null
		&& request.getParameter("NomeAzione").equals("siap.siep.sentenza.action.ActRicercaSentenza")) {
%>
		<td class="LBG">
			<a href="javascript:history.go(-1);">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
		</td>
<%
}
%>
	</tr>
</table>
</FORM>
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l">Numero R.G.N.R.</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%></font></td>
	</tr>
	<tr>
<%
String reg = "";
String anno_reg = "";
String num_reg = "";
if (lSentenza.getAnnoRegeCap() != null) {
	reg = "CAP";
	anno_reg = lSentenza.getAnnoRegeCap() + "";
	num_reg = lSentenza.getNumeroRegeCap() + "";
}
if (lSentenza.getAnnoRegeCas() != null) {
	reg = "CAS";
	anno_reg = lSentenza.getAnnoRegeCas() + "";
	num_reg = lSentenza.getNumeroRegeCas() + "";
}
if (lSentenza.getAnnoRegeDib() != null) {
	reg = "DIB";
	anno_reg = lSentenza.getAnnoRegeDib() + "";
	num_reg = lSentenza.getNumeroRegeDib() + "";
}
if (lSentenza.getAnnoRegeCasap() != null) {
	reg = "CASAP";
	anno_reg = lSentenza.getAnnoRegeCasap() + "";
	num_reg = lSentenza.getNumeroRegeCasap() + "";
}
if (lSentenza.getAnnoRegeGip() != null) {
	reg = "GIP";
	anno_reg = lSentenza.getAnnoRegeGip() + "";
	num_reg = lSentenza.getNumeroRegeGip() + "";
}
// MEV_66: aggiunte quattro nuove proprietà
if (lSentenza.getAnnoRegeGup() != null) {
	reg = "GUP";
	anno_reg = lSentenza.getAnnoRegeGup() + "";
	num_reg = lSentenza.getNumeroRegeGup() + "";
}
if (lSentenza.getAnnoRegeCapsm() != null) {
	reg = "CAPSM";
	anno_reg = lSentenza.getAnnoRegeCapsm() + "";
	num_reg = lSentenza.getNumeroRegeCapsm() + "";
}

if (!reg.equals("")) {
%>

		<td class="l">Numero Reg.Gen.</td>
		<td class="L" colspan=5>
			<font class="campo"> <%=anno_reg%>&nbsp;/&nbsp;<%=num_reg%>&nbsp;&nbsp;&nbsp;<%=reg%></font>
		</td>
<%
}
%>
	</tr>
	<tr>
		<td class="l">Sede PM</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrSedeNotiziaReato())%></font></td>
	</tr>
	<tr>
		<td class="Titolo" colspan=6>Sentenza da Eseguire</td>
	</tr>	
	<tr>
		<td class="l">Data Sentenza</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoSentenza())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font>
		</td>
	</tr>
<%
if (lSentenza.getCodTipoAutoritaEmittente().equals("DIB") || lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
%>
	<tr>
		<td class="l">Tipo Rito</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoRito())%></font></td>
	</tr>
<%
}
%>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font></td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente())%></font></td>
	</tr>
	<tr>
		<td class="Titolo" colspan=6>Altro Grado di Giudizio</td>
	</tr>
	<tr>
		<td class="l">Tipo Provvedimento</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoProvvedimentoRif())%></font>
		</td>
	</tr>
	<tr <%=vis%>>
		<td class="l">Tipo Sentenza</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoProvvRif())%></font></td>
	</tr>
	<tr>
		<td class="l">Data <%=tipoProvvedimento%></td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvRif(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero <%=tipoProvvedimento%></td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoProvvRif())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroProvvRif())%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaProvvRif())%></font></td>
	</tr>
<%
if (lSentenza.getCodTipoAutoritaProvvRif().equals("DIB") || lSentenza.getCodTipoAutoritaProvvRif().equals("TRIBSD")) {
%>
	<tr>
		<td class="l">Tipo Rito</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoRito())%></font></td>
	</tr>
<%
}
%>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoProvvRif())%></font></td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaProvvRif())%></font></td>
	</tr>	
	<tr <%=vis%>>
		<td class="Titolo" colspan=6>Sentenza della Cassazione</td>
	</tr>
	<tr <%=vis%>>
		<td class="l">Tipo Provvedimento</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoProvvedimentoAltro())%></font>
		</td>
	</tr>
	<tr <%=visCas%>>
		<td class="l">Anno/Numero Reg.Gen. Cassazione</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote1DecisioneCassazione())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote2DecisioneCassazione())%></font>
		</td>
	</tr>
	<tr <%=vSpec %>>
		<td class="l">Anno/Numero <%=tipoProvvedimentoCas %></td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoSentenzaCassazione())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione())%></font>
		</td>
	</tr>
	<tr <%=visCas%>>
		<td class="l">Anno/Numero Raccolta Generale</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRaccoltaGenerale())%></font>
			&nbsp;/&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%></font>
		</td>
	</tr>
	<tr <%=visCas%>>
		<td class="l">Dispositivo Cassazione</td>
		<td class="L" colspan=5>
			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoDecisioneCassazione())%></font>
		</td>
	</tr>	
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font></td>
	</tr>
	<tr><td>&nbsp;</td></tr>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio")) {
%>
	<tr>
		<td class="lNoBord" colspan="2">
			<input type="hidden" name="isSentenza" value="true"/>
			<FORM method="POST" name="FascSiep" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadInserisciFascicolo&lTipoFunzione=<%=lTipoFunzione%>">
			<br>
			<INPUT class="bottone" type="button" name="NS" value="Prosegui" onclick="Javascript:Disabilita();"></FORM>
		</td>
		<td class="lNoBord" colspan="2">
			<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSentenza.getIdSentenza()%>&lTipoFunzione=ritornodettaglio">
			<br>
			<INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();"></FORM>
		</td>
	</tr>
<%
}
%>
</table>
</body>
</html>