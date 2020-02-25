package siap.sige.beneficio.action;

import siap.siep.beneficio.action.ActLoadModificaBeneficio;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActLoadModificaBeneficioSige</p>
* <p>Description: Classe Action per la creazione e visualizzazione della form 
* <p> di modifica per un beneficio.
* <p> Poichè la funzione è analoga a quella di SIEP, viene ereditata la funzione 
* <p> corrispondente aggiungendo però  l'attributo modo = "SIGE" per poter specializzare 
* <p> la jsp utilizzata  sia nel caso SIGE che SIEP.
* </p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadModificaBeneficioSige extends ActLoadModificaBeneficio 
{
   public String processRequest() throws F3BException
  {
	   String lPage = preparaDatiForm();
	   
		if(isBeneficioIndulto)
		{
			// Nel caso si tratti di un Beneficio tipo Indulto l'azione viene switchata ad ActLoadModificaBeneficioIndulto
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage( IWebConstants.PG_MAIN );
			lRedirigi.setAction( "siap.sige.beneficio.action.ActLoadModificaBeneficioIndulto" );
			lRedirigi.setParameter(CAMPO_ID_BENEFICIO,getRequestStringParameter(CAMPO_ID_BENEFICIO) );
			lPage =  lRedirigi.toString();  
		}
	   
	   setRequestAttribute("modo", "SIGE");
	   return lPage;  //restituisce la jsp di VIEW
  }
 
}