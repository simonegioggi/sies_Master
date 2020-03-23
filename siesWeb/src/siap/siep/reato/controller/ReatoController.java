package siap.siep.reato.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.siep.reato.dao.ReatoDAO;
import siap.siep.reato.dao.ReatoSqlDAO;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.sige.reato.dao.ReatoSentenzaSigeDAO;
import siap.sige.reato.model.ReatoSentenzaSigeModel;

/**
 * <p>
 * Title: ReatoController
 * </p>
 * <p>
 * Description: Classe Controller per Reato
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
public class ReatoController extends SiapController implements IReato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Inserisce il Reato
	 *
	 * @param aReato
	 * @return
	 * @throws F3BException
	 */
	public ReatoModel ExInserisciReato(ReatoModel aReato) throws F3BException {

		Connection lConn = null;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;
		ReatoModel lReaMod = null;

		try {
			lConn = getDBConnection();

			lReaMod = new ReatoModel(aReato);
			lReaDao = new ReatoDAO(lConn);
			// Gestione Progressivo Reato
			lReaSqlDAo = new ReatoSqlDAO(lConn);
			BigDecimal lProgr = lReaSqlDAo.getProgressivoReato(lReaMod.getFasSieIdFascicoloSiep());
			lReaMod.setProgrReato(new BigDecimal(lProgr.intValue() + 1));
			lReaMod.setProgrCircostanza(new BigDecimal(0));
			lReaDao.setDAOFromModel(lReaMod);
			BigDecimal lKey = null;
			lKey = lReaDao.insert();
			commit(lConn);
			lReaMod.setIdReato(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExInserisciReato: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
			cleanup(lConn);
		}

		return lReaMod;
	}

	/**
	 * Inserimento di piu' reati
	 *
	 * @param aReati
	 *            - ArrayList di reati
	 * @return ReatoModel - il reato inserito
	 * @throws F3BException
	 */
	public ReatoModel ExInserisciReati(ArrayList aReati) throws F3BException {

		Connection lConn = null;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;

		ReatoModel lReaPrincipale = null;

		try {
			lConn = getDBTransaction();

			// Controllo sull'Action che esista almeno un reato
			lReaPrincipale = new ReatoModel((ReatoModel) aReati.get(0));

			// Gestione Progressivo Reato
			lReaSqlDAo = new ReatoSqlDAO(lConn);
			BigDecimal lKeyFascicolo = lReaPrincipale.getFasSieIdFascicoloSiep();
			BigDecimal lProgrReato = lReaSqlDAo.getProgressivoReato(lKeyFascicolo);
			lReaPrincipale.setProgrReato(new BigDecimal(lProgrReato.intValue() + 1));
			lReaPrincipale.setProgrCircostanza(new BigDecimal(1));
			lReaPrincipale.setFasSieIdFascicoloSiep(lReaPrincipale.getFasSieIdFascicoloSiep());

			// Inserimento primo Reato
			lReaDao = new ReatoDAO(lConn);
			lReaDao.setDAOFromModel(lReaPrincipale);

			BigDecimal lKeyReato = null;

			lKeyReato = lReaDao.insert();
			lReaDao.stop();

			// Inserimento successivi
			ReatoModel lReaMod = null;
			BigDecimal lProgrCircostanza = null;

			if (aReati.size() > 0) {
				lProgrCircostanza = lReaSqlDAo.getProgressivoCircostanza(lReaPrincipale.getProgrReato(),
						lKeyFascicolo);
				int lProgrCirc = lProgrCircostanza.intValue() + 1;

				for (int i = 1; i < aReati.size(); i++) {
					lReaMod = new ReatoModel();
					lReaMod = (ReatoModel) aReati.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(lReaMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");
					lReaMod.setProgrReato(lReaPrincipale.getProgrReato());
					lReaMod.setFasSieIdFascicoloSiep(lReaPrincipale.getFasSieIdFascicoloSiep());

					// Gestione Progressivo Circostanza
					// GDV spostato fuori dal ciclo
					// lProgrCircostanza =
					// lReaSqlDAo.getProgressivoCircostanza(lReaPrincipale.getProgrReato(),
					// lReaMod.getFasSieIdFascicoloSiep());
					lReaMod.setProgrCircostanza(new BigDecimal(lProgrCirc));
					lReaDao.setDAOFromModel(lReaMod);
					lReaDao.insert();
					lReaDao.stop();
					lProgrCirc++;
				}
			}

			commit(lConn);

			lReaPrincipale.setIdReato(lKeyReato);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExInserisciReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + sqe);
			throw new F3BException("ReatoController.ExInserisciReati: " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
			cleanup(lConn);
		}

		return lReaPrincipale;
	}

	/**
	 * Inserimento Ulteriori Reati
	 *
	 * @param aReatoPrincipale
	 *            - reato principale
	 * @param aReati
	 *            - Arraylist di reati
	 * @throws F3BException
	 */
	public void ExInserisciUlterioriReati(ReatoModel aReatoPrincipale, ArrayList aReati) throws F3BException {

		Connection lConn = null;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;

		ReatoModel lReaPrincipale = aReatoPrincipale;

		try {
			lConn = getDBTransaction();

			lReaDao = new ReatoDAO(lConn);
			lReaSqlDAo = new ReatoSqlDAO(lConn);

			ReatoModel lReaMod = null;
			BigDecimal lProgrCircostanza = null;

			if (aReati.size() > 0) {
				for (int i = 0; i < aReati.size(); i++) {
					lReaMod = new ReatoModel();
					lReaMod = (ReatoModel) aReati.get(i);
					lReaMod.setProgrReato(lReaPrincipale.getProgrReato());
					lReaMod.setFasSieIdFascicoloSiep(lReaPrincipale.getFasSieIdFascicoloSiep());

					// Gestione Progressivo Circostanza
					lProgrCircostanza = lReaSqlDAo.getProgressivoCircostanza(lReaPrincipale.getProgrReato(),
							lReaMod.getFasSieIdFascicoloSiep());
					lReaMod.setProgrCircostanza(new BigDecimal(lProgrCircostanza.intValue() + 1));
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("------------------------------------------------------------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(lReaMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("------------------------------------------------------------");
					lReaDao.setDAOFromModel(lReaMod);
					lReaDao.insert();
					lReaDao.stop();
				}
			}

			commit(lConn);

			// lReaPrincipale.setIdReato(lKeyReato);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExInserisciUlterioriReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			sqe.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("SQLException: " + sqe);
			throw new F3BException("ReatoController.ExInserisciUlterioriReati: " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
			cleanup(lConn);
		}
	}

	public Vector ExRicercaReato(ReatoModel aReato) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;

		try {
			// Messo un controllo poichè questa ricerca è sempre per Fascicolo SIEP. Luigi
			if (aReato.getFasSieIdFascicoloSiep() == null)
				throw new F3BException(F3BException.USER_MESSAGE, "ID Fascicolo SIEP mancante !");

			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReato(aReato);
			lReati = new Vector(lReaDao.getModels());
			if (lReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReato: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaReatiByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReatiByFascicoloSiep(aKey);
			lReati = new Vector(lReaDao.getModels());
			if (lReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReato: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 * Cerca solo i reati
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaReatiNoCircostanzaByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReatiNoCircostanzaByFascicoloSiep(aKey);
			lReati = new Vector(lReaDao.getModels());
			if (lReati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReatiNoCircostanzaByFascicolo: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaReatiByFascicoloNoError(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReatiByFascicoloSiep(aKey);
			lReati = new Vector(lReaDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReato: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReati;
	}

	public ReatoModel ExRicercaReatoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		ReatoSqlDAO lReaDao = null;
		ReatoModel lReaMod;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReatoByKey(aKey);
			lReaMod = (ReatoModel) lReaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReatoByKey: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	/**
	 * Ricerca il Reato e le circostanze correlate per un Fascicolo
	 *
	 * @param aKey
	 *            - Chiave Fascicolo
	 * @return Vettore di Reati Circostanze
	 * @throws F3BException
	 */
	public Vector ExRicercaReatoCircostanzaByFascicolo(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		ReatoSqlDAO lReaDao = null;
		Vector lListReaCirc = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReatiByFascicoloSiep(aKey);
			Vector lReati = new Vector(lReaDao.getModels());
			lListReaCirc = new Vector();
			Iterator lItx = lReati.iterator();

			while (lItx.hasNext()) {
				ReatoCircostanzaModel aModel = new ReatoCircostanzaModel();
				aModel.setReato((ReatoModel) lItx.next());
				lReaDao.ricercaCircostanzeReatoByReatoFascicoloSiep(aModel.getReato().getProgrReato(), aKey);
				List lCircostanze = new ArrayList(lReaDao.getModels());
				aModel.setCircostanze((ReatoModel[]) lCircostanze.toArray(new ReatoModel[0]));
				lListReaCirc.add(aModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReatoCircostanzaByFascicolo: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lListReaCirc;
	}

	public ReatoModel ExModificaReato(ReatoModel aReato) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;
		ReatoModel lReaMod = new ReatoModel(aReato);

		try {
			lConn = getDBConnection();

			lReaDao = new ReatoDAO(lConn);

			if (lReaMod.getProgrCircostanza().intValue() == 1)
				lReaDao.setDAOFromModelForUpdate(lReaMod);
			else
				lReaDao.setDAOFromModelForUpdateNonPrimaNorma(lReaMod);

			lReaDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExModificaReato: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	public ReatoModel ExModificaPenaReato(ReatoModel aReato, Vector aNormeUlteriori) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;
		ReatoModel lReaMod = new ReatoModel(aReato);

		try {
			lConn = getDBConnection();

			lReaDao = new ReatoDAO(lConn);

			lReaDao.setCodTipoPenaDetentiva(lReaMod.getCodTipoPenaDetentiva());
			lReaDao.setNumGiorni(lReaMod.getNumGiorni());
			lReaDao.setNumMesi(lReaMod.getNumMesi());
			lReaDao.setNumAnni(lReaMod.getNumAnni());
			lReaDao.setNumAnniIsolamentoDiurno(lReaMod.getNumAnniIsolamentoDiurno());
			lReaDao.setNumMesiIsolamentoDiurno(lReaMod.getNumMesiIsolamentoDiurno());
			lReaDao.setNumGiorniIsolamentoDiurno(lReaMod.getNumGiorniIsolamentoDiurno());
			lReaDao.setDataInizioIsolamentoDiurno(lReaMod.getDataInizioIsolamentoDiurno());
			lReaDao.setDataFineIsolamentoDiurno(lReaMod.getDataFineIsolamentoDiurno());
			lReaDao.setSanzionePecuniaria(lReaMod.getSanzionePecuniaria());
			lReaDao.setCodTipoSanzione(lReaMod.getCodTipoSanzione());

			Iterator itx = aNormeUlteriori.iterator();

			// BigDecimal oriIdReato = aReato.getIdReato();

			while (itx.hasNext()) {

				ReatoModel lNormaUlteriore = (ReatoModel) itx.next();

				lReaDao.setCondizioneUpdate(lNormaUlteriore.getIdReato());

				lReaDao.update();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExModificaPenaReato: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	/**
	 * Cancellaizone Reato
	 *
	 * @param aReato
	 *            - reato da Cancellare
	 * @throws F3BException
	 */
	public void ExCancellaReato(ReatoModel aReato) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;

		try {
			if (aReato.getProgrCircostanza() != null) {

				lConn = getDBConnection();

				lReaDao = new ReatoDAO(lConn);

				// se ho cancellato la norma base, devo cancellare anche le norme "legate"
				if (aReato.getProgrCircostanza().intValue() == 1)
					lReaDao.setCondizione_CancellazioneACatena(aReato.getProgrReato(),
							aReato.getFasSieIdFascicoloSiep());
				else
					lReaDao.setCondizioneUpdate(aReato.getIdReato());

				lReaDao.delete();
				commit(lConn);
			}
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibbile cancellare il reato collegato ad altri dati!");
			else
				throw new F3BException("ReatoController.ExCancellaReato: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExCancellaReato: " + e);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	public String ExInserisciReatiWithoutSequence(ArrayList aReati, Connection lConn) throws F3BException {

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;

		ReatoCircostanzaModel lReaPrincipale = null;
		String lCodEsito = "00000";

		try {
			// STUB 12/12/2007 Corretto ciclo di Caricamento vettore con REATO e annesse CIRCOSTANZA_REATO
			// if (aReati != null && aReati.size() > 0)
			for (int j = 0; j < aReati.size(); j++) {
				// Controllo sull'Action che esista almeno un reato

				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// mLog
				// siesLogger.debug("CLASSE * * * = " + aReati.get(j).getClass().getName());
				lReaPrincipale = new ReatoCircostanzaModel((ReatoCircostanzaModel) aReati.get(j));

				// Inserimento primo Reato
				lReaDao = new ReatoDAO(lConn);
				lReaDao.setDAOFromModel(lReaPrincipale.getReato());
				lReaDao.setWithoutSequence(true);

				// BigDecimal lKeyReato = lReaPrincipale.getReato().getIdReato();
				lReaDao.insert();
				lReaDao.stop();

				// Inserimento successivi
				ReatoModel lReaMod = null;
				// BigDecimal lProgrCircostanza = null;

				// STUB 12/12/2007 Corretto ciclo di Caricamento vettore con REATO e annesse CIRCOSTANZA_REATO
				for (int i = 0; i < lReaPrincipale.getCircostanze().length; i++) {
					lReaMod = new ReatoModel();
					lReaMod = lReaPrincipale.getCircostanze()[i];
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(lReaMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");

					// Gestione Progressivo Circostanza
					lReaDao.setDAOFromModel(lReaMod);
					lReaDao.setWithoutSequence(true);
					lReaDao.insert();
					lReaDao.stop();
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Reato gia' presente...");
				// Inizializzo la chiave del fascicolo con quella inviatami
				// lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire i Reati! ");
			}
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
		}

		return lCodEsito;
	}

	public void ExOrganizzaReati(Vector reatiMod, Vector reatiCanc) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDaoMod = null;
		ReatoModel lReaMod = null;
		ReatoDAO lReaDaoCanc = null;
		ReatoModel lReaCanc = null;

		try {
			lConn = getDBConnection();

			lReaDaoMod = new ReatoDAO(lConn);

			Iterator itx = reatiMod.iterator();

			while (itx.hasNext()) {

				lReaMod = new ReatoModel();
				lReaMod = (ReatoModel) itx.next();

				lReaDaoMod.setProgrReato(lReaMod.getProgrReato());
				lReaDaoMod.setProgrCircostanza(lReaMod.getProgrCircostanza());

				lReaDaoMod.setProgrNumeroManuale(lReaMod.getProgrNumeroManuale());

				lReaDaoMod.setCodTipoReato(lReaMod.getCodTipoReato());
				lReaDaoMod.setDataReato(lReaMod.getDataReato());
				lReaDaoMod.setDataInizio(lReaMod.getDataInizio());
				lReaDaoMod.setAnnoInizio(lReaMod.getAnnoInizio());
				lReaDaoMod.setMeseInizio(lReaMod.getMeseInizio());
				lReaDaoMod.setGiornoInizio(lReaMod.getGiornoInizio());
				lReaDaoMod.setDataFine(lReaMod.getDataFine());
				lReaDaoMod.setAnnoFine(lReaMod.getAnnoFine());
				lReaDaoMod.setMeseFine(lReaMod.getMeseFine());
				lReaDaoMod.setGiornoFine(lReaMod.getGiornoFine());
				lReaDaoMod.setCodPeriodoConsumazione(lReaMod.getCodPeriodoConsumazione());
				lReaDaoMod.setDescLuogo(lReaMod.getDescLuogo());
				lReaDaoMod.setNote(lReaMod.getNote());

				lReaDaoMod.setCodTipoPenaDetentiva(lReaMod.getCodTipoPenaDetentiva());
				lReaDaoMod.setNumGiorni(lReaMod.getNumGiorni());
				lReaDaoMod.setNumMesi(lReaMod.getNumMesi());
				lReaDaoMod.setNumAnni(lReaMod.getNumAnni());
				lReaDaoMod.setNumAnniIsolamentoDiurno(lReaMod.getNumAnniIsolamentoDiurno());
				lReaDaoMod.setNumMesiIsolamentoDiurno(lReaMod.getNumMesiIsolamentoDiurno());
				lReaDaoMod.setNumGiorniIsolamentoDiurno(lReaMod.getNumGiorniIsolamentoDiurno());
				lReaDaoMod.setDataInizioIsolamentoDiurno(lReaMod.getDataInizioIsolamentoDiurno());
				lReaDaoMod.setDataFineIsolamentoDiurno(lReaMod.getDataFineIsolamentoDiurno());
				lReaDaoMod.setSanzionePecuniaria(lReaMod.getSanzionePecuniaria());
				lReaDaoMod.setCodTipoSanzione(lReaMod.getCodTipoSanzione());

				if (lReaMod.getIdReato() == null)
					throw new F3BException("Errore non previsto in modifica");

				lReaDaoMod.setCondizioneUpdate(lReaMod.getIdReato());

				lReaDaoMod.update();

				lReaDaoMod.stop();
			}

			lReaDaoCanc = new ReatoDAO(lConn);

			Iterator itx2 = reatiMod.iterator();
			itx2 = reatiCanc.iterator();

			while (itx2.hasNext()) {

				lReaCanc = new ReatoModel();
				lReaCanc = (ReatoModel) itx2.next();

				if (lReaCanc.getIdReato() == null)
					throw new DAOException("Errore non previsto in cancellazione");

				lReaDaoCanc.setCondizioneUpdate(lReaCanc.getIdReato());

				lReaDaoCanc.delete();

				lReaDaoCanc.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExOrganizzaReati: " + ex);
		} finally {
			cleanup(lReaDaoMod);
			cleanup(lReaDaoCanc);
			cleanup(lConn);
		}

	}

	public void ExModificaUlterioriNorme(ReatoModel aNormaUno, Vector aNormeUlteriori) throws F3BException {

		Connection lConn = null;
		ReatoDAO lReaDao = null;

		try {
			lConn = getDBConnection();

			Iterator itx = aNormeUlteriori.iterator();

			BigDecimal oriIdReato = aNormaUno.getIdReato();

			while (itx.hasNext()) {

				ReatoModel lNormaUlteriore = (ReatoModel) itx.next();

				// Faccio l'update se non è la prima norma
				if (lNormaUlteriore.getProgrCircostanza().intValue() != 1) {

					aNormaUno.setIdReato(lNormaUlteriore.getIdReato());

					lReaDao = new ReatoDAO(lConn);

					lReaDao.setDAOFromModelForUpdateUlterioriNorme(aNormaUno);

					lReaDao.update();
				}
			}

			// Per correttezza risetto il progressivo reato originale alla prima norma
			aNormaUno.setIdReato(oriIdReato);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoController.ExModificaUlterioriNorme: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
	}

	public ReatoModel ExRicercaNormaPrincipaleByReatoFascicoloSiep(BigDecimal aProgrReato,
			BigDecimal aKeyFasc) throws F3BException {

		Connection lConn = null;
		ReatoSqlDAO lReaDao = null;
		ReatoModel lReaMod;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaNormaPrincipaleByReatoFascicoloSiep(aProgrReato, aKeyFasc);
			lReaMod = (ReatoModel) lReaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReatoByKey: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	// ///////// SIGE //////////////
	/**
	 * Inserimento di piu' reati associati a Sentenza-ProcedimentoSIGE e non a ProcedimentoSIEP. La funzione è
	 * ottenuta da ExInserisciReatiSige eliminando da questa i riferimenti previsti al Procedimento SIEP.
	 *
	 * @param aReati
	 *            - ArrayList di reati
	 * @param aIdFasSigeSen
	 *            - ID di Sentenza_Fascicolo_Sige
	 * @return ReatoModel - il reato principale inserito
	 * @throws F3BException
	 */
	public ReatoModel ExInserisciReatiSige(ArrayList aReati, BigDecimal aIdFasSigeSen) throws F3BException {

		Connection lConn = null;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;
		ReatoSentenzaSigeDAO lReaSenDao = null;

		ReatoModel lReaPrincipale = null;
		ReatoSentenzaSigeModel lReaSen = null;
		BigDecimal lKeyReato = null;

		try {
			lConn = getDBTransaction();

			// Controllo sull'Action che esista almeno un reato
			lReaPrincipale = new ReatoModel((ReatoModel) aReati.get(0));

			// Gestione Progressivo Reato
			lReaSqlDAo = new ReatoSqlDAO(lConn);
			BigDecimal lProgrReato = lReaSqlDAo.getProgressivoReatoSige(aIdFasSigeSen);
			lReaPrincipale.setProgrReato(new BigDecimal(lProgrReato.intValue() + 1));
			lReaPrincipale.setProgrCircostanza(new BigDecimal(1));

			// Inserimento primo Reato
			lReaDao = new ReatoDAO(lConn);
			lReaDao.setDAOFromModel(lReaPrincipale);
			lKeyReato = lReaDao.insert();
			lReaDao.stop();
			lReaPrincipale.setIdReato(lKeyReato);

			// Inserimento record di relazione in REATO_SENTENZA_SIGE
			lReaSen = new ReatoSentenzaSigeModel(lKeyReato, aIdFasSigeSen);
			lReaSenDao = new ReatoSentenzaSigeDAO(lConn);
			lReaSenDao.setDAOFromModel(lReaSen);
			lReaSenDao.insert();
			lReaSenDao.stop();

			// Inserimento successivi
			ReatoModel lReaMod = null;
			BigDecimal lProgrCircostanza = null;

			if (aReati.size() > 0) {
				lProgrCircostanza = lReaSqlDAo.getProgressivoCircostanzaSige(lReaPrincipale.getProgrReato(),
						aIdFasSigeSen);
				int lProgrCirc = lProgrCircostanza.intValue() + 1;

				for (int i = 1; i < aReati.size(); i++) {
					lReaMod = new ReatoModel((ReatoModel) aReati.get(i));
					// lReaMod = (ReatoModel) aReati.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(lReaMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("-------------------------------------------------------------");
					lReaMod.setProgrReato(lReaPrincipale.getProgrReato());

					// Gestione Progressivo Circostanza
					lReaMod.setProgrCircostanza(new BigDecimal(lProgrCirc));
					lReaDao.setDAOFromModel(lReaMod);
					lProgrCirc++;

					// Inserimento REATO
					lKeyReato = lReaDao.insert();
					lReaDao.stop();

					// Inserimento record di relazione in REATO_SENTENZA_SIGE
					lReaSen = new ReatoSentenzaSigeModel(lKeyReato, aIdFasSigeSen);
					lReaSenDao = new ReatoSentenzaSigeDAO(lConn);
					lReaSenDao.setDAOFromModel(lReaSen);
					lReaSenDao.insert();
					lReaSenDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExInserisciReatiSige: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExInserisciReatiSige -> " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSenDao);
			cleanup(lReaSqlDAo);
			cleanup(lConn);
		}

		return lReaPrincipale;
	}

	/**
	 * Ricerca di Reati SIGE, ovvero reati legati ad un Titolo Esecutivo associato ad un Procedimento SIGE. La
	 * funzione è stata ottenuta da una copia di ExRicercaReato(...) nella quale si usa una nuova funzione
	 * ReatoSqlDAO.ricercaReato(...) (vedi) la quale realizza una condizione di ricerca sulla tabella REATO
	 * che fa uso della tabella di relazione REATO_SENTENZA_SIGE.
	 *
	 */
	public Vector ExRicercaReatoSige(ReatoSentenzaSigeModel aReato) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReato(aReato);
			lReati = new Vector(lReaDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("ReatoController.ExRicercaReatoSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("ReatoController.ExRicercaReatoSige -> " + e);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 * Ricerca di Reati e Circostanze legati allo stesso Titolo Esecutivo legato ad un Fascicolo SIGE. La
	 * chiave di ricerca è l'ID del FascicoloSigeSentenza ovvero della entità che lega un Procedimento SIGE ad
	 * un Titolo Esecutivo. Viene effettuata una ricerca nella tabella REATO di tutti gli elementi collegati
	 * tramite la tabella di relazione REATO_SENTENZA_SIGE alla SentenzaSige definita dal parametro di
	 * ingresso. L'elenco dei Reati così ottenuto viene scandito per discriminare i reati dalle circostanze.
	 * Reati e circostanze legati allo stesso reato hanno lo stesso Progr. Reato, il reato ha il Progr.
	 * Circostanza = 1. La scansione viene effettuata sfruttando l'ordinamento dei Reati per Progr. reato.
	 *
	 */
	public Vector ExRicercaReatoCircostanzaBySentenzaSige(BigDecimal aKey) throws F3BException {

		// Risultato: Elenco di ReatoCircostanzaModel
		Vector lListReaCirc = new Vector();
		// Filtro di Ricerca
		ReatoSentenzaSigeModel lReatoSige = null;
		// Elenco Reati
		Vector lReati = null;
		// ID Prog Reato corrente elemento della lista
		BigDecimal lProgrReatoCorr = null;
		// ID Prog Reato nuovo elemento
		BigDecimal lProgrReato = null;

		ReatoModel lReato = null;
		BigDecimal lUno = new BigDecimal(1);

		try {
			// Ricerca di tutti i Reati legati alla stessa Sentenza-Fascicolo SIGE
			lReatoSige = new ReatoSentenzaSigeModel();
			lReatoSige.setFasSigeSenId(aKey);
			lReati = ExRicercaReatoSige(lReatoSige);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("Num Reati: " + lReati.size());

			// Iterazione sulla lista risultato della ricerca per costruire
			// la lista di ReatiCircostanze a partire da quella di Reati.
			Iterator lItx = lReati.iterator();
			if (lItx.hasNext()) {
				// Prima lettura fuori ciclo
				lReato = new ReatoModel((ReatoModel) lReati.get(0));
				lProgrReatoCorr = lReato.getProgrReato();
				// Si istanzia il nuovo model ReatoCircostanzaModel
				ReatoCircostanzaModel aModel = new ReatoCircostanzaModel();
				// Si istanzia la lista che conterrà le Circostante di uno stesso reato
				List lCircostanze = new ArrayList();

				while (lItx.hasNext()) {
					// Lettura di un nuovo elemento dalla lista Reati
					lReato = new ReatoModel((ReatoModel) lItx.next());
					lProgrReato = lReato.getProgrReato();

					// Cambio del progr reato
					if (lProgrReato.compareTo(lProgrReatoCorr) != 0) {
						// Inserimento nella lista del Model corrente
						aModel.setCircostanze((ReatoModel[]) lCircostanze.toArray(new ReatoModel[0]));
						lListReaCirc.add(aModel);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Inserimento : " + aModel);

						// Aggiornamento del Progr. Reato
						lProgrReatoCorr = lProgrReato;

						// Si istanzia il nuovo model ReatoCircostanzaModel
						aModel = new ReatoCircostanzaModel();
						// Si istanzia la lista che conterrà le Circostante di uno stesso reato
						lCircostanze = new ArrayList();
					}

					// In base al Progr. Circostanza si discrimina un Reato da una Circostanza
					if (lReato.getProgrCircostanza().compareTo(lUno) == 0) {
						// Reato
						aModel.setReato(lReato);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Reato : " + lReato);

					} else {
						// Circostanza
						lCircostanze.add(lReato);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Circostanza : " + lReato);
					}
				} // end while

				// Inserimento nella lista dell'ultimo
				aModel.setCircostanze((ReatoModel[]) lCircostanze.toArray(new ReatoModel[0]));
				lListReaCirc.add(aModel);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.debug("Inserimento : " + aModel);
			}
		} catch (F3BException fEx) {
			throw fEx;
		} catch (Exception e) {
			throw new F3BException("ReatoController.ExRicercaReatoCircostanzaBySentenzaSige -> " + e);
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.debug("Num ReatiCircostanze: " + lListReaCirc.size());

		return lListReaCirc;
	}

	/**
	 * Funzione di ricerca di tutte le norme ed il reato legate ad un Titolo SIGE e con Progr. Reato
	 * specificato.
	 *
	 * @param aReato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaNormeSige(ReatoSentenzaSigeModel aReato) throws F3BException {

		Connection lConn = null;
		Vector lReati = new Vector();
		ReatoSqlDAO lReaDao = null;
		ReatoSentenzaSigeModel lReato;
		try {
			// Valorizzazione delle condizioni di ricerca
			lReato = new ReatoSentenzaSigeModel();
			lReato.setProgrReato(aReato.getProgrReato());
			lReato.setFasSigeSenId(aReato.getFasSigeSenId());

			// Ricerca
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);
			lReaDao.ricercaReato(lReato);
			lReati = new Vector(lReaDao.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException("ReatoController.ExRicercaNormeSige: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("ReatoController.ExRicercaNormeSige -> " + e);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 * Cancellaizone Reato Sige
	 *
	 * @param aReato
	 *            - reato da Cancellare
	 * @throws F3BException
	 */
	public void ExCancellaReatoSige(ReatoSentenzaSigeModel aReato) throws F3BException {

		Connection lConn = null;
		// DAO alla Tabella REATO
		ReatoDAO lReaDao = null;
		// DAO alla tabella di relazione REATO_SENTENZA_SIGE
		ReatoSentenzaSigeDAO lReSenSigeDao = null;
		// elenco dei reati da cancellare
		Vector lNorme = null;

		if (aReato.getProgrCircostanza() != null) {

			try {
				lConn = getDBConnection();
				lReaDao = new ReatoDAO(lConn);
				lReSenSigeDao = new ReatoSentenzaSigeDAO(lConn);
				lReaDao = new ReatoDAO(lConn);

				// se si cancella la norma base, devono essere cancellate anche le norme "legate"
				if (aReato.getProgrCircostanza().intValue() == 1)
					lNorme = ExRicercaNormeSige(aReato);
				else {
					// Unico reato da cancellare
					lNorme = new Vector();
					lNorme.add(aReato);
				}

				// Cancellazione
				Iterator itx = lNorme.iterator();

				while (itx.hasNext()) {
					ReatoModel lReato = (ReatoModel) itx.next();

					// Prima si cancella il riferimento nella tabella di relazione REATO_SENTENZA_SIGE
					lReSenSigeDao.setCondizioneDeleteReato(lReato.getIdReato());
					lReSenSigeDao.delete();
					lReSenSigeDao.stop();

					// Poi si cancella il record nella tabella REATO
					lReaDao.setCondizioneUpdate(lReato.getIdReato());
					lReaDao.delete();
					lReaDao.stop();
				}

				commit(lConn);
			} catch (DAOException daoEx) {
				rollback(lConn);
				if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Non è possibbile cancellare il reato perchè collegato ad altri dati!");
				else
					throw new F3BException("ReatoController.ExCancellaReatoSige: " + daoEx);
			} catch (Exception e) {
				rollback(lConn);
				throw new F3BException("ReatoController.ExCancellaReatoSige: " + e);
			} finally {
				cleanup(lReaDao);
				cleanup(lReSenSigeDao);
				cleanup(lConn);
			}
		} else
			throw new F3BException("ReatoController.ExCancellaReatoSige: Progr Circostanza null!");
	}

	/**
	 * Inserimento Ulteriori Reati Sige
	 *
	 * @param aReatoPrincipale
	 *            - reato principale
	 * @param aReati
	 *            - Arraylist di reati
	 * @throws F3BException
	 */
	public void ExInserisciUlterioriReatiSige(ReatoModel aReatoPrincipale, ArrayList aReati,
			BigDecimal aIdFasSigeSen) throws F3BException {

		Connection lConn = null;
		ReatoModel lReaPrincipale = aReatoPrincipale;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDAo = null;
		ReatoSentenzaSigeDAO lReaSenDao = null;

		ReatoSentenzaSigeModel lReaSen = null;
		BigDecimal lKeyReato = null;

		try {
			// Connessione
			lConn = getDBTransaction();

			// Inizializzazione dei DAO
			lReaDao = new ReatoDAO(lConn);
			lReaSqlDAo = new ReatoSqlDAO(lConn);
			lReaSenDao = new ReatoSentenzaSigeDAO(lConn);

			ReatoModel lReaMod = null;
			BigDecimal lProgrCircostanza = null;

			if (aReati.size() > 0) {
				// Valutazione del Progr Circostanza
				lProgrCircostanza = lReaSqlDAo.getProgressivoCircostanzaSige(lReaPrincipale.getProgrReato(),
						aIdFasSigeSen);
				int lProgrCirc = lProgrCircostanza.intValue() + 1;

				for (int i = 0; i < aReati.size(); i++, lProgrCirc++) {
					// Valorizzazione dell reato i-esimo
					lReaMod = new ReatoModel((ReatoModel) aReati.get(i));
					lReaMod.setProgrReato(lReaPrincipale.getProgrReato());
					// Progressivo Circostanza
					lReaMod.setProgrCircostanza(new BigDecimal(lProgrCirc));

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("------------------------------------------------------------");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug(lReaMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.debug("------------------------------------------------------------");

					// Inserimento nella tabella REATO
					lReaDao.setDAOFromModel(lReaMod);
					lKeyReato = lReaDao.insert();
					lReaDao.stop();

					// Inserimento record di relazione in REATO_SENTENZA_SIGE
					lReaSen = new ReatoSentenzaSigeModel(lKeyReato, aIdFasSigeSen);
					lReaSenDao.setDAOFromModel(lReaSen);
					lReaSenDao.insert();
					lReaSenDao.stop();
				}
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExInserisciUlterioriReati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException("ReatoController.ExInserisciUlterioriReati-> " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDAo);
			cleanup(lReaSenDao);
			cleanup(lConn);
		}
	}

	public void ExModificaKeyNSCByKey(ReatoModel aReato) throws F3BException {

		Connection conn = null;
		ReatoDAO lReaDao = null;
		// ReatoModel lReaMod = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("ReatoController.ExModificaKeyNSCByKey: entrata nel metodo ");

		try {
			conn = getDBConnection();
			lReaDao = new ReatoDAO(conn);
			lReaDao.setDAOFromModelForUpdateKeyReatoNsc(aReato);
			lReaDao.setCondizioneUpdate(aReato.getIdReato());
			lReaDao.update();

			commit(conn);

		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("ReatoController.ExModificaKeyNSCByKey: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(conn);
		}
	}

	// Da utilizzare solo se dobbiamo girare i Reati x NSC
	public Vector ExRicercaReatoCircostanzaByFascicoloOnlyNsc(long aFascicoloSIEP) throws F3BException {

		Connection lConn = null;
		ReatoSqlDAO lReaDao = null;
		Vector lListReaCirc = null;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoSqlDAO(lConn);

			lReaDao.RicercaReatiByFascicoloSiepOnlyNSC(aFascicoloSIEP);
			Vector lReati = new Vector(lReaDao.getModels());
			lReaDao.stop();

			lListReaCirc = new Vector();
			Iterator lItx = lReati.iterator();

			int AppoProgReato = 0, lContaRecord = 0;
			int lFlagReato = 0;

			ReatoCircostanzaModel aModel;
			ReatoModel lReatoModel = null;

			if (lItx.hasNext()) {
				// Prima lettura fuori ciclo
				lReatoModel = new ReatoModel((ReatoModel) lReati.get(0));
				AppoProgReato = lReatoModel.getProgrReato().intValue();
				// AppoProgCirc = lReatoModel.getProgrCircostanza().intValue();

				// ----> Inizio Blocco - Determino Il numero di record per il Reato (Progr_Reato) che stiamo
				// Trattando
				lReaDao = new ReatoSqlDAO(lConn);
				lReaDao.ContaRecordPerReato(aFascicoloSIEP, AppoProgReato);
				lReaDao.start(); // Esegue la Query

				if (lReaDao.next()) // Ciclo su Recordset
				{
					lContaRecord = lReaDao.getRisultatoContaRecordPerReato();
				}
				lReaDao.stop();
				// ----> Fine Blocco

				// Si istanzia il nuovo model ReatoCircostanzaModel
				aModel = new ReatoCircostanzaModel();
				// Si istanzia la lista che conterrà le Circostante di uno stesso reato
				List lCircostanze = new ArrayList();

				while (lItx.hasNext()) {
					lReatoModel = new ReatoModel((ReatoModel) lItx.next());

					if (AppoProgReato != lReatoModel.getProgrReato().intValue()) {
						aModel.setCircostanze((ReatoModel[]) lCircostanze.toArray(new ReatoModel[0]));
						lListReaCirc.add(aModel);

						aModel = new ReatoCircostanzaModel();
						lCircostanze = new ArrayList();
						lFlagReato = 0;
						AppoProgReato = lReatoModel.getProgrReato().intValue();

						// ----> Inizio Blocco - Determino Il numero di record per il Reato (Progr_Reato) che
						// stiamo Trattando
						lReaDao = new ReatoSqlDAO(lConn);
						lReaDao.ContaRecordPerReato(aFascicoloSIEP, AppoProgReato);
						lReaDao.start(); // Esegue la Query

						if (lReaDao.next()) // Ciclo su Recordset
						{
							lContaRecord = lReaDao.getRisultatoContaRecordPerReato();
						}
						lReaDao.stop();
						// ----> Fine Blocco
					}

					if (lContaRecord > 1) {
						// Controllo inserito per gestire i casi in cui l'articolo è null
						if (lReatoModel.getArticolo() == null) {
							lReatoModel.setArticolo("");
						}

						if (lFlagReato == 0 && (!lReatoModel.getArticolo().equals("110")
								&& !lReatoModel.getArticolo().equals("56")
								&& !lReatoModel.getArticolo().equals("81"))) {
							// Carichiamo i Reati
							aModel.setReato(lReatoModel);
							lFlagReato = 1;
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Articolo Reato:" + lReatoModel.getArticolo());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("ID Articolo Reato:" + lReatoModel.getIdReato());
						} else {
							// Carichiamno le Circostanze
							lCircostanze.add(lReatoModel);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("Articolo CircostanzaReato :" + lReatoModel.getArticolo());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("ID Articolo CircostanzaReato:" + lReatoModel.getIdReato());
						}
					} else {
						// Un solo reato quindi Carichiamo il reato indipendentemente dalla tipologia di
						// Articolo
						aModel.setReato(lReatoModel);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Solo 1 Record - Articolo Reato:" + lReatoModel.getArticolo());
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Solo 1 Record - ID Articolo Reato:" + lReatoModel.getIdReato());
					}
				}
				// Inserimento nella lista dell'ultimo Record
				aModel.setCircostanze((ReatoModel[]) lCircostanze.toArray(new ReatoModel[0]));
				lListReaCirc.add(aModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoController.ExRicercaReatoCircostanzaByFascicoloOnlyNsc: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lListReaCirc;
	}

	// AMBROSINO 04/2011
	/**
	 * Inserimento dei Reati Copiati da altro procedimento Siep
	 *
	 * @param aIdFas
	 *            - Procedimento da cui copiare
	 * @param aReati
	 *            - Stringa di reati
	 * @throws F3BException
	 */
	public String ExInserisciReatiCopiati(String[] aReati, BigDecimal aIdFas, ReatoModel aReato)
			throws F3BException {

		Connection lConn = null;

		ReatoDAO lReaDao = null;
		ReatoSqlDAO lReaSqlDao = null;
		ReatoSqlDAO lReaSqlDao1 = null;

		ReatoModel lReaModOld = new ReatoModel();
		String ArrRit = "";

		try {
			// Connessione
			lConn = getDBTransaction();

			lReaDao = new ReatoDAO(lConn);
			lReaSqlDao = new ReatoSqlDAO(lConn);
			lReaSqlDao1 = new ReatoSqlDAO(lConn);

			BigDecimal lProgrReato = lReaSqlDao1.getProgressivoReato(aReato.getFasSieIdFascicoloSiep());

			String NRea = null;
			for (int i = 0; i < aReati.length; i++) {
				NRea = aReati[i];
				// Copia(NRea);

				lReaSqlDao.RicercaReatiNoCircostanzaByFascicoloeProgr(aIdFas, NRea);
				Vector VReati = new Vector(lReaSqlDao.getModels());
				lReaSqlDao.stop();

				lProgrReato = (new BigDecimal(lProgrReato.intValue() + 1));
				ArrRit += lProgrReato.toString() + ",";

				Iterator lItx = VReati.iterator();
				while (lItx.hasNext()) {
					lReaModOld = new ReatoModel();
					lReaModOld = null;

					lReaModOld = (ReatoModel) lItx.next();

					lReaDao.setDAOFromModel(lReaModOld);
					lReaDao.setCodOperatoreInserimento(aReato.getCodOperatoreInserimento());
					lReaDao.setCodUfficioInserimento(aReato.getCodUfficioInserimento());
					lReaDao.setDataInserimento(aReato.getDataInserimento());
					lReaDao.setFasSieIdFascicoloSiep(aReato.getFasSieIdFascicoloSiep());
					lReaDao.setProgrReato(lProgrReato);

					lReaDao.insert();
					lReaDao.stop();
				}

			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(" DAOException - ReatoController.ExInserisciReatiCopiati: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			throw new F3BException(" Exception - ReatoController.ExInserisciReatiCopiati-> " + sqe);
		} finally {
			cleanup(lReaDao);
			cleanup(lReaSqlDao);
			cleanup(lReaSqlDao1);
			cleanup(lConn);
		}

		return ArrRit;
	}

}