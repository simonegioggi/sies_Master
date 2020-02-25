package siap.sige.collegio.action;

//import java.math.BigDecimal;
//import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActModificaCollegio</p>
* <p>Description: Classe Action per la modifica del Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActModificaCollegio extends ActionCollegio implements ICostantiCollegio {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// CollegioModel lColMod = new CollegioModel();

	/**
	* Azione di Modifica del Collegio.
	* <p>
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* <p>
	* @throws Exception
	*/
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		lColMod.setIdCollegio(getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO));
		lColMod.setCodCollegio(getRequestStringParameter(CAMPO_COD_COLLEGIO));

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))
			lColMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		lColMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		lColMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lColMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lColMod.setDataAggiornamento(DateUtils.getSysDate());

		lColMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA, CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
		lColMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA, CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		letturaDatiMagistrati();

		letturaDatiGiudiciPopolari();

		letturaDatiEsperti();

		// chiama il controller
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColModRet = lCtrl.ExModificaCollegio(lColMod);

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.collegio.action.ActLoadDettaglioCollegio");
		lRedir.setParameter(CAMPO_ID_COLLEGIO, lColModRet.getIdCollegio().toString());

		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lPage;
	}
}