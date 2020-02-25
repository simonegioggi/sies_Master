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
public class ActCancellaDomicilioFascicoloSius extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    // Prelevo l' ID fascicolo Sius dalla sessione
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    // Chiama il controller
    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    lCtrl.ExCancellaDomicilioProcedimentoSius(getRequestBigDecimalParameter(ICostantiResidenza.CAMPO_ID_RESIDENZA),lIdFascicoloSius);

    return ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);

  }
}