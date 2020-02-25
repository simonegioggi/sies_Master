package siap.sige.beneficio.action;

import siap.siep.beneficio.action.ActLoadDettaglioBeneficioIndulto;
import siap.sige.web.ActionSige;


/**
* <p>Title: ActLoadDettaglioBeneficioSige</p>
* <p>Description: Classe Action per la load dettaglio di Beneficio</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadDettaglioBeneficioIndultoSige extends ActLoadDettaglioBeneficioIndulto
{

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
			if (mBenMod != null )
			{
				if (getCodUfficioUtenteConnesso().equalsIgnoreCase(mBenMod.getCodUfficioInserimento()))
					lModificabile = "SI";
				else
					lModificabile = "NO";
			}
		}
		else
			lModificabile = "NO";
			

		setRequestAttribute("Cancellabile", lModificabile);
		setRequestAttribute("Modificabile", lModificabile);
	  }	
}