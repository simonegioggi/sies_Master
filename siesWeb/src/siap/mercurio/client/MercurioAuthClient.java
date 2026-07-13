package siap.mercurio.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import f3b.log.LogF3B;
import siap.mercurio.config.MercurioConfig;
import siap.mercurio.exception.MercurioIntegrationException;
import siap.mercurio.model.MercurioLoginRequest;
import siap.mercurio.model.MercurioLoginResponse;
import siap.mercurio.util.MercurioHttpUtil;

/**
 * MercurioAuthClient - Client per l'autenticazione sul sistema Mercurio (Mercurio_LineeGuidaSviluppo_v1.0.docx
 * §8.1 "Autenticazione").
 *
 * Espone:
 * <ul>
 * <li>{@link #login()} - invoca {@code POST /mercurio-auth/api/v1/login} (§8.1.1) usando le credenziali
 * ADN e il token di applicazione configurati in {@link MercurioConfig}, ottenendo un JWT.</li>
 * <li>{@link #refreshToken(String)} - invoca {@code PUT /mercurio-auth/api/v1/token/refresh} (§8.1.2)
 * per rinnovare un JWT ancora valido, senza dover ripetere il login completo.</li>
 * <li>{@link #getValidToken()} - restituisce un JWT valido, effettuando login se non ne è ancora
 * disponibile uno in cache. Non essendo esplicitata nella documentazione la durata di validità del
 * JWT, non viene implementata una scadenza automatica: il chiamante che riceve un errore di
 * autenticazione dai servizi documentali dovrebbe invocare {@link #invalidateToken()} e riprovare
 * (eventualmente con {@link #refreshToken(String)}).</li>
 * </ul>
 *
 * @version 1.0
 */
public class MercurioAuthClient {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private MercurioConfig mConfig;

	private String mCachedToken;

	public MercurioAuthClient() {
		mConfig = MercurioConfig.getInstance();
	}

	/**
	 * Restituisce un JWT valido: se non è già presente in cache effettua il login.
	 */
	public synchronized String getValidToken() throws MercurioIntegrationException {
		if (mCachedToken == null) {
			mCachedToken = login().getToken();
		}
		return mCachedToken;
	}

	/**
	 * Forza l'invalidazione del token in cache (da invocare quando un servizio documentale restituisce
	 * un errore di autenticazione/autorizzazione riconducibile a un token scaduto).
	 */
	public synchronized void invalidateToken() {
		mCachedToken = null;
	}

	/**
	 * Effettua il login su Mercurio (§8.1.1). Le credenziali ADN dell'utente tecnico sono inviate come
	 * HTTP Basic Authentication (preemptive) e il token di applicazione tramite l'header configurato in
	 * {@link MercurioConfig#getApplicationTokenHeader()}; il body della richiesta contiene i soli campi
	 * "application"/"context"/"domain" come da esempio di payload della documentazione.
	 */
	public synchronized MercurioLoginResponse login() throws MercurioIntegrationException {
		MercurioLoginRequest lRequest = new MercurioLoginRequest(mConfig.getApplicationCode(),
				mConfig.getContextCode(), mConfig.getDomainCode());

		HttpClient lClient = MercurioHttpUtil.newHttpClient(mConfig);
		MercurioHttpUtil.applyBasicAuth(lClient, mConfig.getAuthUsername(), mConfig.getAuthPassword());

		PostMethod lMethod = new PostMethod(mConfig.getAuthBaseUrl() + "/login");
		applyApplicationTokenHeader(lMethod);

		JSONObject lBody = new JSONObject();
		try {
			lBody.put("application", lRequest.getApplication());
			lBody.put("context", lRequest.getContext());
			lBody.put("domain", lRequest.getDomain());
		} catch (JSONException e) {
			throw new MercurioIntegrationException("Errore nella costruzione della richiesta di login Mercurio.", e);
		}
		MercurioHttpUtil.setJsonRequestBody(lMethod, lBody);

		siesLogger.debug("MercurioAuthClient.login(): invocazione " + mConfig.getAuthBaseUrl() + "/login application="
				+ lRequest.getApplication() + " context=" + lRequest.getContext() + " domain=" + lRequest.getDomain());

		String lResponseBody = MercurioHttpUtil.execute(lClient, lMethod);
		MercurioLoginResponse lResponse = parseLoginResponse(MercurioHttpUtil.parseAndCheckSummary(lResponseBody));
		mCachedToken = lResponse.getToken();
		return lResponse;
	}

	/**
	 * Rinnova un JWT ancora valido (§8.1.2), senza ripetere l'invio delle credenziali.
	 */
	public synchronized MercurioLoginResponse refreshToken(String aCurrentToken) throws MercurioIntegrationException {
		HttpClient lClient = MercurioHttpUtil.newHttpClient(mConfig);

		PutMethod lMethod = new PutMethod(mConfig.getAuthBaseUrl() + "/token/refresh");
		lMethod.setRequestHeader("Authorization", "Bearer " + aCurrentToken);

		siesLogger.debug("MercurioAuthClient.refreshToken(): invocazione " + mConfig.getAuthBaseUrl() + "/token/refresh");

		String lResponseBody = MercurioHttpUtil.execute(lClient, lMethod);
		MercurioLoginResponse lResponse = parseLoginResponse(MercurioHttpUtil.parseAndCheckSummary(lResponseBody));
		mCachedToken = lResponse.getToken();
		return lResponse;
	}

	private void applyApplicationTokenHeader(org.apache.commons.httpclient.HttpMethod aMethod) {
		if (mConfig.getApplicationToken() != null) {
			aMethod.setRequestHeader(mConfig.getApplicationTokenHeader(), mConfig.getApplicationToken());
		}
	}

	private MercurioLoginResponse parseLoginResponse(JSONObject aRoot) {
		MercurioLoginResponse lResponse = new MercurioLoginResponse();
		lResponse.setToken(aRoot.optString("token", null));
		lResponse.setTimeLastLogin(aRoot.optString("timeLastLogin", null));
		JSONObject lUser = aRoot.optJSONObject("user");
		if (lUser != null) {
			lResponse.setUsername(lUser.optString("username", null));
			lResponse.setNome(lUser.optString("nome", null));
			lResponse.setCognome(lUser.optString("cognome", null));
			lResponse.setApplication(lUser.optString("application", null));
			lResponse.setAdmin(lUser.optBoolean("admin", false));
		}
		return lResponse;
	}
}
