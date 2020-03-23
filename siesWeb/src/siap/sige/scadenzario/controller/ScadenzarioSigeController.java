package siap.sige.scadenzario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.dao.FascicoloSigeSqlDAO;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.scadenzario.dao.ScadenzarioSigeDAO;
import siap.sige.scadenzario.dao.ScadenzarioSigeSqlDAO;
import siap.sige.scadenzario.model.ScadenzarioSigeModel;

/**
 * <p>
 * Title: ScadenzarioSigeController
 * </p>
 * <p>
 * Description: Classe Controller per ScadenzarioSige
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
public class ScadenzarioSigeController extends SiapController implements IScadenzarioSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ScadenzarioSigeModel ExInserisciScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeDAO lScaDao = null;
		ScadenzarioSigeModel lScaMod = null;

		try {
			lConn = getDBConnection();
			lScaMod = new ScadenzarioSigeModel(aScadenzarioSige);
			lScaDao = new ScadenzarioSigeDAO(lConn);
			lScaDao.setDAOFromModel(aScadenzarioSige);
			BigDecimal lKey = null;
			lKey = lScaDao.insert();
			commit(lConn);
			lScaMod.setIdScadenzarioSige(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioSigeController.ExInserisci: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Ricerca i Tipi Scadenzari Sige per l'ufficio Collegato.
	 *
	 * @param aTipoUfficio
	 * @return Vettore di Tipi Scadenzari SIGE
	 * @throws F3BException
	 */
	public Vector ExElencoTipiScadenzarioByTipoUfficio(String aTipoUfficio) throws F3BException {

		Connection lConn = null;
		Vector lTipiScadenzariSige = new Vector();
		ScadenzarioSigeSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);

			lScaDao.ricercaTipiScadenzariSigeByTipoUfficio(aTipoUfficio);
			lScaDao.start();

			while (lScaDao.next()) {

				String lCod = lScaDao.getString("COD_TIPO_SCADENZARIO");
				String lDesc = lScaDao.getString("DESCR_TIPO_SCADENZARIO");

				lTipiScadenzariSige.add(new DecodeModel(lCod, " " + lDesc + " " + " "));
			}

			lScaDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ScadenzarioSigeController.ExElencoTipiScadenzarioByTipoUfficio: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		if (lTipiScadenzariSige.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Nessun Tipo Scadenzario codificato in Archivio");
		}
		return lTipiScadenzariSige;
	}

	public Vector ExRicercaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige) throws F3BException {

		Connection lConn = null;
		Vector lScadenzariSige = new Vector();
		ScadenzarioSigeSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSige(aScadenzarioSige);
			lScadenzariSige = new Vector(lScaDao.getModels());
			if (lScadenzariSige.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExRicercaScadenzarioSige: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScadenzariSige;
	}

	public ScadenzarioSigeModel ExRicercaScadenzarioSigeByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeSqlDAO lScaDao = null;
		ScadenzarioSigeModel lScaMod;
		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSigeByKey(aKey);
			lScaMod = (ScadenzarioSigeModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExRicercaScadenzarioSigeByKey : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Ricerca record con scadenza all'interno di un intervallo di date.
	 */
	public Vector ExRicercaScadenzarioSige(Date aData1, Date aData2) throws F3BException {

		Connection lConn = null;
		Vector lScadenzariSige = new Vector();
		ScadenzarioSigeSqlDAO lScaDao = null;
		FascicoloSigeSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);

			lScaDao.ricercaScadenzarioSigePerDate(aData1, aData2);
			lScadenzariSige = new Vector(lScaDao.getModels());
			if (lScadenzariSige.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {
				lFascDao = new FascicoloSigeSqlDAO(lConn);
				lSoggDao = new SoggettoSqlDAO(lConn);
				lEveDao = new EventoSqlDAO(lConn);

				ScadenzarioSigeModel lScadModTmp = new ScadenzarioSigeModel();
				FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
				// SoggettoModel lSoggMod = new SoggettoModel();
				BigDecimal lFascID;
				BigDecimal lSoggID;
				EventoModel lEvento = new EventoModel();
				BigDecimal lEveID;

				for (int i = 0; i < lScadenzariSige.size(); i++) {
					lScadModTmp = (ScadenzarioSigeModel) lScadenzariSige.get(i);
					lFascID = lScadModTmp.getFasIdFascicoloSige();
					if (lFascID != null) {
						// Cerca il fascicolo by key fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID FASCICOLO: " + lFascID);
						lFascDao.ricercaFascicoloSigeByKey(lFascID);
						lFascicolo = (FascicoloSigeModel) lFascDao.getModelByKey();

						lSoggID = lFascicolo.getSogIdSoggetto();
						if (lSoggID != null) {
							// Cerca il soggetto associato al fascicolo
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("ID SOGGETTO: " + lSoggID);
							lSoggDao.ricercaSoggettoByKey(lSoggID);
							/* lSoggMod = (SoggettoModel) */lSoggDao.getModelByKey();

							lFascicolo.setSogIdSoggetto(lSoggID);

							lScadModTmp.setFascicoloSige(lFascicolo);
							lScadenzariSige.set(i, lScadModTmp);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("Effettuato aggiornamento Fascicolo e Soggetto");
						} else {
							throw new F3BException(F3BException.USER_MESSAGE,
									"Manca ID Soggetto nel Fascicolo");
						}
					} else {
						throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Fascicolo");
					}

					// 02/08/2004 Si Carica anche l'Evento.
					lEveID = lScadModTmp.getEveIdEvento();
					if (lEveID != null) {
						// Cerca l'Evento by key
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID EVENTO: " + lEveID);
						lEveDao.ricercaEventoByKey(lEveID);
						lEvento = (EventoModel) lEveDao.getModelByKey();
						lScadModTmp.setEvento(lEvento);
					}

				} // end for
			} // endif size
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExRicercaScadenzarioSige: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lScadenzariSige;
	}

	/**
	 * Ricerca record di scadenzario di un determinato tipo, con scadenza all'interno di un intervallo di
	 * date.
	 */
	public Vector ExRicercaScadenzarioSige(String aTipoScadenzario, Date aData1, Date aData2)
			throws F3BException {

		Connection lConn = null;
		Vector lScadenzariSige = new Vector();
		ScadenzarioSigeSqlDAO lScaDao = null;
		FascicoloSigeSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);
			lFascDao = new FascicoloSigeSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lEveDao = new EventoSqlDAO(lConn);

			lScaDao.ricercaScadenzarioSigePerTipoDate(aTipoScadenzario, aData1, aData2);
			lScadenzariSige = new Vector(lScaDao.getModels());
			if (lScadenzariSige.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {
				ScadenzarioSigeModel lScadModTmp = new ScadenzarioSigeModel();
				FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
				EventoModel lEvento = new EventoModel();
				SoggettoModel lSoggMod = new SoggettoModel();
				BigDecimal lFascID;
				BigDecimal lSoggID;
				BigDecimal lEveID;
				for (int i = 0; i < lScadenzariSige.size(); i++) {
					lScadModTmp = (ScadenzarioSigeModel) lScadenzariSige.get(i);
					lFascID = lScadModTmp.getFasIdFascicoloSige();
					if (lFascID != null) {
						// Cerca il fascicolo by key fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID FASCICOLO: " + lFascID);
						lFascDao.ricercaFascicoloSigeByKey(lFascID);
						lFascicolo = (FascicoloSigeModel) lFascDao.getModelByKey();

						lSoggID = lFascicolo.getSogIdSoggetto();
						if (lSoggID != null) {
							// Cerca il soggetto associato al fascicolo
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("ID SOGGETTO: " + lSoggID);
							lSoggDao.ricercaSoggettoByKey(lSoggID);
							lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

							lFascicolo.setSogIdSoggetto(lSoggID);

							lScadModTmp.setFascicoloSige(lFascicolo);
							lScadModTmp.setSoggetto(lSoggMod);
							lScadenzariSige.set(i, lScadModTmp);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("Effettuato aggiornamento Fascicolo e Soggetto");
						} else {
							throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Fascicolo");
						}
					} else {
						throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Soggetto nel Fascicolo");
					}

					// 11/10/2004 Si Carica anche l'Evento.
					lEveID = lScadModTmp.getEveIdEvento();
					if (lEveID != null) {
						// Cerca l'Evento by key
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID EVENTO: " + lEveID);
						lEveDao.ricercaEventoByKey(lEveID);
						lEvento = (EventoModel) lEveDao.getModelByKey();
						lScadModTmp.setEvento(lEvento);
					}
				} // end for
			} // endif size
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExRicercaScadenzarioSige: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lScadenzariSige;
	}

	/**
	 * Ricerca record di scadenzario di un determinato tipo, con scadenza all'interno di un intervallo di
	 * date.
	 */
	public Vector ExRicercaScadenzarioSige(String aTipoScadenzario, Date aData1, Date aData2,
			UtenteModel lUteMod) throws F3BException {

		Connection lConn = null;
		Vector lScadenzariSige = new Vector();
		ScadenzarioSigeSqlDAO lScaDao = null;
		FascicoloSigeSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);
			lFascDao = new FascicoloSigeSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lEveDao = new EventoSqlDAO(lConn);

			lScaDao.ricercaScadenzarioSigePerTipoDate(aTipoScadenzario, aData1, aData2, lUteMod);
			lScadenzariSige = new Vector(lScaDao.getModels());
			if (lScadenzariSige.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {
				ScadenzarioSigeModel lScadModTmp = new ScadenzarioSigeModel();
				FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
				EventoModel lEvento = new EventoModel();
				SoggettoModel lSoggMod = new SoggettoModel();
				BigDecimal lFascID;
				BigDecimal lSoggID;
				BigDecimal lEveID;
				for (int i = 0; i < lScadenzariSige.size(); i++) {
					lScadModTmp = (ScadenzarioSigeModel) lScadenzariSige.get(i);
					lFascID = lScadModTmp.getFasIdFascicoloSige();
					if (lFascID != null) {
						// Cerca il fascicolo by key fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID FASCICOLO: " + lFascID);
						lFascDao.ricercaFascicoloSigeByKey(lFascID);
						lFascicolo = (FascicoloSigeModel) lFascDao.getModelByKey();

						lSoggID = lFascicolo.getSogIdSoggetto();
						if (lSoggID != null) {
							// Cerca il soggetto associato al fascicolo
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("ID SOGGETTO: " + lSoggID);
							lSoggDao.ricercaSoggettoByKey(lSoggID);
							lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

							lFascicolo.setSogIdSoggetto(lSoggID);

							lScadModTmp.setFascicoloSige(lFascicolo);
							lScadModTmp.setSoggetto(lSoggMod);
							lScadenzariSige.set(i, lScadModTmp);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("Effettuato aggiornamento Fascicolo e Soggetto");
						} else {
							throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Fascicolo");
						}
					} else {
						throw new F3BException(F3BException.USER_MESSAGE, "Manca ID Soggetto nel Fascicolo");
					}

					// 11/10/2004 Si Carica anche l'Evento.
					lEveID = lScadModTmp.getEveIdEvento();
					if (lEveID != null) {
						// Cerca l'Evento by key
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID EVENTO: " + lEveID);
						lEveDao.ricercaEventoByKey(lEveID);
						lEvento = (EventoModel) lEveDao.getModelByKey();
						lScadModTmp.setEvento(lEvento);
					}
				} // end for
			} // endif size
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExRicercaScadenzarioSige: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lScadenzariSige;
	}

	public ScadenzarioSigeModel ExModificaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeDAO lScaDao = null;
		ScadenzarioSigeModel lScaMod = new ScadenzarioSigeModel(aScadenzarioSige);

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeDAO(lConn);
			lScaDao.setDAOFromModelForUpdate(aScadenzarioSige);
			lScaDao.update();
			lScaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioSigeController.ExModificaScadenzarioSige: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	public void ExCancellaScadenzarioSige(ScadenzarioSigeModel aScadenzarioSige) throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeDAO(lConn);
			lScaDao.setCondizioneUpdate(aScadenzarioSige.getIdScadenzarioSige());
			lScaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSigeController.ExCancellaScadenzarioSige: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

	public ScadenzarioSigeModel ExRicercaScadenzarioSigeByIdFascicoloTipo(BigDecimal aIdFascicolo,
			String aTipo) throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeSqlDAO lScaDao = null;
		ScadenzarioSigeModel lScaMod;
		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSigeSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSigeByIdFascicoloTipo(aIdFascicolo, aTipo);
			lScaMod = (ScadenzarioSigeModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"ScadenzarioSigeController.ExRicercaScadenzarioSigeByIdFascicoloTipo: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Il metodo ritorna true se esiste il record di scadenzario per il fascicolo ed il tipo individuati dai
	 * parametri e se tale record ha data di scadenza inferiore alla data di sistema.
	 */
	public boolean ExScadutoScadenzarioSigeByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws F3BException {

		boolean ritorno = false;

		ScadenzarioSigeModel lScaMod = ExRicercaScadenzarioSigeByIdFascicoloTipo(aIdFascicolo, aTipo);
		if (lScaMod != null) {
			if (lScaMod.getDataFineScadenza() != null)
				if (lScaMod.getDataFineScadenza().before(DateUtils.getSysDate()))
					ritorno = true;
		}
		return ritorno;
	}

	/**
	 * @param aScadenzarioSige
	 * @return aScadenzarioSige
	 * @throws F3BException
	 */
	public ScadenzarioSigeModel ExModificaScadenzario(ScadenzarioSigeModel aScadenzarioSige)
			throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeDAO lScadDao = null;
		ScadenzarioSigeModel lEspMod = new ScadenzarioSigeModel(aScadenzarioSige);

		try {
			lConn = getDBConnection();
			lScadDao = new ScadenzarioSigeDAO(lConn);
			lScadDao.setDAOFromModelForUpdate(aScadenzarioSige);
			// lScadDao.update();
			lScadDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioController.ExModificaScadenzario: " + ex);
		} finally {
			cleanup(lScadDao);
			cleanup(lConn);
		}
		return lEspMod;
	}

	/**
	 * @param aScadenzarioSige
	 * @return void
	 * @throws F3BException
	 */
	public void ExSetVistoScadenzario(ScadenzarioSigeModel aScadenzarioSige) throws F3BException {

		Connection lConn = null;
		ScadenzarioSigeDAO lScadDao = null;
		// ScadenzarioSigeModel lEspMod = new ScadenzarioSigeModel(aScadenzarioSige);

		try {
			lConn = getDBConnection();
			lScadDao = new ScadenzarioSigeDAO(lConn);
			lScadDao.setDAOFromModelForUpdateVisto(aScadenzarioSige);
			// lScadDao.update();
			lScadDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioController.ExSetVistoScadenzario : " + ex);
		} finally {
			cleanup(lScadDao);
			cleanup(lConn);
		}
		return;
	}

}