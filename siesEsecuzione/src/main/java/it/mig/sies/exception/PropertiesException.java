package it.mig.sies.exception;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: F3BException
 * </p>
 * <p>
 * Description: Classe responsabile della gestione degli errori di eccezioni del framework. Questa classe dovr� essere
 * ereditata da eventuali classi del progetto, specializzata per gestire errori di eccezione specifici del progetto.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class PropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1342579357869841293L;

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

	private static final Logger logger = Logger.getLogger(PropertiesException.class);

	/**
	 * Costruttore di classe.
	 */
	public PropertiesException() {

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
	public PropertiesException(int aErrorCode) {

		this.mErrorCode = aErrorCode;
		logger.warn("Errore F3B " + aErrorCode);
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public PropertiesException(String aMessage) {

		super(aMessage);
		this.mErrorCode = SYSTEM_ERROR;
		logger.warn(aMessage);
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
	public PropertiesException(int aErrorCode, String aMessage) {

		super(aMessage);
		this.mErrorCode = aErrorCode;
		logger.warn(aMessage);
	}

	/**
	 * Costruttore con parametro <code>F3BException</code>.
	 * <p>
	 * 
	 * @param aEx
	 *            oggetto della classe <code>F3BException</code>.
	 */
	public PropertiesException(PropertiesException aEx) {

		super(aEx.getMessage());
		this.mErrorCode = aEx.getErrorCode();
		logger.error(aEx.getMessage(), aEx);
	}

	/**
	 * Costruttore con parametro <code>Exception</code>.
	 * <p>
	 * 
	 * @param aEx
	 *            oggetto della classe <code>Exception</code>.
	 */
	public PropertiesException(Exception aEx) {

		super(aEx.getMessage());
		logger.error(aEx.getMessage(), aEx);
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