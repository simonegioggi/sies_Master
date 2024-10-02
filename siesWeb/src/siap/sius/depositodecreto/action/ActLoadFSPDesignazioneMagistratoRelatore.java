package siap.sius.depositodecreto.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * MEV_2019-09: aggiunta action di ricerca
 *
 * @author Gioggi
 */
public class ActLoadFSPDesignazioneMagistratoRelatore extends ActLoadRicercaFSPuntuale {

	public String processRequest() throws Exception {

		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Designazione del Magistrato Relatore");
		setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActLoadInserisciDesignazioneMagistratoRelatore");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}