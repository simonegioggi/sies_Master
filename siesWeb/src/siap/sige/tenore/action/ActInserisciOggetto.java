package siap.sige.tenore.action;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
* <p>Title: ActInserisciOggetto</p>
* <p>Description: Classe Action per l'aggiornamento degli Oggetti Sige in sessione.</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActInserisciOggetto extends ActAggiornaTenoriSigeInSessione implements ICostantiTenoreSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception 
	{
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug(getClass().getName() + "processRequest : inizio");
	    
	    //[EC] Lettura del codice contenuto
        String codContenutoSelected = getRequestStringParameter(COD_CONTENUTO_SEL);
		// Lettura Id degli Oggetti selezionati
		StringTokenizer lCodOggetto = new StringTokenizer(getRequestStringParameter(CAMPO_COD_OGGETTO_SIGE) ,"|");
		
		// Lettura Id dei Titoli Esecutivi selezionati
		StringTokenizer lIdSentenza = new StringTokenizer(getRequestStringParameter(CAMPO_SEN_ID_SENTENZA) ,"|");

		// Lettura Id dei Reati selezionati
	    StringTokenizer lIdReati = new StringTokenizer(getRequestStringParameter(CAMPO_REA_ID_REATO) ,"|");

	    // Inserimento Tenori Sige
	    caricaTenori(lCodOggetto, lIdSentenza, lIdReati, "I", codContenutoSelected);

	    // Ritorno al punto di partenza
	    String lRetPage = ritornoDopoCancellazione("Oggetto aggiunto all'elenco", null);
		return lRetPage;
	}

}