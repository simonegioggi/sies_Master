package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraCautelareCumuloSqlDAO;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;

/**
 * <p>
 * Title: MisuraCautelareCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraCautelareCumulo
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MisuraCautelareCumuloController extends SiapController implements IMisuraCautelareCumulo {

	/*****************************************************************************
	 * Effettua l'inserimento di un MisuraCautelareCumulo a partire dai dati contenuti nel Model
	 *
	 * @param aMisuraCautelareCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MisuraCautelareCumuloModel ExInserisciMisuraCautelareCumulo(
			MisuraCautelareCumuloModel aMisuraCautelareCumulo) throws F3BException {

		Connection lConn = null;
		MisuraCautelareCumuloDAO lMisDao = null;
		MisuraCautelareCumuloModel lMisMod = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareCumuloDAO(lConn);
			lMisDao.setDAOFromModel(aMisuraCautelareCumulo);
			BigDecimal lSequence = lMisDao.insert();
			commit(lConn);
			lMisMod = new MisuraCautelareCumuloModel(aMisuraCautelareCumulo);
			lMisMod.setMessage("Inserimento avvenuto correttamente!");
			lMisMod.setIdMisuraCautelareCumulo(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("MisuraCautelareCumuloController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati MisuraCautelareCumulo
	 *
	 * @param aMisuraCautelareCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector<MisuraCautelareCumuloModel> ExRicercaMisuraCautelareCumulo(
			MisuraCautelareCumuloModel aMisuraCautelareCumulo) throws F3BException {

		Connection lConn = null;
		Vector<MisuraCautelareCumuloModel> lMisuraCautelareCumuli = new Vector<>();
		MisuraCautelareCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareCumuloDAO(lConn);
			lMisDao.setCondizioni(aMisuraCautelareCumulo);
			lMisDao.setOrderBy();
			lMisDao.start();
			while (lMisDao.next()) {
				lMisuraCautelareCumuli.add((MisuraCautelareCumuloModel) lMisDao.getModel());
			}
			lMisDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExRicercaMisuraCautelareCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}

		return lMisuraCautelareCumuli;
	}

	/**
	 * Effettua la ricerca delle MC presenti sul titolo ordinate per data inizio misura
	 *
	 * @param aIdTitolo
	 * @return Vector <MisuraCautelareCumuloModel>
	 */
	public Vector<MisuraCautelareCumuloModel> ExRicercaMisureCautelariCumuloByIdTitolo(BigDecimal aIdTitolo)
			throws F3BException {

		Connection lConn = null;
		Vector<MisuraCautelareCumuloModel> lMisuraCautelareCumuli = new Vector<>();
		MisuraCautelareCumuloSqlDAO lMisSqlDao = null;

		try {
			lConn = getDBConnection();

			lMisSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);

			lMisSqlDao.ricercaMisuraCautelareCumuloByIdTitolo(aIdTitolo);

			lMisSqlDao.start();

			while (lMisSqlDao.next()) {
				lMisuraCautelareCumuli.add((MisuraCautelareCumuloModel) lMisSqlDao.getModel());
			}
			lMisSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx, daoEx);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExRicercaMisureCautelariCumuloByIdTitolo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisSqlDao);
			cleanup(lConn);
		}

		return lMisuraCautelareCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 *
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public MisuraCautelareCumuloModel ExRicercaMisuraCautelareCumuloById(BigDecimal aIdMisuraCautelareCumulo)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareCumuloModel lMisuraCautelareCumuloMod = new MisuraCautelareCumuloModel();
		MisuraCautelareCumuloSqlDAO lMisuraCautelareCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareCumuloSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);
			lMisuraCautelareCumuloSqlDao.ricercaMisuraCautelareCumuloByKey(aIdMisuraCautelareCumulo);
			lMisuraCautelareCumuloMod = (MisuraCautelareCumuloModel) lMisuraCautelareCumuloSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExRicercaMisuraCautelareCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareCumuloSqlDao);
			cleanup(lConn);
		}

		return lMisuraCautelareCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'MisuraCautelareCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 *
	 * @param aMisuraCautelareCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaMisuraCautelareCumulo(MisuraCautelareCumuloModel aMisuraCautelareCumulo)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareCumuloDAO(lConn);
			lMisDao.setDAOFromModelForUpdate(aMisuraCautelareCumulo);
			lMisDao.selCondizioneUpdate(aMisuraCautelareCumulo.getIdMisuraCautelareCumulo());
			lMisDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExModificaMisuraCautelareCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	public void ExCancellaMisuraCautelareCumuloLogica(MisuraCautelareCumuloModel aMisuraCautelareCumulo)
			throws F3BException {

		Connection lConn = null;
		MisuraCautelareCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraCautelareCumuloDAO(lConn);

			lMisDao.setFlagStato(aMisuraCautelareCumulo.getFlagStato());
			lMisDao.setMotivoModifica(aMisuraCautelareCumulo.getMotivoModifica());

			lMisDao.setCodOperatoreAggiornamento(aMisuraCautelareCumulo.getCodOperatoreAggiornamento());
			lMisDao.setCodUfficioAggiornamento(aMisuraCautelareCumulo.getCodUfficioAggiornamento());
			lMisDao.setDataAggiornamento(aMisuraCautelareCumulo.getDataAggiornamento());

			lMisDao.selCondizioneUpdate(aMisuraCautelareCumulo.getIdMisuraCautelareCumulo());

			lMisDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: " + ex);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExCancellaMisuraCautelareCumuloLogica: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 *
	 * @param aIdMisuraCautelareCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaMisuraCautelareCumulo(BigDecimal aIdMisuraCautelareCumulo) throws F3BException {

		Connection lConn = null;
		MisuraCautelareCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraCautelareCumuloDAO(lConn);
			lMisDao.selCondizioneUpdate(aIdMisuraCautelareCumulo);

			lMisDao.delete();

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			rollback(lConn);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExCancellaMisuraCautelareCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Recupera il numero di record restituiti della ricerca. Utile in caso di ricerche paginate per ottenere
	 * il numero totale di record
	 *
	 * @param aMisuraCautelareCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountMisuraCautelareCumulo(MisuraCautelareCumuloModel aMisuraCautelareCumulo)
			throws F3BException {

		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MisuraCautelareCumuloSqlDAO lMisuraCautelareCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareCumuloSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);
			lMisuraCautelareCumuloSqlDao.getCountMisuraCautelareCumulo(aMisuraCautelareCumulo);
			lMisuraCautelareCumuloSqlDao.start();
			lMisuraCautelareCumuloSqlDao.next();
			lCount = lMisuraCautelareCumuloSqlDao.getBigDecimal("HowManyRecords");
			lMisuraCautelareCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExGetCountMisuraCautelareCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 *
	 * @param aMisuraCautelareCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaMisuraCautelareCumuloPaged(MisuraCautelareCumuloModel aMisuraCautelareCumulo,
			int aPage) throws F3BException {

		Connection lConn = null;
		Vector lMisuraCautelareCumuli = new Vector();
		MisuraCautelareCumuloSqlDAO lMisuraCautelareCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraCautelareCumuloSqlDao = new MisuraCautelareCumuloSqlDAO(lConn);
			lMisuraCautelareCumuloSqlDao.ricercaMisuraCautelareCumuloPaged(aMisuraCautelareCumulo, aPage);
			lMisuraCautelareCumuli = new Vector(lMisuraCautelareCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExRicercaMisuraCautelareCumuloPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraCautelareCumuloSqlDao);
			cleanup(lConn);
		}
		return lMisuraCautelareCumuli;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param
	 * @param
	 * @return
	 * @sonce MEV 42 Cumulo Step2
	 */
	public String ExInserisciMisuraCautelareCumuloWithoutSequence(
			Vector<MisuraCautelareCumuloModel> aVecMisureCautelari, Connection lConn) throws F3BException {

		String lCodEsito = "00000";
		MisuraCautelareCumuloDAO lMisCauDao = null;
		MisuraCautelareCumuloModel lMisCauMod = null;
		try {

			lMisCauDao = new MisuraCautelareCumuloDAO(lConn);

			if (aVecMisureCautelari != null && aVecMisureCautelari.size() > 0) {
				for (int i = 0; i < aVecMisureCautelari.size(); i++) {
					lMisCauMod = aVecMisureCautelari.get(i);

					if (lMisCauMod != null && lMisCauMod.getIdMisuraCautelareCumulo() != null) {
						try {
							lMisCauDao.setDAOFromModel(lMisCauMod);
							lMisCauDao.setWithoutSequence(true);
							lMisCauDao.insert();
							lMisCauDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.error("Misura Cautelare Cumulo gia' presente..."
										+ lMisCauMod.getIdMisuraCautelareCumulo() + "<");
								lCodEsito = "00001";
							} else {
								throw ex;
							}
						}
					}
				}
			}
		} catch (DAOException ex) {
			lCodEsito = "01400";
			throw new F3BException(F3BException.USER_MESSAGE,
					"Impossibile inserire le Misure Cautelare Cumulo ! ");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"MisuraCautelareCumuloController.ExInserisciMisuraCautelareCumuloWithoutSequence : "
							+ ex);
		} finally {
			cleanup(lMisCauDao);
		}

		return lCodEsito;
	} // chiude ExInserisciMisuraCautelareCumuloWithoutSequence()

} // Chiude MisuraCautelareCumuloController()