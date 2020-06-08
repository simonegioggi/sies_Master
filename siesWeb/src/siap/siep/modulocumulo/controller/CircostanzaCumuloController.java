package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.siep.modulocumulo.dao.CircostanzaCumuloDAO;
import siap.siep.modulocumulo.dao.CircostanzaCumuloSqlDAO;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;

/**
 * <p>
 * Title: CircostanzaCumuloController
 * </p>
 * <p>
 * Description: Classe Controller per Circostanza Cumulo
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CircostanzaCumuloController extends SiapController implements ICircostanzaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Inserisce solo UN Record CIRCOSTANZA_CUMULO da CircostanzaCumuloModel
	public CircostanzaCumuloModel ExInserisciCircostanzaCumulo(CircostanzaCumuloModel aCircostanza)
			throws F3BException {

		Connection lConn = null;
		CircostanzaCumuloDAO lCirDao = null;
		CircostanzaCumuloModel lCirMod = null;

		try {
			lConn = getDBConnection();

			lCirMod = new CircostanzaCumuloModel(aCircostanza);
			lCirDao = new CircostanzaCumuloDAO(lConn);
			lCirDao.setDAOFromModel(aCircostanza);
			BigDecimal lKey = null;
			lKey = lCirDao.insert();
			commit(lConn);

			lCirMod.setIdCircostanzaCumulo(lKey);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.info("DAOException: " + ex);
			throw new F3BException("CircostanzaCumuloController.ExInserisciCircostanzaCumulo: " + ex);
		} finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}

		return lCirMod;
	}

	public CircostanzaCumuloModel ExRicercaCircostanzaCumuloByKey(BigDecimal aKey) throws F3BException {

		Connection lConn = null;
		CircostanzaCumuloSqlDAO lCirCumSqlDao = null;
		CircostanzaCumuloModel lCirCumMod;

		try {
			lConn = getDBConnection();

			lCirCumSqlDao = new CircostanzaCumuloSqlDAO(lConn);

			lCirCumSqlDao.ricercaCircostanzaCumuloByKey(aKey);

			lCirCumMod = (CircostanzaCumuloModel) lCirCumSqlDao.getModelByKey();
		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			throw new F3BException("CircostanzaCumuloController.ExRicercaCircostanzaCumuloByKey: " + daoEx);
		} finally {
			cleanup(lCirCumSqlDao);
			cleanup(lConn);
		}

		return lCirCumMod;
	}

	public CircostanzaCumuloModel ExModificaCircostanzaCumulo(CircostanzaCumuloModel aCircostanza,
			boolean flagAgg, String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal idTitoloCumulato) throws F3BException {

		Connection lConn = null;
		CircostanzaCumuloDAO lCirCumDao = null;
		CircostanzaCumuloModel lCirMod = new CircostanzaCumuloModel(aCircostanza);

		try {
			lConn = getDBTransaction();
			lCirCumDao = new CircostanzaCumuloDAO(lConn);
			lCirCumDao.setDAOFromModelForUpdate(aCircostanza);

			lCirCumDao.update();
			lCirCumDao.stop();

			// procedo con l'eventuale aggiornamento dei campi comuni
			// su tutte le circostanze del fascicolo
			if (flagAgg) {
				lCirCumDao.setCodBilanciamentoCircostanze(codBil);
				lCirCumDao.setNoteBilanciamento(noteBil);

				lCirCumDao.setCondizioneUpdateTitoloCumulato(idTitoloCumulato);

				lCirCumDao.update();
				lCirCumDao.stop();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.info("DAOException: " + ex);
			throw new F3BException(
					"CircostanzaCumuloController.ExModificaCircostanzaCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirCumDao);
			cleanup(lConn);
		}

		return lCirMod;
	}

	public void ExCancellaCircostanzaCumulo(CircostanzaCumuloModel aCircostanza) throws F3BException {

		Connection lConn = null;
		CircostanzaCumuloDAO lCirCumDao = null;

		try {
			lConn = getDBConnection();
			lCirCumDao = new CircostanzaCumuloDAO(lConn);

			String flagGiudizio = aCircostanza.getFlagGiudizioAbbreviato();
			String flagSentenza = aCircostanza.getFlagSentenzaApplicazPena();
			String codBil = aCircostanza.getCodBilanciamentoCircostanze();
			String noteBil = aCircostanza.getNoteBilanciamento();

			BigDecimal idTitolo = aCircostanza.getTitIdTitoloCumulato();

			boolean aggiornamento = false;
			// Art. 442 C.P.P. = Flag Giudizio Abbreviato
			if (aCircostanza.getArticolo().equals("442") && aCircostanza.getCodFonte().equals("25")) {
				flagGiudizio = "N";
				aggiornamento = true;
			}

			// Art. 444 C.P.P. = Flag Sentenza Applicazione Pena
			if (aCircostanza.getArticolo().equals("444") && aCircostanza.getCodFonte().equals("25")) {
				flagSentenza = "N";
				aggiornamento = true;
			}
			//
			// CANCELLAZIONE CIRCOSTANZA_CUMULO

			// Cancellazione Logica (Update) per Stato = ESTRATTO O MODIFICATO
			if (aCircostanza.getFlagStato().compareTo("E") == 0
					|| aCircostanza.getFlagStato().compareTo("M") == 0) {
				aCircostanza.setFlagStato("C");

				lCirCumDao.setDAOFromModelForUpdate(aCircostanza);
				lCirCumDao.update();
				lCirCumDao.stop();

			} else { // Cancellazione Fisica (Delete) per Stato = ISCRITTO a mano dopo l'estrazione
				lCirCumDao.setDAOFromModelForUpdate(aCircostanza);
				lCirCumDao.delete();
				lCirCumDao.stop();
			}

			// se la circostanza che viene eliminata è art. 442 Cpp o Art. 444 Cpp, la modifica
			// va estesa su tutte le restanti circostanze del Titolo Cumulato
			if (aggiornamento) {
				lCirCumDao.setFlagGiudizioAbbreviato(flagGiudizio);
				lCirCumDao.setFlagSentenzaApplicazPena(flagSentenza);
				lCirCumDao.setCodBilanciamentoCircostanze(codBil);
				lCirCumDao.setNoteBilanciamento(noteBil);
				lCirCumDao.setCondizioneUpdateTitoloCumulato(idTitolo);
				lCirCumDao.update();
			}

			commit(lConn);
		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			throw new F3BException("CircostanzaCumuloController.ExCancellaCircostanzaCumulo: " + daoEx);
		} finally {
			cleanup(lCirCumDao);
			cleanup(lConn);
		}
	}

	/**
	 * Il metodo inserisce una circostanza nella tabella "CIRCOSTANZE_CUMULO" Vengono passati i paramentri per
	 * effettuare l'aggiornamento dei campi comuni a tutte le circostanze relative a titoloCumulato
	 *
	 * @param Vector
	 *            aCircostanze
	 * @param boolean
	 *            aggiornamento
	 * @param boolean
	 *            flagGiudizio
	 * @param boolean
	 *            flagSentenza
	 * @param String
	 *            codBil
	 * @param String
	 *            noteBil
	 * @param BigDecimal
	 *            aIdTitoCum
	 *
	 */
	public void ExInserisciCircostanzeCumulo(Vector<CircostanzaCumuloModel> aCircostanze,
			boolean aggiornamento, String flagGiudizio, String flagSentenza, String codBil, String noteBil,
			BigDecimal aIdTitoCum) throws F3BException {

		Connection lConn = null;

		CircostanzaCumuloDAO lCirDao = null;
		CircostanzaCumuloModel lCirMod = null;

		try {
			lConn = getDBTransaction();

			lCirDao = new CircostanzaCumuloDAO(lConn);

			for (int i = 0; i < aCircostanze.size(); i++) {
				lCirMod = new CircostanzaCumuloModel(aCircostanze.get(i));
				// pezza sbrigativa per evitare che nell'Attributo COD_BILANCIAMENTO_CIRCOSTANZE ci finisca il
				// valore NULL
				if (lCirMod.getCodBilanciamentoCircostanze() == null
						|| lCirMod.getCodBilanciamentoCircostanze().equals("")) {
					lCirMod.setCodBilanciamentoCircostanze("-");
				}

				lCirDao.setDAOFromModel(lCirMod);
				// BigDecimal lKey = null;
				/* lKey = */lCirDao.insert();
				lCirDao.stop();
			}

			// procedo con l'eventuale aggiornamento dei campi comuni
			if (aggiornamento) {
				siesLogger.debug("dentro if aggiornamento ");
				lCirDao.setFlagGiudizioAbbreviato(flagGiudizio);
				lCirDao.setFlagSentenzaApplicazPena(flagSentenza);
				lCirDao.setCodBilanciamentoCircostanze(codBil);
				lCirDao.setNoteBilanciamento(noteBil);
				lCirDao.setCondizioneUpdateTitoloCumulato(aIdTitoCum);

				lCirDao.update();
			}

			commit(lConn);
		} catch (DAOException ex) {
			rollback(lConn);
			siesLogger.info("DAOException: " + ex);
			throw new F3BException(
					"CircostanzaCumuloController.ExInserisciCircostanzeCumulo: Non posso inserire: " + ex);
		} finally {
			cleanup(lCirDao);
			cleanup(lConn);
		}
	}

	public Vector<CircostanzaCumuloModel> ExRicercaCircostanzaCumulobyTitolo(BigDecimal aKeyTito)
			throws F3BException {

		Connection lConn = null;
		Vector<CircostanzaCumuloModel> lCircostanze = new Vector();
		CircostanzaCumuloSqlDAO lCirSqlDao = null;

		try {
			lConn = getDBConnection();
			lCirSqlDao = new CircostanzaCumuloSqlDAO(lConn);
			lCirSqlDao.ricercaCircostanzeCumuloByTitolo(aKeyTito);
			lCircostanze = new Vector(lCirSqlDao.getModels());

		} catch (DAOException daoEx) {
			siesLogger.info("DAOException: " + daoEx);
			throw new F3BException(
					"CircostanzaCumuloController.ExRicercaCircostanzaCumulobyTitolo: " + daoEx);
		} catch (Exception e) {
			siesLogger.info("Exception: " + e);
			throw new F3BException("CircostanzaCumuloController.ExRicercaCircostanzaCumulobyTitolo: " + e);
		} finally {
			cleanup(lCirSqlDao);
			cleanup(lConn);
		}

		return lCircostanze;
	}

	/**
	 * La Insert viene fatta in modalità 'NO SEQUENCE', senza utilizzare le sequnce. il valore della
	 * Primary_Key è già preimpostato; metodi usati nella funzione di presa in carico, per scaricare Tutti i
	 * dati del Fascicolo sulla nuova Base dati.
	 *
	 * @param
	 * @param
	 * @return
	 * @since MEV42 Cumulo Step2
	 */
	public String ExInserisciCircostanzeCumulateWithoutSequence(Vector<CircostanzaCumuloModel> VecCircoCumu,
			Connection lConn) throws F3BException {

		String EsitodiRitorno = "00000";
		CircostanzaCumuloDAO lCirCumDao = null;
		CircostanzaCumuloModel lCirCumMod = null;

		try {
			lCirCumDao = new CircostanzaCumuloDAO(lConn);

			if (VecCircoCumu != null && VecCircoCumu.size() > 0) {
				Iterator ItxC = VecCircoCumu.iterator();
				while (ItxC.hasNext()) {
					lCirCumMod = (CircostanzaCumuloModel) ItxC.next();
					if (lCirCumMod != null && lCirCumMod.getIdCircostanzaCumulo() != null) {
						try {
							lCirCumDao.setDAOFromModel(lCirCumMod);
							lCirCumDao.setWithoutSequence(true);
							lCirCumDao.insert();
							lCirCumDao.stop();
						} catch (DAOException daoEx) {
							if (daoEx.UNIQUE_CONSTRAINT_VIOLATED) {
								siesLogger.error("Circostanza Cumulo gia' presente...>"
										+ lCirCumMod.getIdCircostanzaCumulo() + "<");
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
			throw new F3BException(F3BException.USER_MESSAGE, "Impossibile inserire Circostanza Cumulo! ");
		} catch (Exception ex) {
			siesLogger.error("Exception: " + ex);
			throw new F3BException(
					"CircostanzaCumuloController.ExInserisciCircostanzeCumulateWithoutSequence: " + ex);
		} finally {
			cleanup(lCirCumDao);
		}

		return EsitodiRitorno;
	} // Chiude ExInserisciCircostanzeCumulateWithoutSequence

} // Chiude Controller