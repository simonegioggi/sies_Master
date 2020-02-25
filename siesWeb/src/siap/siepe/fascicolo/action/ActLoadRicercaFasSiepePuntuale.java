package siap.siepe.fascicolo.action;

/**
* <p>Title: ActLoadRicercaFasSiepePuntuale</p>
* <p>Description: Classe Action per la load di RicercaFasSiepePuntuale</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;

public class ActLoadRicercaFasSiepePuntuale extends ActionSiap implements ICostantiFascicoloSiepe
{
  public String processRequest() throws Exception
  {

    return PG_LOAD_RICERCAFASSIEPEPUNTUALE; //restituisce la jsp di VIEW
  }
}
