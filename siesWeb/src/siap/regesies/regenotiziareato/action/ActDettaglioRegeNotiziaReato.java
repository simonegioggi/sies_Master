package siap.regesies.regenotiziareato.action;

import siap.regesies.action.ActionRegeSiap;
import siap.regesies.regenotiziareato.controller.IRegeNotiziaReato;
import siap.regesies.regenotiziareato.model.RegeNotiziaReatoModel;
import siap.regesies.util.RegeSiesLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioRegeCircostanza</p>
* <p>Description: Classe Action per la load dettaglio di RegeCircostanza</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/

public class ActDettaglioRegeNotiziaReato extends ActionRegeSiap
implements ICostantiRegeNotiziaReato
{
public String processRequest() throws F3BException
{
 		 String lId = getRequestStringParameter(CAMPO_ID_FILE);
     int lProgrNot = getRequestIntParameter(CAMPO_PROGR_NOTIZIA);

		 IRegeNotiziaReato lCtrl = RegeSiesLookupRemote.getRegeNotiziaReatoRemote();
		 RegeNotiziaReatoModel llRegMod = lCtrl.ExRicercaRegeNotiziaReatoByKey(lId,lProgrNot);
		 setRequestAttribute("regenotiziareato", llRegMod);

		 return PG_DETTAGLIOREGENOTIZIAREATO;
	 }
}