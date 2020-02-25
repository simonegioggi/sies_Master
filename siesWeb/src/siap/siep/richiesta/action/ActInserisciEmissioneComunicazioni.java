package siap.siep.richiesta.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.richiesta.controller.IRichiesta;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciEmissioneComunicazioni
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della Comunicazione della Richiesta o Concessione (decisioni
 * del GE) di:
 * </p>
 * - applicazione Amnistia/Indulto<br>
 * - applicazione Depenalizzazione/Incostituzionalità<br>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciEmissioneComunicazioni extends ActionSiap implements ICostantiRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lPage = null;
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		/*
		 * // Recupero l'id dell'evento del Provvedimento o Richiesta e quindi l'evento BigDecimal lIdEvento =
		 * getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO); EventoModel lEve = new
		 * EventoModel(); IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote(); lEve =
		 * lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
		 */
		BigDecimal lIdAnnotazioneManuale = this
				.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE);

		String codice = getRequestStringParameter("codice");

		// ==========================================================================
		// INSERISCO EVENTO (comunicazione) E relative NOTIFICHE
		// ==========================================================================
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		EventoModel lEveModel = new EventoModel();
		lEveModel.setCodTipoEvento("01");
		lEveModel.setCodTipoProvvedimento("12");
		lEveModel.setCodMotivo(codice);

		lEveModel.setCodUfficioEmittente(lCodiceUfficio);
		lEveModel.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		lEveModel.setDataTrasmissioneAtti(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setAnnIdAnnotazioneManuale(lIdAnnotazioneManuale);

		lEveModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(lCodiceUfficio);

		lEveModel.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveModel.setCodTipoUfficioDestinatario("-");
		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodEsito("-");
		lEveModel.setFlagStampaSiep("N");
		lEveModel.setFlagVideoSiep("S");

		// Lega la comunicazione al provvedimento
		// SOLO SE richiamato da Decisioni del GE
		// ATTENZIONE! in questo caso il flag 'partenza' è uguale a *AN*
		if (!isRequestParameterNullObj("partenza") && "AN".equals(getRequestStringParameter("partenza"))
				&& !isRequestParameterNullObj(ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO)) {
			lEveModel
					.setEveIdEvento(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO));
		}

		lEveMod.setEvento(lEveModel);

		// setto le notifiche

		ArrayList lNotifiche = new ArrayList();

		// String TipoUff = getRequestStringParameter(ICostantiRichiesta.AUTORITA_DESTINATARIO);

		String lSedeTDS = getRequestStringParameter(ICostantiRichiesta.AUTORITA_SEDE);
		if (lSedeTDS != null && !"-".equals(lSedeTDS) && !"".equals(lSedeTDS)) {
			NotificaModel lNotMod = new NotificaModel();
			// lNotMod.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_TDS));
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(DateUtils.getSysDate());
			lNotMod.setCodTipoNotifica("C");

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiRichiesta.AUTORITA_DESTINATARIO),
					getRequestStringParameter(ICostantiRichiesta.AUTORITA_SEDE));
			lNotMod.setUffCodUfficio(lCodiceUff);
			lNotMod.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_NOTE_TDS));

			lNotifiche.add(lNotMod);
		}

		// GDV 27/11/2006 - Aggiunti due destinatari
		String lAutoritaDestionazione1 = getRequestStringParameter("Autorita1");
		if (lAutoritaDestionazione1 != null && !lAutoritaDestionazione1.equals("-")) {
			// Notifica
			NotificaModel lNotMod1 = new NotificaModel();
			lNotMod1.setCodEsito("-");
			lNotMod1.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod1.setDataInserimento(DateUtils.getSysDate());
			lNotMod1.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod1.setCodTipoNotifica("C");
			lNotMod1.setDataInvio(DateUtils.getSysDate());
			lNotMod1.setNote(getRequestStringParameter("Note1"));

			// Aggiungere Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lAutoritaDestionazione1);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter("Sede1")));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			lNotMod1.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotMod1);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("Ho inserito la prima notifica");
		}

		String lAutoritaDestionazione2 = getRequestStringParameter("Autorita2");
		if (lAutoritaDestionazione2 != null && !lAutoritaDestionazione2.equals("-")) {
			// notifica al destinatario numero 2
			NotificaModel lNotMod2 = new NotificaModel();
			// MERGE v10: commentato codice
//			lNotMod2.setNote(getRequestStringParameter(ICostantiRichiesta.CAMPO_TDS));
			lNotMod2.setCodEsito("-");
			lNotMod2.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod2.setDataInserimento(DateUtils.getSysDate());
			lNotMod2.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod2.setCodTipoNotifica("C");
			lNotMod2.setDataInvio(DateUtils.getSysDate());
			lNotMod2.setNote(getRequestStringParameter("Note2"));

			// Aggiungere Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lAutoritaDestionazione2);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter("Sede2")));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			lNotMod2.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotMod2);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("Ho inserito la seconda notifica");
		}

		lEveMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ==========================================================================
		// Effettuo l'inserimento della 'Comunicazione'
		// ==========================================================================
		IRichiesta lCtrlRich = SIEPLookupRemote.getRichiestaRemote();
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		lRetModel = lCtrlRich.ExInserisciOModificaNotifica(lEveMod);

		// ==========================================================================
		// Invoco la Action per il caricamento del dettaglio della Comunicazine.
		// Le action di dettagli sono differenti in funzione del tipo di comunicazione
		// Nel caso di Decisioni del GE (0301-Indulto/Amnistia, 0300-Depenalizzazione/Incostituzionalità):
		// ActDettaglioEmissioneComunicazioniAN
		// Nel caso di Richieste al GE (0298-Indulto/Amnistia, 0299-Depenalizzazione/Incostituzionalità):
		// ActDettaglioEmissioneComunicazioni
		// ==========================================================================
		if (codice.equals("0301") || codice.equals("0300")) { // Decisioni del GE
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioniAN&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		} else { // Richieste al GE
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioni&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
		}

		return lPage;
	}

}