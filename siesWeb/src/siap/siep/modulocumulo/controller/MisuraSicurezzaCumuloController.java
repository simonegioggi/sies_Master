package siap.siep.modulocumulo.controller;

/**
* <p>Title: MisuraSicurezzaCumuloController</p>
* <p>Description: Classe Controller per MisuraSicurezzaCumulo</p>
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
import siap.sico.ufficio.dao.UfficioSqlDAO;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.dao.DatiFinaliCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloDAO;
import siap.siep.modulocumulo.dao.MisuraSicurezzaCumuloSqlDAO;
import siap.siep.modulocumulo.dao.ProcedimentoCumulatoSqlDAO;
import siap.siep.modulocumulo.dao.TitoloCumulatoSqlDAO;
import siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MisuraSicurezzaCumuloController extends SiapController implements IMisuraSicurezzaCumulo {

	/*****************************************************************************
	 * Effettua l'inserimento di un MisuraSicurezzaCumulo a partire dai dati contenuti nel Model
	 * 
	 * @param aMisuraSicurezzaCumulo
	 *            Model con i dati da inserire
	 * @return il model con i dati inseriti e l'aggiunta dell'id del record inserito
	 * @throws F3BException
	 ****************************************************************************/
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public MisuraSicurezzaCumuloModel ExInserisciMisuraSicurezzaCumulo(
			MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo) throws F3BException {
		Connection lConn = null;
		MisuraSicurezzaCumuloDAO lMisDao = null;
		MisuraSicurezzaCumuloModel lMisMod = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaCumuloDAO(lConn);
			lMisDao.setDAOFromModel(aMisuraSicurezzaCumulo);
			BigDecimal lSequence = lMisDao.insert();
			commit(lConn);

			lMisMod = new MisuraSicurezzaCumuloModel(aMisuraSicurezzaCumulo);
			lMisMod.setMessage("Inserimento avvenuto correttamente!");
			lMisMod.setIdMisuraSicurezzaCumulo(lSequence);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExInserisciMisuraSicurezzaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lMisDao);

			cleanup(lConn);
		}

		return lMisMod;
	}

	/*****************************************************************************
	 * Effettua la ricerca dei dati MisuraSicurezzaCumulo
	 * 
	 * @param aMisuraSicurezzaCumulo
	 *            Model utilizzato per costruire le condizioni di ricerca Ogni valore attualizzato nel model
	 *            verrà utilizzato per imporre una condizione di ricerca
	 * @return un vettore di model con il risultato della ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException {
		Connection lConn = null;
		Vector lMisuraSicurezzaCumuli = new Vector();
		MisuraSicurezzaCumuloSqlDAO lMisSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisSqlDao.ricercaMisuraSicurezzaCumulo(aMisuraSicurezzaCumulo);
			lMisuraSicurezzaCumuli = new Vector(lMisSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisuraSicurezzaCumulo: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisuraSicurezzaCumulo: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lMisSqlDao);
			cleanup(lConn);
		}

		return lMisuraSicurezzaCumuli;
	}

	/*****************************************************************************
	 * Effettua la ricerca per chiave
	 * 
	 * @param akey
	 *            valore della chiave del record da ricercare
	 * @return il model con i dati trovati
	 * @throws F3BException
	 ****************************************************************************/
	public MisuraSicurezzaCumuloModel ExRicercaMisuraSicurezzaCumuloById(BigDecimal aIdMisuraSicurezzaCumulo)
			throws F3BException {
		Connection lConn = null;
		MisuraSicurezzaCumuloModel lMisuraSicurezzaCumuloMod = new MisuraSicurezzaCumuloModel();
		MisuraSicurezzaCumuloSqlDAO lMisuraSicurezzaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraSicurezzaCumuloSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisuraSicurezzaCumuloSqlDao.ricercaMisuraSicurezzaCumuloByKey(aIdMisuraSicurezzaCumulo);
			lMisuraSicurezzaCumuloMod = (MisuraSicurezzaCumuloModel) lMisuraSicurezzaCumuloSqlDao
					.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisuraSicurezzaCumuloById: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraSicurezzaCumuloSqlDao);
			cleanup(lConn);
		}

		return lMisuraSicurezzaCumuloMod;
	}

	/*****************************************************************************
	 * Metodo che modifica i dati dell'MisuraSicurezzaCumulo Viene fatto l'update di tutti i campi del record
	 * recuperando i valori dal Model Se mancano dati nel model i corrispondenti valori della tabella verranno
	 * impostati a null
	 * 
	 * @param aMisuraSicurezzaCumulo
	 *            Model con i nuovi valori
	 * @throws F3BException
	 ****************************************************************************/
	public void ExModificaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException {
		Connection lConn = null;
		MisuraSicurezzaCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			lMisDao = new MisuraSicurezzaCumuloDAO(lConn);
			lMisDao.setDAOFromModelForUpdate(aMisuraSicurezzaCumulo);
			lMisDao.selCondizioneUpdate(aMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo());
			lMisDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExModificaMisuraSicurezzaCumulo: Non posso inserire: "
							+ ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lConn);
		}
	}

	/*****************************************************************************
	 * Effettua la cancellazione del record
	 * 
	 * @param aMisuraSicurezzaCumulo
	 * @throws F3BException
	 ****************************************************************************/
	public void ExCancellaMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException {
		Connection lConn = null;
		MisuraSicurezzaCumuloDAO lMisDao = null;

		try {
			lConn = getDBConnection();
			if (aMisuraSicurezzaCumulo.getFlagStato().equals("E")
					|| aMisuraSicurezzaCumulo.getFlagStato().equals("M")) {
				// Effettuo la cancellazione logica del dato
				aMisuraSicurezzaCumulo.setFlagStato("C");

				lMisDao = new MisuraSicurezzaCumuloDAO(lConn);
				lMisDao.setDAOFromModelForDelete(aMisuraSicurezzaCumulo);
				lMisDao.selCondizioneUpdate(aMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo());
				lMisDao.update();
			} else {
				// Effettuo la cancellazione fisica
				lMisDao = new MisuraSicurezzaCumuloDAO(lConn);
				lMisDao.selCondizioneUpdate(aMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo());
				lMisDao.delete();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			rollback(lConn);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExCancellaMisuraSicurezzaCumulo: Non posso leggere : "
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
	 * @param aMisuraSicurezzaCumulo
	 * @return numero di record trovati dalla funzione dei ricerca
	 * @throws F3BException
	 ****************************************************************************/
	public BigDecimal ExGetCountMisuraSicurezzaCumulo(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo)
			throws F3BException {
		Connection lConn = null;
		BigDecimal lCount = new BigDecimal(0);
		MisuraSicurezzaCumuloSqlDAO lMisuraSicurezzaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraSicurezzaCumuloSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisuraSicurezzaCumuloSqlDao.getCountMisuraSicurezzaCumulo(aMisuraSicurezzaCumulo);
			lMisuraSicurezzaCumuloSqlDao.start();
			lMisuraSicurezzaCumuloSqlDao.next();
			lCount = lMisuraSicurezzaCumuloSqlDao.getBigDecimal("HowManyRecords");
			lMisuraSicurezzaCumuloSqlDao.stop();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExGetCountMisuraSicurezzaCumulo: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraSicurezzaCumuloSqlDao);
			cleanup(lConn);
		}

		return lCount;
	}

	/*****************************************************************************
	 * Funzione di ricerca utilizzata per la paginazione che restituisce i risultati da visualizzare nella
	 * pagina specificata in input
	 * 
	 * @param aMisuraSicurezzaCumulo
	 *            model contenete i parametri della ricerca
	 * @param aPage
	 *            pagina per la quale si vogliono ottenere i risultati
	 * @return Vettore di model con i record da visualizzare nella pagina specificata in input
	 * @throws F3BException
	 ****************************************************************************/
	public Vector ExRicercaMisuraSicurezzaCumuloPaged(MisuraSicurezzaCumuloModel aMisuraSicurezzaCumulo,
			int aPage) throws F3BException {
		Connection lConn = null;
		Vector lMisuraSicurezzaCumuli = new Vector();
		MisuraSicurezzaCumuloSqlDAO lMisuraSicurezzaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraSicurezzaCumuloSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisuraSicurezzaCumuloSqlDao.ricercaMisuraSicurezzaCumuloPaged(aMisuraSicurezzaCumulo, aPage);
			lMisuraSicurezzaCumuli = new Vector(lMisuraSicurezzaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisuraSicurezzaCumuloPaged: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraSicurezzaCumuloSqlDao);
			cleanup(lConn);
		}
		return lMisuraSicurezzaCumuli;
	}

	/**
	 * Recupera tutte le misure di sicurezza iscritte sui Titoli iscritto nell'istruttoria Aggiunge ad ogni
	 * MisuraSicurezzaCumuloModel anche i dati del titolo di riferimento
	 */
	public Vector<MisuraSicurezzaCumuloModel> ExRicercaMisureSicurezzaCumuloByIdIstruttoria(
			BigDecimal aIdIstruttoria, boolean aFlagDatiFinali) throws F3BException {
		Connection lConn = null;

		Vector<MisuraSicurezzaCumuloModel> lMisuraSicurezzaCumuli = new Vector<>();

		MisuraSicurezzaCumuloSqlDAO lMisSqlDao = null;
		TitoloCumulatoSqlDAO lTitoloSqlDao = null;
		ProcedimentoCumulatoSqlDAO lProcedimentoSqlDao = null;
		UfficioSqlDAO lUfficioSqlDao = null;

		try {
			lConn = getDBConnection();

			lMisSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);

			lMisSqlDao.ricercaMisureSicurezzaCumuloByIdIstruttoria(aIdIstruttoria, aFlagDatiFinali);

			lMisuraSicurezzaCumuli = new Vector<MisuraSicurezzaCumuloModel>(lMisSqlDao.getModels());

			Iterator<MisuraSicurezzaCumuloModel> iter = lMisuraSicurezzaCumuli.iterator();

			// ==================================================
			// Per ogni Misura recupero i dati del Titolo
			// ==================================================
			lTitoloSqlDao = new TitoloCumulatoSqlDAO(lConn);
			lUfficioSqlDao = new UfficioSqlDAO(lConn);
			while (iter.hasNext()) {
				MisuraSicurezzaCumuloModel lMisuraModel = iter.next();

				lTitoloSqlDao.ricercaTitoloCumulatoByKey(lMisuraModel.getTitIdTitoloCumulato());

				TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel) lTitoloSqlDao.getModelByKey();
				lTitoloSqlDao.stop();

				// Recupero il procedimento cumulato se presente
				lProcedimentoSqlDao = new ProcedimentoCumulatoSqlDAO(lConn);
				lProcedimentoSqlDao.ricercaProcedimentoCumulatoByIdTitolo(lTitoloModel.getIdTitoloCumulato());

				ProcedimentoCumulatoModel lProcCumModel = (ProcedimentoCumulatoModel) lProcedimentoSqlDao
						.getModelByKey();
				lProcedimentoSqlDao.stop();

				if (lProcCumModel != null && "S".equals(lProcCumModel.getFlagAccorpato())) {
					lUfficioSqlDao.ricercaUfficioByCod(lProcCumModel.getChiaveUfficioOrigine());
					UfficioModel lUffModel = (UfficioModel) lUfficioSqlDao.getModelByKey();
					lProcCumModel.setUfficioOrigine(lUffModel);
					lUfficioSqlDao.stop();
				}

				lTitoloModel.setProcedimentoCumulato(lProcCumModel);

				lMisuraModel.setTitoloCumulato(lTitoloModel);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisureSicurezzaCumuloByIdIstruttoria: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisureSicurezzaCumuloByIdIstruttoria: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lMisSqlDao);
			cleanup(lTitoloSqlDao);
			cleanup(lProcedimentoSqlDao);
			cleanup(lUfficioSqlDao);

			cleanup(lConn);
		}

		return lMisuraSicurezzaCumuli;
	}

	/**
	 * 
	 */
	public void ExAggiornaMisureDatiFinaliCumulo(Vector<MisuraSicurezzaCumuloModel> aListaMisure,
			BigDecimal aIdFascMS, BigDecimal aIdDatiFinali) throws F3BException {
		Connection lConn = null;

		MisuraSicurezzaCumuloDAO lMisDao = null;
		DatiFinaliCumuloDAO lDatiFinaliDao = null;

		Iterator<MisuraSicurezzaCumuloModel> iterMisure = aListaMisure.iterator();

		try {
			lConn = getDBConnection();

			lMisDao = new MisuraSicurezzaCumuloDAO(lConn);

			while (iterMisure.hasNext()) {
				MisuraSicurezzaCumuloModel lMisuraModel = iterMisure.next();

				lMisDao.setFlagDatiFinali(lMisuraModel.getFlagDatiFinali());

				lMisDao.setCondizioneUpdate(lMisuraModel.getIdMisuraSicurezzaCumulo());
				lMisDao.update();
			}

			//
			lDatiFinaliDao = new DatiFinaliCumuloDAO(lConn);
			lDatiFinaliDao.selCondizioneUpdate(aIdDatiFinali);

			if (aIdFascMS == null) {
				lDatiFinaliDao.setFlagCreaFascicoloMs(null);
				lDatiFinaliDao.setFasSieIdFascicoloSiepMs(null);
			} else {
				lDatiFinaliDao.setFlagCreaFascicoloMs("S");
				if (aIdFascMS.compareTo(new BigDecimal(0)) > 0) {
					lDatiFinaliDao.setFasSieIdFascicoloSiepMs(aIdFascMS);
				} else {
					lDatiFinaliDao.setFasSieIdFascicoloSiepMs(null);
				}
			}

			lDatiFinaliDao.update();

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExAggiornaMisureDatiFinaliCumulo: Non posso leggere : "
							+ daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			siesLogger.error("Exception: ", ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExAggiornaMisureDatiFinaliCumulo: Non posso leggere : "
							+ ex);
		} finally {
			cleanup(lMisDao);
			cleanup(lDatiFinaliDao);

			cleanup(lConn);
		}

		return;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 * 
	 * @param
	 * @param
	 * @return
	 * @since MEV 42 Cumulo step 2
	 */
	public String ExInserisciMisuraSicurezzaCumuloWithoutSequence(
			Vector<MisuraSicurezzaCumuloModel> aVecMisure, Connection lConn) throws F3BException {
		String lCodEsito = "00000";
		MisuraSicurezzaCumuloDAO lMisDao = null;
		MisuraSicurezzaCumuloModel lMisMod = null;
		try {
			lMisDao = new MisuraSicurezzaCumuloDAO(lConn);

			if (aVecMisure != null && aVecMisure.size() > 0) {
				for (int i = 0; i < aVecMisure.size(); i++) {
					lMisMod = aVecMisure.get(i);

					if (lMisMod != null && lMisMod.getIdMisuraSicurezzaCumulo() != null) {
						try {
							lMisDao.setDAOFromModel(lMisMod);
							lMisDao.setWithoutSequence(true);
							lMisDao.insert();
							lMisDao.stop();
						} catch (DAOException ex) {
							if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.error("Misura Sicurezza Cumulo gia' presente..."
										+ lMisMod.getIdMisuraSicurezzaCumulo() + "<");
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
					"Impossibile inserire le Misure Sicurezza Cumulo ! ");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExInserisciMisuraSicurezzaCumuloWithoutSequence : "
							+ ex);
		} finally {
			cleanup(lMisDao);
		}

		return lCodEsito;

	} // chiude ExInserisciMisuraSicurezzaCumuloWithoutSequence()

	public Vector<MisuraSicurezzaCumuloModel> ExRicercaMisureSicurezzaCumuloByIdTitoloCum(
			BigDecimal aIdTitolo) throws F3BException {
		Connection lConn = null;
		Vector<MisuraSicurezzaCumuloModel> lMisuraSicurezzaCumuli = new Vector<>();
		MisuraSicurezzaCumuloSqlDAO lMisuraSicurezzaCumuloSqlDao = null;

		try {
			lConn = getDBConnection();
			lMisuraSicurezzaCumuloSqlDao = new MisuraSicurezzaCumuloSqlDAO(lConn);
			lMisuraSicurezzaCumuloSqlDao.ricercaMisuraSicurezzaCumuloByIdTitoloCum(aIdTitolo);
			lMisuraSicurezzaCumuli = new Vector(lMisuraSicurezzaCumuloSqlDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: ", daoEx);
			throw new F3BException(
					"MisuraSicurezzaCumuloController.ExRicercaMisureSicurezzaCumuloByIdTitoloCum: Non posso leggere : "
							+ daoEx);
		} finally {
			cleanup(lMisuraSicurezzaCumuloSqlDao);
			cleanup(lConn);
		}
		return lMisuraSicurezzaCumuli;

	} // Chiude ExRicercaMisureSicurezzaCumuloByIdTitoloCum

} // Chiude MisuraSicurezzaCumuloController