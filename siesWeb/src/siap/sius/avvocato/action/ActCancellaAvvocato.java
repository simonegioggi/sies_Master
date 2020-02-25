package siap.sius.avvocato.action;

/**
* Azione di Cancellazione  Avvocato
* @return Nome della pagina JSP di Inserisci avvocato
* al termine dell'elaborazione
* @throws F3BException
*/


import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
//import siap.sius.avvocato.controller.AvvocatoController;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActCancellaAvvocato extends ActionSiap implements ICostantiAvvocato
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(CAMPO_ID_AVVOCATO);


    // riempie il model
    AvvocatoModel lAvvMod = new AvvocatoModel ();
    lAvvMod.setIdAvvocato(new BigDecimal(lId));
    lAvvMod.setIdAvvocato( getRequestBigDecimalParameter( CAMPO_ID_AVVOCATO) );


    // chiama il controller
    //AvvocatoController lCtrl = new AvvocatoController();
    IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
    lCtrl.ExCancellaAvvocato(lAvvMod);


    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sius.avvocato.action.ActLoadInserisciAvvocato" );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW

     }
}