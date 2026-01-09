package it.mig.sies.exception;

import it.mig.sies.util.ApplicationProperties;

/**
 * Exception sollevata nel caso in cui l'utente non ha il profilo necessario per gestire il trasferimento
 * 
 * @author Federico Paparoni
 */
public class ProfileException extends SiesWsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 844463153415717525L;

	public ProfileException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public ProfileException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ProfileException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ProfileException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public ProfileException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getResponseMessage() {
		String responseCode = "messaggio.errore.profilo";
		String responseMessage = ApplicationProperties.getIstance().getProperty(responseCode);
		return responseMessage;
	}

}