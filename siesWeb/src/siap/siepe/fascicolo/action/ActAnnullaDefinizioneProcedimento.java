package siap.siepe.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;

/**
* <p>Title: ActAnnullaDefinizioneProcedimento</p>
* <p>Description: Classe Action per annullare la Definizione del Procedimento che assume  </p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActAnnullaDefinizioneProcedimento extends ActionSiap
implements ICostantiFascicoloSiepe
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    String lRetPage = null;
    gestioneRitorno();

    this.annulla();
    lRetPage = ritornoDopoCancellazione("Definizione Procedimento Annullato Correttamente!", lRetPage);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRetPage;
  }

  private void annulla() throws Exception
  {
    // Si Istanzia il Model e lo si carica con quello posto in sessione.
    FascicoloSiepeEstesoModel lFasMod = new FascicoloSiepeEstesoModel();
    lFasMod = (FascicoloSiepeEstesoModel)getSessionAttribute("FascicoloSiepeEsteso");
    
    // Dati da aggiornare in Fascicolo SIEPE.
    lFasMod.getFascicoloSiepe().setCodOperatoreAggiornamento( getCodUtenteConnesso() ); //Codice dell'operatore che inserisce
    lFasMod.getFascicoloSiepe().setCodUfficioAggiornamento( getCodUfficioUtenteConnesso() ); //Codice dell'operatore che inserisce
    lFasMod.getFascicoloSiepe().setDataAggiornamento(DateUtils.getSysDate());
    lFasMod.getFascicoloSiepe().setCodStatoFascicolo(COD_ISCRITTO);
    lFasMod.getFascicoloSiepe().setDataDefinizione(null);
    lFasMod.getFascicoloSiepe().setTipoDefinizione("");
    lFasMod.getFascicoloSiepe().setDescrDefinizione("");

    // Viene richiamato il Controller per eseguire l'Update dei campi afferenti alla 
    // definizione procedimento del fascisolo siepe interessato.
    IFascicoloSiepe lFasCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
    lFasCtrl.ExInserisciDefinizioneFascicoloSiepe(lFasMod.getFascicoloSiepe()); 
  }
}