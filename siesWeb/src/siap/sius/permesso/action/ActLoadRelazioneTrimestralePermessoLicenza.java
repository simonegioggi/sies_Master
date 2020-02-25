package siap.sius.permesso.action;

import siap.sius.ActionSius;

public class ActLoadRelazioneTrimestralePermessoLicenza extends ActionSius 
implements ICostantiPermesso
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_RELAZIONE_TRIMESTRALE_PERMESSOLICENZA; //restituisce la jsp di VIEW
  }
}