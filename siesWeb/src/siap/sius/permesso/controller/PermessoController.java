package siap.sius.permesso.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.permesso.dao.PermessoSqlDAO;
import siap.sius.permesso.model.CriteriRicercaProvPermessiLicenzeModel;
import siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel;
import siap.sius.permesso.model.LicenzaModel;
import siap.sius.permesso.model.PermessoModel;
import siap.sius.permesso.model.TotaliPermessiLicenzeModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.dao.TenoreSqlDAO;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;

/**
 * <p>
 * Title: PermessoController
 * </p>
 * <p>
 * Description: Classe Controller per Permesso
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PermessoController extends SiapController implements IPermesso {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca Permessi Paginata per Soggetto, e filtri aggiuntivi.
	 * <p>
	 * 
	 * @param aSogModel
	 * @param strCodUffOTrib
	 * @param lIncludeDistretto
	 * @param lIncludeRigettati
	 * @param lCodPermesso
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */
	public Vector ExRicercaPermessiBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeRigettati, String lCodPermesso, Date dataDalInCanc, Date dataAlInCanc, int aPageNum)
			throws F3BException {

		Connection lConn = null;
		Vector lFascicoli = new Vector();
		// TenoreModel lTenMod = null;

		PermessoSqlDAO lPerSqlDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloGPModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lPerSqlDao = new PermessoSqlDAO(lConn);
			lPerSqlDao.ricercaPermessiBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodPermesso, dataDalInCanc, dataAlInCanc);
			// lPerSqlDao.start();
			lPerSqlDao.startPage(aPageNum);
			while (lPerSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lPerSqlDao.getFascicoloSiusGPdelPermesso();

				// Si popola di tutti i dati il model del soggetto
				lSogDao = new SoggettoSqlDAO(lConn);

				// paolo cherubini per supersoggetto 14/10/2010
				// cambio la select
				lSogDao.ricercaSuperSoggetto("ufficio", lCodUfficioUtenteConnesso, lFascicolo
						.getFascicoloSiusModel().getSoggetto(), "FASCICOLO_SIUS", lCodDistretto);
				// lSogDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
				// fine

				aSogModel = (SoggettoModel) lSogDao.getModelByKey();

				// Si ricarica il model del soggetto nel fascicolo Sius.
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// paolo cherubini per supersoggetto 14/09/2010
				// aggiorno sogidsoggetto del fascicolo
				lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(aSogModel.getIdSoggetto());
				// fine

				// Si Caricano i dati del tenore nell'array di Tenori di FascicoloGPModel.
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lFascicoli.add(lFascicolo);
			}

			lPerSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaPermessiBySoggettoPagina: " + daoEx);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
		 * new
		 * SIUSException(F3BException.USER_MESSAGE,"PermessoController.ExRicercaPermessiBySoggettoPagina: " +
		 * sqe); }
		 */
		catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPerSqlDao);
			cleanup(lTenDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaPermessiBySoggetto
	 * 
	 * @param aSogModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */

	public BigDecimal ExGetNumRicercaPermessiBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeRigettati, String lCodPermesso, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaPermessiBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodPermesso, dataDalInCanc, dataAlInCanc);
			lCont = lPermSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumRicercaPermessiBySoggetto: " + daoEx);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
		 * new
		 * SIUSException(F3BException.USER_MESSAGE,"PermessoController.ExGetNumRicercaPermessiBySoggetto: " +
		 * sqe); }
		 */
		catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Fascicoli relativi a permesso di Un Soggetto in base ai parametri di ricerca selezionati.
	 * <p>
	 * 
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeRigettati
	 * @param lCodPermesso
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaPermessiDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodPermesso,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException {
		Connection lConn = null;

		Vector lPermessi = new Vector();

		SoggettoSqlDAO lSogDao = null;
		PermessoSqlDAO lPermSqlDao = null;
		// boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lPermSqlDao = new PermessoSqlDAO(lConn);

			lPermSqlDao.ricercaPermessiDelSoggetto(aSogModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodPermesso, dataDalInCanc, dataAlInCanc);
			lPermSqlDao.start();

			PermessoModel lPermesso = null;
			// int i = 0;
			while (lPermSqlDao.next()) {
				lPermesso = (PermessoModel) lPermSqlDao.getPermessoModel();

				// Si imposta il model del soggetto nel PermessoModel.
				lPermesso.setSoggetto(aSogModel);

				lPermessi.add(lPermesso);
			}

			lPermSqlDao.stop();

			if (lPermessi.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaPermessiDelSoggetto: " + daoEx);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
		 * new SIUSException(F3BException.USER_MESSAGE,"PermessoController.ExRicercaPermessiDelSoggetto: " +
		 * sqe); }
		 */
		catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lPermessi;
	}

	/**
	 * Ritorna n.ro di record risultato di una ricercaLicenzeBySoggetto
	 * 
	 * @param aSogModel
	 * @return BigDecimal n.ro di record
	 * @throws F3BException
	 */

	public BigDecimal ExGetNumRicercaLicenzeBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeRigettati, String lCodLicenza, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		BigDecimal lCont = new BigDecimal(0);

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaLicenzeBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodLicenza, dataDalInCanc, dataAlInCanc);
			lCont = lPermSqlDao.getNumRowsSelected();
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumRicercaLicenzeBySoggetto: " + daoEx);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
		 * new SIUSException(F3BException.USER_MESSAGE,"PermessoController.ExGetNumRicercaLicenzeBySoggetto: "
		 * + sqe); }
		 */
		catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lCont;
	}

	/**
	 * Ricerca Licenze Paginata per Soggetto, e filtri aggiuntivi.
	 * <p>
	 * 
	 * @param aSogModel
	 * @param strCodUffOTrib
	 * @param lIncludeDistretto
	 * @param lIncludeRigettati
	 * @param lCodLicenza
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @param aPageNum
	 *            : numero pagina > 0
	 * @return Vettore di FascicoloSiusModel
	 * @throws F3BException
	 */

	public Vector ExRicercaLicenzeBySoggettoPagina(SoggettoModel aSogModel, String lCodUfficioUtenteConnesso,
			String lCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc, int aPageNum) throws F3BException {
		Connection lConn = null;
		Vector lFascicoli = new Vector();
		// TenoreModel lTenMod = null;

		PermessoSqlDAO lPerSqlDao = null;
		TenoreSqlDAO lTenDao = null;
		SoggettoSqlDAO lSogDao = null;
		FascicoloGPModel lFascicolo = null;

		try {
			lConn = getDBConnection();

			lPerSqlDao = new PermessoSqlDAO(lConn);
			lPerSqlDao.ricercaLicenzeBySoggetto(aSogModel, lCodUfficioUtenteConnesso, lCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodLicenza, dataDalInCanc, dataAlInCanc);
			// lPerSqlDao.start();
			lPerSqlDao.startPage(aPageNum);
			while (lPerSqlDao.next()) {
				lFascicolo = (FascicoloGPModel) lPerSqlDao.getFascicoloSiusGPdelPermesso();

				// Si popola di tutti i dati il model del soggetto
				lSogDao = new SoggettoSqlDAO(lConn);

				// paolo cherubini per supersoggetto 14/10/2010
				// cambio la select
				lSogDao.ricercaSuperSoggetto("ufficio", lCodUfficioUtenteConnesso, lFascicolo
						.getFascicoloSiusModel().getSoggetto(), "FASCICOLO_SIUS", lCodDistretto);
				// lSogDao.ricercaSoggettoByKey(lFascicolo.getFascicoloSiusModel().getSogIdSoggetto());
				// fine

				aSogModel = (SoggettoModel) lSogDao.getModelByKey();

				// Si ricarica il model del soggetto nel fascicolo Sius.
				lFascicolo.getFascicoloSiusModel().setSoggetto(aSogModel);

				// paolo cherubini per supersoggetto 14/09/2010
				// aggiorno sogidsoggetto del fascicolo
				lFascicolo.getFascicoloSiusModel().setSogIdSoggetto(aSogModel.getIdSoggetto());
				// fine

				// Si Caricano i dati del tenore nell'array di Tenori di FascicoloGPModel.
				lTenDao = new TenoreSqlDAO(lConn);
				lTenDao.ricercaTenoreByGeneraleProc(lFascicolo.getGeneraleProcedimentoModel()
						.getIdGeneraleProcedimento());

				Vector lVectTenori = new Vector(lTenDao.getModels());
				if (lVectTenori != null) {
					TenoreModel[] lTenoriModel = (TenoreModel[]) lVectTenori.toArray(new TenoreModel[0]);
					lFascicolo.setTenori(lTenoriModel);
				}

				lFascicoli.add(lFascicolo);
			}

			lPerSqlDao.stop();

			if (lFascicoli.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE,
						"Nessun Soggetto individuato con i criteri di ricerca selezionati! ");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaLicenzeBySoggettoPagina: " + daoEx);
			// } catch (SQLException sqe) {
			// rollback(lConn);
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.error("SQLException: " + sqe);
			// throw new SIUSException(F3BException.USER_MESSAGE,
			// "PermessoController.ExRicercaLicenzeBySoggettoPagina: " + sqe);
		} catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPerSqlDao);
			cleanup(lTenDao);
			cleanup(lSogDao);
			cleanup(lConn);
		}
		return lFascicoli;
	}

	/**
	 * Ricerca Fascicoli relativi a licenze di un Soggetto in base ai parametri di ricerca selezionati.
	 * <p>
	 * 
	 * @param aSogModel
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodUffOTrib
	 * @param lCodDistretto
	 * @param lIncludeRigettati
	 * @param lCodLicenza
	 * @param dataDalInCanc
	 * @param dataAlInCanc
	 * @return lFascicoli
	 * @throws F3BException
	 */
	public Vector ExRicercaLicenzeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeRigettati, String lCodLicenza,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException {
		Connection lConn = null;

		Vector lLicenze = new Vector();

		SoggettoSqlDAO lSogDao = null;
		PermessoSqlDAO lPermSqlDao = null;
		// boolean lEsiste = false;

		try {
			lConn = getDBConnection();

			// Si Popola di tutti i dati il model del soggetto.
			lSogDao = new SoggettoSqlDAO(lConn);
			lSogDao.ricercaSoggettoByKey(aSogModel.getIdSoggetto());
			aSogModel = (SoggettoModel) lSogDao.getModelByKey();

			lPermSqlDao = new PermessoSqlDAO(lConn);

			lPermSqlDao.ricercaLicenzeDelSoggetto(aSogModel, strCodUfficioUtenteConnesso, strCodUffOTrib,
					lCodDistretto, lIncludeRigettati, lCodLicenza, dataDalInCanc, dataAlInCanc);
			lPermSqlDao.start();

			LicenzaModel lLicenza = null;
			// int i = 0;
			while (lPermSqlDao.next()) {
				lLicenza = (LicenzaModel) lPermSqlDao.getLicenzaModel();

				// Si imposta il model del soggetto nel PermessoModel.
				lLicenza.setSoggetto(aSogModel);

				lLicenze.add(lLicenza);
			}

			lPermSqlDao.stop();

			if (lLicenze.isEmpty())
				throw new SIUSException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException: " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaLicenzeDelSoggetto: " + daoEx);
		}
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * catch (SQLException sqe) { rollback(lConn); siesLogger.error("SQLException: " + sqe); throw
		 * new SIUSException(F3BException.USER_MESSAGE,"PermessoController.ExRicercaLicenzeDelSoggetto: " +
		 * sqe); }
		 */
		catch (Exception e) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception: " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lLicenze;
	}

	public DepositoDecretoMotivazioniLicenzaModel ExRicercaPermessoDepositato(BigDecimal aIDFasSius)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		DepositoDecretoMotivazioniLicenzaModel lDepDecrMotLic = null;

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaPermessoDepositato(aIDFasSius);

			lPermSqlDao.start();

			if (lPermSqlDao.next())
				lDepDecrMotLic = (DepositoDecretoMotivazioniLicenzaModel) lPermSqlDao
						.getDepositoDecretoMotivazioniLicenzaModel();

			lPermSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaPermessoDepositato : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}

		return lDepDecrMotLic;
	}

	public DepositoDecretoMotivazioniLicenzaModel ExRicercaLicenzaDepositata(BigDecimal aIDFasSius)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		DepositoDecretoMotivazioniLicenzaModel lDepDecrMotLic = null;

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaLicenzaDepositata(aIDFasSius);

			lPermSqlDao.start();

			if (lPermSqlDao.next())
				lDepDecrMotLic = (DepositoDecretoMotivazioniLicenzaModel) lPermSqlDao
						.getDepositoDecretoMotivazioniLicenzaModel();

			lPermSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaLicenzaDepositata : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}

		return lDepDecrMotLic;
	}

	/**
	 * Metodo che ritorna elenco dei provvedimenti con Permessi o Licenze, concessi.
	 * <p>
	 * 
	 * @param aCriteriRicerca
	 *            Criteri di Ricerca.
	 * @return Collection insieme delle occorrense.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public DepositoDecretoMotivazioniLicenzaModel ExRicercaPermessoLicenzaDepositati(BigDecimal aIDLicLibAnt)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		DepositoDecretoMotivazioniLicenzaModel lDepDecrMotLic = null;

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaPermessoLicenzaDepositati(aIDLicLibAnt);

			lPermSqlDao.start();

			if (lPermSqlDao.next())
				lDepDecrMotLic = (DepositoDecretoMotivazioniLicenzaModel) lPermSqlDao
						.getDepositoDecretoMotivazioniLicenzaModel();

			lPermSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaPermessoLicenzaDepositati : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lDepDecrMotLic;
	}

	/**
	 * Metodo che individua permesso o licenza depositato e ritorna l'id della Licenza ed il tipo, nel model
	 * di pertinenza. Tale funzione viene impiegata, nella fase di dettaglio del fascicolo sius.
	 * <p>
	 * 
	 * @param BigDecimal
	 *            aIDFascicoloSius.
	 * @return LicenzaLibAnticipataModel.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public LicenzaLibAnticipataModel ExRicercaTipoPermessoLicenzaDepositata(BigDecimal aIDFascicoloSius)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		LicenzaLibAnticipataModel lLicLibAnt = null;

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lLicLibAnt = lPermSqlDao.getTipoPermessoLicenzaDepositata(aIDFascicoloSius);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaTipoPermessoLicenzaDepositata : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE, e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lLicLibAnt;
	}

	/**
	 * Metodo che ritorna elenco dei provvedimenti con Permessi o Licenze, concessi.
	 * <p>
	 * 
	 * @param aCriteriRicerca
	 *            Criteri di Ricerca.
	 * @return Collection insieme delle occorrense.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public Collection ExRicercaProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca) throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		Collection lColl = new ArrayList();

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			lPermSqlDao.ricercaProvvedimentiPermessiLicenze(aCriteriRicerca.getDataDepositoIniziale(),
					aCriteriRicerca.getDataDepositoFinale(), aCriteriRicerca.getCodMotivo(),
					aCriteriRicerca.getCodUfficio());

			lPermSqlDao.start();

			while (lPermSqlDao.next())
				lColl.add(lPermSqlDao.getProvvedimentoPermessoLicenzaModel());

			lPermSqlDao.stop();

		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaProvvedimentiPermessiLicenze : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExRicercaProvvedimentiPermessiLicenze : " + e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}

		return lColl;
	}

	/**
	 * Metodo che ritorna il numero totale dei provvedimenti con Permessi o Licenze, concessi.
	 * <p>
	 * 
	 * @param aCriteriRicerca
	 *            Criteri di Ricerca.
	 * @return TotaliPermessiLicenzeModel model opportunamente popolato con i totali.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public int ExGetNumProvvedimentiPermessiLicenze(CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca)
			throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		int lNum = 0;

		try {
			lConn = getDBConnection();
			lPermSqlDao = new PermessoSqlDAO(lConn);

			lPermSqlDao.ricercaProvvedimentiPermessiLicenze(aCriteriRicerca.getDataDepositoIniziale(),
					aCriteriRicerca.getDataDepositoFinale(), aCriteriRicerca.getCodMotivo(),
					aCriteriRicerca.getCodUfficio());
			lNum = lPermSqlDao.getNumRowsSelected().intValue();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumProvvedimentiPermessiLicenza : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumProvvedimentiPermessiLicenza : " + e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lNum;
	}

	/**
	 * Metodo che recupera i totali afferenti ai provvedimenti per Permessi e Licenze.
	 * <p>
	 * 
	 * @param aCriteriRicerca
	 *            Criteri di Ricerca.
	 * @return TotaliPermessiLicenzeModel model opportunamnte popolato con i totali.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public TotaliPermessiLicenzeModel ExGetTotProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca) throws F3BException {
		Connection lConn = null;
		PermessoSqlDAO lPermSqlDao = null;
		TotaliPermessiLicenzeModel lTotali = null;

		int lNum = 0;

		try {
			lConn = getDBConnection();
			lTotali = new TotaliPermessiLicenzeModel();
			lPermSqlDao = new PermessoSqlDAO(lConn);
			StringTokenizer lStrToken = new StringTokenizer(aCriteriRicerca.getCodMotivo(), ",");

			while (lStrToken.hasMoreTokens()) {
				String lCodMotivo = lStrToken.nextToken();
				lNum = lPermSqlDao.getNumProvvedimentiPermessiLicenze(
						aCriteriRicerca.getDataDepositoIniziale(), aCriteriRicerca.getDataDepositoFinale(),
						lCodMotivo, aCriteriRicerca.getCodUfficio());
				// 20110524 - PM : Aggiunta di :
				// 2680 - Permesso Internati
				// 2450,2451,2452,2460,2461 - Licenza Internati.
				if (lCodMotivo.equals("2020")) // Permesso Premio
					lTotali.setNumPP(lNum);
				else if (lCodMotivo.equals("2021")) // Permesso Necessità
					lTotali.setNumPN(lNum);
				else if (lCodMotivo.equals("2680")) // Permesso Internati
					lTotali.setNumPI(lNum);
				else if (lCodMotivo.equals("2025")) // Licenza
					lTotali.setNumLC(lNum);
				else if (lCodMotivo.indexOf("2450,2451,2452,2460,2461") > -1) // Licenza Internati
					lTotali.setNumLI(lNum);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("DAOException : " + daoEx);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumProvvedimentiPermessiLicenza : " + daoEx);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExGetNumProvvedimentiPermessiLicenza : " + e.getMessage());
		} finally {
			cleanup(lPermSqlDao);
			cleanup(lConn);
		}
		return lTotali;
	}

	/**
	 * Metodo che si occccupa della produzione della stampa elenco provvedimenti con permessi o licenze
	 * concessi.
	 * <p>
	 * 
	 * @param aDataIniziale
	 *            Data deposito iniziale.
	 * @param aDataFinale
	 *            Data deposito finale.
	 * @param aCodMotivo
	 *            Codice motivo.
	 * @param aUtente
	 *            UtenteModel dati dell'utente connesso.
	 * @return ritorna l'output stream.
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	public ByteArrayOutputStream ExStampaProvvedimentiPermessiLicenze(
			CriteriRicercaProvPermessiLicenzeModel aCriteriRicerca, UtenteModel aUtente) throws F3BException {
		ByteArrayOutputStream lReport = null;

		try {
			IStampaSius lCtrl = SIUSLookupRemote.getStampaRemote();
			lReport = lCtrl.ExPreStampaProvvedimentiPermessiLicenza(aCriteriRicerca, aUtente);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.error("Exception : " + e);
			throw new SIUSException(F3BException.USER_MESSAGE,
					"PermessoController.ExStampaProvvedimentiPermessiLicenze : " + e.getMessage());
		}

		return lReport;
	}

}