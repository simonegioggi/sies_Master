package f3b.security.model;

import java.math.BigDecimal;

import f3b.model.GenericModel;

public class UserModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4440691666142638417L;

	protected String mUserId;
	protected String mPwd;

	protected ProfileModel mUserProfile;

	// COSTRUTTORE DI DEFAULT
	/**
	 * Costruttore di classe.
	 */
	public UserModel() {
		this.mUserId = "";
		this.mPwd = "";

		this.mUserProfile = null;
	}

	// COSTRUTTORE MODEL
	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aUserId
	 *            Id dell'utente.
	 * @param aPassword
	 *            password.
	 */
	public UserModel(String aUserId, String aPassword) {
		this.mUserId = aUserId;
		this.mPwd = aPassword;

		this.mUserProfile = null;
	}

	// COSTRUTTORE DI COPIA
	/**
	 * Costruttore di copia.
	 * <p>
	 * 
	 * @param aModel
	 *            istanza della classe UserModel.
	 */
	public UserModel(UserModel aModel) {
		this.mUserId = aModel.mUserId;
		this.mPwd = aModel.mPwd;

		this.mUserProfile = aModel.mUserProfile;
	}

	//
	// METODI GET()
	//

	/**
	 * Ritorna l'id utente.
	 * <p>
	 * 
	 * @return l'id utente.
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * Ritorna la password.
	 * <p>
	 * 
	 * @return la password.
	 */
	public String getPwd() {
		return mPwd;
	}

	/**
	 * Ritorna il profilo utente.
	 * <p>
	 * 
	 * @return il proflio utente come istanza classe </code>UserProfile</code>.
	 */
	public ProfileModel getUserProfile() {
		return mUserProfile;
	}

	//
	// METODI SET()
	//

	/**
	 * Imposta lo User ID.
	 * <p>
	 * 
	 * @param aValue
	 *            user id.
	 */
	public void setUserId(String aValue) {
		mUserId = aValue;
	}

	/**
	 * Imposta la password.
	 * <p>
	 * 
	 * @param aValue
	 *            la password.
	 */
	public void setPwd(String aValue) {
		mPwd = aValue;
	}

	public void setUserProfile(ProfileModel aValue) {
		mUserProfile = aValue;
	}

	/**
	 * Verifica se l'utente è abilitato ad una determinata funzione.
	 * <p>
	 * return l'esito della verifica.
	 */
	public boolean canFunction(BigDecimal aFunctionKey) {
		if (mUserProfile == null)
			return false;

		return mUserProfile.existFunction(aFunctionKey);
	}

	/**
	 * Verifica se l'utente è abilitato ad almeno una funzione tra quelle di un determinato insieme.
	 * <p>
	 * return l'esito della verifica.
	 */
	public boolean canAtLeastOneFunction(BigDecimal[] aFunctionKeys) {
		for (int i = 0; i < aFunctionKeys.length; i++) {
			if (canFunction(aFunctionKeys[i]))
				return true;
		}

		return false;
	}

	/**
	 * Ritorna il contenuto degli attributi di classe, formattai.
	 * <p>
	 * 
	 * @return gli attributi di classe.
	 */
	public String toString() {
		String lToString = this.mUserId + " - " + this.mPwd;

		return lToString;
	}

}