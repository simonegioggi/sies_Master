package siap.sius.fascicolo.action;


public class ActLoadFSPDefinizioneProcedimento extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
      // Imposta alla JSP il nome della funzione e l'azione
      // da chiamare alla conferma.
      setRequestAttribute("functionName", "Definizione Procedimento");
      setRequestAttribute("nextAction", "siap.sius.fascicolo.action.ActLoadDefinizioneProcedimento");
      // Si invoca il metodo della superclasse.
      String lPage = super.processRequest();
      return lPage;
  }
}