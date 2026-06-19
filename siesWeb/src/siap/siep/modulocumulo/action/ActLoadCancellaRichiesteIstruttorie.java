package siap.siep.modulocumulo.action;

import org.apache.log4j.Logger;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadCancellaRichiesteIstruttorie</p>
* <p>Description: Classe Action per la load ActLoadCancellaRichiesteIstruttorie di Istruttoria / Richiesta </p>
* <p>				legati ad una Istruttoria CUMULO	</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadCancellaRichiesteIstruttorie extends ActionSiap implements ICostantiModuloCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
public String processRequest() throws F3BException

  {
	//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.debug(" --XX-- ActLoadCancellaRichiesteIstruttorie - Inizio");

    isFascicoloSiepDiCompetenza();
    
    IOrdineEsecuzione lCtrlOrd = SIEPLookupRemote.getOrdineEsecuzioneRemote();
    CampoNotaModel lCampoMod=lCtrlOrd.ExRicercaEventoCampoNotaByIdEvento(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("camponota",lCampoMod);

    IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
    EventoModel lEveMod=lCtrlEv.ExRicercaEventoByKey(this.getRequestBigDecimalParameter("IdEvento"));
    this.setRequestAttribute("evento",lEveMod);

    this.setRequestAttribute("IdEvento",this.getRequestStringParameter("IdEvento"));
    this.setRequestAttribute("IdIstruCum", this.getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
    this.setRequestAttribute("nextAction", "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteIstruttorie");

    return PG_LOAD_CANCELLA_RICH_ISTRUTTORIE;
  }

}
