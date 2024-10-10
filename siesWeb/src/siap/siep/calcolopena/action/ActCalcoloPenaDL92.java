package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;
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
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.XModel;
import siap.sico.stampa.controller.IStampa;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.controller.TemplateManager;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.calcolopena.model.CalcoloPenaDL92Model;
import siap.siep.calcolopena.model.SemestreDL92Model;
import siap.siep.fascicolo.controller.IFascicoloSiepStampa;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.util.SIEPLookupRemote;

/**
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
		
		lQuantumReclusione.setNumAnni   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE));
		lQuantumReclusione.setNumMesi   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE));
		lQuantumReclusione.setNumGiorni (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE));
		
		// Normalizzo i dati
		lQuantumReclusione = lCalUtil.ricalcolaGAM (lQuantumReclusione);
		
		lCalcoloModel.setNumAnniReclusione   (new BigDecimal (lQuantumReclusione.getNumAnni()));
		lCalcoloModel.setNumMesiReclusione   (new BigDecimal (lQuantumReclusione.getNumMesi()));
		lCalcoloModel.setNumGiorniReclusione (new BigDecimal (lQuantumReclusione.getNumGiorni()));
	
    // Multa
    if (   !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA).equals("")
        || !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA).equals("")
       ) 
    {
    	  lCalcoloModel.setImportoMulta(new BigDecimal(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA)
                                             + "." + getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA)));
    }
	
		// Quantum Arresto 
		CalendarModel lQuantumArresto = new CalendarModel();
		
		lQuantumArresto.setNumAnni   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO));
		lQuantumArresto.setNumMesi   (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO));
		lQuantumArresto.setNumGiorni (getRequestBigDecimalParameter (ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO));
		
	  // Normalizzo i dati
		lQuantumArresto = lCalUtil.ricalcolaGAM (lQuantumArresto);
		
		lCalcoloModel.setNumAnniArresto   (new BigDecimal (lQuantumArresto.getNumAnni()));
		lCalcoloModel.setNumMesiArresto   (new BigDecimal (lQuantumArresto.getNumMesi()));
		lCalcoloModel.setNumGiorniArresto (new BigDecimal (lQuantumArresto.getNumGiorni()));	

    // Ammenda
    if (   !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA).equals("")
        || !getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA).equals("")
       ) 
    {
    	  lCalcoloModel.setImportoAmmenda(new BigDecimal(getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA)
                                               + "." + getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA)));
    }
		
		// Presofferto
		CalendarModel lQuantumPresofferto = new CalendarModel();
		
		lQuantumPresofferto.setNumAnni   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO));
		lQuantumPresofferto.setNumMesi   (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO));
		lQuantumPresofferto.setNumGiorni (getRequestBigDecimalParameter (ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO));
		
	  // Normalizzo i dati
		lQuantumPresofferto = lCalUtil.ricalcolaGAM (lQuantumPresofferto);
		
		lCalcoloModel.setNumAnniPresofferto   (new BigDecimal (lQuantumPresofferto.getNumAnni()));
		lCalcoloModel.setNumMesiPresofferto   (new BigDecimal (lQuantumPresofferto.getNumMesi()));
		lCalcoloModel.setNumGiorniPresofferto (new BigDecimal (lQuantumPresofferto.getNumGiorni()));

		// Posizione Giuridica
		lCalcoloModel.setPosizioneGiuridica (getRequestStringParameter (ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA));
		
		// Data Inizio Pena
		if (lCalcoloModel.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {
			lCalcoloModel.setDataInizioPena (getRequestDateParameter (ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA
					                                                    , ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA
					                                                    , ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA));
		}
		
		siesLogger.debug("Modelprima del calcolo");
		lCalcoloModel.stampaCalcolo(); 
		
		lCalcoloModel.calcolaPenaVirtuale();
		lCalcoloModel.stampaCalcolo();
		setRequestAttribute("EsitoCalcolo",lCalcoloModel);
		
		String tipoOutput = getRequestStringParameter("tipoOutput");
		if ("stampaTemplate".equals(tipoOutput)) {
			siesLogger.debug("richiesta stampa rtf");
			
			ByteArrayOutputStream lReport = this.stampaDocumento (lCalcoloModel);
	    setRequestAttribute("report", lReport);

	    return IWebConstants.PG_DOWNLOAD;
		}
		else if ("stampaExcel".equals(tipoOutput)) {
			siesLogger.debug("richiesta stampa excel");

			HSSFWorkbook wb = new HSSFWorkbook();

			// Generazione file xls
			ByteArrayOutputStream lReport = this.stampaExcel (lCalcoloModel);
			
			setRequestAttribute("report", lReport);
			setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

			return IWebConstants.PG_DOWNLOAD_DOCUMENT;
		}
		else {
			siesLogger.debug("richiesto il dettaglio");
			return PG_CALCOLOPENA_DL92;	
		}			
	}
	
	
	/**
	 * 
	 * @return
	 * @throws F3BException
	 */
  private ByteArrayOutputStream stampaDocumento (CalcoloPenaDL92Model lCalcoloDL92Model) throws F3BException
  {
 		 FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
 		 UfficioModel lUfficio = getUfficioByCodUfficio(lFascicoloModel.getChiaveUfficio());
 		 String descrTipoUff = lFascicoloModel.getDescrTipoUfficio();
 		 
 		 UtenteModel lUtente = getUtenteConnesso();
 		 
 		 String lNomeTemplate = "";
 		 
 		 if ("L".equals(lCalcoloDL92Model.getPosizioneGiuridica()))
 			 lNomeTemplate = "/var/SIES/template/siep/altri/SIEP_DL92-24_LIB.rtf";
 		 else
 			 lNomeTemplate = "/var/SIES/template/siep/altri/SIEP_DL92-24_DET.rtf";
 		 
 		 /*
     EventoNotificaModel lEveMod = new EventoNotificaModel();
     lEveMod.getEvento().setIdEvento(null);
     lEveMod.getEvento().setFasSieIdFascicoloSiep (lFascicoloModel.getIdFascicoloSiep());

     lEveMod.getEvento().setDescrLuogoEmittente(lUfficio.getDescrComune());
     lEveMod.getEvento().setDescrUfficioEmittente(lUfficio.getDescrTipoUfficio());
     lEveMod.setNomeTemplate (lNomeTemplate);
     */
     
		 //IStampa lStampa = SICOLookupRemote.getStampaRemote();
		 //TreeModel lTreeRoot = lStampa.prelevaDatiEventoSiep(lEveMod, lUtente);

			IFascicoloSiepStampa lCtrStam = SIEPLookupRemote.getFascicoloSiepStampaRemote();
			TreeModel lTreeRoot = lCtrStam.prelevaDatiStampaFascicolo (lFascicoloModel, lUtente);
			siesLogger.info("NOME TEMPLATE >>>" + lNomeTemplate);
 		 
 		 /*
 		 
 		 XModel lStampa = new XModel(); 		 
 		 
 		 lStampa.setUfficio (lUfficio.getDescrComune());
 		 lStampa.setTipoUfficio (lUfficio.getDescrTipoUfficio());
 		 lStampa.setDataElaborazione (DateUtils.getSysDate());
 		 
 		 UtenteModel lUtente = getUtenteConnesso();
 		 if (lUtente != null && lUtente.getUfficioUtente() != null) {
			 UfficioModel lUffMod = lUtente.getUfficioUtente();

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
			}
			else
				lStampa.setTipoUfficioT1(descrTipoUff);
 		}
 		 
		if (descrTipoUff.indexOf("GENERALE") > 0) {
      lStampa.setFirmatario("Il Procuratore Generale");
    } else {
      lStampa.setFirmatario("Il Pubblico Ministero");
    }
		
		TreeModel	lTreeRoot = new TreeModel(lStampa);
		TreeModel lTreeFasMod = new TreeModel(lFascicoloModel);
		TreeModel lTreeSoggetto = new TreeModel(lFascicoloModel.getSoggetto());
		TreeModel lTreeSentenza = new TreeModel(lFascicoloModel.getSentenza());
		
		*/
		TreeModel lTreeCalcoloDL92 = new TreeModel(lCalcoloDL92Model);
		
		lTreeCalcoloDL92.add (new TreeModel (lCalcoloDL92Model.getSemestrePresofferto()));
		
		Vector <SemestreDL92Model> lSementri = lCalcoloDL92Model.getListaSemetri();
		for (int i=0; i< lSementri.size(); i++) {
			SemestreDL92Model lSemestre = lSementri.elementAt(i);
			lTreeCalcoloDL92.add (new TreeModel (lSemestre));
		}
		
		//lTreeRoot.add(lTreeFasMod);
		//lTreeRoot.add(lTreeSoggetto);
		//lTreeRoot.add(lTreeSentenza);
		lTreeRoot.add(lTreeCalcoloDL92);
		
		// Per test si aggancia la copertina
    //TemplateModel lTemMod = new TemplateModel();
    //ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

    //lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(null, null, "0302",null);
    
		//String lNomeTemplate = "C:\\template\\siep\\altri\\SIEP_COPERTINA.rtf";
		//String lNomeTemplate = TemplateManager.getInstance().getTemplateName(lTemMod.getIdTemplate());

		ReportGenerator lReport = new ReportGenerator();
		ByteArrayOutputStream lByteArrayOut = (ByteArrayOutputStream) lReport.generateDocument(lTreeRoot,lNomeTemplate);
		
		//IStampa lStampa = SICOLookupRemote.getStampaRemote();
	  //TreeModel lTree = lStampa.prelevaDatiEventoSiep(lEveMod, aUtente);			
     return lByteArrayOut;
   }

	/**
	 * 
	 * @return
	 */
	private ByteArrayOutputStream stampaExcel (CalcoloPenaDL92Model lCalcoloDL92Model) throws F3BException {

		// foglio excel
		HSSFWorkbook wb = new HSSFWorkbook();
		
		this.creaFoglioRiepilogo (wb, lCalcoloDL92Model);
		this.creaFoglioLibero    (wb, lCalcoloDL92Model);
		this.creaFoglioDetenuto  (wb, lCalcoloDL92Model);
		
		//==================================================================
		// 
		//==================================================================	
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("StampaExcel.processRequest: " + ioe);
		}
		
		return fileOut;
		
	}
	
	/**
	 * 
	 * @param wb
	 * @param lCalcoloDL92Model
	 */
	private void creaFoglioRiepilogo (HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {
	  // foglio dettaglio
		HSSFSheet sheetRiepilogo = wb.createSheet("Riepilogo");

		
		// Font 
		HSSFFont fontBold = wb.createFont();
		fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		// Stili
		HSSFCellStyle csNull = wb.createCellStyle();
		
		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);
		
	  // stile per celle col bordo
		HSSFCellStyle csBold = getBordo4Lati(wb);
		csBold.setFont(fontBold);

		
		// stile per celle col bordo con carattere grassetto centrato
		HSSFCellStyle csBoldCenter = getBordo4Lati(wb);
		csBoldCenter.setFont(fontBold);
		csBoldCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);		
		
		// Titolo 1 - 
		HSSFCellStyle csTitolo1 = getBordo4Lati(wb);
		csTitolo1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csTitolo1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csTitolo1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		csTitolo1.setFont(fontBold);
		
		// Stile delle celle grigie allineata a destre 
		HSSFCellStyle csGrigioDestra = getBordo4Lati(wb);
		csGrigioDestra.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		csGrigioDestra.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csGrigioDestra.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		
		// Stile delle celle gialle con i dati allineate al centro (dati di input)
		HSSFCellStyle csGialloCentro = getBordo4Lati(wb);
		csGialloCentro.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csGialloCentro.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csGialloCentro.setFillForegroundColor(HSSFColor.YELLOW.index);
		csGialloCentro.setFont(fontBold);
		
		// Stile delle celle grigie con i dati allineate al centro (dati calcolati)
		HSSFCellStyle csGrigioCentro = getBordo4Lati(wb);
		csGrigioCentro.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csGrigioCentro.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csGrigioCentro.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		csGrigioCentro.setFont(fontBold);
		
		// Stile delle celle Verdi 
		HSSFCellStyle csVerdeCentro = getBordo4Lati(wb);
		csVerdeCentro.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csVerdeCentro.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		csVerdeCentro.setFillForegroundColor(HSSFColor.GREEN.index);
		
		// Impostare opportunamente la larghezza della prima riga
		sheetRiepilogo.setColumnWidth(0, (35 * 256)); // Larghezza prima colonna
		sheetRiepilogo.setColumnWidth(1, (11 * 256)); // Larghezza seconda colonna
		
		int nRow = 0;

		//==================================================================
		// Sezione con i dati del presofferto (dati input)
		//==================================================================
		//nRow++;
		HSSFRow row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Custodia cautelare (presofferto)", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); //su 2 colonne
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Anni di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumAnniPresofferto()," "), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Mesi di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumMesiPresofferto()," "), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di custodia cautelare -->", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumGiorniPresofferto()," "), csGialloCentro);
		
		nRow++; // riga vuota
		
		//==================================================================
		// Sezione con i dati del presofferto calcolati
		//==================================================================	
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto(); 
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di custodia cautelare per erogazione L.A.:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getProgressivo(),""), csGrigioCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturati in custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(),"") , csGrigioCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di custodia cautelare eccedenti i semestri utili per erogazione L.A. (verranno conteggiati per anticipare il primo semestre utile nell'esecuzione pena):", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloPresofferto.getGiorniResiduiPresofferto(),""), csGrigioCentro);		
		
		nRow++; // riga vuota
		
		//==================================================================
		// Sezione Pena da espiare al netto del Presofferto (dati input)
		//==================================================================	
		nRow++;
    row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Pena da espiare al netto della custodia cautelare (presofferto)", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); //su 2 colonne
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Anni pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumAnni(),""), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Mesi pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumMesi(),""), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni pena al netto della custodia cautelare:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getTotaleDaEseguire().getNumGiorni(),""), csGialloCentro);
		
		nRow++; // riga vuota
		
		//==================================================================
		// Sezione con LA e semestri calcolati
		//==================================================================	
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di pena ipotetica per erogazione L.A.:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getSemestriUtili (),""), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturabili e usufruibili in pena ipotetica:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getNumGiorniLAMaturataInPenaresidua(),""), csGialloCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di L.A. maturabili e non usufruibili in pena ipotetica:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(),""), csGialloCentro);		
		
		nRow++; // riga vuota
		
		//==================================================================
		// DATI DA INDICARE NEL PROVVEDIMENTO DI ESECUZIONE
		//==================================================================	
		//@TODO da formattare in verde
		nRow++;
    row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Dati da indicare nei provvedimenti di esecuzione:", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); //su 2 colonne
		
		nRow++;

		nRow++;
		String strPena = "";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumAnni(),"") + " Anni ";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumMesi(),"") + " Mesi ";
		strPena += StringUtils.toStringJSP(lCalcoloDL92Model.getPenaIpotetica().getNumGiorni(),"") + " Giorni ";

		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Pena ipotetica ad esito delle detrazioni dei soli giorni di L.A. usufruibili:", csGrigioDestra);
		setCell(row, 1, strPena , csGrigioCentro);		
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Semestri utili di pena scontata:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getSemestriUtiliPenaScontata(),"") , csGrigioCentro);	
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di liberazione anticipata concedibili:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(),"") , csGrigioCentro);	
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "Giorni di liberazione anticipata usufruibili:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(lCalcoloDL92Model.getLAApplicate().intValue(),"") , csGrigioCentro);	
		
		nRow++;
		//==================================================================
		// CALCOLI CON DATA ESECUZIONE
		//==================================================================	
		nRow++;
    row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "CALCOLI CON DATA ESECUZIONE:", csTitolo1);
		sheetRiepilogo.addMergedRegion(new CellRangeAddress(nRow, nRow, 0, 1)); //su 2 colonne		

		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "data decorrenza pena:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloDL92Model.getDataInizioPena(), "dd/MM/yyyy")) , csGrigioCentro);	
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "data scarcerazione senza LA:", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloDL92Model.getDataScarcerazioneNoLA(), "dd/MM/yyyy")) , csGrigioCentro);	
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "data scarcerazione con LA applicati (data fine pena calcolata CON fungibilita'):", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloDL92Model.getDataScarcerazioneLAFung(), "dd/MM/yyyy")) , csGrigioCentro);
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "data scarcerazione con LA concessi (data fine pena calcolata SENZA fungibilita'):", csGrigioDestra);
		setCell(row, 1, StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloDL92Model.getDataScarcerazioneLANoFung(), "dd/MM/yyyy")) , csGrigioCentro);		
		
		nRow++;
		row = sheetRiepilogo.createRow(nRow);
		setCell(row, 0, "data scarcerazione senza applicare l'ultimo semestre (nei soli casi in cui ci sarebbe un credito di L.A.):", csGrigioDestra);
		if (lCalcoloDL92Model.getLAFungibili().intValue()>0) 
			setCell(row, 1, StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloDL92Model.getDataScarcerazionePenultimoSemestre(), "dd/MM/yyyy")), csGrigioCentro);	
		else 
			setCell(row, 1, "non applicabile", csGrigioCentro);	
	}
	
	
	private void creaFoglioLibero (HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {
	  // foglio Libero
		HSSFSheet sheetLibero = wb.createSheet("LIBERO");
		
		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);
		
		// Impostare opportunamente la larghezza della prima riga
		sheetLibero.setColumnWidth(0, (5 * 256));  // Progressivo
		sheetLibero.setColumnWidth(1, (11 * 256)); // Desc Semestre
		sheetLibero.setColumnWidth(2, (11 * 256)); // LA APPLICATA
		sheetLibero.setColumnWidth(3, (11 * 256)); // Residuao Pena
		sheetLibero.setColumnWidth(4, (20 * 256)); // Colonna riepilogo
		
		int nRow = 0;

		//==================================================================
		// Sezione con i dati del presofferto (dati input)
		//==================================================================
		//nRow++;
		HSSFRow row = sheetLibero.createRow(nRow);
		setCell(row, 0, " ", cs);
		setCell(row, 1, " ", cs);
		setCell(row, 2, "L.A. APPLICATA", cs);
		setCell(row, 3, "RESIDUO ANNI PENA", cs);
		setCell(row, 4, "L.A. MATURATA MA NON APPLICATA / PENA ESPIATA IN ECCESSO", cs);

		//==================================================================
		// Prima Riga Presofferto 
		//==================================================================
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto(); 
		String lStringResiduo =   lCalcoloPresofferto.getResiduoNumAnni()+" anni "
		                        + lCalcoloPresofferto.getResiduoNumMesi()+" mesi " 
				                    + lCalcoloPresofferto.getResiduoNumGiorni()+" giorni" ; 
		nRow++;
		row = sheetLibero.createRow(nRow);
		setCell(row, 0, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(),"0"), cs);
		setCell(row, 1, "SEMESTRI ESPIATI IN C.C. ", cs);
		setCell(row, 2, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(),"0"), cs);
		setCell(row, 3, lStringResiduo, cs);
		setCell(row, 4, StringUtils.toStringJSP(lCalcoloDL92Model.getLAFungibili().intValue(),"0"), cs);

		
		//==================================================================
		// Ciclo sui semestri
		//==================================================================
		Vector <SemestreDL92Model> mListaSemetri = lCalcoloDL92Model.getListaSemetri();
    for (int i = 0; i<mListaSemetri.size(); i++) {
    	SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
    	String lPenaStr = lSemestreUtile.getResiduoNumAnni()+" anni - " 
                      + lSemestreUtile.getResiduoNumMesi()+" mesi - "
                      + lSemestreUtile.getResiduoNumGiorni()+" giorni ";
    	
    	String lColorLastSem="";
    	if (lSemestreUtile.getLAApplicate().intValue()<45)
    		lColorLastSem =  "style='color=red'";
    	
  		nRow++;
  		row = sheetLibero.createRow(nRow);
  		setCell(row, 0, StringUtils.toStringJSP(lSemestreUtile.getProgressivo())+"°", cs);
  		setCell(row, 1, "semestre utile per L.A. ", cs);
  		setCell(row, 2, StringUtils.toStringJSP(lSemestreUtile.getLAApplicate(),"0"), cs);
  		setCell(row, 3, lPenaStr, cs); 
  		
  		if (nRow==2) {
    		setCell(row, 4, "GIORNI L.A. CONCESSI", cs);  			
  		}
  		else if (nRow==3) {
  			setCell(row, 4,StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(),"0"), cs); 
  		}
    }
    
    // Se esco dal ciclo con pochi semestri devo comunque aggiungere il rigo 3 e 4 del foglioexcel (col 5)
		if (nRow==1) {
			// non ho nemmeno 1 semestre devo aggiungere la riga 3
  		nRow++;
  		row = sheetLibero.createRow(nRow);
  		setCell(row, 0, "", cs); 
  		setCell(row, 1, "", cs); 
  		setCell(row, 2, "", cs); 
  		setCell(row, 3, "", cs); 
  		setCell(row, 4, "GIORNI L.A. CONCESSI", cs);  			
		}
		
		if (nRow==2) {
			// 0 o 1 solo semestre utile devo aggiungere una riga
			nRow++;
  		row = sheetLibero.createRow(nRow);
  		setCell(row, 0, "", cs); 
  		setCell(row, 1, "", cs); 
  		setCell(row, 2, "", cs); 
  		setCell(row, 3, "", cs); 
			setCell(row, 4,StringUtils.toStringJSP(lCalcoloDL92Model.getLAMaturate().intValue(),"0"), cs); 
		}
	}
	
	private void creaFoglioDetenuto (HSSFWorkbook wb, CalcoloPenaDL92Model lCalcoloDL92Model) {
	  // foglio dettaglio
		HSSFSheet sheetDetenuto = wb.createSheet("DETENUTO");
		
		// stile per celle col bordo
		HSSFCellStyle cs = getBordo4Lati(wb);
		
		// Impostare opportunamente la larghezza della prima riga
		sheetDetenuto.setColumnWidth(0, (11 * 256)); // Larghezza prima colonna
		sheetDetenuto.setColumnWidth(1, (11 * 256)); // Larghezza seconda colonna
		sheetDetenuto.setColumnWidth(2, (8 * 256));  // LA APPLICATA
		sheetDetenuto.setColumnWidth(3, (11 * 256)); // Larghezza quarta colonna
		sheetDetenuto.setColumnWidth(4, (11 * 256)); // Larghezza quinta colonna		
		sheetDetenuto.setColumnWidth(5, (8 * 256)); // ANNI RESIDUI	
		sheetDetenuto.setColumnWidth(6, (8 * 256)); // MESI RESIDUI			
		sheetDetenuto.setColumnWidth(7, (8 * 256)); // GIORNI RESIDUI		
		sheetDetenuto.setColumnWidth(8, (11 * 256)); //SURPLUS	
		
		int nRow = 0;		
		
		//==================================================================
		// Prima Riga INTESTAZIONE 
		//==================================================================
		//nRow++;
		HSSFRow row = sheetDetenuto.createRow(nRow);
		setCell(row, 0, "DATA DECORRENZA", cs);
		setCell(row, 1, DateUtils.getDateToString  (lCalcoloDL92Model.getDataInizioPena(),"dd/MM/yyyy"), cs);
		setCell(row, 2, "L.A. APPLICATA", cs);
		setCell(row, 3, "DATA IN CUI MATURO L.A.", cs);
		setCell(row, 4, "NUOVA DATA SCADENZA APPLCANDO LE CONCESSIONI DI 45GG PER INTERO", cs);		
		setCell(row, 5, "ANNI RESIDUI", cs);
		setCell(row, 6, "MESI RESIDUI", cs);
		setCell(row, 7, "GIORNI RESIDUI", cs);
		setCell(row, 8, "SURPLUS DETENZIONE (GIORNI DI FUNGIBILITA)", cs);
		//setCell(row, 9, "SURPLUS DETENZIONE", cs);
		
		//==================================================================
		// Prima Riga Presofferto 
		//==================================================================
		SemestreDL92Model lCalcoloPresofferto = lCalcoloDL92Model.getSemestrePresofferto(); 

		nRow++;
		row = sheetDetenuto.createRow(nRow);
		setCell(row, 0, StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(),"0"), cs);
		setCell(row, 1, "SEMESTRI ESPIATI IN C.C. ", cs);
		setCell(row, 2, StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(),"0"), cs);
		setCell(row, 3, "", cs);
		setCell(row, 4, DateUtils.getDateToString (lCalcoloPresofferto.getNuovaDataScadenzaPena(),"dd/MM/yyyy"), cs);
		setCell(row, 5, StringUtils.toStringJSP   (lCalcoloPresofferto.getResiduoNumAnni().intValue(),"0"), cs);
		setCell(row, 6, StringUtils.toStringJSP   (lCalcoloPresofferto.getResiduoNumMesi().intValue(),"0"), cs);
		setCell(row, 7, StringUtils.toStringJSP   (lCalcoloPresofferto.getResiduoNumGiorni().intValue(),"0"), cs);
		setCell(row, 8, "", cs); // SURPLUS solo ultimo semestre. Sicuramente NON presente su presofferto
		// surpl 
		

		//==================================================================
		// Ciclo sui semestri
		//==================================================================
		Vector <SemestreDL92Model> mListaSemetri = lCalcoloDL92Model.getListaSemetri();
    for (int i = 0; i<mListaSemetri.size(); i++) {
    	SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
    	
  		nRow++;
  		row = sheetDetenuto.createRow(nRow);
  		
  		setCell(row, 0, StringUtils.toStringJSP(lSemestreUtile.getProgressivo(),"0"), cs);
  		setCell(row, 1, "semestre maturato per L.A. ", cs);
  		setCell(row, 2, StringUtils.toStringJSP   (lSemestreUtile.getLAApplicate(),"0"), cs);
  		setCell(row, 3, DateUtils.getDateToString (lSemestreUtile.getDataMaturazioneLA(),"dd/MM/yyyy"), cs);
  		setCell(row, 4, DateUtils.getDateToString (lSemestreUtile.getNuovaDataScadenzaPena(),"dd/MM/yyyy"), cs);
  		setCell(row, 5, StringUtils.toStringJSP   (lSemestreUtile.getResiduoNumAnni().intValue(),"0"), cs);
  		setCell(row, 6, StringUtils.toStringJSP   (lSemestreUtile.getResiduoNumMesi().intValue(),"0"), cs);
  		setCell(row, 7, StringUtils.toStringJSP   (lSemestreUtile.getResiduoNumGiorni().intValue(),"0"), cs);
  		// SURPLUS solo ultimo semestre
  		if (i==(mListaSemetri.size()-1))
  			setCell(row, 8, StringUtils.toStringJSP   (lCalcoloDL92Model.getLAFungibili().intValue(),"0"), cs); // Ultimo RIgo
  		else
  			setCell(row, 8, "", cs);
    }
	}
	
	
	/**
	 * 
	 * @param row
	 * @param nCol
	 * @param value
	 * @param cs
	 * @return
	 */
	private HSSFCell setCell(HSSFRow row, int nCol, String value, HSSFCellStyle cs) {
		HSSFCell cell = row.createCell(nCol);
		cell.setCellValue(value);
		cell.setCellStyle(cs);
		return cell;
	}
	
	/**
	 * Crea un style con la cella bordata
	 * @param wb
	 * @return
	 */	
	private HSSFCellStyle getBordo4Lati(HSSFWorkbook wb) {
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		return cs;
	}
}
