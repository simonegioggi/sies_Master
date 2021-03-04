package siap.sico.utente.model;

import java.util.Date;

import f3b.security.model.UserModel;
import siap.sico.profilo.model.ProfiloModel;
import siap.sico.ufficio.model.UfficioModel;

public class UtenteModel extends UserModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 5532027880026954541L;

	private String mCognome;
	private String mNome;
	private String mTelefono;
	private String mFax;
	private String mEmail;
	private Date mDataFineValidita;
	private Date mDataOraConnessione;
	private String mCodOperatoreInserimento;
	private Date mDataInserimento;
	private String mCodOperatoreAggiornamento;
	private Date mDataAggiornamento;
	private Date mDataUltimaModifcaPwd;
	private String mIP;
	private String mUseridNSC;
	private String mPwdNSC;
	// MEV INTEGRAZIONE SIES ADN: aggiunta variabile
	private String mUserAdn;

	private UfficioModel mUfficioUtente;

	// COSTRUTTORE DI DEFAULT
	public UtenteModel() {
		super();

		mCognome = "";
		mNome = "";
		mTelefono = "";
		mFax = "";
		mEmail = "";
		mDataFineValidita = null;
		mDataOraConnessione = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mDataUltimaModifcaPwd = null;
		mUfficioUtente = null;
		mIP = null;
		mUseridNSC = "";
		mPwdNSC = "";
		mUserAdn = "";
	}

	public UtenteModel(String aUserId, String aPassword) {
		super(aUserId, aPassword);

		mCognome = "";
		mNome = "";
		mTelefono = "";
		mFax = "";
		mEmail = "";
		mDataFineValidita = null;
		mDataOraConnessione = null;
		mCodOperatoreInserimento = "";
		mDataInserimento = null;
		mCodOperatoreAggiornamento = "";
		mDataAggiornamento = null;
		mDataUltimaModifcaPwd = null;
		mUfficioUtente = null;
		mIP = "";
		mUseridNSC = "";
		mPwdNSC = "";
		mUserAdn = "";
	}

	// COSTRUTTORE DI COPIA
	public UtenteModel(UtenteModel aModel) {
		super(aModel);
		// super(aModel.getUserId(),aModel.getPwd());

		mCognome = aModel.mCognome;
		mNome = aModel.mNome;
		mTelefono = aModel.mTelefono;
		mFax = aModel.mFax;
		mEmail = aModel.mEmail;
		mDataFineValidita = aModel.mDataFineValidita;
		mDataOraConnessione = aModel.mDataOraConnessione;
		mCodOperatoreInserimento = aModel.mCodOperatoreInserimento;
		mDataInserimento = aModel.mDataInserimento;
		mCodOperatoreAggiornamento = aModel.mCodOperatoreAggiornamento;
		mDataAggiornamento = aModel.mDataAggiornamento;
		mDataUltimaModifcaPwd = aModel.mDataUltimaModifcaPwd;
		mUfficioUtente = aModel.mUfficioUtente;
		mIP = aModel.mIP;
		mUseridNSC = aModel.mUseridNSC;
		mPwdNSC = aModel.mPwdNSC;
		mUserAdn = aModel.mUserAdn;
	}

	// COSTRUTTORE MODEL
	public UtenteModel(String aUserId, String aPwd, String aCognome, String aNome, String aTelefono,
			String aFax, String aEmail, Date aDataFineValidita, Date aDataOraConnessione,
			String aCodOperatoreInserimento, Date aDataInserimento, String aCodOperatoreAggiornamento,
			Date aDataAggiornamento, Date aDataUltimaModifcaPwd, String mIP, String aUseridNSC,
			String aPwdNSC/* , String aUserAdn */) {
		super(aUserId, aPwd);

		mCognome = aCognome;
		mNome = aNome;
		mTelefono = aTelefono;
		mFax = aFax;
		mEmail = aEmail;
		mDataFineValidita = aDataFineValidita;
		mDataOraConnessione = aDataOraConnessione;
		mCodOperatoreInserimento = aCodOperatoreInserimento;
		mDataInserimento = aDataInserimento;
		mCodOperatoreAggiornamento = aCodOperatoreAggiornamento;
		mDataAggiornamento = aDataAggiornamento;
		mDataUltimaModifcaPwd = aDataUltimaModifcaPwd;
		mUfficioUtente = null;
		mIP = "";
		mUseridNSC = aUseridNSC;
		mPwdNSC = aPwdNSC;
		// mUserAdn = aUserAdn;
	}

	//
	// METODI GET()
	//

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	public String getTelefono() {
		return mTelefono;
	}

	public String getFax() {
		return mFax;
	}

	public String getEmail() {
		return mEmail;
	}

	public Date getDataFineValidita() {
		return mDataFineValidita;
	}

	public Date getDataOraConnessione() {
		return mDataOraConnessione;
	}

	public String getCodOperatoreInserimento() {
		return mCodOperatoreInserimento;
	}

	public Date getDataInserimento() {
		return mDataInserimento;
	}

	public String getCodOperatoreAggiornamento() {
		return mCodOperatoreAggiornamento;
	}

	public Date getDataAggiornamento() {
		return mDataAggiornamento;
	}

	public Date getDataUltimaModifcaPwd() {
		return mDataUltimaModifcaPwd;
	}

	public String getIP() {
		return mIP;
	}

	public UfficioModel getUfficioUtente() {
		return mUfficioUtente;
	}

	public String getUseridNSC() {
		return mUseridNSC;
	}

	public String getPwdNSC() {
		return mPwdNSC;
	}

	public String getUserAdn() {
		return mUserAdn;
	}

	//
	// METODI SET()
	//

	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

	public void setTelefono(String aValore) {
		mTelefono = aValore;
	}

	public void setFax(String aValore) {
		mFax = aValore;
	}

	public void setEmail(String aValore) {
		mEmail = aValore;
	}

	public void setDataFineValidita(Date aValore) {
		mDataFineValidita = aValore;
	}

	public void setDataOraConnessione(Date aValore) {
		mDataOraConnessione = aValore;
	}

	public void setCodOperatoreInserimento(String aValore) {
		mCodOperatoreInserimento = aValore;
	}

	public void setDataInserimento(Date aValore) {
		mDataInserimento = aValore;
	}

	public void setCodOperatoreAggiornamento(String aValore) {
		mCodOperatoreAggiornamento = aValore;
	}

	public void setDataAggiornamento(Date aValore) {
		mDataAggiornamento = aValore;
	}

	public void setDataUltimaModifcaPwd(Date aValore) {
		mDataUltimaModifcaPwd = aValore;
	}

	public void setIP(String aValore) {
		mIP = aValore;
	}

	public void setUfficioUtente(UfficioModel aValore) {
		mUfficioUtente = aValore;
	}

	public void setUseridNSC(String aValore) {
		mUseridNSC = aValore;
	}

	public void setPwdNSC(String aValore) {
		mPwdNSC = aValore;
	}

	public void setUserAdn(String aValore) {
		this.mUserAdn = aValore;
	}

	public String toString() {
		String lToString = super.toString() + " - " + mCognome + " - " + mNome + " - " + mTelefono + " - "
				+ mFax + " - " + mEmail + " - " + mDataFineValidita + " - " + mDataOraConnessione + " - "
				+ mCodOperatoreInserimento + " - " + mDataInserimento + " - " + mCodOperatoreAggiornamento
				+ " - " + mDataAggiornamento + " - " + mDataUltimaModifcaPwd + " - " + mIP + " - "
				+ mUseridNSC + " - " + mPwdNSC + " - " + mUserAdn;

		return lToString;
	}

	/**
	 * Il metodo verifica se il profilo dell'utente, corrisponde ad un amministratore di Sistema. Se la
	 * condizione è vera viene ritornato un <code>true</code>, altrimenti se l'oggetto
	 * <code>UserProfile</code> non è valorizzato o l'id profilo <code>getProfileId()</code> anch'esso non è
	 * valorizzato ed è diverso dal valore 99 assegnato alla costante ProfiloModel.COD_PROFILO_SYS_ADMIN,
	 * ritorna un <code>false</code>.
	 * <p>
	 *
	 * @return boolean ritorna lo stato logico.
	 */
	public boolean isSysAdmin() {
		if (super.getUserProfile() != null && super.getUserProfile().getProfileId() != null
				&& super.getUserProfile().getProfileId().compareTo(ProfiloModel.COD_PROFILO_SYS_ADMIN) == 0)
			return true;
		else
			return false;
	}

	public boolean isUtenteSIGE() {
		if (super.getUserProfile() != null && super.getUserProfile().getProfileId() != null
				&& super.getUserProfile().isSige())
			return true;
		else
			return false;
	}

}