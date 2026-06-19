package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.Utils;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.IspConteggioOggettiModel;
import siap.sius.statistiche.model.IspConteggioRelatoriMagistratiModel;
import siap.sius.statistiche.model.IspEstrazioneOggettiModel;
import siap.sius.statistiche.model.IspMotivoOggettoSelezionatiModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;

public class StatisticaComparataMagistratiExcel extends SIAPExcelProducer {

	public ByteArrayOutputStream creaStatisticaComparataMagistratiExcel(
			RicercaProcedimentoModel aRicercaModel, UfficioModel aUfficioUtenteConnesso) throws F3BException {

		ByteArrayOutputStream lFileOut = null;
		HSSFWorkbook lWb = null;

		lWb = new HSSFWorkbook();

		this.creaFoglioStatisticaComparataMagistratiExcel(lWb, aUfficioUtenteConnesso, aRicercaModel);
		this.creaStatisticaComparataMagistratiOggettiExcel(lWb, aUfficioUtenteConnesso, aRicercaModel);
		this.creaStatisticaDettagliOggettiDelMagistrato(lWb, aUfficioUtenteConnesso, aRicercaModel);

		lFileOut = new ByteArrayOutputStream();

		try {
			lWb.write(lFileOut);
		} catch (IOException ioe) {
			throw new F3BException("StatisController.creaFoglioProcAggrIstitutiDetenzione : " + ioe);
		}

		return lFileOut;
	}

	protected void creaFoglioStatisticaComparataMagistratiExcel(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicercaModel) throws F3BException {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator<IspConteggioRelatoriMagistratiModel> lItx = null;
		int lRowCounter = 0;
		IspConteggioRelatoriMagistratiModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		IStatisticheSius lCtrlStatSius = null;
		Collection<IspConteggioRelatoriMagistratiModel> lElenco = null;

		lSheet = aWb.createSheet("Totali per Magistrato");
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aRicercaModel.getUtenteConnesso().getUfficioUtente(),
				lCellStyleNull);

		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		lBuffer = "Statistica comparata dei Magistrati del "
				+ DateUtils.getDateToString(DateUtils.getSysDate(), lPatternData) + " ";
		if (aRicercaModel != null) {
			if (aRicercaModel.getDataIscrizioneInizio() != null) {
				lBuffer += " dal "
						+ DateUtils.getDateToString(aRicercaModel.getDataIscrizioneInizio(), lPatternData);
			}
			if (aRicercaModel.getDataIscrizioneFine() != null) {
				lBuffer += " al "
						+ DateUtils.getDateToString(aRicercaModel.getDataIscrizioneFine(), lPatternData);
			}
		}
		lBuffer += " per specifici oggetti.";
		setCell(lRow, 0, lBuffer, lCellStyleNull);
		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 40 * 256); // Magistrato
		lSheet.setColumnWidth(1, 15 * 256); // Pendenti Inizio Periodo
		lSheet.setColumnWidth(2, 15 * 256); // Sopravvenuti
		lSheet.setColumnWidth(3, 15 * 256); // Accolti
		lSheet.setColumnWidth(4, 15 * 256); // Rigettati
		lSheet.setColumnWidth(5, 15 * 256); // Inammissibilit?
		lSheet.setColumnWidth(6, 15 * 256); // NLP/NDP
		lSheet.setColumnWidth(7, 15 * 256); // Incompetenza
		lSheet.setColumnWidth(8, 15 * 256); // Iscritti per Errore
		lSheet.setColumnWidth(9, 15 * 256); // Unificati
		lSheet.setColumnWidth(10, 15 * 256); // Cancellati
		lSheet.setColumnWidth(11, 15 * 256); // Altro
		lSheet.setColumnWidth(12, 15 * 256); // Pendenti Fine Periodo

		// Intestazione colonne
		setCell(lRow, 0, "Magistrato", lCellStyleCenter);
		setCell(lRow, 1, "Pendenti Inizio Periodo", lCellStyleCenter);
		setCell(lRow, 2, "Sopravvenuti", lCellStyleCenter);
		setCell(lRow, 3, "Accolti", lCellStyleCenter);
		setCell(lRow, 4, "Rigettati", lCellStyleCenter);
		setCell(lRow, 5, "Inammissibilit?", lCellStyleCenter);
		setCell(lRow, 6, "NLP/NDP", lCellStyleCenter);
		setCell(lRow, 7, "Incompetenza", lCellStyleCenter);
		setCell(lRow, 8, "Iscritti per Errore", lCellStyleCenter);
		setCell(lRow, 9, "Unificati", lCellStyleCenter);
		setCell(lRow, 10, "Cancellati", lCellStyleCenter);
		setCell(lRow, 11, "Altro", lCellStyleCenter);
		setCell(lRow, 12, "Pendenti Fine Periodo", lCellStyleCenter);

		BigDecimal totalePendentiInizio = new BigDecimal("0");
		BigDecimal totaleSopravvenuti = new BigDecimal("0");
		BigDecimal totaleAccolti = new BigDecimal("0");
		BigDecimal totaleRigettati = new BigDecimal("0");
		BigDecimal totaleInammissibilita = new BigDecimal("0");
		BigDecimal totaleNLPNDP = new BigDecimal("0");
		BigDecimal totaleIncompetenza = new BigDecimal("0");
		BigDecimal totaleAltro = new BigDecimal("0");
		BigDecimal totalePendentiFine = new BigDecimal("0");
		BigDecimal totalePerErrore = new BigDecimal("0");
		BigDecimal totaleCancellati = new BigDecimal("0");
		BigDecimal totaleUnificati = new BigDecimal("0");

		lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
		lElenco = lCtrlStatSius.ExRicercaStatisticaComparataMagistrati(aRicercaModel);

		lItx = lElenco.iterator();
		while (lItx.hasNext()) {
			lModel = lItx.next();

			totalePendentiInizio = totalePendentiInizio.add(lModel.getNumPendentiInizio());
			totaleSopravvenuti = totaleSopravvenuti.add(lModel.getNumSopravvenuti());
			totaleAccolti = totaleAccolti.add(lModel.getNumDefEsito1());
			totaleRigettati = totaleRigettati.add(lModel.getNumDefEsito2());
			totaleInammissibilita = totaleInammissibilita.add(lModel.getNumDefEsito3());
			totaleNLPNDP = totaleNLPNDP.add(lModel.getNumDefEsito4());
			totaleIncompetenza = totaleIncompetenza.add(lModel.getNumDefEsito5());
			totalePerErrore = totalePerErrore.add(lModel.getNumDefIscErr());
			totaleUnificati = totaleUnificati.add(lModel.getNumUnificati());
			totaleCancellati = totaleCancellati.add(lModel.getNumCancellati());
			totaleAltro = totaleAltro.add(lModel.getNumDefEsito6());
			totalePendentiFine = totalePendentiFine.add(lModel.getNumPendentiFine());

			lRow = lSheet.createRow(lRowCounter++);

			if (lModel.getMagistrato().getCodMagistrato() != null) {
				// Ticket#20230419018: tolgo StringUtils.cStrForJS x nome e cognome
				lBuffer = lModel.getMagistrato().getCognome() + " " + lModel.getMagistrato().getNome();
			} else {
				lBuffer = "";
			}
			setCell(lRow, 0, lBuffer, lCellStyleCenter);
			setCell(lRow, 1, StringUtils.cStrForJS("" + lModel.getNumPendentiInizio()), lCellStyleCenter);
			setCell(lRow, 2, StringUtils.cStrForJS("" + lModel.getNumSopravvenuti()), lCellStyleCenter);
			setCell(lRow, 3, StringUtils.cStrForJS("" + lModel.getNumDefEsito1()), lCellStyleCenter);
			setCell(lRow, 4, StringUtils.cStrForJS("" + lModel.getNumDefEsito2()), lCellStyleCenter);
			setCell(lRow, 5, StringUtils.cStrForJS("" + lModel.getNumDefEsito3()), lCellStyleCenter);
			setCell(lRow, 6, StringUtils.cStrForJS("" + lModel.getNumDefEsito4()), lCellStyleCenter);
			setCell(lRow, 7, StringUtils.cStrForJS("" + lModel.getNumDefEsito5()), lCellStyleCenter);
			setCell(lRow, 8, StringUtils.cStrForJS("" + lModel.getNumDefIscErr()), lCellStyleCenter);
			setCell(lRow, 9, StringUtils.cStrForJS("" + lModel.getNumUnificati()), lCellStyleCenter);
			setCell(lRow, 10, StringUtils.cStrForJS("" + lModel.getNumCancellati()), lCellStyleCenter);
			setCell(lRow, 11, StringUtils.cStrForJS("" + lModel.getNumDefEsito6()), lCellStyleCenter);
			setCell(lRow, 12, StringUtils.cStrForJS("" + lModel.getNumPendentiFine()), lCellStyleCenter);
		}

		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter++);

		setCell(lRow, 0, "TOTALI", lCellStyleCenter);
		setCell(lRow, 1, StringUtils.cStrForJS("" + totalePendentiInizio), lCellStyleCenter);
		setCell(lRow, 2, StringUtils.cStrForJS("" + totaleSopravvenuti), lCellStyleCenter);
		setCell(lRow, 3, StringUtils.cStrForJS("" + totaleAccolti), lCellStyleCenter);
		setCell(lRow, 4, StringUtils.cStrForJS("" + totaleRigettati), lCellStyleCenter);
		setCell(lRow, 5, StringUtils.cStrForJS("" + totaleInammissibilita), lCellStyleCenter);
		setCell(lRow, 6, StringUtils.cStrForJS("" + totaleNLPNDP), lCellStyleCenter);
		setCell(lRow, 7, StringUtils.cStrForJS("" + totaleIncompetenza), lCellStyleCenter);
		setCell(lRow, 8, StringUtils.cStrForJS("" + totalePerErrore), lCellStyleCenter);
		setCell(lRow, 9, StringUtils.cStrForJS("" + totaleUnificati), lCellStyleCenter);
		setCell(lRow, 10, StringUtils.cStrForJS("" + totaleCancellati), lCellStyleCenter);
		setCell(lRow, 11, StringUtils.cStrForJS("" + totaleAltro), lCellStyleCenter);
		setCell(lRow, 12, StringUtils.cStrForJS("" + totalePendentiFine), lCellStyleCenter);

	}

	protected void creaStatisticaComparataMagistratiOggettiExcel(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicerca) throws F3BException {

		String[] lCodMagistrati = aRicerca.getCodMagistrati();
		Collection<IspConteggioOggettiModel> lOggetti = null;
		IStatisticheSius lCtrl = null;
		IMagistrato lCtrlMag = null;
		lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
		lCtrlMag = SICOLookupRemote.getMagistratoRemote();

		for (String lCodMagistrato : lCodMagistrati) {
			aRicerca.setCodMagistrato(lCodMagistrato);
			lOggetti = lCtrl.ExConteggioOggettiMagistrato(aRicerca);
			// Recupera i dati del magistrato
			MagistratoModel lMagistrato = lCtrlMag.ExRicercaMagistratoByCod(lCodMagistrato);
			this.creaFoglioDettagliPerMagistrato(aWb, lOggetti, aUfficioUtenteConnesso, aRicerca,
					lMagistrato);
		}
	}

	private void creaFoglioDettagliPerMagistrato(HSSFWorkbook aWb,
			Collection<IspConteggioOggettiModel> aOggetti, UfficioModel aUfficioUtenteConnesso,
			RicercaProcedimentoModel aRicerca, MagistratoModel aMagistrato) {

		HSSFSheet lSheet = null;
		HSSFCellStyle lCellStyleNull = null;
		HSSFCellStyle lCellStyleCenter = null;
		HSSFRow lRow = null;
		Iterator<IspConteggioOggettiModel> lItx = null;
		int lRowCounter = 0;
		IspConteggioOggettiModel lModel = null;
		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		// IspMotivoOggettoSelezionatiDAO lMotivoOggettoDao = null;
		// Collection<IspMotivoOggettoSelezionatiModel> lMotivoOggetti = null;
		// IspMotivoOggettoSelezionatiModel lMotivoOggetto = null;

		// Ticket#20230419018 - si bonifica il nome del foglio excel che potrebbe contenere le accentate
		// del nome del magistrato
		// lSheet = aWb.createSheet("Dettaglio " + aMagistrato.getCognome() + " " + aMagistrato.getNome());
		lSheet = aWb.createSheet(StringUtils
				.encodeExcelSheetName("Dettaglio " + aMagistrato.getCognome() + " " + aMagistrato.getNome()));
		// Ticket#20230419018 - FINE
		lCellStyleNull = aWb.createCellStyle();

		// Intestazione del foglio excel
		lRowCounter = setIntestazione(lSheet, aRicerca.getUtenteConnesso().getUfficioUtente(),
				lCellStyleNull);

		lRowCounter += 2;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(lRowCounter);
		lBuffer = "Statistica del " + DateUtils.getDateToString(DateUtils.getSysDate(), lPatternData)
				+ " relative al periodo ";
		if (aRicerca != null) {
			if (aRicerca.getDataIscrizioneInizio() != null) {
				lBuffer += " dal "
						+ DateUtils.getDateToString(aRicerca.getDataIscrizioneInizio(), lPatternData);
			}
			if (aRicerca.getDataIscrizioneFine() != null) {
				lBuffer += " al " + DateUtils.getDateToString(aRicerca.getDataIscrizioneFine(), lPatternData);
			}
		}
		lBuffer += " ed agli atti riferiti al Magistrato : " + aMagistrato.getCognome() + " "
				+ aMagistrato.getNome();
		setCell(lRow, 0, lBuffer, lCellStyleNull);
		lRowCounter += 2;

		// stile per celle col bordo con testo centrato
		lCellStyleCenter = getBordo4Lati(aWb);
		lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		lCellStyleCenter.setWrapText(true);

		lRow = lSheet.createRow(lRowCounter++);

		lSheet.setColumnWidth(0, 15 * 256); // Contenuto
		lSheet.setColumnWidth(1, 40 * 256); // Oggetto
		lSheet.setColumnWidth(2, 10 * 256); // Pendenti Inizio Periodo
		lSheet.setColumnWidth(3, 10 * 256); // Sopravvenuti
		lSheet.setColumnWidth(4, 10 * 256); // Accolti
		lSheet.setColumnWidth(5, 10 * 256); // Rigettati
		lSheet.setColumnWidth(6, 10 * 256); // Inammissibilit?
		lSheet.setColumnWidth(7, 10 * 256); // NLP/NDP
		lSheet.setColumnWidth(8, 10 * 256); // Incompetenza
		lSheet.setColumnWidth(9, 10 * 256); // Iscritti per Errore
		lSheet.setColumnWidth(10, 10 * 256); // Unificati
		lSheet.setColumnWidth(11, 10 * 256); // Cancellati
		lSheet.setColumnWidth(12, 10 * 256); // Altro
		lSheet.setColumnWidth(13, 10 * 256); // Pendenti Fine Periodo

		// Intestazione colonne
		setCell(lRow, 0, "Contenuto", lCellStyleCenter);
		setCell(lRow, 1, "Oggetto", lCellStyleCenter);
		setCell(lRow, 2, "Pendenti Inizio Periodo", lCellStyleCenter);
		setCell(lRow, 3, "Sopravvenuti", lCellStyleCenter);
		setCell(lRow, 4, "Accolti", lCellStyleCenter);
		setCell(lRow, 5, "Rigettati", lCellStyleCenter);
		setCell(lRow, 6, "Inammissibilit?", lCellStyleCenter);
		setCell(lRow, 7, "NLP/NDP", lCellStyleCenter);
		setCell(lRow, 8, "Incompetenza", lCellStyleCenter);
		setCell(lRow, 9, "Iscritti per Errore", lCellStyleCenter);
		setCell(lRow, 10, "Unificati", lCellStyleCenter);
		setCell(lRow, 11, "Cancellati", lCellStyleCenter);
		setCell(lRow, 12, "Altro", lCellStyleCenter);
		setCell(lRow, 13, "Pendenti Fine Periodo", lCellStyleCenter);

		BigDecimal totalePendentiInizio = new BigDecimal("0");
		BigDecimal totaleSopravvenuti = new BigDecimal("0");
		BigDecimal totaleAccolti = new BigDecimal("0");
		BigDecimal totaleRigettati = new BigDecimal("0");
		BigDecimal totaleInammissibilita = new BigDecimal("0");
		BigDecimal totaleNLPNDP = new BigDecimal("0");
		BigDecimal totaleIncompetenza = new BigDecimal("0");
		BigDecimal totaleAltro = new BigDecimal("0");
		BigDecimal totalePendentiFine = new BigDecimal("0");
		BigDecimal totalePerErrore = new BigDecimal("0");
		BigDecimal totaleCancellati = new BigDecimal("0");
		BigDecimal totaleUnificati = new BigDecimal("0");

		lItx = aOggetti.iterator();
		while (lItx.hasNext()) {
			lModel = lItx.next();
			lRow = lSheet.createRow(lRowCounter++);

			setCell(lRow, 0, StringUtils.cStrForJS("" + lModel.getDescContenutoStatis()), lCellStyleCenter);
			setCell(lRow, 1, StringUtils.cStrForJS("" + lModel.getDescOggetto()), lCellStyleCenter);
			setCell(lRow, 2, StringUtils.cStrForJS("" + lModel.getNumPendentiInizio()), lCellStyleCenter);
			setCell(lRow, 3, StringUtils.cStrForJS("" + lModel.getNumSopravvenuti()), lCellStyleCenter);
			setCell(lRow, 4, StringUtils.cStrForJS("" + lModel.getNumDefEsito1()), lCellStyleCenter);
			setCell(lRow, 5, StringUtils.cStrForJS("" + lModel.getNumDefEsito2()), lCellStyleCenter);
			setCell(lRow, 6, StringUtils.cStrForJS("" + lModel.getNumDefEsito3()), lCellStyleCenter);
			setCell(lRow, 7, StringUtils.cStrForJS("" + lModel.getNumDefEsito4()), lCellStyleCenter);
			setCell(lRow, 8, StringUtils.cStrForJS("" + lModel.getNumDefEsito5()), lCellStyleCenter);
			setCell(lRow, 9, StringUtils.cStrForJS("" + lModel.getNumDefIscErr()), lCellStyleCenter);
			setCell(lRow, 10, StringUtils.cStrForJS("" + lModel.getNumUnificati()), lCellStyleCenter);
			setCell(lRow, 11, StringUtils.cStrForJS("" + lModel.getNumCancellati()), lCellStyleCenter);
			setCell(lRow, 12, StringUtils.cStrForJS("" + lModel.getNumDefEsito6()), lCellStyleCenter);
			setCell(lRow, 13, StringUtils.cStrForJS("" + lModel.getNumPendentiFine()), lCellStyleCenter);

			totalePendentiInizio = totalePendentiInizio.add(lModel.getNumPendentiInizio());
			totaleSopravvenuti = totaleSopravvenuti.add(lModel.getNumSopravvenuti());
			totaleAccolti = totaleAccolti.add(lModel.getNumDefEsito1());
			totaleRigettati = totaleRigettati.add(lModel.getNumDefEsito2());
			totaleInammissibilita = totaleInammissibilita.add(lModel.getNumDefEsito3());
			totaleNLPNDP = totaleNLPNDP.add(lModel.getNumDefEsito4());
			totaleIncompetenza = totaleIncompetenza.add(lModel.getNumDefEsito5());
			totalePerErrore = totalePerErrore.add(lModel.getNumDefIscErr());
			totaleUnificati = totaleUnificati.add(lModel.getNumUnificati());
			totaleCancellati = totaleCancellati.add(lModel.getNumCancellati());
			totaleAltro = totaleAltro.add(lModel.getNumDefEsito6());
			totalePendentiFine = totalePendentiFine.add(lModel.getNumPendentiFine());

		}

		lRowCounter++;
		lRow = lSheet.createRow(lRowCounter++);

		setCell(lRow, 1, "TOTALI", lCellStyleCenter);
		setCell(lRow, 2, StringUtils.cStrForJS("" + totalePendentiInizio), lCellStyleCenter);
		setCell(lRow, 3, StringUtils.cStrForJS("" + totaleSopravvenuti), lCellStyleCenter);
		setCell(lRow, 4, StringUtils.cStrForJS("" + totaleAccolti), lCellStyleCenter);
		setCell(lRow, 5, StringUtils.cStrForJS("" + totaleRigettati), lCellStyleCenter);
		setCell(lRow, 6, StringUtils.cStrForJS("" + totaleInammissibilita), lCellStyleCenter);
		setCell(lRow, 7, StringUtils.cStrForJS("" + totaleNLPNDP), lCellStyleCenter);
		setCell(lRow, 8, StringUtils.cStrForJS("" + totaleIncompetenza), lCellStyleCenter);
		setCell(lRow, 9, StringUtils.cStrForJS("" + totalePerErrore), lCellStyleCenter);
		setCell(lRow, 10, StringUtils.cStrForJS("" + totaleUnificati), lCellStyleCenter);
		setCell(lRow, 11, StringUtils.cStrForJS("" + totaleCancellati), lCellStyleCenter);
		setCell(lRow, 12, StringUtils.cStrForJS("" + totaleAltro), lCellStyleCenter);
		setCell(lRow, 13, StringUtils.cStrForJS("" + totalePendentiFine), lCellStyleCenter);

	}

	protected void creaStatisticaDettagliOggettiDelMagistrato(HSSFWorkbook aWb,
			UfficioModel aUfficioUtenteConnesso, RicercaProcedimentoModel aRicerca) throws F3BException {

		String[] lCodMagistrati = aRicerca.getCodMagistrati();

		IStatisticheSius lCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
		IMagistrato lCtrlMag = SICOLookupRemote.getMagistratoRemote();

		// Elenco dei motivi oggetti per oggetti selezionati
		ArrayList<IspMotivoOggettoSelezionatiModel> lMotivi = new ArrayList<>();
		lMotivi = lCtrl.ExListaMotivoOggettiSelezionati(aUfficioUtenteConnesso.getCodUfficio());
		// crea un array di stringhe
		String[] lCodMotivi = new String[lMotivi.size()];
		int i = 0;
		for (IspMotivoOggettoSelezionatiModel lMotivo : lMotivi) {
			lCodMotivi[i++] = lMotivo.getCodMotivo();
		}

		// Elenco dei procedimenti per oggetti selezionati e per Magistrato
		Collection<IspEstrazioneOggettiModel> lElencoProc = new ArrayList<>();

		for (String lCodMagistrato : lCodMagistrati) {
			IspEstrazioneOggettiModel lModel = new IspEstrazioneOggettiModel();
			lModel.setFasSiuChiaveUfficio(aRicerca.getCodUfficio());
			lModel.setCodOggettoTenore(Utils.arrayToString(lCodMotivi, "','"));
			lModel.setCodMagistrato(lCodMagistrato);

			MagistratoModel lMagistrato = lCtrlMag.ExRicercaMagistratoByCod(lCodMagistrato);
			lModel.setDescrMagistrato(lMagistrato.getCognome() + " " + lMagistrato.getNome());

			lElencoProc = lCtrl.ExRicercaProcedimentiEstrattiOggettiSelezionatiOrdinati(lModel);

			this.creaFoglioDettaglioOggettiDelMagistrato(aWb, aUfficioUtenteConnesso,
					lModel.getDescrMagistrato(), lElencoProc, aRicerca);
		}
	}

	/**
	 * La funzione prepara la pagina xls di ulteriore dettaglio sulla stastistica dei procedimenti del
	 * Magistrato ordinati per oggetto ed in un periodo di riferimento. Il report preparato contiene l'elenco
	 * dei Procedimenti SIUS di un Magistrato ordinati per oggetto specifico pendenti o sopravvenuti nel
	 * periodo di riferimento. L'elenco dei procedimenti nella lista ? quello passato attraverso il parametro
	 * aElencoProc. Per ogni elemento nella lista viene riportato il suo stato di pendente, sopravvenuto,
	 * definito.
	 *
	 * @param wb
	 * @param aUuffUtenteConnesso
	 * @param aTitolo
	 * @param aElencoProc
	 * @param adataIni
	 */
	private void creaFoglioDettaglioOggettiDelMagistrato(HSSFWorkbook wb, UfficioModel aUuffUtenteConnesso,
			String aDescMagistrato, Collection<IspEstrazioneOggettiModel> aElencoProc,
			RicercaProcedimentoModel aRicerca) {

		String lPatternData = "dd/MM/yyyy";
		String lBuffer = null;
		int lNumCol = 0;
		IspEstrazioneOggettiModel lProcEstrModel = null;

		// Ticket#20230419018 - si bonifica il nome del foglio excel che potrebbe contenere le accentate
		// del nome del magistrato
		// HSSFSheet lSheet = wb.createSheet("Elenco Oggetti " + aDescMagistrato);
		HSSFSheet lSheet = wb
				.createSheet(StringUtils.encodeExcelSheetName("Elenco Oggetti " + aDescMagistrato));
		// Ticket#20230419018 - FINE
		HSSFCellStyle csNull = wb.createCellStyle();

		int nRow = 0;

		// Intestazione del foglio excel
		nRow = setIntestazione(lSheet, aUuffUtenteConnesso, csNull);

		nRow++;
		nRow++;
		HSSFRow lRow = lSheet.createRow(nRow);
		setCell(lRow, (short) 0, "Elenco procedimenti relativi a tutti gli oggetti", csNull);
		nRow++;

		// Inserimento dei parametri di ricerca.
		lRow = lSheet.createRow(nRow);
		lBuffer = "Statistica del " + DateUtils.getDateToString(DateUtils.getSysDate(), lPatternData)
				+ " relative al periodo ";
		if (aRicerca != null) {
			if (aRicerca.getDataIscrizioneInizio() != null) {
				lBuffer += " dal "
						+ DateUtils.getDateToString(aRicerca.getDataIscrizioneInizio(), lPatternData);
			}
			if (aRicerca.getDataIscrizioneFine() != null) {
				lBuffer += " al " + DateUtils.getDateToString(aRicerca.getDataIscrizioneFine(), lPatternData);
			}
		}
		lBuffer += " ed agli atti riferiti al Magistrato : " + aDescMagistrato;
		setCell(lRow, (short) 0, lBuffer, csNull);
		nRow += 2;

		// settaggio della larghezza
		// delle colonne
		lNumCol = 0;
		lSheet.setColumnWidth(lNumCol++, (30 * 256));
		lSheet.setColumnWidth(lNumCol++, (10 * 256));
		lSheet.setColumnWidth(lNumCol++, (10 * 256));
		lSheet.setColumnWidth(lNumCol++, (10 * 256));
		lSheet.setColumnWidth(lNumCol++, (10 * 256));

		// stile per celle col bordo con testo centrato
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
		csCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
		csCenter.setWrapText(true);

		lRow = lSheet.createRow(nRow++);

		// Intestazione colonne
		lNumCol = 0;
		setCell(lRow, lNumCol++, "Oggetto", csCenter);
		setCell(lRow, lNumCol++, "Procedimento", csCenter);
		setCell(lRow, lNumCol++, "Data di Iscrizione", csCenter);
		setCell(lRow, lNumCol++, "Data di Definizione", csCenter);
		setCell(lRow, lNumCol++, "Data di Deposito", csCenter);
		setCell(lRow, lNumCol++, "Provvedimento", csCenter);
		setCell(lRow, lNumCol++, "Pendente Inizio Periodo", csCenter);
		setCell(lRow, lNumCol++, "Sopravvenuto", csCenter);
		setCell(lRow, lNumCol++, "Definito", csCenter);
		setCell(lRow, lNumCol++, "Pendente Fine Periodo", csCenter);

		Iterator<IspEstrazioneOggettiModel> itx = aElencoProc.iterator();
		// inizio ciclo di scrittura dei dati
		while (itx.hasNext()) {
			lNumCol = 0;

			lProcEstrModel = itx.next();
			String lDataIscrizione = "-";
			String lDataDeposito = "-";
			String lDataDefinizione = "-";
			String lDescrOggetto = "-";

			if (lProcEstrModel.getDescrOggettoTenore() != null)
				lDescrOggetto = lProcEstrModel.getDescrOggettoTenore();
			if (lProcEstrModel.getFasSiuDataIscrizione() != null)
				lDataIscrizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataIscrizione(),
						"dd/MM/yyyy");
			if (lProcEstrModel.getDataDeposito() != null)
				lDataDeposito = DateUtils.getDateToString(lProcEstrModel.getDataDeposito(), "dd/MM/yyyy");
			if (lProcEstrModel.getFasSiuDataDefinizione() != null)
				lDataDefinizione = DateUtils.getDateToString(lProcEstrModel.getFasSiuDataDefinizione(),
						"dd/MM/yyyy");

			// String lOggettoEstratto = lProcEstrModel.getDescrOggettoTenore();

			lRow = lSheet.createRow(nRow++);
			setCell(lRow, lNumCol++, lDescrOggetto, csCenter);
			setCell(lRow, lNumCol++, lProcEstrModel.getFasSiuChiaveAnno().toString() + "/"
					+ lProcEstrModel.getFasSiuChiaveProgr().toString(), csCenter);
			setCell(lRow, lNumCol++, lDataIscrizione, csCenter);
			setCell(lRow, lNumCol++, lDataDefinizione, csCenter);
			setCell(lRow, lNumCol++, lDataDeposito, csCenter);

			// Provvedimento: Decreto / Ordinanza
			if (lProcEstrModel.getDepDecIdDepositoDecreto() != null)
				setCell(lRow, lNumCol++, "Decreto", csCenter);
			else if (lProcEstrModel.getDepOpidDepositoOrdinanzaPc() != null)
				setCell(lRow, lNumCol++, "Ordinanza", csCenter);
			else
				setCell(lRow, lNumCol++, "-", csCenter);

			// Determinazione se Pendente inizio periodo o Sopravvenuto
			if (lProcEstrModel.getTenDataIns() == null) {
				setCell(lRow, lNumCol++, "-", csCenter);
				setCell(lRow, lNumCol++, "-", csCenter);
			} else if (lProcEstrModel.getTenDataIns().before(aRicerca.getDataIscrizioneInizio())) {
				setCell(lRow, lNumCol++, "si", csCenter);
				setCell(lRow, lNumCol++, "no", csCenter);
			} else {
				setCell(lRow, lNumCol++, "no", csCenter);
				setCell(lRow, lNumCol++, "si", csCenter);
			}

			// Determinazione se Definito o "Pendente fine periodo"
			if (lProcEstrModel.getDefinito() == null) {
				setCell(lRow, lNumCol++, "-", csCenter);
				setCell(lRow, lNumCol++, "-", csCenter);
			} else if (lProcEstrModel.getDefinito().equalsIgnoreCase("S")) {
				setCell(lRow, lNumCol++, "si" + " : " + lProcEstrModel.getDescrEsitoStatistica(), csCenter);
				setCell(lRow, lNumCol++, "no", csCenter);
			} else {
				setCell(lRow, lNumCol++, "no", csCenter);
				setCell(lRow, lNumCol++, "si", csCenter);
			}
		}
	}

}