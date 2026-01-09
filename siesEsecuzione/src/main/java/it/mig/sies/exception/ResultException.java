package it.mig.sies.exception;

import it.mig.sies.util.ApplicationProperties;

/**
 * SIES FASE 2 - Exception sollevata nel caso in cui ci siano dei problemi nella gestione della risposta
 * inviata da NSC
 * 
 * @author Federico Paparoni
 */
public class ResultException extends SiesWsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3471456753948185623L;

	public ResultException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public ResultException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ResultException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ResultException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public ResultException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getResponseMessage() {
		String responseCode = "messaggio.errore.elaborazione";
		String responseMessage = ApplicationProperties.getIstance().getProperty(responseCode);
		return responseMessage;
	}

}