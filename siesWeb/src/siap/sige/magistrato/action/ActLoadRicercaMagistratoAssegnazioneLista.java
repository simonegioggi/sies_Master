package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaMagistratoAssegnazioneLista</p>
* <p>Description: Classe Action per la load ricerca del Magistrato Assegnatario</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/


public class ActLoadRicercaMagistratoAssegnazioneLista extends ActionSiap 
implements siap.sige.magistrato.action.ICostantiMagistrato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);    
	  setRequestAttribute("elencoSezioni", lOption.toString());
	  
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
	  
	  return PG_LOAD_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA;  //restituisce la jsp di VIEW
  }
}