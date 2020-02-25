package siap.sius.tenore.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioTenore</p>
* <p>Description: Classe Action per la load dettaglio di Tenore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioTenore extends ActionSiap implements ICostantiTenore
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_TENORE);
 		 // riempie il model
		 // chiama il controller

		 ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
		 TenoreModel llTenMod = lCtrl.ExRicercaTenoreByKey(new BigDecimal(lId));
		 setRequestAttribute("tenore", llTenMod);

		 return PG_LOAD_DETTAGLIOTENORE;
	 }



}