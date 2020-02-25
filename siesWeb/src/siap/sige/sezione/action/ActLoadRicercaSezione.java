package siap.sige.sezione.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaSezione</p>
* <p>Description: Classe Action per la load ricerca della Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadRicercaSezione extends ActionSiap implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per la ricerca di una Sezione.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");

    Option lOption = new Option( SezioneUtils.getElencoCodiciSezioni(), Option.BLANK_ITEM );
    setRequestAttribute("elencoCodiciSezioni", "" + lOption );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    return PG_LOAD_RICERCASEZIONE;  //restituisce la jsp di VIEW
  }
}