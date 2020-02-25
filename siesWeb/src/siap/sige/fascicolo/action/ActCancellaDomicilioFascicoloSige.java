package siap.sige.fascicolo.action;

import java.math.BigDecimal;

import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
/**
* <p>Title: ActCancellaDomicilioFascicoloSige</p>
* <p>Description: Classe Action per la cancellazione di Domicilio Fascicolo SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/
public class ActCancellaDomicilioFascicoloSige extends ActionSiap implements ICostantiFascicoloSige
{
  public String processRequest() throws Exception
  {
    // Prelevo l' ID fascicolo Sies dalla sessione
	FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    BigDecimal lIdFascicoloSige = lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige();

    // Chiama il controller
    IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    lCtrl.ExCancellaDomicilioProcedimentoSige(getRequestBigDecimalParameter(ICostantiResidenza.CAMPO_ID_RESIDENZA),lIdFascicoloSige);
    return ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
   /*
    // Setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Cancellazione Avvenuta Correttamente!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&"+ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE+"="+lIdFascicoloSige );
    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE; //restituisce la jsp di VIEW  }
  */
  }
}