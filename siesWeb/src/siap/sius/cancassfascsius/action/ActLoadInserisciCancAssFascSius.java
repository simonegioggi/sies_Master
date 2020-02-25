package siap.sius.cancassfascsius.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sius.cancassfascsius.controller.ICancAssFascSius;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciCancAssFascSius
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della form di Inserimento per CancAssFascSius
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciCancAssFascSius extends ActionSiap implements ICostantiCancAssFascSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// jsp per l'inserimento
		String lretPage = PG_LOAD_INSERISCICANCASSFASCSIUS;

		// Si ricava il Fascicolo SIUS dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo non in sessione");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		// Valutazione della Data di Inizio minima consentita nell'inserimento
		Date lData_minima = lFasGPMod.getFascicoloSiusModel().getDataIscrizione();

		// Si controlla se il fascicolo è già assegnato ad una Cancelleria Assegnataria.
		ICancAssFascSius lCancAssFascCtrl = SIUSLookupRemote.getCancAssFascSiusRemote();
		CancAssFascSiusModel lCancAssFascAttiva = lCancAssFascCtrl
				.ExRicercaCancAssFascSiusAttiva(lIdFascicolo);
		// Se già è stata assegnata una Cancelleria la nuova non potrà avere Data di Inizio precedente a
		// quella già inserita
		if (lCancAssFascAttiva != null)
			lData_minima = lCancAssFascAttiva.getDataInizio();

		// Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'uttente
		CancelleriaAssegnatariaModel lCancAssModel = new CancelleriaAssegnatariaModel();
		lCancAssModel.setCodUfficio(getCodUfficioUtenteConnesso());

		// Ricerca
		ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		Vector lElencoCancellerie = lCancAssCtrl.ExRicercaCancelleriaAssegnataria(lCancAssModel);
		if (lElencoCancellerie == null || lElencoCancellerie.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Non esistono Cancellerie Assegnatarie per l'ufficio !");
		}

		// Lock per evitare inserimento contemporaneo per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "CancelleriaAssegnataria",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new F3BException(F3BException.USER_MESSAGE, "La definizione della  " + lck.getEntity()
					+ " per il Procedimento è in gestione ad un altro utente!");
		}

		// passaggio dei dati alla form
		setRequestAttribute("cancellerie", lElencoCancellerie);
		setRequestAttribute("cod_ufficio", lCancAssModel.getCodUfficio());
		setRequestAttribute("data_minima", lData_minima);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		return lretPage; // restituisce la jsp di VIEW
	}

}