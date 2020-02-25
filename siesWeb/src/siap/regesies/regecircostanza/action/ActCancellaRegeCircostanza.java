package siap.regesies.regecircostanza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActCancellaRegeCircostanza</p>
 * <p>Description: Action che realizza la cancellazione di Rege circostanza</p>
 */
public class ActCancellaRegeCircostanza extends ActionRegeSiap
implements ICostantiRegeCircostanza
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

    RegeCircostanzaModel lCircostanza = new RegeCircostanzaModel();
    lCircostanza.setIdFile(lId);
   lCircostanza.setProgrCircostanza(lProgrCirc);

    IRegeCircostanza lCtrl = RegeSiesLookupRemote.getRegeCircostanzaRemote();
    lCtrl.ExCancellaRegeCircostanza(lCircostanza);

    String lPage = "";
    lPage = PAGE_DETTAGLIO_RITORNO + lId;
    return lPage;
  }
}