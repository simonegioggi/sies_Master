package siap.sius.permesso.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPEsecuzioneLicenza extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Esecuzione Licenza");
    setRequestAttribute("nextAction", "siap.sius.permesso.action.ActLoadDettaglioEsecuzioneLicenza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}