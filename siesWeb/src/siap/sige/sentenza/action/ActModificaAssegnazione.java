package siap.sige.sentenza.action;

/**
 * <p>Title: ActModificaAssegnazione</p>
 * <p>Description: Classe Azione di modifica dell'assegnazione di una 
 * Sentenza ad un  Fascicolo SIGE. 
 * I dati modificabili sono "data di irrevocabilità" e "flag di competenza".
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */

import org.apache.log4j.Logger;

import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;


public class ActModificaAssegnazione extends ActionSige implements ICostantiFasSigeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Interfaccia del Controller Sentenza Sige
	private IFasSigeSentenza mCtrl = null;
	
  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	
	   // Viene istanziato il controller per attuare l'assegnazione della Sentenza al Fascicolo SIGE
	   mCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

	   FascicoloSigeModel lFascicolo = null;
	   SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();
	   
	  // I dati del Fascicolo Sige sono in sessione 
	   lFascicolo = getFascicoloSigeInSessione();
	  if (lFascicolo == null || lFascicolo.getIdFascicoloSige() == null)
		  throw new F3BException(F3BException.USER_MESSAGE, "Mancano i dati del Fascicolo in sessione !");
	  lFasSigeSen.setFasIdFascicoloSige(lFascicolo.getIdFascicoloSige());
  
	  // Lettura dalla request dell' ID del record da modificare
	  lFasSigeSen.setIdFasSigeSentenza(getRequestBigDecimalParameter(CAMPO_ID_FAS_SIGE_SENTENZA));
		  
	  // Valorizzazione dati di sistema
	  lFasSigeSen.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	  lFasSigeSen.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	  lFasSigeSen.setDataAggiornamento(DateUtils.getSysDate());
	  
	  // Lettura Data Irrevocabilità e Flag Competenza dalla request
	  lFasSigeSen.setDataIrrevocabilita( (getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA, CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA )));
	  if(isRequestChecked(CAMPO_FLAG_COMPETENZA))
		  lFasSigeSen.setFlagCompetenza("S");
	  else
		  lFasSigeSen.setFlagCompetenza("N");
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("FasSigeSentenza : " + lFasSigeSen);
		  
	  // Aggiornamento DB
	  lFasSigeSen = mCtrl.ExModificaFasSigeSentenza(lFasSigeSen);
	  
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");
	  
	   // Si ritorna indietro al Dettaglio dove è stato posto il punto di ritorno
	   return goToRitorno();

   }
    
  
}