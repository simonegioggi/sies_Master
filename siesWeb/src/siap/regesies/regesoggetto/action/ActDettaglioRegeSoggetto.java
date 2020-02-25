package siap.regesies.regesoggetto.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regesoggetto.controller.IRegeSoggetto;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioRegeSoggetto</p>
* <p>Description: Classe Action per la load dettaglio di RegeSoggetto</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/

public class ActDettaglioRegeSoggetto extends ActionRegeSiap
implements ICostantiRegeSoggetto
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_FILE);

		 IRegeSoggetto lCtrl = RegeSiesLookupRemote.getRegeSoggettoRemote();
		 RegeSoggettoModel llRegMod = lCtrl.ExRicercaRegeSoggettoByKey(lId);
		 setRequestAttribute("regesoggetto", llRegMod);

		 return PG_LOAD_DETTAGLIOREGESOGGETTO;
	 }
}