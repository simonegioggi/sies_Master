package siap.sige.udienza.action;

import siap.sige.web.ActionSige;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaUdienzePerProcedimento</p>
* <p>Description: Classe Action per il caricamento della Finestra di PopUp</p>
* che consente la ricerca di Udienze a partire da una data</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadRicercaUdienzePerProcedimento extends ActionSige implements ICostantiUdienzaSige
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI;  //restituisce la jsp di VIEW
  }
}




