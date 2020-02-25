package siap.sico;

import siap.SIAPException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class SICOException extends SIAPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1814031464777486247L;

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 */
	public SICOException(int aErrorCode) {
		mErrorCode = aErrorCode;
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public SICOException(String aMessage) {
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
	public SICOException(int aErrorCode, String aMessage) {
		super(aMessage);
		mErrorCode = aErrorCode;
	}

}