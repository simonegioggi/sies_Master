package siap.sige.provvInterlocutori.action;

/**
 * <p>Title:  ActLoadNominaPeriti</p>
 * <p>Description: Classe Action per la visualizzazione della 
 * Form di Input del Provvedimento per la nomina Periti. </p>
 * <p>Company: Agile</p>
 * <p> Author: Luigi </p>
 * @version 1.0
 */

public class ActLoadNominaPeriti extends ActLoadInserisciProvvInterlocutori
{
	public String processRequest() throws Exception
	  {

		// Valorizzazione della pagina di Load Inserimento specifica 
	    mRetPage = ICostantiProvvInterlocutoriSige.PG_LOAD_NOMINA_PERITI;

	    return super.processRequest();
	  }
}