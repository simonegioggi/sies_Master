package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
* <p>Title: ActDettaglioPenaAccessoriaCumulo</p>
* <p>Description: Classe per la load del dettaglio di PenaAccessoria di titolo Cumulato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActDettaglioPenaAccessoriaCumulo extends ActionModuloCumulo implements ICostantiPenaAccessoriaCumulo
{
	 protected PenaAccessoriaCumuloModel mPenMod = new PenaAccessoriaCumuloModel();

	 public String processRequest() throws Exception 
	 {
 		 String lId = getRequestStringParameter(CAMPO_ID_PENA_ACCESSORIA_CUMULO);
 		 
 		//==========================================================================
 	    // Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
 	    // di DettaglioTitoloCumulato.jsp eDettaglioPenaAccessoriaCumulo
 	    //==========================================================================
 	    super.getDatiIstruttoria();
 	    super.getDatiTitoloCumulato();
 	    
 		 // riempie il model
 		 mPenMod.setIdPenaAccessoriaCumulo(new BigDecimal(lId) );

 		 // chiama il controller
		 IPenaAccessoriaCumulo lCtrl = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
		 
		 mPenMod = (PenaAccessoriaCumuloModel)lCtrl.ExRicercaPenaAccessoriaCumuloByKey(new BigDecimal(lId));
		 setRequestAttribute("penaaccessoria", mPenMod);
		 
		 return PG_LOAD_DETTAGLIO_PENEACCESSORIE_CUMULO;
	 }
}
