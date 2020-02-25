package siap.siep.penapresunta.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaPenaPresunta</p>
* <p>Description: Classe Action per la load ricerca di PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaPenaPresunta extends ActionSiap implements ICostantiPenaPresunta
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAPENAPRESUNTA;  //restituisce la jsp di VIEW 

	 }

}