package siap.sige.curatore.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaCuratoreLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Curatoi
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
public class ActRicercaCuratoreLista extends ActionSiap implements ICostantiCuratore {

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

		CuratoreModel lCuratore = new CuratoreModel();
		lCuratore.setCognome(lCognome);
		lCuratore.setCodUfficioAppartenenza(lUffAppa);

		ICuratore lCtrl = SIGELookupRemote.getCuratoreRemote();
		Vector lVect = lCtrl.ExRicercaCuratore(lCuratore);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("curatori", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_CURATORE_LISTA;
	}

}