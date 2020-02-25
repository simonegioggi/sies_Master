package siap.regesies.regeresidenza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActCancellaRegeResidenza</p>
 * <p>Description: Action che realizza la cancellazione di Rege Residenza</p>
 */
public class ActCancellaRegeResidenza
extends ActionRegeSiap implements ICostantiRegeResidenza
{

  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    String lProgr = getRequestStringParameter(CAMPO_COD_TIPO_RESIDENZA);

    IRegeResidenza lCtrl = RegeSiesLookupRemote.getRegeResidenzaRemote();
    lCtrl.ExCancellaRegeResidenza(lId,lProgr);

   String lPage="";
   lPage = PAGE_DETTAGLIO_RITORNO + lId;
   return lPage;
  }
}