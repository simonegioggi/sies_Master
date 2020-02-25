package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * Action per l'annotazione dell'esito della trasmissione per competenza
 * La Action viene invocata o dal dettaglio della risposta ricevuta (riscontro
 * trasmissioni) o direttamente dal tasto presente sulla griglia di gestione delle
 * misure di sicurezza.
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciAnnotaEsitoTrasmissione extends ActionSiap implements ICostantiMisuraSicurezza 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception 
  { 
    

    MessaggioModel lMessaggioEsito = null;
    MessaggioModel lMessaggioRichiesta = null;
    
    if (isRequestParameterNullObj(ICostantiMessaggio.CAMPO_ID_MESSAGGIO)) {
      // Provengo dalla Griglia, devo avere il fascicolo in sessione
      if (this.isSessionAttributeNullObj("fascicolo"))
      {
        return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      }
      
      super.setLinkRitorno();
    }
    else {
      // L'annotazione esito viene sempre fatta sulla risposta (PRESO IN CARICO, ISCRITTO IN CLASSE IV, RESTITUITO, INOLTRATO)
      // ma in caso di ISCRITTO IN CLASSE IV i campi del messaggio riportano gli estremi del fascicolo
      // di classe IV
      BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
      IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
      lMessaggioEsito = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
      
      lMessaggioRichiesta = lCrtl.ExRicercaMessaggioByKey(new BigDecimal(lMessaggioEsito.getJmsCorrelationIdMessage()));
      
      // Recuperao il fascicolo da mettere in sessione
      BigDecimal lChiaveAnno = lMessaggioRichiesta.getChiaveAnnoSiep();
      BigDecimal lChiaveProgr = lMessaggioRichiesta.getChiaveProgrSiep();
      FascicoloSiepModel lFasMod = new FascicoloSiepModel();

      lFasMod.setChiaveUfficio (getCodUfficioUtenteConnesso());
      lFasMod.setChiaveProgr   (lChiaveProgr);
      lFasMod.setChiaveAnno    (lChiaveAnno);

      IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
      FascicoloSiepModel lFasRet = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);
      
      // Pulisco la sessione
      setSessionAttribute("fascicolo",null);
      setSessionAttribute("soggetto",null);
      setSessionAttribute("sentenza",null);

      setSessionAttribute("cumulowiz",null);
      setSessionAttribute("penaresidua",null);
      setSessionAttribute("reato",null);      

      // Metto in sessione il fascicolo
      setSessionAttribute("fascicolo",lFasRet);
      setSessionAttribute("soggetto",lFasRet.getSoggetto());
      setSessionAttribute("sentenza",lFasRet.getSentenza());    
    }
    
    FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    //==========================================================================
    // Verifico se fascicolo di competenza
    //==========================================================================
    this.isFascicoloSiepDiCompetenza();
    
    //==========================================================================
    // Controllo Validazione Fascicolo
    //==========================================================================
    if ( this.isFascicoloNonValidato() )
      return IWebConstants.PG_MESSAGE;
    //==========================================================================
    // Controllo Fascicolo definito
    //==========================================================================
    if (this.isFascicoloArchiviatoDefinito())
      return IWebConstants.PG_MESSAGE;
    //==========================================================================
    // Verifico se esistono eventi non validati
    //==========================================================================
    this.isEventoNonValidato();
    
    
    
    setRequestAttribute("messaggioEsito", lMessaggioEsito );
    setRequestAttribute("messaggioRichiesta", lMessaggioRichiesta );
    
    
    //========================
    // Dati per le combo
    //========================
    Option lOption = null;
    
    // Tipo UFFICIO Destinatario    
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
    String[] lFiltroUffici =  {"-", "PM", "PMM"}; // i destinatari sono solo PM e PMM
    lOption.setFilter(lFiltroUffici);
    lOption.setSelected("-");
    setRequestAttribute("comboUfficiPM", "" + lOption );
    
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPM());
    lOption.setFilter(lFiltroUffici);
    lOption.setSelected("-");
    setRequestAttribute("comboUfficiPMInoltro", "" + lOption );
    

    
    //==========================================================================
    // Se sto annotando l'esito di un messaggio, precarico i dati dell'esito
    // dal messaggio e li posto alla jsp in AnnotazioneEsitoTrasmissioneModel
    // stessa struttura utlizzata nel caso di Modifica
    //==========================================================================
    if (lMessaggioEsito!=null) {     
      UfficioModel ufficioEsito = getUfficioByCodUfficio(lMessaggioEsito.getCodUfficioMittente());
      setRequestAttribute("ufficioEsito",  ufficioEsito );
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("lMessaggioEsito = "+lMessaggioEsito);
      
      AnnotazioneEsitoTrasmissioneModel lAnnotazModel = new AnnotazioneEsitoTrasmissioneModel();
      lAnnotazModel.setDataEsito  (lMessaggioEsito.getDataInvio());
      lAnnotazModel.setCodEsito   (lMessaggioEsito.getCodEsito());
      lAnnotazModel.setDescrEsito (lMessaggioEsito.getDescrEsito());
      
      if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(lMessaggioEsito.getCodEsito())){
        // Se Iscritto recupera i dati del fascicolo di classe IV
        lAnnotazModel.setChiaveAnno  (lMessaggioEsito.getChiaveAnnoSiep());
        lAnnotazModel.setChiaveProgr (lMessaggioEsito.getChiaveProgrSiep());
      }
      else if (ICostantiJMS.TRASFERITO.equals(lMessaggioEsito.getCodEsito())){
        UfficioModel ufficioInoltro = getUfficioByCodUfficio(lMessaggioEsito.getCodUfficioInoltro());
        setRequestAttribute("ufficioInoltro",  ufficioInoltro );
      }
      
      lAnnotazModel.setNoteEsito(lMessaggioEsito.getNote());
      
      setRequestAttribute("annotazioneEsito", lAnnotazModel);
      
    }
    
    
    // Tipologia Atto: EVENTO.TIPO_PROVVEDIMENTO      
    //lOption = new Option(DecodificheManager.getInstance().getTipoRichiestaT());
    lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
    lOption.setFilter("25");
    setRequestAttribute("comboTipoProvv", "" + lOption);
    
    // EVENTO.COD_MOTIVO
    Vector <DecodificheModel> lMotiviProvv = new Vector <DecodificheModel>();
    lMotiviProvv.add(new DecodificheModel("5200","Esito Trasmissione Atti per competenza ai fini dell'esecuzione della misura di sicurezza","","","","","","","") );
    Option lOptionMotivi = new Option(lMotiviProvv);
    setRequestAttribute("comboMotivoProvv", "" + lOptionMotivi);
    
    
    
    
    // Oggetto Atto: EVENTO.COD_MOTIVO
    // RV_HIGH_VALUE = 'RICHGEN'
    // 5404 = Emissione provvedimento di cumulo a seguito revoca beneficio
    // TODO 'MS' gestire i dati nel decodifiche
    Vector <DecodificheModel> lEsiti = new Vector <DecodificheModel>();
    lEsiti.add(new DecodificheModel("-","-","","","","","","","") );
    lEsiti.add(new DecodificheModel(ICostantiJMS.PRESAINCARICO     ,"Atti Presi in carico","","","","","","","") );
    lEsiti.add(new DecodificheModel(ICostantiJMS.ISCRITTO_CLASSE_IV,"Iscritto procedimento di classe IV ","","","","","","","") );
    lEsiti.add(new DecodificheModel(ICostantiJMS.RESTITUITO        ,"Atti restituiti per incompetenza territoriale","","","","","","","") );
    lEsiti.add(new DecodificheModel(ICostantiJMS.TRASFERITO        ,"Atti trasmessi ad altro ufficio","","","","","","","") );

    Option lOptionEsiti = new Option( lEsiti);
    lOptionEsiti.setSelected("-");
    setRequestAttribute("comboEsitiProvvedimento","" + lOptionEsiti);
    
//    lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentiRichGen());
//    lOption.setFilter(new String[]{"-", "5404"});

    
    // MAGISTRATO COMPETENTE
    IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteMagistratoModel lMagi = lMagCtrl.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
    setRequestAttribute("magistratocompetente", lMagi);
   
    setRequestAttribute("modalita", "I");
    
    return PG_LOAD_INSERISCI_ANNOTAZIONE_ESITO;
  }
}