package siap.siep.penaresidua.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.evento.dao.EventoDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.dao.LicenzaLibanticipataDAO;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.siep.penaresidua.dao.PenaPrecedenteSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaDAO;
import siap.siep.penaresidua.dao.PenaResiduaDettaglioSqlDAO;
import siap.siep.penaresidua.dao.PenaResiduaSqlDAO;
import siap.siep.penaresidua.model.PenaPrecedenteModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaDAO;
import siap.siep.sanzionesostitutiva.dao.SanzioneSostResiduaSqlDAO;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.scadenzario.dao.ScadenzarioDAO;
import siap.siep.scadenzario.dao.ScadenzarioSqlDAO;
import siap.siep.scadenzario.model.ScadenzarioModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PenaResiduaController
 * </p>
 * <p>
 * Description: Classe Controller per PenaResidua
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PenaResiduaController extends SiapController implements IPenaResidua {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * ExInserisciPenaResidua
	 * 
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExInserisciPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;
		PenaResiduaDAO lPenDao = null;
		PenaResiduaModel lPenMod = null;

		try {
			lConn = getDBConnection();
			lPenMod = new PenaResiduaModel(aPenaResidua);
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaResidua);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();
			commit(lConn);
			lPenMod.setIdPenaResidua(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			throw new F3BException("PenaResiduaController.ExInserisciPenaResidua: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
   *
   */
	public PenaResiduaModel ExInserisciPenaResiduaScadenzario(PenaResiduaModel aPenaResidua, String aCodice)
			throws F3BException {
		Connection lConn = null;
		PenaResiduaDAO lPenDao = null;
		PenaResiduaModel lPenMod = null;
		ScadenzarioDAO lScaDAO = null;
		ScadenzarioSqlDAO lScaSqlDao = null;

		try {
			lConn = getDBTransaction();

			lPenMod = new PenaResiduaModel(aPenaResidua);

			// Inserisco la pena residua
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaResidua);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();

			// Aggiorno o Inserisco lo scadenzario se presente data fine pena
			if (aPenaResidua.getDataFine() != null) {
				ScadenzarioModel lScaModel = new ScadenzarioModel();

				// Recupero lo scadenzario (ne esiste solo uno)
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				lScaDAO = new ScadenzarioDAO(lConn);

				lScaSqlDao.ricercaScadenzarioByIdFascicolo(aPenaResidua.getFasSieIdFascicoloSiep());
				lScaModel = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaModel != null) {
					// aggiorna scadenzario
					/** TODO ma il tipo_scadenzario non lo aggiorna? */

					lScaDAO.setDataFineScadenza(aPenaResidua.getDataFine());

					lScaDAO.setCodUfficioAggiornamento(aPenaResidua.getCodUfficioInserimento());
					lScaDAO.setCodOperatoreAggiornamento(aPenaResidua.getCodOperatoreInserimento());
					lScaDAO.setDataAggiornamento(aPenaResidua.getDataInserimento());
					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDAO.setCondizioneUpdate(lScaModel.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				} else {
					ScadenzarioModel lScaModelIns = new ScadenzarioModel();

					lScaModelIns.setCodTipoScadenzario(aCodice);
					lScaModelIns.setDataFineScadenza(aPenaResidua.getDataFine());
					lScaModelIns.setDataInizioScadenza(aPenaResidua.getDataInizio());

					lScaModelIns.setCodOperatoreInserimento(aPenaResidua.getCodOperatoreInserimento());
					lScaModelIns.setDataInserimento(aPenaResidua.getDataInserimento());
					lScaModelIns.setCodUfficioInserimento(aPenaResidua.getCodUfficioInserimento());

					lScaModelIns.setFasSieIdFascicoloSiep(aPenaResidua.getFasSieIdFascicoloSiep());

					lScaDAO.setDAOFromModel(lScaModelIns);
					lScaDAO.insert();
					lScaDAO.stop();
				}
			}

			commit(lConn);
			lPenMod.setIdPenaResidua(lKey);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("ExInserisciPenaResiduaScadenzario.ExUpdateOE : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("ExInserisciPenaResiduaScadenzario.ExUpdateOE : " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lScaDAO);
			cleanup(lScaSqlDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
	 * Inserisci pena Residua per JMS senza assegnare la sequence
	 * 
	 * @param aPenaPresunta
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciPenaResiduaWithoutSequence(PenaResiduaModel aPenaPresunta, Connection lConn)
			throws F3BException {
		String lCodEsito = "00000";
		PenaResiduaDAO lPenDao = null;
		try {
			if (aPenaPresunta != null && aPenaPresunta.getFlagValidato().compareTo("S") == 0) // 31/08/2010
			{
				lPenDao = new PenaResiduaDAO(lConn);
				lPenDao.setDAOFromModel(aPenaPresunta);
				lPenDao.setWithoutSequence(true);
				lPenDao.insert();
			}
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.error("Pena Residua gia' presente...");
				lCodEsito = "00001";
			} else {
				lCodEsito = "01400";
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire la Pena Residua! ");
			}
		} finally {
			cleanup(lPenDao);
		}
		return lCodEsito;
	}

	/**
	 * Inserisci i records di Pena Residua per JMS senza assegnare la sequence
	 * 
	 * @param aPenaResidua
	 * @param lConn
	 * @return lCodEsito
	 * @throws F3BException
	 */
	public String ExInserisciPeneResidueWithoutSequence(ArrayList aPenaResidua, Connection lConn)
			throws F3BException {
		String lCodEsito = "00000";
		PenaResiduaDAO lPenDao = null;
		PenaResiduaModel lPenResMod = null;

		if (aPenaResidua != null && aPenaResidua.size() > 0) {
			for (int i = 0; i < aPenaResidua.size(); i++) {
				try {
					lPenDao = new PenaResiduaDAO(lConn);

					lPenResMod = (PenaResiduaModel) aPenaResidua.get(i);
					if (lPenResMod != null) {
						if (lPenResMod.getIdPenaResidua() != null
								&& lPenResMod.getFlagValidato().compareTo("S") == 0) // 31/08/2010
						{
							lPenDao.setDAOFromModel(lPenResMod);
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("=========> PENA RESIDUA scritta----->");
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("lPenResMod.getIdPenaResidua = " + lPenResMod.getIdPenaResidua());
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
							// posto di LogF3B.getLogger()
							siesLogger.info("lPenResMod.getEveIdEvento = " + lPenResMod.getEveIdEvento());

							lPenDao.setWithoutSequence(true);
							lPenDao.insert();
							lPenDao.stop();
						}
					}
				}

				catch (DAOException ex) {
					if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.error("Pena Residua gia' presente...");
						lCodEsito = "00001";
					}
					// 21/12/2010 Si tralascia la pena residua non completa di Evento collegato.
					else if (ex.INTEGRITY_CONSTRAINT_VIOLATED) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.error("Pena Residua non inserita Chiave madre non trovata ( EVENTO )...");
						lCodEsito = "00001";
					} else {
						lCodEsito = "01400";
						throw new F3BException(F3BException.USER_MESSAGE,
								"Impossibile inserire la Pena Residua! ");
					}
				} finally {
					cleanup(lPenDao);
				}
			}
		}
		return lCodEsito;
	}

	/**
	 * Ricerca il record pena residua inserito più recentemente (data inserimento), validato o meno.
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltima(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;
		try {
			lConn = getDBConnection();
			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepData(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltima: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}
		return lPenResMod;
	}

	/*****************************************************************************
	 * Recupera l'ultimo record PENA_RESIDUA <b>VALIDATO</b> per il fascicolo passato in input
	 * 
	 * @param aKey
	 *            - id del fascicolo per cui fare la ricerca
	 * @return PenaResiduaModel
	 * @throws F3BException
	 ************************************************************************** */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaByDate(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaByDate: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/*****************************************************************************
	 * Recupera l'ultimo record PENA RESIDUA inserito indipendentemente dallo stato.
	 *
	 * @param aKey
	 *            - id fascicolo
	 * @return
	 * @throws F3BException
	 ************************************************************************** */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepDataDesc_IgnoraValidazione(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaResiduaController.ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/*****************************************************************************
	 * Restituisce l'ultima pena residua validata (data inserimento più recente)
	 *
	 * @param aKey
	 *            -id del fascicolo
	 * @return
	 * @throws F3BException
	 ************************************************************************** */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidata(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidata(aIdFascicolo);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaValidata: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente e Data Fine Pena valorizzata (not
	 * null)
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataDataFinePena(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataDataFinePena(aIdFascicolo);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaValidataDataFinePena: "
					+ daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Ricerca Pena Residua Ultima Validata Sospesa
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataSospesa(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesa(aIdFascicolo);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaValidataSospesa: "
					+ daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Ricerca Pena Residua Ultima NON Validata Sospesa
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaNonValidataSospesa(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaNonValidataSospesa(aIdFascicolo);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaNonValidataSospesa: "
					+ daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Ricerca Pena Residua Ultima Validata Sospesa Interruzione
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataSospesaInterruzione(BigDecimal aIdFascicolo)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao
					.ricercaPenaResiduaCorrenteByFascicoloSiepUltimaValidataSospesaInterruzione(aIdFascicolo);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaResiduaController.ExRicercaPenaResiduaUltimaValidataSospesaInterruzione: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Inserisci aggiorna pena residua VA
	 * 
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExInserisciAggiornaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;
		PenaResiduaModel lPenMod = null;
		PenaResiduaSqlDAO lPenResDao = null;

		try {
			lConn = getDBTransaction();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenDao = new PenaResiduaDAO(lConn);

			// aggiorna usando lo stesso model che viene passato
			if (aPenaResidua.getFlagValidato().equals("N")) {
				lPenDao.setDAOFromModelForUpdate(aPenaResidua);
				lPenDao.update();
				lPenMod = aPenaResidua;
			} else if (aPenaResidua.getFlagValidato().equals("S")) // fine aggiornamento inizio inserimento
			{
				aPenaResidua.setFlagValidato("N");
				lPenDao.setDAOFromModel(aPenaResidua);
				BigDecimal lKey = null;
				lKey = lPenDao.insert();
				lPenMod = new PenaResiduaModel(aPenaResidua);
				lPenMod.setIdPenaResidua(lKey);
			}
			// fine inserimento

			commit(lConn);

		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaResiduaController.ExInserisciAggiornaPenaResidua: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lPenResDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
	 * Aggiorna pena residua
	 * 
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExAggiornaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;
		PenaResiduaModel lPenMod = null;

		try {
			lConn = getDBTransaction();
			lPenDao = new PenaResiduaDAO(lConn);

			lPenDao.setDAOFromModelForUpdate(aPenaResidua);
			lPenDao.update();
			lPenMod = aPenaResidua;

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaResiduaController.ExAggiornaPenaResidua: " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	public PenaResiduaModel ExInsertOrUpdatePenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenDao = null;
		SanzioneSostResiduaDAO lSSResiduaDao = null;
		SanzioneSostResiduaSqlDAO lSSResiduaSqlDao = null;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaResiduaSqlDAO(lConn);
			aPenaResidua = lPenDao.inserisciOModificaPenaResidua(aPenaResidua);

			if (aPenaResidua.getSanzSostResidua() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Inserisco anche SS residua");

				// ======================================================================
				// n.b. Verifico se esiste una SS già legata alla PR, in questo caso
				// vado in update, altrimenti vado in insert
				// ======================================================================
				lSSResiduaSqlDao = new SanzioneSostResiduaSqlDAO(lConn);
				lSSResiduaSqlDao.ricercaSanzioneSostResiduaByIdPenRes(aPenaResidua.getIdPenaResidua());

				SanzioneSostResiduaModel lSSPresente = (SanzioneSostResiduaModel) lSSResiduaSqlDao
						.getModelByKey();

				SanzioneSostResiduaModel lSSResiduaModel = aPenaResidua.getSanzSostResidua();

				if (lSSPresente != null && lSSPresente.getIdSanzioneSostResidua() != null) {
					// Vado in Update
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Vado in Update = " + lSSPresente);

					lSSResiduaDao = new SanzioneSostResiduaDAO(lConn);

					lSSResiduaModel.setIdSanzioneSostResidua(lSSPresente.getIdSanzioneSostResidua());
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lSSResiduaModel = " + lSSResiduaModel);

					lSSResiduaDao.setDAOFromModelForUpdate(lSSResiduaModel);
					lSSResiduaDao.update();
					lSSResiduaDao.stop();
				} else {
					// Vado in insert
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Vado in Insert");

					lSSResiduaModel.setPenResIdPenaResidua(aPenaResidua.getIdPenaResidua());
					lSSResiduaDao = new SanzioneSostResiduaDAO(lConn);

					lSSResiduaDao.setDAOFromModel(lSSResiduaModel);
					BigDecimal lKeySSres = lSSResiduaDao.insert();
					lSSResiduaDao.stop();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lKeySSres Inserita = " + lKeySSres);
				}

			}

			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExInsertOrUpdatePenaResidua: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lSSResiduaDao);
			cleanup(lSSResiduaSqlDao);

			cleanup(lConn);
		}
		return aPenaResidua;
	}

	/**
	 *
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;
		Vector lPenaResidui = new Vector();
		PenaResiduaSqlDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResidua(aPenaResidua);
			lPenaResidui = new Vector(lPenDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResidua: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenaResidui;
	}

	/****************************************************************************
	 * Recupera il record PENA_RESIDUA con data inserimento più recente indipendentemente dallo stato.
	 *
	 * @param aKey
	 *            del Fascicolo SIEP
	 * @return PenaResiduaModel
	 * @throws F3BException
	 ************************************************************************* */
	public PenaResiduaModel ExRicercaPenaResiduaCorrenteByFascicoloSiep(BigDecimal aKey) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;

		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaCorrenteByFascicoloSiep: "
					+ daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/*****************************************************************************
	 * Ricerca la pena residua NON VALIDATA ('N') inserita più recentemente. (ne può esistere più di una non
	 * validata?)
	 * 
	 * @param aKey
	 * @return pena residua con flagValidato = 'N' più recente
	 * @throws F3BException
	 ************************************************************************** */
	public PenaResiduaModel ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;
		try {
			lConn = getDBConnection();

			lPenResDao = new PenaResiduaSqlDAO(lConn);

			lPenResDao.ricercaPenaResiduaFlagNonValidatoDesc(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			throw new F3BException(
					"PenaResiduaController.ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}

		return lPenResMod;
	}

	/**
	 * Pena residua corrente con flagPiùMeno dell'evento valorizzato
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaCorrenteFlagPiuMenoByFascicoloSiep(BigDecimal aKey)
			throws F3BException {
		Connection lConn = null;
		PenaResiduaSqlDAO lPenResDao = null;
		PenaResiduaModel lPenResMod;
		try {
			lConn = getDBConnection();
			lPenResDao = new PenaResiduaSqlDAO(lConn);
			lPenResDao.ricercaPenaResiduaCorrenteFlagPiuMenoByFascicoloSiep(aKey);
			lPenResMod = (PenaResiduaModel) lPenResDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"PenaResiduaController.ExRicercaPenaResiduaCorrenteFlagPiuMenoByFascicoloSiep: " + daoEx);
		} finally {
			cleanup(lPenResDao);
			cleanup(lConn);
		}
		return lPenResMod;
	}

	/**
	 * Ricerca Pena tramite chiave
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaByKey(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PenaResiduaSqlDAO lPenDao = null;
		PenaResiduaModel lPenMod;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResiduaByKey(aKey);
			lPenMod = (PenaResiduaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaByKey: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPenMod;
	}

	/**
	 * Ricerca Pena tramite chiave Evento
	 * 
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaByIdEvento(BigDecimal aIdEvento) throws F3BException {
		Connection lConn = null;

		PenaResiduaSqlDAO lPenDao = null;
		PenaResiduaModel lPenMod;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaResiduaSqlDAO(lConn);

			lPenDao.ricercaPenaResiduaByKeyEvento(aIdEvento);
			lPenMod = (PenaResiduaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaByIdEvento: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenMod;
	}

	/**
	 * Ricerca Pena Residua tramite chiave Fascicolo
	 * 
	 * @param aIdFasicolo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaPenaResiduaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException {
		Connection lConn = null;

		Vector lPenaResidua = new Vector();
		PenaResiduaSqlDAO lPenDao = null;
//		PenaResiduaModel lPenMod;

		try {
			lConn = getDBConnection();

			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResiduaByIdFascicoloDataDesc(aIdFascicolo);
			lPenaResidua = new Vector(lPenDao.getModels());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaByIdFascicolo: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPenaResidua;
	}

	/**
	 * Modifica Pena Residua
	 * 
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExModificaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;

		PenaResiduaDAO lPenDao = null;

		PenaResiduaModel lPenMod = new PenaResiduaModel(aPenaResidua);

		try {
			lConn = getDBConnection();

			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModelForUpdate(aPenaResidua);
			lPenDao.update();

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("PenaResiduaController.ExModificaPenaResidua: " + ex);
		} finally {
			cleanup(lPenDao);

			cleanup(lConn);
		}

		return lPenMod;
	}

	public void ExCancellaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException {
		Connection lConn = null;
		PenaResiduaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setCondizioneUpdate(aPenaResidua.getIdPenaResidua());
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExCancellaPenaResidua: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Cancella tutti i record pena residua non validati associati al fascicolo
	 * 
	 * @param aFascID
	 * @throws F3BException
	 */
	public void ExCancellaPenaResiduaNonValidata(BigDecimal aFascID) throws F3BException {
		Connection lConn = null;
		PenaResiduaDAO lPenDao = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setCondizioneUpdateNonValidato(aFascID);
			lPenDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExCancellaPenaResiduaNonValidata: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerco la penultima pena Residua Inserita a sistema (data inserimento) validata o meno per il
	 * fascicolo specificato
	 *
	 * @param aFascID
	 * @return PenaPrecedenteModel
	 * @throws F3BException
	 */
	public PenaPrecedenteModel ExRicercaPenaPrecedenteByKeyFascicolo(BigDecimal aFascID) throws F3BException {
		Connection lConn = null;
		PenaPrecedenteSqlDAO lPenDao = null;
		Vector lPrec = null;
		PenaPrecedenteModel lPrecModel = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaPrecedenteSqlDAO(lConn);
			lPenDao.ricercaPenaPrecedenteByFascicolo(aFascID);
			lPrec = new Vector(lPenDao.getModels());
			// Prese tutte le pene residue ordinate per Data, prendo la penultima...
			if (lPrec != null && lPrec.size() >= 2) {
				lPrecModel = (PenaPrecedenteModel) lPrec.get(1);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaPrecedenteByKeyFascicolo: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPrecModel;
	}

	/**
	 * Ricerca l'ultima Pena Residua inserita sul fascicolo, Validata o meno
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaPerFascicolo(BigDecimal aKey) throws F3BException {
		Connection lConn = null;
		PenaResiduaSqlDAO lPenDao = null;
		PenaResiduaModel lPrecModel = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaSqlDAO(lConn);
			lPenDao.ricercaPenaResiduaByIdFascicoloDataDesc(aKey);
			lPrecModel = (PenaResiduaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaResiduaUltimaPerFascicolo: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}

		return lPrecModel;
	}

	/**
	 * Ricerco la pena Residua Inserita subito prima dell'evento selezionato Questo metodo è indispensabile
	 * per il rework del dettaglio Alcuni provveidmenti tipo MA Prosecuzione nel momento in cui vengono
	 * inseriti fanno riferimento alla pena precedente validata, in seguito l'unico modo per ritrovarla è
	 * cercare la pena relativa all'evento e selezionare quella immediatamente precedente.
	 *
	 * @param aEveID
	 *            - Id dell'evento
	 * @return PenaResiduaModel
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaPrecedenteByKeyEvento(BigDecimal aFascID, BigDecimal aEveID)
			throws F3BException {
		Connection lConn = null;
		PenaResiduaDettaglioSqlDAO lPenDao = null;
		PenaResiduaModel lPrecModel = null;

		try {
			lConn = getDBConnection();
			lPenDao = new PenaResiduaDettaglioSqlDAO(lConn);
			lPenDao.ricercaPenultimaPenaDaEvento(aFascID, aEveID);
			lPrecModel = (PenaResiduaModel) lPenDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException("PenaResiduaController.ExRicercaPenaPrecedenteByKeyEvento: " + daoEx);
		} finally {
			cleanup(lPenDao);
			cleanup(lConn);
		}
		return lPrecModel;
	}

	/**
	 * Inserisce i dati della pena Residua Manuale ed eventualmente aggiorna lo scadenzario.
	 * 
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aLibAnt
	 *            null se non presenti
	 * @param aCodTipoScadenzario
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciPenaResiduaManuale(EventoModel aEvento, PenaResiduaModel aPenaResidua,
	// LicenzaLibAnticipataModel aLibAnt,
			Vector<LicenzaLibAnticipataModel> aListaLicenze, String aCodTipoScadenzario) throws F3BException {
		Connection lConn = null;

		EventoModel lEveMod = null;
		PenaResiduaModel lPenMod = null;

		PenaResiduaDAO lPenDao = null;
		ScadenzarioDAO lScaDAO = null;
		ScadenzarioSqlDAO lScaSqlDao = null;

		EventoDAO lEveDAO = null;
		LicenzaLibanticipataDAO lLicDAO = null;

		try {
			lConn = getDBTransaction();
			// =================================================================
			// Inserisco l'evento
			// =================================================================
			lEveMod = new EventoModel(aEvento);
			lEveDAO = new EventoDAO(lConn);
			lEveDAO.setDAOFromModel(lEveMod);
			lEveDAO.setCodOperatoreAggiornamento(lEveMod.getCodOperatoreAggiornamento());
			lEveDAO.setDataAggiornamento(lEveMod.getDataAggiornamento());
			lEveDAO.setCodUfficioAggiornamento(lEveMod.getCodUfficioAggiornamento());

			BigDecimal lKeyEvento = null;
			lKeyEvento = lEveDAO.insert();
			lEveDAO.stop();
			lEveMod.setIdEvento(lKeyEvento);

			if (aListaLicenze != null) {
				lLicDAO = new LicenzaLibanticipataDAO(lConn);

				for (int i = 0; i < aListaLicenze.size(); i++) {
					LicenzaLibAnticipataModel lLicModel = aListaLicenze.elementAt(i);

					lLicModel.setEveIdEvento(lEveMod.getIdEvento());

					if (aPenaResidua != null
							&& aPenaResidua.getDataInizio() != null
							&& aPenaResidua.getDataFine() != null
							&& (aPenaResidua.getFlagErgastolo() == null || (aPenaResidua.getFlagErgastolo() != null && aPenaResidua
									.getFlagErgastolo().equals("N")))) {
						lLicModel.setFlagElaborato("S");
					}

					lLicDAO.setDAOFromModel(lLicModel);
					lLicDAO.insert();
					lLicDAO.stop();
				}
			}

			/*
			 * 
			 * //================================================================= // Inserisco la Liberazine
			 * Anticipata //================================================================= // 20/05/2014
			 * Nuova L.A. : viene inserita una licenza per ogni tipo ci Concessione L.A. presente : // max 3
			 * righe : L.A. , L.A. Speciale e L.A. Integrazione
			 * //=================================================================== if ( aLibAnt!=null ) {
			 * if(ggLibAnt_Ord!=null && ggLibAnt_Ord.intValue() != 0) { lLicDAO = new
			 * LicenzaLibanticipataDAO(lConn); aLibAnt.setEveIdEvento(lEveMod.getIdEvento());
			 * 
			 * // 20/05/2014 Nuova L.A. aLibAnt.setNumeroGiorni(ggLibAnt_Ord);
			 * aLibAnt.setDescrStatoPermesso("LA"); // End Nuova L.A.
			 * 
			 * // Se la pena è in decorenza e non trattasi di ergastolo flaggo le LA come // computate if(
			 * aPenaResidua != null && aPenaResidua.getDataInizio() != null && aPenaResidua.getDataFine() !=
			 * null && ( aPenaResidua.getFlagErgastolo()==null || (aPenaResidua.getFlagErgastolo()!=null &&
			 * aPenaResidua.getFlagErgastolo().equals("N")) ) ) { aLibAnt.setFlagElaborato("S"); }
			 * lLicDAO.setDAOFromModel(aLibAnt); lLicDAO.insert(); lLicDAO.stop(); }
			 * 
			 * if(ggLibAnt_Spe!=null && ggLibAnt_Spe.intValue() != 0) { lLicDAO = new
			 * LicenzaLibanticipataDAO(lConn); aLibAnt.setEveIdEvento(lEveMod.getIdEvento());
			 * 
			 * // 20/05/2014 Nuova L.A. aLibAnt.setNumeroGiorni(ggLibAnt_Spe);
			 * aLibAnt.setDescrStatoPermesso("LS"); // End Nuova L.A.
			 * 
			 * // Se la pena è in decorenza e non trattasi di ergastolo flaggo le LA come // computate if(
			 * aPenaResidua != null && aPenaResidua.getDataInizio() != null && aPenaResidua.getDataFine() !=
			 * null && ( aPenaResidua.getFlagErgastolo()==null || (aPenaResidua.getFlagErgastolo()!=null &&
			 * aPenaResidua.getFlagErgastolo().equals("N")) ) ) { aLibAnt.setFlagElaborato("S"); }
			 * lLicDAO.setDAOFromModel(aLibAnt); lLicDAO.insert(); lLicDAO.stop(); }
			 * 
			 * if(ggLibAnt_Int!=null && ggLibAnt_Int.intValue() != 0) { lLicDAO = new
			 * LicenzaLibanticipataDAO(lConn); aLibAnt.setEveIdEvento(lEveMod.getIdEvento());
			 * 
			 * // 20/05/2014 Nuova L.A. aLibAnt.setNumeroGiorni(ggLibAnt_Int);
			 * aLibAnt.setDescrStatoPermesso("LI"); // End Nuova L.A.
			 * 
			 * // Se la pena è in decorenza e non trattasi di ergastolo flaggo le LA come // computate if(
			 * aPenaResidua != null && aPenaResidua.getDataInizio() != null && aPenaResidua.getDataFine() !=
			 * null && ( aPenaResidua.getFlagErgastolo()==null || (aPenaResidua.getFlagErgastolo()!=null &&
			 * aPenaResidua.getFlagErgastolo().equals("N")) ) ) { aLibAnt.setFlagElaborato("S"); }
			 * lLicDAO.setDAOFromModel(aLibAnt); lLicDAO.insert(); lLicDAO.stop(); }
			 * 
			 * }
			 */

			// =================================================================
			// Inserisco la pena residua
			// =================================================================
			lPenMod = new PenaResiduaModel(aPenaResidua);
			lPenMod.setEveIdEvento(lEveMod.getIdEvento());

			aPenaResidua.setEveIdEvento(lEveMod.getIdEvento());
			lPenDao = new PenaResiduaDAO(lConn);
			lPenDao.setDAOFromModel(aPenaResidua);
			BigDecimal lKey = null;
			lKey = lPenDao.insert();
			lPenDao.stop();

			// =================================================================
			// Aggiorno o Inserisco lo scadenzario se presente data fine pena
			// =================================================================
			if (aPenaResidua.getDataFine() != null) {
				ScadenzarioModel lScaModel = new ScadenzarioModel();

				// Recupero lo scadenzario (ne esiste solo uno)
				lScaSqlDao = new ScadenzarioSqlDAO(lConn);
				lScaDAO = new ScadenzarioDAO(lConn);

				lScaSqlDao.ricercaScadenzarioByIdFascicolo(aPenaResidua.getFasSieIdFascicoloSiep());
				lScaModel = (ScadenzarioModel) lScaSqlDao.getModelByKey();

				if (lScaModel != null) {
					// aggiorna scadenzario
					lScaDAO.setDataFineScadenza(aPenaResidua.getDataFine());

					lScaDAO.setCodUfficioAggiornamento(aPenaResidua.getCodUfficioInserimento());
					lScaDAO.setCodOperatoreAggiornamento(aPenaResidua.getCodOperatoreInserimento());
					lScaDAO.setDataAggiornamento(aPenaResidua.getDataInserimento());
					// lScaModAgg.setIdScadenzario(lScaMod.getIdScadenzario());
					lScaDAO.setCondizioneUpdate(lScaModel.getIdScadenzario());
					lScaDAO.update();
					lScaDAO.stop();
				} else {
					ScadenzarioModel lScaModelIns = new ScadenzarioModel();

					lScaModelIns.setCodTipoScadenzario(aCodTipoScadenzario);
					lScaModelIns.setDataFineScadenza(aPenaResidua.getDataFine());
					lScaModelIns.setDataInizioScadenza(aPenaResidua.getDataInizio());

					lScaModelIns.setCodOperatoreInserimento(aPenaResidua.getCodOperatoreInserimento());
					lScaModelIns.setDataInserimento(aPenaResidua.getDataInserimento());
					lScaModelIns.setCodUfficioInserimento(aPenaResidua.getCodUfficioInserimento());

					lScaModelIns.setFasSieIdFascicoloSiep(aPenaResidua.getFasSieIdFascicoloSiep());

					lScaDAO.setDAOFromModel(lScaModelIns);
					lScaDAO.insert();
					lScaDAO.stop();
				}
			}

			commit(lConn);
			lPenMod.setIdPenaResidua(lKey);
		} catch (DAOException daoEx) {
			rollback(lConn);
			daoEx.printStackTrace();
			throw new F3BException("ExInserisciPenaResiduaManuale : " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			throw new F3BException("ExInserisciPenaResiduaManuale : " + ex);
		} finally {
			cleanup(lPenDao);
			cleanup(lScaDAO);
			cleanup(lScaSqlDao);
			cleanup(lEveDAO);
			cleanup(lLicDAO);

			cleanup(lConn);
		}

		return lEveMod;
	}

}