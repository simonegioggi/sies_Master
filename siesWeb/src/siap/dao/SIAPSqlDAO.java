package siap.dao;

import java.math.BigDecimal;
import java.sql.Connection;

import f3b.dao.DAOException;
import f3b.dao.SqlOracleDAO;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class SIAPSqlDAO extends SqlOracleDAO {

	/**
	 * Costruttore di classe con la connessione al db come parametro.
	 * <p>
	 *
	 * @param aCon
	 *            Connessione la Dbase.
	 */
	public SIAPSqlDAO(Connection aCon) {
		super(aCon);
	}

	/**
	 * Metodo sovrascritto per limitare l'elenco di occorrenze a max 200 elementi.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	/*
	 * public void start() throws f3b.dao.DAOException { start(1,200); }
	 */
	/**
	 * Metodo sovrascritto, ritorna l'insieme di model, per tutte le occorrenze. Max 200.
	 * <p>
	 *
	 * @return l'insieme di models.
	 * @throws DAOException
	 *             propaga l'errore di ecceione.
	 */
	/*
	 * public java.util.Collection getModels() throws f3b.dao.DAOException { return getModels(1,200); }
	 */

	/**
	 * Il metodo ritorna il n.ro di record restituiti da una select già precedentemente impostata.
	 *
	 */
	public BigDecimal getNumRowsSelected() throws DAOException {
		BigDecimal lNum = null;
		String lQuery = " SELECT COUNT(*) NUM FROM ( ";
		setStatement(lQuery + mStatement + ")");
		start();
		next();
		lNum = getBigDecimal("NUM");
		stop();
		return lNum;
	}

	/**
	 * Il metodo a partire dal numero di pagina calcola il range di record da passare alla funzione start(int,
	 * int) per inizializzare l'operazine di fetch.
	 *
	 * @param aPageNum
	 * @throws Exception
	 */
	public void startPage(int aPageNum) throws F3BException, DAOException {
		if (aPageNum > 0) {
			int lFirstRec = (aPageNum - 1) * IWebConstants.RESULT_PER_PAGE + 1;
			int lLastRec = aPageNum * IWebConstants.RESULT_PER_PAGE;
			start(lFirstRec, lLastRec);
		} else
			throw new F3BException(F3BException.USER_MESSAGE, "Numero pagina scorretto-> " + aPageNum);
	}

	/**
	 * Il metodo trasforma la select già impostata nello Statement in una select paginata.
	 *
	 * @param aPageNum
	 * @throws F3BException
	 */
	// @deprecated MEV_6 non più utilizzato: usare "convertSelectToNewPagedSelect"
	// private void convertSelectToPagedSelect(int aPageNum) throws F3BException {
	// if (aPageNum > 0) {
	// // calcolo range record corrispondente al numero di pagina
	// int lFirstRec = (aPageNum - 1) * IWebConstants.RESULT_PER_PAGE + 1;
	// int lLastRec = aPageNum * IWebConstants.RESULT_PER_PAGE;
	//
	// // composizione della query paginata
	// String lQueryStart = " SELECT * FROM (SELECT INNER.* , ROWNUM rn FROM ( ";
	// String lQueryEnd = " ) INNER ) WHERE rn between " + lFirstRec + " AND " + lLastRec;
	// setStatement(lQueryStart + mStatement + lQueryEnd);
	// } else
	// throw new F3BException(F3BException.USER_MESSAGE, "Numero pagina scorretto-> " + aPageNum);
	// return;
	// }

	/**
	 * Il metodo a partire dal numero di pagina riscrive la select in modo da ritornare la ricerca su una sola
	 * pagina. Il metodo è alternativo al metodo startPage() perchè restituisce lo stesso risultato ma con un
	 * metodo diverso. La prima startPage() lascia inalterata la select ma agisce sulla fetch dei record
	 * utili. startPage1(), invece, genera una nuova select innestata per ricavare i record utili senza
	 * alterare la fetch. Questa ultima potrebbe essere più efficiente ( da verificare ??) perchè non
	 * sovraccarica il Resouse Set.
	 *
	 * @param aPageNum
	 * @throws F3BException,DAOException
	 */
	// @deprecated MEV_6 non più utilizzato: usare "startNewPagination"
	// public void startPage1(int aPageNum) throws F3BException, DAOException {
	// convertSelectToPagedSelect(aPageNum);
	// start();
	// return;
	// }

	/*
	 * ISSUE MEV : modificata la paginazione
	 * Numero MEV : 6
	 * Autore : Gioggi
	 * Data : 18 gen 2021
	 * Branch : MEV_6
	 */
	public void startNewPagination(int aPageNum) throws F3BException, DAOException {

		convertSelectToNewPaginedSelect(aPageNum);
		start();
	}

	private void convertSelectToNewPaginedSelect(int aPageNum) throws F3BException {

		if (aPageNum > 0) {
			// calcolo range record corrispondente al numero di pagina
			int lFirstRec = (aPageNum - 1) * IWebConstants.RESULT_PER_PAGE + 1;
			int lLastRec = aPageNum * IWebConstants.RESULT_PER_PAGE;

			// composizione della query paginata
			String lQueryStart = "select * from (select rownum rnum, x.* from (";
			String lQueryEnd = ") x where rownum <= " + lLastRec + ") where rnum >= " + lFirstRec;
			setStatement(lQueryStart + mStatement + lQueryEnd);
		} else
			throw new F3BException(F3BException.USER_MESSAGE, "Numero pagina scorretto -> " + aPageNum);
	}

	public String convertStatementToNewPaginedStatement(int aPageNum, String strQuery) {

		String s = new String();
		int lFirstRec = (aPageNum - 1) * IWebConstants.RESULT_PER_PAGE + 1;
		int lLastRec = aPageNum * IWebConstants.RESULT_PER_PAGE;
		String lQueryStart = "select * from (select rownum rnum, x.* from (";
		String lQueryEnd = ") x where rownum <= " + lLastRec + ") where rnum >= " + lFirstRec;
		s = lQueryStart + strQuery + lQueryEnd;
		return s;
	}

	/**
	 * findColumn
	 *
	 * @param aValue
	 * @return
	 */
	public boolean findColumn(String aValue) {
		try {
			mRs.findColumn(aValue);
		} catch (Exception sqex) {
			return false;
		}
		return true;
	}
	// ***** FINE INTERVENTO MEV_6 *****//

}