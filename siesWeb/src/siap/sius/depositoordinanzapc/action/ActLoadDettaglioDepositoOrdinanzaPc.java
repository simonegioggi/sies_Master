package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActLoadDettaglioDepositoOrdinanzaPc</p>
* <p>Description: Classe Action per la load dettaglio di DepositoOrdinanzaPc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioDepositoOrdinanzaPc extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {

 		 String lId = getRequestStringParameter(CAMPO_ID_DEPOSITO_ORDINANZA_PC);
 		 // riempie il model
		 // chiama il controller

		 IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		 DepositoOrdinanzaPcModel llDepMod = lCtrl.ExRicercaDepositoOrdinanzaPcByKey(new BigDecimal(lId));
		 setRequestAttribute("depositoordinanzapc", llDepMod);

		 return PG_LOAD_DETTAGLIODEPOSITOORDINANZAPC;
	 }

}