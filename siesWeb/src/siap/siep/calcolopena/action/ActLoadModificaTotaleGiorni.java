package siap.siep.calcolopena.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: Modifica Totale Giorni</p>
 * <p>Description: Azione Modifica Totale Giorni (MC)</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;

public class ActLoadModificaTotaleGiorni extends ActionSiap implements ICostantiAnnotazioneManuale {

	/**
	 * Modifica Totale Giorni
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */

	public String processRequest() throws F3BException {

		// String strFormname = getRequestStringParameter("formname");
		// String strfieldname = getRequestStringParameter("fieldname");
		// String strfieldname1 = getRequestStringParameter("fieldname1");
		// String strfieldname2 = getRequestStringParameter("fieldname2");
		// String strfieldname3 = getRequestStringParameter("fieldname3");

		String lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/ModificaTotaleGiorni.jsp";

		return lPage; // restituisce la jsp di VIEW
	}

}