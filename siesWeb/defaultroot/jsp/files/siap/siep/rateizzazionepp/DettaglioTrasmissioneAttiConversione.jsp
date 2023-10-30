<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina di Dettaglio Trasmissione Atti per la Conversione Pena Pecuniaria --%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"           	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="notificaAlMagistrato" 	scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="importoDaPagare"		scope="request" class="java.math.BigDecimal"/>
<jsp:useBean id="domicilio"        		scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel fsm = (FascicoloSiepModel)session.getAttribute("fascicolo");
PosizioneGiuridicaModel pgm = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel ldm = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel acm = posizioneluogoaltra.getAltraCausa();
if (pgm == null)
	pgm = new PosizioneGiuridicaModel();
if (ldm == null)
	ldm = new LuogoDetenzioneModel();
if (acm == null)
	acm = new AltraCausaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Trasmissione Atti per la Conversione Pena Pecuniaria </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
  
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Dettaglio Trasmissione Atti per la Conversione Pena Pecuniaria</font>
		</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>   
		<td class="LBG">
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadModificaTrasmissioneAttiConversione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
				<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Trasmissione Atti per la Conversione Pena Pecuniaria" width="24" height="24" border="0">
		  	</a>
		</td>
		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.rateizzazionepp.action.ActStampaTrasmissioneAttiConversione&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
		</jsp:include>
<%
}
%>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<table cellspacing=0 cellpadding=0 width=95%>
  	<tr>
    	<td class="l" width="20%">Posizione Giuridica</td>
    	<td class="L">
      		<font class="campo">
<%
if (fsm.getFlagAltraCausa() != null && fsm.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA - <%=acm.getDescrTipoPosGiuridica()%>
<%
} else {
%>
				<%=pgm.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
			<input type="HIDDEN" title="Codice Posizione" type="text" maxlength="6" size="6" value="<%=StringUtils.toStringJSP(pgm.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
		</td>
  	</tr>
<%
if (Utils.isPresent(domicilio)) {
%>
	<tr>
		<td class="L" width="20%">Residenza</td>
		<td class="L">
  			<font class="campo">Domicilio Imposto in:&nbsp;<%=domicilio%></font>
		</td>
	</tr>
<%
}
%>
	<tr>
	  	<td class="l" width="20%">Pena Pecuniaria Sostitutiva: Importo</td>
	  	<td class="L">
  			<font class="campo"><%=StringUtils.toEuroFormat(importoDaPagare)%></font>
		</td>
	</tr>
  	<tr>
		<td class="l" width="25%">Data Emissione</td>
		<td class="L">
  			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
	<tr>
		<td class="l" width="20%">Data Trasmissione</td>
		<td class="L">
		  	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
	<tr>
		<td class="l" width="20%">Magistrato</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Autorita' Destinazione</td>
		<td class="L" colspan=2><font class="campo"><%=StringUtils.toStringJSP(notificaAlMagistrato.getUfficio().getDescrTipoUfficio())%></font>
		&nbsp;di&nbsp;
		<font class="campo"><%=StringUtils.toStringJSP(notificaAlMagistrato.getUfficio().getDescrComune())%></font>
		</td>
	</tr>
</table>
<br>
<div align=left style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
	<tr>
  		<td class="L">
			<input class=bottone  type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActUploadTrasmissioneAttiConversione">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.rateizzazionepp.action.ActDettaglioTrasmissioneAttiConversione">
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>