package siap.sius.scadenzario.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.scadenzario.dao.ScadenzarioSiusDAO;
import siap.sius.scadenzario.dao.ScadenzarioSiusSqlDAO;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ScadenzarioSiusController
 * </p>
 * <p>
 * Description: Classe Controller per ScadenzarioSius
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
public class ScadenzarioSiusController extends SiapController implements IScadenzarioSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ScadenzarioSiusModel ExInserisciScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusDAO lScaDao = null;
		ScadenzarioSiusModel lScaMod = null;

		try {
			lConn = getDBConnection();
			lScaMod = new ScadenzarioSiusModel(aScadenzarioSius);
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModel(aScadenzarioSius);
			BigDecimal lKey = null;
			lKey = lScaDao.insert();
			commit(lConn);
			lScaMod.setIdScadenzarioSius(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioSiusController.ExInserisci: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Ricerca i Tipi Scadenzari Sius per l'ufficio Collegato.
	 * <p>
	 * 
	 * @param aTipoUfficio
	 * @return Vettore di Tipi Scadenzari SIUS
	 * @throws F3BException
	 */

	public Vector ExElencoTipiScadenzarioByTipoUfficio(String aTipoUfficio) throws F3BException {
		Connection lConn = null;
		Vector lTipiScadenzariSius = new Vector();
		ScadenzarioSiusSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);

			lScaDao.ricercaTipiScadenzariSiusByTipoUfficio(aTipoUfficio);
			lScaDao.start();

			while (lScaDao.next()) {

				String lCod = lScaDao.getString("COD_TIPO_SCADENZARIO");
				String lDesc = lScaDao.getString("DESCR_TIPO_SCADENZARIO");

				lTipiScadenzariSius.add(new DecodeModel(lCod, " " + lDesc + " " + " "));
			}

			lScaDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSiusController.ExElencoTipiScadenzarioByTipoUfficio: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}

		if (lTipiScadenzariSius.size() == 0) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Nessun Tipo Scadenzario codificato in Archivio");
		}
		return lTipiScadenzariSius;
	}

	public Vector ExRicercaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException {
		Connection lConn = null;
		Vector lScadenzariSius = new Vector();
		ScadenzarioSiusSqlDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSius(aScadenzarioSius);
			lScadenzariSius = new Vector(lScaDao.getModels());
			if (lScadenzariSius.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSiusController.ExRicercaScadenzarioSius: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScadenzariSius;
	}

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusSqlDAO lScaDao = null;
		ScadenzarioSiusModel lScaMod;
		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSiusByKey(aKey);
			lScaMod = (ScadenzarioSiusModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSiusController.ExRicercaScadenzarioSiusByKey : " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Ricerca record con scadenza all'interno di un intervallo di date.
	 *
	 */
	public Vector ExRicercaScadenzarioSius(Date aData1, Date aData2) throws F3BException {
		Connection lConn = null;
		Vector lScadenzariSius = new Vector();
		ScadenzarioSiusSqlDAO lScaDao = null;
		FascicoloSiusSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);

			lScaDao.ricercaScadenzarioSiusPerDate(aData1, aData2);
			lScadenzariSius = new Vector(lScaDao.getModels());
			if (lScadenzariSius.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {
				lFascDao = new FascicoloSiusSqlDAO(lConn);
				lSoggDao = new SoggettoSqlDAO(lConn);
				lEveDao = new EventoSqlDAO(lConn);

				ScadenzarioSiusModel lScadModTmp = new ScadenzarioSiusModel();
				FascicoloSiusModel lFascicolo = new FascicoloSiusModel();
				SoggettoModel lSoggMod = new SoggettoModel();
				BigDecimal lFascID;
				BigDecimal lSoggID;
				EventoModel lEvento = new EventoModel();
				BigDecimal lEveID;

				for (int i = 0; i < lScadenzariSius.size(); i++) {
					lScadModTmp = (ScadenzarioSiusModel) lScadenzariSius.get(i);
					lFascID = lScadModTmp.getFasSiuIdFascicoloSius();
					if (lFascID != null) {
						// Cerca il fascicolo by key fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID FASCICOLO: " + lFascID);
						lFascDao.ricercaFascicoloByKey(lFascID);
						lFascicolo = (FascicoloSiusModel) lFascDao.getModelByKey();

						lSoggID = lFascicolo.getSogIdSoggetto();
						if (lSoggID != null) {
							// Cerca il soggetto associato al fascicolo
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("ID SOGGETTO: " + lSoggID);
							lSoggDao.ricercaSoggettoByKey(lSoggID);
							lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

							lFascicolo.setSoggetto(lSoggMod);

							lScadModTmp.setFascicoloSius(lFascicolo);
							lScadenzariSius.set(i, lScadModTmp);
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
			throw new F3BException("ScadenzarioSiusController.ExRicercaScadenzarioSius: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lScadenzariSius;
	}

	/**
	 * Ricerca record di scadenzario di un determinato tipo, con scadenza all'interno di un intervallo di
	 * date.
	 *
	 */
	public Vector ExRicercaScadenzarioSius(String aTipoScadenzario, Date aData1, Date aData2)
			throws F3BException {
		Connection lConn = null;
		Vector lScadenzariSius = new Vector();
		ScadenzarioSiusSqlDAO lScaDao = null;
		FascicoloSiusSqlDAO lFascDao = null;
		SoggettoSqlDAO lSoggDao = null;
		EventoSqlDAO lEveDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);
			lFascDao = new FascicoloSiusSqlDAO(lConn);
			lSoggDao = new SoggettoSqlDAO(lConn);
			lEveDao = new EventoSqlDAO(lConn);

			lScaDao.ricercaScadenzarioSiusPerTipoDate(aTipoScadenzario, aData1, aData2);
			lScadenzariSius = new Vector(lScaDao.getModels());
			if (lScadenzariSius.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			} else {
				ScadenzarioSiusModel lScadModTmp = new ScadenzarioSiusModel();
				FascicoloSiusModel lFascicolo = new FascicoloSiusModel();
				EventoModel lEvento = new EventoModel();
				SoggettoModel lSoggMod = new SoggettoModel();
				BigDecimal lFascID;
				BigDecimal lSoggID;
				BigDecimal lEveID;
				for (int i = 0; i < lScadenzariSius.size(); i++) {
					lScadModTmp = (ScadenzarioSiusModel) lScadenzariSius.get(i);
					lFascID = lScadModTmp.getFasSiuIdFascicoloSius();
					if (lFascID != null) {
						// Cerca il fascicolo by key fascicolo
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.warn("ID FASCICOLO: " + lFascID);
						lFascDao.ricercaFascicoloByKey(lFascID);
						lFascicolo = (FascicoloSiusModel) lFascDao.getModelByKey();

						lSoggID = lFascicolo.getSogIdSoggetto();
						if (lSoggID != null) {
							// Cerca il soggetto associato al fascicolo
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.warn("ID SOGGETTO: " + lSoggID);
							lSoggDao.ricercaSoggettoByKey(lSoggID);
							lSoggMod = (SoggettoModel) lSoggDao.getModelByKey();

							lFascicolo.setSoggetto(lSoggMod);

							lScadModTmp.setFascicoloSius(lFascicolo);
							lScadenzariSius.set(i, lScadModTmp);
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
			throw new F3BException("ScadenzarioSiusController.ExRicercaScadenzarioSius: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lFascDao);
			cleanup(lSoggDao);
			cleanup(lEveDao);
			cleanup(lConn);
		}
		return lScadenzariSius;
	}

	public ScadenzarioSiusModel ExModificaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusDAO lScaDao = null;
		ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel(aScadenzarioSius);

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setDAOFromModelForUpdate(aScadenzarioSius);
			lScaDao.update();
			lScaDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ScadenzarioSiusController.ExModificaScadenzarioSius: " + ex);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	public void ExCancellaScadenzarioSius(ScadenzarioSiusModel aScadenzarioSius) throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusDAO lScaDao = null;

		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusDAO(lConn);
			lScaDao.setCondizioneUpdate(aScadenzarioSius.getIdScadenzarioSius());
			lScaDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSiusController.ExCancellaScadenzarioSius: " + daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
	}

	public ScadenzarioSiusModel ExRicercaScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo,
			String aTipo) throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusSqlDAO lScaDao = null;
		ScadenzarioSiusModel lScaMod;
		try {
			lConn = getDBConnection();
			lScaDao = new ScadenzarioSiusSqlDAO(lConn);
			lScaDao.ricercaScadenzarioSiusByIdFascicoloTipo(aIdFascicolo, aTipo);
			lScaMod = (ScadenzarioSiusModel) lScaDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ScadenzarioSiusController.ExRicercaScadenzarioSiusByIdFascicoloTipo: "
					+ daoEx);
		} finally {
			cleanup(lScaDao);
			cleanup(lConn);
		}
		return lScaMod;
	}

	/**
	 * Il metodo ritorna true se esiste il record di scadenzario per il fascicolo ed il tipo individuati dai
	 * parametri e se tale record ha data di scadenza inferiore alla data di sistema.
	 *
	 */
	public boolean ExScadutoScadenzarioSiusByIdFascicoloTipo(BigDecimal aIdFascicolo, String aTipo)
			throws F3BException {
		boolean ritorno = false;

		ScadenzarioSiusModel lScaMod = ExRicercaScadenzarioSiusByIdFascicoloTipo(aIdFascicolo, aTipo);
		if (lScaMod != null) {
			if (lScaMod.getDataFineScadenza() != null)
				if (lScaMod.getDataFineScadenza().before(DateUtils.getSysDate()))
					ritorno = true;
		}
		return ritorno;
	}

	/**
	 *
	 * @param aScadenzarioSius
	 * @return aScadenzarioSius
	 * @throws F3BException
	 */
	public ScadenzarioSiusModel ExModificaScadenzario(ScadenzarioSiusModel aScadenzarioSius)
			throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusDAO lScadDao = null;
		ScadenzarioSiusModel lEspMod = new ScadenzarioSiusModel(aScadenzarioSius);

		try {
			lConn = getDBConnection();
			lScadDao = new ScadenzarioSiusDAO(lConn);
			lScadDao.setDAOFromModelForUpdate(aScadenzarioSius);
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
	 *
	 * @param aScadenzarioSius
	 * @return void
	 * @throws F3BException
	 */
	public void ExSetVistoScadenzario(ScadenzarioSiusModel aScadenzarioSius) throws F3BException {
		Connection lConn = null;
		ScadenzarioSiusDAO lScadDao = null;
		// ScadenzarioSiusModel lEspMod = new ScadenzarioSiusModel(aScadenzarioSius);

		try {
			lConn = getDBConnection();
			lScadDao = new ScadenzarioSiusDAO(lConn);
			lScadDao.setDAOFromModelForUpdateVisto(aScadenzarioSius);
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