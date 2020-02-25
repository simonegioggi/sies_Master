package siap.sius.udienzaprocedimento.action;

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadFSPPreFissazioneUdienza extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Prefissazione Udienza");
    setRequestAttribute("nextAction", "siap.sius.udienzaprocedimento.action.ActLoadPreFissazioneUdienza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}

