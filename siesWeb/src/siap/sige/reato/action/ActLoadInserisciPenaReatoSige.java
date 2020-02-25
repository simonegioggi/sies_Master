package siap.sige.reato.action;

import org.apache.log4j.Logger;

import siap.sico.lock.model.LockModel;
import siap.siep.reato.action.ActLoadInserisciPenaReato;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadInserisciPenaReatoSige</p>
* <p>Description: Classe Action per la load inserisci di PosizioneGiuridica</p>
* L'azione richiama  ActLoadInserisciPenaReato.processRequest ed in più passa nella request un flag che 
* indica la modalità di Pena Reato x SIGE.
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciPenaReatoSige extends ActLoadInserisciPenaReato 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
 * Azione di Load Inserisci Pena Reato
 * @return Nome della pagina JSP su cui posizionarsi
 * al termine dell'elaborazione
 * @throws Exception
 */
  public String processRequest() throws Exception
  {
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	
    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck = lockIfNotLocked("reato", getRequestStringParameter(CAMPO_ID_REATO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }

	gestioneRitorno();
	
	setRequestAttribute("modo", "SIGE");
    return super.processRequest();
  }
}