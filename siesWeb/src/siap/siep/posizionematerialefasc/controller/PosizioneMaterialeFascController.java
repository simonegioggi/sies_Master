package siap.siep.posizionematerialefasc.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.posizionematerialefasc.dao.PosizioneMaterialeFascDAO;
import siap.siep.posizionematerialefasc.dao.PosizioneMaterialeFascSqlDAO;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;

/**
 * Title: PosizioneMaterialeFascController Description: Classe Controller per PosizioneMaterialeFasc
 * Copyright: Copyright (c) 2002 Company: Bull
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PosizioneMaterialeFascController extends SiapController implements IPosizioneMaterialeFasc {

	public PosizioneMaterialeFascModel ExInserisciPosizioneMaterialeFasc(
			PosizioneMaterialeFascModel aPosizioneMaterialeFasc) throws F3BException {

		Connection lConn = null;

		PosizioneMaterialeFascDAO lPosDao = null;
		PosizioneMaterialeFascModel lPosMod = null;

		try {
			lConn = getDBConnection();

			lPosMod = new PosizioneMaterialeFascModel(aPosizioneMaterialeFasc);
			lPosDao = new PosizioneMaterialeFascDAO(lConn);

			// update del record attivo
			lPosDao.setDataFine(aPosizioneMaterialeFasc.getDataInizio());
			lPosDao.setCodOperatoreAggiornamento(aPosizioneMaterialeFasc.getCodOperatoreInserimento());
			lPosDao.setDataAggiornamento(aPosizioneMaterialeFasc.getDataInserimento());
			lPosDao.setCodUfficioAggiornamento(aPosizioneMaterialeFasc.getCodUfficioInserimento());
			lPosDao.setCondizioneUpdate(aPosizioneMaterialeFasc.getFasSieIdFascicoloSiep());
			lPosDao.update();
			lPosDao.stop();

			// inserimento del nuovo record
			lPosDao.setDAOFromModel(aPosizioneMaterialeFasc);
			// BigDecimal lKey = null;
			/* lKey = */lPosDao.insert();

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("PosizioneMaterialeFascController.ExInserisci: " + ex);
		} finally {
			cleanup(lPosDao);

			cleanup(lConn);
		}

		return lPosMod;
	}

	public Vector ExRicercaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException {

		Connection lConn = null;
		Vector lPosizioneMaterialeFasi = new Vector();
		PosizioneMaterialeFascSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeFascSqlDAO(lConn);
			lPosDao.ricercaPosizioneMaterialeFasc(aPosizioneMaterialeFasc);
			lPosizioneMaterialeFasi = new Vector(lPosDao.getModels());
			/*
			 * if ( lPosizioneMaterialeFasi.size() == 0 ) { throw new
			 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); }
			 */
		} catch (Exception Ex) {
			Ex.printStackTrace();
			throw new F3BException(
					"PosizioneMaterialeFascController.ExRicercaPosizioneMaterialeFasc: Non posso leggere : "
							+ Ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return lPosizioneMaterialeFasi;
	}

	public Vector ExRicercaPosizioneMaterialeFascAttiva(BigDecimal aIdFascicolo) throws F3BException {

		Connection lConn = null;
		Vector lPosizioneMaterialeFasi = new Vector();
		PosizioneMaterialeFascSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeFascSqlDAO(lConn);
			lPosDao.ricercaPosizioneMaterialeFascAttivaXFas(aIdFascicolo);
			lPosizioneMaterialeFasi = new Vector(lPosDao.getModels());
		} catch (Exception Ex) {
			throw new F3BException(
					"PosizioneMaterialeFascController.ExRicercaPosizioneMaterialeFasc: Non posso leggere : "
							+ Ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}
		return lPosizioneMaterialeFasi;
	}

	/*
	 * La Cancellazione comporta la cancellazione del record attivo nella tabella POSIZIONE_MATERIALE_FASC e
	 * la riattivazione dell'ultimo record chiuso.
	 */
	public void ExCancellaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException {

		Connection lConn = null;

		PosizioneMaterialeFascDAO lPosDao = null;
		// PosizioneMaterialeFascModel lPosMod = null;

		try {
			lConn = getDBConnection();

			// lPosMod = new PosizioneMaterialeFascModel(aPosizioneMaterialeFasc);
			lPosDao = new PosizioneMaterialeFascDAO(lConn);

			// cancellazione del record attivo
			lPosDao.setCondizioneUpdate(aPosizioneMaterialeFasc.getFasSieIdFascicoloSiep());
			lPosDao.delete();
			lPosDao.stop();

			// riattivazione del vecchio record
			lPosDao.setDataFine(null);
			lPosDao.setCodOperatoreAggiornamento(aPosizioneMaterialeFasc.getCodOperatoreInserimento());
			lPosDao.setDataAggiornamento(aPosizioneMaterialeFasc.getDataAggiornamento());
			lPosDao.setCodUfficioAggiornamento(aPosizioneMaterialeFasc.getCodUfficioInserimento());
			lPosDao.setCondizioneRiattivazione(aPosizioneMaterialeFasc.getFasSieIdFascicoloSiep());
			lPosDao.update();
			lPosDao.stop();

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("PosizioneMaterialeFascController.ExCancella: " + ex);
		} finally {
			cleanup(lPosDao);
			cleanup(lConn);
		}

		return;
	}

}