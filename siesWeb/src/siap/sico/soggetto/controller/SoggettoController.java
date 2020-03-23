package siap.sico.soggetto.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.SICOException;
import siap.sico.evento.dao.EventoSqlDAO;
import siap.sico.evento.model.EventoModel;
import siap.sico.residenza.dao.ResidenzaDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSigeDAO;
import siap.sico.residenza.dao.ResidenzaFascicoloSiusDAO;
import siap.sico.residenza.dao.ResidenzaSqlDAO;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import siap.sico.residenza.model.ResidenzaFascicoloSiusModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.dao.SoggettoDAO;
import siap.sico.soggetto.dao.SoggettoFascicoloSqlDAO;
import siap.sico.soggetto.dao.SoggettoSqlDAO;
import siap.sico.soggetto.model.SoggettoFascicoliModel;
import siap.sico.soggetto.model.SoggettoFascicoliSigeModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.storicosoggetto.dao.StoricoSoggettoDAO;
import siap.sico.storicosoggetto.dao.StoricoSoggettoSqlDAO;
import siap.sico.storicosoggetto.model.StoricoSoggettoModel;
import siap.siep.SIEPException;
import siap.siep.alias.dao.AliasDAO;
import siap.siep.alias.dao.AliasSqlDAO;
import siap.siep.alias.model.AliasModel;
import siap.siep.fascicolo.dao.FascicoloSiepDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSoggettoSqlDAO;
import siap.siep.fascicolo.dao.FascicoloSiepSqlDAO;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.dao.NotificaDAO;
import siap.siep.notifica.dao.NotificaEventoSqlDAO;
import siap.siep.notifica.dao.NotificaSqlDAO;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.statoprocedimento.dao.StatoProcedimentoSqlDAO;
import siap.sige.fascicolo.dao.FascicoloSigeDAO;
import siap.sige.fascicolo.dao.FascicoloSigeSqlDAO;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeSentenzaModel;
import siap.sige.sentenza.dao.FasSigeSentenzaSqlDAO;
import siap.sius.fascicolo.dao.FascicoloSiusDAO;
import siap.sius.fascicolo.dao.FascicoloSiusSqlDAO;
import siap.sius.fascicolo.model.FascicoloSiusModel;

/**
 *
 * <p>
 * Title: SoggettoController
 * </p>
 * <p>
 * Description: Classe controller del Soggetto
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SoggettoController extends SiapController implements ISoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Ricerca di Soggetti
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggetto(SoggettoModel aSoggetto) throws F3BException {

		Connection lConn = null;
		Vector lSoggetti = new Vector();

		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggetto(aSoggetto);

			lSoggetti = new Vector(lSogSqlDao.getModels());

			if (lSoggetti.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggetto: " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return lSoggetti;
	}

	/**
	 * Ricerca di Soggetti Omonimi del Soggetto passato I parametri di ricerca sono NOME, COGNOME, DATA DI
	 * NASCITA, COMUNE DI NASCITA.
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettiOmonimi(SoggettoModel aSoggetto) throws F3BException {

		Connection lConn = null;
		Vector lSoggetti = new Vector();

		SoggettoSqlDAO lSogSqlDao = null;

		// Valorizzazione dei parametri di ricerca degli omonimi
		SoggettoModel lSoggetto = new SoggettoModel();
		lSoggetto.setCognome(aSoggetto.getCognome());
		lSoggetto.setNome(aSoggetto.getNome());
		lSoggetto.setDataNascita(aSoggetto.getDataNascita());
		lSoggetto.setCodComuneNascita(aSoggetto.getCodComuneNascita());
		lSoggetto.setSesso(aSoggetto.getSesso());
		lSoggetto.setCodComuneCasellario(aSoggetto.getCodComuneCasellario());
		lSoggetto.setCodProvinciaNascita(aSoggetto.getCodProvinciaNascita());
		lSoggetto.setCodStatoNascita(aSoggetto.getCodStatoNascita());
		lSoggetto.setCodAfis(aSoggetto.getCodAfis());
		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoOmonimo(lSoggetto);

			lSoggetti = new Vector(lSogSqlDao.getModels());
		} catch (Exception lEx) {
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggettiOmonimi : " + lEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}

		return lSoggetti;
	}

	/**
	 * Ricerca di Soggetti all'interno del distretto
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettoPerDistretto(SoggettoModel aSoggetto, String aCodDistretto, int aPage)
			throws F3BException {

		Connection lConn = null;
		Vector lSoggetti = new Vector();

		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoPerDistretto(aSoggetto, aCodDistretto, aPage);

			// lSoggetti = new Vector(lSogSqlDao.getModels());
			// Modificato per ricerca Alias
			lSogSqlDao.start();
			while (lSogSqlDao.next()) {
				lSoggetti.add(lSogSqlDao.getAliasModel());
			}
			lSogSqlDao.stop();

			if (lSoggetti.isEmpty())
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggettoPerDistretto : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}

		return lSoggetti;
	}

	/**
	 * NUmero di soggetti trovati per distretto per la ricerca paginata
	 *
	 * @param aSoggetto
	 * @param aCodDistretto
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountSoggettiPerDistretto(SoggettoModel aSoggetto, String aCodDistretto)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getCountSoggetti(aSoggetto, aCodDistretto);
			lSogSqlDao.start();
			lSogSqlDao.next();
			lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExGetCountSoggettiPerDistretto : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Count dei Soggetti per Ricerca procedimento per soggetto
	 *
	 * @param aSoggetto
	 * @param strCodUfficioUtenteConnesso
	 * @param strCodDistrettoUtenteConnesso
	 * @param strTipoRicerca
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetCountSoggettiPerProcedimenti(SoggettoModel aSoggetto,
			String strCodUfficioUtenteConnesso, String strCodDistrettoUtenteConnesso, String strTipoRicerca)
			throws F3BException {

		BigDecimal lCount = new BigDecimal(0);
		Connection lConn = null;

		SoggettoFascicoloSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoFascicoloSqlDAO(lConn);
			lSogSqlDao.getCountSoggettiPerProcedimenti(aSoggetto, strCodUfficioUtenteConnesso,
					strCodDistrettoUtenteConnesso, strTipoRicerca);
			lSogSqlDao.start();
			lSogSqlDao.next();
			lCount = lSogSqlDao.getBigDecimal("HowManyRecords");
			lSogSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExGetCountSoggettiPerProcedimenti : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Funzione per rilevare l'eta del soggetto
	 *
	 * @param idSoggetto
	 * @return
	 * @throws F3BException
	 */
	public BigDecimal ExGetEtaSoggetto(BigDecimal idSoggetto) throws F3BException {

		BigDecimal eta = null;
		Connection lConn = null;

		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.getEtaSoggetto(idSoggetto);
			lSogSqlDao.start();
			lSogSqlDao.next();
			eta = lSogSqlDao.getBigDecimal("eta_ora");
			lSogSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExGetEtaSoggetto : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}
		return eta;
	}

	/**
	 * Ricerca di Soggetti
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti
	 * @throws F3BException
	 */
	public SoggettoModel ExRicercaSoggettoByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		SoggettoModel lSoggetto = null;
		SoggettoSqlDAO lSogSqlDao = null;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("SoggettoController.ExRicercaSoggettoByKey: entrata nel metodo ");

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoByKey(aKey);

			lSoggetto = new SoggettoModel();
			lSoggetto = (SoggettoModel) lSogSqlDao.getModelByKey();

			if (lSoggetto == null)
				throw new SICOException(SICOException.USER_MESSAGE, "Nessun Elemento trovato");

			// ---GDV lSoggetto = new SoggettoModel((SoggettoModel)lSogSqlDao.getModelByKey());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(lSoggetto.toString());
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggettoByKey : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}

		return lSoggetto;
	}

	/**
	 * Inserimento di un Soggetto
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return SoggettoModel
	 * @throws F3BException
	 */
	public SoggettoModel ExInserisciSoggetto(SoggettoModel aSoggetto) throws F3BException {

		Connection lConn = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSog = null;
		SoggettoModel lSogCui = null;
		boolean lPresenzaOmonimi = false;
		boolean lPresenzaCui = false;

		try {
			// Il controllo sulla presenza di omonimi e' condizionata
			if (aSoggetto.getMessage() != null
					&& aSoggetto.getMessage().compareToIgnoreCase("omonimi") == 0) {
				if (ExRicercaSoggettiOmonimi(aSoggetto).size() > 0)
					lPresenzaOmonimi = true;
			}
			lConn = getDBConnection();
			lSogDao = new SoggettoDAO(lConn);

			/*
			 * 12/08/2006 -- Eliminato codice CS //Verifico l'esistenza di un soggetto con lo stesso COdice CS
			 * lSog = ExVerifyCodCS(aSoggetto.getCodCs(), lSogDao); if (lSog != null) return lSog;
			 */

			// Verifico l'esistenza di un soggetto con lo stesso Codice CUI
			// (viene registrato nella colonna COD_AFIS della tabella SOGGETTO)
			lSogCui = ExVerifyCodiceCUI$AFIS(aSoggetto.getCodAfis(), lSogDao);
			if (lSogCui != null)
				lPresenzaCui = true;
			// return lSog;

			/*
			 * 12/08/2006 -- Eliminato codice CS //Verifico l'esistenza di un soggetto con lo stesso atto di
			 * Nascita lSog = ExVerifyAttoNascita(aSoggetto.getAttoNascita(), lSogDao); if (lSog != null)
			 * return lSog;
			 */

			// if (lPresenzaOmonimi)
			// return aSoggetto;

			lSogDao.setDAOFromModel(aSoggetto);
			BigDecimal lSequence = lSogDao.insert();
			commit(lConn);

			lSog = new SoggettoModel(aSoggetto);
			if (lPresenzaOmonimi)
				lSog.setMessage("Omonimi Inserimento avvenuto correttamente!");
			else if (lPresenzaCui)
				lSog.setMessage("Soggetto con stesso Codice CUI gia' presente in archivio");
			else
				lSog.setMessage("Inserimento avvenuto correttamente!");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(" CHIAVE SOGGETTO POSTO INSERIMENTO = " + lSequence);
			lSog.setIdSoggetto(lSequence);
		} catch (Exception e) {
			rollback(lConn);
			throw new SICOException("SoggettoController.ExInserisciSoggetto : " + e);
		} finally {
			cleanup(lSogDao);
			cleanup(lConn);
		}

		return lSog;
	}

	/**
	 * Modifica di un Soggetto
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return
	 * @throws F3BException
	 */
	public SoggettoModel ExModificaSoggetto(SoggettoModel aSoggetto) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		SoggettoModel lSog = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("SoggettoController.ExModificaSoggetto: entrata nel metodo ");

		try {
			conn = getDBConnection();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("SoggettoController.ExModificaSoggetto: Connessione dal pool : " + conn);
			lSogDao = new SoggettoDAO(conn);

			/*--12/07/2006 --Il codice CS e' stato eliminato
			//Verifico l'esistenza di un soggetto con ID diverso con lo stesso COdice CS
			      lSog = ExVerifyCodCS(aSoggetto.getCodCs(), lSogDao);
			      if (lSog != null)
			      {
			        if (lSog.getIdSoggetto().compareTo(aSoggetto.getIdSoggetto()) != 0)
			          return lSog;
			      }*/

			// Verifico l'esistenza di un soggetto con ID diverso con lo stesso atto di Nascita
			lSog = ExVerifyAttoNascita(aSoggetto.getAttoNascita(), lSogDao);

			if (lSog != null) {
				if (lSog.getIdSoggetto().compareTo(aSoggetto.getIdSoggetto()) != 0)
					return lSog;
			}

			lSogDao.setDAOFromModelForUpdate(aSoggetto);
			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());
			lSogDao.update();

			commit(conn);

			lSog = new SoggettoModel();
			lSog.setMessage("Aggiornamento avvenuto correttamente!");
			lSog.setIdSoggetto(aSoggetto.getIdSoggetto());
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoController.ExModificaSoggetto: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(conn);
		}

		return lSog;
	}

	/**
	 * Cancellazione di un soggetto
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @throws F3BException
	 */
	public void ExCancellaSoggetto(SoggettoModel aSoggetto) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("SoggettoController.ExCancellaSoggetto: entrata nel metodo ");

		try {
			conn = getDBConnection();

			lSogDao = new SoggettoDAO(conn);
			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());
			lSogDao.delete();

			commit(conn);
		} catch (DAOException ex) {
			if (ex.INTEGRITY_CONSTRAINT_VIOLATED)
				throw new SICOException(SICOException.USER_MESSAGE,
						"Esistono dei fascicoli associati al Soggetto. Impossibile effettuare la Cancellazione!");

			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoController.ExCancellaSoggetto: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(conn);
		}
	}

	/**
	 * Controlla l'univocita' del Codice CS specificato per un soggetto
	 *
	 * @param aCodCS
	 * @param aSogDao
	 * @return
	 */
	public SoggettoModel ExVerifyCodCS(String aCodCS, SoggettoDAO aSogDao) throws DAOException {

		SoggettoModel lRetSog = null;

		// COntrollo il Codice CS
		if (aCodCS != null) {
			if (aCodCS.length() > 1) {
				// lSogDao.setCodCs(lSog.getCodCs());
				aSogDao.setCondizioneByCodiceCS(aCodCS);
				try {
					lRetSog = (SoggettoModel) aSogDao.getModelByKey();
				} catch (DAOException ex) {
					throw ex;
				}

				if (lRetSog != null) {
					lRetSog.setMessage("Soggetto con stesso Codice CS gia' presente in BDI");
					return lRetSog;
				}
			}
		}

		return lRetSog;
	}

	/**
	 * Controlla l'univocita' del Codice CUI (che viene registrato nella collonna COD_AFIS della tabella
	 * SOGGETTO) specificato per un soggetto
	 *
	 * @param aCodCUI$AFIS
	 * @param aSogDao
	 * @return
	 */
	public SoggettoModel ExVerifyCodiceCUI$AFIS(String aCodCUI$AFIS, SoggettoDAO aSogDao)
			throws DAOException {

		SoggettoModel lRetSog = null;

		// Controllo il Codice CUI/AFIS
		if (aCodCUI$AFIS != null && !"".equals(aCodCUI$AFIS)) {
			aSogDao.setCondizioneByCodiceCUI$AFIS(aCodCUI$AFIS);

			try {
				lRetSog = (SoggettoModel) aSogDao.getModelByKey();
			} catch (DAOException ex) {
				throw ex;
			}

			if (lRetSog != null) {
				lRetSog.setMessage("Soggetto con stesso Codice CUI gia' presente in archivio");
				return lRetSog;
			}
		}

		return lRetSog;
	}

	/**
	 * Controlla l'univocita' dell'AttodiNascita specificato per un soggetto
	 *
	 * @param aCodCS
	 * @param aSogDao
	 * @return
	 */
	public SoggettoModel ExVerifyAttoNascita(String aAttoNascita, SoggettoDAO aSogDao) throws DAOException {

		SoggettoModel lRetSog = null;
		// Controllo il Atto di Nascita
		if (aAttoNascita != null) {
			if (aAttoNascita.length() > 1) {
				// lSogDao.setAttoNascita(lSog.getAttoNascita());
				aSogDao.setCondizioneByAttoNascita(aAttoNascita);
				try {
					lRetSog = (SoggettoModel) aSogDao.getModelByKey();
				} catch (DAOException ex) {
					throw ex;
				}

				if (lRetSog != null) {
					lRetSog.setMessage("Soggetto con stesso Atto di Nascita gia' presente in BDI");
					return lRetSog;
				}
			}
		}

		return lRetSog;
	}

	/**
	 * Inserisce il soggetto con l'id gia' impostato provenendo da un trasferimento JMS
	 *
	 * @param aSoggetto
	 * @param lConn
	 * @return
	 * @throws F3BException
	 */
	public String ExInserisciSoggettoWithoutSequence(SoggettoModel aSoggetto, Connection lConn)
			throws F3BException {

		SoggettoDAO lSogDao = null;
		String lCodEsito = "00000";

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Inserisco SOGGETTO = " + aSoggetto);
			lSogDao = new SoggettoDAO(lConn);
			lSogDao.setDAOFromModel(aSoggetto);
			lSogDao.setWithoutSequence(true);
			lSogDao.insert();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Inserito " + aSoggetto);
		} catch (DAOException ex) {
			if (ex.UNIQUE_CONSTRAINT_VIOLATED) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Soggetto gia' presente...");
				lCodEsito = "00001";

				// MEV26 Cumulo - Serve aggiornare il soggetto almeno nel campo KEY_SOGG_NSC
				if (aSoggetto.getKeySoggNsc() != null) {
					try {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Soggetto giÃ  presente provo ad aggiornare KEY_SOGG_NSC ");

						lSogDao.setKeySoggNsc(aSoggetto.getKeySoggNsc());

						lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());

						lSogDao.update();

						//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al
						//// posto di LogF3B.getLogger()
						// siesLogger.debug("KEY_SOGG_NSC aggiornata");
					} catch (DAOException dex) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.debug("Eccezione nell'Update del Soggetto (KEY_SOGG_NSC) ---> " + dex);
					}
				}
			} else
				throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire il Soggetto! ");
		} finally {
			cleanup(lSogDao);
		}

		return lCodEsito;
	}

	/* Modifica di un soggetto con un fascicolo Siep associato che aggiorna lo storico */
	public SoggettoModel ExModificaSoggettoStorico(SoggettoModel aSoggetto, String aProfilo,
			SoggettoModel aSoggettoVecchio, BigDecimal lKeyFascicoloUnivoco) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		StoricoSoggettoDAO lStoSogDao = null;
		StoricoSoggettoSqlDAO lStoSogSqlDao = null;
		FascicoloSiusSqlDAO lFascSiepSql = null;

		SoggettoModel lSog = null;
		FascicoloSiusModel lFasMod = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info(lKeyFascicoloUnivoco + "");

		boolean lFaiCommit = true;

		try {
			conn = getDBTransaction();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("SoggettoController.ExModificaSoggettoStorico: Connessione dal pool : " + conn);

			lSogDao = new SoggettoDAO(conn);
			lStoSogSqlDao = new StoricoSoggettoSqlDAO(conn);
			lFascSiepSql = new FascicoloSiusSqlDAO(conn);

			aSoggetto.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
			aSoggetto.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
			aSoggetto.setDataInserimento(aSoggetto.getDataAggiornamento());
			lSogDao.setDAOFromModelForUpdate(aSoggetto);

			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());
			lSogDao.update();

			// Inserire storico soggetto
			// Creazione storicoSoggettoModello
			StoricoSoggettoModel lStoricoSogg = new StoricoSoggettoModel();

			lStoricoSogg.setIdSoggettoVariato(aSoggetto.getIdSoggetto());
			lStoricoSogg.setDataVariazione(aSoggetto.getDataAggiornamento());
			lStoricoSogg.setNome(aSoggettoVecchio.getNome());
			lStoricoSogg.setCognome(aSoggettoVecchio.getCognome());
			lStoricoSogg.setAnnoNascita(aSoggettoVecchio.getAnnoNascita());
			lStoricoSogg.setMeseNascita(aSoggettoVecchio.getMeseNascita());
			lStoricoSogg.setDataNascita(aSoggettoVecchio.getDataNascita());
			lStoricoSogg.setDataNascitaPresunta(aSoggettoVecchio.getDataNascitaPresunta());

			lStoricoSogg.setCodComuneNascita(aSoggettoVecchio.getCodComuneNascita());
			lStoricoSogg.setCodProvinciaNascita(aSoggettoVecchio.getCodProvinciaNascita());
			lStoricoSogg.setCodComuneCasellario(aSoggetto.getCodComuneCasellario());
			lStoricoSogg.setCodFiscale(aSoggettoVecchio.getCodFiscale());
			lStoricoSogg.setCodCs(aSoggettoVecchio.getCodCs());
			lStoricoSogg.setCodAfis(aSoggettoVecchio.getCodAfis());

			lStoricoSogg.setEtaPresuntaAnni(aSoggettoVecchio.getEtaPresuntaAnni());
			lStoricoSogg.setEtaPresuntaMesi(aSoggettoVecchio.getEtaPresuntaMesi());

			lStoricoSogg.setCodStatoNascita(aSoggettoVecchio.getCodStatoNascita());
			lStoricoSogg.setDescComuneNascitaEstero(aSoggettoVecchio.getDescComuneNascitaEstero());
			lStoricoSogg.setNazionalita(aSoggettoVecchio.getNazionalita());
			lStoricoSogg.setPaternita(aSoggettoVecchio.getPaternita());
			lStoricoSogg.setCognomeMadre(aSoggettoVecchio.getCognomeMadre());
			lStoricoSogg.setNomeMadre(aSoggettoVecchio.getNomeMadre());
			lStoricoSogg.setSesso(aSoggettoVecchio.getSesso());
			lStoricoSogg.setAttoNascita(aSoggettoVecchio.getAttoNascita());
			lStoricoSogg.setNote(aSoggettoVecchio.getNote());
			lStoricoSogg.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
			lStoricoSogg.setDataInserimento(aSoggetto.getDataAggiornamento());
			lStoricoSogg.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
			lStoricoSogg.setMeseNascita(aSoggettoVecchio.getMeseNascita());
			// lStoricoSogg.setIdSoggettoNuovo(lKeySoggDuplicato);
			lStoSogSqlDao.nextProgressivo(aSoggetto.getIdSoggetto());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("STORICO---->" + lStoricoSogg);

			int lMaxProg = 0;

			lStoSogSqlDao.start();
			if (lStoSogSqlDao.next()) {
				lMaxProg = lStoSogSqlDao.getInt("max_progressivo");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("MAX = " + lMaxProg);
			}
			BigDecimal lMax = new BigDecimal(lMaxProg + 1);

			if (lMaxProg > 0) {
				lStoricoSogg.setProgressivoStorico(lMax);
			} else {
				// Dovuto al fatto che per un nuovo Id mi ritorna un progressivo uguale a zero
				lStoricoSogg.setProgressivoStorico(new BigDecimal(1));
			}
			if (aProfilo.equals("30") || aProfilo.equals("4") || aProfilo.equals("40")
					|| aProfilo.equals("50")) // SIEP e setto FascioloSiep
			{
				lStoricoSogg.setFasSieIdFascicoloSiep(lKeyFascicoloUnivoco);
			} else {
				lStoricoSogg.setFasSieIdFascicoloSius(lKeyFascicoloUnivoco);
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("Settato setProgressivoStorico = " + lMax);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(lStoricoSogg);

			// Popolazione campi storicoSoggetto con campi aSoggetto
			// Istanzia Dao e popola col nuovo model
			lStoSogDao = new StoricoSoggettoDAO(conn);
			lStoSogDao.setDAOFromModel(lStoricoSogg);
			lStoSogDao.insert();
			// Insert
			lSog = new SoggettoModel();
			lSog.setMessage("Aggiornamento avvenuto correttamente!");

			if (aProfilo.equals("31") || aProfilo.equals("32") || aProfilo.equals("41")
					|| aProfilo.equals("42") || aProfilo.equals("14") || aProfilo.equals("24")
					|| aProfilo.equals("51") || aProfilo.equals("52")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("profilo sius---->");
				lFascSiepSql.ricercaFascicoloByKey(lKeyFascicoloUnivoco);
				lFasMod = (FascicoloSiusModel) lFascSiepSql.getModelByKey();

				String modifica = " E' Stato Modificato il Soggetto  ";
				String modificaSeguito = " a cui e' legato un Procedimento SIEP. Avvertire la Procura";
				String modificaNumFasc = "";
				String modificaDaPassare = "";

				// FascicoloSiepModel lFascSiepMod = new FascicoloSiepModel();
				if (lFasMod != null && lFasMod.getFasSieIdFascicoloSiep() != null) {

					modificaNumFasc += aSoggettoVecchio.getCognome() + "  " + aSoggettoVecchio.getNome()
							+ " nato a " + aSoggettoVecchio.getDescrComuneNascita() + " il "
							+ DateUtils.getDateToString(aSoggettoVecchio.getDataNascita(), "dd-MM-yyyy");
					modificaDaPassare = modifica + modificaNumFasc + modificaSeguito;
					lSog.setMessage(modificaDaPassare);

					/*
					 * consentire sempre modifica paolo c. 19/03/2009 // Correzione 23-11-2007. Non si
					 * permette la modifica se collegato ad un Fascicolo SIEP lSog.setMessage(
					 * "Non e' consentito modificare il Soggetto perche' collegato ad un Procedimento SIEP !"
					 * ); lFaiCommit = false;
					 */
				}
			}

			if (lFaiCommit)
				commit(conn);
			else
				rollback(conn);

			lSog.setIdSoggetto(aSoggetto.getIdSoggetto());
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoController.ExModificaSoggettoStorico: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lStoSogDao);
			cleanup(lStoSogSqlDao);
			cleanup(lFascSiepSql);

			cleanup(conn);
		}
		return lSog;
	}

	/**
	 * Modifica un soggetto e aggiorna lo storico con tutti i fascicoli
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return
	 * @throws F3BException
	 */
	public SoggettoModel ExModificaSoggettoStorici(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			BigDecimal IdSoggettoVecchio) throws F3BException {

		Connection conn = null;

		SoggettoDAO lSogDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		StoricoSoggettoDAO lStoSogDao = null;
		StoricoSoggettoSqlDAO lStoSogSqlDao = null;
		FascicoloSiusSqlDAO lFascSiusSql = null;
		FascicoloSiusDAO lFascSiusDAO = null;
		FascicoloSiepSqlDAO lFasSql = null;

		FascicoloSiepModel lFasMod = null;
		SoggettoModel lSogModificato = new SoggettoModel(aSoggetto);

		try {
			conn = getDBTransaction();

			// lSoggetto = new SoggettoModel(aSoggetto);
			// INSERISCO STORICO SOGGETTO

			if (lKeyFascicoli.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("PASSO QUI 001");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("(lKeyFascicoli.size() > 0) = " + lKeyFascicoli.size());
				lSogSqlDao = new SoggettoSqlDAO(conn);
				lSogSqlDao.ricercaSoggettoByKey(IdSoggettoVecchio);
				SoggettoModel lSoggVecchio = (SoggettoModel) lSogSqlDao.getModelByKey();
				StoricoSoggettoModel lStoricoSogg = new StoricoSoggettoModel();

				lStoricoSogg.setIdSoggettoVariato(IdSoggettoVecchio);
				lStoricoSogg.setDataVariazione(aSoggetto.getDataAggiornamento());
				lStoricoSogg.setNome(lSoggVecchio.getNome());
				lStoricoSogg.setCognome(lSoggVecchio.getCognome());
				lStoricoSogg.setAnnoNascita(lSoggVecchio.getAnnoNascita());
				lStoricoSogg.setMeseNascita(lSoggVecchio.getMeseNascita());
				lStoricoSogg.setDataNascita(lSoggVecchio.getDataNascita());
				lStoricoSogg.setDataNascitaPresunta(lSoggVecchio.getDataNascitaPresunta());

				// MODIFICO SOGGETTO
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("soggetto  ---->" + aSoggetto);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("soggetto IdSoggettoVecchio ---->" + IdSoggettoVecchio);
				aSoggetto.setIdSoggetto(IdSoggettoVecchio);
				lSogDao = new SoggettoDAO(conn);
				lSogDao.setDAOFromModelForUpdate(aSoggetto);
				lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());
				lSogDao.update();

				lSogModificato.setIdSoggetto(aSoggetto.getIdSoggetto());
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("soggetto modificato ---->" + aSoggetto);
				lStoricoSogg.setCodComuneNascita(lSoggVecchio.getCodComuneNascita());
				lStoricoSogg.setCodProvinciaNascita(lSoggVecchio.getCodProvinciaNascita());
				lStoricoSogg.setCodComuneCasellario(lSoggVecchio.getCodComuneCasellario());
				lStoricoSogg.setCodFiscale(lSoggVecchio.getCodFiscale());
				lStoricoSogg.setCodCs(lSoggVecchio.getCodCs());
				lStoricoSogg.setCodAfis(lSoggVecchio.getCodAfis());

				lStoricoSogg.setCodStatoNascita(lSoggVecchio.getCodStatoNascita());
				lStoricoSogg.setDescComuneNascitaEstero(lSoggVecchio.getDescComuneNascitaEstero());
				lStoricoSogg.setNazionalita(lSoggVecchio.getNazionalita());
				lStoricoSogg.setPaternita(lSoggVecchio.getPaternita());
				lStoricoSogg.setCognomeMadre(lSoggVecchio.getCognomeMadre());
				lStoricoSogg.setNomeMadre(lSoggVecchio.getNomeMadre());
				lStoricoSogg.setSesso(lSoggVecchio.getSesso());
				lStoricoSogg.setAttoNascita(lSoggVecchio.getAttoNascita());
				lStoricoSogg.setNote(lSoggVecchio.getNote());
				lStoricoSogg.setCodOperatoreInserimento(lSoggVecchio.getCodOperatoreAggiornamento());
				lStoricoSogg.setDataInserimento(lSoggVecchio.getDataAggiornamento());
				lStoricoSogg.setCodUfficioInserimento(lSoggVecchio.getCodUfficioAggiornamento());
				// lStoricoSogg.setIdSoggettoNuovo(aSoggetto.getIdSoggetto());
				lStoricoSogg.setEtaPresuntaAnni(lSoggVecchio.getEtaPresuntaAnni());
				lStoricoSogg.setEtaPresuntaMesi(lSoggVecchio.getEtaPresuntaMesi());

				// se i fascicoli selezionati sono + di uno
				String modifica = " E' Stato Modificato il Procedimento ";
				String modificaSeguito = " a cui e' legato un Procedimento SIUS. Avvertire il Tribunale di Sorveglianza";
				String modificaNumFasc = "";
				String modificaDaPassare = "";

				// INSERISCO STORICO SOGGETTO
				// lStoricoSogg.setFasSieIdFascicoloSiep(lIdFasc);

				lStoSogSqlDao = new StoricoSoggettoSqlDAO(conn);
				lStoSogSqlDao.nextProgressivo(IdSoggettoVecchio);
				int lMaxProg = 0;

				lStoSogSqlDao.start();
				if (lStoSogSqlDao.next()) {
					lMaxProg = lStoSogSqlDao.getInt("max_progressivo");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MAX = " + lMaxProg);
				}
				BigDecimal lMax = new BigDecimal(lMaxProg + 1);

				if (lMaxProg > 0) {
					lStoricoSogg.setProgressivoStorico(lMax);

				} else {
					// Dovuto al fatto che per un nuovo Id mi ritorna un progressivo
					// uguale a zero
					lStoricoSogg.setProgressivoStorico(new BigDecimal(1));
				}

				lStoSogDao = new StoricoSoggettoDAO(conn);
				lStoSogDao.setDAOFromModel(lStoricoSogg);
				BigDecimal lStoricoNuovo = lStoSogDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("STORICO NUOVO---->" + lStoricoNuovo);

				// ricerca Fascicolo SIEP
				lFasSql = new FascicoloSiepSqlDAO(conn);
				BigDecimal lIdFasc = (BigDecimal) lKeyFascicoli.get(0);
				lFasSql.ricercaFascicoloByKey(lIdFasc);
				lFasMod = (FascicoloSiepModel) lFasSql.getModelByKey();

				// ricerca Fascicolo Sius legato al Fascicolo SIEP
				FascicoloSiusModel lFascSiusMod = new FascicoloSiusModel();
				lFascSiusMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());

				lFascSiusSql = new FascicoloSiusSqlDAO(conn);
				lFascSiusSql.ricercaFascicoloSius(lFascSiusMod);
				Vector FascicoliSius = new Vector(lFascSiusSql.getModels());
				if (FascicoliSius != null && FascicoliSius.size() > 0) {
					for (int y = 0; y < FascicoliSius.size(); y++) {
						FascicoloSiusModel lFasSiusMod = (FascicoloSiusModel) FascicoliSius.get(y);
						lFasSiusMod.setSogIdSoggetto(aSoggetto.getIdSoggetto());
						lFascSiusDAO = new FascicoloSiusDAO(conn);
						lFascSiusDAO.setDAOFromModelForUpdate(lFasSiusMod);
						lFascSiusDAO.update();
						lFascSiusDAO.stop();
					}
					modificaNumFasc += lFasMod.getChiaveAnno() + " / " + lFasMod.getChiaveProgr() + " - ";
					modificaDaPassare = modifica + modificaNumFasc + modificaSeguito;
					lSogModificato.setMessage(modificaDaPassare);
				}
			}
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStorici", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStorici : " + ex);
		} catch (Exception ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStorici", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStorici: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lSogSqlDao);
			cleanup(lStoSogDao);
			cleanup(lStoSogSqlDao);
			cleanup(lFascSiusSql);
			cleanup(lFascSiusDAO);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lFasSql);

			cleanup(conn);
		}

		return lSogModificato;
	}

	/**
	 * Modifica un soggetto Sius e aggiorna lo storico con tutti i fascicoli sius
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return
	 * @throws F3BException
	 */
	public SoggettoModel ExModificaSoggettoStoriciSius(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			int lFascicoli, int lFascicoliAltriUff, BigDecimal IdSoggettoVecchio) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		StoricoSoggettoDAO lStoSogDao = null;
		StoricoSoggettoSqlDAO lStoSogSqlDao = null;
		FascicoloSiusSqlDAO lFasSql = null;
		FascicoloSiusDAO lFasDAO = null;
		ResidenzaSqlDAO lReSql = null;
		ResidenzaDAO lReDAO = null;
		AliasDAO lAliasDAO = null;
		AliasSqlDAO lAliasSqlDAO = null;
		NotificaSqlDAO lNotSQL = null;
		NotificaDAO lNotDAO = null;
		FascicoloSiusSqlDAO lFascSiusSql = null;
		FascicoloSiepSqlDAO lFascSiepSql = null;
		FascicoloSiepDAO lFascSiepDAO = null;
		ResidenzaFascicoloSiusDAO lReFascDAO = null;
		EventoSqlDAO lEveSql = null;
		NotificaEventoSqlDAO lNotEveDao = null;

		FascicoloSiusModel lFasMod = null;
		SoggettoModel lSogDuplicato = null;

		try {
			conn = getDBTransaction();

			lSogDao = new SoggettoDAO(conn);
			lSogSqlDao = new SoggettoSqlDAO(conn);
			lFasSql = new FascicoloSiusSqlDAO(conn);
			lFasDAO = new FascicoloSiusDAO(conn);
			lReDAO = new ResidenzaDAO(conn);
			lAliasDAO = new AliasDAO(conn);
			lAliasSqlDAO = new AliasSqlDAO(conn);
			lNotSQL = new NotificaSqlDAO(conn);
			lNotDAO = new NotificaDAO(conn);
			lFascSiusSql = new FascicoloSiusSqlDAO(conn);
			lReFascDAO = new ResidenzaFascicoloSiusDAO(conn);
			lFascSiepDAO = new FascicoloSiepDAO(conn);
			lEveSql = new EventoSqlDAO(conn);
			lNotEveDao = new NotificaEventoSqlDAO(conn);
			lStoSogSqlDao = new StoricoSoggettoSqlDAO(conn);
			lSogDuplicato = new SoggettoModel();

			// lSoggetto = new SoggettoModel(aSoggetto);

			BigDecimal lKeySoggDuplicato = null;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("lKeyFascicoli selezionati.size----->" + lKeyFascicoli.size());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("lFascicoli presenti----->" + lFascicoli);

			lSogSqlDao.ricercaSoggettoByKey(IdSoggettoVecchio);
			SoggettoModel lSoggVecchio = (SoggettoModel) lSogSqlDao.getModelByKey();

			/*
			 * paolo 26/03/2010 // Correzione del 22-11-2007. Non si permette la modifica se collegato ad un
			 * Fascicolo SIEP if (esisteFascicoloSiepCollegato( lKeyFascicoli, lFascSiusSql)) {
			 * lSoggVecchio.setMessage
			 * ("Non e' consentito modificare il Soggetto perche' collegato ad un Procedimento SIEP !");
			 * return lSoggVecchio; }
			 */

			// setto lo storico pe l'inserimento
			lStoSogDao = new StoricoSoggettoDAO(conn);

			StoricoSoggettoModel lStoricoSogg = new StoricoSoggettoModel();
			lStoricoSogg.setIdSoggettoVariato(IdSoggettoVecchio);
			lStoricoSogg.setDataVariazione(aSoggetto.getDataAggiornamento());
			lStoricoSogg.setNome(lSoggVecchio.getNome());
			lStoricoSogg.setCognome(lSoggVecchio.getCognome());
			lStoricoSogg.setAnnoNascita(lSoggVecchio.getAnnoNascita());
			lStoricoSogg.setMeseNascita(lSoggVecchio.getMeseNascita());
			lStoricoSogg.setDataNascita(lSoggVecchio.getDataNascita());
			lStoricoSogg.setDataNascitaPresunta(lSoggVecchio.getDataNascitaPresunta());
			lStoricoSogg.setCodComuneNascita(lSoggVecchio.getCodComuneNascita());
			lStoricoSogg.setCodProvinciaNascita(lSoggVecchio.getCodProvinciaNascita());
			lStoricoSogg.setCodComuneCasellario(lSoggVecchio.getCodComuneCasellario());
			lStoricoSogg.setCodFiscale(lSoggVecchio.getCodFiscale());
			lStoricoSogg.setCodCs(lSoggVecchio.getCodCs());
			lStoricoSogg.setCodAfis(lSoggVecchio.getCodAfis());
			lStoricoSogg.setCodStatoNascita(lSoggVecchio.getCodStatoNascita());
			lStoricoSogg.setDescComuneNascitaEstero(lSoggVecchio.getDescComuneNascitaEstero());
			lStoricoSogg.setNazionalita(lSoggVecchio.getNazionalita());
			lStoricoSogg.setPaternita(lSoggVecchio.getPaternita());
			lStoricoSogg.setCognomeMadre(lSoggVecchio.getCognomeMadre());
			lStoricoSogg.setNomeMadre(lSoggVecchio.getNomeMadre());
			lStoricoSogg.setSesso(lSoggVecchio.getSesso());
			lStoricoSogg.setAttoNascita(lSoggVecchio.getAttoNascita());
			lStoricoSogg.setNote(lSoggVecchio.getNote());
			lStoricoSogg.setCodOperatoreInserimento(lSoggVecchio.getCodOperatoreAggiornamento());
			lStoricoSogg.setDataInserimento(lSoggVecchio.getDataAggiornamento());
			lStoricoSogg.setCodUfficioInserimento(lSoggVecchio.getCodUfficioAggiornamento());

			// SE VENGONO SELEZIONATI TUTTI I FASCICOLI

			if (lFascicoli == lKeyFascicoli.size() && lFascicoliAltriUff == 0) {

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("SELEZIONATI TUTTI I FASCICOLI----------->");

				lSogSqlDao.ricercaSoggettoByKey(IdSoggettoVecchio);
				SoggettoModel aSoggettoVecchio = (SoggettoModel) lSogSqlDao.getModelByKey();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("SOGGETTO VECCHIO---------->" + aSoggettoVecchio);

				// Se il codice Cs e' null oppure stringa vuota valgono le modifiche a questo campo
				if ((aSoggettoVecchio.getCodCs() != null) && ((!aSoggettoVecchio.getCodCs().equals("")))) {
					// Se non vuoto verifico che non sia stato cambiato il codiceCs
					if (!(aSoggettoVecchio.getCodCs().equals(aSoggetto.getCodCs()))) {
						aSoggettoVecchio.setMessage("Impossibile modificare il CodiceCs del Soggetto");
						return aSoggettoVecchio;
					}
				}
				// Se l'atto di nascita e' null oppure stringa vuota valgono le modifiche
				if ((aSoggettoVecchio.getAttoNascita() != null)
						&& (!(aSoggettoVecchio.getAttoNascita().equals("")))) {
					// Se non vuoto verifico che non sia stato cambiato l'atto di Nascita
					if (!(aSoggettoVecchio.getAttoNascita().equals(aSoggetto.getAttoNascita()))) {
						aSoggettoVecchio.setMessage("Impossibile modificare l'atto di nascita del Soggetto");
						return aSoggettoVecchio;
					}
				}

				aSoggetto.setCodOperatoreAggiornamento(aSoggetto.getCodOperatoreAggiornamento());
				aSoggetto.setCodUfficioAggiornamento(aSoggetto.getCodUfficioAggiornamento());
				aSoggetto.setDataAggiornamento(aSoggetto.getDataAggiornamento());
				aSoggetto.setIdSoggetto(aSoggettoVecchio.getIdSoggetto());
				lSogDao.setDAOFromModelForUpdate(aSoggetto);

				lSogDao.selCondizioneUpdate(aSoggettoVecchio.getIdSoggetto());
				lSogDao.update();

				// INSERISCO STORICO SOGGETTO
				// lStoricoSogg.setFasSieIdFascicoloSiep(lIdFasc);
				lStoSogSqlDao.nextProgressivo(aSoggettoVecchio.getIdSoggetto());
				int lMaxProg = 0;

				lStoSogSqlDao.start();
				if (lStoSogSqlDao.next()) {
					lMaxProg = lStoSogSqlDao.getInt("max_progressivo");
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info("MAX = " + lMaxProg);
				}
				BigDecimal lMax = new BigDecimal(lMaxProg + 1);

				if (lMaxProg > 0) {
					lStoricoSogg.setProgressivoStorico(lMax);

				} else {
					// Dovuto al fatto che per un nuovo Id mi ritorna un progressivo uguale a zero
					lStoricoSogg.setProgressivoStorico(new BigDecimal(1));
				}

				lStoSogDao.setDAOFromModel(lStoricoSogg);
				BigDecimal lStoricoNuovo = lStoSogDao.insert();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("STORICO NUOVO---->" + lStoricoNuovo);

				// SETTO IL SOGGETTO VECCHIO IN QUELLO DUPLICATO PER FAR ANDARE BENE LA SELECT DI DETTAGLIO
				// VISTO CHE NON HO DUPLICATO PERCHe' SONO STATI SELEZIONATI TUTTI I FASCICOLI ASSOCIATI AL
				// SOGG
				lSogDuplicato = aSoggetto;

				// controllo se il sogetto e' associato a un fasciolo siep e amndo messaggio
				for (int i = 0; i < lKeyFascicoli.size(); i++) {
					BigDecimal chiaveFasc = (BigDecimal) lKeyFascicoli.get(i);
					lFascSiusSql.ricercaFascicoloByKey(chiaveFasc);
					lFasMod = (FascicoloSiusModel) lFascSiusSql.getModelByKey();

					String modifica = " E' Stato Modificato il Soggetto  ";
					String modificaSeguito = " a cui e' legato un Procedimento SIEP. Avvertire la Procura";
					String modificaNumFasc = "";
					String modificaDaPassare = "";

					// FascicoloSiepModel lFascSiepMod = new FascicoloSiepModel();
					if (lFasMod != null && lFasMod.getFasSieIdFascicoloSiep() != null) {

						modificaNumFasc += aSoggettoVecchio.getCognome() + " " + aSoggettoVecchio.getNome()
								+ " nato a " + aSoggettoVecchio.getDescrComuneNascita() + " il "
								+ DateUtils.getDateToString(aSoggettoVecchio.getDataNascita(), "dd-MM-yyyy");
						;
						modificaDaPassare = modifica + modificaNumFasc + modificaSeguito;
						aSoggetto.setMessage(modificaDaPassare);
						lSogDuplicato = aSoggetto;

					}
				}
			} else {

				if (lKeyFascicoli.size() > 0) {
					// DUPLICO SOGGETTO
					lSogDao.setDAOFromModel(aSoggetto);
					lKeySoggDuplicato = lSogDao.insert();
					lSogDuplicato.setIdSoggetto(lKeySoggDuplicato);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("soggetto INSERITO----->" + lKeySoggDuplicato);
				}

				// DUPLICO ALIAS
				lAliasSqlDAO.ricercaAliasByIdSoggetto(IdSoggettoVecchio);
				Vector lTutteAlias = new Vector(lAliasSqlDAO.getModels());
				if (lTutteAlias != null) {
					Iterator lItx = lTutteAlias.iterator();

					while (lItx.hasNext()) {
						AliasModel lAliasMod = (AliasModel) lItx.next();

						if (lAliasMod != null) {
							lAliasMod.setSogIdSoggetto(lKeySoggDuplicato);
							lAliasMod.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
							lAliasMod.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
							lAliasMod.setDataInserimento(DateUtils.getSysDate());
							lAliasDAO.setDAOFromModel(lAliasMod);
							lAliasDAO.insert();
							lAliasDAO.stop();
						}
					}
				}

				// RICERCA RESIDENZE

				lReSql = new ResidenzaSqlDAO(conn);
				// ResidenzaFascicoloSiusModel lResFasModRic = null;
				ResidenzaFascicoloSiusModel lResFasMod = null;
				ResidenzaModel lResMod = new ResidenzaModel();
				ResidenzaAssociataModel lResAssMod = null;
				// ResidenzaModel lResModNuove = new ResidenzaModel();

				// Vector lResidenzeFasc = new Vector();
				// Vector lResidenze = new Vector();

				BigDecimal lKeyRes = null;
				char tipoResidenza = 'R';
				for (int v = 0; v < lKeyFascicoli.size(); v++) {
					BigDecimal lIdfFasc = (BigDecimal) lKeyFascicoli.get(v);
					lReSql.ricercaResidenzaByProcedimentoSius(lIdfFasc, tipoResidenza);

					lReSql.start();

					while (lReSql.next()) {
						lResAssMod = new ResidenzaAssociataModel();

						lResMod = (ResidenzaModel) lReSql.getModel();
						lResFasMod = lReSql.getModelResidenzaFascicoloSius();

						lResAssMod.setResidenza(lResMod);
						lResAssMod.setResidenzaFascicoloSius(lResFasMod);

					}

					if (lResMod != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info(" residenza--->" + lResMod);

						// DUPLICO RESIDENZE LEGATE AL VECCHIO SOGGETTO E LE LEGO AL NUOVO
						lResMod.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
						lResMod.setDataInserimento(aSoggetto.getDataAggiornamento());
						lResMod.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
						lResMod.setSogIdSoggetto(lKeySoggDuplicato);
						lReDAO.setDAOFromModel(lResMod);
						lKeyRes = lReDAO.insert();
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("inserisco residenza--->" + lKeyRes);

						lReDAO.stop();
					}
				}

				for (int v = 0; v < lKeyFascicoli.size(); v++) {
					// BigDecimal lIdFascDaMod = (BigDecimal) lKeyFascicoli.get(v);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info(" residenza FASCICOLO--->" + lResFasMod);

					if (lResFasMod != null) {
						lResFasMod.setResIdResidenza(lKeyRes);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("AGGIORNO RESIDENZA fasc--->" + lResFasMod);
						lReFascDAO.setDAOFromModelForUpdateIdResidenza(lResFasMod);
						lReFascDAO.update();
						lReFascDAO.stop();
					}
				}

				// se i fascicoli selezionati sono + di uno
				String modifica = " E' Stato Modificato il Procedimento ";
				String modificaSeguito = " a cui e' legato un Procedimento SIEP. Avvertire la Procura";
				String modificaNumFasc = "";
				String modificaDaPassare = "";
				if (lKeyFascicoli.size() > 0) {
					// INSERISCO STORICO SOGGETTO
					// lStoricoSogg.setFasSieIdFascicoloSiep(lIdFasc);
					lStoricoSogg.setIdSoggettoNuovo(lKeySoggDuplicato);

					lStoSogSqlDao.nextProgressivo(IdSoggettoVecchio);
					int lMaxProg = 0;

					lStoSogSqlDao.start();
					if (lStoSogSqlDao.next()) {
						lMaxProg = lStoSogSqlDao.getInt("max_progressivo");
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("MAX = " + lMaxProg);
					}
					BigDecimal lMax = new BigDecimal(lMaxProg + 1);

					if (lMaxProg > 0) {
						lStoricoSogg.setProgressivoStorico(lMax);

					} else {
						// Dovuto al fatto che per un nuovo Id mi ritorna un progressivo uguale a zero
						lStoricoSogg.setProgressivoStorico(new BigDecimal(1));
					}

					lStoSogDao.setDAOFromModel(lStoricoSogg);
					BigDecimal lStoricoNuovo = lStoSogDao.insert();
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("STORICO NUOVO---->" + lStoricoNuovo);

					for (int h = 0; h < lKeyFascicoli.size(); h++) {

						BigDecimal lIdFasc = (BigDecimal) lKeyFascicoli.get(h);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("lIdFasc--->" + lIdFasc);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info("Settato setProgressivoStorico = " + lMax);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(lStoricoSogg);

						// UPDATE SUL FASCICOLO CON NUOVO IL SOGGETTO

						lFasSql.ricercaFascicoloByKey(lIdFasc);
						lFasMod = (FascicoloSiusModel) lFasSql.getModelByKey();
						lFasMod.setSogIdSoggetto(lKeySoggDuplicato);
						lFasDAO.setSogIdSoggetto(lKeySoggDuplicato);
						lFasDAO.setDAOFromModelForUpdate(lFasMod);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("FASCICOLO sius DA MODIFICARE---->" + lFasMod);
						lFasDAO.update();
						lFasDAO.stop();

						// ricerca Fascicolo Sius legato al Fascicolo SIEP

						FascicoloSiepModel lFascSiepMod = new FascicoloSiepModel();
						lFascSiepSql = new FascicoloSiepSqlDAO(conn);
						if (lFasMod != null && lFasMod.getFasSieIdFascicoloSiep() != null) {
							lFascSiepSql.ricercaFascicoloByKey(lFasMod.getFasSieIdFascicoloSiep());
							lFascSiepMod = (FascicoloSiepModel) lFascSiepSql.getModelByKey();
							lFascSiepDAO.setDAOFromModelForUpdate(lFascSiepMod);
							lFascSiepDAO.setSogIdSoggetto(lKeySoggDuplicato);
							lFascSiepDAO.selCondizioneUpdate(lFasMod.getFasSieIdFascicoloSiep());
							lFascSiepDAO.update();
							lFascSiepDAO.stop();

							modificaNumFasc += lFascSiepMod.getChiaveAnno() + " / "
									+ lFascSiepMod.getChiaveProgr() + " - ";
							modificaDaPassare = modifica + modificaNumFasc + modificaSeguito;
							lSogDuplicato.setMessage(modificaDaPassare);
						}

						// ricerca degli eventi del fascicolo per ricercare le notifiche legate al fascicolo e
						// legate al soggetto
						lEveSql.ricercaEventoByFascicoloSius(lFasMod.getIdFascicoloSius(), null);
						Vector Eventi = new Vector(lEveSql.getModels());

						for (int z = 0; z < Eventi.size(); z++) {
							EventoModel lEveMod = (EventoModel) Eventi.get(z);
							lNotSQL.ricercaNotificaByEvento(lEveMod.getIdEvento());
							Vector lNotifiche = new Vector(lNotSQL.getModels());
							for (int x = 0; x < lNotifiche.size(); x++) {
								NotificaModel lNorMod = (NotificaModel) lNotifiche.get(x);
								if (lNorMod.getSogIdSoggetto() != null) {
									lNorMod.setSogIdSoggetto(lKeySoggDuplicato);
									lNotDAO.setDAOFromModelForUpdate(lNorMod);
									// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
									// siesLogger al posto di mLog
									siesLogger.info("notifica Modificata--->" + lNorMod);
									lNotDAO.update();
									lNotDAO.stop();

								}
							}
						}
					}
				}
			}
			// Insert
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStoriciSius", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStoriciSius : " + ex);
		} catch (Exception ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStoriciSius", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStoriciSius: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lSogSqlDao);
			cleanup(lStoSogDao);
			cleanup(lStoSogSqlDao);
			cleanup(lFasSql);
			cleanup(lFasDAO);
			cleanup(lReSql);
			cleanup(lReDAO);
			cleanup(lAliasDAO);
			cleanup(lAliasSqlDAO);
			cleanup(lNotSQL);
			cleanup(lNotDAO);
			cleanup(lFascSiusSql);
			cleanup(lFascSiepSql);
			cleanup(lFascSiepDAO);
			cleanup(lReFascDAO);
			cleanup(lNotEveDao);
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			cleanup(lEveSql);

			cleanup(conn);
		}

		return lSogDuplicato;
	}

	/**
	 * Modifica del Soggetto riferito a Procedimenti SIGE
	 */
	public SoggettoModel ExModificaSoggettoStoriciSige(SoggettoModel aSoggetto, Vector lKeyFascicoli,
			int lFascicoli, int lFascicoliAltriUff, BigDecimal IdSoggettoVecchio) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;
		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSigeDAO lFasDAO = null;
		AliasDAO lAliasDAO = null;
		AliasSqlDAO lAliasSqlDAO = null;

		StoricoSoggettoModel lStoricoSogg = null;
		SoggettoModel lSogDuplicato = null;
		BigDecimal lKeySoggDuplicato = null;

		try {
			conn = getDBTransaction();

			lSogDao = new SoggettoDAO(conn);
			lSogSqlDao = new SoggettoSqlDAO(conn);
			lFasDAO = new FascicoloSigeDAO(conn);
			lAliasDAO = new AliasDAO(conn);
			lAliasSqlDAO = new AliasSqlDAO(conn);
			lSogDuplicato = new SoggettoModel();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("lKeyFascicoli selezionati.size----->" + lKeyFascicoli.size());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("lFascicoli presenti----->" + lFascicoli);

			lSogSqlDao.ricercaSoggettoByKey(IdSoggettoVecchio);
			SoggettoModel lSoggVecchio = (SoggettoModel) lSogSqlDao.getModelByKey();

			// SE VENGONO SELEZIONATI TUTTI I FASCICOLI
			if (lFascicoli == lKeyFascicoli.size() && lFascicoliAltriUff == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("SELEZIONATI TUTTI I FASCICOLI----------->");

				lSogSqlDao.ricercaSoggettoByKey(IdSoggettoVecchio);
				SoggettoModel aSoggettoVecchio = (SoggettoModel) lSogSqlDao.getModelByKey();

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("SOGGETTO VECCHIO---------->" + aSoggettoVecchio);

				// Se il codice Cs e' null oppure stringa vuota valgono le modifiche a questo campo
				if ((aSoggettoVecchio.getCodCs() != null) && ((!aSoggettoVecchio.getCodCs().equals("")))) {
					// Se non vuoto verifico che non sia stato cambiato il codiceCs
					if (!(aSoggettoVecchio.getCodCs().equals(aSoggetto.getCodCs()))) {
						aSoggettoVecchio.setMessage("Impossibile modificare il CodiceCs del Soggetto");
						return aSoggettoVecchio;
					}
				}
				// Se l'atto di nascita e' null oppure stringa vuota valgono le modifiche
				if ((aSoggettoVecchio.getAttoNascita() != null)
						&& (!(aSoggettoVecchio.getAttoNascita().equals("")))) {
					// Se non vuoto verifico che non sia stato cambiato l'atto di Nascita
					if (!(aSoggettoVecchio.getAttoNascita().equals(aSoggetto.getAttoNascita()))) {
						aSoggettoVecchio.setMessage("Impossibile modificare l'atto di nascita del Soggetto");
						return aSoggettoVecchio;
					}
				}

				aSoggetto.setCodOperatoreAggiornamento(aSoggetto.getCodOperatoreAggiornamento());
				aSoggetto.setCodUfficioAggiornamento(aSoggetto.getCodUfficioAggiornamento());
				aSoggetto.setDataAggiornamento(aSoggetto.getDataAggiornamento());
				aSoggetto.setIdSoggetto(aSoggettoVecchio.getIdSoggetto());
				lSogDao.setDAOFromModelForUpdate(aSoggetto);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("SOGGETTO NUOVO ---------->" + aSoggetto);
				lSogDao.selCondizioneUpdate(aSoggettoVecchio.getIdSoggetto());
				lSogDao.update();

				// INSERISCO STORICO SOGGETTO
				lStoricoSogg = inserimentoStoricoSoggetto(lSoggVecchio, aSoggetto.getDataAggiornamento(),
						conn);

				// SETTO IL SOGGETTO VECCHIO IN QUELLO DUPLICATO PER FAR ANDARE BENE LA SELECT DI DETTAGLIO
				// VISTO CHE NON HO DUPLICATO PERCHe' SONO STATI SELEZIONATI TUTTI I FASCICOLI ASSOCIATI AL
				// SOGG
				lSogDuplicato = aSoggetto;

			} else {
				if (lKeyFascicoli.size() > 0) {
					// DUPLICO SOGGETTO
					lSogDao.setDAOFromModel(aSoggetto);
					lKeySoggDuplicato = lSogDao.insert();
					lSogDuplicato.setIdSoggetto(lKeySoggDuplicato);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// mLog
					siesLogger.info("soggetto INSERITO----->" + lKeySoggDuplicato);

					// DUPLICO ALIAS
					lAliasSqlDAO.ricercaAliasByIdSoggetto(IdSoggettoVecchio);
					Vector lTutteAlias = new Vector(lAliasSqlDAO.getModels());
					if (lTutteAlias != null) {
						Iterator lItx = lTutteAlias.iterator();

						while (lItx.hasNext()) {
							AliasModel lAliasMod = (AliasModel) lItx.next();

							if (lAliasMod != null) {
								lAliasMod.setSogIdSoggetto(lKeySoggDuplicato);
								lAliasMod
										.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
								lAliasMod.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
								lAliasMod.setDataInserimento(DateUtils.getSysDate());
								lAliasDAO.setDAOFromModel(lAliasMod);
								lAliasDAO.insert();
								lAliasDAO.stop();
							}
						}
					}

					// Aggiornamento Residenze
					aggiornaResidenze(aSoggetto, lKeyFascicoli, lKeySoggDuplicato, conn);
					// Aggiornamento Notifiche
					aggiornaNotifiche(aSoggetto, lKeyFascicoli, lKeySoggDuplicato, IdSoggettoVecchio, conn);

					lStoricoSogg = inserimentoStoricoSoggetto(lSoggVecchio, aSoggetto.getDataAggiornamento(),
							conn);

					for (int h = 0; h < lKeyFascicoli.size(); h++) {
						BigDecimal lIdFasc = (BigDecimal) lKeyFascicoli.get(h);
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("lIdFasc--->" + lIdFasc);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.info(lStoricoSogg);

						// UPDATE SUL FASCICOLO CON NUOVO SOGGETTO
						lFasDAO.setSogIdSoggetto(lKeySoggDuplicato);
						lFasDAO.setCodOperatoreAggiornamento(aSoggetto.getCodOperatoreAggiornamento());
						lFasDAO.setCodUfficioAggiornamento(aSoggetto.getCodUfficioAggiornamento());
						lFasDAO.setDataAggiornamento(aSoggetto.getDataAggiornamento());
						lFasDAO.setCondizioneUpdate(lIdFasc);

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di mLog
						siesLogger.info("FASCICOLO SIGE AGGIORNATO ---->" + lIdFasc);
						lFasDAO.update();
						lFasDAO.stop();
					}
				} // enf if (lKeyFascicoli.size() > 0)
					// Insert
					// commit(conn);
			} // end else
			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStoriciSige", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStoriciSige : " + ex);
		} catch (SQLException sqe) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStoriciSige", sqe);
			throw new F3BException("SoggettoController.ExModificaSoggettoStoriciSige : " + sqe);
		} catch (Exception ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("SoggettoController.ExModificaSoggettoStoriciSige", ex);
			throw new F3BException("SoggettoController.ExModificaSoggettoStoriciSige: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(lSogSqlDao);
			cleanup(lFasDAO);
			cleanup(lAliasDAO);
			cleanup(lAliasSqlDAO);

			cleanup(conn);
		}

		return lSogDuplicato;
	}

	// INSERISCO STORICO SOGGETTO
	private StoricoSoggettoModel inserimentoStoricoSoggetto(SoggettoModel aSoggVecchio, Date aDataVariazione,
			Connection aConn) throws Exception {

		// si istanziano i DAO
		StoricoSoggettoDAO lStoSogDao = null;
		StoricoSoggettoSqlDAO lStoSogSqlDao = null;

		StoricoSoggettoModel lStoricoSogg = new StoricoSoggettoModel();

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		try {
			lStoSogDao = new StoricoSoggettoDAO(aConn);
			lStoSogSqlDao = new StoricoSoggettoSqlDAO(aConn);

			// Valorizzazione del record da inserire
			lStoricoSogg.setIdSoggettoVariato(aSoggVecchio.getIdSoggetto());
			lStoricoSogg.setDataVariazione(aDataVariazione);
			lStoricoSogg.setNome(aSoggVecchio.getNome());
			lStoricoSogg.setCognome(aSoggVecchio.getCognome());
			lStoricoSogg.setAnnoNascita(aSoggVecchio.getAnnoNascita());
			lStoricoSogg.setMeseNascita(aSoggVecchio.getMeseNascita());
			lStoricoSogg.setDataNascita(aSoggVecchio.getDataNascita());
			lStoricoSogg.setDataNascitaPresunta(aSoggVecchio.getDataNascitaPresunta());
			lStoricoSogg.setCodComuneNascita(aSoggVecchio.getCodComuneNascita());
			lStoricoSogg.setCodProvinciaNascita(aSoggVecchio.getCodProvinciaNascita());
			lStoricoSogg.setCodComuneCasellario(aSoggVecchio.getCodComuneCasellario());
			lStoricoSogg.setCodFiscale(aSoggVecchio.getCodFiscale());
			lStoricoSogg.setCodCs(aSoggVecchio.getCodCs());
			lStoricoSogg.setCodAfis(aSoggVecchio.getCodAfis());
			lStoricoSogg.setCodStatoNascita(aSoggVecchio.getCodStatoNascita());
			lStoricoSogg.setDescComuneNascitaEstero(aSoggVecchio.getDescComuneNascitaEstero());
			lStoricoSogg.setNazionalita(aSoggVecchio.getNazionalita());
			lStoricoSogg.setPaternita(aSoggVecchio.getPaternita());
			lStoricoSogg.setCognomeMadre(aSoggVecchio.getCognomeMadre());
			lStoricoSogg.setNomeMadre(aSoggVecchio.getNomeMadre());
			lStoricoSogg.setSesso(aSoggVecchio.getSesso());
			lStoricoSogg.setAttoNascita(aSoggVecchio.getAttoNascita());
			lStoricoSogg.setNote(aSoggVecchio.getNote());
			lStoricoSogg.setCodOperatoreInserimento(aSoggVecchio.getCodOperatoreAggiornamento());
			lStoricoSogg.setDataInserimento(aSoggVecchio.getDataAggiornamento());
			lStoricoSogg.setCodUfficioInserimento(aSoggVecchio.getCodUfficioAggiornamento());
			lStoricoSogg.setEtaPresuntaAnni(aSoggVecchio.getEtaPresuntaAnni());
			lStoricoSogg.setEtaPresuntaMesi(aSoggVecchio.getEtaPresuntaMesi());

			// Valutazione del progressivo
			lStoSogSqlDao.nextProgressivo(aSoggVecchio.getIdSoggetto());
			int lMaxProg = 0;

			lStoSogSqlDao.start();
			if (lStoSogSqlDao.next()) {
				lMaxProg = lStoSogSqlDao.getInt("max_progressivo");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("MAX = " + lMaxProg);
			}
			BigDecimal lMax = new BigDecimal(lMaxProg + 1);

			if (lMaxProg > 0) {
				lStoricoSogg.setProgressivoStorico(lMax);
			} else {
				// Dovuto al fatto che per un nuovo Id mi ritorna un progressivo uguale a zero
				lStoricoSogg.setProgressivoStorico(new BigDecimal(1));
			}

			lStoSogDao.setDAOFromModel(lStoricoSogg);
			BigDecimal lStoricoNuovo = lStoSogDao.insert();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("STORICO NUOVO---->" + lStoricoNuovo);
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			// reset dei DAO
			cleanup(lStoSogDao);
			cleanup(lStoSogSqlDao);
		}

		return lStoricoSogg;
	}

	private void aggiornaResidenze(SoggettoModel aSoggetto, Vector aKeyFascicoli,
			BigDecimal aKeySoggDuplicato, Connection aConn) throws Exception {

		ResidenzaDAO lResDAO = null;
		ResidenzaFascicoloSigeDAO lReFascDAO = null;

		// Lista delle Residenze associate allo stesso Fascicolo SIGE
		Vector lResidenze = null;
		// Residenza da duplicare
		ResidenzaModel lResMod = new ResidenzaModel();
		// ID della nuova Residenza inserita
		BigDecimal lKeyNewRes = null;

		// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
		try {
			// DAO alla tabella Residenza
			lResDAO = new ResidenzaDAO(aConn);
			// DAO alla tabella Residenza_Fascicolo_Sige
			lReFascDAO = new ResidenzaFascicoloSigeDAO(aConn);

			// Ricerca di tutte le residenze collegate ai Fascicoli SIGE selezionati per la modifica del
			// Soggetto
			for (int v = 0; v < aKeyFascicoli.size(); v++) {
				// ID Fascicolo Sige
				BigDecimal lIdfFasc = (BigDecimal) aKeyFascicoli.get(v);

				// Ricerca delle Residenze collegate al Fascicolo Sige corrente
				lResDAO.setCondizioneIdFasSige(lIdfFasc);
				lResidenze = new Vector(lResDAO.getModels());
				lResDAO.stop();

				// Puo' esserci 1 o piu' di 1 Residenza per Fascicolo
				if (lResidenze != null) {
					Iterator lResItx = lResidenze.iterator();
					// Itera sulle residenze da duplicare
					while (lResItx.hasNext()) {
						lResMod = (ResidenzaModel) lResItx.next();
						// Duplicazione Residenza per legarla al nuovo Soggetto
						lResMod.setCodOperatoreInserimento(aSoggetto.getCodOperatoreAggiornamento());
						lResMod.setDataInserimento(aSoggetto.getDataAggiornamento());
						lResMod.setCodUfficioInserimento(aSoggetto.getCodUfficioAggiornamento());
						lResMod.setSogIdSoggetto(aKeySoggDuplicato);
						lResDAO.setDAOFromModel(lResMod);
						lKeyNewRes = lResDAO.insert();
						lResDAO.stop();

						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di
						// mLog
						siesLogger.info("inserisco residenza DUPLICATA --->" + lKeyNewRes);

						// Aggiornamento della tabella RESIDENZA_FASCICOLO_SIGE
						ResidenzaFascicoloSigeModel lResFasSige = new ResidenzaFascicoloSigeModel();
						lResFasSige.setFasSigeIdFascicoloSige(lIdfFasc);
						lResFasSige.setResIdResidenza(lResMod.getIdResidenza());
						lReFascDAO.setCondizioneUpdateSoggetto(lResFasSige);

						// Si aggiorna la tabella con il nuovo ID Residenza
						lReFascDAO.setResIdResidenza(lKeyNewRes);
						lReFascDAO.update();
						lReFascDAO.stop();
					}
				} // end if lResidenze
			} // end for
		} finally {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			// reset dei DAO
			cleanup(lResDAO);
			cleanup(lReFascDAO);
		}
	}

	/**
	 * Aggiornamento di eventuali Notifiche a Soggetto modificato legati a Fascicoli Sige.
	 *
	 * @param aSoggetto
	 *            : Soggetto modificato
	 * @param aKeyFascicoli
	 *            : elenco degli ID Fascicolo Sige interessati dalla modifica al Soggetto
	 * @param aKeySoggDuplicato
	 *            : ID Soggetto nuovo
	 * @param aConn
	 *            : connessione
	 * @throws Exception
	 */
	private void aggiornaNotifiche(SoggettoModel aSoggetto, Vector aKeyFascicoli,
			BigDecimal aKeySoggDuplicato, BigDecimal aIdSoggettoVecchio, Connection aConn) throws Exception {

		BigDecimal lIdFasSige = null;
		NotificaDAO lNotDao = null;

		if (aKeyFascicoli != null && aKeyFascicoli.size() > 0) {
			// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
			try {
				lNotDao = new NotificaDAO(aConn);

				Iterator lFasItx = aKeyFascicoli.iterator();
				// Itera sui Fascicoli interessati
				while (lFasItx.hasNext()) {
					lIdFasSige = (BigDecimal) lFasItx.next();

					lNotDao.setSogIdSoggetto(aKeySoggDuplicato);
					lNotDao.setCodOperatoreAggiornamento(aSoggetto.getCodOperatoreAggiornamento());
					lNotDao.setCodUfficioAggiornamento(aSoggetto.getCodUfficioAggiornamento());
					lNotDao.setDataAggiornamento(aSoggetto.getDataAggiornamento());
					lNotDao.selCondizioneUpdateXIdFascicoliSigeIdSoggetto(lIdFasSige, aIdSoggettoVecchio);
					lNotDao.update();
					lNotDao.stop();
				}
			} finally {
				// Scheda Intervento n° 6 - Ottimizzazione SIUS Avvocati
				cleanup(lNotDao);
			}
		}
	}

	public void ExModificaKeyNSCByKey(SoggettoModel aSoggetto) throws F3BException {

		Connection conn = null;
		SoggettoDAO lSogDao = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
		siesLogger.info("SoggettoController.ExModificaKeyNSCByKey: entrata nel metodo ");

		try {
			conn = getDBConnection();
			lSogDao = new SoggettoDAO(conn);
			lSogDao.setDAOFromModelForUpdateKeyNsc(aSoggetto);
			lSogDao.selCondizioneUpdate(aSoggetto.getIdSoggetto());
			lSogDao.update();

			commit(conn);
		} catch (DAOException ex) {
			rollback(conn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("DAOException: " + ex);
			throw new SICOException("SoggettoController.ExModificaKeyNSCByKey: " + ex);
		} finally {
			cleanup(lSogDao);
			cleanup(conn);
		}
	}

	/**
	 * ExRicercaSoggettoFascicoliPaged Ricerca il Soggetto e fascicoli associati in Banca Dati
	 *
	 * @param aSoggetto
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettoConFascicoliPaged(SoggettoModel aSoggetto,
			String lCodUfficioUtenteConnesso, String lCodDistrettoUtenteConnesso, String TipoRicerca,
			int aPage, String majorOffice) throws F3BException {

		Connection lConn = null;
		Vector lSoggettiFascicoli = new Vector(); // vettore in uscita contenente supersoggetto e tutti i
													// fascicoli collegati
		SoggettoSqlDAO lSogSqlDao = null; // usato per cercare il primo soggetto del supersoggetto
		FascicoloSiepSoggettoSqlDAO lFasSoggDao = null; // usato per cercare i fascicoli collegati al
														// supersoggetto
		FascicoloSiepSqlDAO lFasDao = null; // usato per cercare il fascicolo per trasformare chiave ufficio
											// in descrizione ad es. PM ROMA
		SoggettoFascicoloSqlDAO lSoggFasDao = null; // usato per cercare i soggetti secondo le condizioni di
													// ricerca raggruppati per supersoggetto
		StatoProcedimentoSqlDAO lStaDao = null; // usato per trovare lo stato procedimento del fascicolo

		try {
			// paolo cherubini rivista e' commentata per anomalia su ricerca istanza per soggetto

			// apro la connessione e preparo tutti i DAO
			lConn = getDBConnection();
			lFasSoggDao = new FascicoloSiepSoggettoSqlDAO(lConn);
			lSoggFasDao = new SoggettoFascicoloSqlDAO(lConn);
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lStaDao = new StatoProcedimentoSqlDAO(lConn);
			lFasDao = new FascicoloSiepSqlDAO(lConn);

			SoggettoFascicoliModel lSogFasMod = null; // model per contenere sia i fascicoli che il soggetto
			Vector lFascicoli = null; // vettore per contenere di volta in volta i fascicoli del supersoggetto
			FascicoloSiepModel lFascicolo = new FascicoloSiepModel(); // model per il fascicolo

			// effettuo la ricerca dei soggetti secondo le condizioni di ricerca raggruppati per supersoggetto
			lSoggFasDao.ricercaSuperSoggettoFascicoliBySoggetto(aSoggetto, lCodUfficioUtenteConnesso, aPage,
					lCodDistrettoUtenteConnesso, TipoRicerca, majorOffice, false, "");
			lSoggFasDao.start();

			// ciclo su tutti i supersoggetti
			while (lSoggFasDao.next()) {

				// inserisco i risultati della ricerca dentro il model del fascicolo tramite 2 DAO diversi
				// o dell'ufficio o del distretto a seconda del tipo ricerca
				if (TipoRicerca != null && TipoRicerca.equals("ufficio"))
					lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModel();
				else if (TipoRicerca != null && TipoRicerca.equals("distretto"))
					lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModelDistre();
				else if (TipoRicerca != null && TipoRicerca.equals("tutto"))
					lFascicolo = (FascicoloSiepModel) lSoggFasDao.getSoggettoFascicoliModelDistre();
				else
					throw new SIEPException(SIEPException.USER_MESSAGE, "Selezionare il Tipo di ricerca");

				SoggettoModel lSogMod = lFascicolo.getSoggetto(); // prendo il soggetto collegato al fascicolo

				// recupero l'id del primo soggetto del supersoggetto e aggiorno il fascicolo
				// questo perche' l'id del soggetto nel fascicolo era stato impostato a 1 per essere
				// raggruppato
				lSogSqlDao.ricercaSuperSoggetto(TipoRicerca, lCodUfficioUtenteConnesso, lSogMod,
						"FASCICOLO_SIEP", lCodDistrettoUtenteConnesso, majorOffice, false, "");
				aSoggetto = (SoggettoModel) lSogSqlDao.getModelByKey();
				lFascicolo.setSogIdSoggetto(aSoggetto.getIdSoggetto());
				lFascicolo.getSoggetto().setIdSoggetto(aSoggetto.getIdSoggetto());

				// se il numero dei fascicoli e' 1 devo codificare la chiave ufficio in descrizione es: PM
				// ROMA
				if (lFascicolo.getNumFascicoli().equals("1")) {
					// Se l'accoppiata Soggetto/procedimento ha solo 1 procedimento, si carica la descrizione
					// dell'Uffico
					lFasDao.ricercaFascicolo(lFascicolo);
					Vector lFascicoli2 = new Vector(lFasDao.getModels());
					FascicoloSiepModel lFascicolo2 = (FascicoloSiepModel) lFascicoli2.get(0);
					String Compl = lFascicolo2.getCodTipoUfficio() + " Di "
							+ lFascicolo2.getDescrComuneUfficio();
					lFascicolo.setNumFascicoli(Compl);
				}

				// Caricamento dei fascicoli del SuperSoggetto
				lFascicoli = new Vector();
				lSogFasMod = new SoggettoFascicoliModel();
				lSogFasMod.setSoggetto(aSoggetto); // aggiorno il DAO di risposta poiche' ho corretto l'id del
													// soggetto
				lSogMod.setIdSoggetto(new BigDecimal("0")); // imposto a 0 l'id soggetto per poter effettuare
															// la ricerca supersoggetto
															// altrimenti accederebbe per ID

				lFasSoggDao.ricercaFascicoloSuperSoggetto(lSogMod, majorOffice);
				lFasSoggDao.startPage1(aPage);
				while (lFasSoggDao.next()) {
					// ciclo su tutti i fascicoli del supersoggetto
					lFascicolo = (FascicoloSiepModel) lFasSoggDao.getModel();
					// se la ricerca e' per ufficio carico solo i fascicoli di quell'ufficio
					if (TipoRicerca != null && TipoRicerca.equals("ufficio")
							&& !lFascicolo.getChiaveUfficio().equals(aSoggetto.getCodUfficioInserimento())) {
						// non carico
					} else {
						// carico il fascicolo aggiustando con l'id soggetto (vedi sopra)
						lStaDao.ricercaMaxStatoProcedimentoByFascicoloSiep(lFascicolo.getIdFascicoloSiep());
						lFascicolo.setCodStatoProcedimento(lStaDao.getCodStatoByKey());
						lFascicolo.setSogIdSoggetto(aSoggetto.getIdSoggetto()); // aggiungo questa
						lFascicolo.getSoggetto().setIdSoggetto(aSoggetto.getIdSoggetto()); // aggiungo questa
						lFascicoli.add(lFascicolo);
					}
				}

				lFasSoggDao.stop();

				// Caricamento elenco Fascicoli del SuperSoggetto
				if (!lFascicoli.isEmpty())
					lSogFasMod.setFascicoli(
							(FascicoloSiepModel[]) lFascicoli.toArray(new FascicoloSiepModel[0]));

				lSoggettiFascicoli.add(lSogFasMod);
			}
			if (lSoggettiFascicoli.isEmpty())
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("SoggettoController.ExRicercaSoggettoConFascicoliPaged: " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lFasSoggDao);
			cleanup(lFasDao);
			cleanup(lSoggFasDao);
			cleanup(lStaDao);
			cleanup(lConn);
		}

		return lSoggettiFascicoli;
	}

	/**
	 * Ricerca di Soggetti Omonimi del Soggetto passato e i Fascicoli Sige/Sentenze ad esso associati I
	 * parametri di ricerca sono NOME, COGNOME, DATA DI NASCITA, COMUNE DI NASCITA.
	 *
	 * @param aSoggetto
	 *            SoggettoModel
	 * @return Vettore di Soggetti per i quali esiste un Procedimento assegnato
	 * @throws F3BException
	 */
	public Vector ExRicercaSoggettiFascicoliOmonimi(SoggettoModel aSoggetto) throws F3BException {

		Connection lConn = null;

		Vector lSoggFascicoliSige = new Vector();

		SoggettoSqlDAO lSogSqlDao = null;
		FascicoloSigeDAO lFasSigeDao = null;
		FascicoloSigeSqlDAO lFasSigeSqlDao = null;
		FasSigeSentenzaSqlDAO lFasSigeSentenzaSqlDao = null;

		// Valorizzazione dei parametri di ricerca degli omonimi
		SoggettoModel lSoggetto = new SoggettoModel();
		lSoggetto.setCognome(aSoggetto.getCognome());
		lSoggetto.setNome(aSoggetto.getNome());
		lSoggetto.setDataNascita(aSoggetto.getDataNascita());
		lSoggetto.setCodComuneNascita(aSoggetto.getCodComuneNascita());
		lSoggetto.setSesso(aSoggetto.getSesso());
		lSoggetto.setCodComuneCasellario(aSoggetto.getCodComuneCasellario());
		lSoggetto.setCodProvinciaNascita(aSoggetto.getCodProvinciaNascita());
		lSoggetto.setCodStatoNascita(aSoggetto.getCodStatoNascita());
		lSoggetto.setCodAfis(aSoggetto.getCodAfis());

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lFasSigeDao = new FascicoloSigeDAO(lConn);
			lFasSigeSqlDao = new FascicoloSigeSqlDAO(lConn);
			lFasSigeSentenzaSqlDao = new FasSigeSentenzaSqlDAO(lConn);

			// ricerca degli omonimi
			lSogSqlDao.ricercaSoggettoOmonimo(lSoggetto);
			lSogSqlDao.start();

			SoggettoModel soggetto = new SoggettoModel();
			SoggettoFascicoliSigeModel soggFasSigeModel = null;

			// si cicla sui singoli soggetti (omonimo)
			// e per ognuno di essi si recupera il Fascicolo Sige
			// e il Titolo Esecutivo di competenza (Sentenza)
			while (lSogSqlDao.next()) {
				soggFasSigeModel = new SoggettoFascicoliSigeModel();
				// soggetto omonimo
				soggetto = (SoggettoModel) lSogSqlDao.getModel();
				// dati del soggetto
				soggFasSigeModel.setSoggetto(soggetto);

				// si ricercano i Fascicoli associati al soggetto
				lFasSigeSqlDao.ricercaFascicoloSigeBySoggetto(soggetto.getIdSoggetto());
				lFasSigeSqlDao.start();

				FascicoloSigeSentenzaModel lFascicoloSentenza = null;
				FascicoloSigeEstesoModel lFascicoloEsteso = null;
				Vector lFascicoli = new Vector();

				// per ciascun Fascicolo Sige trovato si recupera il Titolo Esecutivo di
				// competenza
				while (lFasSigeSqlDao.next()) {
					lFascicoloSentenza = new FascicoloSigeSentenzaModel();

					lFascicoloEsteso = (FascicoloSigeEstesoModel) lFasSigeSqlDao.getModelEsteso();

					// dati del Fascicolo Sige
					lFascicoloSentenza.setFascicoloEstesoSige(lFascicoloEsteso);

					lFasSigeSentenzaSqlDao.ricercaSentenzaByIdFascicolo(
							lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
					lFasSigeSentenzaSqlDao.start();

					SentenzaModel lSentenza = null;
					while (lFasSigeSentenzaSqlDao.next()) {
						lSentenza = (SentenzaModel) lFasSigeSentenzaSqlDao.getSentenzaByFascicolo();
					}

					// dati della Sentenza
					lFascicoloSentenza.setSentenza(lSentenza);

					lFascicoli.add(lFascicoloSentenza);

				}

				// aggiungo il soggetto omonimo alla lista, solo se ad esso sono associati dei procedimenti
				// commento per anomalia di continuo inserimento di soggetti nuovi da SIGE
				// if (lFascicoli.size() > 0) {
				soggFasSigeModel.setFascicoli(
						(FascicoloSigeSentenzaModel[]) lFascicoli.toArray(new FascicoloSigeSentenzaModel[0]));
				lSoggFascicoliSige.add(soggFasSigeModel);
				// }

			}

			lSogSqlDao.stop();
		} catch (Exception lEx) {
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggettiFascicoliOmonimi : " + lEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lFasSigeDao);
			cleanup(lFasSigeSqlDao);
			cleanup(lFasSigeSentenzaSqlDao);
			cleanup(lConn);
		}

		return lSoggFascicoliSige;
	}

	/**
	 * Ricerca Soggetto IGNOTO
	 *
	 * @return soggetto ignoto
	 * @throws F3BException
	 */
	public SoggettoModel ExRicercaSoggettoIgnoto() throws F3BException {

		Connection lConn = null;
		SoggettoModel lSoggetto = null;
		SoggettoSqlDAO lSogSqlDao = null;

		try {
			lConn = getDBConnection();
			lSogSqlDao = new SoggettoSqlDAO(lConn);
			lSogSqlDao.ricercaSoggettoIgnoto();
			lSogSqlDao.start();

			if (lSogSqlDao.next()) {
				lSoggetto = (SoggettoModel) lSogSqlDao.getSoggettoIgnotoModel();
			}

			lSogSqlDao.stop();

			if (lSoggetto == null) {
				throw new SICOException(SICOException.USER_MESSAGE, "Soggetto Ignoto non trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info(daoEx.getLocalizedMessage());
			throw new SICOException(SICOException.USER_MESSAGE,
					"SoggettoController.ExRicercaSoggettoIgnoto : " + daoEx);
		} finally {
			cleanup(lSogSqlDao);
			cleanup(lConn);
		}

		return lSoggetto;
	}

}