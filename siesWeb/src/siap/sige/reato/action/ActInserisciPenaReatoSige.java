package siap.sige.reato.action;

/**
* <p>Title: ActInserisciPenaReato</p>
* <p>Description: Classe Action per l'inserimento di Pena Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.siep.reato.action.ActInserisciPenaReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;

public class ActInserisciPenaReatoSige extends ActInserisciPenaReato {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Reato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// preparazione dei dati leggendo la request
		ReatoSentenzaSigeModel lReaSigeMod = new ReatoSentenzaSigeModel(LetturaDati());
		// Lettura FAS_SIGE_SEN_ID dalla session
		BigDecimal lIdFasSigeSen = (BigDecimal) getSessionAttribute(
				ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
		lReaSigeMod.setFasSigeSenId(lIdFasSigeSen);

		// Scrittura nel DB
		ReatoModel lReaModRet = inserimento(lReaSigeMod);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Inserito Pena Reato SIGE : " + lReaModRet.getIdReato());

		return paginaDestinazione("siap.sige.reato.action.ActLoadDettaglioPenaReatoSige",
				lReaModRet.getIdReato());
	}

	/**
	 * Inserimento Pena Reato
	 * 
	 * @return
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	protected ReatoModel inserimento(ReatoSentenzaSigeModel aReato) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".inserimento : inizio");

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();
		Vector lNorme = lCtrl.ExRicercaNormeSige(aReato);

		ReatoModel lReaModRet = lCtrl.ExModificaPenaReato(aReato, lNorme);

		////// FINE
		setRequestAttribute("reato", lReaModRet);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".inserimento : fine");

		return lReaModRet;
	}

	protected String paginaDestinazione(String aAction, BigDecimal aIdReato) throws Exception {

		// Costruzione della pagina di redirect
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);

		lRedirectTo.setAction(aAction);
		lRedirectTo.setParameter(CAMPO_ID_REATO, aIdReato.toString());
		// Passaggio dei parametri inerenti il bottone di ritorno
		if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
			lRedirectTo.setParameter(IWebConstants.LINK_RITORNO,
					getRequestStringParameter(IWebConstants.LINK_RITORNO));
		else if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
			lRedirectTo.setParameter(IWebConstants.FLAG_RITORNO,
					getRequestStringParameter(IWebConstants.FLAG_RITORNO));

		// Pagina di destinazione
		String lPage = lRedirectTo.toString();

		return lPage;
	}

}