package siap.sico.evento.action;

/**
 * <p>Title: ActRicercaProvvedimentiNonValidati</p>
 * <p>Description: Classe Action per la ricerca di Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.web.IWebConstants;

public class ActRicercaOmesseNotificheOEPerMesaggio extends ActionSiap implements ICostantiFascicoloSiep {

	public String processRequest() throws Exception {

		return "/jsp/MainAttesa.jsp?" + IWebConstants.ACTION_FIELD
				+ "=siap.sico.evento.action.ActRicercaOmesseNotificheOE";
	}

}