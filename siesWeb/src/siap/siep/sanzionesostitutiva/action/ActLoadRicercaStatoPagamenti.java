package siap.siep.sanzionesostitutiva.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;

/**
 * MEV_2023-33: 
 * 
 * @author df
 * @version 1.0
 */
public class ActLoadRicercaStatoPagamenti extends ActionSiap implements ICostantiSanzioneSostitutiva
{
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws Exception
	{
		siesLogger.debug("ActLoadRicercaStatoPagamenti....");
		
		this.setLinkRitorno();    
		
		if (this.isSessionAttributeNullObj("fascicolo")) {
			setRequestAttribute("fascicoloNotInSession", "S");
		}		
	    
		return PG_LOAD_RICERCA_STATO_PAGAMENTI; 
	}
}
