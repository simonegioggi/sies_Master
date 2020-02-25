package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

/**
 * <p>Title: ActCancellaEventoPermessoLicenza</p>
 * <p>Description: Classe Action per la Cancellazione Evento Permesso Licenza</p> 
 */
public class ActCancellaEventoPermessoLicenza extends ActionSius
implements ICostantiPermesso,ICostantiEventoPermessoLicenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest(): inizio");
    
    EventoPermessoLicenzaModel lEPLModel = new EventoPermessoLicenzaModel();
    lEPLModel.setIdEventoPermessoLicenza(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_PERMESSO_LICENZA));
   
    IEventoPermessoLicenza lEPLCtrl = SIUSLookupRemote.getEventoPermessoLicenzaRemote();    
    lEPLCtrl.ExCancellaEventoPermessoLicenza( lEPLModel );
        
    String lRetPage = ritornoDopoCancellazione("L'evento durante la fruizione è stato cancellato!", null);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest(): fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}