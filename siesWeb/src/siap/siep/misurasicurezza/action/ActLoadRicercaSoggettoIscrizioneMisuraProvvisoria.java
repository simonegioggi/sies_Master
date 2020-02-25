package siap.siep.misurasicurezza.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria</p>
 * <p>Description: Carica la pagina della Ricerca del Soggetto per 
 * <p>l'iscrizione del procedimento di Misura Sicurezza Provvisoria</p>
 * <p>Copyright: Agile Copyright (c) 2014</p>
 * <p>Company: Agile</p>
 * @version 8.2
 */

public class ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria extends ActionSiap implements ICostantiMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws F3BException
  {
      
	 // // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 // siesLogger.debug("--XX-- ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria - NumerazioneManuale is null = "+isRequestParameterNullObj("NumerazioneManuale"));
      if ( !isRequestParameterNullObj("NumerazioneManuale")  && "S".equals(getRequestStringParameter("NumerazioneManuale")))
      {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(" --XX-- ActLoadRicercaSoggettoIscrizioneMisuraProvvisoria - NumerazioneManuale = "+getRequestStringParameter("NumerazioneManuale"));
        setRequestAttribute("NumerazioneManuale", "S");
      }
      
      Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
      setRequestAttribute("nazioni", "" + lOption );

    return PG_LOAD_RICERCA_SOGG_MIS_SIC_PROVVISORIA; //restituisce la jsp di VIEW
  
  }
}