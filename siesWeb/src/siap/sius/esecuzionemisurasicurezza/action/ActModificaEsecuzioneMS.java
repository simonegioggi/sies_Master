package siap.sius.esecuzionemisurasicurezza.action;

/**
 * <p>Title: ActModificaEsecuzioneMS</p>
 * <p>Description: Classe Azione di modifica dell' Esecuzione Misura Sicurezza
 * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 */
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaEsecuzioneMS extends ActionSiap implements ICostantiEsecuzioneMS {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Istanzio il Model di Esecuzione Misura Sicurezza.
		EsecuzioneMisuraSicurezzaModel lEMSMod = new EsecuzioneMisuraSicurezzaModel();

		// Caricamento Esecuzione Misura Sicurezza (solo dati modificati)
		lEMSMod.setIdEsecuzioneMisuraSicurezza(getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_MS));

		// Rilettura Esecuzione Misura Sicurezza.
		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		lEMSMod = lEMSCtrl.ExRicercaEsecuzioneMisuraSicurezzaByKey(lEMSMod.getIdEsecuzioneMisuraSicurezza());

		if (lEMSMod == null || lEMSMod.getIdEsecuzioneMisuraSicurezza().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");
		else
			setRequestAttribute("misuraSicurezza", lEMSMod);

		CalendarModel lCalModDurata = new CalendarModel();
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_TERMINE_ATTUALE))
			lEMSMod.setNumAnniMisura(getRequestBigDecimalParameter(CAMPO_ANNO_TERMINE_ATTUALE));
		lCalModDurata.setNumAnni(getRequestBigDecimalParameter(CAMPO_ANNO_TERMINE_ATTUALE));
		if (!this.isRequestParameterNullObj(CAMPO_MESE_TERMINE_ATTUALE))
			lEMSMod.setNumMesiMisura(getRequestBigDecimalParameter(CAMPO_MESE_TERMINE_ATTUALE));
		lCalModDurata.setNumMesi(getRequestBigDecimalParameter(CAMPO_MESE_TERMINE_ATTUALE));
		if (!this.isRequestParameterNullObj(CAMPO_GIORNO_TERMINE_ATTUALE))
			lEMSMod.setNumGiorniMisura(getRequestBigDecimalParameter(CAMPO_GIORNO_TERMINE_ATTUALE));
		lCalModDurata.setNumGiorni(getRequestBigDecimalParameter(CAMPO_GIORNO_TERMINE_ATTUALE));
		lEMSMod.setLuogoEsecuzioneMisura(getRequestStringParameter(CAMPO_LUOGO_ESECUZIONE_MISURA));

		// se ho modificato almeno un quantum ricalcolo la data fine
		if ((!this.isRequestParameterNullObj(CAMPO_ANNO_TERMINE_ATTUALE)
				|| !this.isRequestParameterNullObj(CAMPO_MESE_TERMINE_ATTUALE)
				|| !this.isRequestParameterNullObj(CAMPO_GIORNO_TERMINE_ATTUALE))
				&& lEMSMod.getDataInizioMisura() != null) {
			ICalcoloPena lCal = SIEPLookupRemote.getCalcoloPenaRemote();
			lEMSMod.setDataTermineIniziale(
					lCal.exCalcolaNuovaDataFine(lEMSMod.getDataInizioMisura(), lCalModDurata, false));
			lEMSMod.setDataTermineAttuale(
					lCal.exCalcolaNuovaDataFine(lEMSMod.getDataInizioMisura(), lCalModDurata, false));
		}

		// Modifica del 16/09/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Modifica Data Termine (Attuale)
		lEMSMod.setDataTermineAttuale(getRequestDateParameter(CAMPO_ANNO_DATA_TERMINE_MISURA,
				CAMPO_MESE_DATA_TERMINE_MISURA, CAMPO_GIORNO_DATA_TERMINE_MISURA));

		lEMSMod.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che modifica
		lEMSMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
		lEMSMod.setDataAggiornamento(DateUtils.getSysDate());

		// Aggiornamento Esecuzione Misura Sicurezza.
		lEMSMod = lEMSCtrl.ExModificaEsecuzioneMisuraSicurezza(lEMSMod);

		Vector lVect = lEMSCtrl.ExRicercaDettaglioEsecuzioneMS(
				getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_MS),
				lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(), this.getCodUfficioUtenteConnesso());
		if (!lEMSMod.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl
					.ExRicercaFascicoloByGenProc(lEMSMod.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		setRequestAttribute("misure", lVect);

		// Leggo anche il fascicolo SIEP.
		BigDecimal lIdFascicoloSiep = null;
		if (!((getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)) == null
				|| (getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.compareTo("null") == 0
				|| (getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.length() == 0)) {
			lIdFascicoloSiep = new BigDecimal(
					(getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)));
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		// Bottone di ritorno
		// Nella gestione del bottone di ritorno non si passa per la modifica
		// In questo caso per ritornare sul dettaglio si posiziona a mano LINK_RITORNO
		// tecnica non sicura.
		// this.gestioneRitorno();
		// if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
		setRequestAttribute(IWebConstants.LINK_RITORNO, "20");
		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}