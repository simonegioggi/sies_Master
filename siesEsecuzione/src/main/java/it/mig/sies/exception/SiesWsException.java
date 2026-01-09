package it.mig.sies.exception;

/**
 * MEV 23010 - Exception generica per il ws di trasferimento
 * 
 * @author Federico Paparoni
 */
public abstract class SiesWsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7842616224552043136L;

	public SiesWsException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public SiesWsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public SiesWsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public SiesWsException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public SiesWsException(String message, Throwable cause) {
		super(message, cause);
	}

	public abstract String getResponseMessage();

}