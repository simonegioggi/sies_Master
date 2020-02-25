package siap.sius.permesso.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPEsecuzionePermesso extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Esecuzione Permesso");
    setRequestAttribute("nextAction", "siap.sius.permesso.action.ActLoadDettaglioEsecuzionePermesso");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}
