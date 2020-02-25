package siap.sius.depositodecreto.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFPSEmissioneDecretoRevoca extends ActLoadRicercaFSPuntuale
//implements ICostantiDepositoOrdinanzaPc
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		setRequestAttribute("functionName", "Emissione Decreto di Revoca");
		setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActLoadEmissioneDecretoRevoca");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}