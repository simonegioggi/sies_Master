package siap.sige.fascicolo.action;

import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti Sige del Soggetto a Partire dal Dettaglio Fascicolo (in Sessione)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActRicercaProcedimentiSigeSoggettoDaFascicolo extends ActionSiap
		implements ICostantiFascicoloSige {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Soggetto Model preso dalla sessione.

		SoggettoModel lsoggettoMod = null;

		if (!isSessionAttributeNullObj("soggetto")) {
			lsoggettoMod = (SoggettoModel) getSessionAttribute("soggetto");
		}

		FascicoloSigeEstesoModel lFasEstMod = null;

		// Fascicolo SIGE esteso potrebbe non essere in sessione
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso")) {
			lFasEstMod = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		}

		// Lettura soggetto.
		SoggettoModel lSogMod = new SoggettoModel();

		// Modifica del 05/09/2013 "Implementazione SIES per accorpamento uffici"
		// Risolto errore preesistente (Questa classe viene invocata non solo dal
		// Dettaglio Fascicolo ma direttamente dal Soggetto. In questo secondo caso il
		// FascicoloSigeEsteso non è presente in sessione causando un NullPointerException)
		if (lFasEstMod != null && lFasEstMod.getFascicoloSige() != null) {
			lSogMod.setIdSoggetto(lFasEstMod.getFascicoloSige().getSogIdSoggetto());
		} else {
			lSogMod.setIdSoggetto(lsoggettoMod.getIdSoggetto());
		}

		// Parametri di ricerca.
		String ufUtConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String codDistretto = getUfficioUtenteConnesso().getCodDistretto();

		// Altri filtri es. archiviati, tribunale, data canc. etc. sono stati rimossi

		// Si chiama il FascicoloSigeController.
		IFascicoloSige lFascSogCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		// Vector lFascicoliSoggetto = lFascSogCtrl.ExRicercaFascicoliDelSoggetto(lSogMod, ufUtConnesso,
		// ufOTribunale, codDistretto, lIncludeArchiviati, codContenuto, DateUtils.getDate(
		// dataAlInCancelleria, "ddMMyyyy"), DateUtils.getDate( dataAlInCancelleria, "ddMMyyyy"));
		Vector lFascicoliSoggetto = lFascSogCtrl.ExRicercaFascSigeDelSoggetto(lSogMod, ufUtConnesso,
				codDistretto);

		String lReturnPage = "";

		// Estrazione Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetto != null) {
			SoggettoModel lSoggetto = ((FascicoloSigeEstesoModel) lFascicoliSoggetto.get(0)).getSoggetto();
			ISoggetto ctrlS = SICOLookupRemote.getSoggettoRemote();
			lSoggetto = ctrlS.ExRicercaSoggettoByKey(lSoggetto.getIdSoggetto());

			setRequestAttribute("soggetto", lSoggetto);
			setSessionAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("IdSoggetto", lSogMod.getIdSoggetto());
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("codDistretto", codDistretto);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetto);

		lReturnPage = ICostantiFascicoloSige.PG_ELENCO_FASCICOLIDELSOGGETTO;

		return lReturnPage; // restituisce la jsp di VIEW
	}
}
