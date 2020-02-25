package siap.siep.luogodetenzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneSqlDAO;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.sius.SIUSException;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: LuogoDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per LuogoDetenzione
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
public class LuogoDetenzioneController extends SiapController implements ILuogoDetenzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public LuogoDetenzioneModel ExInserisciLuogoDetenzioneSius(LuogoDetenzioneModel aLuogoDetenzioneOld,
			LuogoDetenzioneModel aLuogoDetenzione, AltraCausaModel aAltraCausa) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod = null;
		AltraCausaDAO lAltraCausaDao = null;
//		AltraCausaModel lAltraCausaMod = null;

		// Prendo la connessione
		lConn = getDBTransaction();

		try {

			// Update ultimo record LuogoDetenzione
			if (aLuogoDetenzioneOld != null) {
				lLuoDao = new LuogoDetenzioneDAO(lConn);
				lLuoDao.setDAOFromModelForUpdate(aLuogoDetenzioneOld);
				lLuoDao.update();
				lLuoDao.stop();
			}

			// Inserisco nuovo record LuogoDetenzione
			lLuoMod = new LuogoDetenzioneModel(aLuogoDetenzione);
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setDAOFromModel(aLuogoDetenzione);
			BigDecimal lKey = null;
			lKey = lLuoDao.insert();
			lLuoMod.setIdLuogoDetenzione(lKey);

			// Update Altra Causa
			if (aAltraCausa != null) {
				lAltraCausaDao = new AltraCausaDAO(lConn);
				lAltraCausaDao.setDAOFromModelForUpdate(aAltraCausa);
				lAltraCausaDao.update();
				lAltraCausaDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("LuogoDetenzioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lLuoDao);
			cleanup(lAltraCausaDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	public LuogoDetenzioneModel ExInserisciLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione)
			throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod = null;

		try {
			lConn = getDBConnection();
			lLuoMod = new LuogoDetenzioneModel(aLuogoDetenzione);
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setDAOFromModel(aLuogoDetenzione);
			BigDecimal lKey = null;
			lKey = lLuoDao.insert();
			commit(lConn);
			lLuoMod.setIdLuogoDetenzione(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("LuogoDetenzioneController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	public Vector ExRicercaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException {
		Connection lConn = null;
		Vector lLuogoDetenzioni = new Vector();
		LuogoDetenzioneSqlDAO lLuoDao = null;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetenzione(aLuogoDetenzione);
			lLuogoDetenzioni = new Vector(lLuoDao.getModels());
			if (lLuogoDetenzioni.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("LuogoDetenzioneController.ExRicercaLuogoDetenzione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return lLuogoDetenzioni;
	}

	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetenzioneByKey(aKey);
			lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
			if (lLuoMod != null)
				lLuoMod = RicercaIstitutoDetenzione(lLuoMod, lConn);

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("LuogoDetenzioneController.ExRicercaLuogoDetenzione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	/**
	 * Ricerca Luogo Det By Fascicolo
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public LuogoDetenzioneModel ExRicercaLuogoDetByFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetByFascicolo(aKey);
			lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("LuogoDetenzioneController.ExRicercaLuogoDetenzione: Non posso leggere : "
					+ daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	/**
	 * Ricerca Luogo detenzione Corrente
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetenzioneCorrenteByFascicoloSiep(aKey);
			lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
			// modifica relativa al tipo istituto
			if (lLuoMod != null) {
				lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
				lIstDao.ricercaIstitutoDetenzioneByKey(lLuoMod.getIstDetIdIstitutoDetenzione());
				lIstModel = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lLuoMod.setIstitutoDetenzione(lIstModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LuogoDetenzioneController.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	/**
	 * Ricerca Luogo detenzione Corrente
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public LuogoDetenzioneModel ExRicercaLuogoDetenzioneCorrenteByFascicoloSius(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneSqlDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod;
		// modifica relativa al tipo istituto
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(lConn);
			lLuoDao.ricercaLuogoDetenzioneCorrenteByFascicoloSius(aKey);
			lLuoMod = (LuogoDetenzioneModel) lLuoDao.getModelByKey();
			// modifica relativa al tipo istituto
			if (lLuoMod != null) {
				lIstDao = new IstitutoDetenzioneSqlDAO(lConn);
				lIstDao.ricercaIstitutoDetenzioneByKey(lLuoMod.getIstDetIdIstitutoDetenzione());
				lIstModel = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				lLuoMod.setIstitutoDetenzione(lIstModel);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LuogoDetenzioneController.ExRicercaLuogoDetenzioneCorrenteByFascicoloSiep: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lIstDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	public LuogoDetenzioneModel ExModificaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione)
			throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;
		LuogoDetenzioneModel lLuoMod = new LuogoDetenzioneModel(aLuogoDetenzione);

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setDAOFromModelForUpdate(aLuogoDetenzione);
			lLuoDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("LuogoDetenzioneController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return lLuoMod;
	}

	public void ExCancellaLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setCondizioneUpdate(aLuogoDetenzione.getIdLuogoDetenzione());
			lLuoDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"LuogoDetenzioneController.ExCancellaLuogoDetenzione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
	}

	/**
	 * Genera il Vector per l'Elenco dei Luoghi detenzione Sius
	 * 
	 * @param aKey
	 * @return dati del soggetto come TreeModel.
	 * @throws SIUSException
	 *             propaga l'errore di eccezione.
	 */
	public Vector ExElencoDatiLuogoDetenzioneSius(BigDecimal aKey) throws F3BException {
		Vector lLuoMod = new Vector(); // new TreeModel();
		Vector lLuoModOut = new Vector(); // new TreeModel();
		LuogoDetenzioneSqlDAO lLuoDao = null;
		// IstitutoDetenzioneSqlDAO lIstDao = null;
		// IstitutoDetenzioneModel lIstModel = null;
		Connection aConn = null;

		try {
			aConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneSqlDAO(aConn);
			lLuoDao.ricercaLuoghiDetenzioneByFascicoloSius(aKey);
			lLuoMod = new Vector(lLuoDao.getModels());

			if (lLuoMod != null) {
				LuogoDetenzioneModel lLuoModTemp = null;
				Iterator lItx = lLuoMod.iterator();
				while (lItx.hasNext()) {
					lLuoModTemp = new LuogoDetenzioneModel((LuogoDetenzioneModel) lItx.next());

					if (lLuoModTemp.getIstDetIdIstitutoDetenzione() != null) {
						lLuoModTemp = RicercaIstitutoDetenzione(lLuoModTemp, aConn);
					} else {
						lLuoModTemp.setDescrTipoIstituto(lLuoModTemp.getAltroLuogo());
					}
					lLuoModOut.add(lLuoModTemp);
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.debug("##### Dati prelevati nel metodo prelevaDatiLuogoDetenzione : " + lLuoMod);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("luogodetenzione.controller.prelevaDatiLuogoDetenzione : " + daoEx);
		} finally {
			cleanup(lLuoDao);
			cleanup(aConn);
		}

		return lLuoModOut;
	}

	private LuogoDetenzioneModel RicercaIstitutoDetenzione(LuogoDetenzioneModel aLuoModTemp, Connection aConn)
			throws F3BException {
		IstitutoDetenzioneSqlDAO lIstDao = null;
		IstitutoDetenzioneModel lIstModel = null;
		try {

			if (aLuoModTemp.getIstDetIdIstitutoDetenzione() != null) {

				lIstDao = new IstitutoDetenzioneSqlDAO(aConn);
				lIstDao.ricercaIstitutoDetenzioneByKey(aLuoModTemp.getIstDetIdIstitutoDetenzione());
				lIstModel = (IstitutoDetenzioneModel) lIstDao.getModelByKey();
				aLuoModTemp.setIstitutoDetenzione(lIstModel);
				aLuoModTemp
						.setDescrTipoIstituto(StringUtils.toStringJSP(aLuoModTemp.getIstitutoDetenzione()
								.getDescrTipoIstituto())
								+ " di "
								+ StringUtils.toStringJSP(aLuoModTemp.getIstitutoDetenzione()
										.getDescrComune())
								+ " - "
								+ StringUtils.toStringJSP(aLuoModTemp.getIstitutoDetenzione().getIndirizzo()));
				aLuoModTemp.setDescrLuogo(aLuoModTemp.getIstitutoDetenzione().getDescrComune());

			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("luogodetenzione.controller.RicercaIstitutoDetenzione : " + daoEx);
		} finally {
			cleanup(lIstDao);
		}
		return aLuoModTemp;
	}

	/**
	 * Inserimento Luogo detenzione
	 * 
	 * @param aLuogoDetenzione
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciLuogoDetenzioneWithoutSequence(LuogoDetenzioneModel aLuogoDetenzione,
			Connection lConn) throws F3BException {

		LuogoDetenzioneDAO lLuoDao = null;
		String lCodEsito = "00000";

		try {
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setDAOFromModel(aLuogoDetenzione);
			lLuoDao.setWithoutSequence(true);
			lLuoDao.insert();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// Inizializzo la chiave del fascicolo con quella inviatami
				// lChiave = lPars.getFascicolo().getIdFascicoloSiep();
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire il Luogo Detenzione! ");
			}
		} finally {
			cleanup(lLuoDao);
		}

		return lCodEsito;

	}

	public void ExModificaDataFineLuogoDetenzione(LuogoDetenzioneModel aLuogoDetenzione) throws F3BException {
		Connection lConn = null;
		LuogoDetenzioneDAO lLuoDao = null;

		try {
			lConn = getDBConnection();
			lLuoDao = new LuogoDetenzioneDAO(lConn);
			lLuoDao.setDAOFromModelForUpdateDataFine(aLuogoDetenzione);
			lLuoDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException(
					"LuogoDetenzioneController.ExModificaDataFineLuogoDetenzione: Non posso inserire: " + ex);
		} finally {
			cleanup(lLuoDao);
			cleanup(lConn);
		}
		return;
	}

}