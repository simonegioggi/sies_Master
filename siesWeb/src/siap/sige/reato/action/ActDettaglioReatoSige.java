package siap.sige.reato.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: ActLoadDettaglioReato</p>
 * <p>Description: Classe Action per la load dettaglio di Reato Sige</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActDettaglioReatoSige extends ActionSige implements ICostantiReato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// Reato Model 
	protected ReatoModel mReaMod;
	
  public String processRequest() throws Exception
  {
	// gestioneRitorno();
	setLinkRitorno();
	
	preparaDati();

    return PG_LOAD_DETTAGLIOREATO;
  }
  
  protected void preparaDati()  throws Exception
  {
	    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_REATO);

	    IReato lCtrl = SIEPLookupRemote.getReatoRemote();
	    mReaMod = lCtrl.ExRicercaReatoByKey(lId);
	    setRequestAttribute("reato", mReaMod);
	    setSessionAttribute("reato", mReaMod);

		setRequestAttribute("modo", "SIGE");

		if (mReaMod == null )
			throw new F3BException(F3BException.USER_MESSAGE, "Reato non trovato!");
		
		// Modificabilità del Reato
		String lReatoSigeModificabile = "N";
		if ( IsFascicoloSigeModificabile()  &&   getCodUfficioUtenteConnesso().equalsIgnoreCase(mReaMod.getCodUfficioInserimento()))
		{
			lReatoSigeModificabile = "S";
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + "reatSigeModificabile-> " + lReatoSigeModificabile);

		setRequestAttribute("modo", "SIGE");
		setRequestAttribute("reatoSigeModificabile", lReatoSigeModificabile);
		
  }
  
  
}