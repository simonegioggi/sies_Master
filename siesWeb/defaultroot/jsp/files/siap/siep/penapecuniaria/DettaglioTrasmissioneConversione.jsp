<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="lPenComSanSost"      scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="residenzaassociata"  scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<%
	EventoNotificaModel lEve = evento;

	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session
			.getAttribute("fascicolo");

	PosizioneGiuridicaModel lPosizione = posizioneluogoaltra
			.getPosizioneGiuridica();
	LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra
			.getLuogoDetenzione();
	AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

	if (lPosizione == null)
		lPosizione = new PosizioneGiuridicaModel();

	if (lLuogoDetenzione == null)
		lLuogoDetenzione = new LuogoDetenzioneModel();

	if (lAltraCausa == null)
		lAltraCausa = new AltraCausaModel();

	MagistratoModel lMagistrato = lEve.getMagistrato();
	if (lMagistrato == null)
		lMagistrato = new MagistratoModel();

	PenaComplessivaSanzioneSostitutivaModel lPenaComplessSSMod = lPenComSanSost;
// 	if (lPenaComplessSSMod == null)
// 		lPenaComplessSSMod = new PenaComplessivaSanzioneSostitutivaModel();
%>
<html>
<head>
	<title>[S.I.E.S.] - Dettaglio Trasmissione Conversione</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	<script language="JavaScript">
		function conferma(azione)
		{
		  document.comandi.<%=IWebConstants.ACTION_FIELD%>.value = azione;
		  document.comandi.submit();
		}
	</script>
</head>
<BODY class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			<td class="LBG">
				<font class="label">Funzione :</font>&nbsp;
				<font class="campo">Dettaglio Trasmissione Richiesta Conversione</font>
			</td>
			<%if (lEve.getEvento().getFlagDocumentoRegistrato()!=null)
				if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
				{%>
					<!-- BOTTONE DI STAMPA -->
					<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
					<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.penapecuniaria.action.ActStampaTrasmissioneConversione&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
					</jsp:include>
				<%}%>
				<%if (lEve.getEvento().getFlagDocumentoRegistrato()==null)
				{%>
					<!-- BOTTONE DI STAMPA -->
					<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
					<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.penapecuniaria.action.ActStampaTrasmissioneConversione&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
					</jsp:include>
				<%}%>
      				<!-- TOOLBAR HEADER -->
      
      		<td class="LBG">
          		<jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
          		<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          		<jsp:param name="ValoreIdEntita" value="<%=lEve.getEvento().getIdEvento()%>" />
          		<jsp:param name="FlagDocumentoRegistrato" value="<%=lEve.getEvento().getFlagDocumentoRegistrato()%>" />
          		</jsp:include>
     		</td>				
          
		</tr>
	</table>
	<br>
    	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>
    <table>
		<tr>
			<td class="l">Posizione Giuridica </td>
			<td class="L" colspan=5> 
				<font class="campo">
					<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
					{%>
						DETENUTO PER ALTRA CAUSA
					<%}else{%>
						<%=lPosizione.getDescrPosizioneGiuridica()%>
					<%}%>
				</font>
				<%if(lPosizione.isLibero() && residenzaassociata != null && residenzaassociata.getResidenza()!= null)
				{%>   
					Residenza 
					<font class="campo">
						<%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%>
					</font>
				<%}%>           
			</td>
		</tr>
		<%
		if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
		{
			if( lAltraCausa.getIstitutoDetenzione()!= null)
			{
			%>
			<tr>
				<td class="l">Detenuto presso </td>
				<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
				</td>
			</tr>
			<%if (lAltraCausa.getAltroLuogo()!=null)
			{%>
				<tr>
				<td class="l">Altro Luogo </td >
				<td class="L" colspan=5>
				<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
				</td>
				</tr>
			<%}
			}
		}else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
		{%>
			<tr>
				<td class="l">Detenuto presso </td>
				<td class="L" colspan=5>
					<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
					di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
				</td>
			</tr>
		<%}%>
		
		<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
		if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
		{
			if(lLuogoDetenzione.getAltroLuogo() != null)
			{%>
				<tr>
				<td class="l">Indirizzo</td>
				<td class="L" colspan=5>
				<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
				</td>
				</tr>
		<%}
		}%>

	<tr>
		<td class="l">Anno / Numero Partita</td>
		<td class="l"><font class="campo">
		<%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita())%>
		/
		<%=StringUtils.toStringJSP(richiestaconversione.getNumPartita())%></font>&nbsp;</td>
							
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione
							.getNumExCampione())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Autorità</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione
							.getDescrTipoAutoritaEmittente())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione
							.getDescrLuogoEmittente())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data Ricezione Atto</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							richiestaconversione.getDataRicezioneAtto(),
							"dd-MM-yyyy"))%>
		</font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data Iscrizione Atto</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							richiestaconversione.getDataIscrizioneAtto(),
							"dd-MM-yyyy"))%>
		</font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Data Esazione</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							richiestaconversione.getDataEsazione(),
							"dd-MM-yyyy"))%>
		</font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l">Multa: Importo</td>
		<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione
							.getImportoMulta())%></font>&nbsp;</td>
		<td class="l">Data Prescrizione</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							richiestaconversione.getDataPrescrizioneMulta(),
							"dd-MM-yyyy"))%>
		</font>&nbsp;</td>
		<%
		if (richiestaconversione.getFlagImprescrittibileMulta().equals("S")) {
		%>
		<td class="l">Imprescrittibile</td>
		<%
		}
		%>
	</tr>
	<tr>
		<td class="l">Ammenda: Importo</td>
		<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione
							.getImportoAmmenda())%></font>&nbsp;</td>
		<td class="l">Data Prescrizione</td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							richiestaconversione.getDataPrescrizioneAmmenda(),
							"dd-MM-yyyy"))%>
		</font>&nbsp;</td>
		<%
					if (richiestaconversione.getFlagImprescrittibileAmmenda().equals(
					"S")) {
		%>
		<td class="l">Imprescrittibile</td>
		<%
		}
		%>
	</tr>

	<%// da qui in poi dati dell'evento
	if (magistrato != null)
	{%>
		<tr>
			<td class="l">Magistrato
			<td class="L"><font class="campo"><%=StringUtils.toStringJSP(magistrato
										.getCognome())%></font>
				<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
			</td>
		</tr>
	<%}
	if (lEve.getNotifiche() != null && lEve.getNotifiche().length > 0
					&& lEve.getNotifiche()[0].getUfficio() != null) 
	{%>
		<tr>
			<td class="l">Data Emissione</td>
			<td class="L" colspan=1><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve
							.getEvento().getDataEmissione(), "dd/MM/yyyy"))%></font></td>
			<td class="l">Data Trasmissione</td>
			<td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve
							.getNotifiche()[0].getDataInvio(), "dd/MM/yyyy"))%></font></td>
		</tr>
		<tr>
			<td class="l">Autorità Destinazione</td>
			<td class="L" colspan=2><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0]
								.getUfficio().getDescrTipoUfficio())%></font>&nbsp;
			di <font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0]
								.getUfficio().getDescrComune())%></font>&nbsp;
			</td>
		</tr>
	<%}%>
	<%if (lEve.getNotifiche() != null && lEve.getNotifiche().length > 1
					&& lEve.getNotifiche()[1].getAutoritaEsterna() != null) 
	{%>
		<tr>
			<td class="l">Autorità Destinazione</td>
			<td class="L" colspan=2>
				<font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[1].getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
				di 
				<font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[1].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			</td>
		</tr>
	<%}%>

	</table>
	<br>

	<%
				if (lEve.getEvento().getFlagDocumentoRegistrato() == null
				|| (lEve.getEvento().getFlagDocumentoRegistrato() != null && lEve
				.getEvento().getFlagDocumentoRegistrato()
				.compareTo("N") == 0)) {
	%>


	<div align=left style="visibility:hidden" id="upld">
	<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
	
	     <table>
          	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        	<tr>
          		<td class="L">
				
				<input  class=bottone  type="submit" value="Conferma">
				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penapecuniaria.action.ActUploadTrasmissioneConversione">
				<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getEvento().getIdEvento() %>">
				<input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=lEve.getNotifiche()[0].getUfficio().getCodUfficio()%>">
          		</td>
        	</tr>
      	</table>		
	</FORM>
	</div>

	<%
		}

		if (lEve.getEvento().getFlagDocumentoRegistrato() != null
				&& lEve.getEvento().getFlagDocumentoRegistrato()
				.equals("S")
				&& lEve.getEvento().getCodUfficioDestinatario() != null
				&& lEve.getEvento().getCodUfficioDestinatario().equals("-")) {
	%>
	<div align=left style="visibility:hidden" id="upld">
	<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
	
	     <table>
          	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        	<tr>
          		<td class="L">
				
				<input  class=bottone  type="submit" value="Conferma">
				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penapecuniaria.action.ActUploadTrasmissioneConversione">
				<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getEvento().getIdEvento() %>">
				<input type="HIDDEN" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>" value="<%=lEve.getNotifiche()[0].getUfficio().getCodUfficio()%>">
          		</td>
        	</tr>
      	</table>		
	</FORM>
	</div>
	<%
	}
	%>
	<br>
	<br>
</body>

</html>