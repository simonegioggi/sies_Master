package siap.sico.webservice.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import f3b.web.html.Option;

public class ActLoadRicercaTrasmissioni extends ActWsBase implements ICostantiNsc
{
  public String processRequest() throws Exception
  {

    Option lOption = new Option( DecodificheManager.getInstance().getTrasmissioni());
    setRequestAttribute("Trasmissioni", "" + lOption );

    return PG_LOAD_RICERCATRASMISSIONI;       //restituisce la jsp di VIEW
  }
}
