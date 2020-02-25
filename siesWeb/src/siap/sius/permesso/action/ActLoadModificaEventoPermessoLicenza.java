package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
//import siap.sius.permesso.model.PermessoDepositoDecretoModel;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
 
/**
 * <p>Title: ActLoadInserisciEventoPermessoLicenza</p>
 * <p>Description: Classe Action per la load Inserisci Evento del Permesso</p> 
 */
public class ActLoadModificaEventoPermessoLicenza extends ActionSius
implements ICostantiEventoPermessoLicenza 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    String lRetPage = PG_LOAD_INSERISCIEVENTOPERMESSOLICENZA;
        
    IEventoPermessoLicenza lEPLCtrl = SIUSLookupRemote.getEventoPermessoLicenzaRemote();
    EventoPermessoLicenzaModel lModelEPL = 
      lEPLCtrl.ExRicercaEventoPermessoLicenzaByKey(getRequestBigDecimalParameter(CAMPO_ID_EVENTO_PERMESSO_LICENZA ));      
    
    setRequestAttribute("eventoPermesso", lModelEPL);
    
    DepositoDecretoMotivazioniLicenzaModel lPermDepDecr = null;
    IPermesso lCtrlPerm = SIUSLookupRemote.getPermessoRemote();
    lPermDepDecr = lCtrlPerm.ExRicercaPermessoLicenzaDepositati(lModelEPL.getLicIdLicenzaLibAnticipata());
  
    if ( lPermDepDecr == null )
      throw new F3BException(F3BException.USER_MESSAGE,"Per il Procedimento indicato non risulta depositato un decreto " +
                                                       "di concessione permesso/licenza.\n" +
                                                       "Operazione non consentita.");
        
    setRequestAttribute("permessoDepDecr", lPermDepDecr);

    Option lOption = 
      new Option( DecodificheManager.getInstance().getTipoEventoPermessoLicenza());
    lOption.setSelected( lModelEPL.getCodTipoEvento() );
    setRequestAttribute("tipiEventi", "" + lOption );
    
    lOption = 
      new Option( DecodificheManager.getInstance().getTipoConseguenza());
    lOption.setSelected( lModelEPL.getCodTipoConseguenza() );
    setRequestAttribute("tipiConseguenze", "" + lOption );
    
    setRequestAttribute("modalita", "M");
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}