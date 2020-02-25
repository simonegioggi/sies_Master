package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action per l'inserimento del sollecito esito trasmissione misure di sicurezza per iscrizione in classe IV
 * 
 * @author d.fiorletta
 * @since
 */
public class ActInserisciSollecitoEsitoTrasmissione extends ActionSiap implements ICostantiMisuraSicurezza,
		ICostantiSollecitoEsitoTrasmissione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Caricamento Evento (01-12-xxxx) x ora una comunicazione
		// ==========================================================================
		EventoNotificaModel lEveNotModel = new EventoNotificaModel();

		lEveNotModel.getEvento().setCodTipoEvento("01");
		lEveNotModel.getEvento().setCodTipoProvvedimento("12");
		lEveNotModel.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		lEveNotModel.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveNotModel.getEvento().setDataEmissione(lDataEmissione);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEveNotModel.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		String lCodiceUffComp = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO),
				getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO));
		lEveNotModel.getEvento().setCodUfficioDestinatario(lCodiceUffComp);

		lEveNotModel.getEvento().setCodMagistrato(
				getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

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
		// Dati del Sollecito
		// ==========================================================================
		SollecitoEsitoTrasmissioneModel lSolMod = new SollecitoEsitoTrasmissioneModel();

		lSolMod.setCodUffSollecitato(lEveNotModel.getEvento().getCodUfficioDestinatario());
		lSolMod.setOggettoMsSollecito(ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA_MS);
		lSolMod.setOggettoMsSollecitato(getRequestStringParameter(ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE));
		lSolMod.setMesIdMessaggioSollecitato(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_SOLLECITATO));

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRichiesta = lCrtl.ExRicercaMessaggioByKey(lSolMod
				.getMesIdMessaggioSollecitato());

		lSolMod.setDataInvioMsSollecitato(lMessaggioRichiesta.getDataInvio());

		// Recupero gli eventuali dati dell'ufficio che ha inoltrato il messaggio di Trasmissione
		if (!isRequestParameterNullObj(CAMPO_COD_UFF_INOLTRANTE)
				&& getRequestStringParameter(CAMPO_COD_UFF_INOLTRANTE).length() > 0)
			lSolMod.setCodUffInoltrante(getRequestStringParameter(CAMPO_COD_UFF_INOLTRANTE));
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INOLTRO)
				&& getRequestStringParameter(CAMPO_ANNO_DATA_INOLTRO).length() > 0)
			lSolMod.setDataInoltro(getRequestDateParameter(CAMPO_ANNO_DATA_INOLTRO, CAMPO_MESE_DATA_INOLTRO,
					CAMPO_GIORNO_DATA_INOLTRO));
		if (!isRequestParameterNullObj(CAMPO_MES_ID_MESSAGGIO_INOLTRO)
				&& getRequestStringParameter(CAMPO_MES_ID_MESSAGGIO_INOLTRO).length() > 0)
			lSolMod.setMesIdMessaggioInoltro(getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_INOLTRO));

		//
		lSolMod.setEveIdEvento(null);
		lSolMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		//
		lSolMod.setCodOperatoreInserimento(lEveNotModel.getEvento().getCodOperatoreInserimento());
		lSolMod.setDataInserimento(lEveNotModel.getEvento().getDataInserimento());
		lSolMod.setCodUfficioInserimento(lEveNotModel.getEvento().getCodUfficioInserimento());

		// =======================================
		// Notifiche
		// =======================================
		ArrayList lNotifiche = new ArrayList();

		// ==========================================================================
		// Ufficio Sollecitato per l'esecuzione
		// ==========================================================================
		NotificaModel lNotUffComp = new NotificaModel();

		lNotUffComp.setCodTipoNotifica("E"); // Per l'esecuzione
		lNotUffComp.setDataInvio(lDataTrasmissione);
		lNotUffComp.setCodEsito("-");
		lNotUffComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNotUffComp.setDataInserimento(DateUtils.getSysDate());
		lNotUffComp.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lNotUffComp.setUffCodUfficio(lCodiceUffComp); // Cod Uff Destinatario

		lNotifiche.add(lNotUffComp);

		// ALTRO DESTINATARIO
		if (!isRequestParameterNullObj("AltroDestinatario")
				&& !getRequestStringParameter("AltroDestinatario").equals("-")) {
			NotificaModel lNotAltroDestinatario = new NotificaModel();

			lNotAltroDestinatario.setCodTipoNotifica("C");
			lNotAltroDestinatario.setDataInvio(lDataTrasmissione);
			lNotAltroDestinatario.setCodEsito("-");
			lNotAltroDestinatario.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotAltroDestinatario.setDataInserimento(DateUtils.getSysDate());
			lNotAltroDestinatario.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(getRequestStringParameter("AltroDestinatario"));

			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter("SedeAltroDestinatario")));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNotAltroDestinatario.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotAltroDestinatario);
		}

		// Inserisco l'array di Notifiche nell'Evento
		lEveNotModel.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));
		// ==========================================================================
		// Eventuale nota
		// ==========================================================================
		if (!this.isRequestParameterNullObj("camponote") && getRequestStringParameter("camponote") != null
				&& !getRequestStringParameter("camponote").equals("")) {
			ArrayList lCampoNote = new ArrayList();

			CampoNotaModel lCampMod = new CampoNotaModel();

			lCampMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampMod.setDataInserimento(DateUtils.getSysDate());
			lCampMod.setDescr(getRequestStringParameter("camponote"));

			lCampoNote.add(lCampMod);

			lEveNotModel.setCampoNote((CampoNotaModel[]) lCampoNote.toArray(new CampoNotaModel[0]));
		}

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
		BigDecimal lIdEventoInserito = lCtrlMis.ExInserisciSollecitoEsito(lEveNotModel, lSolMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("idEvento Inserito = " + lIdEventoInserito);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioSollecitoEsitoTrasmissione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEventoInserito;

		return lPage;
	}

}