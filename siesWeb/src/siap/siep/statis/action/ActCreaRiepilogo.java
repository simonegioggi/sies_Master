package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.StatRisSiesModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCreaRiepilogo
 * </p>
 * <p>
 * Description: Classe Action per la creazione del riepilogo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCreaRiepilogo extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Genera il foglio Excel a partire dai dati estratti per le statistiche e al tipo di riepilogo
	 * selezionato dall'utente.
	 */
	public String processRequest() throws F3BException {

		// Effettua il lock della funzione
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "RIEPILOGO_STATI_PROCEDIMENTI",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupera le voci selezionate dall'elenco dell'elenco
		String[] lCodStati = getRequestStringParameters(CAMPO_LISTA_STATI);

		String[] lCodStatiArch = new String[1];
		String[] lCodStatiAltre = new String[1];

		boolean bRiepilogo = false;

		int arrLen = lCodStati.length;

		// ciclo per controllare la presenza di Riepilogo e Archiviazioni
		// e per calcolare la lunghezza dell'array dei dettagli
		// n.b. lung. array dettagli = num elementi selezionati - (Riepilogo, Archiviazioni, Altre Posizioni)
		for (int i = 0; i < lCodStati.length; i++) {
			if (lCodStati[i].equals("0")) { // 0 = RIEPILOGO GENERALE
				bRiepilogo = true;
				arrLen--;
			} else if (lCodStati[i].equals(COD_ARCHIVIAZIONI)) { // ARCHIVIAZIONI
				lCodStatiArch[0] = COD_ARCHIVIAZIONI;
				arrLen--;
			} else if (lCodStati[i].equals(COD_ALTRE_POSIZIONI)) {
				lCodStatiAltre[0] = COD_ALTRE_POSIZIONI;
				arrLen--;
			}
		}

		if (arrLen == 0)
			arrLen = 1;

		String[] lCodStatiDett = new String[arrLen];

		// ciclo per caricare l'array string dei dettagli con esclusione
		// di Riepilogo e Archiviazioni
		int iDett = 0;
		for (int i = 0; i < lCodStati.length; i++) {
			if (!lCodStati[i].equals("0") && !lCodStati[i].equals(COD_ARCHIVIAZIONI)
					&& !lCodStati[i].equals(COD_ALTRE_POSIZIONI)) {
				lCodStatiDett[iDett] = lCodStati[i];
				iDett++;
			}
		}

		// NGG

		String lUffScelto = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);

		// ISTANZIA IL CONTROLLER
		StatisController lCtrl = new StatisController();

		// ==========================================================================
		// Recupero dati e creazione del File excel.
		// n.b. il File Excel si compone di 4 fogli
		// - RIEPILOGO [cod. 0]
		// - DETTAGLIO [cod. tutti gli altri]
		// - ARCHIVIAZIONI [cod .131]
		// - ALTRE POSIZIONI [cod. 129]
		// I dati inseriti dipendono dalle voci scelte dall'utente
		// ==========================================================================
		HSSFWorkbook wb = new HSSFWorkbook();

		// =================================================================
		// Recupero dati da inserire nel Foglio RIEPILOGO (se selezionato)
		// =================================================================
		if (bRiepilogo) {
			// RICERCA RIEPILOGO
			IspProvvedimentiModel lIspMod = new IspProvvedimentiModel();
			Vector lIspModVect = lCtrl.ExGetCountRiepilogoIspProvvedimenti(lIspMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" RICERCA RIEPILOGO = " + lIspModVect.size());
			// CREAZIONE FOGLIO RIEPILOGO
			lCtrl.ExCreateRiepilogoIspProvvedimenti(lIspModVect, wb, this.getUfficioUtenteConnesso(),
					lUffScelto);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio DETTAGLIO (in funzione dei codici selezionati)
		// ==========================================================================
		Vector lStatVect = new Vector();
		// Recupera i dati per creare il Foglio Excel DETTAGLI
		Vector lDetModVect = null;
		if (lCodStatiDett[0] != null) {
			// RICERCA DETTAGLI

			lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(addCodiciTitoli(lCodStatiDett));

			// lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(lCodStatiDett);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("RICERCA DETTAGLI = " + lDetModVect.size());
			// CREAZIONE FOGLIO DETTAGLI
			lCtrl.ExCreateDettagliProcedimenti(lDetModVect, wb, this.getUfficioUtenteConnesso(), "Dettagli",
					lUffScelto);

			addToVector(lStatVect, lDetModVect);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio ARCHIVIAZIONI (se selezionato)
		// ==========================================================================
		if (lCodStatiArch[0] != null) {
			// RICERCA ARCHIVIAZIONI
			String[] lCodStatiArchConTitolo = new String[2];
			lCodStatiArchConTitolo[0] = lCodStatiArch[0];
			lCodStatiArchConTitolo[1] = ICostantiStatis.COD_TITOLO1_ARCHIVIAZIONI;

			// lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(lCodStatiArch);
			lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(lCodStatiArchConTitolo);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("RICERCA ARCHIVIAZIONI = " + lDetModVect.size());
			// CREAZIONE FOGLIO ARCHIVIAZIONI
			lCtrl.ExCreateDettagliProcedimenti(lDetModVect, wb, this.getUfficioUtenteConnesso(),
					"Archiviazioni", lUffScelto);

			addToVector(lStatVect, lDetModVect);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio ALTRE POSIZIONI (se selezionato)
		// ==========================================================================
		if (lCodStatiAltre[0] != null) {
			// RICERCA ALTRE POSIZIONI

			String[] lCodStatiArchConTitolo = new String[2];
			lCodStatiArchConTitolo[0] = lCodStatiAltre[0];
			lCodStatiArchConTitolo[1] = ICostantiStatis.COD_TITOLO1_ALTRE_POSIZIONI;

			lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(lCodStatiArchConTitolo);
			// lDetModVect = lCtrl.ExRicercaDettaglioProcedimenti(lCodStatiAltre);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("RICERCA ALTRE POSIZIONI = " + lDetModVect.size());

			// CREAZIONE FOGLIO ALTRE POSIZIONI
			lCtrl.ExCreateDettagliProcedimenti(lDetModVect, wb, this.getUfficioUtenteConnesso(),
					"Altre Posizioni", lUffScelto);

			addToVector(lStatVect, lDetModVect);
		}

		// =================================================
		// Aggiunto controllo per rilancio eccezione
		// =================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lStatVect TOT = " + lStatVect.size());
		if (lStatVect.size() == 0 && !bRiepilogo) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaRiepilogo.processRequest: " + ioe);
		}

		// Codice commentato il 09/04/2009. Salvava sulla tabella ISP_STAT_RIS_SIES
		// l'esito del riepilogo (praticamente i dati utilizzati per
		// generare del foglio excel) storicizzandolo (data riepilogo). In questo
		// modo la tabella cresceva a dismisura (oltre 4milioni di record su TO).
		// Per futuro possibile sviluppo: il salvataggio va effettuato solo se
		// richiesto dall'utente (es riepilogo di fine anno) e non ogni volta che
		// l'utente effettua una stampa (per prova o controlli periodici)
		// n.b. non si farebbe prima a salvare il solo foglio excel (blob)?
		// if (lStatVect.size() > 0) {
		// // INSERT IN STAT_RIS_SIES
		// lCtrl.ExInserisciStatRisSies(lStatVect);
		// }

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

	/**
	 * 
	 * @param aStat
	 * @param aDett
	 * @throws F3BException
	 */
	private void addToVector(Vector aStat, Vector aDett) throws F3BException {

		Date sysDate = DateUtils.getSysDate();
		String codUteCon = this.getCodUtenteConnesso();
		String codUffUteCon = this.getCodUfficioUtenteConnesso();
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
	 * @return
	 */
	private String[] addCodiciTitoli(String[] aCodiciSelezionati) throws F3BException {

		int x = aCodiciSelezionati.length;

		StatisController lStatisCtrl = new StatisController();

		Vector<String> lTitoli1 = null;

		Vector<String> lTitoli2 = null;

		lTitoli1 = lStatisCtrl.getTitoliPerCodici(aCodiciSelezionati, "T1");
		lTitoli2 = lStatisCtrl.getTitoliPerCodici(aCodiciSelezionati, "T2");

		String[] lCodStatiDettConTitoli = new String[x + lTitoli1.size() + lTitoli2.size()];

		int lPosCorrente = 0;

		// Riverso i codici
		for (int i = 0; i < aCodiciSelezionati.length; i++) {
			lCodStatiDettConTitoli[lPosCorrente] = aCodiciSelezionati[i];
			lPosCorrente++;
		}

		// Aggiungo i T1
		for (int i = 0; i < lTitoli1.size(); i++) {
			lCodStatiDettConTitoli[lPosCorrente] = lTitoli1.elementAt(i);
			lPosCorrente++;
		}

		// Aggiungo i T2
		for (int i = 0; i < lTitoli2.size(); i++) {
			lCodStatiDettConTitoli[lPosCorrente] = lTitoli2.elementAt(i);
			lPosCorrente++;
		}

		return lCodStatiDettConTitoli;
	}

}