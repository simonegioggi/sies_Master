package siap.sige.udienza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienza.controller.IUdienzaSigeRuolo;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaFissazioneUdienza </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa Fissazione Udienza
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @author 
 * @version 1.0
 */
public class ActStampaFissazioneUdienza  extends ActionSige
	implements ICostantiUdienzaSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    // Fascicolo Sige Esteso in sessione.
    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore  = getCodUtenteConnesso();
    String lCodiceUfficio    = getCodUfficioUtenteConnesso();

    UfficioModel lUfficio    = getUfficioUtenteConnesso();
    String lIdTemplate       = "";

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

    // Preleva l'Evento
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey( lKeyEvento );

    // Setta i dati per l'aggiornamento
    lEvento.setDataAggiornamento(DateUtils.getSysDate());
    lEvento.setCodUfficioAggiornamento(lCodiceUfficio);
    lEvento.setCodOperatoreAggiornamento(lCodiceOperatore);
    lEvento.setFlagDocumentoRegistrato("N");
    
    // Legge template scelto dalla ComboBox.
    lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
    
    // Verifica l'esistenza del modello di stampa
    if( lIdTemplate == null )
      throw new SIGEException(SIGEException.USER_MESSAGE, "Modello di stampa mancante!");
    
    lEvento.setTemIdTemplate(lIdTemplate);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "ID TEMPLATE -> " + lEvento.getTemIdTemplate() );

    // Crea il ByteArrayOutputStream
    IUdienzaSigeRuolo lCtrl = SIGELookupRemote.getUdienzaSigeRuoloRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaFissazioneUdienza(lFasEsteso.getFascicoloSige().getIdFascicoloSige(), lEvento, lUfficio.getCodUfficio(), super.getUtenteConnesso() );

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return IWebConstants.PG_DOWNLOAD_NEW;
  }  
}