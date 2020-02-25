package siap.sige.misurasicurezza.action;

import siap.siep.misurasicurezza.action.ActLoadInserisciMisuraSicurezza;


/**
* <p>Title: ActLoadInserisciMisuraSicurezzaSige</p>
* <p>Description: Classe Action per la load inserisci di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadInserisciMisuraSicurezzaSige extends ActLoadInserisciMisuraSicurezza 
{
   public String processRequest() throws Exception
  {
	   preparaForm();
	   setRequestAttribute("modo", "SIGE");

	   return PG_LOAD_INSERISCIMISURASICUREZZA;  //restituisce la jsp di VIEW
  }
 
}