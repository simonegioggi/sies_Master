package siap.sius;

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
public class SIUSException extends SIAPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7921728797379866342L;

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 */
	public SIUSException(int aErrorCode) {
		this.mErrorCode = aErrorCode;
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public SIUSException(String aMessage) {
		super(aMessage);
		this.mErrorCode = SYSTEM_ERROR;
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
	public SIUSException(int aErrorCode, String aMessage) {
		super(aMessage);
		this.mErrorCode = aErrorCode;
	}

}