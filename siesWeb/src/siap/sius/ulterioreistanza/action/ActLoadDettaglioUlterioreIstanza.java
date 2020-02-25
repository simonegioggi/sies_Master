package siap.sius.ulterioreistanza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.ulterioreistanza.controller.IUlterioreIstanza;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioUlterioreIstanza</p>
* <p>Description: Classe Action per la load dettaglio di UlterioreIstanza</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadDettaglioUlterioreIstanza extends ActionSius implements ICostantiUlterioreIstanza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Metdodo che esegue il recupero dei dati dal DBase e ritorna
   * il percorso della pagina di dettaglio.
   * <p>
   * @return Pagina di Dettaglio.
   * @throws Exception propaga errore di eccezione.
   */
  public String processRequest() throws Exception 
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    // Preleva dalla request l'id dell'ulteriore istanza da recuperare.
 		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_ULTERIORE_ISTANZA);
 		 
    // Chiama il Controller che si occupa di recupare i dati dell'ulteriore istanza.
		IUlterioreIstanza lCtrl = SIUSLookupRemote.getUlterioreIstanzaRemote();
		UlterioreIstanzaModel lUltMod = lCtrl.ExRicercaUlterioreIstanzaByKey(lId);
		// Imposta in request il model per la visualizzazione dei dati.
    setRequestAttribute("ulterioreistanza", lUltMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
		return PG_LOAD_DETTAGLIOULTERIOREISTANZA;
	}
}