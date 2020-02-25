package siap.sico.magistrato.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaMagistratiUfficio</p>
* <p>Description: Classe Action per la load ricerca di </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaMagistratiUfficio extends ActionSiap implements ICostantiMagistrato
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCA_MAGISTRATI_UFFICIO_POPUP;  
  }
}




