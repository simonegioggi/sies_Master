package siap.sius.fascicolo.action;

/**
 * <p>Title: ActLoadInserisciResidenzaFascicoloSius</p>
 * <p>Description: Classe Action per la load inserisci della residenza fascicolo sius</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.residenza.model.ResidenzaAssociataModel;
//import siap.sico.web.ActionSiap;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

public class ActLoadInserisciResidenzaFascicoloSius extends ActionSius implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    // Attivazione punto di Ritorno
    setLinkRitorno();

    if (this.IsFascicoloSiusModificabile()==false)
       throw new SIUSException(SIUSException.USER_MESSAGE,ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    ResidenzaAssociataModel lResAss = lCtrl.ExRicercaResidenzaFascicoloSiusCorrente(lIdFascicolo);
    setRequestAttribute("residenzaassociata", lResAss);

    setRequestAttribute("modalita", "I");

    //Per default ITALIA (039)
    String lCodStato = "039";
    if (  lResAss != null && lResAss.getResidenza() != null &&  lResAss.getResidenza().getCodStato().length() > 0)
    lCodStato = lResAss.getResidenza().getCodStato();
      Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), lCodStato);
    setRequestAttribute("nazioni", "" + lOption );

    return PG_LOAD_INSERISCRESIDENZAFASCICOLOSIUS;  //restituisce la jsp di VIEW
  }
}