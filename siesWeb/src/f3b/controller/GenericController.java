package f3b.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import f3b.dao.GenericDAO;
import f3b.util.F3BException;
import f3b.util.F3BProperties;

/**
 * <p>
 * Title: GenericController
 * </p>
 * <p>
 * Description: Superclasse del controller di F3B, tale classe deve essere
 * </p>
 * ererditata da tutte le classi controller del progetto.
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class GenericController {

	/**
	 * Metodo che effettua il rollback di una transazione al DBase.
	 *
	 * @param aConn
	 *            Connessione SQL
	 * @throws F3BException
	 *             propaga errore di eccezione.
	 */
	protected void rollback(Connection aConn) throws F3BException {

		try {
			if (aConn != null)
				aConn.rollback();
		} catch (SQLException ex) {
			throw new F3BException(ex.getMessage());
		}
	}

	/**
	 * Effettua la commit di una trasazione di dati al DBASE
	 *
	 * @param aConn
	 *            Connessione al DBASE
	 * @throws F3BException
	 *             Propaga l'errore di eccezione.
	 */
	protected void commit(Connection aConn) throws F3BException {

		try {
			if (aConn != null)
				aConn.commit();
		} catch (SQLException ex) {
			throw new F3BException(ex.getMessage());
		}
	}

	// STUB : 20030524 - Da Riscrivere .
	/**
	 * Ritorna la connessione dal POOL impostando l'autocommit a <code>false</code>.
	 *
	 * @return la connessione al DBase prelevata dal pool.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected static synchronized Connection getDBConnection() throws F3BException {

		try {
			Context lInitialCtx = new InitialContext();
			DataSource lDataSource = (DataSource) lInitialCtx
					.lookup(F3BProperties.getProperty("datasource.ctx"));

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

	// STUB: 20030524 - Da riscrivere.
	/**
	 * Ritorna una connessione prelevata dal POOL, impostanto il massimo livello di TRANSAZIONALITA'.
	 *
	 * @return la connessione al DBase in transazione.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected static synchronized Connection getDBTransaction() throws F3BException {

		try {
			Context lInitialCtx = new InitialContext();
			DataSource lDataSource = (DataSource) lInitialCtx
					.lookup(F3BProperties.getProperty("datasource.ctx"));

			Connection lConn = lDataSource.getConnection();

			// Imposta il livello di TRANSAZIONALITA'.
			// lConn.setTransactionIsolation(lConn.TRANSACTION_SERIALIZABLE);
			lConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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

	/**
	 * Ritorna il data source.
	 *
	 * @return oggetto <code>DataSource</code>.
	 * @throws NamingException
	 *             propaga l'errore di eccezione di Naming.
	 * @throws F3BException
	 *             propaga l'errore di eccezione di F3B.
	 */
	protected static DataSource getDataSource() throws NamingException, F3BException {

		Context lInitialCtx = new InitialContext();
		Context lEnvCtx = (Context) lInitialCtx.lookup(F3BProperties.getProperty("ctx.env"));
		DataSource lDataSource = (DataSource) lEnvCtx.lookup(F3BProperties.getProperty("datasource.ctx"));

		return lDataSource;
	}

	/**
	 * Effettua il rilascio della connessione al DBASE.
	 *
	 * @param aConn
	 *            Connessione al DB da rilasciare.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected void cleanup(Connection aConn) throws F3BException {

		try {
			if (aConn != null && !aConn.isClosed())
				aConn.close();
		} catch (Exception ex) {
			throw new F3BException(ex.getMessage());
		}
	}

	/**
	 * Effettua la chisura di uno <code>Statement</code> SQL.
	 *
	 * @param aStat
	 *            Oggetto <code>Statement</code> SQL.
	 * @throws F3BException
	 *             propga l'errore di eccezione.
	 */
	protected void cleanup(Statement aStat) throws F3BException {

		try {
			if (aStat != null)
				aStat.close();
		} catch (Exception ex) {
			throw new F3BException(ex.getMessage());
		}
	}

	/**
	 * Effettua la chiusura di un <code>PreparedStatement</code> SQL.
	 *
	 * @param aPStat
	 *            Oggetto <code>PreparedStatement</code> SQL:
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected void cleanup(PreparedStatement aPStat) throws F3BException {

		try {
			if (aPStat != null)
				aPStat.close();
		} catch (Exception ex) {
			throw new F3BException(ex.getMessage());
		}
	}

	/**
	 * Effettua lo stop di un oggetto DAO.
	 *
	 * @param aDao
	 *            Oggetto della classe <code>GenericDAO</code>
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 */
	protected void cleanup(GenericDAO aDao) throws F3BException {

		try {
			if (aDao != null)
				aDao.stop();
		} catch (Exception ex) {
			throw new F3BException(ex.getMessage());
		}
	}

}