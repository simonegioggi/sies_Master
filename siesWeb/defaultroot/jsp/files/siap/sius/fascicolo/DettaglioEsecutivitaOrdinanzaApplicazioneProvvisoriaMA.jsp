<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_9: creata nuova pagina di caricamento dati --%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="fascicoloSiusGP" 			scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoModel"				scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="dataEsecutivita"			scope="request" class="java.util.Date"/>
<jsp:useBean id="noteDataEsecutivita"		scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaTemplate" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="existConfermaDecisioneMR" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="notifiche" 				scope="request" class="java.util.Vector<NotificaModel>"/>

<%
String actionModifica = "siap.sius.fascicolo.action.ActLoadRegistrazioneEsecutivitaApplicazioneProvvisoriaMA";
String actionCancella = "siap.sius.fascicolo.action.ActRegistrazioneEsecutivitaApplicazioneProvvisoriaMA";
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Esecutivita&#768; Ordinanza Applicazione Provvisoria M.A.</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>
<body class="corpo">
<form name="dettaglio">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione : Dettaglio Esecutivita&#768; Ordinanza Applicazione Provvisoria M.A.</font>
			<input type="HIDDEN" name="ListaTemplate" value="<%=ListaTemplate%>">
		</td>
		<!-- BOTTONE DI STAMPA -->
	    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
	      	<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
	      	<jsp:param name="ValoreIdEntita" value="<%=eventoModel.getIdEvento()%>"/>
	    </jsp:include>
<%
if ("false".equals(existConfermaDecisioneMR)) {
%>
		<td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=actionModifica%>&TornaQui=<%=TornaQui%>&provenienza=dettaglio&Aggiungi=yes">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          	</a>
          	<a href="Javascript:conferma('<%=actionCancella%>','provenienza','cancella');">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          	</a>
     	</td>
<%
}
%>
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	</tr>
    <tr>
       	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
</table>
</form>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="label" nowrap>Estremi Ordinanza (art. 678 comma 1-ter c.p.p.)</td>
	</tr>
	<tr>
	    <td class="int">Contenuto</td>
	    <td class="int" width="10%">Data Udienza</td>
	    <td class="int">Provvedimento</td>
	    <td class="int" width="10%">Data Emissione</td>
	    <td class="int">Oggetto Provvedimento</td>
	    <td class="int">Esito Provvedimento</td>
	</tr>
	<tr>
		<td class="c"><font class="label"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoModel.getDescrTipoProvvedimento(),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"dd-MM-yyyy"), "-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoModel.getDescrMotivo(),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(eventoModel.getDescrEsito(),"-")%></font></td>
	</tr>
</table>
<br>
<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="l" width="15%">Data Esecutivita&#768;</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEsecutivita, "dd-MM-yyyy"), "-")%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
	    <td class="l">
			<font class="campo"><%=Utils.isPresent(noteDataEsecutivita) ? StringUtils.toStringJSP(noteDataEsecutivita) : "-"%></font>
	    </td>
 	 </tr>
 	 <%-- NOTIFICHE --%>
<%
Iterator<NotificaModel> notificheIterator = notifiche.iterator();
if (notificheIterator.hasNext()) {
	NotificaModel nm = (NotificaModel) notificheIterator.next();
%>
	<tr>
		<td class="l">Data Trasferimento Atti</td>
		<td class="l"><font class="campo"><%=DateUtils.getDateToString(nm.getDataInvio(), "dd/MM/yyyy")%></font></td>
    </tr>
<%
}
%>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>
<jsp:include page="<%=ICostantiUdienza.PG_LOAD_DESTINATARI%>"/>
<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
</body>
</html>