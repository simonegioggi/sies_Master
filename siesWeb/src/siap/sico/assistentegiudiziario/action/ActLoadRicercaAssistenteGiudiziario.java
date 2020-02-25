package siap.sico.assistentegiudiziario.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaAssistenteGiudiziario</p>
* <p>Description: Classe Action per la load ricerca di AssistenteGiudiziario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAASSISTENTEGIUDIZIARIO;  //restituisce la jsp di VIEW 

	 }

}