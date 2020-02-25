package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
 * <p>Title: ActCancellaEsitoPermessoLicenza</p>
 * <p>Description: Classe Action per la Cancellazione Esito Permesso Licenza</p> 
 */
public class ActCancellaEsitoPermessoLicenza extends ActionSius
implements ICostantiLicenzaLibanticipata
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest: inizio");
    
    ILicenzaPeriodiLibAnticipata lCtrlLicPerLibAnt = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
    
    // Recupero del record da modificare.
    LicenzaLibAnticipataModel lLic = 
      lCtrlLicPerLibAnt.ExRicercaLicenzaLibanticipataByKey( 
          getRequestBigDecimalParameter(CAMPO_ID_LICENZA_LIBANTICIPATA) );
    
    // Imposta i parametri da modificare.
    lLic.setCodOperatoreAggiornamento( super.getCodUtenteConnesso() );
    lLic.setCodUfficioAggiornamento( super.getCodUfficioUtenteConnesso() );
    lLic.setDataAggiornamento( DateUtils.getSysDate() );
    lLic.setCodEsito("-");
    lLic.setNumeroGiorniNoFruiti( null );
    lLic.setNumeroOreNoFruite( null );
    lLic.setDataAnnotazioneEsito( null );
    
    // Si esegue la chiamata del metodo del controller preposto alla modifica. 
    lCtrlLicPerLibAnt.ExModificaLicenzaLibanticipata(lLic);
        
    String lRetPage = ritornoDopoCancellazione("L'esito è stato cancellato !", null);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest: fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}