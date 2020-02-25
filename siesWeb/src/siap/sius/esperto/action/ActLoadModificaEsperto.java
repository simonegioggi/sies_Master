package siap.sius.esperto.action;

//import per le combo
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaEsperto</p>
* <p>Description: Classe Action per la load inserisci di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaEsperto extends ActionSiap implements ICostantiEsperto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

/**
  * Carica la form di modifica di un'Esperto.
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
    
    String lIdEsperto = getRequestStringParameter(CAMPO_ID_ESPERTO);

    // Lock
    LockModel lck = 
      LockController.lockIfNotLocked(getServletContext() ,"Esperto" ,lIdEsperto ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'" + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    if (lIdEsperto.equals("0"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la modifica di questo record: Esperto di default.");

    // chiama il controller
    IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
    EspertoModel lEspMod = lCtrl.ExRicercaEspertoByKey(getRequestBigDecimalParameter(CAMPO_ID_ESPERTO));

    // Inserire  ComboBOX
    Option lOption;
    if(lEspMod.getFlagStato() != null)
      lOption = new Option( DecodificheManager.getInstance().getFlagStato(),lEspMod.getFlagStato());
    else
      lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("esperto", lEspMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
 
	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Modifica di un Esperto da Funzioni Amministrative.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);
    
    return PG_LOAD_INSERISCIESPERTO;  //restituisce la jsp di VIEW
  }
}