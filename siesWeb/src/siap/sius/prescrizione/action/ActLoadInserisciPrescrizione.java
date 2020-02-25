package siap.sius.prescrizione.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadInserisciPrescrizione</p>
* <p>Description: Classe Action per la load inserisci di Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciPrescrizione extends ActionSiap implements ICostantiPrescrizione
{
	 public String processRequest() throws F3BException
		{
                  setRequestAttribute("nextaction",getRequestStringParameter("nextaction"));
       //           setRequestAttribute("UffMagComp", getRequestStringParameter("UffMagComp"));
        //          setRequestAttribute("LuogoProva", getRequestStringParameter("LuogoProva"));
                  setRequestAttribute("modalita", "I");
                  setRequestAttribute("IDEvento", getRequestStringParameter("IDEvento"));
		 return PG_LOAD_INSERISCIPRESCRIZIONE;  //restituisce la jsp di VIEW

		}

}
