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
import siap.siep.fungibilita.controller.IFungibilita;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaOSRimediRisarcitori</p>
 * <p>Description: Action di Stampa degli Ordini di scarcerazione concessione
 *    Rimedi Risarcitori DL92/2014</p>
 * @author d.f.
 * @version 1.0
 * @since 10/2014
 */

public class ActStampaOSRimediRisarcitori extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

    
    BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEveModel = null;
    lEveModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
    
    // Verifico se presente la fungibilità che è stata legata all'evento 
    // in fase di inserimento
    IFungibilita lCtrlFung = SIEPLookupRemote.getFungibilitaRemote();
    FungibilitaModel lFungModel = lCtrlFung.ExRicercaFungibilitaByKeyEvento(lIdEvento);
    
    String flagTemplate = "0";
    if (lFungModel!=null && lFungModel.getIdFungibilita()!=null) {
      flagTemplate = "1";
    }
    
    //==========================================
    // Recupero il template 
    //==========================================
    TemplateModel lTemMod = new TemplateModel();

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "09", lEveModel.getCodMotivo(), flagTemplate);

    if (lTemMod==null){
      throw new F3BException(F3BException.USER_MESSAGE, "Template non trovato");
    }

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