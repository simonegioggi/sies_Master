package siap.sius.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;


/**
* <p>Title: ActAnnullaDefinizioneProcedimento</p>
* <p>Description: Classe Action per annullare la Definizione
 * del Procedimento che assume lo stato di ISCRITTO. </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActAnnullaDefinizioneProcedimento extends ActionSius
 implements ICostantiFascicoloSius
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lRectPage = null;
    gestioneRitorno();

    annulla();
    lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRectPage;
  }

  private void annulla() throws Exception
  {
    //Istanzio il Model e lo carico con quello posto in sessione.
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

   //Dati da aggiornare in  Fascicolo SIUS
    lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); //Codice dell'operatore che inserisce
    lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento( getCodUfficioUtenteConnesso()); //Codice dell'operatore che inserisce
    lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());
    lFasGPMod.getFascicoloSiusModel().setDataDefinizione(null);
    lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo(COD_ISCRITTO);

    //Dati da aggiornare in  Generale Procedimento
    lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lFasGPMod.getGeneraleProcedimentoModel().setDataAggiornamento(DateUtils.getSysDate());
    lFasGPMod.getGeneraleProcedimentoModel().setDataDefinizione(lFasGPMod.getFascicoloSiusModel().getDataDefinizione());
    lFasGPMod.getGeneraleProcedimentoModel().setTipoDefinizione("");
    lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione("");

    // Viene richiamato il Controller per eseguire l'Update
    IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    lFasCtrl.ExInserisciDefinizioneFascicoloSius(lFasGPMod);
  }

}