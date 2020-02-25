package siap.sico.soggetto.action;

/**
 * <p>Title: ActLoadCancellaSoggetto</p>
 * <p>Description: Azione Load del Cancella Soggetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
//import siap.sico.soggetto.controller.SoggettoController;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadCancellaSoggetto extends ActionSiap implements ICostantiSoggetto
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
    SoggettoModel sogmod = new SoggettoModel();

    sogmod.setIdSoggetto(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO));

    // chiama il controller
    //SoggettoController lSogCtrl = new SoggettoController();
    ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
    lSogCtrl.ExCancellaSoggetto(sogmod);

    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sico.soggetto.action.ActLoadRicercaSoggetto" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}
