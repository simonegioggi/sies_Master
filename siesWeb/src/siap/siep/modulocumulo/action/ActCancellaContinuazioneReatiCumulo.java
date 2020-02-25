package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.ReatoContinuazioneCumuloController;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActCancellaContinuazioneReatiCumulo</p>
 * <p>Description: Classe Action per la Cancellazione dei Reati in Continuazione </p>
 * <p>      legato al titolo Cumulato </p>
 */

public class ActCancellaContinuazioneReatiCumulo extends ActionModuloCumulo implements ICostantiReatoCumulo
{
  public String processRequest() throws Exception
  {
    
    BigDecimal idTitolo = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);      

    
    ReatoCumuloModel lReato = new ReatoCumuloModel();
    
    lReato.setTitIdTitoloCumulato     (idTitolo);
    
    //n.b. la jsp dovrebbe passare direttamente il campo ID_CONTINUAZIONE_REATO_CUM
    //     ma non è un dato disponibile per cui passa il  CAMPO_PROGR_REATO
    //     e il campo ID_CONTINUAZIONE_REATO_CUM va ricavato in fase di update
    lReato.setProgrReato          (getRequestBigDecimalParameter(CAMPO_PROGR_REATO));
    lReato.setProgrCircostanza    (new BigDecimal(1));
    
    ReatoContinuazioneCumuloController lReaCtrl = new ReatoContinuazioneCumuloController();
    lReaCtrl.ExCancellazioneContinuazioneReati(lReato);
    
    
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";
    return lPage;
  }
}
