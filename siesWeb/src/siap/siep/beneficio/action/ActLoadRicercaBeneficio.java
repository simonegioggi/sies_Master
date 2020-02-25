package siap.siep.beneficio.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaBeneficio</p>
* <p>Description: Classe Action per la load ricerca di Beneficio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaBeneficio extends ActionSiap implements ICostantiBeneficio
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCABENEFICIO;  //restituisce la jsp di VIEW 

	 }

}