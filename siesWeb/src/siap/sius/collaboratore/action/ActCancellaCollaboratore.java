package siap.sius.collaboratore.action;

import java.math.BigDecimal;

import siap.sius.ActionSius;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActCancellaCollaboratore</p>
* <p>Description: Classe Action per la cancellazione di un record 
* Collaboratore di Giustizia legato ad un Procedimento SIUS.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

public class ActCancellaCollaboratore extends ActionSius implements ICostantiCollaboratore
{
    public String processRequest() throws Exception
    {
      BigDecimal lIdCollaboratore = getRequestBigDecimalParameter(CAMPO_ID_COLLABORATORE);
  	
  	 // Lock per evitare che operazione di inserimento o modifica Collaboratore sullo stesso Fascicolo SIUS possa essere fatta contemporaneamente da + utenti.
  	 lockApplicativo("Collaboratore");        
      
     ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
     lCtrl.ExCancellaCollaboratore(lIdCollaboratore);

      return ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", null);
    }
}