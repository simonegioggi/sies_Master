package siap.sico.log.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaLogAttivita</p>
* <p>Description: Classe Action per la load ricerca di LogAttivita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaLogAttivita extends ActionSiap implements ICostantiLogAttivita
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCALOGATTIVITA;  //restituisce la jsp di VIEW 

	 }

}