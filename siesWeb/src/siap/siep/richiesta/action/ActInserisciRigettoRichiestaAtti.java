package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.action.ICostantiCompetenza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.DatiSiepPerTrasferimentoModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRigettoRichiestaAtti Description: Classe Action per INSERIMENTO Comunicazione di RIGETTO
 * RICHIESTA Trasmissione Atti per Competenza
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciRigettoRichiestaAtti extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();

		// Inserimento del provvedimento (EVENTO)

		lEve.getEvento().setCodTipoEvento("01"); // 01 - Provvedimento
		lEve.getEvento().setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO)); // 12 -
																							// Comunicazione
		lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO)); // 0599 -
																									// Rigetto
																									// Richiesta

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		lEve.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());

		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		lEve.getEvento().setFlagStampaSiep("S");
		lEve.getEvento().setFlagVideoSiep("S");
		// lEve.getEvento().setFlagDocumentoRegistrato("N"); inizialmente impostato a null

		siesLogger.debug("--XX-- ActInserisciRigetto - Evento = " + lEve.getEvento());

		// Il campo 'CONTENUTO' è archiviato nella tabella CampoNote
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
				&& getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals("")) {

			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = new CampoNotaModel();
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lUff.getCodUfficio());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));

			lCampoNote.add(lCampMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// ALTRO DESTINATARIO
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO) != null
				&& !getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO).equals("-")) {
			NotificaModel lNotAltroDestinatario = new NotificaModel();

			lNotAltroDestinatario.setCodTipoNotifica("AA"); // C = Comunicazione - N = Notifica - AA =
															// Autorità Esterna
			lNotAltroDestinatario.setDataInvio(lDataTrasmissione);
			lNotAltroDestinatario.setCodEsito("-");
			lNotAltroDestinatario.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotAltroDestinatario.setDataInserimento(DateUtils.getSysDate());
			lNotAltroDestinatario.setCodUfficioInserimento(lUff.getCodUfficio());
			// lNotAltroDestinatario.setNote(getRequestStringParameter("AltroDestinatario"));
			// ===
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO));

			// in realtà ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO è la DESCRIZIONE del comune
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO)));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lUff.getCodUfficio());
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNotAltroDestinatario.setAutoritaEsterna(lAut);
			// ===

			lNotifiche.add(lNotAltroDestinatario);

		}

		// ======== INSERIMENTO NELLA TABELLA COMPETENZA ==========

		CompetenzaModel lComp = new CompetenzaModel();
		String lCodiceUffDaNotificare = null;

		// Fascicolo SEMPRE proveniente da Mesaggio di richiesta
		BigDecimal lIdMessaggio = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

		siesLogger.debug("Dati caricati da messaggio = " + lIdMessaggio);

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);

		// Deserializzazione del BLOB contenuto nel MESSAGGIO di Richiesta
		ParserMessage lParser = new ParserMessage(lMessModel.getTreeModel());

		DettaglioFascicoloModel lDettaglioFasModel = null;
		if (lParser != null && lParser.getDettaglioFascicoloSiep() != null)
			lDettaglioFasModel = (DettaglioFascicoloModel) lParser.getDettaglioFascicoloSiep();

		FascicoloSiepModel fascicoloSIEPCumulante = null;
		if (lDettaglioFasModel != null && lDettaglioFasModel.getFascicoloSiep() != null
				&& lDettaglioFasModel.getFascicoloSiep().getIdFascicoloSiep() != null) {
			fascicoloSIEPCumulante = (FascicoloSiepModel) lDettaglioFasModel.getFascicoloSiep();
		}

		SentenzaModel sentenzaRicevuta = null;
		if (fascicoloSIEPCumulante != null && fascicoloSIEPCumulante.getSentenza() != null
				&& fascicoloSIEPCumulante.getSentenza().getIdSentenza() != null) {
			sentenzaRicevuta = (SentenzaModel) fascicoloSIEPCumulante.getSentenza();
		}

		// Estremi del titolo che avrebbe determinato la competenza: li prendo da fascicoloSIEP e
		// sentenzaRicevuta
		lComp.setCodTipoProvvedimento(sentenzaRicevuta.getCodTipoProvvedimento());
		lComp.setDataProvvedimento(sentenzaRicevuta.getDataProvvedimento());
		lComp.setCodTipoAutoritaEmittente(sentenzaRicevuta.getCodTipoAutoritaEmittente());
		lComp.setCodLuogoEmittente(sentenzaRicevuta.getCodLuogoEmittente());
		lComp.setNumSezioneAutoritaEmittente(sentenzaRicevuta.getNumSezioneAutoritaEmittente());
		lComp.setAnnoSentenza(sentenzaRicevuta.getAnnoSentenza());
		lComp.setNumeroSentenza(sentenzaRicevuta.getNumeroSentenza());
		lComp.setDataIrrevocabilita(fascicoloSIEPCumulante.getDataIrrevocabilita());

		// Procedimento che sarebbe stato il Cumulante
		lComp.setFasSieIdFascicoloSiep(fascicoloSIEPCumulante.getIdFascicoloSiep());
		lComp.setSenIdSentenza(sentenzaRicevuta.getIdSentenza());

		lComp.setChiaveAnno(lMessModel.getChiaveAnnoFasCumulante());
		lComp.setChiaveProgr(lMessModel.getChiaveProgrFasCumulante());
		lComp.setChiaveUfficio(lMessModel.getChiaveUfficioFasCumulante());

		// Ufficio Competente
		// IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		UfficioModel mUffComp = getUfficioByCodUfficio(lMessModel.getChiaveUfficioFasCumulante());

		lComp.setCodTipoAutoritaComp(mUffComp.getCodTipoUfficio());
		lComp.setCodLuogoAutoritaComp(mUffComp.getCodComune());
		lComp.setCodUfficioAutoritaComp(mUffComp.getCodUfficio());

		lComp.setIdMessaggiorichiesta(new BigDecimal(lMessModel.getJmsCorrelationIdMessage()));

		// NEW -->
		// prendoil Record Competenza del BLOB del messaggio di Richiesta
		CompetenzaModel Comp_Titolo_Rich = null;
		DatiSiepPerTrasferimentoModel lDSPT = null;
		Vector lListaCompetenze = null;

		if (lDettaglioFasModel != null && lDettaglioFasModel.getDatiSiepPerTrasferimento() != null) {
			lDSPT = (DatiSiepPerTrasferimentoModel) lDettaglioFasModel.getDatiSiepPerTrasferimento();
			if (lDSPT != null && lDSPT.getListCompetenze() != null && lDSPT.getListCompetenze().size() > 0)
				lListaCompetenze = (Vector) lDSPT.getListCompetenze();
		}

		// Estremi del Titolo Richiesto: li prendo da COMPETENZA del Blob del messaggio di Richiesta
		if (lListaCompetenze != null && !lListaCompetenze.isEmpty()) {
			Comp_Titolo_Rich = (CompetenzaModel) lListaCompetenze.lastElement();

			lComp.setCodTipoProvvedimento_Rich(Comp_Titolo_Rich.getCodTipoProvvedimento());
			lComp.setDataProvvedimento_Rich(Comp_Titolo_Rich.getDataProvvedimento());
			lComp.setCodTipoAutoritaEmittente_Rich(Comp_Titolo_Rich.getCodTipoAutoritaEmittente());
			lComp.setCodLuogoEmittente_Rich(Comp_Titolo_Rich.getCodLuogoEmittente());
			lComp.setNumSezioneAutoritaEmittente_Rich(Comp_Titolo_Rich.getNumSezioneAutoritaEmittente());
			lComp.setAnnoSentenza_Rich(Comp_Titolo_Rich.getAnnoSentenza());
			lComp.setNumeroSentenza_Rich(Comp_Titolo_Rich.getNumeroSentenza());
			lComp.setDataIrrevocabilita_Rich(Comp_Titolo_Rich.getDataIrrevocabilita());
		}

		// Estremi del Soggetto legato al Titolo Richiesto:
		// Prendo i dati dal messaggio di Richiesta (SOGGETTO LEGATO AL TITOLO CUMULANTE)
		lComp.setCognome_Soggetto_Rich(lMessModel.getCognomeSoggetto());
		lComp.setNome_Soggetto_Rich(lMessModel.getNomeSoggetto());
		lComp.setDataNascita_Soggetto_Rich(lMessModel.getDataNascita());
		lComp.setCodStatoNascita_Soggetto_Rich(lMessModel.getCodStatoNascita());
		lComp.setCodComuneNascita_Soggetto_Rich(lMessModel.getCodComuneNascita());

		if (getRequestStringParameter(ICostantiCompetenza.CAMPO_CODICE_CUI_SOGGETTO_RICH) != null
				&& !getRequestStringParameter(ICostantiCompetenza.CAMPO_CODICE_CUI_SOGGETTO_RICH)
						.equals("")) {
			lComp.setCodiceCui_Soggetto_Rich(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_CODICE_CUI_SOGGETTO_RICH));
		}

		// -- END NEW

		lCodiceUffDaNotificare = lMessModel.getChiaveUfficioFasCumulante();

		// Paolo Cherubini 13/05/2011 aggiungo notifica ad ufficio altrimenti non esce nelle stampe
		NotificaModel lNotUffComp = new NotificaModel();

		lNotUffComp.setCodTipoNotifica("C"); // C = Comunicazione
		lNotUffComp.setDataInvio(lDataTrasmissione);
		lNotUffComp.setCodEsito("-");
		lNotUffComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNotUffComp.setDataInserimento(DateUtils.getSysDate());
		lNotUffComp.setCodUfficioInserimento(lUff.getCodUfficio());
		lNotUffComp.setUffCodUfficio(lCodiceUffDaNotificare);

		lNotifiche.add(lNotUffComp);

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		lComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lComp.setDataInserimento(DateUtils.getSysDate());
		lComp.setCodUfficioInserimento(lUff.getCodUfficio());

		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel lNewComp = lCompCtrl.ExInserisciCompetenzaEventoNotifica(lComp, lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioRigettoRichiestaAtti&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lNewComp.getEveIdEvento().toString();

		return lPage;
	}

}