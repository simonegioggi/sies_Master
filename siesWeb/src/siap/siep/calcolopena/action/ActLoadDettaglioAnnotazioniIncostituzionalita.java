package siap.siep.calcolopena.action;

import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ActLoadDettaglioAnnotazioneManuale;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import f3b.web.IWebConstants;

public class ActLoadDettaglioAnnotazioniIncostituzionalita extends ActLoadDettaglioAnnotazioneManuale
                                                           implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    try
    {
      String lPageErr = loadDettaglio("0286", INCOSTITUZIONALITA, "INCOST");

      if (lPageErr != null)
        return lPageErr;
    }
    catch(SIEPException ex)
    {
      if(ex.getErrorCode() == SIEPException.EX_NOT_FOUND)
      {
        this.isEventoNonValidato();
        return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.calcolopena.action.ActLoadGEIncost";
      }
      else
        throw ex;
    }

    return PG_LOAD_DETTAGLIO_ANN_MANUALI;
  }
}