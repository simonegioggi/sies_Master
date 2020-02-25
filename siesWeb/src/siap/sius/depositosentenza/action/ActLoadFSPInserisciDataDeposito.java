package siap.sius.depositosentenza.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPInserisciDataDeposito extends ActLoadRicercaFSPuntuale
	implements ICostantiDepositoSentenza
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Deposito Sentenza");
		setRequestAttribute("nextAction", "siap.sius.depositosentenza.action.ActRicercaDepositoSentenza");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}