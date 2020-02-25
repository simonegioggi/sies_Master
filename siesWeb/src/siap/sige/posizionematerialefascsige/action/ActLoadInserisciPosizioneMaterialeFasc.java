package siap.sige.posizionematerialefascsige.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneMaterialeFascSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciPosizioneMaterialeFasc extends ActionSige
		implements ICostantiPosizioneMaterialeFasc {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		String lretPage = PG_LOAD_INSERISCIPOSIZIONEMATERIALEFASC;

		// Si ricava il Fascicolo SIGE dalla sessione
		FascicoloSigeEstesoModel lFascMod = (FascicoloSigeEstesoModel) getSessionAttribute(
				"FascicoloSigeEsteso");
		if (lFascMod == null || lFascMod.getFascicoloSige() == null
				|| lFascMod.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Fascicolo non in sessione");
		BigDecimal lIdFascicolo = lFascMod.getFascicoloSige().getIdFascicoloSige();
		Date lData_minima = lFascMod.getFascicoloSige().getDataIscrizione();

		// Si controlla se il fascicolo non abbia già una posizione materiale assegnata ed attiva
		IPosizioneMaterialeFascSige lCtrlPosMatFasSige = SIGELookupRemote
				.getPosizioneMaterialeFascSigeRemote();
		Vector lVectPosMatFas = lCtrlPosMatFasSige.ExRicercaPosizioneMaterialeFascAttiva(lIdFascicolo);
		if (lVectPosMatFas.size() > 0)
			lData_minima = ((PosizioneMaterialeFascModel) lVectPosMatFas.get(0)).getDataInizio();

		// Lock per evitare inserimento contemporaneo per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "PosizioneMaterialeFascicoloSige",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"La definizione della  " + lck.getEntity() + " è in gestione ad un altro utente!");
		}

		// Riempie il model di ricerca per Ufficio dell'utente
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

		// Imposta tipo Materiale
		setRequestAttribute("tipo_posizione_materiale", "SIGE");

		return lretPage; // restituisce la jsp di VIEW

	}

}