package siap.sius.depositosentenza.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPModelliSentenza extends ActLoadRicercaFSPuntuale
	implements ICostantiDepositoSentenza
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Generazione Modelli");
    setRequestAttribute("nextAction", "siap.sius.depositosentenza.action.ActRicercaFSPModelliSentenza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}