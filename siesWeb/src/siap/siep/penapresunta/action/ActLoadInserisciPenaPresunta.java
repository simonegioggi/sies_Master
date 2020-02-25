package siap.siep.penapresunta.action;

import f3b.util.F3BException;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;


/**
* <p>Title: ActLoadInserisciPenaPresunta</p>
* <p>Description: Classe Action per la load inserisci di PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciPenaPresunta extends ActionSiap implements ICostantiPenaPresunta
{
	 public String processRequest() throws F3BException 
		{

	 // Inserire Eventuali ComboBOX
	 // Option lOption = new Option( DecodificheManager.getInstance().get???());

	 // setRequestAttribute("???", "" + lOption );
	 // Imposta Modalità.
	 setRequestAttribute("modalita", "I");
		 return PG_LOAD_INSERISCIPENAPRESUNTA;  //restituisce la jsp di VIEW 

		}



}