package f3b.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;

/**
 * Classe per gestire il timeout delle sessioni. I componenti che fanno polling di fatto resettano il
 * contatore per cui il la sessione non scade mai
 *
 * @author d.fiorletta
 *
 */
public class SessionTimeoutFilter implements Filter {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private long TIMEOUT = 1800L; // 30 minutes in seconds di default
	private final String LAST_ACTIVITY = "LAST_ACTIVITY";

	List<String> listPollingCall = null;

	/**
	 * Leggo i parametri di inizializzazione
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

		String timeoutInitParameter = filterConfig.getInitParameter("timeoutSecond");
		siesLogger.debug("SessionTimeoutFilter: timeoutInitParameter = " + timeoutInitParameter);
		TIMEOUT = new Long(timeoutInitParameter).longValue();

		// ==========================================================================
		// Inizializzo la lista delle action da escludere, quelle che fanno polling
		// ==========================================================================
		String[] polligCallActions = new String[] {
				"siap.siep.richiesta.action.ActLoadContaAttiCompetenzaRicevuti" };

		listPollingCall = Arrays.asList(polligCallActions);
	}

	@Override
	public void destroy() {
		siesLogger.debug("SessionTimeoutFilter: destroy...");
	}

	/**
	 *
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// siesLogger.debug("SessionTimeoutFilter: doFilter...");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest theRequest = (HttpServletRequest) request;
			Long now = (new Date()).getTime();

			HttpSession session = theRequest.getSession(false);
			if (session != null) {
				Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY);

				if (lastActivity == null) {
					// The first call for this session.
					lastActivity = now;
				}

				// Long userInactivity = (now - lastActivity) / 1000;
				// siesLogger.debug("SessionTimeoutFilter: userInactivity = "+userInactivity+" sec");

				// ================================================================
				// Questa versione NON resetta in automatico la sessione sul polling
				// ma solo a seguito di una attività utente
				// ================================================================
				if (isPollingCall(theRequest)) {
					// non faccio nulla è una chiamata da una funzione che fa polling
					// siesLogger.debug("SessionTimeoutFilter: pollingUrl NON resetto la sessione!");
				} else {
					// Chiamata da attività utente
					if (now - lastActivity > TIMEOUT * 1000L) {
						// Sessione scaduta la invalido
						// siesLogger.debug("SessionTimeoutFilter: sessione scaduta la invalido...");
						session.invalidate();
					} else {
						// Sessione valida, resetto inactivity timer
						// siesLogger.debug("SessionTimeoutFilter: resetto il timer...");
						session.setAttribute(LAST_ACTIVITY, now);
					}
				}

				// ================================================================
				// Questa versione invalida la sessione anche sul polling, quindi
				// la pagina del SIES viene chiusa automaticamente senza che l'utente
				// interagisca con la stessa. Infatti il filtro viene chiamato
				// prima della main.jsp
				// ================================================================
				// if (now - lastActivity > TIMEOUT*1000L) {
				// // User timed out.
				// siesLogger.debug("SessionTimeoutFilter: sessione scaduta la invalido...");
				// session.invalidate();
				// }
				// else if (!isPollingCall(theRequest)) {
				// // Reset inactivity timer
				// siesLogger.debug("SessionTimeoutFilter: resetto la sessione...");
				// session.setAttribute(LAST_ACTIVITY, now);
				// }
				// else {
				// siesLogger.debug("SessionTimeoutFilter: pollingUrl NON resetto la sessione!");
				// }
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @return true se la chimata è un polling, false negli altri casi
	 */
	private boolean isPollingCall(HttpServletRequest request) {

		// String uri = request.getRequestURI();
		// siesLogger.debug("SessionTimeoutFilter: isPollingCall, uri = "+uri);
		String lStrAction = request.getParameter(IWebConstants.ACTION_FIELD);

		// siesLogger.debug("SessionTimeoutFilter: isPollingCall, lStrAction = "+lStrAction);

		if (listPollingCall.contains(lStrAction)) {
			return true;
		}

		return false;
	}

}