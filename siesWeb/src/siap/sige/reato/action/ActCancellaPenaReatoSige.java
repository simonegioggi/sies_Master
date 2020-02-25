package siap.sige.reato.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.lock.model.LockModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.model.ReatoModel;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

public class ActCancellaPenaReatoSige extends ActInserisciPenaReatoSige implements ICostantiReato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(getClass().getName() + ".processRequest : inizio");

    //Controllo che non si stia lavorando su una entità in modifica ad altri
    LockModel lck = lockIfNotLocked("reato", getRequestStringParameter(CAMPO_ID_REATO), getCodUtenteConnesso());
    if (lck != null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
      return IWebConstants.PG_MESSAGE;
    }
	
	// Si instanzia il model
	ReatoSentenzaSigeModel lReaSigeMod = new ReatoSentenzaSigeModel();
	
	// Lettura ID Reato dalla request
	lReaSigeMod.setIdReato(getRequestBigDecimalParameter(CAMPO_ID_REATO));
	
	// Lettura FAS_SIGE_SEN_ID dalla session
	BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
	lReaSigeMod.setFasSigeSenId(lIdFasSigeSen);

	// Valorizzazione dei campi da annullare
	lReaSigeMod.setCodTipoPenaDetentiva("-");
	lReaSigeMod.setCodTipoSanzione("-");

	// Scrittura nel DB
	ReatoModel lReaModRet = inserimento(lReaSigeMod);
	
	// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug( "Inserito Pena Reato SIGE : " + lReaModRet.getIdReato());
  
	//return paginaDestinazione("siap.sige.reato.action.ActDettaglioReatoSige", lReaModRet.getIdReato());
	  return ritornoDopoCancellazione(" Pena Reato cancellato", null);

  }
}