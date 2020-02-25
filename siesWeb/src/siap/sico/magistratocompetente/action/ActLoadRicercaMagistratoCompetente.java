package siap.sico.magistratocompetente.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaMagistratoCompetente</p>
* <p>Description: Classe Action per la load ricerca di MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaMagistratoCompetente extends ActionSiap implements ICostantiMagistratoCompetente
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAMAGISTRATOCOMPETENTE;  //restituisce la jsp di VIEW 

	 }

}