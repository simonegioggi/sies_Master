package siap.siep.cumulo.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioCumulo</p>
* <p>Description: Classe Action per la load dettaglio di Cumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @deprecated - Classe mai utilizzata (01/12/2007)
* @version 1.0
*/

public class ActLoadDettaglioCumulo extends ActionSiap implements ICostantiCumulo
{
  public String processRequest() throws F3BException {
    String lId = getRequestStringParameter(CAMPO_ID_CUMULO);
  
    ICumulo lCtrl = SIEPLookupRemote.getCumuloRemote();
    CumuloModel llCumMod = lCtrl.ExRicercaCumuloByKey(new BigDecimal(lId));
    setRequestAttribute("cumulo", llCumMod);
  
   return PG_LOAD_DETTAGLIOCUMULO;
  }
}