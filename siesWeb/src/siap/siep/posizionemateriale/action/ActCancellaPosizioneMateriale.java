package siap.siep.posizionemateriale.action;

import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.util.SIEPLookupRemote;


/**
* <p>Title: ActCancellaPosizioneMateriale</p>
* <p>Description: Classe Action per cancellare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaPosizioneMateriale extends ActionSiap implements ICostantiPosizioneMateriale
{
  public String processRequest() throws Exception
  {
    // Riempie il model che individuail record da cancellare
    PosizioneMaterialeModel lPosMatCanc = new PosizioneMaterialeModel();
    lPosMatCanc.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
    lPosMatCanc.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));

    // Cancellazione attraverso il controller
    IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
    lCtrl.ExCancellaPosizioneMateriale(lPosMatCanc);

    String retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
    return retPage;
  }
}