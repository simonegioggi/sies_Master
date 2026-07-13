package siap.mercurio.model;

/**
 * MercurioLoginResponse - Esito dell'autenticazione al servizio Mercurio (Mercurio_LineeGuidaSviluppo_v1.0.docx
 * §8.1.1 "Login" / §8.1.2 "Refresh token", che condividono lo stesso formato di risposta).
 *
 * Il campo {@code token} contiene il JWT da utilizzare come Bearer token nelle successive chiamate ai
 * servizi di amministrazione e gestione documentale (header {@code Authorization: Bearer <token>}).
 *
 * @version 1.0
 */
public class MercurioLoginResponse {

	// JWT da utilizzare come Bearer token nelle chiamate successive.
	private String mToken;

	private String mUsername;
	private String mNome;
	private String mCognome;
	private String mApplication;
	private boolean mAdmin;
	private String mTimeLastLogin;

	public String getToken() {
		return mToken;
	}

	public void setToken(String aToken) {
		mToken = aToken;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String aUsername) {
		mUsername = aUsername;
	}

	public String getNome() {
		return mNome;
	}

	public void setNome(String aNome) {
		mNome = aNome;
	}

	public String getCognome() {
		return mCognome;
	}

	public void setCognome(String aCognome) {
		mCognome = aCognome;
	}

	public String getApplication() {
		return mApplication;
	}

	public void setApplication(String aApplication) {
		mApplication = aApplication;
	}

	public boolean isAdmin() {
		return mAdmin;
	}

	public void setAdmin(boolean aAdmin) {
		mAdmin = aAdmin;
	}

	public String getTimeLastLogin() {
		return mTimeLastLogin;
	}

	public void setTimeLastLogin(String aTimeLastLogin) {
		mTimeLastLogin = aTimeLastLogin;
	}
}
