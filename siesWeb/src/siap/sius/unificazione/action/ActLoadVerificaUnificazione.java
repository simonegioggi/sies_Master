package siap.sius.unificazione.action;

/**
* <p>Title: ActLoadVerificaUnificazione</p>
* <p>Description: Classe Action per la load di VerificaUnificazione</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;

public class ActLoadVerificaUnificazione extends ActionSiap
implements ICostantiUnificazione
{
  public String processRequest() throws Exception
  {
    return PG_LOAD_VERIFICAUNIFICAZIONE; //restituisce la jsp di VIEW
  }
}