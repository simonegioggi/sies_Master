package siap.regesies.regecircostanza.action;

import siap.regesies.regecircostanza.controller.IRegeCircostanza;
import siap.regesies.regecircostanza.model.RegeCircostanzaModel;
import siap.regesies.util.RegeSiesLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioRegeCircostanza</p>
* <p>Description: Classe Action per la load dettaglio di RegeCircostanza</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
*/

public class ActDettaglioRegeCircostanza extends ActionSiap
implements ICostantiRegeCircostanza
{
public String processRequest() throws F3BException
{
 		 String lId = getRequestStringParameter(CAMPO_ID_FILE);
     int lProgrCirc = getRequestIntParameter(CAMPO_PROGR_CIRCOSTANZA);

		 IRegeCircostanza lCtrl = RegeSiesLookupRemote.getRegeCircostanzaRemote();
		 RegeCircostanzaModel llRegMod = lCtrl.ExRicercaRegeCircostanzaByKey(lId,lProgrCirc);
		 setRequestAttribute("regecircostanza", llRegMod);

		 return PG_DETTAGLIOREGECIRCOSTANZA;
	 }
}