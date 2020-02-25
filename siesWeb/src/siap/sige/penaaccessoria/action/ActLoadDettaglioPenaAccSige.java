package siap.sige.penaaccessoria.action;

import org.apache.log4j.Logger;

import siap.siep.penaaccessoria.action.ActLoadDettaglioPenaAccessoria;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;


/**
* <p>Title: ActLoadDettaglioPenaAccSige</p>
* <p>Description: Classe Action per la load dettaglio di PenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioPenaAccSige extends ActLoadDettaglioPenaAccessoria
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
	   * della Pena Accessoria. 
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
			if (mPenMod != null )
			{
				if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mPenMod.getCodUfficioInserimento()))
					lpenaSigeModificabile = "SI";
				else
					lpenaSigeModificabile = "NO";
			}
		}
		else
			lpenaSigeModificabile = "NO";
			
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("penaSigeModificabile : " + lpenaSigeModificabile);

		setRequestAttribute("Cancellabile", lpenaSigeModificabile);
		setRequestAttribute("Modificabile", lpenaSigeModificabile);
	  }	
}