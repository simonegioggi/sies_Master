package siap.sico.utente.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaUtente</p>
* <p>Description: Classe Action per la load ricerca di Utente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaUtente extends ActionSiap implements ICostantiUtente
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAUTENTE;  //restituisce la jsp di VIEW 

	 }

}