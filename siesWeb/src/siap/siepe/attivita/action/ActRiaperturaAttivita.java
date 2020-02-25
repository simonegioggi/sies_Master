package siap.siepe.attivita.action;


/**
* <p>Title: ActChiusuraAttivita</p>
* <p>Description: Classe Action per la chiusura dell'Attivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActRiaperturaAttivita extends ActionSiap implements ICostantiAttivita
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
/**
* Azione di Riapertura dell'Attivita
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
  public String processRequest() throws F3BException
  {
    AttivitaModel lAttMod = new AttivitaModel();

    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"ATTIVITA",getRequestStringParameter(CAMPO_ID_ATTIVITA),getCodUtenteConnesso(),getSession().getId());

    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L' "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    // Per riaprire una Attività è sufficiente annullare la data di chiusura
    lAttMod.setDataChiusura( null );
    //Lettura ID
    lAttMod.setIdAttivita(getRequestBigDecimalParameter(CAMPO_ID_ATTIVITA));
   
    // Imposta i dati relativi all'aggiornamento
    lAttMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lAttMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lAttMod.setDataAggiornamento(DateUtils.getSysDate());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(" Attività da riaprire ->" +lAttMod);
    // Si richiama la funzione di Chiusura 
    IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
    lCtrl.ExChiusuraAttivita(lAttMod);
    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siepe.attivita.action.ActLoadDettaglioAttivita&"+CAMPO_ID_ATTIVITA+"="+lAttMod.getIdAttivita().toString();

    return lPage;
 }
}