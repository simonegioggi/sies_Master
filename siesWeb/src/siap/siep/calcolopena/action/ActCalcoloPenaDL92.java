package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.report.ReportGenerator;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.XModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.model.CalcoloPenaDL92Model;
import siap.siep.calcolopena.model.SemestreDL92Model;
import siap.siep.calcolopenadl92.controller.ICalcoloPenaDL92;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActCalcoloPenaDL92 - Classe Action per la Calcolatrice
 *
 * @since MEV_2024-092
 */
public class ActCalcoloPenaDL92 extends ActionSiap implements ICostantiCalcoloPena {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		CalendarUtil lCalUtil = new CalendarUtil();

		CalcoloPenaDL92Model lCalcoloModel = new CalcoloPenaDL92Model();

		// Quantum Reclusione
		CalendarModel lQuantumReclusione = new CalendarModel();

		lQuantumReclusione.setNumAnni(
				getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE));
		lQuantumReclusione.setNumMesi(
				getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE));
		lQuantumReclusione.setNumGiorni(
				getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE));

		// Normalizzo i dati
		lQuantumReclusione = lCalUtil.ricalcolaGAM(lQuantumReclusione);

		lCalcoloModel.setNumAnniReclusione(new BigDecimal(lQuantumReclusione.getNumAnni()));
		lCalcoloModel.setNumMesiReclusione(new BigDecimal(lQuantumReclusione.getNumMesi()));
		lCalcoloModel.setNumGiorniReclusione(new BigDecimal(lQuantumReclusione.getNumGiorni()));

		// Multa
		if (!getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA).equals("")
				|| !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)
						.equals("")) {
			lCalcoloModel.setImportoMulta(new BigDecimal(getRequestStringParameter(
					ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA) + "."
					+ getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)));
		}

		// Quantum Arresto
		CalendarModel lQuantumArresto = new CalendarModel();

		lQuantumArresto
				.setNumAnni(getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO));
		lQuantumArresto
				.setNumMesi(getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO));
		lQuantumArresto.setNumGiorni(
				getRequestBigDecimalParameter(ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO));

		// Normalizzo i dati
		lQuantumArresto = lCalUtil.ricalcolaGAM(lQuantumArresto);

		lCalcoloModel.setNumAnniArresto(new BigDecimal(lQuantumArresto.getNumAnni()));
		lCalcoloModel.setNumMesiArresto(new BigDecimal(lQuantumArresto.getNumMesi()));
		lCalcoloModel.setNumGiorniArresto(new BigDecimal(lQuantumArresto.getNumGiorni()));

		// Ammenda
		if (!getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA).equals("")
				|| !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)
						.equals("")) {
			lCalcoloModel.setImportoAmmenda(new BigDecimal(getRequestStringParameter(
					ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA) + "."
					+ getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)));
		}

		// Presofferto
		CalendarModel lQuantumPresofferto = new CalendarModel();

		lQuantumPresofferto
				.setNumAnni(getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO));
		lQuantumPresofferto
				.setNumMesi(getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO));
		lQuantumPresofferto.setNumGiorni(
				getRequestBigDecimalParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO));

		// Normalizzo i dati
		lQuantumPresofferto = lCalUtil.ricalcolaGAM(lQuantumPresofferto);

		lCalcoloModel.setNumAnniPresofferto(new BigDecimal(lQuantumPresofferto.getNumAnni()));
		lCalcoloModel.setNumMesiPresofferto(new BigDecimal(lQuantumPresofferto.getNumMesi()));
		lCalcoloModel.setNumGiorniPresofferto(new BigDecimal(lQuantumPresofferto.getNumGiorni()));

		// Posizione Giuridica
		lCalcoloModel.setPosizioneGiuridica(
				getRequestStringParameter(ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA));

		// Data Inizio Pena
		if (lCalcoloModel.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {
			lCalcoloModel.setDataInizioPena(
					getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA,
							ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA));
		}

		// new se provengo dalla form di calcolo recupero i check per l'esclusione dei periodi
		Hashtable<String, String> listaIsCompresa = new Hashtable<>();
		if (!isRequestParameterNullEmptyObj("numSemestriElaborati")) {
			int numSemestriElaborati = getRequestIntParameter("numSemestriElaborati");
			for (int i = 1; i <= numSemestriElaborati; i++) {
				String idSemestre = "prgSemestre_" + i;
				String isCompreso = getRequestStringParameter(idSemestre);
				listaIsCompresa.put(idSemestre, isCompreso);
			}
		}
		lCalcoloModel.setListaIsCompresa(listaIsCompresa);
		
		siesLogger.debug("Model prima del calcolo");
		lCalcoloModel.stampaCalcolo();
		lCalcoloModel.calcolaPenaVirtuale();
		lCalcoloModel.stampaCalcolo();
		setRequestAttribute("EsitoCalcolo", lCalcoloModel);

		String tipoOutput = getRequestStringParameter("tipoOutput");
		if ("stampaTemplate".equals(tipoOutput)) {
			siesLogger.debug("richiesta stampa rtf");

			ByteArrayOutputStream lReport = this.stampaDocumento(lCalcoloModel);
			setRequestAttribute("report", lReport);

			return IWebConstants.PG_DOWNLOAD;
		} else if ("stampaExcel".equals(tipoOutput)) {
			siesLogger.debug("richiesta stampa excel");

			// HSSFWorkbook wb = new HSSFWorkbook();

			// Generazione file xls
			ByteArrayOutputStream lReport = this.stampaExcel(lCalcoloModel);

			setRequestAttribute("report", lReport);
			setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

			return IWebConstants.PG_DOWNLOAD_DOCUMENT;
		} else if ("storicizza".equals(tipoOutput)) {
            // MEV-2026_1
	        siesLogger.debug("richiesta storicizzazione");
	        if (!isSessionAttributeNullObj("fascicolo")) {
	            FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	           
	            CalcoloPenaDL92ModelDB lCalcPenaModelDB = new CalcoloPenaDL92ModelDB (lCalcoloModel);
	            
	            lCalcPenaModelDB.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
	            lCalcPenaModelDB.setCodOperatoreInserimento(getCodUtenteConnesso());
	            lCalcPenaModelDB.setDataInserimento(DateUtils.getSysDate());
	            lCalcPenaModelDB.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
	            
	            ICalcoloPenaDL92 lCalcDL92Ctrl = SIEPLookupRemote.getCalcoloPenaDL92();
	            // CalcoloPenaDL92ModelDB lRetModel = 
	            lCalcDL92Ctrl.ExInserisciCalcoloPenaDL92(lCalcPenaModelDB, lCalcoloModel.getSemestrePresofferto(), lCalcoloModel.getListaSemetri());
	            
	            setRequestAttribute("msgStoricizzazione", "Calcolo pena storicizzato correttamente");
	            setRequestAttribute("fromStoricizza", "S");
	            
//              String lPageDett = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
//              + "=siap.siep.calcolopena.action.ActDettStoricoCalcoloPenaDL92&"
//              +CAMPO_ID_CALCOLO_PENA_DL92+"="+ lRetModel.getIdCalcoloPenaDL92();
//      return lPageDett;	            
	        }    

	        return PG_CALCOLOPENA_DL92;
        } else {
			siesLogger.debug("richiesto il dettaglio");
			return PG_CALCOLOPENA_DL92;
		}
	}

	/**
	 * stampaDocumento
	 *
	 * @return ByteArrayOutputStream
	 * @throws F3BException
	 */
	private ByteArrayOutputStream stampaDocumento(CalcoloPenaDL92Model lCalcoloDL92Model)
			throws F3BException {

		FascicoloSiepModel lFascicoloModel = null;
		TreeModel lTreeRoot = null;

		if (!isSessionAttributeNullObj("fascicolo")) {
			lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			UtenteModel lUtente = getUtenteConnesso();

			IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
			lTreeRoot = lCtrStam.prelevaDatiStampaFascicolo(lFascicoloModel, lUtente);

			TreeModel lTreeFascicolo = lTreeRoot.findTreeModel(lTreeRoot, lFascicoloModel);

			IPenaResidua lCtrlPenaResidua = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel lPenRes = lCtrlPenaResidua
					.ExRicercaPenaResiduaUltimaValidata(lFascicoloModel.getIdFascicoloSiep());
			if (lPenRes != null && lPenRes.getIdPenaResidua() != null)
				lTreeFascicolo.add(new TreeModel(lPenRes));
		} else {
			XModel lStampa = new XModel();

			UfficioModel lUffMod = null;
			UtenteModel lUtente = getUtenteConnesso();
			String descrTipoUff = null;
			if (lUtente.getUfficioUtente() != null) {
				lUffMod = lUtente.getUfficioUtente();

				descrTipoUff = lUffMod.getDescrTipoUfficio();

				lStampa.setUfficio(lUffMod.getDescrComune());
				lStampa.setTipoUfficio(lUffMod.getDescrTipoUfficio());
				lStampa.setDataElaborazione(DateUtils.getSysDate());
				lStampa.setCap(lUffMod.getCap());
				lStampa.setFax(lUffMod.getFax());
				lStampa.setIndirizzo(lUffMod.getIndirizzo());
				lStampa.setTelefono(lUffMod.getTelefono());
				lStampa.setEMail(lUffMod.getEMail());
			}

			if (descrTipoUff != null) {
				if (descrTipoUff.indexOf("PRESSO") > 1) {
					lStampa.setTipoUfficioT1(descrTipoUff.substring(0, descrTipoUff.indexOf("PRESSO")));
					lStampa.setTipoUfficioT2(descrTipoUff.substring(descrTipoUff.indexOf("PRESSO")));
				} else
					lStampa.setTipoUfficioT1(descrTipoUff);
			}

			if (descrTipoUff.indexOf("GENERALE") > 0) {
				lStampa.setFirmatario("Il Procuratore Generale");
			} else {
				lStampa.setFirmatario("Il Pubblico Ministero");
			}

			lTreeRoot = new TreeModel(lStampa);
			TreeModel lTreeFasMod = new TreeModel(new FascicoloSiepModel());
			TreeModel lTreeSoggetto = new TreeModel(new SoggettoModel());
			TreeModel lTreeSentenza = new TreeModel(new SentenzaModel());
			lTreeRoot.add(lTreeFasMod);
			lTreeRoot.add(lTreeSoggetto);
			lTreeRoot.add(lTreeSentenza);
		}

		String lNomeTemplate = "";

		if ("L".equals(lCalcoloDL92Model.getPosizioneGiuridica()))
			lNomeTemplate = "/var/SIES/template/siep/altri/SIEP_DL92-24_LIB.rtf";
		else
			lNomeTemplate = "/var/SIES/template/siep/altri/SIEP_DL92-24_DET.rtf";

		siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);

		TreeModel lTreeCalcoloDL92 = new TreeModel(lCalcoloDL92Model);

		lTreeCalcoloDL92.add(new TreeModel(lCalcoloDL92Model.getSemestrePresofferto()));

		Vector<SemestreDL92Model> lSementri = lCalcoloDL92Model.getListaSemetri();
		for (int i = 0; i < lSementri.size(); i++) {
			SemestreDL92Model lSemestre = lSementri.elementAt(i);
			lTreeCalcoloDL92.add(new TreeModel(lSemestre));
		}

		lTreeRoot.add(lTreeCalcoloDL92);
		ReportGenerator lReport = new ReportGenerator();
		ByteArrayOutputStream lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot,
				lNomeTemplate);

		return lByteArrayOut;
	}

	/**
	 * stampaExcel
	 *
	 * @return ByteArrayOutputStream
	 */
	private ByteArrayOutputStream stampaExcel(CalcoloPenaDL92Model lCalcoloDL92Model) throws F3BException {

		// foglio excel
		HSSFWorkbook wb = new HSSFWorkbook();

		this.creaFoglioRiepilogo(wb, lCalcoloDL92Model);
		this.creaFoglioLibero(wb, lCalcoloDL92Model);
		this.creaFoglioDetenuto(wb, lCalcoloDL92Model);

		// ==================================================================
		//
		// ==================================================================
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("StampaExcel.processRequest: " + ioe);
		}

		return fileOut;
	}

	/**
	 * creaFoglioRiepilogo
	 *
	 * @param wb
	 * @param lCalcoloDL92Model
	 */
	private void creaFoglioRiepilogo(HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {

		// foglio dettaglio
		HSSFSheet sheetRiepilogo = wb.createSheet("Riepilogo");

		// Font
		HSSFFont fontBold = wb.createFont();
		fontBold.setBold(true);

		// Stili
		// HSSFCellStyle csNull = wb.createCellStyle();

		// stile per celle col bordo
		// HSSFCellStyle cs = getBordo4Lati(wb);

		// stile per celle col bordo
		HSSFCellStyle csBold = getBordo4Lati(wb);
		csBold.setFont(fontBold);

		// Titolo 1 -
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);
		csTitolo1.setAlignment(HorizontalAlignment.CENTER);
		csTitolo1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
		csTitolo1.setFont(fontBold);

		// Stile delle celle grigie allineata a destra
		HSSFCellStyle csGrigioDestra = getBordo4Lati(wb);
		csGrigioDestra.setAlignment(HorizontalAlignment.RIGHT);
		// csGrigioDestra.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csGrigioDestra.setWrapText(true); // testo a capo
		// csGrigioDestra.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());

		// Stile delle celle gialle con i dati allineate al centro (dati di input)
		HSSFCellStyle csDatiInput = getBordo4Lati(wb);
		csDatiInput.setAlignment(HorizontalAlignment.CENTER);
		csDatiInput.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csDatiInput.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
		csDatiInput.setFont(fontBold);

		// Stile delle celle grigie con i dati allineate al centro (dati calcolati)
		HSSFCellStyle csDatiCalcolati = getBordo4Lati(wb);
		csDatiCalcolati.setAlignment(HorizontalAlignment.CENTER);
		csDatiCalcolati.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csDatiCalcolati.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
		csDatiCalcolati.setFont(fontBold);

		// Stile delle celle Verdi (dati calcolati)
		HSSFCellStyle csVerdeCentro = getBordo4Lati(wb);
		csVerdeCentro.setAlignment(HorizontalAlignment.CENTER);
		csVerdeCentro.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csVerdeCentro.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());

		// Impostare opportunamente la larghezza della prima riga
		sheetRiepilogo.setColumnWidth(0, (105 * 256)); // Larghezza prima colonna
		sheetRiepilogo.setColumnWidth(1, (30 * 256)); // Larghezza seconda colonna

		int nRow = 0;

		// ==================================================================
		// Sezione con i dati del presofferto (dati input)
		// ==================================================================
		// nRow++;
		HSSFRow row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Custodia cautelare (presofferto)", csTitolo1);
		setCell(row, 1, "", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); // su 2 colonne

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Anni di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumAnniPresofferto(), " "), csDatiInput);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Mesi di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumMesiPresofferto(), " "), csDatiInput);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumGiorniPresofferto(), " "),
				csDatiInput);

		nRow++; // riga vuota

		// ==================================================================
		// Sezione con i dati del presofferto calcolati
		// ==================================================================
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto();
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di custodia cautelare per erogazione L.A.:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(), ""),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturati in custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(), ""), csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0,
				"Giorni di custodia cautelare eccedenti i semestri utili per erogazione L.A. (verranno conteggiati per anticipare il primo semestre utile nell'esecuzione pena):",
				csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getGiorniResiduiPresofferto(), ""),
				csDatiCalcolati);

		nRow++; // riga vuota

		// ==================================================================
		// Sezione Pena da espiare al netto del Presofferto (dati input)
		// ==================================================================
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Pena da espiare al netto della custodia cautelare (presofferto)", csTitolo1);
		setCell(row, 1, "", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); // su 2 colonne

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Anni pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumAnni(), ""),
				csDatiInput);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Mesi pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumMesi(), ""),
				csDatiInput);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumGiorni(), ""),
				csDatiInput);

		nRow++; // riga vuota

		// ==================================================================
		// Sezione con LA e semestri calcolati
		// ==================================================================
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di pena ipotetica per erogazione L.A.:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getSemestriUtili(), ""), csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturabili e usufruibili in pena ipotetica:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumGiorniLAMaturataInPenaresidua(), ""),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturabili e non usufruibili in pena ipotetica:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), ""),
				csDatiCalcolati);

		nRow++; // riga vuota

		// ==================================================================
		// DATI DA INDICARE NEL PROVVEDIMENTO DI ESECUZIONE
		// ==================================================================
		// @TODO da formattare in verde
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Dati da indicare nei provvedimenti di esecuzione", csTitolo1);
		setCell(row, 1, "", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); // su 2 colonne

		nRow++;
		String strPena = "";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumAnni(), "") + " Anni ";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumMesi(), "") + " Mesi ";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumGiorni(), "")
				+ " Giorni ";

		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Pena ipotetica ad esito delle detrazioni dei soli giorni di L.A. usufruibili:",
				csGrigioDestra);
		setCell(row, 1, strPena, csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di pena scontata:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getSemestriUtiliPenaScontata(), ""),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di liberazione anticipata concedibili:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(), ""),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di liberazione anticipata usufruibili:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAApplicate().intValue(), ""),
				csDatiCalcolati);

		nRow++;
		// ==================================================================
		// CALCOLI CON DATA ESECUZIONE
		// ==================================================================
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "CALCOLI CON DATA ESECUZIONE", csTitolo1);
		setCell(row, 1, "", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); // su 2 colonne

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Data decorrenza pena:", csGrigioDestra);
		setCell(row, 1,
				StringUtils.toStringJSP(
						DateUtils.getDateToString(lCalcoloDL92Model.getDataInizioPena(), "dd/MM/yyyy")),
				csDatiInput);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Data scarcerazione senza calcolare la liberazione anticipata:", csGrigioDestra);
		setCell(row, 1,
				StringUtils.toStringJSP(DateUtils
						.getDateToString(lCalcoloDL92Model.getDataScarcerazioneNoLA(), "dd/MM/yyyy")),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0,
				"Data scarcerazione con giorni Liberazione Anticipata applicata per intero (data fine pena calcolata con giorni non usufruibili):",
				csGrigioDestra);
		setCell(row, 1,
				StringUtils.toStringJSP(DateUtils
						.getDateToString(lCalcoloDL92Model.getDataScarcerazioneLAFung(), "dd/MM/yyyy")),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0,
				"Data scarcerazione con giorni Liberazione Anticipata concessi (data fine pena calcolata con giorni di fungibilita'):",
				csGrigioDestra);
		setCell(row, 1,
				StringUtils.toStringJSP(DateUtils
						.getDateToString(lCalcoloDL92Model.getDataScarcerazioneLANoFung(), "dd/MM/yyyy")),
				csDatiCalcolati);

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Data scarcerazione senza applicare l'ultimo semestre di Liberazione:",
				csGrigioDestra);
		if (lCalcoloDL92Model.getLAFungibili().intValue() > 0)
			setCell(row, 1,
					StringUtils.toStringJSP(DateUtils.getDateToString(
							lCalcoloDL92Model.getDataScarcerazionePenultimoSemestre(), "dd/MM/yyyy")),
					csDatiCalcolati);
		else
			setCell(row, 1, "non applicabile", csDatiCalcolati);
	}

	private void creaFoglioLibero(HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {

		// foglio Libero
		HSSFSheet sheetLibero = wb.createSheet("LIBERO");

		// Font
		HSSFFont fontBold = wb.createFont();
		fontBold.setBold(true);

		HSSFFont fontBoldRed = wb.createFont();
		fontBoldRed.setBold(true);
		fontBoldRed.setColor(HSSFFont.COLOR_RED);

		// Colore celle RGB
		// XSSFColor myGreen = new XSSFColor (new java.awt.Color(146,208,80));
		// XSSFColor myOrange = new XSSFColor (new java.awt.Color(255,192,0));

		// stile per celle col bordo (allineamento a sinistra)
		// HSSFCellStyle cs = getBordo4Lati(wb);

		HSSFCellStyle csBoldCenterRed = getBordo4Lati(wb);
		csBoldCenterRed.setFont(fontBoldRed);
		csBoldCenterRed.setAlignment(HorizontalAlignment.CENTER);

		// stile per celle col bordo centrata
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HorizontalAlignment.CENTER);

		// stile per celle col bordo allineamento da desta
		HSSFCellStyle csRight = getBordo4Lati(wb);
		csRight.setAlignment(HorizontalAlignment.RIGHT);

		// Stile intestazione: bold centrato e sfondo grigio
		HSSFCellStyle csIntestazione = getBordo4Lati(wb);
		csIntestazione.setFont(fontBold);
		csIntestazione.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csIntestazione.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
		// csIntestazione.setFillForegroundColor(myGreen.getIndexed());
		csIntestazione.setAlignment(HorizontalAlignment.CENTER);
		csIntestazione.setWrapText(true); // testo a capo
		csIntestazione.setVerticalAlignment(VerticalAlignment.BOTTOM);

		HSSFCellStyle csRiepilogo = getBordo4Lati(wb);
		csRiepilogo.setFont(fontBold);
		csRiepilogo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csRiepilogo.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
		// csRiepilogo.setFillForegroundColor(myOrange.getIndexed());
		csRiepilogo.setAlignment(HorizontalAlignment.CENTER);

		// Impostare opportunamente la larghezza della prima riga
		sheetLibero.setColumnWidth(0, (5 * 256)); // Progressivo
		sheetLibero.setColumnWidth(1, (30 * 256)); // Desc Semestre
		sheetLibero.setColumnWidth(2, (13 * 256)); // Larghezza colonna COMPRESO/ESCLUSO
		sheetLibero.setColumnWidth(3, (20 * 256)); // LA APPLICATA
		sheetLibero.setColumnWidth(4, (17 * 256)); // ANNI RESIDUI
		sheetLibero.setColumnWidth(5, (17 * 256)); // MESI RESIDUI
		sheetLibero.setColumnWidth(6, (17 * 256)); // GIORNI RESIDUI
		sheetLibero.setColumnWidth(7, (65 * 256)); // Colonna riepilogo

		int nRow = 0;

		// ==================================================================
		// Riga Interstazioni
		// ==================================================================
		// nRow++;
		HSSFRow row = sheetLibero.createRow(nRow);
		setCell(row, 0, " ", csIntestazione);
		setCell(row, 1, " ", csIntestazione);
		setCell(row, 2, " ", csIntestazione);
		setCell(row, 3, "L.A. APPLICATA", csIntestazione);
		setCell(row, 4, "ANNI RESIDUI", csIntestazione);
		setCell(row, 5, "MESI RESIDUI", csIntestazione);
		setCell(row, 6, "GIORNI RESIDUI", csIntestazione);

		// ==================================================================
		// Prima Riga Presofferto
		// ==================================================================
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto();
		// String lStringResiduo = lCalcoloPresofferto.getResiduoNumAnni() + " anni "
		// + lCalcoloPresofferto.getResiduoNumMesi() + " mesi "
		// + lCalcoloPresofferto.getResiduoNumGiorni() + " giorni";
		nRow++;
		row = sheetLibero.createRow(nRow);
		setCell(row, 0, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(), "0"), csCenter);
		setCell(row, 1, "SEMESTRI ESPIATI IN C.C. ", csCenter);
		setCell(row, 2, "COMPRESO", csCenter);
		setCell(row, 3, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(), "0"), csCenter);
		setCell(row, 4, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumAnni().intValue(), "0"),
				csCenter);
		setCell(row, 5, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumMesi().intValue(), "0"),
				csCenter);
		setCell(row, 6, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumGiorni().intValue(), "0"),
				csCenter);

		// ==================================================================
		// Ciclo sui semestri
		// ==================================================================
		Vector<SemestreDL92Model> mListaSemetri = lCalcoloDL92Model.getListaSemetri();
		for (int i = 0; i < mListaSemetri.size(); i++) {
			SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
			// String lPenaStr = lSemestreUtile.getResiduoNumAnni() + " anni - "
			// + lSemestreUtile.getResiduoNumMesi() + " mesi - " + lSemestreUtile.getResiduoNumGiorni()
			// + " giorni ";

			nRow++;
			row = sheetLibero.createRow(nRow);
			setCell(row, 0, StringUtils.toStringJSP(lSemestreUtile.getProgressivo()) + "�", csCenter);
			setCell(row, 1, "semestre utile per L.A. ", csCenter);

			if ("S".equals(lSemestreUtile.getIsCompreso()))
				setCell(row, 2, "COMPRESO", csCenter);
			else
				setCell(row, 2, "ESCLUSO", csBoldCenterRed);

			if (lSemestreUtile.getLAApplicate().intValue() < 45) {
				setCell(row, 3, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"),
						csBoldCenterRed);
				setCell(row, 4, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumAnni().intValue(), "0"),
						csBoldCenterRed);
				setCell(row, 5, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumMesi().intValue(), "0"),
						csBoldCenterRed);
				setCell(row, 6, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumGiorni().intValue(), "0"),
						csBoldCenterRed);
			} else {
				setCell(row, 3, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"), csCenter);
				setCell(row, 4, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumAnni().intValue(), "0"),
						csCenter);
				setCell(row, 5, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumMesi().intValue(), "0"),
						csCenter);
				setCell(row, 6, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumGiorni().intValue(), "0"),
						csCenter);
			}
		}

		// Mi servono min 4 righe per la colonna del riepilogo, se non create le devo generare
		while (nRow < 4) {
			nRow++;
			row = sheetLibero.createRow(nRow);
			setCell(row, 0, "", csCenter);
			setCell(row, 1, "", csCenter);
			setCell(row, 2, "", csCenter);
			setCell(row, 3, "", csCenter);
			setCell(row, 4, "", csCenter);
			setCell(row, 5, "", csCenter);
			setCell(row, 6, "", csCenter);
		}

		// Riepilogo
		setCell(sheetLibero.getRow(1), 7, "L.A. MATURATA MA NON APPLICATA / PENA ESPIATA IN ECCESSO",
				csRiepilogo);
		if (lCalcoloDL92Model.getLAFungibili().intValue() > 0)
			setCell(sheetLibero.getRow(2), 7,
					StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), "0"),
					csBoldCenterRed);
		else
			setCell(sheetLibero.getRow(2), 7,
					StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), "0"), csCenter);
		setCell(sheetLibero.getRow(3), 7, "GIORNI L.A. CONCESSI", csRiepilogo);
		setCell(sheetLibero.getRow(4), 7,
				StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(), "0"), csCenter);
	}

	private void creaFoglioDetenuto(HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {

		// foglio dettaglio
		HSSFSheet sheetDetenuto = wb.createSheet("DETENUTO");

		// Font
		HSSFFont fontBold = wb.createFont();
		fontBold.setBold(true);

		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);

		HSSFFont fontBoldRed = wb.createFont();
		fontBoldRed.setBold(true);
		fontBoldRed.setColor(HSSFFont.COLOR_RED);

		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(fontBold);
		csBoldCenter.setAlignment(HorizontalAlignment.CENTER);

		HSSFCellStyle csBoldCenterRed = getBordo4Lati(wb);
		csBoldCenterRed.setFont(fontBoldRed);
		csBoldCenterRed.setAlignment(HorizontalAlignment.CENTER);

		// stile per celle col bordo centrata
		HSSFCellStyle csCenter = getBordo4Lati(wb);
		csCenter.setAlignment(HorizontalAlignment.CENTER);

		// stile per celle col bordo allineamento da desta
		HSSFCellStyle csRight = getBordo4Lati(wb);
		csRight.setAlignment(HorizontalAlignment.RIGHT);

		// Stile intestazione: bold centrato e sfondo grigio
		HSSFCellStyle csIntestazione = getBordo4Lati(wb);
		csIntestazione.setFont(fontBold);
		csIntestazione.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csIntestazione.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
		csIntestazione.setAlignment(HorizontalAlignment.CENTER);
		csIntestazione.setWrapText(true); // testo a capo
		csIntestazione.setVerticalAlignment(VerticalAlignment.BOTTOM);

		HSSFCellStyle csRiepilogo = getBordo4Lati(wb);
		csRiepilogo.setFont(fontBold);
		csRiepilogo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		csRiepilogo.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
		csRiepilogo.setAlignment(HorizontalAlignment.CENTER);

		// Impostare opportunamente la larghezza delle righe
		sheetDetenuto.setColumnWidth(0, (6 * 256)); // Larghezza prima colonna
		sheetDetenuto.setColumnWidth(1, (28 * 256)); // Larghezza seconda colonna
		sheetDetenuto.setColumnWidth(2, (13 * 256)); // Larghezza colonna COMPRESO/ESCLUSO
		sheetDetenuto.setColumnWidth(3, (17 * 256)); // LA APPLICATA
		sheetDetenuto.setColumnWidth(4, (17 * 256)); // Data In cui maturo LA
		sheetDetenuto.setColumnWidth(5, (32 * 256)); // Nuova Data Scadenza
		sheetDetenuto.setColumnWidth(6, (17 * 256)); // ANNI RESIDUI
		sheetDetenuto.setColumnWidth(7, (17 * 256)); // MESI RESIDUI
		sheetDetenuto.setColumnWidth(8, (17 * 256)); // GIORNI RESIDUI
		sheetDetenuto.setColumnWidth(9, (22 * 256)); // SURPLUS
		sheetDetenuto.setColumnWidth(10, (23 * 256)); // RIEPILOGO

		int nRow = 0;

		// ==================================================================
		// Prima Riga INTESTAZIONE
		// ==================================================================
		// nRow++;
		HSSFRow row = sheetDetenuto.createRow(nRow);
		// row.setHeight (altezzaIntestazione);
		setCell(row, 0, "", csIntestazione);
		setCell(row, 1, "", csIntestazione);
		setCell(row, 2, "", csIntestazione);
		setCell(row, 3, "L.A. APPLICATA", csIntestazione);
		setCell(row, 4, "DATA IN CUI MATURO L.A.", csIntestazione);
		setCell(row, 5, "NUOVA DATA SCADENZA APPLCANDO LE CONCESSIONI DI 45GG PER INTERO", csIntestazione);
		setCell(row, 6, "ANNI RESIDUI", csIntestazione);
		setCell(row, 7, "MESI RESIDUI", csIntestazione);
		setCell(row, 8, "GIORNI RESIDUI", csIntestazione);
		setCell(row, 9, "SURPLUS DETENZIONE (GIORNI DI FUNGIBILITA)", csIntestazione);
		setCell(row, 10, "RIEPILOGO", csIntestazione);

		// ==================================================================
		// Prima Riga Presofferto
		// ==================================================================
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto();

		nRow++;
		row = sheetDetenuto.createRow(nRow);
		setCell(row, 0, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(), "0"), csCenter);
		setCell(row, 1, "SEMESTRI ESPIATI IN C.C. ", csCenter);
		setCell(row, 2, "COMPRESO", csCenter);
		setCell(row, 3, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(), "0"), csCenter);
		setCell(row, 4, "", csCenter);
		setCell(row, 5,
				DateUtils.getDateToString(lCalcoloPresofferto.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),
				csCenter);
		setCell(row, 6, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumAnni().intValue(), "0"),
				csCenter);
		setCell(row, 7, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumMesi().intValue(), "0"),
				csCenter);
		setCell(row, 8, StringUtils.toStringJSP(lCalcoloPresofferto.getResiduoNumGiorni().intValue(), "0"),
				csCenter);
		setCell(row, 9, "", csCenter); // SURPLUS solo ultimo semestre. Sicuramente NON presente su
										// presofferto

		// ==================================================================
		// Ciclo sui semestri
		// ==================================================================
		Vector<SemestreDL92Model> mListaSemetri = lCalcoloDL92Model.getListaSemetri();
		for (int i = 0; i < mListaSemetri.size(); i++) {
			SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);

			nRow++;
			row = sheetDetenuto.createRow(nRow);

			HSSFCellStyle lastCellStyle = csCenter;

			if (i == (mListaSemetri.size() - 1) && lCalcoloDL92Model.getLAFungibili().intValue() > 0)
				lastCellStyle = csBoldCenterRed;

			setCell(row, 0, StringUtils.toStringJSP(lSemestreUtile.getProgressivo(), "0"), csCenter);
			setCell(row, 1, "semestre maturato per L.A. ", cs);

			if ("S".equals(lSemestreUtile.getIsCompreso()))
				setCell(row, 2, "COMPRESO", csCenter);
			else
				setCell(row, 2, "ESCLUSO", csBoldCenterRed);
			if (lSemestreUtile.getLAApplicate().intValue() < 45)
				setCell(row, 3, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"),
						csBoldCenterRed);
			else
				setCell(row, 3, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"), csCenter);

			setCell(row, 4, DateUtils.getDateToString(lSemestreUtile.getDataMaturazioneLA(), "dd/MM/yyyy"),
					csCenter);
			setCell(row, 5,
					DateUtils.getDateToString(lSemestreUtile.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),
					lastCellStyle);
			setCell(row, 6, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumAnni().intValue(), "0"),
					lastCellStyle);
			setCell(row, 7, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumMesi().intValue(), "0"),
					lastCellStyle);
			setCell(row, 8, StringUtils.toStringJSP(lSemestreUtile.getResiduoNumGiorni().intValue(), "0"),
					lastCellStyle);

			// SURPLUS solo ultimo semestre
			if (i == (mListaSemetri.size() - 1) && lCalcoloDL92Model.getLAFungibili().intValue() == 0)
				setCell(row, 9, "", csCenter);
			else if (i == (mListaSemetri.size() - 1) && lCalcoloDL92Model.getLAFungibili().intValue() > 0)
				setCell(row, 9, StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), "0"),
						csBoldCenterRed);
			else
				setCell(row, 9, "", csCenter);
		}

		// Mi servono min 7 righe per la colonna del riepilogo, se non create le devo generare
		while (nRow < 6) {
			nRow++;
			row = sheetDetenuto.createRow(nRow);
			setCell(row, 0, "", csCenter);
			setCell(row, 1, "", csCenter);
			setCell(row, 2, "", csCenter);
			setCell(row, 3, "", csCenter);
			setCell(row, 4, "", csCenter);
			setCell(row, 5, "", csCenter);
			setCell(row, 6, "", csCenter);
			setCell(row, 7, "", csCenter);
			setCell(row, 8, "", csCenter);
			setCell(row, 9, "", csCenter);
		}

		// Data Decorrenza
		setCell(sheetDetenuto.getRow(1), 10, "DATA DECORRENZA", csRiepilogo);
		setCell(sheetDetenuto.getRow(2), 10,
				DateUtils.getDateToString(lCalcoloDL92Model.getDataInizioPena(), "dd/MM/yyyy"), csCenter);
		setCell(sheetDetenuto.getRow(3), 10, "DATA SCARCERAZIONE", csRiepilogo);
		setCell(sheetDetenuto.getRow(4), 10,
				DateUtils.getDateToString(lCalcoloDL92Model.getDataScarcerazioneLAFung(), "dd/MM/yyyy"),
				csCenter);
		setCell(sheetDetenuto.getRow(5), 10, "GIORNI L.A. CONCESSI", csRiepilogo);
		setCell(sheetDetenuto.getRow(6), 10,
				StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(), "0"), csCenter);
	}

	/**
	 * setCell
	 *
	 * @param row
	 * @param nCol
	 * @param value
	 * @param cs
	 * @return HSSFCell
	 */
	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {

		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);
		return cell;
	}

	/**
	 * Crea un style con la cella bordata
	 *
	 * @param wb
	 * @return HSSFCellStyle
	 */
	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {

		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(BorderStyle.THIN);
		cs.setBorderTop(BorderStyle.THIN);
		cs.setBorderRight(BorderStyle.THIN);
		cs.setBorderLeft(BorderStyle.THIN);
		return cs;
	}

	// private void creaFoglioLiberoBCK(HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {
	//
	// // foglio Libero
	// HSSFSheet sheetLibero = wb.createSheet("LIBERO");
	//
	// // Font
	// HSSFFont fontBold = wb.createFont();
	// fontBold.setBold(true);
	//
	// HSSFFont fontBoldRed = wb.createFont();
	// fontBoldRed.setBold(true);
	// fontBoldRed.setColor(HSSFFont.COLOR_RED);
	//
	// // Colore celle RGB
	// // XSSFColor myGreen = new XSSFColor (new java.awt.Color(146,208,80));
	// // XSSFColor myOrange = new XSSFColor (new java.awt.Color(255,192,0));
	//
	// // stile per celle col bordo (allineamento a sinistra)
	// HSSFCellStyle cs = getBordo4Lati(wb);
	//
	// HSSFCellStyle csBoldCenterRed = getBordo4Lati(wb);
	// csBoldCenterRed.setFont(fontBoldRed);
	// csBoldCenterRed.setAlignment(HorizontalAlignment.CENTER);
	//
	// // stile per celle col bordo centrata
	// HSSFCellStyle csCenter = getBordo4Lati(wb);
	// csCenter.setAlignment(HorizontalAlignment.CENTER);
	//
	// // stile per celle col bordo allineamento da desta
	// HSSFCellStyle csRight = getBordo4Lati(wb);
	// csRight.setAlignment(HorizontalAlignment.RIGHT);
	//
	// // Stile intestazione: bold centrato e sfondo grigio
	// HSSFCellStyle csIntestazione = getBordo4Lati(wb);
	// csIntestazione.setFont(fontBold);
	// csIntestazione.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	// csIntestazione.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
	// // csIntestazione.setFillForegroundColor(myGreen.getIndexed());
	// csIntestazione.setAlignment(HorizontalAlignment.CENTER);
	// csIntestazione.setWrapText(true); // testo a capo
	// csIntestazione.setVerticalAlignment(VerticalAlignment.BOTTOM);
	//
	// HSSFCellStyle csRiepilogo = getBordo4Lati(wb);
	// csRiepilogo.setFont(fontBold);
	// csRiepilogo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	// csRiepilogo.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
	// // csRiepilogo.setFillForegroundColor(myOrange.getIndexed());
	// csRiepilogo.setAlignment(HorizontalAlignment.CENTER);
	//
	// // Impostare opportunamente la larghezza della prima riga
	// sheetLibero.setColumnWidth(0, (5 * 256)); // Progressivo
	// sheetLibero.setColumnWidth(1, (30 * 256)); // Desc Semestre
	// sheetLibero.setColumnWidth(2, (20 * 256)); // LA APPLICATA
	// sheetLibero.setColumnWidth(3, (25 * 256)); // Residuao Pena
	// sheetLibero.setColumnWidth(4, (65 * 256)); // Colonna riepilogo
	//
	// int nRow = 0;
	//
	// // ==================================================================
	// // Riga Interstazioni
	// // ==================================================================
	// // nRow++;
	// HSSFRow row = sheetLibero.createRow(nRow);
	// setCell(row, 0, " ", csIntestazione);
	// setCell(row, 1, " ", csIntestazione);
	// setCell(row, 2, "L.A. APPLICATA", csIntestazione);
	// setCell(row, 3, "RESIDUO ANNI PENA", csIntestazione);
	//
	// // ==================================================================
	// // Prima Riga Presofferto
	// // ==================================================================
	// SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto();
	// String lStringResiduo = lCalcoloPresofferto.getResiduoNumAnni() + " anni "
	// + lCalcoloPresofferto.getResiduoNumMesi() + " mesi "
	// + lCalcoloPresofferto.getResiduoNumGiorni() + " giorni";
	// nRow++;
	// row = sheetLibero.createRow(nRow);
	// setCell(row, 0, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(), "0"), csCenter);
	// setCell(row, 1, "SEMESTRI ESPIATI IN C.C. ", csCenter);
	// setCell(row, 2, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(), "0"), csCenter);
	// setCell(row, 3, lStringResiduo, csCenter);
	//
	// // ==================================================================
	// // Ciclo sui semestri
	// // ==================================================================
	// Vector<SemestreDL92Model> mListaSemetri = lCalcoloDL92Model.getListaSemetri();
	// for (int i = 0; i < mListaSemetri.size(); i++) {
	// SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
	// String lPenaStr = lSemestreUtile.getResiduoNumAnni() + " anni - "
	// + lSemestreUtile.getResiduoNumMesi() + " mesi - " + lSemestreUtile.getResiduoNumGiorni()
	// + " giorni ";
	//
	// // String lColorLastSem = "";
	// // if (lSemestreUtile.getLAApplicate().intValue() < 45)
	// // lColorLastSem = "style='color=red'";
	//
	// nRow++;
	// row = sheetLibero.createRow(nRow);
	// setCell(row, 0, StringUtils.toStringJSP(lSemestreUtile.getProgressivo()) + "�", csCenter);
	// setCell(row, 1, "semestre utile per L.A. ", csCenter);
	//
	// if (lSemestreUtile.getLAApplicate().intValue() < 45) {
	// setCell(row, 2, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"),
	// csBoldCenterRed);
	// setCell(row, 3, lPenaStr, csBoldCenterRed);
	// } else {
	// setCell(row, 2, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(), "0"), csCenter);
	// setCell(row, 3, lPenaStr, csCenter);
	// }
	// }
	//
	// // Mi servono min 4 righe per la colonna del riepilogo, se non create le devo generare
	// while (nRow < 4) {
	// nRow++;
	// row = sheetLibero.createRow(nRow);
	// setCell(row, 0, "", csCenter);
	// setCell(row, 1, "", csCenter);
	// setCell(row, 2, "", csCenter);
	// setCell(row, 3, "", csCenter);
	// }
	//
	// // Riepilogo
	// setCell(sheetLibero.getRow(1), 4, "L.A. MATURATA MA NON APPLICATA / PENA ESPIATA IN ECCESSO",
	// csRiepilogo);
	// if (lCalcoloDL92Model.getLAFungibili().intValue() > 0)
	// setCell(sheetLibero.getRow(2), 4,
	// StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), "0"),
	// csBoldCenterRed);
	// else
	// setCell(sheetLibero.getRow(2), 4,
	// StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(), "0"), csCenter);
	// setCell(sheetLibero.getRow(3), 4, "GIORNI L.A. CONCESSI", csRiepilogo);
	// setCell(sheetLibero.getRow(4), 4,
	// StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(), "0"), csCenter);
	// }

}