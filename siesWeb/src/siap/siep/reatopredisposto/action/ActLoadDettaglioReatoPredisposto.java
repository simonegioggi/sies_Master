package siap.siep.reatopredisposto.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.reatopredisposto.controller.ReatoPredispostoController;
import siap.siep.reatopredisposto.model.ReatoPredispostoModel;

/**
 * <p>Title: ActLoadDettaglioReato</p>
 * <p>Description: Classe Action per la load dettaglio di Reato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadDettaglioReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto
{
  public String processRequest() throws Exception
  {
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_REATO_PREDISPOSTO);

//    IReato lCtrl = SIEPLookupRemote.getReatoRemote();
    ReatoPredispostoController lCtrl = new ReatoPredispostoController();    
    ReatoPredispostoModel lReaMod = lCtrl.ExRicercaReatoPredispostoByKey(lId);

    setRequestAttribute("reatopredisposto", lReaMod);
    setSessionAttribute("reatopredisposto", lReaMod);

    return PG_LOAD_DETTAGLIOREATOPREDISPOSTO;
  }
}