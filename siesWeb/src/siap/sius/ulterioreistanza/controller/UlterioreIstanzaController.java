package siap.sius.ulterioreistanza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sius.tenore.dao.TenoreDAO;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.ulterioreistanza.dao.UlterioreIstanzaDAO;
import siap.sius.ulterioreistanza.dao.UlterioreIstanzaSqlDAO;
import siap.sius.ulterioreistanza.model.UlterioreIstanzaModel;
import siap.sius.ulterioreistanzatenore.dao.UlterioreIstanzaTenoreDAO;
import siap.sius.ulterioreistanzatenore.dao.UlterioreIstanzaTenoreSqlDAO;
import siap.sius.ulterioreistanzatenore.model.UlterioreIstanzaTenoreModel;

/**
 * <p>
 * Title: UlterioreIstanzaController
 * </p>
 * <p>
 * Description: Classe Controller per UlterioreIstanza
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
public class UlterioreIstanzaController extends SiapController implements IUlterioreIstanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Il metodo esegue l'inserimento di una ulteriore istanza, per un determinato procedimento SIUS
	 *
	 * @param aUlterioreIstanza
	 *            UlterioreIstanzaModel model con i parametri per l'inserimento.
	 * @return UlterioreIstanzaModel Ritorna il model con i dati appena inseriti, in più l'id del record
	 *         assegnato dal sistema.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public UlterioreIstanzaModel ExInserisciUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza,
			TenoreModel[] aTenori) throws F3BException {

		Connection lConn = null;
		UlterioreIstanzaDAO lUltDao = null;
		UlterioreIstanzaTenoreDAO lUltIstTenDao = null;
		TenoreSqlDAO lTenSqlDao = null;
		TenoreDAO lTenDao = null;

		UlterioreIstanzaModel lUltMod = null;

		try {
			lConn = getDBConnection(); // Preleva connessione dal Dbase.

			lUltMod = new UlterioreIstanzaModel(aUlterioreIstanza);
			lUltDao = new UlterioreIstanzaDAO(lConn);
			lUltIstTenDao = new UlterioreIstanzaTenoreDAO(lConn);
			lTenSqlDao = new TenoreSqlDAO(lConn);
			lTenDao = new TenoreDAO(lConn);

			// Imposta il DAO con i dati prelevati dal Model
			lUltDao.setDAOFromModel(aUlterioreIstanza);
			BigDecimal lKey = null;
			lKey = lUltDao.insert();

			// Fase di inserimento dei Tenori.
			for (int i = 0; i < lUltMod.getUltIstTenori().length; i++) {
				lUltMod.getUltIstTenori()[i].setUltIstIdUlterioreIstanza(lKey);
				lUltIstTenDao.setDAOFromModel(lUltMod.getUltIstTenori()[i]);
				lUltIstTenDao.insert();
				lUltIstTenDao.stop();
			}

			lUltMod.setIdUlterioreIstanza(lKey);

			// Si preleva il numero max del progressivo del tenore in
			// tabella tenori. Il numero prelevato viene assegnato ad
			// una variabile.
			BigDecimal lMaxProg = new BigDecimal(0);

			// Se l'array dei tenori è > di Zero preleva dal primo elemento
			// l'id del genraleprocedimento e esecuzione della interrogazione
			// al dbase del Max num progressivo di eventuali tenori presenti
			// nella tabella tenori.
			if (aTenori.length > 0)
				lMaxProg = lTenSqlDao
						.getMaxProgTenoreByGenProcNoDataFine(aTenori[0].getGenPridGeneraleProcedimento());

			// Esegue la lettura di occorrenze su Tenore e se non esiste
			// inserisce il medesimo.
			for (int i = 0; i < aTenori.length; i++) {
				// Imposta e legge il numero di records che corrispondono
				// al CodOggettoTenore e IDGenProc.
				lTenSqlDao.countTenoriByCodOggettoTenoreGenProcNoFine(aTenori[i]);
				lTenSqlDao.start();

				lTenSqlDao.next();
				// Preleva la count e verifica che sia
				// == a zero. True scrive.
				if (lTenSqlDao.getInt("COUNT") == 0) {
					// Incrementa il contatore lMaxProg di + 1
					lMaxProg = lMaxProg.add(new BigDecimal(1));
					// Imposta nel tenore da inserire il progressivo
					aTenori[i].setProgrTenore(lMaxProg);
					// Esegue Inserimento in dbase.
					lTenDao.setDAOFromModel(aTenori[i]);
					lTenDao.insert();
					lTenDao.stop();
				}

				lTenSqlDao.stop();
			}
			commit(lConn); // Esegue la commit.
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoex);
			throw new F3BException(
					"UlterioreIstanzaController.ExInserisciUlterioreIstanza: Non posso inserire: " + daoex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"UlterioreIstanzaController.ExInserisciUlterioreIstanza: Non posso inserire i dati : "
							+ ex);
		} finally {
			cleanup(lUltDao);
			cleanup(lUltIstTenDao);
			cleanup(lTenSqlDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}
		return lUltMod;
	}

	/**
	 * Il metodo esegue la ricerca di Ulteriori Istanze, utilizzando come parametri di ricerca quelli
	 * opportunamente contenuti nel model passato come parametro. Questo Metodo ritorna le occorrenze dei dati
	 * contenuti nella tabella Ulteriore Istanze. Metodo utilizzato
	 *
	 * @param aUlterioreIstanza
	 *            UlterioreIstanzaModel Model opportunamente popolato con i parametri di ricerca.
	 * @return Vector elenco delle occorrenze ritornate.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza) throws F3BException {

		Connection lConn = null;
		Vector lUlterioreIstanze = new Vector();
		UlterioreIstanzaSqlDAO lUltSqlDao = null;

		try {
			lConn = getDBConnection();
			lUltSqlDao = new UlterioreIstanzaSqlDAO(lConn);
			lUltSqlDao.ricercaUlterioreIstanza(aUlterioreIstanza);
			lUlterioreIstanze = new Vector(lUltSqlDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UlterioreIstanzaController.ExRicercaUlterioreIstanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUltSqlDao);
			cleanup(lConn);
		}

		return lUlterioreIstanze;
	}

	/**
	 * Esegue la ricerca di una determinata Ulteriore Istanza, attraverso il proprio id. Tale valore è passato
	 * come parametro al metodo.
	 *
	 * @param Id
	 *            per il quale "puntare" alla UlterioreIstanza.
	 * @return UlterioreIstanzaModel Istanza singola del model con i dati prelevati dal DBASE.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public UlterioreIstanzaModel ExRicercaUlterioreIstanzaByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		UlterioreIstanzaSqlDAO lUltSqlDao = null;
		UlterioreIstanzaModel lUltMod = null;

		UlterioreIstanzaTenoreSqlDAO lUltTenSqlDao = null;

		try {
			lConn = getDBConnection();

			// Recupero dei dati dell'ulteriore istanza.
			lUltSqlDao = new UlterioreIstanzaSqlDAO(lConn);
			lUltSqlDao.ricercaUlterioreIstanzaByKey(aKey);
			lUltMod = (UlterioreIstanzaModel) lUltSqlDao.getModelByKey();
			lUltSqlDao.stop();

			// Recupero dei dati dei tenori afferenti all'ulteriore Istanza.
			lUltTenSqlDao = new UlterioreIstanzaTenoreSqlDAO(lConn);
			lUltTenSqlDao.ricercaUlterioreIstanzaTenoreByUltIstKey(aKey);
			ArrayList lTenori = new ArrayList(lUltTenSqlDao.getModels());
			lUltMod.setUltIstTenori(
					(UlterioreIstanzaTenoreModel[]) lTenori.toArray(new UlterioreIstanzaTenoreModel[0]));
			lUltTenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UlterioreIstanzaController.ExRicercaUlterioreIstanza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lUltSqlDao);
			cleanup(lUltTenSqlDao);
			cleanup(lConn);
		}

		return lUltMod;
	}

	/**
	 * Esegue la modifica di una Ulteriore Istanza, i quali dati opportunamente incapsulati nel model,
	 * quest'ultimo vine passato come argomento.
	 * <p>
	 *
	 * @param aUlterioreIstanza
	 *            Istanza del Model, popolato con i valori per la modifica.
	 * @return UlterioreIstanzaModel model con i dati appena modificati.
	 * @throws F3BException
	 *             rilancia errore di eccezione.
	 */
	public UlterioreIstanzaModel ExModificaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza)
			throws F3BException {

		Connection lConn = null;
		UlterioreIstanzaDAO lUltDao = null;
		UlterioreIstanzaModel lUltMod = null;
		UlterioreIstanzaTenoreDAO lUltIstTenDao = null;

		try {
			lConn = getDBConnection(); // Preleva connessione al dbase

			lUltMod = new UlterioreIstanzaModel(aUlterioreIstanza);
			lUltDao = new UlterioreIstanzaDAO(lConn);
			lUltIstTenDao = new UlterioreIstanzaTenoreDAO(lConn);

			// Esegue aggiornamnto del record
			lUltDao.setDAOFromModelForUpdate(aUlterioreIstanza);
			lUltDao.update();

			// Rimozione dei tenori e reinserimento.
			lUltIstTenDao = new UlterioreIstanzaTenoreDAO(lConn);
			lUltIstTenDao.setCondizioneUpdateByIdUltIst(lUltMod.getIdUlterioreIstanza());
			lUltIstTenDao.delete();
			lUltIstTenDao.stop();

			// Fase di inserimento dei Tenori.
			for (int i = 0; i < lUltMod.getUltIstTenori().length; i++) {
				lUltMod.getUltIstTenori()[i].setUltIstIdUlterioreIstanza(lUltMod.getIdUlterioreIstanza());
				lUltIstTenDao.setDAOFromModel(lUltMod.getUltIstTenori()[i]);
				lUltIstTenDao.insert();
				lUltIstTenDao.stop();
			}

			lUltMod.setIdUlterioreIstanza(lUltMod.getIdUlterioreIstanza());
			commit(lConn); // Esegue la commit
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("UlterioreIstanzaController.ExModifica: Non posso inserire: " + ex);
		} catch (Exception ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"UlterioreIstanzaController.ExModificaUlterioreIstanza: Non posso inserire il soggetti : "
							+ ex);
		} finally {
			cleanup(lUltDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lUltIstTenDao);
			cleanup(lConn);
		}
		return lUltMod;
	}

	/**
	 * Metodo che si occupa della cancellazione di una ulteriore istanza.
	 *
	 * @param Model
	 *            con i criteri di rimozione
	 * @throws F3Bexception
	 *             propaga errore di eccezione.
	 */
	public void ExCancellaUlterioreIstanza(UlterioreIstanzaModel aUlterioreIstanza) throws F3BException {

		Connection lConn = null;
		UlterioreIstanzaDAO lUltDao = null;
		UlterioreIstanzaTenoreDAO lUltIstTenDao = null;

		try {
			lConn = getDBConnection();

			// Esegue la cancellazione dei tenori associati alla uletriore istanza.
			lUltIstTenDao = new UlterioreIstanzaTenoreDAO(lConn);
			lUltIstTenDao.setCondizioneUpdateByIdUltIst(aUlterioreIstanza.getIdUlterioreIstanza());
			lUltIstTenDao.delete();

			// Esegue la cancellazione della Ulteriore Istanza.
			lUltDao = new UlterioreIstanzaDAO(lConn);
			lUltDao.setCondizioneUpdate(aUlterioreIstanza.getIdUlterioreIstanza());
			lUltDao.delete();

			commit(lConn); // Esegue commit
		} catch (DAOException daoEx) {
			rollback(lConn); // Esegue rollback
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"UlterioreIstanzaController.ExCancellaUlterioreIstanza: Non posso leggere : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn); // Esegue rollback
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"UlterioreIstanzaController.ExCancellaUlterioreIstanza: Non posso leggere  : " + ex);
		} finally {
			cleanup(lUltIstTenDao);
			cleanup(lUltDao);
			cleanup(lConn);
		}
	}

}