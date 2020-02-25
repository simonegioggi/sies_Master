package siap.sico.soggettodattilo.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRicercaSoggettoDattilo</p>
* <p>Description: Classe Action per la load ricerca di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	public String processRequest() throws F3BException {
		return PG_LOAD_RICERCASOGGETTODATTILO; // restituisce la jsp di VIEW
	}

}