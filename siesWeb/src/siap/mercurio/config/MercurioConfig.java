package siap.mercurio.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;

/**
 * MercurioConfig - Carica e centralizza i parametri di configurazione necessari per invocare i servizi
 * REST esposti da Mercurio (autenticazione, gestione documentale, credenziali/keystore, timeout).
 *
 * I parametri sono attesi nel file {@code sies.properties} (uno per ambiente: TEST / COLLAUDO / PROD),
 * con prefisso {@code mercurio.*}, ad es.:
 *
 * <pre>
 * mercurio.master.hostname=mercurio-test.giustizia.it
 * mercurio.contents.service.host=master-contents-service-host
 * mercurio.domain.code=PENALE
 * mercurio.application.code=PROTOCOLLO
 * mercurio.context.code=UFF01
 * mercurio.auth.username=appl\mercurio.sies
 * mercurio.auth.password=******
 * mercurio.auth.application.token=******
 * mercurio.auth.application.token.header=X-Application-Token
 * mercurio.keystore.path=/etc/sies/keystore/mercurio-client.p12
 * mercurio.keystore.password=******
 * mercurio.timeout.connect=5000
 * mercurio.timeout.read=30000
 * </pre>
 *
 * Gli endpoint e i path param (domainCode/applicationCode/contextCode) sono quelli descritti nel
 * documento "Mercurio_LineeGuidaSviluppo_v1.0.docx" (§8.1 Autenticazione, §8.3 Gestione Documentale).
 *
 * TODO: il nome esatto dell'header HTTP che veicola il "token di applicazione" citato in §8.1.1 Login
 * non è specificato in modo univoco nella documentazione consultata (l'esempio di payload riporta solo
 * application/context/domain, mentre il testo descrittivo cita anche username/password e token di
 * applicazione). Da confermare con il team Mercurio prima del collaudo; nel frattempo il nome header è
 * configurabile tramite {@code mercurio.auth.application.token.header} (default "X-Application-Token").
 *
 * @version 1.1
 */
public class MercurioConfig {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private static final String PROPERTIES_FILE = "mercurio.properties";

	private static MercurioConfig mInstance;

	private String mMasterHostname;
	private String mContentsServiceHost;
	private String mDomainCode;
	private String mApplicationCode;
	private String mContextCode;
	private String mAuthUsername;
	private String mAuthPassword;
	private String mApplicationToken;
	private String mApplicationTokenHeader;
	private String mKeystorePath;
	private String mKeystorePassword;
	private int mConnectTimeoutMs;
	private int mReadTimeoutMs;

	// Credenziali di firma (SignData) e attivazione dell'integrazione dai punti di upload documento
	// (es. ActUploadDocument), cfr. stima_integrazione_mercurio.md §2.4 "Politica di rollback".
	private String mSignUsername;
	private String mSignPassword;
	private String mSignPin;
	private String mSignReason;
	private boolean mIntegrationEnabled;
	private boolean mBlockOnIntegrationError;

	private MercurioConfig() {
		Properties lProps = new Properties();
		try (InputStream lIs = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
			if (lIs != null) {
				lProps.load(lIs);
			} else {
				siesLogger.warn("MercurioConfig: " + PROPERTIES_FILE + " non trovato nel classpath.");
			}
		} catch (IOException e) {
			siesLogger.error("MercurioConfig: errore caricamento " + PROPERTIES_FILE, e);
		}

		mMasterHostname = lProps.getProperty("mercurio.master.hostname");
		mContentsServiceHost = lProps.getProperty("mercurio.contents.service.host");
		mDomainCode = lProps.getProperty("mercurio.domain.code");
		mApplicationCode = lProps.getProperty("mercurio.application.code");
		mContextCode = lProps.getProperty("mercurio.context.code");
		mAuthUsername = lProps.getProperty("mercurio.auth.username");
		mAuthPassword = lProps.getProperty("mercurio.auth.password");
		mApplicationToken = lProps.getProperty("mercurio.auth.application.token");
		mApplicationTokenHeader = lProps.getProperty("mercurio.auth.application.token.header", "X-Application-Token");
		mKeystorePath = lProps.getProperty("mercurio.keystore.path");
		mKeystorePassword = lProps.getProperty("mercurio.keystore.password");
		mSignUsername = lProps.getProperty("mercurio.sign.username");
		mSignPassword = lProps.getProperty("mercurio.sign.password");
		mSignPin = lProps.getProperty("mercurio.sign.pin");
		mSignReason = lProps.getProperty("mercurio.sign.reason", "Firma digitale SIES");
		mIntegrationEnabled = Boolean.parseBoolean(lProps.getProperty("mercurio.integration.enabled", "true"));
		mBlockOnIntegrationError = Boolean
				.parseBoolean(lProps.getProperty("mercurio.integration.blockOnError", "false"));
		mConnectTimeoutMs = Integer.parseInt(lProps.getProperty("mercurio.timeout.connect", "5000"));
		mReadTimeoutMs = Integer.parseInt(lProps.getProperty("mercurio.timeout.read", "30000"));
	}

	public static synchronized MercurioConfig getInstance() {
		if (mInstance == null) {
			mInstance = new MercurioConfig();
		}
		return mInstance;
	}

	/**
	 * Base URL del servizio di autenticazione Mercurio (§8.1):
	 * {@code https://{mercurio-master-hostname}/mercurio-auth/api/v1}.
	 */
	public String getAuthBaseUrl() {
		return "https://" + mMasterHostname + "/mercurio-auth/api/v1";
	}

	/**
	 * Base URL del servizio di gestione documentale Mercurio (§8.3):
	 * {@code https://{mercurio-master-hostname}/{master-contents-service-host}/api/v1/documents}.
	 */
	public String getDocumentsBaseUrl() {
		return "https://" + mMasterHostname + "/" + mContentsServiceHost + "/api/v1/documents";
	}

	public String getDomainCode() {
		return mDomainCode;
	}

	public String getApplicationCode() {
		return mApplicationCode;
	}

	public String getContextCode() {
		return mContextCode;
	}

	public String getAuthUsername() {
		return mAuthUsername;
	}

	public String getAuthPassword() {
		return mAuthPassword;
	}

	public String getApplicationToken() {
		return mApplicationToken;
	}

	public String getApplicationTokenHeader() {
		return mApplicationTokenHeader;
	}

	public String getKeystorePath() {
		return mKeystorePath;
	}

	public String getKeystorePassword() {
		return mKeystorePassword;
	}

	public int getConnectTimeoutMs() {
		return mConnectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return mReadTimeoutMs;
	}

	public String getSignUsername() {
		return mSignUsername;
	}

	public String getSignPassword() {
		return mSignPassword;
	}

	public String getSignPin() {
		return mSignPin;
	}

	public String getSignReason() {
		return mSignReason;
	}

	/**
	 * Indica se i punti di upload documento (es. ActUploadDocument) devono invocare l'integrazione
	 * Mercurio (archiviazione + firma). Di default disattivata ({@code false}) finché l'integrazione non
	 * sia stata validata in ambiente di TEST, per non impattare il flusso di upload esistente.
	 */
	public boolean isIntegrationEnabled() {
		return mIntegrationEnabled;
	}

	/**
	 * Politica di rollback in caso di errore Mercurio (cfr. stima_integrazione_mercurio.md §2.4, ancora
	 * da confermare con il referente funzionale): se {@code true} un errore di integrazione blocca
	 * l'intero upload (fail-closed); se {@code false} (default) l'errore viene solo loggato e l'upload
	 * prosegue senza idDocMercurio (fail-open), per non introdurre una dipendenza bloccante da Mercurio
	 * finché la politica non sia stata formalmente decisa.
	 */
	public boolean isBlockOnIntegrationError() {
		return mBlockOnIntegrationError;
	}
}
