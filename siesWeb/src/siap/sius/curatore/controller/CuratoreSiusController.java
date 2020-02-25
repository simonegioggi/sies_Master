package siap.sius.curatore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sige.SIGEException;
import siap.sige.curatore.model.CuratoreModel;
import siap.sius.SIUSException;
import siap.sius.curatore.dao.CuratoreSiusDAO;
import siap.sius.curatore.dao.CuratoreSiusSqlDAO;
import siap.sius.curatore.model.CuratoreSiusModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
* <p>Title: CuratoreSiusController</p>
* <p>Description: Classe Controller per CuratoreSius</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: </p>
* @version 1.0
*/
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CuratoreSiusController extends SiapController
implements ICuratoreSius
 {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Esegue l'inserimento del curatore, verificando se il curatore da inserire è lo stesso di quello
	 * corrente se non lo è, chiude il record precedente e inserisce quello nuovo.
	 * <p>
	 * 
	 * @param aCurSiusMod
	 *            dati del curatore da inserire.
	 * @return i dati del curatore appena inserito.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public CuratoreSiusModel ExInserisciCuratoreSius(CuratoreSiusModel aCurSiusMod) throws F3BException {
		Connection lConn = null;
		// CuratoreSiusDAO lCurSiusDao = null;
		CuratoreSiusDAO lCurSiusDao = null;
		CuratoreSiusModel lCurSiusChiuso = new CuratoreSiusModel();

		try {
			lConn = getDBConnection();
			// Interrogazione Curatore Impostato.
			lCurSiusDao = new CuratoreSiusDAO(lConn);
			// lCurSiusSqlDao = new CuratoreSiusSqlDAO( lConn );
			// lCurSiusDao.ricercaCuratoreSiusCorrenteByFascicolo( aCurSiusMod.getFasSiuIdFascicoloSius() );

			// Chiusura di eventuale Curatore già collegato al fascicolo
			lCurSiusChiuso.setFasSiuIdFascicoloSius(aCurSiusMod.getFasSiuIdFascicoloSius());
			lCurSiusChiuso.setCodOperatoreAggiornamento(aCurSiusMod.getCodOperatoreInserimento());
			lCurSiusChiuso.setCodUfficioAggiornamento(aCurSiusMod.getCodUfficioInserimento());
			lCurSiusChiuso.setDataAggiornamento(DateUtils.getSysDate());
			lCurSiusChiuso.setDataFine(DateUtils.getSysDate());

			lCurSiusDao.setDAOFromModelForChiusura(lCurSiusChiuso);
			lCurSiusDao.update();
			lCurSiusDao.stop();

			// Inserimento nuovo curatore.
			lCurSiusDao.setDAOFromModel(aCurSiusMod);
			lCurSiusDao.insert();
			commit(lConn);
		} catch (SIUSException siusEx) {
			rollback(lConn);
			throw siusEx;
		} catch (DAOException daoex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoex);

			if (daoex.getMessage().indexOf("CUR_PK") != -1)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Impossibile inserire il curatore! Esiste un curatore per lo stesso numero SIUS");

			throw new SIUSException("CuratoreSiusController.ExInserisciCuratoreSius : " + daoex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lCurSiusDao);
			cleanup(lConn);
		}

		return aCurSiusMod;
	}

	/**
	 * Esegue la ricerca dei curatori SIUS per id fascicolo.
	 * <p>
	 * 
	 * @param aKey
	 *            id del fascicolo.
	 * @return model aggregato con i dati del Curatore Sius e quelli del Curatore.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaCurSiusByFascicolo(BigDecimal aKey, String codUfficioUtenteConnesso)
			throws F3BException {
		Connection lConn = null;
		Vector lCuratori = new Vector();
		CuratoreSiusSqlDAO lCurSiusSqlDao = null;
		CuratoreSiusModel lCurSiusMod;

		try {
			lConn = getDBConnection();

			lCurSiusSqlDao = new CuratoreSiusSqlDAO(lConn);
			lCurSiusSqlDao.ricercaCuratoreSiusByIdFascicolo(aKey, codUfficioUtenteConnesso);

			lCurSiusSqlDao.start();

			lCurSiusMod = null;
			CuratoreModel lCurMod = null;
			while (lCurSiusSqlDao.next()) {
				lCurSiusMod = new CuratoreSiusModel();
				// lMagMod = (MagistratoModel)lDao.getModel();
				lCurMod = lCurSiusSqlDao.getModelCuratore();
				lCurSiusMod = lCurSiusSqlDao.getModelCuratoreSius();
				lCurSiusMod.setCuratore(lCurMod);
				lCuratori.add(lCurSiusMod);

			}
			lCurSiusSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIGEException(F3BException.USER_MESSAGE,
					"CuratoreSiusController.ExRicercaCurSiusByFascicolo: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIGEException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lCurSiusSqlDao);
			cleanup(lConn);
		}
		return lCuratori;
	}

	/**
	 * Esegue la ricerca del curatore precedente e corrente, per l'id del fascicolo sius.
	 * 
	 * @param aKey
	 *            id del fasciclo sius.
	 * @return l'elenco dei magistrati Corrente e il precedente.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Vector ExRicercaCuratoreCorrentePrecedenteByFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		CuratoreSiusSqlDAO lMagRelSqlDao = null;
		Vector lMagistrati = null;

		try {
			lConn = getDBConnection();
			lMagRelSqlDao = new CuratoreSiusSqlDAO(lConn);
			lMagRelSqlDao.ricercaCuratoreSiusByDataFine(aKey);
			lMagistrati = new Vector(lMagRelSqlDao.getModels());
		} catch (DAOException daoEx) {
			throw new SIUSException("CuratoreSiusController.ExRicercaCuratoreSiusCorrenteByFascicolo: "
					+ daoEx);
		} finally {
			cleanup(lMagRelSqlDao);
			cleanup(lConn);
		}
		return lMagistrati;
	}

	/**
	 * Esegue la ricerca del curatore corrente per id fascicolo.
	 * <p>
	 * Si effettua anche la ricerca del Magistrato.
	 * <p>
	 * 
	 * @param aKey
	 *            id del fascicolo.
	 * @return model aggregato con i dati del curatore e quelli del magistrato o dell'esperto.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public CuratoreSiusModel ExRicercaCurSiusByFascicolo(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;
		CuratoreSiusDAO lCurSiusDao = null;
		CuratoreSiusModel lCurSius = null;

		try {

			lConn = getDBConnection();
			lCurSiusDao = new CuratoreSiusDAO(lConn);
			lCurSiusDao.setCondizioneAttivo(aIdFascicolo);
			lCurSius = (CuratoreSiusModel) lCurSiusDao.getModelByKey();
			if (lCurSius != null)
				lCurSius.setDescrTipo(DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
						.getTipoCuratore(), lCurSius.getFlagTipo()));
		} catch (DAOException daoEx) {
			throw new F3BException("CuratoreSiusController.ExRicercaCurSiusByFascicolo: " + daoEx);
		} catch (Exception e) {
			throw new F3BException("CuratoreSiusController.ExRicercaCurSiusByFascicolo: " + e);
		} finally {
			cleanup(lCurSiusDao);
			cleanup(lConn);
		}
		return lCurSius;
	}

	/**
   * 
   */
	public void ExModificaMultiplaCuratoreSius(CuratoreSiusModel aCurSiusModel, String[] aListaFascicoli)
			throws F3BException {
		Connection lConn = null;

		CuratoreSiusDAO lCurSiusDao = null;

		try {
			lConn = getDBConnection();
			lCurSiusDao = new CuratoreSiusDAO(lConn);
			for (int i = 0; i < aListaFascicoli.length; i++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("id_fascicolo = " + aListaFascicoli[i]);

				BigDecimal lIdFascicoloSius = new BigDecimal(aListaFascicoli[i]);

				// ======================================================================
				// Aggiorno la data fine validità del vecchio magistrato
				// n.b. Il curatore attuale è quello con DATA_FINE = null
				// ======================================================================

				lCurSiusDao.setDataFine(DateUtils.getSysDate());

				lCurSiusDao.setCodOperatoreAggiornamento(aCurSiusModel.getCodOperatoreInserimento());
				lCurSiusDao.setCodUfficioAggiornamento(aCurSiusModel.getCodUfficioInserimento());
				lCurSiusDao.setDataAggiornamento(DateUtils.getSysDate());

				lCurSiusDao.setCondizioneUpdateCuratoreSiusCorrente(lIdFascicoloSius);
				lCurSiusDao.update();
				lCurSiusDao.stop();

				// ======================================================================
				// Inserisco il nuovo magistrato
				// ======================================================================
				aCurSiusModel.setFasSiuIdFascicoloSius(lIdFascicoloSius);
				lCurSiusDao.setDAOFromModel(aCurSiusModel);
				lCurSiusDao.insert();
				lCurSiusDao.stop();
			}

			commit(lConn);
			// rollback(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("CuratoreSiusController.ExModificaMultiplaCuratoreSius: " + ex);
		} finally {
			cleanup(lCurSiusDao);

			cleanup(lConn);
		}
	}

	/**
	 * <p>
	 * Description: : la funzione effettua la cancellazione di un curatore nella tabella
	 * CURATORE_FASCICOLO_SIUS
	 * 
	 * @param IdCuratore
	 *            : identificatore CURATORE, IdFascicolo identificatore del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public void ExCancellaCuratoreSius(BigDecimal IdCuratore, BigDecimal IdFascicolo) throws F3BException {
		Connection lConn = null;
		CuratoreSiusDAO lCurSiusDao = null;
		CuratoreSiusSqlDAO lCurSiusSqlDao = null;

		try {
			lConn = getDBConnection();
			lCurSiusDao = new CuratoreSiusDAO(lConn);
			lCurSiusDao.setCondizioneUpdate(IdCuratore, IdFascicolo);
			lCurSiusDao.delete();
			lCurSiusDao.stop();
			// Ripristino ultimo Curatore.
			lCurSiusSqlDao = new CuratoreSiusSqlDAO(lConn);
			lCurSiusSqlDao.ricercaCuratoreSiusByDataFine(IdFascicolo);
			lCurSiusSqlDao.start();
			if (lCurSiusSqlDao.next()) {
				CuratoreSiusModel lCurSiusMod = lCurSiusSqlDao.getModelCuratoreSius();
				if (lCurSiusMod != null) {
					lCurSiusMod.setDataFine(null);
					lCurSiusDao = new CuratoreSiusDAO(lConn);

					lCurSiusDao.setDAOFromModelForApertura(lCurSiusMod);
					lCurSiusDao.update();
				}
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Curatore collegato ad altri dati. Impossibile effettuare la Cancellazione!");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"CuratoreSiusController.ExCancellaCuratoreSius: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lCurSiusDao);
			cleanup(lCurSiusSqlDao);
			cleanup(lConn);
		}
	}

}