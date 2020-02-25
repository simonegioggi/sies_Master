package siap.sius.depositodecreto.action;

import org.apache.log4j.Logger;

import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.log.LogF3B;

/**
 * ActRicercaFSPModelliDecreto - Azione che esegue la ricerca del fascicolo/procedimento, e 
 * ritorna la JSP con l'elenco dei modelli generabili. 
 */
public class ActRicercaFSPModelliDecreto extends ActRicercaFSPuntuale
implements ICostantiDepositoDecreto
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    // Prelevo la data camera consiglio dalla sessione
    // Processo la request
    super.processRequest();
    
    FascicoloGPModel lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    
    // Esegue controllo che il controllo della Data Camera di consiglio venga eseguito solo
    // per TDS.
    if( super.getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("TDS") )
    {
      if( lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null  )
      {
        removeSessionAttribute( "fascicoloSiusGP" );
        throw new SIUSException( SIUSException.USER_MESSAGE, "Modelli non generabili per questo procedimento, manca la data fissazione udienza.");
      }
    }

    if( super.isRequestParameterNullObj("viewElenco") )
      super.processRequest();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    // Ritorna la JSP di view dell'elenco stampe.
    return PG_ELENCOSTAMPEMODELLIDECRETO;
  }
}