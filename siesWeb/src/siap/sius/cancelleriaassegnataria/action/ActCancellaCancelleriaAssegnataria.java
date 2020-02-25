package siap.sius.cancelleriaassegnataria.action;

import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.util.SIUSLookupRemote;


/**
* <p>Title: ActCancellaCancelleriaAssegnataria</p>
* <p>Description: Classe Action per la cancellazione di una CancelleriaAssegnataria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaCancelleriaAssegnataria extends ActionSiap implements ICostantiCancelleriaAssegnataria
{
   public String processRequest() throws Exception
   {
      // Riempie il model che individuail record da cancellare
      CancelleriaAssegnatariaModel lCanMod = new CancelleriaAssegnatariaModel();
      lCanMod.setCodCancelleriaAssegnataria(getRequestStringParameter(CAMPO_COD_CANCELLERIA_ASSEGNATARIA));
      lCanMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));

     // Cancellazione attraverso il controller
     ICancelleriaAssegnataria lCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
     lCtrl.ExCancellaCancelleriaAssegnataria(lCanMod);

     String retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);

      return retPage;
   }
}