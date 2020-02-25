package siap.sico.ufficio.action;

import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioUfficio
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ufficio
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
public class ActLoadDettaglioUfficio extends ActionSiap implements ICostantiUfficio {

	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_COD_UFFICIO);
		IUfficio lCtrl = SICOLookupRemote.getUfficioRemote();
		UfficioModel llUffMod = lCtrl.ExRicercaUfficioByCod(lId);
		setRequestAttribute("ufficio", llUffMod);

		return PG_LOAD_DETTAGLIOUFFICIO;
	}

}