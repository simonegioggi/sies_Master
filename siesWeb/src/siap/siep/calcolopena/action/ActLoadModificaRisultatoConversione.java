package siap.siep.calcolopena.action;

import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadAnnotazioniManualiCompAltroTitolo</p>
 * <p>Description: Azione Load della form di inserimento Computo Fungibilità (MC)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;

public class ActLoadModificaRisultatoConversione extends ActionSiap implements ICostantiAnnotazioneManuale {

	/**
	 * Modifica Risultato Conversione
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */

	public String processRequest() throws F3BException {

		// String strFormname = getRequestStringParameter("formname");
		// String strfieldname = getRequestStringParameter("fieldname");

		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/calcolopena/ModificaRisultatoConversione.jsp";

		return lPage; // restituisce la jsp di VIEW
	}

}