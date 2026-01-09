package it.mig.sies.exception;

import it.mig.sies.util.ApplicationProperties;

import java.io.Serial;

/**
 * SIES FASE 2 - Exception sollevata nel caso in cui ci siano dei problemi nel caricamento delle informazioni
 * dal database locale di SIES
 * 
 * @author Federico Paparoni
 */
public class LoadException extends SiesWsException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -2539933537729075460L;

	public LoadException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public LoadException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public LoadException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public LoadException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public LoadException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getResponseMessage() {
		String responseCode = "messaggio.errore.caricamento";
		String responseMessage = ApplicationProperties.getIstance().getProperty(responseCode);
		return responseMessage;
	}

}