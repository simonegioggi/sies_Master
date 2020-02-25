package siap.siep.penaresidua.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioPenaResidua</p>
* <p>Description: Classe Action per la load dettaglio di PenaResidua</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioPenaResidua extends ActionSiap implements ICostantiPenaResidua
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_PENA_RESIDUA);
 		 // riempie il model
		 // chiama il controller

		 IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		 PenaResiduaModel llPenMod = lCtrl.ExRicercaPenaResiduaByKey(new BigDecimal(lId));
		 setRequestAttribute("penaresidua", llPenMod);

		 return PG_LOAD_DETTAGLIOPENARESIDUA;
	 }



}