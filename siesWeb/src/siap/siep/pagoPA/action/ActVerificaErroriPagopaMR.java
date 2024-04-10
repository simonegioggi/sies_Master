package siap.siep.pagoPA.action;


import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;


/**
 * Clase di comodo per registrare sul menu rapido dell'utente ufficio l'icona di aggancio della 
 * ActVerificaErroriPagopa che è abilitata anche all'utente Amministratore di ufficio che non deve vederlas
 * 
 */
public class ActVerificaErroriPagopaMR extends ActionSiap implements ICostantiErroriSiesPagopa {
  
  public String processRequest() throws Exception {
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
        + "=siap.siep.pagoPA.action.ActVerificaErroriPagopa";
    return lPage;
  }

}
