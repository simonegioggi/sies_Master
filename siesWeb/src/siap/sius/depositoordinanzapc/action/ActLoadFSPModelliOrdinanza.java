package siap.sius.depositoordinanzapc.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPModelliOrdinanza extends ActLoadRicercaFSPuntuale
implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Generazione Modelli");
    setRequestAttribute("nextAction", "siap.sius.depositoordinanzapc.action.ActRicercaFSPModelliOrdinanza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}