package siap.sius.collaboratore.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActModificaCollaboratore</p>
* <p>Description: Classe Action per attivare la modifica di un record Collaboratore.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
/**
 * La funzione effettua la modifica di un record relativo ad un collaboratore 
 * di Giustizia legato ad un Procedimento SIUS.
 * I campi modificabili sono: data_inizio e data_fine. 
 * La funzione legge questi dati più l'ID del record dalla form e 
 * chiama attraverso il Controller la stored procedure adibita 
 * all'update del record.
 */
public class ActModificaCollaboratore extends ActionSius implements ICostantiCollaboratore
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    public String processRequest() throws Exception
    {
    	// Istanza del model Collaboratore e sua valorizzazione con i dati letti dalla form
    	CollaboratoreModel lCollaboratore = new CollaboratoreModel();
      
    	lCollaboratore.setIdCollaboratore(getRequestBigDecimalParameter(CAMPO_ID_COLLABORATORE));
    	lCollaboratore.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    	lCollaboratore.setCodOperatoreAggiornamento(getCodUtenteConnesso());
        // data di inizio
        lCollaboratore.setDataInizio(getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO ));
        // data di fine opzionale
        if (getRequestStringParameter(CAMPO_ANNO_DATA_FINE).trim().length() == 4)
        	lCollaboratore.setDataFine(getRequestDateParameter( CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE ));
    	
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Collaboratore Model :" +  lCollaboratore);  

        // Aggiornamento
    	ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
    	if (lCtrl.ExIsPackage())
    	{
    		lCtrl.ExAggiornaCollaboratore(lCollaboratore);
     	}       
        
    	// Si passa al dettaglio
        RedirectTo lPage = new RedirectTo();
        lPage.setPage(IWebConstants.PG_MAIN);
        lPage.setAction("siap.sius.collaboratore.action.ActLoadDettaglioCollaboratore");

      return lPage.toString();
    }
}