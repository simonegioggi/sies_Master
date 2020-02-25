package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadFSigePerInsDepositoDecreto extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Deposito Decreto");
		setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActRicercaDepositoDecreto");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}