package siap.siep.scadenzario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaScadenzarioDifferimento</p>
* <p>Description: Classe Action per la load ricerca di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaScadenzarioDifferimento extends ActionSiap implements ICostantiScadenzario
{
	public String processRequest() throws F3BException
	 {
		 return PG_LOAD_RICERCASCADENZARIODIFFERIMENTO;
	 }
}