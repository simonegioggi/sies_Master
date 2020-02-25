package siap.siep.sentenzariunita.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaSentenzaRiunita</p>
* <p>Description: Classe Action per la load ricerca di SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCASENTENZARIUNITA;  //restituisce la jsp di VIEW 

	 }

}