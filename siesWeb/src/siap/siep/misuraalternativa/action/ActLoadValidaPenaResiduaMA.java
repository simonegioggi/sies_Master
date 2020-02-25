package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadValidaPenaResiduaMA</p>
 * <p>Description: Classe Action per la load Valida pena Residua</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadValidaPenaResiduaMA extends ActMisuraAlternativa implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
    BigDecimal lId = new BigDecimal(this.getRequestStringParameter("idpena"));

    PenaResiduaModel lPenMod = new PenaResiduaModel();
    IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
    lPenMod = lCtrl.ExRicercaPenaResiduaByKey(lId);

    setRequestAttribute("pena", lPenMod);

    return PG_VALIDA_PENA_RESIDUA;
  }
}