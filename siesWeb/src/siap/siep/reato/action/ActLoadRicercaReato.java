package siap.siep.reato.action;

import siap.sico.web.ActionSiap;


/**
* <p>Title: ActLoadRicercaReato</p>
* <p>Description: Classe Action per la load ricerca di Reato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaReato extends ActionSiap implements ICostantiReato
{
	public String processRequest() throws Exception
	 {

		 return PG_LOAD_RICERCAREATO;  //restituisce la jsp di VIEW

	 }

}