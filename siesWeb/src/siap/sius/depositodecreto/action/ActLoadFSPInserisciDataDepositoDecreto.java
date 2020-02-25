package siap.sius.depositodecreto.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPInserisciDataDepositoDecreto extends ActLoadRicercaFSPuntuale
implements ICostantiDepositoDecreto
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Deposito Decreto");
		setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActRicercaDepositoDecreto");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}