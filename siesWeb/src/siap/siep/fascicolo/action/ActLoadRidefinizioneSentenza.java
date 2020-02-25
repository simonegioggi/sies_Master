package siap.siep.fascicolo.action;

import siap.sico.web.ActionSiap;


public class ActLoadRidefinizioneSentenza extends ActionSiap implements ICostantiFascicoloSiep
{
  public String processRequest() throws Exception
  {   
    return PG_RIDEFINIZIONESENTENZA;
  }
}
