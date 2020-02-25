package siap.siep.posizionematerialefasc.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.controller.IPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PosizioneMaterialeFasc
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

		// Si ricava il Fascicolo dalla sessione
		FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicolo.getIdFascicoloSiep();
		Date lData_minima = lFascicolo.getDataIscrizione();

		// Si controlla se il fascicolo non abbià già una posizione materiale assegnata ed attiva
		IPosizioneMaterialeFasc lCtrlPosMatFas = SIEPLookupRemote.getPosizioneMaterialeFascRemote();
		Vector lVectPosMatFas = lCtrlPosMatFas.ExRicercaPosizioneMaterialeFascAttiva(lIdFascicolo);
		if (lVectPosMatFas.size() > 0)
			lData_minima = ((PosizioneMaterialeFascModel) lVectPosMatFas.get(0)).getDataInizio();

		// Lock per evitare inserimento contemporaneo per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "PosizioneMateriale",
				lIdFascicolo.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"La definizione della Posizione Materiale per questo fascicolo è in gestione ad un altro utente ["
							+ lck.getCodOperatore() + "]!");
		}

		// Riempie il model di ricerca per Ufficio dell'uttente
		PosizioneMaterialeModel lPosMod = new PosizioneMaterialeModel();
		lPosMod.setCodUfficio(getCodUfficioUtenteConnesso());
		lPosMod.setFiltroDataValidita("0"); // FiltroDataValidita = 0 ==> Visualizza Solo Posizioni Materiali
											// Valide

		// Ricerca
		IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
		Vector lVectPos = lCtrl.ExRicercaPosizioneMateriale(lPosMod);
		// passaggio alla form
		setRequestAttribute("posizioni", lVectPos);
		setRequestAttribute("cod_ufficio", lPosMod.getCodUfficio());
		setRequestAttribute("data_minima", lData_minima);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		lretPage = PG_LOAD_INSERISCIPOSIZIONEMATERIALEFASC;
		// }
		return lretPage; // restituisce la jsp di VIEW
	}

}