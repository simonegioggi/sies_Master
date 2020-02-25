package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;

import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActValidaPenaResiduaMA</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActValidaPenaResiduaMA extends ActMisuraAlternativa implements ICostantiMisuraAlternativa
{
  public String processRequest() throws F3BException
  {
     BigDecimal lidPena = this.getRequestBigDecimalParameter("ipenaresidua");

     PenaResiduaModel lPenMod = new PenaResiduaModel();
     IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
     lPenMod = lCtrl.ExRicercaPenaResiduaByKey(lidPena);

     lPenMod.setIdPenaResidua(lidPena);
     lPenMod.setFlagValidato("S");

     if(!this.isRequestParameterNullObj("GPV") && this.getRequestStringParameter("GPV") != null)
     {
       lPenMod.setDataFine(getRequestDateParameter("APV","MPV","GPV"));
     }

     lCtrl.ExAggiornaPenaResidua(lPenMod);

     return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "="
            + getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
  }
}