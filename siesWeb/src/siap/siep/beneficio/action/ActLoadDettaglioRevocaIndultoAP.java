package siap.siep.beneficio.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioRevocaIndultoAP</p>
* <p>Description: Classe Action per la load dettaglio del Beneficio Revocato (tipo Indulto)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioRevocaIndultoAP extends ActionSiap implements ICostantiBeneficio
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);
 		 // riempie il model
		 // chiama il controller

		 IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		 BeneficioModel llBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));
		 setRequestAttribute("beneficio", llBenMod);

		 return PG_LOAD_DETTAGLIO_REVOCA_INDULTO;
	 }



}