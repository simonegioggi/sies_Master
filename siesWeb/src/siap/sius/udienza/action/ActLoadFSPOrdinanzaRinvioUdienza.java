package siap.sius.udienza.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPOrdinanzaRinvioUdienza extends ActLoadRicercaFSPuntuale
implements ICostantiUdienza
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Ordinanza Rinvio Udienza");
    setRequestAttribute("nextAction", "siap.sius.udienza.action.ActLoadInserisciOrdinanzaRinvioUdienza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}

