package siap.sico.utenzaAdn.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.utenzaAdn.dao.AssocUtenteSiesAdnDAO;
import siap.sico.utenzaAdn.dao.AssocUtenteSiesAdnSqlDAO;
import siap.sico.utenzaAdn.model.AssocUtenteSiesAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class AssocUtenteSiesAdnController extends SiapController implements IAssocUtenteSiesAdn {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("unchecked")
	@Override
	public List<AssocUtenteSiesAdnModel> verificaAssociazioneSiesAdn(String userId) throws F3BException {

		Connection c = null;
		List<AssocUtenteSiesAdnModel> l = new ArrayList<>();
		AssocUtenteSiesAdnSqlDAO ausasDAO = null;

		try {
			c = getDBConnection();
			ausasDAO = new AssocUtenteSiesAdnSqlDAO(c);
			ausasDAO.verificaAssociazioneSiesAdn(userId);
			l = new ArrayList<AssocUtenteSiesAdnModel>(ausasDAO.getModels());
		} catch (DAOException daoEx) {
			throw new F3BException(
					"UtenzaAdnController.verificaAssociazioneSiesAdn --> Non posso leggere: " + daoEx);
		} finally {
			cleanup(ausasDAO);
			cleanup(c);
		}
		return l;
	}

	@Override
	public AssocUtenteSiesAdnModel inserisciAssociazioneSiesAdn(String userId, BigDecimal id)
			throws F3BException {

		Connection c = null;

		AssocUtenteSiesAdnDAO ausaDAO = null;
		AssocUtenteSiesAdnModel ausam = null;

		try {
			c = getDBConnection();
			ausam = new AssocUtenteSiesAdnModel();
			ausam.setUteCodUtente(userId);
			ausam.setIdUtenteAdn(id);
			ausam.setCodOperatoreInserimento("ADMIN");
			ausam.setDataInserimento(DateUtils.getSysDate());
			ausaDAO = new AssocUtenteSiesAdnDAO(c);
			ausaDAO.setDAOFromModel(ausam);
			BigDecimal bd = ausaDAO.insert();
			ausam.setId(bd);
			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"UtenzaAdnController.inserisciAssociazioneSiesAdn --> Non posso inserire: " + ex);
		} finally {
			cleanup(ausaDAO);
			cleanup(c);
		}

		// valore di ritorno
		return ausam;
	}

}