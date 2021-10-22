package siap.siep.risultatoricerca.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.controller.SiapController;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.risultatoricerca.dao.RisultatoRicercaSqlDAO;
import siap.siep.risultatoricerca.dao.RisultatoRicercaStoreProcedureDAO;
import siap.siep.risultatoricerca.model.RisultatoRicercaModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.util.HSSFUtils;

/**
 * <p>
 * Title: RisultatoRicercaController
 * </p>
 * <p>
 * Description: Classe Controller per RisultatoRicerca
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RisultatoRicercaController extends SiapController implements IRisultatoRicerca {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// ricerca con store procedure
	public BigDecimal ExRicercaConStoreProcedure(String aCodUtente, String aCodUfficio, BigDecimal aAnnoInzio,
			BigDecimal aNumeroInzio, BigDecimal aAnnoFine, BigDecimal aNumeroFine, String aDataReato,
			BigDecimal aAnniRes, BigDecimal aMesiRes, BigDecimal aGiorniRes, String aDataPr,
			BigDecimal aPosAggregata, String aCodPosGiuridica, BigDecimal aAnniSen, BigDecimal aMesiSen,
			BigDecimal aGiorniSen, String aNazione) throws F3BException {

		Connection lConn = null;

		BigDecimal lId = null;
		RisultatoRicercaStoreProcedureDAO lProc = null;
		try {
			lConn = getDBTransaction();

			lProc = new RisultatoRicercaStoreProcedureDAO(lConn);

			lProc.setCodUtente(aCodUtente);
			lProc.setCodUfficio(aCodUfficio);
			lProc.setAnnoIni(aAnnoInzio);
			lProc.setNumIni(aNumeroInzio);
			lProc.setAnnoFin(aAnnoFine);
			lProc.setNumFin(aNumeroFine);
			lProc.setDataReato(aDataReato);
			lProc.setAnniPr(aAnniRes);
			lProc.setMesiPr(aMesiRes);
			lProc.setGiorniPr(aGiorniRes);
			lProc.setDataPr(aDataPr);
			lProc.setPosGiuAgg(aPosAggregata);
			lProc.setCodPosGiu(aCodPosGiuridica);
			lProc.setAnniPenaComp(aAnniSen);
			lProc.setMesiPenaComp(aMesiSen);
			lProc.setGiorniPenaComp(aGiorniSen);
			if (aNazione.equals("I")) {
				lProc.setNazione("I");
			} else if (aNazione.equals("S")) {
				lProc.setNazione("S");
			} else {
				lProc.setNazione("-");
			}

			lProc.execute();

			lId = lProc.getReturn();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("RisultatoRicercaController.ExRicercaConStoreProcedure: " + daoEx);
		} catch (Exception ex) {
			ex.printStackTrace();
			rollback(lConn);
			throw new F3BException("RisultatoRicercaController.ExRicercaConStoreProcedure: " + ex);
		} finally {
			cleanup(lProc);
			cleanup(lConn);
		}

		return lId;
	}

	public Vector ExRicercaRisultatoRicercaByKeyPage(BigDecimal aKey, int aPage) throws F3BException {

		Connection lConn = null;
		RisultatoRicercaSqlDAO lRisDao = null;
		Vector lVect = null;

		try {
			lConn = getDBConnection();
			lRisDao = new RisultatoRicercaSqlDAO(lConn);
			lRisDao.ricercaRisultatoRicercaByKeyPage(aKey, aPage);
			lVect = new Vector(lRisDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RisultatoRicercaController.ExRicercaRisultatoRicercaByKeyPage: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lRisDao);
			cleanup(lConn);
		}
		return lVect;
	}

	public BigDecimal ExGetCountRicercaRisultatoByKey(BigDecimal aKey) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		RisultatoRicercaSqlDAO lSqlDao = null;
		try {
			lConn = getDBConnection();
			lSqlDao = new RisultatoRicercaSqlDAO(lConn);
			lSqlDao.getCountRicercaRisultatoByKey(aKey);
			lSqlDao.start();
			lSqlDao.next();
			lCount = lSqlDao.getBigDecimal("HowManyRecords");
			lSqlDao.stop();
		} catch (DAOException daoEx) {
			throw new F3BException("RisultatoRicercaController.ExGetCountRicercaRisultatoByKey: " + daoEx);
		} finally {
			cleanup(lSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	public Vector ExRicercaCompletoById(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		RisultatoRicercaSqlDAO lRisDao = null;
		Vector lVect = null;

		try {
			lConn = getDBConnection();
			lRisDao = new RisultatoRicercaSqlDAO(lConn);
			lRisDao.ricercaCompletoById(aKey);
			lVect = new Vector(lRisDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RisultatoRicercaController.ExRicercaCompletoById: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRisDao);
			cleanup(lConn);
		}
		return lVect;
	}

	/**
	 * creazione nuovo metodo di ricerca non paginata, per stampa foglio xls. Ricerca Procedimenti per
	 * Applicazione Benefici
	 * <p>
	 *
	 * @param aIdRisultatoRicerca
	 * @param aUfficio
	 * @param aParams
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream ExReportRicercaByIdExcel(String aIdRisultatoRicerca, UfficioModel aUfficio,
			HashMap<String, Object> aParams) throws F3BException {

		// Invocazione metodo estrazione dati.
		Collection<RisultatoRicercaModel> lElenco = ExRicercaRisultatoRicerca(aIdRisultatoRicerca);

		// Preparazione foglio excel.
		HSSFWorkbook lWb = new HSSFWorkbook();
		HSSFSheet lSheet;
		HSSFCellStyle lCellStyleNull;
		HSSFCellStyle lCellStyleCenter;
		// HSSFCellStyle lCellStyleBold;
		HSSFRow lRow;
		Iterator<RisultatoRicercaModel> lItx;
		int lContatore = 0;
		int lRowCounter = 0;
		String lDatePattern = "dd/MM/yyyy";

		// creazione foglio
		lSheet = lWb.createSheet("Elenco Procedimenti per Applicazione Benefici");
		lCellStyleNull = lWb.createCellStyle();

		HSSFFont my_font = lWb.createFont();
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// stile per celle col bordo con testo centrato e grossetto
		HSSFCellStyle csCenterBold = getBordo4LatiBold(lWb);
		csCenterBold.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csCenterBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csCenterBold.setWrapText(true);
		csCenterBold.setFont(my_font);

		// Intestazione del foglio excel
		lRowCounter = HSSFUtils.getInstance().setIntestazione(lSheet, aUfficio, lCellStyleNull);
		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Criteri di ricerca selezionati : ", lCellStyleNull);
		lRowCounter++;

		// Anno/Numero Iniziale
		if (aParams.get("lAnnoInzio") != null) {
			String lAnnoInzio = ((BigDecimal) aParams.get("lAnnoInzio")).toString();
			String lNumeroInzio = ((BigDecimal) aParams.get("lNumeroInzio")).toString();
			if (lAnnoInzio.equals("null")) {
				lAnnoInzio = "";
				lNumeroInzio = "";
			}
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0,
					"Anno/Numero Iniziale: " + lAnnoInzio + "/" + lNumeroInzio, lCellStyleNull);
			lRowCounter++;
		}

		// Anno/Numero Finale
		if (aParams.get("lAnnoFine") != null) {
			String lAnnoFine = ((BigDecimal) aParams.get("lAnnoFine")).toString();
			String lNumeroFine = ((BigDecimal) aParams.get("lNumeroFine")).toString();
			if (lAnnoFine.equals("null")) {
				lAnnoFine = "";
				lNumeroFine = "";
			}
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Anno/Numero Finale: " + lAnnoFine + "/" + lNumeroFine,
					lCellStyleNull);
			lRowCounter++;
		}

		// Data massima di commesso reato
		if (aParams.get("lDataReato") != null && !aParams.get("lDataReato").equals("null")) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0,
					"Data massima di commesso reato: " + aParams.get("lDataReato"), lCellStyleNull);
			lRowCounter++;
		}
		// Quantum di pena residua da espiare
		String lAnniRes = "0";
		String lMesiRes = "0";
		String lGiorniRes = "0";
		if (aParams.get("lAnniRes") != null) {
			lAnniRes = ((BigDecimal) aParams.get("lAnniRes")).toString();
		}

		if (aParams.get("lMesiRes") != null) {
			lMesiRes = ((BigDecimal) aParams.get("lMesiRes")).toString();
		}

		if (aParams.get("lGiorniRes") != null) {
			lGiorniRes = ((BigDecimal) aParams.get("lGiorniRes")).toString();
		}

		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Quantum di pena residua da espiare: Anni: " + lAnniRes
				+ " Mesi:" + lMesiRes + " Giorni: " + lGiorniRes, lCellStyleNull);
		lRowCounter++;

		// Posizione giuridica aggregata
		if (aParams.get("lPosAggregata") != null) {
			String descPosGiu = "";
			if (aParams.get("lPosAggregata").equals(new BigDecimal(0))) {
				descPosGiu = "Nessuna";
			} else if (aParams.get("lPosAggregata").equals(new BigDecimal(1))) {
				descPosGiu = "In espiazione pena in carcere";
			} else if (aParams.get("lPosAggregata").equals(new BigDecimal(2))) {
				descPosGiu = "In misura tutte";
			} else if (aParams.get("lPosAggregata").equals(new BigDecimal(3))) {
				descPosGiu = "Libero e assimilati";
			}

			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Posizione giuridica aggregata: " + descPosGiu,
					lCellStyleNull);
			lRowCounter++;
		}

		// Singola posizione giuridica
		if (aParams.get("lDescPosGiuridica") != null && !aParams.get("lDescPosGiuridica").equals("null")) {
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0,
					"Singola posizione giuridica: " + aParams.get("lDescPosGiuridica"), lCellStyleNull);
			lRowCounter++;
		}

		// Quantum pena irrogata in sentenza
		String lAnniSen = "0";
		String lMesiSen = "0";
		String lGiorniSen = "0";
		if (aParams.get("lAnniSen") != null) {
			lAnniSen = ((BigDecimal) aParams.get("lAnniSen")).toString();
		}
		if (aParams.get("lMesiSen") != null) {
			lMesiSen = ((BigDecimal) aParams.get("lMesiSen")).toString();
		}
		if (aParams.get("lGiorniSen") != null) {
			lGiorniSen = ((BigDecimal) aParams.get("lGiorniSen")).toString();
		}

		lRow = lSheet.createRow(lRowCounter);
		HSSFUtils.getInstance().setCell(lRow, 0, "Quantum pena irrogata in sentenza: Anni: " + lAnniSen
				+ " Mesi:" + lMesiSen + " Giorni: " + lGiorniSen, lCellStyleNull);
		lRowCounter++;

		// Nazionalità
		if (aParams.get("lNazione") != null) {
			String nazionalita = "";
			if (aParams.get("lNazione").equals("I")) {
				nazionalita = "Italiana";
			} else if (aParams.get("lNazione").equals("S")) {
				nazionalita = "Straniera";
			}
			lRow = lSheet.createRow(lRowCounter);
			HSSFUtils.getInstance().setCell(lRow, 0, "Nazionalità: " + nazionalita, lCellStyleNull);
			lRowCounter++;
		}

		lRow = lSheet.createRow(lRowCounter);

		HSSFUtils.getInstance().setCell(lRow, (short) 0, "", lCellStyleNull);
		lRowCounter += 2;

		// impostazione della larghezza
		// delle colonne
		lSheet.setColumnWidth(0, (short) (10 * 256)); // Prog
		lSheet.setColumnWidth(1, (short) (15 * 256)); // N°SIEP
		lSheet.setColumnWidth(2, (short) (20 * 256)); // Cognome
		lSheet.setColumnWidth(3, (short) (20 * 256)); // Nome
		lSheet.setColumnWidth(4, (short) (20 * 256)); // Luogo di Nascita
		lSheet.setColumnWidth(5, (short) (15 * 256)); // Data di Nascita
		lSheet.setColumnWidth(6, (short) (15 * 256)); // Data Fine Pena
		lSheet.setColumnWidth(7, (short) (25 * 256)); // Pena Residua
		lSheet.setColumnWidth(8, (short) (40 * 256)); // Posizione Giuridica
		lSheet.setColumnWidth(9, (short) (25 * 256)); // Nazionalità

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = HSSFUtils.getInstance().getBordo4Lati(lWb);
		lCellStyleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		lCellStyleCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		// Intestazione colonne
		HSSFUtils.getInstance().setCell(lRow, (short) 0, "Progr.", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 1, "N°SIEP", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 2, "Cognome", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 3, "Nome", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 4, "Luogo di Nascita", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 5, "Data di Nascita", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 6, "Data Fine Pena", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 7, "Pena Residua", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 8, "Posizione Giuridica", csCenterBold);
		HSSFUtils.getInstance().setCell(lRow, (short) 9, "Nazionalità", csCenterBold);

		lItx = lElenco.iterator();
		// inizio ciclo di scrittura dei dati

		while (lItx.hasNext()) {
			lContatore++;
			RisultatoRicercaModel lModel = lItx.next();

			lRow = lSheet.createRow(lRowCounter++);

			HSSFUtils.getInstance().setCell(lRow, (short) 0, "" + lContatore, lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 1,
					lModel.getChiaveAnno() + "/" + lModel.getChiaveProgr(), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 2,
					StringUtils.toStringJSP(lModel.getCognome(), "-"), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 3, StringUtils.toStringJSP(lModel.getNome(), "-"),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 4,
					StringUtils.toStringJSP(lModel.getLuogoNascita(), "-"), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 5,
					StringUtils.cStrForJS(DateUtils.getDateToString(lModel.getDataNascita(), lDatePattern)),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 6,
					StringUtils.cStrForJS(DateUtils.getDateToString(lModel.getDataFinePena(), lDatePattern)),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 7,
					"Anni: " + StringUtils.toStringJSP(lModel.getNumAnniPenaRes(), "-") + " Mesi: "
							+ StringUtils.toStringJSP(lModel.getNumMesiPenaRes(), "-") + " Giorni: "
							+ StringUtils.toStringJSP(lModel.getNumGiorniPenaRes(), "-"),
					lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 8,
					StringUtils.toStringJSP(lModel.getDescrPosizioneGiuridica(), "-"), lCellStyleCenter);

			HSSFUtils.getInstance().setCell(lRow, (short) 9,
					StringUtils.toStringJSP(lModel.getNazionalita(), "-"), lCellStyleCenter);

		}

		// Generazione foglio excel e ineristo by referece nell'oggetto BAOS
		ByteArrayOutputStream lBAOS = new ByteArrayOutputStream();
		try {
			lWb.write(lBAOS);
		} catch (IOException e) {
			e.printStackTrace();
			throw new F3BException(e);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("######### creaFoglioElencoProc STOP ########");
		return lBAOS;
	}

	/**
	 * creazione nuovo metodo di ricerca non paginata, per stampa folglio xls. Ricerca Procedimenti per
	 * Applicazione Benefici
	 * <p>
	 *
	 * @param aFascModel
	 * @param sTipoAtto
	 * @param aCancAssFascSius
	 * @param aFiltroCollaboratore
	 * @return
	 * @throws F3BException
	 */
	public Collection<RisultatoRicercaModel> ExRicercaRisultatoRicerca(String aIdRisultatoRicerca)
			throws F3BException {

		Connection lConn = null;
		Collection<RisultatoRicercaModel> lRisultatoRicerca = new ArrayList<>();

		RisultatoRicercaSqlDAO lRisDao = null;

		try {
			lConn = getDBConnection();
			lRisDao = new RisultatoRicercaSqlDAO(lConn);
			lRisDao.ricercaCompletoById(new BigDecimal(aIdRisultatoRicerca));
			lRisDao.start();
			lRisultatoRicerca = lRisDao.getModels();

			if (lRisultatoRicerca.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"RisultatoRicercaController.ExRicercaRisultatoRicerca: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lRisDao);
			cleanup(lConn);
		}
		return lRisultatoRicerca;
	}

	private HSSFCellStyle getBordo4LatiBold(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THICK);
		cs.setBorderTop(HSSFCellStyle.BORDER_THICK);
		cs.setBorderRight(HSSFCellStyle.BORDER_THICK);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THICK);

		return cs;
	}

}