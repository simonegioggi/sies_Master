<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.web.Action"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="flagRicercaData" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRegistro" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />

<html>
<head>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<title>[S.I.E.S.] - Ricerca Sentenza per Soggetto</title>
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<BODY class="corpo">
<FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">

<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Elenco Titoli Esecutivi</font></td>
		<jsp:include page="/jsp/files/siap/siep/sentenza/BottoneInserimentoSige.jsp"></jsp:include>
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

<table cellpadding="2" cellspacing="2">
	<tr>
        <td class="int">Anno/Numero SIEP</td>
        <td class="int">Sede PM</td>
        <td class="int">Soggetto</td>
        <td class="int">Data Tit. Esecutivo</td>
        <td class="int">Anno/Numero Tit. Esecutivo</td>
        <td class="int">Estremi Tit. Esecutivo</td>
        <td class="int">Azioni</td>
    </tr>
<%
Iterator itx = sentenze.iterator();
while (itx.hasNext()) {
	SentenzaSoggettoFascicoloModel sentenza = (SentenzaSoggettoFascicoloModel) itx.next();
%>
	<tr>
		<td class=C>
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&ChiaveFascicolo=<%=sentenza.getIdFascicoloSiep()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(sentenza.getChiaveAnnoFascicolo())%>
	          	/
	          	<%=StringUtils.toStringJSP(sentenza.getChiaveNumeroFascicolo())%>
	        </a>        
		</td>
        <td class=C>
          	<%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </td>
        <td class=C>
          	<%=StringUtils.toStringJSP(sentenza.getCognome()) + " " + StringUtils.toStringJSP(sentenza.getNome())%>
        </td>
        <td class=C>
          	<%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))%>
        </td>
        <td class=C>
          	<%=StringUtils.toStringJSP(sentenza.getNumSentenza()).length() > 0
           		? StringUtils.toStringJSP(sentenza.getAnnoSentenza()) + "/" + StringUtils.toStringJSP(sentenza.getNumSentenza()) : "-"%>
        </td>
        <td class=C>
          	<%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%>&nbsp;
          	<%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </td>
      	<td class=C>
        	<jsp:include page="<%=ICostantiSentenza.PG_BUTTONS_SENTENZA%>">
				<jsp:param name="CampoAzioneChiamante" value="NomeAzione"/>
				<jsp:param name="ValoreAzioneChiamante" value="<%=AzioneChiamante%>"/>
				<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
				<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>"/>
				<jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>"/>
				<jsp:param name="ValoreIdEntitaProvv" value="<%=sentenza.getCodTipoProvvedimento()%>"/>
			</jsp:include>
		</td>
	</tr>
<%
}
%>
</table>
</FORM>
</body>
</html>