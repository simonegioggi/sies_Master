package siap.sius.scadenzario.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class RicercaFinePenaProcedimentiPendentiExcel extends SIAPExcelProducer {

	/**
	 * Metodo per la stampa in Excel
	 *
	 * @param ufficioUtenteConnesso
	 * @param l
	 *
	 * @return ByteArrayOutputStream
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ByteArrayOutputStream stampaExcel(UfficioModel ufficioUtenteConnesso, List<Object> l)
			throws F3BException {

		ByteArrayOutputStream baos = null;
		HSSFWorkbook lWb = null;
		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator iter = null;
		int lRowCounter = 0;
		ScadenzarioSiusModel ssm = null;
		String lPatternData = "dd/MM/yyyy";
		IScadenzarioSius iss = null;

		lWb = new HSSFWorkbook();

		iss = SIUSLookupRemote.getScadenzarioRemote();
		Vector<ScadenzarioSiusModel> v = iss.ExRicercaFinePenaProcedimentiPendentiPaginata("" + l.get(0),
				(BigDecimal) l.get(1), (BigDecimal) l.get(2), (BigDecimal) l.get(3), (BigDecimal) l.get(4),
				(Date) l.get(5), (Date) l.get(6), (Date) l.get(7), (Date) l.get(8), "" + l.get(9),
				(Boolean) l.get(11), 0);

		// creazione foglio
		lSheet = lWb.createSheet("Elenco Procedimenti Pendenti");
		lCellStyleNull = lWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, ufficioUtenteConnesso, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter);
		setCell(lRow, 0, (String) l.get(10), lCellStyleNull);
		lRowCounter++;

		// Stampa filtri di ricerca
		lRowCounter += 1;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 25 * 256); // Procedimento SIUS
		lSheet.setColumnWidth(1, 30 * 256); // Soggetto
		lSheet.setColumnWidth(2, 25 * 256); // Luogo Nascita
		lSheet.setColumnWidth(3, 15 * 256); // Data Nascita
		lSheet.setColumnWidth(4, 25 * 256); // Posizione Giuridica
		lSheet.setColumnWidth(5, 35 * 256); // Contenuto
		lSheet.setColumnWidth(6, 15 * 256); // Data Inizio Pena
		lSheet.setColumnWidth(7, 15 * 256); // Data Fine Pena
		lSheet.setColumnWidth(8, 15 * 256); // Giorni Residui
		lSheet.setColumnWidth(9, 20 * 256); // Fine Pena Virtuale
		lSheet.setColumnWidth(10, 15 * 256); // Giorni Residui
		lSheet.setColumnWidth(11, 20 * 256); // Procedimento SIEP

		// Intestazione colonne
		setCell(lRow, 0, "Procedimento SIUS", lCellStyleCenter);
		setCell(lRow, 1, "Soggetto", lCellStyleCenter);
		setCell(lRow, 2, "Luogo Nascita", lCellStyleCenter);
		setCell(lRow, 3, "Data Nascita", lCellStyleCenter);
		setCell(lRow, 4, "Posizione Giuridica", lCellStyleCenter);
		setCell(lRow, 5, "Contenuto", lCellStyleCenter);
		setCell(lRow, 6, "Data Inizio Pena", lCellStyleCenter);
		setCell(lRow, 7, "Data Fine Pena", lCellStyleCenter);
		setCell(lRow, 8, "Giorni Residui", lCellStyleCenter);
		setCell(lRow, 9, "Fine Pena Virtuale", lCellStyleCenter);
		setCell(lRow, 10, "Giorni Residui", lCellStyleCenter);
		setCell(lRow, 11, "Procedimento SIEP", lCellStyleCenter);

		iter = v.iterator();
		while (iter.hasNext()) {
			ssm = (ScadenzarioSiusModel) iter.next();

			int giorniResidui = Utils.isNullObj(ssm.getGiorniResidui()) ? 0
					: ssm.getGiorniResidui().intValue();
			int giorniResiduiVirtuali = Utils.isNullObj(ssm.getGiorniResiduiVirtuali()) ? 0
					: ssm.getGiorniResiduiVirtuali().intValue();
			String straniero = (Utils
					.isPresent(ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero())
					&& !"-".equals(ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero()))
							? ssm.getFascicoloSius().getSoggetto().getDescComuneNascitaEstero()
							: "";
			String luogoNascitaStraniero = (Utils.isPresent(straniero))
					? straniero + " (" + ssm.getFascicoloSius().getSoggetto().getDescrStatoNascita() + ")"
					: ssm.getFascicoloSius().getSoggetto().getDescrStatoNascita();
			String luogoNascita = (Utils
					.isPresent(ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita())
					&& !"-".equals(ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita()))
							? ssm.getFascicoloSius().getSoggetto().getDescrComuneNascita()
							: luogoNascitaStraniero;

			lRow = lSheet.createRow(lRowCounter++);

			// Procedimento SIUS (Descrizione Stato Fascicolo)
			setCell(lRow, 0,
					"" + ssm.getFascicoloSius().getChiaveAnno() + "/"
							+ ssm.getFascicoloSius().getChiaveProgr() + "\n("
							+ ssm.getFascicoloSius().getDescrStatoFascicolo() + ")",
					lCellStyleCenter);
			// Soggetto
			setCell(lRow, 1, ssm.getFascicoloSius().getSoggetto().getCognome() + " "
					+ ssm.getFascicoloSius().getSoggetto().getNome(), lCellStyleCenter);
			// Luogo Nascita
			setCell(lRow, 2, StringUtils.toStringJSP(luogoNascita, "-"), lCellStyleCenter);
			// Data Nascita
			setCell(lRow, 3,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							ssm.getFascicoloSius().getSoggetto().getDataNascita(), lPatternData), "-"),
					lCellStyleCenter);
			// Posizione Giuridica
			setCell(lRow, 4,
					StringUtils.toStringJSP(ssm.getPosizioneGiuridica().getDescrPosizioneGiuridica(), "-"),
					lCellStyleCenter);
			// Contenuto
			setCell(lRow, 5,
					StringUtils.toStringJSP(ssm.getGeneraleProcedimento().getDescrOggettoProcedimento(), "-"),
					lCellStyleCenter);
			// Data Inizio Pena
			setCell(lRow, 6,
					StringUtils.toStringJSP(
							DateUtils.getDateToString(ssm.getDataInizioScadenza(), lPatternData), "-"),
					lCellStyleCenter);
			// Data Fine Pena
			setCell(lRow, 7,
					StringUtils.toStringJSP(
							DateUtils.getDateToString(ssm.getDataFineScadenza(), lPatternData), "-"),
					lCellStyleCenter);
			// Giorni Residui
			setCell(lRow, 8, (Utils.isNullObj(ssm.getDataFineScadenza())) ? "-" : "" + giorniResidui,
					lCellStyleCenter);
			// Fine Pena Virtuale
			setCell(lRow, 9,
					StringUtils.toStringJSP(
							DateUtils.getDateToString(ssm.getDataFinePenaVirtuale(), lPatternData), "-"),
					lCellStyleCenter);
			// Giorni Residui Virtuali
			setCell(lRow, 10,
					(Utils.isNullObj(ssm.getDataFinePenaVirtuale())) ? "-" : "" + giorniResiduiVirtuali,
					lCellStyleCenter);
			// Procedimento SIEP (anno/numero tipo (tipoufficio descComuneUfficio)
			setCell(lRow, 11,
					"" + ssm.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep() + "/"
							+ ssm.getRiferimentoFascicoloSiep().getProgrFascicoloSiep() + "\n("
							+ ssm.getRiferimentoFascicoloSiep().getCodUffFascicoloSiep() + " "
							+ ssm.getRiferimentoFascicoloSiep().getDescrUffFascicoloSiep() + ")",
					lCellStyleCenter);
		}

		baos = new ByteArrayOutputStream();

		try {
			lWb.write(baos);
		} catch (IOException ioe) {
			throw new F3BException("RicercaFinePenaProcedimentiPendentiExcel: " + ioe);
		}

		return baos;
	}

}