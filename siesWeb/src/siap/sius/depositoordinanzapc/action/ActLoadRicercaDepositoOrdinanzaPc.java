package siap.sius.depositoordinanzapc.action;

import siap.sico.web.ActionSiap;

/**
* <p>Title: ActLoadRicercaDepositoOrdinanzaPc</p>
* <p>Description: Classe Action per la load ricerca di DepositoOrdinanzaPc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
		 return PG_LOAD_RICERCADEPOSITOORDINANZAPC;  //restituisce la jsp di VIEW
  }

}