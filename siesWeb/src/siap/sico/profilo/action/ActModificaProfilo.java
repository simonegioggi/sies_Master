package siap.sico.profilo.action;

/**
* <p>Title: ActModificaProfilo</p>
* <p>Description: Classe Action per la modifica di Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaProfilo extends ActionSiap implements ICostantiProfilo {

	/**
	 * Azione di Modifica del Profilo
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// String lId = getRequestStringParameter(CAMPO_COD_PROFILO);
		// riempie il model
		ProfiloModel lProMod = new ProfiloModel();

		lProMod.setCodProfilo(getRequestBigDecimalParameter(CAMPO_COD_PROFILO));
		lProMod.setDescrizione(getRequestStringParameter(CAMPO_DESCRIZIONE));
		if (!getRequestStringParameter(CAMPO_ANNO_DATA_FINE_VALIDITA).equals("")) {
			lProMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
					CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
		}
		// chiama il controller
		IProfilo lCtrl = SICOLookupRemote.getProfiloRemote();
		ProfiloModel llProModRet = lCtrl.ExModificaProfilo(lProMod);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("profilo", llProModRet);

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.profilo.action.ActListaProfili";
		return lPage;
	}

}