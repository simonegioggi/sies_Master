package siap.sige.sezione.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciSezione</p>
* <p>Description: Classe Action per l'inserimento della SEzione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActInserisciSezione extends ActionSiap 
implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Effettua Inserimento della Sezione.
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
    
    SezioneModel lSezMod = new SezioneModel();

    lSezMod.setCodice(getRequestStringParameter( CAMPO_CODICE).toUpperCase() );
    lSezMod.setDescrizione(getRequestStringParameter( CAMPO_DESCRIZIONE ).toUpperCase() );
    lSezMod.setCodUfficioAppartenenza( getCodUfficioUtenteConnesso());
    lSezMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lSezMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lSezMod.setDataInserimento(DateUtils.getSysDate());
    // Chiama il controller.
    ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
    SezioneModel lSezModRet = lCtrl.ExInserisciSezione(lSezMod);		 // setta la risposta nella request

    setRequestAttribute("sezione", lSezModRet);
    
    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage( IWebConstants.PG_MAIN );
    lRedir.setAction( "siap.sige.sezione.action.ActLoadDettaglioSezione" );
    lRedir.setParameter(CAMPO_ID_SEZIONE, lSezModRet.getIdSezione().toString() );
    
    String lPage = lRedir.toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return lPage;
  }
}