package siap.sius.udienza.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPSentenzaRinvioUdienza extends ActLoadRicercaFSPuntuale
implements ICostantiUdienza
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Sentenza Rinvio Udienza");
    setRequestAttribute("nextAction", "siap.sius.udienza.action.ActLoadInserisciSentenzaRinvioUdienza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}

