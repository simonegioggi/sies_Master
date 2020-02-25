package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

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
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
  * <p>Title: ActStampaRichiestaRevocaEspulsione</p>
  * <p>Description: Produce il documento</p>
  * <p>Copyright: Copyright (c) 2007</p>
  * <p>Company: </p>
  * @author not attributable
  * @version 1.0
  */

public class ActStampaRichiestaRevocaEspulsione extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();
    
    //==========================================================================
    // Recupero l'evento per il quale produrre la Stampa (Richiesta)
    //==========================================================================
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(lIdEvento);


//cerco il documento
    //==========================================================================
    // Recupero il template
    //==========================================================================
    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    TemplateModel lTemMod = new TemplateModel();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lEventoModel.getCodTipoEvento(),
                                                                              lEventoModel.getCodTipoProvvedimento(),	
                                                                              lEventoModel.getCodMotivo(),"0");
    //lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
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
    //ISanzioneSostitutiva lCtrlSanzSost = SIEPLookupRemote.getSanzioneSostitutivaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, lUtenteMod);
//    ByteArrayOutputStream lReport = lCtrlSanzSozt.exStampaAnnotazioneEspulsione(lEveNotMod, lUtenteMod);
    
    //==============================================
    // Setta il documento di stampa sulla response
    //==============================================
    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}