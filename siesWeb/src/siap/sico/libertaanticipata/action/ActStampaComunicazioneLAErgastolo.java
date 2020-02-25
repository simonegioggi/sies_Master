package siap.sico.libertaanticipata.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaComunicazioneLAErgastolo extends ActionSiap
{
  public String processRequest() throws F3BException
  {
    BigDecimal lId = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    EventoNotificaModel lEveModel = new EventoNotificaModel();
    lEveModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lId);

    String lMotivo = lEveModel.getEvento().getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(lId);
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    String flagTemplate = "0";

    TemplateModel lTemMod = new TemplateModel();

    ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
    lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", "12", lMotivo, flagTemplate);

    lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}