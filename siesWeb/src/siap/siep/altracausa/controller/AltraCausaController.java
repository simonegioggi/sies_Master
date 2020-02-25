package siap.siep.altracausa.controller;

import java.math.BigDecimal;
import java.sql.Connection;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.altracausa.dao.AltraCausaSqlDAO;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.istitutodetenzione.dao.IstitutoDetenzioneSqlDAO;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;

public class AltraCausaController extends SiapController implements IAltraCausa {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExRicercaAltraCausaByFascicolo
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public AltraCausaModel ExRicercaAltraCausaByFascicolo(BigDecimal aKeyFascicolo) throws F3BException {

		AltraCausaSqlDAO lAltCauDao = null;
		Connection lConn = null;
		AltraCausaModel lAltraCausa = null;

		try {
			lConn = getDBConnection();

			// ** Altra Causa **
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			lAltCauDao.ricercaAltraCausaByIdFascicolo(aKeyFascicolo);
			lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();
		} catch (DAOException daoEx) {
			System.out.println("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey: "
							+ daoEx);
			// } catch (SQLException sqe) {
			// System.out.println("SQLException: " + sqe);
			// throw new F3BException(
			// "PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey:
			// "
			// + sqe);
		} finally {
			cleanup(lAltCauDao);
			cleanup(lConn);
		}
		return lAltraCausa;
	}

	/**
	 * ExRicercaAltraCausaIstitutoByFascicolo
	 *
	 * @param aKeyFascicolo
	 * @return
	 * @throws F3BException
	 */
	public AltraCausaModel ExRicercaAltraCausaIstitutoByFascicolo(BigDecimal aKeyFascicolo)
			throws F3BException {
		AltraCausaSqlDAO lAltCauDao = null;
		IstitutoDetenzioneSqlDAO lIstDAO = null;
		Connection lConn = null;
		AltraCausaModel lAltraCausa = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {
			lConn = getDBConnection();

			// ** Altra Causa **
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			lAltCauDao.ricercaAltraCausaByIdFascicolo(aKeyFascicolo);
			lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

			if (lAltraCausa != null) {
				lIstDAO = new IstitutoDetenzioneSqlDAO(lConn);
				lIstDAO.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
				lIstModel = (IstitutoDetenzioneModel) lIstDAO.getModelByKey();

				if (lIstModel != null)
					lAltraCausa.setIstitutoDetenzione(lIstModel);
			}
		} catch (DAOException daoEx) {
			System.out.println("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey: "
							+ daoEx);
			// } catch (SQLException sqe) {
			// System.out.println("SQLException: " + sqe);
			// throw new F3BException(
			// "PosizioneGiuridicaController.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByKey:
			// "
			// + sqe);
		} finally {
			cleanup(lAltCauDao);
			cleanup(lIstDAO);
			cleanup(lConn);
		}
		return lAltraCausa;
	}

	/**
	 * ExRicercaAltraCausaIstitutoByKey
	 *
	 * @param aKeyAltraCausa
	 * @return
	 * @throws F3BException
	 */
	public AltraCausaModel ExRicercaAltraCausaIstitutoByKey(BigDecimal aKeyAltraCausa) throws F3BException {
		AltraCausaSqlDAO lAltCauDao = null;
		IstitutoDetenzioneSqlDAO lIstDAO = null;
		Connection lConn = null;
		AltraCausaModel lAltraCausa = null;
		IstitutoDetenzioneModel lIstModel = null;

		try {

			lConn = getDBConnection();

			// ** Altra Causa **
			lAltCauDao = new AltraCausaSqlDAO(lConn);
			lAltCauDao.ricercaAltraCausaByKey(aKeyAltraCausa);
			lAltraCausa = (AltraCausaModel) lAltCauDao.getModelByKey();

			if (lAltraCausa != null) {
				lIstDAO = new IstitutoDetenzioneSqlDAO(lConn);
				lIstDAO.ricercaIstitutoDetenzioneByKey(lAltraCausa.getIstDetIdIstitutoDetenzione());
				lIstModel = (IstitutoDetenzioneModel) lIstDAO.getModelByKey();

				if (lIstModel != null)
					lAltraCausa.setIstitutoDetenzione(lIstModel);
			}
		} catch (DAOException daoEx) {
			throw new F3BException("AltraCausaController.ExRicercaAltraCausaIstitutoByKey: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("AltraCausaController.ExRicercaAltraCausaIstitutoByKey -> " + e);
		} finally {
			cleanup(lAltCauDao);
			cleanup(lIstDAO);
			cleanup(lConn);
		}
		return lAltraCausa;
	}

	/**
	 * MEV_67 ExInserisciAltraCausaWithoutSequence
	 *
	 * @param aKeyAltraCausa
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciAltraCausaWithoutSequence(AltraCausaModel altraCausa, Connection lConn)
			throws F3BException {

		AltraCausaDAO lPosDao = null;
		String lCodEsito = "00000";

		try {
			lPosDao = new AltraCausaDAO(lConn);
			lPosDao.setDAOFromModel(altraCausa);
			lPosDao.setWithoutSequence(true);
			lPosDao.insert();
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Altra Causa gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire altra Causa della Posizione Giuridica! ");
			}
			// } catch (SQLException sqe) {
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			// siesLogger.error("SQLException: " + sqe);
			// throw new F3BException("AltraCausaController.ExInserisciAltraCausaWithoutSequence: " + sqe);
		} finally {
			cleanup(lPosDao);
		}

		return lCodEsito;
	}

}