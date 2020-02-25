package siap.sius.richiestaatti.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadRicercaStatoAtti extends ActLoadRicercaFSPuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Visualizza Stato Atti");
    setRequestAttribute("nextAction", "siap.sius.richiestaatti.action.ActRicercaStatoAtti");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}