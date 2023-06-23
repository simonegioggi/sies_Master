package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.rateizzazionepp.action.ICostantiRateizzazionePP;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13: aggiunta classe per l'inserimento dell'ordine di ingiunzione
 *
 * @author sgioggi
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

    	// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		EventoModel lEve = getEventoOrdineIngiunzione();
		// Recupero le notifiche
		NotificaModel[] lNotifiche = this.getNotificheOrdineIngiunzione();

		lEveNot.setEvento(lEve);
		lEveNot.setNotifiche(lNotifiche);

		// recupero le rateizzazioni da collegare all'evento
		String[] lArrayIdRate = this.getRequestStringParameters(ICostantiRateizzazionePP.CAMPO_EVE_ID_EVENTO);

		ISanzioneSostitutiva lCtrlSS = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		/* EventoNotificaModel lRetModel = */lCtrlSS.exInserisciOrdineIngiunzione(lEveNot, lArrayIdRate);

		String lPage = null;

		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sanzionesostitutiva.action.ActLoadDettaglioOrdineIngiunzione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNot.getEvento().getIdEvento();

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return lPage;
	}

	protected EventoModel getEventoOrdineIngiunzione() throws F3BException {

		// info per il log
		siesLogger.info("getEventoOrdineIngiunzione(): inizio");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		EventoModel lEve = new EventoModel();

		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEve.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		lEve.setCodTipoProvvedimento("06"); // Tipo Provvedimento = ORDINE ESECUZIONE
		lEve.setCodMotivo("0622"); // Motivo Evento = Ordine di ingiunzione

		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);

		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		// Magistrato
		lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setCodEsito("-");
		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");

		lEve.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
		// lEve.setCodUfficioAggiornamento(lCodiceUfficio);
		// lEve.setDataAggiornamento(DateUtils.getSysDate());

		// info per il log
		siesLogger.info("getEventoOrdineIngiunzione(): fine");

		return lEve;
	}

	/**
	 * Imposta le notifiche per l'ordine di ingiunzione
	 *
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] getNotificheOrdineIngiunzione() throws F3BException {

		// info per il log
		siesLogger.info("getNotificheOrdineIngiunzione(): inizio");

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lTrasmissione = lDataEmissione;

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		ArrayList lNotificheArray = new ArrayList();

		String[] lArrayDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lArrayNote = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		int lIndNotifiche = 0;
		int lNumAvvNotifiche = lAvvocati.length;
		if (lArrayDestinatari[0].compareTo("-") == 0)
			lNumAvvNotifiche -= 1;

		// ===================================================
		// Notifica per l'esecuzione
		// ===================================================
		{
			String lSedeDestinatario_E = null;
			String lDestinatario_E = null;
			String lDestinatario_EAE = null;
			String lNote_E = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
				lDestinatario_EAE = this
						.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
				lSedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			}

			if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				lDestinatario_E = this
						.getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				lDestinatario_E = this.getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
				lNote_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("E");
			lNotMod.setDataInvio(lTrasmissione);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setNote(lNote_E);

			// Prima notifica esecuzione
			if (lDestinatario_E != null)
				lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);

			if (lDestinatario_EAE != null) {
				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita(lDestinatario_EAE);
				ComuneModel lComModel = new ComuneModel(getCodComuneByDescrFlagVal(lSedeDestinatario_E));
				lAutMod.setCodSede(lComModel.getCodComune());
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());
				lNotMod.setIstDetIdIstitutoDetenzione("");

				lNotMod.setAutoritaEsterna(lAutMod);
			}

			lNotificheArray.add(lNotMod);
		}

		// ===================================================
		// Notifica agli avvocati del Condannato
		// ===================================================
		// Notifiche all'avvocato
		while (lIndNotifiche < lNumAvvNotifiche) {
			NotificaModel lNot = new NotificaModel();
			lNot.setCodTipoNotifica("N");
			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndNotifiche]));
			lNot.setNote(lArrayNote[lIndNotifiche]);
			lNot.setDataInvio(lTrasmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lArrayDestinatari[lIndNotifiche]);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lArraySedeDestinatari[lIndNotifiche]));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNot.setAutoritaEsterna(lAut);
			lIndNotifiche++;
			lNotificheArray.add(lNot);
		}

		// ===================================================
		// Notifiche ai Civilmente Obbligati
		// ===================================================
		// Ricerco il civilmente Obbligato se esiste
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> lListaObbligati = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		siesLogger.debug("Carico le Notifiche ai Civilmente Obbligati N. : " + lListaObbligati.size());
		Iterator<CivilmenteObbligatoModel> itx = lListaObbligati.iterator();
		while (itx.hasNext()) {
			CivilmenteObbligatoModel obbligatoModel = itx.next();
			siesLogger.debug("Civilmente Obbligato id = " + obbligatoModel.getIdCivilmenteObbligato());
			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_CO_"
					+ obbligatoModel.getIdCivilmenteObbligato())) {
				String lTipoAutorita_E_CO = this
						.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_CO_"
								+ obbligatoModel.getIdCivilmenteObbligato());
				String lSedeDestinatario_E_CO = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E + "_CO_"
								+ obbligatoModel.getIdCivilmenteObbligato());
				String lNote_E_CO = getRequestStringParameter(
						ICostantiNotifica.CAMPO_NOTE_E + "_CO_" + obbligatoModel.getIdCivilmenteObbligato());

				if (!"-".equals(lTipoAutorita_E_CO)) {
					NotificaModel lNotModCO = new NotificaModel();

					lNotModCO.setIdCivilmenteObbligato(obbligatoModel.getIdCivilmenteObbligato());
					lNotModCO.setCodTipoNotifica("N");
					lNotModCO.setNote(lNote_E_CO);
					lNotModCO.setDataInvio(lTrasmissione);

					lNotModCO.setCodEsito("-");
					lNotModCO.setIstDetIdIstitutoDetenzione("");
					lNotModCO.setCodOperatoreInserimento(lCodiceOperatore);
					lNotModCO.setDataInserimento(DateUtils.getSysDate());
					lNotModCO.setCodUfficioInserimento(lCodiceUfficio);

					AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

					lAutMod.setCodTipoAutorita(lTipoAutorita_E_CO);
					ComuneModel lComModel = new ComuneModel(
							getCodComuneByDescrFlagVal(lSedeDestinatario_E_CO));
					lAutMod.setCodSede(lComModel.getCodComune());
					lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
					lAutMod.setCodUfficioInserimento(lCodiceUfficio);
					lAutMod.setDataInserimento(DateUtils.getSysDate());

					lNotModCO.setAutoritaEsterna(lAutMod);

					lNotificheArray.add(lNotModCO);
				}
			}
		}

		// info per il log
		siesLogger.info("getNotificheOrdineIngiunzione(): fine");

		// valore di ritorno
		return (NotificaModel[]) lNotificheArray.toArray(new NotificaModel[0]);
	}

}