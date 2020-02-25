package siap.sius.depositoordinanzapc.action;

import siap.sico.web.ActionSiap;

/**
* <p>Title: ActLoadInserisciDepositoOrdinanzaPc</p>
* <p>Description: Classe Action per la load inserisci di DepositoOrdinanzaPc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
    setRequestAttribute("modalita", "I");
    return PG_LOAD_INSERISCIDEPOSITOORDINANZAPC;  //restituisce la jsp di VIEW
  }
}