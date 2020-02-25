package f3b.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>
 * Title: TableDAO
 * </p>
 * <p>
 * Description: Classe per la gestione dati al DBase rifeririti da una singola tabella.
 * </p>
 * <p>
 * Company: Bull ITALIA S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableOracleDAO extends GenericDAO {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected Hashtable mFields = null;
	protected Hashtable mFieldsKey = null;
	protected Hashtable mFieldsValues = null;
	protected Hashtable mFieldsNullValues = null;

	protected String mSequenceName = null;
	protected String mTableName = null;
	protected String mStrConditions = null;
	protected String mStrSelect = null;
	protected String mStrOrdering = null;

	// Parametro per la selectForUpdate
	protected boolean mForUpdate = false;
	protected boolean mPrimaryKeyMode = false;
	protected boolean mDistinctMode = false;
	protected boolean mBlobMode = false;

	// Parametro per l'esclusione della sequence nelle Insert
	protected boolean mWithoutSequence = false;

	/**
	 * Costruttore di classe con parametro. Passa la connessione alla superclasse e inizializza i membri di
	 * classe.
	 * <p>
	 *
	 * @param aCon
	 *            connessione al DBase.
	 */
	public TableOracleDAO(Connection aCon) {
		super(aCon);

		mFields = new Hashtable();
		mFieldsKey = new Hashtable();
		mFieldsValues = new Hashtable();
		mFieldsNullValues = new Hashtable();
	}

	/**
	 * Reimposta con valori di default i membri di classe.
	 */
	protected void reset() {
		super.reset();

		mFieldsValues.clear();
		mFieldsNullValues.clear();

		mStrConditions = null;
		mStrOrdering = null;
		mPrimaryKeyMode = false;
		mDistinctMode = false;
	}

	/**
	 * Imposta il nome della tabella.
	 * <p>
	 *
	 * @param aTableName
	 *            nome tabella riferimento.
	 */
	protected void setTable(String aTableName) {
		mTableName = aTableName;
	}

	/**
	 * Imposta le condizioni di filtro (where condition).
	 * <p>
	 *
	 * @param aCondition
	 *            stringa di condizione.
	 */
	protected void setCondition(String aCondition) {
		mStrConditions = aCondition;
	}

	/**
	 * Imposta la condizione della condizione for update.
	 */
	protected void setForUpdate() {
		mForUpdate = true;
	}

	/**
	 * Imposta la select che verrà annidata ad una insert.
	 * <p>
	 *
	 * @param aSelect
	 *            stringa con select.
	 */
	protected void setSelect(String aSelect) {
		mStrSelect = aSelect;
	}

	/**
	 * Imposta la clausola ORDER BY nella select su singola tabella.
	 * <p>
	 *
	 * @param aOrdering
	 *            stringa di ordinamento.
	 */
	protected void setOrder(String aOrdering) {
		mStrOrdering = aOrdering;
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
	protected void setField(String aFieldName, int aType) {
		mFields.put(aFieldName, new Integer(aType));
	}

	/**
	 * Imposta nome campo chiave e il relativo tipo.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo dell tabella di riferimento.
	 * @param aType
	 *            tipo di campo.
	 */
	protected void setFieldKey(String aFieldName, int aType) {
		if (mSequenceName == null) {
			setField(aFieldName, aType);
			mFieldsKey.put(aFieldName, new Integer(aType));
		}
	}

	/**
	 * Imposta il nome del campo sequence.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo della tabella di riferimento.
	 * @param aSequenceName
	 *            nome della sequence.
	 */
	protected void setSequenceField(String aFieldName, String aSequenceName) {
		if (mSequenceName == null) {
			setField(aFieldName, BIG_DECIMAL);
			mFieldsKey.put(aFieldName, new Integer(BIG_DECIMAL));
			mSequenceName = aSequenceName;
		}
	}

	/**
	 * Imposta il campo di riferimento con il valore <code>int</code>
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setInt(String aFieldName, int aValue) {
		mFieldsValues.put(aFieldName, new Integer(aValue));
	}

	/**
	 * Imposta il campo di riferimento con il valore <code>Integer</code>
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setInteger(String aFieldName, Integer aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
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

	/**
	 * Imposta il campo di riferimento con il valore <code>Date</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setDate(String aFieldName, java.util.Date aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
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
	 * Ritorna il prossimo id della sequence.
	 * <p>
	 *
	 * @param aSequenceName
	 *            nom della sequence.
	 * @return l'id della sequence.
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	/*
	 * protected BigDecimal getNextSeq(String aSequenceName) throws DAOException { Statement lStatement =
	 * null; ResultSet lRS = null; java.math.BigDecimal lValue = null; try { lStatement =
	 * mCon.createStatement(); String lStrSeq = "select " + aSequenceName + ".nextval from dual";
	 * lStatement.executeQuery(lStrSeq); lRS = lStatement.getResultSet(); if (lRS.next()) lValue =
	 * lRS.getBigDecimal(1); else throw new DAOException("Valore sequence non ottenuto"); } catch
	 * (SQLException SQLEx) { SQLEx.printStackTrace(); throw new
	 * DAOException("Errore nella richiesta dell'id della sequence : " + SQLEx ); } finally { try { if (lRS !=
	 * null) lRS.close(); if (lStatement != null) lStatement.close(); } catch (Exception ex) { throw new
	 * DAOException("" + ex); } } return lValue; }
	 */
	/**
	 * Imposta il campo di riferimento con un valore del tipo <code>Blob</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimeno.
	 * @param aValue
	 *            valore da impostare.
	 */
	public ByteArrayOutputStream getBlob(String aFieldName) throws DAOException {
		// BLOB lBlob = null;
		ByteArrayOutputStream lStream = null;
		try {
			/*
			 * lBlob = ((OracleResultSet)super.mRs).getBLOB(aFieldName); //--------------------GDV 31/07/2003
			 * long lLen = lBlob.length(); BigDecimal lLen = new BigDecimal(lBlob.length()); byte[] lBuffer =
			 * new byte[lLen.intValue()]; lStream = new ByteArrayOutputStream(); lBuffer = lBlob.getBytes(1,
			 * lLen.intValue()); lStream.write(lBuffer); lStream.flush(); lStream.close();
			 */

			Blob blob = super.mRs.getBlob(aFieldName);

			// --------------------GDV 31/07/2003
			BigDecimal lLen = new BigDecimal(blob.length());
			byte[] lBuffer = new byte[lLen.intValue()];
			lStream = new ByteArrayOutputStream();
			lBuffer = blob.getBytes(1, lLen.intValue());
			lStream.write(lBuffer);
			lStream.flush();
			lStream.close();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			throw new DAOException(sqlEx);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			throw new DAOException("Errore nell'apertura dello Stream!");
		}
		return lStream;
	}

	/**
	 * Setta un campo BLOB
	 *
	 * @param aFieldName
	 *            Nome del campo BLOB
	 * @param aValue
	 *            Valore InputStream
	 */
	protected void setBlob(String aFieldName, ByteArrayInputStream aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

	/**
	 * Imposta il campo di riferimento con un valore del tipo <code>Blob</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimeno.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setBinaryStream(String aFieldName, InputStream aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

	/**
	 * Imposta il campo di riferimento con un valore del tipo <code>Clob</code>.
	 * <p>
	 *
	 * @param aFieldName
	 *            nome del campo di riferimento.
	 * @param aValue
	 *            valore da impostare.
	 */
	protected void setClob(String aFieldName, Clob aValue) {
		if (aValue != null)
			mFieldsValues.put(aFieldName, aValue);
		else
			mFieldsNullValues.put(aFieldName, new Boolean(true));
	}

	//
	// metodi pubblici
	//

	/**
	 * Attiva la modalità della <code>Primary Key</code>.
	 */
	public void setPrimaryKeyMode() {
		mPrimaryKeyMode = true;
	}

	/**
	 * Attiva la modalità <code>Distinct</code>.
	 */
	public void setDistinctMode() {
		mDistinctMode = true;
	}

	/**
	 * Esegue l'interrogazione sulla tabella di riferimento, passando ad esso il numero del primo e l'ultimo
	 * record.
	 * <p>
	 *
	 * @param aFirstRec
	 *            numero del primo record.
	 * @param aLastRec
	 *            numero dell'ultimo record.
	 * @throws DAOException
	 *             propga l'errore di eccezione.
	 */
	public void start(int aFirstRec, int aLastRec) throws DAOException {
		String lStatement = null;
		Enumeration lEn = null;

		lStatement = " SELECT ";

		if (mDistinctMode)
			lStatement += "DISTINCT ";

		if (mPrimaryKeyMode)
			lEn = mFieldsKey.keys();
		else
			lEn = mFields.keys();

		while (lEn.hasMoreElements())
			lStatement += (String) lEn.nextElement() + ",";

		lStatement = lStatement.substring(1, lStatement.length() - 1);
		lStatement += " FROM " + mTableName;

		if (mStrConditions != null && !mStrConditions.equals(""))
			lStatement += " WHERE " + mStrConditions;

		if (mStrOrdering != null && !mStrOrdering.equals(""))
			lStatement += " ORDER BY " + mStrOrdering;

		// GDV --- if sulla selectForUpdate
		if (mForUpdate)
			lStatement += " FOR UPDATE ";

		setStatement(lStatement);

		super.start(aFirstRec, aLastRec);
	}

	/**
	 * Effettua l'inserimento di un record nel dbase, inoltre ritorna il relativo id della sequence.
	 *
	 * @return l'id della sequence
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public BigDecimal insert() throws DAOException {
		BigDecimal lIdSequence = null;

		int lFieldType = 0;
		int lFieldsCount = 0;
		String lStatement = "";
		String lQuestionMarks = "";
		String lField = null;
		String lFieldKey = null;
		String lSequenceField = null;
		Object lValue = null;
		Enumeration lEnumFields = null;
		String lBlobName = null;
		ByteArrayInputStream lBlobValue = null;
		String lLog = "";

		lStatement = "INSERT INTO " + mTableName + " ( ";

		//
		// GESTIONE DELLA SEQUENCE AUTOMATICA
		//
		if (!mWithoutSequence) {
			if (mSequenceName != null) {
				Enumeration lEnumFieldsKey = mFieldsKey.keys();
				lSequenceField = (String) lEnumFieldsKey.nextElement();
			}
		}

		lEnumFields = mFields.keys();
		// Creazioen della stringa del PrepareStatement
		while (lEnumFields.hasMoreElements()) {
			lField = (String) lEnumFields.nextElement();
			lFieldType = ((Integer) mFields.get(lField)).intValue();

			if (lFieldType == TBLOB) { // Il campo BLOB va alla fine
				lBlobName = lField + ",";
				mBlobMode = true;
			} else {
				lStatement += lField + ",";
				lQuestionMarks += "?,";
			}
		}

		if (mBlobMode) // se c'e un BLOB lo scrivo alla fine
		{
			lStatement += lBlobName;
			lQuestionMarks += "EMPTY_BLOB(),";
		}

		lStatement = lStatement.substring(0, lStatement.length() - 1);
		lQuestionMarks = lQuestionMarks.substring(0, lQuestionMarks.length() - 1);
		lStatement += ") VALUES (" + lQuestionMarks + ")";
		lEnumFields = mFields.keys();

		try {
			mPs = mCon.prepareStatement(lStatement);
			lEnumFields = mFields.keys();

			while (lEnumFields.hasMoreElements()) {
				lFieldsCount++;
				lField = (String) lEnumFields.nextElement();
				lFieldType = ((Integer) mFields.get(lField)).intValue();

				// Se il campo non è stato valorizzato restituisce null
				lValue = mFieldsValues.get(lField);

				lLog += "(" + lField + "," + mFieldsValues.get(lField) + ")";

				switch (lFieldType) {
				case STRING: // Attributo STRING
					mPs.setString(lFieldsCount, (String) lValue);
					break;

				case INT: // Attributo INT
					mPs.setInt(lFieldsCount, ((Integer) lValue).intValue());
					break;

				case INTEGER: // Attributo INTEGER
					if (lValue != null)
						mPs.setInt(lFieldsCount, ((Integer) lValue).intValue());
					else
						mPs.setInt(lFieldsCount, 0);
					break;

				case DATE: // Attributo DATE
					if (lValue != null)
						mPs.setTimestamp(lFieldsCount,
								new java.sql.Timestamp(((java.util.Date) lValue).getTime()));
					else
						mPs.setTimestamp(lFieldsCount, null);
					break;

				case BIG_DECIMAL: // Attributo BIG_DECIMAL
					if (lSequenceField != null) {
						if (lSequenceField.equals(lField)) {
							lFieldKey = lField;
							lIdSequence = getNextSeq(mSequenceName);
							mPs.setBigDecimal(lFieldsCount, lIdSequence);
						} else {
							mPs.setBigDecimal(lFieldsCount, (BigDecimal) lValue);
						}
					} else {
						mPs.setBigDecimal(lFieldsCount, (BigDecimal) lValue);
					}
					break;

				case TBLOB: // Attributo BLOB
					mBlobMode = true;
					lBlobName = lField;
					lBlobValue = (ByteArrayInputStream) lValue;
					lFieldsCount--; // Il BLOB si inserisce alla fine
					break;

				case CLOB: // Attributo CLOB
					mPs.setClob(lFieldsCount, (Clob) lValue);
					break;

				case BINARY_STREAM: // Attributo BINARY_STREAM
					int lSize = 0;
					ByteArrayInputStream lStream = ((ByteArrayInputStream) lValue);
					lSize = lStream.available();
					mPs.setBinaryStream(lFieldsCount, lStream, lSize);
					break;

				default:
					throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");
				}
			}

			mStatement = lStatement;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(lLog);
			super.insert(); // Anteposto il log all'inserimento.

			if (mBlobMode && (lBlobValue != null)) // Se c'e' da inserire un BLOB
			{
				if ((lFieldKey != null) && (lIdSequence != null))
					setBigDecimal(lFieldKey, lIdSequence); // Setto la sequence inserita

				selByKey(); // seleziona la riga inseirta loccandola
				setForUpdate();
				start();

				Blob lBlob = null;

				while (this.next()) {
					lBlob = super.mRs.getBlob(lBlobName);
				}

				OutputStream lStream = ((oracle.sql.BLOB) lBlob).getBinaryOutputStream();
				// int lSize = lBlobValue.available();
				// byte[] lBuffer = new byte[lSize];
				// MEV_42 - commento l'inizializzazione del lBuffer di cui sopra, in quanto per file troppo
				// grandi non si riesce a scrivere sullo stream
				// pertanto spezzetto il buffer settando 25mb alla volta
				byte[] lBuffer = new byte[25600];
				// int lLength = -1;
				// Setto il BLOB sul DB
				while (/* lLength = */lBlobValue.read(lBuffer) != -1)
					lStream.write(lBuffer);

				lStream.flush();
				lStream.close();
				lBlobValue.close();
			}
		} catch (SQLException sex) {
			stop();
			sex.printStackTrace();
			throw new DAOException(sex);
		} catch (Exception ex) {
			stop();
			ex.printStackTrace();
			throw new DAOException("Errore durante le stream del BLOB");
		}
		return lIdSequence;
	}

	/**
	 * Effettua l'insert con dati provenienti da una select.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void insertWithSelect() throws DAOException {
		String lStatement = "";
		Enumeration lEnum = null;

		lStatement = "INSERT INTO " + mTableName + "( ";

		lEnum = mFields.keys();

		while (lEnum.hasMoreElements())
			lStatement += (String) lEnum.nextElement() + ",";

		// Elimina l'ultima virgola
		lStatement = lStatement.substring(0, lStatement.length() - 1);
		lStatement += " ) ( " + mStrSelect + " ) ";
		try {
			mPs = mCon.prepareStatement(lStatement);
			mStatement = lStatement;
			super.insert();
		} catch (SQLException sqlex) {
			stop();
			throw new DAOException(sqlex);
		}

	}

	/**
	 * Setta il parametro per l'esclusione della sequence
	 *
	 * @param aValue
	 */
	public void setWithoutSequence(boolean aValue) {
		mWithoutSequence = aValue;
	}

	public boolean getWithoutSequence() {
		return mWithoutSequence;
	}

	/**
	 * Effettua l'UPDATE di uno statement impostato.
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void update(String lStatement) throws DAOException {
		try {
			mPs = mCon.prepareStatement(lStatement);
			setStatement(lStatement);
			super.update();
		} catch (SQLException sex) {
			stop();
			throw new DAOException(sex);
		}
	}

	/**
	 * Effettua l'UPDATE di un record con i relativi dati.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void update() throws DAOException {
		String lStatSQL = "";
		String lField = null;
		int lFieldType = 0;
		int lFieldsCount = 0;
		Object lValue = null;
		boolean lNullValue = false;
		Enumeration lEnumFields = null;
		// Enumeration lEnum = null;
		// Enumeration lEnumNullValues = null;
		String lLog = "";

		String lBlobName = null;
		ByteArrayInputStream lBlobValue = null;

		if ((mFieldsValues.size() == 0) && (mFieldsNullValues.size() == 0))
			throw new DAOException("Non sono stati specificati valori da aggiornare.");

		lStatSQL = "UPDATE " + mTableName + " SET ";

		lEnumFields = mFields.keys();
		// lEnum = mFieldsValues.keys();
		// lEnumNullValues = mFieldsNullValues.keys();

		// per tutti i campi della tabella
		while (lEnumFields.hasMoreElements()) {
			lField = (String) lEnumFields.nextElement();
			lFieldType = ((Integer) mFields.get(lField)).intValue();
			if (lFieldType == TBLOB) { // Il campo BLOB va alla fine
				lBlobName = lField;
				mBlobMode = true;
			} else {
				// Aggiungi allo statement solo i campi valorizzati con un valore oppure impostati a null
				if ((mFieldsValues.get(lField) != null) || (mFieldsNullValues.get(lField) != null))
					lStatSQL += lField + "=?,";
			}

			// lField = (String) lEnum.nextElement();
		}
		lStatSQL = lStatSQL.substring(0, lStatSQL.length() - 1);

		if (mStrConditions != null)
			lStatSQL += " WHERE " + mStrConditions;

		// log("SQL: " + lStatSQL);
		// lEnum = mFieldsValues.keys();
		lEnumFields = mFields.keys();

		try {
			mPs = mCon.prepareStatement(lStatSQL);
			// log("statement preparato...");

			while (lEnumFields.hasMoreElements()) {
				lField = (String) lEnumFields.nextElement();
				lFieldType = ((Integer) mFields.get(lField)).intValue();
				lValue = mFieldsValues.get(lField);

				if (mFieldsNullValues.get(lField) != null)
					lNullValue = true;
				else
					lNullValue = false;

				// se il campo non è stato valorizzato non fare l'update del campo ma passa al successivo
				if ((lValue == null) && (lNullValue == false))
					continue;

				lFieldsCount++;

				lLog += "(" + lField + "," + mFieldsValues.get(lField) + ")";
				// log("campo: " + lField);
				// log("lFieldType: " + lFieldType);
				// log("lValue: " + mFieldsValues.get(lField));
				// log("lNullValue: " + lNullValue);

				switch (lFieldType) {
				case STRING:
					if (lValue != null)
						mPs.setString(lFieldsCount, (String) lValue);
					else
						mPs.setString(lFieldsCount, null);
					break;

				case INT:
					mPs.setInt(lFieldsCount, ((Integer) lValue).intValue());
					break;

				case INTEGER:
					mPs.setInt(lFieldsCount, ((Integer) lValue).intValue());
					break;

				case DATE:
					if (lValue != null)
						mPs.setTimestamp(lFieldsCount,
								new java.sql.Timestamp(((java.util.Date) lValue).getTime()));
					else
						mPs.setTimestamp(lFieldsCount, null);
					break;

				case BIG_DECIMAL:
					if (lValue != null)
						mPs.setBigDecimal(lFieldsCount, (BigDecimal) lValue);
					else
						mPs.setBigDecimal(lFieldsCount, null);
					break;

				case TBLOB:
					mBlobMode = true;
					lBlobName = lField;
					lBlobValue = (ByteArrayInputStream) lValue;
					lFieldsCount--;
					break;

				case CLOB:
					// if (lValue != null)
					mPs.setClob(lFieldsCount, (Clob) lValue);
					// else
					// mPs.setClob(lFieldsCount, null);
					break;

				default:
					throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");
				}
			}
			setStatement(lStatSQL);
			super.update();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info(lLog);

			if (mBlobMode && (lBlobValue != null)) // Se c'e' da inserire un BLOB
			{
				if (mStrConditions != null) // Stessa Where che per l'update
					setCondition(mStrConditions);

				setForUpdate();
				start();
				Blob lBlob = null;
				while (this.next())
					lBlob = super.mRs.getBlob(lBlobName);

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>>>>>>>lBlob Length Before : " + lBlob.length());

				// 2009-09-01 - Modifica necessaria per ridimensionare il blob presente nel
				// dbase con il nuovo dato da inserire.
				// La ridimensione avviene se il dato presente sul dbase è maggiore rispetto
				// a quello nuovo.
				if (lBlob.length() > lBlobValue.available())
					((oracle.sql.BLOB) lBlob).trim(lBlobValue.available());

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(">>>>>>>>>lBlob Length After : " + lBlob.length());

				OutputStream lStream = ((oracle.sql.BLOB) lBlob).getBinaryOutputStream();
				int lSize = lBlobValue.available();
				byte[] lBuffer = new byte[lSize];
				// int lLength = -1;
				// Setto il BLOB sul DB

				while (/* lLength = */lBlobValue.read(lBuffer) != -1)
					lStream.write(lBuffer);

				lStream.flush();
				lStream.close();
			}

		} catch (SQLException sex) {
			stop();
			throw new DAOException(sex);
		} catch (IOException ioex) {
			stop();
			ioex.printStackTrace();
			throw new DAOException("IOException");
		} catch (Exception ex) {
			stop();
			ex.printStackTrace();
			throw new DAOException("Exception");
		}
	}

	/**
	 * Effettua la delete di un record.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void delete() throws DAOException {
		String lStatSQL = "";
		lStatSQL = "DELETE FROM " + mTableName;

		try {
			if (mStrConditions != null)
				lStatSQL += " WHERE " + mStrConditions;

			// log("SQL: " + lStatSQL);
			setStatement(lStatSQL);
			super.delete();
		} catch (SQLException sex) {
			stop();
			throw new DAOException(sex);
		}
	}

	/**
	 * Imposta le condizioni di ricerca per chiave.
	 * <p>
	 *
	 * @throws DAOException
	 *             propaga l'errore di eccezione.
	 */
	public void selByKey() throws DAOException {
		String lStr = "";
		String lFieldName = "";
		String lStrValue = "";
		Object lValue = null;
		Object lType = null;
		Enumeration lEnum = null;

		if (mFieldsKey.size() == 0)
			throw new DAOException("Non è stato definito alcun campo chiave");

		int count = 0;
		lEnum = mFieldsKey.keys();

		while (lEnum.hasMoreElements()) {
			count++;
			lFieldName = (String) lEnum.nextElement();
			lType = mFieldsKey.get(lFieldName);
			lValue = mFieldsValues.get(lFieldName);

			// log("TableDAO.selPerChiave lFieldName: " + lFieldName + " lType: " + lType + " lValue: " +
			// lValue.toString());

			if (lValue == null)
				throw new DAOException("Non è stato valorizzato uno dei mCampi chiave");
			else {
				switch (((Integer) lType).intValue()) {
				case STRING:
					lStrValue = "'" + lValue + "'";
					break;

				case INT:
					lStrValue = "" + lValue;
					break;

				case INTEGER:
					lStrValue = "" + lValue;
					break;

				case BIG_DECIMAL:
					lStrValue = "" + lValue;
					break;

				default:
					throw new DAOException("Il tipo di dato del campo chiave non è ancora gestito.");

				}
				lStr += "(" + lFieldName + "=" + lStrValue + ")";

				if (count < mFieldsKey.size())
					lStr += " AND ";
			}
		}
		// lStr=lStr.substring(0,lStr.length()-1);

		setCondition(lStr);
	}

}