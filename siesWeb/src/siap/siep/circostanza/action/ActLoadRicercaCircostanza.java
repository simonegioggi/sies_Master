package siap.siep.circostanza.action;

import siap.sico.web.ActionSiap;

/**
* <p>Title: ActLoadRicercaCircostanza</p>
* <p>Description: Classe Action per la load ricerca di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaCircostanza extends ActionSiap implements ICostantiCircostanza
{
	public String processRequest() throws Exception
	 {

		 return PG_LOAD_RICERCACIRCOSTANZA;  //restituisce la jsp di VIEW

	 }

}