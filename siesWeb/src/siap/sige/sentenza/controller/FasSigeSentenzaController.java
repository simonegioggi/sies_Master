package siap.sige.sentenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.beneficio.dao.BeneficioDAO;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.circostanza.dao.CircostanzaDAO;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.dao.PenaAccessoriaDAO;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.beneficio.dao.BeneficioSenSigeDAO;
import siap.sige.beneficio.model.BeneficioSigeModel;
import siap.sige.circostanza.dao.CircostanzaSenSigeDAO;
import siap.sige.circostanza.model.CircostanzaSigeModel;
import siap.sige.penaaccessoria.dao.PenaAccSenSigeDAO;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import siap.sige.penacomplessiva.dao.PenaCompSenSigeDAO;
import siap.sige.penacomplessiva.model.PenaCompSigeModel;
import siap.sige.reato.dao.ReatoSentenzaSigeDAO;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.dao.FasSigeSentenzaDAO;
import siap.sige.sentenza.dao.FasSigeSentenzaSqlDAO;
import siap.sige.sentenza.model.FasSigeSentenzaModel;
import siap.sige.sentenza.model.FasSigeSentenzaRicercaModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.tenore.dao.TenoreSentenzaReatoDAO;

/**
 * <p>
 * Title: FasSigeSentenzaController
 * </p>
 * <p>
 * Description: Classe Controller per FasSigeSentenza.
 * </p>
 * Questo controller fornisce le funzioni per l'accesso ai Titoli esecutivi (sentenze) associati ai Fascicoli
 * SIGE. Tali funzioni accedono alla tabella FAS_SIGE_SENTENZA ed alla tabella SENTENZA.
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FasSigeSentenzaController extends SiapController implements IFasSigeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public SentenzaSigeModel ExInserisciFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza)
			throws F3BException {

		Connection lConn = null;
		FasSigeSentenzaDAO lFasDao = null;
		SentenzaSigeModel lFasMod = null;

		try {
			lConn = getDBConnection();
			lFasMod = new SentenzaSigeModel(aFasSigeSentenza);
			lFasDao = new FasSigeSentenzaDAO(lConn);
			lFasDao.setDAOFromModel(aFasSigeSentenza);
			BigDecimal lKey = null;
			lKey = lFasDao.insert();
			commit(lConn);
			lFasMod.setIdFasSigeSentenza(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FasSigeSentenzaController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * L'assegnazione di una sentenza ad un Fascicolo SIGE viene effettuata inserendo un record nella tabella
	 * FAS_SIGE_SEN. Se esiste anche il legame ad un Fascicolo SIEP vengono associati alla Sentenza-Sige anche
	 * i Reati legati al Fascicolo SIEP:
	 *
	 * @param SentenzaSigeModel
	 * @return SentenzaSigeModel
	 * @throws F3BException
	 */
	public SentenzaSigeModel ExAssegnaSentenzaFascicoloSige(SentenzaSigeModel aFasSigeSentenza)
			throws F3BException {

		Connection lConn = null;
		SentenzaSigeModel lFasMod = null;

		try {
			lConn = getDBConnection();
			lFasMod = assegnaSentenzaFascicoloSige(aFasSigeSentenza, lConn);

			// Se presente il legame al Fascicolo SIEP vengono associati anche i reati
			if (lFasMod.getFasSieIdFascicoloSiep() != null)
				assegnaReatiSiep(lFasMod.getFasSieIdFascicoloSiep(), lFasMod.getIdFasSigeSentenza(), lConn);

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaFascicoloSige : " + e);
		} finally {
			cleanup(lConn);
		}
		return lFasMod;
	}

	/**
	 * Funzione di De-Assegnazione di una Sentenza da un Fascicolo SIGE. La funzione effettua prima un
	 * controllo per assicurarsi che la Sentenza non sia già stata inserita nella definizione di un Oggetto
	 * per lo stesso Fascicolo SIGE. Nel caso il controllo dia esito positivo viene segnalato tramite una
	 * Eccezione che la De-Assegnazione non può essere effettuata. Nel caso contrario si prosegue nella
	 * De-Assegnazione. La De-Assegnazione viene effettuata cancellando nelle tabelle di relazione
	 * PENA_COMPLESSIVA_SENTENZA_SIGE, PENA_ACCESSORIA_SENTENZA_SIGE e REATO_SENTENZA_SIGE i riferimenti al
	 * record nella tabella FAS_SIGE_SENTENZA che esprime l'assegnazione e che viene infine cancellato.
	 *
	 * @param aIdFasSigeSentenza
	 * @throws F3BException
	 */
	public void ExDeAssegnaSentenzaFascicoloSige(BigDecimal aIdFasSigeSentenza) throws F3BException {

		Connection lConn = null;
		FasSigeSentenzaDAO lFasDao = null;
		TenoreSentenzaReatoDAO lTenSenReaDAO = null;
		PenaCompSenSigeDAO lPenComSenSigeDAO = null;
		PenaAccSenSigeDAO lPenaAccSigeDAO = null;
		ReatoSentenzaSigeDAO lReaSenDAO = null;
		CircostanzaSenSigeDAO lCircostanzaSigeDAO = null;
		BeneficioSenSigeDAO lBeneficioSigeDAO = null;

		SentenzaSigeModel lSentSige = null;

		try {
			// Inizializzazione
			lConn = getDBConnection();
			lFasDao = new FasSigeSentenzaDAO(lConn);

			// Ricerca del record da cancellare per risalire all'ID Sentenza
			lFasDao.setCondizioneUpdate(aIdFasSigeSentenza);
			lSentSige = (SentenzaSigeModel) lFasDao.getModelByKey();
			if (lSentSige == null || lSentSige.getIdSentenza() == null
					|| lSentSige.getFasIdFascicoloSige() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Errore nella lettura dei dati ! ");
			lFasDao.stop();

			// Ricerca di Oggetti SIGE legati alla sentenza da De-Assegnare
			lTenSenReaDAO = new TenoreSentenzaReatoDAO(lConn);
			lTenSenReaDAO.selCondizioneSentenzaFascicolo(lSentSige.getIdSentenza(),
					lSentSige.getFasIdFascicoloSige());
			if (lTenSenReaDAO.getModelByKey() != null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibile De-Assegnare la Sentenza perchè già selezionata nella definizione di un oggetto.");

			// Cancellazione dei riferimenti nella tabella PENA_COMPLESSIVA_SENTENZA_SIGE
			lPenComSenSigeDAO = new PenaCompSenSigeDAO(lConn);
			lPenComSenSigeDAO.setCondizioneIdFasSigeSen(aIdFasSigeSentenza);
			lPenComSenSigeDAO.delete();

			// Cancellazione dei riferimenti nella tabella PENA_ACCESSORIA_SENTENZA_SIGE
			lPenaAccSigeDAO = new PenaAccSenSigeDAO(lConn);
			lPenaAccSigeDAO.setCondizioneIdFasSigeSen(aIdFasSigeSentenza);
			lPenaAccSigeDAO.delete();

			// Cancellazione dei riferimenti nella tabella REATO_SENTENZA_SIGE
			lReaSenDAO = new ReatoSentenzaSigeDAO(lConn);
			lReaSenDAO.setCondizioneIdFasSigeSen(aIdFasSigeSentenza);
			lReaSenDAO.delete();

			// Cancellazione dei riferimenti nella tabella CIRCOSTANZA_SENTENZA_SIGE
			lCircostanzaSigeDAO = new CircostanzaSenSigeDAO(lConn);
			lCircostanzaSigeDAO.setCondizioneIdFasSigeSen(aIdFasSigeSentenza);
			lCircostanzaSigeDAO.delete();

			// Cancellazione dei riferimenti nella tabella BENEFICIO_SENTENZA_SIGE
			lBeneficioSigeDAO = new BeneficioSenSigeDAO(lConn);
			lBeneficioSigeDAO.setCondizioneIdFasSigeSen(aIdFasSigeSentenza);
			lBeneficioSigeDAO.delete();

			// Cancellazione del record in FAS_SIGE_SENTENZA
			lFasDao.setCondizioneUpdate(aIdFasSigeSentenza);
			lFasDao.delete();
			lFasDao.stop();

			commit(lConn);
		} catch (F3BException fe) {
			rollback(lConn);
			throw fe;
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("FasSigeSentenzaController.ExDeAssegnaSentenzaFascicoloSige : " + e);
		} finally {
			cleanup(lFasDao);
			cleanup(lTenSenReaDAO);
			cleanup(lPenComSenSigeDAO);
			cleanup(lPenaAccSigeDAO);
			cleanup(lReaSenDAO);
			cleanup(lCircostanzaSigeDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lBeneficioSigeDAO);

			cleanup(lConn);
		}
	}

	/**
	 * Funzione che implementa l'inserimento nella tabella FAS_SIGE_SENTENZA del record che esprime
	 * l'associazione tra Fascicolo SIGE e Sentenza. Questa funzione utilizza una connessione passata come
	 * parametro per poter operare in una transazione unica gestita dalla funzione chiamante. aperta e chiusa
	 *
	 * @param aFasSigeSentenza
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	private SentenzaSigeModel assegnaSentenzaFascicoloSige(SentenzaSigeModel aFasSigeSentenza,
			Connection lConn) throws F3BException {

		FasSigeSentenzaDAO lFasDao = null;
		SentenzaSigeModel lFasMod = null;

		try {
			lFasMod = new SentenzaSigeModel(aFasSigeSentenza);
			lFasDao = new FasSigeSentenzaDAO(lConn);
			if (lFasMod.getFlagCompetenza() != null && lFasMod.getFlagCompetenza().equalsIgnoreCase("S")) {
				// Annullamento della competenza di un altro titolo già in tabella
				lFasDao.annullaCompetenzaXFascicolo(lFasMod);
				lFasDao.stop();
			}

			lFasDao.setDAOFromModelForInsert(lFasMod);
			BigDecimal lKey = null;
			lKey = lFasDao.insert();
			lFasMod.setIdFasSigeSentenza(lKey);
		} catch (DAOException ex) {
			if (ex.INTEGRITY_CONSTRAINT_VIOLATED || ex.UNIQUE_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore: La Sentenza risulta già assegnata al Procedimento !");
			else
				throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaFascicoloSige:  " + ex);
		} catch (Exception e) {
			throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaFascicoloSige : " + e);
		} finally {
			cleanup(lFasDao);
		}
		return lFasMod;
	}

	/**
	 * La funzione effettua ricerca nella sola tabella FAS_SIGE_SENTENZA.
	 */
	public Vector ExRicercaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza) throws F3BException {

		Connection lConn = null;
		Vector lFasSigeSentenze = new Vector();
		FasSigeSentenzaDAO lFasSenDao = null;

		try {
			lConn = getDBConnection();
			lFasSenDao = new FasSigeSentenzaDAO(lConn);
			lFasSenDao.setCondizione(aFasSigeSentenza);
			lFasSigeSentenze = new Vector(lFasSenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFasSigeSentenza: " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFasSigeSentenza -> " + sqe);
		} finally {
			cleanup(lFasSenDao);
			cleanup(lConn);
		}
		return lFasSigeSentenze;
	}

	/**
	 * la funzione ricerca tutte le sentenze associate ad un FASCICOLO_SIGE. L'argomento di ingresso della
	 * funzione è l'ID del Fascicolo SIGE. La ricerca viene effettuata richiamando la funzione
	 * ExRicercaSentenzeSige(...).
	 *
	 */
	public Vector ExRicercaSentenzeAssegnateFascicolo(BigDecimal aIdFascicoloSige) throws F3BException {

		SentenzaSigeModel lFasSigeSentenza = new SentenzaSigeModel();

		Vector<FasSigeSentenzaModel> lSentenze = new Vector<>();

		// Valorizzazione del model da passare alla funzione di ricerca
		lFasSigeSentenza.setFasIdFascicoloSige(aIdFascicoloSige);

		// Ricerca delle Sentenze
		lSentenze = ExRicercaSentenzeSige(lFasSigeSentenza);

		return lSentenze;
	}

	/**
	 * Funzione di ricerca delle Sentenze SIGE. In base alle condizioni codificate nel SentenzaSigeModel
	 * passato come argomento, la funzione effettua una ricerca nella tabella FAS_SIGE_SENTENZA. Per ognuno
	 * dei record trovati che rappresenta l'aggregato FASCICOLO_SIGE - SENTENZA, viene poi ricercata la
	 * sentenza nella tabella SENTENZA. Infine il risultato viene restituito come Vector di SentenzaSigeModel.
	 *
	 * @param aFasSigeSentenza
	 * @return Vector
	 * @throws F3BException
	 */

	public Vector ExRicercaSentenzeSige(SentenzaSigeModel aFasSigeSentenza) throws F3BException {

		Vector lFasSentenze = null;
		Vector lSentenze = new Vector();
		Connection lConn = null;
		SentenzaSqlDAO lSenDao = null;
		SentenzaModel lSen = null;
		SentenzaSigeModel lSenSige = null;
		// Controller per ricerca Fascicolo SIEP
		IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();

		// Ricerca dei riferimenti a Sentenze nella tabella di relazione FAS_SIGE_SENTENZA
		lFasSentenze = ExRicercaFasSigeSentenza(aFasSigeSentenza);

		if (lFasSentenze != null) {
			try {
				lConn = getDBConnection();
				lSenDao = new SentenzaSqlDAO(lConn);

				// Si itera sull'elenco dei FasSigeSentenzaModel per trovare le Sentenze
				Iterator itx = lFasSentenze.iterator();
				while (itx.hasNext()) {
					// Ricerca Sentenza a partire dal suo ID
					SentenzaSigeModel lFasSigeSen = (SentenzaSigeModel) itx.next();
					lSenDao.ricercaSentenzaBykey(lFasSigeSen.getIdSentenza());
					lSen = new SentenzaModel((SentenzaModel) lSenDao.getModelByKey());

					// Ricerca del Fascicolo SIEP
					if (lFasSigeSen.getFasSieIdFascicoloSiep() != null)
						lFasSigeSen.setFascicoloSiep(lCtrlSIEP
								.ExRicercaFascicoloByKeyNoError(lFasSigeSen.getFasSieIdFascicoloSiep()));

					// Costruzione del SentenzaSigeModel
					lSenSige = new SentenzaSigeModel(lFasSigeSen, lSen);

					lSentenze.add(lSenSige);
				}
			} catch (Exception e) {
				throw new F3BException("FasSigeSentenzaController.ExRicercaSentenzeSige Exception: " + e);
			} finally {
				cleanup(lSenDao);
				cleanup(lConn);
			}
		}
		return lSentenze;
	}

	/**
	 * Funzione di ricerca delle Sentenze SIGE. In base alle condizioni codificate nel SentenzaSigeModel
	 * passato come argomento, la funzione effettua una ricerca nella tabella FAS_SIGE_SENTENZA. Per ognuno
	 * dei record trovati che rappresenta l'aggregato FASCICOLO_SIGE - SENTENZA, viene poi ricercata la
	 * sentenza nella tabella SENTENZA. Infine il risultato viene restituito come Vector di SentenzaSigeModel.
	 *
	 * @param aFasSigeSentenza
	 * @return Vector
	 * @throws F3BException
	 */

	public Vector ExRicercaProcedimentoSiepDiCumulo(BigDecimal aIdFascicoloSiep) throws F3BException {

		// Vector lFasSentenze = null;
		Vector lSentenze = new Vector();
		Connection lConn = null;
		SentenzaSqlDAO lSenDao = null;
		SentenzaModel sentenza = null;
		// SentenzaModel lSenSentenze = null;
		// Controller per ricerca Fascicolo SIEP
		// IFascicoloSiep lCtrlSIEP = SIEPLookupRemote.getFascicoloSiepRemote();

		ICumulo lCtrCum = SIEPLookupRemote.getCumuloRemote();
		CumuloModel aModelCum = new CumuloModel();
		aModelCum.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
		Vector VCumuli = lCtrCum.ricercaCumuloSentenzaFascicoloSige(aModelCum);

		if (VCumuli != null) {
			try {
				lConn = getDBConnection();
				lSenDao = new SentenzaSqlDAO(lConn);

				Iterator itx = VCumuli.iterator();
				while (itx.hasNext()) {
					CumuloModel lCumulo = (CumuloModel) itx.next();
					sentenza = lCumulo.getSentenza();
					lSentenze.add(sentenza);
				}
			} catch (Exception e) {
				throw new F3BException("FasSigeSentenzaController.ExRicercaSentenzeSige Exception: " + e);
			} finally {
				cleanup(lSenDao);
				cleanup(lConn);
			}
		}
		return lSentenze;
	}

	/**
	 * La funzione effettua la ricerca di una sentenza a partire dall'ID_FAS_SIGE_SENTENZA.
	 *
	 */
	public SentenzaSigeModel ExRicercaFasSigeSentenzaByKey(BigDecimal aKey) throws F3BException {

		SentenzaSigeModel lFasMod = null;
		Vector lRisultatiRicerca = null;

		lFasMod = new SentenzaSigeModel();
		lFasMod.setIdFasSigeSentenza(aKey);
		lRisultatiRicerca = ExRicercaSentenzeSige(lFasMod);
		if (lRisultatiRicerca != null && lRisultatiRicerca.size() > 0) {
			lFasMod = (SentenzaSigeModel) lRisultatiRicerca.get(0);
		} else
			throw new F3BException(F3BException.EX_NOT_FOUND, "Titolo Esecutivo non trovato !");

		return lFasMod;
	}

	/**
	 * Funzione di Modifica.
	 *
	 * @param aFasSigeSentenza
	 * @return
	 * @throws F3BException
	 */
	public SentenzaSigeModel ExModificaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza)
			throws F3BException {

		Connection lConn = null;
		FasSigeSentenzaDAO lFasDao = null;
		SentenzaSigeModel lFasMod = new SentenzaSigeModel(aFasSigeSentenza);

		try {
			lConn = getDBConnection();
			lFasDao = new FasSigeSentenzaDAO(lConn);

			if (lFasMod.getFlagCompetenza() != null && lFasMod.getFlagCompetenza().equalsIgnoreCase("S")) {
				// Annullamento della competenza di un altro titolo già in tabella
				lFasDao.annullaCompetenzaXFascicolo(lFasMod);
				lFasDao.stop();
			}

			lFasDao.setDAOFromModelForUpdate(lFasMod);
			lFasDao.setCondizioneUpdate(lFasMod.getIdFasSigeSentenza());
			lFasDao.update();
			lFasDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("FasSigeSentenzaController.ExModifica: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("FasSigeSentenzaController.ExModificaFasSigeSentenza-> " + sqe);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public void ExCancellaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza) throws F3BException {

		Connection lConn = null;
		FasSigeSentenzaDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FasSigeSentenzaDAO(lConn);
			lFasDao.setCondizioneUpdate(aFasSigeSentenza.getIdFasSigeSentenza());
			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"FasSigeSentenzaController.ExCancellaFasSigeSentenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	/**
	 * La funzione assegna ad un Procedimento SIGE la Sentenza ed i Reati collegati ad un Fascicolo SIEP.
	 *
	 * @param aFasSigeSentenza
	 * @param aIdFasSiep
	 * @param aConn
	 * @return
	 * @throws F3BException
	 */
	public SentenzaSigeModel ExAssegnaSentenzaReatiFasSiep(SentenzaSigeModel aFasSigeSentenza,
			Connection aConn) throws F3BException {

		// Model relativo alla relazione FascicoloSige-Sentenza
		SentenzaSigeModel lFasSen = null;
		// Fascicolo SIEP
		FascicoloSiepModel lFascicolo = null;
		try {

			// Viene istanziato SentenzaSigeModel ed inizializzato con i dati passati
			lFasSen = new SentenzaSigeModel(aFasSigeSentenza);

			// Ricerca del Fascicolo SIEP
			lFascicolo = getFascicoloSIEP(lFasSen.getFasSieIdFascicoloSiep(), aConn);

			if (lFascicolo != null && lFascicolo.getSenIdSentenza() != null) {
				// Viene assegnata la Sentenza legata al Fascicolo SIEP anche al Fascicolo SIGE
				lFasSen.setIdSentenza(lFascicolo.getSenIdSentenza());
				// Data di irrevocabilita' è quella dal Fascicolo SIEP
				lFasSen.setDataIrrevocabilita(lFascicolo.getDataIrrevocabilita());
				// Viene creata la relazione Sentenza - Fascicolo SIGE
				lFasSen = assegnaSentenzaFascicoloSige(lFasSen, aConn);
				// Vengono assegnati anche i Reati SIEP alla Sentenza - Fascicolo SIGE
				// @emma 20072018 intervento post COLLAUDO 11.2
				if (lFasSen.getFasSieIdFascicoloSiep() != null) {
					assegnaReatiSiep(lFasSen.getFasSieIdFascicoloSiep(), lFasSen.getIdFasSigeSentenza(),
							aConn);
				}
			} else
				throw new F3BException("Errore: Fascicolo SIEP non risulta associato ad una sentenza !");
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("FasSigeSentenzaController.ExAssegnaSentenzaXFasSiep : " + e);
		}
		return lFasSen;
	}

	/**
	 * Funzione per la ricerca Fascicolo SIEP. Alla funzione viene passata anche la connessione per poterla
	 * utilizzare in una unica transazione.
	 *
	 * @param aIdFasSiep
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	private FascicoloSiepModel getFascicoloSIEP(BigDecimal aIdFasSiep, Connection lConn) throws F3BException {

		FascicoloSiepModel lFascicolo = null;
		FascicoloSiepDAO lFascDao = null;

		try {
			lFascDao = new FascicoloSiepDAO(lConn);

			// Cerca il fascicolo SIEP by key
			lFascDao.selCondizioneUpdate(aIdFasSiep);
			lFascicolo = (FascicoloSiepModel) lFascDao.getModelByKey();

			if (lFascicolo == null)
				throw new F3BException("Errore nella ricerca del Fascicolo SIEP !");
			lFascicolo.getDataIrrevocabilita();
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca della sentenza del Fascicolo SIEP : " + e);
		} finally {
			cleanup(lFascDao);
		}

		return lFascicolo;
	}

	/**
	 * Effettua una ricerca di tutti i Reati legati allo stesso Fascicolo SIEP.
	 *
	 * @param aIdFasSiep
	 * @param lConn
	 * @return ArrayList
	 * @throws F3BException
	 */
	private ArrayList getReatiFascicoloSIEP(BigDecimal aIdFasSiep, Connection lConn) throws F3BException {

		// DAO di accesso alla tabella REATO
		ReatoDAO lReatoDao = null;
		// Elenco dei Reati risultato della ricerca
		ArrayList lReati = null;

		try {
			lReatoDao = new ReatoDAO(lConn);

			// Cerca tutti i reati legati al Fascicolo SIEP
			lReatoDao.setCondizioneIdSiep(aIdFasSiep);
			lReati = new ArrayList(lReatoDao.getModels());
		} catch (Exception e) {
			throw new F3BException("Errore nella ricerca della sentenza del Fascicolo SIEP : " + e);
		} finally {
			cleanup(lReatoDao);
		}

		return lReati;
	}

	/**
	 * La Funzione assegna i reati, la pena complessiva e le pene accessorie legati ad un Fascicolo SIEP ad
	 * una sentenza legata ad un Fascicolo SIGE.
	 *
	 * @param aIdFascicoloSiep
	 *            : id del Fascicolo SIEP
	 * @param aIdFasSigeSentenza
	 *            : id della Sentenza - Fascicolo Sige
	 * @param aConn
	 *            : connessione
	 * @throws F3BException
	 */
	private void assegnaReatiSiep(BigDecimal aIdFascicoloSiep, BigDecimal aIdFasSigeSentenza,
			Connection aConn) throws F3BException {

		// Elenco di Reati collegati al Fascicolo SIEP
		ArrayList lReati = null;
		// Reato corrente
		ReatoModel lReato = null;
		// DAO relativo a tabella di relazione REATO_SENTENZA_SIGE
		ReatoSentenzaSigeDAO lReatoSigeDao = null;
		// Relazione FascicoloSige-Sentenza - Reato
		ReatoSentenzaSigeModel lReatoSige = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		BeneficioSenSigeDAO lBeneficioSigeDAO = null;
		CircostanzaSenSigeDAO lCircostanzaSigeDAO = null;
		PenaAccSenSigeDAO lPenaAccSigeDAO = null;
		PenaCompSenSigeDAO lPenaCompSigeDAO = null;

		try {
			// Ricerca reati
			lReati = getReatiFascicoloSIEP(aIdFascicoloSiep, aConn);

			// Assegnazione reati
			if (lReati != null && lReati.size() > 0) {
				lReatoSigeDao = new ReatoSentenzaSigeDAO(aConn);

				for (int i = 0; i < lReati.size(); i++) {
					// Lettura i-esimo reato per ricavare il suo ID
					lReato = new ReatoModel((ReatoModel) lReati.get(i));

					// STUB: qua ci vogliono data di inserimento etc...
					// Inserimento record di relazione in REATO_SENTENZA_SIGE
					lReatoSige = new ReatoSentenzaSigeModel(lReato.getIdReato(), aIdFasSigeSentenza);
					lReatoSigeDao.setDAOFromModel(lReatoSige);
					lReatoSigeDao.insert();
					lReatoSigeDao.stop();
				}
			}

			// Ricerca Pena Complessiva
			IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaModel lPenMod = ICtrlPenCom
					.ExRicercaPenaComplessivaByIdFascicolo(aIdFascicoloSiep);

			if (lPenMod != null) {
				// Inserimento record di relazione in PENA_COMPLESSIVA_SENTENZA_SIGE
				PenaCompSigeModel lPenaCompSige = new PenaCompSigeModel(lPenMod.getIdPenaComplessiva(),
						aIdFasSigeSentenza);
				lPenaCompSigeDAO = new PenaCompSenSigeDAO(aConn);
				lPenaCompSigeDAO.setDAOFromModel(lPenaCompSige);
				lPenaCompSigeDAO.insert();
				lPenaCompSigeDAO.stop();
			}

			// Ricerca Pene Accessorie legate al Fascicolo SIEP
			PenaAccessoriaDAO lPenDao = new PenaAccessoriaDAO(aConn);
			lPenDao.selCondizioneFascSiep(aIdFascicoloSiep);
			Vector lPeneAccessorie = new Vector(lPenDao.getModels());

			if (lPeneAccessorie != null && lPeneAccessorie.size() > 0) {
				// Pena Accessoria corrente
				PenaAccessoriaModel lPenAccMod = null;

				// Inserimento record di relazione in PENA_ACCESSORIA_SENTENZA_SIGE
				lPenaAccSigeDAO = new PenaAccSenSigeDAO(aConn);
				PenaAccSigeModel lPenaAccSige = null;

				Iterator itx = lPeneAccessorie.iterator();
				while (itx.hasNext()) {
					lPenAccMod = (PenaAccessoriaModel) itx.next();
					lPenaAccSige = new PenaAccSigeModel(lPenAccMod.getIdPenaAccessoria(), aIdFasSigeSentenza);
					lPenaAccSigeDAO.setDAOFromModel(lPenaAccSige);
					lPenaAccSigeDAO.insert();
					lPenaAccSigeDAO.stop();
				}
			}

			// Circostanze
			// Ricerca Circostanze legate al Fascicolo SIEP
			CircostanzaDAO lCirDao = new CircostanzaDAO(aConn);
			lCirDao.setCondizioneUpdateFascicolo(aIdFascicoloSiep);
			Vector lCircostanze = new Vector(lCirDao.getModels());

			if (lCircostanze != null && lCircostanze.size() > 0) {
				// Circostanza corrente
				CircostanzaModel lCircostanza = null;

				// Inserimento record di relazione in CIRCOSTANZA_SENTENZA_SIGE
				lCircostanzaSigeDAO = new CircostanzaSenSigeDAO(aConn);
				CircostanzaSigeModel lCircostanzaSige = null;

				Iterator itx = lCircostanze.iterator();
				while (itx.hasNext()) {
					lCircostanza = (CircostanzaModel) itx.next();
					lCircostanzaSige = new CircostanzaSigeModel(lCircostanza.getIdCircostanza(),
							aIdFasSigeSentenza);
					lCircostanzaSigeDAO.setDAOFromModel(lCircostanzaSige);
					lCircostanzaSigeDAO.insert();
					lCircostanzaSigeDAO.stop();
				}
			}

			// Benefici
			// Ricerca Benefici legate al Fascicolo SIEP
			BeneficioDAO lBenDao = new BeneficioDAO(aConn);
			lBenDao.setCondizioneFascSiep(aIdFascicoloSiep);
			Vector lBenefici = new Vector(lBenDao.getModels());

			if (lBenefici != null && lBenefici.size() > 0) {
				// Beneficio corrente
				BeneficioModel lBeneficio = null;

				// Inserimento record di relazione in BENEFICIO_SENTENZA_SIGE
				lBeneficioSigeDAO = new BeneficioSenSigeDAO(aConn);
				BeneficioSigeModel lBeneficioSige = null;

				Iterator itx = lBenefici.iterator();
				while (itx.hasNext()) {
					lBeneficio = (BeneficioModel) itx.next();
					lBeneficioSige = new BeneficioSigeModel(lBeneficio.getIdBeneficio(), aIdFasSigeSentenza);
					lBeneficioSigeDAO.setDAOFromModel(lBeneficioSige);
					lBeneficioSigeDAO.insert();
					lBeneficioSigeDAO.stop();
				}
			}
		} catch (F3BException fe) {
			throw fe;
		} catch (Exception e) {
			throw new F3BException("FasSigeSentenzaController.assegnaReatiSiep : " + e);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lReatoSigeDao);
			cleanup(lPenaCompSigeDAO);
			cleanup(lPenaAccSigeDAO);
			cleanup(lCircostanzaSigeDAO);
			cleanup(lBeneficioSigeDAO);
		}
	}

	/**
	 * Ricerca la Sentenze riferite da Fascicoli SIGE.
	 *
	 * @param aSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSentenzePerSIGEPaged(SentenzaModel aSentenza, int aPage, String majorOffice)
			throws F3BException {

		// 20181031: aggiunto parametro di passaggio
		Connection lConn = null;
		Vector lSentenzeSige;
		FasSigeSentenzaSqlDAO lFSSSqlDao = null;
		SentenzaSqlDAO lSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lFSSSqlDao = new FasSigeSentenzaSqlDAO(lConn);

			// 20181031: aggiunto parametro di passaggio
			lFSSSqlDao.ricercaSentenzePerSIGEPaged(aSentenza, aPage, majorOffice);
			lSentenzeSige = new Vector(lFSSSqlDao.getModels());

			if (lSentenzeSige.isEmpty())
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			else {
				// Caricamento SentenzeModel.
				lSenSqlDao = new SentenzaSqlDAO(lConn);
				for (int i = 0; i < lSentenzeSige.size(); i++) {
					FasSigeSentenzaRicercaModel lFasSigeSentenza = (FasSigeSentenzaRicercaModel) lSentenzeSige
							.get(i);
					lSenSqlDao.ricercaSentenzaBykey(
							lFasSigeSentenza.getFasSigeSentenzaModel().getSenIdSentenza());
					SentenzaModel lSentenzaModel = (SentenzaModel) lSenSqlDao.getModelByKey();
					lFasSigeSentenza.setSentenzaModel(lSentenzaModel);
					lSentenzeSige.setElementAt(lFasSigeSentenza, i);
				}
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FasSigeSentenzaController.ExRicercaSentenzePerSIGEPaged: " + daoEx);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lSenSqlDao);
			cleanup(lFSSSqlDao);
			cleanup(lConn);
		}

		return lSentenzeSige;
	}

	public BigDecimal ExGetCountFasSigeSentenze(SentenzaModel aSentenza) throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		// SentenzaSqlDAO lSenSqlDao = null;
		FasSigeSentenzaSqlDAO lFSSSqlDao = null;
		try {
			lConn = getDBConnection();
			lFSSSqlDao = new FasSigeSentenzaSqlDAO(lConn);
			lFSSSqlDao.getCountFasSigeSentenze(aSentenza);
			lFSSSqlDao.start();
			lFSSSqlDao.next();
			lCount = lFSSSqlDao.getBigDecimal("HowManyRecords");
			lFSSSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new F3BException(F3BException.USER_MESSAGE,
					"FasSigeSentenzaController.ExGetCountFasSigeSentenze: " + daoEx);
		} finally {
			cleanup(lFSSSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * 20/01/2010 La funzione effettua ricerca nella tabella FAS_SIGE_SENTENZA.
	 */
	public Vector ExRicercaFSSentenza(FasSigeSentenzaModel aFasSigeSentenza) throws F3BException {

		Connection lConn = null;
		Vector lFasSigeSentenze = new Vector();
		FasSigeSentenzaDAO lFasSenDao = null;

		try {
			lConn = getDBConnection();
			lFasSenDao = new FasSigeSentenzaDAO(lConn);
			lFasSenDao.setCondizione(aFasSigeSentenza);
			lFasSigeSentenze = new Vector(lFasSenDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFSSentenza: " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFSSentenza -> " + sqe);
		} finally {
			cleanup(lFasSenDao);
			cleanup(lConn);
		}
		return lFasSigeSentenze;
	}

	/**
	 * La funzione verifica se la Sentenza passata come parametro è già associata al Fascicolo Sige passato
	 * come argomento
	 */
	public Vector ExRicercaFascicoloSigeSentenzaAssociata(BigDecimal aSenIdSentenza,
			BigDecimal aIdFascicoloSige) throws F3BException {

		Connection lConn = null;

		Vector<FasSigeSentenzaModel> lSentenze = new Vector<>();

		// SentenzaSqlDAO lSenSqlDao = null;
		FasSigeSentenzaSqlDAO lFSSSqlDao = null;
		SentenzaModel aSenModel = new SentenzaModel();
		try {
			lConn = getDBConnection();

			lFSSSqlDao = new FasSigeSentenzaSqlDAO(lConn);
			aSenModel.setIdSentenza(aSenIdSentenza);
			lFSSSqlDao.ricercaFasSigeSentenzaPerIdSentenza(aSenModel, aIdFascicoloSige);
			lFSSSqlDao.start();
			while (lFSSSqlDao.next()) {
				FasSigeSentenzaModel fasSigeSentenza = (FasSigeSentenzaModel) lFSSSqlDao
						.getFasSigeSentenzaPerIdSentenza();
				lSentenze.addElement(fasSigeSentenza);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFasSigeSentenza: " + daoEx);
		} catch (Exception sqe) {
			throw new F3BException("FasSigeSentenzaController.ExRicercaFasSigeSentenza -> " + sqe);
		} finally {
			cleanup(lFSSSqlDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lConn);
		}
		return lSentenze;
	}

}