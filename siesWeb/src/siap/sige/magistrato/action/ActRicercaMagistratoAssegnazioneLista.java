package siap.sige.magistrato.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaMagistratoAssegnazioneLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Magistrato Assegnatario (SIGE)
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
public class ActRicercaMagistratoAssegnazioneLista extends ActionSiap implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		String lCognome = null;
		String lSezione = null;
		String lUffAppa = null;

		IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
		lCognome = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase());
		lSezione = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_SEZIONE)).toUpperCase());
		lUffAppa = this.getCodUfficioUtenteConnesso();

		Vector lVect = lCtrl.ExRicercaMagistratoByCognomeSezione(lCognome, lSezione, lUffAppa);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("idfieldnum", getRequestStringParameter("idfieldnum"));
		setRequestAttribute("magistrati", lVect);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_MAGISTRATO_ASSEGNAZIONE_LISTA;
	}

}