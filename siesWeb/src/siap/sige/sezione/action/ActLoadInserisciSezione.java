package siap.sige.sezione.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciSezione</p>
 * <p>Description: Classe Action per la load inserisci della Sezione</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
*/
public class ActLoadInserisciSezione extends ActionSiap 
implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per l'inserimento della Sezione.
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
    
    Option lOption = new Option( SezioneUtils.getElencoCodiciSezioni());
    setRequestAttribute("elencoCodiciSezioni", "" + lOption );
    // Imposta Modalità inserimento.
    setRequestAttribute("modalita", "I");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCISEZIONE;  //restituisce la jsp di VIEW
  }  
}