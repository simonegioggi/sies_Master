package siap.siep.competenza.action;


/**
* <p>Title: ActLoadInserisciCompetenza</p>
* <p>Description: Classe Action per la load inserisci di Competenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
//import f3b.web.html.Option; 

public class ActLoadInserisciCompetenza extends ActionSiap implements ICostantiCompetenza
{
 /*****************************************************************************
  * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche 
  * di precaricare tutti i dati da visualizzare i tale pagina (es: combo) 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {

    // Inserire Eventuali ComboBOX
    // Option lOption = new Option( DecodificheManager.getInstance().get???());
    // setRequestAttribute("???", "" + lOption );

    // Imposta la Modalità a Inserimento.
    setRequestAttribute("modalita", "I");

    // Restituisce la pagina di Inserimento dei Dati 
    return PG_LOAD_INSERISCICOMPETENZA; 
  }
}