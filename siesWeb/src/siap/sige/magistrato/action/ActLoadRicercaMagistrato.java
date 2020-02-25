package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaMagistrato</p>
* <p>Description: Classe Action per la load ricerca di Magistrato ambito sige</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company:Eutelia S.p.A.</p>
*/
public class ActLoadRicercaMagistrato extends ActionSiap 
implements ICostantiMagistrato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws F3BException 
	{
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
	  return PG_LOAD_RICERCAMAGISTRATO;  //restituisce la jsp di VIEW 
	}
}