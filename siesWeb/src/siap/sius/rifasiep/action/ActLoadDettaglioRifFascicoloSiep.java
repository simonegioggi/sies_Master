package siap.sius.rifasiep.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;


/**
* <p>Title: ActLoadDettaglioRifFascicoloSiep</p>
* <p>Description: Classe Action per la load dettaglio di Udienza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioRifFascicoloSiep extends ActionSiap implements ICostantiRifFascicoloSiep
{
  public String processRequest() throws Exception
  {
    this.gestioneRitorno();

    String lId = getRequestStringParameter(CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP);
    // riempie il model
    // chiama il controller

    IRiferimentoFascicoloSiep lCtrl = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
    RiferimentoFascicoloSiepModel lRFSMod = lCtrl.ExRicercaRiferimentoFascicoloSiepByKey(new BigDecimal(lId));
    setRequestAttribute("RiferimentoFascicoloSiep", lRFSMod);

    return PG_LOAD_DETTAGLIORIFASIEP;
  }

}