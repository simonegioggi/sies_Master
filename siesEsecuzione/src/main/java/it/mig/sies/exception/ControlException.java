package it.mig.sies.exception;

public class ControlException extends SiesWsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6079916352574466710L;

	public ControlException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio
	 */
	public ControlException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ControlException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param cause
	 *            causa
	 */
	public ControlException(Exception exception) {
		super(exception);
	}

	/**
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public ControlException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getResponseMessage() {
		return this.getMessage();
	}

}