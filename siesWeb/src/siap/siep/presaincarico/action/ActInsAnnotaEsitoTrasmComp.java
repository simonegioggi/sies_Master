package siap.siep.presaincarico.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.jms.model.PresaInCaricoModel;
import siap.siep.SIEPException;
import siap.siep.jms.controller.IPresaInCaricoJMS;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action di inserimento dell'annotazione Esito Trasmissione atti per competenza Cumulo
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInsAnnotaEsitoTrasmComp extends ActionSiap
		implements ICostantiAnnotazioneEsitoTrasmissione, ICostantiJMS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

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

		lEveNotModel.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveNotModel.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveNotModel.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveNotModel.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveNotModel.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lEveNotModel.getEvento().setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveNotModel.getEvento().setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());

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
			// siesLogger.debug("--XX-- Aggiungo CampoNotaModel");
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

		lAnnEsitoModel.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
				CAMPO_MESE_DATA_TRASMISSIONE, CAMPO_GIORNO_DATA_TRASMISSIONE));
		if (!getRequestStringParameter(CAMPO_OGGETTO_TRASMISSIONE).equals(""))
			lAnnEsitoModel.setOggettoTrasmissione(getRequestStringParameter(CAMPO_OGGETTO_TRASMISSIONE));
		else
			lAnnEsitoModel.setOggettoTrasmissione(ICostantiJMS.TRASFERIMENTO_COMPETENZA);

		lAnnEsitoModel.setCodUfficioDestinatario(getRequestStringParameter(CAMPO_COD_UFFICIO_DESTINATARIO));

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

		// In caso di presa in carico provo a recuperare i dati del procedimento
		// competente all'emissione di cumulo
		if (ICostantiJMS.PRESAINCARICO.equals(lAnnEsitoModel.getCodEsito())
				|| ICostantiJMS.ASSORBITO_IN_CUMULO.equals(lAnnEsitoModel.getCodEsito())) {

			if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO)
					&& getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO) != null) {
				lAnnEsitoModel.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
				lAnnEsitoModel.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
				lAnnEsitoModel.setChiaveUfficio(lAnnEsitoModel.getCodUfficioEsito());
			}
		}

		// In caso di presa in carico di comunicazione cumulo, provo a scaricare in locale i dati del
		// procedimento cumulante
		// dal treemodel ricevuto.
		BigDecimal lIdMess = null;
		// if (getRequestBigDecimalParameter("idMessaggioComunicazione") != null ) {
		if (!isRequestParameterNullObj("idMessaggioComunicazione")
				&& !getRequestStringParameter("idMessaggioComunicazione").equals("")) {

			// Recupero il messaggio
			lIdMess = getRequestBigDecimalParameter("idMessaggioComunicazione");
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMess);

			// Verifico che il fascicolo da acquisire sia di altra BDI.
			boolean lStessaBDI = false;
			UfficioModel lUfficioFascicoloRicevuto = getUfficioByCodUfficio(
					lMess.getChiaveUfficioFasCumulante());

			if (lUfficioFascicoloRicevuto.getCodDistretto().equals(getCodDistrettoUtenteConnesso())) {
				siesLogger.debug("Fascicolo Cumulante della stessa BDI !");
				lStessaBDI = true;
			} else {
				siesLogger.debug("Fascicolo Cumulante proveniente da fuori Distretto");
				lStessaBDI = false;
			}

			// Se fascicolo di altra BDI procedo all'acquisizione a sistema
			// MessaggioModel lMessReturn = new MessaggioModel();
			if (!lStessaBDI) {
				try {
					MessaggioModel lMessIns = new MessaggioModel(lMess);
					IPresaInCaricoJMS lPres = SIEPLookupRemote.getPresaInCarico();
				  
					// MEV_2024-DNA - Si passano al controller anche le informazione su data utente e ufficio 
			    //                che precede alla presa in carico
			    PresaInCaricoModel lPresaIncaricoModel = new PresaInCaricoModel();
			    lPresaIncaricoModel.setDataPresaInCarico (DateUtils.getSysDate());
			    lPresaIncaricoModel.setCodOperatorePresaInCarico (getCodUtenteConnesso());
			    lPresaIncaricoModel.setCodUfficioPresaInCarico (getCodUfficioUtenteConnesso());
			    
					// /* lMessReturn = */lPres.ExInserisciFascicoloSiep(lMessIns);
					/* lMessReturn = */lPres.ExInserisciFascicoloSiep(lMessIns, lPresaIncaricoModel);
					// MEV_2024-DNA - FINE
					
					// n.b. l'esito dell'acquisizione viene registrato in lMessReturn.getRapportoEsito();
				} catch (F3BException ex) {
					siesLogger.error("Exception >>> " + ex, ex);
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"Si è verificato un errore durante il caricamento dell'Atto pervenuto.<br> Riprovare in seguito.");
				}
			}

			// ==========================================
			// Marco il messaggio di richiesta evaso.
			// ==========================================
			/*
			 * ( eseguito nel controller di modifica più avanti) lMess.setFlagVisto("S");
			 * lMess.setDataEsito(DateUtils.getSysDate()); lMess.setCodEsito("01001");
			 * lCrtl.ExModificaMessaggio(lMess);
			 */
		}

		// lAnnEsitoModel.setEveIdEvento ( getRequestBigDecimalParameter ( CAMPO_EVE_ID_EVENTO) );
		lAnnEsitoModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		if (!isRequestParameterNullObj(CAMPO_MES_ID_MESSAGGIO_RICHIESTA)
				&& getRequestStringParameter(CAMPO_MES_ID_MESSAGGIO_RICHIESTA).length() > 0)
			lAnnEsitoModel.setMesIdMessaggioRichiesta(
					getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_RICHIESTA));
		if (!isRequestParameterNullObj(CAMPO_MES_ID_MESSAGGIO_ESITO)
				&& getRequestStringParameter(CAMPO_MES_ID_MESSAGGIO_ESITO).length() > 0){
			lAnnEsitoModel
					.setMesIdMessaggioEsito(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_ESITO));
		
			// mev 39
			// In caso di presa in carico provo a recuperare i dati del procedimento
			// competente all'emissione di cumulo
			BigDecimal mylIdMess = getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_ESITO);
			IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
			MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(mylIdMess);
			if(lMess!=null && "00079".equals(lMess.getCodTipoOperazione())){								
					if(lMess.getDataEsito()!= null)
						lAnnEsitoModel.setDataEsito(lMess.getDataEsito());
					else
						lAnnEsitoModel.setDataEsito(getRequestDateParameter(CAMPO_ANNO_DATA_ESITO, CAMPO_MESE_DATA_ESITO,
								CAMPO_GIORNO_DATA_ESITO));
					lAnnEsitoModel.setDataTrasmissione(getRequestDateParameter(CAMPO_ANNO_DATA_ESITO, CAMPO_MESE_DATA_ESITO,
							CAMPO_GIORNO_DATA_ESITO));									
			}		
		}		

		lAnnEsitoModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnEsitoModel.setDataInserimento(DateUtils.getSysDate());
		lAnnEsitoModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ==========================================================================
		// siesLogger.debug("Evento: "+lEveNotModel.getEvento());
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
				lOECtrl.ExCancellaEventoConStoreProcedure(lEveModel);
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		siesLogger.debug("Inizio Inserimento");
		IIstruttoriaCumulo lCtrlIstrCum = SIEPLookupRemote.getIstruttoriaCumuloRemote();
		BigDecimal lIdEventoInserito = lCtrlIstrCum.ExInserisciAnnotazioneEsitoTrasmComp(lEveNotModel,
				lAnnEsitoModel, lIdMess);
		siesLogger.debug("idEvento Inserito = " + lEveNotModel.getEvento().getIdEvento());

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.presaincarico.action.ActDettAnnotaEsitoTrasmComp&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEventoInserito;

		return lPage;
	}

}