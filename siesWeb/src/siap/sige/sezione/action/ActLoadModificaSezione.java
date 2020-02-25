package siap.sige.sezione.action;


import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadModificaSezione</p>
* <p>Description: Classe Action per la load modifica della Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadModificaSezione extends ActionSiap 
implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  /**
  * Carica la form di modifica di una Sezione.
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
    
    String lIdSezione = getRequestStringParameter(CAMPO_ID_SEZIONE);

    // Lock
    LockModel lck = 
      LockController.lockIfNotLocked(getServletContext() ,"Sezione" ,lIdSezione ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'" + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }
    
    // chiama il controller
    ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
    SezioneModel lSezMod = lCtrl.ExRicercaSezioneByKey(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("sezione", lSezMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCISEZIONE;  //restituisce la jsp di VIEW
  }
  

}