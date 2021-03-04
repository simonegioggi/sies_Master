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

	// static String ATTRIBUTE_FOR_USER = "sAMAccountName";
	//
	// private String hostLDAPServer;
	// private String portLDAPServer;
	// private String domain;
	// private String dn;
	// private String adminPassword;
	// private String adminUser;
	// private static List<String> returnedAttsList = new ArrayList<>();

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// public boolean authenticate(String username, String password) {
	//
	// Attributes att = authenticateUser(username, password);
	// if (att == null) {
	// siesLogger.info("Login non effettuato");
	// siesLogger.info("Verificare le credenzali");
	// return false;
	// } else {
	// siesLogger.info("Login effettuato");
	// try {
	// if (att.get("givenName") != null) {
	// String s = att.get("givenName").get().toString();
	// siesLogger.info("GIVEN NAME:" + s);
	// }
	// if (att.get("description") != null) {
	// String s = att.get("description").get().toString();
	// siesLogger.info("description:" + s);
	// }
	// } catch (NamingException e) {
	// e.printStackTrace();
	// siesLogger.info("ERRORE in ActUtenzaAdn.authenticate!");
	// return false;
	// }
	// return true;
	// }
	// }

	// public Attributes authenticateUser(String username, String password) {
	//
	// // ******* DA USARE SOLO PER CHIAMATE LDAPS*************
	//
	// // URL url = this.getClass().getResource("/ldap.jks");
	// // impostazione per i certificati
	// // System.setProperty("javax.net.ssl.trustStore", url.getFile());
	// // System.setProperty("javax.net.ssl.trustStorePassword","123456");
	//
	// // ******* DA USARE SOLO PER CHIAMATE LDAPS*************
	//
	// Hashtable<String, String> environment = new Hashtable<>();
	// siesLogger.info("Preparo parametri connessione al server ldap " + hostLDAPServer + " su porta "
	// + portLDAPServer);
	// environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	//
	// // ******* DA USARE SOLO PER CHIAMATE LDAPS*************
	// // environment.put(Context.SECURITY_PROTOCOL,"ssl");
	// // ******* DA USARE SOLO PER CHIAMATE LDAPS*************
	//
	// environment.put(Context.PROVIDER_URL, "ldap://" + hostLDAPServer + ":" + portLDAPServer);
	// environment.put(Context.SECURITY_AUTHENTICATION, "simple");
	// environment.put(Context.SECURITY_PRINCIPAL, username + "@" + domain);
	// environment.put(Context.SECURITY_CREDENTIALS, password);
	//
	// LdapContext ctxGC = null;
	// try {
	// ctxGC = new InitialLdapContext(environment, null);
	// siesLogger.info("Connessione effettuata " + ctxGC);
	// String returnedAtts[] = returnedAttsList.toArray(new String[returnedAttsList.size()]);
	// String searchFilter = "(&(objectClass=user)(" + ATTRIBUTE_FOR_USER + "=" + username + "))";
	//
	// SearchControls searchCtls = new SearchControls();
	// searchCtls.setReturningAttributes(returnedAtts);
	//
	// searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	// String searchBase = dn;
	//
	// NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
	// siesLogger.info("risposta ricerca " + answer);
	// while (answer.hasMoreElements()) {
	// SearchResult sr = (SearchResult) answer.next();
	// siesLogger.info("risultato ricerca " + sr);
	// Attributes attrs = sr.getAttributes();
	// siesLogger.info("attributi ricerca " + attrs);
	// if (attrs != null) {
	// return attrs;
	// }
	// }
	// } catch (NamingException e) {
	// siesLogger.error("ERRORE in ActUtenzaAdn.authenticateUser!", e);
	// }
	// return null;
	// }

	// public static void main(String[] args) {
	//
	// // prepara il model dell'utente per la login
	// String userId = "emma.caporizzo";
	// String password = "Federica21!!";
	// if (!LdapUtil.authenticate(userId, password))
	// System.out.println("Combinazione User/Password ADN non esistente!");
	// else
	// System.out.println("Combinazione User/Password ADN esistente!");
	// }

	public String processRequest() throws F3BException {

		// VALIDO per PSM
		// configurazione autorita' da file di properties
		// setHostLDAPServer(F3BProperties.getProperty("auth.setHostLDAPServer"));
		// setPortLDAPServer(F3BProperties.getProperty("auth.setPortLDAPServer"));
		// setDomain(F3BProperties.getProperty("auth.setDomain"));
		// setDn(F3BProperties.getProperty("auth.setDn"));
		// setAdminUser(F3BProperties.getProperty("auth.setAdminUser"));
		// setAdminPassword(F3BProperties.getProperty("auth.setAdminPassword"));

		// prepara il model dell'utente per la login
		String userId = StringUtils.convertSqlString(getRequestStringParameter(CAMPO_USER_ID));
		String password = getRequestStringParameter(CAMPO_PASSWORD);
		if (!LdapUtil.authenticate(userId, password)) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Combinazione User/Password ADN non esistente!");
			return IWebConstants.PG_MESSAGE_ADN;
		} else {
			// per recuperare gli attributi utente
			// List<String> utenti = new ArrayList<>();
			// utenti.add(userId);
			// HashMap<String, String> ret = getAttributeAboutUser(utenti, "samAccountName");
			// if (!ret.isEmpty()) {
			// for (String user : ret.keySet())
			// siesLogger.info(user + " " + ret.get(user));
			/* String username = ret.get(userId) */;
			String username = StringUtils.capitalize(userId.substring(0, userId.indexOf("."))) + " "
					+ StringUtils.capitalize(userId.substring(userId.indexOf(".") + 1));
			setRequestAttribute("username", username);
			siesLogger.debug(username);
			setRequestAttribute("usernameDB", /* ret.get(userId) */userId);
			siesLogger.debug(userId);
			// }
			return PG_LOGIN_SIES;
		}
	}

	// public HashMap<String, String> getAttributeAboutUser(List<String> utenti, String attribute) {
	//
	// Hashtable<String, String> environment = new Hashtable<>();
	// HashMap<String, String> returnMap = new HashMap<>();
	// environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	//
	// // ******* DA USARE SOLO PER CHIAMATE LDAPS*************
	// // environment.put(Context.SECURITY_PROTOCOL,"ssl");
	//
	// siesLogger.info("Connessione al server ldap " + hostLDAPServer + " su porta " + portLDAPServer);
	// environment.put(Context.PROVIDER_URL, "ldap://" + hostLDAPServer + ":" + portLDAPServer);
	// environment.put(Context.SECURITY_AUTHENTICATION, "simple");
	//
	// environment.put(Context.SECURITY_PRINCIPAL, adminUser);
	// environment.put(Context.SECURITY_CREDENTIALS, adminPassword);
	//
	// LdapContext ctxGC = null;
	// try {
	// ctxGC = new InitialLdapContext(environment, null);
	//
	// String returnedAtts[] = new String[] { ATTRIBUTE_FOR_USER, attribute };
	//
	// for (Iterator iterator = utenti.iterator(); iterator.hasNext();) {
	// String utente = (String) iterator.next();
	//
	// String searchFilter = "(&(objectClass=user)(" + ATTRIBUTE_FOR_USER + "=" + utente + "))";
	//
	// SearchControls searchCtls = new SearchControls();
	// searchCtls.setReturningAttributes(returnedAtts);
	//
	// searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	// String searchBase = dn;
	//
	// NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchCtls);
	// while (answer.hasMoreElements()) {
	// SearchResult sr = (SearchResult) answer.next();
	// Attributes attrs = sr.getAttributes();
	// if (attrs != null) {
	// if (attrs.get(attribute) != null)
	// returnMap.put(attrs.get(ATTRIBUTE_FOR_USER).get().toString(),
	// attrs.get(attribute).get().toString());
	// else
	// returnMap.put(attrs.get(ATTRIBUTE_FOR_USER).get().toString(), "");
	// }
	// }
	// }
	// } catch (NamingException e) {
	// siesLogger.error("ERRORE in ActUtenzaAdn.getAttributeAboutUser!");
	// e.printStackTrace();
	// }
	// return returnMap;
	// }

	// public String getHostLDAPServer() {
	// return hostLDAPServer;
	// }
	//
	// public void setHostLDAPServer(String hostLDAPServer) {
	// this.hostLDAPServer = hostLDAPServer;
	// }
	//
	// public String getPortLDAPServer() {
	// return portLDAPServer;
	// }
	//
	// public void setPortLDAPServer(String portLDAPServer) {
	// this.portLDAPServer = portLDAPServer;
	// }
	//
	// public String getDomain() {
	// return domain;
	// }
	//
	// public void setDomain(String domain) {
	// this.domain = domain;
	// }
	//
	// public String getDn() {
	// return dn;
	// }
	//
	// public void setDn(String dn) {
	// this.dn = dn;
	// }
	//
	// public List<String> getReturnedAttsList() {
	// return returnedAttsList;
	// }
	//
	// public void setReturnedAttsList(List<String> returnedAttsList) {
	// ActUtenzaAdn.returnedAttsList = returnedAttsList;
	// }
	//
	// public String getAdminPassword() {
	// return adminPassword;
	// }
	//
	// public void setAdminPassword(String adminPassword) {
	// this.adminPassword = adminPassword;
	// }
	//
	// public String getAdminUser() {
	// return adminUser;
	// }
	//
	// public void setAdminUser(String adminUser) {
	// this.adminUser = adminUser;
	// }

}