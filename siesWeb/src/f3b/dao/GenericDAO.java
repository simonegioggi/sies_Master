package f3b.dao;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.JdbcTrackerUtil;
import f3b.util.StringUtils;

/**
 * <p>
 * Title: GenericDAO
 * </p>
 * <p>
 * Description: E' la classe padre di tutti gli oggetti DAO, infatti essa ha la responsabilità di dialogare
 * con il DBASE e la gestione di accesso ai dati del ResultSet
 * </p>
 * .
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Tipi di oggetti gestiti.
	protected static final int STRING = 1;
	protected static final int INT = 2;
	protected static final int DATE = 3;
	protected static final int BIG_DECIMAL = 4;
	protected static final int TBLOB = 5;
	protected static final int CLOB = 6;
	protected static final int BINARY_STREAM = 7;
	protected static final int INTEGER = 8;

	protected Connection mCon = null;
	protected PreparedStatement mPs = null;
	protected ResultSet mRs = null;
	protected String mStatement = null;
	protected int mActualRec = 0;
	protected int mFirstRec = 0;
	protected int mLastRec = 0;
	protected List<PreparedStatement> oldPs = new ArrayList<>();
	protected List<ResultSet> oldRs = new ArrayList<>();

	/**
	 * Costruttore con parametro. Come attributo viene passata la connessione al Dbase.
	 *
	 * @param aCon
	 *            Connessione al dbase.
	 */
	public GenericDAO(Connection aCon) {
		mCon = aCon;
	}

	/**
	 * Effettua il reset dei membri di classe.
	 */
	protected void reset() {
		mStatement = null;
		mActualRec = 0;
		mFirstRec = 0;
		mLastRec = 0;
	}

	/**
	 * Ritorna una stringa SQL, relazionata logicamente con gli OR, dei vari valori riferiti ad un campo.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @param aElements
	 *            elementi dei valori da ricercare.
	 * @return stringa composita SQL Standard.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	protected String getSQLStringFromArray(String aFieldName, String[] aElements) throws DAOException {
		String lString = "";

		if (aElements != null && aElements.length > 0) {
			int x = aElements.length; // Numero di elementi
			for (int t = 0; t < x; t++) {
				lString += aFieldName + "='" + aElements[t] + "' OR ";
			}
			lString = lString.substring(0, lString.length() - 3);
		} else
			throw new DAOException("L'array passato non contiene alcun valore o è nullo[" + aElements + "]");

		return lString;
	}

	/**
	 * Ritorna una stringa SQL, relazionata logicamente con gli OR, dei vari valori riferiti ad un campo, in
	 * questo caso i valori sono <code>BigDecimal</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @param aElements
	 *            elementi dei valori da ricercare, di tipo <code>BigDecimal</code>.
	 * @return stringa composita SQL Standard.
	 * @throws DAOException
	 *             propaga errore di eccezione.
	 */
	protected String getSQLStringFromArray(String aFieldName, BigDecimal[] aElements) throws DAOException {
		String lString = "";

		if (aElements != null && aElements.length > 0) {
			int x = aElements.length; // Numero di elementi.
			for (int t = 0; t < x; t++) {
				lString += aFieldName + "=" + aElements[t].toString() + " OR ";
			}
			lString = lString.substring(0, lString.length() - 3);
		} else
			throw new DAOException("L'array passato non contiene alcun valore o è nullo[" + aElements + "]");

		return lString;
	}

	/**
	 * Ritorna il valore del tipo <code>String</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public String getString(String aFieldName) throws DAOException {
		String lField = null;

		try {
			lField = mRs.getString(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lField;
	}

	/**
	 * Ritorna il valore del tipo <code>boolean</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public boolean getBoolean(String aFieldName) throws DAOException {
		boolean lValue = false;

		try {
			lValue = mRs.getBoolean(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>byte</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public byte getByte(String aFieldName) throws DAOException {
		byte lValue = 0;

		try {
			lValue = mRs.getByte(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo array di <code>byte</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public byte[] getBytes(String aFieldName) throws DAOException {
		byte[] lValue = {};

		try {
			lValue = mRs.getBytes(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>double</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public double getDouble(String aFieldName) throws DAOException {
		double lValue = 0.0;

		try {
			lValue = mRs.getDouble(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>float</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public float getFloat(String aFieldName) throws DAOException {
		float lValue = 0f;

		try {
			lValue = mRs.getFloat(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>int</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public int getInt(String aFieldName) throws DAOException {
		int lValue = 0;

		try {
			lValue = mRs.getInt(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>INteger</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public Integer getInteger(String aFieldName) throws DAOException {
		Integer lValue = null;

		try {
			int lIntValue = mRs.getInt(aFieldName);
			lValue = new Integer(lIntValue);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>long</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public long getLong(String aFieldName) throws DAOException {
		long lValue = 0;

		try {
			lValue = mRs.getLong(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>short</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public short getShort(String aFieldName) throws DAOException {
		short lValue = 0;

		try {
			lValue = mRs.getShort(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>Date</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public java.util.Date getDate(String aFieldName) throws DAOException {
		java.util.Date lValue = null;

		try {
			lValue = mRs.getTimestamp(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>Object</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public Object getObject(String aFieldName) throws DAOException {
		Object lValue = null;

		try {
			lValue = mRs.getObject(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>BigDecimal</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public BigDecimal getBigDecimal(String aFieldName) throws DAOException {
		BigDecimal lValue = null;

		try {
			lValue = mRs.getBigDecimal(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Ritorna il valore del tipo <code>Blob</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato. public ByteArrayOutputStream getBlob(String aFieldName) throws
	 *         DAOException { Blob lValue = null; try { lValue = mRs.getBlob(aFieldName); } catch
	 *         (SQLException sqlEx) { sqlEx.printStackTrace(); throw new DAOException(sqlEx); } return lValue;
	 *         }
	 */

	/**
	 * Ritorna il valore del tipo <code>Clob</code> di uno specifico campo del dbase.
	 * <p>
	 *
	 * @param aFieldName
	 *            campo di riferimento.
	 * @return valore del campo desiderato.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public Clob getClob(String aFieldName) throws DAOException {
		Clob lValue = null;

		try {
			lValue = mRs.getClob(aFieldName);
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx + "(" + aFieldName + ")");
		}

		return lValue;
	}

	/**
	 * Imposta lo statement SQL.
	 * <p>
	 *
	 * @param aStatement
	 *            valore stringa statement SQL.
	 */
	protected void setStatement(String aStatement) {
		mStatement = aStatement;
	}

	/**
	 * Sposta il cursore del <code>ResulSet</code> di una unità.
	 * <p>
	 *
	 * @throws DAOException
	 */
	public void start() throws DAOException {
		start(1, Integer.MAX_VALUE);
	}

	/**
	 * Esegue lo statement per un certo numero di records, partendo da uno.
	 * <p>
	 *
	 * @param aNumRecord
	 *            numero di record da elaborare.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void start(int aNumRecord) throws DAOException {
		start(1, aNumRecord);
	}

	/**
	 * Esegue lo statement SQL per un determinato range di records.
	 * <p>
	 *
	 * @param aFirstRec
	 *            primo record del range
	 * @param aLastRec
	 *            ultimo record del range.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void start(int aFirstRec, int aLastRec) throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + " " + mStatement.toString());

		mFirstRec = aFirstRec;
		mLastRec = aLastRec;

		try {
			// fvender 02/08/2018: aggiunto controllo per la gestione della problematica della chiusura delle
			// connessioni
			if (mPs != null) {
				// 22/11/2018 - commento il debug in quanto genera file di log pesantissimi
				// siesLogger.debug("Aggiungo prepared statement a lista.");
				oldPs.add(mPs);
			}
			mPs = mCon.prepareStatement(mStatement);
			JdbcTrackerUtil.trackOpen(mPs,mCon,this);
			
			// AVVOCATURA: calcolo tempo esecuzione query
			long millis = System.currentTimeMillis();

			// fvender 02/08/2018: aggiunto controllo per la gestione della problematica della chiusura delle
			// connessioni
			if (mRs != null) {
				// 22/11/2018 - commento il debug in quanto genera file di log pesantissimi
				// siesLogger.debug("Aggiungo result set a lista.");
				oldRs.add(mRs);
			}

			mRs = mPs.executeQuery();
			JdbcTrackerUtil.trackOpen(mRs,mCon,this);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("########## Tempo di Esecuzione Query: " + (System.currentTimeMillis() - millis)
					+ " (ms) ##########");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			stop();
			throw new DAOException(sqlEx);
		}
	}

	/**
	 * Effettua la chiusura del <code>ResultSet</code> e del <code>PrepareStatement</code>.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void stop() throws DAOException {
		 // siesLogger.info(getClass().getName() + " STOP");
		try {
			reset();
			
//			if (mRs != null)
//				siesLogger.info(getClass().getName() + " STOP");
			
			if (mRs != null) {
				// 202603 Traccio la chiusira el RS
				JdbcTrackerUtil.trackClose(mRs,mCon);
				mRs.close();
			}
			// fvender 02/08/2018: aggiunto blocco for per la gestione della problematica della chiusura delle
			// connessioni
			
			// Loggature per verifica se e quanti RS restano aperti contemporaneamente
			// Attivare solo per Debug per tracciare situazioni in cui lo stesso sqlDao
			// viene usato può volte senza lo stop attivando più cursori contemporaneamente
//			if (oldRs!=null && oldRs.size()>0) {
//				siesLogger.debug("Closing oldRs.size() = "+oldRs.size()+" - "+this.getClass());	
//				Throwable t = new Throwable("Cleaning CACHED ResultSet");
//				siesLogger.debug("",t);
//				if (oldRs.size()>4) {
//					siesLogger.debug("=============================================");
//					siesLogger.debug("ERROR vado in sleep controllare i cursori....");	
//					siesLogger.debug("=============================================");
//					//Thread.sleep(10000);
//				}
//			}
			
			for (ResultSet oRs : oldRs) {
				siesLogger.debug("Closing oldRs ..."+this.getClass());				
				try {
					JdbcTrackerUtil.trackClose(oRs,mCon);
					oRs.close();
//					if (oldRs.size()>4) {
//						siesLogger.debug("oldRs closed seep 3s");
//						//Thread.sleep(3000);
//					}
					// 22/11/2018 - commento il debug in quanto genera file di log pesantissimi
					// siesLogger.debug("Chiuso result set da lista.");
				} catch (SQLException e) {
					siesLogger.debug(e.getMessage());
				}
			}

			// 202603 devo pulire RS corrente e la lista dopo aver chiuso tutti i ResultSet!!!
			mRs = null;
			if (oldRs!=null) 
				oldRs.clear();
			// 202603 - FINE

			if (mPs != null) {
				// 202603 Traccio la chiusira del PS
				JdbcTrackerUtil.trackClose(mPs,mCon);
				mPs.close();
			}
			

//			if (oldPs!=null && oldPs.size()>0) {
//				siesLogger.debug("Closing oPs.size() = "+oldPs.size()+" - "+this.getClass());	
//				if (oldRs.size()>4) {
//					siesLogger.debug("Prima della close oldPs sleep 10s ");	
//					//Thread.sleep(10000);
//				}
//			}
			
			for (PreparedStatement oPs : oldPs) {
//				siesLogger.debug("Closing oPs ..."+this.getClass());	
				try {
					JdbcTrackerUtil.trackClose(mPs, mCon);
					oPs.close();
					// 22/11/2018 - commento il debug in quanto genera file di log pesantissimi
					// siesLogger.debug("Chiuso prepared statement da lista.");
				} catch (SQLException e) {
					siesLogger.debug(e.getMessage());
				}
			}
			
//			if (oldPs!=null && oldPs.size()>4) {
//				siesLogger.debug("DOPO della close oldPs sleep 10s ");	
//				//Thread.sleep(10000);				
//			}
			// 202603 devo pulire PS corrente e la lista dopo aver chiuso tutti i PS!!!
			mPs = null;
			if (oldPs!=null) oldPs.clear();
			// 202603 - FINE
			
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza
			// siesLogger al posto di LogF3B.getLogger()
			siesLogger.debug(ex.getMessage(), ex);
			// throw new DAOException(sqlEx);
		}
	}

	/**
	 * Si sposta alla prossima occorrenza nel <code>ResultSet</code>.
	 * <p>
	 *
	 * @return ritorna il flag che indica se esistono prossime occorrenze.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public boolean next() throws DAOException {
		boolean lResult = true;

		try {
			while (mActualRec < (mFirstRec - 1)) {
				lResult = mRs.next();
				mActualRec++;
			}

			if (mActualRec < mLastRec) {
				lResult = mRs.next();
				mActualRec++;
			} else
				lResult = false;

		} catch (SQLException sqlEx) {
			stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx);
		}

		return lResult;
	}

	/**
	 * Esegue lo statement SQL per l'inserimento di un record.
	 * <p>
	 *
	 * @return ritorna l'id della sequence.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public BigDecimal insert() throws DAOException {
		BigDecimal lReturn = null;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + " " + mStatement.toString());

		try {
			mPs.executeUpdate();
			mPs.close();
		} catch (SQLException sqlEx) {
			stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx);
		}

		return lReturn;
	}

	/**
	 * Metodo per l'esecuzione dello statement per update record.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void update() throws DAOException {

		try {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(getClass().getName() + " " + mStatement.toString());

			// Esegue controllo d'impostazione della condizione di WHERE.
			if (mStatement.trim().toUpperCase().indexOf("WHERE") == -1)
				throw new DAOException(
						"Operazione di UPDATE non permessa! Comando SQL senza condizione di WHERE.");

			mPs.executeUpdate();
			mPs.close();
		} catch (SQLException sqlEx) {
			stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx);
		}
	}

	/**
	 * Metodo per l'esecuzionedello statement per cancellazione record.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void delete() throws DAOException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info(getClass().getName() + " " + mStatement.toString());

		try {
			// Esegue controllo d'impostazione della condizione di WHERE.
			if (mStatement.trim().toUpperCase().indexOf("WHERE") == -1)
				throw new DAOException(
						"Operazione di DELETE non permessa! Comando SQL senza condizione di WHERE.");

			// fvender 02/08/2018: aggiunto controllo per la gestione della problematica della chiusura delle
			// connessioni
			if (mPs != null) {
				// 22/11/2018 - commento il debug in quanto genera file di log pesantissimi
				// siesLogger.debug("Aggiungo prepared statement a lista.");
				oldPs.add(mPs);
			}

			mPs = mCon.prepareStatement(mStatement);
			mPs.executeUpdate();
			mPs.close();
		} catch (SQLException sqlEx) {
			stop();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(sqlEx.getMessage(), sqlEx);
			throw new DAOException(sqlEx);
		}
	}

	/**
	 * Ritorna il prossimo id della sequence.
	 * <p>
	 *
	 * @param aSequenceName
	 *            nom della sequence.
	 * @return l'id della sequence.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	protected BigDecimal getNextSeq(String aSequenceName) throws DAOException {
		Statement lStatement = null;
		ResultSet lRS = null;
		BigDecimal lValue = null;

		try {
			lStatement = mCon.createStatement();
			// Preleva dal properties la stringa SQL per ottenere la sequence.
			String lStrSeq = F3BProperties.getProperty("sequence.command");
			// Parserizza e sostituisce il ? con il nome della sequence.
			lStrSeq = StringUtils.replace(lStrSeq, "?", aSequenceName);

			lStatement.executeQuery(lStrSeq);
			lRS = lStatement.getResultSet();

			if (lRS.next())
				lValue = lRS.getBigDecimal(1);
			else
				throw new DAOException("Valore sequence non ottenuto");
		} catch (SQLException SQLEx) {
			SQLEx.printStackTrace();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(SQLEx.getMessage(), SQLEx);
			throw new DAOException("Errore nella richiesta dell'id della sequence : " + SQLEx);
		} catch (F3BException F3BEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(F3BEx.getMessage(), F3BEx);
			throw new DAOException("Errore nella richiesta dell'id della sequence : " + F3BEx);
		} finally {
			try {
				if (lRS != null)
					lRS.close();
				if (lStatement != null)
					lStatement.close();
			} catch (Exception ex) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error(ex.getMessage(), ex);
				throw new DAOException("" + ex);
			}
		}

		return lValue;
	}

	/**
	 * Effettua il cleanup dell'oggetto DAO.
	 * <p>
	 *
	 * @param aDAO
	 *            oggetto DAO.
	 */
	public static void cleanup(GenericDAO aDAO) {
		try {
			if (aDAO != null)
				aDAO.stop();
		} catch (DAOException DAOEx) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(DAOEx, DAOEx);
			DAOEx.printStackTrace();
		}
	}

	/**
	 * Ritorna il model come padre, tale metodo è da ridefinire in tutte le classi derivate.
	 * <p>
	 *
	 * @return una nuova istanza di <code>GenericModel</code>.
	 * @throws DAOException
	 */
	public GenericModel getModel() throws DAOException {
		return new GenericModel();
	}

	/**
	 * Imposta il model e contestaulmente viene popolato.
	 * <p>
	 *
	 * @param aModel
	 *            model di riferimento.
	 * @throws DAOException
	 *             propaga errori di eccezione.
	 */
	public void setModel(GenericModel aModel) throws DAOException {
	}

	/**
	 * Ritorna l'insieme di model, per tutte le occorrenze.
	 * <p>
	 *
	 * @return l'insieme di models.
	 * @throws DAOException
	 *             propaga l'errore di ecceione.
	 */
	public Collection getModels() throws DAOException {
		return getModels(1, Integer.MAX_VALUE);
	}

	/**
	 * Ritorna l'insieme dei models opportunamente popolati. Come parametri viene passato il range di
	 * occorrenze.
	 * <p>
	 *
	 * @param aFromRec
	 *            dal record.
	 * @param aToRec
	 *            al record.
	 * @return l'insieme dei models opportunamente popolati.
	 * @throws DAOException
	 */
	public Collection getModels(int aFromRec, int aToRec) throws DAOException {
		ArrayList lAL = null;

		start(aFromRec, aToRec);
		lAL = new ArrayList();

		while (next())
			lAL.add(getModel());

		stop();
		return lAL;
	}

	/**
	 * Ritorna un model solo usato per le selezione tramite chiave. Come parametri viene passato il range di
	 * occorrenze.
	 * <p>
	 *
	 * @return il model opportunamente popolato.
	 * @throws DAOException
	 */
	public GenericModel getModelByKey() throws DAOException {
		GenericModel lModel = null;

		start();

		if (next())
			lModel = getModel();

		stop();

		return lModel;
	}

	/**
	 * Ritorna l'insieme di models, per il numero di occorrenze/record specificate.
	 * <p>
	 *
	 * @param aNumRec
	 *            limite numero di record.
	 * @return l'insieme di model.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public Collection getModels(int aNumRec) throws DAOException {
		return getModels(1, aNumRec);
	}

	/**
	 * Richiama la funzione di stop() per rilasciare la connessione alla distruzione del DAO.
	 */
	// STUB : Probabilmente è inutile lanciare l'eccezione Luigi 7-9-04
	public void finalize() throws Exception {

		stop();
	}

}