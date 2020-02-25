package siap.sige.penacomplessiva.action;


import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
/**
 * <p> Title: ActLoadDettaglioPenaCompSige</p>
* <p> Description: Classe Action cerca la Pena Complessiva 
*     legata ad un Titolo esecutivo associato ad un Procedimento SIGE 
*     e ne visualizza il dettaglio.
*  </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActLoadDettaglioPenaCompSige extends ActionSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	private DettaglioPenaComplessivaModel lDettMod = null;

  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");

	   gestioneRitorno();
	   
	   // Ricerca Pena Complessiva 
	   IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
	   BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	   lDettMod = lCtrl.ExRicercaPenaComplessivaCompletaByIdSIGE(lIdFasSigeSen);
	   
	   if (lDettMod != null)
		   setRequestAttribute("dettaglioPenaComplessiva", lDettMod);
	   
	   setRequestAttribute("modo", "SIGE");
	   
	   // Valutazione della possibilità di modifica
	   modificabilita();
	   
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");

      return ICostantiPenaComplessiva.PG_LOAD_DETTAGLIOPENACOMPLESSIVA;  //restituisce la jsp di VIEW
  }
  
  /**
   * La funzione valuta la possibbilità di modifica della Pena Complessiva. 
   * @throws Exception
   */
  private void modificabilita() throws Exception 
  {
		// Modificabilità della Pena Complessiva 
		String lpenaSigeModificabile = "NO";
		
		if ( IsFascicoloSigeModificabile() )
		{
			lpenaSigeModificabile = "SI";
			   if (lDettMod != null && lDettMod.getPenaComplessivaSanzioneSostitutiva() != null && lDettMod.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() != null )
			   {
				   if (getCodUfficioUtenteConnesso().equalsIgnoreCase(lDettMod.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva().getCodUfficioInserimento()))
						lpenaSigeModificabile = "SI";
				   else
					   lpenaSigeModificabile = "NO";
			   }
		}
		else
			lpenaSigeModificabile = "NO";
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("penaSigeModificabile : " + lpenaSigeModificabile);

		setRequestAttribute("penaSigeModificabile", lpenaSigeModificabile);

  }
}