package siap.sius.statistiche.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;


/**
 * <p>Title: ActStampaElencoProc </p>
 * <p>Description: Attiva la Ricerca dei Procedimenti per stamparne l'elenco.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eunics</p>
 * @author Luigi
 * @version 2.0
 */

public class ActStampaElencoProc
extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: inizio " );
    
    // Si sfrutta il punto di ritorno memorizzato per riattivare
    // la Action di Ricerca Notifiche SIUS
    // con tutti i parametri di filtro preimpostati.
    String lRetPage = goToRitorno();
    
    lRetPage += "&Stampa=" + "SI";

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "" + getClass().getName() + " .processRequest: fine " );

    return lRetPage;
  }
}