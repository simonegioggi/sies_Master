package siap.siep.notifica.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioOmesaNotifica</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioOmesaNotifica extends ActionSiap implements ICostantiNotifica
{
 public String processRequest() throws F3BException
  {
    String lIdRinnovo = this.getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

    RinnovoModel lRinMod = new RinnovoModel();
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    lRinMod = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));

    VerbaleModel lVerMod = new VerbaleModel();
    IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();

    if(lRinMod != null && lRinMod.getIdRinnovo() != null && lRinMod.getVerIdVerbale()!= null)
    {
      lVerMod = lCtrlVer.ExRicercaVerbaleByKey(lRinMod.getVerIdVerbale());
    }

    setRequestAttribute("verbale",lVerMod);
    setRequestAttribute("rinnovo",lRinMod);


    this.setRequestAttribute("Notifica", getRequestStringParameter("Notifica"));
    this.setRequestAttribute("Ufficiali", getRequestStringParameter("Ufficiali"));
    this.setRequestAttribute("Rinnovo", getRequestStringParameter("Rinnovo"));

    return PG_DETTAGLIO_OMESSA_NOTIFICA;  //restituisce la jsp di VIEW
   }
}