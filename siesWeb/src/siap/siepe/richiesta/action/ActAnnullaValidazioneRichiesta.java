package siap.siepe.richiesta.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.richiesta.controller.IRichiesta;
import siap.siepe.richiesta.model.RichiestaModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
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

public class ActAnnullaValidazioneRichiesta  extends ActionSiap implements ICostantiRichiesta
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

    RichiestaModel lRichModel = null;

    // Preleva dalla request la chiave della richiesta come parametro
    BigDecimal lKeyRichiesta = getRequestBigDecimalParameter(ICostantiRichiesta.CAMPO_ID_RICHIESTA );

    // Istanzia la Richiesta Model
    lRichModel = new RichiestaModel();

    // Imposta i dati per l'aggiornamento
    lRichModel.setDataAggiornamento( DateUtils.getSysDate());
    lRichModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lRichModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lRichModel.setFlagDocumentoRegistrato("N");
    lRichModel.setIdRichiesta(lKeyRichiesta);

    // Update
    IRichiesta lCtrl = SIEPELookupRemote.getRichiestaRemote();
    lCtrl.ExAggiornaValidazioneRichiesta(lRichModel);

    //Prepara la "pagina" di destinAction
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Annullata la Validazione della Richiesta!");

    goToRitorno();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return IWebConstants.PG_MESSAGE;
  }
}