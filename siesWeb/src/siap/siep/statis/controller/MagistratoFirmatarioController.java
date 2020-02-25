package siap.siep.statis.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.magistrato.model.MagistratoModel;
import siap.siep.statis.dao.MagistratoFirmatarioSqlDAO;
import f3b.controller.GenericController;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MagistratoFirmatarioController
 * </p>
 * <p>
 * Description: Controller per il magistrato firmatario
 * </p>
 */
public class MagistratoFirmatarioController extends GenericController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// public Vector<MagistratoModel> ExRicercaMagistratiFirmatari(String aUfficio, String aInizio, String
	// aFine, String Accorpato1, String Accorpato2, String Accorpato3)
	public Vector<MagistratoModel> ExRicercaMagistratiFirmatari(String aUfficio, String aInizio, String aFine)
			throws F3BException {
		Connection lConn = null;
		Vector<MagistratoModel> lMagistrati = new Vector<MagistratoModel>();
		MagistratoFirmatarioSqlDAO lMagSqlDao = null;

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoFirmatarioSqlDAO(lConn);
			// lMagSqlDao.ricercaMagistratoFirmatario( aUfficio, aInizio, aFine, Accorpato1, Accorpato2,
			// Accorpato3);
			lMagSqlDao.ricercaMagistratoFirmatario(aUfficio, aInizio, aFine);
			lMagSqlDao.start();

			while (lMagSqlDao.next()) {
				lMagistrati.add((MagistratoModel) lMagSqlDao.getModel());
			}
			if (lMagistrati.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MagistratoFirmatarioController.ExRicercaMagistratiFirmatari: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	// public Vector<MagistratoModel> ExRicercaMagistratiFirmatari(String aUfficio, String aInizio, String
	// aFine, String Accorpato1, String Accorpato2, String Accorpato3)
	public MagistratoModel ExRicercaW_MagistratoByCod(String aCod) throws F3BException {
		Connection lConn = null;
		MagistratoModel MagModel = new MagistratoModel();
		MagistratoFirmatarioSqlDAO lMagSqlDao = null;

		try {
			lConn = getDBConnection();
			lMagSqlDao = new MagistratoFirmatarioSqlDAO(lConn);
			lMagSqlDao.ricercaW_Magistrato(aCod);
			lMagSqlDao.start();

			MagModel = (MagistratoModel) lMagSqlDao.getModelByKey();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MagistratoFirmatarioController.ExRicercaW_MagistratoByCod: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lMagSqlDao);
			cleanup(lConn);
		}

		return MagModel;

	} // Chiude ExRicercaW_MagistratoByCod

} // Chiude controller