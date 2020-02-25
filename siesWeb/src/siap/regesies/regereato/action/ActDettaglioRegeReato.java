package siap.regesies.regereato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regereato.controller.IRegeReato;
import siap.regesies.regereato.model.RegeReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActDettaglioRegeReato</p>
* <p>Description: Classe Action per la load dettaglio di RegeReato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/
public class ActDettaglioRegeReato extends ActionRegeSiap
implements ICostantiRegeReato
{

  public String processRequest() throws F3BException
  {
  	 String lId = getRequestStringParameter(CAMPO_ID_FILE);
     int lProgr = getRequestIntParameter(CAMPO_PROGR_REATO);
     int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

 		 IRegeReato lCtrl = RegeSiesLookupRemote.getRegeReatoRemote();
  	 RegeReatoModel lRegMod = lCtrl.ExRicercaRegeReatoByKey(lId, lProgr,lProgrCirc);

     setRequestAttribute("regereato", lRegMod);

		 return PG_LOAD_DETTAGLIOREGEREATO;
	 }
}