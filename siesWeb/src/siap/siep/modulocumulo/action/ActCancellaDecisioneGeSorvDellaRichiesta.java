package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.siep.modulocumulo.controller.IRichiestePmInCumulo;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 *  Classe Action per la cancellazione della decisione del G.E. o della SORV. modificando la relativa sezione
 *  di una generica Richiesta del P.M.
 * 
 * @author Intersistemi Italia S.p.A.
 *
 */
public class ActCancellaDecisioneGeSorvDellaRichiesta extends ActionModuloCumulo implements ICostantiRichiestePmInCumulo
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
  public String processRequest() throws F3BException 
  {
	  siesLogger.debug("--XX-- >>>> Start  ActCancellaDecisioneGeSorvDellaRichiesta....");
	  
	  BigDecimal aIdRich = getRequestBigDecimalParameter( CAMPO_ID_RICHIESTE_PM_IN_CUMULO);
	  String aNextAction = getRequestStringParameter( "nextAction");
    
      
	  // Cancellazione
	  IRichiestePmInCumulo ICtrlRic = SIEPLookupRemote.getRichiestePmInCumuloRemote();
	  ICtrlRic.ExCancellaDecisioneDellaRichiesta(aIdRich);
	  String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +"="+aNextAction;

	  //siesLogger.debug("--XX-- >>>> lPage = "+lPage);
	  
	  return lPage;
  }
  
}	// Chiude classe 

