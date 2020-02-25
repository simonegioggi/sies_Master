package siap.siep.scarti.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.scarti.controller.IWScarti;
import siap.siep.scarti.model.WScartiModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioWScarti</p>
* <p>Description: Classe Action per la load dettaglio di WScarti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioWScarti extends ActionSiap implements ICostantiWScarti
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_SCARTI);
 		 // riempie il model
		 // chiama il controller

		 IWScarti lCtrl = SIEPLookupRemote.getWScartiRemote();
		 WScartiModel llWScMod = lCtrl.ExRicercaWScartiByKey(new BigDecimal(lId));
		 setRequestAttribute("wscarti", llWScMod);

		 return PG_LOAD_DETTAGLIOWSCARTI;
	 }



}