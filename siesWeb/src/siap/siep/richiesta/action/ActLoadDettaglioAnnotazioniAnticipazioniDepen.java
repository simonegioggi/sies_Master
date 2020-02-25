package siap.siep.richiesta.action;

import org.apache.log4j.Logger;

import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.calcolopena.action.ActLoadDettaglioAnticipazioni;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadDettaglioAnnotazioniAnticipazioniDepen extends ActLoadDettaglioAnticipazioni
                                                              implements ICostantiAnnotazioneManuale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // pagina di ritorno
    String lPage = PG_LOAD_DETTAGLIO_ANTICIPAZIONI;

    try
    {
      String lPageErr = loadDettaglio("0210", DEPENALIZZAZIONE, "RICH_DEPEN");

      if (lPageErr != null)
        lPage = lPageErr;
    }
    catch(F3BException ex)
    {
      if(ex.getErrorCode() == SIEPException.EX_NOT_FOUND)
      {
        this.isEventoNonValidato();
        lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.richiesta.action.ActLoadRichiestaDepenalizzazione";
      }
      else
        throw ex;
    }
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + " Pagina di ritorno ->" + lPage);

    return lPage;
  }
}