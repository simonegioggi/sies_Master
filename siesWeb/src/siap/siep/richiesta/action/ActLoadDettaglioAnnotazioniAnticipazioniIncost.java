package siap.siep.richiesta.action;

import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActLoadDettaglioAnticipazioni;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadDettaglioAnnotazioniAnticipazioniIncost extends ActLoadDettaglioAnticipazioni
                                                            implements ICostantiAnnotazioneManuale
{
  public String processRequest() throws Exception
  {
    try
    {
      String lPageErr = loadDettaglio("0211", INCOSTITUZIONALITA, "RICH_INCOST");

      if (lPageErr != null)
        return lPageErr;
    }
    catch(F3BException ex)
    {
      if(ex.getErrorCode() == SIEPException.EX_NOT_FOUND)
      {
        this.isEventoNonValidato();
        return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaIncostituzionalita";
      }
      else
        throw ex;
    }

    return PG_LOAD_DETTAGLIO_ANTICIPAZIONI;
  }
}