package siap.sico.versione.controller;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.versione.dao.VersioneSqlDAO;
import siap.sico.versione.model.VersioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: VersioneController
 * </p>
 * <p>
 * Description: Classe Controller per Versione
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
public class VersioneController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// [FT] - 03/08/2016 - MAC_LOG - Commento la dichiarazione di mLog in favore della variabile siesLogger
	// static Logger mLog = LogF3B.getLogger();

	public VersioneModel ExRicercaVersione() throws F3BException {
		Connection lConn = null;
		VersioneModel lVersioni = null;
		VersioneSqlDAO lVerDao = null;

		try {
			lConn = getDBConnection();
			lVerDao = new VersioneSqlDAO(lConn);
			lVerDao.ricercaVersione();
			lVersioni = (VersioneModel) lVerDao.getModelByKey();
			if (lVersioni == null) {
				lVersioni = new VersioneModel();
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("VersioneController.ExRicercaVersione: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lVerDao);
			cleanup(lConn);
		}
		return lVersioni;
	}

}