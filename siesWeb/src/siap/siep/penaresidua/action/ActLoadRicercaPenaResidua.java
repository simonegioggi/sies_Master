package siap.siep.penaresidua.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaPenaResidua</p>
* <p>Description: Classe Action per la load ricerca di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaPenaResidua extends ActionSiap implements ICostantiPenaResidua
{
	public String processRequest() throws F3BException 
	 {

		 return PG_LOAD_RICERCAPENARESIDUA;  //restituisce la jsp di VIEW 

	 }

}