package siap.siepe.attivita.action;

import siap.siepe.fascicolo.action.ActLoadRicercaFasSiepePuntuale;

public class ActLoadRicercaFSPInserisciAttivita extends ActLoadRicercaFasSiepePuntuale
implements ICostantiAttivita
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Inserimento Attivita");
    setRequestAttribute("nextAction", "siap.siepe.attivita.action.ActLoadInserisciFSPAttivita");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();

    return lPage;
  }
}
