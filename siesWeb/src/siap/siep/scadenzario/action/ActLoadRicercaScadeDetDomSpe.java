package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import siap.web.ISIAPCostantiWeb;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaScadenzario</p>
* <p>Description: Classe Action per la load ricerca di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaScadeDetDomSpe extends ActionSiap implements ICostantiScadenzario
{
	public String processRequest() throws F3BException
	 {
    //avviso di pagina in costruzione
     return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;

    // scommentare appena si toglie l'avviso
		// return PG_LOAD_RICERCA_SCADE_DET_DOM_SPE;  //restituisce la jsp di VIEW

	 }

}