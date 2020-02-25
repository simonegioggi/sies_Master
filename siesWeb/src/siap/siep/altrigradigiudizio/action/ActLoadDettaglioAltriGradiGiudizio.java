package siap.siep.altrigradigiudizio.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioAltriGradiGiudizio</p>
* <p>Description: Classe Action per la load dettaglio di AltriGradiGiudizio</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter( CAMPO_ID_ALTRIGRADIGIUDIZIO);
 		 // riempie il model
		 // chiama il controller

		 IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();
		 AltriGradiGiudizioModel llAltMod = lCtrl.ExRicercaAltriGradiGiudizioByKey(new BigDecimal(lId));
		 setRequestAttribute("altrogradogiudizio", llAltMod);

		 return PG_LOAD_DETTAGLIOALTRIGRADIGIUDIZIO;
	 }



}