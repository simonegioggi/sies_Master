package f3b.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * The Class IECompatibilityFilter.
 */
public class IECompatibilityFilter implements Filter {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/** The Constant headerName. */
	private static final String headerName = "X-UA-Compatible";

	/**
	 * The Constant headerValue. X-UA-Compatible IE=5 Quirks Mode IE=7 IE7 mode IE=8 IE8 mode IE=9 IE9 mode IE=10 IE10
	 * mode IE=11 IE11 mode IE=edge The highest supported document mode of the browser IE=EmulateIE7 IE7 mode (if a
	 * valid <!DOCTYPE> declaration is present) Quirks Mode (otherwise) IE=EmulateIE8 IE8 mode (if a valid <!DOCTYPE>
	 * declaration is present) Quirks Mode (otherwise) IE=EmulateIE9 IE9 mode (if a valid <!DOCTYPE> declaration is
	 * present) Quirks Mode (otherwise) IE=EmulateIE10 IE10 mode (if a valid <!DOCTYPE> declaration is present) Quirks
	 * Mode (otherwise) IE=EmulateIE11 IE11 mode (if a valid <!DOCTYPE> declaration is present) Quirks Mode (otherwise)
	 * */
	private static final String headerDefaulValue = "IE=5";
	private String headerValue = "IE=5";

	private boolean enabled = true;

	/**
	 * Destroy.
	 */
	@Override
	public void destroy() {
	}

	/**
	 * Do filter.
	 *
	 * @param servletRequest
	 *            the servlet request
	 * @param servletResponse
	 *            the servlet response
	 * @param filterChain
	 *            the filter chain
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServletException
	 *             the servlet exception
	 */
	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {

//		HttpServletRequest req = (HttpServletRequest) servletRequest;
//		HttpServletRequestWrapper request = new HttpServletRequestWrapper(req);
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//		siesLogger.debug("#################IECompatibilityFilter DOFILTER");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//		siesLogger.debug("CURRENT REQUEST");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//		siesLogger.debug("------------------------------------------------");
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//		siesLogger.debug("URL REQUESTED:       " + request.getRequestURL());
//		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//		siesLogger.debug("URL REFERER  :       " + request.getHeader("referer"));
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		// get the headers we placed in the request
		// based on those request headers, set some response headers

		httpServletResponse.setHeader(headerName, headerValue);

		filterChain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * Inits the.
	 *
	 * @param filterConfig
	 *            the filter config
	 * @throws ServletException
	 *             the servlet exception
	 */
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {

		String enabledInitParameter = filterConfig.getInitParameter("enabled");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("#################IECompatibilityFilter init enabled=" + enabledInitParameter);
		this.enabled = !"false".equals(enabledInitParameter);
		String levelInitParameter = filterConfig.getInitParameter("level");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("#################IECompatibilityFilter init level=" + levelInitParameter);
		if (levelInitParameter != null) {
			this.headerValue = levelInitParameter;
		} else {
			this.headerValue = headerDefaulValue;

		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(
				"#################IECompatibilityFilter init final level=" + headerValue + " enabled=" + enabled);
	}

}