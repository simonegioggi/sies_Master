package siap.siep.reatopredisposto.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.DecodificheManager;


/**
* <p>Title: ActLoadInserisciReatoPredisposto</p>
* <p>Description: Classe Action per la load inserisci di ReatoPredisposto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciReatoPredisposto extends ActionSiap implements ICostantiReatoPredisposto
{
	 public String processRequest() throws F3BException 
		{

	 Option lOption  = new Option( DecodificheManager.getInstance().getTipoFonteReato() );
	 setRequestAttribute("TipiFontiReato", "" + lOption );
	 lOption  = new Option( DecodificheManager.getInstance().getSottonumerazione() );
	 setRequestAttribute("TipiSottonumerazione", "" + lOption );

	 setRequestAttribute("modalita", "I");
	 
	 return PG_LOAD_INSERISCIREATOPREDISPOSTO;  //restituisce la jsp di VIEW 

		}



}