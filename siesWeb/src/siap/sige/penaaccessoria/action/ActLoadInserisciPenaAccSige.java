package siap.sige.penaaccessoria.action;


import org.apache.log4j.Logger;

import siap.siep.penaaccessoria.action.ActLoadInserisciPenaAccessoria;
import f3b.log.LogF3B;

/**
 * <p>Title: ActLoadInserisciPenaCompSige</p>
* <p>Description: Classe Action visualizza la form di "Inserimento Pena Accessoria".
*  </p>
*  L'action specializza  siap.siep.penaaccessoria.action.ActLoadInserisciPenaAccessoria  
*  per poter usare la stessa form (jsp) ma cambiando in questa l'azione successiva, 
*  per fare ciò viene passato nella request il flag modo = SIGE che viene utilizzato nella jsp.
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @author luigi
* @version 1.0
 */

public class ActLoadInserisciPenaAccSige extends ActLoadInserisciPenaAccessoria
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");

	   gestioneRitorno();

	   setRequestAttribute("modo", "SIGE");
   
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");

      return preparazioneForm();  //restituisce la jsp di VIEW

  }
}