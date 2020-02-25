package siap.sige.circostanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.circostanza.action.ICostantiCircostanza;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.circostanza.model.CircostanzaSigeModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaCircostanzaSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Circostanza legata a Sentenza Sige
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
public class ActRicercaCircostanzaSige extends ActionSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPage = ICostantiCircostanza.PG_RICERCACIRCOSTANZA;

		// Gestione ritorno
		setLinkRitorno();

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = getIdFasSigeSentenzaInSessione();

		// Circostanza Sige per il passaggio della condizione di ricerca
		CircostanzaSigeModel lCircostanzaSige = null;
		lCircostanzaSige = new CircostanzaSigeModel();
		lCircostanzaSige.setFasSigeSenId(lIdFasSigeSen);

		Vector lVect = null;
		ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();

		try {
			lVect = lCtrl.ExRicercaCircostanza(lCircostanzaSige);
		} catch (F3BException fe) {
			// Viene intercettato il messaggio di "elementi non trovati"
			if (fe.getErrorCode() != F3BException.USER_MESSAGE)
				throw fe;
		} catch (Exception e) {
			// Nessun Elemento Trovato
			throw new SIGEException(SIGEException.USER_MESSAGE, e.getMessage());
		}
		setRequestAttribute("modalita", "R");
		setRequestAttribute("modo", "SIGE");
		modificabilita();

		setRequestAttribute("circostanza", lVect);
		return lPage;
	}

	/**
	 * La modificabilità del Fascicolo Sige in sessione abilità la funzione di cancellazione e di inserimento
	 * di una nuova circostanza.
	 *
	 * @throws Exception
	 */
	private void modificabilita() throws Exception {

		// Modificabilità della Pena Accessoria
		String lCircostanzaModificabile = "SI";

		if (IsFascicoloSigeModificabile())
			lCircostanzaModificabile = "SI";
		else
			lCircostanzaModificabile = "NO";

		setRequestAttribute("Cancellabile", lCircostanzaModificabile);
		setRequestAttribute("Modificabile", lCircostanzaModificabile);
	}

}