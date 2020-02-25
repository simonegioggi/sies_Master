package siap.sius.collaboratore.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import siap.sius.collaboratore.model.CollaboratoreModel;
import f3b.dao.DAOException;
import f3b.dao.StoreProcedureDAO;
import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: getCollabCurStProDAO
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * La classe costituisce l'interfaccia a 2 funzioni rese disponibili dal package COLLA.COLL.
 * <p>
 * Le due funzioni sono COLLA.COLL.getCollabCur() e COLLA.COLL.getCollabById().
 * </p>
 * <p>
 * Schema : COLLA
 * </p>
 * <p>
 * Nome del package : COLL
 * </p>
 * <p>
 * procedure getCollabCur ( par_ufficio IN VARCHAR2, par_id IN number, par_cur IN OUT COLLAB_CUR)
 * </p>
 * <p>
 * procedure getCollabById ( par_id IN number, par_cur IN OUT COLLAB_CUR);
 * </p>
 * <p>
 * Entrambe le procedure restituiscono il risultato attraverso un REF CURSOR, poichè tale tipo è gestito solo
 * dai driver Oracle questa classe e le funzioni gestite non sono portabili su JDBS non Oracle.
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class getCollabCurStProDAO extends StoreProcedureDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Ridefinizione del CallableStatement
	// protected OracleCallableStatement mCallStat = null;
	// Ridefinizione del ResultSet
	// protected OracleResultSet mRes = null;

	public getCollabCurStProDAO(Connection lConn) {
		super(lConn);
	}

	/**
	 * Costruttore per creare l'interfaccia alla procedure : COLLA.COLL.getCollabCur.
	 * 
	 * @param aConn
	 *            : connessione;
	 * @param aCodUfficio
	 *            : codice ufficio di riferimento;
	 * @param aIdFascicoloSius
	 *            : ID Fascicolo SIUS.
	 */
	public getCollabCurStProDAO(Connection aConn, String aCodUfficio, BigDecimal aIdFascicoloSius) {
		super(aConn);

		// Nome della Stored Procedure
		setStoreProcedure("COLLA.COLL.getCollabCur");

		// Primo parametro di input
		setArgInput("COD_UFFICIO", STRING);
		setArgInputPosition("COD_UFFICIO", 1);

		// Secondo parametro di input
		setArgInput("ID", BIG_DECIMAL);
		setArgInputPosition("ID", 2);

		// Tipo di output gestito da OracleCallableStatement
		setArgOutput("LISTA", OracleTypes.CURSOR);
		setArgOutputPosition("LISTA", 3);

		// Valorizzazione dei parametri di Input
		setString("COD_UFFICIO", aCodUfficio);
		setBigDecimal("ID", aIdFascicoloSius);
	}

	/**
	 * Costruttore per creare l'interfaccia alla procedure : COLLA.COLL.getCollabById.
	 * 
	 * @param aConn
	 *            : connessione;
	 * @param aIdCollaboratore
	 *            : ID del record Collaboratore.
	 */
	public getCollabCurStProDAO(Connection aConn, BigDecimal aIdCollaboratore) {
		super(aConn);
		// Nome della Stored Procedure
		setStoreProcedure("COLLA.COLL.getCollabById");

		// Parametro di input
		setArgInput("ID", BIG_DECIMAL);
		setArgInputPosition("ID", 1);

		// Tipo di output gestito da OracleCallableStatement
		setArgOutput("LISTA", OracleTypes.CURSOR);
		setArgOutputPosition("LISTA", 2);

		// Valorizzazione del parametro di Input
		setBigDecimal("ID", aIdCollaboratore);
	}

	/**
	 * La funzione è analoga alla GenericDAO.getModels(). Chiama la execute() per eseguuire la stored
	 * procedure di ricerca che è stata impostata dal costruttore; effettua un casting del ResouseSet ad
	 * OracleResultSet e lo associa al CURSOR risultato della stored procedure; esegue quindi il ciclo di
	 * fetch sul cursore e ritorna l'elenco di CollaboratoreModel costruiti attraverso l'uso della getModel().
	 * 
	 * @return Collection : Elenco di CollaboratoreModel risultato della ricerca.
	 * @throws Exception
	 */
	public Collection getModelsByCur() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getModelsByCur : inizio");

		ArrayList lAL = null;

		try {
			execute();

			mCallStat = (OracleCallableStatement) super.mCallStat;
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("effettuato casting CallableStatement -> OracleCallableStatement ");

			if (mCallStat == null)
				throw new DAOException("OracleCallableStatement NULL !!");
			mRs = (OracleResultSet) ((OracleCallableStatement) (mCallStat))
					.getCursor(((Integer) mArgOutputsPosition.get("LISTA")).intValue());

			// mRs = (OracleResultSet) (mCallStat.getCursor( ( (Integer) mArgOutputsPosition.get("LISTA")
			// ).intValue()));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("effettuato casting ResultSet -> OracleResultSet ");

			if (mRs == null)
				throw new DAOException("OracleResultSet NULL !!");
			lAL = new ArrayList();
			int i = 1;
			while (mRs.next()) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("sono nel ciclo sul cursore: " + i++);
				lAL.add(getModel());
			}
			mRs.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException Message: " + sqlEx.getMessage());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SQLException ErrorCode: " + sqlEx.getErrorCode());
			throw new DAOException(sqlEx);
		} catch (Exception eEx) {
			eEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Exception Message: " + eEx.getMessage());
			throw new DAOException(eEx.toString());
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".getModelsByCur : fine");

		return lAL;
	}

	/**
	 * restituisce CollaboratoreModel risultato della fetch sul resourceset.
	 */
	public GenericModel getModel() throws DAOException {
		CollaboratoreModel aModel = new CollaboratoreModel();
		aModel.setIdCollaboratore(getBigDecimal("id"));
		aModel.setDataInizio(getDate("data_inizio"));
		aModel.setDataFine(getDate("data_fine"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data 1: " + DateUtils.getDateToString(aModel.getDataInizio(), "dd/MM/yyyy"));
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Data 2: " + DateUtils.getDateToString(aModel.getDataFine(), "dd/MM/yyyy"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("CollaboratoreModel: " + aModel);
		return aModel;
	}

}