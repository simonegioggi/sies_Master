package siap.sige.reato.action;


/**
* <p>Title: ActInserisciUlterioriReatiSige</p>
* <p>Description: Classe Action per l'inserimento di Reato</p>
* La classe viene ottenuta specializzando ActInserisciUlterioriReati, 
* poichè i dati da leggere dalla form sono gli stessi si eredita da quella classe la funzione di lettura dati, 
* l'unica cosa che cambia è la funzione di aggiornamento dei dati: ExInserisciUlterioriReatiSige. 
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.siep.reato.action.ActInserisciUlterioriReati;
import siap.siep.reato.controller.IReato;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.log.LogF3B;
import f3b.util.F3BException;

public class ActInserisciUlterioriReatiSige extends ActInserisciUlterioriReati 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	
  /**
  * Azione di Inserimento del Reato
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
 {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");

	   // Viene chiamata la funzione ereditata per leggere i dati dalla form
	   letturaDati(null);
	   
	   // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	   BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	  
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug("ID_FAS_SISGE_SENTENZA : " + lIdFasSigeSen);
	   
	   if(mReati == null || mReati.size() == 0)
	        throw new F3BException( F3BException.USER_MESSAGE, "Specificare almeno un reato!" );

	   IReato lCtrl = SIEPLookupRemote.getReatoRemote();
	   lCtrl.ExInserisciUlterioriReatiSige(mReatoPrincipale, mReati, lIdFasSigeSen);

	   String lRetPage = ritornoDopoCancellazione("Ulteriori reati inseriti", null);
    
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : fine");

	  return lRetPage;  //restituisce la jsp di VIEW

  }
  
}