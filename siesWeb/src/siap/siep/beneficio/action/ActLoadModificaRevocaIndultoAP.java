package siap.siep.beneficio.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaRevocaIndultoAP</p>
* <p>Description: Classe Action per la load Modifica Revoca Beneficio (tipo Indulto)</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaRevocaIndultoAP extends ActionSiap implements ICostantiBeneficio
{
  public String processRequest() throws Exception
  {
		 String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO);
 		 // riempie il model
		 // chiama il controller

		 IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		 BeneficioModel llBenMod = lCtrl.ExRicercaBeneficioByKey(new BigDecimal(lId));
		 setRequestAttribute("beneficio", llBenMod);
		 
		 Option lOption  = new Option( DecodificheManager.getInstance().getDPR(),llBenMod.getCodDpr());
		 setRequestAttribute("listaDPR", "" + lOption  );

		 Option lOptionAut  = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(),llBenMod.getRifCodTipoAutoEmittente());
		 setRequestAttribute("CodTipoAutoritaEmittente", "" + lOptionAut );
    	
		 Option lOptionProv = new Option( DecodificheManager.getInstance().getTipoProvvedimenti(),llBenMod.getRifCodTipoProvvedimento());
		 lOptionProv.setFilter( new String[] {"01", "02", "03"} );
		 setRequestAttribute("CodTipoProvvedimento", "" + lOptionProv );	    

		 // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    	
  		setRequestAttribute("ComingFromInsert", "");

    	setRequestAttribute("modalita", "M");

    	return PG_LOAD_MODIFICA_REVOCA_INDULTO;  //restituisce la jsp di VIEW
 
  }
}