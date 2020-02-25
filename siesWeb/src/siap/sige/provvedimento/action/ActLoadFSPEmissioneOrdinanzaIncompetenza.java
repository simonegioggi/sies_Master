package siap.sige.provvedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadFSPEmissioneOrdinanzaIncompetenza extends ActLoadRicercaFSigePuntuale
	implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		// Imposta alla JSP il nome della funzione e l'azione
		// da chiamare alla conferma.
		setRequestAttribute("functionName", "Emissione Ordinanza Incompetenza");
		setRequestAttribute("nextAction", "siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaIncompetenza");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}