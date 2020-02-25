package siap.sius.depositodecreto.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFPSEmissioneDecreto extends ActLoadRicercaFSPuntuale
//implements ICostantiDepositoOrdinanzaPc
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		setRequestAttribute("functionName", "Emissione Decreto");
		setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActLoadEmissioneDecreto");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}