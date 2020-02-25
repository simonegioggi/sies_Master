package siap.sius.fascicolo.action;

/**
* <p>Title: ActLoadRicercaFSPuntuale</p>
* <p>Description: Classe Action per la load di RicercaFSPuntuale</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;

public class ActLoadRicercaFSPuntuale extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {

    return PG_LOAD_RICERCAFSPUNTUALE; //restituisce la jsp di VIEW
  }
}