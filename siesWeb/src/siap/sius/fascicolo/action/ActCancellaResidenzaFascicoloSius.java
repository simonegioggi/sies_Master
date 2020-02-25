package siap.sius.fascicolo.action;

import java.math.BigDecimal;

import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
/**
* <p>Title: ActCancellaResidenzaFascicoloSius</p>
* <p>Description: Classe Action per la cancellazione di un Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaResidenzaFascicoloSius extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    // Prelevo l' ID fascicolo Sius dalla sessione
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    // Chiama il controller
    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    lCtrl.ExCancellaResidenzaProcedimentoSius(getRequestBigDecimalParameter(ICostantiResidenza.CAMPO_ID_RESIDENZA),lIdFascicoloSius);
    return ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
   /*
    // Setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS+"="+lIdFascicoloSius );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  */
  }
}