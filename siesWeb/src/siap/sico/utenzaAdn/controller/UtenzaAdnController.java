package siap.sico.utenzaAdn.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.utenzaAdn.dao.UtenzaAdnDAO;
import siap.sico.utenzaAdn.dao.UtenzaAdnSqlDAO;
import siap.sico.utenzaAdn.model.UtenzaAdnModel;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class UtenzaAdnController extends SiapController implements IUtenzaAdn {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@Override
	public BigDecimal inserisciUtenzaAdn(String userId) throws F3BException {

		Connection c = null;

		UtenzaAdnDAO uaDAO = null;
		UtenzaAdnModel uam = null;
		BigDecimal bd = null;

		try {
			c = getDBConnection();
			uam = new UtenzaAdnModel();
			uam.setSamAccountName(userId);
			uam.setCodOperatoreInserimento("ADMIN");
			uam.setDataInserimento(DateUtils.getSysDate());
			uaDAO = new UtenzaAdnDAO(c);
			uaDAO.setDAOFromModel(uam);
			bd = uaDAO.insert();
			commit(c);
		} catch (DAOException ex) {
			rollback(c);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("UtenzaAdnController.inserisciUtenzaAdn --> Non posso inserire: " + ex);
		} finally {
			cleanup(uaDAO);
			cleanup(c);
		}

		// valore di ritorno
		return bd;
	}

	@Override
	public UtenzaAdnModel verificaEsistenzaUtenzaAdn(String userId) throws F3BException {

		Connection c = null;

		UtenzaAdnSqlDAO uasDAO = null;
		UtenzaAdnModel uam = null;

		try {
			c = getDBConnection();
			uam = new UtenzaAdnModel();
			uam.setSamAccountName(userId);
			uasDAO = new UtenzaAdnSqlDAO(c);
			uasDAO.ricercaUtenteAdn(uam);
			uasDAO.start();
			if (uasDAO.next())
				uam = (UtenzaAdnModel) uasDAO.getModel();
			uasDAO.stop();
		} catch (DAOException ex) {
			rollback(c);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"UtenzaAdnController.verificaEsistenzaUtenzaAdn --> Non posso inserire: " + ex);
		} finally {
			cleanup(uasDAO);
			cleanup(c);
		}

		// valore di ritorno
		return uam;
	}

}