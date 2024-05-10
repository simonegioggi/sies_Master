package siap.sius.esecuzionesanzionesostitutiva.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaDAO;
import siap.sius.esecuzionesanzionesostitutiva.dao.EsecuzioneSanzioneSostitutivaSqlDAO;
import siap.sius.esecuzionesanzionesostitutiva.model.ESSFascGPModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: EsecuzioneSSController
 * </p>
 * <p>
 * Description: Classe Controller per EsecuzioneSanzioneSostitutiva
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EsecuzioneSSController extends SiapController implements IEsecuzioneSS {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public EsecuzioneSanzioneSostitutivaModel ExInserisciEsecuzioneSanzioneSostitutiva(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod = null;
		try {
			lConn = getDBConnection();
			lEseMod = new EsecuzioneSanzioneSostitutivaModel(aEsecuzioneSanzioneSostitutiva);
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setDAOFromModel(aEsecuzioneSanzioneSostitutiva);
			BigDecimal lKey = null;
			lKey = lEseDao.insert();
			commit(lConn);
			lEseMod.setIdEsecuzioneSanzioneSost(lKey);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExInserisciEsecuzioneSanzioneSostitutiva: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public Vector ExRicercaEsecuzioneSanzioneSostitutiva(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;
		Vector lEsecuzioneSanzioniSostitutive = new Vector();
		EsecuzioneSanzioneSostitutivaSqlDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneSanzioneSostitutiva(aEsecuzioneSanzioneSostitutiva);
			lEsecuzioneSanzioniSostitutive = new Vector(lEseDao.getModels());
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioneSostitutiva: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEsecuzioneSanzioniSostitutive;
	}

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByKey(BigDecimal aKey)
			throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneSanzioneSostitutivaByKey(aKey);
			lEseMod = (EsecuzioneSanzioneSostitutivaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioneSostitutivaByKey: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByAnnoProg(
			BigDecimal aAnno, BigDecimal aProg) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod = null;
		if (aAnno != null && aProg != null) {
			try {
				lConn = getDBConnection();
				lEseDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
				lEseDao.ricercaEsecuzioneSanzioneSostitutivaByAnnoProg(aAnno, aProg);
				lEseMod = (EsecuzioneSanzioneSostitutivaModel) lEseDao.getModelByKey();
			} catch (DAOException daoEx) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("DAOException: " + daoEx);
				throw new SIUSException(F3BException.USER_MESSAGE,
						"EsecuzioneSSController.ExRicercaEsecuzioneSanzioneSostitutivaByAnnoProg: " + daoEx);
			} catch (Exception e) {
				rollback(lConn);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("Exception: " + e);
				throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
			} finally {
				cleanup(lEseDao);
				cleanup(lConn);
			}
		}
		return lEseMod;
	}

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
			BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			lEseDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneSanzioneSostitutivaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneSanzioneSostitutivaModel ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(
			BigDecimal aKey, Connection aConn) throws F3BException {

		EsecuzioneSanzioneSostitutivaSqlDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod;
		try {
			lEseDao = new EsecuzioneSanzioneSostitutivaSqlDAO(aConn);
			lEseDao.ricercaEsecuzioneSanzioneSostitutivaByIdFascicolo(aKey);
			lEseMod = (EsecuzioneSanzioneSostitutivaModel) lEseDao.getModelByKey();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
		}
		return lEseMod;
	}

	public EsecuzioneSanzioneSostitutivaModel ExModificaEsecuzioneSanzioneSostitutiva(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod = new EsecuzioneSanzioneSostitutivaModel(
				aEsecuzioneSanzioneSostitutiva);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);

			lEseDao.setDAOFromModelForUpdate(aEsecuzioneSanzioneSostitutiva);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExModificaEsecuzioneSanzioneSostitutiva : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public EsecuzioneSanzioneSostitutivaModel ExModificaESSbyFascicolo(
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutiva) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		EsecuzioneSanzioneSostitutivaModel lEseMod = new EsecuzioneSanzioneSostitutivaModel(
				aEsecuzioneSanzioneSostitutiva);
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);

			lEseDao.setDAOFromModelForUpdatebyFascicolo(aEsecuzioneSanzioneSostitutiva);
			lEseDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + ex);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExModificaEsecuzioneSanzioneSostitutiva : " + ex);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
		return lEseMod;
	}

	public void ExCancellaEsecuzioneSanzioneSostitutiva(BigDecimal idESS) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaDAO lEseDao = null;
		try {
			lConn = getDBConnection();
			lEseDao = new EsecuzioneSanzioneSostitutivaDAO(lConn);
			lEseDao.setCondizioneUpdate(idESS);
			lEseDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExCancellaEsecuzioneSanzioneSostitutiva: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseDao);
			cleanup(lConn);
		}
	}

	/**
	 * Ricerca Procedimenti di Esecuzione Sanzioni Sostitutive
	 *
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di ESSFascGPModel
	 * @throws F3BException
	 */
	public Vector ExRicercaEsecuzioneSanzioniSostitutive(String lAnno, String lProgr, String lAnnoIniziale,
			String lProgrIniziale, String lAnnoFinale, String lProgrFinale, String lUfficioUtenteConnesso,
			int aPageNum, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		Vector lEsecuzioneSS = new Vector();

		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;

		try {
			lConn = getDBConnection();
			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
			lEseSSSqlDao.ricercaEsecuzioneSanzioniSostitutive(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso, lCodContenuto);
			lEseSSSqlDao.startPage(aPageNum);

			ESSFascGPModel lEMaFascGP = null;
			while (lEseSSSqlDao.next()) {
				lEMaFascGP = (ESSFascGPModel) lEseSSSqlDao.getEsecuzioneSanzioniSostitutive();
				lEsecuzioneSS.add(lEMaFascGP);
			}
			lEseSSSqlDao.stop();

			if (lEsecuzioneSS.isEmpty()){
			  if ("U019".equals(lCodContenuto))
				throw new SIUSException(F3BException.USER_MESSAGE,"Esecuzione Sanzione Sostitutiva non trovata");
			  else if ("U126".equals(lCodContenuto))
			    throw new SIUSException(F3BException.USER_MESSAGE,"Esecuzione Pena Sostitutiva non trovata");
	          else 
	            throw new SIUSException(F3BException.USER_MESSAGE,"Esecuzione non trovata");
			}
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioniSostitutive: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lConn);
		}
		return lEsecuzioneSS;
	}

	/**
	 * Ricerca Numero Procedimenti di Esecuzione Sanzioni Sostitutive.
	 *
	 * @param lAnno
	 * @param lProgr
	 * @param lAnnoIniziale
	 * @param lProgrIniziale
	 * @param lAnnoFinale
	 * @param lProgrFinale
	 * @param lUfficioUtenteConnesso
	 * @return Vettore di ESSFascGPModel
	 * @throws F3BException
	 */
	public BigDecimal ExGetNumRicercaEsecuzioneSanzioniSostitutive(String lAnno, String lProgr,
			String lAnnoIniziale, String lProgrIniziale, String lAnnoFinale, String lProgrFinale,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			// MEV_2023_35 si aggiunge un ulteriore parametro lCodContenuto
			lEseSSSqlDao.ricercaEsecuzioneSanzioniSostitutive(lAnno, lProgr, lAnnoIniziale, lProgrIniziale,
					lAnnoFinale, lProgrFinale, lUfficioUtenteConnesso, lCodContenuto);
			lCont = lEseSSSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaEsecuzioneSanzioniSostitutive: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Sanzioni Sostitutive. (Dettaglio Esec. Mis.
	 * Alt.).
	 *
	 * @param aEseSSKey
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	// MEV_2023-35 si parametrizza il lCodContenutoper gestire anche le EPS
	public Vector ExRicercaDettaglioEsecuzioneSS(BigDecimal aEseSSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		Vector lSanzioni = new Vector();
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			// MEV_2023-35 si parametrizza il lCodContenutoper gestire anche le EPS
			lEseSSSqlDao.ricercaDettaglioEsecuzioneSS(aEseSSKey, lUfficioUtenteConnesso, lCodContenuto);
			lEseSSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseSSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseSSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lSanzioni.add(lFascicolo);

			}
			lEseSSSqlDao.stop();

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaDettaglioEsecuzioneSS: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}
		return lSanzioni;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Sanzioni Sostitutive. (Dettaglio Esec. San.
	 * Sos.).
	 *
	 * @param aEseSSKey
	 * @param aIdSoggetto
	 * @param aConn
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 */
	// FIXME probabilmente mai richiamato
	// MEV_2023-35 si parametrizza il lCodContenuto per gestire anche le EPS
	public Vector ExRicercaDettaglioEsecuzioneSS(BigDecimal aEseSSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, Connection aConn, String lCodContenuto) throws F3BException {

		Vector lSanzioni = new Vector();
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(aConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(aConn);
			// MEV_2023-35 si parametrizza il lCodContenuto per gestire anche le EPS
			lEseSSSqlDao.ricercaDettaglioEsecuzioneSS(aEseSSKey, lUfficioUtenteConnesso, lCodContenuto);
			lEseSSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseSSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseSSSqlDao.getFascicoloSiusGPModel();

				// Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso da quello
				// del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(aConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lSanzioni.add(lFascicolo);

			}
			lEseSSSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaDettaglioEsecuzioneSS: " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lTenDao);
			// cleanup(lSogDao);
		}
		return lSanzioni;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Sanzioni Sostitutive. (Dettaglio Esec. San.
	 * Sos.). e per ogni Sanzione, si cercano i procedimenti correlati.
	 *
	 * @param aEseSSKey
	 * @param aIdSoggetto
	 * @return Coppia di Vettori di FascicoloGPModel
	 * @throws F3BException
	 */
	// MEV_2023-35 si parametrizza il lCodContenutoper gestire anche le EPS
	public Vector[] ExRicercaDettaglioESSeCorrelati(BigDecimal aEseSSKey, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		Vector[] lSanzioniECorrelati = new Vector[2];
		Vector lSanzioni = new Vector();
		Vector lCorrelati = new Vector();
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao2 = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			// MEV_2023-35 si parametrizza il lCodContenutoper gestire anche le EPS
			lEseSSSqlDao.ricercaDettaglioEsecuzioneSS(aEseSSKey, lUfficioUtenteConnesso, lCodContenuto);
			lEseSSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseSSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseSSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// Si Caricano i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}
				lSanzioni.add(lFascicolo);

				// Caricamento Procedimenti Correlati all'ESS corrente.
				if (lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null
						&& lFascicolo.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0) {
					lEseSSSqlDao2 = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
					lEseSSSqlDao2.ricercaProcedimentiCorrelatiAllESS(
							lFascicolo.getFascicoloSiusModel().getIdFascicoloSius(), lUfficioUtenteConnesso);
					lEseSSSqlDao2.start();
					FascicoloGPModel lFascCorrelato = null;
					while (lEseSSSqlDao2.next()) {
						lFascCorrelato = (FascicoloGPModel) lEseSSSqlDao2.getFascicoloSiusGPModel();
						lCorrelati.add(lFascCorrelato);
					}
					lEseSSSqlDao2.stop();
				}
			}
			lEseSSSqlDao.stop();

		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaDettaglioESSeCorrelati: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lEseSSSqlDao2);
			cleanup(lTenDao);
			// cleanup(lSogDao);
			cleanup(lConn);
		}
		lSanzioniECorrelati[0] = lSanzioni;
		lSanzioniECorrelati[1] = lCorrelati;
		// return lSanzioni;
		return lSanzioniECorrelati;
	}

	/**
	 * Ricerca dei Procedimenti relativi ad una Esecuzione di Sanzioni Sostitutive per IdFascicolo. (Dettaglio
	 * Esec. Mis. Alt.).
	 *
	 * @param aIdFascicolo
	 * @param aIdSoggetto
	 * @return Vettore di FascicoloGPModel
	 * @throws F3BException
	 * @deprecated 05.2024 il metodo non viene mai chiamato (MEV_2023-35)
	 */	
	// FIXME verificare se tale metodo viene richiamato. NO era commentato anche nell'interfaccia
	// MEV_2023-35 si parametrizza il lCodContenuto per gestire anche le EPS.
	public Vector ExRicercaDettaglioEsecuzioneSSbyFascicolo(BigDecimal aIdFascicolo, BigDecimal aIdSoggetto,
			String lUfficioUtenteConnesso, String lCodContenuto) throws F3BException {

		Connection lConn = null;
		Vector lSanzioni = new Vector();
		EsecuzioneSanzioneSostitutivaSqlDAO lEseSSSqlDao = null;
		// TenoreModel lTenMod = null;
		TenoreSqlDAO lTenDao = null;
		// SoggettoSqlDAO lSogDao = null;

		try {
			lConn = getDBConnection();

			// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere diverso
			// da quello del padre.
			// Si Popola di tutti i dati il model del soggetto.
			// lSogDao = new SoggettoSqlDAO(lConn);
			// lSogDao.ricercaSoggettoByKey( aIdSoggetto );
			// SoggettoModel aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			// MEV_2023-35 si parametrizza il lCodContenuto per gestire anche le EPS.
			lEseSSSqlDao = new EsecuzioneSanzioneSostitutivaSqlDAO(lConn);
			lEseSSSqlDao.ricercaDettaglioEsecuzioneSSbyFascicolo(aIdFascicolo, lUfficioUtenteConnesso, lCodContenuto);
			lEseSSSqlDao.start();

			FascicoloGPModel lFascicolo = null;
			while (lEseSSSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lEseSSSqlDao.getFascicoloSiusGPModel();

				// STUB 26/04/2007 Il model del soggetto nei figli non va normalizzato, perchè puo essere
				// diverso da quello del padre.
				// Si reimposta il model del soggetto nel Fascicolo Sius.
				// lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// Si Inseriscono i Tenori
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(
						lFascicolo.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

				// 05/11/2003 Carico i dati del tenore nell'array di Tenori in FascicoloGPModel.
				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lSanzioni.add(lFascicolo);
			}
			lEseSSSqlDao.stop();

			if (lSanzioni.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Esecuzione Sanzione Sostitutiva priva di procedimenti");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"EsecuzioneSSController.ExRicercaDettaglioEsecuzioneSSbyFascicolo: " + daoEx);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new F3BException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lEseSSSqlDao);
			cleanup(lTenDao);
			cleanup(lConn);
		}

		return lSanzioni;
	}

}