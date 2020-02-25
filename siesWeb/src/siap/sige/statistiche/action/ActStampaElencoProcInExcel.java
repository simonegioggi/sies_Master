package siap.sige.statistiche.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sige.statistiche.controller.IStatisticheSige;
import siap.sige.statistiche.controller.StatisController;
import siap.sige.statistiche.model.RicercaFogliCompModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaElencoProcInExcel
 * </p>
 * <p>
 * Description: Attiva la Ricerca dei Procedimenti per stamparne l'elenco in un foglio excel.
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActStampaElencoProcInExcel extends ActionSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	String lCriterio1 = "";
	String lCriterio2 = "";

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controller per la Ricerca

		HSSFWorkbook wb = new HSSFWorkbook();

		// parametro che indica il tipo di ricerca da effettuare
		String modalitaRicerca = "";
		if (!isRequestParameterNullObj("modalitaRicerca")) {
			modalitaRicerca = getRequestStringParameter("modalitaRicerca");
		}

		RicercaFogliCompModel lRicercaModel = new RicercaFogliCompModel();
		lRicercaModel = (RicercaFogliCompModel) getSessionAttribute("ProvvRicercaModel");

		IStatisticheSige lCtrlStatSige = SIGELookupRemote.getStatisticheSigeRemote();
		// Vector mProvv = lCtrlStatSige.ExRicercaProcSiusXProvvedimentiPaginata( lRicercaModel, -1 );
		Vector mProvv = lCtrlStatSige.ExRicercaFogliComplementariPaginata(lRicercaModel, -1);

		if (mProvv != null)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug("Risultati ricerca completa : " + mProvv.size());

		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();

		// Creazione del file excel
		/*String titolo = */creaTitoloFoglio(lRicercaModel);

		StatisController lStatisCtrl = new StatisController();

		if (lRicercaModel.isRicercaXFoglioComplementare() && !modalitaRicerca.equals("")) {
			lStatisCtrl.creaFoglioElencoFogliComplementari(wb, getUfficioUtenteConnesso(), lCriterio1,
					lCriterio2, null, mProvv);
		} else {

			if (lRicercaModel.getModalitaRicerca().compareTo(
					ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE) == 0) {
				wb = lStatisCtrl.creaFoglioFoglioComplementare(getUfficioUtenteConnesso(), lRicercaModel);
			} else {
				lStatisCtrl.creaFoglioElencoProvvedimentiSige(wb, getUfficioUtenteConnesso(), lCriterio1,
						lCriterio2, null, mProvv);
			}

		}

		// Generazione file xls

		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActStampaElencoProcInExcel.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;

	}

	public String creaTitoloFoglio(RicercaFogliCompModel aFiltroRicerca) throws F3BException {

		String lTitolo = new String();

		if (aFiltroRicerca.isTipoIntervalloRicercaXEstremiProvvedimento()) {
			String lNumIniziale = aFiltroRicerca.getAnnoIniziale().toString() + "/"
					+ aFiltroRicerca.getNumIniziale().toString();
			String lNumFinale = aFiltroRicerca.getAnnoFinale().toString() + "/"
					+ aFiltroRicerca.getNumFinale().toString();
			lCriterio1 = "Dal N. " + lNumIniziale + " al " + lNumFinale;
		} else if (aFiltroRicerca.isRicercaXDateDeposito()) {
			String lDataIniziale = " "
					+ StringUtils
							.toStringJSP(DateUtils.getDateToString(aFiltroRicerca.getDataDepositoIniziale(),
									"dd-MM-yyyy"), "-");
			String lDataFinale = " "
					+ StringUtils.toStringJSP(
							DateUtils.getDateToString(aFiltroRicerca.getDataDepositoFinale(), "dd-MM-yyyy"),
							"-");
			lCriterio1 = "Data di deposito tra  " + lDataIniziale + " e   " + lDataFinale;
		} else if (aFiltroRicerca.isRicercaXDateEmissione()) {
			String lDataIniziale = " "
					+ StringUtils.toStringJSP(DateUtils.getDateToString(
							aFiltroRicerca.getDataEmissioneIniziale(), "dd-MM-yyyy"), "-");
			String lDataFinale = " "
					+ StringUtils.toStringJSP(
							DateUtils.getDateToString(aFiltroRicerca.getDataEmissioneFinale(), "dd-MM-yyyy"),
							"-");
			lCriterio1 = "Data di emissione tra  " + lDataIniziale + " e   " + lDataFinale;
		}

		if (aFiltroRicerca.getStatoValidazione() != null) {
			if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI))
				lCriterio2 = "Annullato";
			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI))
				lCriterio2 = "Validato";
			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.TUTTI))
				lCriterio2 = "Tutti";
			else if (aFiltroRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI))
				lCriterio2 = "Non Validato";
			else if (aFiltroRicerca.getStatoValidazione()
					.equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI))
				lCriterio2 = "Non Annullato";
			lCriterio2 = "Stato: " + lCriterio2;
		}

		lTitolo = lCriterio1 + " -- " + lCriterio2;

		return lTitolo;
	}

}