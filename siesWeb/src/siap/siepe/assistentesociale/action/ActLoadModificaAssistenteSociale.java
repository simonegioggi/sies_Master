package siap.siepe.assistentesociale.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.assistentesociale.model.AssistenteSocialeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaAssistenteSociale</p>
* <p>Description: Classe Action per la load inserisci di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaAssistenteSociale extends ActionSiap implements ICostantiAssistenteSociale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    Option lOption;

    String lIdAssistenteSociale = getRequestStringParameter(CAMPO_ID_ASSISTENTE_SOCIALE);

    // Da Trovare una Posizione più coerente con l'architettura.
    LockModel lck = LockController.lockIfNotLocked(getServletContext() ,"AssistenteSociale" ,lIdAssistenteSociale ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il/la  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    // STUB: 2006-08-10 Controllo ... ma è necessario ?
    if (lIdAssistenteSociale.equals("0"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la modifica di questo record: Assistente Sociale di default.");

    // Chiama il controller
    IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
    AssistenteSocialeModel lAssSocMod = lCtrl.ExRicercaAssistenteSocialeByKey(getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_SOCIALE));

    // Inserire ComboBOX
    if(lAssSocMod.getFlagStato() != null)
      lOption = new Option( DecodificheManager.getInstance().getFlagStato(),lAssSocMod.getFlagStato());
    else
      lOption = new Option( DecodificheManager.getInstance().getFlagStato());

    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("assistentesociale", lAssSocMod);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return PG_LOAD_INSERISCIASSISTENTESOCIALE;  //Restituisce la jsp di VIEW
  }
}