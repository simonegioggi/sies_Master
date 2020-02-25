package siap.siep.posizionemateriale.action;

import siap.sico.web.ActionSiap;
import siap.siep.posizionemateriale.controller.IPosizioneMateriale;
import siap.siep.posizionemateriale.model.PosizioneMaterialeModel;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascicoliModel;
import siap.siep.util.SIEPLookupRemote;

/**
* <p>Title: ActLoadDettaglioPosizioneMateriale</p>
* <p>Description: Classe Action per la load dettaglio di PosizioneMateriale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioPosizioneMateriale extends ActionSiap 
                                                implements ICostantiPosizioneMateriale
{
  public String processRequest() throws Exception
  {
    gestioneRitorno();

    // Riempie il model di ricerca
    PosizioneMaterialeModel lPosModRic = new PosizioneMaterialeModel();
    lPosModRic.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
    lPosModRic.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));

    // Ricerca Posizione Materiale
    IPosizioneMateriale lCtrl = SIEPLookupRemote.getPosizioneMaterialeRemote();
    //Vector lPosizioni = lCtrl.ExRicercaPosizioneMateriale(lPosModRic);
    //PosizioneMaterialeModel lPosMod = (PosizioneMaterialeModel) lPosizioni.get(0);
    PosizioneMaterialeFascicoliModel lPosFasc = lCtrl.ExRicercaPosizioneMaterialeFascicoli(lPosModRic);
    
    setRequestAttribute("fascicoliposizionemateriale", lPosFasc);

    return PG_LOAD_DETTAGLIOPOSIZIONEMATERIALE;
  }
}