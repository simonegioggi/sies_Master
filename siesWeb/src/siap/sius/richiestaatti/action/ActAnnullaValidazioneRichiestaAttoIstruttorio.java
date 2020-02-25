package siap.sius.richiestaatti.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAnnullaValidazioneRichiestaAttoIstruttorio</p>
 * <p>
 *  Description: Classe Azione responsabile di annullare la validazione.
 *  di un a richiesta di atto istruttorio.
 * </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Eutelia S.p.A.</p>
 * @author not attributable
 * @version 1.0
 */
public class ActAnnullaValidazioneRichiestaAttoIstruttorio  extends ActionSiap 
implements ICostantiRichiestaAtti
{
  public String processRequest() throws Exception
  {
    EventoModel lEveModel = null;

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

    // Istanzia l'Evento.
    lEveModel = new EventoModel();

    // Setta i dati per l'aggiornamento.
    lEveModel.setDataAggiornamento( DateUtils.getSysDate());
    lEveModel.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso() );
    lEveModel.setCodOperatoreAggiornamento( getCodUtenteConnesso() );
    lEveModel.setFlagDocumentoRegistrato("N");
    lEveModel.setIdEvento(lKeyEvento);

    // Update.
    IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
    lCtrlEve.ExAggiornaValidazione(lEveModel);

    // Prepara la "pagina" di destinAction.
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Annullata la validazione dell'atto istruttorio!");
    goToRitorno(); // Gestione ritorno.

    return IWebConstants.PG_MESSAGE;
  }
}