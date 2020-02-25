package siap.sige.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.statistiche.controller.StatisController;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * MEV_65: aggiunta classe Action per la visualizzazione del foglio excel con i Procedimenti Sige Con Ricorso
 * od Opposizione.
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActExportProcedimentiSigeConRicorsoOpposizioneInExcel extends ActionSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// recupero oggetti in sessione
		RicercaFascicoloSigeModel rfsm = (RicercaFascicoloSigeModel) getSessionAttribute("modelRO");
		// Viene istanziato il controller per la ricerca
		IFascicoloSige ifs = SIGELookupRemote.getFascicoloSigeRemote();
		// Ricerca Procedimenti Sige Con Ricorso od Opposizione
		Vector<FascicoloSigeEstesoModel> v = ifs.ExRicercaProcedimentiSigeConRicorsoOpposizione(rfsm, 0);

		// risultato della ricerca supera 65536 record
		if (!v.isEmpty() && v.size() > 65536)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione! Ridurre i parametri di ricerca, in quanto il risultato non è interamente visualizzabile.");

		// if (!isRequestParameterNullEmptyObj("stampa"))
		// return stampa();

		StatisController sc = new StatisController();
		HSSFWorkbook excel = sc.getReportProcedimentiSigeConRicorsoOpposizione(rfsm, v,
				getUfficioUtenteConnesso());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		excel.write(baos);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		setRequestAttribute("report", baos);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}