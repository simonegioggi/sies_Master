package siap.siep.fascicolo.action;

import siap.sico.web.ActionSiap;

public class ActGrigliaRicerca extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {
    return PG_GRIGLIA_RICERCHE_SIEP;

  }
}
