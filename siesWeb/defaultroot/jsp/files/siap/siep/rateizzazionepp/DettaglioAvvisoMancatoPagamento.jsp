<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_2023-33: aggiunta pagina di Dettaglio Avviso Mancato Pagamento Pena Pecuniaria --%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.rateizzazionepp.model.RateizzazionePPModel"%>
<%@ page import="siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="listaRateizzazioni"	scope="request" class="java.util.Vector<RateizzazionePPModel>"/>
<jsp:useBean id="magistrato"           	scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="notificaAlCondannato" 	scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="listaNotAvvSiep"      	scope="request" class="java.util.ArrayList<NotificaModel>"/>
<jsp:useBean id="listaNotObbligati"   	scope="request" class="java.util.ArrayList<NotificaModel>"/>

<%
FascicoloSiepModel fsm = (FascicoloSiepModel) session.getAttribute("fascicolo");
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
<title>[S.I.E.S.] - Dettaglio Avviso Mancato Pagamento Pena Pecuniaria</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
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
			<font class="campo">Dettaglio Avviso Mancato Pagamento Pena Pecuniaria</font>
		</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>   
		<td class="LBG">
		  	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.rateizzazionepp.action.ActLoadModificaAvvisoMancatoPagamento&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
				<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Avviso Mancato Pagamento Pena Pecuniaria" width="24" height="24" border="0">
		  	</a>
		</td>
		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.rateizzazionepp.action.ActStampaAvvisoMancatoPagamento&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
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
    	<td class="L" colspan=5>
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
			<input type="HIDDEN" title="Codice Posizione" type="text" maxlength="6" size="6"
       				value="<%=StringUtils.toStringJSP(pgm.getCodPosizioneGiuridica())%>"
					name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
		</td>
  	</tr>
</table>
<br>
<%
if (!"A".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
	// Se l'evento non e'annullato faccio vedere i dati della rate agganciate
	RateizzazionePPModel primarata = (RateizzazionePPModel) listaRateizzazioni.elementAt(0);
	String lTipoRateizzazione = primarata.getTipoRateizzazione();
	BigDecimal lImportoDaPagare = primarata.getImportoDaPagare();
%>
<table cellspacing=0 cellpadding=0 width=95%>
  	<tr>
    	<td class="L">
			<font class="label">Importo da pagare</font>
			<font class="campo"><%=StringUtils.toEuroFormat(lImportoDaPagare)%> &euro;</font>
			<font class="label">con le seguenti modalita'</font>
    	</td>
  	</tr>
</table>
<table cellspacing=0 cellpadding=0 width=95%>
  	<tr>
<%
	if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
		<td class="Titolo" colspan="6"> Pagamento in una Unica Soluzione</td>
<%
	} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
		<td class="Titolo" colspan="6"> Pagamento Rateizzato </td>
<%
	}
%>
	</tr>
<%
	Iterator<RateizzazionePPModel> rate = listaRateizzazioni.iterator();
	int conta = 0;
	while (rate.hasNext()) {
  		RateizzazionePPModel rata = (RateizzazionePPModel) rate.next();
  		conta++;
		if (rata.getEveIdEvento() != null && eventonotifica.getEvento().getIdEvento().compareTo(rata.getEveIdEvento()) == 0) {
    		if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_UNICA)) {
%>
	<tr>
		<td class="L" nowrap><font class="label">Rata unica da</font>&nbsp;<font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
		<td class="R" nowrap><font class="label">termine di pagamento fissato entro </font></td>
		<td class="R" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento</font></td>
	</tr>
<%
			} else if (lTipoRateizzazione.equals(ICostantiRateizzazionePP.TIPO_RATEIZZAZIONE_RATEALE)) {
%>
	<tr>
		<td class="R" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getNumeroRate(),"&nbsp;")%></font></td>
		<td class="L" nowrap><font class="label"> rate da </font></td>
		<td class="R" nowrap><font class="campo"><%=StringUtils.toEuroFormat(rata.getImportoRata())%> &euro;</font></td>      
<%
				if (conta == 1) {
%>
		<td class="L" nowrap><font class="label">termine di pagamento della prima rata fissato entro </font></td>
		<td class="R" nowrap><font class="campo"><%=StringUtils.toStringJSP(rata.getScadenzaGiorni(),"&nbsp;")%></font></td>
		<td class="L" nowrap><font class="label">giorni dalla notifica dell'avviso di pagamento </font></td>
<%
				} else {
%>
		<td class="R" colspan="3">&nbsp;</td>
<%
				}
%>
	</tr>
<%
			}
    	}
	} // end while
%>
</table>
<%
}
%>
<br>
<table cellspacing=0 cellpadding=0 width=95%>
  	<tr>
<%
if (eventonotifica.getEvento().getDataEmissione() != null) {
%>
		<td class="l" width="20%">Data Emissione</td>
		<td class="L">
  			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
		</td>
<%
}

// SE NON ESISTE NESSUNA NOTIFICA FALLISCE
if (eventonotifica.getNotifiche()[0].getDataInvio() != null) {
%>
		<td class="l" width="20%">Data Trasmissione</td>
		<td class="L">
		  	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "dd-MM-yyyy"))%></font>
		</td>
<%
}
%>
	</tr>
</table>
<%
if (magistrato != null) {
%>
<br>
<table cellspacing=0 cellpadding=0 width=95%>
	<tr>
		<td class="Titolo" colspan="2"> Magistrato </td>
	</tr>    
	<tr>
		<td class="l" width="20%">Magistrato Assegnatario
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
		</td>
	</tr>
</table>
<%
}
%>
<br>
<table cellspacing=0 cellpadding=0 width=95%>
	<tr>
		<td class="Titolo" colspan="2"> Notifica al condannato </td>
	</tr>
<%
if (notificaAlCondannato.getAutoritaEsterna() != null) {
%>
	<tr>
		<td class="l" width="20%">Autorita' Destinazione</td>
		<td class="L" colspan=2>
			<font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getAutoritaEsterna().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getAutoritaEsterna().getDescrSede())%></font>
		</td>
	</tr>
<%
	if (notificaAlCondannato.getNote() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getNote())%></font>&nbsp;</td>
	</tr>
<%
	}
}
if (notificaAlCondannato.getIstitutoDetenzione() != null) {
	String lNotificaIstituto = notificaAlCondannato.getIstitutoDetenzione().getDescrTipoIstituto();
	if (notificaAlCondannato.getIstitutoDetenzione().getDescrComune() != null)
		lNotificaIstituto += " di " + notificaAlCondannato.getIstitutoDetenzione().getDescrComune();
	if (notificaAlCondannato.getIstitutoDetenzione().getIndirizzo() != null)
		lNotificaIstituto += " - " + notificaAlCondannato.getIstitutoDetenzione().getIndirizzo();
%>
	<tr>
		<td class="L">Istituto Notifica</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(lNotificaIstituto)%></font>
		</td>
	</tr>
<%
}
%>
	<tr>
		<td class="Titolo" colspan="2"> Notifica al Difensore </td>
	</tr>
<%
// Avvocati Siep
for (int i = 0; i < listaNotAvvSiep.size(); i++) {
	NotificaModel lAvvocatoSiep = (NotificaModel) listaNotAvvSiep.get(i);
  	if (lAvvocatoSiep.getAvvSiep() == null) {
    	lAvvocatoSiep.setAvvSiep( new AvvocatoSiepModel() );
	}
%>
	<tr>
		<td class="l">Avvocato per Notifica</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getNome())%></font>
			&nbsp;Foro di&nbsp;
			<font class="campo">
				<%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getForo())%>
			</font>
			&nbsp;Difensore di&nbsp;
			<font class="campo">
				<%=StringUtils.toStringJSP(lAvvocatoSiep.getAvvSiep().getAvvocato().getDescrTipo())%>
			</font>
		</td>
	</tr>
<%
	if (lAvvocatoSiep.getAutoritaEsterna() == null) {
		lAvvocatoSiep.setAutoritaEsterna( new AutoritaEsternaModel());
	}
%>
	<tr>
		<td class="l">Autorita Notifica</td>
		<td class="L">
    		<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getAutoritaEsterna().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getAutoritaEsterna().getDescrSede())%></font>
		</td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocatoSiep.getNote(), "-")%></font>
		</td>
	</tr>
<%
}
if (listaNotObbligati.size() > 0) {
%>
	<tr>
		<td class="Titolo" colspan="2"> Civilmente Obbligati </td>
	</tr>
<%
}
// Civilmente Obbligati
for (int i = 0; i < listaNotObbligati.size(); i++) {
	NotificaModel lNotificaObbligato = (NotificaModel) listaNotObbligati.get(i);
	if (lNotificaObbligato.getCivilmenteObbligato() == null) {
 		lNotificaObbligato.setCivilmenteObbligato(new CivilmenteObbligatoModel());
	}
	CivilmenteObbligatoModel com = lNotificaObbligato.getCivilmenteObbligato();
%>
	<tr>
		<td class="l">Civilmente Obbligato</td>
		<td class="L">
			<font class="campo"><%=com.getCognome()%></font>&nbsp;
			<font class="campo"><%=com.getNome()%></font>&nbsp;
			<font class="label">nato a</font>&nbsp;<font class="campo"><%=com.getDescComuneNascita()%></font>&nbsp;
			<font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(com.getDataNascita(), "dd-MM-yyyy"))%></font>
<%
	if ("G".equals(com.getCodPersona())) {
%>
			<font class="label">&nbsp;in qualita' di Legale Rappresentante di&nbsp;</font>
			<font class="campo"><%=com.getDenominazione()%></font>
<%
	}
%>   
		</td>
	</tr>
<%
	if (lNotificaObbligato.getAutoritaEsterna() == null) {
		lNotificaObbligato.setAutoritaEsterna(new AutoritaEsternaModel());
	}
%>
	<tr>
	  	<td class="l">Autorita Notifica</td>
	  	<td class="L">
	    	<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrSede())%></font>
	  	</td>
	</tr>
	<tr>
	  	<td class="l">Note</td>
	  	<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotificaObbligato.getNote(), "-")%></font></td>
	</tr>
<%
} // end ciclo for
%>
</table>
<%
if ("S".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>   
<table>
	<tr>
    	<td class="L">
      		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadNotificheOrdineIngiunzione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>">Visualizza stato notifiche</a>
    	</td>
	</tr>
</table>
<%
}
%>
<br>
<div align=left style="visibility:hidden" id="upld">
<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
	<tr>
  		<td class="L">
			<input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.rateizzazionepp.action.ActUploadAvvisoMancatoPagamento">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.rateizzazionepp.action.ActDettaglioAvvisoMancatoPagamento">
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>