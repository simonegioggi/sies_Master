package siap.sige.avvocato.action;


import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;

public class ActLoadFSigePerAssociaAvvocato extends ActLoadRicercaFSigePuntuale implements ICostantiProvvedimentoSige {
	public String processRequest() throws Exception
	{
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		
		// Imposta alla JSP il nome della funzione e l'azione da chiamare alla conferma.
		setRequestAttribute("functionName", "Associa Avvocato");
		setRequestAttribute("nextAction", "siap.sige.avvocato.action.ActLoadInserisciAssegnaAvvocato");
		String idAvvocato=super.getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		setRequestAttribute("idAvvocato", idAvvocato);
		
		return lPage;
	}
}
