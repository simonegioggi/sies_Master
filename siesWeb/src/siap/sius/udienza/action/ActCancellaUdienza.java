package siap.sius.udienza.action;

/**
 * <p>Title: ActCancellaUdienza</p>
 * <p>Description: Azione Load del Cancella Udienza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaUdienza extends ActionSiap implements ICostantiUdienza
{
  /**
   * Azione di cancellazione dell'Udienza
   * @return Nome della pagina JSP su cui posizionarsi
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_UDIENZA);
    // riempie il model
    UdienzaModel lUdiMod = new UdienzaModel ();

    lUdiMod.setIdUdienza(new BigDecimal(lId));


    // chiama il controller
    IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
    lCtrl.ExCancellaUdienza(lUdiMod);


    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sius.udienza.action.ActLoadRicercaUdienza" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW
  }
}
