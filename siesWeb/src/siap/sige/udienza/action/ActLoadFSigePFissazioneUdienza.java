package siap.sige.udienza.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import f3b.web.IWebConstants;

public class ActLoadFSigePFissazioneUdienza extends ActLoadRicercaFSigePuntuale implements ICostantiUdienzaSige {

	public String processRequest() throws Exception {

		String lPage = "";

		// ==========================================================================
		// Verifico che il parametro CAMPO_ID_FASCICOLO_SIGE sia stato passato sulla
		// request o presente in sessione.
		// Questo controllo è necessario in quanto questa funzione
		// può essere richiamata anche dal menù di scelta rapida
		// ==========================================================================
		if (!this.isRequestParameterNullObj(CAMPO_ID_FASCICOLO_SIGE) || !this.isSessionAttributeNullObj("FascicoloSigeEsteso")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.udienza.action.ActLoadInserisciFissazioneUdienza&ritorno=ok";
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			// Imposta alla JSP il nome della funzione e l'azione
			// da chiamare alla conferma.
			setRequestAttribute("functionName", "Fissazione Udienza");
			setRequestAttribute("nextAction", "siap.sige.udienza.action.ActLoadInserisciFissazioneUdienza");
			// Si invoca il metodo della superclasse.
			lPage = super.processRequest();
		}

		return lPage;
	}

}
