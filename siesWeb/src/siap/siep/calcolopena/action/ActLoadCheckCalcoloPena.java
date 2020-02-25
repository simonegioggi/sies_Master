package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadCalcoloPena</p>
 * <p>Description: Azione Load della Pagina di Check dek calcolo della Pena</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author
 * @version 1.0
 */

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiepCheck;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadCheckCalcoloPena extends ActionSiap {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Action per il caricamento della pagina da cui selezionare i dati per il check del calcolo della pena
	 * 
	 * @return ritorna la pagina con il dettaglio dei fascicoli a sistema e i criteri di ricerca
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lChiaveUfficio = getCodUfficioUtenteConnesso();

		Vector lListaFascicoli = new Vector();
		Vector lListaFascicoliSIEP = new Vector();
		Vector lListaFascicoliRES = new Vector();

		IFascicoloSiepCheck lCheckCtrl = SIEPLookupRemote.getFascicoloSiepCheckRemote();

		lListaFascicoli = lCheckCtrl.ExContaFascicoli(lChiaveUfficio);
		lListaFascicoliSIEP = lCheckCtrl.ExContaFascicoliIscrittiSIEP(lChiaveUfficio);
		lListaFascicoliRES = lCheckCtrl.ExContaFascicoliIscrittiRES(lChiaveUfficio);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaFascicoli     =" + lListaFascicoli.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaFascicoliSIEP = " + lListaFascicoliSIEP.size());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lListaFascicoliRES  = " + lListaFascicoliRES.size());

		setRequestAttribute("ListaFascicoli", lListaFascicoli);
		setRequestAttribute("ListaFascicoliSIEP", lListaFascicoliSIEP);
		setRequestAttribute("ListaFascicoliRES", lListaFascicoliRES);

		return IWebConstants.ROOT_DIR + "files/siap/siep/calcolopena/LoadCheckCalcoloPena.jsp";
	}
}