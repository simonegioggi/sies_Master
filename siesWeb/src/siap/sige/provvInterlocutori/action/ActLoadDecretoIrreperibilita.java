package siap.sige.provvInterlocutori.action;

/**
 * <p>Title:  ActLoadDecretoIrreperibilita</p>
 * <p>Description: Classe Action per la visualizzazione della 
 * Form di Input del Decreto di Irreperibilità. </p>
 * <p>Company: Agile</p>
 * <p> Author: Luigi </p>
 * @version 1.0
 */


public class ActLoadDecretoIrreperibilita extends ActLoadInserisciProvvInterlocutori
		implements ICostantiProvvInterlocutoriSige
{
	public String processRequest() throws Exception
	  {

		// Valorizzazione della pagina di Load Inserimento specifica 
	    mRetPage = PG_LOAD_DECRETO_IRREPERIBILITA;

	    return super.processRequest();
	  }

}