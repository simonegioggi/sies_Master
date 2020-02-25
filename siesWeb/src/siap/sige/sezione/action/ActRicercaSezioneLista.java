package siap.sige.sezione.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaSezioneLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle Sezioni
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaSezioneLista extends ActionSiap implements ICostantiSezione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		String lCodice = null;
		String lUffAppa = null;
		String lMod = "POP";

		lCodice = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_CODICE)).toUpperCase());
		lUffAppa = getCodUfficioUtenteConnesso();

		SezioneModel lSezione = new SezioneModel();
		lSezione.setCodice(lCodice);
		lSezione.setCodUfficioAppartenenza(lUffAppa);

		ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
		Vector lVect = lCtrl.ExRicercaSezione(lSezione);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("sezioni", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_SEZIONE_LISTA;
	}

}