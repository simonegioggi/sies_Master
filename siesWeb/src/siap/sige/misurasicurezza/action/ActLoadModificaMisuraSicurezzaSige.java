package siap.sige.misurasicurezza.action;
import siap.siep.misurasicurezza.action.ActLoadModificaMisuraSicurezza;


/**
* <p>Title: ActLoadModificaMisuraSicurezzaSige</p>
* <p>Description: Classe Action per la load inserisci di MisuraSicurezza</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadModificaMisuraSicurezzaSige extends ActLoadModificaMisuraSicurezza 
{
   public String processRequest() throws Exception
  {
	   setRequestAttribute("modo", "SIGE");

	   return super.processRequest();  //restituisce la jsp di VIEW
  }
 
}