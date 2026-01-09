package it.mig.sies.exception;

import it.mig.sies.util.ApplicationProperties;

import java.io.Serial;

/**
 * SIES FASE 2 - Exception sollevata nel caso in cui ci siano dei problemi di interconnessione con il
 * webservice disponibile su NSC/SIES
 * 
 * @author Federico Paparoni
 */
public class CommunicationException extends SiesWsException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 6432361697228507310L;

	public CommunicationException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public CommunicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public CommunicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public CommunicationException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getResponseMessage() {
		String responseCode = "messaggio.errore.comunicazione";
		String responseMessage = ApplicationProperties.getIstance().getProperty(responseCode);
		return responseMessage;
	}

}