package siap.sico.evento.action;


/**
* <p>Title: ActLoadRicercaEvento</p>
* <p>Description: Classe Action per la load ricerca di Evento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaEvento extends ActionSiap implements ICostantiEvento
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAEVENTO;  //restituisce la jsp di VIEW 

	 }

}
