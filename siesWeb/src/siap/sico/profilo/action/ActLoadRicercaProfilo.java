package siap.sico.profilo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaProfilo</p>
* <p>Description: Classe Action per la load ricerca di Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaProfilo extends ActionSiap implements ICostantiProfilo
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAPROFILO;  //restituisce la jsp di VIEW 

	 }

}