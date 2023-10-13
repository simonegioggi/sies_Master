package siap.siep.rateizzazionepp.action;

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
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per l'inserimento dell'Avviso Mancato Pagamento
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciAvvisoMancatoPagamento extends ActionSiap implements ICostantiRateizzazionePP {

	private static Logger siesLogger = Logger.getLogger(LogF3B.WS_PAGO_PA_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicoloSiep = fsm.getIdFascicoloSiep();

		EventoNotificaModel enm = new EventoNotificaModel();
		EventoModel em = getEventoAvvisoMancatoPagamento(idFascicoloSiep);
		// Recupero le notifiche
		NotificaModel[] nmArray = getNotificheAvvisoMancatoPagamento();

		enm.setEvento(em);
		enm.setNotifiche(nmArray);

		// modello la rata unica
		RateizzazionePPModel rppm = new RateizzazionePPModel();
		rppm.setCodOperatoreInserimento(getCodUtenteConnesso());
		rppm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		rppm.setDataInserimento(DateUtils.getSysDate());
		rppm.setFasSieIdFascicoloSiep(idFascicoloSiep);
		rppm.setImportoDaPagare(new BigDecimal(getRequestStringParameter("importoDaPagare")));
		rppm.setImportoRata(new BigDecimal(getRequestStringParameter("importoDaPagare")));
		rppm.setNumeroRate(new BigDecimal(1));
		rppm.setProgressivoRata(new BigDecimal(1));
		rppm.setScadenzaGiorni(new BigDecimal(60));
		rppm.setTipoRateizzazione("U");
		IRateizzazionePP irpp = SIEPLookupRemote.getRateizzazionePPRemote();
		BigDecimal idEvento = irpp.exInserisciAvvisoMancatoPagamento(enm, rppm);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActDettaglioAvvisoMancatoPagamento&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + idEvento;
	}

	private EventoModel getEventoAvvisoMancatoPagamento(BigDecimal idFascicoloSiep) throws F3BException {

		// info per il log
		siesLogger.info("getEventoAvvisoMancatoPagamento(): inizio");

		EventoModel em = new EventoModel();

		em.setFasSieIdFascicoloSiep(idFascicoloSiep);

		em.setCodTipoEvento("01"); // Tipo Evento = PROVVEDIMENTO
		em.setCodTipoProvvedimento("04"); // Tipo Provvedimento = PROVVEDIMENTO
		em.setCodMotivo("1308"); // Motivo Evento = 'Avviso Mancato Pagamento Pena Pecuniaria'

		em.setFlagStampaSiep("S");
		em.setFlagVideoSiep("S");

		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		em.setDataEmissione(dataEmissione);

		em.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		em.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		// Magistrato
		em.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		em.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		em.setCodEsito("-");
		em.setCodLuogoDestinatario("-");
		em.setCodTipoUfficioDestinatario("-");

		em.setCodOperatoreInserimento(getCodUtenteConnesso());
		em.setDataInserimento(DateUtils.getSysDate());
		em.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// info per il log
		siesLogger.info("getEventoAvvisoMancatoPagamento(): fine");

		return em;
	}

	/**
	 * Imposta le notifiche per l'Avviso Mancato Pagamento
	 *
	 * @return
	 * @throws F3BException
	 */
	private NotificaModel[] getNotificheAvvisoMancatoPagamento() throws F3BException {

		// info per il log
		siesLogger.info("getNotificheAvvisoMancatoPagamento(): inizio");

		String codiceOperatore = getCodUtenteConnesso();
		String codiceUfficio = getCodUfficioUtenteConnesso();
		Date dataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date dataTrasmissione = dataEmissione;

		if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			dataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		ArrayList notificheArray = new ArrayList();

		String[] destinatariArray = getRequestStringParameters(
				ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] sedeDestinatariArray = getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] noteArray = getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] avvocatiArray = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		int notificheIndex = 0;
		int numAvvNotifiche = avvocatiArray.length;
		if (destinatariArray[0].compareTo("-") == 0)
			numAvvNotifiche -= 1;

		// ===================================================
		// Notifica per l'esecuzione
		// ===================================================
		{
			String sedeDestinatario_E = null;
			String destinatario_E = null;
			String destinatario_EAE = null;
			String note_E = null;

			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
				destinatario_EAE = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
				sedeDestinatario_E = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			}

			if (!isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_E = getRequestStringParameter(
						ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
				destinatario_E = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E))
				note_E = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("E");
			nm.setDataInvio(dataTrasmissione);
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(codiceOperatore);
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(codiceUfficio);
			nm.setNote(note_E);

			// Prima notifica esecuzione
			if (destinatario_E != null)
				nm.setIstDetIdIstitutoDetenzione(destinatario_E);

			if (destinatario_EAE != null) {
				AutoritaEsternaModel aem = new AutoritaEsternaModel();

				aem.setCodTipoAutorita(destinatario_EAE);
				ComuneModel cm = new ComuneModel(getCodComuneByDescrFlagVal(sedeDestinatario_E));
				aem.setCodSede(cm.getCodComune());
				aem.setCodOperatoreInserimento(codiceOperatore);
				aem.setCodUfficioInserimento(codiceUfficio);
				aem.setDataInserimento(DateUtils.getSysDate());
				nm.setIstDetIdIstitutoDetenzione("");

				nm.setAutoritaEsterna(aem);
			}

			notificheArray.add(nm);
		}

		// ===================================================
		// Notifica agli avvocati del Condannato
		// ===================================================
		// Notifiche all'avvocato
		while (notificheIndex < numAvvNotifiche) {
			NotificaModel nm = new NotificaModel();
			nm.setCodTipoNotifica("N");
			nm.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(avvocatiArray[notificheIndex]));
			nm.setNote(noteArray[notificheIndex]);
			nm.setDataInvio(dataTrasmissione);
			nm.setCodEsito("-");
			nm.setCodOperatoreInserimento(codiceOperatore);
			nm.setDataInserimento(DateUtils.getSysDate());
			nm.setCodUfficioInserimento(codiceUfficio);

			AutoritaEsternaModel aem = new AutoritaEsternaModel();
			aem.setCodTipoAutorita(destinatariArray[notificheIndex]);

			ComuneModel cm = new ComuneModel(getCodComuneByDescr(sedeDestinatariArray[notificheIndex]));

			aem.setCodSede(cm.getCodComune());
			aem.setCodOperatoreInserimento(codiceOperatore);
			aem.setCodUfficioInserimento(codiceUfficio);
			aem.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			nm.setAutoritaEsterna(aem);
			notificheIndex++;
			notificheArray.add(nm);
		}

		// ===================================================
		// Notifiche ai Civilmente Obbligati
		// ===================================================
		// Ricerco il civilmente Obbligato se esiste
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		Vector<CivilmenteObbligatoModel> comVector = ico
				.ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());

		siesLogger.debug("Carico le Notifiche ai Civilmente Obbligati N. : " + comVector.size());
		Iterator<CivilmenteObbligatoModel> itx = comVector.iterator();
		while (itx.hasNext()) {
			CivilmenteObbligatoModel com = itx.next();
			siesLogger.debug("Civilmente Obbligato id = " + com.getIdCivilmenteObbligato());
			if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_CO_"
					+ com.getIdCivilmenteObbligato())) {
				String tipoAutorita_E_CO = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E + "_CO_"
								+ com.getIdCivilmenteObbligato());
				String sedeDestinatario_E_CO = getRequestStringParameter(
						ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E + "_CO_" + com.getIdCivilmenteObbligato());
				String note_E_CO = getRequestStringParameter(
						ICostantiNotifica.CAMPO_NOTE_E + "_CO_" + com.getIdCivilmenteObbligato());

				if (!"-".equals(tipoAutorita_E_CO)) {
					NotificaModel nm = new NotificaModel();
					nm.setIdCivilmenteObbligato(com.getIdCivilmenteObbligato());
					nm.setCodTipoNotifica("N");
					nm.setNote(note_E_CO);
					nm.setDataInvio(dataTrasmissione);
					nm.setCodEsito("-");
					nm.setIstDetIdIstitutoDetenzione("");
					nm.setCodOperatoreInserimento(codiceOperatore);
					nm.setDataInserimento(DateUtils.getSysDate());
					nm.setCodUfficioInserimento(codiceUfficio);

					AutoritaEsternaModel aem = new AutoritaEsternaModel();

					aem.setCodTipoAutorita(tipoAutorita_E_CO);
					ComuneModel cm = new ComuneModel(getCodComuneByDescrFlagVal(sedeDestinatario_E_CO));
					aem.setCodSede(cm.getCodComune());
					aem.setCodOperatoreInserimento(codiceOperatore);
					aem.setCodUfficioInserimento(codiceUfficio);
					aem.setDataInserimento(DateUtils.getSysDate());

					nm.setAutoritaEsterna(aem);

					notificheArray.add(nm);
				}
			}
		}

		// info per il log
		siesLogger.info("getNotificheAvvisoMancatoPagamento(): fine");

		// valore di ritorno
		return (NotificaModel[]) notificheArray.toArray(new NotificaModel[0]);
	}

}