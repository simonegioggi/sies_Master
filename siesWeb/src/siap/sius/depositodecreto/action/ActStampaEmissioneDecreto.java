package siap.sius.depositodecreto.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

//import siap.sius.udienza.controller.IUdienza;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
 * <p>Title: ActStampaFissazioneUdienza </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa Fissazione Udienza
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActStampaEmissioneDecreto  extends ActionSiap
 implements ICostantiDepositoDecreto
{
  public String processRequest() throws Exception
  {
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore   = getCodUtenteConnesso();
    String lCodiceUfficio     = getCodUfficioUtenteConnesso();
    UfficioModel lUfficio     = getUfficioUtenteConnesso();

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

    // Preleva l'Evento
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    EventoModel lEvento = lCtrlEve.ExRicercaEventoByKey( lKeyEvento );

    // Setta i dati per l'aggiornamento
    lEvento.setDataAggiornamento(DateUtils.getSysDate());
    lEvento.setCodUfficioAggiornamento(lCodiceUfficio);
    lEvento.setCodOperatoreAggiornamento(lCodiceOperatore);
    lEvento.setFlagDocumentoRegistrato("N");
    lEvento.setTemIdTemplate(getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));

//    lEvento.setNomeTemplate(ICostantiUdienza.TEMPLATE_FISSAZIONE_UDIENZA);

    // Crea il ByteArrayOutputStream
    IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampEmissioneDecreto( lEvento, lUfficio, super.getUtenteConnesso() );

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

//    return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;

  }
}





