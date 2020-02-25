package siap.sico.ufficio.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class ActLoadRicercaComunePerDistretto extends ActionSiap implements ICostantiUfficio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// STUB: 2003/02/17 - UfficioController lUCon = new UfficioController();
		// IUfficio lUCon = SICOLookupRemote.getUfficioRemote();

		/*
		 * String prova=this.getRequestStringParameter("distretto"); Vector lUffDistr =
		 * lUCon.ListaUfficiDistretto(getRequestStringParameter("distretto"));
		 */

		String codTipoUff = this.getRequestStringParameter("codTipoUff");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("codTipoUff------>" + codTipoUff);
		this.setRequestAttribute("codTipoUff", codTipoUff);

		return PG_RICERCA_COMUNE_PER_DISTRETTO;
	}

}