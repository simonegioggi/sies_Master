package siap.sius.esperto.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaEspertoLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Esperti
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
public class ActRicercaEspertoLista extends ActionSiap implements ICostantiEsperto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		String lCognome = null;
		String lUffAppa = null;
		String lMod = "POP";

		lCognome = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase());
		lUffAppa = getCodUfficioUtenteConnesso();

		EspertoModel lEsperto = new EspertoModel();
		lEsperto.setCognome(lCognome);
		lEsperto.setCodUfficioAppartenenza(lUffAppa);

		IEsperto lCtrl = SIUSLookupRemote.getEspertoRemote();
		Vector lVect = lCtrl.ExRicercaEsperto(lEsperto);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("idfieldnum", getRequestStringParameter("idfieldnum"));
		setRequestAttribute("esperti", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_ESPERTO_LISTA;
	}

}