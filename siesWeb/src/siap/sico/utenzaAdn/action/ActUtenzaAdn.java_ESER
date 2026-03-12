package siap.sico.utenzaAdn.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utenzaAdn.util.LdapUtil;
import siap.sico.web.ActionSiap;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class ActUtenzaAdn extends ActionSiap implements ICostantiSecurity {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// recupero dell'utenza per la login
		String userId = StringUtils.convertSqlString(getRequestStringParameter(CAMPO_USER_ID));
		String password = getRequestStringParameter(CAMPO_PASSWORD);
		// controllo dell'utenza per la login
		if (!LdapUtil.authenticate(userId, password)) {
			// UTENZA KO
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Combinazione User/Password ADN non esistente!");
			return IWebConstants.PG_MESSAGE_ADN;
		} else {
			// UTENZA OK
			String username = StringUtils.capitalize(userId.substring(0, userId.indexOf("."))) + " "
					+ StringUtils.capitalize(userId.substring(userId.indexOf(".") + 1));
			setRequestAttribute("username", username);
			siesLogger.debug("NOME UTENTE = " + username);
			setRequestAttribute("usernameDB", userId);
			siesLogger.debug("NOME UTENTE DB = " + userId);
			return PG_LOGIN_SIES;
		}
	}

}