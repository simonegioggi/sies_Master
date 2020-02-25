package siap.sius.udienza.action;

//import java.util.Vector;
//import java.util.StringTokenizer;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
//import siap.sico.utente.model.UtenteModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
//import siap.sius.fascicolo.model.FascicoloGPModel;
//import siap.sius.fascicolo.model.FascicoloSiusModel;
//import siap.sius.fascicolo.controller.IFascicoloSius;
//import siap.sico.evento.action.ICostantiEvento;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.stampa.controller.IStampaSius;
//import siap.sico.evento.controller.IEvento;
//import siap.sico.template.action.ICostantiTemplate;
//import siap.sico.template.controller.ITemplate;
//import siap.sico.template.model.TemplateModel;
//import siap.sico.util.SICOLookupRemote;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
//import f3b.util.F3BException;
//import f3b.util.F3BException;
//import f3b.web.RedirectTo;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaFlashVerbaleUdienza </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActStampaFlashVerbaleUdienza extends ActionSiap
implements ICostantiUdienza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");
    
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore = getCodUtenteConnesso();
    String lCodiceUfficio   = getCodUfficioUtenteConnesso();
    String lCodComune       = getCodComuneUtenteConnesso();

    // Preleva dal FascicoloGPModel
    // Necessario Poichè lo si preleva anche nel controller di stampa
    BigDecimal lIdFasSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
    //IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    //FascicoloGPModel lFasGPSius = lFasCtrl.ExRicercaFascicoloByKey(lIdFasSius );

    // Preleva la data udienza.
    Date lDataUdienza = getRequestDateParameter("dataUdienza","dd-MM-yyyy")  ;

    // Prepara il model EventoNotifica.
    // Imposta i dati necessari per la gestione dell'evento.
    EventoModel lEve = new EventoModel();
    lEve.setCodTipoEvento("07");
    lEve.setDataEmissione( lDataUdienza );
    lEve.setCodMotivo("0700");
    lEve.setCodUfficioEmittente(lCodiceUfficio);
    lEve.setCodLuogoEmittente(lCodComune);
    lEve.setCodOperatoreInserimento(lCodiceOperatore);
    lEve.setCodUfficioInserimento(lCodiceUfficio);
    lEve.setDataInserimento(DateUtils.getSysDate());
    lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
    lEve.setCodEsito("-");
    lEve.setCodTipoProvvedimento("-");
    lEve.setCodLuogoDestinatario("-");
    lEve.setCodTipoUfficioDestinatario("-");
    lEve.setFasSiuIdFascicoloSius( lIdFasSius );
    //lEve.setFasSiuIdFascicoloSius( lFasGPSius.getFascicoloSiusModel().getIdFascicoloSius() );

    /*
    String lIdTemplate = new String();
    // Individuazione template
    if (isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE))
      lIdTemplate = ricercaIdTemplate(lEve);
    else
      lIdTemplate = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
   
    lEve.setTemIdTemplate(lIdTemplate);
    */
    
    // Chiamata al Controller
    /* 20080208 Eliminato.
     
    IEvento lCtrl2 = SICOLookupRemote.getEventoRemote();
    lEve = lCtrl2.ExInserisciEvento(lEve );
    */
    
    // Preleva dalla sessione i dati dell'utente connesso.
    UfficioModel lUfficio     = getUfficioUtenteConnesso();

    // Imposta il modello di stampa per tipo Ufficio
    if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("TDS"))
      lEve.setTemIdTemplate( ID_TEMPLATE_VERBALE_UDIENZA );
    else if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("UDS"))
      lEve.setTemIdTemplate( ID_TEMPLATE_VERBALE_UDIENZA_UDS );
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Modello di stampa non disponibile per il tipo ufficio.");

    
    ByteArrayOutputStream lByteArrayOut = null;

    // Generazione documento di stampa
    IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
    lByteArrayOut = lCtrlSta.ExPreStampaVerbaleUdienza(lEve, lUfficio.getCodUfficio(), super.getUtenteConnesso());

    //Prepara la pagina di destinazione
    if (lByteArrayOut != null)
      setRequestAttribute("report", lByteArrayOut);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");
    
    return IWebConstants.PG_DOWNLOAD;
  }
  
  
  /**
   * Funzione di ricerca Template in base al Tipo Provvedimento, Tipo Evento, Cod Esito e Flag (U/T).
   * Viene usata questa funzione quando nella form non esiste l'indicazione del template da usare.
   * La funzione restituisce il primo dei template se individuati.
   * <p>
   * @param aEvento 
   * @return
   * @throws Exception
   */
  /*
  private String ricercaIdTemplate(EventoModel aEvento) throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".ricercaIdTemplate(EventoModel): inizio");
    
    ITemplate lTempCtrl = SICOLookupRemote.getTemplateRemote();
        
    // Si recupera info dell'Ufficio Utente connesso serve per ricavare il FLAG TEMPLATE
    UfficioModel lUfficio = getUfficioUtenteConnesso();
       
    // Si valorizza il filtro di ricerca sui Template
    TemplateModel lTemplate = new TemplateModel();
    lTemplate.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
    lTemplate.setCodTipoEvento(aEvento.getCodTipoEvento());
    lTemplate.setCodEsito(aEvento.getCodEsito());
    // Preleva dal codice tipo ufficio la prima lettera, in questo caso può essere
    // T = TDS o U = UDS.
    lTemplate.setFlagTemplate(lUfficio.getCodTipoUfficio().substring(0,1));
    
    try
    {
      //  Ricerca dei Template
      Vector lTemplates = lTempCtrl.ExRicercaTemplate(lTemplate);
      if (lTemplates != null && lTemplates.size() > 0)
        lTemplate = (TemplateModel) lTemplates.get(0);
      else
        throw new F3BException(F3BException.USER_MESSAGE,"Non esiste il template di stampa !");
    }
    catch (Exception e)
    {
      throw new F3BException(F3BException.USER_MESSAGE,"Errore nella ricerca del template : " + e.toString());
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".ricercaIdTemplate(EventoModel): fine");
    
    return lTemplate.getIdTemplate();
  } 
  */ 
}