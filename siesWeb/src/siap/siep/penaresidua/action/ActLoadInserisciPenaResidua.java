package siap.siep.penaresidua.action;

import f3b.util.F3BException;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;


/**
* <p>Title: ActLoadInserisciPenaResidua</p>
* <p>Description: Classe Action per la load inserisci di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciPenaResidua extends ActionSiap implements ICostantiPenaResidua
{
	 public String processRequest() throws F3BException 
		{

	 // Inserire Eventuali ComboBOX
	 // Option lOption = new Option( DecodificheManager.getInstance().get???());

	 // setRequestAttribute("???", "" + lOption );
	 // Imposta Modalità.
	 setRequestAttribute("modalita", "I");
		 return PG_LOAD_INSERISCIPENARESIDUA;  //restituisce la jsp di VIEW 

		}



}