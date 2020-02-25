package siap.siep.nuovaistanza.action;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadCancellaInoltroNuovaIstanza</p>
* <p>Description: Classe Action per la load ActLoadCancellaInoltroNuovaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadCancellaInoltroNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
public String processRequest() throws F3BException

  {
	FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	if (lFascMod.getCodStatoFascicolo().compareTo("01")==0)
	  	throw new F3BException(F3BException.USER_MESSAGE, "Attenzione : Il fascicolo indicato è già archiviato! Impossibile procedere.");

	
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
    // 10/04/2006 il parametro nextAction consente di prestabilire l'azione da eseguire
    // alla fine dell'attività di revoca/cancellazione provvedimento.
    if  (!isRequestParameterNullObj("nextAction"))
      setRequestAttribute("nextAction", getRequestStringParameter("nextAction"));
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("DENTRO ActLoadCancellaInoltroNuovaIstanza");
    if(!this.isRequestParameterNullObj("nextAction"))
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("nextAction : " + getRequestStringParameter("nextAction"));
    
	

    setRequestAttribute("IdIstanza", getRequestStringParameter("IdIstanza"));
    setRequestAttribute("TipoProvvedimento", getRequestStringParameter("TipoProvvedimento"));

  return IWebConstants.ROOT_DIR + "files/siap/siep/nuovaistanza/LoadCancellazioneInoltro.jsp";
}



}