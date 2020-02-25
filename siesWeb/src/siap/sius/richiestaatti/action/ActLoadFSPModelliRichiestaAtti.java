package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPModelliRichiestaAtti extends ActLoadRicercaFSPuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Generazione Modelli");
    setRequestAttribute("nextAction", "siap.sius.richiestaatti.action.ActRicercaFSPModelliRichiestaAtti");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}