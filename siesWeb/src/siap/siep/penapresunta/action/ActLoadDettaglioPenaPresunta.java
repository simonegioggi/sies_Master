package siap.siep.penapresunta.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.penapresunta.controller.IPenaPresunta;
import siap.siep.penapresunta.model.PenaPresuntaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioPenaPresunta</p>
* <p>Description: Classe Action per la load dettaglio di PenaPresunta</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioPenaPresunta extends ActionSiap implements ICostantiPenaPresunta
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_PENA_PRESUNTA);
 		 // riempie il model
		 // chiama il controller

		 IPenaPresunta lCtrl = SIEPLookupRemote.getPenaPresuntaRemote();
		 PenaPresuntaModel llPenMod = lCtrl.ExRicercaPenaPresuntaByKey(new BigDecimal(lId));
		 setRequestAttribute("penapresunta", llPenMod);

		 return PG_LOAD_DETTAGLIOPENAPRESUNTA;
	 }



}