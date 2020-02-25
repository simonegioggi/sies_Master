package siap.sius.statistiche.action;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.model.MagistratoModel;
import siap.siep.statis.controller.StatisController;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.statistiche.model.IspProcIntervalliModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


/**
 * <p>
 * Title: ActCreaStatisticheTempi
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report di Statistica: Numero Procedimenti SIUS per
 * Principali Intervalli Temporali
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
* @version 3.0
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCreaStatisticheTempi extends ActCreaStatisticheOggetti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws F3BException {
		
		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;
		String strRelatore = null;
		
	    // Lock
		// LockModel lck =
		// LockController.lockIfNotLocked(getServletContext(),"STATISTICHE_TEMPI","1",getCodUtenteConnesso(),getSession().getId());
	    // mod. michele 5/12/2008
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICHE",
				getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
	      return IWebConstants.PG_MESSAGE;
	    }

	    // Lettura Intervallo date
	    Date lDataIni = null, lDataFin = null;

	    lDataIni = getRequestDateParameter(  CAMPO_ANNO_INIZIALE, CAMPO_MESE_INIZIALE, CAMPO_GIORNO_INIZIALE );
	    lDataFin = getRequestDateParameter(CAMPO_ANNO_FINALE, CAMPO_MESE_FINALE, CAMPO_GIORNO_FINALE  );
		
	 // Recupero dati della maschera
		String lCodMag = getRequestStringParameter(CB_LISTA_MAGISTRATI);
		
				
	    // Controller per l'attivazione della STORED PROCEDURE adibita alla 
		// creazione delle Statistiche.
	  	
	    mCtrl = SIUSLookupRemote.getStatisticheSiusRemote();
	    mCtrl.ExCreaStatisticaTempiStProc(getCodUfficioUtenteConnesso(), lCodMag, lDataIni, lDataFin);

	    // Ricerca eventuale Cancelleria Assegnataria filtro nell'estrazione
	  	mCancMod = leggiCancelleriaAssegnataria();
	  	// Ricerca eventuale filtro su Collaboratore di Giustizia
	  	String lFiltroCollab = leggiFiltroCollaboratore();

	  	// Eventuale ricerca dei procedimenti da riportare nel dettaglio
	  	Vector lProcEstratti = dettaglioProcedimentiEstratti(lCodMag);
	  	
	  	if( lProcEstratti != null )
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
	  		siesLogger.info( "######## vector size : " +  lProcEstratti.size() );

		if (lCodMag.compareTo("0") != 0) {
			if (lCodMag.length() < 9) {
	  			// Ricerca del Magistrato
	  			lMagistrato = mCtrl.ExRicercaMagistratoByCod(lCodMag);
	  			strRelatore = lMagistrato.getCognome() + " " + lMagistrato.getNome();
			} else {
	  			// Ricerca dell' Esperto
	  			lEsperto = mCtrl.ExRicercaEspertoByCod(lCodMag);
	  			strRelatore = lEsperto.getCognome() + " " + lEsperto.getNome() + " (ESPERTO)";
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
	  			// siesLogger.debug(">>>> Test 011 <<<<"+ nomeMagistrato); 
	  		}
	  		
	  	}
	  	
	  	
	  	// 20131123 - dettaglioProcedimentiGiorniIntercorsi
	  	Vector lElencoProcGGIntercorsi = this.dettaglioProcedimentiGiorniIntercorsi(lCodMag);
	  	
	  	
		HSSFWorkbook wb = new HSSFWorkbook();
		
		// Recupero del Codice Ufficio michele 5/12/2008
		String lCodUfficio = getCodUfficioUtenteConnesso();
		// Ricerca riepilogo -- michele 5/12/2008
		Vector lStatistiche = mCtrl.ExRicercaStatisticaTempi(lCodUfficio);
		
		// creazione del file excel (foglio dettaglio)
		StatisController lStatisCtrl = new StatisController();
		
		/*
		 * Il metodo ExCreaReportTempiSIUS viene modificato per accettare, come primo parametro, un hashtable
		 * contenente i vettori dei dati da visualizzare negli SHEET del file EXCEL da generare. Per
		 * convenzione, la chiave della hashtable è una stringa composta dalla lettera 'M' seguita da un
		 * contatore numerico.
		 */
		/*
		 * Hashtable<String, Vector<?>> lHash = new Hashtable<String, Vector<?>>(); if( lStatistiche != null )
		 * lHash.put("M1", lStatistiche); if( lElencoProcGGIntercorsi != null) lHash.put("M3",
		 * lElencoProcGGIntercorsi);
		*/
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### chiamata al metodo di creazione report ####### ");
		
		// 20131124 - commentato
		// lStatisCtrl.ExCreaReportTempiSIUS(lStatistiche, wb, getUfficioUtenteConnesso(), lDataIni, lDataFin,
		// strRelatore, lProcEstratti,mDescOggetto, mCancMod, lFiltroCollab );
		lStatisCtrl.ExCreaReportTempiSIUS(wb, getUfficioUtenteConnesso(), lDataIni, lDataFin, strRelatore,
				lProcEstratti, mDescOggetto, mCancMod, lFiltroCollab, lStatistiche, lElencoProcGGIntercorsi);
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### invio file xls di report ####### ");
		
		// Generazione file xls
	    ByteArrayOutputStream fileOut = new ByteArrayOutputStream();	    
		try {
       	 wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException(	"ActCreaStatisticheTempi.processRequest: " + ioe);	    	
	    }
	    	    
		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD,IWebConstants.ATTACHMENT_DISPOSITION_FILE); 
		
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}
	
	
	/**
	 * La funzione effettua la ricerca degli oggetti estratti per codice Oggetto. La ricerca viene effettuata
	 * solo se è stato selezionato il check Dettaglio.
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	private Vector dettaglioProcedimentiEstratti(String aCodMag) throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### dettgalioProcedimentiEstratti - START ####### ");
		Vector lElenco = null;
		IspProcIntervalliModel lFiltroModel;
		if (isRequestChecked(CHK_DETTAGLIO) && !isRequestParameterNullObj(CB_LISTA_OGGETTI)) {
			mDescOggetto = new String();
			lFiltroModel = new IspProcIntervalliModel();
			lFiltroModel.setCodOggettoTenore(getRequestStringParameter(CB_LISTA_OGGETTI));
			if (aCodMag != null && !aCodMag.equalsIgnoreCase("0"))
				lFiltroModel.setCodMagistrato(aCodMag);
			lElenco = mCtrl.ExRicercaTempiEstratti(lFiltroModel);
			
			// decodifica dell'oggetto
			mDescOggetto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getMotivoProvvedimento(), getRequestStringParameter(CB_LISTA_OGGETTI));
		}
		
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### dettgalioProcedimentiEstratti - STOP ####### ");
		return lElenco;
	}
	
	/**
	 * La funzione effettua la ricerca degli oggetti estratti per codice Oggetto. La ricerca viene effettuata
	 * solo se è stato selezionato il check Dettaglio.
	 * 
	 * @return Vector
	 * @throws F3BException
	 */
	private Vector<IspProcIntervalliModel> dettaglioProcedimentiGiorniIntercorsi(String aCodMag)
			throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### dettgalioProcedimentiGiorniIntercorsi - START ####### ");
		Vector<IspProcIntervalliModel> lElenco = null;
		IspProcIntervalliModel lFiltroModel;
		
		lFiltroModel = new IspProcIntervalliModel();
		
		// inposta le condizioni di ordinamento.
		lFiltroModel
				.setOrder("COD_OGGETTO_TENORE ASC, TEMPO_RICEZIONE_DEPOSITO DESC, FAS_SIU_CHIAVE_ANNO ASC, FAS_SIU_CHIAVE_PROGR ASC");
		if (aCodMag != null && !aCodMag.equalsIgnoreCase("0"))
		    lFiltroModel.setCodMagistrato(aCodMag);
		
		// MEV_55 Modifica del 11/12/2017 Inizio ********
		String lCodUfficio = getCodUfficioUtenteConnesso();
		lFiltroModel.setFasSiuChiaveUfficio(lCodUfficio);
		// MEV_55 Modifica del 11/12/2017 Fine ********

		lElenco = mCtrl.ExRicercaTempiEstratti(lFiltroModel);
			
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug( "FiltroRicercaOggetti" + lFiltroModel.toString() );

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("####### dettgalioProcedimentiGiorniIntercorsi - STOP ####### ");
		return lElenco;
	}
	
}