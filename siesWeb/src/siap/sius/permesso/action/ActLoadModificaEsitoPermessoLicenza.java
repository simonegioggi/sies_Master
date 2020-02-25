package siap.sius.permesso.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sius.ActionSius;
import siap.sius.permesso.controller.IPermesso;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
 
/**
 * <p>Title: ActLoadModificaEsitoPermessoLicenza</p>
 * <p>Description: Classe Action per la load Modifica Esito Permesso Licenza</p> 
 */
public class ActLoadModificaEsitoPermessoLicenza extends ActionSius
implements ICostantiPermesso
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    String lRetPage = PG_LOAD_MODIFICA_ESITO_PERMESSOLICENZA;
    
    // Recupera Id della licenza
    BigDecimal lIDLicLibAnt = 
      super.getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA);
    
    IPermesso lCtrlPerm = SIUSLookupRemote.getPermessoRemote();
    /*
    PermessoDepositoDecretoModel lPermDepDecr = 
      lCtrlPerm.ExRicercaPermessoLicenzaDepositati(lIDLicLibAnt);
    */
    DepositoDecretoMotivazioniLicenzaModel lPermDepDecr = 
      lCtrlPerm.ExRicercaPermessoLicenzaDepositati(lIDLicLibAnt);
    
    if ( lPermDepDecr == null )
      throw new F3BException(F3BException.USER_MESSAGE,"Per il Procedimento indicato non risulta depositato un decreto " +
                                                       "di concessione permesso/licenza.\n" +
                                                       "Operazione non consentita.");
    
    Option lOptionEsiti = new Option( DecodificheManager.getInstance().getEsitoPermessoLicenza());
    /*
    lOptionEsiti.setSelected( lPermDepDecr.getLicenzaLibAnticipata().getCodEsito() );
    */
    lOptionEsiti.setSelected( lPermDepDecr.getLicenza().getCodEsito() );
    setRequestAttribute("esiti", "" + lOptionEsiti );
    setRequestAttribute("permessoDepDecr", lPermDepDecr);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}