package siap.sige.circostanza.action;

import org.apache.log4j.Logger;

import siap.siep.circostanza.action.ActLoadDettaglioCircostanza;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;


/**
* <p>Title: ActLoadDettaglioCircostanzaSige</p>
* <p>Description: Classe Action per la load dettaglio di Circostanza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadDettaglioCircostanzaSige extends ActLoadDettaglioCircostanza
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
	   * della Circostanza. 
	   * @throws Exception
	   */
	  private void modificabilita() throws Exception 
	  {
		ActionSige lActSige = new ActionSige( this);
			
		// Modificabilità della Pena Accessoria 
		String lpenaSigeModificabile = "SI";
			
		if ( lActSige.IsFascicoloSigeModificabile())
		{
			lpenaSigeModificabile = "SI";
			if (mlCirMod != null )
			{
				if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mlCirMod.getCodUfficioInserimento()))
					lpenaSigeModificabile = "SI";
				else
					lpenaSigeModificabile = "NO";
			}
		}
		else
			lpenaSigeModificabile = "NO";
			
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("CircostanzaSigeModificabile : " + lpenaSigeModificabile);

		setRequestAttribute("Cancellabile", lpenaSigeModificabile);
		setRequestAttribute("Modificabile", lpenaSigeModificabile);
	  }	
}