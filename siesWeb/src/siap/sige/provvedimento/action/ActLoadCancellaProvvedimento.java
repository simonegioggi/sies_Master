package siap.sige.provvedimento.action;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadCancellaProvvedimento</p>
* <p>Description: Classe Action per la load ActLoadCancellaProvvedimento</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/

public class ActLoadCancellaProvvedimento extends ActionSige implements ICostantiProvvedimentoSige
{
public String processRequest() throws F3BException

  {
    IOrdineEsecuzione lCtrlOrd = SIEPLookupRemote.getOrdineEsecuzioneRemote();

    CampoNotaModel lCampoMod=lCtrlOrd.ExRicercaEventoCampoNotaByIdEvento(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("camponota",lCampoMod);

    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    EventoModel lEveMod=lCtrlEv.ExRicercaEventoByKey(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("evento",lEveMod);

    this.setRequestAttribute("IdEvento",this.getRequestStringParameter("IdEvento"));
    // Il parametro nextAction consente di prestabilire l'azione da eseguire
    // alla fine dell'attività di revoca/cancellazione provvedimento.
    if  (!isRequestParameterNullObj("nextAction"))
      setRequestAttribute("nextAction", getRequestStringParameter("nextAction"));

    if(!isRequestParameterNullObj("lOrdinamento"))
      setRequestAttribute("lOrdinamento", getRequestStringParameter("lOrdinamento"));

  return PG_LOAD_CANCELLA_PROVVEDIMENTO;
}



}
