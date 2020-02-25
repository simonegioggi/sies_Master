package siap.sige.collegio.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActRicercaCollegioLista
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Collegi
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
@SuppressWarnings("rawtypes")
public class ActRicercaCollegioLista extends ActionSiap implements ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String frameFilter() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".frameFilter : inizio");
		String lCodice = null;
		BigDecimal lIdSezione = null;
		String lUffAppa = null;
		String lMod = "POP";

		lCodice = StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COD_COLLEGIO)).toUpperCase());

		if (isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))
			lIdSezione = null;
		else if (getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE).equals("Tutte"))
			lIdSezione = null;
		else
			lIdSezione = getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE);

		lUffAppa = getCodUfficioUtenteConnesso();

		CollegioModel lCollegio = new CollegioModel();
		lCollegio.setCodCollegio(lCodice);
		lCollegio.setSezIdSezione(lIdSezione);
		lCollegio.setCodUfficioAppartenenza(lUffAppa);

		// Impostazione filtri sulla data di fine validità.
		lCollegio.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA,
				CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));

		if (isRequestChecked("flagDataFine@Null"))
			lCollegio.setMessage("dataFineisNull");

		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		Vector lVect = lCtrl.ExRicercaCollegio(lCollegio);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("collegi", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_COLLEGIO_LISTA;
	}

	private String directFilter() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".directFilter : inizio");

		String lCodice = "";
		String lMod = "POP";

		BigDecimal lIdSezione = null;
		if (isRequestParameterNullObj("sezione"))
			lIdSezione = null;
		else if (getRequestStringParameter("sezione").equals("Tutte"))
			lIdSezione = null;
		else
			lIdSezione = getRequestBigDecimalParameter("sezione");

		String lUffAppa = getCodUfficioUtenteConnesso();

		CollegioModel lCollegio = new CollegioModel();
		lCollegio.setCodCollegio(lCodice);
		lCollegio.setSezIdSezione(lIdSezione);
		lCollegio.setCodUfficioAppartenenza(lUffAppa);

		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		Vector lVect = lCtrl.ExRicercaCollegio(lCollegio);

		setRequestAttribute("formname", getRequestStringParameter("formname"));
		setRequestAttribute("magistrato", getRequestStringParameter("magistrato"));
		setRequestAttribute("collegi", lVect);
		setRequestAttribute("modalita", lMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return PG_RICERCA_MAGISTRATI_LISTA;
	}

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		if (!isRequestParameterNullObj("directFilter")) {
			return directFilter();
		} else {
			return frameFilter();
		}
	}

}