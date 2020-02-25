package siap.siepe.richiesta.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActCancellaRichiesta</p>
* <p>Description: Classe Action per cancellare la richiesta</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaRichiesta extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // Chiama il controller ed esegue la cancellazione
    IRichiesta lRichCtrl = SIEPELookupRemote.getRichiestaRemote();
    lRichCtrl.ExCancellaRichiesta(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA));

    String retPage = null;
    retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return retPage; // Pagina di ritorno.
  }
}