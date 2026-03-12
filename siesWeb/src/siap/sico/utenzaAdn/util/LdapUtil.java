package siap.sico.utenzaAdn.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;

/**
 * MEV INTEGRAZIONE SIES ADN
 *
 * @author sgioggi
 *
 */
public class LdapUtil {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private static String ATTRIBUTE_FOR_USER = "sAMAccountName";

	public static boolean authenticate(String username, String password) {
		try {
			Attributes att = authenticateUser(username, password);
			if (att == null) {
				siesLogger.info("Login non effettuato");
				siesLogger.info("Verificare le credenzali");
				// return true;
				return false;
			} else {
				siesLogger.info("Login effettuato");
				try {
					if (att.get("givenName") != null) {
						String s = att.get("givenName").get().toString();
						siesLogger.info("GIVEN NAME:" + s);
					}
					if (att.get("description") != null) {
						String s = att.get("description").get().toString();
						siesLogger.info("description:" + s);
					}
				} catch (NamingException e) {
					e.printStackTrace();
					siesLogger.error("ERRORE Naming", e);
					return false;
				}
				return true;
			}
		} catch (Throwable th) {
			siesLogger.error("errore durante autenticazione LDAP", th);
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	private static Attributes authenticateUser(String username, String password) throws F3BException {

		List<String> returnedAttsList = new ArrayList<>();

		String urlLDAP = F3BProperties.getProperty("ldap.provider.url");
		String domain = F3BProperties.getProperty("ldap.domain");
		String dn = F3BProperties.getProperty("ldap.dn");

		Hashtable<String, String> environment = new Hashtable<>();
		siesLogger.info("Preparo parametri connessione al server ldap " + urlLDAP);
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.SECURITY_PROTOCOL, "ssl");
		environment.put(Context.PROVIDER_URL, urlLDAP);
		environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		environment.put(Context.SECURITY_PRINCIPAL, username + "@" + domain);
		environment.put(Context.SECURITY_CREDENTIALS, password);

		environment.put("java.naming.ldap.factory.socket", "siap.sico.utenzaAdn.util.LdapSSLSocketFactory");

		LdapContext ctxGC = null;
		try {
			ctxGC = new InitialLdapContext(environment, null);
			siesLogger.info("Connessione effettuata " + ctxGC);
			// See which server was used
			siesLogger.info("server used: " + ctxGC.getEnvironment().get(Context.PROVIDER_URL));
			String returnedAtts[] = returnedAttsList.toArray(new String[returnedAttsList.size()]);
			String searchFilter = "(&(objectClass=user)(" + ATTRIBUTE_FOR_USER + "=" + username + "))";

			SearchControls searchCtls = new SearchControls();
			searchCtls.setReturningAttributes(returnedAtts);

			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String searchBase = dn;

			NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
			siesLogger.info("risposta ricerca " + answer);
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				siesLogger.info("risultato ricerca " + sr);
				Attributes attrs = sr.getAttributes();
				siesLogger.info("attributi ricerca " + attrs);
				if (attrs != null) {
					return attrs;
				}
			}
		} catch (NamingException e) {
			siesLogger.error("Just reporting error", e);
		} finally {
			if (ctxGC != null) {
				try {
					ctxGC.close();
				} catch (Throwable th) {
					siesLogger.error("errore durante close LdapContext", th);
				}
			}
		}
		return null;
	}

}