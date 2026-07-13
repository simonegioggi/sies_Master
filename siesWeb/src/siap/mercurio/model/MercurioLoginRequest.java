package siap.mercurio.model;

/**
 * MercurioLoginRequest - Payload della richiesta di autenticazione al servizio Mercurio
 * (Mercurio_LineeGuidaSviluppo_v1.0.docx §8.1.1 "Login", campi "application", "context", "domain").
 *
 * Le credenziali dell'utente ADN (username/password) e il token di applicazione non fanno parte del
 * body JSON: sono veicolati rispettivamente tramite HTTP Basic Authentication e tramite l'header
 * configurato in {@link siap.mercurio.config.MercurioConfig#getApplicationTokenHeader()} (vedi TODO in
 * {@code MercurioConfig} sull'ambiguità della documentazione a riguardo).
 *
 * @version 1.0
 */
public class MercurioLoginRequest {

	// Codice dell'applicazione Mercurio con cui si vuole operare (es. "PROTOCOLLO").
	private String mApplication;

	// Codice del contesto Mercurio (es. "UFF01").
	private String mContext;

	// Codice del dominio Mercurio (es. "PENALE").
	private String mDomain;

	public MercurioLoginRequest() {
	}

	public MercurioLoginRequest(String aApplication, String aContext, String aDomain) {
		mApplication = aApplication;
		mContext = aContext;
		mDomain = aDomain;
	}

	public String getApplication() {
		return mApplication;
	}

	public void setApplication(String aApplication) {
		mApplication = aApplication;
	}

	public String getContext() {
		return mContext;
	}

	public void setContext(String aContext) {
		mContext = aContext;
	}

	public String getDomain() {
		return mDomain;
	}

	public void setDomain(String aDomain) {
		mDomain = aDomain;
	}
}
