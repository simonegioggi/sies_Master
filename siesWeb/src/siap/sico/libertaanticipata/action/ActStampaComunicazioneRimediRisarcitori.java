package siap.sico.libertaanticipata.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaComunicazioneRimediRisarcitori</p>
 * <p>Description: Action di Stampa dei provvedimenti di Comunicazione concessione
 *    Rimedi Risarcitori DL92/2014</p>
 * @author d.f.
 * @version 1.0
 * @since 10/2014
 */

public class ActStampaComunicazioneRimediRisarcitori extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEveModel = null;
    lEveModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    //==========================================
    // Recupero il template 
    //==========================================
    String flagTemplate = "0";

    TemplateModel lTemMod = new TemplateModel();

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "12", lEveModel.getCodMotivo(), flagTemplate);


    //==========================================================================
    // Preparo l'evento da passare la componente di stampa
    //==========================================================================
    EventoNotificaModel lEveNotMod = new EventoNotificaModel();

    lEveNotMod.getEvento().setIdEvento(lIdEvento);
    lEveNotMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveNotMod.getEvento().setDescrLuogoEmittente   (getUfficioUtenteConnesso().getDescrComune());
    lEveNotMod.getEvento().setDescrUfficioEmittente (getUfficioUtenteConnesso().getDescrTipoUfficio());

    lEveNotMod.getEvento().setCodUfficioAggiornamento   (getUfficioUtenteConnesso().getCodUfficio());
    lEveNotMod.getEvento().setCodOperatoreAggiornamento (this.getCodUtenteConnesso());
    lEveNotMod.getEvento().setDataAggiornamento         (DateUtils.getSysDate());

    lEveNotMod.getEvento().setFlagDocumentoRegistrato("N");

    lEveNotMod.setNomeTemplate(lTemMod.getIdTemplate());
    
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveNotMod, getUtenteConnesso());

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}