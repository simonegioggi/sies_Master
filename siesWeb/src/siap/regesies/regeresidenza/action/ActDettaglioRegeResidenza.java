package siap.regesies.regeresidenza.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regeresidenza.controller.IRegeResidenza;
import siap.regesies.regeresidenza.model.RegeResidenzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActDettaglioRegeResidenza</p>
 * <p>Description: Dettaglio della rege Sentenza</p>
 */
public class ActDettaglioRegeResidenza
extends ActionRegeSiap implements ICostantiRegeResidenza
{

  public String processRequest() throws F3BException
  {

    String lId = getRequestStringParameter(CAMPO_ID_FILE);
    String lProgr = getRequestStringParameter(CAMPO_COD_TIPO_RESIDENZA);

    IRegeResidenza lCtrl = RegeSiesLookupRemote.getRegeResidenzaRemote();
    RegeResidenzaModel llRegMod = lCtrl.ExRicercaRegeResidenzaByKey(lId,lProgr);
    setRequestAttribute("regeresidenza", llRegMod);

    return PG_DETTAGLIO_REGERESIDENZA;
  }

}
