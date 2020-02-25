
package siap.sius.udienza.action;

import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadInserisciCopiaUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load modifica Udienza
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
public class ActLoadInserisciCopiaUdienzaUDS extends ActLoadModificaUdienzaUDS {

	public String processRequest() throws F3BException {

		// genny 18/03/2004
		// String lCodUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		// String lCodTipoUfficio = "PM";

		// valorizzazione della request
		preparaRequest();

		mUdiMod.setDataUdienza(null);
		// Imposta la risposta nella request.
		setRequestAttribute("modalita", "X");
		setRequestAttribute("udienza", mUdiMod);

		return PG_LOAD_INSERISCICOPIAUDIENZA_UDS; // restituisce la jsp di VIEW
	}

}