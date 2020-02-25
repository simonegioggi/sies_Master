package siap.sius.udienzaprocedimento.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadRicercaUdienzaProcedimentoMag</p>
* <p>Description: Classe Action per la load ricerca di UdienzaProcedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaUdienzaProcedimentoMag extends ActionSiap implements ICostantiUdienzaProcedimento
{
	public String processRequest() throws F3BException
	 {
		 setRequestAttribute("tiporicerca", "xmagistrato");
		 return PG_LOAD_RICERCAUDIENZAPROCEDIMENTO;  //restituisce la jsp di VIEW
	 }
}