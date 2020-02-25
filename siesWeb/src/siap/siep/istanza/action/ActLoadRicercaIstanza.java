package siap.siep.istanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaIstanza</p>
* <p>Description: Classe Action per la load ricerca di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaIstanza extends ActionSiap implements ICostantiIstanza
{
  public String processRequest() throws Exception
  {
    //Oggetto dell'istanza
    Option lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoTDS());
    setRequestAttribute("contenuto", "" + lOption );

    return PG_LOAD_RICERCAISTANZA;  //restituisce la jsp di VIEW
  }
}