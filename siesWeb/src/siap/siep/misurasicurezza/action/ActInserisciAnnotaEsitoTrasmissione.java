package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action che effettua l'inserimento del provvedimento di annotazione esito
 * 
 * @author d.fiorletta
 *
 */
public class ActInserisciAnnotaEsitoTrasmissione extends ActionSiap implements
		ICostantiAnnotazioneEsitoTrasmissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoNotificaModel lEveNotModel = new EventoNotificaModel();

		// setto il tipo provvedimento
		lEveNotModel.getEvento().setCodTipoEvento("01");
		lEveNotModel.getEvento().setCodTipoProvvedimento(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO));
		lEveNotModel.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		lEveNotModel.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveNotModel.getEvento().setDataEmissione(lDataEmissione);
		// lEve.getEvento().setDataTrasmissioneAtti(lDataTrasmissione); NO PREVISTA

		lEveNotModel.getEvento().setCodMagistrato(
				getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveNotModel.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveNotModel.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveNotModel.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNotModel.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lEveNotModel.getEvento().setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveNotModel.getEvento().setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveNotModel.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveNotModel.getEvento().setCodEsito("-");
		lEveNotModel.getEvento().setCodLuogoDestinatario("-");
		lEveNotModel.getEvento().setCodTipoUfficioDestinatario("-");

		lEveNotModel.getEvento().setFlagStampaSiep("S");
		lEveNotModel.getEvento().setFlagVideoSiep("S");
		lEveNotModel.getEvento().setFlagDocumentoRegistrato(null); // Diventa N in fase di inserimento del
																	// BLOB

		// ==========================================================================
		// Eventuale nota
		// ==========================================================================
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("Aggiungo CampoNotaModel");
			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = new CampoNotaModel();

			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));

			lCampoNote.add(lCampMod);

			lEveNotModel.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

		// ==================================================
		// Dati dell'annotazione
		// ==================================================
		AnnotazioneEsitoTrasmissioneModel lAnnEsitoModel = new AnnotazioneEsitoTrasmissioneModel();

		// lAnnEsitoModel.setIdEsitoTrasmissione ( );
		lAnnEsitoModel.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		if (!getRequestStringParameter(CAMPO_OGGETTO_TRASMISSIONE).equals(""))
			lAnnEsitoModel.setOggettoTrasmissione(getRequestStringParameter(CAMPO_OGGETTO_TRASMISSIONE));
		else
			lAnnEsitoModel.setOggettoTrasmissione("00071");

		lAnnEsitoModel.setCodUfficioDestinatario(getRequestStringParameter(CAMPO_COD_UFFICIO_DESTINATARIO));
		// lAnnEsitoModel.setCodUfficioInoltrante ( getRequestStringParameter ( CAMPO_COD_UFFICIO_INOLTRANTE)
		// );

		if (!isRequestParameterNullObj(CAMPO_TIPO_UFFICIO_INOLTRO)
				&& getRequestStringParameter(CAMPO_TIPO_UFFICIO_INOLTRO).length() > 0) {
			String lCodiceUffInoltro = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(CAMPO_TIPO_UFFICIO_INOLTRO),
					getRequestStringParameter(CAMPO_COMUNE_UFFICIO_INOLTRO));
			lAnnEsitoModel.setCodUfficioInoltro(lCodiceUffInoltro);
		}

		String lCodiceUffEsito = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_TIPO_UFFICIO_ESITO),
				getRequestStringParameter(CAMPO_COMUNE_UFFICIO_ESITO));

		lAnnEsitoModel.setCodUfficioEsito(lCodiceUffEsito);
		lAnnEsitoModel.setDataEsito(getRequestDateParameter(CAMPO_ANNO_DATA_ESITO, CAMPO_MESE_DATA_ESITO,
				CAMPO_GIORNO_DATA_ESITO));
		lAnnEsitoModel.setCodEsito(getRequestStringParameter(CAMPO_COD_ESITO));

		if (!isRequestParameterNullObj(CAMPO_NOTE_ESITO)
				&& getRequestStringParameter(CAMPO_NOTE_ESITO).length() > 0)
			lAnnEsitoModel.setNoteEsito(getRequestStringParameter(CAMPO_NOTE_ESITO));

		if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(lAnnEsitoModel.getCodEsito())) {
			lAnnEsitoModel.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
			lAnnEsitoModel.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
			lAnnEsitoModel.setChiaveUfficio(lAnnEsitoModel.getCodUfficioEsito());
		}

		// lAnnEsitoModel.setEveIdEvento ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
		lAnnEsitoModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		if (!isRequestParameterNullObj(CAMPO_MES_ID_MESSAGGIO_RICHIESTA)
				&& getRequestStringParameter(CAMPO_MES_ID_MESSAGGIO_RICHIESTA).length() > 0)
			lAnnEsitoModel
					.setMesIdMessaggioRichiesta(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_RICHIESTA));
		if (!isRequestParameterNullObj(CAMPO_MES_ID_MESSAGGIO_ESITO)
				&& getRequestStringParameter(CAMPO_MES_ID_MESSAGGIO_ESITO).length() > 0)
			lAnnEsitoModel
					.setMesIdMessaggioEsito(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_ESITO));

		lAnnEsitoModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnEsitoModel.setDataInserimento(DateUtils.getSysDate());
		lAnnEsitoModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		//
		// ==========================================================================
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Evento: "+lEveNotModel.getEvento());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Annotazione: "+lAnnEsitoModel);

		// ===============================================
		// Sono in modifica cancello l'evento e lo reinserisce
		// ===============================================
		if (!isRequestParameterNullObj("modalita")) {
			if ("M".equals(getRequestStringParameter("modalita"))) {
				// Cancella l'evento precedente
				BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
				EventoModel lEveModel = new EventoModel();

				IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
				lEveModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

				IOrdineEsecuzione lOECtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				lOECtrl.ExCancellaEventoConStorePocedure(lEveModel);
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio Inserimento");
		IMisuraSicurezza lCtrlMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
		BigDecimal lIdEventoInserito = lCtrlMis.ExInserisciAnnotazioneEsito(lEveNotModel, lAnnEsitoModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("idEvento Inserito = " + lEveNotModel.getEvento().getIdEvento());
		//
		// FIXME 'MS' Se esito = iscritto allora aggiorno anche la tabella FASC_MS_TO_FASC_SIEP. Verificare se
		// in fase di validazione
		// if (1==1)
		// throw new SIUSException(SICOException.USER_MESSAGE, "Ritorno dopo debug.");

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotaEsitoTrasmissione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEventoInserito;

		return lPage;
	}

}