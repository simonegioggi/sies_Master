package siap.sico.soggettodattilo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadInserisciSoggettoDattilo</p>
* <p>Description: Classe Action per la load inserisci di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	public String processRequest() throws F3BException {
		setRequestAttribute("modalita", "I");
		return PG_LOAD_INSERISCISOGGETTODATTILO; // restituisce la jsp di VIEW
	}

}