package siap.sige.sentenza.action;

/**
 * <p>Title: ActLoadAssegnazione</p>
 * <p>Description: Classe Azione di visualizzazione della form 
 * per l'assegnazione di una Sentenza o di un Procedimento SIEP 
 * ad un Procedimento SIGE.
  * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */

import java.util.Vector;

import org.apache.log4j.Logger;

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadAssegnazione extends ActionSige implements ICostantiFasSigeSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	FascicoloSigeModel mFascicolo = null;

  public String processRequest() throws Exception
  {
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug(getClass().getName() + ".processRequest : inizio");
	
	 // Si richiama il lock
	 
	 String isSentenza = "false";
	 if( !isRequestParameterNullObj("isSentenza")) {
	     isSentenza =super.getRequestStringParameter("isSentenza");
	     super.setSessionAttribute("isSentenza", isSentenza);  
	 }
	 
	 
	 try {
	     lockApplicativo("Sentenza");
	 } catch (F3BException frb) {
		 RedirectTo lPage=new RedirectTo();
		 lPage.setPage(IWebConstants.PG_MAIN);
         lPage.setAction( "siap.sige.sentenza.action.ActLoadFSIscrizioneSenDecSenDelib");
         return lPage.toString();
	 }

	 // Controllo univocità del Titolo di competenza
	 if(esisteTitoloCompetenza())
	 {
		   setRequestAttribute("titoloCompetenza", "S");
	 }
	 
	 // Se l'assegnazione è di un Fascicolo SIEP
   	 // Trasformazione di Sentenza in SentenzaSige in sessione solo se associo il Fascicolo SIEP
	 if( !isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP) && !isSessionAttributeNullObj("fascicolo") && !isSessionAttributeNullObj("sentenza") )
	 {
		 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 siesLogger.debug("Fascicolo SIEP");
		 FascicoloSiepModel lFascicoloSiep = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		 SentenzaModel lSentenza = (SentenzaModel) getSessionAttribute("sentenza");
		 SentenzaSigeModel lSentenzaSige = new SentenzaSigeModel(lSentenza);
		 lSentenzaSige.setFascicoloSiep(lFascicoloSiep);
		 setSessionAttribute("sentenza", lSentenzaSige);
	 }
	 else
	 {
		 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 siesLogger.debug("Sentenza ");
		 removeSessionAttribute("fascicolo");
	 }
	 super.setRequestAttribute("titolo", "Inserimento Sentenza, Decreto Penale, Sentenza Straniera Delibata");
	 super.setRequestAttribute("isSentenza", isSentenza);
	 // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 siesLogger.debug(getClass().getName() + ".processRequest : fine");
	 return PG_LOAD_ASSEGNA_SENTENZA; 
	 
   }
  
  /**
   * La funzione effettua un controllo per determinare se già esiste Titolo di Competenza per il Fascicolo
   * @return
   * @throws F3BException
   */
	protected boolean esisteTitoloCompetenza() throws F3BException
	{
		// Interfaccia del Controller Sentenza Sige
		IFasSigeSentenza lCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

		boolean risultato = false;
		
		  // I dati del Fascicolo Sige sono in sessione 
		FascicoloSigeModel lFascicolo = getFascicoloSigeInSessione();
		  if (lFascicolo == null || lFascicolo.getIdFascicoloSige() == null)
			  throw new F3BException(F3BException.USER_MESSAGE, "Mancano i dati del Fascicolo in sessione !");

		// Ricerca di Sentenza associata al Fascicolo con FlagCompetenza ad "S"
		SentenzaSigeModel lFasSigeSen = new SentenzaSigeModel();
		lFasSigeSen.setFasIdFascicoloSige(lFascicolo.getIdFascicoloSige());
		lFasSigeSen.setFlagCompetenza("S");
		
		@SuppressWarnings("rawtypes")
		Vector lVect = lCtrl.ExRicercaFasSigeSentenza(lFasSigeSen);
		
		if (lVect != null && lVect.size() > 0)
			risultato = true;
		
		return risultato;
	}
}