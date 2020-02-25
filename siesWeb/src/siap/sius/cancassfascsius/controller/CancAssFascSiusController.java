package siap.sius.cancassfascsius.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import siap.controller.SiapController;
import siap.sius.cancassfascsius.dao.CancAssFascSiusDAO;
import siap.sius.cancassfascsius.dao.CancAssFascSiusSqlDAO;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;

/**
 * <p>
 * Title: CancAssFascSiusController
 * </p>
 * <p>
 * Description: Classe Controller per CancAssFascSius
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
public class CancAssFascSiusController extends SiapController implements ICancAssFascSius {

	/**
	 * La funzione crea una nuova istanza nella tabella CANC_ASS_FSAC_SIUS. Poichè un Fascicolo SIUS può
	 * essere associato ad una una sola Cancelleria Assegnataria oltre all'inserimento della nuova istanza
	 * relazione tra Cancelleria e Fascicolo viene anche effettuata la "chiusura" di una eventuale relazione
	 * precedente attiva. La "chiusura" consiste nella valorizzare della Data Di Chiusura dell'istanza
	 * precedente con la stessa Data Di Inizio della nuova che si sta inserendo. In questa maniera tali
	 * istanze risultano essere storicizzate.
	 * 
	 * @param aCancAssFascSius
	 * @return
	 * @throws F3BException
	 */
	public CancAssFascSiusModel ExInserisciCancAssFascSius(CancAssFascSiusModel aCancAssFascSius)
			throws F3BException {

		Connection lConn = null;
		CancAssFascSiusDAO lCanDao = null;
		CancAssFascSiusModel lCanMod = null;

		try {
			// Inizializzazioni
			lConn = getDBConnection();
			lCanMod = new CancAssFascSiusModel(aCancAssFascSius);
			lCanDao = new CancAssFascSiusDAO(lConn);

			// Chiusura del record attivo ovvero valorizzazione della Data di Fine
			lCanDao.setDataFine(aCancAssFascSius.getDataInizio());
			lCanDao.setCodOperatoreAggiornamento(aCancAssFascSius.getCodOperatoreInserimento());
			lCanDao.setDataAggiornamento(aCancAssFascSius.getDataInserimento());
			lCanDao.setCodUfficioAggiornamento(aCancAssFascSius.getCodUfficioInserimento());
			lCanDao.setCondizioneUpdate(aCancAssFascSius.getFasSiusIdFascicoloSius());
			lCanDao.update();
			lCanDao.stop();

			// inserimento del nuovo record
			lCanDao.setDAOFromModel(aCancAssFascSius);
			lCanDao.insert();
			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException("CancAssFascSiusController.ExInserisci: Non posso inserire: " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCanMod;
	}

	/**
	 * La funzione effettua una ricerca di associazioni Fascicolo SIUS - Cancelleria Assegnataria. Il filtro
	 * di ricerca da utilizzare viene passato attraverso il CancAssFascSiusModel. L'elenco di istanze
	 * risultato della ricerca viene restituito nella forma di Vector di CancAssFascSiusModel
	 * 
	 * @param aCancAssFascSius
	 *            : CancAssFascSiusModel
	 * @return Elenco : Vector
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ExRicercaCancAssFascSius(CancAssFascSiusModel aCancAssFascSius) throws F3BException {

		// Connessione
		Connection lConn = null;
		// Elenco risultato della ricerca
		Vector lElenco = new Vector();
		// SqlDAO utilizzato per l'accesso al DB
		CancAssFascSiusSqlDAO lCanDao = null;

		try {
			// Si accede alla connessione
			lConn = getDBConnection();
			// Si istanzia il DAO
			lCanDao = new CancAssFascSiusSqlDAO(lConn);
			// Viene attivata la ricerca
			lCanDao.ricercaCancAssFascSius(aCancAssFascSius);
			lElenco = new Vector(lCanDao.getModels());
			/*
			 * if ( lElenco.size() == 0 ) { throw new
			 * F3BException(F3BException.USER_MESSAGE,"Nessun Elemento trovato"); }
			 */
		} catch (DAOException ex) {
			throw new F3BException(
					"CancAssFascSiusController.ExRicercaCancAssFascSius: Non posso leggere  : " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lElenco;
	}

	/**
	 * La funzione effettua la ricerca dell' istanza attiva Cancelleria Assegnataria _ Fascicolo SIUS.
	 * 
	 * @param aIdFascicolo
	 * @return CancAssFascSiusModel
	 * @throws F3BException
	 */
	public CancAssFascSiusModel ExRicercaCancAssFascSiusAttiva(BigDecimal aIdFascicolo) throws F3BException {
		// CancAssFascSiusModel risultato della ricerca
		CancAssFascSiusModel lCanMod = null;
		// Connessione
		Connection lConn = null;
		// SqlDAO utilizzato per l'accesso al DB
		CancAssFascSiusSqlDAO lCanDao = null;

		try {
			// Si accede alla connessione
			lConn = getDBConnection();
			// Si istanzia il DAO
			lCanDao = new CancAssFascSiusSqlDAO(lConn);
			// Viene attivata la ricerca
			lCanDao.ricercaCancAssFascSiusAttivaXFas(aIdFascicolo);
			lCanMod = (CancAssFascSiusModel) lCanDao.getModelByKey();
		} catch (SQLException sqe) {
			throw new F3BException(
					"CancAssFascSiusController.ExRicercaCancAssFascSius: Non posso leggere  : " + sqe);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCanMod;
	}

	/**
	 * Cancellazione della relazione Cancelleria Assegnataria - Fascicolo SIUS. La cancellazione comporta
	 * l'eliminazione della relazione attiva per il Fascicolo specificato e la riattivazione dell'eventuale
	 * relazione precedentemente chiusa.
	 * 
	 * @param aCancAssFascSius
	 * @throws F3BException
	 */
	public void ExCancellaCancAssFascSius(CancAssFascSiusModel aCancAssFascSius) throws F3BException {
		Connection lConn = null;
		CancAssFascSiusDAO lCanDao = null;

		try {
			lConn = getDBConnection();
			lCanDao = new CancAssFascSiusDAO(lConn);

			// cancellazione del record attivo
			lCanDao.setCondizioneUpdate(aCancAssFascSius.getFasSiusIdFascicoloSius());
			lCanDao.delete();
			lCanDao.stop();

			// riattivazione del vecchio record
			lCanDao.setDataFine(null);
			lCanDao.setCodOperatoreAggiornamento(aCancAssFascSius.getCodOperatoreAggiornamento());
			lCanDao.setDataAggiornamento(aCancAssFascSius.getDataAggiornamento());
			lCanDao.setCodUfficioAggiornamento(aCancAssFascSius.getCodUfficioAggiornamento());
			lCanDao.setCondizioneRiattivazione(aCancAssFascSius.getFasSiusIdFascicoloSius());
			lCanDao.update();
			lCanDao.stop();

			commit(lConn);
		} catch (Exception ex) {
			rollback(lConn);
			throw new F3BException(
					"CancAssFascSiusController.ExCancellaCancAssFascSius: Non posso cancellare : " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
	}

}