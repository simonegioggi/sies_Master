package siap.sige.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAnnullaValidazioneAllegato </p>
 * <p>Description: Classe Action responsabile dell' annullamento Validazione dell'Allegato
 * </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActAnnullaValidazioneAllegato  extends ActionSige
 	implements ICostantiProvvedimentoSige
{
  public String processRequest() throws Exception
  {
    DocumentoAllegatoModel lAllegatoModel = null;

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    // Istanzia l'Allegato
    lAllegatoModel = new DocumentoAllegatoModel();

    // Setta i dati per l'aggiornamento
    lAllegatoModel.setDataAggiornamento( DateUtils.getSysDate());
    lAllegatoModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lAllegatoModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lAllegatoModel.setFlagDocumentoRegistrato("N");
    lAllegatoModel.setEveIdEvento(lKeyEvento);

    // Update
    IDocumentoAllegato lCtrlAllegato = SIUSLookupRemote.getDocumentoAllegatoRemote();
    lCtrlAllegato.ExAggiornaValidazione(lAllegatoModel);

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Annullata la Validazione degli Allegati!");
    goToRitorno();

    return IWebConstants.PG_MESSAGE;
  }
}
