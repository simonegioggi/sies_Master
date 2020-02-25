package siap.sige.udienzaprocedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.udienza.action.ICostantiUdienzaSige;

public class ActLoadFSigePVerbaleRinvioUdienza extends ActLoadRicercaFSigePuntuale
implements ICostantiUdienzaSige
{
  public String processRequest() throws Exception
  {
    // Imposta alla JSP il nome della funzione e l'azione
    // da chiamare alla conferma.
    setRequestAttribute("functionName", "Rinvio Udienza da Verbale");
    setRequestAttribute("nextAction", 
        "siap.sige.udienzaprocedimento.action.ActLoadInserisciVerbaleRinvioUdienza");
    
   // setRequestAttribute("nextAction", "siap.sige.udienza.action.ActRicercaFSPRinvioUdienza");
    // Si invoca il metodo della superclasse.
    String lPage = super.processRequest();
    return lPage;
  }
}