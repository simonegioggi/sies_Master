package f3b.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for remote scripting servlets that use Microsoft's remote scripting (MSRS) or JavaScript remote
 * scripting (JSRS).
 *
 * Usage:
 * 
 * <pre>
 * public class RSExample extends RemoteScriptingServlet {
 * 	public static String getSubcategories(String catstr) throws Exception {
 * 		String retval = &quot;&quot;;
 * 		// ... implementation details
 * 		return retval;
 * 	}
 * }
 * </pre>
 *
 * @version 1.0 February 10, 2001
 * @author Erik Hatcher
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
abstract public class RemoteScriptingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1587770889204726273L;

	boolean debugOn = false;

	private final static int MSRS = 0;
	private final static int JSRS = 1;

	private int clientType;

	/**
	 * Generates the appropriate response to either an MSRS or JSRS method invocation.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		// Requests look like this:
		// MSRS: <servletname>?_method=method&_mtype=execute&pcount=1&p0=1
		// JSRS: <servletname>?C=callback&F=method&P0=[1]

		// debugOn = request.getParameter("debug") != null;
		debugOn = false;
		debug("-------");

		boolean error = false;
		String returnValue;
		String callbackName = "";

		// Everything is wrapped in a try/catch block, any exception will cause the ERROR return value
		// so that the client side can deal with it gracefully
		try {
			String method;
			int pcount = 0;

			callbackName = request.getParameter("C");
			if (callbackName != null) {
				// client is JSRS - it passes a "C" parameter
				clientType = JSRS;
				method = request.getParameter("F");

				// JSRS doesn't tell us how many parameters, so count them
				while (request.getParameter("P" + pcount) != null)
					pcount++;
			} else {
				clientType = MSRS;
				method = request.getParameter("_method");
				pcount = Integer.parseInt(request.getParameter("pcount"));
			}

			debug("clientType = " + clientType);
			debug("Method = " + method);
			debug("pcount = " + pcount);

			// build paramSpec array with all String class items
			// build params array with the p0, p1, ...., pN request values

			Class[] paramSpec = new Class[pcount];
			Class stringClass = Class.forName("java.lang.String");
			String[] params = new String[pcount];
			if (pcount > 0) {
				for (int i = 0; i < pcount; i++) {
					paramSpec[i] = stringClass;
					params[i] = request.getParameter(((clientType == MSRS) ? "p" : "P") + i);

					if (clientType == JSRS) {
						// JSRS sends parameters wrapped with brackets, strip them off
						params[i] = params[i].substring(1, params[i].length() - 1);
					}
					debug("p" + i + " = " + params[i]);
				}
			}
			// ---------------------------------------------------------------------
			// find and invoke the appropriate static method in the concrete class
			// ---------------------------------------------------------------------
			Class c = this.getClass();
			Method m = c.getMethod(method, paramSpec);
			returnValue = (String) m.invoke(null, (Object[]) params);
		} catch (Exception e) {
			// if the invoked method threw an exception, pull it out of the wrapper exception that "invoke"
			// throws
			// so that the client gets the real error message
			if (e instanceof InvocationTargetException) {
				e = (Exception) ((InvocationTargetException) e).getTargetException();
			}
			debug("Oops, exception: " + e);
			error = true;
			returnValue = e.toString();
			e.printStackTrace();
		}

		String outputString = "";
		if (clientType == MSRS) {
			// Build the appropriate MSRS response
			// Microsoft's Remote Scripting has three types: SIMPLE, EVAL_OBJECT, and ERROR.
			// Currently only SIMPLE and ERROR are supported.
			outputString = "<METHOD VERSION=\"1.0.8044\"><RETURN_VALUE TYPE=" + (error ? "ERROR" : "SIMPLE")
					+ ">" + encode(returnValue) + "</RETURN_VALUE></METHOD>";
		} else {
			// Build the appropriate JSRS response
			outputString = "<html><head></head><body onload=\"p=document.layers?parentLayer:window.parent;";
			if (error) {
				outputString += "p.jsrsError('" + callbackName + "','jsrsError: " + encode(returnValue)
						+ "');\">jsrsError: " + jsrsErrorEscape(returnValue);
			} else {
				outputString += "p.jsrsLoaded('" + callbackName
						+ "');\">jsrsPayload:<br><form name=\"jsrs_Form\">"
						+ "<textarea name=\"jsrs_Payload\">" + jsrsEscape(returnValue) + "</textarea></form>";
			}
			outputString += "</body></html>";
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(outputString);

		debug(outputString);
	}

	private void debug(String str) {
		if (debugOn) {
		}
	}

	public static String jsrsEscape(String str) {
		StringBuffer sb = new StringBuffer(str);

		// probably an easier way to do this, but this will do for now
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '/') {
				sb.replace(i, i + 1, "\\/");
				i += 2;
			}
		}

		return new String(sb);
	}

	public static String jsrsErrorEscape(String str) {
		StringBuffer sb = new StringBuffer(str);

		// probably an easier way to do this, but this will do for now
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '\'') {
				sb.replace(i, i + 1, "\\'");
				i += 2;
			}
			if (sb.charAt(i) == '\"') {
				sb.replace(i, i + 1, "\\\"");
				i += 2;
			}
		}

		return new String(sb);
	}

	public static String encode(String str) {
		// but URLEncoder.encode isn't enough... '+' should really be "%20"
		StringBuffer sb = new StringBuffer(URLEncoder.encode(str));

		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '+') {
				sb.replace(i, i + 1, "%20");
				i += 2;
			}
		}

		return new String(sb);
	}

	/*
	 * -------------------------------------------------------------------**
	 * -------------------------------------------------------------------
	 */
//	private void start_up() {
//	}

	/*
	 * MSRS return details:
	 * 
	 * String return: <METHOD VERSION="1.0.8044"><RETURN_VALUE TYPE=SIMPLE>%3CMessages%
	 * 3E%3CMsg7%20ID%3D%221%22%3EMsg7%20data%3C/Msg7%3E%3C/Messages%3E</RETURN_VALUE></METHOD>
	 * 
	 * Error return: <METHOD VERSION="1.0.8044"><RETURN_VALUE
	 * TYPE=ERROR>xyz%20%3A%20not%20a%20public%20function</RETURN_VALUE></METHOD>
	 */

	/*
	 * JSRS return details:
	 * 
	 * Successful JSRS return: <html><head></head><body
	 * onload="p=document.layers?parentLayer:window.parent;p.jsrsLoaded('jsrs1');">jsrsPayload:<br><form
	 * name="jsrs_Form"><textarea name="jsrs_Payload">string~TEST</textarea></form></body></html>
	 * 
	 * Error JSRS return: <html><head></head><body
	 * onload="p=document.layers?parentLayer:window.parent;p.jsrsError('jsrs1','error');">error</body></html>
	 */

}