package siap.sius.depositoordinanzapc.action;

import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;

public class ActRicercaFSPModelliOrdinanza extends ActRicercaFSPuntuale
implements ICostantiDepositoOrdinanzaPc
{
  public String processRequest() throws Exception
  {
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

    // Ritorna la JSP di view dell'elenco stampe.
    return PG_ELENCOSTAMPEMODELLIORDINANZA;
  }
}
