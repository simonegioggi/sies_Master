package siap.sius.provvedimento.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActStampaFoglioComp </p>
 * <p>Description: Classe Azione responsabile della richiesta stampa Foglio Complementare
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActAnnullaValidazioneAllegato  extends ActionSiap
 implements ICostantiProvvedimento
{
  public String processRequest() throws Exception
  {
    DocumentoAllegatoModel lAllegatoModel = null;

    // Preleva dalla request la chiave dell'evento come parametro
    BigDecimal lKeyEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO );

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
