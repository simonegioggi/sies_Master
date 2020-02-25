package siap.siepe.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>Title: ActInserisciDefinizioneProcedimento</p>
 * <p>Description: Classe Azione di definizione Fascicolo SIEPE.</p>
 * In base ai dati nella request viene chiamato l'Update della tabella: </p>
 * Fascicolo_SIEPE .
 * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 */
public class ActInserisciDefinizioneProcedimento extends ActionSiap 
implements ICostantiFascicoloSiepe
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    gestioneRitorno();

    //Si Istanzia il Model e lo si carica con quello posto in sessione.
    FascicoloSiepeEstesoModel lFasMod = new FascicoloSiepeEstesoModel();
    lFasMod = (FascicoloSiepeEstesoModel)getSessionAttribute("FascicoloSiepeEsteso");
            
    //Dati da aggiornare in  Fascicolo SIEPE by Reference
    lFasMod.getFascicoloSiepe().setCodOperatoreAggiornamento ( getCodUtenteConnesso() ); //Codice dell'operatore che inserisce
    lFasMod.getFascicoloSiepe().setCodUfficioAggiornamento( getCodUfficioUtenteConnesso() ); //Codice dell'operatore che inserisce
    lFasMod.getFascicoloSiepe().setDataAggiornamento( DateUtils.getSysDate() );
    lFasMod.getFascicoloSiepe().setCodStatoFascicolo( COD_DEFINITO );
    lFasMod.getFascicoloSiepe().setTipoDefinizione( getRequestStringParameter( CAMPO_TIPO_DEFINIZIONE ) );
    lFasMod.getFascicoloSiepe().setDataDefinizione( getRequestDateParameter( CAMPO_ANNO_DATA_DEFINIZIONE, CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE) );
    lFasMod.getFascicoloSiepe().setDescrDefinizione( getRequestStringParameter( CAMPO_DESCR_DEFINIZIONE ));
    
    // Viene richiamato il Controller per eseguire l'Update dei campi afferenti alla 
    // definizione procedimento del fascisolo siepe interessato.
    IFascicoloSiepe lFasCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
    lFasCtrl.ExInserisciDefinizioneFascicoloSiepe(lFasMod.getFascicoloSiepe());

    // Prepara la "pagina" di dettaglio
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siepe.fascicolo.action.ActLoadDefinizioneProcedimento");
    lRedirigi.setParameter(CAMPO_CHIAVE_ANNO, lFasMod.getFascicoloSiepe().getChiaveAnno().toString());
    lRedirigi.setParameter(CAMPO_CHIAVE_PROGR,lFasMod.getFascicoloSiepe().getChiaveProgr().toString());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return lRedirigi.toString();
  }
}