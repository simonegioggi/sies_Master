package siap.siep.calcolopena.action;

/**
* <p>Title: ActInserisciAnnotazioniManualiCompSenzaTitolo</p>
* <p>Description: Classe Action per l'inserimento di un computo Custodia Cautelare
*    sofferta per altro reato (fungibilità)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import f3b.web.IWebConstants;

public class ActInserisciAnnotazioniManualiCompSenzaTitolo extends ActInserisciAnnotazioniManualiComputo
                                                           implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    String lPageErr = eseguiRichiesta(SENZA_TITOLO, "0213");

    if (lPageErr != null)
      return lPageErr;

    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadDettaglioAnnotazioniSenzaTitolo";

    return lPage;
  }
}