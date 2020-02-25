package siap.sico.soggettodattilo.action;

import java.math.BigDecimal;

import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadDettaglioSoggettoDattilo</p>
* <p>Description: Classe Action per la load dettaglio di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadDettaglioSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {

	public String processRequest() throws F3BException {
		String lId = getRequestStringParameter(CAMPO_ID_DATTILO);
		// riempie il model
		// chiama il controller

		ISoggettoDattilo lCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		SoggettoDattiloModel llSogMod = lCtrl.ExRicercaSoggettoDattiloByKey(new BigDecimal(lId));
		setRequestAttribute("soggettodattilo", llSogMod);

		return PG_LOAD_DETTAGLIOSOGGETTODATTILO;
	}

}