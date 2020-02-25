package siap.sige.sezione.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadRicercaSezioneLista</p>
* <p>Description: Classe Action per la load ricerca della Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadRicercaSezioneLista extends ActionSiap implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    return PG_LOAD_RICERCA_SEZIONE_LISTA;  //restituisce la jsp di VIEW
  }
}
