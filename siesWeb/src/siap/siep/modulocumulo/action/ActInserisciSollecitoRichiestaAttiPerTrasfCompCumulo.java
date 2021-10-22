package siap.siep.modulocumulo.action;

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
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action per l'inserimento del sollecito trasmissione Atti per Trasfewrimento competenza Cumulo
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciSollecitoRichiestaAttiPerTrasfCompCumulo extends ActionModuloCumulo
		implements ICostantiSollecitoEsitoTrasmissione, ICostantiMessaggio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" --XXX-- FascicoloModel = "+lFascicoloModel);

		// ==========================================================================
		// Caricamento Evento (01-12-xxxx) x ora una comunicazione
		// ==========================================================================
		EventoNotificaModel lEveNotModel = new EventoNotificaModel();

		lEveNotModel.getEvento().setCodTipoEvento("01"); // Provvedimento
		lEveNotModel.getEvento().setCodTipoProvvedimento("12"); // Comunicazione
		// 5203 Sollecito Richiesta Atti per comp. per Cumulo
		lEveNotModel.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));

		lEveNotModel.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveNotModel.getEvento().setDataEmissione(lDataEmissione);

		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		lEveNotModel.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// String lCodiceUffComp =
		// getCodUfficioByCodTipoUfficioDescrComune(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO),
		// getRequestStringParameter("DescrSedeUfficio"));

		String lCodiceUffComp = getRequestStringParameter(ICostantiEvento.CAMPO_COD_UFFICIO_DESTINATARIO);

		lEveNotModel.getEvento().setCodUfficioDestinatario(lCodiceUffComp);

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

		lEveNotModel.getEvento().setIstruidIstruttoriaCumulo(new BigDecimal(
				getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)));

		// ==========================================================================
		// Dati del Sollecito
		// ==========================================================================
		SollecitoEsitoTrasmissioneModel lSolMod = new SollecitoEsitoTrasmissioneModel();

		lSolMod.setCodUffSollecitato(lEveNotModel.getEvento().getCodUfficioDestinatario());
		lSolMod.setOggettoMsSollecito(ICostantiJMS.SOLLECITO_RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);
		lSolMod.setOggettoMsSollecitato(ICostantiJMS.RICHIESTA_TRASMISSIONE_ATTI_PER_COMP);
		lSolMod.setMesIdMessaggioSollecitato(
				getRequestBigDecimalParameter(CAMPO_MES_ID_MESSAGGIO_SOLLECITATO));

		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggioRichiesta = lCrtl
				.ExRicercaMessaggioByKey(lSolMod.getMesIdMessaggioSollecitato());

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

		lNotUffComp.setCodTipoNotifica("C"); // Comunicazione
		lNotUffComp.setDataInvio(lDataTrasmissione);
		lNotUffComp.setCodEsito("-");
		lNotUffComp.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNotUffComp.setDataInserimento(DateUtils.getSysDate());
		lNotUffComp.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lNotUffComp.setUffCodUfficio(lCodiceUffComp); // Cod Uff Destinatario

		lNotifiche.add(lNotUffComp);

		// ALTRO DESTINATARIO (Altra Autorità)
		if (!isRequestParameterNullObj("AltroDestinatario")
				&& !getRequestStringParameter("AltroDestinatario").equals("-")) {
			NotificaModel lNotAltroDestinatario = new NotificaModel();

			lNotAltroDestinatario.setCodTipoNotifica("AA"); // ALTRA AUTORITA
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

		// COMPETENZA: Riiscrivo una nuova riga di COMPETENZA praticamente uguale a quella reltiva alla
		// richiesta di Trasmissione che si vuole sollecitare
		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel mCompModel = (CompetenzaModel) lCompCtrl
				.ExRicercaCompetenzaByIdMessaggioRichiesta(lMessaggioRichiesta.getIdMessaggio());

		CompetenzaModel CompetenzaSoll = new CompetenzaModel(mCompModel);
		CompetenzaSoll.setCodOperatoreInserimento(getCodUtenteConnesso());
		CompetenzaSoll.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		CompetenzaSoll.setDataInserimento(DateUtils.getSysDate());

		// Leghiamo l'Evento_Sollecito all'Evento_Richiesta (Tramite EVE_ID_EVENTO)
		// --
		lEveNotModel.getEvento().setEveIdEvento(mCompModel.getEveIdEvento());
		// --

		// ===============================================
		// Sono in modifica cancello l'evento e lo reinserisce
		// ===============================================
		if (!isRequestParameterNullObj("modalita")) {
			if ("M".equals(getRequestStringParameter("modalita"))) {

				/*
				 * // Cancella l'evento precedente BigDecimal lIdEvento =
				 * getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO); EventoModel lEveModel = new
				 * EventoModel();
				 * 
				 * IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote(); lEveModel =
				 * lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
				 * 
				 * IOrdineEsecuzione lOECtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
				 * lOECtrl.ExCancellaEventoConStoreProcedure(lEveModel);
				 */
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inizio Inserimento");
		IModuloCumulo lCtrlM = SIEPLookupRemote.getModuloCumuloRemote();

		BigDecimal lIdEventoInserito = lCtrlM.ExInserisciSollecitoRichiestaAtti(lEveNotModel, lSolMod,
				CompetenzaSoll);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("idEvento Inserito = " + lIdEventoInserito);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActDettaglioSollecitoRichiestaAttiTrasfCompCumulo&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEventoInserito;
		// + ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" +
		return lPage;
	}

}