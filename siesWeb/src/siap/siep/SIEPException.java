package siap.siep;

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
public class SIEPException extends SIAPException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6314380174382180456L;

	/**
	 * Costante che codifica l'eccezione di sentenza (inteso come anno/numero/ufficio emittente) già presente
	 * in archivio.
	 */
	public static final int SENTENZA_PRESENTE_NEL_SISTEMA = 100;

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aErrorCode
	 *            codice di errore.
	 */
	public SIEPException(int aErrorCode) {
		this.mErrorCode = aErrorCode;
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aMessage
	 *            messaggio di errore.
	 */
	public SIEPException(String aMessage) {
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
	public SIEPException(int aErrorCode, String aMessage) {
		super(aMessage);
		this.mErrorCode = aErrorCode;
	}

}