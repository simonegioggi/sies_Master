package siap.sige.udienzacollegiale.action;

/**
* <p>Title: ActLoadInserisciCopiaUdienzaCollegiale</p>
* <p>Description: Classe Action per la load modifica Udienza</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia </p>
* @version 1.0
*/
public class ActLoadInserisciCopiaUdienzaCollegiale extends ActLoadModificaUdienzaCollegiale {

	public String processRequest() throws Exception {
		// valorizzazione della request
		String lPage = super.processRequest();

		mUdienzaSige.setDataUdienza(null);
		// Imposta la risposta nella request.
		setRequestAttribute("modalita", "X");
		setRequestAttribute("udienzasige", mUdienzaSige);

		return lPage;
	}

}