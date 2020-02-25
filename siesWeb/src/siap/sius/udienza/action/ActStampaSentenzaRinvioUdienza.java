package siap.sius.udienza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaSentenzaRinvioUdienza extends ActionSiap implements ICostantiUdienza
{
 /**
  * Azione di Stampa della Sentenza
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);

    EventoNotificaModel lEveMod = new EventoNotificaModel();
    // chiama il controller
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(new BigDecimal(lId));

    lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());


    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    lEveMod.getEvento().setTemIdTemplate("SIUS_SE_023");

    lEveMod.setNomeTemplate( lEveMod.getEvento().getTemIdTemplate() );

    IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lFasGPMod,lEveMod, super.getUtenteConnesso());

    setRequestAttribute("eventonotifica", lEveMod);
    setRequestAttribute("report", lReport);


    return IWebConstants.PG_DOWNLOAD_NEW;

  }

}