package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;

public class ActLoadRicercaScadenzarioStatoPagamentiPP extends ActionSiap implements ICostantiScadenzario {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public String processRequest() throws Exception {
    
    if (this.isSessionAttributeNullObj("fascicolo")) {
      // return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }

    // pagina di ritorno
    return PG_LOAD_RICERCA_SCADENZARI_PP;
  }    
    
}
