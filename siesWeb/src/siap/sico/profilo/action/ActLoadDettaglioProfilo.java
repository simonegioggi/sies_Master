package siap.sico.profilo.action;

import java.math.BigDecimal;

import siap.sico.profilo.controller.IProfilo;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioProfilo</p>
* <p>Description: Classe Action per la load dettaglio di Profilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioProfilo extends ActionSiap implements ICostantiProfilo
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_COD_PROFILO);
 		 // riempie il model
		 // chiama il controller

		 IProfilo lCtrl = SICOLookupRemote.getProfiloRemote();
		 ProfiloModel llProMod = lCtrl.ExRicercaProfiloByKey(new BigDecimal(lId));
		 setRequestAttribute("profilo", llProMod);

		 return PG_LOAD_DETTAGLIOPROFILO;
	 }



}