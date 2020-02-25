package siap.sico.avvocato.controller;

/**
* <p>Title: AvvocatoController</p>
* <p>Description: Classe Controller per Avvocato</p>
* <p>Copyright: Copyright (c) 2008</p>
* @version 1.0
*/

import java.sql.Connection;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.avvocato.dao.AvvocatoSqlDAO;
import siap.sico.avvocato.model.AvvocatoModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AvvocatoController extends SiapController implements IAvvocato {

	/**
	 * <p>
	 * Title: ExRicercaForo
	 * </p>
	 * <p>
	 * Description: Metodo che permette la ricerca dei fori di appartenenza per gli avvocati di un Distretto
	 * </p>
	 * 
	 * @version 1.0
	 */

	public Vector ExRicercaForo() throws F3BException {
		Connection lConn = null;
		Vector lFori = new Vector();
		AvvocatoSqlDAO lAvvSqlDAO = null;

		try {
			lConn = getDBConnection();
			lAvvSqlDAO = new AvvocatoSqlDAO(lConn);
			lAvvSqlDAO.ricercaForo();
			lAvvSqlDAO.start();

			while (lAvvSqlDAO.next()) {
				lFori.add((AvvocatoModel) lAvvSqlDAO.getModelForo());
			}

			lAvvSqlDAO.stop();

			if (lFori.size() == 0)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Nessun Foro trovato, impossibile caricare gli avvocati");

		} catch (DAOException daoEx) {
			throw new F3BException("AvvocatoController.ExRicercaForo: " + daoEx);
		} catch (SICOException se) {
			throw se;
		} catch (Exception e) {
			throw new F3BException("AvvocatoController.ExRicercaForo: " + e);
		} finally {
			cleanup(lAvvSqlDAO);
			cleanup(lConn);
		}
		return lFori;
	}

}