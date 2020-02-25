package siap.sico.security;

import siap.SIAPException;

public class SecurityException extends SIAPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3908284053055335849L;

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 */
	public SecurityException(int aErrorCode) {
		mErrorCode = aErrorCode;
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public SecurityException(String aMessage) {
		super(aMessage);
		mErrorCode = SYSTEM_ERROR;
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
	public SecurityException(int aErrorCode, String aMessage) {
		super(aMessage);
		mErrorCode = aErrorCode;
	}

}