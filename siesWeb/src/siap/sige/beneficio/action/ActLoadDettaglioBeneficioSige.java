package siap.sige.beneficio.action;

import siap.siep.beneficio.action.ActLoadDettaglioBeneficio;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActLoadDettaglioBeneficioSige</p>
* <p>Description: Classe Action per la load dettaglio di Beneficio</p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadDettaglioBeneficioSige extends ActLoadDettaglioBeneficio
{

	public String processRequest() throws Exception 
	{
		gestioneRitorno();
		String lRetPage = super.processRequest();
		
		if(isBeneficioIndulto)
		{
			// Nel caso si tratti di un Beneficio tipo Indulto l'azione viene switchata ad ActLoadDettaglioBeneficioIndultoSige
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage( IWebConstants.PG_MAIN );
			lRedirigi.setAction( "siap.sige.beneficio.action.ActLoadDettaglioBeneficioIndultoSige" );
			lRedirigi.setParameter(CAMPO_ID_BENEFICIO,getRequestStringParameter(CAMPO_ID_BENEFICIO) );
			lRetPage =  lRedirigi.toString();  
		}
		else
		{
			setRequestAttribute("modo", "SIGE");
			modificabilita();
		}
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