package siap.sico.residenza.action;

/**
* <p>Title: ActLoadRicercaResidenza</p>
* <p>Description: Classe Action per la load ricerca di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaResidenza extends ActionSiap implements ICostantiResidenza
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCARESIDENZA;  //restituisce la jsp di VIEW
  }
}