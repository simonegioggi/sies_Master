package siap.sige.reato.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.siep.reato.action.ActInserisciReato;
import siap.siep.reato.controller.IReato;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;

/**
 * <p>
 * Title: ActInserisciReatoSige
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Reato.
 * </p>
 * L'action specializza siap.siep.reato.action.ActInserisciReato poichè la form di input utilizzata è la
 * stessa; questa action utilizza la funzione letturaDati(...) ereditata che valorizza i reati da inserire.
 * Viene poi richiamata la funzione specifica per l'inserimento di reati
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
public class ActInserisciReatoSige extends ActInserisciReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");

		// Viene chiamata la funzione ereditata per leggere i dati dalla form
		letturaDati(null);

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = (BigDecimal) getSessionAttribute(
				ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ID_FAS_SISGE_SENTENZA : " + lIdFasSigeSen);

		if (lReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare almeno un reato!");

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		/* ReatoModel lReatoPrincipale = */lCtrl.ExInserisciReatiSige(lReati, lIdFasSigeSen);

		// Prepara la pagina di destinazione

		// String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.reato.action.ActRicercaReato&"+CAMPO_PROGR_REATO+"="+lReatoPrincipale.getProgrReato();

		// Ritorno al punto di partenza
		String lRetPage = ritornoDopoCancellazione("Reati inseriti", null);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : fine");

		return lRetPage; // restituisce la jsp di VIEW
	}

}