package siap.sius.permesso.action;

import org.apache.log4j.Logger;

import siap.sius.ActionSius;
import siap.sius.permesso.controller.IEventoPermessoLicenza;
import siap.sius.permesso.model.EventoPermessoLicenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
/**
 * <p>Title: ActInserisciEventoPermessoLicenza</p>
 * <p>Description: Classe Action per Inserimento Evento Permesso</p> 
 */
public class ActInserisciEventoPermessoLicenza extends ActionSius
implements ICostantiEventoPermessoLicenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest: inizio");
    // Imposta i dati da inserire nel dbase.
    EventoPermessoLicenzaModel lModelEPL = new EventoPermessoLicenzaModel();
    lModelEPL.setLicIdLicenzaLibAnticipata( getRequestBigDecimalParameter( CAMPO_LIC_ID_LICENZA_LIBANTICIPATA ) );
    lModelEPL.setCodOperatoreInserimento(super.getCodUtenteConnesso());
    lModelEPL.setCodUfficioInserimento(super.getCodUfficioUtenteConnesso());
    lModelEPL.setDataInserimento( DateUtils.getSysDate() );
    lModelEPL.setDataSegnalazione(getRequestDateParameter( CAMPO_ANNO_DATA_SEGNALAZIONE,
                                                           CAMPO_MESE_DATA_SEGNALAZIONE,
                                                           CAMPO_GIORNO_DATA_SEGNALAZIONE ) );
    lModelEPL.setDescrEvento( getRequestStringParameter( CAMPO_DESCR_EVENTO ));
    lModelEPL.setMittenteSegnalazione( getRequestStringParameter( CAMPO_MITTENTE_SEGNALAZIONE ));
    lModelEPL.setCodTipoEvento( getRequestStringParameter( CAMPO_COD_TIPO_EVENTO ) );
    lModelEPL.setCodTipoConseguenza(getRequestStringParameter( CAMPO_COD_TIPO_CONSEGUENZA ) );
    lModelEPL.setDescrConseguenze( getRequestStringParameter( CAMPO_DESCR_CONSEGUENZE ) );
    
    IEventoPermessoLicenza lCtrlEPL =SIUSLookupRemote.getEventoPermessoLicenzaRemote();
    // Invoca metodo preposto all'inserimento dei dati.
    lModelEPL = lCtrlEPL.ExInserisciEventoPermessoLicenza( lModelEPL );    
        
    // Prepara la view di ritorno.
    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage(IWebConstants.PG_MAIN);
    lRedir.setAction("siap.sius.permesso.action.ActLoadDettaglioEventoPermessoLicenza");
    lRedir.setParameter(CAMPO_ID_EVENTO_PERMESSO_LICENZA, ""+lModelEPL.getIdEventoPermessoLicenza() );    
    
    String lRetPage = lRedir.toString();
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( this.getClass().getName()+".processRequest: fine");
    
    return lRetPage; //restituisce la jsp di VIEW
  }
}