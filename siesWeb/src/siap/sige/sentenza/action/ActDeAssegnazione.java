package siap.sige.sentenza.action;

/**
 * <p>Title: ActDeAssegnazione</p>
 * <p>Description: Classe Azione per la De-Assegnazione 
 * di una Sentenza legata ad un  Fascicolo SIGE.
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;

public class ActDeAssegnazione extends ActionSige implements ICostantiFasSigeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws Exception
  {
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	// Interfaccia del Controller Sentenza Sige
	IFasSigeSentenza lCtrl = null;
	
	// Si richiama il lock 
	lockApplicativo("Sentenza");
		
	// Viene istanziato il controller per attuare l'assegnazione della Sentenza al Fascicolo SIGE
	lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

	// Lettura dalla request dell' ID del record da cancellare in FAS_SIGE_SENTENZA
	BigDecimal lIdFasSigeSen = getRequestBigDecimalParameter(CAMPO_ID_FAS_SIGE_SENTENZA);
	lCtrl.ExDeAssegnaSentenzaFascicoloSige(lIdFasSigeSen);
	  
	// Ritorno al punto di partenza
	String lRetPage = ritornoDopoCancellazione("Sentenza De-Assegnata dal Procedimento", null);
		 
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : fine");

	return lRetPage;
  }
    
  
}