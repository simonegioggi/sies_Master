package siap.sige.sentenza.action;

/**
 * <p>Title: ActAssegnazione</p>
 * <p>Description: Classe Azione di assegnazione di una 
 * Sentenza ad un  Fascicolo SIGE.
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */
import org.apache.log4j.Logger;

import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActAssegnazione extends ActionSige implements ICostantiFasSigeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Interfaccia del Controller Sentenza Sige
	private IFasSigeSentenza mCtrl = null;
	
  public String processRequest() throws F3BException
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
  
	 // ID della Sentenza nella request 
	  lFasSigeSen.setIdSentenza(getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA));
	  
	  // Eventuale ID Fascicolo SIEP nella request
	  if (!isRequestParameterNullObj(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP))
		  lFasSigeSen.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP));
		  
	  // Valorizzazione dati di sistema
	  lFasSigeSen.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	  lFasSigeSen.setCodOperatoreInserimento(getCodUtenteConnesso());
	  lFasSigeSen.setDataInserimento(DateUtils.getSysDate());
	  lFasSigeSen.setCodUfficioAggiornamento(lFasSigeSen.getCodUfficioInserimento());
	  lFasSigeSen.setCodOperatoreAggiornamento(lFasSigeSen.getCodOperatoreInserimento());
	  lFasSigeSen.setDataAggiornamento(lFasSigeSen.getDataInserimento());
	  
	  // Lettura Data Irrevocabilità e Flag Competenza dalla request
	  lFasSigeSen.setDataIrrevocabilita( (getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA, CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA )));
	  if(isRequestChecked(CAMPO_FLAG_COMPETENZA))
		  lFasSigeSen.setFlagCompetenza("S");
	  else
		  lFasSigeSen.setFlagCompetenza("N");
		  
	  
	  lFasSigeSen = mCtrl.ExAssegnaSentenzaFascicoloSige(lFasSigeSen);
	  
	  //Prepara la "pagina" di destinazione
	  RedirectTo lRedirigi = new RedirectTo();
	  lRedirigi.setPage( IWebConstants.PG_MAIN );
	  lRedirigi.setAction( "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo" );
	  lRedirigi.setParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE,lFascicolo.getIdFascicoloSige().toString() );
	  
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");
	  return lRedirigi.toString();  
   }
    
  
}