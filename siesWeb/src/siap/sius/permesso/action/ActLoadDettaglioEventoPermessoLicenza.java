package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;

 
/**
 * <p>Title: ActLoadDettaglioEventoPermessoLicenza</p>
 * <p>Description: Classe Action per la load Dettaglio Evento Permesso</p> 
 */
public class ActLoadDettaglioEventoPermessoLicenza extends ActionSius
implements ICostantiEventoPermessoLicenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    String lRetPage = PG_LOAD_DETTAGLIOEVENTOPERMESSOLICENZA;

    //super.gestioneRitorno();
    //setLinkRitorno();
        
    IEventoPermessoLicenza lEPLCtrl = SIUSLookupRemote.getEventoPermessoLicenzaRemote();
    EventoPermessoLicenzaModel lModelEPL = 
      lEPLCtrl.ExRicercaEventoPermessoLicenzaByKey(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_PERMESSO_LICENZA ) );
    
    // ATWK --
    IPermesso lCtrlPerm = SIUSLookupRemote.getPermessoRemote();
    //PermessoDepositoDecretoModel lPermDepDecr = null;
    DepositoDecretoMotivazioniLicenzaModel lPermDepDecr = null;
    lPermDepDecr = lCtrlPerm.ExRicercaPermessoLicenzaDepositati(lModelEPL.getLicIdLicenzaLibAnticipata());

    if ( lPermDepDecr == null )
      throw new F3BException(F3BException.USER_MESSAGE,
                                               "Per il Procedimento indicato non risulta depositato un decreto " +
                                               "di concessione permesso/licenza.\n" +
                                               "Operazione non consentita.");
    
    String lCodUfficioIns = 
      lPermDepDecr.getLicenza().getCodUfficioInserimento();
        
    if( lCodUfficioIns.equals( super.getCodUfficioUtenteConnesso() ))
    {
      super.setRequestAttribute("Modificabile", "SI");
    }
    else
    {
      super.setRequestAttribute("Modificabile", "NO");
    }
    
    setRequestAttribute("permessoDepDecr", lPermDepDecr);
    setRequestAttribute("eventoPermesso", lModelEPL);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}