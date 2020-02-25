package siap.siep.altrigradigiudizio.action;

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;
//import per le combo
//import f3b.web.html.Option;
//import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.DecodificheManager;


/**
* <p>Title: ActLoadInserisciAltriGradiGiudizio</p>
* <p>Description: Classe Action per la load inserisci di AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio
{
	 public String processRequest() throws F3BException 
		{

		// Inserire Eventuali ComboBOX
		    //Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
	  		Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");

		    // Imposta i provvedimenti Rif.
		    setRequestAttribute("autoritaEmi", "" + lOption );
		    
		 // Imposta i provvedimenti Rif.
		    setRequestAttribute("autoritaProvRif", "" + lOption );

		    lOption = new Option( DecodificheManager.getInstance().getTipoDecisioneCassazione(), "-");
		    // Imposta la decisione cassazione
		    setRequestAttribute("tipoDecisioneCassazione", "" + lOption );
		    
		    lOption = new Option( DecodificheManager.getInstance().getTipoProvvedimentiRif(), "-");

		    setRequestAttribute("tipoProvvedimentiRif", "" + lOption );
		    
		    // Imposta Modalità.
		    setRequestAttribute("modalita", "I");

		    lOption = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
		    setRequestAttribute("tipoRito1", "" + lOption );
		    setRequestAttribute("tipoRito2", "" + lOption );
		    
		 return PG_LOAD_INSERISCIALTRIGRADIGIUDIZIO;  //restituisce la jsp di VIEW 

		}



}