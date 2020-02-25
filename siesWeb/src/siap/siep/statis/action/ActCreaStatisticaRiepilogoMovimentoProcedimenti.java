package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisticheMSController;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.StatRisSiesModel;

/**
 * MEV_39: creata nuova classe per gestire statistiche per procedimenti di classe IV (MS)
 * <p>
 * Title: ActCreaStatisticaRiepilogoMovimentoProcedimenti
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per riepilogo movimento procedimenti
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCreaStatisticaRiepilogoMovimentoProcedimenti extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Effettua il lock della funzione
		LockModel lm = LockController.lockIfNotLocked(getServletContext(),
				"ESTRAZIONE_STATISTICA_RIEPILOGO_MOVIMENTO_PROCEDIMENTI", "1", getCodUtenteConnesso(),
				getSession().getId());

		if (lm != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti!<BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// Setto Data Inizio Ricerca e Data Fine Ricerca
		String dataIniziale = getRequestStringParameter("dataIniziale");
		String dataFinale = getRequestStringParameter("dataFinale");
		siesLogger.info("DATA INIZIALE: " + dataIniziale);
		siesLogger.info("DATA FINALE: " + dataFinale);

		// Recupera le voci selezionate dall'elenco dell'elenco
		String[] listaStati = getRequestStringParameters(CAMPO_LISTA_STATI);

		String[] codStatiArch = new String[1];
		String[] codStatiAltre = new String[1];

		boolean bRiepilogo = false;
		int lunghezzaArray = listaStati.length;

		// ciclo per controllare la presenza di Riepilogo e Archiviazioni
		// e per calcolare la lunghezza dell'array dei dettagli
		// n.b. lung. array dettagli = num elementi selezionati - (Riepilogo, Archiviazioni, Altre Posizioni)
		for (int i = 0; i < lunghezzaArray; i++) {
			// 0 = RIEPILOGO GENERALE
			if (listaStati[i].equals("0")) {
				bRiepilogo = true;
				lunghezzaArray--;
			} else
			// ARCHIVIAZIONI
			if (listaStati[i].equals(COD_ARCHIVIAZIONI_MS)) {
				codStatiArch[0] = COD_ARCHIVIAZIONI_MS;
				lunghezzaArray--;
			} else if (listaStati[i].equals(COD_ALTRE_POSIZIONI_MS)) {
				codStatiAltre[0] = COD_ALTRE_POSIZIONI_MS;
				lunghezzaArray--;
			}
		}

		if (lunghezzaArray == 0)
			lunghezzaArray = 1;

		String[] codStatiDett = new String[lunghezzaArray];

		// ciclo per caricare l'array string dei dettagli con esclusione
		// di Riepilogo e Archiviazioni
		int iDett = 0;
		for (int i = 0; i < lunghezzaArray; i++) {
			if (!listaStati[i].equals("0") && !listaStati[i].equals(COD_ARCHIVIAZIONI_MS)
					&& !listaStati[i].equals(COD_ALTRE_POSIZIONI_MS)) {
				codStatiDett[iDett] = listaStati[i];
				iDett++;
			}
		}

		String codiceUffAccorpato = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);

		// ISTANZIA IL CONTROLLER
		StatisticheMSController smsc = new StatisticheMSController();

		// ==========================================================================
		// Recupero dati e creazione del File excel.
		// n.b. il File Excel si compone di 4 fogli
		// - RIEPILOGO [cod. 0] --> NO
		// - DETTAGLIO [cod. tutti gli altri]
		// - ARCHIVIAZIONI [cod .131]
		// - ALTRE POSIZIONI [cod. 129]
		// I dati inseriti dipendono dalle voci scelte dall'utente
		// ==========================================================================
		HSSFWorkbook wb = new HSSFWorkbook();

		// =================================================================
		// Recupero dati da inserire nel Foglio RIEPILOGO (se selezionato) --> NO
		// =================================================================
		if (bRiepilogo) {
			// RICERCA RIEPILOGO
			IspProvvedimentiModel lIspMod = new IspProvvedimentiModel();
			try {
				// CREAZIONE FOGLIO RIEPILOGO
				Vector lIspModVect = smsc.ExGetCountRiepilogoIspProvvedimentiMS(lIspMod);
				siesLogger.debug(" RICERCA RIEPILOGO = " + lIspModVect.size());
				smsc.ExCreateRiepilogoIspProvvedimentiMS(lIspModVect, wb, getUfficioUtenteConnesso(),
						codiceUffAccorpato);
			} catch (Exception e) {
				// info per il log
				siesLogger.error(e.getMessage() + " ### " + e);
				if (e instanceof DAOException || e instanceof SQLException)
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione dei dati presenti nel database!");
				else
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione della Statistica!");
				// valore di ritorno
				return IWebConstants.PG_MESSAGE;
			}
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio DETTAGLIO (in funzione dei codici selezionati)
		// ==========================================================================
		Vector stati = new Vector();
		// Recupera i dati per creare il Foglio Excel DETTAGLI
		Vector dettagli = null;
		if (codStatiDett[0] != null) {
			try {
				// RICERCA DETTAGLI
				dettagli = smsc.ricercaDettaglioProcedimenti(addCodiciTitoli(codStatiDett, smsc));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RICERCA DETTAGLI = " + dettagli.size());
			} catch (Exception e) {
				// info per il log
				siesLogger.error(e.getMessage() + " ### " + e);
				if (e instanceof DAOException || e instanceof SQLException)
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione dei dati presenti nel database!");
				else
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione della Statistica!");
				// valore di ritorno
				return IWebConstants.PG_MESSAGE;
			}
			// CREAZIONE FOGLIO DETTAGLI
			smsc.creaDettagliProcedimenti(dettagli, wb, getUfficioUtenteConnesso(), "Dettagli",
					codiceUffAccorpato, dataIniziale, dataFinale);
			addToVector(stati, dettagli);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio ARCHIVIAZIONI (se selezionato)
		// ==========================================================================
		if (codStatiArch[0] != null) {
			// RICERCA ARCHIVIAZIONI
			String[] lCodStatiArchConTitolo = new String[2];
			lCodStatiArchConTitolo[0] = codStatiArch[0];
			lCodStatiArchConTitolo[1] = ICostantiStatis.COD_TITOLO1_ARCHIVIAZIONI_MS;
			try {
				dettagli = smsc.ricercaDettaglioProcedimenti(lCodStatiArchConTitolo);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RICERCA ARCHIVIAZIONI = " + dettagli.size());
			} catch (Exception e) {
				// info per il log
				siesLogger.error(e.getMessage() + " ### " + e);
				if (e instanceof DAOException || e instanceof SQLException)
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione dei dati presenti nel database!");
				else
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione della Statistica!");
				// valore di ritorno
				return IWebConstants.PG_MESSAGE;
			}
			// CREAZIONE FOGLIO ARCHIVIAZIONI
			smsc.creaDettagliProcedimenti(dettagli, wb, getUfficioUtenteConnesso(), "Archiviazioni",
					codiceUffAccorpato, dataIniziale, dataFinale);
			addToVector(stati, dettagli);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio ALTRE POSIZIONI (se selezionato)
		// ==========================================================================
		if (codStatiAltre[0] != null) {
			// RICERCA ALTRE POSIZIONI

			String[] lCodStatiArchConTitolo = new String[2];
			lCodStatiArchConTitolo[0] = codStatiAltre[0];
			lCodStatiArchConTitolo[1] = ICostantiStatis.COD_TITOLO1_ALTRE_POSIZIONI;
			try {
				dettagli = smsc.ricercaDettaglioProcedimenti(lCodStatiArchConTitolo);
				// dettagli = smsc.ExRicercaDettaglioProcedimenti(codStatiAltre);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("RICERCA ALTRE POSIZIONI = " + dettagli.size());
			} catch (Exception e) {
				// info per il log
				siesLogger.error(e.getMessage() + " ### " + e);
				if (e instanceof DAOException || e instanceof SQLException)
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione dei dati presenti nel database!");
				else
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Errore nell'elaborazione della Statistica!");
				// valore di ritorno
				return IWebConstants.PG_MESSAGE;
			}

			// CREAZIONE FOGLIO ALTRE POSIZIONI
			smsc.creaDettagliProcedimenti(dettagli, wb, getUfficioUtenteConnesso(), "Altre Posizioni",
					codiceUffAccorpato, dataIniziale, dataFinale);
			addToVector(stati, dettagli);
		}

		// =================================================
		// Aggiunto controllo per rilancio eccezione
		// =================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" stati TOT = " + stati.size());
		if (stati.size() == 0 && !bRiepilogo) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaRiepilogo.processRequest: " + ioe);
		}

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		// valore di ritorno
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	} // CHIUDE processRequest()

	private void addToVector(Vector aStat, Vector aDett) throws F3BException {

		Date sysDate = DateUtils.getSysDate();
		String codUteCon = getCodUtenteConnesso();
		String codUffUteCon = getCodUfficioUtenteConnesso();
		Iterator itx = aDett.iterator();
		while (itx.hasNext()) {
			IspProvvedimentiModel lMod = (IspProvvedimentiModel) itx.next();
			StatRisSiesModel lMod2 = new StatRisSiesModel();
			lMod2.setChiaveAnno(lMod.getChiaveAnno());
			lMod2.setChiaveProgr(lMod.getChiaveProgr());
			lMod2.setCodOperatoreInserimento(codUteCon);
			lMod2.setCodPosizioneGiuridica(lMod.getCodPosizioneGiuridica());
			lMod2.setCodStatoProcedimento(lMod.getCodStatoProcedimento());
			lMod2.setCodUfficio(lMod.getCodUfficio());
			lMod2.setCodUfficioInserimento(codUffUteCon);
			lMod2.setDataEstrazione(sysDate);
			lMod2.setDataInserimento(sysDate);
			lMod2.setFasSieIdFascicoloSiep(lMod.getIdFascicoloSiep());
			aStat.add(lMod2);
		}
	}

	/**
	 * Aggiunge all'elenco dei codici selezionati, anche i codici dei relativi gruppi e sottogruppi di
	 * appartenenza in modo che la successiva estrazione carichi dalla tabella anche gli opportuni titoli da
	 * visualizzare. n.b. se per una voce selezionata dall'utente non è presente alcun dato, non viene
	 * recuperato nemmeno il titolo
	 *
	 * @param aCodiciSelezionati
	 * @param smsc
	 * @return String[]
	 */
	private String[] addCodiciTitoli(String[] codiciSelezionati, StatisticheMSController smsc)
			throws Exception {

		int x = codiciSelezionati.length;
		Vector<String> titoli1 = null;
		Vector<String> titoli2 = null;
		titoli1 = smsc.getTitoliPerCodici(codiciSelezionati, "T1");
		titoli2 = smsc.getTitoliPerCodici(codiciSelezionati, "T2");
		String[] codStatiDettConTitoli = new String[x + titoli1.size() + titoli2.size()];
		int posCorrente = 0;
		// Riverso i codici
		for (int i = 0; i < codiciSelezionati.length; i++) {
			codStatiDettConTitoli[posCorrente] = codiciSelezionati[i];
			posCorrente++;
		}
		// Aggiungo i T1
		for (int i = 0; i < titoli1.size(); i++) {
			codStatiDettConTitoli[posCorrente] = titoli1.elementAt(i);
			posCorrente++;
		}
		// Aggiungo i T2
		for (int i = 0; i < titoli2.size(); i++) {
			codStatiDettConTitoli[posCorrente] = titoli2.elementAt(i);
			posCorrente++;
		}

		// valore di ritorno
		return codStatiDettConTitoli;
	}

} // CHIUDE CLASSE ActCreaRiepilogoMovimentoProcedimenti