<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaFascicoliModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" 

class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="flagRicercaData" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRegistro" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />

<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<html>
<head>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<title>[S.I.E.S.] - Ricerca Procedimento R.G.N.R.</title>
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<BODY class="corpo">
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Titoli Esecutivi</font></td>
  		<jsp:include page="/jsp/files/siap/siep/sentenza/BottoneInserimentoSige.jsp"></jsp:include>
       	<td class="LBG">
			<a href="javascript:history.go(-1);">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
        </td>
	</tr>
</table>
<br>
<%
if (!(RequestForPaging.equals("NO"))) {
%>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%
}
%>
<br>
<table style="width: 95%;" cellpadding=2 cellspacing=2>
	<tr>
		<td colspan="5" class="int">Titolo esecutivo</td>		
		<td colspan="4" class="int">Procedimento SIEP</td>
	</tr>

    <tr>
        <td class="int">&nbsp;&nbsp;R.G.N.R.&nbsp;&nbsp;</td>
        <td class="int">Reg.Gen.</td>
        <td class="int">Data emissione</td>
        <td class="int">Anno/numero</td>
        <td class="int">Autorità</td>
        <td class="int">Anno/numero</td>
        <td class="int">Data irrevocabilita</td>
        <td class="int">Soggetto</td>
      	<td class="int">Azioni</td>
    </tr>
<%
Iterator itx = sentenze.iterator();
while (itx.hasNext()) {
	int i;
	FascicoloSiepModel senfas = (FascicoloSiepModel) itx.next();
	SentenzaModel sentenza = senfas.getSentenza(); %>
	<tr>
		<td class=C>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&IdSentenza=<%=sentenza.getIdSentenza()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(sentenza.getAnnoRegePm())%>/<%=StringUtils.toStringJSP(sentenza.getNumeroRegePm())%>
			</a>        
		</td>
		<td class=C>
<%
	if (StringUtils.toStringJSP(sentenza.getNumeroRegistroGenerale()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegistroGenerale())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegistroGenerale())%>
<%
	} else {
		if (StringUtils.toStringJSP(sentenza.getNumeroRegeCap()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%>
<%
		} else if (StringUtils.toStringJSP(sentenza.getNumeroRegeCas()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%>
<%
		} else if (StringUtils.toStringJSP(sentenza.getNumeroRegeCasap()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%>
<%
		} else if (StringUtils.toStringJSP(sentenza.getNumeroRegeDib()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%>
<%
		} else if (StringUtils.toStringJSP(sentenza.getNumeroRegeGip()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%>
<%
		// MEV_66: aggiunte quattro nuove proprietà
		} else if (StringUtils.toStringJSP(sentenza.getNumeroRegeGup()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%>
<%
		}  else if (StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm()).length() > 0) {
%>
			<%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())+"/"+StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%>
<%
		} else {
%>
			-
<%
		}
	}
%>
		</td>			
		<td class=C>
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy")).length() > 0 ? StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy")) : "-"%>
		</td>        
		<td class=C>
			<%=StringUtils.toStringJSP(sentenza.getNumeroSentenza()).length()>0
			? StringUtils.toStringJSP(sentenza.getAnnoSentenza())+"/"+StringUtils.toStringJSP(sentenza.getNumeroSentenza()) : "-"%>
		</td>        
		<td class=C>
		  	<%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
		</td>
		<td class=C>
			<%=StringUtils.toStringJSP(senfas.getChiaveAnno()).length()>0
			? StringUtils.toStringJSP(senfas.getChiaveAnno())+"/"+StringUtils.toStringJSP(senfas.getChiaveProgr()) : "-"%>
		</td>
		<td class=C>
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(senfas.getDataIrrevocabilita(),"dd-MM-yyyy")).length()>0
			? StringUtils.toStringJSP(DateUtils.getDateToString(senfas.getDataIrrevocabilita(),"dd-MM-yyyy")) : "-"%>
		</td>
		<td class=C>
			<%=StringUtils.toStringJSP(senfas.getSoggetto().getCognome()).length()>0
			? (StringUtils.toStringJSP(senfas.getSoggetto().getCognome()))+" "+(StringUtils.toStringJSP(senfas.getSoggetto().getNome())): "-"%>
		</td>
		<td class=C>
			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
				<jsp:param name="CampoAzioneChiamante" value="NomeAzione" />
				<jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>" />
				<jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
				<jsp:param name="ValoreIdEntita" value="<%=senfas.getIdFascicoloSiep()%>" />
				<jsp:param name="FlagValidato" value="<%=senfas.getFlagValidato()%>" />
			</jsp:include>
		</td>
	</tr>		
<%
}
%>
</table>
</FORM>
<br>
</body>
</html>