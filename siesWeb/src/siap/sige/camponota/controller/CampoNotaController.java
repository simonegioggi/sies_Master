package siap.sige.camponota.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.camponota.dao.CampoNotaDAO;
import siap.sico.camponota.model.CampoNotaModel;

public class CampoNotaController extends SiapController implements ICampoNota {

	@Override
	public void ExAggiornaNoteByIdEvento(CampoNotaModel[] note, BigDecimal idEvento) throws F3BException {

		Connection lConn = null;
		CampoNotaDAO lCampoNotaDao = null;
		try {
			lConn = getDBTransaction();

			lCampoNotaDao = new CampoNotaDAO(lConn);
			lCampoNotaDao.setCondizioneEvento(idEvento);
			lCampoNotaDao.delete();

			for (CampoNotaModel nota : note) {
				nota.setProgressivo(new BigDecimal(1));
				lCampoNotaDao.setDAOFromModel(nota);
				lCampoNotaDao.insert();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExAnnullaProvvedimento : " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ProvvedimentoSigeController.ExAnnullaProvvedimento : " + e);
		} finally {
			cleanup(lCampoNotaDao);
			cleanup(lConn);
		}
	}

}