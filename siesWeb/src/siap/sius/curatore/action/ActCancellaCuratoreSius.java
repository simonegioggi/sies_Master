package siap.sius.curatore.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.curatore.action.ICostantiCuratore;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
/**
* <p>Title: ActCancellaCuratoreSius</p>
* <p>Description: Classe Action per la cancellazione di un Curatore Sius</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/
public class ActCancellaCuratoreSius extends ActionSiap implements ICostantiCuratoreSius, ICostantiCuratore
{

  public String processRequest() throws Exception
  {
    // Prelevo l' ID fascicolo Sius dalla sessione
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFascicoloSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    // Chiama il controller
    ICuratoreSius lCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
    lCtrl.ExCancellaCuratoreSius(getRequestBigDecimalParameter(ICostantiCuratore.CAMPO_ID_CURATORE), lIdFascicoloSius);
    String retPage = ritornoDopoCancellazione("Cancellazione Curatore avvenuta correttamente!", "siap.sius.curatore.action.ActRicercaCuratoreSius");
    return retPage;
  }
}