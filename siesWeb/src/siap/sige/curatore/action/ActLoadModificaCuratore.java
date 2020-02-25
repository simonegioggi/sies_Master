package siap.sige.curatore.action;

//import per le combo
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaCuratore</p>
* <p>Description: Classe Action per la load inserisci di Curatore</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadModificaCuratore extends ActionSiap 
implements ICostantiCuratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

  /**
  * Carica la form di modifica di un Curatore.
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
    
    String lIdCuratore = getRequestStringParameter(CAMPO_ID_CURATORE);

    // Lock
    LockModel lck = 
      LockController.lockIfNotLocked(getServletContext() ,"Curatore" ,lIdCuratore ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'" + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    if (lIdCuratore.equals("0"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la modifica di questo record: Curatore di default.");

    // chiama il controller
    ICuratore lCtrl = SIGELookupRemote.getCuratoreRemote();
    CuratoreModel lCurMod = lCtrl.ExRicercaCuratoreByKey(getRequestBigDecimalParameter(CAMPO_ID_CURATORE));

    // Inserire  ComboBOX
    Option lOption;
    if(lCurMod.getFlagStato() != null)
      lOption = new Option( DecodificheManager.getInstance().getFlagStato(),lCurMod.getFlagStato());
    else
      lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("curatore", lCurMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCICURATORE;  //restituisce la jsp di VIEW
  }
}