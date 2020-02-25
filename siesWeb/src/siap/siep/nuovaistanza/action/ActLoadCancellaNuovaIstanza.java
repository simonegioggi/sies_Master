package siap.siep.nuovaistanza.action;

import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadCancellaNuovaIstanza</p>
* <p>Description: Classe Action per la load Cancella Nuova Istanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile S.r.l.</p>
* @version 5.0
*/

public class ActLoadCancellaNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza
{

 public String processRequest() throws F3BException
  {
    INuovaIstanza lCtrlISt = SIEPLookupRemote.getNuovaIstanzaRemote();
    NuovaIstanzaModel lIstaMod =lCtrlISt.ExRicercaNuovaIstanzaById(this.getRequestBigDecimalParameter(CAMPO_ID_NUOVA_ISTANZA));
    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    if (lCtrlEv.ExRicercaEventoInoltroDispPM(lIstaMod.getEveIdEvento())) 
    	throw new F3BException(F3BException.USER_MESSAGE, "Impossibile annullare l'istanza. Sono presenti Inoltri al PM o Disposizioni PM!");
    
    this.setRequestAttribute("istanza",lIstaMod);

    ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
    CampoNotaModel lCampNotMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lIstaMod.getEveIdEvento());
    this.setRequestAttribute("camponota",lCampNotMod);

    if(!this.isRequestParameterNullObj(IWebConstants.GOTO_PAGE))
      setRequestAttribute(IWebConstants.GOTO_PAGE, ""+this.getRequestStringParameter(IWebConstants.GOTO_PAGE));

    return PG_INSERICI_MOTIVAZIONI_ISTANZA;
 }
}