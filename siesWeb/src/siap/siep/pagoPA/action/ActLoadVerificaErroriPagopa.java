package siap.siep.pagoPA.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;

public class ActLoadVerificaErroriPagopa extends ActionSiap implements ICostantiErroriSiesPagopa {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");
    
    UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    
    return ICostantiErroriSiesPagopa.PG_LOAD_VERIFICA_ERRORI;
  }
}
