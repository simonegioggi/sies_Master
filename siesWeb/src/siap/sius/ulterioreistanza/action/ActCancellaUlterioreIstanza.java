package siap.sius.ulterioreistanza.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActCancellaUlterioreIstanza</p>
* <p>Description: Classe Action per cancellare l'ulteriore istanza </p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull S.p.A.</p>
* @version 1.0
*/
public class ActCancellaUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Metodo che si occupa di eseguire la cancellazione di una
   * ulteriore istanza.
   * <p>
   * @return String ritorna la pagina di destinazione. 
   */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    // Inizializza il model per impostare l'id di cancellazione.
    UlterioreIstanzaModel lUltIst = new UlterioreIstanzaModel();
    lUltIst.setIdUlterioreIstanza( getRequestBigDecimalParameter( CAMPO_ID_ULTERIORE_ISTANZA ) );
    
    // Chiama il controller ed esegue la cancellazione, della Ulteriore Istanza.
    IUlterioreIstanza lUltIstCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
    lUltIstCtrl.ExCancellaUlterioreIstanza( lUltIst );

    String retPage = null;
    // Il metodo invocato ritorna la pagina di destinazione dopo la cancellazione.
    retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return retPage; // Pagina di ritorno.
  }
}