package siap.siep.sentenzariunita.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.sentenzariunita.controller.ISentenzaRiunita;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioSentenzaRiunita</p>
* <p>Description: Classe Action per la load dettaglio di SentenzaRiunita</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioSentenzaRiunita extends ActionSiap implements ICostantiSentenzaRiunita
{
public String processRequest() throws F3BException {

			String lId = getRequestStringParameter(CAMPO_ID_SENTENZA_RIUNITA);
			// riempie il model
		 // chiama il controller

		 ISentenzaRiunita lCtrl = SIEPLookupRemote.getSentenzaRiunitaRemote();
		 SentenzaRiunitaModel llSenMod = lCtrl.ExRicercaSentenzaRiunitaByKey(new BigDecimal(lId));
		 setRequestAttribute("sentenzariunita", llSenMod);

		 return PG_LOAD_DETTAGLIOSENTENZARIUNITA;
	 }



}
