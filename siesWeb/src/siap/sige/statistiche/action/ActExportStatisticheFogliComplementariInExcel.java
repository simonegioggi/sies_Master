package siap.sige.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sige.SIGEException;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.statistiche.controller.IStatisticheSige;
import siap.sige.statistiche.controller.StatisController;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariContainerModel;
import siap.sige.statistiche.model.StatisticheFogliComplementariModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;

public class ActExportStatisticheFogliComplementariInExcel extends ActionSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		RicercaFogliCompModel filtro = (RicercaFogliCompModel) getSessionAttribute("FiltroRicerca");
		IStatisticheSige ctrl = SIGELookupRemote.getStatisticheSigeRemote();

		if (!super.isRequestParameterNullEmptyObj("stampa")) {
			return this.stampa();
		}

		StatisticheFogliComplementariContainerModel container = ctrl
				.ExEstraiStatisticheFogliComplementariExportExcel(filtro);
		container.setFiltro(filtro);
		container.setUffUteConnesso(getUfficioUtenteConnesso());
		container.setUtenteConnesso(super.getUtenteConnesso());

		StatisController ctrlStats = new StatisController();
		HSSFWorkbook excel = ctrlStats.getReportStatisticheFogliComplementari(container);
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		excel.write(fileOut);
		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

	/**
	 * Esegue la stampa.
	 * <p>
	 * 
	 * @throws Exception
	 *             propaga errore di eccezione.
	 * @return String Ritorna documneto rtf di stampa.
	 */
	private String stampa() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): inizio ");
		RicercaFogliCompModel filtro = (RicercaFogliCompModel) getSessionAttribute("FiltroRicerca");
		StatisticheFogliComplementariContainerModel container = new StatisticheFogliComplementariContainerModel();
		container.setUffUteConnesso(getUfficioUtenteConnesso());
		container.setUtenteConnesso(super.getUtenteConnesso());
		container.setFiltro(filtro);
		IStatisticheSige ctrl = SIGELookupRemote.getStatisticheSigeRemote();
		Vector<StatisticheFogliComplementariModel> fogli = ctrl.ExEstraiStatisticheFogliComplementari(filtro,
				-1);
		container.setElenco(fogli);

		// Generazione documento di stampa
		IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();

		ByteArrayOutputStream lReport = lCtrlSta.ExPreStampaStatisticheFC(container);

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .Stampa(): fine ");
		return IWebConstants.PG_DOWNLOAD;
	}

}