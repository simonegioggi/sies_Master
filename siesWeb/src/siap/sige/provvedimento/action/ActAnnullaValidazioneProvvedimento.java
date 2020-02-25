package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAnnullaValidazioneProvvedimento </p>
 * <p>Description: Classe Action responsabile dell' annullamento Validazione del Provvedimento
 * </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActAnnullaValidazioneProvvedimento  extends ActionSige
 			implements ICostantiProvvedimentoSige
{
  public String processRequest() throws Exception
  {
    EventoModel lEveModel = null;

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

    // Istanzia l'Evento
    lEveModel = new EventoModel();

    // Setta i dati per l'aggiornamento
    lEveModel.setDataAggiornamento( DateUtils.getSysDate());
    lEveModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lEveModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lEveModel.setFlagDocumentoRegistrato("N");
    lEveModel.setIdEvento(lKeyEvento);

    // Update
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    lCtrlEve.ExAggiornaValidazione(lEveModel);

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Annullata la Validazione del Provvedimento!");
    goToRitorno();

    return IWebConstants.PG_MESSAGE;
  }
}
