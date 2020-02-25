package siap.sige.sentenza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.reato.controller.IReato;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActDettaglioAltroTitolo
 * </p>
 * <p>
 * Description: Classe Action visualizza il Dettaglio di un Titolo Esecutivo (sentenza) legato ad un
 * Procedimento SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @author luigi
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioAltroTitolo extends ActionSige implements ICostantiFasSigeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// Eventuale ID Fascicolo SIEP
		BigDecimal lIdFasSiep = null;

		setLinkRitorno();

		// Lettura dalla request ID_FAS_SISGE_SENTENZA
		BigDecimal lIdFasSigeSen = getRequestBigDecimalParameter(CAMPO_ID_FAS_SIGE_SENTENZA);

		// Viene istanziato il controller per effettuare la ricerca
		IFasSigeSentenza lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		SentenzaSigeModel lSentenzaSige = lCtrl.ExRicercaFasSigeSentenzaByKey(lIdFasSigeSen);

		setSessionAttribute("sentenza", lSentenzaSige);
		setRequestAttribute("sentenza", lSentenzaSige);

		// Viene messo in sessione ID_FAS_SISGE_SENTENZA
		setSessionAttribute(CAMPO_ID_FAS_SIGE_SENTENZA, lIdFasSigeSen);

		ricercaReati(lIdFasSigeSen);

		if (lSentenzaSige != null && lSentenzaSige.getFascicoloSiep() != null)
			lIdFasSiep = lSentenzaSige.getFascicoloSiep().getIdFascicoloSiep();
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("ID Fascicolo SIEP -> " + lIdFasSiep );

		// Vengono passate nella request i parametri Modificabile Cancellabile
		setModificaCancellaTitolo(lIdFasSiep);

		return PG_DETTAGLIO_ALTRO_TITOLO;
	}

	private void ricercaReati(BigDecimal aKey) throws Exception {
		ReatoSentenzaSigeModel lReatoSige = new ReatoSentenzaSigeModel();

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		lReatoSige.setFasSigeSenId(aKey);
		Vector lVect = lCtrl.ExRicercaReatoSige(lReatoSige);
		if (lVect != null && lVect.size() > 0) {
			setSessionAttribute("reato", lVect.get(0));
			setRequestAttribute("reati", lVect);
			setRequestAttribute("modo", "SIGE");
		}
	}

}