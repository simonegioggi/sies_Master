package siap.sige.magistrato.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.util.SezioneUtils;
import f3b.log.LogF3B;
import f3b.web.html.Option;
//import f3b.util.F3BException;

/**
* <p>Title: ActLoadInserisciMagistrato</p>
* <p>Description: Classe Action per la load inserisci Magistrato</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
*/
public class ActLoadInserisciMagistrato extends ActionSiap 
implements ICostantiMagistrato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");    

    super.gestioneRitorno();
    
    // Inserire Eventuali ComboBOX.
    Option lOption = new Option(DecodificheManager.getInstance().getFlagStato(), "P");
    setRequestAttribute("elencoFlagStato", "" + lOption );
   
    lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.NO_BLANK_ITEM);    
    setRequestAttribute("elencoSezioni", lOption.toString());
    
    //Imposta Modalità.
    setRequestAttribute("modalita", "I");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCIMAGISTRATO;  //Restituisce la jsp di VIEW
  }
}