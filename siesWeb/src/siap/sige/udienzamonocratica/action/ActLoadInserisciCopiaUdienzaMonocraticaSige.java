package siap.sige.udienzamonocratica.action;

import f3b.util.F3BException;

/**
* <p>Title: ActLoadInserisciCopiaUdienzaMonocratica</p>
* <p>Description: Classe Action per la load modifica Udienza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia </p>
* @version 1.0
*/
public class ActLoadInserisciCopiaUdienzaMonocraticaSige extends ActLoadModificaUdienzaMonocraticaSige {

	public String processRequest() throws Exception {
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP") || getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") || getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP"))
			throw new F3BException(F3BException.USER_MESSAGE, "Funzione inibita per il tipo ufficio di competenza.");

		// valorizzazione della request
		String lPage = super.processRequest();

		mUdienzaSige.setDataUdienza(null);
		// Imposta la risposta nella request.
		setRequestAttribute("modalita", "X");
		setRequestAttribute("udienzamonocraticasige", mUdienzaSige);

		return lPage;
	}

}