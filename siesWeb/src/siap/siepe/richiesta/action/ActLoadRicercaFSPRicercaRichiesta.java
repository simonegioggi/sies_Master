package siap.siepe.richiesta.action;

import siap.siepe.fascicolo.action.ActLoadRicercaFasSiepePuntuale;

public class ActLoadRicercaFSPRicercaRichiesta extends ActLoadRicercaFasSiepePuntuale
implements ICostantiRichiesta
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Elenco Richieste");
    setRequestAttribute("nextAction", "siap.siepe.richiesta.action.ActRicercaRichiesta");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();

    return lPage;
  }
}
