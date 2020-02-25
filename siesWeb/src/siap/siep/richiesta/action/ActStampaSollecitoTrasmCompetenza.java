package siap.siep.richiesta.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import f3b.log.LogF3B;
import f3b.util.DateUtils;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.jms.JMSLookupRemote;

import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoria.controller.IIstruttoria;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * <p>Title: ActStampaSollecitoTrasmCompetenza</p>
 * <p>Description: Stampa del Sollecito - Inserimento Evento</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaSollecitoTrasmCompetenza extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	  
  public String processRequest() throws F3BException
  {
    BigDecimal lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
    IFascicoloSiep lCrtlFas = SIEPLookupRemote.getFascicoloSiepRemote();
    FascicoloSiepModel lFascicoloModel = lCrtlFas.ExRicercaFascicoloByKey(lIdFascicolo);

    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    
    BigDecimal lIdMessage = null;
    lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    //siesLogger.debug("--XX-- Id_Message che arriva in INPUT = "+lIdMessage);
    
    MessaggioModel lMess = new MessaggioModel();
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    siesLogger.debug("--XX-- Message trovato con query By ID_Mess = "+lMess);
    
    // Recupera il messaggio di trasmizzione atti
    MessaggioModel lTrasmCompetenzaMsg = null;
    if (lMess.getJmsCorrelationIdMessage()!=null){
      lTrasmCompetenzaMsg = lCrtl.ExRicercaMessaggioByKey(new BigDecimal(lMess.getJmsCorrelationIdMessage()));
      siesLogger.debug("--XX-- Message trovato con query By JMD_Corr_ID = "+lTrasmCompetenzaMsg);
    }    
    
    
    // Creo un Evento per registrare il sollecito 
    EventoNotificaModel lEveSoll = new EventoNotificaModel();
    
    lEveSoll.getEvento().setCodTipoEvento("13");
    lEveSoll.getEvento().setCodTipoProvvedimento("04");
    lEveSoll.getEvento().setCodMotivo("5402");
    lEveSoll.getEvento().setCodEsito("-");
    
    lEveSoll.getEvento().setCodUfficioEmittente (this.getCodUfficioUtenteConnesso());
    lEveSoll.getEvento().setCodLuogoEmittente   (this.getCodComuneUtenteConnesso());


    lEveSoll.getEvento().setCodUfficioDestinatario("-");//lMess.getCodUfficioDestinatario());
    lEveSoll.getEvento().setCodTipoUfficioDestinatario("-");
    lEveSoll.getEvento().setCodLuogoDestinatario("-");

    lEveSoll.getEvento().setDataEmissione    (DateUtils.getSysDateAsDate("dd/MM/yyyy"));
    
    // n.b. serve per la stampa ed è la data di trasmissione dell'evento che si sta sollecitando
    //      andrebbe recuperata dal messaggio 00066 e nel dall'esito
    lEveSoll.getEvento().setDataTrasmissioneAtti (lTrasmCompetenzaMsg.getDataInvio());
    
    lEveSoll.getEvento().setCodOperatoreInserimento (this.getCodUtenteConnesso());
    lEveSoll.getEvento().setDataInserimento         (DateUtils.getSysDate());
    lEveSoll.getEvento().setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());
    
    lEveSoll.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lEveSoll.getEvento().setFlagStampaSiep("N");
    lEveSoll.getEvento().setFlagVideoSiep("N");
    
    //===============================================
    // Recupero il template
    //===============================================
    String  flagTemplate ="0";

    TemplateModel lTemMod = new TemplateModel();
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
        lEveSoll.getEvento().getCodTipoEvento(),
        lEveSoll.getEvento().getCodTipoProvvedimento(),
        lEveSoll.getEvento().getCodMotivo(),
        flagTemplate);

    lEveSoll.setNomeTemplate(lTemMod.getIdTemplate());

    lEveSoll.getEvento().setDataAggiornamento         (DateUtils.getSysDate());
    lEveSoll.getEvento().setCodUfficioAggiornamento   (lUff.getCodUfficio());
    lEveSoll.getEvento().setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
    
    // =======================================================================================
    // MEV42 - Cumulo Step2 - Gestione semplificata del 'Sollecito' 
    //  se l'Utente Non avesse VALIDATO il sollecito, sarebbe rimasto con il 
    //	Fascicolo Bloccato ( Esiste un Evento Non Validato) senza poterlo validare in seguito
    // =======================================================================================
    //lEveSoll.getEvento().setFlagDocumentoRegistrato("N");
    lEveSoll.getEvento().setFlagDocumentoRegistrato("S");
    
    // SALVO L'EVENTO
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel mEveSoll = lCtrlEve.ExInserisciEventoNotifica(lEveSoll);
    
    // INVIO LA STAMPA
    IIstruttoria lCtrlIstruttoria = SIEPLookupRemote.getIstruttoriaRemote();
    ByteArrayOutputStream lReport = lCtrlIstruttoria.ExStampaIstruttoria(lEveSoll, lUtenteMod);  
    
    
    setRequestAttribute("report", lReport);
    
	    // METTO IN SESSIONE L'ID DELL'EVENTO SOLLECITO (ripulendo prima la sessione)
	    if(!isSessionAttributeNullObj(FIELD_TEMP_ID_EVENTO_SOLLECITO))
	      this.removeSessionAttribute(FIELD_TEMP_ID_EVENTO_SOLLECITO);
	
	    this.setSessionAttribute(FIELD_TEMP_ID_EVENTO_SOLLECITO, mEveSoll.getEvento().getIdEvento());
	  
	    return IWebConstants.PG_DOWNLOAD;
    
  }
}