package siap.siep.penaaccessoria.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaPenaAccessoria</p>
* <p>Description: Classe Action per la load ricerca di PenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAPENAACCESSORIA;  //restituisce la jsp di VIEW 

	 }

}