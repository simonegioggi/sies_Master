package siap.sige.posizionematerialefascsige.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sige.posizionematerialefascsige.dao.PosizioneMaterialeFascSigeDAO;
import siap.sige.posizionematerialefascsige.dao.PosizioneMaterialeFascSigeSqlDAO;

/**
 * <p>
 * Title: PosizioneMaterialeFascController
 * </p>
 * <p>
 * Description: Classe Controller per PosizioneMaterialeFasc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PosizioneMaterialeFascSigeController extends SiapController
		implements IPosizioneMaterialeFascSige {

	public PosizioneMaterialeFascModel ExInserisciPosizioneMaterialeFasc(
			PosizioneMaterialeFascModel aPosizioneMaterialeFasc) throws F3BException {

		Connection lConn = null;
		PosizioneMaterialeFascSigeDAO lPosDao = null;
		PosizioneMaterialeFascModel lPosMod = null;

		try {
			lConn = getDBConnection();
			lPosMod = new PosizioneMaterialeFascModel(aPosizioneMaterialeFasc);
			lPosDao = new PosizioneMaterialeFascSigeDAO(lConn);

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
			throw new F3BException("PosizioneMaterialeFascController.ExInserisci: Non posso inserire: " + ex);
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
		PosizioneMaterialeFascSigeSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeFascSigeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMaterialeFasc(aPosizioneMaterialeFasc);
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

	public Vector<PosizioneMaterialeFascModel> ExRicercaPosizioneMaterialeFascAttiva(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;
		Vector<PosizioneMaterialeFascModel> lPosizioneMaterialeFasi = new Vector<>();
		PosizioneMaterialeFascSigeSqlDAO lPosDao = null;

		try {
			lConn = getDBConnection();
			lPosDao = new PosizioneMaterialeFascSigeSqlDAO(lConn);
			lPosDao.ricercaPosizioneMaterialeFascAttivaXFas(aIdFascicolo);
			lPosizioneMaterialeFasi = new Vector<PosizioneMaterialeFascModel>(lPosDao.getModels());
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
	 * La Cancellazione comporta la cancellazione del record attivo nella tabella
	 * POSIZIONE_MATERIALE_FASC_SIGE e la riattivazione dell'ultimo record chiuso.
	 */
	public void ExCancellaPosizioneMaterialeFasc(PosizioneMaterialeFascModel aPosizioneMaterialeFasc)
			throws F3BException {
		Connection lConn = null;
		PosizioneMaterialeFascSigeDAO lPosDao = null;
		// PosizioneMaterialeFascModel lPosMod = null;

		try {
			lConn = getDBConnection();
			// lPosMod = new PosizioneMaterialeFascModel(aPosizioneMaterialeFasc);
			lPosDao = new PosizioneMaterialeFascSigeDAO(lConn);

			// cancellazione del record attivo
			lPosDao.setCondizioneUpdate(aPosizioneMaterialeFasc.getFasSieIdFascicoloSiep());
			lPosDao.delete();
			lPosDao.stop();

			// riattivazione del vecchio record
			lPosDao.setDataFine(null);
			lPosDao.setCodOperatoreAggiornamento(aPosizioneMaterialeFasc.getCodOperatoreAggiornamento());
			lPosDao.setDataAggiornamento(aPosizioneMaterialeFasc.getDataAggiornamento());
			lPosDao.setCodUfficioAggiornamento(aPosizioneMaterialeFasc.getCodUfficioAggiornamento());
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