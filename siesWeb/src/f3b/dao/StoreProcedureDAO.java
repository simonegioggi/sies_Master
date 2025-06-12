package f3b.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * StoreProcedureDAO - Classe padre per la gestione delle Store Procedure, infatti eredita tutte le proprietà
 * di GenericDAO pertanto dovrà essere ereditata da tutte le classi che hanno la responsabilità di effettuare
 * StoreProcedure
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StoreProcedureDAO extends GenericDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected CallableStatement mCallStat = null;

	protected String mNameStoreProcedure = null;

	protected Hashtable mArgInputs = null;
	protected Hashtable mArgOutputs = null;

	protected Hashtable mFieldsValues = null;
	protected Hashtable mFieldsNullValues = null;

	protected Hashtable mArgInputsPosition = null;
	protected Hashtable mArgOutputsPosition = null;

	/**
	 * Costruttore di classe con la connessione al db come parametro.
	 * <p>
	 *
	 * @param aCon
	 *            Connessione al Dbase.
	 */
	public StoreProcedureDAO(Connection aCon) {
		super(aCon);

		mFieldsValues = new Hashtable();
		mFieldsNullValues = new Hashtable();
		mArgInputs = new Hashtable();
		mArgOutputs = new Hashtable();
		mArgInputsPosition = new Hashtable();
		mArgOutputsPosition = new Hashtable();
	}

	/**
	 * Setta il Nome della StoreProcedure
	 *
	 * @param aName
	 * @throws DAOException
	 */
	public void setStoreProcedure(String aName) {
		mNameStoreProcedure = aName;
	}

	/**
	 * Esecuzione della Store Procedure.
	 *
	 * @return Ritorna true se non si è verificato un errore
	 * @throws DAOException
	 */
	public boolean execute() throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".execute : inizio");

		try {
			int lFieldType = 0;
			int lFieldsPosition = 0;

			String lCall = "";
			String lField = null;
			// String lFieldKey = null;
			Object lValue = null;
			Enumeration lEnumFields = null;

			lCall = "{call " + mNameStoreProcedure;

			int lCountArg = 0;

			// Setto la stringa di chiamata alla Store Procedure impostando i parametri
			if (mArgInputs != null)
				lCountArg = mArgInputs.size();
			if (mArgOutputs != null)
				lCountArg += mArgOutputs.size();

			if (lCountArg != 0) {
				lCall += "(";
				while (lCountArg > 0) {
					lCall += "?,";
					lCountArg--;
				}
				lCall = lCall.substring(0, lCall.length() - 1);
				lCall += ")";
			}
			lCall += "}";
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Call: " + lCall);

			// Prepare Call
			mCallStat = mCon.prepareCall(lCall);

			if (mArgInputs != null) {
				lEnumFields = mArgInputs.keys();

				while (lEnumFields.hasMoreElements()) {
					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgInputs.get(lField)).intValue();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Parametro di input (nome,tipo): " + lField + ", " + lFieldType);

					if (mArgInputsPosition.size() > 0)
						lFieldsPosition = ((Integer) mArgInputsPosition.get(lField)).intValue();
					else
						lFieldsPosition++;

					// Se il campo non è stato valorizzato restituisce null
					lValue = mFieldsValues.get(lField);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Parametro di input (value): " + lValue);

					switch (lFieldType) {
					case STRING: // Attributo STRING
						mCallStat.setString(lFieldsPosition, (String) lValue);
						break;

					case INT: // Attributo INT
						mCallStat.setInt(lFieldsPosition, ((Integer) lValue).intValue());

						break;

					case DATE: // Attributo DATE
						if (lValue != null)
							mCallStat.setTimestamp(lFieldsPosition,
									new java.sql.Timestamp(((java.util.Date) lValue).getTime()));
						else
							mCallStat.setTimestamp(lFieldsPosition, null);

						break;

					case BIG_DECIMAL: // Attributo BIG_DECIMAL
						mCallStat.setBigDecimal(lFieldsPosition, (BigDecimal) lValue);

						break;

					default:
						throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");
					}

				}
			}

			// Registrazione degli Output Parameter
			if (mArgOutputs != null) {

				lFieldsPosition = 0;
				lEnumFields = mArgOutputs.keys();
				int lSizeInput = mArgInputs.size();
				int lPosOut;

				while (lEnumFields.hasMoreElements()) {

					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgOutputs.get(lField)).intValue();

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Parametro di output (nome,tipo): " + lField + ", " + lFieldType);

					if (mArgOutputsPosition.size() > 0) {
						lFieldsPosition = ((Integer) mArgOutputsPosition.get(lField)).intValue();
						lPosOut = lFieldsPosition;
					} else {
						lFieldsPosition++;
						lPosOut = lFieldsPosition + lSizeInput;
					}

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("Prima di chiamare =  mCallStat.registerOutParameter(" + (lPosOut) + ","
							+ lFieldType + ");");

					// Se il campo non è stato valorizzato restituisce null
					mCallStat.registerOutParameter(lPosOut, lFieldType);

					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("ESEGUITO =  mCallStat.registerOutParameter(" + (lPosOut) + ","
							+ lFieldType + ");");
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CALL STORE PROCEDURE " + lCall);

			if (mCon != null && mCallStat != null) {
				// AVVOCATURA: calcolo tempo esecuzione query
				long millis = System.currentTimeMillis();
				mCallStat.executeUpdate();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("########## Tempo di Esecuzione Update x Store Procedure: "
						+ (System.currentTimeMillis() - millis) + " (ms) ##########");
			}

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(this.getClass().getName() + ".execute : fine");

			return true;
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
			throw new DAOException("Errore Generico " + eEx);
		}

		// return false;
	}

	/**
	 * Esecuzione di una Store procedure che restituisce un resultset
	 *
	 * @return Ritorna true se non si è verificato un errore
	 * @throws DAOException
	 */
	public void executeQuery() throws DAOException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".executeQuery : inizio");
		try {
			int lFieldType = 0;
			int lFieldsCount = 0;
			String lCall = "";
			String lField = null;
			// String lFieldKey = null;
			Object lValue = null;
			Enumeration lEnumFields = null;

			lCall = "{call " + mNameStoreProcedure;

			int lCountArg = 0;

			// Setto la stringa di chiamata alla Store Procedure impostando i parametri
			if (mArgInputs != null)
				lCountArg = mArgInputs.size();
			if (mArgOutputs != null)
				lCountArg += mArgOutputs.size();

			if (lCountArg != 0) {
				lCall += "(";
				while (lCountArg > 0) {
					lCall += "?,";
					lCountArg--;
				}
				lCall = lCall.substring(0, lCall.length() - 1);
				lCall += ")";
			}
			lCall += "}";

			// Prepare Call
			mCallStat = mCon.prepareCall(lCall);

			if (mArgInputs != null) {
				lEnumFields = mArgInputs.keys();

				while (lEnumFields.hasMoreElements()) {
					lFieldsCount++;
					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgInputs.get(lField)).intValue();

					// Se il campo non è stato valorizzato restituisce null
					lValue = mFieldsValues.get(lField);

					switch (lFieldType) {
					case STRING: // Attributo STRING
						mCallStat.setString(lFieldsCount, (String) lValue);
						break;

					case INT: // Attributo INT
						mCallStat.setInt(lFieldsCount, ((Integer) lValue).intValue());
						break;

					case DATE: // Attributo DATE
						if (lValue != null)
							mCallStat.setTimestamp(lFieldsCount,
									new java.sql.Timestamp(((java.util.Date) lValue).getTime()));
						else
							mCallStat.setTimestamp(lFieldsCount, null);
						break;

					case BIG_DECIMAL: // Attributo BIG_DECIMAL
						mCallStat.setBigDecimal(lFieldsCount, (BigDecimal) lValue);
						break;

					default:
						throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");
					}
				}
			}

			// Registrazione degli Output Parameter
			if (mArgOutputs != null) {
				lFieldsCount = 0;
				lEnumFields = mArgOutputs.keys();
				int lSizeInput = mArgInputs.size();

				while (lEnumFields.hasMoreElements()) {
					lFieldsCount++;
					lField = (String) lEnumFields.nextElement();
					lFieldType = ((Integer) mArgOutputs.get(lField)).intValue();

					// Se il campo non è stato valorizzato restituisce null
					mCallStat.registerOutParameter(lFieldsCount + lSizeInput, lFieldType);
				}
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("CALL STORE PROCEDURE " + lCall);

			if (mCon != null && mCallStat != null) {
				// AVVOCATURA: calcolo tempo esecuzione query
				long millis = System.currentTimeMillis();
				this.mRs = mCallStat.executeQuery();
				siesLogger.info("########## Tempo di Esecuzione Query x Store Procedure: "
						+ (System.currentTimeMillis() - millis) + " (ms) ##########");
			}
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(this.getClass().getName() + ".executeQuery : fine");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		}
	}

	public CallableStatement getStatement() {
		return mCallStat;
	}

	/**
	 * Imposta nome e tipo campo.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo della tablla di riferimento.
	 * @param aType
	 *            tipo di campo.
	 */
	protected void setArgInput(String aFieldName, int aType) {
		mArgInputs.put(aFieldName, new Integer(aType));
	}

	/**
	 * Imposta nome e posizione del parametro di input.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo della tablla di riferimento.
	 * @param aPosition
	 *            Posizione del parametro.
	 */
	protected void setArgInputPosition(String aFieldName, int aPosition) {
		mArgInputsPosition.put(aFieldName, new Integer(aPosition));
	}

	/**
	 * Imposta nome e tipo campo dei parametri in uscita.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo della tablla di riferimento.
	 * @param aType
	 *            tipo di campo.
	 */
	protected void setArgOutput(String aFieldName, int aType) {
		mArgOutputs.put(aFieldName, new Integer(aType));
		mFieldsValues.put(aFieldName, new Integer(mArgInputs.size() + mArgOutputs.size()));
	}

	/**
	 * Imposta nome e posizione del parametro di output.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo della tablla di riferimento.
	 * @param aPosition
	 *            Posizione del parametro.
	 */
	protected void setArgOutputPosition(String aFieldName, int aPosition) {
		mArgOutputsPosition.put(aFieldName, new Integer(aPosition));
	}

	/**
	 * Imposta il campo di riferimento con il valore <code>BigDecimal</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setBigDecimal(String aFieldName, BigDecimal aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

	/**
	 * Imposta il campo di riferimento con il valore <code>String</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setString(String aFieldName, String aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

	protected String getOutString(String aFieldName) throws DAOException {
		try {
			String lReturn = null;

			if (aFieldName != null) {
				int lPosArg = ((Integer) mFieldsValues.get(aFieldName)).intValue();
				lReturn = mCallStat.getString(lPosArg);
			}

			return lReturn;
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		}

	}

	protected BigDecimal getOutBigDecimal(String aFieldName) throws DAOException {
		try {
			BigDecimal lReturn = null;

			if (aFieldName != null) {
				int lPosArg = ((Integer) mFieldsValues.get(aFieldName)).intValue();
				lReturn = mCallStat.getBigDecimal(lPosArg);
			}

			return lReturn;
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		}

	}

	protected int getOutInteger(String aFieldName) throws DAOException {
		try {
			int lReturn = -1;

			if (aFieldName != null) {
				int lPosArg = ((Integer) mFieldsValues.get(aFieldName)).intValue();
				lReturn = mCallStat.getInt(lPosArg);
			}

			return lReturn;
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		}
	}

}