package siap.sige.richiestaatti.action;


import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActRicercaEsitoParere extends ActLoadRicercaFSigePuntuale
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Esito parere ");
    setRequestAttribute("nextAction", "siap.sige.richiestaatti.action.ActElencoEsitoParere");
    
 
    
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    
 // Bottone di ritorno
    setLinkRitorno();
    
    return lPage;
  }
}