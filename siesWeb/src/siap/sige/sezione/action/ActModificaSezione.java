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
* <p>Title: ActModificaSezione</p>
* <p>Description: Classe Action per la modifica del Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActModificaSezione extends ActionSiap implements ICostantiSezione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Modifica del Sezione.
* <p>
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* <p>
* @throws Exception
*/
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    // istanzia e riempie il model
    SezioneModel lSezMod = new SezioneModel();

    lSezMod.setIdSezione(getRequestBigDecimalParameter(CAMPO_ID_SEZIONE));
    lSezMod.setCodice(getRequestStringParameter(CAMPO_CODICE));
    lSezMod.setDescrizione(getRequestStringParameter(CAMPO_DESCRIZIONE));
    lSezMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lSezMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lSezMod.setDataAggiornamento(DateUtils.getSysDate());

    // chiama il controller
    ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
    SezioneModel lSezModRet = lCtrl.ExModificaSezione(lSezMod);

    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage( IWebConstants.PG_MAIN );
    lRedir.setAction( "siap.sige.sezione.action.ActLoadDettaglioSezione" );
    lRedir.setParameter(CAMPO_ID_SEZIONE, lSezModRet.getIdSezione().toString());
    
    String lPage = lRedir.toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

    return lPage;
  }
}