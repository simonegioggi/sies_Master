package siap.sius.collaboratore.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActInserisciCollaboratore</p>
* <p>Description: Classe Action per l'inserimento di 
* un nuovo Collaboratore di giustizia o 
* di un nuovo periodo per uno già esistente.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 3.0
*/

/**
 * La funzione inserisce un nuovo Collaboratore associato ad un Fascicolo SIUS.
 * I dati in input sono: ID Fascicolo SIUS, COD_UFFICIO, DATA_INIZIO, DATA_FINE.
 * La funzione utilizza una stored procedure attraverso il controller CollaboratoreController per effettuare l'inserimento.
 * La funzione non effettua controlli sull'abilitazione all'operazione che si presume siano già stati fatti.
 * Effettuato l'inserimento si passa al Dettaglio. 
 * 
 */
public class ActInserisciCollaboratore extends ActionSius implements ICostantiCollaboratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    public String processRequest() throws Exception
    {
    	// Istanza del model Collaboratore e sua valorizzazione con i dati letti dalla form
    	CollaboratoreModel lCollaboratore = new CollaboratoreModel();
      
    	lCollaboratore.setIdFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
    	lCollaboratore.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
    	lCollaboratore.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    	lCollaboratore.setCodOperatoreInserimento(getCodUtenteConnesso());
        // data di inizio
        lCollaboratore.setDataInizio(getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO ));
        // data di fine opzionale
        if (getRequestStringParameter(CAMPO_ANNO_DATA_FINE).trim().length() == 4)
        	lCollaboratore.setDataFine(getRequestDateParameter( CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE ));
    	
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Collaboratore Model :" +  lCollaboratore);  

        // Inserimento
    	ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
    	if (lCtrl.ExIsPackage())
    	{
    		lCtrl.ExInserisciCollaboratore(lCollaboratore);
     	}       
        
    	// Si passa al dettaglio
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.collaboratore.action.ActLoadDettaglioCollaboratore");

      return lPage.toString();
    }
}