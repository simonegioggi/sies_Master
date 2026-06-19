package siap.sius.esecuzionemisuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

/**
 * RicercaDataScadenzaProcEsecMAExcel - Classe di ricerca Data Scadenza Procedimenti di Esecuzione Misura
 * Alternativa
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class RicercaDataScadenzaProcEsecMAExcel extends SIAPExcelProducer {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream creaFoglioRicercaDataScadenzaProcEsecMA(RicercaProcedimentoModel rpm)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: inizio");

		ByteArrayOutputStream baos = null;
		HSSFWorkbook hssw = null;
		HSSFSheet hssfs = null;
		HSSFCellStyle hssfcsNull = null;
		HSSFCellStyle hssfcsCenter = null;
		HSSFCellStyle hssfcsCenterBold = null;
		HSSFFont hssff = null;
		HSSFRow hssfr = null;
		HSSFRow hssfrBold = null;
		Iterator iter = null;
		int contatoreRiga = 0;
		EMAFascGPModel emafgpm = null;
		String patternData = "dd/MM/yyyy";
		String lBuffer = null;
		IEsecuzioneMA iema = null;
		Collection<EMAFascGPModel> elenco = null;

		hssw = new HSSFWorkbook();

		iema = SIUSLookupRemote.getEsecuzioneMARemote();
		elenco = iema.ExRicercaDataScadenzaProcEsecMAPaginata(rpm, 0);

		// creazione foglio
		hssfs = hssw.createSheet("Elenco Procedimenti");
		hssfcsNull = hssw.createCellStyle();

		// Intestazione del foglio excel aUfficioUtenteConnesso
		contatoreRiga = setIntestazione(hssfs, rpm.getUtenteConnesso().getUfficioUtente(), hssfcsNull);
		contatoreRiga += 2;

		// Inserimento dei parametri di ricerca.
		hssfr = hssfs.createRow(contatoreRiga);
		setCell(hssfr, 0, "Criteri di ricerca selezionati : ", hssfcsNull);
		contatoreRiga++;

		// Stampa filtri di ricerca
		if (rpm != null) {
			hssfr = hssfs.createRow(contatoreRiga);
			setCell(hssfr, 0, lBuffer, hssfcsNull);
			contatoreRiga++;

			if (rpm.getAnnoInizio() != null || rpm.getAnnoFine() != null) {
				lBuffer = "Intervallo Estremi Procedimenti : ";
				if (rpm.getAnnoInizio() != null) {
					lBuffer += " da " + rpm.getAnnoInizio() + "/" + rpm.getNumeroInizio();
				}
				if (rpm.getAnnoFine() != null) {
					lBuffer += " a " + rpm.getAnnoFine() + "/" + rpm.getNumeroFine();
				}
				hssfr = hssfs.createRow(contatoreRiga);
				setCell(hssfr, 0, lBuffer, hssfcsNull);
				contatoreRiga++;
			}

			if (rpm.getDataIscrizioneInizio() != null || rpm.getDataIscrizioneFine() != null) {
				lBuffer = "Procedimenti con Data Iscrizione : ";
				if (rpm.getDataIscrizioneInizio() != null) {
					lBuffer += " dal " + DateUtils.getDateToString(rpm.getDataIscrizioneInizio(), patternData);
				}
				if (rpm.getDataIscrizioneFine() != null) {
					lBuffer += " al " + DateUtils.getDateToString(rpm.getDataIscrizioneFine(), patternData);
				}
				hssfr = hssfs.createRow(contatoreRiga);
				setCell(hssfr, 0, lBuffer, hssfcsNull);
				contatoreRiga++;
			}
			// Tipo estrazione
			String descTipoEstrazione = "";
			if (Utils.isNullObj(rpm.getDataEmissioneInizio()) && Utils.isNullObj(rpm.getDataEmissioneFine()))
				descTipoEstrazione += "tutti";
			else if (!Utils.isNullObj(rpm.getDataEmissioneInizio())
					&& Utils.isNullObj(rpm.getDataEmissioneFine())
					&& DateUtils.isEquals(rpm.getDataEmissioneInizio(), rpm.getDataEmissioneFine()))
				descTipoEstrazione += "In Scadenza Oggi";
			else if (Utils.isNullObj(rpm.getDataEmissioneInizio())
					&& !Utils.isNullObj(rpm.getDataEmissioneFine()))
				descTipoEstrazione += "Scaduti";
			else
				descTipoEstrazione += rpm.getDescrCancelleria();
			lBuffer = "Criteri di Ricerca : " + descTipoEstrazione;
			hssfr = hssfs.createRow(contatoreRiga);
			setCell(hssfr, 0, lBuffer, hssfcsNull);
			contatoreRiga++;
		}

		contatoreRiga += 2;

		// stile per celle col bordo con testo centrato
		hssfcsCenter = getBordo4Lati(hssw);
		hssfcsCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		hssfcsCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		hssfcsCenter.setWrapText(true);

		hssff = hssw.createFont();
		hssff.setBold(true);

		hssfcsCenterBold = getBordo4Lati(hssw);
		hssfcsCenterBold.setFont(hssff);
		hssfcsCenterBold.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		hssfcsCenterBold.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		hssfcsCenterBold.setWrapText(true);

		hssfrBold = hssfs.createRow(contatoreRiga++);

		hssfs.setColumnWidth(0, 20 * 256); // Procedimento SIUS
		hssfs.setColumnWidth(1, 20 * 256); // Stato Procedimento
		hssfs.setColumnWidth(2, 40 * 256); // Soggetto
		hssfs.setColumnWidth(3, 15 * 256); // Ordinanza
		hssfs.setColumnWidth(4, 15 * 256); // TDS Emittente
		hssfs.setColumnWidth(5, 15 * 256); // Data Emissione
		hssfs.setColumnWidth(6, 50 * 256); // Misura da eseguire
		hssfs.setColumnWidth(7, 20 * 256); // Data inizio misura
		hssfs.setColumnWidth(8, 20 * 256); // Data fine misura
		hssfs.setColumnWidth(9, 15 * 256); // Giorni residui
		hssfs.setColumnWidth(10, 20 * 256); // Procedimento SIEP

		// Intestazione colonne
		setCell(hssfrBold, 0, "Procedimento SIUS", hssfcsCenterBold);
		setCell(hssfrBold, 1, "Stato Procedimento", hssfcsCenterBold);
		setCell(hssfrBold, 2, "Soggetto", hssfcsCenterBold);
		setCell(hssfrBold, 3, "Ordinanza", hssfcsCenterBold);
		setCell(hssfrBold, 4, "TDS Emittente", hssfcsCenterBold);
		setCell(hssfrBold, 5, "Data Emissione", hssfcsCenterBold);
		setCell(hssfrBold, 6, "Misura da eseguire", hssfcsCenterBold);
		setCell(hssfrBold, 7, "Data inizio misura", hssfcsCenterBold);
		setCell(hssfrBold, 8, "Data fine misura", hssfcsCenterBold);
		setCell(hssfrBold, 9, "Giorni residui", hssfcsCenterBold);
		setCell(hssfrBold, 10, "Procedimento SIEP", hssfcsCenterBold);

		iter = elenco.iterator();
		while (iter.hasNext()) {
			emafgpm = (EMAFascGPModel) iter.next();
			hssfr = hssfs.createRow(contatoreRiga++);
			// Procedimento SIUS
			setCell(hssfr, 0, "" + emafgpm.getFascicoloSiusModel().getChiaveAnno() + "/"
					+ emafgpm.getFascicoloSiusModel().getChiaveProgr(), hssfcsCenter);
			// Stato Procedimento
			setCell(hssfr, 1, "" + emafgpm.getFascicoloSiusModel().getDescrStatoFascicolo(), hssfcsCenter);
			// Soggetto
			setCell(hssfr, 2, emafgpm.getFascicoloSiusModel().getSoggetto().getCognome() + " "
					+ emafgpm.getFascicoloSiusModel().getSoggetto().getNome(), hssfcsCenter);
			// Ordinanza
			setCell(hssfr, 3, "" + emafgpm.getEsecuzioneMAModel().getAnnoS07() + "/"
					+ emafgpm.getEsecuzioneMAModel().getProgrS07(), hssfcsCenter);
			// TDS Emittente
			setCell(hssfr, 4,
					emafgpm.getEsecuzioneMAModel() != null
							? StringUtils.toStringJSP(
									emafgpm.getEsecuzioneMAModel().getDescrLuogoAutoritaEmittOrd(), "-")
							: "-",
					hssfcsCenter);
			// Data Emissione
			setCell(hssfr, 5, emafgpm.getEsecuzioneMAModel() != null ? StringUtils.toStringJSP(
					DateUtils.getDateToString(emafgpm.getEsecuzioneMAModel().getDataOrdinanza(), patternData),
					"-") : "-", hssfcsCenter);
			// Misura da eseguire
			setCell(hssfr, 6,
					emafgpm.getEsecuzioneMAModel() != null
							? StringUtils.toStringJSP(
									emafgpm.getGeneraleProcedimentoModel().getDescrDefinizione(), "-")
							: "-",
					hssfcsCenter);
			// Data inizio misura
			setCell(hssfr, 7,
					emafgpm.getEsecuzioneMAModel() != null
							? StringUtils.toStringJSP(DateUtils.getDateToString(
									emafgpm.getEsecuzioneMAModel().getDataInizioMisura(), patternData), "-")
							: "-",
					hssfcsCenter);
			// Data fine misura
			setCell(hssfr, 8,
					emafgpm.getEsecuzioneMAModel() != null
							? StringUtils.toStringJSP(DateUtils.getDateToString(
									emafgpm.getEsecuzioneMAModel().getDataTermineAttuale(), patternData), "-")
							: "-",
					hssfcsCenter);
			// Giorni residui
			setCell(hssfr, 9,
					emafgpm.getEsecuzioneMAModel() != null
							? StringUtils.toStringJSP(DateUtils.getIntervallo(new Date(),
									emafgpm.getEsecuzioneMAModel().getDataTermineAttuale()))
							: " - ",
					hssfcsCenter);
			// Procedimento SIEP
			setCell(hssfr, 10, "" + emafgpm.getFascicoloSiusModel().getChiaveAnnoSIEP() + "/"
					+ emafgpm.getFascicoloSiusModel().getChiaveProgrSIEP(), hssfcsCenter);
		}

		baos = new ByteArrayOutputStream();

		try {
			hssw.write(baos);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcPerProvvNoValidatiNoDeposito: " + ioe);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + " .processRequest: fine");

		// valore di ritorno
		return baos;
	}

}
