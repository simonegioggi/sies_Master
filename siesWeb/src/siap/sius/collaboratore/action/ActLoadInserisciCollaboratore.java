package siap.sius.collaboratore.action;

import siap.sius.ActionSius;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;



/**
* <p>Title: ActLoadInserisciCollaboratore</p>
* <p>Description: Classe Action per il caricamento della form di Input o di Modifica 
* relativa ad un Collaboratore di Giustizia. </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActLoadInserisciCollaboratore extends ActionSius implements ICostantiCollaboratore
{
    public String processRequest() throws Exception
    {	
    	// Lock per evitare che operazione di inserimento, modifica,o cancellazione su Collaboratore dello stesso Fascicolo SIUS possano essere attivate contemporaneamente da + utenti.
    	lockApplicativo("Collaboratore");        
        
    	gestioneRitorno();
      
    	// Istanza del model Collaboratore e sua prima valorizzazione
    	CollaboratoreModel lCollaboratore = new CollaboratoreModel();
    	lCollaboratore.setIdFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
    	lCollaboratore.setCodUfficio(getCodUfficioUtenteConnesso());
    	
    	// Passaggio dei dati alla form
    	setRequestAttribute("collaboratore",lCollaboratore);
    	setRequestAttribute("modalita","I");

    	return PG_LOAD_MODIFICACOLLABORATORE;
    }
}