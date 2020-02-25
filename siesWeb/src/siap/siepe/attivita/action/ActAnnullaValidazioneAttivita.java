package siap.siepe.attivita.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.attivita.controller.IAttivita;
import siap.siepe.attivita.model.AttivitaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAnnullaValidazioneAttivita </p>
 * <p>Description: Classe Azione responsabile dell'annullamento della validazione di un'attività
 * </p>
 * <p>Copyright: Bull Italia S.p.A. Copyright (c) 2006</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @author not attributable
 * @version 1.0
 */
public class ActAnnullaValidazioneAttivita  extends ActionSiap implements ICostantiAttivita
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    AttivitaModel lAttModel = null;

    // Preleva dalla request la chiave della richiesta come parametro
    BigDecimal lKey = getRequestBigDecimalParameter(ICostantiAttivita.CAMPO_ID_ATTIVITA );

    // Istanzia il model  dell'attivita
    lAttModel = new AttivitaModel();

    // Imposta i dati per l'aggiornamento
    lAttModel.setDataAggiornamento( DateUtils.getSysDate());
    lAttModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lAttModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lAttModel.setFlagDocumentoRegistrato("N");
    lAttModel.setIdAttivita(lKey);

    // Update
    IAttivita lCtrl = SIEPELookupRemote.getAttivitaRemote();
    lCtrl.ExAggiornaValidazioneAttivita(lAttModel);

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Annullata la Validazione della Attivita!");

    goToRitorno();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return IWebConstants.PG_MESSAGE;
  }
}