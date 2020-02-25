package siap.siep.scarti.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaWScarti</p>
* <p>Description: Classe Action per la load ricerca di WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaWScarti extends ActionSiap implements ICostantiWScarti
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAWSCARTI;  //restituisce la jsp di VIEW 

	 }

}