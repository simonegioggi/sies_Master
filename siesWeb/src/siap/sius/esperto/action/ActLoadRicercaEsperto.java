package siap.sius.esperto.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaEsperto</p>
* <p>Description: Classe Action per la load ricerca di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRicercaEsperto extends ActionSiap implements ICostantiEsperto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
  * Carica la form per la ricerca di un Esperto.
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

    // 20081222 - Inibizione funzione per tipo uffici in ambito SIGE
    if( getUfficioUtenteConnesso().getCodTipoUfficio().equals("GIP") ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS") || 
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIB") )
      throw new F3BException( F3BException.USER_MESSAGE, "Funzione inibita per il tipo ufficio di competenza.");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    return PG_LOAD_RICERCAESPERTO;  //restituisce la jsp di VIEW
  }
}