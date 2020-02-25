package siap.siep.sentenza.action;

/**
 * <p>Title: ActLoadCancellaSoggetto</p>
 * <p>Description: Azione Load del Cancella Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
//import siap.sico.soggetto.controller.SoggettoController;

public class ActLoadCancellaSentenza extends ActionSiap implements ICostantiSentenza
{
  /**
   * Azione di cancellazione del Soggetto
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    // riempie il model
    SentenzaModel senmod = new SentenzaModel();

    senmod.setIdSentenza(getRequestBigDecimalParameter(CAMPO_ID_SENTENZA));
    
    // chiama il controller
 
    ISentenza lSenCtrl = SIEPLookupRemote.getSentenzaRemote();
    
    try{
    lSenCtrl.ExCancellaSentenza(senmod);
    }catch(Exception ex)
    {
    	  setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Impossibile cancellare la sentenza. <br>Esistono dei procedimenti collegati.");
     	  return IWebConstants.PG_MESSAGE;
    }

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.sentenza.action.ActLoadRicercaSentenza" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}
