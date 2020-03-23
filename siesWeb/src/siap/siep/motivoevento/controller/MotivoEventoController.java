package siap.siep.motivoevento.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.motivoevento.dao.MotivoEventoSqlDAO;
import siap.siep.motivoevento.model.MotivoEventoModel;

/**
 * <p>
 * Title: MotivoEventoController
 * </p>
 * <p>
 * Description: Classe Controller per MotivoEvento
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

public class MotivoEventoController extends SiapController implements IMotivoEvento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MotivoEventoModel ExRicercaMotivoEventoByEveIdEvento(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		MotivoEventoSqlDAO lMotDao = null;
		MotivoEventoModel lMotMod;

		try {
			lConn = getDBConnection();
			lMotDao = new MotivoEventoSqlDAO(lConn);
			lMotDao.ricercaMotivoEventoByEveIdEvento(aKey);
			lMotMod = (MotivoEventoModel) lMotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MotivoEventoController.ExRicercaMotivoEventoByEveIdEvento: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMotDao);
			cleanup(lConn);
		}
		return lMotMod;
	}

}