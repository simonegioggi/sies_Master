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
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import siap.siep.statis.controller.StatisControllerCPP;
import siap.siep.statis.model.IspProvvedimentiModel;
import siap.siep.statis.model.RiepilogoPendentiDefinitiCPPModel;
import siap.siep.statis.model.StatRisSiesModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCreaRiepilogo_CPP
 * </p>
 * <p>
 * Description: Classe Action per la creazione della Statistica
 * </p>
 * <p>
 * Riepilogo Pendenti per procedimenti di classe VII
 * </p>
 * <p>
 * (CPP Conversione Pene pecuniarie)
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCreaRiepilogo_CPP extends ActionSiap implements ICostantiStatis {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// Effettua il lock della funzione
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "STATISTICA_PROC_PENDENTI_CPP",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// le voci dell'elenco sono per 'default' TUTTE selezionate
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

		// Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSession().getAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();

		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUff.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		String UfficioConnesso = "";
		if (lUffScelto.equals("-")) {
			UfficioConnesso = this.getCodUfficioUtenteConnesso();
		} else {
			Iterator itx = lListaUffici.iterator();
			while (itx.hasNext()) {
				UfficioAccorpatoModel lUffac = (UfficioAccorpatoModel) itx.next();

				if (lUffac.getDescrizione().equals(lUffScelto)) {
					UfficioConnesso = lUffac.getCodUfficio();
				}
			}
		}

		String DescUffIntesta = "";
		// NGG Statistiche SIEP - Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufMod = new UfficioModel();

		ufMod = lCtrlu.getUfficioByKey(UfficioConnesso);
		DescUffIntesta = ufMod.getDescrComune();

		// ===============================================================================================
		// MEV 27 - Prendo i dati della FORM relativi al Periodo per la Ricerca RIEPILOGO_DEFINITI_CPP
		String dataIni = "";
		String dataFin = "";
		String ggmmIni = "";
		String ggmmFin = "";
		String Tipo = "";

		// Recupero dati della maschera - parametri data Inizio e Data Fine
		int annoIni = 0;
		String meseIni = "";
		String giornoIni = "";

		int annoFin = 0;
		String meseFin = "";
		String giornoFin = "";

		if (!isRequestParameterNullObj(CAMPO_GIORNO_INIZIALE)) {
			annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
			meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
			giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

			annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
			meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
			giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);

			dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
			dataFin = giornoFin + "/" + meseFin + "/" + annoFin;
			ggmmIni = giornoIni + meseIni;
			ggmmFin = giornoFin + meseFin;
			Tipo = "datadata";
		}

		// parametri Splo ANNO
		String soloAnnoIni = "";
		String soloAnnoFin = "";

		if (!isRequestParameterNullObj(CAMPO_SOLO_ANNO_INIZIALE)) {
			soloAnnoIni = getRequestStringParameter(CAMPO_SOLO_ANNO_INIZIALE);
			soloAnnoFin = getRequestStringParameter(CAMPO_SOLO_ANNO_FINALE);
			dataIni = "01/01/" + soloAnnoIni;
			dataFin = "31/12/" + soloAnnoFin;
			ggmmIni = "0101";
			ggmmFin = "3112";
			Tipo = "annoanno";
		}

		// ISTANZIA I CONTROLLER
		StatisController lCtrl = new StatisController();
		StatisControllerCPP lCtrlC = new StatisControllerCPP();

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
			Vector lIspModVect = lCtrlC.ExGetCountRiepilogoIspProvvedimenti_CPP(lIspMod);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" RICERCA RIEPILOGO = " + lIspModVect.size());

			// CREAZIONE FOGLIO RIEPILOGO
			lCtrl.ExCreateRiepilogoIspProvvedimenti(lIspModVect, wb, this.getUfficioUtenteConnesso(),
					lUffScelto);
		}
		//
		// =======================================================================================
		// MEV 27 Recupero Dati per Preparare i File di Input dei fogli xls RIEPILOGO_DEFINITI
		// =======================================================================================
		Vector<RiepilogoPendentiDefinitiCPPModel> lRiepilogoArc = new Vector<RiepilogoPendentiDefinitiCPPModel>();

		if (Tipo.compareTo("datadata") == 0) {
			lRiepilogoArc = lCtrlC.ExRicercaRiepilogoDefinitiCPP(annoIni, annoFin, ggmmIni, ggmmFin);
		} else if (Tipo.compareTo("annoanno") == 0) {
			// annoIni = Integer.parseInt(soloAnnoIni);
			// annoFin = Integer.parseInt(soloAnnoFin);
			lRiepilogoArc = lCtrlC.ExRicercaRiepilogoDefinitiCPP(Integer.parseInt(soloAnnoIni),
					Integer.parseInt(soloAnnoFin), ggmmIni, ggmmFin);
		}

		// Creazione e Scrittura DEL FOGLIO 'RIEPILOGO DEFINITI per ANNO', del FILE Excel
		if (Tipo.compareTo("datadata") == 0 || Tipo.compareTo("annoanno") == 0) {
			lCtrlC.ExCreateRiepilogoDefinitiCPP(lRiepilogoArc, wb, this.getUfficioUtenteConnesso(), dataIni,
					dataFin, DescUffIntesta);
		}

		// ==========================================================================
		// Recupero dati da inserire nel Foglio DETTAGLIO (in funzione dei codici selezionati)
		// ==========================================================================
		Vector lStatVect = new Vector();

		// Recupera i dati per creare il Foglio Excel DETTAGLI
		Vector lDetModVect = null;
		if (lCodStatiDett[0] != null) {
			// RICERCA DETTAGLI
			lDetModVect = lCtrlC.ExRicercaDettaglioProcedimenti_CPP(addCodiciTitoli_CPP(lCodStatiDett));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("... RICERCA DETTAGLI = " + lDetModVect.size());

			// CREAZIONE FOGLIO DETTAGLI
			lCtrl.ExCreateDettagliProcedimenti(lDetModVect, wb, this.getUfficioUtenteConnesso(), "Dettagli",
					lUffScelto);
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

			lDetModVect = lCtrlC.ExRicercaDettaglioProcedimenti_CPP(lCodStatiArchConTitolo);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("RICERCA ALTRE POSIZIONI = " + lDetModVect.size());

			// CREAZIONE FOGLIO ALTRE POSIZIONI
			lCtrl.ExCreateDettagliProcedimenti(lDetModVect, wb, this.getUfficioUtenteConnesso(),
					"Altre Posizioni", lUffScelto);
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

			// 07-06-2016 - Riciclo dopo primo collaudo V.10

			// lDetModVect = lCtrlC.ExRicercaDettaglioArchiviazioni_CPP(lCodStatiArchConTitolo);
			lDetModVect = lCtrlC
					.ExRicercaDettaglioArchiviazioni_CPP(lCodStatiArchConTitolo, dataIni, dataFin);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("RICERCA ARCHIVIAZIONI = " + lDetModVect.size());

			// CREAZIONE FOGLIO ARCHIVIAZIONI
			// lCtrlC.ExCreateFoglioDettaglioArchiviazioni_CPP(lDetModVect, wb,
			// this.getUfficioUtenteConnesso(), DescUffIntesta);
			lCtrlC.ExCreateFoglioDettaglioArchiviazioni_CPP(lDetModVect, wb, this.getUfficioUtenteConnesso(),
					dataIni, dataFin, DescUffIntesta);

			// 07-06-2016 - END Riciclo
		}

		// =================================================
		// Aggiunto controllo per rilancio eccezione
		// =================================================
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lStatVect TOT = " + lStatVect.size() + " - lRiepilogoArc TOT = "
				+ lRiepilogoArc.size());
		if (lStatVect.size() == 0 && !bRiepilogo && lRiepilogoArc.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		}

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaRiepilogo_CPP.processRequest: " + ioe);
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
//	private String[] addCodiciTitoli(String[] aCodiciSelezionati) throws F3BException {
//
//		int x = aCodiciSelezionati.length;
//
//		StatisController lStatisCtrl = new StatisController();
//
//		Vector<String> lTitoli1 = null;
//
//		Vector<String> lTitoli2 = null;
//
//		lTitoli1 = lStatisCtrl.getTitoliPerCodici(aCodiciSelezionati, "T1");
//		lTitoli2 = lStatisCtrl.getTitoliPerCodici(aCodiciSelezionati, "T2");
//
//		String[] lCodStatiDettConTitoli = new String[x + lTitoli1.size() + lTitoli2.size()];
//
//		int lPosCorrente = 0;
//
//		// Riverso i codici
//		for (int i = 0; i < aCodiciSelezionati.length; i++) {
//			lCodStatiDettConTitoli[lPosCorrente] = aCodiciSelezionati[i];
//			lPosCorrente++;
//		}
//
//		// Aggiungo i T1
//		for (int i = 0; i < lTitoli1.size(); i++) {
//			lCodStatiDettConTitoli[lPosCorrente] = lTitoli1.elementAt(i);
//			lPosCorrente++;
//		}
//
//		// Aggiungo i T2
//		for (int i = 0; i < lTitoli2.size(); i++) {
//			lCodStatiDettConTitoli[lPosCorrente] = lTitoli2.elementAt(i);
//			lPosCorrente++;
//		}
//
//		return lCodStatiDettConTitoli;
//	}

	private String[] addCodiciTitoli_CPP(String[] aCodiciSelezionati) throws F3BException {

		int x = aCodiciSelezionati.length;

		StatisControllerCPP lStatisCtrl = new StatisControllerCPP();

		Vector<String> lTitoli1 = null;

		Vector<String> lTitoli2 = null;

		lTitoli1 = lStatisCtrl.getTitoliPerCodici_CPP(aCodiciSelezionati, "T1");
		lTitoli2 = lStatisCtrl.getTitoliPerCodici_CPP(aCodiciSelezionati, "T2");

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

} // CHIUDE Classe