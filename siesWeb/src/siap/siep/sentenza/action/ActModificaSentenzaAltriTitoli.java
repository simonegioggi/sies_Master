package siap.siep.sentenza.action;


/**
 * <p>
 * Title: ActModificaSentenzaAltriTitoli
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Sentenza Altri Titoli
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @version 1.0
 */
public class ActModificaSentenzaAltriTitoli extends ActInserisciSentenzaAltriTitoli {

	/**
	 * Azione di Modifica di altri titoli
	 * 
	 * @return String
	 * @throws Exception
	 */

	public String processRequest() throws Exception {

		// Setta il flag che segnala operazione di Modifica
		mModifica = true;

		return super.processRequest();
	}

}