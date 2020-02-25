package siap.regesies.regenotiziareato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActCancellaRegeNotiziaReato</p>
 * <p>Description: Action che realizza la cancellazione di Rege notiziareato</p>
 */
public class ActCancellaRegeNotiziaReato extends ActionRegeSiap
implements ICostantiRegeNotiziaReato
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    String lProgr = getRequestStringParameter(CAMPO_PROGR_NOTIZIA);

    RegeNotiziaReatoModel lNotiziaReato = new RegeNotiziaReatoModel();
    lNotiziaReato.setIdFile(lId);
    lNotiziaReato.setProgrNotizia(lProgr);

    IRegeNotiziaReato lCtrl = RegeSiesLookupRemote.getRegeNotiziaReatoRemote();
    lCtrl.ExCancellaRegeNotiziaReato(lNotiziaReato);

    String lPage = "";
    lPage = PAGE_DETTAGLIO_RITORNO + lId;
    return lPage;
  }
}