package siap.sige.provvInterlocutori.action;

/**
 * <p>Title:  ActLoadCitazioneTesti</p>
 * <p>Description: Classe Action per la visualizzazione della 
 * Form di Input del Provvedimento di Citazione Testi. </p>
 * <p>Company: Agile</p>
 * <p> Author: Luigi </p>
 * @version 1.0
 */

public class ActLoadCitazioneTesti extends ActLoadInserisciProvvInterlocutori 
			implements ICostantiProvvInterlocutoriSige
{
	public String processRequest() throws Exception
	  {

		// Valorizzazione della pagina di Load Inserimento specifica 
	    mRetPage = PG_LOAD_CITAZIONE_TESTI;

	    return super.processRequest();
	  }
}