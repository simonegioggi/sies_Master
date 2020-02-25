package siap.sige.impugnazione.action;

/**
* <p>Title: ActLoadRicercaFSPOpposizione</p>
* <p>Description: Classe Action per la load di RicercaFSPuntuale</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;

public class ActLoadRicercaFSPOpposizione extends ActLoadRicercaFSigePuntuale
{
  public String processRequest() throws Exception
  {
    super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    
    setRequestAttribute("nextAction", "siap.sige.impugnazione.action.ActRicercaFSPImpugnazioneSige" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti per Opposizione" );
    setSessionAttribute("tipoImpugnazione", "Opposizione");
    setSessionAttribute("codTipoImpugnazione", "04");
    
    return PG_LOAD_RICERCAFSIGEPUNTUALE; //restituisce la jsp di VIEW.
  }
}