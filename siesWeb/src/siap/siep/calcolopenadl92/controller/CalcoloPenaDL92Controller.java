package siap.siep.calcolopenadl92.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.calcolopena.model.SemestreDL92Model;
import siap.siep.calcolopenadl92.dao.CalcoloPenaDL92DAO;
import siap.siep.calcolopenadl92.dao.CalcoloPenaDL92SqlDAO;
import siap.siep.calcolopenadl92.dao.SemestreDL92DAO;
import siap.siep.calcolopenadl92.dao.SemestreDL92SqlDAO;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;

/**
 * Controller di accesso alla tabella CALCOLO_PENA_DL92
 *
 * @since MEV_2026-1
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CalcoloPenaDL92Controller extends SiapController implements ICalcoloPenaDL92 {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Effettua la insert
	 */
	public CalcoloPenaDL92ModelDB ExInserisciCalcoloPenaDL92(CalcoloPenaDL92ModelDB aCalcoloPenaModel,
			SemestreDL92Model aSemestrePresofferto, Vector<SemestreDL92Model> aListaSemestri)
			throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92DAO lCalcoloPenaDao = null;
		CalcoloPenaDL92ModelDB lCalcoloPenaMod = null;

		SemestreDL92DAO lSemestreDAO = null;
		lCalcoloPenaMod = new CalcoloPenaDL92ModelDB(aCalcoloPenaModel);
		try {
			lConn = getDBConnection();

			lCalcoloPenaDao = new CalcoloPenaDL92DAO(lConn);
			lCalcoloPenaDao.setDAOFromModel(aCalcoloPenaModel);
			BigDecimal lIdCalc = lCalcoloPenaDao.insert();
			lCalcoloPenaMod.setIdCalcoloPenaDL92(lIdCalc);

			lSemestreDAO = new SemestreDL92DAO(lConn);

			// Inserisco il "semestre" per il Presofferto
			if (aSemestrePresofferto != null) {
				siesLogger.debug("Inserisco il 'semestre' per il Presofferto...");
				lSemestreDAO.setDAOFromModel(aSemestrePresofferto);

				lSemestreDAO.setCalcIdCalcoloPenaDl92(lIdCalc);

				lSemestreDAO.setCodOperatoreInserimento(aCalcoloPenaModel.getCodOperatoreInserimento());
				lSemestreDAO.setDataInserimento(aCalcoloPenaModel.getDataInserimento());
				lSemestreDAO.setCodUfficioInserimento(aCalcoloPenaModel.getCodUfficioInserimento());

				lSemestreDAO.insert();
				lSemestreDAO.stop();
			}

			// Inserisco i semestri calcolati
			if (aListaSemestri != null) {
				siesLogger.debug("Inserisco i semestri...");

				for (SemestreDL92Model lSemestre : aListaSemestri) {
					// siesLogger.debug("Inserisco semestre = "+lSemestre.getProgressivo());

					lSemestreDAO.setDAOFromModel(lSemestre);

					lSemestreDAO.setCalcIdCalcoloPenaDl92(lIdCalc);

					lSemestreDAO.setCodOperatoreInserimento(aCalcoloPenaModel.getCodOperatoreInserimento());
					lSemestreDAO.setDataInserimento(aCalcoloPenaModel.getDataInserimento());
					lSemestreDAO.setCodUfficioInserimento(aCalcoloPenaModel.getCodUfficioInserimento());

					lSemestreDAO.insert();
					lSemestreDAO.stop();
				}
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.ExInserisciCalcoloPenaDL92", daoEx);
			rollback(lConn);
			throw new F3BException("CalcoloPenaDL92Controller.ExInserisciCalcoloPenaDL92: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.ExInserisciCalcoloPenaDL92", ex);
			rollback(lConn);
			throw new F3BException("CalcoloPenaDL92Controller.ExInserisciCalcoloPenaDL92: " + ex);
		} finally {
			cleanup(lCalcoloPenaDao);
			cleanup(lSemestreDAO);
			cleanup(lConn);
		}
		return lCalcoloPenaMod;
	}

	/**
	 * Effettua la cancellazione in chiave
	 */
	public void ExCancellaCalcoloPenaDL92(BigDecimal aIdCalcoloPena) throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92DAO lCalcoloPenaDao = null;
		SemestreDL92DAO lSemetriDAO = null;

		try {
			lConn = getDBConnection();

			// Cancello prima i semestri
			siesLogger.debug("Cancello prima i semestri ...");
			lSemetriDAO = new SemestreDL92DAO(lConn);
			lSemetriDAO.setCondizioneByIdCalc(aIdCalcoloPena);
			lSemetriDAO.delete();

			siesLogger.debug("Cancello il calcolo pena ...");
			lCalcoloPenaDao = new CalcoloPenaDL92DAO(lConn);
			lCalcoloPenaDao.setCondizioneUpdate(aIdCalcoloPena);
			lCalcoloPenaDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.ExCancellaCalcoloPenaDL92", daoEx);
			rollback(lConn);
			throw new F3BException("CalcoloPenaDL92Controller.ExCancellaCalcoloPenaDL92: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.ExCancellaCalcoloPenaDL92", ex);
			rollback(lConn);
			throw new F3BException("CalcoloPenaDL92Controller.ExCancellaCalcoloPenaDL92: " + ex);
		} finally {
			cleanup(lCalcoloPenaDao);
			cleanup(lSemetriDAO);
			cleanup(lConn);
		}
	}

	/**
	 * Effettua la ricerca in chiave senza join
	 */
	public CalcoloPenaDL92ModelDB ExRicercaCalcoloPenaDL92ById(BigDecimal aIdCalcoloPena)
			throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92DAO lCalcoloPenaDao = null;
		CalcoloPenaDL92ModelDB lCalcoloPenaMod = null;

		try {
			lConn = getDBConnection();
			lCalcoloPenaDao = new CalcoloPenaDL92DAO(lConn);
			lCalcoloPenaDao.setCondizioneUpdate(aIdCalcoloPena);
			lCalcoloPenaMod = (CalcoloPenaDL92ModelDB) lCalcoloPenaDao.getModelByKey();

		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ById", daoEx);
			throw new F3BException("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ById: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ById", ex);
			throw new F3BException("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ById: " + ex);
		} finally {
			cleanup(lCalcoloPenaDao);
			cleanup(lConn);
		}

		return lCalcoloPenaMod;
	}

	/**
	 * Effettua la ricerca dei record collegati al fascicolo ordinato per data inserimento
	 */
	public Vector<CalcoloPenaDL92ModelDB> ExRicercaCalcoloPenaDL92ByIdFas(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92SqlDAO lCalcoloPenaSqlDao = null;
		Vector<CalcoloPenaDL92ModelDB> lListaModel = null;

		try {
			lConn = getDBConnection();
			lCalcoloPenaSqlDao = new CalcoloPenaDL92SqlDAO(lConn);

			lCalcoloPenaSqlDao.ricercaCalcoloPenaDL92ByIdFasc(aIdFascicolo);

			lListaModel = new Vector<CalcoloPenaDL92ModelDB>(lCalcoloPenaSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFas", daoEx);
			throw new F3BException("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFas: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFas", ex);
			throw new F3BException("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFas: " + ex);
		} finally {
			cleanup(lCalcoloPenaSqlDao);
			cleanup(lConn);
		}

		return lListaModel;

	}

	/**
	 * Recupera il model aggregato
	 */
	public CalcoloPenaDL92ModelDB GetDettaglioStoricoById(BigDecimal aIdCalcoloPena) throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92SqlDAO lCalcoloPenaSqlDao = null;
		CalcoloPenaDL92ModelDB lCalcoloPenaMod = null;

		SemestreDL92SqlDAO lSemDL92SqlDAO = null;

		try {
			lConn = getDBConnection();
			lCalcoloPenaSqlDao = new CalcoloPenaDL92SqlDAO(lConn);
			lCalcoloPenaSqlDao.ricercaCalcoloPenaDL92ById(aIdCalcoloPena);
			lCalcoloPenaMod = (CalcoloPenaDL92ModelDB) lCalcoloPenaSqlDao.getModelByKey();

			lSemDL92SqlDAO = new SemestreDL92SqlDAO(lConn);
			lSemDL92SqlDAO.ricercaSemestriByIdCalc(aIdCalcoloPena);
			Vector<SemestreDL92Model> lListaSemestriDB = new Vector(lSemDL92SqlDAO.getModels());

			Vector<SemestreDL92Model> lListaSemestri = new Vector<>();
			lCalcoloPenaMod.setListaSemetri(lListaSemestri);
			// separo il semetre del presofferto dagli altri
			for (SemestreDL92Model lSemestre : lListaSemestriDB) {
				if ("S".equals(lSemestre.getIsPresofferto()))
					lCalcoloPenaMod.setSemestrePresofferto(lSemestre);
				else
					lListaSemestri.add(lSemestre);
			}

		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.GetDettaglioStoricoById", daoEx);
			throw new F3BException("CalcoloPenaDL92Controller.GetDettaglioStoricoById: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.GetDettaglioStoricoById", ex);
			throw new F3BException("CalcoloPenaDL92Controller.GetDettaglioStoricoById: " + ex);
		} finally {
			cleanup(lCalcoloPenaSqlDao);
			cleanup(lSemDL92SqlDAO);

			cleanup(lConn);
		}

		return lCalcoloPenaMod;
	}

	/**
	 * Restituisce, se presente, il calcolo DL92 validato calcolato più recentemente su fascicolo passato in
	 * input
	 */
	public CalcoloPenaDL92ModelDB GetLastCalcoloDL92(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92SqlDAO lCalcoloPenaSqlDao = null;
		CalcoloPenaDL92ModelDB lLastCalcolo = null;

		try {
			lConn = getDBConnection();
			lCalcoloPenaSqlDao = new CalcoloPenaDL92SqlDAO(lConn);

			lCalcoloPenaSqlDao.ricercaCalcoloPenaDL92ByIdFasc(aIdFascicolo);

			Vector<CalcoloPenaDL92ModelDB> lListaModel = null;
			lListaModel = new Vector<CalcoloPenaDL92ModelDB>(lCalcoloPenaSqlDao.getModels());

			int size = lListaModel.size();

			if (size > 0)
				lLastCalcolo = lListaModel.elementAt(size - 1);

		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.GetLastCalcoloDL92", daoEx);
			throw new F3BException("CalcoloPenaDL92Controller.GetLastCalcoloDL92: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.GetLastCalcoloDL92", ex);
			throw new F3BException("CalcoloPenaDL92Controller.GetLastCalcoloDL92: " + ex);
		} finally {
			cleanup(lCalcoloPenaSqlDao);
			cleanup(lConn);
		}

		return lLastCalcolo;
	}

	/**
	 * Effettua la ricerca dei record collegati al fascicolo ordinato per data inserimento Recupera ache i
	 * semestri
	 */
	public Vector<CalcoloPenaDL92ModelDB> ExRicercaCalcoloPenaDL92ByIdFasCompleta(BigDecimal aIdFascicolo)
			throws F3BException {

		Connection lConn = null;
		CalcoloPenaDL92SqlDAO lCalcoloPenaSqlDao = null;
		SemestreDL92SqlDAO lSemDL92SqlDAO = null;
		Vector<CalcoloPenaDL92ModelDB> lListaModel = null;

		try {
			lConn = getDBConnection();
			lCalcoloPenaSqlDao = new CalcoloPenaDL92SqlDAO(lConn);

			lCalcoloPenaSqlDao.ricercaCalcoloPenaDL92ByIdFasc(aIdFascicolo);

			lListaModel = new Vector<CalcoloPenaDL92ModelDB>(lCalcoloPenaSqlDao.getModels());

			lSemDL92SqlDAO = new SemestreDL92SqlDAO(lConn);

			for (CalcoloPenaDL92ModelDB lCalcolo : lListaModel) {
				lSemDL92SqlDAO.ricercaSemestriByIdCalc(lCalcolo.getIdCalcoloPenaDL92());
				Vector<SemestreDL92Model> lListaSemestriDB = new Vector(lSemDL92SqlDAO.getModels());
				lCalcolo.setListaSemetri(lListaSemestriDB);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFasCompleta", daoEx);
			throw new F3BException(
					"CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFasCompleta: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFasCompleta", ex);
			throw new F3BException(
					"CalcoloPenaDL92Controller.ExRicercaCalcoloPenaDL92ByIdFasCompleta: " + ex);
		} finally {
			cleanup(lCalcoloPenaSqlDao);
			cleanup(lSemDL92SqlDAO);
			cleanup(lConn);
		}

		return lListaModel;

	}

}