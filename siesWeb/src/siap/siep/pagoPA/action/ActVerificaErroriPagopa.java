package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.SICOException;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.controller.IUtente;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
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
		siesLogger.debug("User Profile Id = " + lUtenteMod.getUserProfile().getProfileId());

		if ("90".equals(lUtenteMod.getUserProfile().getProfileId().toString())) {
		  
		  // se non miarrivano sulla request i campi di ricerca vuol dire che sono di ritorno dalla rilancio della funzione.
		  // I criteri li prendo dalla sessione e rieseguo l'ultima ricerca.
		  // Altrimenti li prendo dalla requesta
		  if (isRequestParameterNullObj(CAMPO_GIORNO_DATA_DAL)) {
		    lCriteriRicerca = (ErroriSiesPagopaModel) getSessionAttribute(ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_OBJ);
        siesLogger.debug("Recupero i criteri dalla sessione: "+lCriteriRicerca); 
		  } else {
		    siesLogger.debug("Recupero i criteri dalla request...");
  			// Amministratore di ufficio ha i filtri di ricerca
  			// Data Iniziale
  			if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_DAL)
  					&& !isRequestParameterNullObj(CAMPO_MESE_DATA_DAL)
  					&& !isRequestParameterNullObj(CAMPO_ANNO_DATA_DAL)) {
  				lCriteriRicerca.setDataInserimento(getRequestDateParameter(CAMPO_ANNO_DATA_DAL,
  						CAMPO_MESE_DATA_DAL, CAMPO_GIORNO_DATA_DAL));
  			}
  
  			// Data Finale
  			if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_AL)
  					&& !isRequestParameterNullObj(CAMPO_MESE_DATA_AL)
  					&& !isRequestParameterNullObj(CAMPO_ANNO_DATA_AL)) {
  				lCriteriRicerca.setDataInserimentoAl(getRequestDateParameter(CAMPO_ANNO_DATA_AL,
  						CAMPO_MESE_DATA_AL, CAMPO_GIORNO_DATA_AL));
  			}

  			String lTipoUtente = getRequestStringParameter(CAMPO_TIPO_UTENTE);
  			if (CAMPO_TIPO_UTENTE_CODICE.equals(lTipoUtente)) {
          String lUtente = getRequestStringParameter(CAMPO_COD_UTENTE).trim();
          IUtente lCtrlUtente = SICOLookupRemote.getUtenteRemote();
          UtenteModel lUteRicerca = lCtrlUtente.ExRicercaUtenteByKey(lUtente);
          if (lUteRicerca==null){
            // Utente inesistete
            throw new SICOException(SICOException.USER_MESSAGE,"L'Utente "+lUtente+" non esiste");
          }
          else if (!lUteRicerca.getUfficioUtente().getCodUfficio().equals(getCodUfficioUtenteConnesso())){
            // Utente non appartenente all'ufficio 
            throw new SICOException(SICOException.USER_MESSAGE,"L'Utente "+lUtente+" non appartiene all'uffcio");
          }
          lCriteriRicerca.setCodUtente (lUtente);
  			}
			
        if (!"-".equals(getRequestStringParameter(CAMPO_TIPO_EVENTO))){
          String lAzionecContestoJava = getRequestStringParameter(CAMPO_TIPO_EVENTO);
          lCriteriRicerca.setAzioneContestoJava(lAzionecContestoJava);
          if (lAzionecContestoJava.equals(DESC_ACTION_RICHIESTA))
            lCriteriRicerca.setDescrizioneFunzione(DESC_FUNZIONE_RICHIESTA);
          else if (lAzionecContestoJava.equals(DESC_ACTION_VERIFICA))
            lCriteriRicerca.setDescrizioneFunzione(DESC_FUNZIONE_VERIFICA);
          else 
            lCriteriRicerca.setDescrizioneFunzione("-");
        }
        
        setSessionAttribute(ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_OBJ, lCriteriRicerca);
      }
		} else {
			siesLogger.debug("Utente non amministratore...");
			lCriteriRicerca.setCodUtente(getCodUtenteConnesso());
		}

		String lPagina = "1";
    if (   "90".equals(lUtenteMod.getUserProfile().getProfileId().toString())
        && isRequestParameterNullObj(CAMPO_GIORNO_DATA_DAL)) 
    {
      // Sono di ritorno dalla funzione, recupero la pagina dallla sessione
      lPagina = (String) getSessionAttribute (ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_CURRENT_PAGE);
    }		
    else if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IErroriSiesPagopa lCtrl = SIEPLookupRemote.getErroriSiesPagopaRemote();

		// Recupero il numero totale di record
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			Vector<ErroriSiesPagopaModel> lListaErrori = lCtrl
					.ExRicercaErroriSiesPagopaByCriteria(lCriteriRicerca, 0);
			lCountRisultati = new BigDecimal(lListaErrori.size());
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		Vector<ErroriSiesPagopaModel> lListaErrori = lCtrl
				.ExRicercaErroriSiesPagopaByCriteria(lCriteriRicerca, Integer.parseInt(lPagina));
		
		if (lListaErrori.size()==0 && Integer.parseInt(lPagina)>1) {
		  // ho cancellato l'ultimo record ella pagina e la sto ricaricando ma non ci sono più record.
		  // In questo caso carico la pagina precedente
		  lPagina = ""+(Integer.parseInt(lPagina)-1);
	    lListaErrori = lCtrl
	        .ExRicercaErroriSiesPagopaByCriteria(lCriteriRicerca, Integer.parseInt(lPagina));
		}
		setRequestAttribute("ListaErrori", lListaErrori);

		setRequestAttribute("CriteriRicerca", lCriteriRicerca);

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		if ("90".equals(lUtenteMod.getUserProfile().getProfileId().toString())) {
		  setSessionAttribute(ICostantiErroriSiesPagopa.CRITERI_RICERCA_SESSION_CURRENT_PAGE, lPagina);
		  // Se amministratore potrei essere di ritorno della funzione in errore. In questo caso NON ho i criteri di ricerca
		  // sulla request per cui devo rigenerarli
		  setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getRequestForPaging(lCriteriRicerca));
		}
		
		return ICostantiErroriSiesPagopa.PG_ESITO_VERIFICA_ERRORI;
	}

	
	private String getRequestForPaging (ErroriSiesPagopaModel lCriteriRicerca)  {
	  String request = "";
    request+="Main.jsp?"+IWebConstants.ACTION_FIELD+"=siap.siep.pagoPA.action.ActVerificaErroriPagopa";
	  
	  request+="&"+CAMPO_GIORNO_DATA_DAL+"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimento(),"dd"),"");
    request+="&"+CAMPO_MESE_DATA_DAL  +"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimento(),"MM"),"");
    request+="&"+CAMPO_ANNO_DATA_DAL  +"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimento(),"yyyy"),"");
	  
    request+="&"+CAMPO_GIORNO_DATA_AL+"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimentoAl(),"dd"),"");
    request+="&"+CAMPO_MESE_DATA_AL  +"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimentoAl(),"MM"),"");
    request+="&"+CAMPO_ANNO_DATA_AL  +"="+StringUtils.toStringJSP(DateUtils.getDateToString(lCriteriRicerca.getDataInserimentoAl(),"yyyy"),"");

    if (lCriteriRicerca.getCodUtente()!=null && lCriteriRicerca.getCodUtente().length()>0){
      request+="&"+CAMPO_TIPO_UTENTE  +"="+CAMPO_TIPO_UTENTE_CODICE;
      request+="&"+CAMPO_COD_UTENTE  +"="+StringUtils.toStringJSP(lCriteriRicerca.getCodUtente(),"");
    }
    else 
      request+="&"+CAMPO_TIPO_UTENTE  +"="+CAMPO_TIPO_UTENTE_TUTTI;
    
    if (lCriteriRicerca.getAzioneContestoJava()!=null && lCriteriRicerca.getAzioneContestoJava().length()>1)
      request+="&"+CAMPO_TIPO_EVENTO  +"="+StringUtils.toStringJSP(lCriteriRicerca.getAzioneContestoJava(),"");
    else 
      request+="&"+CAMPO_TIPO_EVENTO  +"=-";
     
    
	  return request;
	}
}