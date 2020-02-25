package siap.jms.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siap.jms.SIAPReceiver;

/**
 * <p>
 * Title: ServletJMSWakeUp
 * </p>
 * <p>
 * Description: Servlet che richiama i listner di openJMS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
public class ServletJMSWakeUp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4920796913160252858L;

	/**
	 * Implements the HTTP GET method. The GET method is the standard browser method.
	 *
	 * @param request
	 *            the request object, containing data from the browser
	 * @param repsonse
	 *            the response object to send data to the browser
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SIAPReceiver.getInstance().checkTimeOut();
		} catch (Exception ex) {
		}

		PrintWriter out = response.getWriter();

		// Writes the string to the browser.
		out.println("Hello, world!");
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			SIAPReceiver.getInstance().checkTimeOut();
		} catch (Exception ex) {
		}

		PrintWriter out = response.getWriter();

		// Writes the string to the browser.
		out.println("Listner di OpenJMS ripartiti!");
		out.close();
	}

}