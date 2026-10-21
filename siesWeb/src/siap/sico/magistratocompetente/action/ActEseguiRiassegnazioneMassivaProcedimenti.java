package siap.sico.magistratocompetente.action;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes" })
public class ActEseguiRiassegnazioneMassivaProcedimenti extends ActionSiap
		implements ICostantiMagistratoCompetente {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String param(String aName) throws F3BException {
		return isRequestParameterNullObj(aName) ? "" : getRequestStringParameter(aName).trim();
	}

	private static String nz(String a) {
		return a == null ? "" : a;
	}

	/**
	 * Riassegna al nuovo Magistrato i procedimenti pendenti (dell'intero ufficio o del Magistrato di partenza)
	 * che rispondono al criterio scelto, con le stesse operazioni di "Elenco Procedimenti assegnati", e restituisce
	 * un file Excel con gli estremi dei procedimenti riassegnati.
	 */
	public String processRequest() throws F3BException {

		String lAmbito = param(CAMPO_AMBITO_RIASSEGNAZIONE);
		String lCodMagistratoOld = param(ICostantiMagistrato.CAMPO_COD_MAGISTRATO + "_OLD");
		String lCodMagistratoNew = param(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		if (!AMBITO_RIASSEGNAZIONE_MAGISTRATO.equals(lAmbito)) {
			lAmbito = AMBITO_RIASSEGNAZIONE_UFFICIO;
			lCodMagistratoOld = "";
		} else if (lCodMagistratoOld.length() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Magistrato di partenza non indicato");
		}
		if (lCodMagistratoNew.length() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Selezionare il nuovo Magistrato assegnatario");
		}
		if (lCodMagistratoNew.equals(lCodMagistratoOld)) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Il nuovo Magistrato coincide con il Magistrato di partenza");
		}

		String lTipoCriterio = param(CAMPO_TIPO_CRITERIO_RIASSEGNAZIONE);
		String lValoreCriterio = RiassegnazioneMassivaUtil.normalizzaValore(lTipoCriterio,
				param(CAMPO_VALORE_CRITERIO_RIASSEGNAZIONE));

		MagistratoCompetenteModel lMagCompModel = new MagistratoCompetenteModel();
		lMagCompModel.setMagCodMagistrato(lCodMagistratoNew);
		lMagCompModel.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
				CAMPO_GIORNO_DATA_INIZIO));
		lMagCompModel.setCodRuoloMagistrato("01");
		lMagCompModel.setDataInserimento(DateUtils.getSysDate());
		lMagCompModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lMagCompModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		String lStato[] = { "02", "03" };
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		// i procedimenti gia' assegnati al nuovo Magistrato non vengono riassegnati
		Vector lProcedimenti = ifs.ExRicercaProcedimentiRiassegnazioneMassiva(lCodMagistratoOld,
				getCodUfficioUtenteConnesso(), lStato, lTipoCriterio, lValoreCriterio, lCodMagistratoNew);
		if (lProcedimenti.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Nessun procedimento da riassegnare con il criterio indicato");
		}

		String[] lListaIdFascicoli = new String[lProcedimenti.size()];
		for (int i = 0; i < lProcedimenti.size(); i++) {
			lListaIdFascicoli[i] = ((String[]) lProcedimenti.get(i))[0];
		}
		siesLogger.debug("Riassegnazione massiva - numero fascicoli = " + lListaIdFascicoli.length);

		IMagistratoCompetente lMagCompCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		lMagCompCtrl.ExModificaMultiplaMagistratoCompetente(lMagCompModel, lListaIdFascicoli);

		String lNuovoMagistrato = (param(ICostantiMagistrato.CAMPO_COGNOME) + " "
				+ param(ICostantiMagistrato.CAMPO_NOME)).trim();
		String lDataCompetenza = DateUtils.getDateToString(lMagCompModel.getDataInizio(), "dd/MM/yyyy");

		HSSFWorkbook lExcel = new HSSFWorkbook();
		HSSFSheet lSheet = lExcel.createSheet("Procedimenti riassegnati");
		String[] lIntestazioni = { "Anno", "Numero", "Condannato", "Magistrato precedente",
				"Nuovo magistrato", "Data inizio competenza" };
		HSSFRow lHeader = lSheet.createRow(0);
		for (int c = 0; c < lIntestazioni.length; c++) {
			lHeader.createCell(c).setCellValue(lIntestazioni[c]);
		}
		for (int i = 0; i < lProcedimenti.size(); i++) {
			String[] lRiga = (String[]) lProcedimenti.get(i);
			HSSFRow lRow = lSheet.createRow(i + 1);
			lRow.createCell(0).setCellValue(lRiga[1]);
			lRow.createCell(1).setCellValue(lRiga[2]);
			lRow.createCell(2).setCellValue((nz(lRiga[3]) + " " + nz(lRiga[4])).trim());
			lRow.createCell(3).setCellValue((nz(lRiga[6]) + " " + nz(lRiga[7])).trim());
			lRow.createCell(4).setCellValue(lNuovoMagistrato);
			lRow.createCell(5).setCellValue(lDataCompetenza);
		}
		HSSFRow lTotale = lSheet.createRow(lProcedimenti.size() + 2);
		lTotale.createCell(0).setCellValue("Totale procedimenti riassegnati");
		lTotale.createCell(1).setCellValue(lProcedimenti.size());
		for (int c = 0; c < lIntestazioni.length; c++) {
			lSheet.autoSizeColumn(c);
		}

		ByteArrayOutputStream lOut = new ByteArrayOutputStream();
		try {
			lExcel.write(lOut);
		} catch (java.io.IOException e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Riassegnazione eseguita ma errore nella generazione del file Excel: " + e.getMessage());
		}
		setRequestAttribute("report", lOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}
}
