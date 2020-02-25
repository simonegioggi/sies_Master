package siap.siep.altrigradigiudizio.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaAltriGradiGiudizio</p>
* <p>Description: Classe Action per la load ricerca di AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAALTRIGRADIGIUDIZIO;  //restituisce la jsp di VIEW 

	 }

}