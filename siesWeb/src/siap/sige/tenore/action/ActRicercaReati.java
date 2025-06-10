package siap.sige.tenore.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.action.ICostantiDecodifiche;
import siap.siep.reato.controller.IReato;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * ActRicercaReati - Classe action per la ricerca Reati da Sentenza. Attualmente la funzione effettua una
 * ricerca diversa per i Reati collegati alla Sentenza del Procedimemto SIEP da quella per i reati invece
 * collegati ad "Altro Titolo Esecutivo". STUB: da rivedere
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaReati extends ActionSige implements ICostantiTenoreSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	IReato lReaCtrl = null;

	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");
		BigDecimal lIdFasSigeSentenza = null;
		Vector lReati = null;

		lReaCtrl = SIEPLookupRemote.getReatoRemote();

		// Ricerca riferimento Sentenza-Procedimento
		lIdFasSigeSentenza = ricercaIdFasSigeSentenza();
		lReati = ricercaReatiByFasSen(lIdFasSigeSentenza);

		if (lReati == null || lReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile individuare reati per la sentenza selezionata !");

		setRequestAttribute("ListaReati", lReati);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return ICostantiDecodifiche.PG_LISTAREATI_SIGE;
	}

	/**
	 * La funzione ricerca un riferimento nella tabella FAS_SIGE_SENTENZA tra Procedimento e Sentenza. Se il
	 * riferimento esiste è anche unico e servirà ad individuare i reati collegati.
	 *
	 * @return
	 * @throws F3BException
	 */

	private BigDecimal ricercaIdFasSigeSentenza() throws F3BException {
		BigDecimal lIdSentenza = null;
		BigDecimal lIdFasSigeSentenza = null;
		SentenzaSigeModel lFasSigeSen = null;

		if (isRequestParameterNullObj(CAMPO_SEN_ID_SENTENZA)
				|| getRequestStringParameter(CAMPO_SEN_ID_SENTENZA).trim().length() == 0)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile risalire a Reati in mancanza di una sentenza !!");

		// Lettura dell'ID Sentenza dalla request
		lIdSentenza = getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA);

		// Preparazione del Model con il filtro di ricerca
		lFasSigeSen = new SentenzaSigeModel();
		lFasSigeSen.setFasIdFascicoloSige(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
		lFasSigeSen.setIdSentenza(lIdSentenza);

		// Ricerca
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lFasSigeSenLista = lFasSenCtrl.ExRicercaFasSigeSentenza(lFasSigeSen);
		if (lFasSigeSenLista != null && lFasSigeSenLista.size() > 0) {
			lFasSigeSen = (SentenzaSigeModel) lFasSigeSenLista.get(0);
			lIdFasSigeSentenza = lFasSigeSen.getIdFasSigeSentenza();
		}

		return lIdFasSigeSentenza;

	}

	/**
	 * Ricerca dei Reati collegati aLTitolo Esecutivo
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */

	private Vector ricercaReatiByFasSen(BigDecimal aKey) throws F3BException {
		Vector lVectRisultato = new Vector();

		ReatoSentenzaSigeModel lReatoSige = new ReatoSentenzaSigeModel();

		// Ricerca per ID Fascicolo SIGE
		lReatoSige.setFasSigeSenId(aKey);
		lVectRisultato = lReaCtrl.ExRicercaReatoCircostanzaBySentenzaSige(aKey);
		return lVectRisultato;
	}

}