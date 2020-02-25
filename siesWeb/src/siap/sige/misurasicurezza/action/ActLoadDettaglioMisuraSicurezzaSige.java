package siap.sige.misurasicurezza.action;

import org.apache.log4j.Logger;

import siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;


/**
* <p>Title: ActLoadDettaglioMisuraSicurezzaSige</p>
* <p>Description: Classe Action per la load dettaglio di Misura di Sicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadDettaglioMisuraSicurezzaSige extends ActLoadDettaglioMisuraSicurezza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception 
	{
		gestioneRitorno();
		String lRetPage = super.processRequest();
		setRequestAttribute("modo", "SIGE");
		modificabilita();
		return lRetPage;
	 }
	
	
	  
	  /**
	   * La funzione valuta la possibbilità di modifica 
	   * @throws Exception
	   */
	  private void modificabilita() throws Exception 
	  {
		ActionSige lActSige = new ActionSige( this);
			
		// Modificabilità della Misura di Sicurezza 
		String lModificabile = "SI";
			
		if ( lActSige.IsFascicoloSigeModificabile())
		{
			lModificabile = "SI";
			if (mlMisMod != null )
			{
				if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mlMisMod.getCodUfficioInserimento()))
					lModificabile = "SI";
				else
					lModificabile = "NO";
			}
		}
		else
			lModificabile = "NO";
			
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("penaSigeModificabile : " + lModificabile);

		setRequestAttribute("Cancellabile", lModificabile);
		setRequestAttribute("Modificabile", lModificabile);
	  }	
}