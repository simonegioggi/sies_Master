package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPRimessioneAtti extends ActLoadRicercaFSPuntuale
implements ICostantiDepositoOrdinanzaPc
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		setRequestAttribute("functionName", "Rimessione Atti");
		setRequestAttribute("nextAction", "siap.sius.depositoordinanzapc.action.ActLoadRimessioneAtti");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}