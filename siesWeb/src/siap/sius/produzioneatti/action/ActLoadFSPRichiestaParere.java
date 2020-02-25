package siap.sius.produzioneatti.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPRichiestaParere extends ActLoadRicercaFSPuntuale
implements ICostantiProduzioneAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Richiesta parere ");
    setRequestAttribute("nextAction", "siap.sius.produzioneatti.action.ActLoadRichiestaParere");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}