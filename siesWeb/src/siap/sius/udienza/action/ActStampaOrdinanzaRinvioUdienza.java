package siap.sius.udienza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaOrdinanzaRinvioUdienza extends ActionSiap implements ICostantiUdienza
{
	/**
  * Azione di Stampa dell'Ordinanza
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

    // genny 08/03/2004
    lEveMod.getEvento().setTemIdTemplate("SIUS_OR_603");

    //lEveMod.setNomeTemplate(TEMPLATE_ORDINANZA_AFFIDAMENTO);
    lEveMod.setNomeTemplate( lEveMod.getEvento().getTemIdTemplate() );

    IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lFasGPMod,lEveMod, super.getUtenteConnesso());		 // setta la risposta nella request

    setRequestAttribute("eventonotifica", lEveMod);
    setRequestAttribute("report", lReport);


  //  return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;

  }

}