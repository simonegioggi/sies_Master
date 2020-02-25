package siap.siepe.attivita.action;

import siap.siepe.fascicolo.action.ActLoadRicercaFasSiepePuntuale;

public class ActLoadRicercaFSPRicercaAttivita extends ActLoadRicercaFasSiepePuntuale
implements ICostantiAttivita
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Ricerca Attività");
    setRequestAttribute("nextAction", "siap.siepe.attivita.action.ActRicercaAttivita");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();

    return lPage;
  }
}
