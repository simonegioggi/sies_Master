package siap.sius.collaboratore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import siap.controller.SiapController;
import siap.sius.collaboratore.dao.CollaboratoreSqlDAO;
import siap.sius.collaboratore.dao.aggiornaCollabStProDAO;
import siap.sius.collaboratore.dao.delCollabStProDAO;
import siap.sius.collaboratore.dao.getCollabCurStProDAO;
import siap.sius.collaboratore.dao.isCollaboratoreStProDAO;
import siap.sius.collaboratore.dao.setCollaboratoreStProDAO;
import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.dao.DAOException;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;


/**
 * <p>
 * Title: CollaboratoreController
 * </p>
 * <p>
 * Description: Classe Controller per Notifica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 3.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CollaboratoreController extends SiapController implements ICollaboratore {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * La funzione verifica se al Fascicolo Sius individuato dal suo ID e dal codice dell'ufficio di
	 * competenza è associato ad un collaboratore di giustizia.
	 * 
	 * @param BigDecimal
	 *            aIdFascicoloSius : ID del Fascicolo;
	 * @param String
	 *            aCodUfficio : ufficio di competenza del Fascicolo.
	 * @return boolean : true/false.
	 * @throws F3BException
	 */
	public boolean ExIsCollaboratore(BigDecimal aIdFascicoloSius, String aCodUfficio) throws F3BException {
		Connection lConn = null;
		boolean lRet = false;
		int lCont = 0;
		isCollaboratoreStProDAO lDao = null;
		try {
			lConn = getDBConnection();

			lDao = new isCollaboratoreStProDAO(lConn);
			lDao.setCodUfficio(aCodUfficio);
			lDao.setID(aIdFascicoloSius);
			lDao.execute();
			lCont = lDao.getReturn();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ExIsCollaboratore Cont :" + lCont);

			if (lCont > 0)
				lRet = true;
		} catch (Exception daoEx) {
			throw new F3BException("ExIsCollaboratore: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lRet;
	}

	/**
	 * La funzione va richiamata prima di chiamare le altre funzioni all'interno di questo Controller; questo
	 * perchè effettua un controllo sull'esistenza del package di interfaccia per la Gestione del
	 * Collaboratore di Giustizia.
	 */
	public boolean ExIsPackage() throws F3BException {
		Connection lConn = null;
		boolean lRet = false;
		isCollaboratoreStProDAO lDao = null;
		try {
			lConn = getDBConnection();

			lDao = new isCollaboratoreStProDAO(lConn);
			lRet = lDao.esistePackage();
		} catch (Exception daoEx) {
			// STUB : per il momento non viene propagata l'eccezione
			// throw new F3BException( "ExIsPackage: " + daoEx);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.info("Eccezione nel CollaboratoreController.ExIsPackage() : " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lRet;

	}

	/**
	 * Funzione di ricerca di CollaboratoreModel legati ad uno stesso Fascicolo SIUS.
	 * 
	 * @param BigDecimal
	 *            aIdFascicoloSius : ID del Fascicolo;
	 * @param String
	 *            aCodUfficio : ufficio di competenza del Fascicolo.
	 * @return Vector : elenco di CollaboratoreModel individuati.
	 * @throws F3BException. La
	 *             funzione non viene utilizzata perchè utilizza un DAO che necessita di un driver che
	 *             supporti le "Estensioni Oracle".
	 */
	public Vector ExGetCollaboratore(BigDecimal aIdFascicoloSius, String aCodUfficio) throws F3BException {
		Vector lLista = null;
		// OracleConnection lConn = null;
		Connection lConn = null;
		getCollabCurStProDAO lDao = null;

		try {
			lConn = getDBConnection();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getInfoDriver(lConn));

			lDao = new getCollabCurStProDAO(lConn, aCodUfficio, aIdFascicoloSius);

			lLista = new Vector(lDao.getModelsByCur());
		} catch (Exception daoEx) {
			throw new F3BException("ExGetCollaboratore: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lLista;
	}

	/**
	 * Funzione di ricerca di un CollaboratoreModel. Ricerca il record Collaboratore individuato univocamente
	 * dal suo ID.
	 * 
	 * @param BigDecimal
	 *            aIdCollaboratore : ID del Collaboratore;
	 * @return CollaboratoreModel : record individuato.
	 * @throws F3BException. La
	 *             funzione non viene utilizzata perchè utilizza un DAO che necessita di un driver che
	 *             supporti le "Estensioni Oracle".
	 * 
	 */
	public CollaboratoreModel ExGetCollaboratoreById(BigDecimal aIdCollaboratore) throws F3BException {
		CollaboratoreModel lCollaboratore = null;
		Vector lLista = null;
		Connection lConn = null;
		getCollabCurStProDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new getCollabCurStProDAO(lConn, aIdCollaboratore);
			lLista = new Vector(lDao.getModelsByCur());

			// Viene estratto dall'elenco il primo ed unico record presente
			if (lLista != null && lLista.size() > 0) {
				lCollaboratore = (CollaboratoreModel) lLista.get(0);
			} else
				throw new F3BException("Record non trovato !");
		} catch (Exception daoEx) {
			throw new F3BException("ExGetCollaboratoreById: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return lCollaboratore;
	}

	/**
	 * Funzione di inserimento di un Collaboratore di Giustizia legato ad un Fascicolo SIUS.
	 * 
	 * @param CollaboratoreModel
	 *            aCollaboratore: model contenente i dati del nuovo record da inserire.
	 * @throws F3BException.
	 */
	public void ExInserisciCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException {
		Connection lConn = null;
		setCollaboratoreStProDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new setCollaboratoreStProDAO(lConn, aCollaboratore);
			lDao.execute();
			commit(lConn);
		} catch (Exception daoEx) {
			rollback(lConn);
			throw new F3BException("ExInserisciCollaboratore: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Funzione di modifica di un record Collaboratore di Giustizia legato ad un Fascicolo SIUS.
	 * 
	 * @param CollaboratoreModel
	 *            aCollaboratore: model contenente i dati da aggiornare insieme all'ID che individua il record
	 *            da modificare.
	 * @throws F3BException.
	 */
	public void ExAggiornaCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException {
		Connection lConn = null;
		aggiornaCollabStProDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new aggiornaCollabStProDAO(lConn, aCollaboratore);
			lDao.execute();
			commit(lConn);
		} catch (Exception daoEx) {
			rollback(lConn);
			throw new F3BException("ExAggiornaCollaboratore: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return;
	}

	/**
	 * Funzione di cancellazione di un record Collaboratore di Giustizia legato ad un Fascicolo SIUS.
	 * 
	 * @param BigDecimal
	 *            aIdCollaboratore : ID del record da cancellare.
	 * @throws F3BException.
	 */
	public void ExCancellaCollaboratore(BigDecimal aIdCollaboratore) throws F3BException {
		Connection lConn = null;
		delCollabStProDAO lDao = null;
		try {
			lConn = getDBConnection();
			lDao = new delCollabStProDAO(lConn, aIdCollaboratore);
			lDao.execute();
			commit(lConn);
		} catch (Exception daoEx) {
			rollback(lConn);
			throw new F3BException("ExCancellaCollaboratore: " + daoEx);
		} finally {
			cleanup(lDao);
			cleanup(lConn);
		}
		return;
	}

	// Ricava il nome del driver dalla connessione al DataSource
	private String getInfoDriver(Connection aConn) throws Exception {
		String lDriverName = "Driver: ";
		DatabaseMetaData lDMD = aConn.getMetaData();
		lDriverName += lDMD.getDriverName() + " ver.  " + lDMD.getDriverVersion();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Driver Minor ver :" + lDMD.getDriverMinorVersion() + "Driver Max ver :"
				+ lDMD.getDriverMajorVersion());
		ResultSet lRes = lDMD.getUDTs(null, "COLLA", null, null);
		if (lRes == null)
			throw new DAOException("ResultSet getUDTs null !!");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("richiamato getUDTs");
		while (lRes.next()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ciclo sul getUDTs: ");
			for (int i = 1; i < 8; i++)
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("[" + i + "] -> " + lRes.getString(i));
		}
		lRes.close();

		lRes = lDMD.getCatalogs();
		if (lRes == null)
			throw new DAOException("ResultSet getCatalogs null !!");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("richiamato getCatalogs");
		while (lRes.next()) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ciclo sul getCatalogs ");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ciclo sul getCatalogs -> " + lRes.getString(1));
		}
		lRes.close();

		return lDriverName;
	}

	/**
	 * La funzione restituisce una connessione utilizzando il Datasouse di nome "jdbc/siap2" definito sul
	 * server.xml.
	 * 
	 * @return Connection
	 * @throws F3BException
	 */
	protected static synchronized Connection getDBConnection2() throws F3BException {
		try {
			Context lInitialCtx = new InitialContext();
			// Context envCtx = (Context)initCtx.lookup("java:comp/env");
			Context lEnvCtx = (Context) lInitialCtx.lookup(F3BProperties.getProperty("ctx.env"));
			// DataSource ds = (DataSource)envCtx.lookup("jdbc/siap");
			DataSource lDataSource = (DataSource) lEnvCtx.lookup("jdbc/siap2");

			Connection lConn = lDataSource.getConnection();
			lConn.setAutoCommit(false);

			return lConn;
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			throw new F3BException(sqlex.getMessage());
		} catch (NamingException ex) {
			ex.printStackTrace();
			throw new F3BException(ex.getMessage());
		}
	}

//	private void mapTipo(Connection aConn) throws Exception {
//		// tentativo : mappare il tipo
//		java.util.Map map = aConn.getTypeMap();
//		map.put("COLLA.COLL.ARRLISTA", Class.forName("siap.sius.collaboratore.model.CollaboratoreModel"));
//		map.put("COLLA.COLL.ARRLISTA", Class.forName("siap.sius.collaboratore.model.CollaboratoreModel"));
//		aConn.setTypeMap(map);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
//		// LogF3B.getLogger()
//		siesLogger.debug("richiamato setTypeMap");
//	}

	/**
	 * Funzione di Ricerca record su tabella COLLA.COLLABORATORE.
	 * 
	 * @param CollaboratoreModel
	 *            : filtro di ricerca
	 * @return Vector : elenco risultato
	 */
	public Vector ExRicercaCollaboratore(CollaboratoreModel aCollaboratore) throws F3BException {
		// Connessione
		Connection lConn = null;
		// Elenco risultato della ricerca
		Vector lElenco = null;
		// SqlDAO utilizzato per l'accesso al DB
		CollaboratoreSqlDAO lCanDao = null;

		try {
			// Si accede alla connessione
			lConn = getDBConnection();
			// Si istanzia il DAO
			lCanDao = new CollaboratoreSqlDAO(lConn);
			// Viene attivata la ricerca
			lCanDao.ricercaCollaboratore(aCollaboratore);
			lElenco = new Vector(lCanDao.getModels());
		} catch (DAOException ex) {
			throw new F3BException("CollaboratoreController.ExRicercaCollaboratore: Non posso leggere  : "
					+ ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lElenco;
	}

	/**
	 * Funzione di Ricerca di un record su tabella COLLA.COLLABORATORE individuato univocamente dal suo ID.
	 * 
	 * @param BigDecimal
	 *            : ID chiave
	 * @return CollaboratoreModel : risultato
	 */
	public CollaboratoreModel ExRicercaCollaboratoreById(BigDecimal aId) throws F3BException {
		// Model usato come parametro di scambio verso il DAO e come risultato
		CollaboratoreModel lCollaboratore = new CollaboratoreModel();
		// Connessione
		Connection lConn = null;
		// SqlDAO utilizzato per l'accesso al DB
		CollaboratoreSqlDAO lCanDao = null;

		try {
			lCollaboratore.setIdCollaboratore(aId);
			// Si accede alla connessione
			lConn = getDBConnection();
			// Si istanzia il DAO
			lCanDao = new CollaboratoreSqlDAO(lConn);
			// Viene attivata la ricerca
			lCanDao.ricercaCollaboratore(lCollaboratore);
			lCollaboratore = (CollaboratoreModel) lCanDao.getModelByKey();
			if (lCollaboratore == null)
				throw new F3BException(F3BException.USER_MESSAGE, "Record non trovato !");
		} catch (DAOException ex) {
			throw new F3BException(
					"CollaboratoreController.ExRicercaCollaboratoreById: Non posso leggere  : " + ex);
		} finally {
			cleanup(lCanDao);
			cleanup(lConn);
		}
		return lCollaboratore;
	}

}