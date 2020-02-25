package siap.sius.rifasiep.action;

import siap.sico.web.ActionSiap;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
//import siap.sico.evento.action.ICostantiEvento;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;


/**
* <p>Title: ActCancellaRifFascicoloSiep</p>
* <p>Description: Classe Action per cancellare il riferimento fascicolo SIEP </p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaRifFascicoloSiep extends ActionSiap implements ICostantiRifFascicoloSiep
{
  public String processRequest() throws Exception
  {

    // chiama il controller
    IRiferimentoFascicoloSiep lCtrl = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
    lCtrl.ExCancellaRiferimentoFascicoloSiep(getRequestBigDecimalParameter(ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP));

    String retPage = null;
   String nextAct = null;

   if(!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE))
   {
      nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
      if (!isRequestParameterNullObj("noQuery"))
      {
        nextAct += "&noQuery=";
        nextAct += "OK";
      }
   }
   retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);
   return retPage;
  }
}