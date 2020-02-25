package siap.siep.notifica.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioSollecitiVerbVaneRicerche</p>
* <p>Description: Classe Action per la load dettaglio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioSollecitiVerbVaneRicerche extends ActionSiap implements ICostantiNotifica
{
 public String processRequest() throws F3BException
  {
    String lIdRinnovo = this.getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

    RinnovoModel lRinMod = new RinnovoModel();
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    lRinMod = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));

    setRequestAttribute("rinnovo",lRinMod);
    setRequestAttribute("Notifica", getRequestStringParameter("Notifica"));

    return PG_DETTAGLIO_SOLLECITI_VANE_RICERCHE;  //restituisce la jsp di VIEW
   }
}