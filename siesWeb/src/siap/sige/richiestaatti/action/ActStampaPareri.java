package siap.sige.richiestaatti.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.produzioneatti.action.ICostantiProduzioneAtti;
import f3b.log.LogF3B;


/**
 * <p>Title: ActdRicercaPareri </p>
 * <p>Description: Attiva la Ricerca delle richieste di parere.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActStampaPareri
extends ActionSius implements ICostantiProduzioneAtti
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "AA1" + getClass().getName() + " .processRequest: inizio " );
    // Si sfrutta il punto di ritorno memorizzato per riattivare
    // la Action di Ricerca Pareri
    // con tutti i parametri di filtro preimpostati.

    String lRetPage = goToRitorno();
    lRetPage += "&Stampa=" + "SI";

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "AA2" + getClass().getName() + " .processRequest: fine " );
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info( "AA2" + lRetPage );

    return lRetPage;
  }
}