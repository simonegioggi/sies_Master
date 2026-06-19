package siap.sius.statistiche.util.excel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

//import siap.sius.fascicolo.util.HSSFUtils;
import siap.sius.statistiche.controller.IStatisticheSius;
import siap.sius.statistiche.model.EveFasGepSogModel;
import siap.sius.statistiche.model.RicercaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import siap.util.excel.SIAPExcelProducer;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;

@SuppressWarnings("rawtypes")
public class ProcAggregatiPerProcuraMittenteExcel extends SIAPExcelProducer {

	  public ByteArrayOutputStream creaExcelProcProcuraMittente(RicercaProcedimentoModel aRicercaModel) 
	  throws F3BException {

	      ByteArrayOutputStream             lFileOut                = null;
	      HSSFWorkbook                      lWb                     = null;
	      IStatisticheSius                  lCtrlStatSius           = null;
	      Collection<EveFasGepSogModel>		lElenco                 = null;      
	      
	      lWb = new HSSFWorkbook();
	      
	      lCtrlStatSius = SIUSLookupRemote.getStatisticheSiusRemote();
	      lElenco  = lCtrlStatSius.ExRicercaProcProcuraMittente(aRicercaModel);
	      this.creaFoglioProcPerProcuraMittente(lWb, aRicercaModel, lElenco);
	      
	      lElenco = lCtrlStatSius.ExRicercaProcTotaliProcuraMittente(aRicercaModel);
	      this.creaFoglioProcTotaliPerProcuraMittente(lWb,aRicercaModel,lElenco);     
	      
	      
	      lFileOut = new ByteArrayOutputStream();
	      
	      try {
	          lWb.write(lFileOut);
	      } catch (IOException ioe) {
	          throw new F3BException("StatisController.creaFoglioProcAggrIstitutiDetenzione : " + ioe);
	      }
	      
	      return lFileOut;
	  }
	  
	/**
	   * 
	   * @param aWb
	   * @param aElenco
	   * @return
	   */
	  private HSSFWorkbook creaFoglioProcPerProcuraMittente( HSSFWorkbook aWb, RicercaProcedimentoModel aRicerca, Collection<EveFasGepSogModel> aElenco ) {
	      
	      HSSFSheet                         lSheet                  = null; 
	      HSSFCellStyle                     lCellStyleNull          = null;
	      HSSFCellStyle                     lCellStyleCenter        = null;
	      HSSFRow                           lRow                    = null;
	      Iterator                          lItx                    = null;
	      int                               lRowCounter             = 0;
	      EveFasGepSogModel             	lModel                  = null;
	      String                            lPatternData            = "dd/MM/yyyy";
	      String                            lBuffer                 = null;
      

	      if( aWb == null )
	    	  aWb = new HSSFWorkbook();
	      
	      // creazione primo foglio
	      lSheet = aWb.createSheet("Elenco dei Procedimenti");
	      lCellStyleNull = aWb.createCellStyle();

	      //Intestazione del foglio excel
	      lRowCounter = setIntestazione(lSheet, aRicerca.getUtenteConnesso().getUfficioUtente(), lCellStyleNull);
	      lRowCounter += 2;

	      // Inserimento dei parametri di ricerca.
	      lRow = lSheet.createRow(lRowCounter);
	      setCell(lRow, 0, "Criteri di ricerca selezionati : " , lCellStyleNull); 
	      lRowCounter++;
	      
	      // Stampa filtri di ricerca
	      if (aRicerca != null) {   
	          lRow = lSheet.createRow(lRowCounter);
	          setCell(lRow, 0, lBuffer , lCellStyleNull); 
	          lRowCounter++;
	          if (aRicerca.getDataIscrizioneInizio() != null || aRicerca.getDataIscrizioneFine() != null) {
	              lBuffer = "Procedimenti iscritti : ";
	              if (aRicerca.getDataIscrizioneInizio() != null) {
	                  lBuffer += " dal " + DateUtils.getDateToString(aRicerca.getDataIscrizioneInizio(), lPatternData);
	              }
	              if (aRicerca.getDataIscrizioneFine() != null) {
	                  lBuffer += " al " + DateUtils.getDateToString(aRicerca.getDataIscrizioneFine(), lPatternData);
	              }
	              lRow = lSheet.createRow(lRowCounter);
	              setCell(lRow, 0, lBuffer , lCellStyleNull); 
	              lRowCounter++;
	          }
	      }

	      lRowCounter += 2;
	      
	      // stile per celle col bordo con testo centrato
	      lCellStyleCenter = getBordo4Lati(aWb);
	      lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
	      lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
	      lCellStyleCenter.setWrapText(true);

	      lRow = lSheet.createRow(lRowCounter++);
	      
	      lSheet.setColumnWidth( 0, 40 * 256);  // Procura Mittente 
	      lSheet.setColumnWidth( 1, 20 * 256);  // Descrizione (Comune Procura)
	      lSheet.setColumnWidth( 2, 25 * 256);  // Cognome
	      lSheet.setColumnWidth( 3, 25 * 256);  // Nome
	      lSheet.setColumnWidth( 4, 10 * 256);  // Data Nascita
	      lSheet.setColumnWidth( 5, 20 * 256);  // Luogo Nascita
	      lSheet.setColumnWidth( 6, 10 * 256);  // Anno
	      lSheet.setColumnWidth( 7, 11 * 256);  // Progressivo
	      lSheet.setColumnWidth( 8, 25 * 256);  // Posizione Giuridica
	      lSheet.setColumnWidth( 9, 40 * 256);  // Contenuto
	      lSheet.setColumnWidth(10, 40 * 256);  // Stato Procedimento

	      // Intestazione colonne
	      setCell(lRow,  0, "Procura Mittente",			lCellStyleCenter);
	      setCell(lRow,  1, "Descrzione",				lCellStyleCenter);
	      setCell(lRow,  2, "Cognome",  				lCellStyleCenter);
	      setCell(lRow,  3, "Nome",      				lCellStyleCenter);
	      setCell(lRow,  4, "Data Nascita",          	lCellStyleCenter);
	      setCell(lRow,  5, "Luogo Nascita",         	lCellStyleCenter);
	      setCell(lRow,  6, "Anno",           			lCellStyleCenter);
	      setCell(lRow,  7, "Progressivo",       		lCellStyleCenter);
	      setCell(lRow,  8, "Posizione Giuridica",      lCellStyleCenter);
	      setCell(lRow,  9, "Contenuto",    			lCellStyleCenter);
	      setCell(lRow, 10, "Stato Procedimento",   	lCellStyleCenter);
	      
	      lItx = aElenco.iterator();
	      while (lItx.hasNext()) {
	          lModel = (EveFasGepSogModel) lItx.next();
	          lRow = lSheet.createRow(lRowCounter++);          
	          
	          setCell(lRow, 0, lModel.getGeneraleProcedimento().getDescrTipoMittenteAtto(), lCellStyleCenter);
	          setCell(lRow, 1, lModel.getGeneraleProcedimento().getDescrMittente(), lCellStyleCenter);
	          setCell(lRow, 2, lModel.getFascicoloSius().getSoggetto().getCognome() ,lCellStyleCenter);
	          setCell(lRow, 3, lModel.getFascicoloSius().getSoggetto().getNome() ,lCellStyleCenter);
	          setCell(lRow, 4, 
	  						  StringUtils.toStringJSP( 
	  								  DateUtils.getDateToString(lModel.getFascicoloSius().getSoggetto().getDataNascita(), lPatternData), "-" ),lCellStyleCenter );
	          setCell(lRow, 5, 
	        		  		  StringUtils.toStringJSP(lModel.getFascicoloSius().getSoggetto().getDescrComuneNascita(), "-") ,lCellStyleCenter);
	          setCell(lRow, 6, ""+lModel.getFascicoloSius().getChiaveAnno() ,lCellStyleCenter);
	          setCell(lRow, 7, ""+lModel.getFascicoloSius().getChiaveProgr() ,lCellStyleCenter);
	          setCell(lRow, 8, StringUtils.toStringJSP( lModel.getGeneraleProcedimento().getDescrPosGiuridica(), "-" ),lCellStyleCenter);
	          setCell(lRow, 9, StringUtils.toStringJSP( lModel.getGeneraleProcedimento().getDescrOggettoProcedimento(), "-"),lCellStyleCenter);
	          setCell(lRow, 10, StringUtils.toStringJSP(lModel.getFascicoloSius().getDescrStatoFascicolo(), "-"), lCellStyleCenter);
	      }
		  
	      return aWb;
	  }  

	  /**
	   * 
	   * @param aWb
	   * @param aElenco
	   * @return
	   */
	private HSSFWorkbook creaFoglioProcTotaliPerProcuraMittente( HSSFWorkbook aWb, RicercaProcedimentoModel aRicerca, Collection<EveFasGepSogModel> aElenco ) {
	      
	      HSSFSheet                         lSheet                  = null; 
	      HSSFCellStyle                     lCellStyleNull          = null;
	      HSSFCellStyle                     lCellStyleCenter        = null;
	      HSSFRow                           lRow                    = null;
	      Iterator                          lItx                    = null;
	      int                               lRowCounter             = 0;
	      EveFasGepSogModel             	lModel                  = null;
	      String                            lPatternData            = "dd/MM/yyyy";
	      String                            lBuffer                 = null;
	      int								lTotalideiTotali		= 0;

	      if( aWb == null )
	    	  aWb = new HSSFWorkbook();
	      
	      // creazione primo foglio
	      lSheet = aWb.createSheet("Totali Procedimenti");
	      lCellStyleNull = aWb.createCellStyle();

	      //Intestazione del foglio excel
	      lRowCounter = setIntestazione(lSheet, aRicerca.getUtenteConnesso().getUfficioUtente(), lCellStyleNull);
	      lRowCounter += 2;

	      // Inserimento dei parametri di ricerca.
	      lRow = lSheet.createRow(lRowCounter);
	      setCell(lRow, 0, "Criteri di ricerca selezionati : " , lCellStyleNull); 
	      lRowCounter++;
	      
	      // Stampa filtri di ricerca
	      if (aRicerca != null) {   
	          lRow = lSheet.createRow(lRowCounter);
	          setCell(lRow, 0, lBuffer , lCellStyleNull); 
	          lRowCounter++;
	          if (aRicerca.getDataIscrizioneInizio() != null || aRicerca.getDataIscrizioneFine() != null) {
	              lBuffer = "Procedimenti iscritti : ";
	              if (aRicerca.getDataIscrizioneInizio() != null) {
	                  lBuffer += " dal " + DateUtils.getDateToString(aRicerca.getDataIscrizioneInizio(), lPatternData);
	              }
	              if (aRicerca.getDataIscrizioneFine() != null) {
	                  lBuffer += " al " + DateUtils.getDateToString(aRicerca.getDataIscrizioneFine(), lPatternData);
	              }
	              lRow = lSheet.createRow(lRowCounter);
	              setCell(lRow, 0, lBuffer , lCellStyleNull); 
	              lRowCounter++;
	          }
	      }

	      lRowCounter += 2;
	      
	      // stile per celle col bordo con testo centrato
	      lCellStyleCenter = getBordo4Lati(aWb);
	      lCellStyleCenter.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
	      lCellStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
	      lCellStyleCenter.setWrapText(true);

	      lRow = lSheet.createRow(lRowCounter++);
	      
	      lSheet.setColumnWidth( 0, 50 * 256);  // Tipo Ufficio 
	      lSheet.setColumnWidth( 1, 25 * 256);  // Sede
	      lSheet.setColumnWidth( 2, 20 * 256);  // Totale Procedimenti

	      // Intestazione colonne
	      setCell(lRow,  0, "Tipo Ufficio",			lCellStyleCenter);
	      setCell(lRow,  1, "Sede",					lCellStyleCenter);
	      setCell(lRow,  2, "Totale Procedimenti",  lCellStyleCenter);
	      
	      lItx = aElenco.iterator();
	      while (lItx.hasNext()) {
	          lModel = (EveFasGepSogModel) lItx.next();
	          lRow = lSheet.createRow(lRowCounter++);          
	          
	          setCell(lRow, 0, lModel.getGeneraleProcedimento().getDescrTipoMittenteAtto(), lCellStyleCenter);
	          setCell(lRow, 1, lModel.getGeneraleProcedimento().getDescrMittente(), lCellStyleCenter);
	          setCell(lRow, 2, lModel.getTotale() ,lCellStyleCenter);
	          lTotalideiTotali += lModel.getTotale();
	      }
	      
	      lRowCounter++;
	      lRow = lSheet.createRow(lRowCounter++);
	      setCell(lRow,  0, "Totale Procedimenti",	lCellStyleCenter);
	      setCell(lRow,  1, "",  					lCellStyleCenter);
	      setCell(lRow,  2, ""+lTotalideiTotali,  	lCellStyleCenter);
		  
	      return aWb;
	  }  

}