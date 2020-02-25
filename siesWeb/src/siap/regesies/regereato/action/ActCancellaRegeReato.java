package siap.regesies.regereato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.regereato.model.RegeReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActCancellaRegeReato</p>
 * <p>Description: Action che realizza la cancellazione di Rege reato</p>
 */
public class ActCancellaRegeReato extends ActionRegeSiap implements ICostantiRegeReato
{

  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    int lProgr = getRequestIntParameter(CAMPO_PROGR_REATO);
    int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

    RegeReatoModel lReato = new RegeReatoModel();
    lReato.setIdFile(lId);
    lReato.setProgrReato(lProgr);
    lReato.setProgrCircostanza(lProgrCirc);

    IRegeReato lCtrl = RegeSiesLookupRemote.getRegeReatoRemote();
    lCtrl.ExCancellaRegeReato(lReato);

    String lPage = "";
    lPage = PAGE_DETTAGLIO_RITORNO + lId;
    return lPage;
  }
}