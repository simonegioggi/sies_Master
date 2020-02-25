package siap.sige.collegio.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
* <p>Title: ActLoadDettaglioCollegio</p>
* <p>Description: Classe Action per la load dettaglio della Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class ActLoadDettaglioCollegio extends ActionSiap implements ICostantiCollegio
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    setLinkRitorno();
    
    // Chiama il controller.
    ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
    CollegioModel lColMod = 
      lCtrl.ExRicercaCollegioByKey(getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO));

    //
    // Decisione del ruolo magistrato in virtù del tipo ufficio.
    //
    String lRuoloMagistrato = "Giudice";
    
    if( getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP")    ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP")  ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAPSM")  ||
        getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIBM")  )
      lRuoloMagistrato = "Consigliere";      
    
    
    // Passaggio alla request.
    setRequestAttribute("collegio", lColMod);
    setRequestAttribute("ruoloMagistrato", lRuoloMagistrato);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_DETTAGLIOCOLLEGIO;
  }
}