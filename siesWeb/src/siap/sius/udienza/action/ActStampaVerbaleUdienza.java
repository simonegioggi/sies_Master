package siap.sius.udienza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
//import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
//import siap.sico.template.action.ICostantiTemplate;
//import siap.sico.template.controller.ITemplate;
//import siap.sico.template.model.TemplateModel;
import siap.sius.SIUSException;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
//import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaVerbaleUdienza </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActStampaVerbaleUdienza extends ActionSiap
implements ICostantiUdienza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore   = getCodUtenteConnesso();
    String lCodiceUfficio     = getCodUfficioUtenteConnesso();

    UfficioModel lUfficio     = getUfficioUtenteConnesso();

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

    // Preleva l'Evento
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey( lKeyEvento );

    // Setta i dati per l'aggiornamento
    lEvento.setDataAggiornamento(DateUtils.getSysDate());
    lEvento.setCodUfficioAggiornamento(lCodiceUfficio);
    lEvento.setCodOperatoreAggiornamento(lCodiceOperatore);
    lEvento.setFlagDocumentoRegistrato("N");
      
    // Imposta il modello di stampa per tipo Ufficio
    if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("TDS"))
      lEvento.setTemIdTemplate( ID_TEMPLATE_VERBALE_UDIENZA );
    else if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("UDS"))
      lEvento.setTemIdTemplate( ID_TEMPLATE_VERBALE_UDIENZA_UDS );
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Modello di stampa non disponibile per il tipo ufficio.");
        
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "ID TEMPLATE -> " + lEvento.getTemIdTemplate() );
    
    // Crea il ByteArrayOutputStream
    IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaVerbaleUdienza( lEvento, lUfficio, getUtenteConnesso() );

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
//    return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;

  }
  
}