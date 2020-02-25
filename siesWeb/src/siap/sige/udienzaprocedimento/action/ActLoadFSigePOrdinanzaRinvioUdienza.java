package siap.sige.udienzaprocedimento.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.udienza.action.ICostantiUdienzaSige;

public class ActLoadFSigePOrdinanzaRinvioUdienza extends ActLoadRicercaFSigePuntuale
implements ICostantiUdienzaSige
{
  public String processRequest() throws Exception
  {
    
    setRequestAttribute("functionName", "Ordinanza Rinvio Udienza");
    setRequestAttribute("nextAction", "siap.sige.udienza.action.ActRicercaFSPRinvioUdienza");
    
    String lPage = super.processRequest();
    return lPage;
  }
}