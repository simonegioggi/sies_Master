package f3b.security;

import f3b.util.F3BException;

public class SecurityException extends F3BException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4609051608377672672L;

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