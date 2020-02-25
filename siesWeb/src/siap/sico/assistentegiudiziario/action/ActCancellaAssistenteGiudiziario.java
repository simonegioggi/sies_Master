package siap.sico.assistentegiudiziario.action;

/**
 * <p>Title: ActCancellaAssistenteGiudiziario</p>
 * <p>Description: Azione di Cancella Assistente Giudiziario</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActCancellaAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
  /**
   * Azione di cancellazione dell'Assistente
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    // viene istanziato il model e valorizzato l'Id
    AssistenteGiudiziarioModel lAss = new AssistenteGiudiziarioModel();
    lAss.setIdAssistenteGiudiziario(getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_GIUDIZIARIO));

    // chiama il controller
    IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
    lCtrl.ExCancellaAssistenteGiudiziario(lAss);


    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sico.assistentegiudiziario.action.ActLoadRicercaAssistenteGiudiziario" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}
