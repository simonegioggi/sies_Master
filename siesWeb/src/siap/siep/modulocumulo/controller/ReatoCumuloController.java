package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.ReatoCumuloDAO;
import siap.siep.modulocumulo.dao.ReatoCumuloSqlDAO;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;

/**
 * <p>
 * Title: ReatoCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Reato nell'ambito del CUMULO
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReatoCumuloController extends SiapController implements IReatoCumulo {

	/**
	 * Inserimento di piu' reati in tabella REATO_CUMULO
	 * 
	 * @param aReati
	 *            - ArrayList di reati
	 * @return ReatoModel - il reato inserito
	 * @throws F3BException
	 */

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public ReatoCumuloModel ExInserisciReatiCumulo(ArrayList aReati) throws F3BException {
		Connection lConn = null;

		ReatoCumuloDAO lReaCumDao = null;
		ReatoCumuloSqlDAO lReaCumSqlDAo = null;

		ReatoCumuloModel lReaPrincipale = null;

		try {
			lConn = getDBTransaction();

			//
			lReaPrincipale = new ReatoCumuloModel((ReatoCumuloModel) aReati.get(0));

			// Gestione Progressivo Reato
			lReaCumSqlDAo = new ReatoCumuloSqlDAO(lConn);
			BigDecimal lIdTitolo = lReaPrincipale.getTitIdTitoloCumulato();
			BigDecimal lProgrReato = lReaCumSqlDAo.getProgressivoReatoCumulo(lIdTitolo);

			lReaPrincipale.setProgrReato(new BigDecimal(lProgrReato.intValue() + 1));
			lReaPrincipale.setProgrCircostanza(new BigDecimal(1));

			// Inserimento primo Reato
			lReaCumDao = new ReatoCumuloDAO(lConn);
			lReaCumDao.setDAOFromModel(lReaPrincipale);

			BigDecimal lKeyReato = null;
			lKeyReato = lReaCumDao.insert();
			lReaPrincipale.setIdReatoCum(lKeyReato);
			lReaCumDao.stop();

			// Inserimento eventuali ulteriori 'circostanze'
			ReatoCumuloModel lReaMod = null;
			if (aReati.size() > 1) {
				int lProgrCirc = 2; // Il principale a 1

				for (int i = 1; i < aReati.size(); i++) {
					lReaMod = (ReatoCumuloModel) aReati.get(i);

					lReaMod.setProgrReato(lReaPrincipale.getProgrReato());
					lReaMod.setProgrCircostanza(new BigDecimal(lProgrCirc));

					lReaCumDao.setDAOFromModel(lReaMod);
					lReaCumDao.insert();
					lReaCumDao.stop();
					lProgrCirc++;
				}
			}

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.error("DAOException: ", ex);
			throw new F3BException("ReatoCumuloController.ExInserisciReatiCumulo: " + ex);
		} catch (Exception sqe) {
			rollback(lConn);
			siesLogger.error("Exception: ", sqe);
			throw new F3BException("ReatoControllerCumulo.ExInserisciReatiCumulo: " + sqe);
		} finally {
			cleanup(lReaCumDao);
			cleanup(lReaCumSqlDAo);

			cleanup(lConn);
		}

		return lReaPrincipale;
	}

	/**
	 * Ricerca i Reati e le Circostanze per il titolo passato nel model
	 * 
	 */
	public Vector<ReatoCumuloModel> ExRicercaReatoCumulo(ReatoCumuloModel aReatoCum) throws F3BException {
		Connection lConn = null;
		Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>();
		ReatoCumuloSqlDAO lReaCumDao = null;

		try {
			lConn = getDBConnection();
			lReaCumDao = new ReatoCumuloSqlDAO(lConn);
			lReaCumDao.ricercaReatoCumulo(aReatoCum);
			lReati = new Vector<ReatoCumuloModel>(lReaCumDao.getModels());
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoCumuloController.ExRicercaReatoCumulo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ReatoCumuloController.ExRicercaReatoCumulo:" + ex);
		} finally {
			cleanup(lReaCumDao);
			cleanup(lConn);
		}
		return lReati;
	}

	/**
	 * Effettua la ricerca in chiave
	 * 
	 * @param aIdReatoCumulo
	 */
	public ReatoCumuloModel ExRicercaReatoCumuloByKey(BigDecimal aIdReatoCumulo) throws F3BException {
		Connection lConn = null;
		ReatoCumuloSqlDAO lReaDao = null;
		ReatoCumuloModel lReaMod;

		try {
			lConn = getDBConnection();
			lReaDao = new ReatoCumuloSqlDAO(lConn);
			lReaDao.ricercaReatoCumuloByKey(aIdReatoCumulo);
			lReaMod = (ReatoCumuloModel) lReaDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoCumuloController.ExRicercaReatoCumuloByKey: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ReatoCumuloController.ExRicercaReatoCumuloByKey: " + ex);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	/**
	 * Modifica il Reato e eventuali altri record collegati che hanno lo stesso PROGR_REATO, quindi reato
	 * principale o norme collegati, in funzione del tipo di reato passato in input.
	 * 
	 * @param aReato
	 *            - reato da Modificare
	 * @throws F3BException
	 * 
	 */
	public ReatoCumuloModel ExModificaReatoCumulo(ReatoCumuloModel aReato) throws F3BException {
		Connection lConn = null;
		ReatoCumuloDAO lReaCumDao = null;

		ReatoCumuloModel lReaCumMod = new ReatoCumuloModel(aReato);

		try {
			lConn = getDBConnection();

			lReaCumDao = new ReatoCumuloDAO(lConn);

			if (lReaCumMod.getProgrCircostanza().intValue() == 1) {
				// Aggiorna il record principale
				siesLogger.debug("Aggiorna il record principale");
				lReaCumDao.setDAOFromModelForUpdate(lReaCumMod);
				lReaCumDao.update();
				lReaCumDao.stop();

				// Aggiorno anche eventuali ulteriori norme nei campi in comune
				siesLogger.debug("Aggiorno anche eventuali  ulteriori norme nei campi in comune");
				lReaCumDao.setDAOFromModelForUpdateUlterioriNorme(lReaCumMod);
				lReaCumDao.update();
				lReaCumDao.stop();
			} else {
				//
				siesLogger.debug("Aggiorno il record della singola norma");
				lReaCumDao.setDAOFromModelForUpdateNonPrimaNorma(lReaCumMod);
				lReaCumDao.update();
				lReaCumDao.stop();

				// Aggiorno i dati delle altre norme
				siesLogger.debug("Aggiorno i dati della norma principale");
				lReaCumDao.setFlagStato(lReaCumMod.getFlagStato());
				lReaCumDao.setMotivoModifica(lReaCumMod.getMotivoModificaNote());

				lReaCumDao.setCodOperatoreAggiornamento(lReaCumMod.getCodOperatoreAggiornamento());
				lReaCumDao.setDataAggiornamento(lReaCumMod.getDataAggiornamento());
				lReaCumDao.setCodUfficioAggiornamento(lReaCumMod.getCodUfficioAggiornamento());

				lReaCumDao.setCondizioneUpdateReatoECircostanze(lReaCumMod.getProgrReato(),
						lReaCumMod.getTitIdTitoloCumulato());
				lReaCumDao.update();
				lReaCumDao.stop();

			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			siesLogger.error("DAOException: " + ex);
			throw new F3BException("ReatoCumuloController.ExModificaReatoCumulo: " + ex);
		} catch (Exception ex) {
			rollback(lConn);

			siesLogger.error("Exception: " + ex);
			throw new F3BException("ReatoCumuloController.ExModificaReatoCumulo: " + ex);
		} finally {
			cleanup(lReaCumDao);
			cleanup(lConn);
		}

		return lReaCumMod;
	}

	/**
	 * Cancellaizone Reato_Cumulo
	 * 
	 * @param aReato
	 *            - reato da Cancellare
	 * @throws F3BException
	 */
	public void ExCancellaReatoCumulo(ReatoCumuloModel aReato) throws F3BException {
		Connection lConn = null;
		ReatoCumuloDAO lReaCumDao = null;

		try {
			lConn = getDBConnection();

			lReaCumDao = new ReatoCumuloDAO(lConn);

			// Cancellazione Logica
			if (aReato.getFlagStato().compareTo("E") == 0 || aReato.getFlagStato().compareTo("M") == 0) {
				lReaCumDao.setMotivoModifica(aReato.getMotivoModificaNote());
				lReaCumDao.setCodOperatoreAggiornamento(aReato.getCodOperatoreAggiornamento());
				lReaCumDao.setCodUfficioAggiornamento(aReato.getCodUfficioAggiornamento());
				lReaCumDao.setDataAggiornamento(aReato.getDataAggiornamento());

				lReaCumDao.setFlagStato("C");

				// se ho cancellato la norma base (ProgrCircostanza=1), devo cancellare anche le norme
				// "legate"
				if (aReato.getProgrCircostanza().intValue() == 1) {
					lReaCumDao.setCondizioneUpdateReatoECircostanze(aReato.getProgrReato(),
							aReato.getTitIdTitoloCumulato());
				} else {
					lReaCumDao.setCondizioneUpdate(aReato.getIdReatoCum());
				}

				lReaCumDao.update();

			} else {
				// se ho cancellato la norma base (ProgrCircostanza=1), devo cancellare anche le norme
				// "legate"
				if (aReato.getProgrCircostanza().intValue() == 1) {
					lReaCumDao.setCondizioneUpdateReatoECircostanze(aReato.getProgrReato(),
							aReato.getTitIdTitoloCumulato());
				} else {
					lReaCumDao.setCondizioneUpdate(aReato.getIdReatoCum());
				}

				lReaCumDao.delete();
			}
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			if (daoEx.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibbile cancellare il reato Cumulato collegato ad altri dati!");
			else
				throw new F3BException("ReatoCumuloController.ExCancellaReatoCumulo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			throw new F3BException("ReatoCumuloController.ExCancellaReatoCumulo: " + e);
		}

		finally {
			cleanup(lReaCumDao);
			cleanup(lConn);
		}
	}

	/**
	 *
	 * @param reatiMod
	 *            - Vettore dei reati da Aggiornare
	 * @param reatiCanc
	 *            - Vettore dei reati da Cancellare
	 * @throws F3BException
	 */
	public void ExOrganizzaReatiCum(Vector reatiMod, Vector reatiCanc) throws F3BException {
		Connection lConn = null;
		ReatoCumuloDAO lReaDaoMod = null;
		ReatoCumuloModel lReaMod = null;

		ReatoCumuloDAO lReaDaoCanc = null;
		ReatoCumuloModel lReaCanc = null;

		try {
			lConn = getDBConnection();

			// ==========================================
			// Record da aggiornare
			// ==========================================
			lReaDaoMod = new ReatoCumuloDAO(lConn);

			Iterator itx = reatiMod.iterator();

			while (itx.hasNext()) {
				lReaMod = new ReatoCumuloModel();

				lReaMod = (ReatoCumuloModel) itx.next();

				lReaDaoMod.setProgrReato(lReaMod.getProgrReato());
				lReaDaoMod.setProgrCircostanza(lReaMod.getProgrCircostanza());

				lReaDaoMod.setProgrNumeroManuale(lReaMod.getProgrNumeroManuale());

				lReaDaoMod.setCodTipoReato(lReaMod.getCodTipoReato());
				lReaDaoMod.setDataReato(lReaMod.getDataReato());
				lReaDaoMod.setDataInizio(lReaMod.getDataInizio());
				lReaDaoMod.setAnnoInizio(lReaMod.getAnnoInizio());
				lReaDaoMod.setMeseInizio(lReaMod.getMeseInizio());
				lReaDaoMod.setGiornoInizio(lReaMod.getGiornoInizio());
				lReaDaoMod.setDataFine(lReaMod.getDataFine());
				lReaDaoMod.setAnnoFine(lReaMod.getAnnoFine());
				lReaDaoMod.setMeseFine(lReaMod.getMeseFine());
				lReaDaoMod.setGiornoFine(lReaMod.getGiornoFine());
				lReaDaoMod.setCodPeriodoConsumazione(lReaMod.getCodPeriodoConsumazione());
				lReaDaoMod.setDescLuogo(lReaMod.getDescLuogo());
				lReaDaoMod.setNote(lReaMod.getNote());

				lReaDaoMod.setCodTipoPenaDetentiva(lReaMod.getCodTipoPenaDetentiva());
				lReaDaoMod.setNumGiorni(lReaMod.getNumGiorni());
				lReaDaoMod.setNumMesi(lReaMod.getNumMesi());
				lReaDaoMod.setNumAnni(lReaMod.getNumAnni());
				lReaDaoMod.setAnniIsolamentoDiurno(lReaMod.getNumAnniIsolamentoDiurno());
				lReaDaoMod.setMesiIsolamentoDiurno(lReaMod.getNumMesiIsolamentoDiurno());
				lReaDaoMod.setGiorniIsolamentoDiurno(lReaMod.getNumGiorniIsolamentoDiurno());
				lReaDaoMod.setSanzionePecuniaria(lReaMod.getSanzionePecuniaria());
				lReaDaoMod.setCodTipoSanzione(lReaMod.getCodTipoSanzione());

				if (lReaMod.getIdReatoCum() == null)
					throw new F3BException(
							"Errore non previsto in modifica: idReato non valorizzato, impossibile aggiornare in chiave");

				lReaDaoMod.setCondizioneUpdate(lReaMod.getIdReatoCum());

				lReaDaoMod.update();

				lReaDaoMod.stop();
			}

			// ==========================================
			// Record da cancellare
			// ==========================================
			lReaDaoCanc = new ReatoCumuloDAO(lConn);

			Iterator itx2 = reatiCanc.iterator();
			while (itx2.hasNext()) {
				lReaCanc = new ReatoCumuloModel();
				lReaCanc = (ReatoCumuloModel) itx2.next();

				if (lReaCanc.getIdReatoCum() == null)
					throw new DAOException(
							"Errore non previsto in cancellazione: idReato non valorizzato, impossibile aggiornare in chiave");

				lReaDaoCanc.setCondizioneUpdate(lReaCanc.getIdReatoCum());

				lReaDaoCanc.delete();

				lReaDaoCanc.stop();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException 1: ", daoEx);
			rollback(lConn);
			throw new F3BException("ReatoCumuloController.ExOrganizzaReatiCumulo: " + daoEx);
		} catch (Exception ex) {
			siesLogger.error("Exception 2: ", ex);
			rollback(lConn);
			throw new F3BException("ReatoCumuloController.ExOrganizzaReatiCumulo: " + ex);
		} finally {
			cleanup(lReaDaoMod);
			cleanup(lReaDaoCanc);
			cleanup(lConn);
		}

	}

	public ReatoCumuloModel ExModificaPenaReatoCumulo(ReatoCumuloModel aReato, Vector aNormeUlteriori)
			throws F3BException {
		Connection lConn = null;
		ReatoCumuloDAO lReaDao = null;
		ReatoCumuloModel lReaMod = new ReatoCumuloModel(aReato);

		try {
			lConn = getDBConnection();

			lReaDao = new ReatoCumuloDAO(lConn);

			lReaDao.setCodTipoPenaDetentiva(lReaMod.getCodTipoPenaDetentiva());
			lReaDao.setNumGiorni(lReaMod.getNumGiorni());
			lReaDao.setNumMesi(lReaMod.getNumMesi());
			lReaDao.setNumAnni(lReaMod.getNumAnni());
			lReaDao.setAnniIsolamentoDiurno(lReaMod.getNumAnniIsolamentoDiurno());
			lReaDao.setMesiIsolamentoDiurno(lReaMod.getNumMesiIsolamentoDiurno());
			lReaDao.setGiorniIsolamentoDiurno(lReaMod.getNumGiorniIsolamentoDiurno());
			// lReaDao.setDataInizioIsolamentoDiurno(lReaMod.getDataInizioIsolamentoDiurno());
			// lReaDao.setDataFineIsolamentoDiurno(lReaMod.getDataFineIsolamentoDiurno());
			lReaDao.setSanzionePecuniaria(lReaMod.getSanzionePecuniaria());
			lReaDao.setCodTipoSanzione(lReaMod.getCodTipoSanzione());

			lReaDao.setCodOperatoreAggiornamento(lReaMod.getCodOperatoreAggiornamento());
			lReaDao.setCodUfficioAggiornamento(lReaMod.getCodUfficioAggiornamento());
			lReaDao.setDataAggiornamento(lReaMod.getDataAggiornamento());

			lReaDao.setFlagStato(lReaMod.getFlagStato());
			lReaDao.setMotivoModifica(lReaMod.getMotivoModificaNote());

			Iterator itx = aNormeUlteriori.iterator();
			while (itx.hasNext()) {
				ReatoCumuloModel lNormaUlteriore = (ReatoCumuloModel) itx.next();
				lReaDao.setCondizioneUpdate(lNormaUlteriore.getIdReatoCum());
				lReaDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);

			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoCumuloController.ExModificaPenaReatoCumulo: " + daoEx);
		} finally {
			cleanup(lReaDao);
			cleanup(lConn);
		}

		return lReaMod;
	}

	/**
	 * Ricerca il ReatoCumulo e le circostanzeCumulo correlate per un TitoloCumulato
	 * 
	 * @param aKey
	 *            - Chiave Titolo cumulato
	 * @return Vettore di Reati Circostanze cumulo
	 * @throws F3BException
	 */
	public Vector<ReatoCircostanzaCumuloModel> ExRicercaReatoCircostanzaCumByTitoloCum(BigDecimal aTitoloKey)
			throws F3BException {
		Connection lConn = null;
		ReatoCumuloSqlDAO lReaSqlDao = null;
		Vector<ReatoCircostanzaCumuloModel> lListReaCirc = null;

		try {
			lConn = getDBConnection();
			lReaSqlDao = new ReatoCumuloSqlDAO(lConn);
			lReaSqlDao.ricercaReatiCumNoCircostanzaByIdTitolo(aTitoloKey);
			Vector<ReatoCumuloModel> lReati = new Vector<ReatoCumuloModel>(lReaSqlDao.getModels());
			lListReaCirc = new Vector<ReatoCircostanzaCumuloModel>();
			Iterator<ReatoCumuloModel> lItx = lReati.iterator();

			while (lItx.hasNext()) {
				ReatoCircostanzaCumuloModel aModel = new ReatoCircostanzaCumuloModel();
				aModel.setReatoCum((ReatoCumuloModel) lItx.next());
				lReaSqlDao.ricercaCircostanzeReatoCumByReatoTitoloCum(aModel.getReatoCum().getProgrReato(),
						aTitoloKey);
				List lCircostanze = new ArrayList(lReaSqlDao.getModels());
				aModel.setCircostanzeCum((ReatoCumuloModel[]) lCircostanze.toArray(new ReatoCumuloModel[0]));
				lListReaCirc.add(aModel);
			}
		} catch (DAOException daoEx) {
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("ReatoCumuloController.ExRicercaReatoCircostanzaCumByTitoloCum: " + daoEx);
		} finally {
			cleanup(lReaSqlDao);
			cleanup(lConn);
		}

		return lListReaCirc;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 * 
	 * @param
	 * @param
	 * @return
	 * @since MEV 42 Cumulo Step2
	 */
	public String ExInserisciReatiCumulatiWithoutSequence(Vector<ReatoCumuloModel> VecReatiCumu,
			Connection lConn) throws F3BException {
		String EsitodiRitorno = "00000";
		ReatoCumuloDAO lReaCumDao = null;
		ReatoCumuloModel lReaCumMod = null;

		try {
			lReaCumDao = new ReatoCumuloDAO(lConn);

			if (VecReatiCumu != null && VecReatiCumu.size() > 0) {
				Iterator ItxR = VecReatiCumu.iterator();
				while (ItxR.hasNext()) {
					lReaCumMod = (ReatoCumuloModel) ItxR.next();
					if (lReaCumMod != null && lReaCumMod.getIdReatoCum() != null) {
						try {
							lReaCumDao.setDAOFromModel(lReaCumMod);
							lReaCumDao.setWithoutSequence(true);
							lReaCumDao.insert();
							lReaCumDao.stop();
						} catch (DAOException daoEx) {
							if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.error(
										"Reato Cumulo gia' presente...>" + lReaCumMod.getIdReatoCum() + "<");
								EsitodiRitorno = "00001";
							} else {
								throw daoEx;
							}
						}
					}
				}
			}
		} catch (DAOException daoEx) {
			EsitodiRitorno = "01400";
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Reato Cumulo! ");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException("ReatoCumuloController.ExInserisciReatiCumulatiWithoutSequence: " + ex);
		} finally {
			cleanup(lReaCumDao);
		}

		return EsitodiRitorno;

	} // Chiude ExInserisciReatiCumulatiWithoutSequence()

} // CHIUDE ReatoCumuloController();