package siap.siep.notefascicolo.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.notefascicolo.dao.NoteFascicoloSqlDAO;
import siap.siep.notefascicolo.model.NoteFascicoloModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NoteFascicoloController
 * </p>
 * <p>
 * Description: Controller della tabella Note fascicolo
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class NoteFascicoloController extends SiapController {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExRicercaAltraCausaByFascicolo
	 * 
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public NoteFascicoloModel ExRicercaNoteFascicolo(BigDecimal aKeyFascicolo) throws F3BException {
		NoteFascicoloSqlDAO lNotDao = null;
		Connection lConn = null;
		NoteFascicoloModel lNotMod = null;

		try {
			lConn = getDBConnection();

			// ** Ricerca Note **
			lNotDao = new NoteFascicoloSqlDAO(lConn);
			lNotDao.ricercaNoteFascicoloByKey(aKeyFascicolo);
			lNotMod = (NoteFascicoloModel) lNotDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey: "
							+ daoEx);
		} finally {
			cleanup(lNotDao);
			cleanup(lConn);
		}
		return lNotMod;
	}

}