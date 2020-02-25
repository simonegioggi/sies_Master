package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActCancellaContinuazioneCumulo</p>
 * <p>Description: Azione di Cancellazione Continuazione</p>
 * <p>		in ambito Cumulo (Continuazione_Cumulo) </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IContinuazioneCumulo;
import siap.siep.modulocumulo.model.ContinuazioneCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;

public class ActCancellaContinuazioneCumulo extends ActionModuloCumulo implements ICostantiContinuazioneCumulo
{
  /**
   * Azione di cancellazione ContinuazioneCumulo
   * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
   * @throws Exception
   */
	
  public String processRequest() throws Exception
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();   
    
    // riempie il model
    ContinuazioneCumuloModel lMod = new ContinuazioneCumuloModel();

    lMod.setIdContinuazioneCum(getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE_CUM));

    IContinuazioneCumulo lCtrl = SIEPLookupRemote.getContinuazioneCumuloRemote();
    lCtrl.ExCancellaContinuazioneCum(lMod);

    BigDecimal lIdTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
    
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+lIdTitolo.toString();

    return lPage;
  }
}
