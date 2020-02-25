package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPRichiestaAtti extends ActLoadRicercaFSPuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Richiesta Atti");
    setRequestAttribute("nextAction", "siap.sius.richiestaatti.action.ActRicercaFSPRichiestaAtti");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}