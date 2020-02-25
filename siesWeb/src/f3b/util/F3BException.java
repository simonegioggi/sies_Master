package f3b.util;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * <p>
 * Title: F3BException
 * </p>
 * <p>
 * Description: Classe responsabile della gestione degli errori di eccezioni del framework. Questa classe
 * dovrà essere ereditata da eventuali classi del progetto, specializzata per gestire errori di eccezione
 * specifici del progetto.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class F3BException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 475684644626818545L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Costante che codifica un codice di errore, da inviare alla super classe.
	 */
	public static final int SYSTEM_ERROR = -1;

	/**
	 * Costante che codifica un codice di errore.
	 */
	public static final int EX_NOT_FOUND = 1;

	/**
	 * Costante che codifica un codice di errore.
	 */
	public static final int USER_MESSAGE = 2;

	/**
	 * Costante che codifica un codice di errore.
	 */
	public static final int EX_OPERATION_FAILED = 3;

	/**
	 * Costante che codifica un codice di errore. STUB 20030203 - Utile ?
	 */
	public static final int MAX_RECORDS_EXCEDED = 6;

	/**
	 * Costante che codifica un codice di errore.
	 */
	public static final int NULL_OBJECT_ERROR = 7;

	protected int mErrorCode;

	/**
	 * Costruttore di classe.
	 */
	public F3BException() {
		super();
		this.mErrorCode = SYSTEM_ERROR;
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 */
	public F3BException(int aErrorCode) {
		this.mErrorCode = aErrorCode;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn("Errore F3B " + aErrorCode);
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public F3BException(String aMessage) {
		super(aMessage);
		this.mErrorCode = SYSTEM_ERROR;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn(aMessage);
	}

	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public F3BException(int aErrorCode, String aMessage) {
		super(aMessage);
		this.mErrorCode = aErrorCode;
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.warn(aMessage);
	}

	/**
	 * Costruttore con parametro <code>F3BException</code>.
	 * <p>
	 * 
	 * @param aEx
	 *            oggetto della classe <code>F3BException</code>.
	 */
	public F3BException(F3BException aEx) {
		super(aEx.getMessage());
		this.mErrorCode = aEx.getErrorCode();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error(aEx.getMessage(), aEx);
	}

	/**
	 * Costruttore con parametro <code>Exception</code>.
	 * <p>
	 * 
	 * @param aEx
	 *            oggetto della classe <code>Exception</code>.
	 */
	public F3BException(Exception aEx) {
		super(aEx.getMessage());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error(aEx.getMessage(), aEx);
	}

	/**
	 * Ritorna il codice di Errore.
	 * <p>
	 * 
	 * @return codice di errore.
	 */
	public int getErrorCode() {
		return this.mErrorCode;
	}

}