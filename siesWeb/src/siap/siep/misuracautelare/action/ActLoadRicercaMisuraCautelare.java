package siap.siep.misuracautelare.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaMisuraCautelare</p>
* <p>Description: Classe Action per la load ricerca di MisuraCautelare</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAMISURACAUTELARE;  //restituisce la jsp di VIEW 

	 }

}