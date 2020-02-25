package siap.siep.penaaccessoria.action;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadCancellaEsecuzionePA</p>
* <p>Description: Classe Action per la load ActLoadCancellaEsecuzionePA</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadCancellaEsecuzionePA extends ActionSiap implements ICostantiPenaAccessoria, ICostantiOrdineEsecuzione
{
public String processRequest() throws F3BException

  {
    // Controllo non effettuato nel caso di provvedimento SIUS
    if  (isRequestParameterNullObj("campoSIUS"))
        isFascicoloSiepDiCompetenza();
    else
      setRequestAttribute("campoSIUS", getRequestStringParameter("campoSIUS"));

    IOrdineEsecuzione lCtrlOrd = SIEPLookupRemote.getOrdineEsecuzioneRemote();

    CampoNotaModel lCampoMod=lCtrlOrd.ExRicercaEventoCampoNotaByIdEvento(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("camponota",lCampoMod);

    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    EventoModel lEveMod=lCtrlEv.ExRicercaEventoByKey(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("evento",lEveMod);
    this.setRequestAttribute("IdEvento",this.getRequestStringParameter("IdEvento"));

  return PG_INSERICI_MOTIVAZIONI_EVENTO;
}



}
