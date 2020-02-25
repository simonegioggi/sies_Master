<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>

<jsp:useBean id="eventonotifica" scope="request"
	class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request"
	class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="StrdataInizioPena" scope="request"
	class="java.lang.String" />
<jsp:useBean id="penaresidua" scope="request"
	class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="fogliocomplementare" scope="request"
	class="java.lang.String" />
<jsp:useBean id="documentoAllegato" scope="request"
	class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />

<%
	EventoNotificaModel lEve = eventonotifica;
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session
			.getAttribute("fascicolo");

	PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
	LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
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

	// Gestione Autorità Esterne sulla prima Notifica
	String lCodTipoAutorita = "-";
	NotificaModel lPrimaNotifica = new NotificaModel();
	AutoritaEsternaModel lPrimaAutoritaEsterna = new AutoritaEsternaModel();

	if (lEve.getNotifiche() != null && lEve.getNotifiche().length > 0) {
		if (lEve.getNotifiche()[0].getAutoritaEsterna() != null) {
			lCodTipoAutorita = lEve.getNotifiche()[0].getAutoritaEsterna()
					.getCodTipoAutorita();
		}

		lPrimaNotifica = lEve.getNotifiche()[0];

		if (lPrimaNotifica.getAutoritaEsterna() != null) {
			lPrimaAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
		}
	}
%>

<html>

<head>
<title>[S.I.E.S.] - Dettaglio Evento-</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript"
	src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<BODY class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();">
				<img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
				class="campo">Dettaglio Ordine esecuzione legge 165/98 -
					condannato libero </font></td>

			<%
				// MEV 16: gestite casistiche per cui far vedere l'icona del FC
				String[] lListaCodMotivi = ICostantiEvento.CODICI_SOSPENSIONE_DELLA_PENA;
				List<String> lCodMotivi = Arrays.asList(lListaCodMotivi);
				String lCodMotivo = eventonotifica.getEvento().getCodMotivo();
				boolean isPresentCodMotivo = false;
				if (lCodMotivo != null && lCodMotivo.length() > 0)
					isPresentCodMotivo = lCodMotivi.contains(lCodMotivo);
				if (isPresentCodMotivo) {
					// FlagDocumentoRegistrato=A  il provvedimento è annullato ==> il bottone "FC" non deve essere visibile
					// FlagDocumentoRegistrato=N  il provvedimento non è validato ==> il bottone "FC" non deve essere visibile
					// FlagDocumentoRegistrato=S  l'evento è stato validato  ==> il bottone "FC" deve essere visibile
					if (documentoAllegato.getIdDocumentoAllegato() != null
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A") != 0
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") != 0
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
						if (documentoAllegato.getDataAnnullamento() == null) {//il foglio complementare esiste ==> azione: modifica foglio complementare
			%>
			<td class="LBG"><a
				href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=ModificaFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
					<img src="/images/fcNsc.gif" width="30" height="30"
					alt="Modifica Foglio Complementare" border="0">
			</a></td>
			<%
				} else {//il foglio complementare non esiste(annullato) ==> azione: inserimento foglio complementare
			%>
			<td class="LBG"><a
				href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
					<img src="/images/fcNsc.gif" width="30" height="30"
					alt="Inserimento Foglio Complementare" border="0">
			</a></td>
			<%
				}
					} else if (documentoAllegato.getIdDocumentoAllegato() == null
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A") != 0
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") != 0
							&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
					//il foglio complementare non esiste ==> azione: inserimento foglio complementare 
					//se il provvedimento è stato validato eventonotifica.getEvento().getFlagDocumentoRegistrato()=="S"
			%>
			<td class="LBG"><a
				href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
					<img src="/images/fcNsc.gif" width="30" height="30"
					alt="Inserimento Foglio Complementare" border="0">
			</a></td>
			<%
					}
				}
			%>

			<%
				if (lEve.getEvento().getFlagDocumentoRegistrato() != null)
					if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
			%>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      		<%--td class="LBG">
        		<a href="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaLSLibero&autorita=<%=lCodTipoAutorita%>&fc=<%=fogliocomplementare%>&IdEvento=<%= lEve.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
          			<img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
        		</a>
      		</td--%>
			<!-- BOTTONE DI STAMPA -->
			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink"
					value="<%="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaLSLibero&autorita="
							+ lCodTipoAutorita
							+ "&fc="
							+ fogliocomplementare
							+ "&IdEvento="
							+ lEve.getEvento().getIdEvento()%>" />
			</jsp:include>
			<%
				}

				if (lEve.getEvento().getFlagDocumentoRegistrato() == null) {
			%>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    		<%--td class="LBG">
      			<a href="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaLSLibero&autorita=<%=lCodTipoAutorita%>&fc=<%=fogliocomplementare%>&IdEvento=<%= lEve.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
        			<img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      			</a>
    		</td--%>
			<!-- BOTTONE DI STAMPA -->
			<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink"
					value="<%="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaLSLibero&autorita="
						+ lCodTipoAutorita
						+ "&fc="
						+ fogliocomplementare
						+ "&IdEvento="
						+ lEve.getEvento().getIdEvento()%>" />
			</jsp:include>
			<%
				}
			%>
		</tr>
	</table>

	<br>
	<jsp:include
		page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp" />
	<br>

	<table>
		<tr>
			<td class="l">Posizione Giuridica</td>
			<%
				if (lFascicoloAssociato.getFlagAltraCausa() != null
						&& lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
			%>
			<td class="L" colspan=3><font class="campo">DETENUTO PER
					ALTRA CAUSA</font></td>
			<%
				} else {
			%>
			<td class="L" colspan=3><font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font>
				<%
					}
				%>
		</tr>
		<%
			if (lFascicoloAssociato.getFlagAltraCausa() != null
					&& lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
				// modifica relativa al tipo istituto
				if (lAltraCausa.getIstitutoDetenzione() != null)
				// if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
				{
		%>
		<tr>
			<td class="l">Detenuto presso</td>
			<td class="L" colspan=3><font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%>
			</font> <%
 	//if(lAltraCausa.getDescrLuogoIstituto()!=null)
 			//{
 %> di<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
				<%
					//}
				%></td>
		</tr>
		<%
			if (lAltraCausa.getAltroLuogo() != null) {
		%>
		<tr>
			<td class="l">Altro Luogo</td>
			<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
			</td>
			<%
				}
					}
				} else if (lAltraCausa.getIstitutoDetenzione() != null)
				//if(!lLuogoDetenzione.getDescrTipoIstituto().equals("") && lLuogoDetenzione.getDescrTipoIstituto()!= null && !lLuogoDetenzione.getDescrTipoIstituto().equals("-"))
				{
			%>
		
		<tr>
			<td class="l">Detenuto presso</td>
			<td class="L" colspan=3><font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
				<%
					//if(lLuogoDetenzione.getDescrLuogo()!=null)
						//{
				%> di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
				<%
					//}
				%></td>
		</tr>
		<%
			if (lLuogoDetenzione.getAltroLuogo() != null) {
		%>
		<tr>
			<td class="l">Altro Luogo</td>
			<td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
			</td>
		</tr>
		<%
			}
			}
		%>
		<%
			// fine modifica relativa al tipo istituto
			if (penaresidua.getIdPenaResidua() != null
					&& ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
							&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua
							.getFlagErgastolo().equals("D")))) {
				if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
						&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
						&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
				} else {
		%>
		<tr>
			<td class="l">Reclusione</td>
			<td class="l" colspan=1><font class="l">Anni&nbsp;</font><font
				class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
				<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
				<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
			</td>
			<td class="l">Multa</td>
			<td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font
				class="l">Euro</font></td>
		</tr>
		<%
			}
		%>
		<tr>
			<%
				if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
							&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
					} else {
			%>
			<td class="l">Arresto</td>
			<td class="l" colspan=1><font class="l">Anni&nbsp;</font><font
				class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
				<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
				<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
			</td>
			<td class="l">Ammenda</td>
			<td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font
				class="l">Euro</font></td>
			<%
				}
				}

				if (penaresidua.getDataInizio() != null) {
			%>
		
		<tr>
			<td class="l">Data Decorrenza Pena</td>
			<td class="L" colspan="1"><font class="campo"><%=StrdataInizioPena%></font></td>
		</tr>
		<%
			}
		%>
		<tr>
			<%
				if (penaresidua.getFlagErgastolo() != null) {
					if (penaresidua.getFlagErgastolo().equals("S")) {
			%>
			<td class="l">Pena Detentiva</td>
			<td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
			<%
				} else if (penaresidua.getFlagErgastolo().equals("D")) {
			%>
			<td class="l">Pena Detentiva</td>
			<td class="L"><font class="campo">ERGASTOLO CON
					ISOLAMENTO DIURNO&nbsp;</font></td>
			<%
				}
				}

				if (((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
						&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua
						.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine() != null) {
					if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
			%>
			<td class="l">Data Fine Pena</td>
			<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							penaresidua.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
			</td>
			<%
				} else {
			%>
			<td class="l">Data Fine Pena</td>
			<td class="lRosso"><font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							penaresidua.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
			</td>


			<%
				}
				}
			%>
		</tr>
		<tr>
			<td class="l">Data Emissione</td>
			<td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento()
					.getDataEmissione(), "dd-MM-yyyy"))%></font>
			</td>
			<td class="l">Data Trasmissione</td>
			<td class="L" colspan=1><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
					lPrimaNotifica.getDataInvio(), "dd-MM-yyyy"))%>
			</font></td>
		</tr>
		<tr>
			<td class="l">Foglio Complementare</td>
			<td class="c"><font class="campo">&nbsp; <%
 	if (fogliocomplementare.equals("1")) {
 %> <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif"
					border="0"> <%
 	}
 %>
			</font></td>
			<%
				if (fogliocomplementare.equals("1")) {
			%>
			<td class="l">Casellario Giudiziale</td>
			<td class="l">
				<%
					for (int i = 0; i < lEve.getNotifiche().length; i++) {
							// La notifica corrispondente al Casellario Giudiziale corrisponde al tipo "C"
							if (lEve.getNotifiche()[i] != null
									&& "C".equals(lEve.getNotifiche()[i].getCodTipoNotifica())
									&& lEve.getNotifiche()[i].getAutoritaEsterna() != null) {
				%> <font class="campo"> <%=StringUtils.toStringJSP(lEve.getNotifiche()[i]
								.getAutoritaEsterna().getDescrSede())%>
			</font> <%
 	}
 		}
 %>
			</td>
			<%
				}
			%>
		</tr>
	</table>
	<table>
		<%
			if (lMagistrato != null) {
		%>
		<tr>
			<td class="l">Magistrato</td>
			<td class="L" colspan="2"><font class="campo"> <%=StringUtils.toStringJSP(lMagistrato.getCognome())%>
					&nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
			</font></td>
		</tr>
		<%
			}

			if (lPrimaNotifica.getAutoritaEsterna() != null) {
		%>
		<tr>
			<td class="l">Autorità Destinazione</td>
			<td class="L" colspan=2><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getAutoritaEsterna()
						.getDescrTipoAutorita())%></font>&nbsp;
				di <font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getAutoritaEsterna()
						.getDescrSede())%></font>&nbsp;
			</td>
		</tr>
		<%
			if (lPrimaNotifica.getNote() != null) {
		%>
		<tr>
			<td class="l">Indirizzo</td>
			<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		</tr>
		<%
			}
			}

			if (lEve.getAvvocati() != null) {
				for (int lIndex = 0; lIndex < lEve.getAvvocati().length; lIndex++) {
		%>
		<tr>
			<td class="l">Avvocato per Notifica</td>
			<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato()
							.getCognome())
							+ " "
							+ StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato()
									.getNome())%></font>&nbsp;
				&nbsp;Foro di&nbsp; <font class="campo"> <%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato()
							.getForo())%>
			</font> &nbsp;Difensore di&nbsp; <font class="campo"> <%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato()
							.getDescrTipo())%>
			</font></td>
		</tr>
		<%
			if (lEve.getNotifiche() != null
							&& (lEve.getNotifiche().length >= lEve.getAvvocati().length)) {
						for (int i = 0; i < lEve.getNotifiche().length; i++) {
							if (lEve.getNotifiche()[i].getAvvIdAvvocatoFascicoloSiep() != null
									&& lEve.getNotifiche()[i].getAvvIdAvvocatoFascicoloSiep()
											.equals(lEve.getAvvocati()[lIndex]
													.getAvvocatoFascicoloSiepModel()
													.getIdAvvocatoFascicoloSiep())) {
								if (lEve.getNotifiche()[i].getAutoritaEsterna() != null) {
		%>
		<tr>
			<td class="l">Autorità Notifica</td>
			<td class="L" colspan=2><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[i]
											.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
				di <font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[i]
											.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			</td>
		</tr>
		<%
			}

								if (lEve.getNotifiche()[i].getNote() != null) {
		%>
		<tr>
			<td class="l">Note</td>
			<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[i]
											.getNote())%></font>&nbsp;</td>
		</tr>
		<%
			}
							}
						}
					}
				}
			}
		%>
	</table>
	<br>
	<%
		if (lEve != null && lEve.getEvento() != null
				&& lEve.getEvento().getFlagDocumentoRegistrato() != null
				&& "S".equals(lEve.getEvento().getFlagDocumentoRegistrato())) {
	%>
	<a class="cliccabile"
		href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadDettaglioScadenzario"
		title="Stato Notifiche">Visualizza Stato Notifiche</a>
	<br>
	<%
		}
	%>
	<div align=left style="visibility: hidden" id="upld">
		<FORM name="comandi" enctype="multipart/form-data" method="post"
			onSubmit="return controllaUpload();">
			<table>
				<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
				<tr>
					<td class="L"><input class="bottone" type="submit"
						value="Conferma"> <input type="HIDDEN" name="fc"
						value="<%=fogliocomplementare%>"> <input type="HIDDEN"
						name="autorita"
						value="<%=lPrimaAutoritaEsterna.getCodTipoAutorita()%>"> <input
						type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
						value="siap.siep.ordineesecuzione.action.ActUploadLS"> <input
						type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"
						value="<%=lEve.getEvento().getIdEvento()%>"> <%--input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.ordineesecuzione.action.ActLoadTrasferisciProvvedimentoLS"--%>
						<input type="HIDDEN"
						name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"
						value="siap.siep.ordineesecuzione.action.ActDettaglioLSLibero">
					</td>
				</tr>
			</table>
		</FORM>
	</div>
	<br>
	<br>
</body>
</html>