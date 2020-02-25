package siap.sius.provvedimento.action;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

/**
* <p>Title: ActLoadRicercaFSProvvedimenti</p>
* <p>Description: Classe Action per la load di RicercaFSProvvedimenti,
* cioè la ricerca prima del Fascicolo Sius e quindi
* dei provvedimenti collegati.</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRicercaFSProvvedimenti extends ActionSiap
implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    //super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
    setRequestAttribute("nextAction", "siap.sius.provvedimento.action.ActRicercaFSProvvedimenti" );
    setRequestAttribute("functionName", "Ricerca Provvedimenti" );
    
    return PG_LOAD_RICERCAFSPUNTUALE; //restituisce la jsp ricerca Fascicolo.
  }
}