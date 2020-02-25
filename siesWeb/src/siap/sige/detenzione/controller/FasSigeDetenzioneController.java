package siap.sige.detenzione.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.dao.AltraCausaDAO;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.dao.LuogoDetenzioneDAO;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.detenzione.dao.FasSigeDetenzioneDAO;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FasSigeDetenzioneController
 * </p>
 * <p>
 * Description: Classe Controller per FasSigeDetenzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FasSigeDetenzioneController extends SiapController implements IFasSigeDetenzione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public FasSigeDetenzioneModel ExInserisciFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione)
			throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDetDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		AltraCausaDAO lAltCauDao = null;
		FasSigeDetenzioneModel lFasDetMod = new FasSigeDetenzioneModel(aFasSigeDetenzione);
		try {
			lConn = getDBConnection();
			// Aggiornamento ultimo LUOGO DETENZIONE per impostazione DATA_FINE.
			lFasDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasDetDao.setCondizioneUltimoLuogoByFascicolo(aFasSigeDetenzione.getFasIdFasSige());
			FasSigeDetenzioneModel lDetenzione = (FasSigeDetenzioneModel) lFasDetDao.getModelByKey();

			if (lDetenzione != null) {
				if (lDetenzione.getLdIdLuogoDetenzione() != null) {
					lLuoDetDao = new LuogoDetenzioneDAO(lConn);
					lLuoDetDao.setCondizioneUpdate(lDetenzione.getLdIdLuogoDetenzione());
					lLuoDetDao.setCodOperatoreAggiornamento(lFasDetMod.getCodOperatoreInserimento());
					lLuoDetDao.setCodUfficioAggiornamento(lFasDetMod.getCodUfficioInserimento());
					lLuoDetDao.setDataAggiornamento(DateUtils.getSysDate());
					lLuoDetDao.setDataFineDetenzione(lFasDetMod.getLuogoDetenzione()
							.getDataInizioDetenzione());
					lLuoDetDao.update();
					lLuoDetDao.stop();
				}
			}
			// Inserimento nuovo LUOGO DETENZIONE.
			lLuoDetDao = new LuogoDetenzioneDAO(lConn);
			lLuoDetDao.setDAOFromModel(lFasDetMod.getLuogoDetenzione());
			BigDecimal lKeyLD = null;
			lKeyLD = lLuoDetDao.insert();

			lFasDetMod.setLdIdLuogoDetenzione(lKeyLD);
			lFasDetMod.getLuogoDetenzione().setIdLuogoDetenzione(lKeyLD);

			lAltCauDao = new AltraCausaDAO(lConn);
			if (lFasDetMod.getAltraCausa() != null && lFasDetMod.getAltraCausa().getAltroLuogo() != null
					&& lFasDetMod.getAltraCausa().getAltroLuogo().length() > 1) {
				lAltCauDao.setDAOFromModel(lFasDetMod.getAltraCausa());
				BigDecimal lKeyAC = null;
				lKeyAC = lAltCauDao.insert();

				lFasDetMod.setAcIdAltraCausa(lKeyAC);
				lFasDetMod.getAltraCausa().setIdAltraCausa(lKeyAC);
			}

			lFasDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasDetDao.setDAOFromModel(lFasDetMod);
			BigDecimal lKeyFSD = lFasDetDao.insert();

			lFasDetMod.setIdFasSigeDetenzione(lKeyFSD);

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FasSigeDetenzioneController.ExInserisciFasSigeDetenzione: " + ex);
		} finally {
			cleanup(lFasDetDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);

			cleanup(lConn);
		}
		return lFasDetMod;
	}

	/**
	 * La funzione determina il luogo di Detenzione corrente (ultimo) legato ad un Fascicolo SIGE. SE esiste
	 * il luogo di Detenzione può essere alternativamente un LuogoDetenzioneModel o AltraCausaModel.
	 * 
	 * @param aIdFasSige
	 * @return FasSigeDetenzioneModel
	 * @throws F3BException
	 */

	public FasSigeDetenzioneModel ExRicercaUltimaDetenzioneFascicolo(BigDecimal aIdFasSige)
			throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDao = null;
		FasSigeDetenzioneModel lDetenzione = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FasSigeDetenzioneDAO(lConn);
			lFasDao.setCondizioneUltimoLuogoByFascicolo(aIdFasSige);
			lDetenzione = (FasSigeDetenzioneModel) lFasDao.getModelByKey();

			if (lDetenzione != null) {
				if (lDetenzione.getLdIdLuogoDetenzione() != null) {
					// Ricerca Luogo Detenzione
					ILuogoDetenzione lLdCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
					lDetenzione.setLuogoDetenzione(lLdCtrl.ExRicercaLuogoDetenzioneByKey(lDetenzione
							.getLdIdLuogoDetenzione()));
				}
				if (lDetenzione.getAcIdAltraCausa() != null) {
					// Ricerca Altra Causa
					IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
					lDetenzione.setAltraCausa(lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lDetenzione
							.getAcIdAltraCausa()));
				}
				if (lDetenzione.getLdIdLuogoDetenzione() == null && lDetenzione.getAcIdAltraCausa() == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Errore nella lettura del Luogo di Detenzione");
			}

		} catch (F3BException fEx) {
			throw fEx;
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeDetenzioneController.ExRicercaUltimaDetenzioneFascicolo: " + daoEx);
		} catch (Exception eEx) {
			throw new F3BException("FasSigeDetenzioneController.ExRicercaUltimaDetenzioneFascicolo " + eEx);
		}

		finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
		return lDetenzione;
	}

	/**
	 * La funzione ricerca il FasSigeDetenzione per Chiave SE esiste il luogo di Detenzione può essere
	 * alternativamente un LuogoDetenzioneModel o AltraCausaModel.
	 * 
	 * @param aIdFasSige
	 * @return FasSigeDetenzioneModel
	 * @throws F3BException
	 */

	public FasSigeDetenzioneModel ExRicercaFasSigeDetenzione(BigDecimal aIdFasSigeDet) throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDetDao = null;
		FasSigeDetenzioneModel lDetenzione = null;

		try {
			lConn = getDBConnection();
			lFasDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasDetDao.setCondizioneByKey(aIdFasSigeDet);
			lDetenzione = (FasSigeDetenzioneModel) lFasDetDao.getModelByKey();

			if (lDetenzione != null) {
				if (lDetenzione.getLdIdLuogoDetenzione() != null) {
					// Ricerca Luogo Detenzione
					ILuogoDetenzione lLdCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
					lDetenzione.setLuogoDetenzione(lLdCtrl.ExRicercaLuogoDetenzioneByKey(lDetenzione
							.getLdIdLuogoDetenzione()));
				} else if (lDetenzione.getAcIdAltraCausa() != null) {
					// Ricerca Altra Causa
					IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
					lDetenzione.setAltraCausa(lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lDetenzione
							.getAcIdAltraCausa()));
				}
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Errore nella lettura del Luogo di Detenzione");
		} catch (F3BException fEx) {
			throw fEx;
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeDetenzioneController.ExRicercaFasSigeDetenzione: " + daoEx);
		} catch (Exception eEx) {
			throw new F3BException("FasSigeDetenzioneController.ExRicercaFasSigeDetenzione: " + eEx);
		}

		finally {
			cleanup(lFasDetDao);

			cleanup(lConn);
		}
		return lDetenzione;
	}

	/**
	 * Funzione di ricerca dei luoghi di Detenzione legati ad un Fascicolo SIGE.
	 * 
	 * @param aIdFasSige
	 * @return Vector <FasSigeDetenzioneModel>
	 * @throws F3BException
	 */
	public Vector exRicercaLuoghiDetenzioneFascicoloSige(BigDecimal aIdFasSige) throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasSigeDetDao = null;
		FasSigeDetenzioneModel lDetenzione = new FasSigeDetenzioneModel();
		lDetenzione.setFasIdFasSige(aIdFasSige);
		Vector lFasSigeDetenzioni = new Vector();

		try {
			lConn = getDBConnection();
			lFasSigeDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasSigeDetDao.setCondizioneByIdFasSige(lDetenzione);
			lFasSigeDetenzioni = new Vector(lFasSigeDetDao.getModels());

			if (lFasSigeDetenzioni != null && lFasSigeDetenzioni.size() > 0) {
				for (int i = 0; i < lFasSigeDetenzioni.size(); i++) {
					lDetenzione = (FasSigeDetenzioneModel) lFasSigeDetenzioni.elementAt(i);
					if (lDetenzione.getLdIdLuogoDetenzione() != null) {
						// Ricerca Luogo Detenzione
						ILuogoDetenzione lLdCtrl = SIEPLookupRemote.getLuogoDetenzioneRemote();
						lDetenzione.setLuogoDetenzione(lLdCtrl.ExRicercaLuogoDetenzioneByKey(lDetenzione
								.getLdIdLuogoDetenzione()));
					} else if (lDetenzione.getAcIdAltraCausa() != null) {
						// Ricerca Altra Causa
						IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
						lDetenzione.setAltraCausa(lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lDetenzione
								.getAcIdAltraCausa()));
					} else
						throw new F3BException(F3BException.USER_MESSAGE,
								"Errore nella lettura del Luogo di Detenzione");
					// Impostazione dell'elemento corrente.
					lFasSigeDetenzioni.setElementAt(lDetenzione, i);
				}
			}
		} catch (F3BException fEx) {
			throw fEx;
		} catch (DAOException daoEx) {
			throw new F3BException("FasSigeDetenzioneController.exRicercaLuoghiDetenzioneFascicoloSige: "
					+ daoEx);
		} catch (Exception eEx) {
			throw new F3BException("FasSigeDetenzioneController.exRicercaLuoghiDetenzioneFascicoloSige -> "
					+ eEx);
		}

		finally {
			cleanup(lFasSigeDetDao);
			cleanup(lConn);
		}
		return lFasSigeDetenzioni;
	}

	/*
	 * public FasSigeDetenzioneModel ExRicercaFasSigeDetenzioneByKey ( BigDecimal aKey) throws F3BException {
	 * Connection lConn = null; FasSigeDetenzioneSqlDAO lFasDao = null; FasSigeDetenzioneModel lFasMod;
	 * 
	 * 
	 * 
	 * try { lConn = getDBConnection(); lFasDao = new FasSigeDetenzioneSqlDAO(lConn);
	 * lFasDao.ricercaFasSigeDetenzioneByKey(aKey); lFasMod = (FasSigeDetenzioneModel)lFasDao.getModelByKey();
	 * } catch (DAOException daoEx) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
	 * siesLogger al posto di mLog siesLogger.error("DAOException: " + daoEx); throw new
	 * F3BException("FasSigeDetenzioneController.ExRicercaFasSigeDetenzione: Non posso leggere : " + daoEx); }
	 * catch (SQLException sqe) { // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger
	 * al posto di mLog siesLogger.error("SQLException: " + sqe); throw new
	 * F3BException("FasSigeDetenzioneController.ExRicercaFasSigeDetenzione: Non posso leggere  : " + sqe); }
	 * finally { cleanup(lFasDao); cleanup(lConn); } return lFasMod; }
	 */

	public FasSigeDetenzioneModel ExModificaFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione)
			throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDetDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		AltraCausaDAO lAltCauDao = null;
		FasSigeDetenzioneModel lFasMod = new FasSigeDetenzioneModel(aFasSigeDetenzione);

		try {
			lConn = getDBConnection();
			lFasDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasDetDao.setDAOFromModelForUpdate(aFasSigeDetenzione);
			lFasDetDao.update();

			lLuoDetDao = new LuogoDetenzioneDAO(lConn);
			lLuoDetDao.setIdLuogoDetenzione(aFasSigeDetenzione.getLuogoDetenzione().getIdLuogoDetenzione());
			lLuoDetDao.setDAOFromModelForUpdate(aFasSigeDetenzione.getLuogoDetenzione());
			lLuoDetDao.setIstDetIdIstitutoDetenzione(aFasSigeDetenzione.getLuogoDetenzione()
					.getIstDetIdIstitutoDetenzione());
			lLuoDetDao.update();
			lLuoDetDao.stop();

			lAltCauDao = new AltraCausaDAO(lConn);
			lAltCauDao.setIdAltraCausa(aFasSigeDetenzione.getAltraCausa().getIdAltraCausa());
			lAltCauDao.setDAOFromModelForUpdate(aFasSigeDetenzione.getAltraCausa());
			lAltCauDao.setIstDetIdIstitutoDetenzione(aFasSigeDetenzione.getAltraCausa()
					.getIstDetIdIstitutoDetenzione());
			lAltCauDao.update();
			lAltCauDao.stop();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FasSigeDetenzioneController.ExModificaFasSigeDetenzione: " + ex);
		} finally {
			cleanup(lFasDetDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lConn);
		}
		return lFasMod;
	}

	public void ExCancellaFasSigeDetenzione(FasSigeDetenzioneModel aFasSigeDetenzione) throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDao = null;

		try {
			lConn = getDBConnection();
			lFasDao = new FasSigeDetenzioneDAO(lConn);
			lFasDao.setCondizioneByKey(aFasSigeDetenzione.getIdFasSigeDetenzione());
			lFasDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("FasSigeDetenzioneController.ExCancellaFasSigeDetenzione: " + daoEx);
		} finally {
			cleanup(lFasDao);
			cleanup(lConn);
		}
	}

	public void exCancellaUltimaDetenzioneFascicoloSige(BigDecimal aIdFasSige) throws F3BException {
		Connection lConn = null;
		FasSigeDetenzioneDAO lFasDetDao = null;
		LuogoDetenzioneDAO lLuoDetDao = null;
		AltraCausaDAO lAltCauDao = null;
		FasSigeDetenzioneModel lDetenzione = new FasSigeDetenzioneModel();

		try {
			lConn = getDBConnection();
			lFasDetDao = new FasSigeDetenzioneDAO(lConn);
			lFasDetDao.setCondizioneUltimoLuogoByFascicolo(aIdFasSige);
			lDetenzione = (FasSigeDetenzioneModel) lFasDetDao.getModelByKey();

			if (lDetenzione != null) {
				lFasDetDao.setCondizioneByKey(lDetenzione.getIdFasSigeDetenzione());
				lFasDetDao.delete();

				lLuoDetDao = new LuogoDetenzioneDAO(lConn);
				lLuoDetDao.setCondizioneUpdate(lDetenzione.getLdIdLuogoDetenzione());
				lLuoDetDao.delete();

				lAltCauDao = new AltraCausaDAO(lConn);
				lAltCauDao.setCondizioneUpdate(lDetenzione.getAcIdAltraCausa());
				lAltCauDao.delete();

				commit(lConn);
			}
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("FasSigeDetenzioneController.ExCancellaUltimaDetenzioneFascicolo: " + ex);
		} finally {
			cleanup(lFasDetDao);
			cleanup(lLuoDetDao);
			cleanup(lAltCauDao);
			cleanup(lConn);
		}
	}

}