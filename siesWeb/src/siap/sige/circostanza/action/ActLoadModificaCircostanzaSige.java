package siap.sige.circostanza.action;

import siap.siep.circostanza.action.ActLoadModificaCircostanza;

/**
* <p>Title: ActLoadModificaCircostanzaSige</p>
* <p>Description: Classe Action per la load modifica di Circostanza Sige. 
* Specializza ActLoadModificaCircostanza per utilizzare la stessa funzione 
* per la preparazione della form.</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: Agile</p>
* @version 1.0
*/

public class ActLoadModificaCircostanzaSige extends ActLoadModificaCircostanza
{
	public String processRequest() throws Exception
	{
		String lRet = elaborazione();
		setRequestAttribute("modo", "SIGE");
    	return lRet;
	}

}