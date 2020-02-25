package siap.sige.sentenza.action;

/**
 * <p>Title: ActLoadModificaAssegnazione</p>
 * <p>Description: Classe Azione di visualizzazione della form 
 * per la Modifica dell'assegnazione di una Sentenza o di un Procedimento SIEP 
 * ad un Procedimento SIGE. 
 * I dati modificabili sono "data di irrevocabilità" e "flag di competenza".
  * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

public class ActLoadModificaAssegnazione extends ActLoadAssegnazione implements ICostantiFasSigeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	FascicoloSigeModel mFascicolo = null;

  public String processRequest() throws Exception
  {
	   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	   siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	
	   // Si richiama il lock 
	   lockApplicativo("Sentenza");
	  
	   gestioneRitorno();
	   setRequestAttribute("modalita","M" );
	   
	   
		// Lettura dalla request ID_FAS_SISGE_SENTENZA
		BigDecimal lIdFasSigeSen = getRequestBigDecimalParameter(CAMPO_ID_FAS_SIGE_SENTENZA);
			 
		// Viene istanziato il controller per effettuare la ricerca 
		IFasSigeSentenza lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		SentenzaSigeModel lSentenzaSige = lCtrl.ExRicercaFasSigeSentenzaByKey(lIdFasSigeSen);

		setSessionAttribute("sentenza",lSentenzaSige );
		setRequestAttribute("sentenza",lSentenzaSige );
			
		// Viene messo in sessione ID_FAS_SISGE_SENTENZA 
		setSessionAttribute(CAMPO_ID_FAS_SIGE_SENTENZA, lIdFasSigeSen);

	   // Se il Titolo Esecutivo non è quello di competenza
		if(lSentenzaSige.getFlagCompetenza() != null && lSentenzaSige.getFlagCompetenza().compareToIgnoreCase("S") != 0)
		{
			// Controllo univocità del Titolo di competenza 
			if ( esisteTitoloCompetenza())
				setRequestAttribute("titoloCompetenza", "S");
		}
		 

	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug(getClass().getName() + ".processRequest : fine");
	  return PG_LOAD_ASSEGNA_SENTENZA;  
   }

}