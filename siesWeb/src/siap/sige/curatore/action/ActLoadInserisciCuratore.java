package siap.sige.curatore.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciCuratore</p>
 * <p>Description: Classe Action per la load inserisci di Curatore</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
*/
public class ActLoadInserisciCuratore extends ActionSiap 
implements ICostantiCuratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per l'inserimento del Curatore.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception propaga errore di eccezione.
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    // Inserire Eventuali ComboBOX
    Option lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute("elencoFlagStato", "" + lOption );

    // Imposta Modalità inserimento.
    setRequestAttribute("modalita", "I");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCICURATORE;  //restituisce la jsp di VIEW
  }
}