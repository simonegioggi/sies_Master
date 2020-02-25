package siap.sige.penacomplessiva.action;


import org.apache.log4j.Logger;

import siap.sico.lock.model.LockModel;
import siap.siep.penacomplessiva.action.ActLoadModificaPenaComplessiva;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActLoadModificaPenaCompSige</p>
* <p>Description: Classe Action visualizza la form Modifica Pena Complessiva.
*  </p>
*  L'action specializza   siap.siep.penacomplessiva.action.ActLoadModificaPenaComplessiva  
*  per poter usare la stessa form ma cambiando in questa l'azione successiva.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActLoadModificaPenaCompSige extends ActLoadModificaPenaComplessiva
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	  
	  gestioneRitorno();
	  
	   //Controllo che non si stia lavorando su una entità in modifica ad altri
	   LockModel lck =  lockIfNotLocked("pena complessiva", getRequestStringParameter(CAMPO_ID_PENA_COMPLESSIVA), getCodUtenteConnesso());
	   if (lck != null)
	   {
		   setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La "+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
	       return IWebConstants.PG_MESSAGE;
	   }
	      
	    // funzione ereditata dall'ancestor
	    preparazioneDati();  

	   setRequestAttribute("modo", "SIGE");
   
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");

      return PG_LOAD_INSERISCIPENACOMPLESSIVA;  //restituisce la jsp di VIEW

  }
}