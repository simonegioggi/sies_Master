package siap.siep.richiesta.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

import siap.jms.ICostantiJMS;
import siap.jms.SIAPSender;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.util.SIEPLookupRemote;



/**
 * <p>Title: ActTrasferisciTrasmissioneCompetenza</p>
 * <p>Description: Si occupa di inviare la richiesta di Trasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActTrasferisciTrasmissioneCompetenza extends ActionSiap implements ICostantiRichiesta, ICostantiJMS {

  public String processRequest() throws Exception
  {
    BigDecimal lIdEvento =  getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    
    // ricerca evento competenza
    ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
    CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);
    setRequestAttribute("competenza", mComp);
    
 // Ricerca EventoNotifiche per le Motivazioni presenti in CampoNote
    IEvento lCrtlEve = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveMod = lCrtlEve.ExRicercaEventoNotificaByKey(lIdEvento);
    
    //destinatari
    //String lTipoUff = mComp.getCodTipoAutoritaComp();
    //String lSedeUff = mComp.getDescrLuogoAutoritaComp();
    //String lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);
    
    String lCodiceUfficioDest = mComp.getCodUfficioAutoritaComp();

    UfficioModel lUfficioDestModel = getUfficioByCodUfficio (lCodiceUfficioDest);
    UfficioModel lBDIDest = getUfficioByCodUfficio (lUfficioDestModel.getCodDistretto());

    UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());
    
    //soggetto
    SoggettoModel lSogg = new SoggettoModel(lFascicoloModel.getSoggetto()); 
    
    // cambio l'ufficio di competenza. Inutile è un dato di sessione.
    //lFascicoloModel.setCodUfficioInserimento (lCodiceUfficio);   

    MessaggioModel lMessage = null;
    //==========================================================================
    // Ottimizzazione BLOB: se trasmissione su stessa BDI non valorizzo il blob per evitare
    //       di sovraccaricare la tabella MESSAGGIO. Il destinatario prenderà
    //       i dati direttamente dal DB.
    if (lBDIDest.getCodDistretto().equals(lBDIMittente.getCodDistretto())) {
      lMessage = new MessaggioModel();
      lMessage.setTreeModel(new TreeModel());
      //lMessage.setCodEsito(aValore);
    }
    else {
      IRicercaJMS lCtrlMes = SIEPLookupRemote.getRicercaJMS();
      lMessage = lCtrlMes.ExRicercaFascicoloSiepPerTrasferimento(lFascicoloModel);   
      lMessage.setCodEsito (""); // resetto il codEsito che viene messao a 10000 (TROVATO) dal controller
    }    
    //==========================================================================
      
    //Prendo dal TreeModel il Dettaglio del Fasciocolo per modificare lo stato procedimento
    /*TreeModel ltree = lMessage.getTreeModel();
    StatoProcedimentoModel lStat = new StatoProcedimentoModel();
    ltree.findTreeModel(ltree, lStat);
    lStat.setCodStatoProcedimento("0347");*/

    lMessage.setDescrBdiDestinataria   (lBDIDest.getDescrComune());
    lMessage.setCodBdiDestinataria     (lBDIDest.getCodUfficio());
    lMessage.setCodUfficioDestinatario (lCodiceUfficioDest);
    
    lMessage.setCodBdiMittente       (lBDIMittente.getCodUfficio());
    lMessage.setDescrBdiMittente     (lBDIMittente.getDescrComune());
    lMessage.setCodUfficioMittente   (getCodUfficioUtenteConnesso());
    lMessage.setCodiceUtenteMittente (this.getCodUtenteConnesso());
    
    lMessage.setCodTipoMessaggio  (RICHIESTA);
    lMessage.setDataInvio         (DateUtils.getSysDate());
    
    if("0740".equals(lEveMod.getEvento().getCodMotivo()))
    	lMessage.setCodTipoOperazione (SEGUITO_ATTI_TRASFERIMENTO_COMPETENZA);
    else
    	lMessage.setCodTipoOperazione (TRASFERIMENTO_COMPETENZA);
    
    // Inserisco le Motivazioni nelle note
    if( lEveMod!=null && lEveMod.getCampoNote()!= null && 
       	lEveMod.getCampoNote().length >0 && lEveMod.getCampoNote()[0] != null &&
   		lEveMod.getCampoNote()[0].getDescr() != null)
    {
    	lMessage.setNote(lEveMod.getCampoNote()[0].getDescr() );
    }
   
    // Riferimento al Messaggio di Richiesta Trasmissione 
    if(mComp!=null && mComp.getIdCompetenza()!=null && mComp.getIdMessaggioRichiesta()!=null )
     	lMessage.setIdRichiesta(mComp.getIdMessaggioRichiesta());
    
    //dati soggetto
    lMessage.setNomeSoggetto(lSogg.getNome());
    lMessage.setCognomeSoggetto(lSogg.getCognome());
    lMessage.setDataNascita(lSogg.getDataNascita());
    lMessage.setCodComuneNascita(lSogg.getCodComuneNascita());
    lMessage.setCodStatoNascita(lSogg.getCodStatoNascita());
      
    //SETTA RIFERIMENTI FASCICOLO SIEP
    lMessage.setChiaveAnnoSiep    (lFascicoloModel.getChiaveAnno());
    lMessage.setChiaveProgrSiep   (lFascicoloModel.getChiaveProgr());
    lMessage.setChiaveUfficioSiep (lFascicoloModel.getChiaveUfficio());

    lMessage.setChiaveAnnoFasCumulante    (mComp.getChiaveAnno());
    lMessage.setChiaveProgrFasCumulante   (mComp.getChiaveProgr());
    lMessage.setChiaveUfficioFasCumulante (mComp.getChiaveUfficio());   
     
    // Invio il messaggio
    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);
      
    //AGGIORNO LA DATA DI TRASMISSIONE DELL'EVENTO
    EventoModel lModel = lCrtlEve.ExRicercaEventoByKey(lIdEvento);
    
    //EventoModel lModel = new EventoModel();
    //lModel.setIdEvento(lIdEvento);
    lModel.setFlagDocumentoRegistrato("S");
    lModel.setDataTrasmissioneAtti (DateUtils.getSysDate());
    
    lModel.setDataAggiornamento         (DateUtils.getSysDate());
    lModel.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso() );
    lModel.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    lCtrl.ExModificaEventoTrasmissioneCompetenza(lModel, lFascicoloModel,  "01");
    
    //====================
    // setta la risposta nella request
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Trasmissione Provvedimento sottomessa al Sistema!");

    //Prepara la "pagina" di destinAction
    RedirectTo lRedirigi = new RedirectTo();
    lRedirigi.setPage( IWebConstants.PG_MAIN );
    lRedirigi.setAction( "siap.siep.richiesta.action.ActDettaglioTrasmissioneCompetenza" );
    lRedirigi.setParameter( ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lMessage.getIdMessaggio().toString() );
    lRedirigi.setParameter( ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString() );

    setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    return IWebConstants.PG_MESSAGE;
    //================================================================================
    
  }
}
