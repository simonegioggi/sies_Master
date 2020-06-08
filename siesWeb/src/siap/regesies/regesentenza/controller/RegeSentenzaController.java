package siap.regesies.regesentenza.controller;

import java.sql.Connection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.regesies.regeavvocato.dao.RegeAvvocatoSqlDAO;
import siap.regesies.regecircostanza.dao.RegeCircostanzaSqlDAO;
import siap.regesies.regenotiziareato.dao.RegeNotiziaReatoSqlDAO;
import siap.regesies.regereato.controller.RegeReatoController;
import siap.regesies.regeresidenza.dao.RegeResidenzaSqlDAO;
import siap.regesies.regesentenza.dao.RegeSentenzaDAO;
import siap.regesies.regesentenza.dao.RegeSentenzaElencoSqlDAO;
import siap.regesies.regesentenza.dao.RegeSentenzaSqlDAO;
import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.regesies.regesentenza.model.RegeSentenzaModel;
import siap.regesies.regesoggetto.dao.RegeSoggettoSqlDAO;
import siap.regesies.regesoggetto.model.RegeSoggettoModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.sentenza.dao.SentenzaSqlDAO;
import siap.siep.sentenza.model.SentenzaModel;

/**
 * <p>
 * Title: RegeSentenzaController
 * </p>
 * <p>
 * Description: Classe Controller per RegeSentenza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RegeSentenzaController extends SiapController implements IRegeSentenza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Trova un provvedimento dai suoi estremi
	 *
	 * @param aRegeSentenza
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoModel ExRicercaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException {

		Connection lConn = null;
		Vector lRegeSentenzi = new Vector();
		RegeSentenzaSqlDAO lRegDao = null;
		ProvvedimentoModel lProvvedimento = null;
		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaSqlDAO(lConn);
			lRegDao.ricercaRegeSentenza(aRegeSentenza);
			lRegeSentenzi = new Vector(lRegDao.getModels());
			if (lRegeSentenzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
			lProvvedimento = new ProvvedimentoModel();
			lProvvedimento.setRegeSentenza((RegeSentenzaModel) lRegeSentenzi.firstElement());
			this.ExRicercaElementiPerProvvedimento(lProvvedimento);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExRicercaRegeSentenza: Non posso leggere: " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lProvvedimento;
	}

	/**
	 * Trova un vettore di provvedimenti
	 *
	 * @param aRegeSentenza
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaRegeSentenzaDaElenco(RegeSentenzaModel aRegeSentenza) throws F3BException {

		Connection lConn = null;
		Vector lRegeSentenzi = new Vector();
		RegeSentenzaSqlDAO lRegDao = null;
		// ProvvedimentoModel lProvvedimento = null;
		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaSqlDAO(lConn);
			lRegDao.ricercaRegeSentenza(aRegeSentenza);
			lRegeSentenzi = new Vector(lRegDao.getModels());
			if (lRegeSentenzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExRicercaRegeSentenzaDaElenco: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeSentenzi;
	}

	/**
	 * Ricerca il dettaglio della sentenza/decreto
	 *
	 * @param aKey
	 *            - Chiave dell'id del file
	 * @return Rege Sentenza Model inserito
	 * @throws F3BException
	 */
	public RegeSentenzaModel ExRicercaRegeSentenzaByKey(String aKey) throws F3BException {

		Connection lConn = null;
		RegeSentenzaSqlDAO lRegDao = null;
		RegeSentenzaModel lRegMod;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaSqlDAO(lConn);
			lRegDao.ricercaRegeSentenzaByKey(aKey);
			lRegMod = (RegeSentenzaModel) lRegDao.getModelByKey();
			if (lRegMod == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExRicercaRegeSentenzaByKey: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Modifica la sentenza Rege
	 *
	 * @param aRegeSentenza
	 * @return RegeSentenzaModel modificato
	 * @throws F3BException
	 */
	public RegeSentenzaModel ExModificaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException {

		Connection lConn = null;
		RegeSentenzaDAO lRegDao = null;
		RegeSentenzaModel lRegMod = new RegeSentenzaModel(aRegeSentenza);

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaDAO(lConn);
			lRegDao.setDAOFromModelForUpdate(aRegeSentenza);
			lRegDao.update();
			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + ex);
			throw new F3BException("RegeSentenzaController.ExModifica: Non posso inserire: " + ex);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegMod;
	}

	/**
	 * Elenco Provvedimenti
	 *
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaElencoProvvedimenti(int aPage, String aCodComune) throws F3BException {

		Connection lConn = null;
		Vector lRegeSentenzi = new Vector();
		RegeSentenzaElencoSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaElencoSqlDAO(lConn);
			if (aCodComune.equals("000000"))
				lRegDao.ricercaPaginataProvvedimentoSecGrado(aPage);
			else
				lRegDao.ricercaPaginataProvvedimento(aPage, aCodComune);

			lRegeSentenzi = new Vector(lRegDao.getModels());
			if (lRegeSentenzi.size() == 0) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExRicercaElencoProvvedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lRegeSentenzi;
	}

	/**
	 * Cancella la sentenza
	 *
	 * @param aRegeSentenza
	 * @throws F3BException
	 */
	public void ExCancellaRegeSentenza(RegeSentenzaModel aRegeSentenza) throws F3BException {

		Connection lConn = null;
		RegeSentenzaDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaDAO(lConn);
			lRegDao.setCondizioneUpdate(aRegeSentenza.getIdFile());
			lRegDao.delete();
			commit(lConn);
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExCancellaRegeSentenza: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
	}

	/**
	 * Conta le occorrenze dei provvedimenti
	 *
	 * @return
	 * @throws F3BException
	 */
	public int ExGetCountProvvedimenti(String aCodComune) throws F3BException {

		int lCount = 0;
		Connection lConn = null;
		RegeSentenzaElencoSqlDAO lSenSqlDao = null;

		try {
			lConn = getDBConnection();
			lSenSqlDao = new RegeSentenzaElencoSqlDAO(lConn);

			if (aCodComune.equals("000000"))
				lSenSqlDao.getCountProvvedimenti();
			else
				lSenSqlDao.getCountProvvedimenti(aCodComune);
			lSenSqlDao.start();
			lSenSqlDao.next();
			lCount = lSenSqlDao.getInt("HowManyRecords");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Numero Rege Sentenze Trovate" + lCount);
			lSenSqlDao.stop();
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error(daoEx.getLocalizedMessage());
			throw new F3BException(F3BException.USER_MESSAGE, this.getClass().getName()
					+ ".ExGetCountProvvedimenti: Non posso leggere i soggetti : " + daoEx);
		} finally {
			cleanup(lSenSqlDao);
			cleanup(lConn);
		}
		return lCount;
	}

	/**
	 * Metodo per scoprire se esiste un provvedimento duplicato
	 *
	 * @param aRegeSentenza
	 * @return Sentenza trovata in SIEP
	 * @throws F3BException
	 */
	public SentenzaModel ExRicercaSentenzaDuplicata(RegeSentenzaModel aRegeSentenza) throws F3BException {

		Connection lConn = null;
		SentenzaModel lSen = null;
		SentenzaSqlDAO lSenSQL = null;

		try {
			lConn = getDBConnection();

			lSenSQL = new SentenzaSqlDAO(lConn);
			SentenzaModel lSent = aRegeSentenza.toSentenza();
			SentenzaModel lRis = null;
			lSenSQL.ricercaSentenzaDuplicataRege(lSent);

			Vector lRisRic = new Vector(lSenSQL.getModels());
			if (lRisRic != null && lRisRic.size() > 0) {
				lRis = (SentenzaModel) lRisRic.firstElement();

				if (lRisRic.size() > 0 && !lRis.isAltroGiudizio() && !lRis.isSentenzaCassazione()) {
					lSen = new SentenzaModel(lRis);
				}
			}
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("RegeSentenzaController.ExRicercaSentenzaDuplicata " + daoEx);
		} catch (Exception ex) {
			rollback(lConn);
			ex.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Exception: " + ex);
			throw new SIEPException("RegeSentenzaController.ExRicercaSentenzaDuplicata " + ex);
		} finally {
			cleanup(lSenSQL);
			cleanup(lConn);
		}

		return lSen;
	}

	/**
	 * Dettaglio Provvedimento proveniente da Rege
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoModel ExDettaglioProvvedimento(String aKey) throws F3BException {

		ProvvedimentoModel lProvvedimento = new ProvvedimentoModel();

		RegeSentenzaModel lRegeSen = ExRicercaRegeSentenzaByKey(aKey);
		lProvvedimento.setRegeSentenza(lRegeSen);

		this.ExRicercaElementiPerProvvedimento(lProvvedimento);

		return lProvvedimento;
	}

	/**
	 * Metodo per riempire il model Provvedimento con tutte le entità correlate
	 *
	 * @param lProvvedimento
	 * @return Provvedimento trovato
	 * @throws F3BException
	 *             nel caso in cui non c'e' un soggetto associato
	 */
	private ProvvedimentoModel ExRicercaElementiPerProvvedimento(ProvvedimentoModel lProvvedimento)
			throws F3BException {

		RegeSoggettoSqlDAO lRegSoggDao = null;
		RegeSoggettoModel lRegeSoggMod = null;
		Connection lConn = null;
		RegeResidenzaSqlDAO lRegResidenzaDao = null;
		RegeCircostanzaSqlDAO lCircSqlDao = null;
		RegeNotiziaReatoSqlDAO lNotReaSqlDao = null;
		RegeAvvocatoSqlDAO lAvvSqlDao = null;

		try {
			// Ricerco sentenza duplicata in SIEP
			SentenzaModel lSent = ExRicercaSentenzaDuplicata(lProvvedimento.getRegeSentenza());
			if (lSent != null) // Set sentenza duplicata
				lProvvedimento.setSentenza(lSent);

			String aKey = lProvvedimento.getRegeSentenza().getIdFile();

			lConn = getDBConnection();
			// Ricerco il Soggetto
			lRegSoggDao = new RegeSoggettoSqlDAO(lConn);
			lRegSoggDao.ricercaRegeSoggettoByKey(aKey);
			lRegeSoggMod = (RegeSoggettoModel) lRegSoggDao.getModelByKey();

			// Setto il Soggetto
			if (lRegeSoggMod != null) {
				lProvvedimento.setRegeSoggetto(lRegeSoggMod);

				// Cerco l'omonimo in SIEP
				ISoggetto lCSiepSoggetto = SICOLookupRemote.getSoggettoRemote();
				SoggettoModel lSogMod = lRegeSoggMod.toSoggettoModel();

				// Qui resta il dubbio di dover cambiare la query eliminando il like!!!!
				Vector lSoggettiOmonimi = lCSiepSoggetto.ExRicercaSoggettiOmonimi(lSogMod);

				if (lSoggettiOmonimi.size() > 0) {
					// Carico i soggetti omonimi
					lProvvedimento.setSoggettiOmonimi(lSoggettiOmonimi);
				}
			} else
				// Soggetto non Trovato
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile visualizzare il dettaglio del Provvedimento " + "<font class=\"cRosso\">"
								+ lProvvedimento.getRegeSentenza().getAnnoSentenza() + "/"
								+ lProvvedimento.getRegeSentenza().getNumeroSentenza()
								+ "</font>.<br>Nessun soggetto associato al Provvedimento selezionato.");

			// Residenza Domicilio
			lRegResidenzaDao = new RegeResidenzaSqlDAO(lConn);
			lRegResidenzaDao.ricercaRegeResidenza(aKey);
			Vector lRegeResidenzi = new Vector(lRegResidenzaDao.getModels());
			if (lRegeResidenzi != null)
				lProvvedimento.setResidenze(lRegeResidenzi);

			// Reati
			RegeReatoController lReaCtrl = new RegeReatoController();
			try {
				Vector lReati = lReaCtrl.ExRicercaReatoCircostanzaByProvvedimento(aKey);
				if (lReati != null)
					lProvvedimento.setReati(lReati);
			} catch (F3BException ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Nessun reato per il provvedimento");
			}

			// Circostanze
			lCircSqlDao = new RegeCircostanzaSqlDAO(lConn);
			lCircSqlDao.ricercaRegeCircostanzaByIdFile(aKey);
			Vector lCircostanze = new Vector(lCircSqlDao.getModels());
			if (lCircostanze != null && lCircostanze.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
				siesLogger.info("Sono nel settaggio delle circostanze");
				lProvvedimento.setCircostanze(lCircostanze);
			}

			// Notizia di reato
			lNotReaSqlDao = new RegeNotiziaReatoSqlDAO(lConn);
			lNotReaSqlDao.ricercaRegeNotiziaReatoByIdFile(aKey);
			Vector lNotReati = new Vector(lNotReaSqlDao.getModels());
			if (lNotReati != null && lNotReati.size() > 0) {
				lProvvedimento.setNotizieDiReato(lNotReati);
			}

			// Difensori
			lAvvSqlDao = new RegeAvvocatoSqlDAO(lConn);
			lAvvSqlDao.ricercaRegeAvvocatoByProvvedimento(aKey);
			Vector lAvvocati = new Vector(lAvvSqlDao.getModels());
			if (lAvvocati != null && lAvvocati.size() > 0) {
				lProvvedimento.setDifensori(lAvvocati);
			}
		} catch (DAOException daoEx) {
			rollback(lConn);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new SIEPException("RegeSentenzaController.ExRicercaElementiPerProvvedimento " + daoEx);
		} finally {
			cleanup(lRegSoggDao);
			cleanup(lRegResidenzaDao);
			cleanup(lCircSqlDao);
			cleanup(lNotReaSqlDao);
			cleanup(lAvvSqlDao);

			cleanup(lConn);
		}
		return lProvvedimento;
	}

	/**
	 * Ricerca sentenza rege per estremi
	 *
	 * @param aRegeSentenza
	 * @return
	 * @throws F3BException
	 */
	public ProvvedimentoModel ExRicercaRegeSentenzaPerEstremi(RegeSentenzaModel aRegeSentenza)
			throws F3BException {

		Connection lConn = null;
		RegeSentenzaModel lRegeSentenzi = null;
		ProvvedimentoModel lProvvedimento = null;
		RegeSentenzaElencoSqlDAO lRegDao = null;

		try {
			lConn = getDBConnection();
			lRegDao = new RegeSentenzaElencoSqlDAO(lConn);
			lRegDao.ricercaRegeSentenza(aRegeSentenza);
			lRegeSentenzi = (RegeSentenzaModel) lRegDao.getModelByKey();
			if (lRegeSentenzi == null) {
				throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");
			}
			lProvvedimento = new ProvvedimentoModel();
			lProvvedimento.setRegeSentenza(lRegeSentenzi);

			if (lRegeSentenzi.getCountSoggetti() == 1) {
				lProvvedimento = this.ExRicercaRegeSentenza(lRegeSentenzi);
				lProvvedimento.getRegeSentenza().setCountSoggetti(1);
			}
		} catch (DAOException daoEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("DAOException: " + daoEx);
			throw new F3BException(
					"RegeSentenzaController.ExRicercaElencoProvvedimenti: Non posso leggere : " + daoEx);
		} finally {
			cleanup(lRegDao);
			cleanup(lConn);
		}
		return lProvvedimento;
	}

}