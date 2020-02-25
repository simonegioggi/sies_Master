package siap.jms.manage;

import siap.jms.connection.ConnectionPoolJMS;
import f3b.util.F3BException;
import f3b.web.Action;
import f3b.web.IWebConstants;

public class ActResetMessageQueueConnection extends Action {

	public String processRequest() throws F3BException {

		try {
			ConnectionPoolJMS.restart();
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new F3BException("Eccezione nel restart di message queue");
		}

		setRequestAttribute(IWebConstants.MESSAGE_TEXT,
				"Connessione al Message Queue effettuata con successo!");

		/*
		 * RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage( IWebConstants.PG_MAIN );
		 * lRedirigi.setAction( "siap.sico.security.action.ActLoadOrizontalMenu&IdFunzione=21020130" );
		 * setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
		 */

		return IWebConstants.PG_MESSAGE;
	}

}