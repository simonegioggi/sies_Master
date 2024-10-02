package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogProvModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

// MEV_2019-09: aggiunta classe per le statistiche
public class ProcPerStatisticaMisureAlternativeExcel extends SIAPExcelProducer {

	@SuppressWarnings("rawtypes")
	public ByteArrayOutputStream creaFoglioProcPerStatisticaMisureAlternative(UfficioModel um,
			RicercaProcedimentoModel rpm) throws F3BException {

		ByteArrayOutputStream baos = null;
		HSSFWorkbook hssfw = null;
		HSSFSheet hssfs = null;
		HSSFCellStyle hssfcsNull = null;
		HSSFCellStyle hssfcsCenter = null;
		HSSFCellStyle hssfcsCenterIntest = null;
		HSSFRow hssfr = null;
		HSSFFont hssff = null;
		Iterator i = null;
		int contatore = 0;
		int rowCounter = 0;
		EveFasGepSogProvModel efgspm = null;
		String patternData = "dd/MM/yyyy";
		String buffer = null;
		IStatisticheSius iss = null;
		Collection<EveFasGepSogProvModel> elenco = null;

		hssfw = new HSSFWorkbook();

		iss = SIUSLookupRemote.getStatisticheSiusRemote();
		elenco = iss.ProcPerStatisticaMisureAlternative(rpm);

		// creazione foglio
		hssfs = hssfw.createSheet("Elenco Procedimenti");
		hssfcsNull = hssfw.createCellStyle();

		// Intestazione del foglio excel
		rowCounter = setIntestazione(hssfs, um, hssfcsNull);
		rowCounter += 2;

		// Inserimento dei parametri di ricerca.
		hssfr = hssfs.createRow(rowCounter);
		setCell(hssfr, 0, "Criteri di ricerca selezionati : ", hssfcsNull);
		rowCounter++;

		// Stampa filtri di ricerca
		if (rpm != null) {
			switch (rpm.getStatoProcedimento()) {
			case 0:
				buffer = "Ordinanze Non Emesse - Atti al Presidente";
				break;
			case 1:
				buffer = "Procedimenti con Magistrato Designato - Ordinanze Non Emesse";
				break;
			case 2:
				// MEV_2024-092: cambio messaggio da Provvisoria a Misure Alternative Dl 123/2018
				buffer = "Ordinanze Applicazione Misure Alternative Dl 123/2018 Emesse ma prive di Data di Esecutività";
				break;
			case 3:
				buffer = "Ordinanze Emesse con Data Esecutività Inserita ma Prive di Decisione del Collegio";
				break;
			case 4:
				buffer = "Procedimenti Privi di Provvedimenti";
				break;
			}
			hssfr = hssfs.createRow(rowCounter);
			setCell(hssfr, 0, buffer, hssfcsNull);
			rowCounter++;

			if (rpm.getAnnoInizio() != null || rpm.getAnnoFine() != null) {
				buffer = "Intervallo Estremi Procedimenti : ";
				if (rpm.getAnnoInizio() != null) {
					buffer += " da " + rpm.getAnnoInizio() + "/" + rpm.getNumeroInizio();
				}
				if (rpm.getAnnoFine() != null) {
					buffer += " a " + rpm.getAnnoFine() + "/" + rpm.getNumeroFine();
				}
				hssfr = hssfs.createRow(rowCounter);
				setCell(hssfr, 0, buffer, hssfcsNull);
				rowCounter++;
			}

			if (rpm.getDataIscrizioneInizio() != null || rpm.getDataIscrizioneFine() != null) {
				buffer = "Procedimenti con Data Iscrizione : ";
				if (rpm.getDataIscrizioneInizio() != null) {
					buffer += " dal " + DateUtils.getDateToString(rpm.getDataIscrizioneInizio(), patternData);
				}
				if (rpm.getDataIscrizioneFine() != null) {
					buffer += " al " + DateUtils.getDateToString(rpm.getDataIscrizioneFine(), patternData);
				}
				hssfr = hssfs.createRow(rowCounter);
				setCell(hssfr, 0, buffer, hssfcsNull);
				rowCounter++;
			}
		}

		rowCounter += 2;

		// stile per celle col bordo con testo centrato
		hssfcsCenter = getBordo4Lati(hssfw);
		hssfcsCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcsCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcsCenter.setWrapText(true);
		// font grassetto
		hssff = hssfw.createFont();
		hssff.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hssfcsCenterIntest = getBordo4Lati(hssfw);
		hssfcsCenterIntest.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		hssfcsCenterIntest.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		hssfcsCenterIntest.setWrapText(true);
		hssfcsCenterIntest.setFont(hssff);

		hssfr = hssfs.createRow(rowCounter++);

		hssfs.setColumnWidth(0, 10 * 256); // Prog
		hssfs.setColumnWidth(1, 20 * 256); // Procedimento Sius
		hssfs.setColumnWidth(2, 35 * 256); // Generalità Soggetto
		hssfs.setColumnWidth(3, 15 * 256); // Data Iscrizione
		hssfs.setColumnWidth(4, 15 * 256); // Data Emissione
		hssfs.setColumnWidth(5, 35 * 256); // Tipo Provvedimento
		hssfs.setColumnWidth(6, 45 * 256); // Esito

		// Intestazione colonne
		setCell(hssfr, 0, "Progr.", hssfcsCenterIntest);
		setCell(hssfr, 1, "Procedimento Sius", hssfcsCenterIntest);
		setCell(hssfr, 2, "Generalità Soggetto", hssfcsCenterIntest);
		setCell(hssfr, 3, "Data Iscrizione", hssfcsCenterIntest);
		setCell(hssfr, 4, "Data Emissione", hssfcsCenterIntest);
		setCell(hssfr, 5, "Provvedimento", hssfcsCenterIntest);
		setCell(hssfr, 6, "Esito", hssfcsCenterIntest);

		i = elenco.iterator();
		while (i.hasNext()) {
			efgspm = (EveFasGepSogProvModel) i.next();

			contatore++;
			hssfr = hssfs.createRow(rowCounter++);

			setCell(hssfr, 0, "" + contatore, hssfcsCenter);

			setCell(hssfr, 1, "" + efgspm.getFascicoloSius().getChiaveAnno() + "/"
					+ efgspm.getFascicoloSius().getChiaveProgr(), hssfcsCenter);

			setCell(hssfr, 2, efgspm.getFascicoloSius().getSoggetto().getCognome() + " "
					+ efgspm.getFascicoloSius().getSoggetto().getNome(), hssfcsCenter);

			setCell(hssfr, 3, StringUtils.toStringJSP(
					DateUtils.getDateToString(efgspm.getFascicoloSius().getDataIscrizione(), patternData),
					"-"), hssfcsCenter);

			setCell(hssfr, 4,
					efgspm.getEvento() != null
							? StringUtils.toStringJSP(DateUtils
									.getDateToString(efgspm.getEvento().getDataEmissione(), patternData), "-")
							: "-",
					hssfcsCenter);

			// Tipo Provvedimento
			setCell(hssfr, 5,
					efgspm.getEvento() != null
							&& Utils.isPresent(efgspm.getEvento().getDescrTipoProvvedimento())
							&& Utils.isPresent(efgspm.getEvento().getDescrMotivo())
									? efgspm.getEvento().getDescrTipoProvvedimento() + " "
											+ efgspm.getEvento().getDescrMotivo()
									: "-",
					hssfcsCenter);
			// Esito
			setCell(hssfr, 6,
					(efgspm.getEvento() != null && Utils.isPresent(efgspm.getEvento().getDescrEsito())
							? efgspm.getEvento().getDescrEsito()
							: " - "),
					hssfcsCenter);
		}

		baos = new ByteArrayOutputStream();

		try {
			hssfw.write(baos);
		} catch (IOException ioe) {
			throw new F3BException(
					"ProcPerStatisticaMisureAlternativeExcel.creaFoglioProcPerStatisticaMisureAlternative: "
							+ ioe);
		}

		return baos;
	}

}