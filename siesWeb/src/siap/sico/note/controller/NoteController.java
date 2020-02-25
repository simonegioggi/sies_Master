package siap.sico.note.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sico.note.dao.NoteSqlDAO;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NoteController
 * </p>
 * <p>
 * Description: Classe Controller per Note
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NoteController extends SiapController implements INote {

	public Vector ExRicercaNote(BigDecimal aIdFascicoloSius) throws F3BException {
		Connection lConn = null;
		NoteSqlDAO lNoteSqlDao = null;

		Vector lVectNote = null;

		try {
			lConn = getDBConnection();

			lNoteSqlDao = new NoteSqlDAO(lConn);

			// Ricerca delle Note per ID Fascicolo SIUS.
			lNoteSqlDao.ricercaNoteByIdFascicoloSius(aIdFascicoloSius);
			lVectNote = new Vector(lNoteSqlDao.getModels());

			lNoteSqlDao.stop();

		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("NoteController.ExRicercaNote:  : " + ex);
		} finally {
			cleanup(lNoteSqlDao);
			cleanup(lConn);
		}

		return lVectNote;
	}

	public Vector ExRicercaNoteFasSige(BigDecimal aIdFascicoloSige) throws F3BException {
		Connection lConn = null;
		NoteSqlDAO lNoteSqlDao = null;

		Vector lVectNote = null;

		try {
			lConn = getDBConnection();

			lNoteSqlDao = new NoteSqlDAO(lConn);

			// Ricerca delle Note per ID Fascicolo SIGE.
			lNoteSqlDao.ricercaNoteByIdFascicoloSige(aIdFascicoloSige);
			lVectNote = new Vector(lNoteSqlDao.getModels());

			lNoteSqlDao.stop();

		} catch (SQLException sqe) {
			rollback(lConn);
			throw new F3BException("NoteController.ExRicercaNoteFasSige: " + sqe);
		} finally {
			cleanup(lNoteSqlDao);
			cleanup(lConn);
		}

		return lVectNote;
	}

}