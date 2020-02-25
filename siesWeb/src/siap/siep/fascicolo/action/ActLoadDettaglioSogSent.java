package siap.siep.fascicolo.action;

import siap.sico.web.ActionSiap;

public class ActLoadDettaglioSogSent extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {

    return PG_DETTAGLIO_SOGGETTO_SENTENZA;
  }
}
