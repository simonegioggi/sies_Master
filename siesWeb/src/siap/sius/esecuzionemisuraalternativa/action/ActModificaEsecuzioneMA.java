package siap.sius.esecuzionemisuraalternativa.action;

/**
 * <p>Title: ActModificaEsecuzioneMA</p>
 * <p>Description: Classe Azione di modifica dell' Esecuzione Misura Alternativa
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull</p>
 */
import java.math.BigDecimal;
//import java.util.StringTokenizer;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaEsecuzioneMA extends ActionSiap implements ICostantiEsecuzioneMA {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Bottone di ritorno
		this.gestioneRitorno();

		// Recupero l'utente e il Fascicolo SIUS dalla sessione
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

		// Istanzio il Model e lo carico con quello posto in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Istanzio il Model di EsecuzioneMisuraAlternativa.
		EsecuzioneMisuraAlternativaModel lEMAMod = new EsecuzioneMisuraAlternativaModel();

		// Caricamento Esecuzione Misura Alternativa (solo dati modificati)
		lEMAMod.setIdEsecuzioneMisuraAlternati(getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_MA));
		lEMAMod.setDataInizioMisura(getRequestDateParameter(CAMPO_ANNO_INIZIO_MISURA,
				CAMPO_MESE_INIZIO_MISURA, CAMPO_GIORNO_INIZIO_MISURA));
		lEMAMod.setDataTermineIniziale(getRequestDateParameter(CAMPO_ANNO_TERMINE_INIZIALE,
				CAMPO_MESE_TERMINE_INIZIALE, CAMPO_GIORNO_TERMINE_INIZIALE));
		lEMAMod.setDataTermineAttuale(getRequestDateParameter(CAMPO_ANNO_TERMINE_ATTUALE,
				CAMPO_MESE_TERMINE_ATTUALE, CAMPO_GIORNO_TERMINE_ATTUALE));
		lEMAMod.setLuogoEsecuzioneMisura(getRequestStringParameter(CAMPO_LUOGO_ESECUZIONE_MISURA));

		lEMAMod.setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice dell'operatore che modifica
		lEMAMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
		lEMAMod.setDataAggiornamento(DateUtils.getSysDate());

		IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
		// Aggiornamento Esecuzione Misura Alternativa.
		lEMAMod = lEMACtrl.ExModificaEsecuzioneMisuraAlternativa(lEMAMod);

		// Rilettura Esecuzione Misura Alternativa.
		lEMAMod = lEMACtrl
				.ExRicercaEsecuzioneMisuraAlternativaByKey(lEMAMod.getIdEsecuzioneMisuraAlternati());

		if (lEMAMod == null || lEMAMod.getIdEsecuzioneMisuraAlternati().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA ALTERNATIVA Assente!");
		else
			setRequestAttribute("misuraAlternativa", lEMAMod);

		Vector lVect = lEMACtrl.ExRicercaDettaglioEsecuzioneMA(
				getRequestBigDecimalParameter(CAMPO_ID_ESECUZIONE_MA),
				lFasGPMod.getFascicoloSiusModel().getSogIdSoggetto(), this.getCodUfficioUtenteConnesso());

		if (!lEMAMod.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl
					.ExRicercaFascicoloByGenProc(lEMAMod.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		setRequestAttribute("misure", lVect);

		// Leggo anche il fascicolo SIEP.
		BigDecimal lIdFascicoloSiep = null;
		if (!((getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)) == null
				|| (getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.compareTo("null") == 0
				|| (getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)).trim()
						.length() == 0)) {
			lIdFascicoloSiep = new BigDecimal(
					(getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)));
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MA;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}