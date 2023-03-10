package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActStampaOrdineIngiunzione extends ActionSiap 
{
  private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    
    //==========================================================================
    // Recupero l'evento per il quale produrre la Stampa (comunicazione)
    //==========================================================================
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);

    
    String flagTemplate = "0"; // 0 = rata unica, 1 = pagamento rateizzato
    IRateizzazionePP lCtrlRate = SIEPLookupRemote.getRateizzazionePPRemote();
    Vector <RateizzazionePPModel> listaRateEvento = lCtrlRate.exRicercaRateizzazioniByIdEvento (lIdEvento);
    if ("U".equals(listaRateEvento.elementAt(0).getTipoRateizzazione()) )
        flagTemplate = "0";
     else if ("R".equals(listaRateEvento.elementAt(0).getTipoRateizzazione()))
        flagTemplate = "1";
        
    //==========================================================================
    // Recupero il template
    //==========================================================================
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),
                                                                              lEventoModel.getCodTipoProvvedimento(),   
                                                                              lEventoModel.getCodMotivo(),flagTemplate);

    siesLogger.debug("lTemMod = "+lTemMod);
    
    //==========================================================================
    // Genero il model Evento da passare alla funzione di stampa
    //==========================================================================
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();
    lEveNotMod.setNomeTemplate(lTemMod.getIdTemplate());

    lEveNotMod.getEvento().setIdEvento(lIdEvento);
    lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    

    lEveNotMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveNotMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
    
    lEveNotMod.getEvento().setDataAggiornamento         (DateUtils.getSysDate());
    lEveNotMod.getEvento().setCodUfficioAggiornamento   (lUff.getCodUfficio());
    lEveNotMod.getEvento().setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
    
    lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");
    
    //==========================================================================
    // Produce la stampa
    //==========================================================================
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, lUtenteMod);

    //==============================================
    // Setta il documento di stampa sulla response
    //==============================================
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}
