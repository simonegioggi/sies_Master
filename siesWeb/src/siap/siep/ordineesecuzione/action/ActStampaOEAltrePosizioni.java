package siap.siep.ordineesecuzione.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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

public class ActStampaOEAltrePosizioni extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws F3BException
  {
    String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();
    UfficioModel lUff = this.getUfficioUtenteConnesso();

    IEvento lCtrl = SICOLookupRemote.getEventoRemote();
    EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
    String lMotivo = lEventoModel.getCodMotivo();

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    String istanza = this.getRequestStringParameter("istanza");
    String finePena = this.getRequestStringParameter("finePena");

    lEveMod.setNomeTemplate(TEMPLATE_ALTRE_POSIZIONI_VUOTO);

    if (istanza.equals("S") && finePena.equals("S"))
    {
      lEveMod.setNomeTemplate(TEMPLATE_ALTRE_POSIZIONI_VUOTO);
    }
    else if (istanza.equals("N") && finePena.equals("S"))
    {
      lEveMod.setNomeTemplate(TEMPLATE_ALTRE_POSIZIONI_VUOTO);
    }

    if("0246".equals(lMotivo))
    {
      ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
      TemplateModel lTemMod = null;
      lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, lMotivo, "0");
      lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
    }

    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta nella request

//Prepara la pagina di destinazione
    setRequestAttribute("report", lReport);
    setRequestAttribute("fc", getRequestStringParameter("fc"));

    return IWebConstants.PG_DOWNLOAD;
  }
}