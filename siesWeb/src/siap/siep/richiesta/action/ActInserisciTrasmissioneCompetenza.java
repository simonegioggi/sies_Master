package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

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
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.action.ICostantiCompetenza;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: ActInserisciTrasmissioneCompetenza
 * </p>
 * <p>
 * Description: ActInserisciTrasmissioneCompetenza
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>> START - Cod_motivo =
		// "+getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		// siesLogger.debug("--XX-- >>>>>>>>>>>>>>>>>>> START - Fasc trovato =
		// "+getRequestStringParameter(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO));
		EventoNotificaModel lEve = new EventoNotificaModel();

		// setto il tipo provvedimento
		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = Provvedimento 14/01/08
		lEve.getEvento().setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		siesLogger.debug("--XX-- Fascicolo in sessione " + lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// Gestisco l'inserimento del firmatario
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
		lEve.getEvento().setFlagDocumentoRegistrato("N");

		// imposto il campo contenuto nella tabella CampoNote
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = new CampoNotaModel();
			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(lUff.getCodUfficio());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));

			lCampoNote.add(lCampMod);

			lEve.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// Preparo le notifiche
		ArrayList lNotifiche = new ArrayList();

		// ALTRO DESTINATARIO
		if (!isRequestParameterNullObj("AltroDestinatario")
				&& getRequestStringParameter("AltroDestinatario") != null
				&& !getRequestStringParameter("AltroDestinatario").equals("-")) {
			NotificaModel lNotAltroDestinatario = new NotificaModel();

			lNotAltroDestinatario.setCodTipoNotifica("E");
			lNotAltroDestinatario.setDataInvio(lDataTrasmissione);
			lNotAltroDestinatario.setCodEsito("-");
			lNotAltroDestinatario.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotAltroDestinatario.setDataInserimento(DateUtils.getSysDate());
			lNotAltroDestinatario.setCodUfficioInserimento(lUff.getCodUfficio());
			// lNotAltroDestinatario.setNote(getRequestStringParameter("AltroDestinatario"));
			// ===
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(getRequestStringParameter("AltroDestinatario"));

			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter("SedeAltroDestinatario")));

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
		// i valori sono relativi al fascicolo ricercato (o imputato)
		CompetenzaModel lComp = new CompetenzaModel();
		// campi presi dall'evento corrente

		String lCodiceUffDaNotificare = null;

		// ======================================================================================================
		// Controllo se Trasferimento Comptz e/o Seguito Atti sono su STESSA BDI: in questo caso non c'è il
		// BLOB
		// ==================================================================================================
		Boolean lStessaBdi = false;
		MessaggioModel lMessRichModel = null;

		if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)
				&& getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) != null) {
			BigDecimal lIdMessag = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

			// siesLogger.debug("--XX-- Controllo stessa BDI = "+lIdMessag);
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			lMessRichModel = (MessaggioModel) lCrtl.ExRicercaMessaggioByKey(lIdMessag);

			if (lMessRichModel != null && lMessRichModel.getIdMessaggio() != null) {
				if (lMessRichModel.getCodBdiMittente().equals(lMessRichModel.getCodBdiDestinataria())) {
					lStessaBdi = true;
				}
			}

		}

		if (!this.isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)
				&& getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO) != null) {
			// Fascicolo proveniente da Mesaggio di richiesta con diversa BDI
			BigDecimal lIdMessaggio = this
					.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);

			siesLogger.debug("--XX-- Dati caricati da messaggio = " + lIdMessaggio);

			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMessModel = lCrtl.ExRicercaMessaggioByKey(lIdMessaggio);

			ParserMessage lParser = new ParserMessage(lMessModel.getTreeModel());
			DettaglioFascicoloModel lDettaglioFasModel = lParser.getDettaglioFascicoloSiep();

			FascicoloSiepModel fascicoloSIEP = lDettaglioFasModel.getFascicoloSiep();
			SentenzaModel sentenzaRicevuta = fascicoloSIEP.getSentenza();

			// Estremi del titolo che determina la competenza (sentenza)
			lComp.setCodTipoProvvedimento(sentenzaRicevuta.getCodTipoProvvedimento());
			lComp.setDataProvvedimento(sentenzaRicevuta.getDataProvvedimento());
			lComp.setCodTipoAutoritaEmittente(sentenzaRicevuta.getCodTipoAutoritaEmittente());
			lComp.setCodLuogoEmittente(sentenzaRicevuta.getCodLuogoEmittente());
			lComp.setNumSezioneAutoritaEmittente(sentenzaRicevuta.getNumSezioneAutoritaEmittente());
			lComp.setAnnoSentenza(sentenzaRicevuta.getAnnoSentenza());
			lComp.setNumeroSentenza(sentenzaRicevuta.getNumeroSentenza());
			lComp.setDataIrrevocabilita(fascicoloSIEP.getDataIrrevocabilita());

			// Procedimento Cumulante
			// FIXME completare i dati delle chiavi dovrebbero esistere
			// lComp.setFasSieIdFascicoloSiep (findedFasc.getIdFascicoloSiep());
			// lComp.setSenIdSentenza (aValore);
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

			lCodiceUffDaNotificare = lMessModel.getChiaveUfficioFasCumulante();

			// siesLogger.debug ("--XX-- Competenza = "+lComp);
		} else if (!this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO)
				&& isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA)) {
			// fascicolo e sentenza competenti provenienti da ricerca nel distretto
			siesLogger.debug("--XX-- Caricati Da ricerca, parametri presi da form; fascicolo trovato");

			IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			FascicoloSiepModel findedFasc = lFasCtrl.ExRicercaFascicoloByKey(
					getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO));

			ISentenza lCrtlSentenza = SIEPLookupRemote.getSentenzaRemote();
			SentenzaModel aSent = lCrtlSentenza.ExRicercaSentenzaByKey(findedFasc.getSenIdSentenza());

			// Estremi del titolo che determina la competenza (sentenza)
			lComp.setCodTipoProvvedimento(aSent.getCodTipoProvvedimento());
			lComp.setDataProvvedimento(aSent.getDataProvvedimento());
			lComp.setCodTipoAutoritaEmittente(aSent.getCodTipoAutoritaEmittente());
			lComp.setCodLuogoEmittente(aSent.getCodLuogoEmittente());
			lComp.setNumSezioneAutoritaEmittente(aSent.getNumSezioneAutoritaEmittente());
			lComp.setAnnoSentenza(aSent.getAnnoSentenza());
			lComp.setNumeroSentenza(aSent.getNumeroSentenza());
			lComp.setDataIrrevocabilita(findedFasc.getDataIrrevocabilita());

			lComp.setSenIdSentenza(aSent.getIdSentenza());

			// Estremi del procedimento competente (fascicolo)
			lComp.setFasSieIdFascicoloSiep(findedFasc.getIdFascicoloSiep());
			lComp.setChiaveAnno(findedFasc.getChiaveAnno());
			lComp.setChiaveUfficio(findedFasc.getChiaveUfficio());
			lComp.setChiaveProgr(findedFasc.getChiaveProgr());

			// Stessa Base dati:
			if (lStessaBdi) {
				lComp.setIdMessaggiorichiesta(lMessRichModel.getIdMessaggio());
			}

			if (!isRequestParameterNullObj("lid_MessaggioRichiesta")
					&& getRequestStringParameter("lid_MessaggioRichiesta") != null
					&& !getRequestStringParameter("lid_MessaggioRichiesta").equals("")) {
				lComp.setIdMessaggiorichiesta(
						new BigDecimal(this.getRequestStringParameter("lid_MessaggioRichiesta")));
			}

			// Ufficio Competente
			IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel mUffComp = lCtrlUff.ExRicercaUfficioByCod(findedFasc.getChiaveUfficio());

			lComp.setCodTipoAutoritaComp(mUffComp.getCodTipoUfficio());
			lComp.setCodLuogoAutoritaComp(mUffComp.getCodComune());
			lComp.setCodUfficioAutoritaComp(findedFasc.getChiaveUfficio());

			lCodiceUffDaNotificare = findedFasc.getChiaveUfficio();

		}
		// -------------------------------------------------------------------------------------------------------------
		else if (!this.isRequestParameterNullObj(ICostantiCompetenza.CAMPO_ID_COMPETENZA)) {
			// SEGUITO ATTI - TRASFEROMENTO IN ALTRA BASE DATI - Il nuovo Record Competenza lo scrivo dai dati
			// del Primo Trasferimento per Competenza
			// siesLogger.debug("--XX-- SEGUITO ATTI - dati Caricati Da record Competenza del primo
			// Trasferimento per competenza");

			//// ??????? NON SO SE ENTRA MAI QUI DENTRO !!!!!!!!!

			ICompetenza lCompTrCtrl = SIEPLookupRemote.getCompetenzaRemote();
			CompetenzaModel mTrasfComp = lCompTrCtrl.ExRicercaCompetenzaById(
					getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_ID_COMPETENZA));

			// Estremi del titolo che determina la competenza (sentenza)
			lComp.setCodTipoProvvedimento(mTrasfComp.getCodTipoProvvedimento());
			lComp.setDataProvvedimento(mTrasfComp.getDataProvvedimento());
			lComp.setCodTipoAutoritaEmittente(mTrasfComp.getCodTipoAutoritaEmittente());
			lComp.setCodLuogoEmittente(mTrasfComp.getCodLuogoEmittente());
			lComp.setNumSezioneAutoritaEmittente(mTrasfComp.getNumSezioneAutoritaEmittente());
			lComp.setAnnoSentenza(mTrasfComp.getAnnoSentenza());
			lComp.setNumeroSentenza(mTrasfComp.getNumeroSentenza());
			lComp.setDataIrrevocabilita(mTrasfComp.getDataIrrevocabilita());

			if (mTrasfComp.getSenIdSentenza() != null)
				lComp.setSenIdSentenza(mTrasfComp.getSenIdSentenza()); // ???????

			// Estremi del procedimento competente (fascicolo)
			if (mTrasfComp.getFasSieIdFascicoloSiep() != null)
				lComp.setFasSieIdFascicoloSiep(mTrasfComp.getFasSieIdFascicoloSiep());

			lComp.setChiaveAnno(mTrasfComp.getChiaveAnno());
			lComp.setChiaveUfficio(mTrasfComp.getChiaveUfficio());
			lComp.setChiaveProgr(mTrasfComp.getChiaveProgr());

			// Ufficio Competente

			lComp.setCodTipoAutoritaComp(mTrasfComp.getCodTipoAutoritaComp());
			lComp.setCodLuogoAutoritaComp(mTrasfComp.getCodLuogoAutoritaComp());
			lComp.setCodUfficioAutoritaComp(mTrasfComp.getChiaveUfficio());

			lCodiceUffDaNotificare = mTrasfComp.getChiaveUfficio();

		}
		// -------------------------------------------------------------------------------------------------------------
		else {
			// fascicolo e sentenza inputati manualmente in questo caso non tutti i
			// dati sono necessariamente presenti. In particolare i dati del procedimento
			// (fascicolo) non è detto che siano presenti, anno e numero non sono obbligatori
			siesLogger.debug("--XX-- Dati Imputati manualmente");

			// Estremi del titolo che determina la competenza (sentenza)
			lComp.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO));
			lComp.setDataProvvedimento(
					getRequestDateParameter(ICostantiCompetenza.CAMPO_ANNO_DATA_INSERIMENTO,
							ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO,
							ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO));

			lComp.setCodTipoAutoritaEmittente(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA)));
			lComp.setCodLuogoEmittente(lComModAutEmi.getCodComune());
			lComp.setNumSezioneAutoritaEmittente(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

			lComp.setDataIrrevocabilita(
					getRequestDateParameter(ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA,
							ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA,
							ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA));

			// Estremi del procedimento cumumlante o almeno dell'ufficio competente (fascicolo)
			// Anno e numero fascicolo competente. .n.b. non obbligatori
			lComp.setChiaveAnno(getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_ANNO)); // Chiave
																										// anno
																										// comp
			lComp.setChiaveProgr(getRequestBigDecimalParameter(ICostantiCompetenza.CAMPO_CHIAVE_PROGR)); // Chiave
																											// progr
																											// comp

			// Ricerco la chiave dell'ufficio che ha emesso la sentenza
			// String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
			// getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
			// getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA));
			//
			// // FIXME d.f. da verificare perchè utilizza l'uffico che ha emesso la sentenza e non
			// // quello competente indicato in maschera? Se la sentenza è stata emessa
			// // dal Tribunale Ordinario di Roma (cod Uff 05809102205) verrà eseguita
			// // dalla procura (PM cod uff 05809102104)
			// lComp.setChiaveUfficio (lCodiceUffEmi);

			// UFFICIO COMPETENTE
			// Attenzione CAMPO_COD_LUOGO_EMITTENTE è il cod tipo ufficio e non c'entra nulla con il luogo
			// CAMPO_LUOGO_UFFICIO_TROVATO il la dscrizione sede ufficio
			String lCodiceUffComp = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE), // n.b. è il
																								// COD_TIPO_UFFICIO
					getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO));
			lComp.setChiaveUfficio(lCodiceUffComp);

			ComuneModel lComModAutComp = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO)));

			lComp.setCodTipoAutoritaComp(
					getRequestStringParameter(ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE));
			lComp.setCodLuogoAutoritaComp(lComModAutComp.getCodComune());
			lComp.setCodUfficioAutoritaComp(lCodiceUffComp);

			lCodiceUffDaNotificare = lCodiceUffComp;
		}

		// Paolo Cherubini 13/05/2011 aggiungo notifica ad ufficio altrimenti non esce nelle stampe
		NotificaModel lNotUffComp = new NotificaModel();

		lNotUffComp.setCodTipoNotifica("C");
		lNotUffComp.setDataInvio(lDataTrasmissione);
		lNotUffComp.setCodEsito("-");
		lNotUffComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNotUffComp.setDataInserimento(DateUtils.getSysDate());
		lNotUffComp.setCodUfficioInserimento(lUff.getCodUfficio());
		lNotUffComp.setUffCodUfficio(lCodiceUffDaNotificare);

		lNotifiche.add(lNotUffComp);

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ----------------------------------------------------------------------------------
		// MEV 26 CUMULO Step2 - Inserimento effettuato con una sola connessione al DB - InserisciEvento e
		// InserisciCompetenza sono stati Uniti
		/*
		 * IEvento lCtrl = SICOLookupRemote.getEventoRemote(); EventoNotificaModel lRetModel =
		 * lCtrl.ExInserisciEventoNotifica(lEve);
		 * 
		 * lComp.setCodOperatoreInserimento (lRetModel.getEvento().getCodOperatoreInserimento());
		 * lComp.setDataInserimento (lRetModel.getEvento().getDataInserimento());
		 * lComp.setCodUfficioInserimento (lRetModel.getEvento().getCodUfficioInserimento());
		 * 
		 * lComp.setEveIdEvento (lRetModel.getEvento().getIdEvento());
		 * 
		 * ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote(); CompetenzaModel lNewComp =
		 * lCompCtrl.ExInserisciCompetenza(lComp);
		 */
		// END MEV 26
		// ----------------------------------------------------------------------------------------

		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel lNewComp = lCompCtrl.ExInserisciCompetenzaEventoNotifica(lComp, lEve);
		// CompetenzaModel lNewComp = new CompetenzaModel();
		// ==================

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza&"
				// + ICostantiEvento.CAMPO_ID_EVENTO + "="+ lRetModel.getEvento().getIdEvento() + "&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lNewComp.getEveIdEvento() + "&"
				+ ICostantiCompetenza.CAMPO_ID_COMPETENZA + "=" + lNewComp.getIdCompetenza();
		// + "&modalita=I";

		return lPage;
	}

}