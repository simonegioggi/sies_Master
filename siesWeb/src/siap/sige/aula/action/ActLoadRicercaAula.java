package siap.sige.aula.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaAula</p>
* <p>Description: Classe Action per la load ricerca dell'Aula</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering</p>
* @version 1.0
*/
public class ActLoadRicercaAula extends ActionSiap implements ICostantiAula
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per la ricerca di un'Aula.
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

    Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    setRequestAttribute("elencoSezioni", "" + lOption );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_RICERCA_AULA;  //restituisce la jsp di VIEW
  }
}