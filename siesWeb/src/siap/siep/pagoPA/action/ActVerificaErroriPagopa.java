package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.IErroriSiesPagopa;
import siap.siep.pagoPA.model.ErroriSiesPagopaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActVerificaErroriPagopa extends ActionSiap implements ICostantiErroriSiesPagopa {
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws Exception {

    siesLogger.debug(getClass().getName() + ".processRequest: inizio");
    ErroriSiesPagopaModel lCriteriRicerca = new ErroriSiesPagopaModel();
    
    UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    siesLogger.debug("User Profile Id = "+lUtenteMod.getUserProfile().getProfileId());
    
    if ("90".equals(lUtenteMod.getUserProfile().getProfileId().toString())) {
      // Amministratore di ufficio ha i filtri di ricerca
      // Data Iniziale
      if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_DAL)
          && !isRequestParameterNullObj(CAMPO_MESE_DATA_DAL)
          && !isRequestParameterNullObj(CAMPO_ANNO_DATA_DAL)) {
        lCriteriRicerca.setDataInserimento (getRequestDateParameter(CAMPO_ANNO_DATA_DAL, CAMPO_MESE_DATA_DAL, CAMPO_GIORNO_DATA_DAL));
      }
  
      // Data Finale
      if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_AL)
          && !isRequestParameterNullObj(CAMPO_MESE_DATA_AL)
          && !isRequestParameterNullObj(CAMPO_ANNO_DATA_AL)) {
        lCriteriRicerca.setDataInserimentoAl (getRequestDateParameter(CAMPO_ANNO_DATA_AL, CAMPO_MESE_DATA_AL, CAMPO_GIORNO_DATA_AL));
      }
      
      String lTipoUtente = getRequestStringParameter(CAMPO_TIPO_UTENTE);
      if (CAMPO_TIPO_UTENTE_CODICE.equals(lTipoUtente)){
        //FIXME Testare che l'utente sia dell'ufficio
        lCriteriRicerca.setCodUtente(getRequestStringParameter(CAMPO_COD_UTENTE).trim());
      }
    } else {
      siesLogger.debug("Utente non amministratore...");
      lCriteriRicerca.setCodUtente(getCodUtenteConnesso());
    }
    
    String lPagina = "1";
    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
      lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);
    
    IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();
    
    // Recupero il numero totale di record
    BigDecimal lCountRisultati;
    if (isRequestParameterNullObj("CountRisultati")) {
      Vector <ErroriSiesPagopaModel> lListaErrori = lCtrl.ExRicercaErroriSiesPagopaByCriteria (lCriteriRicerca, 0);
      lCountRisultati = new BigDecimal(lListaErrori.size());
    } else {
      lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
    }
    
    Vector <ErroriSiesPagopaModel> lListaErrori = lCtrl.ExRicercaErroriSiesPagopaByCriteria (lCriteriRicerca, Integer.parseInt(lPagina));
    setRequestAttribute("ListaErrori", lListaErrori);

    setRequestAttribute("CriteriRicerca", lCriteriRicerca);

    // Passo i dati per gestire la paginazione
    setRequestAttribute("CountRisultati", lCountRisultati);
    setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
    setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
    
    return ICostantiErroriSiesPagopa.PG_ESITO_VERIFICA_ERRORI;
  }
}
