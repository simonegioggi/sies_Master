package siap.sius.depositodecreto.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

/**
 * MEV_9: aggiunta action di ricerca
 *
 * @author Gioggi
 */
public class ActLoadFSPConfermaDecisioneMagistratoRelatore extends ActLoadRicercaFSPuntuale {

	public String processRequest() throws Exception {

		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Conferma della Decisione del Magistrato Relatore");
		setRequestAttribute("nextAction",
				"siap.sius.depositodecreto.action.ActLoadInserisciConfermaDecisioneMagistratoRelatore");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}