package siap.sius.tenore.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaTenore</p>
* <p>Description: Classe Action per la load ricerca di Tenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaTenore extends ActionSiap implements ICostantiTenore
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCATENORE;  //restituisce la jsp di VIEW 

	 }

}