package siap.sius.prescrizione.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaPrescrizione</p>
* <p>Description: Classe Action per la load ricerca di Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaPrescrizione extends ActionSiap implements ICostantiPrescrizione
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAPRESCRIZIONE;  //restituisce la jsp di VIEW 

	 }

}