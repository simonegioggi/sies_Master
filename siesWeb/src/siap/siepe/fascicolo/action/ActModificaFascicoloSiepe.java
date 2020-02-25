package siap.siepe.fascicolo.action;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSiepeModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActModificaFascicoloSiepe extends ActionSiap implements ICostantiFascicoloSiepe
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest() : inizio");

    // Istanzia e riempie il model
    FascicoloSiepeModel lFascicoloSiepe = new FascicoloSiepeModel();
    // Imposta i dati interessati alla modifica.
    lFascicoloSiepe.setIdFascicoloSiepe(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEPE));
    lFascicoloSiepe.setAnnoUepe(getRequestBigDecimalParameter(CAMPO_ANNO_UEPE));
    lFascicoloSiepe.setNumUepe(getRequestBigDecimalParameter(CAMPO_NUM_UEPE));
    lFascicoloSiepe.setProgrUepe(getRequestBigDecimalParameter(CAMPO_PROGR_UEPE));
    lFascicoloSiepe.setNote( getRequestStringParameter(CAMPO_NOTE) );
    // Imposta i dati dell'operatore, ufficio e data aggiornamento record
    lFascicoloSiepe.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lFascicoloSiepe.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lFascicoloSiepe.setDataAggiornamento(DateUtils.getSysDate());

    // Chiama il controller per eseguire l'update.
    IFascicoloSiepe lCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
    FascicoloSiepeModel lFasSiepeRet = lCtrl.ExModificaFascicoloSiepe(lFascicoloSiepe);

    // Preepara il path della Action di ritorno.
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" +
            IWebConstants.ACTION_FIELD + "=siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe&"+
            CAMPO_ID_FASCICOLO_SIEPE+"="+lFasSiepeRet.getIdFascicoloSiepe().toString();

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

    return lPage;
  }
}