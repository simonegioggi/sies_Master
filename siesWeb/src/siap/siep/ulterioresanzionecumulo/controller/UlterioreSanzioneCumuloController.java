package siap.siep.ulterioresanzionecumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.ulterioresanzionecumulo.dao.UlterioreSanzioneCumuloDAO;
import siap.siep.ulterioresanzionecumulo.dao.UlterioreSanzioneCumuloSqlDAO;
import siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>Title: UlterioreSanzioneCumuloController</p>
 * <p>Description: Classe Controller per UlterioreSanzioneCumulo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UlterioreSanzioneCumuloController
    extends SiapController
    implements IUlterioreSanzioneCumulo
 {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public UlterioreSanzioneCumuloModel ExInserisciUlterioreSanzioneCumulo(
			UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo) throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloDAO lUltDao = null;
		UlterioreSanzioneCumuloModel lUltMod = null;

		try {
			lConn = getDBConnection();
			lUltMod = new UlterioreSanzioneCumuloModel(aUlterioreSanzioneCumulo);
			lUltDao = new UlterioreSanzioneCumuloDAO(lConn);
			lUltDao.setDAOFromModel(aUlterioreSanzioneCumulo);
			BigDecimal lKey = null;
			lKey = lUltDao.insert();
			commit(lConn);
			lUltMod.setIdUlterioreSanzioneCumulo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UlterioreSanzioneCumuloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUltMod;
	}

	public UlterioreSanzioneCumuloModel ExInserisciOModificaUlterioriSanzioniCumulo(Vector aSanzioni,
			BigDecimal aIdFas) throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloDAO lUltDao = null;
		UlterioreSanzioneCumuloSqlDAO lUltSqlDao = null;

		UlterioreSanzioneCumuloModel lUltMod = null;
//		UlterioreSanzioneCumuloModel lUltModEsist = null;

		try {
			lConn = getDBConnection();
			lUltMod = new UlterioreSanzioneCumuloModel();
			lUltSqlDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			lUltDao = new UlterioreSanzioneCumuloDAO(lConn);

			BigDecimal lKey = null;
			// Cla
			if (!aSanzioni.isEmpty())
				lUltMod = (UlterioreSanzioneCumuloModel) aSanzioni.get(0);
			else
				lUltMod = new UlterioreSanzioneCumuloModel();
			lUltDao.setDAOFromModelForDeleteUlterioriSanz(lUltMod, aIdFas);
			lUltDao.delete();
			lUltDao.stop();

			for (int i = 0; i < aSanzioni.size(); i++) {
				UlterioreSanzioneCumuloModel lUltSan = new UlterioreSanzioneCumuloModel();
				lUltSan = (UlterioreSanzioneCumuloModel) aSanzioni.get(i);

				if (lUltSan.getCodTipoUlterioreSanzione() != null) {
					lUltDao.setDAOFromModel(lUltSan);
					lKey = lUltDao.insert();
					lUltDao.stop();
				}

			}
			// End Cla
			commit(lConn);
			lUltMod.setIdUlterioreSanzioneCumulo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UlterioreSanzioneCumuloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lUltDao);
			cleanup(lUltSqlDao);

			cleanup(lConn);
		}

		return lUltMod;
	}

	public Vector ExRicercaUlterioreSanzioneCumulo(UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo)
			throws F3BException {
		Connection lConn = null;
		Vector lUlterioreSanzioneCumuli = new Vector();
		UlterioreSanzioneCumuloSqlDAO lUltDao = null;

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			lUltDao.ricercaUlterioreSanzioneCumulo(aUlterioreSanzioneCumulo);
			lUlterioreSanzioneCumuli = new Vector(lUltDao.getModels());
			if (lUlterioreSanzioneCumuli.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UlterioreSanzioneCumuloController.ExRicercaUlterioreSanzioneCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUlterioreSanzioneCumuli;
	}

	public UlterioreSanzioneCumuloModel ExRicercaUlterioreSanzioneCumuloByKey(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloSqlDAO lUltDao = null;

		UlterioreSanzioneCumuloModel lUltMod;

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			lUltDao.ricercaUlterioreSanzioneCumuloByKey(aKey);
			lUltMod = (UlterioreSanzioneCumuloModel) lUltDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UlterioreSanzioneCumuloController.ExRicercaUlterioreSanzioneCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUltMod;
	}

	public Vector ExRicercaUlterioreSanzioneCumuloByFascicoloCumulante(FascicoloSiepModel aModel)
			throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloSqlDAO lUltDao = null;
		Vector lUlterioreSanzioneCumuli = new Vector();

//		UlterioreSanzioneCumuloModel lUltMod;

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			lUltDao.ricercaUlterioreSanzioneCumuloByFascicoloCumulante(aModel);
			lUltDao.start();

			while (lUltDao.next()) {
				lUlterioreSanzioneCumuli.add((UlterioreSanzioneCumuloModel) lUltDao.getModel());
			}

			lUltDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"UlterioreSanzioneCumuloController.ExRicercaUlterioreSanzioneCumuloByFascicoloCumulante: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUlterioreSanzioneCumuli;
	}

	/**
	 * Ricerca tutti i record Ulteriore_sanzione_cumulo collegati al fascicolo specificato e al CUMULO
	 * specificato.
	 * 
	 * @param aIdFasc
	 *            - id del fascicolo cumulante
	 * @param aIdCum
	 *            - id del CUMULO
	 * @return Vector di UlterioreSanzioneCumuloModel
	 * @throws F3BException
	 */
	public Vector ExRicercaUlterioreSanzioneCumuloByFascicoloIDCumul0(BigDecimal aIdFasc, BigDecimal aIdCum)
			throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloSqlDAO lUltDao = null;
		Vector lUlterioreSanzioneCumuli = new Vector();

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloSqlDAO(lConn);
			lUltDao.ricercaUlterioreSanzioneCumuloByFascicoloIdCumulo(aIdFasc, aIdCum);
			lUltDao.start();

			while (lUltDao.next()) {
				lUlterioreSanzioneCumuli.add((UlterioreSanzioneCumuloModel) lUltDao.getModel());
			}

			lUltDao.stop();

		} catch (DAOException daoEx) {
			throw new F3BException(
					"UlterioreSanzioneCumuloController.ExRicercaUlterioreSanzioneCumuloByFascicoloCumulante: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUlterioreSanzioneCumuli;
	}

	public UlterioreSanzioneCumuloModel ExModificaUlterioreSanzioneCumulo(
			UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo) throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloDAO lUltDao = null;
		UlterioreSanzioneCumuloModel lUltMod = new UlterioreSanzioneCumuloModel(aUlterioreSanzioneCumulo);

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloDAO(lConn);
			lUltDao.setDAOFromModelForUpdate(aUlterioreSanzioneCumulo);
			lUltDao.update();
			lUltDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("UlterioreSanzioneCumuloController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
		return lUltMod;
	}

	public void ExCancellaUlterioreSanzioneCumulo(UlterioreSanzioneCumuloModel aUlterioreSanzioneCumulo)
			throws F3BException {
		Connection lConn = null;
		UlterioreSanzioneCumuloDAO lUltDao = null;

		try {
			lConn = getDBConnection();
			lUltDao = new UlterioreSanzioneCumuloDAO(lConn);
			lUltDao.setCondizioneUpdate(aUlterioreSanzioneCumulo.getIdUlterioreSanzioneCumulo());
			lUltDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UlterioreSanzioneCumuloController.ExCancellaUlterioreSanzioneCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lUltDao);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua l'inserimento di un Elenco ULTERIORE_SANZIONE_CUMULO <br>
	 *
	 * @param aUltSanCumuli
	 *            - Elenco UlterioreSanzioneCumuli
	 * @param lConn
	 *            - Connection parametro
	 * @return String - Rapporto Operazione
	 */
	public String ExInserisciUlterioreSanzioneCumuloWithoutSequence(ArrayList aUltSanCumuli, Connection lConn)
			throws F3BException {

		String lCodEsito = "00000";
		UlterioreSanzioneCumuloDAO lUltSanCumuloDao = null;
		UlterioreSanzioneCumuloModel lUltSanCumuloMod = null;
		try {
			lUltSanCumuloDao = new UlterioreSanzioneCumuloDAO(lConn);

			if (aUltSanCumuli != null && aUltSanCumuli.size() > 0) {
				for (int i = 0; i < aUltSanCumuli.size(); i++) {
					lUltSanCumuloMod = (UlterioreSanzioneCumuloModel) aUltSanCumuli.get(i);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("UlterioreSanzioneCumulo da inserire = " + lUltSanCumuloMod);
					if (lUltSanCumuloMod != null && lUltSanCumuloMod.getIdUlterioreSanzioneCumulo() != null) {
						lUltSanCumuloDao.setDAOFromModel(lUltSanCumuloMod);
						lUltSanCumuloDao.setWithoutSequence(true);
						lUltSanCumuloDao.insert();
						lUltSanCumuloDao.stop();
					}
				}
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.warn("UlterioreSanzioneCumulo gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error(F3BException.USER_MESSAGE
						+ " Impossibile inserire l'UlterioreSanzioneCumulo! ");
			}
		} finally {
			cleanup(lUltSanCumuloDao);
		}
		return lCodEsito;
	}

}