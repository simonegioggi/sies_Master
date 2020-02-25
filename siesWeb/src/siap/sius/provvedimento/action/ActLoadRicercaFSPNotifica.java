package siap.sius.provvedimento.action;

/**
* <p>Title: ActLoadRicercaFSPNotifica</p>
* <p>Description: Classe Action per la load di RicercaFSPNotifica</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.fascicolo.action.ActLoadRicercaFSPuntuale;

public class ActLoadRicercaFSPNotifica extends ActLoadRicercaFSPuntuale
{
  public String processRequest() throws Exception
  {
    //super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sius.provvedimento.action.ActRicercaFSPNotifica" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti per Notifica" );
    return PG_LOAD_RICERCAFSPUNTUALE; //restituisce la jsp di VIEW.
  }
}