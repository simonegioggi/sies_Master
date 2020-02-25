package siap.sius.collaboratore.action;

import java.math.BigDecimal;

import siap.sius.ActionSius;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadModificaCollaboratore</p>
* <p>Description: Classe Action per il caricamento della form di Modifica 
* relativa ad un Collaboratore di Giustizia. </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
/**
 * La funzione legge il parametro ID_COLLABORATORE, effettua una ricerca del Collaboratore univocamente 
 * individuato e lo passa alla form di modifica.
 */
public class ActLoadModificaCollaboratore extends ActionSius implements ICostantiCollaboratore
{
    public String processRequest() throws Exception
    {
      CollaboratoreModel lCollaboratore = null;
      BigDecimal lIdCollaboratore = getRequestBigDecimalParameter(CAMPO_ID_COLLABORATORE);
  	
  	// Lock per evitare che operazione di inserimento, modifica,o cancellazione su Collaboratore dello stesso Fascicolo SIUS possano essere attivate contemporaneamente da + utenti.
  	lockApplicativo("Collaboratore");        

     gestioneRitorno();
      
     ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
     if (lCtrl.ExIsPackage())
     {
   // 	lCollaboratore = lCtrl.ExGetCollaboratoreById(lIdCollaboratore);
    	lCollaboratore = lCtrl.ExRicercaCollaboratoreById(lIdCollaboratore);

    	setRequestAttribute("collaboratore",lCollaboratore);
       	setRequestAttribute("modalita","M");
     }
     else
    	 throw new F3BException(F3BException.USER_MESSAGE, "funzione non disponibile!");
     
 
      return PG_LOAD_MODIFICACOLLABORATORE;
    }
}