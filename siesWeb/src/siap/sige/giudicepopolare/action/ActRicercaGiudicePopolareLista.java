package siap.sige.giudicepopolare.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaGiudicePopolareLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Giudici Popolari
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaGiudicePopolareLista extends ActionSiap implements ICostantiGiudicePopolare {

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

		GiudicePopolareModel lGiudicePopolare = new GiudicePopolareModel();
		lGiudicePopolare.setCognome(lCognome);
		lGiudicePopolare.setCodUfficioAppartenenza(lUffAppa);

		// Impostazione condizioni di ricerca per la sezione.

		/*
		 * 20081030 - Commentato su richiesta cliente, per cambio requisiti funzionali. La sezione è un campo
		 * obbligatorio. Vedi codice successivo a questo.
		 * 
		 * if( isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE) && isRequestChecked("flagTutti") ) {
		 * lGiudicePopolare.setSezIdSezione(null); } else if(!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)
		 * && getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("")) {
		 * lGiudicePopolare.setSezIdSezione(new BigDecimal("-1")); } else
		 * lGiudicePopolare.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		 * 
		 */

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE)) {
			// Filtro sulla sezione
			if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Tutte"))
				lGiudicePopolare.setSezIdSezione(null);
			else
				lGiudicePopolare.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		}

		// Impostazione filtri sulla data di fine validità.
		lGiudicePopolare.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
				CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));

		if (isRequestChecked("flagDataFine@Null"))
			lGiudicePopolare.setMessage("dataFineisNull");

		IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
		Vector lVect = lCtrl.ExRicercaGiudicePopolare(lGiudicePopolare);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("idfieldnum", getRequestStringParameter("idfieldnum"));

		setRequestAttribute("giudicipopolari", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_GIUDICE_POPOLARE_LISTA;
	}

}