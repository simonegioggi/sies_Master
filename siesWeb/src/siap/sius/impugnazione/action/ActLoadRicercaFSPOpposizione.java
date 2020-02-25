package siap.sius.impugnazione.action;

/**
* <p>Title: ActLoadRicercaFSPOpposizione</p>
* <p>Description: Classe Action per la load di RicercaFSPuntuale Opposizioni</p>
* <p>Copyright: Copyright (c) 2003</p>
*
* @since 06/2014
*/

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

public class ActLoadRicercaFSPOpposizione extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    setRequestAttribute("nextAction", "siap.sius.impugnazione.action.ActRicercaFSPOpposizione" );    

    setRequestAttribute("functionName", "Ricerca Provvedimenti per Opposizione" );

    return PG_LOAD_RICERCAFSPUNTUALE; 
  }
}