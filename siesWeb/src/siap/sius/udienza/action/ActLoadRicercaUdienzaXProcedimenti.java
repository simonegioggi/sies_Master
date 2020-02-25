package siap.sius.udienza.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaUdienzaXProcedimenti</p>
* <p>Description: Classe Action per il caricamento della Finestra di PopUp</p>
* che consente la ricerca di Udienze a partire da una data</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaUdienzaXProcedimenti extends ActionSiap implements ICostantiUdienza
{
  public String processRequest() throws F3BException
  {
    return PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI;  //restituisce la jsp di VIEW
  }
}




