package siap.util;

import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * <p>
 * Title: SessionManager
 * </p>
 * <p>
 * Description: Servlet caricata in fase di startup per la gestione degli eventi Session
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class SessionManager extends HttpServlet implements HttpSessionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6671396418455382565L;

	public void sessionCreated(HttpSessionEvent se) {
		/*HttpSession session = */se.getSession();
	}

	public void sessionDestroyed(HttpSessionEvent se) {

		HttpSession session = se.getSession();
		ServletContext ctx = session.getServletContext();
		Hashtable lockTable = (Hashtable) ctx.getAttribute("lockTable");
		lockTable.remove(session.getId());
	}

}