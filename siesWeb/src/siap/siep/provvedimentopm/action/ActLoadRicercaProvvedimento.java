package siap.siep.provvedimentopm.action;


/**
* <p>Title: ActLoadRicercaProvvedimento</p>
* <p>Description: Classe Action per la load ricerca di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadRicercaProvvedimento extends ActionSiap implements ICostantiProvvedimento
{
public String processRequest() throws F3BException {

		 return PG_LOAD_RICERCAPROVVEDIMENTO;  //restituisce la jsp di VIEW 

}






}