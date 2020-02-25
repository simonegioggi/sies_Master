package siap.sige.reato.action;

import org.apache.log4j.Logger;

import siap.siep.reato.action.ICostantiReato;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioPenaReatoSige</p>
 * <p>Description: Classe Action per la load dettaglio di Pena Reato</p>
 * La classe specializza ActLoadDettaglioPenaReatoSige per poter richiamare la processRequest() di questa poichè la ricerca è la stessa (ricerca reato).
 * La jsp è invece quella ereditata dall'analoga funzione SIEP.
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioPenaReatoSige extends ActDettaglioReatoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	
	gestioneRitorno();
	
	preparaDati();
	
    if(mReaMod != null && !mReaMod.isPenaReatoInserita())
        throw new F3BException(F3BException.USER_MESSAGE, "Pena Reato non presente");

	
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : fine");
   
    return ICostantiReato.PG_LOAD_DETTAGLIOPENAREATO;
  }
}