package siap.sius.esperto.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioEsperto
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Esperto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioEsperto extends ActionSiap implements ICostantiEsperto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		setLinkRitorno();

		// chiama il controller
		IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
		EspertoModel lEspMod = lCtrl.ExRicercaEspertoByKey(getRequestBigDecimalParameter(CAMPO_ID_ESPERTO));

		// Decodifica di Flag_stato
		Collection lCol = (DecodificheManager.getInstance()).getFlagStato();
		lEspMod.setFlagStato(DecodificheUtils.getDescbyCode(lCol, lEspMod.getFlagStato()));

		// passaggio alla request
		setRequestAttribute("esperto", lEspMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_LOAD_DETTAGLIOESPERTO;
	}

}