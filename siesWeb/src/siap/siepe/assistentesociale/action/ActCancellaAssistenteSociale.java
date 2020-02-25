package siap.siepe.assistentesociale.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.assistentesociale.controller.IAssistenteSociale;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaAssistenteSociale</p>
* <p>Description: Classe Action per la cancellazione di un Assistente Sociale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaAssistenteSociale extends ActionSiap implements ICostantiAssistenteSociale
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    BigDecimal lIdAssistenteSociale = getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_SOCIALE);

    // Verifica se l'id selezionato sia pari a 0
    if (lIdAssistenteSociale.compareTo(new BigDecimal("0")) == 0 )
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la cancellazione di questo record: Assistente Sociale di default.");

    // Chiama il controller ed esegue la cancellazione dell'assistente selezionato
    IAssistenteSociale lCtrl = SIEPELookupRemote.getAssistenteSocialeRemote();
    lCtrl.ExCancellaAssistenteSociale(lIdAssistenteSociale);

    // Setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    // Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();

    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siepe.assistentesociale.action.ActLoadRicercaAssistenteSociale" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}