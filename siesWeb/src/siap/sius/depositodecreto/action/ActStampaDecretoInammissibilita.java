package siap.sius.depositodecreto.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaDecretoInammissibilita extends ActionSiap
implements ICostantiDepositoDecreto
{
	/**
  * Azione di Stampa dell'decreto d'inammissibilità.
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @return la pagina di ritorno.
  * @throws Exception propaga errore di eccezione.
  */
  public String processRequest() throws Exception
  {
    // Preleva dati di sessione.
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
    // Preelva l'evento dalla request.
    BigDecimal lIdEvento = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO );

    // Legge l'evento + notifiche dal dbase.
    // STUB : 20030917 - Inutile ?
    EventoNotificaModel lEveMod = new EventoNotificaModel();
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    lEveMod = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEvento);

    lEveMod.getEvento().setIdEvento(lIdEvento); //STUB : 20030917 - Inutile ?
    // STUB : 20030917 - Inutile ?
    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

    UfficioModel lUff = getUfficioUtenteConnesso();
    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

    lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.getEvento().setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lEveMod.getEvento().setFlagDocumentoRegistrato("N");

    lEveMod.setNomeTemplate(TEMPLATE_DECRETO_INAMMISSIBILITA);
    lEveMod.getEvento().setTemIdTemplate(TEMPLATE_DECRETO_INAMMISSIBILITA);

    IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
//    ByteArrayOutputStream lReport = lCtrl.ExStampaDecreto(lFasGPMod,lEveMod);
    ByteArrayOutputStream lReport = lCtrl.ExStampEmissioneDecreto(lEveMod.getEvento(), getUfficioUtenteConnesso(), super.getUtenteConnesso());

    // setta la risposta nella request
    setRequestAttribute("eventonotifica", lEveMod);
    setRequestAttribute("report", lReport);
//    return IWebConstants.PG_DOWNLOAD;

    return IWebConstants.PG_DOWNLOAD_NEW;
  }

}