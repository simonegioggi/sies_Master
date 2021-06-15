package siap.sico.saml.model;

/**
 * CodiceSedeUfficio 
 * CodiceTipoUfficio 
 * Utente (UserID) 
 * HostAddress (DNS o IP del Server) 
 * CognomeUtente
 * NomeUtente 
 * Distretto 
 * Sistema SIEP
 *
 * @author Giselda De Vita
 */
public class SamlModel {

	private String mCodSedeUfficio; /* Codice ISTAT del Comune 6 caratteri */

	private String mCodTipoUfficio; /* Tipo Ufficio */

	private String mIdUtente;
	private String mHostAddress; /* hostname:porta */

	private String mCognomeUtente;
	private String mNomeUtente;
	private String mDistretto;
	private String mSistema;
	// MEV INTEGRAZIONE SIES ADN: aggiunta variabile
	private String mUserAdn;

	public final static String COD_SEDE_UFFICIO = "CodSedeUfficio"; /* Codice ISTAT del Comune 6 caratteri */
	public final static String COD_TIPO_UFFICIO = "CodTipoUfficio"; /* Tipo Ufficio */
	public final static String ID_UTENTE = "IdUtente";
	public final static String HOST_ADDRESS = "HostAddress"; /* hostname:porta */
	public final static String COGNOME_UTENTE = "CognomeUtente";
	public final static String NOME_UTENTE = "NomeUtente";
	public final static String DISTRETTO = "Distretto";
	public final static String SISTEMA = "Sistema";
	public final static String USER_ADN = "IdUtenteADN";

	public SamlModel() {
	}

	public SamlModel(String mCodSedeUfficio, String mCodTipoUfficio, String mIdUtente, String mHostAddress,
			String mCognomeUtente, String mNomeUtente, String mDistretto, String mSistema, String mUserAdn) {

		this.mCodSedeUfficio = mCodSedeUfficio;
		this.mCodTipoUfficio = mCodTipoUfficio;
		this.mIdUtente = mIdUtente;
		this.mHostAddress = mHostAddress;
		this.mCognomeUtente = mCognomeUtente;
		this.mNomeUtente = mNomeUtente;
		this.mDistretto = mDistretto;
		this.mSistema = mSistema;
		this.mUserAdn = mUserAdn;
	}

	public String getCodSedeUfficio() {
		return mCodSedeUfficio;
	}

	public void setCodSedeUfficio(String mCodSedeUfficio) {
		this.mCodSedeUfficio = mCodSedeUfficio;
	}

	public String getCodTipoUfficio() {
		return mCodTipoUfficio;
	}

	public void setCodTipoUfficio(String mCodTipoUfficio) {
		this.mCodTipoUfficio = mCodTipoUfficio;
	}

	public String getCognomeUtente() {
		return mCognomeUtente;
	}

	public void setCognomeUtente(String mCognomeUtente) {
		this.mCognomeUtente = mCognomeUtente;
	}

	public String getDistretto() {
		return mDistretto;
	}

	public void setMDistretto(String mDistretto) {
		this.mDistretto = mDistretto;
	}

	public String getHostAddress() {
		return mHostAddress;
	}

	public void setHostAddress(String mHostAddress) {
		this.mHostAddress = mHostAddress;
	}

	public String getIdUtente() {
		return mIdUtente;
	}

	public void setIdUtente(String mIdUtente) {
		this.mIdUtente = mIdUtente;
	}

	public String getNomeUtente() {
		return mNomeUtente;
	}

	public void setNomeUtente(String mNomeUtente) {
		this.mNomeUtente = mNomeUtente;
	}

	public String getSistema() {
		return mSistema;
	}

	public void setSistema(String mSistema) {
		this.mSistema = mSistema;
	}

	public String getUserAdn() {
		return mUserAdn;
	}

	public void setUserAdn(String mUserAdn) {
		this.mUserAdn = mUserAdn;
	}

}