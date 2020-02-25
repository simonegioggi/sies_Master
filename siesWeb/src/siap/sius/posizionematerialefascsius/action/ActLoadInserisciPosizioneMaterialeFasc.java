package siap.sius.posizionematerialefascsius.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneMaterialeFascSius
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

public class ActLoadInserisciPosizioneMaterialeFasc extends ActionSiap
		implements ICostantiPosizioneMaterialeFasc {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lretPage = PG_LOAD_INSERISCIPOSIZIONEMATERIALEFASC;

		// Si ricava il Fascicolo SIUS dalla sessione
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo non in sessione");
		BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		Date lData_minima = lFasGPMod.getFascicoloSiusModel().getDataIscrizione();

		// Si controlla se il fascicolo non abbià già una posizione materiale assegnata ed attiva
		IPosizioneMaterialeFascSius lCtrlPosMatFasSius = SIUSLookupRemote
				.getPosizioneMaterialeFascSiusRemote();
		Vector lVectPosMatFas = lCtrlPosMatFasSius.ExRicercaPosizioneMaterialeFascAttiva(lIdFascicolo);
		if (lVectPosMatFas.size() > 0)
			lData_minima = ((PosizioneMaterialeFascModel) lVectPosMatFas.get(0)).getDataInizio();

		// Lock per evitare inserimento contemporaneo per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "PosizioneMaterialeFascicoloSius",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"La definizione della  " + lck.getEntity() + " è in gestione ad un altro utente!");
		}

		// Riempie il model di ricerca per Ufficio dell'uttente
		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel();
		lPosMod.setCodUfficio(getCodUfficioUtenteConnesso());
		// Ricerca
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		Vector lVectPos = lCtrl.ExRicercaPosizioneMateriale(lPosMod);
		// passaggio alla form
		setRequestAttribute("posizioni", lVectPos);
		setRequestAttribute("cod_ufficio", lPosMod.getCodUfficio());
		setRequestAttribute("data_minima", lData_minima);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");

		// Imposta tipo fi Posizione Remota
		setRequestAttribute("tipo_posizione_materiale", "SIUS");

		lretPage = PG_LOAD_INSERISCIPOSIZIONEMATERIALEFASC;

		return lretPage; // restituisce la jsp di VIEW
	}

}