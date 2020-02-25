package siap.sius.trasmissioneatti.action;

/**
* <p>Title: ActLoadRicercaFSPTrasmAtti</p>
* <p>Description: Classe Action per la load di RicercaFSPuntuale</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadRicercaFSPTrasmAtti extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sius.trasmissioneatti.action.ActRicercaFSPTrasmAtti" );
    setRequestAttribute("functionName", "Trasmissione atti per competenza" );
    return PG_LOAD_RICERCAFSPUNTUALE; //restituisce la jsp di VIEW.
  }
}