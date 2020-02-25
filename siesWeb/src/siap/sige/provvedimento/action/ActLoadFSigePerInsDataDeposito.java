package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadFSigePerInsDataDeposito extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Deposito Ordinanza");
		setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActRicercaDepositoOrdinanza");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}

}