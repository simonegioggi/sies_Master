package siap.siep.calcolopena.action;

/**
 * <p>Title: ActInserisciAnnotazioniManualiMC</p>
 * <p>Description: Classe Action per l'inserimento di Circostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import f3b.web.IWebConstants;

public class ActInserisciAnnotazioniManualiMC extends ActInserisciAnnotazioniManualiComputo
                                              implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    String lPageErr = eseguiRichiesta(STESSO_TITOLO, "0121");

    if (lPageErr != null)
      return lPageErr;

    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniStessoTitolo";

    return lPage;
  }
}