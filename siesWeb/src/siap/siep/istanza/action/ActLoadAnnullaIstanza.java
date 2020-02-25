package siap.siep.istanza.action;

import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadAnnullaIstanza</p>
* <p>Description: Classe Action per la load ActLoadAnnullaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadAnnullaIstanza extends ActionSiap implements ICostantiIstanza
{
 public String processRequest() throws F3BException
  {
    IIstanza lCtrlISt = SIEPLookupRemote.getIstanzaRemote();
    IstanzaModel lIstaMod =lCtrlISt.ExRicercaIstanzaByKey(this.getRequestBigDecimalParameter(ICostantiIstanza.CAMPO_ID_ISTANZA));
    this.setRequestAttribute("istanza",lIstaMod);

    ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
    CampoNotaModel lCampNotMod = lCtrlCam.ExRicercaCampoNotaByKey(lIstaMod.getCamIdCampoNote());
    this.setRequestAttribute("camponota",lCampNotMod);

    if(!this.isRequestParameterNullObj(IWebConstants.GOTO_PAGE))
      setRequestAttribute(IWebConstants.GOTO_PAGE, ""+this.getRequestStringParameter(IWebConstants.GOTO_PAGE));

    return PG_INSERICI_MOTIVAZIONI_ISTANZA;
 }
}