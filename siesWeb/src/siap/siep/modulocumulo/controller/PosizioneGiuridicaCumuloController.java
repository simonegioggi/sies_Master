package siap.siep.modulocumulo.controller;

/**
* <p>Title: PosizioneGiuridicaCumuloController</p>
* <p>Description: Classe Controller per PosizioneGiuridicaCumulo</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloDAO;
import siap.siep.modulocumulo.dao.PosizioneGiuridicaCumuloSqlDAO;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class PosizioneGiuridicaCumuloController extends SiapController implements IPosizioneGiuridicaCumulo {

	/*****************************************************************************
	 * Effettua l'inserimento di un PosizioneGiuridicaCumulo a partire dai dati contenuti nel Model
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public PosizioneGiuridicaCumuloModel ExInserisciPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaCumuloDAO lComDao = null;
		PosizioneGiuridicaCumuloModel lComMod = null;

		try {
			lConn = getDBConnection();
			lComDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lComDao.setDAOFromModel(aPosizioneGiuridicaCumulo);
			BigDecimal lSequence = lComDao.insert();
			commit(lConn);

			lComMod = new PosizioneGiuridicaCumuloModel(aPosizioneGiuridicaCumulo);
			lComMod.setMessage("Inserimento avvenuto correttamente!");
			lComMod.setIdPosizioneGiuridicaCum(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExInserisciPosizioneGiuridicaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}

		return lComMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati PosizioneGiuridicaCumulo
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException {
		Connection lConn = null;
		Vector<PosizioneGiuridicaCumuloModel> lPosizioneGiuridicaCumuli = new Vector<>();
		PosizioneGiuridicaCumuloDAO lComDao = null;

		try {
			lConn = getDBConnection();
			lComDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lComDao.setCondizioni(aPosizioneGiuridicaCumulo);
			lComDao.setOrderBy();
			lComDao.start();
			while (lComDao.next()) {
				lPosizioneGiuridicaCumuli.add((PosizioneGiuridicaCumuloModel) lComDao.getModel());
			}
			lComDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}

		return lPosizioneGiuridicaCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati di PosizioneGiuridicaCumulo
	 * 
	 * @param aIdTitoloCumulato
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumulobyIdTitCum(
			BigDecimal aIdTitoloCumulato) throws F3BException {
		Connection lConn = null;
		Vector<PosizioneGiuridicaCumuloModel> lVectPosGiuCumulo = new Vector<>();
		PosizioneGiuridicaCumuloSqlDAO lPosGiuCumSqlDao = null;

		try {
			lConn = getDBConnection();
			lPosGiuCumSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosGiuCumSqlDao.ricercaPosizioneGiuridicaCumuloByIdTitoloCumulato(aIdTitoloCumulato);
			lVectPosGiuCumulo = new Vector<PosizioneGiuridicaCumuloModel>(lPosGiuCumSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumuloByIdTitCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPosGiuCumSqlDao);
			cleanup(lConn);
		}

		return lVectPosGiuCumulo;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public PosizioneGiuridicaCumuloModel ExRicercaPosizioneGiuridicaCumuloById(
			BigDecimal aIdPosizioneGiuridicaCumulo) throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaCumuloModel lPosizioneGiuridicaCumuloMod = new PosizioneGiuridicaCumuloModel();
		PosizioneGiuridicaCumuloSqlDAO lPosizioneGiuridicaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lPosizioneGiuridicaCumuloSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosizioneGiuridicaCumuloSqlDao.ricercaPosizioneGiuridicaCumuloByKey(aIdPosizioneGiuridicaCumulo);
			lPosizioneGiuridicaCumuloMod = (PosizioneGiuridicaCumuloModel) lPosizioneGiuridicaCumuloSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPosizioneGiuridicaCumuloSqlDao);
			cleanup(lConn);
		}

		return lPosizioneGiuridicaCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'PosizioneGiuridicaCumulo Viene fatto l'update di tutti i campi del
	 * record recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella
	 * verranno impostati a null
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaPosizioneGiuridicaCumulo(PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo)
			throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaCumuloDAO lComDao = null;

		try {
			lConn = getDBConnection();
			lComDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lComDao.setDAOFromModelForUpdate(aPosizioneGiuridicaCumulo);
			lComDao.selCondizioneUpdate(aPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum());
			lComDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExModificaPosizioneGiuridicaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaPosizioneGiuridicaCumuloBykey(BigDecimal aIdPosizioneGiuridicaCumulo)
			throws F3BException {
		Connection lConn = null;
		PosizioneGiuridicaCumuloDAO lComDao = null;

		if (aIdPosizioneGiuridicaCumulo == null)
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExCancellaPosizioneGiuridicaCumulo: Impossibile cancellare: idComputo non specificato.");

		try {
			lConn = getDBConnection();
			lComDao = new PosizioneGiuridicaCumuloDAO(lConn);
			lComDao.selCondizioneUpdate(aIdPosizioneGiuridicaCumulo);
			lComDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExCancellaPosizioneGiuridicaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lComDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountPosizioneGiuridicaCumulo(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		PosizioneGiuridicaCumuloSqlDAO lPosizioneGiuridicaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lPosizioneGiuridicaCumuloSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			/// lPosizioneGiuridicaCumuloSqlDao.getCountPosizioneGiuridicaCumulo(aPosizioneGiuridicaCumulo);
			lPosizioneGiuridicaCumuloSqlDao.start();
			lPosizioneGiuridicaCumuloSqlDao.next();
			lCount = lPosizioneGiuridicaCumuloSqlDao.getBigDecimal("HowManyRecords");
			lPosizioneGiuridicaCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExGetCountPosizioneGiuridicaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPosizioneGiuridicaCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aPosizioneGiuridicaCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumuloPaged(
			PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo, int aPage) throws F3BException {
		Connection lConn = null;
		Vector<PosizioneGiuridicaCumuloModel> lPosizioneGiuridicaCumuli = new Vector<>();
		PosizioneGiuridicaCumuloSqlDAO lPosizioneGiuridicaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lPosizioneGiuridicaCumuloSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			/// lPosizioneGiuridicaCumuloSqlDao.ricercaPosizioneGiuridicaCumuloPaged(aPosizioneGiuridicaCumulo,
			/// aPage );
			lPosizioneGiuridicaCumuli = new Vector<PosizioneGiuridicaCumuloModel>(
					lPosizioneGiuridicaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumuloPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lPosizioneGiuridicaCumuloSqlDao);
			cleanup(lConn);
		}
		return lPosizioneGiuridicaCumuli;
	}

	/**
	 * Recupera tutte le Richieste con Anticipazione (PosizioneGiuridica Cumulo) legati all'istruttoria
	 * indicata
	 * 
	 * @param aIdIstruttoria
	 *            = id dell'istruttoria
	 */
	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioneGiuridicaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, BigDecimal aIdDatiFinali) throws F3BException {
		Connection lConn = null;

		Vector<PosizioneGiuridicaCumuloModel> lPosizioneGiuridicaCumulo = new Vector<>();

		PosizioneGiuridicaCumuloSqlDAO lPosizioneGiuridicaSqlDao = null;

		try {
			lConn = getDBConnection();

			lPosizioneGiuridicaSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);

			if (aIdDatiFinali == null) {
				lPosizioneGiuridicaSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoria(aIdIstruttoria);
			} else {
				/// lPosizioneGiuridicaSqlDao.ricercaPosizioneGiuridicaCumuloByIdIstruttoriaDatiFinali
				/// (aIdIstruttoria, aIdDatiFinali);
			}

			lPosizioneGiuridicaCumulo = new Vector<PosizioneGiuridicaCumuloModel>(
					lPosizioneGiuridicaSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioneGiuridicaCumuloByIdIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lPosizioneGiuridicaSqlDao);

			cleanup(lConn);
		}

		return lPosizioneGiuridicaCumulo;
	}

	// MEV 26 CUMULO Step2
	// La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce, ma inserendo
	// il valore della Primary_Key è già preimpostato;
	// Questo metodo è usato nella funzione di presa in carico, per scaricare Tutti i dati del Fascicolo sulla
	// nuova Base dati.

	public String ExInserisciPosizioneGiuridicaCumuloWithoutSequence(
			Vector<PosizioneGiuridicaCumuloModel> VecPosizioneGiuridicaCum, Connection lConn)
			throws F3BException {
		String EsitodiRitorno = "00000";
		PosizioneGiuridicaCumuloDAO lCompCumDao = null;
		PosizioneGiuridicaCumuloModel lCompCumMod = null;

		try {
			lCompCumDao = new PosizioneGiuridicaCumuloDAO(lConn);

			if (VecPosizioneGiuridicaCum != null && VecPosizioneGiuridicaCum.size() > 0) {
				Iterator ItxC = VecPosizioneGiuridicaCum.iterator();
				while (ItxC.hasNext()) {
					lCompCumMod = (PosizioneGiuridicaCumuloModel) ItxC.next();
					if (lCompCumMod != null && lCompCumMod.getIdPosizioneGiuridicaCum() != null) {
						lCompCumDao.setDAOFromModel(lCompCumMod);
						lCompCumDao.setWithoutSequence(true);
						lCompCumDao.insert();
						lCompCumDao.stop();
					}
				}
			}
		} catch (DAOException daoEx) {
			if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
				siesLogger.error("PosizioneGiuridica Cumulo gia' presente...>"
						+ lCompCumMod.getIdPosizioneGiuridicaCum() + "<");
				EsitodiRitorno = "00001";
			} else {
				EsitodiRitorno = "01400";
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile inserire PosizioneGiuridicaCumulo! ");
			}
		} finally {
			cleanup(lCompCumDao);
		}

		return EsitodiRitorno;

	} // CHIUDE ExInserisciPosizioneGiuridicaCumuloWithoutSequence

	/**
	 * 
	 */
	public Vector<PosizioneGiuridicaCumuloModel> ExRicercaPosizioniGiuridicheTitoliByIdIstruttoria(
			BigDecimal aIdIstruttoria) throws F3BException {
		Connection lConn = null;

		Vector<PosizioneGiuridicaCumuloModel> lListaPosizioniGiuridiche = new Vector<>();

		PosizioneGiuridicaCumuloSqlDAO lPosizioneGiuridicaSqlDao = null;

		try {
			lConn = getDBConnection();

			lPosizioneGiuridicaSqlDao = new PosizioneGiuridicaCumuloSqlDAO(lConn);
			lPosizioneGiuridicaSqlDao.ricercaPosizioniGiuridicheTitoliByIdIstruttoria(aIdIstruttoria);
			lListaPosizioniGiuridiche = new Vector<PosizioneGiuridicaCumuloModel>(
					lPosizioneGiuridicaSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioniGiuridicheTitoliByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"PosizioneGiuridicaCumuloController.ExRicercaPosizioniGiuridicheTitoliByIdIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lPosizioneGiuridicaSqlDao);

			cleanup(lConn);
		}

		return lListaPosizioniGiuridiche;
	}

}