package siap.sius.decretounificazione.action;

/**
* <p>Title: ActLoadVerificaDecretoUnificazione</p>
* <p>Description: Classe Action per la load di VerificaDecretoUnificazione</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;

public class ActLoadVerificaDecretoUnificazione extends ActionSiap
implements ICostantiDecretoUnificazione
{
	public String processRequest() throws Exception
	{
    return PG_LOAD_VERIFICADECRETOUNIFICAZIONE; //restituisce la jsp di VIEW
	}
}