package siap.sius.fascicolo.action;

/**
 * <p>Title: ActLoadInserisciDomicilioFascicoloSius</p>
 * <p>Description: Classe Action per la load inserisci del domicilio fascicolo sius</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

public class ActLoadInserisciDomicilioFascicoloSius extends ActionSiap implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    ResidenzaAssociataModel lResAss = lCtrl.ExRicercaResidenzaFascicoloSiusCorrente(lIdFascicolo);
    setRequestAttribute("residenzaassociata", lResAss);

    setRequestAttribute("modalita", "I");

    //Per default ITALIA (039)
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "039");
    setRequestAttribute("nazioni", "" + lOption );

    return PG_LOAD_INSERISCDOMICILIOFASCICOLOSIUS;  //restituisce la jsp di VIEW
  }
}